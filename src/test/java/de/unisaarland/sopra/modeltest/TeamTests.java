package de.unisaarland.sopra.modeltest;

import de.unisaarland.sopra.model.Team;
import de.unisaarland.sopra.utility.GameVector;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Team14 on 9/12/16.
 * Responsible for Player JUnit tests: Lukas Kirschner
 */
public class TeamTests {

    @Test
    public void testGetters(){
        Team myTeam = new Team("OUTATEAM");
        assertEquals("OUTATEAM", myTeam.getName());
        GameVector testStart = new GameVector(69,1337);
        myTeam.addStartPosition(testStart);
        assertTrue(testStart.equals(myTeam.nextStartPosition()));
    }

    @Test
    public void testStartPositionQueue(){
        Team myTeam = new Team("DavidTennantIsBetterThanMattSmith");
        GameVector vec1 = new GameVector(1,2);
        GameVector vec2 = new GameVector(3,4);
        myTeam.addStartPosition(vec1);
        myTeam.addStartPosition(vec2);
        assertTrue(vec1.equals(myTeam.nextStartPosition()));
        assertTrue(vec2.equals(myTeam.nextStartPosition()));
    }
}
