package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team14 on 23/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class SingeKillPlayerTest extends OurSystemTest {
    public SingeKillPlayerTest() {
        super("SingeKillPlayerTest");
    }

    @Override
    protected void executeTest() {
        // registration phase
        ClientConnection<Event> thera = register(0, "Therazane", CreatureType.ROOK, "Earth", 1, 0);
        ClientConnection<Event> rag = register(1, "Ragnaros", CreatureType.IFRIT, "Fire", 0, 0);

        assertRegisterEvent(rag.nextEvent(), 0, "Therazane", CreatureType.ROOK, "Earth", 1, 0);
        assertRegisterEvent(thera.nextEvent(), 1, "Ragnaros", CreatureType.IFRIT, "Fire", 0, 0);

        for (int round = 1; round < 5; round++) {
            // round begins
            assertRoundBegin(assertAndMerge(thera, rag), round);

            // thera's turn
            assertActNow(assertAndMerge(rag, thera), 0);
            thera.sendDoneActing();
            assertDoneActing(assertAndMerge(rag, thera), 0);

            // rag's turn
            for (int i = 0; i < 3; i++) {
                assertActNow(assertAndMerge(rag, thera), 1);
                rag.sendSinge(Direction.EAST);
                assertSinged(assertAndMerge(rag, thera), 1, Direction.EAST);
            }
            assertActNow(assertAndMerge(rag, thera), 1);
            rag.sendDoneActing();
            assertDoneActing(assertAndMerge(rag, thera), 1);

            // round ends
            assertRoundEnd(assertAndMerge(thera, rag), round, 0);
        }

        // round 5 begins
        assertRoundBegin(assertAndMerge(thera, rag), 5);

        // thera's turn
        assertActNow(assertAndMerge(rag, thera), 0);
        thera.sendDoneActing();
        assertDoneActing(assertAndMerge(rag, thera), 0);

        // rag's turn
        for (int i = 0; i < 3; i++) {
            assertActNow(assertAndMerge(rag, thera), 1);
            rag.sendSinge(Direction.EAST);
            assertSinged(assertAndMerge(rag, thera), 1, Direction.EAST);
        }
        assertDied(assertAndMerge(rag, thera), 0);
        assertActNow(assertAndMerge(rag, thera), 1);
        rag.sendDoneActing();
        assertDoneActing(assertAndMerge(rag, thera), 1);

        // round ends
        assertRoundEnd(assertAndMerge(thera, rag), 5, 0);

        // winner: Fire
        assertWinner(assertAndMerge(thera, rag), "Fire");
    }

    @Override
    protected String getMapFile() {
        return "01";
    }

    @Override
    protected String getFightFile() {
        return "Fire, Earth\n"
                + "Fire, Ragnaros, Ifrit\n"
                + "Earth, Therazane, Rook";
    }
}
