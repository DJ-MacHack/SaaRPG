package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.model.Game;

/**
 * Created by Team 14 on 12.09.2016.
 */
public class Winner implements Event {

    private final String teamName;

    public Winner(String teamName){
        this.teamName = teamName;
    }

    public void executeEvent(Game game, Client client){
        client.setRunning(false);
    }

    public boolean validateEvent(Game game, Client client){
     return true;
    }

    @Override
    public void sendCommand(ClientConnection cc) {
        throw new UnsupportedOperationException("You are not allowed to send a WinnerEvent!");
    }

    @Override
    public String getText(Client cli) {
        return String.format("%s wins!", this.teamName);
    }

    public String getTeamName() {
        return this.teamName;
    }
}
