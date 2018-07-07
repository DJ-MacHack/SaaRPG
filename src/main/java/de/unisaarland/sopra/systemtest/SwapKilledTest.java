package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Karla on 23.09.2016.
 */
public class SwapKilledTest extends OurSystemTest {

    private static final String MAPFILE =
            "0    \n"
                    + "1~~~x\n"
                    + "~~~~x\n"
                    + "~   x";

    private static final String FIGHTFILE =
            "Black, White \n"
                    + "Black, Cat, Wraith\n"
                    + "White, Pigeon, Naga";

    public SwapKilledTest() {
        super("SwapKilledTest");
    }

    @Override
    protected void executeTest() {

        ClientConnection<Event> pigeon = register(0, "Pigeon", CreatureType.NAGA, "White", 0, 1);
        ClientConnection<Event> cat = register(1, "Cat", CreatureType.WRAITH, "Black", 0, 0);

        assertRegisterEvent(cat.nextEvent(), 0, "Pigeon", CreatureType.NAGA, "White", 0, 1);
        assertRegisterEvent(pigeon.nextEvent(), 1, "Cat", CreatureType.WRAITH, "Black", 0, 0);

        for(int j = 1; j < 5; j++) {
            assertRoundBegin(assertAndMerge(cat, pigeon), j);
            //Pigeon's turn
            //Pigeon passes
            assertActNow(assertAndMerge(cat, pigeon), 0);
            pigeon.sendDoneActing();
            assertDoneActing(assertAndMerge(cat, pigeon), 0);
            //Cat's turn
            //Cat attacks E:0 HP:100
            for (int i = 0; i < 2; i++) {
                assertActNow(assertAndMerge(cat, pigeon), 1);
                cat.sendSwap(0, 1);
                assertSwapped(assertAndMerge(cat, pigeon), 1, 0, 1);
                assertActNow(assertAndMerge(cat, pigeon), 1);
                cat.sendSwap(0, 0);
                assertSwapped(assertAndMerge(cat, pigeon), 1, 0, 0);
            }
            //Cat passes
            assertActNow(assertAndMerge(cat, pigeon), 1);
            cat.sendDoneActing();
            assertDoneActing(assertAndMerge(cat, pigeon), 1);
            //Round ends
            assertRoundEnd(assertAndMerge(cat, pigeon),j,0);

        }

        assertRoundBegin(assertAndMerge(cat, pigeon), 5);
        //Pigeon's turn
        //Pigeon passes
        //Pigeon HP: 4
        assertActNow(assertAndMerge(cat, pigeon), 0);
        pigeon.sendDoneActing();
        assertDoneActing(assertAndMerge(cat, pigeon), 0);
        //Cat attacks one last time
        assertActNow(assertAndMerge(cat, pigeon), 1);
        cat.sendSwap(0, 1);
        assertSwapped(assertAndMerge(cat, pigeon),1, 0, 1);
        assertDied(assertAndMerge(cat,pigeon),0);
        //Cat passes
        assertActNow(assertAndMerge(cat, pigeon), 1);
        cat.sendDoneActing();
        assertDoneActing(assertAndMerge(cat, pigeon), 1);

        assertRoundEnd(assertAndMerge(cat,pigeon),5,0);
        assertWinner(assertAndMerge(cat,pigeon),"Black");
        passed();
    }

    @Override
    protected String getMapFile() {
        return MAPFILE;
    }

    @Override
    protected String getFightFile() {
        return FIGHTFILE;
    }
}
