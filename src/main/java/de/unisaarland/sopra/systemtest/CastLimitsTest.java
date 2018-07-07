package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Lukas Kirschner on 9/23/16.
 */
public class CastLimitsTest extends OurSystemTest {
    private final static String PLAYERA = "VictimIfrit";
    private final static String PLAYERB = "IfritTheBoss";
    private final static String PLAYERC = "AnotherVictimIfrit";
    private final static String TEAMA = "TeamLappen";
    private final static String TEAMB = "TeamIfrit";
    private final static CreatureType CREATUREA = CreatureType.IFRIT;
    private final static CreatureType CREATUREB = CreatureType.ROOK;
    private final static CreatureType CREATUREC = CreatureType.IFRIT;
    private final static String VALIDFIGHTFILE =
            TEAMA + ", " + TEAMB + "\n" +
                    TEAMA + ", " + PLAYERA + ", " + CREATUREA.toString() + "\n" +
                    TEAMB + ", " + PLAYERB + ", " + CREATUREB.toString() + "\n" +
                    TEAMA + ", " + PLAYERC + ", " + CREATUREC.toString();
    public final static String VALIDMAPFILE = "1    00";

    public CastLimitsTest() {
        super("CastLimitsTest");
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> pa = register(0, PLAYERA, CREATUREA, TEAMA, 5, 0);
        ClientConnection<Event> pb = register(1, PLAYERB, CREATUREB, TEAMB, 0, 0);
        assertRegisterEvent(pb.nextEvent(), 0, PLAYERA, CREATUREA, TEAMA, 5, 0);
        assertRegisterEvent(pa.nextEvent(), 1, PLAYERB, CREATUREB, TEAMB, 0, 0);
        ClientConnection<Event> pc = register(2, PLAYERC, CREATUREC, TEAMA, 6, 0);
        assertRegisterEvent(pa.nextEvent(), 2, PLAYERC, CREATUREC, TEAMA, 6, 0);
        assertRegisterEvent(pb.nextEvent(), 2, PLAYERC, CREATUREC, TEAMA, 6, 0);
        assertRegisterEvent(pc.nextEvent(), 0, PLAYERA, CREATUREA, TEAMA, 5, 0);
        assertRegisterEvent(pc.nextEvent(), 1, PLAYERB, CREATUREB, TEAMB, 0, 0);

        assertRoundBegin(assertAndMerge(pa, pb, pc), 1);
        assertActNow(assertAndMerge(pa, pb, pc),0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc),0);

        assertActNow(assertAndMerge(pa, pb, pc),1);
        pb.sendCast(5,0);
        assertCast(assertAndMerge(pa, pb, pc),1,5,0);
        assertActNow(assertAndMerge(pa, pb, pc),1);
        pb.sendCast(6,0);
        assertKicked(assertAndMerge(pa, pb, pc),1);

        assertActNow(assertAndMerge(pa, pb, pc),2);
        pc.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc),2);

        assertRoundEnd(assertAndMerge(pa, pb, pc),1,0);
        assertWinner(assertAndMerge(pa, pb, pc),TEAMA);
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
