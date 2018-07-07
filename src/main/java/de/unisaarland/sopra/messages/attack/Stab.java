package de.unisaarland.sopra.messages.attack;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.comm.ServerConnection;
import de.unisaarland.sopra.model.RoundState;
import de.unisaarland.sopra.utility.LogString;

/**
 * Created by Team14 on 9/12/16.
 * Responsible for stub creation: LUKAS KIRSCHNER
 */
public class Stab extends SimpleAttack {

    private static final int DAMAGE = 7;
    private static final int ENERGYCOST = 250;
    private static final CreatureType[] VALIDCREATURES = {CreatureType.KOBOLD, CreatureType.ELF, CreatureType.WRAITH};

    /**
     * Creates a new instance of Stab attacktest event/command.
     *
     * @param comm Helper class for CommlibID/PlayerID conversion
     * @param n    Player ID of the attacker
     * @param dir  Direction of the attacktest
     */
    public Stab(CommIds comm, int n, Direction dir) {
        super(comm, n, dir);
    }

    /**
     * Executes the command at the given {@link Game} instance.
     *
     * @param game Game model to change by the command
     */
    @Override
    public void executeCommand(Game game) {
        attack(game);
        if (super.targetDied && (game.getCreatureById(super.targetId).getCreatureType() != CreatureType.BOAR)) {
            game.removeCreature(game.getCreatureById(super.targetId));
        }
    }

    /**
     * Tests the command using the given {@link Game} instance
     *
     * @param game {@link Game} instance
     * @return true, if command test was successful
     */
    @Override
    public boolean validateCommand(Game game) {
        // check if clientGame is in RoundState ActNow (first set by ActNow Event)
        if (game.getRoundState() != RoundState.ACTNOW) {
            return false;
        }

        // check if it is acting monster's turn
        if (game.getMonsterActing() != this.actorId) {
            return false;
        }

        return canAttack(game);
    }

    /**
     * Sends the result of the executed command to the client using a given {@link ServerConnection}
     *
     * @param serverConn Connection to the clients
     */
    @Override
    public void sendResults(ServerConnection serverConn) {
        for (int commId : super.commIDs.commLibIds) {
            serverConn.sendStabbed(commId, super.actorId, super.direction);
            if (super.targetDied) {
                serverConn.sendDied(commId, super.targetId);
            }
        }
    }

    /**
     * Sends a command to the server using a given {@link ClientConnection} instance
     *
     * @param cliConn Connection instance to use for sending
     */
    @Override
    public void sendCommand(ClientConnection cliConn) {
        cliConn.sendStab(super.direction);
    }

    /**
     * Executes the event at a given {@link Client}
     *
     * @param cli {@link Client} to be changed
     */
    @Override
    public void executeEvent(Game game, Client cli) {
        attack(game);
    }

    /**
     * Tests the event using a given {@link Client}
     *
     * @param cli {@link Client} to test on
     * @return true, if test was successful
     */
    @Override
    public boolean validateEvent(Game game, Client cli) {
        return canAttack(game);
    }

    @Override
    public String getText(Client cli) {
        return String.format("%s stabs %s!", LogString.createVictimName(cli.getGame(),this.actorId),
                LogString.createVictimName(cli.getGame(),this.actorId,super.direction));
    }

    private void attack(Game game) {
        super.meleeAttack(game, this.ENERGYCOST, this.VALIDCREATURES, this.DAMAGE);

    }

    private boolean canAttack(Game game) {

        return super.meleeTestCommand(game, this.ENERGYCOST, this.VALIDCREATURES);
    }
}
