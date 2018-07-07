package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team14 on 9/20/16.
 */
public class BoarReviveTest extends OurSystemTest {

    private final static String PLAYERA = "AndreasKieling";
    private final static String PLAYERB = "MarcelDavis";
    private final static CreatureType CREATUREA = CreatureType.YETI;
    private final static CreatureType CREATUREB = CreatureType.IFRIT;
    private final static String TEAMA = "YTK";
    private final static String TEAMB = "VollerEmpfang";
    private final static String VALIDFIGHTFILE = OurSystemTest.buildValidFightFile(TEAMA, TEAMB, PLAYERA, PLAYERB, CREATUREA.name(), CREATUREB.name());
    private final static String VALIDMAPFILE = ". 01";

    public BoarReviveTest() {
        super("BoarReviveTest");
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> pa = register(0, PLAYERA, CREATUREA, TEAMA, 2, 0);
        ClientConnection<Event> pb = register(1, PLAYERB, CREATUREB, TEAMB, 3, 0);
        assertRegisterEvent(pb.nextEvent(), 0, PLAYERA, CREATUREA, TEAMA, 2, 0);
        assertRegisterEvent(pa.nextEvent(), 1, PLAYERB, CREATUREB, TEAMB, 3, 0);

        assertRoundBegin(assertAndMerge(pa, pb), 1);
        assertBoarSpawned(assertAndMerge(pa, pb), 2, 0, 0);

        assertMoved(assertAndMerge(pa, pb), 2, Direction.EAST);
        assertBoarAttack(assertAndMerge(pa, pb), 2, 0);
        assertMoved(assertAndMerge(pa, pb), 2, Direction.WEST); //TODO Error: ActNow is sent?!? Maybe because boar does a 180° turn
        assertMoved(assertAndMerge(pa, pb), 2, Direction.EAST);
        assertBoarAttack(assertAndMerge(pa, pb), 2, 0);
        assertMoved(assertAndMerge(pa, pb), 2, Direction.WEST);
        assertMoved(assertAndMerge(pa, pb), 2, Direction.EAST);
        assertBoarAttack(assertAndMerge(pa, pb), 2, 0);
        assertMoved(assertAndMerge(pa, pb), 2, Direction.WEST);

        assertActNow(assertAndMerge(pa, pb), 0); //Yeti moves one tile left
        pa.sendMove(Direction.WEST);
        assertMoved(assertAndMerge(pa, pb), 0, Direction.WEST);

        assertActNow(assertAndMerge(pa, pb), 0); //Yeti kills the boar
        pa.sendCrush(Direction.WEST);
        assertCrush(assertAndMerge(pa, pb), 0, Direction.WEST);
        assertActNow(assertAndMerge(pa, pb), 0);
        pa.sendClaw(Direction.WEST);
        assertClaw(assertAndMerge(pa, pb), 0, Direction.WEST);
        assertDied(assertAndMerge(pa, pb), 2);
        assertActNow(assertAndMerge(pa, pb), 0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb), 0);

        assertActNow(assertAndMerge(pa, pb), 1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb), 1);

        assertRoundEnd(assertAndMerge(pa, pb), 1, 0);

        assertRoundBegin(assertAndMerge(pa, pb), 2); //Round 1 without Boar
        assertActNow(assertAndMerge(pa, pb), 1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb), 1);
        assertActNow(assertAndMerge(pa, pb), 0);
        pa.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(pa, pb), 0, Direction.EAST);
        assertActNow(assertAndMerge(pa, pb), 0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb), 0);
        assertRoundEnd(assertAndMerge(pa, pb), 2, 1);

        assertRoundBegin(assertAndMerge(pa, pb), 3); //Round 2 without Boar
        assertActNow(assertAndMerge(pa, pb), 1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb), 1);
        assertActNow(assertAndMerge(pa, pb), 0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb), 0);
        assertRoundEnd(assertAndMerge(pa, pb), 3, 2);

        assertRoundBegin(assertAndMerge(pa, pb), 4); //Round 3 without Boar
        assertActNow(assertAndMerge(pa, pb), 0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb), 0);
        assertActNow(assertAndMerge(pa, pb), 1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb), 1);
        assertRoundEnd(assertAndMerge(pa, pb), 4, 3);

        assertRoundBegin(assertAndMerge(pa, pb), 5); //Round 4 without Boar
        assertActNow(assertAndMerge(pa, pb), 1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb), 1);
        assertActNow(assertAndMerge(pa, pb), 0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb), 0);
        assertRoundEnd(assertAndMerge(pa, pb), 5, 4);

        assertRoundBegin(assertAndMerge(pa, pb), 6); //Round 5 without Boar
        assertActNow(assertAndMerge(pa, pb), 0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb), 0);
        assertActNow(assertAndMerge(pa, pb), 1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb), 1);
        assertRoundEnd(assertAndMerge(pa, pb), 6, 5);

        assertRoundBegin(assertAndMerge(pa, pb), 7); //Round 6 without Boar
        assertActNow(assertAndMerge(pa, pb), 1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb), 1);
        assertActNow(assertAndMerge(pa, pb), 0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb), 0);
        assertRoundEnd(assertAndMerge(pa, pb), 7, 6);

        assertRoundBegin(assertAndMerge(pa, pb), 8); //Round 7 without Boar
        assertBoarSpawned(assertAndMerge(pa, pb), 2, 0, 0);

        assertMoved(assertAndMerge(pa, pb), 2, Direction.EAST);
        assertBoarAttack(assertAndMerge(pa, pb), 2, 0);
        assertMoved(assertAndMerge(pa, pb), 2, Direction.WEST);
        assertMoved(assertAndMerge(pa, pb), 2, Direction.EAST);
        assertBoarAttack(assertAndMerge(pa, pb), 2, 0);
        assertMoved(assertAndMerge(pa, pb), 2, Direction.WEST);
        assertMoved(assertAndMerge(pa, pb), 2, Direction.EAST);
        assertBoarAttack(assertAndMerge(pa, pb), 2, 0);
        assertMoved(assertAndMerge(pa, pb), 2, Direction.WEST);

        assertActNow(assertAndMerge(pa, pb), 0);
        pa.sendClaw(Direction.WEST); //pa sends Claw to west, gets kicked because boar is out of reach
        assertKicked(assertAndMerge(pa, pb), 0);

        assertActNow(assertAndMerge(pa, pb), 1);
        pb.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb), 1);

        assertRoundEnd(assertAndMerge(pa, pb), 8, 0);
        assertWinner(assertAndMerge(pa, pb), TEAMB);
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
