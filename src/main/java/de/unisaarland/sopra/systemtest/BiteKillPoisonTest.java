package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team14 on 22/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class BiteKillPoisonTest extends OurSystemTest {
    /**
     */
    public BiteKillPoisonTest() {
        super("BiteKillPoisonTest");
    }

    @Override
    protected void executeTest() {
        // register phase
        ClientConnection<Event> rook = register(0, "rook", CreatureType.ROOK, "RookTeam", 0, 0);
        ClientConnection<Event> rook2 = register(1, "rook2", CreatureType.ROOK, "RookTeam", 1, 0);
        assertRegisterEvent(rook2.nextEvent(), 0, "rook", CreatureType.ROOK, "RookTeam", 0, 0);
        assertRegisterEvent(rook.nextEvent(), 1, "rook2", CreatureType.ROOK, "RookTeam", 1, 0);

        ClientConnection<Event> naga = register(2, "naga", CreatureType.NAGA, "NagaTeam", 2, 0);
        assertRegisterEvent(naga.nextEvent(), 0, "rook", CreatureType.ROOK, "RookTeam", 0, 0);
        assertRegisterEvent(naga.nextEvent(), 1, "rook2", CreatureType.ROOK, "RookTeam", 1, 0);
        assertRegisterEvent(assertAndMerge(rook, rook2), 2, "naga", CreatureType.NAGA, "NagaTeam", 2, 0);

        // round 1 starts
        assertRoundBegin(assertAndMerge(naga, rook, rook2), 1);

        // rook's turn
        assertActNow(assertAndMerge(naga, rook, rook2), 0);
        rook.sendDoneActing();
        assertDoneActing(assertAndMerge(naga, rook, rook2), 0);

        // rook2's turn
        assertActNow(assertAndMerge(naga, rook, rook2), 1);
        rook2.sendCast(1, 0);
        assertCast(assertAndMerge(rook, rook2, naga), 1, 1, 0);
        assertActNow(assertAndMerge(naga, rook, rook2), 1);
        rook2.sendCast(1, 0);
        assertCast(assertAndMerge(rook, rook2, naga), 1, 1, 0);
        assertActNow(assertAndMerge(naga, rook, rook2), 1);
        rook2.sendDoneActing();
        assertDoneActing(assertAndMerge(naga, rook, rook2), 1);

        // naga's turn
        for (int j = 0; j < 5; j++) {
            assertActNow(assertAndMerge(naga, rook, rook2), 2);
            naga.sendCut(Direction.WEST);
            assertCut(assertAndMerge(naga, rook, rook2), 2, Direction.WEST);
        }
        assertActNow(assertAndMerge(naga, rook, rook2), 2);
        naga.sendDoneActing();
        assertDoneActing(assertAndMerge(naga, rook, rook2), 2);

        // round 1 ends
        assertRoundEnd(assertAndMerge(naga, rook, rook2), 1, 0);

        // round 2 starts
        assertRoundBegin(assertAndMerge(naga, rook, rook2), 2);

        // rook's turn
        assertActNow(assertAndMerge(naga, rook, rook2), 0);
        rook.sendDoneActing();
        assertDoneActing(assertAndMerge(naga, rook, rook2), 0);

        // rook2's turn
        assertActNow(assertAndMerge(naga, rook, rook2), 1);
        rook2.sendCast(1, 0);
        assertCast(assertAndMerge(rook, rook2, naga), 1, 1, 0);
        assertActNow(assertAndMerge(naga, rook, rook2), 1);
        rook2.sendCast(1, 0);
        assertCast(assertAndMerge(rook, rook2, naga), 1, 1, 0);
        assertActNow(assertAndMerge(naga, rook, rook2), 1);
        rook2.sendDoneActing();
        assertDoneActing(assertAndMerge(naga, rook, rook2), 1);

        // naga's turn
        for (int k = 0; k < 5; k++) {
            assertActNow(assertAndMerge(naga, rook, rook2), 2);
            naga.sendCut(Direction.WEST);
            assertCut(assertAndMerge(naga, rook, rook2), 2, Direction.WEST);
        }
        assertActNow(assertAndMerge(naga, rook, rook2), 2);
        naga.sendDoneActing();
        assertDoneActing(assertAndMerge(naga, rook, rook2), 2);

        // round 2 ends
        assertRoundEnd(assertAndMerge(naga, rook, rook2), 2, 0);

        // round 3 starts
        assertRoundBegin(assertAndMerge(naga, rook, rook2), 3);

        // rook's turn
        assertActNow(assertAndMerge(naga, rook, rook2), 0);
        rook.sendDoneActing();
        assertDoneActing(assertAndMerge(naga, rook, rook2), 0);

        // rook2's turn
        assertActNow(assertAndMerge(naga, rook, rook2), 1);
        rook2.sendDoneActing();
        assertDoneActing(assertAndMerge(naga, rook, rook2), 1);

        // naga's turn
        assertActNow(assertAndMerge(naga, rook, rook2), 2);
        naga.sendBite(Direction.WEST);
        assertBitten(assertAndMerge(naga, rook, rook2), 2, Direction.WEST);
        assertActNow(assertAndMerge(naga, rook, rook2), 2);
        naga.sendBite(Direction.WEST);
        assertBitten(assertAndMerge(naga, rook, rook2), 2, Direction.WEST);
        assertDied(assertAndMerge(naga, rook, rook2), 1);

        assertActNow(assertAndMerge(naga, rook, rook2), 2);
        naga.sendDoneActing();
        assertDoneActing(assertAndMerge(naga, rook, rook2), 2);

        // round 3 ends
        assertRoundEnd(assertAndMerge(naga, rook2, rook), 3, 0);

        // round 4 starts
        assertRoundBegin(assertAndMerge(naga, rook2, rook), 4);

        // rook's turn
        assertActNow(assertAndMerge(naga, rook, rook2), 0);
        rook.sendDoneActing();
        assertDoneActing(assertAndMerge(naga, rook, rook2), 0);

        // naga's turn
        assertActNow(assertAndMerge(naga, rook2, rook), 2);
        naga.sendMove(Direction.WEST);
        assertMoved(assertAndMerge(naga, rook2, rook), 2, Direction.WEST);
        assertActNow(assertAndMerge(naga, rook, rook2), 2);
        naga.sendDoneActing();
        assertDoneActing(assertAndMerge(naga, rook, rook2), 2);

        // round 4 ends
        assertRoundEnd(assertAndMerge(naga, rook2, rook), 4, 1);

        // round 5 starts
        assertRoundBegin(assertAndMerge(naga, rook2, rook), 5);

        // rook's turn
        assertActNow(assertAndMerge(naga, rook, rook2), 0);
        rook.sendCast(2, 0);
        assertKicked(assertAndMerge(naga, rook2, rook), 0);

        // naga's turn
        assertActNow(assertAndMerge(naga, rook, rook2), 2);
        naga.sendDoneActing();
        assertDoneActing(assertAndMerge(naga, rook, rook2), 2);

        // round 5 ends
        assertRoundEnd(assertAndMerge(naga, rook2, rook), 5, 0);

        // winner: naga
        assertWinner(assertAndMerge(naga, rook2, rook), "NagaTeam");
    }

    @Override
    protected String getMapFile() {
        return "001";
    }

    @Override
    protected String getFightFile() {
        return "RookTeam, NagaTeam\n"
             + "RookTeam, rook, Rook\n"
             + "RookTeam, rook2, ROOK\n"
             + "NagaTeam, naga, Naga";
    }
}
