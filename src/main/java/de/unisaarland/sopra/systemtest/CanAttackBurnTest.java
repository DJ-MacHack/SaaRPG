package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team 14 on 23.09.2016.
 */
public class CanAttackBurnTest extends OurSystemTest {

    public CanAttackBurnTest(){
        super("CanAttackBurnTest");
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> p1 = register(0, "X", CreatureType.IFRIT, "A", 0, 0);
        ClientConnection<Event> p2 = register(1, "Y", CreatureType.ELF, "B", 1, 0);

        assertRegisterEvent(p1.nextEvent(), 1, "Y", CreatureType.ELF, "B", 1, 0);
        assertRegisterEvent(p2.nextEvent(), 0, "X", CreatureType.IFRIT, "A", 0, 0);

        assertRoundBegin(assertAndMerge(p1, p2), 1);


        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendBurn();
        assertBurned(assertAndMerge(p1, p2), 0);
        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendBurn();
        assertBurned(assertAndMerge(p1, p2), 0);
        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendBurn();
        assertKicked(assertAndMerge(p1, p2), 0);

        assertActNow(assertAndMerge(p1, p2), 1);
        p1.sendBurn();
        p2.sendDoneActing();
        assertDoneActing(assertAndMerge(p1, p2), 1);

        assertRoundEnd(assertAndMerge(p1, p2), 1, 0);
    }

    @Override
    protected String getMapFile() {
        return "01";
    }

    @Override
    protected String getFightFile() {
        return "A, B\n"
                +"A, X, Ifrit\n"
                +"B, Y, Elf";
    }
}
