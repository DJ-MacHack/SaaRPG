package de.unisaarland.sopra.modeltest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.model.Npc;
import de.unisaarland.sopra.utility.GameVector;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for Npc
 * Created on 12.09.16.
 *
 * @author Henrik Paul Koehn
 */
public class NpcTest {


    private Npc setupNpcBoar() {
        return new Npc(10, new GameVector(1, 2), 20, CreatureType.BOAR, 6, 20);
    }

    private Npc setupNpcFairy() {
        return new Npc(12, new GameVector(4, 5), 100, CreatureType.FAIRY, 4, 10);
    }

    /**
     * Test if Pc creature NPCs cannot be created
     */
    /*
    @Test(expected = IllegalArgumentException.class)
    public void canPcNpc() {
        Npc nsc = new Npc(10, new GameVector(1, 2), 20, CreatureType.WRAITH, 6, 10);
        Npc npc = new Npc(12, new GameVector(4, 5), 100, CreatureType.ROOK, 6, 10);
    }

    /**
     * Test for bad parameter
     */
    /*
    @Test(expected = IllegalArgumentException.class)
    public void testBadParam() {
        // check if cannot create negative max step
        Npc nsc = new Npc(10, new GameVector(1, 2), 20, CreatureType.BOAR, -5, 10);
        // check if cannot create negative max anchor point distance
        Npc npc = new Npc(12, new GameVector(4, 5), 100, CreatureType.FAIRY, 6, -10);
    }

    /**
     * Test if data is set correctly
     */
    @Test
    public void checkInitData() {
        Npc boar = setupNpcBoar();
        Npc fairy = setupNpcFairy();

        // Check if start position and anchorpoint are identical
        assertTrue(boar.getAnchorPoint().equals(boar.getPosition()));
        assertTrue(fairy.getAnchorPoint().equals(fairy.getPosition()));

        // Check if start Direction is EAST
        assertEquals(boar.getCurrentDirection(), Direction.EAST);
        assertEquals(fairy.getCurrentDirection(), Direction.EAST);

        // Check if last direction is null
        assertNull(boar.getLastDirection());
        assertNull(fairy.getLastDirection());

        // Check if max steps set correctly
        assertEquals(boar.getMaxSteps(), 6);
        assertEquals(fairy.getMaxSteps(), 4);

        // Check if anchor point distance is set correctly
        assertEquals(boar.getMaxAnchorPointDistance(), 20);
        assertEquals(fairy.getMaxAnchorPointDistance(), 10);

    }

    /**
     * Tests equals method
     */
    /*
    @Test
    public void testEquals() {
        Npc npc1 = new Npc(1, new GameVector(1, 1), 10, CreatureType.FAIRY, 4, 10);
        Npc npc2 = new Npc(1, new GameVector(1, 1), 10, CreatureType.FAIRY, 4, 10);
        assertTrue(npc1.equals(npc2));
        assertEquals(npc1.hashCode(),npc2.hashCode());
        Npc npcTrolf = new Npc(npc1);
        assertEquals(npc1.hashCode(),npcTrolf.hashCode());

        // Test different anchorpoint
        Npc npc3 = new Npc(1, new GameVector(2, 2), 10, CreatureType.FAIRY, 4, 10);
        npc3.setPosition(new GameVector(1, 1));
        assertFalse(npc1.equals(npc3));

        // Test hp
        npc1.receiveDamage(1);
        assertFalse(npc1.equals(npc2));
        npc1.receiveDamage(-1);

        // Test position
        npc1.setPosition(new GameVector(2, 2));
        assertFalse(npc1.equals(npc2));
        npc1.setPosition(new GameVector(1, 1));

        // Test creature type
        Npc npc5 = new Npc(1, new GameVector(1, 1), 10, CreatureType.BOAR, 6, 20);
        assertFalse(npc1.equals(npc5));

        // Test if ids are checked
        Npc npc6 = new Npc(2, new GameVector(1, 1), 10, CreatureType.FAIRY, 4, 10);
        assertFalse(npc2.equals(npc6));
    }
    */

    @Test
    public void testNpcRoundCounterIncr(){
        Npc npc1 = setupNpcBoar();
        int n1 = npc1.getRoundsDead();
        npc1.increaseRoundsDead();
        assertEquals(n1 + 1, npc1.getRoundsDead());
    }

}
