package de.unisaarland.sopra.messagestest.attacktest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.messages.attack.Cut;
import de.unisaarland.sopra.model.*;
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
 * Created by Team14 on 13/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class CutTest {

    private final static String FightFile1 = "DC, Marvel, Rito\n" +
            "DC, GreenLantern, Elf\n" +
            "DC, Vashjir, Naga\n" +
            "DC, Batman, Wraith\n" +
            "Marvel, GruenerKobold, Kobold\n" +
            "Rito, Nunu, Yeti\n" +
            "Rito, Brand, Ifrit";

    private final static String FightFile2 = "DC, Marvel\n" +
            "DC, Vashjir, Naga\n" +
            "Marvel, BenGrimm, Rook";

    private final static String MapFile1 = "0.2\n" +
            "+01\n" +
            "20 ";

    private final static String MapFile2 = " . \n" +
            "10 \n" +
            " # ";

    private Game game1;
    private Game game2;

    private Cut cutNW;
    private Cut cutSW;
    private Cut cutW;
    private Cut cutE;

    private Pc naga;
    private Pc wraith;
    private Pc kobold;
    private Pc rook;
    private Pc elf;
    private Pc yeti;
    private Pc ifrit;

    private Npc boar;
    private Npc fairy;

    @Before
    public void setup() throws InvalidMapFileException, InvalidFightFileException {
        naga = new Pc(0, new GameVector(1, 1), 100, CreatureType.NAGA);
        wraith = new Pc(1, new GameVector(0, 2), 100, CreatureType.WRAITH);
        kobold = new Pc(2, new GameVector(2, 1), 100, CreatureType.KOBOLD);
        rook = new Pc(3, new GameVector(0, 1), 100, CreatureType.ROOK);
        elf = new Pc(4, new GameVector(0, 0), 100, CreatureType.ELF);
        yeti = new Pc(5, new GameVector(2, 0), 200, CreatureType.YETI);
        ifrit = new Pc(6, new GameVector(-1, 2), 87, CreatureType.IFRIT);

        boar = new Npc(7, new GameVector(1, 0), 20, CreatureType.BOAR, 6, 20);
        fairy = new Npc(8, new GameVector(0, 1), 100, CreatureType.FAIRY, 4, 10);

        game1 = new Game();
        game1.handleFightfile(FightFile1);
        game1.handleMapfile(MapFile1);
        game1.addMonster(naga);
        game1.addMonster(wraith);
        game1.addMonster(kobold);
        game1.addMonster(elf);
        game1.addMonster(yeti);
        game1.addMonster(ifrit);
        game1.addNpc(boar);
        game1.addNpc(fairy);
        game1.setRoundState(RoundState.ACTNOW);

        List<Integer> initOrder = new ArrayList<>();
        initOrder.add(0);
        initOrder.add(1);
        initOrder.add(2);
        initOrder.add(4);
        initOrder.add(5);
        initOrder.add(6);
        game1.setInitOrder(initOrder);


        game2 = new Game();
        game2.handleFightfile(FightFile2);
        game2.handleMapfile(MapFile2);
        game2.addMonster(naga);
        game2.addMonster(rook);
        game2.addNpc(boar);
        game2.setRoundState(RoundState.ACTNOW);

        List<Integer> initOrder2 = new ArrayList<>();
        initOrder2.add(0);
        initOrder2.add(3);
        game2.setInitOrder(initOrder2);

        cutNW = new Cut(null, 0, Direction.NORTH_WEST);
        cutSW = new Cut(null, 0, Direction.SOUTH_WEST);
        cutW = new Cut(null, 0, Direction.WEST);
        cutE = new Cut(null, 0, Direction.EAST);
    }

    @Test
    public void teamAttackTest() {
        assertTrue(cutSW.validateCommand(game1));
        cutSW.executeCommand(game1);

        assertEquals(wraith.getHp(), 95);
        assertEquals(naga.getHp(), 100);
        assertEquals(kobold.getHp(), 100);
        assertEquals(elf.getHp(), 100);
        assertEquals(yeti.getHp(), 200);
        assertEquals(ifrit.getHp(), 87);
        assertEquals(boar.getHp(), 20);
        assertEquals(fairy.getHp(), 100);

        assertEquals(naga.getEnergy(), 800);
        assertEquals(wraith.getEnergy(), 1000);
        assertEquals(kobold.getEnergy(), 1000);
        assertEquals(elf.getEnergy(), 1000);
        assertEquals(yeti.getEnergy(), 1000);
        assertEquals(ifrit.getEnergy(), 1000);
    }

    @Test
    public void standardEnemyAttackTest() {
        assertTrue(cutE.validateCommand(game1));
        cutE.executeCommand(game1);

        assertEquals(kobold.getHp(), 95);
        assertEquals(naga.getHp(), 100);
        assertEquals(wraith.getHp(), 100);
        assertEquals(elf.getHp(), 100);
        assertEquals(yeti.getHp(), 200);
        assertEquals(ifrit.getHp(), 87);
        assertEquals(boar.getHp(), 20);
        assertEquals(fairy.getHp(), 100);

        assertEquals(naga.getEnergy(), 800);
        assertEquals(wraith.getEnergy(), 1000);
        assertEquals(kobold.getEnergy(), 1000);
        assertEquals(elf.getEnergy(), 1000);
        assertEquals(yeti.getEnergy(), 1000);
        assertEquals(ifrit.getEnergy(), 1000);
    }

    @Test
    public void boarAttackTest() {
        assertTrue(cutNW.validateCommand(game1));
        cutNW.executeCommand(game1);

        assertEquals(kobold.getHp(), 100);
        assertEquals(naga.getHp(), 100);
        assertEquals(wraith.getHp(), 100);
        assertEquals(elf.getHp(), 100);
        assertEquals(yeti.getHp(), 200);
        assertEquals(ifrit.getHp(), 87);
        assertEquals(boar.getHp(), 15);
        assertEquals(fairy.getHp(), 100);

        assertEquals(naga.getEnergy(), 800);
        assertEquals(wraith.getEnergy(), 1000);
        assertEquals(kobold.getEnergy(), 1000);
        assertEquals(elf.getEnergy(), 1000);
        assertEquals(yeti.getEnergy(), 1000);
        assertEquals(ifrit.getEnergy(), 1000);
    }

    @Test
    public void fairyAttackTest() {
        assertTrue(cutW.validateCommand(game1));
        cutW.executeCommand(game1);

        assertEquals(kobold.getHp(), 100);
        assertEquals(naga.getHp(), 100);
        assertEquals(wraith.getHp(), 100);
        assertEquals(elf.getHp(), 100);
        assertEquals(yeti.getHp(), 200);
        assertEquals(ifrit.getHp(), 87);
        assertEquals(boar.getHp(), 20);
        assertEquals(fairy.getHp(), 95);

        assertEquals(naga.getEnergy(), 800);
        assertEquals(wraith.getEnergy(), 1000);
        assertEquals(kobold.getEnergy(), 1000);
        assertEquals(elf.getEnergy(), 1000);
        assertEquals(yeti.getEnergy(), 1000);
        assertEquals(ifrit.getEnergy(), 1000);
    }

    @Test
    public void invalidKoboldCutTest() {
        game1.nextMonster();
        game1.nextMonster();
        Cut cutKobold = new Cut(null, 2, Direction.WEST);
        assertFalse(cutKobold.validateCommand(game1));
    }

    @Test
    public void invalidElfCutTest() {
        game1.nextMonster();
        game1.nextMonster();
        game1.nextMonster();
        Cut cutElf = new Cut(null, 4, Direction.EAST);
        assertFalse(cutElf.validateCommand(game1));
    }

    @Test
    public void invalidRookCutTest() {
        game2.nextMonster();
        Cut cutRook = new Cut(null, 3, Direction.EAST);
        assertFalse(cutRook.validateCommand(game2));
    }

    @Test
    public void invalidYetiCutTest() {
        game1.nextMonster();
        game1.nextMonster();
        game1.nextMonster();
        game1.nextMonster();
        Cut cutYeti = new Cut(null, 5, Direction.WEST);
        assertFalse(cutYeti.validateCommand(game1));
    }

    @Test
    public void invalidIfritCutTest() {
        game1.nextMonster();
        game1.nextMonster();
        game1.nextMonster();
        game1.nextMonster();
        game1.nextMonster();
        Cut cutIfrit = new Cut(null, 6, Direction.EAST);
        assertFalse(cutIfrit.validateCommand(game1));
    }

    @Test
    public void invalidWraithCutTest() {
        game1.nextMonster();
        Cut cutWraith = new Cut(null, 1, Direction.EAST);
        assertFalse(cutWraith.validateCommand(game1));
    }

    @Test
    public void invalidBoarCutTest() {
        Cut cutBoar = new Cut(null, 7, Direction.EAST);
        assertFalse(cutBoar.validateCommand(game1));
    }

    @Test
    public void invalidFairyCutTest() {
        Cut cutFairy = new Cut(null, 8, Direction.EAST);
        assertFalse(cutFairy.validateCommand(game1));
    }

    @Test
    public void invalidNoTargetGrassTest() {
        assertFalse(cutE.validateCommand(game2));
    }

    @Test
    public void invalidNoTargetRockTest() {
        assertFalse(cutSW.validateCommand(game2));
    }

    @Test
    public void koboldBushDamageReductionTest() {
        game1.setFieldAt(new GameVector(2, 1), Field.BUSH);
        assertTrue(cutE.validateCommand(game1));
        cutE.executeCommand(game1);

        assertEquals(kobold.getHp(), 98);
        assertEquals(naga.getHp(), 100);
        assertEquals(wraith.getHp(), 100);
        assertEquals(elf.getHp(), 100);
        assertEquals(yeti.getHp(), 200);
        assertEquals(ifrit.getHp(), 87);
        assertEquals(boar.getHp(), 20);
        assertEquals(fairy.getHp(), 100);

        assertEquals(naga.getEnergy(), 800);
        assertEquals(wraith.getEnergy(), 1000);
        assertEquals(elf.getEnergy(), 1000);
        assertEquals(yeti.getEnergy(), 1000);
        assertEquals(ifrit.getEnergy(), 1000);
        assertEquals(kobold.getEnergy(), 1000);
    }

    @Test
    public void koboldTreeDamageReductionTest() {
        game1.setFieldAt(new GameVector(2, 1), Field.TREE);
        assertTrue(cutE.validateCommand(game1));
        cutE.executeCommand(game1);

        assertEquals(kobold.getHp(), 97);
        assertEquals(naga.getHp(), 100);
        assertEquals(wraith.getHp(), 100);
        assertEquals(elf.getHp(), 100);
        assertEquals(yeti.getHp(), 200);
        assertEquals(ifrit.getHp(), 87);
        assertEquals(boar.getHp(), 20);
        assertEquals(fairy.getHp(), 100);

        assertEquals(naga.getEnergy(), 800);
        assertEquals(wraith.getEnergy(), 1000);
        assertEquals(elf.getEnergy(), 1000);
        assertEquals(yeti.getEnergy(), 1000);
        assertEquals(ifrit.getEnergy(), 1000);
        assertEquals(kobold.getEnergy(), 1000);
    }

    @Test
    public void wraithTreeDamageReductionTest() {
        game1.setFieldAt(new GameVector(0, 2), Field.TREE);
        assertTrue(cutSW.validateCommand(game1));
        cutSW.executeCommand(game1);

        assertEquals(wraith.getHp(), 97);
        assertEquals(kobold.getHp(), 100);
        assertEquals(naga.getHp(), 100);
        assertEquals(elf.getHp(), 100);
        assertEquals(yeti.getHp(), 200);
        assertEquals(ifrit.getHp(), 87);
        assertEquals(boar.getHp(), 20);
        assertEquals(fairy.getHp(), 100);

        assertEquals(naga.getEnergy(), 800);
        assertEquals(wraith.getEnergy(), 1000);
        assertEquals(elf.getEnergy(), 1000);
        assertEquals(yeti.getEnergy(), 1000);
        assertEquals(ifrit.getEnergy(), 1000);
        assertEquals(kobold.getEnergy(), 1000);
    }

    @Test
    public void boarTreeDamageReductionTest() {
        game1.setFieldAt(new GameVector(1, 0), Field.TREE);
        assertTrue(cutNW.validateCommand(game1));
        cutNW.executeCommand(game1);

        assertEquals(kobold.getHp(), 100);
        assertEquals(naga.getHp(), 100);
        assertEquals(wraith.getHp(), 100);
        assertEquals(elf.getHp(), 100);
        assertEquals(yeti.getHp(), 200);
        assertEquals(ifrit.getHp(), 87);
        assertEquals(boar.getHp(), 17);
        assertEquals(fairy.getHp(), 100);

        assertEquals(naga.getEnergy(), 800);
        assertEquals(wraith.getEnergy(), 1000);
        assertEquals(kobold.getEnergy(), 1000);
        assertEquals(elf.getEnergy(), 1000);
        assertEquals(yeti.getEnergy(), 1000);
        assertEquals(ifrit.getEnergy(), 1000);
    }

    @Test
    public void fairyTreeReductionTest() {
        game1.setFieldAt(new GameVector(0, 1), Field.TREE);
        assertTrue(cutW.validateCommand(game1));
        cutW.executeCommand(game1);

        assertEquals(kobold.getHp(), 100);
        assertEquals(naga.getHp(), 100);
        assertEquals(wraith.getHp(), 100);
        assertEquals(elf.getHp(), 100);
        assertEquals(yeti.getHp(), 200);
        assertEquals(ifrit.getHp(), 87);
        assertEquals(boar.getHp(), 20);
        assertEquals(fairy.getHp(), 97);

        assertEquals(naga.getEnergy(), 800);
        assertEquals(wraith.getEnergy(), 1000);
        assertEquals(kobold.getEnergy(), 1000);
        assertEquals(elf.getEnergy(), 1000);
        assertEquals(yeti.getEnergy(), 1000);
        assertEquals(ifrit.getEnergy(), 1000);
    }

    @Test
    public void treeEnergieCostReductionTest() {
        game1.setFieldAt(new GameVector(1, 1), Field.TREE);
        assertTrue(cutW.validateCommand(game1));
        cutW.executeCommand(game1);

        assertEquals(kobold.getHp(), 100);
        assertEquals(naga.getHp(), 100);
        assertEquals(wraith.getHp(), 100);
        assertEquals(elf.getHp(), 100);
        assertEquals(yeti.getHp(), 200);
        assertEquals(ifrit.getHp(), 87);
        assertEquals(boar.getHp(), 20);
        assertEquals(fairy.getHp(), 95);

        assertEquals(naga.getEnergy(), 600);
        assertEquals(wraith.getEnergy(), 1000);
        assertEquals(kobold.getEnergy(), 1000);
        assertEquals(elf.getEnergy(), 1000);
        assertEquals(yeti.getEnergy(), 1000);
        assertEquals(ifrit.getEnergy(), 1000);
    }

    @Test
    public void curseEnergieCostReductionTest() {
        game1.setFieldAt(new GameVector(1, 1), Field.CURSE);
        assertTrue(cutW.validateCommand(game1));
        cutW.executeCommand(game1);

        assertEquals(kobold.getHp(), 100);
        assertEquals(naga.getHp(), 100);
        assertEquals(wraith.getHp(), 100);
        assertEquals(elf.getHp(), 100);
        assertEquals(yeti.getHp(), 200);
        assertEquals(ifrit.getHp(), 87);
        assertEquals(boar.getHp(), 20);
        assertEquals(fairy.getHp(), 95);

        assertEquals(naga.getEnergy(), 400);
        assertEquals(wraith.getEnergy(), 1000);
        assertEquals(kobold.getEnergy(), 1000);
        assertEquals(elf.getEnergy(), 1000);
        assertEquals(yeti.getEnergy(), 1000);
        assertEquals(ifrit.getEnergy(), 1000);
    }
}
