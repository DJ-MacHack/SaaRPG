package de.unisaarland.sopra.modeltest;

import de.unisaarland.sopra.model.Field;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.model.Map;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.utility.InvalidFightFileException;
import de.unisaarland.sopra.utility.InvalidMapFileException;
import org.junit.Before;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Team14 on 12/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class MapTest {

    public final static String FIGHTFILE1 = "LoL, Dota\n"
            + "LoL, Teemo, Kobold\n"
            + "LoL, Brand, Ifrit\n"
            + "LoL, Nocturne, Wraith\n"
            + "Dota, Sandking, Rook\n"
            + "Dota, Siren, Naga\n"
            + "Dota, Drowranger, Elf";

    public final static String FIGHTFILE2 = "LoL, Dota, WoW, Gw2\n"
            + "LoL, Nunu, Yeti\n"
            + "Dota, Siren, Naga\n"
            + "WoW, Ragnaros, Ifrit\n"
            + "Gw2, Yasura, Kobold";

    public final static String MAPFILE1 = "txXT%&.  t\n"
            + "txx%% ~~__\n"
            + "t^A0^^1###\n"
            + " +. #0#$##\n"
            + "1 .^A## ##\n"
            + "##### ####\n"
            + "+#~~~x~~~~\n"
            + "__#~~ ~~~~\n"
            + "_~##~^0~~~\n"
            + "$1.  **^~~";       // 10x10 map with 2 teams 3 players each

    public final static String MAPFILE2 = "0###+~~#*1\n"
            + " ###~##**_\n"
            + " ##%%t##__\n"
            + " #&%tTt#__\n"
            + ".##tt%~~#_\n"
            + ".#tTt%%~#_\n"
            + " . ^AxX~__\n"
            + " ##**#% # \n"
            + "$#***+^~#$\n"
            + "2***^A#~#3";       // 10x10 map with 4 teams 1 player each

    private Map map1;
    private Map map2;

    @Before
    public void setup() throws InvalidMapFileException, InvalidFightFileException {
        Game game1 = new Game();
        game1.handleFightfile(FIGHTFILE1);
        game1.handleMapfile(MAPFILE1);
        map1 = game1.getMap();

        Game game2 = new Game();
        game2.handleFightfile(FIGHTFILE2);
        game2.handleMapfile(MAPFILE2);
        map2 = game2.getMap();
    }

    @Test
    public void fieldTypeTest1() throws InvalidMapFileException {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                GameVector v = new GameVector(j - i/2, i);
                assertEquals(map1.getFieldAt(v), Map.charToField(MAPFILE1.charAt(i * 11 + j)));
            }
        }
    }

    @Test
    public void fieldTypeTest2()  throws InvalidMapFileException {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                GameVector v = new GameVector(j - i/2, i);
                assertEquals(map2.getFieldAt(v), Map.charToField(MAPFILE2.charAt(i * 11 + j)));
            }
        }
    }

    /*
    @Test(expected = IllegalArgumentException.class)
    public void addFairySpawnInvalidAlreadyIncludedTest() {
        List<GameVector> fairySpawns = map1.getFairySpawns();
        if (fairySpawns.contains(new GameVector(0, 3)))
            map1.addFairySpawn(new GameVector(0, 3));
        else
            fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFairySpawnInvalidFieldTest1() {
        GameVector v = new GameVector(0, 0);
        List<GameVector> fairySpawns = map1.getFairySpawns();
        if (fairySpawns.contains(v))
            fail();
        else
            map1.addFairySpawn(v);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFairySpawnInvalidFieldTest2() {
        GameVector v = new GameVector(0, 4);
        List<GameVector> fairySpawns = map1.getFairySpawns();
        if (fairySpawns.contains(v))
            fail();
        else
            map1.addFairySpawn(v);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFairySpawnInvalidFieldTest3() {
        GameVector v = new GameVector(3, 5);
        List<GameVector> fairySpawns = map1.getFairySpawns();
        if (fairySpawns.contains(v))
            fail();
        else
            map1.addFairySpawn(v);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFairySpawnInvalidFieldTest4() {
        GameVector v = new GameVector(6, 7);
        List<GameVector> fairySpawns = map1.getFairySpawns();
        if (fairySpawns.contains(v))
            fail();
        else
            map1.addFairySpawn(v);
    }
    */

    @Test
    public void fairySpawnTest1() {
        List<GameVector> fairySpawns = map1.getFairySpawns();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                GameVector v = new GameVector(j - i/2, i);
                if (map1.getFieldAt(v) == Field.HEAL)
                    assertTrue(fairySpawns.contains(v));
                else
                    assertFalse(fairySpawns.contains(v));
            }
        }
    }

    @Test
    public void fairySpawnTest2() {
        List<GameVector> fairySpawns = map2.getFairySpawns();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                GameVector v = new GameVector(j - i/2, i);
                if (map2.getFieldAt(v) == Field.HEAL)
                    assertTrue(fairySpawns.contains(v));
                else
                    assertFalse(fairySpawns.contains(v));
            }
        }
    }

    /*
    // BoarSpawn on Field .
    @Test(expected = IllegalArgumentException.class)
    public void addBoarSpawnInvalidAlreadyIncludedTest1() {
        List<GameVector> boarSpawns = map1.getBoarSpawns();
        if (boarSpawns.contains(new GameVector(1, 2)))
            map1.addBoarSpawn(new GameVector(1, 2));
        else
            fail();
    }

    // BoarSpawn on Field X
    @Test(expected = IllegalArgumentException.class)
    public void addBoarSpawnInvalidAlreadyIncludedTest2() {
        List<GameVector> boarSpawns = map2.getBoarSpawns();
        if (boarSpawns.contains(new GameVector(3, 6)))
            map2.addBoarSpawn(new GameVector(3, 6));
        else
            fail();
    }

    // BoarSpawn on Field T
    @Test(expected = IllegalArgumentException.class)
    public void addBoarSpawnInvalidAlreadyIncludedTest3() {
        List<GameVector> boarSpawns = map2.getBoarSpawns();
        if (boarSpawns.contains(new GameVector(4, 3)))
            map2.addBoarSpawn(new GameVector(4, 3));
        else
            fail();
    }

    // BoarSpawn on Field A
    @Test(expected = IllegalArgumentException.class)
    public void addBoarSpawnInvalidAlreadyIncludedTest4() {
        List<GameVector> boarSpawns = map1.getBoarSpawns();
        if (boarSpawns.contains(new GameVector(1, 2)))
            map1.addBoarSpawn(new GameVector(1, 2));
        else
            fail();
    }

    // BoarSpawn on Field & //fix to %
    @Test(expected = IllegalArgumentException.class)
    public void addBoarSpawnInvalidAlreadyIncludedTest5() {
        List<GameVector> boarSpawns = map2.getBoarSpawns();
        if (boarSpawns.contains(new GameVector(1, 3)))
            map2.addBoarSpawn(new GameVector(1, 3));
        else
            fail();
    }

    // invalid FieldType: ~
    @Test(expected = IllegalArgumentException.class)
    public void addBoarSpawnInvalidFieldTest1() {
        List<GameVector> boarSpawns = map1.getBoarSpawns();
        if (boarSpawns.contains(new GameVector(7, 0)))
            fail();
        else
            map1.addFairySpawn(new GameVector(7, 0));
    }

    // invalid FieldType: _
    @Test(expected = IllegalArgumentException.class)
    public void addBoarSpawnInvalidFieldTest2() {
        List<GameVector> boarSpawns = map1.getBoarSpawns();
        if (boarSpawns.contains(new GameVector(8, 0)))
            fail();
        else
            map1.addFairySpawn(new GameVector(8, 0));
    }

    // invalid FieldType: *
    @Test(expected = IllegalArgumentException.class)
    public void addBoarSpawnInvalidFieldTest3() {
        List<GameVector> boarSpawns = map1.getBoarSpawns();
        if (boarSpawns.contains(new GameVector(1, 9)))
            fail();
        else
            map1.addFairySpawn(new GameVector(1, 9));
    }

    // invalid FieldType: $
    @Test(expected = IllegalArgumentException.class)
    public void addBoarSpawnInvalidFieldTest4() {
        List<GameVector> boarSpawns = map1.getBoarSpawns();
        if (boarSpawns.contains(new GameVector(-4, 9)))
            fail();
        else
            map1.addFairySpawn(new GameVector(-4, 9));
    }

    // invalid FieldType: +
    @Test(expected = IllegalArgumentException.class)
    public void addBoarSpawnInvalidFieldTest5() {
        List<GameVector> boarSpawns = map1.getBoarSpawns();
        if (boarSpawns.contains(new GameVector(0, 2)))
            fail();
        else
            map1.addFairySpawn(new GameVector(0, 2));
    }

    // invalid FieldType: #
    @Test(expected = IllegalArgumentException.class)
    public void addBoarSpawnInvalidFieldTest6() {
        List<GameVector> boarSpawns = map1.getBoarSpawns();
        if (boarSpawns.contains(new GameVector(8, 2)))
            fail();
        else
            map1.addFairySpawn(new GameVector(8, 2));
    }
    */

    // valid FieldType: ' '
    @Test
    public void addBoarValidTest1() {
        List<GameVector> boarSpawns = map1.getBoarSpawns();
        if (boarSpawns.contains(new GameVector(7, 0)))
            fail();
        else
            map1.addBoarSpawn(new GameVector(7, 0));
        assertTrue(map1.getBoarSpawns().contains(new GameVector(7, 0)));
    }

    // valid FieldType: x
    @Test
    public void addBoarValidTest2() {
        List<GameVector> boarSpawns = map1.getBoarSpawns();
        if (boarSpawns.contains(new GameVector(1, 0)))
            fail();
        else
            map1.addBoarSpawn(new GameVector(1, 0));
        assertTrue(map1.getBoarSpawns().contains(new GameVector(1, 0)));
    }

    // valid FieldType: t
    @Test
    public void addBoarValidTest3() {
        List<GameVector> boarSpawns = map1.getBoarSpawns();
        if (boarSpawns.contains(new GameVector(0, 0)))
            fail();
        else
            map1.addBoarSpawn(new GameVector(0, 0));
        assertTrue(map1.getBoarSpawns().contains(new GameVector(0, 0)));
    }

    // valid FieldType: ^
    @Test
    public void addBoarValidTest4() {
        List<GameVector> boarSpawns = map1.getBoarSpawns();
        if (boarSpawns.contains(new GameVector(0, 2)))
            fail();
        else
            map1.addBoarSpawn(new GameVector(0, 2));
        assertTrue(map1.getBoarSpawns().contains(new GameVector(0, 2)));
    }

    // valid FieldType: &
    @Test
    public void addBoarValidTest5() {
        List<GameVector> boarSpawns = map2.getBoarSpawns();
        if (boarSpawns.contains(new GameVector(2, 2)))
            fail();
        else
            map2.addBoarSpawn(new GameVector(2, 2));
        assertTrue(map2.getBoarSpawns().contains(new GameVector(2, 2)));
    }

    @Test
    public void boarSpawnTest1() {
        List<GameVector> boarSpawns = map1.getBoarSpawns();
        assertTrue(boarSpawns.contains(new GameVector(2, 0)));
        assertTrue(boarSpawns.contains(new GameVector(5, 0)));
        assertTrue(boarSpawns.contains(new GameVector(6, 0)));
        assertTrue(boarSpawns.contains(new GameVector(1, 2)));
        assertTrue(boarSpawns.contains(new GameVector(1, 3)));
        assertTrue(boarSpawns.contains(new GameVector(0, 4)));
        assertTrue(boarSpawns.contains(new GameVector(2, 4)));
        assertTrue(boarSpawns.contains(new GameVector(-2, 9)));
        assertFalse(boarSpawns.contains(new GameVector(0, 0)));
        assertFalse(boarSpawns.contains(new GameVector(5, 9)));
        assertFalse(boarSpawns.contains(new GameVector(2, 2)));
        assertFalse(boarSpawns.contains(new GameVector(0, 8)));
        assertFalse(boarSpawns.contains(new GameVector(5, 4)));
        assertFalse(boarSpawns.contains(new GameVector(-2, 6)));
        assertFalse(boarSpawns.contains(new GameVector(6, 5)));
        assertFalse(boarSpawns.contains(new GameVector(-1, 5)));
        assertFalse(boarSpawns.contains(new GameVector(9, 0)));
    }

    @Test
    public void boarSpawnTest2() {
        List<GameVector> boarSpawns = map2.getBoarSpawns();
        assertTrue(boarSpawns.contains(new GameVector(1, 3)));
        assertTrue(boarSpawns.contains(new GameVector(4, 3)));
        assertTrue(boarSpawns.contains(new GameVector(-2, 4)));
        assertTrue(boarSpawns.contains(new GameVector(-2, 5)));
        assertTrue(boarSpawns.contains(new GameVector(1, 5)));
        assertTrue(boarSpawns.contains(new GameVector(-2, 6)));
        assertTrue(boarSpawns.contains(new GameVector(1, 6)));
        assertTrue(boarSpawns.contains(new GameVector(3, 6)));
        assertTrue(boarSpawns.contains(new GameVector(1, 9)));
        assertFalse(boarSpawns.contains(new GameVector(2, 3)));
        assertFalse(boarSpawns.contains(new GameVector(2, 0)));
        assertFalse(boarSpawns.contains(new GameVector(4, 7)));
        assertFalse(boarSpawns.contains(new GameVector(1, 8)));
        assertFalse(boarSpawns.contains(new GameVector(4, 9)));
        assertFalse(boarSpawns.contains(new GameVector(2, 1)));
        assertFalse(boarSpawns.contains(new GameVector(9, 1)));
    }

    @Test
    public void setFieldTest() {
        // One GameVector as Field for each FieldType
        GameVector[] gameVectors = {new GameVector(6, 0), new GameVector(7, 0), new GameVector(0, 3), new GameVector(3, 3), new GameVector(4, 0),
                new GameVector(5, 0), new GameVector(0, 0), new GameVector(1, 0), new GameVector(2, 0), new GameVector(3, 0),
                new GameVector(6, 1), new GameVector(0, 2), new GameVector(1, 2), new GameVector(-3, 7), new GameVector(-4, 9),
                new GameVector(1, 9)};

        for (GameVector v : gameVectors) {
            map1.setFieldAt(v, Field.GRASS);
            assertEquals(map1.getFieldAt(v), Field.GRASS);

            map1.setFieldAt(v, Field.HEAL);
            assertEquals(map1.getFieldAt(v), Field.HEAL);

            map1.setFieldAt(v, Field.HEAL_DISABLED);
            assertEquals(map1.getFieldAt(v), Field.HEAL_DISABLED);

            map1.setFieldAt(v, Field.HILL);
            assertEquals(map1.getFieldAt(v), Field.HILL);

            map1.setFieldAt(v, Field.ROCK);
            assertEquals(map1.getFieldAt(v), Field.ROCK);

            map1.setFieldAt(v, Field.SWAMP);
            assertEquals(map1.getFieldAt(v), Field.SWAMP);

            map1.setFieldAt(v, Field.BUSH);
            assertEquals(map1.getFieldAt(v), Field.BUSH);

            map1.setFieldAt(v, Field.TREE);
            assertEquals(map1.getFieldAt(v), Field.TREE);

            map1.setFieldAt(v, Field.WATER);
            assertEquals(map1.getFieldAt(v), Field.WATER);

            map1.setFieldAt(v, Field.ICE);
            assertEquals(map1.getFieldAt(v), Field.ICE);

            map1.setFieldAt(v, Field.LAVA);
            assertEquals(map1.getFieldAt(v), Field.LAVA);

            map1.setFieldAt(v, Field.CURSE);
            assertEquals(map1.getFieldAt(v), Field.CURSE);
        }
    }

    /*
    @Test
    public void copyMapTest() {
        Map mapCopy = new Map(map1);

        HashMap<GameVector, Field> fields = mapCopy.getFields();
        assertTrue(fields.equals(map1.getFields()));

        assertTrue(mapCopy.getBoarSpawns().equals(map1.getBoarSpawns()));
        assertTrue(mapCopy.getFairySpawns().equals(map1.getFairySpawns()));

        assertFalse("You should not simply copy the reference!", map1.getFields() == mapCopy.getFields());
        assertFalse("You should not simply copy the reference!", map1.getBoarSpawns() == mapCopy.getBoarSpawns());
        assertFalse("You should not simply copy the reference!", map1.getFairySpawns() == mapCopy.getFairySpawns());
    }
    */

    /*
    @Test
    public void testMapEquals(){
        Map map3 = new Map(map1);
        assertTrue(map1.equals(map3));
        assertEquals(map1.hashCode(),map3.hashCode());
    }
    */
}
