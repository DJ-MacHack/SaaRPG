package de.unisaarland.sopra.messagestest.attacktest;

import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.messages.Command;
import de.unisaarland.sopra.messages.attack.Cast;
import de.unisaarland.sopra.model.*;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.utility.InvalidFightFileException;
import de.unisaarland.sopra.utility.InvalidMapFileException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Team14 on 9/13/16.
 * Responsible: Lukas Kirschner
 */
public class CastTest {

    private final static String VALIDFIGHT = "TeamJoko, TeamKlaas\nTeamJoko, Joko, Rook\nTeamKlaas, Klaas, Ifrit\nTeamKlaas, Bernhard, Naga\nTeamKlaas, Thorsten, Wraith";
    private final static String VALIDMAP = "01   11\n       \n       \n       \n       \n       \n       ";

    private Game myGame;

    @Before
    public void setup() throws InvalidMapFileException, InvalidFightFileException {
        myGame = new Game();
        myGame.handleFightfile(VALIDFIGHT);
        myGame.handleMapfile(VALIDMAP);

        Pc joko = new Pc(0,new GameVector(0,0),100, CreatureType.ROOK);
        Pc klaas = new Pc(1, new GameVector(1,0),87,CreatureType.IFRIT);
        Pc bernhard = new Pc(2, new GameVector(5,0),100,CreatureType.NAGA);
        Pc thorsten = new Pc(3, new GameVector(6,0),100,CreatureType.WRAITH);

        joko.setEnergy(1000);

        myGame.addMonster(joko);
        myGame.addMonster(klaas);
        myGame.addMonster(bernhard);
        myGame.addMonster(thorsten);

        List<Integer> initOrder = new ArrayList();
        initOrder.add(0);
        initOrder.add(1);
        initOrder.add(2);
        initOrder.add(3);
        myGame.setInitOrder(initOrder);
        myGame.setCurrentMonsterIndex(0);
        myGame.setRoundState(RoundState.ACTNOW);


    }

    @Test
    public void testCastSimple(){
        Pc klaas = myGame.getMonsterById(1);
        final int before = klaas.getHp();
        Command myCast = new Cast(new CommIds(),0,1,0); //Joko casts against Klaas
        assertTrue(myCast.validateCommand(myGame));
        myCast.executeCommand(myGame);
        final int after = klaas.getHp();
        final int expected = before - 10;
        assertEquals("Cast attacktest did not drain the right amount of energy. Expected " + expected + ", got " + after,expected,after);
    }

    @Test
    public void testCastRangeMax(){
        Pc bernhard = myGame.getMonsterById(2);
        final int before = bernhard.getHp();
        Command myCast = new Cast(new CommIds(),0,5,0); //Joko casts against Bernhard
        assertTrue(myCast.validateCommand(myGame));
        myCast.executeCommand(myGame);
        final int after = bernhard.getHp();
        final int expected = before - 10;
        assertEquals("Cast attacktest did not drain the right amount of energy. Expected " + expected + ", got " + after, expected, after);
    }

    @Test
    public void testCastInvalidRange(){
        Pc thorsten = myGame.getMonsterById(3);
        assertNotNull(thorsten);
        Command myCast = new Cast(new CommIds(), 0, 6, 0);
        assertFalse("validateCommand returned true although target was out of range.", myCast.validateCommand(myGame));
    }

    @Test
    public void testEnergyDrained(){
        Pc joko = myGame.getMonsterById(0);
        Command myCast = new Cast(new CommIds(),0,1,0);
        joko.setEnergy(449);
        assertFalse(myCast.validateCommand(myGame));
        joko.setEnergy(450);
        assertTrue(myCast.validateCommand(myGame));
    }

    @Test
    public void testAttackOnWater(){
        Pc joko = myGame.getMonsterById(0);
        assertNotNull(joko);
        Command myCast = new Cast(new CommIds(),0,1,0);
        assertTrue(myCast.validateCommand(myGame));
        myGame.setFieldAt(new GameVector(0,0), Field.WATER);
        assertFalse(myCast.validateCommand(myGame));
    }

    @Test
    public void testInvalidTarget(){
        Pc joko = myGame.getMonsterById(0);
        assertNotNull(joko);
        Command myCast = new Cast(new CommIds(),0,2,0);
        assertFalse(myCast.validateCommand(myGame));
        myCast = new Cast(new CommIds(),0,4,0);
        assertFalse(myCast.validateCommand(myGame));
    }


}
