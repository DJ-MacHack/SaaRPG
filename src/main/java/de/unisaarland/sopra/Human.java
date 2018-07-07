package de.unisaarland.sopra;

import de.unisaarland.sopra.messages.*;
import de.unisaarland.sopra.messages.attack.*;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.model.Pc;
import de.unisaarland.sopra.utility.LogString;

import java.io.Console;

/**
 * Created by Lukas Kirschner and DJ MacHack
 */
public class Human extends Client {

    private final static boolean DEBUGMODE = true; //Set verbose level

    public static final String MOTD = "Welcome to SaaRPG! Type \"help\" for a quick command reference.";
    public static final String HELPTEXT = "***SAARPG HUMAN PLAYER COMMAND REFERENCE***\n\n" +
            "***Commands:\n" +
            "bite <Dir> - Sends a bite command\n" +
            "blink <x> <y> - Sends a blink command with given target coordinates\n" +
            "burn - Sends a burn command\n" +
            "cast <x> <y> - Sends a cast command with given target coordinates\n" +
            "crush <Dir> - Sends a crush command\n" +
            "cut <Dir> - Sends a cut command\n" +
            "done|d|doneacting - Passes the round\n" +
            "move <Dir> - Moves your player to the given direction.\n" +
            "\tAliases:\n" +
            "\t<m|move> <Dir> - Moves your player in the given direction\n" +
            "\tmnw|mul|mol - Moves your player to north-west\n" +
            "\tmw|ml - Moves your player to west\n" +
            "\tmsw|mdl|mrl - Moves your player to south-west\n" +
            "\tmse|mdr|mrr - Moves your player to south-east\n" +
            "\tme|mr - Moves your player to east\n" +
            "\tmne|mur|mor - Moves your player to north-east\n" +
            "stab <Dir> - Sends a stab command\n" +
            "slash <Dir> - Sends a slash command\n" +
            "stare <Dir> - Sends a stare command\n" +
            "claw <Dir> - Sends a claw command\n" +
            "shoot <Dir> - sends a shoot command\n" +
            "singe <Dir> - Sends a singe command\n" +
            "stat|status|stats|who|players - Prints out a quick overview of players alive\n" +
            "swap <x> <y> - Sends a swap command with given target coordinates\n" +
            "<warcry|cry> <message> - Sends a war cry with the given message\n" +
            "watch - Forfeits the game and turns yourself into an observer\n\n" +
            "***Directions:\n" +
            "north_west|northwest|nw - North west (the top left tile)\n" +
            "west|w - West (the left tile)\n" +
            "south_west|southwest|sw - South west (the bottom left tile)\n" +
            "south_east|southeast|se - South east (the bottom right tile)\n" +
            "east|e - East (the right tile)\n" +
            "north_east|northeast|ne - North east (the top right tile)\n\n" +
            "***Rules:\n" +
            "For game rules, rtfm! ;)\n\n";

    CreatureType creature;
    String name;
    String team;
    MonsterType monster;

    /**
     * create a new human client
     *
     * @param host    IP or host
     * @param port    port number
     * @param timeout timeout in ms
     */
    public Human(String host, int port, int timeout, String name, String teamname, CreatureType creaturetype) {
        super(host, port, timeout);
        this.name = (name != null) ? name : requestPlayerName();
        this.team = (teamname != null) ? teamname : requestTeamName();
        this.creature = (creaturetype != null) ? creaturetype : requestCreatureType();
        MonsterType monster = MonsterType.valueOf(this.creature.name());
        this.monster = monster;
        register();
    }

    private CreatureType requestCreatureType() {
        System.out.printf("Enter your CreatureType: > ");
        String t = System.console().readLine();
        return CreatureType.valueOf(t.toUpperCase());
    }

    private String requestTeamName() {
        System.out.printf("Enter your TeamName: > ");
        return System.console().readLine();
    }

    private String requestPlayerName() {
        System.out.printf("Enter your PlayerName: > ");
        return System.console().readLine();
    }

    @Override
    public void actNow() {
        Pc thisPlayer = this.game.getMonsterById(this.ownCreatureId);
        System.out.printf("%dEP,%dHP > ", thisPlayer.getEnergy(), thisPlayer.getHp());
        String input = System.console().readLine();

        if (!parseInputString(input)) {
            if (DEBUGMODE) {
                LogString.printInvalidEnterDebugLog(input);
            }
            actNow();
        }
    }

    public void run() {
        System.out.printf("%s%n%n", MOTD);
        while (isRunning()) {
            Event event = this.clientConnection.nextEvent();
            LogString.printEventLog(event,this);

            if (event.validateEvent(game, this)) {
                event.executeEvent(game, this);
            } /*else {
                if (DEBUGMODE) {
                    //    System.out.printf("%s Event could not be executed: %s%n", DEBUGPREFIX, event.getClass().getName());
                }
            }*/
        }
        clientConnection.close();
    }

