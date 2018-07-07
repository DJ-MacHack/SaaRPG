

package de.unisaarland.sopra.ai;


import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.model.CreatureType;
import org.apache.commons.cli.*;


/**
 * The entry point for your { Server} implementation.
 *
 * @author Henrik Paul Koehn
 */
public final class Main {
    private Main() {
    }
    /**
     * This method is called when the { Server} is started.
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
        // Client
        if (line.hasOption("ki")) {

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
        // No valid option
        else {
            exit("Invalid parameters! Use -ki!");
        }
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
     * Setup and start of a { KI} ({@link Client})
     *
     * @param host         Name of the host
     * @param port         Port for connection
     * @param timeout      Timeout for client-server connection
     * @param name         Name of {@link de.unisaarland.sopra.model.Player}
     * @param teamname     Teamname of {@link de.unisaarland.sopra.model.Player}
     * @param creatureType Creature type of { KI} as String
     */
    private static void setupClient(String host, int port, int timeout, String name, String teamname, String creatureType) {
        Client ki = null;
        CreatureType ct = null;
        try {
            ct = CreatureType.valueOf(creatureType);
        } catch (IllegalArgumentException e) {
            exit("Monster type does not exist:\n" + e.getMessage());
        }
        assert ct != null; // Just to make sure, but it should not happen
        switch (ct) {
            case ELF:
                ki = new Elf_DJ_MacHack_old(name, port, timeout, teamname, host, CreatureType.ELF);
                break;
            default:
                exit("It is not possible to play a boar or a fairy or another character!");
        }
        assert ki != null; // Just to make sure, but it should not happen
        ki.run();
        //throw new UnsupportedOperationException();
    }

    /**
     * Builds all required options
     * @return {@link Options} with all needed {@link Option}
     */

    private static Options setupOptions (){

        // Setup classes
        Options options = new Options();
        OptionGroup mode = new OptionGroup();

        // add options for modes
        mode.addOption(new Option("ki", "starts ki mode"));               // Ki

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

        return options;
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

