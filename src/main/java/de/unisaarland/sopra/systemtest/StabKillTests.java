package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Team 14 on 22.09.2016.
 */
public class StabKillTests extends OurSystemTest {

    private CreatureType creatureType;

    private StabKillTests(CreatureType creatureType){
        super("StabKillTests." + creatureType);
        this.creatureType = creatureType;
    }

    public static Collection<OurSystemTest> getTests() {
        ArrayList<OurSystemTest> sysTests = new ArrayList<OurSystemTest>();

        sysTests.add(new StabKillTests(CreatureType.KOBOLD));
        sysTests.add(new StabKillTests(CreatureType.ELF));
        sysTests.add(new StabKillTests(CreatureType.WRAITH));

        return sysTests;
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> p1 = register(0, "Red", creatureType, "A", 0, 0);
        ClientConnection<Event> p2 = register(1, "Blue", CreatureType.ELF, "B", 1, 0);

        assertRegisterEvent(p1.nextEvent(), 1, "Blue", CreatureType.ELF, "B", 1, 0);
        assertRegisterEvent(p2.nextEvent(), 0, "Red", creatureType, "A", 0, 0);

        assertRoundBegin(assertAndMerge(p1, p2), 1);

        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(p1, p2), 0, Direction.EAST);
        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(p1, p2), 0, Direction.EAST);
        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(p1, p2), 0, Direction.EAST);
        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(p1, p2), 0, Direction.EAST);

        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendDoneActing();
        assertDoneActing(assertAndMerge(p1, p2), 0);

        assertActNow(assertAndMerge(p1, p2), 1);
        p2.sendDoneActing();
        assertDoneActing(assertAndMerge(p1, p2), 1);

        assertRoundEnd(assertAndMerge(p1, p2), 1, 0);
        assertRoundBegin(assertAndMerge(p1, p2), 2);

        assertActNow(assertAndMerge(p1, p2), 1);
        p2.sendDoneActing();
        assertDoneActing(assertAndMerge(p1, p2), 1);


        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(p1, p2), 0, Direction.EAST);
        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(p1, p2), 0, Direction.EAST);
        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(p1, p2), 0, Direction.EAST);
        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(p1, p2), 0, Direction.EAST);

        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendDoneActing();
        assertDoneActing(assertAndMerge(p1, p2), 0);

        assertRoundEnd(assertAndMerge(p1, p2), 2, 0);
        assertRoundBegin(assertAndMerge(p1, p2), 3);

        assertActNow(assertAndMerge(p1, p2), 1);
        p2.sendDoneActing();
        assertDoneActing(assertAndMerge(p1, p2), 1);


        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(p1, p2), 0, Direction.EAST);
        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(p1, p2), 0, Direction.EAST);
        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(p1, p2), 0, Direction.EAST);
        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(p1, p2), 0, Direction.EAST);

        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendDoneActing();
        assertDoneActing(assertAndMerge(p1, p2), 0);

        assertRoundEnd(assertAndMerge(p1, p2), 3, 0);
        assertRoundBegin(assertAndMerge(p1, p2), 4);

        assertActNow(assertAndMerge(p1, p2), 1);
        p2.sendDoneActing();
        assertDoneActing(assertAndMerge(p1, p2), 1);


        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(p1, p2), 0, Direction.EAST);
        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(p1, p2), 0, Direction.EAST);
        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(p1, p2), 0, Direction.EAST);
        assertDied(assertAndMerge(p1, p2), 1);

        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendDoneActing();
        assertDoneActing(assertAndMerge(p1, p2), 0);

        assertRoundEnd(assertAndMerge(p1, p2), 4, 0);
        assertWinner(assertAndMerge(p1, p2), "A");
    }

    @Override
    protected String getMapFile() {
        return "01";
    }

    @Override
    protected String getFightFile() {
        return "A, B\n"+
                String.format("A, Red, %s %n", creatureType)+
                "B, Blue, Elf";
    }
}
