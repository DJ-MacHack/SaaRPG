package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.model.Creature;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.model.RoundState;
import de.unisaarland.sopra.utility.LogString;

/**
 * Created by Team14 on 12/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class Died implements Event {

    private final int entityId;


    /**
     * creates an instance of {@link Died} {@link Event} with given argument
     * @param entityId id of died {@link de.unisaarland.sopra.model.Creature}
     */
    public Died(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public void executeEvent(Game game, Client client) {
        Creature c = game.getCreatureById(this.entityId);
        game.removeCreature(c);
    }

    @Override
    public boolean validateEvent(Game game, Client client) {
        Creature dyingCreature = game.getCreatureById(this.entityId);
        if (dyingCreature == null) {
            return false;
        }

        return true;
    }

    @Override
    public void sendCommand(ClientConnection cc) {
        throw new UnsupportedOperationException("You are not allowed to call a DiedEvent!");
    }

    @Override
    public String getText(Client cli) {
        return String.format("%s died!", LogString.createVictimName(cli.getGame(),this.entityId));
    }

    public int getEntityId() {
        return this.entityId;
    }
}
