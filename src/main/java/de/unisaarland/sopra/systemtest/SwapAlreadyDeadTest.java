package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team14 on 23/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class SwapAlreadyDeadTest extends OurSystemTest {
    public SwapAlreadyDeadTest() {
        super("SwapAlreadyDeadTest");
    }

    @Override
    protected void executeTest() {
        // register phase
        ClientConnection<Event> hakkar = register(0, "Hakkar", CreatureType.WRAITH, "Void", 0, 0);

        ClientConnection<Event> noc = register(1, "Nocturne", CreatureType.WRAITH, "Void", 1, 0);
        assertRegisterEvent(hakkar.nextEvent(), 1, "Nocturne", CreatureType.WRAITH, "Void", 1, 0);
        assertRegisterEvent(noc.nextEvent(), 0, "Hakkar", CreatureType.WRAITH, "Void", 0, 0);

        ClientConnection<Event> garen = register(2, "Garen", CreatureType.YETI, "Demacia", 2, 0);
        assertRegisterEvent(assertAndMerge(hakkar, noc), 2, "Garen", CreatureType.YETI, "Demacia", 2, 0);
        assertRegisterEvent(garen.nextEvent(), 0, "Hakkar", CreatureType.WRAITH, "Void", 0, 0);
        assertRegisterEvent(garen.nextEvent(), 1, "Nocturne", CreatureType.WRAITH, "Void", 1, 0);

        // round 1 begins
        assertRoundBegin(assertAndMerge(noc, garen, hakkar), 1);

        // hakkar's turn
        for (int attacks = 0; attacks < 4; attacks++) {
            assertActNow(assertAndMerge(hakkar, noc, garen), 0);
            hakkar.sendStab(Direction.EAST);
            assertStabbed(assertAndMerge(hakkar, noc, garen), 0, Direction.EAST);
        }
        assertActNow(assertAndMerge(hakkar, noc, garen), 0);
        hakkar.sendDoneActing();
        assertDoneActing(assertAndMerge(hakkar, noc, garen), 0);

        // noc's turn
        assertActNow(assertAndMerge(hakkar, noc, garen), 1);
        noc.sendDoneActing();
        assertDoneActing(assertAndMerge(hakkar, noc, garen), 1);

        // garen's turn
        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(hakkar, noc, garen), 2);
            garen.sendCrush(Direction.WEST);
            assertCrush(assertAndMerge(hakkar, noc, garen), 2, Direction.WEST);
        }
        assertActNow(assertAndMerge(hakkar, noc, garen), 2);
        garen.sendDoneActing();
        assertDoneActing(assertAndMerge(hakkar, noc, garen), 2);

        // round 1 ends: noc's hp: 40
        assertRoundEnd(assertAndMerge(hakkar, noc, garen), 1, 0);

        // round 2 begins
        assertRoundBegin(assertAndMerge(hakkar, noc, garen), 2);

        // noc's turn
        assertActNow(assertAndMerge(hakkar, noc, garen), 1);
        noc.sendDoneActing();
        assertDoneActing(assertAndMerge(hakkar, noc, garen), 1);

        // garen's turn
        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(hakkar, noc, garen), 2);
            garen.sendCrush(Direction.WEST);
            assertCrush(assertAndMerge(hakkar, noc, garen), 2, Direction.WEST);
        }
        assertActNow(assertAndMerge(hakkar, noc, garen), 2);
        garen.sendDoneActing();
        assertDoneActing(assertAndMerge(hakkar, noc, garen), 2);

        // noc's hp: 8
        // hakkar's turn
        assertActNow(assertAndMerge(hakkar, noc, garen), 0);
        hakkar.sendSwap(1, 0);
        assertSwapped(assertAndMerge(hakkar, noc, garen), 0, 1, 0);
        assertActNow(assertAndMerge(hakkar, noc, garen), 0);
        hakkar.sendSwap(0, 0);
        assertSwapped(assertAndMerge(hakkar, noc, garen), 0, 0, 0);
        // noc died
        assertDied(assertAndMerge(hakkar, noc, garen), 1);
        assertActNow(assertAndMerge(hakkar, noc, garen), 0);
        hakkar.sendDoneActing();
        assertDoneActing(assertAndMerge(hakkar, noc, garen), 0);

        // round 2 ends
        assertRoundEnd(assertAndMerge(hakkar, noc, garen), 2, 0);

        // round 3 begins
        assertRoundBegin(assertAndMerge(hakkar, noc, garen), 3);

        // hakkar's turn
        assertActNow(assertAndMerge(hakkar, noc, garen), 0);
        noc.sendWarCry("Am I dead? ;(");
        noc.sendSwap(0, 0);
        hakkar.sendSwap(1, 0);
        assertKicked(assertAndMerge(hakkar, noc, garen), 0);

        // garen's turn
        assertActNow(assertAndMerge(hakkar, noc, garen), 2);
        garen.sendDoneActing();
        assertDoneActing(assertAndMerge(hakkar, noc, garen), 2);

        // round 3 ends
        assertRoundEnd(assertAndMerge(hakkar, noc, garen), 3, 0);

        assertWinner(assertAndMerge(hakkar, noc, garen), "Demacia");
    }

    @Override
    protected String getMapFile() {
        return "001";
    }

    @Override
    protected String getFightFile() {
        return "Void, Demacia\n"
             + "Void, Hakkar, Wraith\n"
             + "Void, Nocturne, Wraith\n"
             + "Demacia, Garen, Yeti";
    }
}
