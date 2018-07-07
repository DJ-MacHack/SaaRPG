package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import static de.unisaarland.sopra.Direction.EAST;
import static de.unisaarland.sopra.Direction.WEST;

/**
 * Created by Team14 on 9/22/16.
 */
public class BoarOnLavaTest extends OurSystemTest {
    private final static String PLAYERA = "AndreasKieling";
    private final static String PLAYERB = "MarcelDavis";
    private final static String PLAYERC = "PeterLustig";
    private final static String TEAMA = "YTK";
    private final static String TEAMB = "VollerEmpfang";
    private final static CreatureType CREATUREA = CreatureType.WRAITH;
    private final static CreatureType CREATUREB = CreatureType.YETI;
    private final static String VALIDFIGHTFILE =
            TEAMA + ", " + TEAMB + "\n" +
                    TEAMA + ", " + PLAYERA + ", " + CREATUREA.toString() + "\n" +
                    TEAMB + ", " + PLAYERB + ", " + CREATUREB.toString();
    public final static String VALIDMAPFILE = "01.     *";


    public BoarOnLavaTest() {
        super("BoarOnLavaTest");
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> pa = register(0, PLAYERA, CREATUREA, TEAMA, 0, 0);
        ClientConnection<Event> pb = register(1, PLAYERB, CREATUREB, TEAMB, 1, 0);
        assertRegisterEvent(pb.nextEvent(), 0, PLAYERA, CREATUREA, TEAMA, 0, 0);
        assertRegisterEvent(pa.nextEvent(), 1, PLAYERB, CREATUREB, TEAMB, 1, 0);
        assertRoundBegin(assertAndMerge(pb,pa),1);

        assertBoarSpawned(assertAndMerge(pb,pa),2,2,0);
        assertMoved(assertAndMerge(pb,pa),2,EAST);
        assertMoved(assertAndMerge(pb,pa),2,EAST);
        assertMoved(assertAndMerge(pb,pa),2,EAST);
        assertMoved(assertAndMerge(pb,pa),2,EAST);
        assertMoved(assertAndMerge(pb,pa),2,EAST);
        assertMoved(assertAndMerge(pb,pa),2,EAST);

        assertActNow(assertAndMerge(pb,pa),0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pb,pa),0);

        assertActNow(assertAndMerge(pb,pa),1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pb,pa),1);

        assertFieldEffect(assertAndMerge(pb,pa),8,0,25); //Possible cause of a Reference Crash?
        assertDied(assertAndMerge(pb,pa),2);

        assertRoundEnd(assertAndMerge(pb,pa),1,1);

        assertRoundBegin(assertAndMerge(pb,pa),2);

        assertActNow(assertAndMerge(pb,pa),1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pb,pa),1);

        assertActNow(assertAndMerge(pb,pa),0);
        pa.sendBlink(-1,-1);
        assertKicked(assertAndMerge(pb,pa),0);

        assertRoundEnd(assertAndMerge(pb,pa),2,0);
        assertWinner(assertAndMerge(pb,pa),TEAMB);

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
