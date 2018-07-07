package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.model.RoundState;

/**
 * Created by Team14 on 12/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class RegistrationAborted implements Event {

    /**
     * creates an instance of {@link RegistrationAborted} {@link Event} which aborts the game registration
     */
    public RegistrationAborted() {
    }

    @Override
    public void executeEvent(Game game, Client client) {
        client.setRunning(false);
    }

    @Override
    public boolean validateEvent(Game game, Client client) {
        return true;
    }

    @Override
    public void sendCommand(ClientConnection cc) {
        throw new UnsupportedOperationException("You are not allowed to call a RegistrationAbortedEvent!");
    }

    @Override
    public String getText(Client cli) {
        return "Server aborted registration!";
    }
}
