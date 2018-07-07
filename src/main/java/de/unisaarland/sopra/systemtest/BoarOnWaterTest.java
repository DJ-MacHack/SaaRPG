package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import static de.unisaarland.sopra.Direction.EAST;
import static de.unisaarland.sopra.Direction.NORTH_EAST;
import static de.unisaarland.sopra.Direction.NORTH_WEST;

/**
 * Created by Lukas Kirschner on 9/22/16.
 */
public class BoarOnWaterTest extends OurSystemTest {
    private final static String PLAYERA = "AndreasKieling";
    private final static String PLAYERB = "MarcelDavis";
    private final static String TEAMA = "YTK";
    private final static String TEAMB = "VollerEmpfang";
    private final static CreatureType CREATUREA = CreatureType.WRAITH;
    private final static CreatureType CREATUREB = CreatureType.YETI;
    private final static String VALIDFIGHTFILE = TEAMA + ", " + TEAMB + "\n" +
                    TEAMA + ", " + PLAYERA + ", " + CREATUREA.toString() + "\n" +
                    TEAMB + ", " + PLAYERB + ", " + CREATUREB.toString();
    public final static String VALIDMAPFILE = "*********\n" +
            "01.     ~";


    public BoarOnWaterTest() {
        super("BoarOnWaterTest");
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> pa = register(0, PLAYERA, CREATUREA, TEAMA, 0, 1);
        ClientConnection<Event> pb = register(1, PLAYERB, CREATUREB, TEAMB, 1, 1);
        assertRegisterEvent(pb.nextEvent(), 0, PLAYERA, CREATUREA, TEAMA, 0, 1);
        assertRegisterEvent(pa.nextEvent(), 1, PLAYERB, CREATUREB, TEAMB, 1, 1);
        assertRoundBegin(assertAndMerge(pb,pa),1);

        assertBoarSpawned(assertAndMerge(pb,pa),2,2,1);
        assertMoved(assertAndMerge(pb,pa),2,EAST);
        assertMoved(assertAndMerge(pb,pa),2,EAST);
        assertMoved(assertAndMerge(pb,pa),2,EAST);
        assertMoved(assertAndMerge(pb,pa),2,EAST);
        assertMoved(assertAndMerge(pb,pa),2,EAST);
        assertMoved(assertAndMerge(pb,pa),2,NORTH_EAST);

        assertActNow(assertAndMerge(pb,pa),0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pb,pa),0);

        assertActNow(assertAndMerge(pb,pa),1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pb,pa),1);

        assertFieldEffect(assertAndMerge(pb,pa),8,0,25);
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
