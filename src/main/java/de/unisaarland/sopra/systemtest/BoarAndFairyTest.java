package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hpk on 19.09.16.
 */
public class BoarAndFairyTest extends OurSystemTest {

    public BoarAndFairyTest() {
        super("Boar and Fairy Test");
    }

    @Override
    protected String getMapFile() {
        return "  +~  X\n" +
                "  0A^^_\n" +
                "##+.   \n" +
                "     1 \n" +
                "~~~###~\n" +
                "$$$$$T_";
    }

    @Override
    protected String getFightFile() {
        return "Alpha, Beta\n" +
                "Alpha, Floppy, Rook\n" +
                "Beta, Bobby, Ifrit";
    }

    @Override
    protected void executeTest() {
        List<ClientConnection<Event>> ccs = new ArrayList<>();

        // Registration Phase
        ClientConnection<Event> floppy = register(0, "Floppy", CreatureType.ROOK, "Alpha", 2, 1);
        ccs.add(floppy);
        ClientConnection<Event> bobby = register(1, "Bobby", CreatureType.IFRIT, "Beta", 4, 3);
        ccs.add(bobby);
        assertRegisterEvent(floppy.nextEvent(), 1, "Bobby", CreatureType.IFRIT, "Beta", 4, 3);
        assertRegisterEvent(bobby.nextEvent(), 0, "Floppy", CreatureType.ROOK, "Alpha", 2, 1);

        // Fairy Spawn Events
        assertFairySpawned(assertAndMerge(ccs), 2, 2, 0); // Fairy 1
        assertFairySpawned(assertAndMerge(ccs), 3, 1, 2); // Fairy 2

        // Round Begin
        assertRoundBegin(assertAndMerge(ccs), 1);

        // Boar Spawn
        assertBoarSpawned(assertAndMerge(ccs), 4, 6, 0); // Boar 1
        assertBoarSpawned(assertAndMerge(ccs), 5, 3, 1); // Boar 2
        assertBoarSpawned(assertAndMerge(ccs), 6, 2, 2); // Boar 3
        assertBoarSpawned(assertAndMerge(ccs), 7, 3, 5); // Boar 4

        // Fairy 1 Move
        assertMoved(assertAndMerge(ccs), 2, Direction.WEST);
        assertMoved(assertAndMerge(ccs), 2, Direction.WEST);
        assertMoved(assertAndMerge(ccs), 2, Direction.SOUTH_EAST);
        assertMoved(assertAndMerge(ccs), 2, Direction.EAST);

        // Fairy 2 Move
        assertMoved(assertAndMerge(ccs), 3,Direction.SOUTH_WEST);
        assertMoved(assertAndMerge(ccs), 3,Direction.EAST);
        assertMoved(assertAndMerge(ccs), 3,Direction.EAST);
        assertMoved(assertAndMerge(ccs), 3,Direction.EAST);

        // Boar 1 Move
        assertMoved(assertAndMerge(ccs), 4, Direction.WEST);
        assertMoved(assertAndMerge(ccs), 4, Direction.WEST);
        assertBoarAttack(assertAndMerge(ccs), 4, 5);
        assertMoved(assertAndMerge(ccs), 4, Direction.SOUTH_EAST);
        assertMoved(assertAndMerge(ccs), 4, Direction.SOUTH_EAST);
        assertBoarAttack(assertAndMerge(ccs), 4, 1);
        assertMoved(assertAndMerge(ccs), 4, Direction.EAST);
        assertMoved(assertAndMerge(ccs), 4, Direction.NORTH_EAST);

        // Boar 2 Move
        assertMoved(assertAndMerge(ccs), 5, Direction.EAST);
        assertMoved(assertAndMerge(ccs), 5, Direction.EAST);
        assertBoarAttack(assertAndMerge(ccs), 5, 4);
        assertMoved(assertAndMerge(ccs), 5, Direction.NORTH_EAST);
        assertMoved(assertAndMerge(ccs), 5, Direction.WEST);
        assertMoved(assertAndMerge(ccs), 5, Direction.WEST);
        assertMoved(assertAndMerge(ccs), 5, Direction.SOUTH_WEST);

        // Boar 3 Move
        assertMoved(assertAndMerge(ccs), 6, Direction.EAST);
        assertMoved(assertAndMerge(ccs), 6, Direction.EAST);
        assertMoved(assertAndMerge(ccs), 6, Direction.EAST);
        assertBoarAttack(assertAndMerge(ccs), 6, 4);
        assertMoved(assertAndMerge(ccs), 6, Direction.NORTH_WEST);
        assertMoved(assertAndMerge(ccs), 6, Direction.NORTH_WEST);
        assertMoved(assertAndMerge(ccs), 6, Direction.WEST);

        // Boar 4 Move
        assertMoved(assertAndMerge(ccs), 7, Direction.EAST);

        assertActNow(assertAndMerge(ccs), 0);
        passed();
    }
}
