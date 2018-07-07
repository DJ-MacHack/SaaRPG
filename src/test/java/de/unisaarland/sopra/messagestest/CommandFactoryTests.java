package de.unisaarland.sopra.messagestest;

import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.CommandFactory;
import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.messages.*;
import de.unisaarland.sopra.messages.attack.*;
import de.unisaarland.sopra.MonsterType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Team14 on 9/12/16.
 * * Responsible for Player JUnit tests: Lukas Kirschner
 */
public class CommandFactoryTests {

    private CommandFactory<Command> myCmdFac;

    @Before
    public void initTests(){
        CommIds myCoI = new CommIds();
        myCoI.addCommMonsterId(1,1);
        myCmdFac = new CommandFactoryImpl(myCoI);
    }

    @Test
    public void testCreateRegister(){
        Command testCmd = myCmdFac.createRegister(1,"MarcelDavis", MonsterType.NAGA,"1&1");
        assertNotNull(testCmd);
        assertTrue("CommandFactoryImpl returned a wrong dynamic type", testCmd instanceof Register);
    }

    @Test
    public void testCreateWatch(){
        Command testCmd = myCmdFac.createWatch(1);
        assertNotNull(testCmd);
        assertTrue("CommandFactoryImpl returned a wrong dynamic type", testCmd instanceof Watch);
    }

    @Test
    public void testCreateMove(){
        Command testCmd = myCmdFac.createMove(1, Direction.NORTH_EAST);
        assertNotNull(testCmd);
        assertTrue("CommandFactoryImpl returned a wrong dynamic type", testCmd instanceof Move);
    }

    @Test
    public void testCreateStab(){
        Command testCmd = myCmdFac.createStab(1, Direction.NORTH_WEST);
        assertNotNull(testCmd);
        assertTrue("CommandFactoryImpl returned a wrong dynamic type", testCmd instanceof Stab);
    }

    @Test
    public void testCreateSlash(){
        Command testCmd = myCmdFac.createSlash(1, Direction.EAST);
        assertNotNull(testCmd);
        assertTrue("CommandFactoryImpl returned a wrong dynamic type", testCmd instanceof Slash);
    }

    @Test
    public void testCreateStare(){
        Command testCmd = myCmdFac.createStare(1,Direction.SOUTH_EAST);
        assertNotNull(testCmd);
        assertTrue("CommandFactoryImpl returned a wrong dynamic type", testCmd instanceof Stare);
    }

    @Test
    public void testCreateClaw(){
        Command testCmd = myCmdFac.createClaw(1,Direction.SOUTH_WEST);
        assertNotNull(testCmd);
        assertTrue("CommandFactoryImpl returned a wrong dynamic type", testCmd instanceof Claw);
    }

    @Test
    public void testCreateCrush(){
        Command testCmd = myCmdFac.createCrush(1,Direction.WEST);
        assertNotNull(testCmd);
        assertTrue("CommandFactoryImpl returned a wrong dynamic type", testCmd instanceof Crush);
    }

    @Test
    public void testCreateSinge(){
        Command testCmd = myCmdFac.createSinge(1,Direction.NORTH_WEST);
        assertNotNull(testCmd);
        assertTrue("CommandFactoryImpl returned a wrong dynamic type", testCmd instanceof Singe);
    }

    @Test
    public void testCreateShoot(){
        Command testCmd = myCmdFac.createShoot(1,Direction.EAST);
        assertNotNull(testCmd);
        assertTrue("CommandFactoryImpl returned a wrong dynamic type", testCmd instanceof Shoot);
    }

    @Test
    public void testCreateBurn(){
        Command testCmd = myCmdFac.createBurn(1);
        assertNotNull(testCmd);
        assertTrue("CommandFactoryImpl returned a wrong dynamic type", testCmd instanceof Burn);
    }

    @Test
    public void testCreateCast(){
        Command testCmd = myCmdFac.createCast(1, 2, 3);
        assertNotNull(testCmd);
        assertTrue("CommandFactoryImpl returned a wrong dynamic type", testCmd instanceof Cast);
    }

    @Test
    public void testCreateBlink(){
        Command testCmd = myCmdFac.createBlink(1, 2, 3);
        assertNotNull(testCmd);
        assertTrue("CommandFactoryImpl returned a wrong dynamic type", testCmd instanceof Blink);
    }

    @Test
    public void testCreateSwap(){
        Command testCmd = myCmdFac.createSwap(1, 2, 3);
        assertNotNull(testCmd);
        assertTrue("CommandFactoryImpl returned a wrong dynamic type", testCmd instanceof Swap);
    }

    @Test
    public void testCreateCut(){
        Command testCmd = myCmdFac.createCut(1, Direction.SOUTH_WEST);
        assertNotNull(testCmd);
        assertTrue("CommandFactoryImpl returned a wrong dynamic type", testCmd instanceof Cut);
    }

    @Test
    public void testCreateBite(){
        Command testCmd = myCmdFac.createBite(1, Direction.SOUTH_EAST);
        assertNotNull(testCmd);
        assertTrue("CommandFactoryImpl returned a wrong dynamic type", testCmd instanceof Bite);
    }

    @Test
    public void testCreateDoneActing(){
        Command testCmd = myCmdFac.createDoneActing(1);
        assertNotNull(testCmd);
        assertTrue("CommandFactoryImpl returned a wrong dynamic type", testCmd instanceof DoneActing);
    }

    @Test
    public void testCreateWarCry(){
        String testString = "";
        for (char i = 32; i < 127; i++)
            testString = testString.concat(String.valueOf(i));
        testString = testString.concat("\n\t");
        Command testCmd = myCmdFac.createWarCry(1, testString);
        assertNotNull(testCmd);
        assertTrue("CommandFactoryImpl returned a wrong dynamic type", testCmd instanceof WarCry);
    }
}
