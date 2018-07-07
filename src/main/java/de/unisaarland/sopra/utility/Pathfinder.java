package de.unisaarland.sopra.utility;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.model.Field;
import de.unisaarland.sopra.model.FieldEffects;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.utility.DirectionHelper;
import de.unisaarland.sopra.utility.GameVector;

import java.util.*;

/**
 * Created by Team 14 on 26.09.2016.
 */
public class Pathfinder {

    public static Queue<Direction> findPath(GameVector start, GameVector end, Game game, CreatureType creatureType) {
        return findPath(start, end, game, creatureType, false, 0);
    }

    public static Queue<Direction> findPath(GameVector start, GameVector end, Game game, CreatureType creatureType, boolean goOnTo){
        return findPath(start, end, game, creatureType, goOnTo, 0);
    }

    public static Queue<Direction> findPath(GameVector start, GameVector end, Game game, CreatureType creatureType, boolean goOnTo, int distanceTo) {
        LinkedList<GameVector> open = new LinkedList<>();
        open.add(start);
        List<GameVector> closed = new LinkedList<>();
        HashMap<GameVector, GameVector> path = new HashMap<>();

        HashMap<GameVector, Integer> gScore = new HashMap<>();
        gScore.put(start, 0);

        HashMap<GameVector, Integer> fScore = new HashMap<>();
        fScore.put(start, start.distanceTo(end));

        while (!open.isEmpty()) {
            GameVector current = getLowest(open, fScore);
            if ((distanceTo > 0) && (end.distanceTo(current) <= distanceTo)){
                return revertPath(path, current);
            }
            if (current.equals(end) || (!goOnTo && end.distanceTo(current) <= 1)) {
                return revertPath(path, current);
            }

            open.remove(current);
            closed.add(current);
            for (Direction dir : Direction.values()) {
                GameVector neighbour = current.add(DirectionHelper.toVector(dir));

                Field f = game.getFieldAt(neighbour);
                if (closed.contains(neighbour) || !FieldEffects.canEnter(f, creatureType) || game.getCreatureByPosition(neighbour) != null) {
                    continue;
                }

                // Remove heal "dmg"
                int field_dmg = FieldEffects.getDamage(f, creatureType);
                if (field_dmg < 0) {
                    field_dmg = 0;
                }
                int cost = gScore.get(current) + FieldEffects.getMovementCostMultiplier(f, creatureType) + field_dmg;

                if (!open.contains(neighbour)) {
                    open.add(neighbour);
                } else if (cost >= (gScore.containsKey(neighbour) ? gScore.get(neighbour) : Integer.MAX_VALUE)) {
                    continue;
                }

                path.put(neighbour, current);
                gScore.put(neighbour, cost);
                fScore.put(neighbour, cost + neighbour.distanceTo(end));
            }
        }

        return null;
    }

    private static Queue<Direction> revertPath(HashMap<GameVector, GameVector> path, GameVector current) {
        LinkedList<Direction> p = new LinkedList<>();
        GameVector last = current;

        while (path.containsKey(last)) {
            GameVector d = last.sub(path.get(last));
            p.add(d.toDirection());
            last = path.get(last);
        }

        Collections.reverse(p);

        return p;
    }

    private static GameVector getLowest(List<GameVector> list, HashMap<GameVector, Integer> fScore) {
        int curMin = Integer.MAX_VALUE;
        GameVector value = null;

        for (GameVector v : list) {
            int m = (fScore.containsKey(v) ? fScore.get(v) : Integer.MAX_VALUE);
            if (m <= curMin) {
                value = v;
                curMin = m;
            }
        }

        return value;
    }
}
