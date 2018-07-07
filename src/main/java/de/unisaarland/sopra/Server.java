package de.unisaarland.sopra;

import de.unisaarland.sopra.comm.CommException;
import de.unisaarland.sopra.comm.TimeoutException;
import de.unisaarland.sopra.messages.Command;
import de.unisaarland.sopra.messages.CommandFactoryImpl;
import de.unisaarland.sopra.messages.RegistrationAborted;
import de.unisaarland.sopra.model.*;
import de.unisaarland.sopra.utility.*;
import de.unisaarland.sopra.comm.ServerConnection;

import java.util.*;
import java.util.Map;

/**
 * Created by Team 14 on 12.09.2016.
 */
public class Server extends Master {

    private ServerConnection<Command> serverConnection;
    private CommIds commIds;

    /**
     * Creates a new instance of {@link Server} with given parameters.
     *
     * @param port      The port for the {@link ServerConnection}
     * @param timeout   The timeout for the {@link ServerConnection}
     * @param fightFile The read fightFile as {@link String}.
     * @param mapFile   The read mapFile as {@link String}.
     */
    public Server(int port, int timeout, String fightFile, String mapFile) throws InvalidFightFileException, InvalidMapFileException {
        super();
        commIds = new CommIds();
        CommandFactoryImpl cmdFactory = new CommandFactoryImpl(commIds);
        serverConnection = new ServerConnection<>(port, timeout, cmdFactory);

        try {
            game.handleFightfile(fightFile);
            game.handleMapfile(mapFile);
        } catch (InvalidFightFileException | InvalidMapFileException exception) {
            serverConnection.close();
            running = false;
            throw exception;
        }
    }

    /**
     * Starts the {@link Server}.
     */
    public void run() throws RegistrationAbortedException{
        registerState();
        spawnFairies();

        while (running) {
            int hpRoundBegin = hpSumOfCreatures();
            roundBeginState();
            spawnBoars();
            moveFairies();
            moveBoars();
            actState();
            fieldEffectState();
            poisonState();

            int hpRoundEnd = hpSumOfCreatures();
            roundEndState(hpRoundBegin - hpRoundEnd);
        }
        serverConnection.close();
    }

    public void close() {
        running = false;
        serverConnection.close();
    }

    private void registerState() throws RegistrationAbortedException{
        game.setRoundState(RoundState.REGISTER);
        boolean allRegistered = false;
        List<Player> players = game.getPlayers();

        while (!allRegistered) {
            Command cmd = null;
            try {
                cmd = serverConnection.nextCommand();
            } catch (TimeoutException e) {
                System.out.println("Not all players have registrated!");
            }

            if (cmd != null && cmd.validateCommand(game)) {
                cmd.executeCommand(game);
                cmd.sendResults(serverConnection);
            } else {
                for (int id : commIds.getCommLibIds()) {
                    serverConnection.sendRegistrationAborted(id);
                }

                serverConnection.close();
                running = false;

                throw new RegistrationAbortedException("Invalid Command!");
            }

            allRegistered = true;
            for (Player p : players) {
                if (!p.getRegistered()) {
                    allRegistered = false;
                    break;
                }
            }
        }
    }

    private void spawnFairies() {
        game.setRoundState(RoundState.SPAWNFAIRIES);
        List<GameVector> fairySpawns = game.getFairySpawns();
        int playerCount = game.getPlayers().size();

        for (GameVector spawn : fairySpawns) {
            int id = playerCount + game.getNpcs().size();
            Npc fairy = new Npc(id, spawn, 100, CreatureType.FAIRY, 4, 10);
            game.addNpc(fairy);

            for (int commId : commIds.getCommLibIds()) {
                serverConnection.sendFairySpawned(commId, id, spawn.getX(), spawn.getY());
            }
        }
    }

    private void roundBeginState() {
        game.setRoundState(RoundState.ROUNDBEGIN);
        game.increaseRoundCounter();
        calculateInitOrder();
        game.setCurrentMonsterIndex(0);

        super.resetCreatureStats(game);

        for (int commId : commIds.getCommLibIds()) {
            serverConnection.sendRoundBegin(commId, game.getRoundCounter());
        }
    }

