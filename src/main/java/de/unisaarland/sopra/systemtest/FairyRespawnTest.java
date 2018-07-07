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
public class FairyRespawnTest extends OurSystemTest {

    public FairyRespawnTest() {
        super("FairySpawnTest");
    }

    @Override
    protected void executeTest() {
        List<ClientConnection<Event>> ccs = new ArrayList<>();

        // register
        ClientConnection<Event> yeti = (register(0, "1", CreatureType.YETI, "A", 1, 0));
        ClientConnection<Event> yeti2 = (register(1, "2", CreatureType.YETI, "B", 2, 0));
        // check registration
        assertRegisterEvent(yeti.nextEvent(), 1, "2", CreatureType.YETI, "B", 2, 0);
        assertRegisterEvent(yeti2.nextEvent(), 0, "1", CreatureType.YETI, "A", 1, 0);

        ccs.add(yeti);
        ccs.add(yeti2);

        // Fairy Spawn
        assertFairySpawned(assertAndMerge(ccs), 2, 1, 1);

        // Round Begin
        assertRoundBegin(assertAndMerge(ccs), 1);

        // Movement of Fairy cannot happen

        // Act phase
        // yeti 1 crush fairy
        assertActNow(assertAndMerge(ccs), 0);
        yeti.sendCrush(Direction.SOUTH_EAST);
        assertCrush(assertAndMerge(ccs), 0, Direction.SOUTH_EAST);
        // again
        assertActNow(assertAndMerge(ccs), 0);
        yeti.sendCrush(Direction.SOUTH_EAST);
        assertCrush(assertAndMerge(ccs), 0, Direction.SOUTH_EAST);

        assertActNow(assertAndMerge(ccs), 0);
        yeti.sendDoneActing();
        assertDoneActing(assertAndMerge(ccs), 0);

        // fairy health 68
        // yeti 2 attacks

        assertActNow(assertAndMerge(ccs), 1);
        yeti2.sendCrush(Direction.SOUTH_WEST);
        assertCrush(assertAndMerge(ccs), 1, Direction.SOUTH_WEST);
        // again
        assertActNow(assertAndMerge(ccs), 1);
        yeti2.sendCrush(Direction.SOUTH_WEST);
        assertCrush(assertAndMerge(ccs), 1, Direction.SOUTH_WEST);

        assertActNow(assertAndMerge(ccs), 1);
        yeti2.sendDoneActing();
        assertDoneActing(assertAndMerge(ccs), 1);

        // fairy health 36
        // Field effect heal
        assertFieldEffect(assertAndMerge(ccs), 1, 1, -20);

        // fairy health 56
        // round end
        assertRoundEnd(assertAndMerge(ccs), 1, 0);

        // next round
        assertRoundBegin(assertAndMerge(ccs), 2);

        // yeti 2 is first
        // yeti 2 attacks

        assertActNow(assertAndMerge(ccs), 1);
        yeti2.sendCrush(Direction.SOUTH_WEST);
        assertCrush(assertAndMerge(ccs), 1, Direction.SOUTH_WEST);
        // again
        assertActNow(assertAndMerge(ccs), 1);
        yeti2.sendCrush(Direction.SOUTH_WEST);
        assertCrush(assertAndMerge(ccs), 1, Direction.SOUTH_WEST);

        assertActNow(assertAndMerge(ccs), 1);
        yeti2.sendDoneActing();
        assertDoneActing(assertAndMerge(ccs), 1);

        // fairy health 24

        // yeti 1 crush fairy
        assertActNow(assertAndMerge(ccs), 0);
        yeti.sendCrush(Direction.SOUTH_EAST);
        assertCrush(assertAndMerge(ccs), 0, Direction.SOUTH_EAST);
        // again
        assertActNow(assertAndMerge(ccs), 0);
        yeti.sendCrush(Direction.SOUTH_EAST);
        assertCrush(assertAndMerge(ccs), 0, Direction.SOUTH_EAST);
        // fairy dies
        assertDied(assertAndMerge(ccs), 2);

        assertActNow(assertAndMerge(ccs), 0);
        yeti.sendDoneActing();
        assertDoneActing(assertAndMerge(ccs), 0);

        // no field effect since fairy is dead
        // round end
        assertRoundEnd(assertAndMerge(ccs), 2, 0);

        // play till one round before boredom lose
        for (int round = 3; round < 103; round++) {
            assertRoundBegin(assertAndMerge(ccs), round);

            assertActNow(assertAndMerge(ccs), (round - 1) % 2);
            ccs.get((round - 1) % 2).sendDoneActing();
            assertDoneActing(assertAndMerge(ccs), (round - 1) % 2);

            assertActNow(assertAndMerge(ccs), round % 2);
            ccs.get(round % 2).sendDoneActing();
            assertDoneActing(assertAndMerge(ccs), round % 2);

            assertRoundEnd(assertAndMerge(ccs), round, round - 2);
        }
    }

    @Override
    protected String getMapFile() {
        return " 01\n" +
                "#+#";
    }

    @Override
    protected String getFightFile() {
        return "A, B\n" +
                "A, 1, Yeti\n" +
                "B, 2, Yeti";

    }
}
