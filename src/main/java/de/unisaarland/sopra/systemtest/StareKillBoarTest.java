package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team14 on 23/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class StareKillBoarTest extends OurSystemTest {
    public StareKillBoarTest() {
        super("StareKillBoarTest");
    }

    @Override
    protected void executeTest() {
        // registration phase
        ClientConnection<Event> rag = register(0, "Ragnaros", CreatureType.IFRIT, "Fire", 0, 0);
        ClientConnection<Event> thera = register(1, "Therazane", CreatureType.ROOK, "Earth", 2, 0);

        assertRegisterEvent(rag.nextEvent(), 1, "Therazane", CreatureType.ROOK, "Earth", 2, 0);
        assertRegisterEvent(thera.nextEvent(), 0, "Ragnaros", CreatureType.IFRIT, "Fire", 0, 0);

        // round 1 begins
        assertRoundBegin(assertAndMerge(rag, thera), 1);

        // boar spawn
        assertBoarSpawned(assertAndMerge(rag, thera), 2, 1, 0);

        // boar attack
        assertBoarAttack(assertAndMerge(rag, thera), 2, 1);
        assertBoarAttack(assertAndMerge(rag, thera), 2, 0);

        // rag's turn
        assertActNow(assertAndMerge(rag, thera), 0);
        rag.sendDoneActing();
        assertDoneActing(assertAndMerge(rag, thera), 0);

        // thera's turn
        for (int i = 0; i < 3; i++) {
            assertActNow(assertAndMerge(rag, thera), 1);
            thera.sendStare(Direction.WEST);
            assertStared(assertAndMerge(rag, thera), 1, Direction.WEST);
        }
        assertActNow(assertAndMerge(rag, thera), 1);
        thera.sendDoneActing();
        assertDoneActing(assertAndMerge(rag, thera), 1);

        // round 1 ends
        assertRoundEnd(assertAndMerge(rag, thera), 1, 0);

        // round 2 begins
        assertRoundBegin(assertAndMerge(rag, thera), 2);

        // boar attack
        assertBoarAttack(assertAndMerge(rag, thera), 2, 1);
        assertBoarAttack(assertAndMerge(rag, thera), 2, 0);

        // rag's turn
        assertActNow(assertAndMerge(rag, thera), 0);
        rag.sendDoneActing();
        assertDoneActing(assertAndMerge(rag, thera), 0);

        // thera's turn
        assertActNow(assertAndMerge(rag, thera), 1);
        thera.sendStare(Direction.WEST);
        assertStared(assertAndMerge(rag, thera), 1, Direction.WEST);
        assertDied(assertAndMerge(rag, thera), 2);
        assertActNow(assertAndMerge(rag, thera), 1);
        thera.sendDoneActing();
        assertDoneActing(assertAndMerge(rag, thera), 1);

        // round 2 ends
        assertRoundEnd(assertAndMerge(rag, thera), 2, 0);
    }

    @Override
    protected String getMapFile() {
        return "0.1";
    }

    @Override
    protected String getFightFile() {
        return "Fire, Earth\n"
                + "Fire, Ragnaros, Ifrit\n"
                + "Earth, Therazane, Rook";
    }
}
