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
 * Created by Team14 on 12/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class Crush extends SimpleAttack{

    private static final int DAMAGE = 16;
    private static final int ENERGYCOST = 460;
    private static final CreatureType[] VALIDCREATURES = {CreatureType.YETI};

    /**
     * creates new instance of {@link Crush} with given parameters
     * @param commIds {@link CommIds} for communication
     * @param actorId monsterId of creature executing this crush-attacktest
     * @param direction {@link Direction} direction of the attacktest
     */
    public Crush(CommIds commIds, int actorId, Direction direction) {
        super(commIds, actorId, direction);
    }

    @Override
    public void executeCommand(Game game) {
        attack(game);
        if (super.targetDied && (game.getCreatureById(super.targetId).getCreatureType() != CreatureType.BOAR))
            game.removeCreature(game.getCreatureById(super.targetId));
    }

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

    @Override
    public void sendResults(ServerConnection serverConn) {
        for (int commId : super.commIDs.commLibIds) {
            serverConn.sendCrushed(commId, super.actorId, super.direction);
            if (super.targetDied)
                serverConn.sendDied(commId, super.targetId);
        }
    }

    @Override
    public void sendCommand(ClientConnection cliConn) {
        cliConn.sendCrush(super.direction);
    }

    @Override
    public void executeEvent(Game game, Client cli) {
        attack(game);
    }

    @Override
    public boolean validateEvent(Game game, Client cli) {
        return canAttack(game);
    }

    @Override
    public String getText(Client cli) {
        return String.format("%s crushes %s!", LogString.createVictimName(cli.getGame(),this.actorId),
                LogString.createVictimName(cli.getGame(),this.actorId,super.direction));
    }

    /**
     * executes the attacktest on the given {@link Game} model and therefore changes its condition
     * @param game {@link Game} on which the attacktest is executed
     */
    private void attack(Game game) {
        meleeAttack(game, this.ENERGYCOST, this.VALIDCREATURES, this.DAMAGE);
    }

    /**
     * checks if the attacktest is possible on the given {@link Game} model
     * @param game model on which the possibility of the attacktest is tested
     * @return boolean value if the attacktest is possible
     */
    private boolean canAttack(Game game) {
        return meleeTestCommand(game, this.ENERGYCOST, this.VALIDCREATURES);
    }
}
