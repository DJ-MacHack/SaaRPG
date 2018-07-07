package de.unisaarland.sopra.messagestest;

import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.messages.Register;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.model.Player;
import de.unisaarland.sopra.model.RoundState;
import de.unisaarland.sopra.utility.InvalidFightFileException;
import de.unisaarland.sopra.utility.InvalidMapFileException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Karla on 12.09.2016.
 */
public class RegisterTest {

    private final static String fightFile = "Rot, Blau\n"
                                    + "Rot, Christopher, Elf\n"
                                    + "Rot, Lukas, Kobold\n" + "\n"
                                    + "Blau, Henrik, RooK";

    private final static String MAPFILE = "       \n"
            + " 0     \n"
            + "  0    \n"
            + "       \n"
            + "      1";

    private Game game;
    private CommIds commIds;

    @Before
    public void setUp() throws InvalidFightFileException, InvalidMapFileException{
        game = new Game();
        game.handleFightfile(fightFile);
        game.handleMapfile(MAPFILE);
        game.setRoundState(RoundState.REGISTER);
        commIds = new CommIds();

    }


    @Test
    public void testValidRegisterExecuteCommand(){

        assertTrue(game.getRoundCounter() == 0);
        assertEquals(game.getRoundState(), RoundState.REGISTER);
        Player chris = game.getPlayerByName("Christopher");
        Player lukas = game.getPlayerByName("Lukas");
        Player henrik = game.getPlayerByName("Henrik");

        //First Player
        Register reg1 = new Register(commIds, 123, "Christopher", CreatureType.ELF, "Rot", 0, 1, 1);
        assertTrue(reg1.validateCommand(game));
        assertFalse(chris.getRegistered());
        reg1.executeCommand(game);
        assertTrue(chris.getRegistered());
        assertEquals(chris.getId(),0);
        assertEquals(chris.getMonsterType(), CreatureType.ELF);
        assertEquals(chris.getName(), "Christopher");
        assertFalse(lukas.getRegistered());
        assertFalse(henrik.getRegistered());

        //Second Player

        Register reg2 = new Register(commIds, 456, "Lukas", CreatureType.KOBOLD, "Rot", 1, 1, 2);
        assertTrue(reg2.validateCommand(game));
        assertFalse(lukas.getRegistered());
        reg2.executeCommand(game);
        assertTrue(lukas.getRegistered());
        assertTrue(chris.getRegistered());
        assertEquals(lukas.getId(),1);
        assertEquals(lukas.getMonsterType(), CreatureType.KOBOLD);
        assertEquals(lukas.getName(),"Lukas");
        assertFalse(henrik.getRegistered());
        assertEquals(lukas.getTeamName(), chris.getTeamName());

        //Third Player
        Register reg3 = new Register(commIds, 789, "Henrik", CreatureType.ROOK, "Blau", 2, 4, 4);
        assertTrue(reg3.validateCommand(game));
        assertFalse(henrik.getRegistered());
        reg3.executeCommand(game);
        assertTrue(henrik.getRegistered());
        assertEquals(henrik.getMonsterType(), CreatureType.ROOK);
        assertEquals(henrik.getId(),2);
        assertEquals(henrik.getName(), "Henrik");
        assertEquals(henrik.getTeamName(), "Blau");

    }

    @Test
    public void falseRegisterStateTest(){
        Game game2 = new Game();
        game2.setRoundState(RoundState.ROUNDBEGIN);

        Register reg1 = new Register(commIds, 123, "Christopher", CreatureType.ELF, "Rot", 0, 1, 1);
        assertTrue(reg1.validateCommand(game));
        assertTrue(reg1.validateCommand(game2));
    }

    @Test
    public void invalidRegistration(){

        Register reg1= new Register(commIds, 123, "Karla", CreatureType.ELF, "Rot",0,0,1);
        assertFalse(reg1.validateCommand(game));

        Register reg2= new Register(commIds, 123, "Christopher", CreatureType.KOBOLD, "Rot",0,0,1);
        assertFalse(reg2.validateCommand(game));

        Register reg3= new Register(commIds, 123, "Christopher", CreatureType.ELF, "Blau",0,0,1);
        assertFalse(reg3.validateCommand(game));

    }

    @After
    public void destroy() {
        game = null;

    }
}
