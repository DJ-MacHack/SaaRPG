package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team 14 on 23.09.2016.
 */
public class CanBurnBlinkOnWater extends OurSystemTest {

    public CanBurnBlinkOnWater(){
        super("CanBurnBlinkOnWater");
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> p1 = register(0, "N", CreatureType.IFRIT, "A", 0, 0);
        ClientConnection<Event> p2 = register(1, "M", CreatureType.WRAITH, "B", 1, 0);

        assertRegisterEvent(p1.nextEvent(), 1, "M", CreatureType.WRAITH, "B", 1, 0);
        assertRegisterEvent(p2.nextEvent(), 0, "N", CreatureType.IFRIT, "A", 0, 0);

        assertRoundBegin(assertAndMerge(p1, p2), 1);

        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendMove(Direction.SOUTH_EAST);
        assertMoved(assertAndMerge(p1, p2), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendBurn();
        assertKicked(assertAndMerge(p1, p2), 0);

        assertActNow(assertAndMerge(p1, p2), 1);
        p2.sendMove(Direction.SOUTH_EAST);
        assertMoved(assertAndMerge(p1, p2), 1, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(p1, p2), 1);
        p2.sendBlink(1, 0);
        assertBlinked(assertAndMerge(p1, p2), 1, 1, 0);
        assertActNow(assertAndMerge(p1, p2), 1);
        p2.sendDoneActing();
        assertDoneActing(assertAndMerge(p1, p2), 1);

        assertRoundEnd(assertAndMerge(p1, p2), 1, 0);
    }

    @Override
    protected String getMapFile() {
        return "01\n"+
                "~~";
    }

    @Override
    protected String getFightFile() {
        return "A, B\n"+
                "A, N, Ifrit\n"+
                "B, M, Wraith";
    }
}
