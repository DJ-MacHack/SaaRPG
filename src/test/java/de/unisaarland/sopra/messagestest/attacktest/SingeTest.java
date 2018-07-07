package de.unisaarland.sopra.messagestest.attacktest;

import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.messages.attack.Singe;
import de.unisaarland.sopra.model.*;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.utility.InvalidFightFileException;
import de.unisaarland.sopra.utility.InvalidMapFileException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for Singe
 * Created on 13.09.16.
 *
 * @author Henrik Paul Koehn
 */
public class SingeTest {

    // Setup for tests \\

    // Fightfile
    public final static String FIGHTFILE = "Fiaha, Dorkness\n" +
            "\n" +
            "Fiaha, Cinder, Ifrit\n" +
            "\n" +
            "Dorkness, Horst, Wraith";

    // Mapfile
    public final static String MAPFILE =
            "01^      \n" +
            " t$      \n" +
            " ~       ";

    /**
     * @return Returns a basic "Game" setup
     */
    private Game setupGame() throws InvalidMapFileException, InvalidFightFileException {
        Game game = new Game();
        game.handleFightfile(FIGHTFILE);
        game.handleMapfile(MAPFILE);
        game.addMonster(new Pc(1, new GameVector(0, 0), 87, CreatureType.IFRIT));
        game.addMonster(new Pc(2, new GameVector(1, 0), 100, CreatureType.WRAITH));

        List<Integer> initOrder = new ArrayList();
        initOrder.add(1);
        initOrder.add(2);
        game.setInitOrder(initOrder);
        game.setCurrentMonsterIndex(0);
        game.setRoundState(RoundState.ACTNOW);

        return game;
    }

    /**
     * Setup for testing if monsters can only use their attacks
     *
     * @param ct {@link CreatureType} that shall try to execute the attacktest
     * @return According {@link Game}
     */
    private Game setupArbitraryCreatureGame(CreatureType ct) throws InvalidMapFileException, InvalidFightFileException{
        Game game = new Game();
        game.handleFightfile(FIGHTFILE);
        game.handleMapfile(MAPFILE);
        game.addMonster(new Pc(1, new GameVector(0, 0), 100, ct));
        game.addMonster(new Pc(2, new GameVector(1, 0), 100, CreatureType.ROOK));

        List<Integer> initOrder = new ArrayList();
        initOrder.add(1);
        initOrder.add(2);
        game.setInitOrder(initOrder);
        game.setCurrentMonsterIndex(0);
        game.setRoundState(RoundState.ACTNOW);

        return game;
    }

    /**
     * @return Returns a valid "Singe" in "Direction.EAST"
     */
    private Singe getValidSingeEast() {
        return new Singe(new CommIds(), 1, Direction.EAST);
    }

    /**
     * @return Returns a valid "Singe" in "Direction.WEST"
     */
    private Singe getValidSingeWest() {
        return new Singe(new CommIds(), 1, Direction.WEST);
    }

    /**
     * @return Returns a valid "Singe" in "Direction.South_West"
     */
    private Singe getValidSingeSouthWest() {
        return new Singe(new CommIds(), 1, Direction.SOUTH_WEST);
    }

    /**
     * @return Returns an invalid "Singe" for this setup
     */
    private Singe getInvalidSinge() {
        return new Singe(new CommIds(), 2, Direction.WEST);
    }

    // Actual tests \\

    /**
     * Tests if legit instances of Singe can be created
     */
    @Test
    public void canCreateValidSinges() {
        assertNotNull(getValidSingeEast());
        assertNotNull(getValidSingeWest());
        assertNotNull(getValidSingeSouthWest());
    }

    /**
     * Tests if the attacktest handled correctly
     */
    @Test
    public void testValidSingeBasic() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Singe singe = getValidSingeEast();

        // Test test method
        assertTrue(singe.validateCommand(game));

        //execution
        singe.executeCommand(game);
        // hp
        assertEquals(game.getCreatureById(2).getHp(), 93);
        assertEquals(game.getCreatureById(1).getHp(), 87);
        //energy
        assertEquals(game.getMonsterById(1).getEnergy(), 1000 - 255);
    }

    /**
     * Tests if an only other creatures cannot use Crush
     */
    @Test
    public void testInvalidCreature() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Singe singe = getInvalidSinge();

        assertFalse(singe.validateCommand(game));
    }

    /**
     * Tests if an invalid creature can execute the attacktest
     */
    @Test
    public void testInvalidCreatureV2() throws InvalidMapFileException, InvalidFightFileException{
        Singe singe = getValidSingeEast();

        // Test all different Monster
        assertFalse(singe.validateCommand(setupArbitraryCreatureGame(CreatureType.KOBOLD)));
        assertFalse(singe.validateCommand(setupArbitraryCreatureGame(CreatureType.ELF)));
        assertFalse(singe.validateCommand(setupArbitraryCreatureGame(CreatureType.ROOK)));
        assertFalse(singe.validateCommand(setupArbitraryCreatureGame(CreatureType.NAGA)));
        assertFalse(singe.validateCommand(setupArbitraryCreatureGame(CreatureType.WRAITH)));
        assertFalse(singe.validateCommand(setupArbitraryCreatureGame(CreatureType.YETI)));
    }

    /**
     * Checks if out of range is recognized correctly
     */
    @Test
    public void testOutOfRange() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Singe singe = getValidSingeEast();

        // Move Wraith out of range
        Pc wraith = game.getMonsterById(2);
        wraith.setPosition(new GameVector(2, 0));

        // Test attacktest
        assertFalse(singe.validateCommand(game));
    }

    /**
     * Tests if diagonal movement like North-East is possible
     */
    @Test
    public void testDiagonal() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Singe singe = getValidSingeSouthWest();

        //Move Ifrit and Wraith
        Pc ifrit = game.getMonsterById(1);
        Pc wraith = game.getMonsterById(2);
        ifrit.setPosition(new GameVector(1, 0));
        wraith.setPosition(new GameVector(0, 1));

        // Test attacktest
        assertTrue(singe.validateCommand(game));
    }

    /**
     * Tests if FieldEffects are applied
     */
    @Test
    public void testForFieldEffects() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Singe singe = getValidSingeWest();

        // Move Ifrit and Wraith
        Pc ifrit = game.getMonsterById(1);
        Pc wraith = game.getMonsterById(2);
        ifrit.setPosition(new GameVector(1, 2));
        wraith.setPosition(new GameVector(0, 2));
        game.setFieldAt(ifrit.getPosition(),Field.WATER);

        // Test Water
        assertFalse(singe.validateCommand(game));

        // Move again
        ifrit.setPosition(new GameVector(2, 1));
        wraith.setPosition(new GameVector(1, 1));

        ifrit.setEnergy(600);
        //Test attacktest ( Expect fail because not enough energy
        assertFalse(singe.validateCommand(game));

    }

    /**
     * Checks if Energy is controlled correctly
     */
    @Test
    public void testEnergy() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Singe singe = getValidSingeEast();

        // Change Energy of Ifrit
        Pc ifrit = game.getMonsterById(1);
        ifrit.setEnergy(254);

        assertFalse(singe.validateCommand(game));

        // Raise Energy so singe is valid now
        ifrit.drainEnergy(-1);

        assertTrue(singe.validateCommand(game));
    }
}
