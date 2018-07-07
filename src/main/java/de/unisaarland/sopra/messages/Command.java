package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.comm.ServerConnection;

/**
 * Created by Team14 on 9/12/16.7
 */
public interface Command {
    /**
     * Executes the command at the given {@link Game} instance.
     * @param game Game model to change by the command
     */
    public void executeCommand(Game game);

    /**
     * Tests the command using the given {@link Game} instance
     * @param game {@link Game} instance
     * @return true, if command test was successful
     */
    public boolean validateCommand(Game game);

    /**
     * Sends the result of the executed command to the client using a given {@link ServerConnection}
     * @param sc Connection to the clients
     */
    public void sendResults(ServerConnection sc);

    public int getMonsterId();

}
