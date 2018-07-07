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
public class Cast extends MagicAttack {
    private static final int DAMAGE = 10;
    private static final int RANGE = 5;
    private static final int ENERGYCOST = 450;
    private static final CreatureType[] VALIDCREATURES = {CreatureType.ROOK};

    public Cast(CommIds commIds, int actorId, int x, int y) {
        super(commIds, actorId, x, y);
    }

    /**
     *
     * @param game
     */
    @Override
    public void executeCommand(Game game) {
        attack(game);
        if (super.targetDied && (game.getCreatureById(super.targetId).getCreatureType() != CreatureType.BOAR))
            game.removeCreature(game.getCreatureById(super.targetId));
    }

    /**
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
     *
     * @param sc
     */
    @Override
    public void sendResults(ServerConnection sc) {
        for (int id : commIds.getCommLibIds()) {
            sc.sendCast(id, super.actorId, super.xCoord, super.yCoord);
            if (super.targetDied)
                sc.sendDied(id, super.targetId);
        }
    }

    /**
     *
     * @param cc
     */
    @Override
    public void sendCommand(ClientConnection cc) {
        cc.sendCast(super.xCoord,super.yCoord);
    }

    /**
     *
     * @param client
     */
    @Override
    public void executeEvent(Game game, Client client) {
        attack(game);
    }

    /**
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
        return String.format("%s casts against %s!", LogString.createVictimName(cli.getGame(),this.actorId),
                LogString.createVictimName(cli.getGame(),this.xCoord,this.yCoord));
    }

    /**
     *
     * @param game
     */

    private void attack(Game game) {
        Pc attacker = game.getMonsterById(this.actorId);
        GameVector targetpos = new GameVector(super.xCoord, super.yCoord);

        // enough energy
        int energycost = (int) (this.ENERGYCOST * FieldEffects.getAttackCostMultiplier(game.getFieldAt(attacker.getPosition()), attacker.getCreatureType()));
        attacker.drainEnergy(energycost);

        Creature target = game.getCreatureByPosition(targetpos);
        int damage = (int) (this.DAMAGE * FieldEffects.getDefensiveMultiplier(game.getFieldAt(targetpos), target.getCreatureType()));
        target.receiveDamage(damage);

        super.targetId = target.getId();
        if (target.isDead()) {
            super.targetDied = true;
        }
    }

    private boolean canAttack(Game game) {
        Pc attacker = game.getMonsterById(this.actorId);

        if(attacker == null) {
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

        GameVector targetPos = new GameVector(super.xCoord, super.yCoord);
        int distance = attacker.getPosition().distanceTo(targetPos);

        int range = this.RANGE + FieldEffects.getRangeIncrease(game.getFieldAt(attacker.getPosition()), attacker.getCreatureType());

        if(distance > range) {
            return false;
        }

        //enough energy
        int energycost = this.ENERGYCOST * FieldEffects.getAttackCostMultiplier(game.getFieldAt(attacker.getPosition()), attacker.getCreatureType());
        if (attacker.getEnergy() < energycost) {
            return false;
        }

        Creature target = game.getCreatureByPosition(targetPos);
        if (target == null || target.isDead()) {                     //is there an enemy?
            return false;
        }

        if (!FieldEffects.canAttack(game.getFieldAt(attacker.getPosition()), attacker.getCreatureType())) {       // can attacktest from field
            return false;
        }

        return true;
    }
}
