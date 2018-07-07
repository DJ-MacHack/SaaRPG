package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.utility.LogString;

/**
 * Created by Team14 on 12/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class ActNow implements Event {

    private final int monsterId;

    /**
     * creates an instance of {@link ActNow} {@link Event} with the given parameter
     * @param monsterId monsterId of {@link de.unisaarland.sopra.model.Pc} which should act now
     */
    public ActNow(int monsterId) {
        this.monsterId = monsterId;
    }

    @Override
    public void executeEvent(Game game, Client client) {
        if(client.getOwnCreatureId() == monsterId){
            client.actNow();
        }
    }

    @Override
    public boolean validateEvent(Game game, Client client) {
        if (game.getMonsterById(monsterId) == null){
            return false;
        }

        if (client.getOwnCreatureId() != this.monsterId){
            return false;
        }

        return true;
    }

    @Override
    public void sendCommand(ClientConnection cc) {
        throw new UnsupportedOperationException("You are not allowed to call a ActNowEvent!");
    }

    @Override
    public String getText(Client cli) {
        return String.format("Act now, %s!", LogString.createVictimName(cli.getGame(),this.monsterId));
    }

    public int getMonsterId() {
        return this.monsterId;
    }
}
