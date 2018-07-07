package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.CommIds;

import de.unisaarland.sopra.model.*;

import de.unisaarland.sopra.MonsterType;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.comm.ServerConnection;
import de.unisaarland.sopra.utility.LogString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Register {@link Command}/{@link Event}
 * Created on 12.09.16.
 *
 * @author Henrik Paul Koehn
 */
public class Register implements Command, Event {

    private static final int KOBOLD_HP = 100;
    private static final int ELF_HP = 100;
    private static final int ROOK_HP = 100;
    private static final int YETI_HP = 200;
    private static final int IFRIT_HP = 87;
    private static final int WRAITH_HP = 100;
    private static final int NAGA_HP = 100;

    // Health of creatures
    private static final Map<CreatureType, Integer> HP;

    static {
        HP = new HashMap<>();
        HP.put(CreatureType.KOBOLD, KOBOLD_HP);
        HP.put(CreatureType.ELF, ELF_HP);
        HP.put(CreatureType.ROOK, ROOK_HP);
        HP.put(CreatureType.YETI, YETI_HP);
        HP.put(CreatureType.IFRIT, IFRIT_HP);
        HP.put(CreatureType.WRAITH, WRAITH_HP);
        HP.put(CreatureType.NAGA, NAGA_HP);
    }

    //Fields
    private int commId;                 // The commlib id of the new player
    private String name;                // Name of the player
    private CreatureType creatureType;  // Creature type of the player
    private String teamName;            // name of the team of the player
    private int startNumber;            // start number of the player
    private int xCoord;                // x Coordinate of the start position of the player
    private int yCoord;                // y Coordinate of the start position of the player
    private CommIds commIds;            // Class with all Commlib ids

    private Game game;                  // Save game for results

    /**
     * @param commIds      {@link CommIds} Ids
     * @param playerId     Player Id of the to be registered player
     * @param name         Name of the {@link Player}
     * @param creatureType {@link CreatureType} of the player
     * @param teamName     Name of the {@link Team} of the {@link Player}
     * @param startNumber  Startnumber of the {@link Player}
     * @param x            x Coordinate of the start position of the {@link Player}
     * @param y            y Coordinate of the start position of the {@link Player}
     */
    public Register(CommIds commIds, int playerId, String name, CreatureType creatureType, String teamName,
                    int startNumber, int x, int y) {
        this.commIds = commIds;
        this.commId = playerId;
        this.name = name;
        this.teamName = teamName;
        this.creatureType = creatureType;
        this.startNumber = startNumber;
        this.xCoord = x;
        this.yCoord = y;
    }

    /**
     * Executes as {@link Command} on a given {@link Game}
     *
     * @param game {@link Game} to manipulate
     */
    @Override
    public void executeCommand(Game game) {
        if(game.getRoundState() != RoundState.REGISTER){
            return;
        }

        Team pTeam = game.getTeam(this.teamName);
        GameVector startVec = pTeam.nextStartPosition();
        addPc(game, startVec);
        commIds.addCommMonsterId(commId, startNumber);
        this.xCoord = startVec.getX();
        this.yCoord = startVec.getY();

        // Save files
        this.game = game;
    }

    /**
     * Tests if this {@link Event}/{@link Command} is valid as command for a given {@link Game}
     *
     * @param game {@link Game} to test
     * @return Returns true if valid else false
     */
    @Override
    public boolean validateCommand(Game game) {
        int id = commIds.getMonsterId(commId);
        if (id >= 0) {
            // schon registriert
            return false;
        } else {
            // noch nicht registriert
            if (game.getRoundState() == RoundState.REGISTER) {
                Player player = game.getPlayerByName(name);
                return player != null
                        && player.getTeamName().equals(teamName)
                        && player.getMonsterType() == this.creatureType
                        && testRegistered(game);
            } else {
                return true;
            }
        }
    }

    /**
     * Handles aftermath of execution and sends corresponding {@link Event}
     *
     * @param sc {@link ServerConnection} which shall send the {@link Event}
     */
    @Override
    public void sendResults(ServerConnection sc) {
        if(game.getRoundState() != RoundState.REGISTER){
            return;
        }

        // Send map file
        sc.sendMap(this.commId, this.game.getMapFile());
        // Send fight file
        sc.sendFight(this.commId, this.game.getFightFile());

        // Send registered events to all registered player (inclusive new player)
        List<Integer> ids = this.commIds.getCommLibIds();
        for (int id : ids) {
            sc.sendRegistered(id, this.startNumber, this.name, MonsterType.valueOf(this.creatureType.name()),
                    this.teamName, this.startNumber, this.xCoord, this.yCoord);
        }

        // Send register events of all old players to the new player
        List<Player> players = this.game.getPlayers();
        for (Player player : players) {
            // Is player already registered and not the new player
            if (player.getRegistered() && !this.name.equals(player.getName())) {

                // Get x and y coord of target player
                GameVector vec = game.getCreatureById(player.getId()).getPosition();
                sc.sendRegistered(this.commId, player.getId(), player.getName(),
                        MonsterType.valueOf(player.getMonsterType().name()),
                        player.getTeamName(), player.getId(), vec.getX(), vec.getY());
            }
        }
    }

    /**
     * Sends this as {@link Command} via the given {@link ClientConnection}
     *
     * @param cc {@link ClientConnection} which shall send the {@link Command}
     */
    @Override
    public void sendCommand(ClientConnection cc) {
        cc.sendRegister(this.name, MonsterType.valueOf(this.creatureType.name()), this.teamName);
    }

    @Override
    public int getMonsterId() {
        return startNumber;
    }

    /**
     * Execute as {@link Event} for a given {@link Client}
     *
     * @param client {@link Client} to manipulate
     */
    @Override
    public void executeEvent(Game game, Client client) {
        GameVector startPos = new GameVector(this.xCoord, this.yCoord);
        addPc(game, startPos);

        if (this.name.equals(client.getName()) && this.teamName.equals(client.getTeamName())) {
            client.setOwnCreature(this.startNumber);
        }
    }

    /**
     * Tests if this {@link Event}/{@link Command} is valid as {@link Event} for a given {@link Client}
     *
     * @param client {@link Client} to test
     * @return returns true if valid else false
     */
    @Override
    public boolean validateEvent(Game game, Client client) {
        Player player = game.getPlayerByName(this.name);
        return (player != null && !player.getRegistered());
    }

    @Override
    public String getText(Client cli) {
        return String.format("%s registered!", LogString.createVictimName(this.name,this.startNumber,this.teamName));
    }

    /**
     * Adds a {@link Player} to a given {@link Game}.
     * The information of the {@link Player} are taken from the fields of this class
     *
     * @param game {@link Game} the {@link Player} is to be added
     */
    private void addPc(Game game, GameVector position) {
        // TODO assumption id is already assinged
        // add pc to game
        Pc pc = new Pc(this.startNumber, position, HP.get(this.creatureType), this.creatureType);
        game.addMonster(pc);
        // Do register stuff of player
        Player player = game.getPlayerByName(this.name);
        player.register(this.startNumber);
    }

    /**
     * Checks if the {@link Player} may be added to the {@link Game}
     *
     * @param game {@link Game} the {@link Player} is to be added
     */
    private boolean testRegistered(Game game) {
        Player player = game.getPlayerByName(this.name);
        return player != null
                && !player.getRegistered();
    }

    // Getter for systemtests

    public int getId() {
        return startNumber;
    }

    public String getName() {
        return name;
    }

    public String getTeamName() {
        return teamName;
    }

    public CreatureType getCreatureType() {
        return creatureType;
    }

    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }
}