    /**
     * Parses the given user input string and executes the given commands.
     * Arguments must be seperated by whitespaces
     *
     * @param input The user input string, eg from System.console().readLine()
     * @return false if string could not be parsed
     */
    private boolean parseInputString(String input) {
        if (input == null) {
            throw new IllegalArgumentException();
        }
        String[] normalizedInput = input.toLowerCase().split("\\s+");
        switch (normalizedInput[0]) {
            //case "watch": //Disabled because server keeps crashing
            //    watch();
            //    break;
            case "m": //m is short alias of move
            case "move":
                if ((normalizedInput.length != 2) || (parseDirection(normalizedInput[1]) == null)) {
                    return false;
                }
                return move(Direction.valueOf(parseDirection(normalizedInput[1])));
            case "mnw": //MOVE ALIASES----------------------
            case "mol":
            case "mul":
                return move(Direction.NORTH_WEST);
            case "mw":
            case "ml":
                return move(Direction.WEST);
            case "msw":
            case "mrl":
            case "mdl":
                return move(Direction.SOUTH_WEST);
            case "mse":
            case "mdr":
            case "mrr":
                return move(Direction.SOUTH_EAST);
            case "me":
            case "mr":
                return move(Direction.EAST);
            case "mne":
            case "mur":
            case "mor":
                return move(Direction.NORTH_EAST);
            case "stab":
                if ((normalizedInput.length != 2) || (parseDirection(normalizedInput[1]) == null)) {
                    return false;
                }
                return stab(Direction.valueOf(parseDirection(normalizedInput[1])));
            case "slash":
                if ((normalizedInput.length != 2) || (parseDirection(normalizedInput[1]) == null)) {
                    return false;
                }
                return slash(Direction.valueOf(parseDirection(normalizedInput[1])));

            case "stare":
                if ((normalizedInput.length != 2) || (parseDirection(normalizedInput[1]) == null)) {
                    return false;
                }
                return stare(Direction.valueOf(parseDirection(normalizedInput[1])));
            case "claw":
                if ((normalizedInput.length != 2) || (parseDirection(normalizedInput[1]) == null)) {
                    return false;
                }
                return claw(Direction.valueOf(parseDirection(normalizedInput[1])));

            case "crush":
                if ((normalizedInput.length != 2) || (parseDirection(normalizedInput[1]) == null)) {
                    return false;
                }
                return crush(Direction.valueOf(parseDirection(normalizedInput[1])));
            case "singe":
                if ((normalizedInput.length != 2) || (parseDirection(normalizedInput[1]) == null)) {
                    return false;
                }
                return singe(Direction.valueOf(parseDirection(normalizedInput[1])));
            case "shoot":
                if ((normalizedInput.length != 2) || (parseDirection(normalizedInput[1]) == null)) {
                    return false;
                }
                return shoot(Direction.valueOf(parseDirection(normalizedInput[1])));
            case "burn":
                return burn();
            case "cast":
                if (normalizedInput.length != 3) {
                    return false;
                }
                try {
                    return cast(Integer.parseInt(normalizedInput[1]), Integer.parseInt(normalizedInput[2]));
                } catch (NumberFormatException e) {
                    return false;
                }
            case "blink":
                if (normalizedInput.length != 3) {
                    return false;
                }
                try {
                    return blink(Integer.parseInt(normalizedInput[1]), Integer.parseInt(normalizedInput[2]));
                } catch (NumberFormatException e) {
                    return false;
                }
            case "swap":
                if (normalizedInput.length != 3) {
                    return false;
                }
                try {
                    return swap(Integer.parseInt(normalizedInput[1]), Integer.parseInt(normalizedInput[2]));
                } catch (NumberFormatException e) {
                    return false;
                }
            case "cut":
                if ((normalizedInput.length != 2) || (parseDirection(normalizedInput[1]) == null)) {
                    return false;
                }
                return cut(Direction.valueOf(parseDirection(normalizedInput[1])));
            case "bite":
                if ((normalizedInput.length != 2) || (parseDirection(normalizedInput[1]) == null)) {
                    return false;
                }
                return bite(Direction.valueOf(parseDirection(normalizedInput[1])));
            case "done":
            case "doneacting":
            case "d":
                done();
                break;
            case "warcry":
            case "cry":
                if ((normalizedInput.length < 2) || (input.length() < normalizedInput[0].length() + 1)) {
                    return false;
                }
                return cry(input.substring(normalizedInput[0].length() + 1));
            case "help":
            case "?":
            case "h":
                printHelp();
                return false;
            case "stat":
            case "status":
            case "who":
            case "players":
            case "stats":
                printPlayerInfo();
                return false;
            default:
                return false;
        }
        return true;
    }

    private void printPlayerInfo() { //Unfortunately not sortable
        System.out.printf("%n%5s%12s%12s%5s%12s%n", "ID", "PlayerName", "TeamName", "HP", "Type");
        for (Pc m : super.game.getMonsters()) {
            System.out.printf("%5d%12s%12s%5d%12s%n",
                    m.getId(),
                    super.game.getPlayerByMonsterId(m.getId()).getName(),
                    super.game.getPlayerByMonsterId(m.getId()).getTeamName(),
                    super.game.getMonsterById(m.getId()).getHp(),
                    super.game.getMonsterById(m.getId()).getCreatureType().toString());
        }
        System.out.printf("%n");
    }


