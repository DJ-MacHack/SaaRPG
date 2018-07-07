package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team14 on 23/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class SwapNotYourTurnTest extends OurSystemTest {
    public SwapNotYourTurnTest() {
        super("SwapNotYourTurnTest");
    }

    @Override
    protected void executeTest() {
        // register phase
        ClientConnection<Event> noc = register(0, "Nocturne", CreatureType.WRAITH, "Void", 0, 0);
        ClientConnection<Event> garen = register(1, "Garen", CreatureType.YETI, "Demacia", 8, 0);

        assertRegisterEvent(noc.nextEvent(), 1, "Garen", CreatureType.YETI, "Demacia", 8, 0);
        assertRegisterEvent(garen.nextEvent(), 0, "Nocturne", CreatureType.WRAITH, "Void", 0, 0);

        // round 1 begins
        assertRoundBegin(assertAndMerge(noc, garen), 1);

        // noc's turn
        assertActNow(assertAndMerge(noc, garen), 0);
        noc.sendDoneActing();
        assertDoneActing(assertAndMerge(noc, garen), 0);

        // garen's turn
        assertActNow(assertAndMerge(noc, garen), 1);
        noc.sendSwap(8, 0);
        assertKicked(assertAndMerge(noc, garen), 0);
        garen.sendDoneActing();
        assertDoneActing(assertAndMerge(noc, garen), 1);

        // round 1 ends
        assertRoundEnd(assertAndMerge(noc, garen), 1, 0);

        assertWinner(assertAndMerge(noc, garen), "Demacia");
    }

    @Override
    protected String getMapFile() {
        return "0       1";
    }

    @Override
    protected String getFightFile() {
        return "Void, Demacia\n"
             + "Void, Nocturne, Wraith\n"
             + "Demacia, Garen, Yeti";
    }
}
