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
 * Implemented by Lukas Kirschner
 */
public class Move implements Command, Event {

    private int actorId;
    private Direction direction;
    private CommIds commid;

    public Move(CommIds commid, int actorId, Direction direction) {
        this.actorId = actorId;
        this.direction = direction;
        this.commid = commid;


    }

    @Override
    public void executeCommand(Game game) {
        this.move(game);
    }

    @Override
    public boolean validateCommand(Game game) {
        Creature me = game.getCreatureById(this.actorId);

        if (me == null) {
            return false;
        }

        if (me.isDead()) {
            return false;
        }

        //Check if current game state fits to the CreatureType of the actor
        switch (me.getCreatureType()) {
            case FAIRY:
                if (game.getRoundState() != RoundState.MOVEFAIRIES) {
                    return false;
                }
                break;
            case BOAR:
                if (game.getRoundState() != RoundState.MOVEBOARS) {
                    return false;
                }
                break;
            default:
                if (game.getMonsterActing() != this.actorId || game.getRoundState() != RoundState.ACTNOW) {
                    return false; //Check if actor is on turn
                }
        }

        return this.canMove(game);
    }

    @Override
    public void sendResults(ServerConnection serverconnection) {
        for (int commid : this.commid.getCommLibIds())
            serverconnection.sendMoved(commid, this.actorId, this.direction);
    }

    @Override
    public void sendCommand(ClientConnection clientconnection) {
        clientconnection.sendMove(this.direction);
    }

    @Override
    public int getMonsterId() {
        return actorId;
    }

    @Override
    public void executeEvent(Game game, Client client) {
        Creature me = game.getCreatureById(this.actorId);
        move(game);

        //Set Round State for client games to the corresponding value
        switch (me.getCreatureType()) {
            case FAIRY:
                game.setRoundState(RoundState.MOVEFAIRIES);
                break;
            case BOAR:
                game.setRoundState(RoundState.MOVEBOARS);
                break;
            default:
                game.setRoundState(RoundState.ACTNOW);
        }

    }

    @Override
    public boolean validateEvent(Game game, Client client) {
        return canMove(game);
    }

    @Override
    public String getText(Client cli) {
        return String.format("%s moves %s!", LogString.createVictimName(cli.getGame(),this.actorId), LogString.getReadableDirectionString(this.direction));
    }

    private void move(Game game) {
        //We assume that testComand has been executed before executeCommand
        Creature me = game.getCreatureById(this.actorId);
        GameVector newPosition = me.getPosition().add(DirectionHelper.toVector(this.direction));
        if (me instanceof Pc) {
            ((Pc) me).drainEnergy(calculateActualMovementCosts((Pc) me, game, newPosition)); //Drain energy
        }
        me.setPosition(newPosition);
        me.setLastDirection(this.direction);
    }

    private boolean canMove(Game game) {
        Creature me = game.getCreatureById(this.actorId);

        GameVector newPosition = me.getPosition().add(DirectionHelper.toVector(this.direction));

        //Check if Direction fits to the last direction of monster if monster stands on an ice tile
        if ((game.getFieldAt(me.getPosition()) == Field.ICE) && (me.getCreatureType() != CreatureType.YETI)){
            if ((me.getLastDirection() != null) && (me.getLastDirection() != this.direction)) {
                return false;
            }
        }

        //Check if target field is obstructed
        if (game.getCreatureByPosition(newPosition) != null) {
            return false;
        }

        //Check if me is allowed to enter the target field
        if (!(FieldEffects.canEnter(game.getFieldAt(newPosition),me.getCreatureType()))) {
            return false;
        }

        //Check if (Pc)me has enough energy to move
        if (me instanceof Pc) {
            int actualMovementCosts = calculateActualMovementCosts((Pc) me, game, newPosition);
            if (((Pc) me).getEnergy() < actualMovementCosts) {
                return false;
            }
        }

        return true;
    }

    private int calculateActualMovementCosts(Pc me, Game game, GameVector newPosition) {
        int actualMovementCosts = -1;
        switch (me.getCreatureType()) {
            case NAGA:
                actualMovementCosts = 110;
                break;
            case ROOK:
                actualMovementCosts = 230;
                break;
            case YETI:
                actualMovementCosts = 200;
                break;
            case ELF:
            case KOBOLD:
            case IFRIT:
            case WRAITH:
            default:
                actualMovementCosts = 100;
                break;
        }
        actualMovementCosts *= FieldEffects.getMovementCostMultiplier(game.getFieldAt(newPosition), me.getCreatureType());
        return actualMovementCosts;
    }

    public int getActorId() {
        return this.actorId;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public CommIds getCommids() {
        return this.commid;
    }
}
