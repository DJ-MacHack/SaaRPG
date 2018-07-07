package de.unisaarland.sopra.messagestest.attacktest;

import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.messages.attack.Claw;
import de.unisaarland.sopra.model.*;
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
public class ClawTest {

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
        if(ct == CreatureType.IFRIT){
            game.addMonster(new Pc(1, new GameVector(0, 0), 87, ct));
        }else{
            game.addMonster(new Pc(1, new GameVector(0, 0), 100, ct));
        }
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
     * @return Returns a valid "Claw" in "Direction.EAST"
     */
    private Claw getValidClawEast() {
        return new Claw(new CommIds(), 1, Direction.EAST);
    }

    /**
     * @return Returns a valid "Claw" in "Direction.WEST"
     */
    private Claw getValidClawWest() {
        return new Claw(new CommIds(), 1, Direction.WEST);
    }

    /**
     * @return Returns a valid "Claw" in "Direction.South_West"
     */
    private Claw getValidClawSouthWest() {
        return new Claw(new CommIds(), 1, Direction.SOUTH_WEST);
    }

    /**
     * @return Returns an invalid "Claw" for this setup
     */
    private Claw getInvalidClaw() {
        return new Claw(new CommIds(), 2, Direction.WEST);
    }

    // Actual tests \\

    /**
     * Tests if legit instances of Claw can be created
     */
    @Test
    public void canCreateValidClaws() {
        assertNotNull(getValidClawEast());
        assertNotNull(getValidClawWest());
        assertNotNull(getValidClawSouthWest());
    }

    /**
     * Tests if the attacktest handled correctly
     */
    @Test
    public void testValidClawBasic() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Claw claw = getValidClawEast();

        // Test test method
        assertTrue(claw.validateCommand(game));

        // execution
        claw.executeCommand(game);
        // hp
        assertEquals(game.getCreatureById(2).getHp(), 94);
        assertEquals(game.getCreatureById(1).getHp(), 200);
        // energy
        assertEquals(game.getMonsterById(1).getEnergy(), 730);
    }

    /**
     * Tests if an only other creatures cannot use claw
     */
    @Test
    public void testInvalidCreatureV1() throws InvalidMapFileException, InvalidFightFileException{
        Claw claw = getInvalidClaw();
        assertFalse(claw.validateCommand(setupGame()));
    }

    /**
     * Tests if an invalid creature can execute the attacktest
     */
    @Test
    public void testInvalidCreatureV2() throws InvalidMapFileException, InvalidFightFileException{
        Claw claw = getValidClawEast();

        // Test all different Monster
        assertFalse(claw.validateCommand(setupArbitraryCreatureGame(CreatureType.KOBOLD)));
        assertFalse(claw.validateCommand(setupArbitraryCreatureGame(CreatureType.ELF)));
        assertFalse(claw.validateCommand(setupArbitraryCreatureGame(CreatureType.ROOK)));
        assertFalse(claw.validateCommand(setupArbitraryCreatureGame(CreatureType.NAGA)));
        assertFalse(claw.validateCommand(setupArbitraryCreatureGame(CreatureType.WRAITH)));
        assertFalse(claw.validateCommand(setupArbitraryCreatureGame(CreatureType.IFRIT)));
    }

    /**
     * Checks if out of range is recognized correctly
     */
    @Test
    public void testOutOfRange() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Claw claw = getValidClawEast();

        // Move Naga out of range
        Pc naga = game.getMonsterById(2);
        naga.setPosition(new GameVector(2, 0));

        // Test attacktest
        assertFalse(claw.validateCommand(game));
    }

    /**
     * Tests if diagonal movement like North-East is possible
     */
    @Test
    public void testDiagonal() throws InvalidMapFileException, InvalidFightFileException {
        Game game = setupGame();
        Claw claw = getValidClawSouthWest();

        //Move Yeti and Naga
        Pc yeti = game.getMonsterById(1);
        Pc naga = game.getMonsterById(2);
        yeti.setPosition(new GameVector(1, 0));
        naga.setPosition(new GameVector(0, 1));

        // Test attacktest
        assertTrue(claw.validateCommand(game));
    }

    /**
     * Tests if FieldEffects are applied
     */
    @Test
    public void testForFieldEffects() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Claw claw = getValidClawWest();

        // Move Yeti and Naga
        Pc yeti = game.getMonsterById(1);
        Pc naga = game.getMonsterById(2);
        yeti.setPosition(new GameVector(1, 2));
        naga.setPosition(new GameVector(0, 2));

        game.setFieldAt(yeti.getPosition(), Field.WATER);
        // Test Water
        assertFalse(claw.validateCommand(game));

        // Move again
        yeti.setPosition(new GameVector(2, 1));
        naga.setPosition(new GameVector(1, 1));

        //Test attacktest
        assertTrue(claw.validateCommand(game));

        //execute attacktest
        claw.executeCommand(game);

        // Check values
        //hp
        int damage = (int) (0.7 * 6);
        assertEquals(naga.getHp(), 100 - damage);

        //energy
        assertEquals(yeti.getEnergy(), 1000 - 3 * 270);
    }

    /**
     * Checks if Energy is controlled correctly
     */
    @Test
    public void testEnergy() throws InvalidMapFileException, InvalidFightFileException{
        Game game = setupGame();
        Claw claw = getValidClawEast();

        // Change Energy of Yeti
        Pc yeti = game.getMonsterById(1);
        yeti.setEnergy(269);

        assertFalse(claw.validateCommand(game));

        // Raise Energy so crush is valid now
        yeti.drainEnergy(-1);

        assertTrue(claw.validateCommand(game));
    }
}
