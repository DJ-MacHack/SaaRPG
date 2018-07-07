package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.model.*;
import de.unisaarland.sopra.utility.DirectionHelper;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.utility.LogString;

/**
 * Created by Team 14 on 12.09.2016.
 * implemented by Karla
 */
public class BoarAttack implements Event{

    private final static int damage = 5;
    private final int boarId;
    private final int victimId;

    public BoarAttack(int boardId, int victimId){
        this.boarId = boardId;
        this.victimId = victimId;
    }

    public void executeEvent(Game game, Client client){
        Creature victim = game.getCreatureById(victimId);
        int d = (int)(5 * FieldEffects.getDefensiveMultiplier(game.getFieldAt(victim.getPosition()), victim.getCreatureType()));
        victim.receiveDamage(d);
    }

    public boolean validateEvent(Game game, Client client){
        Creature boar = game.getCreatureById(boarId);

        if (boar == null) {
            return false;
        }

        //Am I a boar?
        if(boar.getCreatureType() != CreatureType.BOAR) {
            return false;
        }

        // am I dead?
        if(boar.isDead()){
            return false;
        }

        Creature victim = game.getCreatureById(victimId);

        return (victim != null && boar.getPosition().distanceTo(victim.getPosition()) <= 1);
    }

    @Override
    public void sendCommand(ClientConnection cc) {
        throw new UnsupportedOperationException("You are not allowed to call a BoarAttackEvent!");
    }

    @Override
    public String getText(Client cli) {
        return String.format("Boar(%d) attacked %s!", boarId, LogString.createVictimName(cli.getGame(),victimId));
    }

    public int getDamage() {
        return this.damage;
    }

    public int getBoardId() {
        return this.boarId;
    }

    public int getVictimId() {
        return this.victimId;
    }
}
