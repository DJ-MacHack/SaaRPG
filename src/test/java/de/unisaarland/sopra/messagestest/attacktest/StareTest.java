package de.unisaarland.sopra.messagestest.attacktest;

import de.unisaarland.sopra.*;
import de.unisaarland.sopra.messages.attack.Stare;
import de.unisaarland.sopra.model.*;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.utility.InvalidFightFileException;
import de.unisaarland.sopra.utility.InvalidMapFileException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for Stare
 * Created on 12.09.16.
 *
 * @author Henrik Paul Koehn
 */
public class StareTest {

    // Setup for tests \\

    // Fightfile
    public final static String FIGHTFILE = "Yellow, Green\n" +
            "\n" +
            "Yellow, Sun, Rook\n" +
            "\n" +
            "Green, Leeeeeaf, Elf";

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
        game.addMonster(new Pc(1, new GameVector(0, 0), 100, CreatureType.ROOK));
        game.addMonster(new Pc(2, new GameVector(1, 0), 100, CreatureType.ELF));

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
     * @return Returns a valid "Stare" in "Direction.EAST"
     */
    private Stare getValidStareEast() {
        return new Stare(new CommIds(), 1, Direction.EAST);
    }

    /**
     * @return Returns a valid "Stare" in "Direction.WEST"
     */
    private Stare getValidStareWest() {
        return new Stare(new CommIds(), 1, Direction.WEST);
    }

    /**
     * @return Returns a valid "Stare" in "Direction.NORTH_EAST"
     */
    private Stare getValidStareNorthEast() {
        return new Stare(new CommIds(), 1, Direction.NORTH_EAST);
    }

    /**
     * @return Returns a non valid "Stare" since the creature with id 1 is not a "Creature.ROOK"
     */
    private Stare getInvalidStare() {
        return new Stare(new CommIds(), 2, Direction.WEST);
    }

    // Actual tests \\

    @Test
    public void canCreateValidStares() {
        assertNotNull(getValidStareEast());
        assertNotNull(getValidStareWest());
        assertNotNull(getValidStareNorthEast());
    }

    /**
     * Checks if a valid stare is executed correctly
     */
    @Test
    public void testValidStareBasic() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Stare stare = getValidStareEast();

        // Test test method
        assertTrue(stare.validateCommand(game));

        //execution
        stare.executeCommand(game);
        // hp
        assertEquals(game.getCreatureById(2).getHp(), 94);
        assertEquals(game.getCreatureById(1).getHp(), 100);
        //energy
        assertEquals(game.getMonsterById(1).getEnergy(), 730);
    }

    /**
     * Tests if an invalid creature can execute the attacktest
     */

    @Test
    public void testInvalidCreatureV1() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Stare stare = getInvalidStare();

        assertFalse(stare.validateCommand(game));
    }

    /**
     * Tests if an invalid creature can execute the attacktest
     */
    @Test
    public void testInvalidCreatureV2() throws InvalidMapFileException, InvalidFightFileException{
        Stare stare = getValidStareEast();

        // Test all different Monster
        assertFalse(stare.validateCommand(setupArbitraryCreatureGame(CreatureType.KOBOLD)));
        assertFalse(stare.validateCommand(setupArbitraryCreatureGame(CreatureType.ELF)));
        assertFalse(stare.validateCommand(setupArbitraryCreatureGame(CreatureType.YETI)));
        assertFalse(stare.validateCommand(setupArbitraryCreatureGame(CreatureType.NAGA)));
        assertFalse(stare.validateCommand(setupArbitraryCreatureGame(CreatureType.WRAITH)));
        assertFalse(stare.validateCommand(setupArbitraryCreatureGame(CreatureType.IFRIT)));
    }

    /**
     * Tests if Stare can hit targets out of range (it shall not)
     */
    @Test
    public void testOutOfRange() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Stare stare = getValidStareEast();

        // Move Elf out of range
        Pc elf = game.getMonsterById(2);
        elf.setPosition(new GameVector(2, 0));

        // Test attacktest
        assertFalse(stare.validateCommand(game));
    }

    /**
     * Tests if diagonal Movement link NorthEast is possible
     */
    @Test
    public void testDiagonal() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Stare stare = getValidStareNorthEast();

        //Move Rook and Elf
        Pc rook = game.getMonsterById(1);
        Pc elf = game.getMonsterById(2);
        rook.setPosition(new GameVector(2, 1));
        elf.setPosition(new GameVector(3, 0));

        // Test attacktest
        assertTrue(stare.validateCommand(game));
    }

    /**
     * Tests if FieldEffects are regarded
     */
    @Test
    public void testForFieldEffects() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Stare stare = getValidStareWest();

        // Move Rook and Elf
        Pc rook = game.getMonsterById(1);
        Pc elf = game.getMonsterById(2);
        rook.setPosition(new GameVector(0, 2));
        elf.setPosition(new GameVector(-1, 2));

        // Water Field check
        assertFalse(stare.validateCommand(game));

        // Move Elf on tree and Rook on Curse
        rook.setPosition(new GameVector(2, 1));
        elf.setPosition(new GameVector(1, 1));

        // Check for Damage Multiplier
        stare.executeCommand(game);

        int damage = (int) (0.7 * 6);
        assertEquals(elf.getHp(), 100 - damage);

        //Check Energy
        assertEquals(rook.getEnergy(), 1000 - 3 * 270);
    }

    /**
     * Tests if the range modifier for rook is applied to melee attacktest (it shall not)
     */
    @Test
    public void testForInvalidRangeModifier() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Stare stare = getValidStareEast();

        // move Elf and Rook
        Pc rook = game.getMonsterById(1);
        Pc elf = game.getMonsterById(2);
        rook.setPosition(new GameVector(2, 0));
        elf.setPosition(new GameVector(4, 0));

        assertFalse(stare.validateCommand(game));
    }

}
