package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team 14 on 19.09.2016.
 */
public class SimpleAttackTestOne extends OurSystemTest {

    private static final String MAPFILE = "0^^\n"
            + "  ^\n"
            + "1 ^";

    private static final String FIGHTFILE = "Stark, Bolton\n"
            + "Stark, John, Elf\n"
            + "Bolton, Ramsay, Kobold";

    public SimpleAttackTestOne() {
        super("SimpleAttackTestOne");
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> c1 = register(0, "John", CreatureType.ELF, "Stark", 0, 0);
        ClientConnection<Event> c2 = register(1, "Ramsay", CreatureType.KOBOLD, "Bolton", -1, 2);

        assertRegisterEvent(c1.nextEvent(), 1, "Ramsay", CreatureType.KOBOLD, "Bolton", -1, 2);
        assertRegisterEvent(c2.nextEvent(), 0, "John", CreatureType.ELF, "Stark", 0, 0);

        assertRoundBegin(assertAndMerge(c1, c2), 1);    // Ramsay: 100 hp | John: 100 hp

        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendDoneActing();
        assertDoneActing(assertAndMerge(c1, c2), 0);

        assertActNow(assertAndMerge(c1, c2), 1);
        c2.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(c1, c2), 1, Direction.EAST);
        assertActNow(assertAndMerge(c1, c2), 1);
        c2.sendDoneActing();
        assertDoneActing(assertAndMerge(c1, c2), 1);

        assertRoundEnd(assertAndMerge(c1, c2), 1, 1);
        assertRoundBegin(assertAndMerge(c1, c2), 2);    // Ramsay: 100 hp | John: 100 hp

        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendShoot(Direction.SOUTH_EAST);
        assertShot(assertAndMerge(c1, c2), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendDoneActing();
        assertDoneActing(assertAndMerge(c1, c2), 0);

        assertActNow(assertAndMerge(c1, c2), 1);
        c2.sendMove(Direction.NORTH_WEST);
        assertMoved(assertAndMerge(c1, c2), 1, Direction.NORTH_WEST);
        assertActNow(assertAndMerge(c1, c2), 1);
        c2.sendStab(Direction.NORTH_WEST);
        assertStabbed(assertAndMerge(c1, c2), 1, Direction.NORTH_WEST);
        assertActNow(assertAndMerge(c1, c2), 1);
        c2.sendDoneActing();
        assertDoneActing(assertAndMerge(c1, c2), 1);

        assertRoundEnd(assertAndMerge(c1, c2), 2, 0);
        assertRoundBegin(assertAndMerge(c1, c2), 3);    // Ramsay: 89 hp | John: 93 hp

        assertActNow(assertAndMerge(c1, c2), 1);
        c2.sendDoneActing();
        assertDoneActing(assertAndMerge(c1, c2), 1);

        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendStab(Direction.SOUTH_EAST);
        assertStabbed(assertAndMerge(c1, c2), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendStab(Direction.SOUTH_EAST);
        assertStabbed(assertAndMerge(c1, c2), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendStab(Direction.SOUTH_EAST);
        assertStabbed(assertAndMerge(c1, c2), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendStab(Direction.SOUTH_EAST);
        assertStabbed(assertAndMerge(c1, c2), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendDoneActing();
        assertDoneActing(assertAndMerge(c1, c2), 0);

        assertRoundEnd(assertAndMerge(c1, c2), 3, 0);
        assertRoundBegin(assertAndMerge(c1, c2), 4);    // Ramsay: 61 hp | John: 93 hp

        assertActNow(assertAndMerge(c1, c2), 1);
        c2.sendDoneActing();
        assertDoneActing(assertAndMerge(c1, c2), 1);

        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendStab(Direction.SOUTH_EAST);
        assertStabbed(assertAndMerge(c1, c2), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendStab(Direction.SOUTH_EAST);
        assertStabbed(assertAndMerge(c1, c2), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendStab(Direction.SOUTH_EAST);
        assertStabbed(assertAndMerge(c1, c2), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendStab(Direction.SOUTH_EAST);
        assertStabbed(assertAndMerge(c1, c2), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendDoneActing();
        assertDoneActing(assertAndMerge(c1, c2), 0);

        assertRoundEnd(assertAndMerge(c1, c2), 4, 0);
        assertRoundBegin(assertAndMerge(c1, c2), 5);    // Ramsay: 33 hp | John: 93 hp

        assertActNow(assertAndMerge(c1, c2), 1);
        c2.sendDoneActing();
        assertDoneActing(assertAndMerge(c1, c2), 1);

        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendStab(Direction.SOUTH_EAST);
        assertStabbed(assertAndMerge(c1, c2), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendStab(Direction.SOUTH_EAST);
        assertStabbed(assertAndMerge(c1, c2), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendStab(Direction.SOUTH_EAST);
        assertStabbed(assertAndMerge(c1, c2), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendStab(Direction.SOUTH_EAST);
        assertStabbed(assertAndMerge(c1, c2), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendDoneActing();
        assertDoneActing(assertAndMerge(c1, c2), 0);

        assertRoundEnd(assertAndMerge(c1, c2), 5, 0);
        assertRoundBegin(assertAndMerge(c1, c2), 6);    // Ramsay: 5 hp | John: 93 hp

        assertActNow(assertAndMerge(c1, c2), 1);
        c2.sendDoneActing();
        assertDoneActing(assertAndMerge(c1, c2), 1);

        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendStab(Direction.SOUTH_EAST);
        assertStabbed(assertAndMerge(c1, c2), 0, Direction.SOUTH_EAST);
        assertDied(assertAndMerge(c1, c2), 1);
        assertActNow(assertAndMerge(c1, c2), 0);
        c1.sendDoneActing();
        assertDoneActing(assertAndMerge(c1, c2), 0);

        assertRoundEnd(assertAndMerge(c1, c2), 6, 0);
        assertWinner(assertAndMerge(c1, c2), "Stark");
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