    private String parseDirection(String s) {
        if (s == null) {
            return null;
        }
        String normalizedDirection = s.toLowerCase();
        switch (normalizedDirection) {
            case "north_east":
            case "northeast":
            case "ne":
                return "NORTH_EAST";
            case "north_west":
            case "northwest":
            case "nw":
                return "NORTH_WEST";
            case "west":
            case "w":
                return "WEST";
            case "south_west":
            case "southwest":
            case "sw":
                return "SOUTH_WEST";
            case "south_east":
            case "southeast":
            case "se":
                return "SOUTH_EAST";
            case "east":
            case "e":
                return "EAST";
            default:
                return null;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTeamName() {
        return team;
    }

    private boolean move(Direction direction) {
        Move e = new Move(null, this.ownCreatureId, direction);
        if (e.validateEvent(super.game, this)) {
            e.sendCommand(super.clientConnection);
            return true;
        }
        return false;
        //super.clientConnection.sendMove(direction);
    }

    private void register() {
        super.clientConnection.sendRegister(this.name, this.monster, this.team);
    }

    private void watch() {
        super.clientConnection.sendWatch();
    }

    private boolean stab(Direction direction) {
        Stab e = new Stab(null, super.ownCreatureId, direction);
        if (e.validateEvent(super.game, this)) {
            e.sendCommand(this.clientConnection);
            return true;
        }
        return false;
    }

    private boolean slash(Direction direction) {
        Slash e = new Slash(null, super.ownCreatureId, direction);
        if (e.validateEvent(super.game, this)) {
            e.sendCommand(this.clientConnection);
            return true;
        }
        return false;
    }

    private boolean cry(String cry) {
        WarCry e = new WarCry(null, super.ownCreatureId, cry);
        if (e.validateEvent(super.game, this)) {
            e.sendCommand(this.clientConnection);
            return true;
        }
        return false;
    }

    private boolean singe(Direction direction) {

        Singe e = new Singe(null, super.ownCreatureId, direction);
        if (e.validateEvent(super.game, this)) {
            e.sendCommand(this.clientConnection);
            return true;
        }
        return false;
    }

    private boolean swap(int x, int y) {
        Swap e = new Swap(null, super.ownCreatureId, x, y);
        if (e.validateEvent(super.game, this)) {
            e.sendCommand(this.clientConnection);
            return true;
        }
        return false;
    }

    private boolean cast(int x, int y) {
        Cast e = new Cast(null, super.ownCreatureId, x, y);
        if (e.validateEvent(super.game, this)) {
            e.sendCommand(this.clientConnection);
            return true;
        }
        return false;
    }

    private boolean shoot(Direction direction) {

        Shoot e = new Shoot(null, super.ownCreatureId, direction);
        if (e.validateEvent(super.game, this)) {
            e.sendCommand(this.clientConnection);
            return true;
        }
        return false;
    }

    private void done() {
        super.clientConnection.sendDoneActing();
    }

    private boolean bite(Direction direction) {

        Bite e = new Bite(null, super.ownCreatureId, direction);
        if (e.validateEvent(super.game, this)) {
            e.sendCommand(this.clientConnection);
            return true;
        }
        return false;
    }

    private boolean blink(int x, int y) {

        Blink e = new Blink(null, super.ownCreatureId, x, y);
        if (e.validateEvent(super.game, this)) {
            e.sendCommand(this.clientConnection);
            return true;
        }
        return false;
    }

    private boolean burn() {

        Burn e = new Burn(null, super.ownCreatureId);
        if (e.validateEvent(super.game, this)) {
            e.sendCommand(this.clientConnection);
            return true;
        }
        return false;
    }

    private boolean cut(Direction direction) {

        Cut e = new Cut(null, super.ownCreatureId, direction);
        if (e.validateEvent(super.game, this)) {
            e.sendCommand(this.clientConnection);
            return true;
        }
        return false;
    }

    private boolean claw(Direction direction) {

        Claw e = new Claw(null, super.ownCreatureId, direction);
        if (e.validateEvent(super.game, this)) {
            e.sendCommand(this.clientConnection);
            return true;
        }
        return false;
    }

    private boolean crush(Direction direction) {
        Crush e = new Crush(null, super.ownCreatureId, direction);
        if (e.validateEvent(super.game, this)) {
            e.sendCommand(this.clientConnection);
            return true;
        }
        return false;
    }

    private boolean stare(Direction direction) {

        Stare e = new Stare(null, super.ownCreatureId, direction);
        if (e.validateEvent(super.game, this)) {
            e.sendCommand(this.clientConnection);
            return true;
        }
        return false;
    }

    private void printHelp() {
        System.out.printf("%s%n", HELPTEXT);
    }


}
