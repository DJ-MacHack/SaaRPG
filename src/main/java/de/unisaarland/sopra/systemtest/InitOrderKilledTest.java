package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import static de.unisaarland.sopra.Direction.EAST;
import static de.unisaarland.sopra.Direction.WEST;

/**
 * Created by Team14 on 9/22/16.
 */
public class InitOrderKilledTest extends OurSystemTest {
    private final static String PLAYERA = "AndreasKieling";
    private final static String PLAYERB = "MarcelDavis";
    private final static String PLAYERC = "PeterLustig";
    private final static String TEAMA = "YTK";
    private final static String TEAMB = "VollerEmpfang";
    private final static CreatureType CREATUREA = CreatureType.WRAITH;
    private final static CreatureType CREATUREB = CreatureType.YETI;
    private final static CreatureType CREATUREC = CreatureType.WRAITH;
    private final static String VALIDFIGHTFILE =
            TEAMA + ", " + TEAMB + "\n" +
                    TEAMA + ", " + PLAYERA + ", " + CREATUREA.toString() + "\n" +
                    TEAMB + ", " + PLAYERB + ", " + CREATUREB.toString() + "\n" +
                    TEAMA + ", " + PLAYERC + ", " + CREATUREC.toString();
    public final static String VALIDMAPFILE = "010";


    public InitOrderKilledTest() {
        super("InitOrderKilledTest");
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> pa = register(0, PLAYERA, CREATUREA, TEAMA, 0, 0);
        ClientConnection<Event> pb = register(1, PLAYERB, CREATUREB, TEAMB, 1, 0);
        assertRegisterEvent(pb.nextEvent(), 0, PLAYERA, CREATUREA, TEAMA, 0, 0);
        assertRegisterEvent(pa.nextEvent(), 1, PLAYERB, CREATUREB, TEAMB, 1, 0);
        ClientConnection<Event> pc = register(2, PLAYERC, CREATUREC, TEAMA, 2, 0);
        assertRegisterEvent(pa.nextEvent(), 2, PLAYERC, CREATUREC, TEAMA, 2, 0);
        assertRegisterEvent(pb.nextEvent(), 2, PLAYERC, CREATUREC, TEAMA, 2, 0);
        assertRegisterEvent(pc.nextEvent(), 0, PLAYERA, CREATUREA, TEAMA, 0, 0);
        assertRegisterEvent(pc.nextEvent(), 1, PLAYERB, CREATUREB, TEAMB, 1, 0);

        assertRoundBegin(assertAndMerge(pa, pb, pc), 1);
        for (int i = 0; i < (99 / 6); i++) {
            assertActNow(assertAndMerge(pa, pb, pc), 0);
            pa.sendSwap(0, 0);
            assertSwapped(assertAndMerge(pa, pb, pc), 0, 0, 0);
        }
        assertActNow(assertAndMerge(pa, pb, pc), 0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 0); //A is extremely low

        assertActNow(assertAndMerge(pa, pb, pc), 1); //B kills A
        pb.sendClaw(WEST);
        assertClaw(assertAndMerge(pa, pb, pc), 1, WEST);
        assertDied(assertAndMerge(pa, pb, pc), 0);
        assertActNow(assertAndMerge(pa, pb, pc), 1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 1);
        assertActNow(assertAndMerge(pa, pb, pc), 2);
        pc.sendBlink(0, 0);
        assertBlinked(assertAndMerge(pa, pb, pc),2,0,0);

        for (int i = 0; i < (99 / 6); i++) {
            assertActNow(assertAndMerge(pa, pb, pc), 2);
            pc.sendSwap(0, 0);
            assertSwapped(assertAndMerge(pa, pb, pc), 2, 0, 0);
        }
        assertActNow(assertAndMerge(pa, pb, pc), 2);
        pc.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 2);
        assertRoundEnd(assertAndMerge(pa, pb, pc), 1, 0);

        assertRoundBegin(assertAndMerge(pa, pb, pc), 2);

        assertActNow(assertAndMerge(pa, pb, pc), 1);
        pb.sendCrush(WEST); //B kills C
        assertCrush(assertAndMerge(pa, pb, pc), 1, WEST);
        assertDied(assertAndMerge(pa, pb, pc), 2);
        assertActNow(assertAndMerge(pa, pb, pc), 1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 1);
        assertRoundEnd(assertAndMerge(pa, pb, pc), 2, 0);
        assertWinner(assertAndMerge(pa, pb, pc), TEAMB);
    }

    @Override
    protected String getMapFile() {
        return VALIDMAPFILE;
    }

    @Override
    protected String getFightFile() {
        return VALIDFIGHTFILE;
    }
}
