package de.unisaarland.sopra.messages.attack;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.comm.ServerConnection;
import de.unisaarland.sopra.model.RoundState;
import de.unisaarland.sopra.utility.LogString;


/**
 * Created by DJ MacHack on 12.09.2016.
 */
public class Cut extends SimpleAttack {

    private static final int DAMAGE = 5;
    private static final int ENERGYCOST = 200;
    private static final CreatureType[] VALIDCREATURES = new CreatureType[] {CreatureType.NAGA} ;

    /**
     * create new cut attacktest
     * @param comm  CommID
     * @param n     PlayerID
     * @param dir   Direction
     */
    public Cut (CommIds comm, int n, Direction dir) {
        super(comm, n, dir);
    }

    /**
     * test cut on game
     * @param game
     * @return
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
     * test on client
     * @param cli
     * @return
     */
    @Override
    public boolean validateEvent(Game game, Client cli) {
        return canAttack(game);
    }

    @Override
    public String getText(Client cli) {
        return String.format("%s cuts %s!", LogString.createVictimName(cli.getGame(),this.actorId),
                LogString.createVictimName(cli.getGame(),this.actorId,super.direction));
    }

    /**
     * execute on game
     * @param game
     */
    @Override
    public void executeCommand(Game game) {
        attack(game);
        if (super.targetDied && (game.getCreatureById(super.targetId).getCreatureType() != CreatureType.BOAR))
            game.removeCreature(game.getCreatureById(super.targetId));
    }

    /**
     * execute on client
     * @param cli
     */
    @Override
    public void executeEvent(Game game, Client cli) {
        attack(game);
    }

    /**
     * send command
     * @param cliConn
     */
    @Override
    public void sendCommand(ClientConnection cliConn) {
        cliConn.sendCut(super.direction);
    }

    /**
     * send results
     * @param serverConn
     */
    @Override
    public void sendResults(ServerConnection serverConn) {
        for (int commId : super.commIDs.commLibIds) {
            serverConn.sendCut(commId, super.actorId, super.direction);
            if (super.targetDied)
                serverConn.sendDied(commId, super.targetId);
        }
    }

    private void attack(Game game) {
        meleeAttack(game, this.ENERGYCOST, this.VALIDCREATURES, this.DAMAGE);
    }

    private boolean canAttack(Game game) {
        return meleeTestCommand(game, this.ENERGYCOST, this.VALIDCREATURES);
    }
}
