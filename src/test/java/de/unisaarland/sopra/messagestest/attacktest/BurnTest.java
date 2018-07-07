package de.unisaarland.sopra.messagestest.attacktest;

import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.messages.attack.Burn;
import de.unisaarland.sopra.model.*;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.utility.InvalidFightFileException;
import de.unisaarland.sopra.utility.InvalidMapFileException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Team 14 on 13.09.2016.
 */
public class BurnTest {

    private final static String FIGHTFILE = "Rot, Blau\n"
            + "Rot, Dobby, Ifrit\n"
            + "Rot,Flobby,kobold\n" + "\n"
            + "Blau,    Robby, RooK";

    private final static String MAPFILE1 = " 1 \n" +
            "10 \n" +
            " 0 ";

    private Game game;
    private CommIds commIds;

    @Before
    public void setup() throws InvalidMapFileException, InvalidFightFileException {
        game = new Game();
        game.handleFightfile(FIGHTFILE);
        game.handleMapfile(MAPFILE1);

        Pc dobby = new Pc(0, new GameVector(1, 1), 20, CreatureType.IFRIT);
        Pc flobby = new Pc(1, new GameVector(1, 0), 10, CreatureType.KOBOLD);
        Pc robby = new Pc(2, new GameVector(1, 2), 10, CreatureType.ROOK);

        game.addMonster(dobby);
        game.addMonster(flobby);
        game.addMonster(robby);

        commIds = new CommIds();

        List<Integer> initOrder = new ArrayList();
        initOrder.add(0);
        initOrder.add(1);
        initOrder.add(2);
        game.setInitOrder(initOrder);
        game.setCurrentMonsterIndex(0);
        game.setRoundState(RoundState.ACTNOW);
    }

    @Test
    public void consumeEnergyTest(){
        Burn burn = new Burn(commIds, 0);
        burn.executeCommand(game);

        Pc monster = game.getMonsterById(0);

        assertEquals(monster.getEnergy(), 550);

        Burn burn2 = new Burn(commIds, 0);
        burn2.executeCommand(game);

        assertEquals(monster.getEnergy(), 100);
    }

    @Test
    public void consumeEnergyFieldTest(){
        game.setFieldAt(new GameVector(1,1), Field.TREE);
        Burn burn = new Burn(commIds, 0);
        burn.executeCommand(game);

        Pc monster = game.getMonsterById(0);

        assertEquals(monster.getEnergy(), 100);
    }

    @Test
    public void damageReducedTest(){
        Burn burn = new Burn(commIds, 0);
        game.setFieldAt(new GameVector(1, 0), Field.BUSH);
        game.setFieldAt(new GameVector(1, 2), Field.TREE);
        burn.executeCommand(game);

        Pc monster = game.getMonsterById(1);        //Kobold
        Pc monster2 = game.getMonsterById(2);       //Rook

        assertEquals(7,monster.getHp());
        assertEquals(6,monster2.getHp());
    }

    @Test
    public void damageReducedTest2(){
        Burn burn = new Burn(commIds, 0);
        game.setFieldAt(new GameVector(1, 0), Field.BUSH);
        assertTrue(burn.validateCommand(game));
        burn.executeCommand(game);

        Pc monster = game.getMonsterById(1);
        Pc monster2 = game.getMonsterById(2);

        game.addMonster(monster);
        game.addMonster(monster2);

        assertEquals(7,monster.getHp());
        assertEquals(4,monster2.getHp());
    }

    @Test
    public void damageTest(){
        Burn burn = new Burn(commIds, 0);
        burn.executeCommand(game);

        Pc monster = game.getMonsterById(1);
        Pc monster2 = game.getMonsterById(2);

        assertEquals(monster.getHp(), 4);
        assertEquals(monster2.getHp(), 4);
    }

    @Test
    public void damageTest2(){
        Pc monster3 = new Pc(3, new GameVector(0, 1), 10, CreatureType.ELF);
        game.addMonster(monster3);
        Burn burn = new Burn(commIds, 0);
        burn.executeCommand(game);

        Pc monster = game.getMonsterById(1);
        Pc monster2 = game.getMonsterById(2);

        assertEquals(6,monster.getHp());
        assertEquals(6,monster2.getHp());
        assertEquals(6,monster3.getHp());
    }

    @Test
    public void fieldEffectTest(){
        Burn burn = new Burn(commIds, 0);
        assertTrue(burn.validateCommand(game));

        game.setFieldAt(new GameVector(1, 1), Field.WATER);

        assertFalse(burn.validateCommand(game));
    }

    @Test
    public void noIfritTest(){
        Burn burn = new Burn(commIds, 0);
        assertTrue(burn.validateCommand(game));

        Burn burn2 = new Burn(commIds, 1);

        assertFalse(burn2.validateCommand(game));
    }

    @Test
    public void noEnemiesTest(){
        Burn burn = new Burn(commIds, 0);
        assertTrue(burn.validateCommand(game));

        Pc monster = game.getMonsterById(1);
        Pc monster2 = game.getMonsterById(2);

        game.removeMonster(monster);
        game.removeMonster(monster2);
        burn = new Burn(commIds, 0);
        assertFalse(burn.validateCommand(game));
    }

    @Test
    public void noEnergyTest(){
        Burn burn = new Burn(commIds, 0);
        assertTrue(burn.validateCommand(game));

        Pc monster = game.getMonsterById(0);
        monster.setEnergy(0);

        assertFalse(burn.validateCommand(game));
    }

    @Test
    public void energyFieldTest(){
        Burn burn = new Burn(commIds, 0);
        assertTrue(burn.validateCommand(game));

        game.setFieldAt(new GameVector(1, 1), Field.CURSE);
        Pc monster = game.getMonsterById(0);
        monster.setEnergy(0);

        assertFalse(burn.validateCommand(game));
    }

    @After
    public void destroy(){
        game = null;
    }
}
