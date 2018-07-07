package de.unisaarland.sopra.model;

import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.utility.InvalidMapFileException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by DJ MacHack on 12.09.2016.
 */
public class Map {

    private HashMap<GameVector, Field> fields;
    private List<GameVector> fairySpawn;
    private List<GameVector> boarSpawn;


    /**
     * create new map
     */
    public Map() {
        this.boarSpawn = new ArrayList<>();
        this.fairySpawn = new ArrayList<>();
        this.fields = new HashMap<>();
    }

    /**
     * deep copy of map
     *
     * @param map
     */
    public Map(Map map) {
        this.fields = new HashMap<>();
        for(java.util.Map.Entry<GameVector, Field> field : map.getFields().entrySet()){
            this.fields.put(field.getKey(), field.getValue());
        }

        // copy all fairySpawns
        this.fairySpawn = new ArrayList<>();
        for (GameVector fairySpawn :  map.getFairySpawns()) {
            this.fairySpawn.add(fairySpawn);
        }

        // copy all boarSpawns
        this.boarSpawn = new ArrayList<>();
        for (GameVector boarSpawn : map.getBoarSpawns()) {
            this.boarSpawn.add(boarSpawn);
        }
    }

    /**
     * shows a field at gameVector
     *
     * @param gameVector
     * @return
     */
    public final Field getFieldAt(GameVector gameVector) throws IllegalArgumentException {
        if (fields.containsKey(gameVector))
            return fields.get(gameVector);
        else
            return null;
    }

    /**
     * set a field at gameVector
     *
     * @param gameVector
     * @param field
     */
    public final void setFieldAt(GameVector gameVector, Field field) {
        fields.put(gameVector, field);
    }

    /**
     * shows fairy spawn list
     *
     * @return
     */
    public List<GameVector> getFairySpawns() {
        return this.fairySpawn;
    }

    /**
     * shows boar spawn list
     *
     * @return
     */
    public List<GameVector> getBoarSpawns() {
        return this.boarSpawn;
    }

    /**
     * adds fairies
     *
     * @param vec
     */
    public void addFairySpawn(GameVector vec) throws IllegalArgumentException {
        //Field test = this.fields.get(vec);
        //assert(test == Field.HEAL || test == Field.HEAL_DISABLED);
        //assert(!fairySpawn.contains(vec));
        this.fairySpawn.add(vec);
    }

    /**
     * adds boars
     *
     * @param vec
     */
    public void addBoarSpawn(GameVector vec) throws IllegalArgumentException {
        //Field test = this.fields.get(vec);
        //assert(test == Field.BUSH || test == Field.TREE || test == Field.SWAMP || test == Field.HILL || test == Field.GRASS);
        //assert(!boarSpawn.contains(vec));
        this.boarSpawn.add(vec);
    }

    /**
     * gets fields hashMap
     *
     * @return
     */
    public HashMap<GameVector, Field> getFields() {
        return this.fields;
    }

    public static Field charToField(char c) throws InvalidMapFileException {
        switch (c) {
            case ' ':
                return Field.GRASS;
            case '.':
                return Field.GRASS;
            case '0':
                return Field.GRASS;
            case '1':
                return Field.GRASS;
            case '2':
                return Field.GRASS;
            case '3':
                return Field.GRASS;
            case '4':
                return Field.GRASS;
            case '5':
                return Field.GRASS;
            case '6':
                return Field.GRASS;
            case '7':
                return Field.GRASS;
            case '8':
                return Field.GRASS;
            case '9':
                return Field.GRASS;
            case '+':
                return Field.HEAL;
            case '#':
                return Field.ROCK;
            case '%':
                return Field.SWAMP;
            case '&':
                return Field.SWAMP;
            case 'x':
                return Field.BUSH;
            case 'X':
                return Field.BUSH;
            case 't':
                return Field.TREE;
            case 'T':
                return Field.TREE;
            case '~':
                return Field.WATER;
            case '^':
                return Field.HILL;
            case 'A':
                return Field.HILL;
            case '_':
                return Field.ICE;
            case '*':
                return Field.LAVA;
            case '$':
                return Field.CURSE;
            default:
                throw new InvalidMapFileException(String.format("Wrong char: %s!",c));
        }
    }

    public static byte fieldToChar(Field field) {
        switch (field){

            case GRASS:
                return ' ';
            case HILL:
                return '^';
            case HEAL:
                return '+';
            case ROCK:
                return '#';
            case SWAMP:
                return '%';
            case BUSH:
                return 'x';
            case TREE:
                return 't';
            case WATER:
                return '~';
            case ICE:
                return '_';
            case LAVA:
                return '*';
            case CURSE:
                return '$';
            case HEAL_DISABLED:
                return '+';
            default:
                throw new IllegalArgumentException("We <3 Findbugs");
        }
    }

    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Map map = (Map) o;

        if (fields != null ? !fields.equals(map.fields) : map.fields != null) {
            return false;
        }
        if (fairySpawn != null ? !fairySpawn.equals(map.fairySpawn) : map.fairySpawn != null) {
            return false;
        }
        return boarSpawn != null ? boarSpawn.equals(map.boarSpawn) : map.boarSpawn == null;

    }

    @Override
    public int hashCode() {
        int result = fields != null ? fields.hashCode() : 0;
        final int MAGICPRIM = 31;

        result = MAGICPRIM * result + (fairySpawn != null ? fairySpawn.hashCode() : 0);
        result = MAGICPRIM * result + (boarSpawn != null ? boarSpawn.hashCode() : 0);
        return result;
    }
    */
}
