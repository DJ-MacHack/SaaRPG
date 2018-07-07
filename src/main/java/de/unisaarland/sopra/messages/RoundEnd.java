package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.model.RoundState;

/**
 * RoundEnd {@link Event}
 * Created on 12.09.16.
 * @author Henrik Paul Koehn
 */
public class RoundEnd implements Event{

    //Fields
    private int roundNumber;  // Number of the current round
    private int boredom;      // Current boredom

    /**
     * @param roundNumber Current round
     * @param boredom     Current boredom
     */
    public RoundEnd(int roundNumber, int boredom) {
        this.roundNumber = roundNumber;
        this.boredom = boredom;
    }

    /**
     * Execute as {@link Event} for a given {@link Client}
     *
     * @param client to manipulate
     */
    @Override
    public void executeEvent(Game game, Client client) {
        game.setRoundState(RoundState.ROUNDEND);
        game.setBoringRounds(this.boredom);
    }

    /**
     * Tests if this {@link Event}/{@link Command} is valid as {@link Event} for a given {@link Client}
     *
     * @param client {@link Client} to test
     * @return returns true if valid else false
     */
    @Override
    public boolean validateEvent(Game game, Client client) {
        return game.getRoundCounter() == this.roundNumber;
    }

    @Override
    public void sendCommand(ClientConnection cc) {
        throw new UnsupportedOperationException("You are not allowed to call a PoisonEffectEvent!");
    }

    @Override
    public String getText(Client cli) {
        return String.format("Round %d ends. Number of boring rounds: %d!", this.roundNumber, this.boredom);
    }

    public int getRoundNumber() {
        return this.roundNumber;
    }

    public int getBoredom() {
        return this.boredom;
    }
}