    private void calculateInitOrder() {
        List<Pc> monsterOrder = new ArrayList<>();
        monsterOrder.addAll(game.getMonsters());

        Collections.sort(monsterOrder, (monster1, monster2) -> {
            if (monster1.getEnergy() > monster2.getEnergy()) {
                return -1;
            } else if (monster1.getEnergy() == monster2.getEnergy()) {
                if (monster1.getFairness() < monster2.getFairness()) {
                    return -1;
                } else if (monster1.getFairness() == monster2.getFairness()) {
                    if (monster1.getId() < monster2.getId()) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else {
                    return 1;
                }
            } else {
                return 1;
            }
        });

        List<Integer> initOrder = new ArrayList<>();

        for (Pc monster : monsterOrder) {
            initOrder.add(monster.getId());
        }

        game.setInitOrder(initOrder);
    }

    private void spawnBoars() {
        game.setRoundState(RoundState.SPAWNBOARS);
        int playerCount = game.getPlayers().size();
        for (GameVector spawn : game.getBoarSpawns()) {
            Npc boar = null;
            for(Npc npc : game.getNpcs()){
                if(npc.getCreatureType() == CreatureType.BOAR && npc.getAnchorPoint().equals(spawn)){
                    boar = npc;
                    break;
                }
            }

            boolean spawnBoar = false;
            if (boar != null) {
                if (boar.isDead()) {
                    if (boar.getRoundsDead() >= 6 && game.getCreatureByPosition(spawn) == null) {
                        boar.receiveDamage(-boar.getMaxHp());
                        boar.setPosition(spawn);
                        boar.setPoisons(new ArrayList<>());
                        boar.setCurrentDirection(Direction.EAST);
                        boar.setRoundsDead(0);
                        spawnBoar = true;
                    } else {
                        boar.increaseRoundsDead();
                    }
                }
            } else {
                int id = playerCount + game.getNpcs().size();
                boar = new Npc(id, spawn, 20, CreatureType.BOAR, 6, 20);
                game.addNpc(boar);

                spawnBoar = true;
            }

            if (spawnBoar) {
                for (int commId : commIds.getCommLibIds()) {
                    serverConnection.sendBoarSpawned(commId, boar.getId(), spawn.getX(), spawn.getY());
                }
            }
        }
    }

    private void moveFairies() {
        game.setRoundState(RoundState.MOVEFAIRIES);
        for (Npc npc : game.getNpcs()) {
            if (npc.getCreatureType() == CreatureType.FAIRY) {
                if (npc.getPosition().distanceTo(npc.getAnchorPoint()) > npc.getMaxAnchorPointDistance()) {
                    game.removeNpc(npc);

                    for (int commId : commIds.getCommLibIds()) {
                        serverConnection.sendDied(commId, npc.getId());
                    }

                    continue;
                }

                int moves = npc.getMaxSteps();
                int dirChanged = 6;

                while (moves > 0 && dirChanged > 0) {
                    if (canMoveNpc(npc)) {
                        moveNpc(npc);
                        dirChanged = 6;
                        moves--;
                    } else {
                        Direction newDir = DirectionHelper.getCounterClockwise(npc.getCurrentDirection());
                        npc.setCurrentDirection(newDir);
                        dirChanged--;

                        if (game.getFieldAt(npc.getPosition()) == Field.ICE && npc.getLastDirection() != null) {
                            break;
                        }
                    }
                }

            }
        }
    }

    private void moveBoars() {
        game.setRoundState(RoundState.MOVEBOARS);
        for (Npc npc : game.getNpcs()) {
            if (!npc.isDead() && npc.getCreatureType() == CreatureType.BOAR) {
                if (npc.getPosition().distanceTo(npc.getAnchorPoint()) > npc.getMaxAnchorPointDistance()) {
                    npc.receiveDamage(npc.getHp());

                    for (int commId : commIds.getCommLibIds()) {
                        serverConnection.sendDied(commId, npc.getId());
                    }

                    continue;
                }

                int moves = npc.getMaxSteps();
                int dirChanged = 6;

                while (moves > 0 && dirChanged > 0) {
                    if (canMoveNpc(npc)) {
                        moveNpc(npc);
                        dirChanged = 6;
                        moves--;
                    } else {
                        GameVector newPos = DirectionHelper.toVector(npc.getCurrentDirection());
                        newPos = newPos.add(npc.getPosition());
                        Creature victim = game.getCreatureByPosition(newPos);
                        if (victim != null) {
                            int dmg = (int)(5 * FieldEffects.getDefensiveMultiplier(game.getFieldAt(newPos), victim.getCreatureType()));
                            int hpCalc = victim.getHp() - dmg;
                            boolean died = false;

                            if (hpCalc <= 0) {
                                died = true;
                            }

                            victim.receiveDamage(dmg);

                            for (int id : commIds.getCommLibIds()) {
                                serverConnection.sendBoarAttack(id, npc.getId(), victim.getId());
                                if (died) {
                                    serverConnection.sendDied(id, victim.getId());
                                }
                            }

                            if (died && victim.getCreatureType() != CreatureType.BOAR) {
                                game.removeCreature(victim);
                            }
                        }

                        Direction newDir = DirectionHelper.getCounterClockwise(npc.getCurrentDirection());
                        npc.setCurrentDirection(newDir);
                        dirChanged--;
                    }
                }

            }
        }
    }

    private boolean canMoveNpc(Npc npc) {
        Direction dir = npc.getCurrentDirection();
        GameVector newPosition = DirectionHelper.toVector(dir);
        newPosition = newPosition.add(npc.getPosition());

        if (newPosition.distanceTo(npc.getAnchorPoint()) > npc.getMaxAnchorPointDistance()) {
            return false;
        }

        if(game.getFieldAt(npc.getPosition()) == Field.ICE && npc.getLastDirection() != null && npc.getLastDirection() != dir){
            return false;
        }

        if (game.getCreatureByPosition(newPosition) != null) {
            return false;
        }

        Field field = game.getFieldAt(newPosition);

        return FieldEffects.canEnter(field, npc.getCreatureType());
    }

    private void moveNpc(Npc npc) {
        Direction dir = npc.getCurrentDirection();
        npc.setLastDirection(dir);
        GameVector newPosition = DirectionHelper.toVector(dir);
        newPosition = newPosition.add(npc.getPosition());

        npc.setPosition(newPosition);

        for (int commId : commIds.getCommLibIds()) {
            serverConnection.sendMoved(commId, npc.getId(), npc.getCurrentDirection());
        }
    }

    private void actState() {
        game.setRoundState(RoundState.ACTNOW);
        game.setCurrentMonsterIndex(0);
        boolean sendActNow = true;
        while (game.getCurrentMonsterIndex() < game.getInitOrder().size()) {
            if(sendActNow){
                for (int commId : commIds.getCommLibIds()) {
                    serverConnection.sendActNow(commId, game.getMonsterActing());
                }
            }

            sendActNow = true;
            Command cmd = null;
            String message = "Kicked because of invalid move!";
            try {
                cmd = serverConnection.nextCommand();
            } catch (TimeoutException e) {
                System.out.println("Player with id: " + game.getMonsterActing() + " had timeout!");
                message = "Kicked because of timeout!";
            }

            if (cmd != null && cmd.validateCommand(game)) {
                cmd.executeCommand(game);
                cmd.sendResults(serverConnection);
            } else {
                int id = game.getMonsterActing();
                if(cmd != null && cmd.getMonsterId() != game.getMonsterActing()) {
                    id = cmd.getMonsterId();

                    sendActNow = false;
                }

                if(id >= 0){
                    Pc pc = game.getMonsterById(id);
                    if(pc != null){
                        for (int commId : commIds.getCommLibIds()) {
                            serverConnection.sendKicked(commId, id, message);
                        }
                        game.removeMonster(pc);
                    }
                }
            }
        }
    }

    private void fieldEffectState() {
        game.setRoundState(RoundState.FIELDEFFECTS);
        for (Creature creature : game.getCreatures()) {
            if(creature.isDead()){
                continue;
            }

            GameVector position = creature.getPosition();
            Field field = game.getFieldAt(position);

            int dmg = FieldEffects.getDamage(field, creature.getCreatureType());
            if (dmg == 0)
                continue;

            int hpCalc = creature.getHp() - dmg;
            boolean died = false;

            if (hpCalc <= 0) {
                died = true;
            } else if (hpCalc > creature.getMaxHp())
                dmg += (hpCalc - creature.getMaxHp());

            if (dmg != 0) {
                creature.receiveDamage(dmg);

                for (int id : commIds.getCommLibIds()) {
                    serverConnection.sendFieldEffect(id, creature.getPosition().getX(), creature.getPosition().getY(), dmg);
                    if (died) {
                        serverConnection.sendDied(id, creature.getId());
                    }
                }

                if (died && creature.getCreatureType() != CreatureType.BOAR) {
                    game.removeCreature(creature);
                }
            }
        }
    }

    private void poisonState() {
        game.setRoundState(RoundState.POISONS);
        for (Creature creature : game.getCreatures()) {
            if(creature.isDead()){
                continue;
            }

            List<Poison> poisons = creature.getPoisons();

            for (Poison p : poisons) {
                if(creature.isDead()){
                    break;
                }

                switch (p) {
                    case FIRSTROUND:
                        break;
                    case HEAVY:
                        DoPoisonDmg(creature, 4);
                        break;
                    case MINOR:
                        DoPoisonDmg(creature, 2);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void DoPoisonDmg(Creature creature, int dmg) {
        int hpCalc = creature.getHp() - dmg;
        boolean died = false;

        if (hpCalc <= 0) {
            died = true;
        }

        if (dmg != 0) {
            creature.receiveDamage(dmg);

            for (int id : commIds.getCommLibIds()) {
                serverConnection.sendPoisonEffect(id, creature.getId(), dmg);
                if (died) {
                    serverConnection.sendDied(id, creature.getId());
                }
            }

            if (died && creature.getCreatureType() != CreatureType.BOAR) {
                game.removeCreature(creature);
            }
        }
    }

    private void roundEndState(int hpDifference) {
        game.setRoundState(RoundState.ROUNDEND);
        if (hpDifference <= 0) {
            game.increaseBoringRounds();
        } else {
            game.setBoringRounds(0);
        }

        for (Creature creature : game.getCreatures()) {
            if(creature.isDead()){
                continue;
            }

            List<Poison> poisons = creature.getPoisons();
            List<Poison> newPoisons = new ArrayList<>();

            for (Poison p : poisons) {
                switch (p) {
                    case FIRSTROUND:
                        newPoisons.add(Poison.HEAVY);
                        break;
                    case HEAVY:
                        newPoisons.add(Poison.MINOR);
                        break;
                    case MINOR:
                        break;
                    default:
                        break;
                }
            }

            creature.setPoisons(newPoisons);
        }

        List<Pc> monsters = game.getMonsters();
        for (Pc monster : monsters) {
            int pos = game.getInitOrder().indexOf(monster.getId());
            int fairness = (pos + 1) % monsters.size();
            monster.setFairness(fairness);
            monster.setCriedInRound(false);
        }

        for (int id : commIds.getCommLibIds()) {
            serverConnection.sendRoundEnd(id, game.getRoundCounter(), game.getBoringRounds());
        }

        HashMap<String, Integer> teamMap = new HashMap<>();
        for (Player player : game.getPlayers()) {
            String teamName = player.getTeamName();
            int players = 0;
            if (teamMap.containsKey(teamName)) {
                players = teamMap.get(teamName);
            }
            players++;
            teamMap.put(teamName, players);
        }

        if (teamMap.isEmpty() || game.getBoringRounds() == 100) {
            if (game.getBoringRounds() == 100) {
                List<Pc> pcs = game.getMonsters();
                for (Pc pc : pcs) {
                    for (int id : commIds.getCommLibIds()) {
                        serverConnection.sendKicked(id, pc.getId(), "Too much boring rounds!");
                    }
                    game.removeMonster(pc);
                }
            }

            for (int id : commIds.getCommLibIds()) {
                serverConnection.sendWinner(id, "");
            }
            running = false;
        } else if (teamMap.size() == 1) {
            Map.Entry<String, Integer> winnerTeam = teamMap.entrySet().iterator().next();
            for (int id : commIds.getCommLibIds()) {
                serverConnection.sendWinner(id, winnerTeam.getKey());
            }
            running = false;
        }
    }

    private int hpSumOfCreatures() {
        int hpSum = 0;

        for (Npc npc : game.getNpcs()) {
            if(npc.getCreatureType() == CreatureType.FAIRY){
                hpSum += npc.getHp();
            }
        }

        for (Pc monster : game.getMonsters()) {
            hpSum += monster.getHp();
        }

        return hpSum;
    }

    public CommIds getCommIds() {
        return commIds;
    }

    public ServerConnection getServerConnection() {
        return serverConnection;
    }
}
