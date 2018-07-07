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

import static de.unisaarland.sopra.model.FieldEffects.canAttack;
import static de.unisaarland.sopra.model.FieldEffects.getAttackCostMultiplier;
import static de.unisaarland.sopra.model.FieldEffects.getDefensiveMultiplier;

/**
 * Created by Team14 on 9/12/16.
 * Abstract superclass of commands and events concerning attacks. For a detailed documentation, see javadocs of the subclass.
 */
public abstract class SimpleAttack implements Command, Event {

    protected int actorId;
    protected Direction direction;
    protected int targetId;
    protected int damage;
    protected int range;
    protected int energyCost;
    protected CommIds commIDs;
    protected boolean targetDied;


    public SimpleAttack(CommIds comm, int actorId, Direction dir) {
        this.commIDs = comm;
        this.direction = dir;
        this.actorId = actorId;
        this.targetDied = false;
        this.targetId = -1;
    }

    /**
     * Executes the command at the given {@link Game} instance.
     *
     * @param game Game model to change by the command
     */
    @Override
    public abstract void executeCommand(Game game);

    /**
     * Tests the command using the given {@link Game} instance
     *
     * @param game {@link Game} instance
     * @return true, if command test was successful
     */
    @Override
    public abstract boolean validateCommand(Game game);

    /**
     * Sends the result of the executed command to the client using a given {@link ServerConnection}
     *
     * @param serverConn Connection to the clients
     */
    @Override
    public abstract void sendResults(ServerConnection serverConn);

    /**
     * Sends a command to the server using a given {@link ClientConnection} instance
     *
     * @param cliConn Connection instance to use for sending
     */
    @Override
    public abstract void sendCommand(ClientConnection cliConn);

    /**
     * Executes the event at a given {@link Client}
     *
     * @param cli {@link Client} to be changed
     */
    @Override
    public abstract void executeEvent(Game game, Client cli);

    /**
     * Tests the event using a given {@link Client}
     *
     * @param cli {@link Client} to test on
     * @return true, if test was successful
     */
    @Override
    public abstract boolean validateEvent(Game game, Client cli);

    protected boolean meleeTestCommand(Game game, int energyCost, CreatureType[] validCreatures) {
        Pc me = game.getMonsterById(this.actorId);

        if(me == null){
            return false;
        }

        if (me.isDead()) {
            return false; //Check if me is a cheater
        }

        int attackCost = getAttackCost(energyCost, game.getFieldAt(me.getPosition()), me.getCreatureType());
        if (me.getEnergy() < attackCost) {
            return false; //Check if me has sufficient energy
        }

        boolean allowed = false;
        for (CreatureType t : validCreatures) {
            allowed = allowed || (me.getCreatureType() == t);
        }
        if (!allowed) {
            return false; //Check if me is a valid creature
        }

        if (!canAttack(game.getFieldAt(me.getPosition()), me.getCreatureType())) {
            return false; //Check if field me is standing on allows me to attack
        }

        GameVector target = me.getPosition().add(DirectionHelper.toVector(this.direction));
        Creature targetCreature = game.getCreatureByPosition(target);
        //Check if Direction of attacktest points to a creature
        return (targetCreature != null && !targetCreature.isDead());
    }

    protected void meleeAttack(Game game, int energyCost, CreatureType[] validCreatures, int damage) {
        Pc me = game.getMonsterById(this.actorId);
        GameVector target = me.getPosition().add(DirectionHelper.toVector(this.direction));
        Creature targetCreature = game.getCreatureByPosition(target);
        int attackCost = getAttackCost(energyCost, game.getFieldAt(me.getPosition()), me.getCreatureType());
        int targetDamg = getTargetDamg(game, damage, targetCreature);


        me.drainEnergy(attackCost);  //Drain energy of me
        targetCreature.receiveDamage(targetDamg);   //Target receives damage

        if (targetCreature.isDead()) {
            targetDied = true;
        }

        targetId = targetCreature.getId();
    }

    private int getAttackCost(int baseNrg, Field myField, CreatureType myCreatureType) {
        return (baseNrg * getAttackCostMultiplier(myField, myCreatureType));
    }

    private int getTargetDamg(Game myGame, int baseDmg, Creature targetCreature) {
        return (int) (baseDmg * getDefensiveMultiplier(myGame.getFieldAt(targetCreature.getPosition()), targetCreature.getCreatureType()));
    }

    // Getter for test purposes

    public int getActorId() {
        return actorId;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getTargetId() {
        return targetId;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public CommIds getCommIDs() {
        return commIDs;
    }

    public boolean isTargetDead() {
        return targetDied;
    }

    @Override
    public int getMonsterId() {
        return actorId;
    }
}
