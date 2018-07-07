package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.model.Game;


/**
 * Created by Team14 on 9/12/16.
 */
public interface Event {

    /**
     * Executes the Event at the given {@link Client} instance.
     * @param game {@link Game} to be updated
     * @param client {@link Client} to be used
     */
    public void executeEvent(Game game, Client client);

    /**
     * Test the Event at the given {@link Client} instance.
     * @param game {@link Game} to be updated
     * @param client {@link Client} to be used (for ActNow espacially)
     * @return true, if Event is valid
     */
    public boolean validateEvent(Game game, Client client);

    /**
     * Sends a command to the server using a given {@link ClientConnection} instance
     * @param cc Connection instance to use for sending
     */
    public void sendCommand(ClientConnection cc);

    /**
     * Gets a text that can be printed out into a logfile
     * @param cli The client on which the event should be executed on
     * @return a string that represents the event in a logfile
     */
    public String getText(Client cli);
}
