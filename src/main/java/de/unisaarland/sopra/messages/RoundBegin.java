package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.model.RoundState;

/**
 * RoundBegin {@link Event}
 * Created on 12.09.16.
 * @author Henrik Paul Koehn
 */
public class RoundBegin implements Event {

    //Fields
    private int roundNumber;  // Number of the current round

    /**
     * @param roundNumber Current round
     */
    public RoundBegin(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    /**
     * Execute as {@link Event} for a given {@link Client}
     *
     * @param client to manipulate
     */
    @Override
    public void executeEvent(Game game, Client client) {
        game.setRoundState(RoundState.ROUNDBEGIN);
        game.increaseRoundCounter();
        game.setCurrentMonsterIndex(0);
        client.resetCreatureStats(game);
    }

    /**
     * Tests if this {@link Event}/{@link Command} is valid as {@link Event} for a given {@link Client}
     *
     * @param client {@link Client} to test
     * @return returns true if valid else false
     */
    @Override
    public boolean validateEvent(Game game, Client client) {
        return (game.getRoundCounter() == this.roundNumber - 1);
    }

    @Override
    public void sendCommand(ClientConnection cc) {
        throw new UnsupportedOperationException("You are not allowed to call a RoundBeginEvent!");
    }

    @Override
    public String getText(Client cli) {
        return String.format("Round %d begins!", this.roundNumber);
    }

    public int getRoundNumber() {
        return this.roundNumber;
    }
}
