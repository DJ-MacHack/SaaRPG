package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import static de.unisaarland.sopra.Direction.WEST;

/**
 * Created by Lukas Kirschner on 9/23/16.
 */
public class BurnDoubleKillTest extends OurSystemTest {
    private final static String PLAYERA = "VictimIfrit";
    private final static String PLAYERB = "IfritTheBoss";
    private final static String PLAYERC = "AnotherVictimIfrit";
    private final static String TEAMA = "TeamLappen";
    private final static String TEAMB = "TeamIfrit";
    private final static CreatureType CREATUREA = CreatureType.IFRIT;
    private final static CreatureType CREATUREB = CreatureType.IFRIT;
    private final static CreatureType CREATUREC = CreatureType.IFRIT;
    private final static String VALIDFIGHTFILE =
            TEAMA + ", " + TEAMB + "\n" +
                    TEAMA + ", " + PLAYERA + ", " + CREATUREA.toString() + "\n" +
                    TEAMB + ", " + PLAYERB + ", " + CREATUREB.toString() + "\n" +
                    TEAMA + ", " + PLAYERC + ", " + CREATUREC.toString();
    public final static String VALIDMAPFILE = "01 \n 0 \n~~~";

    public BurnDoubleKillTest() {
        super("BurnDoubleKillTest");
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> pa = register(0, PLAYERA, CREATUREA, TEAMA, 0, 0);
        ClientConnection<Event> pb = register(1, PLAYERB, CREATUREB, TEAMB, 1, 0);
        assertRegisterEvent(pb.nextEvent(), 0, PLAYERA, CREATUREA, TEAMA, 0, 0);
        assertRegisterEvent(pa.nextEvent(), 1, PLAYERB, CREATUREB, TEAMB, 1, 0);
        ClientConnection<Event> pc = register(2, PLAYERC, CREATUREC, TEAMA, 1, 1);
        assertRegisterEvent(pa.nextEvent(), 2, PLAYERC, CREATUREC, TEAMA, 1, 1);
        assertRegisterEvent(pb.nextEvent(), 2, PLAYERC, CREATUREC, TEAMA, 1, 1);
        assertRegisterEvent(pc.nextEvent(), 0, PLAYERA, CREATUREA, TEAMA, 0, 0);
        assertRegisterEvent(pc.nextEvent(), 1, PLAYERB, CREATUREB, TEAMB, 1, 0);

        assertRoundBegin(assertAndMerge(pa, pb, pc), 1);
        assertActNow(assertAndMerge(pa, pb, pc), 0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 0);

        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(pa, pb, pc), 1);
            pb.sendBurn();
            assertBurned(assertAndMerge(pa, pb, pc), 1); //Victim HP: 75
        }
        assertActNow(assertAndMerge(pa, pb, pc), 1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 1);

        assertActNow(assertAndMerge(pa, pb, pc), 2);
        pc.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 2);

        assertRoundEnd(assertAndMerge(pa, pb, pc), 1, 0);

        assertRoundBegin(assertAndMerge(pa, pb, pc), 2);

        assertActNow(assertAndMerge(pa, pb, pc), 2);
        pc.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 2);

        assertActNow(assertAndMerge(pa, pb, pc), 0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 0);

        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(pa, pb, pc), 1);
            pb.sendBurn();
            assertBurned(assertAndMerge(pa, pb, pc), 1); //Victim HP: 63
        }
        assertActNow(assertAndMerge(pa, pb, pc), 1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 1);

        assertRoundEnd(assertAndMerge(pa, pb, pc), 2, 0);

        assertRoundBegin(assertAndMerge(pa, pb, pc), 3);

        assertActNow(assertAndMerge(pa, pb, pc), 2);
        pc.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 2);

        assertActNow(assertAndMerge(pa, pb, pc), 0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 0);

        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(pa, pb, pc), 1);
            pb.sendBurn();
            assertBurned(assertAndMerge(pa, pb, pc), 1); //Victim HP: 51
        }
        assertActNow(assertAndMerge(pa, pb, pc), 1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 1);

        assertRoundEnd(assertAndMerge(pa, pb, pc), 3, 0);

        assertRoundBegin(assertAndMerge(pa, pb, pc), 4);

        assertActNow(assertAndMerge(pa, pb, pc), 2);
        pc.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 2);

        assertActNow(assertAndMerge(pa, pb, pc), 0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 0);

        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(pa, pb, pc), 1);
            pb.sendBurn();
            assertBurned(assertAndMerge(pa, pb, pc), 1); //Victim HP: 39
        }
        assertActNow(assertAndMerge(pa, pb, pc), 1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 1);

        assertRoundEnd(assertAndMerge(pa, pb, pc), 4, 0);

        assertRoundBegin(assertAndMerge(pa, pb, pc), 5);

        assertActNow(assertAndMerge(pa, pb, pc), 2);
        pc.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 2);

        assertActNow(assertAndMerge(pa, pb, pc), 0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 0);

        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(pa, pb, pc), 1);
            pb.sendBurn();
            assertBurned(assertAndMerge(pa, pb, pc), 1); //Victim HP: 27
        }
        assertActNow(assertAndMerge(pa, pb, pc), 1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 1);

        assertRoundEnd(assertAndMerge(pa, pb, pc), 5, 0);

        assertRoundBegin(assertAndMerge(pa, pb, pc), 6);

        assertActNow(assertAndMerge(pa, pb, pc), 2);
        pc.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 2);

        assertActNow(assertAndMerge(pa, pb, pc), 0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 0);

        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(pa, pb, pc), 1);
            pb.sendBurn();
            assertBurned(assertAndMerge(pa, pb, pc), 1); //Victim HP: 15
        }
        assertActNow(assertAndMerge(pa, pb, pc), 1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 1);

        assertRoundEnd(assertAndMerge(pa, pb, pc), 6, 0);

        assertRoundBegin(assertAndMerge(pa, pb, pc), 7);

        assertActNow(assertAndMerge(pa, pb, pc), 2);
        pc.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 2);

        assertActNow(assertAndMerge(pa, pb, pc), 0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 0);

        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(pa, pb, pc), 1);
            pb.sendBurn();
            assertBurned(assertAndMerge(pa, pb, pc), 1); //Victim HP: 3
        }
        assertActNow(assertAndMerge(pa, pb, pc), 1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 1);

        assertRoundEnd(assertAndMerge(pa, pb, pc), 7, 0);

        assertRoundBegin(assertAndMerge(pa, pb, pc), 8);

        assertActNow(assertAndMerge(pa, pb, pc), 2);
        pc.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 2);

        assertActNow(assertAndMerge(pa, pb, pc), 0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 0);

        assertActNow(assertAndMerge(pa, pb, pc), 1);
        pb.sendBurn();
        assertBurned(assertAndMerge(pa, pb, pc), 1); //Victim HP: 0
        assertDied(assertAndMerge(pa, pb, pc),0);
        assertDied(assertAndMerge(pa, pb, pc),2);

        assertActNow(assertAndMerge(pa, pb, pc), 1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb, pc), 1);

        assertRoundEnd(assertAndMerge(pa, pb, pc), 8, 0);
        assertWinner(assertAndMerge(pa, pb, pc),TEAMB);


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
