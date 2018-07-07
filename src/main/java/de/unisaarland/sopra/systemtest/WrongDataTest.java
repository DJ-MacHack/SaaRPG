package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.comm.TimeoutException;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Lukas Kirschner on 9/20/16.
 * This test checks if the tested server implementation crashes when invalid data is sent.
 */
public class WrongDataTest extends OurSystemTest {
    private String INVALIDFIGHTFILE;
    private String INVALIDMAPFILE;
    private final static String PLAYERA = "AndreasKieling";
    private final static String PLAYERB = "MarcelDavis";
    private final static String TEAMA = "YTK";
    private final static String TEAMB = "VollerEmpfang";
    private CreatureType CREATUREA = CreatureType.YETI;
    private CreatureType CREATUREB = CreatureType.NAGA;
    private final static String VALIDFIGHTFILE = OurSystemTest.buildValidFightFile(TEAMA, TEAMB, PLAYERA, PLAYERB, "Yeti", "Naga");
    private final static String VALIDMAPFILE = "01\n  ";
    private final static char[] ILLEGALMAPCHARS = {'(', 'P', '\t', 'u'};

    private WrongDataTest(String invalidFightFile, String invalidMapFile, String testName) {
        super("WrongDataTest." + testName);
        this.INVALIDFIGHTFILE = (invalidFightFile == null) ? VALIDFIGHTFILE : invalidFightFile;
        this.INVALIDMAPFILE = (invalidMapFile == null) ? VALIDMAPFILE : invalidMapFile;
    }
    private WrongDataTest(String invalidFightFile, String invalidMapFile, String testName, CreatureType a, CreatureType b){
        super("WrongDataTest." + testName);
        assert(invalidFightFile != null);
        this.INVALIDFIGHTFILE = invalidFightFile;
        this.INVALIDMAPFILE = (invalidMapFile == null) ? VALIDMAPFILE : invalidMapFile;
        this.CREATUREA = a;
        this.CREATUREB = b;
    }

    @Override
    protected void executeTest() {
        expectServerFail();
        try (ClientConnection<Event> pa = register(0, PLAYERA, CREATUREA, TEAMA, 0, 0);
             ClientConnection<Event> pb = register(1, PLAYERB, CREATUREB, TEAMB, 1, 0);) {
            assertRegisterEvent(pb.nextEvent(), 0, PLAYERA, CREATUREA, TEAMA, 0, 0);
            assertRegisterEvent(pa.nextEvent(), 1, PLAYERB, CREATUREB, TEAMB, 1, 0);
            Assert.fail("Server did not time out.");
        } catch (TimeoutException | IllegalArgumentException ex) {
            Assert.assertNotNull(ex);
        }

    }

    @Override
    protected String getMapFile() {
        return this.INVALIDMAPFILE;
    }

    @Override
    protected String getFightFile() {
        return this.INVALIDFIGHTFILE;
    }

    public static Collection<OurSystemTest> getTests() {
        ArrayList<OurSystemTest> sysTests = new ArrayList<OurSystemTest>();
        sysTests.add(new WrongDataTest(null, "  \n  ", "MapNoSpawnPoints"));
        sysTests.add(new WrongDataTest(null, "0 \n  ", "MapTooFewSpawnPoints"));
        sysTests.add(new WrongDataTest(null, "01 \n  ", "MapOneLineTooShort"));
        sysTests.add(new WrongDataTest(null, "01 \n  \n   ", "MapMiddleLineTooShort"));
        sysTests.add(new WrongDataTest(null, "01\n##\n##\n###", "MapOneLineTooLong"));
        for (int i = 0; i < ILLEGALMAPCHARS.length; i++) {
            sysTests.add(new WrongDataTest(null, "01\n " + ILLEGALMAPCHARS[i], "MapIllegalCharacter" + (i + 1)));
        }
        sysTests.add(new WrongDataTest(
                "TeamA, TeamB, TeamC, TeamD, TeamE, TeamF, TeamG, TeamH, TeamI, TeamJ\n" +
                        "TeamA, Player0, Naga\n" +
                        "TeamB, Player1, Naga\n" +
                        "TeamC, Player2, Naga\n" +
                        "TeamD, Player3, Naga\n" +
                        "TeamE, Player4, Naga\n" +
                        "TeamF, Player5, Naga\n" +
                        "TeamG, Player6, Naga\n" +
                        "TeamH, Player7, Naga\n" +
                        "TeamI, Player8, Naga\n" +
                        "TeamJ, Player9, Naga\n",
                "01234\n56789\n +#%x\nt~^_*\n$&XTA\nABCDE",
                "MapTestAllTilesAndInvalid"));

        sysTests.add(new WrongDataTest(OurSystemTest.buildValidFightFile(TEAMA, "", PLAYERA, PLAYERB, "Yeti", "Naga"), null, "FightTooFewTeams"));
        sysTests.add(new WrongDataTest(TEAMA + ", " + TEAMB + "\n" + TEAMA + ", " + PLAYERA + "Yeti", null, "FightTooFewPlayers"));
        sysTests.add(new WrongDataTest("\n" + OurSystemTest.buildValidFightFile(TEAMA, "", PLAYERA, PLAYERB, "Yeti", "Naga"), null, "FightEmptyFirstLine"));
        sysTests.add(new WrongDataTest(
                "TeamA, TeamB, TeamC, TeamD, TeamE, TeamF, TeamG, TeamH, TeamI, TeamJ, TeamK\n" +
                        "TeamA, Player0, Naga\n" +
                        "TeamB, Player1, Naga\n" +
                        "TeamC, Player2, Naga\n" +
                        "TeamD, Player3, Naga\n" +
                        "TeamE, Player4, Naga\n" +
                        "TeamF, Player5, Naga\n" +
                        "TeamG, Player6, Naga\n" +
                        "TeamH, Player7, Naga\n" +
                        "TeamI, Player8, Naga\n" +
                        "TeamJ, Player9, Naga\n" +
                        "TeamK, Player10, Naga\n",
                null,
                "FightTooManyTeams"));
        sysTests.add(new WrongDataTest(OurSystemTest.buildValidFightFile(TEAMA, TEAMB, PLAYERA, PLAYERA, "Yeti", "Naga"), null, "FightDuplicatePlayerName"));
        sysTests.add(new WrongDataTest(OurSystemTest.buildValidFightFile(TEAMA, TEAMB, PLAYERA, PLAYERB, "Yeti", "Fairy"), null, "FightPlayAsFairy", CreatureType.YETI, CreatureType.FAIRY));
        sysTests.add(new WrongDataTest(OurSystemTest.buildValidFightFile(TEAMA, TEAMB, PLAYERA, PLAYERB, "Yeti", "Boar"), null, "FightPlayAsBoar", CreatureType.YETI, CreatureType.BOAR));
        return sysTests;
    }
}
