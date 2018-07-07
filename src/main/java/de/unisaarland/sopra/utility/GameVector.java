package de.unisaarland.sopra.utility;
import de.unisaarland.sopra.Direction;

/**
 * Created by Team 14 on 12.09.2016.
 */
public class GameVector {

    private int x;
    private int y;

    /**
     * Creates a new instance of {@link GameVector} with given parameters.
     *
     * @param x X Position.
     * @param y Y Position.
     */
    public GameVector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns x position.
     *
     * @return X position of this {@link GameVector}.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns y position.
     *
     * @return Y position of this {@link GameVector}.
     */
    public int getY() {
        return y;
    }

    /**
     * Calculates the distance between this {@link GameVector} and the given parameter {@link GameVector}.
     *
     * @param gameVector
     * @return Distance between this {@link GameVector} and the parameter
     */
    public int distanceTo(GameVector gameVector) {
        int z1 = -(x + y);
        int z2 = -(gameVector.getX() + gameVector.getY());

        int xDif = Math.abs(gameVector.getX() - x);
        int yDif = Math.abs(gameVector.getY() - y);
        int zDif = Math.abs(z2 - z1);

        int d = Math.max(xDif, yDif);
        d = Math.max(d, zDif);

        return d;
    }

    /**
     * Converts this {@link GameVector} to a {@link Direction}.
     *
     * @return
     */
    public Direction toDirection() {
        if (x == 1 && y == -1) {
            return Direction.NORTH_EAST;
        } else {
            if (x == 1 && y == 0) {
                return Direction.EAST;
            } else {
                if (x == 0 && y == 1) {
                    return Direction.SOUTH_EAST;
                } else {
                    if (x == -1 && y == 1) {
                        return Direction.SOUTH_WEST;
                    } else {
                        if (x == -1 && y == 0) {
                            return Direction.WEST;
                        } else {
                            if (x == 0 && y == -1) {
                                return Direction.NORTH_WEST;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Returns the sum of this {@link GameVector} and the parameter {@link GameVector}.
     *
     * @param other The summand.
     * @return Sum of this {@link GameVector} and the parameter.
     */
    public GameVector add(GameVector other) {
        if (other == null) {
            return null;
        }

        return new GameVector(x + other.getX(), y + other.getY());
    }

    public GameVector sub(GameVector other) {
        if (other == null) {
            return null;
        }

        return new GameVector(x - other.getX(), y - other.getY());
    }

    public boolean equals(Object o) {
        if (!(o instanceof GameVector)) {
            return false;
        }

        GameVector gameVector = (GameVector) o;

        return y == gameVector.y && x == gameVector.x;
    }

    public int hashCode() {
        int result = x;
        result = 997 * result + y;
        return result;
    }
}
