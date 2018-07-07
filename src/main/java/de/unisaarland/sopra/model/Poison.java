package de.unisaarland.sopra.model;

/**
 * Created by Team14 on 9/12/16.
 * Responsible for stub creation: LUKAS KIRSCHNER
 * The Poison enum specifies the poison state of a {@link Creature}.
 */
public enum Poison {
    /**
     * Initial poison state, does not cause any damage to the {@link Creature}.
     */
    FIRSTROUND,
    /**
     * Heavy poison state, replaces the initial poison state after one round. Causes 4 damage to the {@link Creature}.
     */
    HEAVY,
    /**
     * Minor poison state, replaces the heavy poison state after another round, causes 2 damage to the {@link Creature}.
     */
    MINOR
}
