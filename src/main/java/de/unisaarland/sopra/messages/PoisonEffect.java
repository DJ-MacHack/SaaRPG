package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.model.Creature;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.utility.LogString;

/**
 * Created by Team14 on 12/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 * implemented by Karla
 */
public class PoisonEffect implements Event {

    private final int entityId;
    private final int value;

    /**
     * creates an instance of {@link PoisonEffect} {@link Event} representing a
     * poison-effect (damage) at a given {@link de.unisaarland.sopra.model.Creature}
     * and given damage-value
     * @param entityId id of the affected {@link de.unisaarland.sopra.model.Creature}
     * @param value damage-value of the effect
     */
    public PoisonEffect(int entityId, int value) {
        this.entityId = entityId;
        this.value = value;
    }

    @Override
    public int hashCode() {
        int result = entityId;
        result = 31 * result + value;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PoisonEffect that = (PoisonEffect) o;

        if (entityId != that.entityId) return false;
        return value == that.value;
    }

    @Override
    public void executeEvent(Game game, Client client) {
        Creature creature = game.getCreatureById(entityId);

        creature.receiveDamage(value);
    }

    @Override
    public boolean validateEvent(Game game, Client client) {
        Creature monster = game.getMonsterById(entityId);

        if(monster == null || monster.isDead()){
            return false;
        }

        if (this.value == 0){
            return false;
        }

        return true;
    }

    @Override
    public void sendCommand(ClientConnection cc) {
        throw new UnsupportedOperationException("You are not allowed to call a PoisonEffectEvent!");
    }

    @Override
    public String getText(Client cli) {
        return String.format("%s received %d poison damage!", LogString.createVictimName(cli.getGame(),this.entityId),this.value);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public int getValue() {
        return this.value;
    }
}
