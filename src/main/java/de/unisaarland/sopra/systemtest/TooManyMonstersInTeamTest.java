package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.comm.TimeoutException;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;

/**
 * Created by Team14 on 9/22/16.
 */
public class TooManyMonstersInTeamTest extends OurSystemTest {
    private final static String PLAYERA = "AndreasKieling";
    private final static String PLAYERB = "MarcelDavis";
    private final static String PLAYERC = "PeterLustig";
    private final static String TEAMA = "YTK";
    private final static String TEAMB = "VollerEmpfang";
    private final static CreatureType CREATUREA = CreatureType.YETI;
    private final static CreatureType CREATUREB = CreatureType.NAGA;
    private final static CreatureType CREATUREC = CreatureType.ELF;
    private final static String VALIDFIGHTFILE =
            TEAMA + ", " + TEAMB + "\n" +
                    TEAMA + ", " + PLAYERA + ", " + CREATUREA.toString() + "\n" +
                    TEAMA + ", " + PLAYERB + ", " + CREATUREB.toString() + "\n" +
                    TEAMB + ", " + PLAYERC + ", " + CREATUREC.toString();
    private  String VALIDMAPFILE;

    private TooManyMonstersInTeamTest(String mapfile, String testname) {
        super("TooManyMonstersInTeamTest." + testname);
        this.VALIDMAPFILE = mapfile;
    }

    @Override
    protected void executeTest() {
        expectServerFail();
        try {
            ClientConnection<Event> pa = register(0, PLAYERA, CREATUREA, TEAMA, 0, 0);
            ClientConnection<Event> pb = register(1, PLAYERB, CREATUREB, TEAMA, 0, 0);
            assertRegisterEvent(pb.nextEvent(), 0, PLAYERA, CREATUREA, TEAMA, 0, 0);
            assertRegisterEvent(pa.nextEvent(), 1, PLAYERB, CREATUREB, TEAMA, 0, 0);
            ClientConnection<Event> pc = register(2, PLAYERC, CREATUREC, TEAMB, 1, 0);
            assertRegisterEvent(pa.nextEvent(), 2, PLAYERC, CREATUREC, TEAMB, 1, 0);
            assertRegisterEvent(pb.nextEvent(), 2, PLAYERC, CREATUREC, TEAMB, 1, 0);
            assertRegisterEvent(pc.nextEvent(), 0, PLAYERA, CREATUREA, TEAMA, 0, 0);
            assertRegisterEvent(pc.nextEvent(), 1, PLAYERB, CREATUREB, TEAMA, 0, 0);
        } catch (TimeoutException | IllegalArgumentException e) {
            Assert.assertNotNull(e);
        }
    }

    @Override
    protected String getMapFile() {
        return VALIDMAPFILE;
    }

    @Override
    protected String getFightFile() {
        return VALIDFIGHTFILE;
    }

    public static Collection<OurSystemTest> getTests(){
        ArrayList<OurSystemTest> sysTests = new ArrayList<>();
        sysTests.add(new TooManyMonstersInTeamTest("01\n  ", "SimpleMap"));
        sysTests.add(new TooManyMonstersInTeamTest("011#\n####", "MoreComplexMap"));
        sysTests.add(new TooManyMonstersInTeamTest("011#~*xxx\n####___^^", "EvenMoreComplexMap"));
        return sysTests;
    }
}
