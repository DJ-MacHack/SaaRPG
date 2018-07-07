package de.unisaarland.sopra;

import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.model.Npc;
import de.unisaarland.sopra.model.Pc;
import de.unisaarland.sopra.utility.RegistrationAbortedException;

/**
 * Master class for {@link Server} and {@link Client}
 * Created on 12.09.16.
 * @author Henrik Paul Koehn, Lukas Kirschner
 */
public abstract class Master {

    //Fields
    protected boolean running;  // true if current game is running
    protected Game game;        // game model to play on

    public Master() {
        this.running = true;
        this.game = new Game();
    }

    /**
     * @return Returns current {@link Game} model
     */
    public Game getGame() {
        return game;
    }

    /**
     * runs {@link Server}/{@link Client}
     */
    public abstract void run() throws RegistrationAbortedException;

    /**
     * Resets energy and lastDirection for every creature in given game instance
     * @param game game instance to reset
     */
    public void resetCreatureStats(Game game){
        for (Pc monster : game.getMonsters()) {
            monster.setEnergy(1000);
            monster.setLastDirection(null);
            monster.setCriedInRound(false);
        }

        for (Npc npc : game.getNpcs()) {
            npc.setLastDirection(null);
        }
    }
}
