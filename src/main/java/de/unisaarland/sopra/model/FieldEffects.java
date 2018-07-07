package de.unisaarland.sopra.model;

/**
 * Created by DJ MacHack on 12.09.2016.
 */
public final class FieldEffects {

    // Damage modifications
    private static final int DAMAGE_LAVA = 25;
    private static final int DAMAGE_HEAL = -20;
    private static final int DAMAGE_WATER = 20;

    // Defense modifications
    private static final double DEFENSE_KOBOLD_BUSH = 0.5;
    private static final double DEFENSE_TREE = 0.7;

    // Range modifications
    private static final int RANGE_ELF_TREE = 4;
    private static final int RANGE_ROOK_HILL = 3;

    // Movement cost modifications
    private static final int MOVE_WATER = 4;
    private static final int MOVE_SWAMP = 3;
    private static final int MOVE_NAGA_WATER = 2;
    private static final int MOVE_NAGA_SWAMP = 1;

    // Attack cost modifications
    private static final int ATTACK_TREE = 2;
    private static final int ATTACK_CURSE = 3;

    private FieldEffects() {
    }

    /**
     * calculates damage on field
     *
     * @param field
     * @param type
     * @return
     */
    public static int getDamage(Field field, CreatureType type) {

        if (field == Field.LAVA) {
            if (type == CreatureType.IFRIT) {
                return 0;
            } else {
                return DAMAGE_LAVA;
            }
        }
        if (field == Field.HEAL) {
            return DAMAGE_HEAL;
        }

        if (field == Field.WATER) {
            return DAMAGE_WATER;
        }
        return 0;
    }

    /**
     * get a multiplier on field for damage
     *
     * @param field
     * @param type
     * @return
     */
    public static double getDefensiveMultiplier(Field field, CreatureType type) {

        if (field == Field.BUSH && type == CreatureType.KOBOLD) {
            return DEFENSE_KOBOLD_BUSH;
        }
        if (field == Field.TREE) {
            return DEFENSE_TREE;
        } else {
            return 1.0;
        }
    }

    /**
     * get range factor on field
     *
     * @param field
     * @param type
     * @return
     */
    public static int getRangeIncrease(Field field, CreatureType type) {
        if (field == Field.TREE && type == CreatureType.ELF) {
            return RANGE_ELF_TREE;
        }
        if (field == Field.HILL && type == CreatureType.ROOK) {
            return RANGE_ROOK_HILL;
        }
        return 0;
    }

    /**
     * tests if a creature can enter a field
     *
     * @param field
     * @param type
     * @return
     */
    public static boolean canEnter(Field field, CreatureType type) {
        if(field == Field.WATER && (type == CreatureType.BOAR || type == CreatureType.FAIRY)){
            return false;
        }

        return (field != Field.ROCK) && (field != null);
    }

    /**
     * tests if a creature can attacktest on a field
     *
     * @param field
     * @param type
     * @return
     */
    public static boolean canAttack(Field field, CreatureType type) {
        return !(field == Field.WATER);
    }

    /**
     * get movement cost multiplier on field
     *
     * @param field
     * @param type
     * @return
     */
    public static int getMovementCostMultiplier(Field field, CreatureType type) {
        if (field == Field.WATER) {
            if (type == CreatureType.NAGA) {
                return MOVE_NAGA_WATER;
            } else {
                return MOVE_WATER;
            }
        }
        if (field == Field.SWAMP) {
            if (type == CreatureType.NAGA) {
                return MOVE_NAGA_SWAMP;
            } else {
                return MOVE_SWAMP;
            }
        }
        return 1;
    }

    /**
     * test if projectiles get blocked
     *
     * @param field
     * @return
     */
    public static boolean blockProjectile(Field field) {
        return (field == Field.ROCK);
    }

    /**
     * get attacktest cost multiplier
     *
     * @param field
     * @param type
     * @return
     */
    public static int getAttackCostMultiplier(Field field, CreatureType type) {

        if (field == Field.TREE) {
            return ATTACK_TREE;
        }
        if (field == Field.CURSE) {
            if (type == CreatureType.WRAITH) {
                return 1;
            } else {
                return ATTACK_CURSE;
            }
        } else {
            return 1;
        }
    }
}
