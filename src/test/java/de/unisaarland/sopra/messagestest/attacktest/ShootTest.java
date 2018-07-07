package de.unisaarland.sopra.messagestest.attacktest;

import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.messages.Command;
import de.unisaarland.sopra.messages.attack.Shoot;
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
 * Created by Karla on 14.09.2016.
 * Responsible: Lukas Kirschner
 */
public class ShootTest {

    private final static String VALIDFIGHT = "Rot, Blau\n"
            + "Rot, Legolas, Elf\n"
            + "Blau,Gimli,kobold\n"
            + "Blau,    Gandalf, rook\n"
            + "Blau, Saruman, Naga\n"
            + "Blau, Gollum, Ifrit\n"
            + "Blau, Sauron, Wraith";

    private final static String VALIDMAP = "01   11  11\n###########";
    private CommIds commIds1;
    private Game game;

    private Pc legolas, gimli, gandalf, saruman, gollum, sauron;

    @Before
    public void setup() throws InvalidFightFileException, InvalidMapFileException {
        game = new Game();
        game.handleFightfile(VALIDFIGHT);
        game.handleMapfile(VALIDMAP);
        commIds1 = new CommIds();
        game.setRoundState(RoundState.ACTNOW);

        legolas = new Pc(0, new GameVector(0, 0), 100, CreatureType.ELF);
        gimli = new Pc(1, new GameVector(1, 0), 100, CreatureType.KOBOLD);
        gandalf = new Pc(2, new GameVector(5, 0), 100, CreatureType.ROOK);
        saruman = new Pc(3, new GameVector(6, 0), 100, CreatureType.NAGA);
        gollum = new Pc(4, new GameVector(9, 0), 87, CreatureType.IFRIT);
        sauron = new Pc(5, new GameVector(10, 0), 100, CreatureType.WRAITH);

        /*legolas.setEnergy(1000);
        gimli.setEnergy(1000);
        gandalf.setEnergy(1000);
        saruman.setEnergy(1000);*/


        game.addMonster(legolas);
        game.addMonster(gimli);
        game.addMonster(gandalf);
        game.addMonster(saruman);
        game.addMonster(gollum);
        game.addMonster(sauron);
        List<Integer> initOrder = new ArrayList();
        initOrder.add(0);
        initOrder.add(1);
        initOrder.add(2);
        initOrder.add(3);
        initOrder.add(4);
        initOrder.add(5);
        game.setInitOrder(initOrder);
        game.setCurrentMonsterIndex(0);
        game.setRoundState(RoundState.ACTNOW);
    }

    @Test
    public void testShootSimple() {
        Command legoShoot = new Shoot(commIds1, 0, Direction.EAST);
        assertTrue(legoShoot.validateCommand(game));
        legoShoot.executeCommand(game);
        assertEquals(89, gimli.getHp());
    }

    @Test
    public void testShootLimits() {
        Command legoShoot = new Shoot(commIds1, 0, Direction.EAST);
        game.removeMonster(gimli);
        assertNull(game.getMonsterById(1));
        assertTrue(legoShoot.validateCommand(game));
        legoShoot.executeCommand(game);
        assertEquals(89, gandalf.getHp());
    }

    @Test
    public void testShootOutOfLimits() {
        Command legoShoot = new Shoot(commIds1, 0, Direction.EAST);
        game.removeMonster(gimli);
        game.removeMonster(gandalf);
        assertNull(game.getMonsterById(1));
        assertNull(game.getMonsterById(2));
        assertFalse(legoShoot.validateCommand(game));
        assertEquals(100,saruman.getHp());
    }

    @Test
    public void testShootOnTree(){
        game.setFieldAt(legolas.getPosition(), Field.TREE);
        game.removeMonster(gimli);
        game.removeMonster(gandalf);
        game.removeMonster(saruman);
        Command legoShoot = new Shoot(commIds1, 0, Direction.EAST);
        assertTrue(legoShoot.validateCommand(game));
        legoShoot.executeCommand(game);
        assertEquals(76,gollum.getHp());
    }

    @Test
    public void testShootOnTreeOutOfBounds(){
        game.setFieldAt(legolas.getPosition(), Field.TREE);
        game.removeMonster(gimli);
        game.removeMonster(gandalf);
        game.removeMonster(saruman);
        game.removeMonster(gollum);
        Command legoShoot = new Shoot(commIds1, 0, Direction.EAST);
        assertFalse(legoShoot.validateCommand(game));
    }

    @Test
    public void testShootOverRock(){
        game.setFieldAt(new GameVector(2,0),Field.ROCK);
        Command legoShoot = new Shoot(commIds1, 0, Direction.EAST);
        game.removeMonster(gimli);
        assertFalse(legoShoot.validateCommand(game));
    }

    @Test
    public void testShootOnWater(){
        game.setFieldAt(legolas.getPosition(),Field.WATER);
        Command legoShoot = new Shoot(commIds1, 0, Direction.EAST);
        assertFalse(legoShoot.validateCommand(game));
    }

    @Test
    public void testShootDeadBoar(){
        game.removeMonster(gimli);
        game.removeMonster(gandalf);
        game.removeMonster(saruman);
        game.removeMonster(gollum);
        Npc boar = new Npc(33,new GameVector(2,0),20,CreatureType.BOAR,6,20);
        boar.receiveDamage(20);
        game.addNpc(boar);
        assertEquals(1,game.getNpcs().size());
        Command legoShoot = new Shoot(commIds1, 0, Direction.EAST);
        assertFalse(legoShoot.validateCommand(game));
    }
}
