package de.unisaarland.sopra.modeltest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.model.Pc;
import de.unisaarland.sopra.model.Poison;
import de.unisaarland.sopra.utility.GameVector;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for Pc and creature
 * Created on 12.09.16.
 *
 * @author Henrik Paul Koehn
 */
public class PcTest {

    private Pc setupPcYeti() {
        return new Pc(1, new GameVector(1, 1), 200, CreatureType.YETI);
    }

    private Pc setupPcNaga() {
        return new Pc(2, new GameVector(2, 2), 100, CreatureType.NAGA);
    }

    /**
     * Check if it is possible to create an Fairy or Boar Pc
     */
    /*
    @Test(expected = IllegalArgumentException.class)
    public void testBoarFairy() {

        // Shall throw exception
        assertNotNull(new Pc(1, new GameVector(1, 1), 20, CreatureType.BOAR));
        assertNotNull(new Pc(2, new GameVector(2, 2), 100, CreatureType.ELF));
    }

    /**
     * Check if it is possible to create a creature with negative hp or id
     */
    /*
    @Test(expected = IllegalArgumentException.class)
    public void testBadParam() {

        // Shall throw exception
        assertNotNull(new Pc(-3, new GameVector(3, 1), 40, CreatureType.ELF));
        assertNotNull(new Pc(4, new GameVector(3, 1), -30, CreatureType.IFRIT));
    }
    */

    /**
     * Check if Data was set correctly
     */
    @Test
    public void checkInitData() {
        Pc yeti = setupPcYeti();
        Pc naga = setupPcNaga();

        // Positions
        assertTrue(yeti.getPosition().equals(new GameVector(1, 1)));
        assertTrue(naga.getPosition().equals(new GameVector(2, 2)));

        // health
        assertEquals(yeti.getHp(), 200);
        assertEquals(naga.getHp(), 100);

        // Creature Type
        assertEquals(yeti.getCreatureType(), CreatureType.YETI);
        assertEquals(naga.getCreatureType(), CreatureType.NAGA);

        // IDs
        assertEquals(yeti.getId(), 1);
        assertEquals(naga.getId(), 2);

        // Energy
        assertEquals(yeti.getEnergy(), 1000);
        assertEquals(naga.getEnergy(), 1000);

        // WarCry
        assertFalse(yeti.getCriedInRound());
        assertFalse(naga.getCriedInRound());

        // Fairness
        assertEquals(yeti.getFairness(), 0);
        assertEquals(naga.getFairness(), 0);

        // Poison List
        assertTrue(yeti.getPoisons().isEmpty());
        assertTrue(naga.getPoisons().isEmpty());

    }

    /**
     * Test setter
     */
    /*
    @Test
    public void testSett() {
        Pc yeti = setupPcYeti();
        Pc naga = setupPcNaga();

        // check for if damage received correctly
        yeti.receiveDamage(15);
        assertEquals(yeti.getHp(), 185);
        naga.receiveDamage(110);
        assertEquals(naga.getHp(), 0);

        // healz
        yeti.receiveDamage(-20);
        assertEquals(yeti.getHp(), 200);
        naga.receiveDamage(-10);
        assertEquals(naga.getHp(), 10);

        // cry setter
        yeti.setCriedInRound(true);
        assertTrue(yeti.getCriedInRound());

        // fairness
        naga.setFairness(42);
        assertEquals(naga.getFairness(), 42);

        //Energy
        naga.setEnergy(5);
        assertEquals(naga.getEnergy(), 5);
        naga.drainEnergy(30);
        assertEquals(naga.getEnergy(), 0);
        naga.drainEnergy(-1000);
        assertEquals(naga.getEnergy(), 1000);

        //Last Direction
        assertNull(naga.getLastDirection());
        naga.setLastDirection(Direction.NORTH_WEST);
        assertEquals(naga.getLastDirection(), Direction.NORTH_WEST);

        // dead
        yeti.receiveDamage(300);
        assertTrue(yeti.isDead());
        assertFalse(naga.isDead());

        // Add Poison
        naga.addPoison();
        assertFalse(naga.getPoisons().isEmpty());
        List<Poison> poison = new LinkedList<>();
        poison.add(Poison.FIRSTROUND);
        assertEquals(poison.get(0), naga.getPoisons().get(0));
    }
    */

    /**
     * Tests equals method
     */
    /*
    @Test
    public void testEquals() {
        Pc pc1 = new Pc(2, new GameVector(2, 2), 100, CreatureType.NAGA);
        Pc pc2 = setupPcNaga();

        assertTrue(pc1.equals(pc2));
        assertEquals(pc1.hashCode(), pc2.hashCode());

        pc2.setPosition(new GameVector(1, 1));

        // GameVector not equal
        assertFalse(pc1.equals(pc2));

        pc2.setPosition(new GameVector(2, 2));
        pc1.receiveDamage(1);

        // Hp not equal
        assertFalse(pc1.equals(pc2));

        pc1.receiveDamage(-1);
        pc1.drainEnergy(1);

        //Energy not equal
        assertFalse(pc1.equals(pc2));

        pc1.drainEnergy(-1);
        pc1.setFairness(1);
        pc2.setFairness(2);

        //Fairness not equal
        assertFalse(pc1.equals(pc2));

        // Check if id is checked
        Pc pc3 = new Pc(3, new GameVector(4, 4), 10, CreatureType.ELF);
        Pc pc4 = new Pc(4, new GameVector(4, 4), 10, CreatureType.ELF);
        assertFalse(pc3.equals(pc4));

        // Check if creature type is checked
        Pc pc5 = new Pc(3, new GameVector(4, 4), 10, CreatureType.KOBOLD);
        assertFalse(pc3.equals(pc5));
    }

    /*
    @Test
    public void testPcIllegalArgumentsWhileCreating() {
        for (CreatureType t : CreatureType.values()) {
            if (t == CreatureType.BOAR || t == CreatureType.FAIRY) {
                continue;
            }
            try {
                Pc fail = new Pc(9, new GameVector(4, 4), 1000, t);
                assertTrue("Error: Could initialize a Pc with too much Hp without an exception!", false);
            } catch (IllegalArgumentException e) {
                assertTrue(true);
            }
        }
        try {
            Pc fail = new Pc(9, new GameVector(4, 4), -1, CreatureType.ELF);
            assertTrue("Error: Could initialize a Pc with negative Hp without an exception!", false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            Pc fail = new Pc(9, new GameVector(4, 4), 100, CreatureType.FAIRY);
            assertTrue("Error: Could initialize a FAIRY Pc without an exception!", false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
    */
}
