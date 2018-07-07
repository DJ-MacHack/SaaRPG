package de.unisaarland.sopra.modeltest;

import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.model.Player;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Team14 on 9/12/16.
 * Responsible for Player JUnit tests: Lukas Kirschner
 */
public class PlayerTests {

    @Test
    public void testGetters(){
        Player myPlayer = new Player("JokoWinterscheidt","TeamJoko", CreatureType.KOBOLD);
        assertEquals("JokoWinterscheidt", myPlayer.getName());
        assertEquals("TeamJoko", myPlayer.getTeamName());
        assertEquals(CreatureType.KOBOLD, myPlayer.getMonsterType());
        myPlayer.setId(1337);
        assertEquals(1337, myPlayer.getId());
        assertFalse(myPlayer.getRegistered());
        myPlayer.setRegistered(true);
        assertTrue(myPlayer.getRegistered());
    }


}
