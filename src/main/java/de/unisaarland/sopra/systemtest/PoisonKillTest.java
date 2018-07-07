package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team14 on 23/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class PoisonKillTest extends OurSystemTest {
    /**
     */
    public PoisonKillTest() {
        super("PoisonKillTest");
    }

    @Override
    protected void executeTest() {
        // registration phase
        ClientConnection<Event> therazane = register(0, "Therazane", CreatureType.ROOK, "Deepholm", 1, 0);
        ClientConnection<Event> vashj = register(1, "Vashj", CreatureType.NAGA, "Vashjir", 0, 0);
        assertRegisterEvent(therazane.nextEvent(), 1, "Vashj", CreatureType.NAGA, "Vashjir", 0, 0);
        assertRegisterEvent(vashj.nextEvent(), 0, "Therazane", CreatureType.ROOK, "Deepholm", 1, 0);

        for (int round = 1; round < 18; round++) {
            // round starts
            assertRoundBegin(assertAndMerge(therazane, vashj), round);

            // therazane's turn
            assertActNow(assertAndMerge(therazane, vashj), 0);
            therazane.sendDoneActing();
            assertDoneActing(assertAndMerge(therazane, vashj), 0);

            // vashj's turn
            assertActNow(assertAndMerge(therazane, vashj), 1);
            vashj.sendCut(Direction.EAST);
            assertCut(assertAndMerge(therazane, vashj), 1, Direction.EAST);
            assertActNow(assertAndMerge(therazane, vashj), 1);
            vashj.sendDoneActing();
            assertDoneActing(assertAndMerge(therazane, vashj), 1);

            // round ends
            assertRoundEnd(assertAndMerge(therazane, vashj), round, 0);
        }

        // round 18 starts
        assertRoundBegin(assertAndMerge(therazane, vashj), 18);

        // therazane's turn
        assertActNow(assertAndMerge(therazane, vashj), 0);
        therazane.sendDoneActing();
        assertDoneActing(assertAndMerge(therazane, vashj), 0);

        // vashj's turn
        assertActNow(assertAndMerge(therazane, vashj), 1);
        vashj.sendBite(Direction.EAST);
        assertBitten(assertAndMerge(therazane, vashj), 1, Direction.EAST);
        assertActNow(assertAndMerge(therazane, vashj), 1);
        vashj.sendBite(Direction.EAST);
        assertBitten(assertAndMerge(therazane, vashj), 1, Direction.EAST);
        assertActNow(assertAndMerge(therazane, vashj), 1);
        vashj.sendDoneActing();
        assertDoneActing(assertAndMerge(therazane, vashj), 1);

        // round 18 ends
        assertRoundEnd(assertAndMerge(therazane, vashj), 18, 0);

        // round 19 starts
        assertRoundBegin(assertAndMerge(therazane, vashj), 19);

        // therazane's turn
        assertActNow(assertAndMerge(therazane, vashj), 0);
        therazane.sendDoneActing();
        assertDoneActing(assertAndMerge(therazane, vashj), 0);

        // vashj's turn
        assertActNow(assertAndMerge(therazane, vashj), 1);
        vashj.sendDoneActing();
        assertDoneActing(assertAndMerge(therazane, vashj), 1);

        // PoisonEffect Heavy on therazane
        assertPoisonEffect(assertAndMerge(therazane, vashj), 0, 4);
        assertDied(assertAndMerge(therazane, vashj), 0);

        // round 19 ends
        assertRoundEnd(assertAndMerge(therazane, vashj), 19, 0);
    }

    @Override
    protected String getMapFile() {
        return "01";
    }

    @Override
    protected String getFightFile() {
        return "Vashjir, Deepholm\n"
                + "Vashjir, Vashj, Naga\n"
                + "Deepholm, Therazane, Rook";
    }
}
