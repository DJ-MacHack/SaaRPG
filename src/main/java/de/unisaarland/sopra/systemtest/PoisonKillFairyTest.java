package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team14 on 23/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class PoisonKillFairyTest extends OurSystemTest {
    public PoisonKillFairyTest() {
        super("PoisonKillFairyTest");
    }

    @Override
    protected void executeTest() {
        // registration phase
        ClientConnection<Event> therazane = register(0, "Therazane", CreatureType.ROOK, "Deepholm", 0, 0);

        ClientConnection<Event> vashj = register(1, "Vashj", CreatureType.NAGA, "Vashjir", 1, 0);
        assertRegisterEvent(therazane.nextEvent(), 1, "Vashj", CreatureType.NAGA, "Vashjir", 1, 0);
        assertRegisterEvent(vashj.nextEvent(), 0, "Therazane", CreatureType.ROOK, "Deepholm", 0, 0);

        ClientConnection<Event> karathress = register(2, "Karathress", CreatureType.NAGA, "Vashjir", 3, 0);
        assertRegisterEvent(karathress.nextEvent(), 1, "Vashj", CreatureType.NAGA, "Vashjir", 1, 0);
        assertRegisterEvent(karathress.nextEvent(), 0, "Therazane", CreatureType.ROOK, "Deepholm", 0, 0);
        assertRegisterEvent(assertAndMerge(therazane, vashj), 2, "Karathress", CreatureType.NAGA, "Vashjir", 3, 0);

        // spawn Fairy
        assertFairySpawned(assertAndMerge(therazane, vashj, karathress), 3, 2, 0);

        for (int round = 1; round < 8; round++) {
            // round starts
            assertRoundBegin(assertAndMerge(therazane, vashj, karathress), round);

            // therazane's turn
            assertActNow(assertAndMerge(therazane, vashj, karathress), 0);
            therazane.sendDoneActing();
            assertDoneActing(assertAndMerge(therazane, vashj, karathress), 0);

            // vashj's turn
            for (int i = 0; i < 2; i++) {
                assertActNow(assertAndMerge(therazane, vashj, karathress), 1);
                vashj.sendCut(Direction.EAST);
                assertCut(assertAndMerge(therazane, vashj, karathress), 1, Direction.EAST);
            }
            assertActNow(assertAndMerge(therazane, vashj, karathress), 1);
            vashj.sendDoneActing();
            assertDoneActing(assertAndMerge(therazane, vashj, karathress), 1);

            // karathress' turn
            for (int i = 0; i < 4; i++) {
                assertActNow(assertAndMerge(therazane, vashj, karathress), 2);
                karathress.sendCut(Direction.WEST);
                assertCut(assertAndMerge(therazane, vashj, karathress), 2, Direction.WEST);
            }
            assertActNow(assertAndMerge(therazane, vashj, karathress), 2);
            karathress.sendDoneActing();
            assertDoneActing(assertAndMerge(therazane, vashj, karathress), 2);

            // fairy is healed
            assertFieldEffect(assertAndMerge(therazane, vashj, karathress), 2, 0, -20);

            // round ends
            assertRoundEnd(assertAndMerge(therazane, vashj, karathress), round, 0);
        }

        // round 8 starts
        assertRoundBegin(assertAndMerge(therazane, vashj, karathress), 8);

        // therazane's turn
        assertActNow(assertAndMerge(therazane, vashj, karathress), 0);
        therazane.sendDoneActing();
        assertDoneActing(assertAndMerge(therazane, vashj, karathress), 0);

        // vashj's turn
        assertActNow(assertAndMerge(therazane, vashj, karathress), 1);
        vashj.sendBite(Direction.EAST);
        assertBitten(assertAndMerge(therazane, vashj, karathress), 1, Direction.EAST);
        assertActNow(assertAndMerge(therazane, vashj, karathress), 1);
        vashj.sendBite(Direction.EAST);
        assertBitten(assertAndMerge(therazane, vashj, karathress), 1, Direction.EAST);
        assertActNow(assertAndMerge(therazane, vashj, karathress), 1);
        vashj.sendDoneActing();
        assertDoneActing(assertAndMerge(therazane, vashj, karathress), 1);

        // karathress' turn
        assertActNow(assertAndMerge(therazane, vashj, karathress), 2);
        karathress.sendBite(Direction.WEST);
        assertBitten(assertAndMerge(therazane, vashj, karathress), 2, Direction.WEST);
        assertActNow(assertAndMerge(therazane, vashj, karathress), 2);
        karathress.sendBite(Direction.WEST);
        assertBitten(assertAndMerge(therazane, vashj, karathress), 2, Direction.WEST);
        assertActNow(assertAndMerge(therazane, vashj, karathress), 2);
        karathress.sendDoneActing();
        assertDoneActing(assertAndMerge(therazane, vashj, karathress), 2);

        // fairy is healed
        assertFieldEffect(assertAndMerge(therazane, vashj, karathress), 2, 0, -20);

        // round 8 ends
        assertRoundEnd(assertAndMerge(therazane, vashj, karathress), 8, 0);

        // round 9 starts
        assertRoundBegin(assertAndMerge(therazane, vashj, karathress), 9);

        // therazane's turn
        assertActNow(assertAndMerge(therazane, vashj, karathress), 0);
        therazane.sendDoneActing();
        assertDoneActing(assertAndMerge(therazane, vashj, karathress), 0);

        // karathress' turn
        assertActNow(assertAndMerge(therazane, vashj, karathress), 2);
        karathress.sendBite(Direction.WEST);
        assertBitten(assertAndMerge(therazane, vashj, karathress), 2, Direction.WEST);
        assertActNow(assertAndMerge(therazane, vashj, karathress), 2);
        karathress.sendBite(Direction.WEST);
        assertBitten(assertAndMerge(therazane, vashj, karathress), 2, Direction.WEST);
        assertActNow(assertAndMerge(therazane, vashj, karathress), 2);
        karathress.sendDoneActing();
        assertDoneActing(assertAndMerge(therazane, vashj, karathress), 2);

        // vashj's turn
        assertActNow(assertAndMerge(therazane, vashj, karathress), 1);
        vashj.sendBite(Direction.EAST);
        assertBitten(assertAndMerge(therazane, vashj, karathress), 1, Direction.EAST);
        assertActNow(assertAndMerge(therazane, vashj, karathress), 1);
        vashj.sendBite(Direction.EAST);
        assertBitten(assertAndMerge(therazane, vashj, karathress), 1, Direction.EAST);
        assertActNow(assertAndMerge(therazane, vashj, karathress), 1);
        vashj.sendDoneActing();
        assertDoneActing(assertAndMerge(therazane, vashj, karathress), 1);

        // fairy is healed
        assertFieldEffect(assertAndMerge(therazane, vashj, karathress), 2, 0, -20);
        assertPoisonEffect(assertAndMerge(therazane, vashj, karathress), 3, 4);
        assertPoisonEffect(assertAndMerge(therazane, vashj, karathress), 3, 4);
        assertPoisonEffect(assertAndMerge(therazane, vashj, karathress), 3, 4);
        assertPoisonEffect(assertAndMerge(therazane, vashj, karathress), 3, 4);

        // round 9 ends
        assertRoundEnd(assertAndMerge(therazane, vashj, karathress), 9, 0);

        // round 10 starts
        assertRoundBegin(assertAndMerge(therazane, vashj, karathress), 10);

        // therazane's turn
        assertActNow(assertAndMerge(therazane, vashj, karathress), 0);
        therazane.sendCast(0, 1);
        assertKicked(assertAndMerge(therazane, vashj, karathress), 0);

        // vashj's turn
        assertActNow(assertAndMerge(therazane, vashj, karathress), 1);
        vashj.sendCut(Direction.EAST);
        assertCut(assertAndMerge(therazane, vashj, karathress), 1, Direction.EAST);
        assertActNow(assertAndMerge(therazane, vashj, karathress), 1);
        vashj.sendDoneActing();
        assertDoneActing(assertAndMerge(therazane, vashj, karathress), 1);

        // karathress' turn
        assertActNow(assertAndMerge(therazane, vashj, karathress), 2);
        karathress.sendDoneActing();
        assertDoneActing(assertAndMerge(therazane, vashj, karathress), 2);

        // HealEffect on Fairy
        assertFieldEffect(assertAndMerge(therazane, vashj, karathress), 2, 0, -20);
        assertPoisonEffect(assertAndMerge(therazane, vashj, karathress), 3, 2);
        assertPoisonEffect(assertAndMerge(therazane, vashj, karathress), 3, 2);
        assertPoisonEffect(assertAndMerge(therazane, vashj, karathress), 3, 2);
        assertPoisonEffect(assertAndMerge(therazane, vashj, karathress), 3, 2);
        assertPoisonEffect(assertAndMerge(therazane, vashj, karathress), 3, 4);
        assertPoisonEffect(assertAndMerge(therazane, vashj, karathress), 3, 4);
        assertPoisonEffect(assertAndMerge(therazane, vashj, karathress), 3, 4);
        assertPoisonEffect(assertAndMerge(therazane, vashj, karathress), 3, 4);
        assertDied(assertAndMerge(therazane, vashj, karathress), 3);

        // round 10 ends
        assertRoundEnd(assertAndMerge(therazane, vashj, karathress), 10, 0);

        assertWinner(assertAndMerge(therazane, vashj, karathress), "Vashjir");
    }

    @Override
    protected String getMapFile() {
        return "10+0";
    }

    @Override
    protected String getFightFile() {
        return "Vashjir, Deepholm\n"
             + "Vashjir, Vashj, NAGA\n"
             + "Vashjir, Karathress, NAgA\n"
             + "Deepholm, Therazane, rook";
    }
}
