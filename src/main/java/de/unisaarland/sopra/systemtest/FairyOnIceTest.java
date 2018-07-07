package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team14 on 22/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class FairyOnIceTest extends OurSystemTest {

    public FairyOnIceTest() {
        super("FairyOnIcetest");
    }

    @Override
    protected void executeTest() {
        // register phase
        ClientConnection<Event> boateng = register(0, "Boateng", CreatureType.ROOK, "Nationalmannschaft", 0, 0);
        ClientConnection<Event> gauland = register(1, "Gauland", CreatureType.KOBOLD, "Afd", 0, 1);

        assertRegisterEvent(boateng.nextEvent(), 1, "Gauland", CreatureType.KOBOLD, "Afd", 0, 1);
        assertRegisterEvent(gauland.nextEvent(), 0, "Boateng", CreatureType.ROOK, "Nationalmannschaft", 0, 0);

        // fairy spawns
        assertFairySpawned(assertAndMerge(boateng, gauland), 2, 1, 1);

        // round 1 starts
        assertRoundBegin(assertAndMerge(boateng, gauland), 1);

        // fairy movement
        assertMoved(assertAndMerge(boateng, gauland), 2, Direction.EAST);
        assertMoved(assertAndMerge(boateng, gauland), 2, Direction.EAST);
        assertMoved(assertAndMerge(boateng, gauland), 2, Direction.EAST);

        // boateng does nothing
        assertActNow(assertAndMerge(boateng, gauland), 0);
        boateng.sendDoneActing();
        assertDoneActing(assertAndMerge(boateng, gauland), 0);

        // gauland does nothing
        assertActNow(assertAndMerge(boateng, gauland), 1);
        gauland.sendDoneActing();
        assertDoneActing(assertAndMerge(boateng, gauland), 1);

        // round 1 ends
        assertRoundEnd(assertAndMerge(boateng, gauland), 1, 1);

        // round 2 starts
        assertRoundBegin(assertAndMerge(boateng, gauland), 2);

        // fairy movement
        assertMoved(assertAndMerge(boateng, gauland), 2, Direction.NORTH_EAST);
        assertMoved(assertAndMerge(boateng, gauland), 2, Direction.WEST);
        assertMoved(assertAndMerge(boateng, gauland), 2, Direction.WEST);
        assertMoved(assertAndMerge(boateng, gauland), 2, Direction.WEST);

        passed();
    }

    @Override
    protected String getMapFile() {
        return "0         \n"
             + "1+___#    ";
    }

    @Override
    protected String getFightFile() {
        return "Nationalmannschaft, Afd\n"
                + "Nationalmannschaft, Boateng, ROOK\n"
                + "Afd, Gauland, Kobold";
    }
}
