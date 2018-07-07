package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.model.RoundState;
import de.unisaarland.sopra.utility.InvalidFightFileException;
import de.unisaarland.sopra.utility.InvalidMapFileException;

/**
 * Created by Team 14 on 12.09.2016.
 * implemented by Karla
 */
public class Fight implements Event {

    private final String fightFile;

    public Fight(String fightFile){
        this.fightFile = fightFile;
    }

    public void executeEvent(Game game, Client client){
        try {
            game.handleFightfile(fightFile);
            game.handleMapfile(game.getMapFile());
        } catch (InvalidFightFileException | InvalidMapFileException e) {
            e.printStackTrace();
            client.setRunning(false);
        }
    }

    public boolean validateEvent(Game game, Client client){
        return (fightFile != null) && (game.getMapFile() != null);
    }

    @Override
    public void sendCommand(ClientConnection cc) {
        throw new UnsupportedOperationException("You are not allowed to call a FightEvent!");
    }

    @Override
    public String getText(Client cli) {
        return "Server sent a FightFile!";
    }

    public String getFightFile() {
        return this.fightFile;
    }
}
