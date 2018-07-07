package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team14 on 23/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class PoisonKillBoarTest extends OurSystemTest {
    public PoisonKillBoarTest() {
        super("PoisonKillBoarTest");
    }

    @Override
    protected void executeTest() {
        // registration phase
        ClientConnection<Event> therazane = register(0, "Therazane", CreatureType.ROOK, "Deepholm", 0, 0);
        ClientConnection<Event> vashj = register(1, "Vashj", CreatureType.NAGA, "Vashjir", 1, 0);
        assertRegisterEvent(therazane.nextEvent(), 1, "Vashj", CreatureType.NAGA, "Vashjir", 1, 0);
        assertRegisterEvent(vashj.nextEvent(), 0, "Therazane", CreatureType.ROOK, "Deepholm", 0, 0);

        // round 1 starts
        assertRoundBegin(assertAndMerge(therazane, vashj), 1);

        // spawn Boar
        assertBoarSpawned(assertAndMerge(therazane, vashj), 2, 2, 0);

        // boar movement
        assertBoarAttack(assertAndMerge(therazane, vashj), 2, 1);

        // therazane's turn
        assertActNow(assertAndMerge(therazane, vashj), 0);
        therazane.sendDoneActing();
        assertDoneActing(assertAndMerge(therazane, vashj), 0);

        // vashj's turn
        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(therazane, vashj), 1);
            vashj.sendCut(Direction.EAST);
            assertCut(assertAndMerge(therazane, vashj), 1, Direction.EAST);
        }
        assertActNow(assertAndMerge(therazane, vashj), 1);
        vashj.sendDoneActing();
        assertDoneActing(assertAndMerge(therazane, vashj), 1);

        // round ends
        assertRoundEnd(assertAndMerge(therazane, vashj), 1, 0);

        // round 2 starts
        assertRoundBegin(assertAndMerge(therazane, vashj), 2);

        // boar movement
        assertBoarAttack(assertAndMerge(therazane, vashj), 2, 1);

        // therazane's turn
        assertActNow(assertAndMerge(therazane, vashj), 0);
        therazane.sendDoneActing();
        assertDoneActing(assertAndMerge(therazane, vashj), 0);

        // vashj's turn
        assertActNow(assertAndMerge(therazane, vashj), 1);
        vashj.sendBite(Direction.EAST);
        assertBitten(assertAndMerge(therazane, vashj), 1, Direction.EAST);
        assertActNow(assertAndMerge(therazane, vashj), 1);
        vashj.sendDoneActing();
        assertDoneActing(assertAndMerge(therazane, vashj), 1);

        // round 2 ends
        assertRoundEnd(assertAndMerge(therazane, vashj), 2, 0);

        // round 3 starts
        assertRoundBegin(assertAndMerge(therazane, vashj), 3);

        // boar movement
        assertBoarAttack(assertAndMerge(therazane, vashj), 2, 1);

        // therazane's turn
        assertActNow(assertAndMerge(therazane, vashj), 0);
        therazane.sendStab(Direction.EAST);
        assertKicked(assertAndMerge(therazane, vashj), 0);

        // vashj's turn
        assertActNow(assertAndMerge(therazane, vashj), 1);
        vashj.sendDoneActing();
        assertDoneActing(assertAndMerge(therazane, vashj), 1);

        // PoisonEffect Heavy on Boar
        assertPoisonEffect(assertAndMerge(therazane, vashj), 2, 4);
        assertDied(assertAndMerge(therazane, vashj), 2);

        // round 3 ends
        assertRoundEnd(assertAndMerge(therazane, vashj), 3, 0);

        assertWinner(assertAndMerge(therazane, vashj), "Vashjir");
    }

    @Override
    protected String getMapFile() {
        return "10.";
    }

    @Override
    protected String getFightFile() {
        return "Vashjir, Deepholm\n"
             + "Vashjir, Vashj, NAGA\n"
             + "Deepholm, Therazane, rook";
    }
}
