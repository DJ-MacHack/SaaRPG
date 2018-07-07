package de.unisaarland.sopra.utility;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.model.Field;
import de.unisaarland.sopra.model.FieldEffects;
import de.unisaarland.sopra.model.Game;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * WARNING: This pathfinder is not that good
 * It gets its job done in a lot of cases, but not always.
 * Also there is no guarantee that the path found is optimal
 * One advantage is the algorithm should be faster
 * Additional testing is not completed so far
 *
 * USAGE: Create an instance of NoviceGuide with an List of illegal fields as parameter
 * IncludeTarget decides if a creature is allowed to stand at the target location. If this is
 * disabled and at the target location stands a creature the algorithm will stop
 * then use find path to get a list of directions
 *
 * Created on 26.09.16.
 *
 * @author Henrik Paul Koehn
 */
public class SimplePathfinder {

    private Boolean includeTarget;
    private List<Field> illegalFields;
    private List<Direction> dirs = new LinkedList<>();
    private Game game = null;
    private CreatureType ct = null;

    public SimplePathfinder(List<Field> illegalFields, Boolean includeTarget) {
        this.illegalFields = illegalFields;
        this.includeTarget = includeTarget;
    }

    public Queue<Direction> findPath(Game game, CreatureType ct, GameVector src, GameVector dst) {
        dirs.clear();
        this.game = game;
        this.ct = ct;

        GameVector movVec = dst.sub(src);
        recPath(src, movVec);
        Collections.reverse(dirs);
        Queue<Direction> resultQueue = new LinkedList<>();
        resultQueue.addAll(dirs);
        return resultQueue;
    }

    private boolean recPath(GameVector src, GameVector v) {
        // NE
        if (v.getX() > 0 && v.getY() < 0 && canEnter(src.add(new GameVector(1, -1))) && recPath( src.add(new GameVector(1, -1)), v.sub(new GameVector(1, -1)))) {
            this.dirs.add(Direction.NORTH_EAST);
            return true;
        }
        // SW
        else if (v.getX() < 0 && v.getY() > 0 && canEnter(src.add(new GameVector(-1, 1))) && recPath(src.add(new GameVector(-1, 1)),v.sub(new GameVector(-1, 1)))) {
            this.dirs.add(Direction.SOUTH_WEST);
            return true;
        }
        // E
        else if (v.getX() > 0 && canEnter(src.add(new GameVector(1, 0))) && recPath(src.add(new GameVector(1, 0)), v.sub(new GameVector(1, 0)))){
            this.dirs.add(Direction.EAST);
            return true;
        }
        // SE
        else if (v.getY() > 0 && canEnter(src.add(new GameVector(0, 1))) && recPath(src.add(new GameVector(0, 1)), v.sub(new GameVector(0, 1)))) {
            this.dirs.add(Direction.SOUTH_EAST);
            return true;
        }
        // W
        else if (v.getX() < 0 && canEnter(src.add(new GameVector(-1, 0))) && recPath(src.add(new GameVector(-1, 0)), v.sub(new GameVector(-1, 0)))) {
            this.dirs.add(Direction.WEST);
            return true;
        }
        // NW
        else if (v.getY() < 0 && canEnter(v.add(new GameVector(0, -1))) && recPath(src.add(new GameVector(0, -1)), v.add(new GameVector(0, -1)))){
            this.dirs.add(Direction.NORTH_WEST);
            return true;
        }
        // target location found
        else if (includeTarget) {
            if (Math.abs(v.getX()) + Math.abs(v.getY()) < 2 || v.getX() == 1 && v.getY() == -1 || v.getX() == -1 && v.getY() == 1){
                this.dirs.add(v.toDirection());
                return true;
            }
            else {
                return false;
            }
        }
        else return (v.getX() == 0 && v.getY() == 0);


    }

    private boolean canEnter(GameVector v) {
        Field field = this.game.getFieldAt(v);
        return field != null
                && FieldEffects.canEnter(field, this.ct)
                && (game.getCreatureByPosition(v) == null)
                && !illegalFields.contains(field);
    }

}

