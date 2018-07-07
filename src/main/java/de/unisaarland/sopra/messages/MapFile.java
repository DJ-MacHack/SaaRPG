package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.model.RoundState;

/**
 * Created by Team14 on 9/12/16.
 * Responsible for stub creation: LUKAS KIRSCHNER
 */
public class MapFile implements Event {

    private String map;

    /**
     * Generates a new instance of the Map event
     *
     * @param mapfile Map file to read
     */
    public MapFile(String mapfile) {
        this.map = mapfile;
    }

    /**
     * Executes the event on a given {@link Client}
     *
     * @param cli Client to use
     */
    @Override
    public void executeEvent(Game game, Client cli) {
        game.setMapFile(map);
    }

    /**
     * Tests the event on a given {@link Client}
     *
     * @param client Client to test
     * @return true, if test was successful
     */
    @Override
    public boolean validateEvent(Game game, Client client) {
        return (map != null);
    }

    @Override
    public void sendCommand(ClientConnection cc) {
        throw new UnsupportedOperationException("You are not allowed to call a MapFileEvent!");
    }

    @Override
    public String getText(Client cli) {
        return "Server sent a MapFile!";
    }

    public String getValue() {
        return this.map;
    }
}
