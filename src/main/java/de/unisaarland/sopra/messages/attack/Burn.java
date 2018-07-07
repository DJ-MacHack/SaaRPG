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

import java.util.*;
import java.util.Map;

import static de.unisaarland.sopra.model.FieldEffects.getAttackCostMultiplier;

/**
 * Created by Wiebk on 12.09.2016.
 * Edited/ implemented by Lukas on 14.09.2016.
 */
public class Burn implements Command, Event {

    private int actorId;
    private HashMap<Integer, Boolean> targetIdsDead;
    private static final int DAMAGE = 12;
    private static final int ENERGYCOST = 450;
    private CommIds commIds;
    private static final CreatureType[] VALIDCREATURES = {CreatureType.IFRIT};

    public Burn(CommIds commids, int actorId) {
        this.commIds = commids;
        this.actorId = actorId;
        this.targetIdsDead = new HashMap<>();
    }

    @Override
    public void executeCommand(Game game) {
        attack(game);
        for (Map.Entry<Integer, Boolean> targetId : this.targetIdsDead.entrySet()) {
            Creature creature = game.getCreatureById(targetId.getKey());
            if (targetId.getValue() && creature.getCreatureType() != CreatureType.BOAR){
                game.removeCreature(game.getCreatureById(targetId.getKey()));
            }
        }
    }

    @Override
    public boolean validateCommand(Game game) {
        // check if clientGame is in RoundState ActNow (first set by ActNow Event)
        if (game.getRoundState() != RoundState.ACTNOW){
            return false;
        }

        // check if it is acting monster's turn
        if (game.getMonsterActing() != this.actorId){
            return false;
        }
        return canAttack(game);
    }

    @Override
    public void sendResults(ServerConnection serverConn) {
        for (int commId : commIds.commLibIds) {
            serverConn.sendBurned(commId, actorId);
            for (Map.Entry<Integer, Boolean> targetId : this.targetIdsDead.entrySet()) {
                if (targetId.getValue()) {
                    serverConn.sendDied(commId, targetId.getKey());
                }
            }
        }
    }

    @Override
    public void sendCommand(ClientConnection clientconnection) {
        clientconnection.sendBurn();
    }

    @Override
    public int getMonsterId() {
        return actorId;
    }

    @Override
    public void executeEvent(Game game, Client client) {
        attack(game);
    }

    @Override
    public boolean validateEvent(Game game, Client client) {
        return canAttack(game);
    }

    @Override
    public String getText(Client cli) {
        return String.format("%s burns!", LogString.createVictimName(cli.getGame(),this.actorId));
    }

    private void attack(Game game) {
        Pc me = game.getMonsterById(this.actorId);

        int attackCost = (int) (ENERGYCOST * FieldEffects.getAttackCostMultiplier(game.getFieldAt(me.getPosition()), me.getCreatureType()));
        me.drainEnergy(attackCost);  //Drain energy of me

        int targetNumber = 0;

        Direction[] directions = {Direction.NORTH_WEST, Direction.NORTH_EAST, Direction.WEST, Direction.EAST, Direction.SOUTH_WEST, Direction.SOUTH_EAST};
        for (Direction d : directions) {
            GameVector targetPos = me.getPosition().add(DirectionHelper.toVector(d));
            if (game.getCreatureByPosition(targetPos) != null) {
                this.targetIdsDead.put(game.getCreatureByPosition(targetPos).getId(), false);
                targetNumber++;
            }
        }

        for (int targetId : this.targetIdsDead.keySet()) {
            Creature target = game.getCreatureById(targetId);
            double baseDmg = Math.floor(DAMAGE / (double)targetNumber);
            int targetDamage = (int)(baseDmg * FieldEffects.getDefensiveMultiplier(game.getFieldAt(target.getPosition()), target.getCreatureType()));
            target.receiveDamage(targetDamage);

            // target died
            if (target.isDead())
                this.targetIdsDead.put(target.getId(), true);
        }

    }

    private boolean canAttack(Game game) {
        Pc me = game.getMonsterById(this.actorId);

        if(me == null){
            return false;
        }

        //Check if me is a cheater
        if (me.isDead()){
            return false;
        }

        int attackCost = (int) (ENERGYCOST * getAttackCostMultiplier(game.getFieldAt(me.getPosition()), me.getCreatureType()));
        if (me.getEnergy() < attackCost) {
            return false; //Check if me has sufficient energy
        }

        boolean allowed = false;
        for (CreatureType t : VALIDCREATURES) {
            allowed = allowed || (me.getCreatureType() == t);
        }
        if (!allowed) {
            return false; //Check if me is a valid creature
        }

        if (!FieldEffects.canAttack(game.getFieldAt(me.getPosition()), me.getCreatureType()))
            return false; //Check if field me is standing on allows me to attacktest

        Direction[] directions = {Direction.NORTH_WEST, Direction.NORTH_EAST, Direction.WEST, Direction.EAST, Direction.SOUTH_WEST, Direction.SOUTH_EAST};
        for (Direction d : directions) {
            GameVector targetPos = me.getPosition().add(DirectionHelper.toVector(d));
            if (game.getCreatureByPosition(targetPos) != null)
                this.targetIdsDead.put(game.getCreatureByPosition(targetPos).getId(), false);
        }

        if (this.targetIdsDead.keySet().isEmpty())
            return false;

        return true;
    }

    public int getActorId() {
        return actorId;
    }

    public HashMap<Integer, Boolean> getTargetIdsDead() {
        return targetIdsDead;
    }

    public static int getDAMAGE() {
        return DAMAGE;
    }

    public static int getENERGYCOST() {
        return ENERGYCOST;
    }

    public CommIds getCommIds() {
        return commIds;
    }

    public static CreatureType[] getVALIDCREATURES() {
        return VALIDCREATURES;
    }
}
