package de.unisaarland.sopra.messages.attack;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.messages.Command;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.comm.ServerConnection;

/**
 * Created by Karla on 12.09.2016.
 */
public abstract class MagicAttack implements Command, Event {

    protected int actorId;
    protected int xCoord;
    protected int yCoord;
    protected int targetId;
    protected boolean targetDied;
    protected int damage;
    protected int range;
    protected int energyCost;
    protected CreatureType[] validCreatures = new CreatureType[0];
    protected CommIds commIds;

    /**
     * creates an instance of MagicAttack (abstract but used in Cast and Swap avoiding duplicated code)
     *
     * @param commIds CommIds instance for communication
     * @param actorId id of monster executing the attacktest
     * @param x       x-coordination of target-position
     * @param y       y-coordination of target-position
     */
    public MagicAttack(CommIds commIds, int actorId, int x, int y) {
        this.actorId = actorId;
        this.commIds = commIds;
        this.xCoord = x;
        this.yCoord = y;
        this.targetId = -1; //default value
        this.targetDied = false;
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
     * @param sc Connection to the clients
     */
    @Override
    public abstract void sendResults(ServerConnection sc);

    /**
     * Sends a command to the server using a given {@link ClientConnection} instance
     *
     * @param cc Connection instance to use for sending
     */
    @Override
    public abstract void sendCommand(ClientConnection cc);

    /**
     * Executes the event at a given {@link Client}
     *
     * @param client {@link Client} to be changed
     */
    @Override
    public abstract void executeEvent(Game game, Client client);

    /**
     * Tests the event using a given {@link Client}
     *
     * @param client {@link Client} to test on
     * @return true, if test was successful
     */
    @Override
    public abstract boolean validateEvent(Game game, Client client);

    // Getter for test purposes

    public int getActorId() {
        return actorId;
    }

    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public int getTargetId() {
        return targetId;
    }

    public boolean isTargetDead() {
        return targetDied;
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

    public CreatureType[] getValidCreatures() {
        return validCreatures;
    }

    public CommIds getCommIds() {
        return commIds;
    }

    @Override
    public int getMonsterId() {
        return actorId;
    }
}
