package de.unisaarland.sopra.messages.attack;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.model.*;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.comm.ServerConnection;
import de.unisaarland.sopra.utility.LogString;

/**
 * Created by DJ MacHack on 12.09.2016.
 */
public class Swap extends MagicAttack {

    private static final int DAMAGE = 6;
    private static final int ENERGYCOST = 250;
    private static final CreatureType[] VALIDCREATURES = {CreatureType.WRAITH};

    /**
     * create new swap
     *
     * @param commIds CommID
     * @param actorId PlayerID
     * @param x       x_cord
     * @param y       y_cord
     */
    public Swap(CommIds commIds, int actorId, int x, int y) {
        super(commIds, actorId, x, y);
    }

    /**
     * execute on game
     *
     * @param game
     */
    @Override
    public void executeCommand(Game game) {
        attack(game);
        if (super.targetDied && (game.getCreatureById(super.targetId).getCreatureType() != CreatureType.BOAR)) {
            game.removeCreature(game.getCreatureById(super.targetId));
        }
    }

    /**
     * test on game
     *
     * @param game
     * @return
     */
    @Override
    public boolean validateCommand(Game game) {
        // check if clientGame is in RoundState ActNow (first set by ActNow Event)
        if (game.getRoundState() != RoundState.ACTNOW)
            return false;

        // check if it is acting monster's turn
        if (game.getMonsterActing() != this.actorId) {
            return false;
        }

        return canAttack(game);
    }

    /**
     * send result
     *
     * @param sc
     */
    @Override
    public void sendResults(ServerConnection sc) {
        for (int id : commIds.getCommLibIds()) {
            sc.sendSwapped(id, super.actorId, super.xCoord, super.yCoord);
            if (super.targetDied) {
                sc.sendDied(id, super.targetId);
            }
        }
    }

    /**
     * send command
     *
     * @param cc
     */
    @Override
    public void sendCommand(ClientConnection cc) {
        cc.sendSwap(xCoord, yCoord);
    }

    /**
     * execute on client
     *
     * @param client
     */
    @Override
    public void executeEvent(Game game, Client client) {
        attack(game);
    }

    /**
     * test on client
     *
     * @param client
     * @return
     */
    @Override
    public boolean validateEvent(Game game, Client client) {
        return canAttack(game);
    }

    @Override
    public String getText(Client cli) {
        return String.format("%s swaps with %s!", LogString.createVictimName(cli.getGame(),this.actorId),
                LogString.createVictimName(cli.getGame(),this.xCoord,this.yCoord));
    }

    private void attack(Game game) {
        Pc attacker = game.getMonsterById(this.actorId);
        attacker.setLastDirection(null);
        GameVector targetpos = new GameVector(super.xCoord, super.yCoord);

        int range = attacker.getPosition().distanceTo(targetpos);           //enough Energy
        int energycost = this.ENERGYCOST * range * FieldEffects.getAttackCostMultiplier(game.getFieldAt(attacker.getPosition()), attacker.getCreatureType());
        attacker.drainEnergy(energycost);

        Creature target = game.getCreatureByPosition(targetpos);
        int damage = (int) (this.DAMAGE * FieldEffects.getDefensiveMultiplier(game.getFieldAt(targetpos), target.getCreatureType()));
        target.receiveDamage(damage);

        target.setPosition(attacker.getPosition());
        attacker.setPosition(targetpos);

        super.targetId = target.getId();
        if (target.isDead()) {
            super.targetDied = true;
        }
    }

    private boolean canAttack(Game game) {
        Pc attacker = game.getMonsterById(this.actorId);

        if(attacker == null){
            return false;
        }

        if (attacker.isDead()) {
            return false;
        }

        boolean allowed = false;
        for (CreatureType t : this.VALIDCREATURES) {
            allowed = allowed || (attacker.getCreatureType() == t);
        }

        if (!allowed) {
            return false; //Check if me is a valid creature
        }

        GameVector targetpos = new GameVector(super.xCoord, super.yCoord);
        int range = attacker.getPosition().distanceTo(targetpos);           //enough energy
        int energycost = this.ENERGYCOST * range * FieldEffects.getAttackCostMultiplier(game.getFieldAt(attacker.getPosition()), attacker.getCreatureType());
        if (attacker.getEnergy() < energycost){
            return false;
        }

        Creature target = game.getCreatureByPosition(targetpos);
        if (target == null || target.isDead()){                  //is there an enemy?
            return false;
        }

        if (!FieldEffects.canAttack(game.getFieldAt(attacker.getPosition()), attacker.getCreatureType())) {      // can attacktest from field
            return false;
        }

        return true;
    }
}
