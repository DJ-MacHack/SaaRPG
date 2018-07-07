package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.model.Creature;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.model.RoundState;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.utility.LogString;

/**
 * Created by Team14 on 12/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 * implementation Karla
 */
public class FieldEffect implements Event {

    private final int xCoord;
    private final int yCoord;
    private final int value;

    /**
     * creates an instance of {@link FieldEffect} {@link Event} representing a damage/heal fieldeffect on given field with given value
     * @param xCoord xCoord of position of the effect
     * @param yCoord yCoord of position of the effect
     * @param value value of the fieldeffect at the given position (damage: value greater than 0/ heal: value less than 0)
     */
    public FieldEffect(int xCoord, int yCoord, int value) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.value = value;
    }

    @Override
    public void executeEvent(Game game, Client client) {
            GameVector v = new GameVector(xCoord, yCoord);
            Creature creature = game.getCreatureByPosition(v);
            creature.receiveDamage(value);
            game.setRoundState(RoundState.FIELDEFFECTS);
    }

    @Override
    public boolean validateEvent(Game game, Client client) {
        GameVector v = new GameVector(xCoord, yCoord);

        //gibt es ein monster?
        if(game.getCreatureByPosition(v) == null){
            return false;
        }

        if (value == 0) {
            return false;
        }

        return true;
    }

    @Override
    public void sendCommand(ClientConnection cc) {
        throw new UnsupportedOperationException("You are not allowed to call a FieldEffectEvent!");
    }

    @Override
    public String getText(Client cli) {
        return String.format("FieldEffect at %d,%d caused %d damage to %s!", this.xCoord,this.yCoord,this.value,
                LogString.createVictimName(cli.getGame(),cli.getGame().getCreatureByPosition(new GameVector(this.xCoord,this.yCoord)).getId()));
    }

    public int getxCoord() {
        return this.xCoord;
    }

    public int getyCoord() {
        return this.yCoord;
    }

    public int getValue() {
        return this.value;
    }
}
