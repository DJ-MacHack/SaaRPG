package de.unisaarland.sopra.utility;

import de.unisaarland.sopra.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Team 14 on 27.09.2016.
 */
public class KiHelper {

    private KiHelper(){}

    public static final  Creature nearestCreature(Game game, Creature myCreature, GameVector pos, int maxDist) {
        List<Creature> creatures = game.getCreatures();
        int dist = (maxDist <= 0 ? Integer.MAX_VALUE : maxDist);
        Creature creature = null;

        for (Creature c : creatures) {
            if (c == myCreature) {
                continue;
            }

            int d = pos.distanceTo(c.getPosition());
            if (d < dist) {
                creature = c;
                dist = d;
            }
        }

        return creature;
    }

    public static final  Pc nearestMonster(Game game, Pc myMonster, GameVector pos, int maxDist) {
        List<Pc> monsters = game.getMonsters();
        int dist = (maxDist <= 0 ? Integer.MAX_VALUE : maxDist);
        Pc monster = null;

        for (Pc pc : monsters) {
            if (pc == myMonster) {
                continue;
            }

            int d = pos.distanceTo(pc.getPosition());
            if (d < dist) {
                monster = pc;
                dist = d;
            }
        }

        return monster;
    }

    public static final Pc nearestEnemy(Game game, Pc myMonster, GameVector pos, int maxDist) {
        List<Pc> monsters = game.getMonsters();
        int dist = (maxDist <= 0 ? Integer.MAX_VALUE : maxDist);
        Pc monster = null;

        for (Pc pc : monsters) {
            String t1 = game.getPlayerByMonsterId(pc.getId()).getTeamName();
            String t2 = game.getPlayerByMonsterId(myMonster.getId()).getTeamName();

            if (pc == myMonster || t1.equals(t2)) {
                continue;
            }

            int d = pos.distanceTo(pc.getPosition());
            if (d < dist) {
                monster = pc;
                dist = d;
            }
        }

        return monster;
    }

    public static final  Npc nearestNpc(Game game, GameVector pos, int maxDist) {
        List<Npc> npcs = game.getNpcs();
        int dist = (maxDist <= 0 ? Integer.MAX_VALUE : maxDist);
        Npc npc = null;

        for (Npc c : npcs) {
            int d = pos.distanceTo(c.getPosition());
            if (d < dist) {
                npc = c;
                dist = d;
            }
        }

        return npc;
    }

    public static final  GameVector nearestField(Game game, Creature myCreature, GameVector pos, Field field) {
        HashMap<GameVector, Field> fields = game.getMap().getFields();
        int dist = Integer.MAX_VALUE;
        GameVector nearest = null;

        for (Map.Entry<GameVector, Field> f : fields.entrySet()) {
            Creature creatureOnField = game.getCreatureByPosition(f.getKey());
            if (f.getValue() == field && (creatureOnField == null || creatureOnField == myCreature)) {
                int d = pos.distanceTo(f.getKey());
                if (d < dist) {
                    nearest = f.getKey();
                    dist = d;
                }
            }
        }

        return nearest;
    }
}
