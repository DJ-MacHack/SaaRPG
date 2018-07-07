package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team14 on 23/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class StareKillPlayerTest extends OurSystemTest {
    public StareKillPlayerTest() {
        super("StareKillPlayerTest");
    }

    @Override
    protected void executeTest() {
        // registration phase
        ClientConnection<Event> rag = register(0, "Ragnaros", CreatureType.IFRIT, "Fire", 0, 0);
        ClientConnection<Event> thera = register(1, "Therazane", CreatureType.ROOK, "Earth", 1, 0);

        assertRegisterEvent(rag.nextEvent(), 1, "Therazane", CreatureType.ROOK, "Earth", 1, 0);
        assertRegisterEvent(thera.nextEvent(), 0, "Ragnaros", CreatureType.IFRIT, "Fire", 0, 0);

        for (int round = 1; round < 5; round++) {
            // round begins
            assertRoundBegin(assertAndMerge(rag, thera), round);

            // rag's turn
            assertActNow(assertAndMerge(rag, thera), 0);
            rag.sendDoneActing();
            assertDoneActing(assertAndMerge(rag, thera), 0);

            // thera's turn
            for (int i = 0; i < 2; i++) {
                assertActNow(assertAndMerge(rag, thera), 1);
                thera.sendCast(0, 0);
                assertCast(assertAndMerge(rag, thera), 1, 0, 0);
            }
            assertActNow(assertAndMerge(rag, thera), 1);
            thera.sendDoneActing();
            assertDoneActing(assertAndMerge(rag, thera), 1);

            // round ends
            assertRoundEnd(assertAndMerge(rag, thera), round, 0);
        }

        // round 5 begins
        assertRoundBegin(assertAndMerge(rag, thera), 5);

        // rag's turn
        assertActNow(assertAndMerge(rag, thera), 0);
        rag.sendDoneActing();
        assertDoneActing(assertAndMerge(rag, thera), 0);

        // thera's turn
        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(rag, thera), 1);
            thera.sendStare(Direction.WEST);
            assertStared(assertAndMerge(rag, thera), 1, Direction.WEST);
        }
        assertDied(assertAndMerge(rag, thera), 0);
        assertActNow(assertAndMerge(rag, thera), 1);
        thera.sendDoneActing();
        assertDoneActing(assertAndMerge(rag, thera), 1);

        // round 5 ends
        assertRoundEnd(assertAndMerge(rag, thera), 5, 0);

        assertWinner(assertAndMerge(rag, thera), "Earth");
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
