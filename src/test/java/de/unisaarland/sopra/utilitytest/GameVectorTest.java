package de.unisaarland.sopra.utilitytest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.utility.DirectionHelper;
import de.unisaarland.sopra.utility.GameVector;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Team 14 on 12.09.2016.
 */
public class GameVectorTest {

    @Test
    public void testEqualsTrue(){
        GameVector v = new GameVector(2,2);
        GameVector w = new GameVector(2,2);
        assertEquals(v.equals(w), true);
    }

    @Test
    public void testEqualsFalse(){
        GameVector v = new GameVector(2,2);
        GameVector w = new GameVector(2,3);
        assertEquals(v.equals(w), false);
    }

    @Test
    public void testAdd(){
        GameVector v = new GameVector(1,1);
        GameVector w = new GameVector(1,1);
        GameVector z = new GameVector(2,2);
        assertEquals(z, v.add(w));
    }

    @Test
    public void testDistanceTo1(){
        GameVector v = new GameVector(2,2);
        GameVector w = new GameVector(2,3);
        assertEquals(1, v.distanceTo(w));
    }

    @Test
    public void testDistanceTo2() {
        GameVector v = new GameVector(0,0);
        GameVector w = new GameVector(9,0);
        assertEquals(9, v.distanceTo(w));
    }

    @Test
    public void testDistanceTo3() {
        GameVector v = new GameVector(0,0);
        GameVector w = new GameVector(0,42);
        assertEquals(42, v.distanceTo(w));
    }

    @Test
    public void testDistanceTo4() {
        GameVector v = new GameVector(0,0);
        GameVector w = new GameVector(40,8);
        assertEquals(48, v.distanceTo(w));
    }

    @Test
    public void testDistanceTo5() {
        GameVector v = new GameVector(8,5);
        GameVector w = new GameVector(-5,15);
        assertEquals(13, v.distanceTo(w));
    }
    /*
    @Test
    public void testToDirection(){
        GameVector v = new GameVector(-1,0);
        assertEquals(v.toDirection(), Direction.WEST);
    }*/

    @Test
    public void testAddVectorDirection() {
        GameVector v = new GameVector(0,1);
        v = v.add(DirectionHelper.toVector(Direction.NORTH_WEST));
        assertEquals(v, new GameVector(0,0));
    }

    @Test
    public void testAddVectorDirection2() {
        GameVector v = new GameVector(-1,2);
        v = v.add(DirectionHelper.toVector(Direction.NORTH_EAST));
        assertEquals(v, new GameVector(0,1));
    }
}
