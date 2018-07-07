package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 22.09.16.
 *
 * @author Henrik Paul Koehn
 */
public class MoveOutOfAnchorFairyTest extends OurSystemTest {

    public MoveOutOfAnchorFairyTest() {
        super("MoveOutOfAnchorFairyTest");
    }

    @Override
    protected void executeTest() {
        List<ClientConnection<Event>> ccs = new ArrayList<>();

        ClientConnection<Event> wraith = register(0, "alpha", CreatureType.WRAITH, "A", 11, 0);
        ClientConnection<Event> naga = register(1, "beta", CreatureType.NAGA, "B", 12, 0);
        assertRegisterEvent(naga.nextEvent(), 0, "alpha", CreatureType.WRAITH, "A" ,11, 0);
        assertRegisterEvent(wraith.nextEvent(), 1, "beta", CreatureType.NAGA, "B" ,12, 0);

        ccs.add(wraith);
        ccs.add(naga);

        assertFairySpawned(assertAndMerge(ccs), 2, 0, 0);


        assertRoundBegin(assertAndMerge(ccs), 1);


        // Move Fairy
        for(int i = 0; i < 4; i++){
            assertMoved(assertAndMerge(ccs), 2, Direction.EAST);
        }

        // Act Phase doing nothing
        for(int k = 0; k < 2; k++) {
            assertActNow(assertAndMerge(ccs), k);
            ccs.get(k).sendDoneActing();
            assertDoneActing(assertAndMerge(ccs), k);
        }
        assertRoundEnd(assertAndMerge(ccs), 1, 1);

        // Round 2

        assertRoundBegin(assertAndMerge(ccs), 2);

        // Fairy Movement
        for(int i = 0; i < 4; i++){
            assertMoved(assertAndMerge(ccs), 2, Direction.EAST);
        }
        // Naga does nothing
        assertActNow(assertAndMerge(ccs), 1);
        naga.sendDoneActing();
        assertDoneActing(assertAndMerge(ccs), 1);

        // Wraith swaps with fairy
        assertActNow(assertAndMerge(ccs), 0);
        wraith.sendSwap(8,0);
        assertSwapped(assertAndMerge(ccs), 0, 8, 0);

        // And is done
        assertActNow(assertAndMerge(ccs), 0);
        wraith.sendDoneActing();
        assertDoneActing(assertAndMerge(ccs), 0);

        // No field or poison effects
        assertRoundEnd(assertAndMerge(ccs), 2, 0);

        assertRoundBegin(assertAndMerge(ccs), 3);

        // no fairy dies since she is not within her anchorpoint

        assertDied(assertAndMerge(ccs), 2);
        assertActNow(assertAndMerge(ccs), 1);
        wraith.sendDoneActing();
        assertKicked(assertAndMerge(ccs), 0);
        naga.sendDoneActing();
        assertDoneActing(assertAndMerge(ccs), 1);
        assertRoundEnd(assertAndMerge(ccs), 3, 0);
        assertWinner(assertAndMerge(ccs), "B");
    }

    @Override
    protected String getMapFile() {
        return "+          01";
    }

    @Override
    protected String getFightFile() {
        return "A, B\n" +
                "A, alpha, Wraith\n" +
                "B, beta, nagA";
    }
}
