package de.unisaarland.sopra.messagestest;

import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.messages.attack.Move;
import de.unisaarland.sopra.model.*;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.utility.InvalidFightFileException;
import de.unisaarland.sopra.utility.InvalidMapFileException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Team 14 on 13.09.2016.
 */
public class MoveTest {

    private final static String FIGHTFILE = "Rot, Blau\n"
            + "Rot, Dobby, Ifrit\n"
            + "Rot,Flobby,kobold\n"
            + "Blau,    Robby, RooK";

    private final static String MAPFILE1 = " 00 \n" +
            "10# \n" +
            " ## ";

    private Game game;
    private CommIds commIds;

    @Before
    public void setup() throws InvalidMapFileException, InvalidFightFileException {
        game = new Game();
        game.handleFightfile(FIGHTFILE);
        game.handleMapfile(MAPFILE1);

        Pc dobby = new Pc(0, new GameVector(1, 1), 20, CreatureType.IFRIT);
        Pc robby = new Pc(1, new GameVector(0, 1), 20, CreatureType.IFRIT);

        game.addMonster(dobby);
        game.addMonster(robby);

        commIds = new CommIds();
        List<Integer> initOrder = new ArrayList();
        initOrder.add(0);
        initOrder.add(1);
        game.setInitOrder(initOrder);
        game.setCurrentMonsterIndex(0);
        game.setRoundState(RoundState.ACTNOW); //Add these lines to the @Before procedure of every test!!!
    }

    @Test
    public void moveBlockedFieldTest(){
        Move move = new Move(commIds, 0, Direction.NORTH_EAST);
        assertTrue(move.validateCommand(game));

        Move move2 = new Move(commIds, 0, Direction.EAST);
        assertFalse(move2.validateCommand(game));

        Move move3 = new Move(commIds, 0, Direction.SOUTH_EAST);
        assertFalse(move3.validateCommand(game));
    }

    @Test
    public void moveBlockedMonsterTest(){
        Move move = new Move(commIds, 0, Direction.NORTH_EAST);
        assertTrue(move.validateCommand(game));

        Move move2 = new Move(commIds, 0, Direction.WEST);
        assertFalse(move2.validateCommand(game));
    }

    @Test
    public void moveEnergyCostFieldTest(){
        game.setFieldAt(new GameVector(2,0), Field.WATER);
        Pc monster = game.getMonsterById(0);
        Move move = new Move(commIds, 0, Direction.NORTH_EAST);
        assertTrue(move.validateCommand(game));

        monster.setEnergy(399);
        Move move2 = new Move(commIds, 0, Direction.NORTH_EAST);
        assertFalse(move2.validateCommand(game));
    }

    @Test
    public void moveEnergyCostTest(){
        Pc monster = game.getMonsterById(0);
        Move move = new Move(commIds, 0, Direction.NORTH_EAST);
        assertTrue(move.validateCommand(game));

        monster.setEnergy(0);
        Move move2 = new Move(commIds, 0, Direction.NORTH_EAST);
        assertFalse(move2.validateCommand(game));
    }

    @Test
    public void testMoveTest(){
        assertNotNull(game.getMonsterByPosition(new GameVector(1, 1)));
        Move move = new Move(commIds, 0, Direction.NORTH_EAST);
        move.executeCommand(game);

        assertNotNull(game.getMonsterByPosition(new GameVector(2, 0)));
    }

    @Test
    public void consumeEnergyTest(){
        Pc monster = game.getMonsterById(0);
        assertEquals(monster.getEnergy(), 1000);
        Move move = new Move(commIds, 0, Direction.NORTH_EAST);
        move.executeCommand(game);

        assertEquals(monster.getEnergy(), 900);
    }

    @Test
    public void consumeEnergyFieldTest(){
        Pc monster = game.getMonsterById(0);
        assertEquals(monster.getEnergy(), 1000);

        game.setFieldAt(new GameVector(2,0), Field.WATER);

        Move move = new Move(commIds, 0, Direction.NORTH_EAST);
        move.executeCommand(game);

        assertEquals(monster.getEnergy(), 600);
    }

    @After
    public void destroy(){
        game = null;
    }
}
