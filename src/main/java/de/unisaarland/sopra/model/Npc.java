package de.unisaarland.sopra.model;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.utility.GameVector;

/**
 * Created by Team14 on 12/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class Npc extends Creature {

    private static final int HASH_PRIME = 31;

    // Boar data
    private static final int BOAR_HP = 20;
    private static final int BOAR_ANCHOR_DIS = 20;
    private static final int BOAR_MAX_STEPS = 6;

    // Fairy data
    private static final int FAIRY_HP = 100;
    private static final int FAIRY_ANCHOR_DIS = 10;
    private static final int FAIRY_MAX_STEPS = 4;

    // Fields
    private GameVector anchorPoint;
    private Direction currentDirection;
    private int maxSteps;
    private int maxAnchorPointDistance;
    private int roundsDead;

    /**
     * creates a new instance of {@link Npc} representing a neutral creature (fairy/ boar)
     *
     * @param id                     id of the creature
     * @param position               {@link GameVector} position of the creature on the map
     * @param hp                     health points of the creature
     * @param creatureType           {@link CreatureType} type of the creature
     * @param maxSteps               number of movements this Npc does in its turn
     * @param maxAnchorPointDistance {@link GameVector} maximal distance the Npc can move away from its anchorPoint. This can not be exceeded in its own movement!
     */
    public Npc(int id, GameVector position, int hp, CreatureType creatureType, int maxSteps, int maxAnchorPointDistance) throws IllegalArgumentException {
        super(id, position, hp, creatureType);

        /*
        if (id < 0 || hp < 0) {
            throw new IllegalArgumentException();
        }
        if (creatureType != CreatureType.BOAR && creatureType != CreatureType.FAIRY) {
            throw new IllegalArgumentException();
        }
        if (creatureType == CreatureType.BOAR
                && (hp > BOAR_HP || maxSteps != BOAR_MAX_STEPS || maxAnchorPointDistance != BOAR_ANCHOR_DIS)) {
            throw new IllegalArgumentException();
        }

        if (creatureType == CreatureType.FAIRY
                && (hp > FAIRY_HP || maxSteps != FAIRY_MAX_STEPS || maxAnchorPointDistance != FAIRY_ANCHOR_DIS)) {
            throw new IllegalArgumentException();
        }
        */
        this.anchorPoint = position;
        this.maxSteps = maxSteps;
        this.maxAnchorPointDistance = maxAnchorPointDistance;
        this.currentDirection = Direction.EAST;
    }

    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        Npc npc = (Npc) o;

        if (maxSteps != npc.maxSteps) {
            return false;
        }
        if (maxAnchorPointDistance != npc.maxAnchorPointDistance) {
            return false;
        }
        if (roundsDead != npc.roundsDead) {
            return false;
        }
        if (anchorPoint != null ? !anchorPoint.equals(npc.anchorPoint) : npc.anchorPoint != null) {
            return false;
        }
        if (super.getId() != npc.getId()) {
            return false;
        }
        if (super.getHp() != npc.getHp()) {
            return false;
        }
        if (super.getMaxHp() != npc.getMaxHp()) {
            return false;
        }
        if (!super.getPosition().equals(npc.getPosition())) {
            return false;
        }
        if (super.isDead() != npc.isDead()) {
            return false;
        }
        if (!super.getPoisons().equals(npc.getPoisons())) {
            return false;
        }
        if (super.getCreatureType() != npc.getCreatureType()) {
            return false;
        }
        if (super.getLastDirection() != npc.getLastDirection()) {
            return false;
        }
        return currentDirection == npc.currentDirection;

    }

    @Override
    public int hashCode() {
        int result = anchorPoint != null ? anchorPoint.hashCode() : 0;
        result = HASH_PRIME * result + (currentDirection != null ? currentDirection.hashCode() : 0);
        result = HASH_PRIME * result + maxSteps;
        result = HASH_PRIME * result + maxAnchorPointDistance;
        result = HASH_PRIME * result + roundsDead;

        result = HASH_PRIME * result + super.hp;
        result = HASH_PRIME * result + super.id;
        result = HASH_PRIME * result + super.getMaxHp();
        result = HASH_PRIME * result + super.getPosition().hashCode();
        result = HASH_PRIME * result + (super.isDead() ? 1 : 0);
        result = HASH_PRIME * result + super.getPoisons().hashCode();
        result = HASH_PRIME * result + super.getCreatureType().hashCode();
        if (this.lastDirection != null) {
            result = HASH_PRIME * result + super.getLastDirection().hashCode();
        }
        return result;
    }
    */

    /**
     * copy constructor of Npc
     *
     * @param npc Npc which is copied
     */
    public Npc(Npc npc) {
        super(npc);
        this.maxSteps = npc.getMaxSteps();
        this.maxAnchorPointDistance = npc.getMaxAnchorPointDistance();
        this.roundsDead = npc.getRoundsDead();
        this.anchorPoint = npc.getAnchorPoint();
        this.currentDirection = npc.getCurrentDirection();
    }

    /**
     * gets the {@link Npc} anchorPoint as a {@link GameVector}
     *
     * @return {@link Npc} anchorPoint
     */
    public GameVector getAnchorPoint() {
        return this.anchorPoint;
    }

    /**
     * gets current movement {@link Direction} of the {@link Npc}
     *
     * @return {@link Npc} current movement {@link Direction}
     */
    public Direction getCurrentDirection() {
        return this.currentDirection;
    }

    /**
     * gets max amount of movements the {@link Npc} does in its turn
     *
     * @return {@link Npc} max amount of steps
     */
    public int getMaxSteps() {
        return this.maxSteps;
    }

    /**
     * gets max possible distance of {@link Npc} to its anchorPoint
     *
     * @return {@link Npc} max distance
     */
    public int getMaxAnchorPointDistance() {
        return this.maxAnchorPointDistance;
    }

    public int getRoundsDead() {
        return this.roundsDead;
    }

    public void increaseRoundsDead() {
        this.roundsDead += 1;
    }

    public void setRoundsDead(int r){
        this.roundsDead = r;
    }

    public void setCurrentDirection(Direction direction) {
        this.currentDirection = direction;
    }
}