package de.unisaarland.sopra.model;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.utility.GameVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Team14 on 9/12/16.
 * Responsible for stub creation and implementation: LUKAS KIRSCHNER
 */
public class Creature {

    protected int id;
    protected GameVector position;
    protected int hp;
    protected CreatureType type;
    protected List<Poison> poisons;
    protected Direction lastDirection;
    private int maxHP;

    /**
     * Creates a new instance of {@link Creature} with given parameters.
     *
     * @param id  {@link Creature} ID
     * @param pos {@link Creature} position
     * @param hp  Health points
     * @param typ {@link Creature} type
     */
    protected Creature(int id, GameVector pos, int hp, CreatureType typ) {
        this.id = id;
        this.position = pos;
        this.hp = hp;
        this.type = typ;
        this.poisons = new ArrayList<Poison>();
        this.lastDirection = null;
        this.maxHP = hp;
    }

    /**
     * Creates a deep copy of the given {@link Creature}
     *
     * @param toCopy {@link Creature} to copy
     */
    protected Creature(Creature toCopy) {
        this.id = toCopy.id;
        this.position = toCopy.getPosition();
        this.hp = toCopy.hp;
        this.type = toCopy.type;
        this.poisons = new ArrayList<Poison>();
        this.poisons.addAll(toCopy.poisons);
        this.lastDirection = toCopy.lastDirection;
        this.maxHP = toCopy.maxHP;
    }

    /**
     * gets the {@link Creature} ID
     *
     * @return {@link Creature} ID
     */
    public int getId() {

        return this.id;
    }

    /**
     * gets the {@link Creature}'s position
     *
     * @return {@link Creature}'s position as {@link GameVector}
     */
    public GameVector getPosition() {

        return this.position;
    }

    /**
     * Sets the position of the {@link Creature} to a given {@link GameVector}
     *
     * @param pos the new position of the {@link Creature}
     */
    public void setPosition(GameVector pos) {

        this.position = pos;
    }

    /**
     * gets the health points of the {@link Creature}
     *
     * @return Health points
     */
    public int getHp() {

        return this.hp;
    }

    /**
     * Damages the {@link Creature} for given amount of health points
     *
     * @param dam Damage points to receive
     */
    public void receiveDamage(int dam) {

        if (dam > this.hp) {
            this.hp = 0;
        } else {
            if (this.hp - dam > this.getMaxHp()) {
                this.hp = this.getMaxHp();
            } else {
                this.hp -= dam;
            }
        }
    }

    /**
     * Gets the {@link Creature} type
     *
     * @return Creature type
     */
    public CreatureType getCreatureType() {

        return this.type;
    }

    /**
     * Gets the {@link Poison} list of the {@link Creature}
     *
     * @return {@link Poison} list
     */
    public List<Poison> getPoisons() {

        return this.poisons;
    }

    /**
     * Sets the {@link Poison} list
     *
     * @param pois New {@link Poison} list
     */
    public void setPoisons(List<Poison> pois) {

        this.poisons = pois;
    }

    /**
     * Adds a poison to the {@link Creature}'s poison list
     */
    public void addPoison() {

        this.poisons.add(Poison.FIRSTROUND);
    }

    /**
     * Gets the death status of the {@link Creature}
     *
     * @return true, if {@link Creature} is dead
     */
    public boolean isDead() {

        return (this.hp <= 0);
    }

    /**
     * gets the last {@link Direction} of the {@link Creature}
     *
     * @return last {@link Direction}
     */
    public Direction getLastDirection() {

        return this.lastDirection;
    }

    /**
     * sets the last {@link Direction} of the {@link Creature} to a given Direction
     *
     * @param dir {@link Direction} to set
     */
    public void setLastDirection(Direction dir) {

        this.lastDirection = dir;
    }

    /**
     * gets the maximum amount of HP the creature can have
     *
     * @return maxHP
     */
    public int getMaxHp() {
        return this.maxHP;
    }
}
