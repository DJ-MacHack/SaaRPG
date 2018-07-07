package de.unisaarland.sopra.utilitytest;

/**
 * Created by Wiebk on 12.09.2016.
 */

import de.unisaarland.sopra.Direction;

import de.unisaarland.sopra.utility.DirectionHelper;
import de.unisaarland.sopra.utility.GameVector;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DirectionTests {

    @Test
    public void testtoVector(){
        GameVector w = new GameVector(1,0);
        Direction dir = Direction.EAST;
        Assert.assertEquals(DirectionHelper.toVector(dir), w);
    }

    @Test
    public void directionLength1TestNW() {
        GameVector v = new GameVector(0,0);
        GameVector w = v.add(DirectionHelper.toVector(Direction.NORTH_WEST));
        assertEquals(1, v.distanceTo(w));
    }

    @Test
    public void directionLength1TestNE() {
        GameVector v = new GameVector(42,0);
        GameVector w = v.add(DirectionHelper.toVector(Direction.NORTH_EAST));
        assertEquals(1, v.distanceTo(w));
    }

    @Test
    public void directionLength1TestE() {
        GameVector v = new GameVector(21,12);
        GameVector w = v.add(DirectionHelper.toVector(Direction.EAST));
        assertEquals(1, v.distanceTo(w));
    }

    @Test
    public void directionLength1TestSE() {
        GameVector v = new GameVector(33,3);
        GameVector w = v.add(DirectionHelper.toVector(Direction.SOUTH_EAST));
        assertEquals(1, v.distanceTo(w));
    }

    @Test
    public void directionLength1TestSW() {
        GameVector v = new GameVector(42,420);
        GameVector w = v.add(DirectionHelper.toVector(Direction.SOUTH_WEST));
        assertEquals(1, v.distanceTo(w));
    }

    @Test
    public void directionLength1TestW() {
        GameVector v = new GameVector(99,4);
        GameVector w = v.add(DirectionHelper.toVector(Direction.WEST));
        assertEquals(1, v.distanceTo(w));
    }

    @Test
    public void testgetCounterClockwise(){
        Direction d = Direction.EAST;
        Direction e = Direction.NORTH_EAST;
        assertEquals(DirectionHelper.getCounterClockwise(d), e);
    }

}
