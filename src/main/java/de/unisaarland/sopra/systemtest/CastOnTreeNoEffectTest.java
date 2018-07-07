package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team14 on 22/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class CastOnTreeNoEffectTest extends OurSystemTest {
    /**
     */
    public CastOnTreeNoEffectTest() {
        super("CastOnTreeNoEffectTest");
    }

    @Override
    protected void executeTest() {
        // register phase
        ClientConnection<Event> rook = register(0, "Rook", CreatureType.ROOK, "GuardiansOfTheGalaxy", 0, 0);
        ClientConnection<Event> vader = register(1, "DarthVader", CreatureType.WRAITH, "StarWars", 6, 0);

        assertRegisterEvent(rook.nextEvent(), 1, "DarthVader", CreatureType.WRAITH, "StarWars", 6, 0);
        assertRegisterEvent(vader.nextEvent(), 0, "Rook", CreatureType.ROOK, "GuardiansOfTheGalaxy", 0, 0);

        // round 1 begins
        assertRoundBegin(assertAndMerge(rook, vader), 1);

        // rook's turn
        assertActNow(assertAndMerge(rook, vader), 0);
        rook.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(rook, vader), 0, Direction.EAST);
        assertActNow(assertAndMerge(rook, vader), 0);
        rook.sendDoneActing();
        assertDoneActing(assertAndMerge(rook, vader), 0);

        // vader's turn
        assertActNow(assertAndMerge(rook, vader), 1);
        vader.sendDoneActing();
        assertDoneActing(assertAndMerge(rook, vader), 1);

        // round 1 ends
        assertRoundEnd(assertAndMerge(rook, vader), 1, 1);

        // round 2 begins
        assertRoundBegin(assertAndMerge(rook, vader), 2);

        // vader's turn
        assertActNow(assertAndMerge(rook, vader), 1);
        vader.sendDoneActing();
        assertDoneActing(assertAndMerge(rook, vader), 1);

        // rook's turn
        assertActNow(assertAndMerge(rook, vader), 0);
        rook.sendCast(6, 0);
        assertCast(assertAndMerge(rook, vader), 0, 6, 0);
        assertActNow(assertAndMerge(rook, vader), 0);
        rook.sendDoneActing();
        assertDoneActing(assertAndMerge(rook, vader), 0);

        // round 2 ends
        assertRoundEnd(assertAndMerge(rook, vader), 2, 0);

        // round 3 begins
        assertRoundBegin(assertAndMerge(rook, vader), 3);

        // vader's turn
        assertActNow(assertAndMerge(rook, vader), 1);
        vader.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(rook, vader), 1, Direction.EAST);
        assertActNow(assertAndMerge(rook, vader), 1);
        vader.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(rook, vader), 1, Direction.EAST);
        assertActNow(assertAndMerge(rook, vader), 1);
        vader.sendDoneActing();
        assertDoneActing(assertAndMerge(rook, vader), 1);

        // rook's turn
        assertActNow(assertAndMerge(rook, vader), 0);
        rook.sendCast(8, 0);
        assertKicked(assertAndMerge(rook, vader), 0);

        // round 2 ends
        assertRoundEnd(assertAndMerge(rook, vader), 3, 0);

        // winner: vader
        assertWinner(assertAndMerge(rook, vader), "StarWars");
    }

    @Override
    protected String getMapFile() {
        return "0t    1  ";
    }

    @Override
    protected String getFightFile() {
        return "GuardiansOfTheGalaxy, StarWars\n"
             + "GuardiansOfTheGalaxy, Rook, ROOK\n"
             + "StarWars, DarthVader, wraith";
    }
}
