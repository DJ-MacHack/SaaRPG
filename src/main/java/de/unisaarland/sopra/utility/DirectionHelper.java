package de.unisaarland.sopra.utility;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.utility.GameVector;

/**
 * Created by Team 14 on 12.09.2016.
 */
public class DirectionHelper {

    private DirectionHelper() {
    }

    public static GameVector toVector(Direction direction) {
        switch (direction) {
            case EAST:
                return new GameVector(1, 0);
            case NORTH_EAST:
                return new GameVector(1, -1);
            case NORTH_WEST:
                return new GameVector(0, -1);
            case WEST:
                return new GameVector(-1, 0);
            case SOUTH_WEST:
                return new GameVector(-1, 1);
            default:
                return new GameVector(0, 1);
        }
    }

    public static Direction getCounterClockwise(Direction direction) {
        switch (direction) {
            case EAST:
                return Direction.NORTH_EAST;
            case NORTH_EAST:
                return Direction.NORTH_WEST;
            case NORTH_WEST:
                return Direction.WEST;
            case WEST:
                return Direction.SOUTH_WEST;
            case SOUTH_WEST:
                return Direction.SOUTH_EAST;
            default:
                return Direction.EAST;
        }
    }
}
