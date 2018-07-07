package de.unisaarland.sopra;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.gui.Gui;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.publictest.*;
import de.unisaarland.sopra.systemtest.*;
import de.unisaarland.sopra.utility.InvalidFightFileException;
import de.unisaarland.sopra.utility.InvalidMapFileException;
import de.unisaarland.sopra.utility.RegistrationAbortedException;
import org.apache.commons.cli.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * The entry point for your {@link Server} implementation.
 *
 * @author Henrik Paul Koehn
 */
public final class Main {
    private Main() {
    }

    /**
     * This method is called when the {@link Server} is started.
     *
     * @param args Command line arguments
     */
    public static void main(final String[] args) {

        //Setup options
        Options options = setupOptions();

        // Setup Parser
        CommandLineParser parser = new DefaultParser();
        CommandLine line = null;

        // run parser
        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            exit("Error while parsing:\n" + e.getMessage());
        }

        // Handle parsed results
        assert line != null; // TODO maybe redundant
        // Server
        if (line.hasOption("server")) {

            // Check if args are valid
            if (!checkServerArgs(line)) {
                exit("Invalid server arguments");
            }
            // Get parameters
            int port = Integer.parseInt(line.getOptionValue("port"));
            int timeout = Integer.parseInt(line.getOptionValue("timeout", "1000"));
            String map = line.getOptionValue("map");
            String fight = line.getOptionValue("file");

            setupServer(port, timeout, fight, map);
        }
        // Client
        else if (line.hasOption("ki")) {

            // Check if args are valid
            if (!checkClientArgs(line)) {
                exit("Invalid ki arguments");
            }
            // Get parameters
            String host = line.getOptionValue("host");
            int port = Integer.parseInt(line.getOptionValue("port"));
            int timeout = Integer.parseInt(line.getOptionValue("timeout", "1000"));
            String name = line.getOptionValue("name");
            String team = line.getOptionValue("team");
            String monster = line.getOptionValue("monster").toUpperCase();

            // If Monster is valid create ki, else abort
            setupClient(host, port, timeout, name, team, monster);
        }
        // Test
        else if (line.hasOption("test")) {
            // Check if args are valid
            if (!checkTestArgs(line)) {
                exit("Invalid test arguments!");
            }
            // Parse test arguments
            String test = line.getOptionValue("test");
            String json = line.getOptionValue("json");
            int port = Integer.parseInt(line.getOptionValue("port"));
            int timeout = Integer.parseInt(line.getOptionValue("timeout", "1000"));
            int dport = Integer.parseInt(line.getOptionValue("dport", "-1"));

            setupTests(test, json, port, timeout, dport);
        }
        // gui
        else if (line.hasOption("gui")) {
            //Check if args are valid
            if (!checkGuiArgs(line)) {
                exit("Invalid Gui arguments!");
            }
            // Parse gui arguments
            String host = line.getOptionValue("host");
            int port = Integer.parseInt(line.getOptionValue("port"));
            int timeout = Integer.parseInt(line.getOptionValue("timeout", "1000"));

            setupGui(host, port, timeout);
        }
        // Human player mode
        else if (line.hasOption("player")) {
            // Check if args are valid
            if (!checkPlayerArgs(line)) {
                exit("Invalid player arguments");
            }
            // Get parameters
            String host = line.getOptionValue("host");
            int port = Integer.parseInt(line.getOptionValue("port"));
            int timeout = Integer.parseInt(line.getOptionValue("timeout", "1000"));
            String name = line.hasOption("name") ? line.getOptionValue("name") : null;
            String team = line.hasOption("team") ? line.getOptionValue("team") : null;
            String monster = line.hasOption("monster") ? line.getOptionValue("monster").toUpperCase() : null;

            // Create and start a human player
            setupHumanPlayer(host, port, timeout, name, team, monster);
        }
        //Map Generator Mode
        else if (line.hasOption("mapgenerator")) {
            // Check if args are valid
            if (!checkMapGeneratorArgs(line)) {
                exit("Invalid player arguments");
            }

            try {
                int xdim = line.hasOption("xdim") ? Integer.parseInt(line.getOptionValue("xdim")) : 100;
                int ydim = line.hasOption("ydim") ? Integer.parseInt(line.getOptionValue("ydim")) : 100;
                String algorithm = line.hasOption("algorithm") ? line.getOptionValue("algorithm") : "perlinnoise";
                String path = line.getOptionValue("map");
                setupMapGenerator(xdim, ydim, algorithm, path);
            } catch (NumberFormatException e) {
                exit("Invalid Arguments, NumberFormatException");
            }
            // Create and start a Map Generator
        }
        // No valid option
        else {
            exit("Invalid parameters! Use either -server, -ki, -test, -player or -gui");
        }
    }

    private static boolean checkMapGeneratorArgs(CommandLine line) {
        return line.hasOption("map");
    }

    /**
     * Setups all needed options for SaaRPG
     * @return {@link Options} with all needed {@link Option}
     */

    // Validation methods

    /**
     * Tests if commandline has needed options for server
     *
     * @param line {@link CommandLine} to controll
     * @return Returns true if arguments are valid
     */
    private static boolean checkServerArgs(CommandLine line) {
        return line.hasOption("port")
                && line.hasOption("map")
                && line.hasOption("file");
    }

    /**
     * Tests if commandline has needed options for client
     *
     * @param line {@link CommandLine} to control
     * @return Returns true if arguments are valid
     */

    private static boolean checkClientArgs(CommandLine line) {
        return line.hasOption("port")
                && line.hasOption("name")
                && line.hasOption("team")
                && line.hasOption("monster")
                && line.hasOption("host");
    }

    /**
     * Tests if commandline has needed options for test
     *
     * @param line {@link CommandLine} to control
     * @return Returns true if arguments are valid
     */

    private static boolean checkTestArgs(CommandLine line) {
        return line.hasOption("json")
                && line.hasOption("port");
    }

    /**
     * Tests if commandline has needed options for gui
     *
     * @param line {@link CommandLine} to control
     * @return Returns true if arguments are valid
     */

    private static boolean checkGuiArgs(CommandLine line) {
        return line.hasOption("port")
                && line.hasOption("host");
    }

    /**
     * Tests if commandline has needed options for human player
     *
     * @param line {@link CommandLine} to control
     * @return Returns true if arguments are valid
     */
    private static boolean checkPlayerArgs(CommandLine line) {
        return line.hasOption("port")
                && line.hasOption("host");
    }

    // Setup methods

    private static void setupMapGenerator(int xdim, int ydim, String algorithm, String outFile) {
        try {
            MapGenerator myGen = new MapGenerator(xdim, ydim, algorithm, outFile);
            myGen.generateMap();
            myGen.generatePlayerSpawns(9);
            myGen.saveMap();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            exit("Could not use " + algorithm);
        } catch (IOException e) {
            e.printStackTrace();
            exit("File IO Error");
        }
    }

    /**
     * Setup and start of {@link Server}
     *
     * @param port      Port for {@link Server}
     * @param timeout   Timeout for client-server connection
     * @param fightFile Path to fightFile
     * @param mapFile   Path mapFile
     */
    private static void setupServer(int port, int timeout, String fightFile, String mapFile) {
        // Tries to build a server and handles upcoming exceptions via ending program
        try {
            String pFightFile = readFile(fightFile);
            String pMapFile = readFile(mapFile);

            Server server = new Server(port, timeout, pFightFile, pMapFile);
            server.run();
        } catch (InvalidFightFileException e) {
            e.printStackTrace();
            exit("Invalid FightFile!");
        } catch (InvalidMapFileException e) {
            e.printStackTrace();
            exit("Invalid MapFile!");
        } catch (IOException e) {
            e.printStackTrace();
            exit("Error reading files:\n" + e.getMessage());
        } catch (RegistrationAbortedException e) {
            e.printStackTrace();
            exit("Registration was aborted!");
        }
    }

    /**
     * Setup and start of a {@link KI} ({@link Client})
     *
     * @param host         Name of the host
     * @param port         Port for connection
     * @param timeout      Timeout for client-server connection
     * @param name         Name of {@link de.unisaarland.sopra.model.Player}
     * @param teamname     Teamname of {@link de.unisaarland.sopra.model.Player}
     * @param creatureType Creature type of {@link KI} as String
     */
    private static void setupClient(String host, int port, int timeout, String name, String teamname, String creatureType) {
        /*
        Client ki = null;
        CreatureType ct = null;
        try {
            ct = CreatureType.valueOf(creatureType);
        } catch (IllegalArgumentException e) {
            exit("Monster type does not exist:\n" + e.getMessage());
        }
        assert ct != null; // Just to make sure, but it should not happen
        switch (ct) {
            case KOBOLD:
                ki = new KoboldKI(name, port, timeout, teamname, host);
                break;
            case ELF:
                ki = new ElfKI(name, port, timeout, teamname, host);
                break;
            case ROOK:
                ki = new RookKi(name, port, timeout, teamname, host);
                break;
            case IFRIT:
                ki = new IfritKi(name, port, timeout, teamname, host);
                break;
            case YETI:
                ki = new YetiKi(name, port, timeout, teamname, host);
                break;
            case WRAITH:
                ki = new WraithKi(name, port, timeout, teamname, host);
                break;
            case NAGA:
                ki = new NagaKi(name, port, timeout, teamname, host);
                break;
            default:
                exit("It is not possible to play a boar or a fairy!");
        }
        assert ki != null; // Just to make sure, but it should not happen
        ki.run();*/
        throw new UnsupportedOperationException();
    }

    /**
     * Setup and start of the testmode
     *
     * @param tests      Path to <tests>.jar
     * @param outputFile Target file for test output
     * @param port       Port for connection
     * @param timeout    Timeout for client-server connection
     * @param dport      Debugger port
     */
    private static void setupTests(String tests, String outputFile, int port, int timeout, int dport) {

        // Open files
        File tFile = new File(tests);
        File oFile = new File(outputFile);

        // Start test controller and get tests
        TestController tc = new TestController(tFile, port, dport, timeout, oFile);
        List<SystemTest> sysTests = setupSystemTests();

        // Execute system tests
        try {
            tc.executeTests(sysTests);
        } catch (IOException e) {
            exit("IO error: " + e.getMessage());
        }

        // Print results
        System.out.println("SUCCESS: " + tc.count(SystemTest.TestState.SUCCESS) + "/" + tc.getTestCount());
        System.out.println("FAIL: " + tc.count(SystemTest.TestState.FAIL) + "/" + tc.getTestCount());
        System.out.println("NOEXEC: " + tc.count(SystemTest.TestState.NOEXEC) + "/" + tc.getTestCount());
        System.out.println("SERVERFAIL: " + tc.count(SystemTest.TestState.SERVERFAIL) + "/" + tc.getTestCount());

    }

    /**
     * Runs gui
     *
     * @param host    host
     * @param port    port
     * @param timeout timeout
     */
    private static void setupGui(String host, int port, int timeout) {
        Gui gui = new Gui(host, port, timeout);
        gui.run();
    }

    /**
     * Runs game in player mode
     *
     * @param host     Name of the host
     * @param port     Port for connection
     * @param timeout  Timeout for client-server connection
     * @param name     Name of {@link de.unisaarland.sopra.model.Player}
     * @param teamname Teamname of {@link de.unisaarland.sopra.model.Player}
     * @param monster  Creature type of {@link KI} as String
     */

    private static void setupHumanPlayer(String host, int port, int timeout, String name, String teamname,
                                         String monster) {
        CreatureType ct = null;
        // Cast string to creature type
        try {
            ct = (monster != null) ? CreatureType.valueOf(monster) : null;
        } catch (IllegalArgumentException e) {
            exit("Monster type does not exist:\n" + e.getMessage());
        }
        // Create new human player and start him
        Human human = new Human(host, port, timeout, name, teamname, ct);
        human.run();
    }

    /**
     * Builds all required options
     *
     * @return {@link Options} with all needed {@link Option}
     */

    private static Options setupOptions() {

        // Setup classes
        Options options = new Options();
        OptionGroup mode = new OptionGroup();

        // add options for modes
        mode.addOption(new Option("server", "starts server mode"));       // Server
        mode.addOption(new Option("ki", "starts ki mode"));               // Ki
        mode.addOption(new Option("test", true, "starts test mode"));     // Test
        mode.addOption(new Option("gui", "starts gui mode"));             // gui
        mode.addOption(new Option("player", "starts human player mode")); // human player
        mode.addOption(new Option("mapgenerator", "starts map generator mode"));

        options.addOptionGroup(mode);

        // parameter with arguments
        // Host
        options.addOption(Option.builder("host")
                .hasArg()
                .argName("host")
                .desc("Adress of host")
                .build());

        // Port
        options.addOption(Option.builder("port")
                .hasArg()
                .argName("port")
                .type(Number.class)
                .desc("port to listen")
                .build());

        // Timeout
        options.addOption(Option.builder("timeout")
                .hasArg()
                .argName("milliseconds")
                .desc("timeout for connection")
                .build());


        // Map file
        options.addOption(Option.builder("map")
                .longOpt("mapfile")
                .hasArg()
                .argName("path")
                .desc("specify map file")
                .build());

        // Fight file
        options.addOption(Option.builder("file")
                .longOpt("fightfile")
                .hasArg()
                .argName("path")
                .desc("specify fight file")
                .build());

        // Name
        options.addOption(Option.builder("name")
                .numberOfArgs(1)
                .hasArg()
                .argName("Name")
                .desc("Name of Player")
                .build());

        // Team name
        options.addOption(Option.builder("team")
                .longOpt("teamname")
                .hasArg()
                .argName("teamName")
                .desc("Team name of player")
                .build());

        // Monster type
        options.addOption(Option.builder("monster")
                .longOpt("monstertype")
                .hasArg()
                .argName("monsterType")
                .desc("monster ki shall play")
                .build());

        // Output for tests (json)
        options.addOption(Option.builder("json")
                .hasArg()
                .argName("debuggerPort")
                .desc("Path to output file for test results")
                .build());

        // Debbuger port
        options.addOption(Option.builder("dport")
                .longOpt("debuggerport")
                .hasArg()
                .argName("port")
                .type(Number.class)
                .desc("port for debugging")
                .build());

        //X Dimensions for Map Generator
        options.addOption(Option.builder("xdim")
                .longOpt("xdimensions")
                .hasArg()
                .argName("xDimensions")
                .desc("X Dimensions of the created map (in tiles)")
                .build());

        //Y Dimensions for Map Generator
        options.addOption(Option.builder("ydim")
                .longOpt("ydimensions")
                .hasArg()
                .argName("yDimensions")
                .desc("Y Dimensions of the created map (in tiles)")
                .build());

        //Algorithm for Map Generator
        options.addOption(Option.builder("algorithm")
                .hasArg()
                .argName("Algorithm")
                .desc("Algorithm to use in creation")
                .build());




        return options;
    }

    /**
     * Setups all system tests
     * add a test here to execute it
     * if tests shall not be execute, comment them
     *
     * @return A list with all system tests
     */
    private static List<SystemTest> setupSystemTests() {
        List<SystemTest> sysTests = new LinkedList<>();

        // public tests
        sysTests.add(new AmokBoar2Test());
        sysTests.add(new AmokBoarTest());
        sysTests.add(new BlinkOnIceTest());
        sysTests.add(new BoarAttackOnIceSameRound());
        sysTests.add(new BoarAttackOnIceTest());
        sysTests.add(new BoarAttackOnWaterTest());
        sysTests.add(new BoarRangeAttackTest());
        sysTests.add(new BoarSurroundedTest());
        sysTests.addAll(BrokenInputTest.getTests());
        sysTests.add(new PublicBeispielTest());
        sysTests.addAll(PublicWarCryTest.getTests());
        sysTests.add(new TrappedBoarTest());

        /*
        // Our system tests
        sysTests.add(new AnotherAttackOnCurseFieldTest());
        sysTests.add(new AttackKickedPlayerTest());
        sysTests.add(new AttackOnCurseFieldTest());
        sysTests.add(new AttackOnWaterTest());
        sysTests.add(new AttackOutsideOfMap());
        sysTests.add(new BiteCutTest());
        sysTests.add(new BiteKillPoisonTest());
        sysTests.add(new BlinkSwapTestSimple());
        sysTests.add(new BoarAndFairyTest());
        //sysTests.add(new BoarAttackOnIce());
        sysTests.add(new BoarKilledThenInvisibleTest());
        sysTests.add(new BoarOnLavaTest());
        sysTests.add(new BoarOnWaterTest());
        sysTests.add(new BoarRespawnInhibitedTest());
        sysTests.add(new BoarReviveTest());
        sysTests.add(new BoringHundredRoundsTest());
        sysTests.add(new BurnDoubleKillTest());             // TODO: new Test
        sysTests.add(new BurnFourTargetsTest());            // TODO: new Test
        sysTests.add(new BurnMultipleKillTest());
        sysTests.add(new BurnOneDiedTest());                // TODO: new Test
        sysTests.add(new Burn6Test());                      // TODO: new Test
        sysTests.add(new BushFieldEffectTest());
        sysTests.add(new CanBlinkTest());                   // TODO: New Test
        sysTests.add(new CanBurnBlinkOnWater());            // TODO: New Test
        sysTests.add(new CanAttackBurnTest());              // TODO: New Test
        sysTests.add(new CastCanAttackTest());              //TODO: new Test
        sysTests.add(new CastLimitsTest());                 // TODO: new Test
        sysTests.add(new CastOnTreeNoEffectTest());
        sysTests.add(new ClawCrushTestSimple());
        sysTests.add(new CrushOnBoar());                    //TODO: New Test
        sysTests.add(new CutOnBoar());                      //TODO: New Test
        sysTests.add(new FairyKilledTest());
        sysTests.add(new FairyOnIceTest());
        sysTests.add(new FairyOnWaterTest());
        sysTests.add(new FairyRespawnTest());
        sysTests.add(new FightFileTests());
        sysTests.add(new FightSimpleGrassTest());
        sysTests.add(new HealAndKillFairyTest());
        sysTests.add(new HSystemTest());
        sysTests.add(new InitOrderKilledTest());
        sysTests.add(new LavaFieldeffectTest());
        sysTests.add(new LoosingLifeOnWaterTest());
        sysTests.add(new NoHealTest());
        //sysTests.add(new PoisonKillBoarTest());             // TODO: new Test
        //sysTests.add(new PoisonKillFairyTest());            // TODO: new Test
        sysTests.add(new PoisonKillTest());                 // TODO: new Test
        sysTests.add(new RegistrationAbortedTest());
        sysTests.add(new RegistrationAbortedTestOne());
        sysTests.add(new RegistrationAbortedTestTwo());
        sysTests.add(new RegistrationAbortedTestThree());
        sysTests.add(new RegistrationAbortedTestFour());
        sysTests.add(new RegistrationPhaseMoveCommandTest());
        sysTests.add(new RegistrationPhaseTest());
        sysTests.add(new RookRangeInHillFieldTest());
        sysTests.add(new ShootAcrossRockfield());
        sysTests.add(new ShootOnHillNoEffectTest());
        sysTests.add(new ShootWithoutHindranceTest());
        sysTests.add(new SimpleAttackTestOne());
        sysTests.add(new SimpleFightTest());
        sysTests.add(new SingeKillPlayerTest());     // TODO: new Test
        sysTests.add(new SingeKillBoarTest());       // TODO: new Test
        sysTests.add(new SingeBurnTestSimple());
        sysTests.add(new StareKillPlayerTest());     // TODO: new Test
        sysTests.add(new StareKillBoarTest());       // TODO: new Test
        sysTests.add(new StareCastTestSimple());
        sysTests.add(new SwapNoEnergyTest());       // TODO: new Test
        sysTests.add(new SwapNotYourTurnTest());    // TODO: new Test
        sysTests.add(new SwapAlreadyDeadTest());     // TODO: new Test
        sysTests.add(new SwapAlreadyDeadTest());     // TODO: new Test
        sysTests.add(new SwapKilledTest());         //TODO: new Test
        sysTests.add(new TreeFieldEffectTest());

        sysTests.addAll(StabKillTests.getTests());

        // Attack commands while register phase tests
        sysTests.addAll(RegisterPhaseAttackTest.getTests());  // TODO new Test

        //WRONG DATA TESTS - TEST IF SERVER CRASHES WHEN WRONG MAP OR FIGHT DATA IS SENT
        sysTests.addAll(WrongDataTest.getTests());
        sysTests.addAll(TooManyMonstersInTeamTest.getTests());

        //TURN ON ICE TESTS - TEST IF ONLY YETI CAN TURN ON ICE
        sysTests.addAll(TurnOnIceTest.getTests());

        //ILLEGAL ATTACK TESTS - TEST IF MISMATCHING CREATURES ARE ALLOWED TO ATTACK
        sysTests.addAll(IllegalAttackHelper.getTests());

        //MELEE TESTS - TEST IF CREATURES GET KICKED AFTER ATTACKING AN EMPTY FIELD
        sysTests.addAll(MeleeTest.getTests());

        sysTests.addAll(MovementTest.getTests());

        //PRIVATE SYSTEM TESTS - PLS COMMENT THESE TESTS OUT BEFORE PUSHING
        //sysTests.add(new BoarAttackOnIceTwo());
        */
        return sysTests;
    }

    // utility methods

    /**
     * Pareses fightFile and returns the according String
     *
     * @param path Path to fightFile
     * @return returns the parsed fightFile String
     */

    private static String readFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Shutdowns program
     *
     * @param s Reason of shtudown
     */
    private static void exit(String s) {
        System.out.println(s);
        System.exit(1);
    }
}
