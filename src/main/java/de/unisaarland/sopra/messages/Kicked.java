package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.model.Pc;
import de.unisaarland.sopra.utility.LogString;

/**
 * Created by DJ MacHack on 15.09.2016.
 */
public class Kicked implements Event {

    private int monsterId;
    private String message;

    public Kicked(int monsterId, String message) {
        this.monsterId = monsterId;
        this.message = message;
    }

    @Override
    public void executeEvent(Game game, Client client) {
        if (monsterId == client.getOwnCreatureId()) {
            System.out.println(message);
            // client.setRunning(false);
        }
        
        Pc pc = game.getMonsterById(monsterId);
        game.removeMonster(pc);
    }

    @Override
    public boolean validateEvent(Game game, Client client) {
        Pc pc = game.getMonsterById(monsterId);
        if(pc == null){
            return false;
        }

        return true;
    }

    @Override
    public void sendCommand(ClientConnection cc) {
        throw new UnsupportedOperationException("You are not allowed to call a KickedEvent!");
    }

    @Override
    public String getText(Client cli) {
        return String.format("%s has been kicked!%n   Message: %s", LogString.createVictimName(cli.getGame(),this.monsterId),this.message);
    }

    public int getMonsterId() {
        return this.monsterId;
    }

    public String getMessage() {
        return this.message;
    }
}
