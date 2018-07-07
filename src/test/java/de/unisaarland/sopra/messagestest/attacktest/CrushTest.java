package de.unisaarland.sopra.messagestest.attacktest;

import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.messages.attack.Crush;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.model.Pc;
import de.unisaarland.sopra.model.RoundState;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.utility.InvalidFightFileException;
import de.unisaarland.sopra.utility.InvalidMapFileException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for Claw
 * Created on 13.09.16.
 *
 * @author Henrik Paul Koehn
 */
public class CrushTest {

    // Setup for tests \\

    // Fightfile
    public final static String FIGHTFILE = "Ice, Water\n" +
            "\n" +
            "Ice, Nunu, Yeti\n" +
            "\n" +
            "Water, Bubbles, Naga";

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
        game.addMonster(new Pc(1, new GameVector(0, 0), 200, CreatureType.YETI));
        game.addMonster(new Pc(2, new GameVector(1, 0), 100, CreatureType.NAGA));

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
        game.addMonster(new Pc(1, new GameVector(0, 0), 80, ct));
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
     * @return Returns a valid "Crush" in "Direction.EAST"
     */
    private Crush getValidCrushEast() {
        return new Crush(new CommIds(), 1, Direction.EAST);
    }

    /**
     * @return Returns a valid "Crush" in "Direction.WEST"
     */
    private Crush getValidCrushWest() {
        return new Crush(new CommIds(), 1, Direction.WEST);
    }

    /**
     * @return Returns a valid "Crush" in "Direction.South_West"
     */
    private Crush getValidCrushSouthWest() {
        return new Crush(new CommIds(), 1, Direction.SOUTH_WEST);
    }

    /**
     * @return Returns an invalid "Crush" for this setup
     */
    private Crush getInvalidCrush() {
        return new Crush(new CommIds(), 2, Direction.WEST);
    }

    // Actual tests \\

    /**
     * Tests if legit instances of Crush can be created
     */
    @Test
    public void canCreateValidCrushs() {
        assertNotNull(getValidCrushEast());
        assertNotNull(getValidCrushWest());
        assertNotNull(getValidCrushSouthWest());
    }

    /**
     * Tests if the attacktest handled correctly
     */
    @Test
    public void testValidCrushBasic() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Crush crush = getValidCrushEast();

        // Test test method
        assertTrue(crush.validateCommand(game));

        //execution
        crush.executeCommand(game);
        // hp
        assertEquals(game.getCreatureById(2).getHp(), 84);
        assertEquals(game.getCreatureById(1).getHp(), 200);
        //energy
        assertEquals(game.getMonsterById(1).getEnergy(), 1000 - 460);
    }

    /**
     * Tests if an only other creatures cannot use Crush
     */
    @Test
    public void testInvalidCreature() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Crush crush = getInvalidCrush();

        assertFalse(crush.validateCommand(game));
    }

    /**
     * Tests if an invalid creature can execute the attacktest
     */
    @Test
    public void testInvalidCreatureV2() throws InvalidMapFileException, InvalidFightFileException{
        Crush crush = getValidCrushEast();

        // Test all different Monster
        assertFalse(crush.validateCommand(setupArbitraryCreatureGame(CreatureType.KOBOLD)));
        assertFalse(crush.validateCommand(setupArbitraryCreatureGame(CreatureType.ELF)));
        assertFalse(crush.validateCommand(setupArbitraryCreatureGame(CreatureType.ROOK)));
        assertFalse(crush.validateCommand(setupArbitraryCreatureGame(CreatureType.NAGA)));
        assertFalse(crush.validateCommand(setupArbitraryCreatureGame(CreatureType.WRAITH)));
        assertFalse(crush.validateCommand(setupArbitraryCreatureGame(CreatureType.IFRIT)));
    }

    /**
     * Checks if out of range is recognized correctly
     */
    @Test
    public void testOutOfRange() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Crush crush = getValidCrushEast();

        // Move Naga out of range
        Pc naga = game.getMonsterById(2);
        naga.setPosition(new GameVector(2, 0));

        // Test attacktest
        assertFalse(crush.validateCommand(game));
    }

    /**
     * Tests if diagonal movement like North-East is possible
     */
    @Test
    public void testDiagonal() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Crush crush = getValidCrushSouthWest();

        //Move Yeti and Naga
        Pc yeti = game.getMonsterById(1);
        Pc naga = game.getMonsterById(2);
        yeti.setPosition(new GameVector(1, 0));
        naga.setPosition(new GameVector(0, 1));

        // Test attacktest
        assertTrue(crush.validateCommand(game));
    }

    /**
     * Tests if FieldEffects are applied
     */
    @Test
    public void testForFieldEffects() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Crush crush = getValidCrushWest();

        // Move Yeti and Naga
        Pc yeti = game.getMonsterById(1);
        Pc naga = game.getMonsterById(2);
        yeti.setPosition(new GameVector(0, 2));
        naga.setPosition(new GameVector(-1, 2));

        // Test Water
        assertFalse(crush.validateCommand(game));

        // Move again
        yeti.setPosition(new GameVector(2, 1));
        naga.setPosition(new GameVector(1, 1));

        //Test attacktest ( Expect fail because not enough energy
        assertFalse(crush.validateCommand(game));

    }

    /**
     * Checks if Energy is controlled correctly
     */
    @Test
    public void testEnergy() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Crush crush = getValidCrushEast();

        // Change Energy of Yeti
        Pc yeti = game.getMonsterById(1);
        yeti.setEnergy(459);

        assertFalse(crush.validateCommand(game));

        // Raise Energy so crush is valid now
        yeti.drainEnergy(-1);

        assertTrue(crush.validateCommand(game));
    }
}
