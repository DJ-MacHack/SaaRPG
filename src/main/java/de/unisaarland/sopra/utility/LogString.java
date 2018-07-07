package de.unisaarland.sopra.utility;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.Creature;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.model.Pc;

import static de.unisaarland.sopra.model.CreatureType.BOAR;

/**
 * Created by Lukas Kirschner on 9/23/16.
 */
public class LogString {

    private final static String PCFORMATSTRING = "%s(%d) (Team: %s)";
    private final static String SERVERPREFIX = "[Server]";
    public static final String DEBUGPREFIX = "[Debugger]";
    public final static String INFOPREFIX = "[Info]";

    private LogString() {
    }

    public static String createVictimName(Game myGame, int victimId) {
        if (myGame.getMonsterById(victimId) != null) {
            return createVictimName(
                    myGame.getPlayerByMonsterId(victimId).getName(),
                    victimId,
                    myGame.getPlayerByMonsterId(victimId).getTeamName());
        } else {
            Creature c = myGame.getCreatureById(victimId);
            if (c == null){
                return String.format("ERROR1|%d", victimId);
            }
            if (c.getCreatureType() == BOAR) {
                return String.format("Boar(%d)", victimId);
            } else {
                return String.format("Fairy(%d)", victimId);
            }
        }
    }

    public static String createVictimName(String monsterName, int monsterId, String teamName) {
        return String.format(PCFORMATSTRING, monsterName, monsterId, teamName); //Playername Output for PC Characters
    }

    public static String createVictimName(Game myGame, int self, Direction dir) {
        GameVector target = myGame.getMonsterById(self).getPosition().add(DirectionHelper.toVector(dir));
        Creature c = myGame.getCreatureByPosition(target);
        if (c == null){
            return String.format("ERROR1|%s", dir.toString());
        }
        return createVictimName(myGame, c.getId());
    }

    public static String createVictimName(Game myGame, int x, int y) {
        Creature c = myGame.getCreatureByPosition(new GameVector(x,y));
        if (c == null){
            return String.format("ERROR1|%d|%d",x,y); //Error1 - Invalid Coordinates
        }
        return createVictimName(myGame,c.getId());
    }


    public static String getReadableDirectionString(Direction dir) {
        switch (dir) {

            case NORTH_WEST:
                return "northwest";
            case WEST:
                return "west";
            case SOUTH_WEST:
                return "southwest";
            case SOUTH_EAST:
                return "southeast";
            case EAST:
                return "east";
            case NORTH_EAST:
            default:
                return "northeast";
        }
    }

    public static void printEventLog(Event e, Client cli){
        System.out.printf("%s %s%n", SERVERPREFIX, e.getText(cli)); //PRINT OUT LOG
    }

    public static void printInvalidEnterDebugLog(String input){
        printDebug(String.format("Did not send: %s%n",input));
    }

    public static void printDebug(String input){
        System.out.printf("%s %s%n", DEBUGPREFIX, input);
    }

    public static void printInfo(String input){
        System.out.printf("%s %s%n", INFOPREFIX, input);
    }
}
