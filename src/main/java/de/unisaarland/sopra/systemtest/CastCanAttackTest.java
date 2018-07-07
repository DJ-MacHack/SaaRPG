package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Lukas Kirschner on 9/23/16.
 */
public class CastCanAttackTest extends OurSystemTest {
    private final static String PLAYERA = "VictimIfrit";
    private final static String PLAYERB = "IfritTheBoss";
    private final static String PLAYERC = "AnotherVictimIfrit";
    private final static String TEAMA = "TeamLappen";
    private final static String TEAMB = "TeamIfrit";
    private final static CreatureType CREATUREA = CreatureType.ROOK;
    private final static CreatureType CREATUREB = CreatureType.ROOK;
    private final static CreatureType CREATUREC = CreatureType.ROOK;
    private final static String VALIDFIGHTFILE =
            TEAMA + ", " + TEAMB + "\n" +
                    TEAMA + ", " + PLAYERA + ", " + CREATUREA.toString() + "\n" +
                    TEAMB + ", " + PLAYERB + ", " + CREATUREB.toString() + "\n" +
                    TEAMA + ", " + PLAYERC + ", " + CREATUREC.toString();
    public final static String VALIDMAPFILE = "010                                        ";

    public CastCanAttackTest() {
        super("CastCanAttackTest");
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
        assertActNow(assertAndMerge(pa, pb, pc),0);
        pc.sendCast(1,0); //Player C sends a cast attack although Player A is on turn
        assertKicked(assertAndMerge(pa, pb, pc),2); //Player C gets kicked
        pa.sendCast(1,0); //A casts against B
        assertCast(assertAndMerge(pa, pb, pc),0,1,0);
        assertActNow(assertAndMerge(pa, pb, pc),0);
        pa.sendCast(1,0); //again
        assertCast(assertAndMerge(pa, pb, pc),0,1,0);
        assertActNow(assertAndMerge(pa, pb, pc),0);
        pa.sendCast(1,0); //A has insufficient energy and gets kicked
        assertKicked(assertAndMerge(pa, pb, pc),0);
        assertActNow(assertAndMerge(pa, pb, pc),1);
        pc.sendCast(1,0); //PC is dead and not on turn
        pb.sendDoneActing(); //PB is finished
        assertDoneActing(assertAndMerge(pa, pb, pc),1);
        assertRoundEnd(assertAndMerge(pa, pb, pc),1,0);
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
