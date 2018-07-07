package de.unisaarland.sopra.messagestest.attacktest;

import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.CommandFactory;
import de.unisaarland.sopra.messages.Command;
import de.unisaarland.sopra.messages.CommandFactoryImpl;
import de.unisaarland.sopra.messages.attack.Swap;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.model.Pc;
import de.unisaarland.sopra.model.RoundState;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.utility.InvalidFightFileException;
import de.unisaarland.sopra.utility.InvalidMapFileException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Team14 on 9/13/16.
 * Responsible: Lukas Kirschner
 */
public class SwapTest {
    private final static String VALIDFIGHT = "Gryffindor, Slytherin\nGryffindor, Harry, Wraith\nSlytherin, Crabbe, Elf\nSlytherin, Goyle, Kobold";
    private final static String VALIDMAP = "0   11\n      \n      \n      \n      \n      ";
    private final CommIds myCid = new CommIds();
    private final CommandFactory myCommandFactory = new CommandFactoryImpl(myCid);
    private Pc harry;
    private Game myGame;

    @Before
    public void setup() throws InvalidMapFileException, InvalidFightFileException {
        myGame = new Game();
        myGame.handleFightfile(VALIDFIGHT);
        myGame.handleMapfile(VALIDMAP);

        harry = new Pc(0,new GameVector(0,0),100, CreatureType.WRAITH);
        Pc crabbe = new Pc(1, new GameVector(4,0),87,CreatureType.ELF);
        Pc goyle = new Pc(2, new GameVector(5,0),100,CreatureType.KOBOLD);

        harry.setEnergy(1000);

        myGame.addMonster(harry);
        myGame.addMonster(crabbe);
        myGame.addMonster(goyle);

        List<Integer> initOrder = new ArrayList();
        initOrder.add(0);
        initOrder.add(1);
        initOrder.add(2);
        myGame.setInitOrder(initOrder);
        myGame.setCurrentMonsterIndex(0);
        myGame.setRoundState(RoundState.ACTNOW);
    }

    @Test
    public void testSwapSimple(){
        Pc crabbe = myGame.getMonsterById(1);
        final int before = crabbe.getHp();
        Command myCast = new Swap(myCid,0,4,0); //Harry casts against Crabbe
        assertTrue(myCast.validateCommand(myGame));
        myCast.executeCommand(myGame);
        final int after = crabbe.getHp();
        final int expected = before - 6;
        assertEquals("Swap attacktest did not drain the right amount of energy. Expected " + expected + ", got " + after,expected,after);
    }

    @Test
    public void testSwapInvalidEnergy(){
        Command myCast = new Swap(myCid,0,5,0); //Harry swaps with Goyle, needs 1250 energy.
        assertFalse(myCast.validateCommand(myGame));
    }

    @Test
    public void testSwapSelf(){
        Command myCast = new Swap(myCid,0,0,0); //Harry swaps with himself
        assertTrue(myCast.validateCommand(myGame));
        myCast.executeCommand(myGame);

        assertEquals(harry.getHp(), 94);
        assertEquals(harry.getEnergy(), 1000);
    }

}
