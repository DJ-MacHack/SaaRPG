package de.unisaarland.sopra.messages.attack;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.messages.Command;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.*;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.comm.ServerConnection;
import de.unisaarland.sopra.utility.LogString;

/**
 * Created by Team 14 on 12.09.2016.
 */
public class Blink implements Command, Event {

    private static final int ENERGYCOST = 200;
    private static final CreatureType[] VALIDCREATURES = new CreatureType[]{ CreatureType.WRAITH };
    private final int actorId;
    private final int x;
    private final int y;
    private CommIds commIds;

    public Blink(CommIds commIds, int actorId, int x, int y){
        this.commIds = commIds;
        this.actorId = actorId;
        this.x = x;
        this.y = y;
    }

    @Override
    public void executeCommand(Game game) {
        execute(game);
    }

    @Override
    public boolean validateCommand(Game game) {
        // check if clientGame is in RoundState ActNow (first set by ActNow Event)
        if (game.getRoundState() != RoundState.ACTNOW)
            return false;

        // check if it is acting monster's turn
        if (game.getMonsterActing() != this.actorId) {
            return false;
        }
        return canExecute(game);
    }

    @Override
    public void sendResults(ServerConnection sc) {
        for (int commId : this.commIds.commLibIds)
            sc.sendBlinked(commId, this.actorId, this.x, this.y);
    }

    @Override
    public void sendCommand(ClientConnection cc) {
        cc.sendBlink(this.x, this.y);
    }

    @Override
    public int getMonsterId() {
        return actorId;
    }

    @Override
    public void executeEvent(Game game, Client client) {
        execute(game);
    }

    @Override
    public boolean validateEvent(Game game, Client client) {
        return canExecute(game);
    }

    @Override
    public String getText(Client cli) {
        return String.format("%s blinks to %d,%d!", LogString.createVictimName(cli.getGame(),this.actorId),this.x,this.y);
    }

    public void execute(Game game) {
        Pc executer = game.getMonsterById(this.actorId);
        executer.setLastDirection(null);
        GameVector targetpos = new GameVector(this.x, this.y);

        int range = executer.getPosition().distanceTo(targetpos);           //enough Energy
        int energycost = this.ENERGYCOST * range;
        executer.drainEnergy(energycost);

        executer.setPosition(targetpos);
    }

    public boolean canExecute(Game game) {
        Pc executer = game.getMonsterById(this.actorId);

        if(executer == null){
            return false;
        }

        if(executer.isDead()){
            return false;
        }

        boolean allowed = false;
        for (CreatureType t : this.VALIDCREATURES) {
            allowed = allowed || (executer.getCreatureType() == t);
        }
        if (!allowed) return false; //Check if me is a valid creature

        GameVector targetpos = new GameVector(this.x, this.y);
        int range = executer.getPosition().distanceTo(targetpos);           //enough energy
        int energycost = this.ENERGYCOST * range;
        if (executer.getEnergy() < energycost)
            return false;

        if (!FieldEffects.canEnter(game.getFieldAt(targetpos), executer.getCreatureType()))
            return false;
        if (game.getCreatureByPosition(targetpos) != null)                     //is there already a creature
            return false;

        return true;
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CommIds getCommIds() {
        return commIds;
    }
}
