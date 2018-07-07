package de.unisaarland.sopra.messages.attack;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.messages.Command;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.*;
import de.unisaarland.sopra.utility.DirectionHelper;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.comm.ServerConnection;
import de.unisaarland.sopra.utility.LogString;

/**
 * Created by Wiebk on 12.09.2016.
 * Implemented by Karla
 */

public class Shoot implements Command, Event {

    private static final int DAMAGE = 11;
    private static final int RANGE = 5;
    private static final int ENERGYCOST = 420;
    private static final CreatureType[] VALIDCREATURES = {CreatureType.ELF};


    private int actorId;
    private int targetId;
    private boolean targetisDead;
    private Direction direction;
    private CommIds commIds;

    public Shoot(CommIds commids, int actorId, Direction direction) {
        this.direction = direction;
        this.actorId = actorId;
        this.commIds = commids;
    }

    public void executeCommand(Game game) {
        attack(game);
        if (targetisDead) {
            Creature target = game.getCreatureById(targetId);
            if (target.getCreatureType() != CreatureType.BOAR) {
                game.removeCreature(target);
            }
        }
    }

    public boolean validateCommand(Game game) {
        if (game.getRoundState() != RoundState.ACTNOW) {
            return false;
        }

        if(game.getMonsterActing() != actorId){
            return false;
        }

        return canAttack(game);
    }

    public void sendResults(ServerConnection serverconnection) {
        for (int commid : commIds.getCommLibIds()) {
            serverconnection.sendShot(commid, actorId, direction);
            if (targetisDead) {
                serverconnection.sendDied(commid, targetId);
            }
        }
    }

    public void sendCommand(ClientConnection clientconnection) {
        clientconnection.sendShoot(direction);
    }

    @Override
    public int getMonsterId() {
        return actorId;
    }

    public void executeEvent(Game game, Client client) {
        attack(game);
    }

    public boolean validateEvent(Game game, Client client) {
        return canAttack(game);
    }

    @Override
    public String getText(Client cli) {
        return String.format("%s shoots %s!", LogString.createVictimName(cli.getGame(),this.actorId),
                LogString.getReadableDirectionString(this.direction));
    }

    /**
     * Changes the status of the game
     *
     * @param game
     */
    private void attack(Game game) {

        Pc monster = game.getMonsterById(actorId);
        GameVector position = monster.getPosition();
        GameVector direc = DirectionHelper.toVector(direction);
        GameVector neposition = position.add(direc);

        this.targetisDead = false;
        this.targetId = 0;
        Creature target = null;
        int newrange = RANGE + FieldEffects.getRangeIncrease(game.getFieldAt(position), monster.getCreatureType());

        for (int i = 0; i < newrange; i++) {
            target = game.getCreatureByPosition(neposition);
            if (target != null) {
                int damage = (int) (DAMAGE * FieldEffects.getDefensiveMultiplier(game.getFieldAt(neposition), target.getCreatureType()));
                target.receiveDamage(damage);
                break;
            }
            neposition = neposition.add(direc);
        }
        //Energy cost
        int energy = (int) (ENERGYCOST * FieldEffects.getAttackCostMultiplier(game.getFieldAt(position), monster.getCreatureType()));
        monster.drainEnergy(energy);

        if (target != null) {
            this.targetId = target.getId();
            if (target.isDead()) {
                this.targetisDead = true;
            }
        }
    }

    /**
     * Proofs if the creature is able/allowed to attacktest in the curren {@Link game}
     *
     * @param game
     * @return true if the creature can attacktest
     */
    private boolean canAttack(Game game) {
        Pc monster = game.getMonsterById(actorId);

        if(monster == null){
            return false;
        }

        // Am I Dead? What is life?
        if (monster.isDead()) {
            return false;
        }
        // Who am I? Am I an Elf?
        if (!(monster.getCreatureType() == CreatureType.ELF)) {
            return false;
        }

        GameVector position = monster.getPosition();

        //Where am I? Can I attacktest?
        if (!FieldEffects.canAttack(game.getFieldAt(position), monster.getCreatureType())) {
            return false;
        }

        int energy = (int) (ENERGYCOST * FieldEffects.getAttackCostMultiplier(game.getFieldAt(position), monster.getCreatureType()));
        // Am I strong enough? Do I have enough energy?
        if (monster.getEnergy() < energy) {
            return false;
        }

        int newrange = RANGE + FieldEffects.getRangeIncrease(game.getFieldAt(position), monster.getCreatureType());

        GameVector direc = DirectionHelper.toVector(direction);
        GameVector neposition = position.add(direc);

        for (int i = 0; i < newrange; i++) {
            // Is something blocking me?
            Field f = game.getFieldAt(neposition);
            if (FieldEffects.blockProjectile(f)) {
                return false;
            }
            //Is someone there?
            if (game.getCreatureByPosition(neposition) != null) {
                return true;
            }
            neposition = neposition.add(direc);
        }
        return false;
    }


    public static int getDAMAGE() {
        return DAMAGE;
    }

    public static int getRANGE() {
        return RANGE;
    }

    public static int getENERGYCOST() {
        return ENERGYCOST;
    }

    public static CreatureType[] getVALIDCREATURES() {
        return VALIDCREATURES;
    }

    public int getActorId() {
        return actorId;
    }

    public int getTargetId() {
        return targetId;
    }

    public boolean isTargetisDead() {
        return targetisDead;
    }

    public Direction getDirection() {
        return direction;
    }

    public CommIds getCommIds() {
        return commIds;
    }
}
