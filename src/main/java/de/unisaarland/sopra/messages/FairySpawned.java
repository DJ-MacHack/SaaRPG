package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.model.Field;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.model.Npc;
import de.unisaarland.sopra.utility.GameVector;

/**
 * Created by Team14 on 9/12/16.
 * Responsible for stub creation: LUKAS KIRSCHNER
 */
public class FairySpawned extends SpawnEvent {
    /**
     * Creates a new instance of a FairySpawned Event
     *
     * @param id Actor ID of the fairy
     * @param x  x coord of the healing tile
     * @param y  y coord of the healing tile
     */
    public FairySpawned(int id, int x, int y) {

        super(id, x, y);
    }

    /**
     * Executes the event at a given {@link Client}
     *
     * @param cli {@link Client} to be changed
     */
    @Override
    public void executeEvent(Game game, Client cli) {
        Npc fairy = new Npc(super.creatureId, new GameVector(super.x_coord, super.y_coord), 100, CreatureType.FAIRY, 4, 10);
        game.addNpc(fairy);

    }

    /**
     * Tests the event using a given {@link Client}
     *
     * @param cli {@link Client} to test on
     * @return true, if test was successful
     */
    @Override
    public boolean validateEvent(Game game, Client cli) {
        //Test only if fairy spawns on heal tile
        return (game.getFieldAt(new GameVector(super.x_coord, super.y_coord)) == Field.HEAL);
    }

    @Override
    public void sendCommand(ClientConnection cc) {
        throw new UnsupportedOperationException("You are not allowed to call a SpawnEvent!");
    }

    @Override
    public String getText(Client cli) {
        return String.format("Fairy(%d) spawned at %d,%d!",super.creatureId,super.x_coord,super.y_coord);
    }
}
