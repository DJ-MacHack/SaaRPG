package de.unisaarland.sopra.model;

import de.unisaarland.sopra.utility.GameVector;

/**
 * Created by Team14 on 12/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class Pc extends Creature {

    private static final int MAGICPRIM = 31;

    // HP data
    private static final int MAX_HP_100 = 100;
    private static final int MAX_HP_IFRIT = 87;
    private static final int MAX_HP_YETI = 200;

    // Energy
    private static final int MAX_ENERGY = 1000;

    // Fields
    private int energy;
    private boolean criedInRound;
    private int fairness;

    /**
     * creates an instance of {@link Pc} with given attributes
     *
     * @param id           {@link Pc} monsterId
     * @param position     {@link Pc} position as a {@link GameVector}
     * @param hp           {@link Pc} health points
     * @param creatureType {@link Pc} type of Creature {@link CreatureType}
     */
    public Pc(int id, GameVector position, int hp, CreatureType creatureType) throws IllegalArgumentException {
        super(id, position, hp, creatureType);

        /*
        if (id < 0 || hp < 0) {
            throw new IllegalArgumentException();
        }

        if (creatureType == CreatureType.BOAR || creatureType == CreatureType.FAIRY) {
            throw new IllegalArgumentException();
        } else {
            if (creatureType == CreatureType.WRAITH && hp > MAX_HP_100) {
                throw new IllegalArgumentException();
            } else {
                if (creatureType == CreatureType.ELF && hp > MAX_HP_100) {
                    throw new IllegalArgumentException();
                } else {
                    if (creatureType == CreatureType.IFRIT && hp > MAX_HP_IFRIT) {
                        throw new IllegalArgumentException();
                    } else {
                        if (creatureType == CreatureType.KOBOLD && hp > MAX_HP_100) {
                            throw new IllegalArgumentException();
                        } else {
                            if (creatureType == CreatureType.NAGA && hp > MAX_HP_100) {
                                throw new IllegalArgumentException();
                            } else {
                                if (creatureType == CreatureType.ROOK && hp > MAX_HP_100) {
                                    throw new IllegalArgumentException();
                                } else {
                                    if (creatureType == CreatureType.YETI && hp > MAX_HP_YETI) {
                                        throw new IllegalArgumentException();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        */
        this.energy = MAX_ENERGY;
    }

    /*
    @Override
    public int hashCode() {
        int result = energy;



        result = MAGICPRIM * result + (criedInRound ? 1 : 0);
        result = MAGICPRIM * result + fairness;
        result = MAGICPRIM * result + super.hp;
        result = MAGICPRIM * result + super.id;
        result = MAGICPRIM * result + super.getMaxHp();
        result = MAGICPRIM * result + super.getPosition().hashCode();
        result = MAGICPRIM * result + (super.isDead() ? 1 : 0);
        result = MAGICPRIM * result + super.getPoisons().hashCode();
        result = MAGICPRIM * result + super.getCreatureType().hashCode();
        result = MAGICPRIM * result + ((super.getLastDirection() == null) ? 0 : super.getLastDirection().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pc pc = (Pc) o;

        return energy == pc.energy
                && criedInRound == pc.criedInRound
                && super.getId() == pc.getId()
                && super.getHp() == pc.getHp()
                && super.getMaxHp() == pc.getMaxHp()
                && super.getPosition().equals(pc.getPosition())
                && super.isDead() == pc.isDead()
                && super.getPoisons().equals(pc.getPoisons())
                && super.getCreatureType() == pc.getCreatureType()
                && !(super.lastDirection != null
                && super.getLastDirection() != pc.getLastDirection())
                && fairness == pc.fairness;
    }
    */

    /**
     * copy constructor of {@link Pc}
     *
     * @param pc {@link Pc} instance which is copied
     */
    public Pc(Pc pc) {
        super(pc);
        this.energy = pc.getEnergy();
        this.criedInRound = pc.getCriedInRound();
        this.fairness = pc.getFairness();
    }

    /**
     * gets energy value of {@link Pc}
     *
     * @return {@link Pc} energy
     */
    public int getEnergy() {
        return this.energy;
    }

    /**
     * sets energy value of {@link Pc}
     *
     * @param energy {@link Pc} to set value of energy
     */
    public void setEnergy(int energy) {
        this.energy = energy;
    }

    /**
     * reduces {@link Pc} energy value by given drain
     *
     * @param drain {@link Pc} energy drain
     */
    public void drainEnergy(int drain) {
        //assert(this.energy - drain >= 0);
        //assert(this.energy - drain  <= MAX_ENERGY);
        this.energy = this.energy - drain;
    }

    /**
     * get {@link Pc} fairness value (used for initiative order)
     *
     * @return {@link Pc} fairness value
     */
    public int getFairness() {
        return this.fairness;
    }

    /**
     * sets {@link Pc} fairness value (used for initiative oder)
     *
     * @param fairness {@link Pc} fairness value to set
     */
    public void setFairness(int fairness) {
        this.fairness = fairness;
    }

    /**
     * gets if the {@link Pc} has already used its WarCry in this round
     *
     * @return {@link Pc} if WarCry was already used in this round
     */
    public boolean getCriedInRound() {
        return this.criedInRound;
    }

    /**
     * sets the attribute if {@link Pc} has already used its WarCry in this round
     *
     * @param criedInRound {@link Pc} WarCry used value in this round to set
     */
    public void setCriedInRound(boolean criedInRound) {
        this.criedInRound = criedInRound;
    }
}
