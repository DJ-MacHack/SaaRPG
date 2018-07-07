package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.model.Game;

/**
 * Created by Team14 on 9/12/16.
 */
public abstract class SpawnEvent implements Event {
    protected int creatureId;
    protected int x_coord, y_coord;

    public SpawnEvent(int id, int x, int y){
        this.creatureId = id;
        this.x_coord = x;
        this.y_coord = y;
    }

    @Override
    public abstract void executeEvent(Game game, Client cli);

    @Override
    public abstract boolean validateEvent(Game game, Client cli);

    public int getCreatureId() {
        return this.creatureId;
    }

    public int getX_coord() {
        return this.x_coord;
    }

    public int getY_coord() {
        return this.y_coord;
    }
}
