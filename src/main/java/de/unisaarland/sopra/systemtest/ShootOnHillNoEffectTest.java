package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team14 on 22/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class ShootOnHillNoEffectTest extends OurSystemTest {

    public ShootOnHillNoEffectTest() {
        super("ShootOnHillNoEffectTest");
    }

    @Override
    protected void executeTest() {
        // register phase
        ClientConnection<Event> legolas = register(0, "Legolas", CreatureType.ELF, "Bruchtal", 0, 0);
        ClientConnection<Event> nazgul = register(1, "Nazgul", CreatureType.WRAITH, "Mordor", 6, 0);

        assertRegisterEvent(legolas.nextEvent(), 1, "Nazgul", CreatureType.WRAITH, "Mordor", 6, 0);
        assertRegisterEvent(nazgul.nextEvent(), 0, "Legolas", CreatureType.ELF, "Bruchtal", 0, 0);

        // round 1 begins
        assertRoundBegin(assertAndMerge(legolas, nazgul), 1);

        // legolas' turn
        assertActNow(assertAndMerge(legolas, nazgul), 0);
        legolas.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(legolas, nazgul), 0, Direction.EAST);
        assertActNow(assertAndMerge(legolas, nazgul), 0);
        legolas.sendShoot(Direction.EAST);
        assertShot(assertAndMerge(legolas, nazgul), 0, Direction.EAST);
        assertActNow(assertAndMerge(legolas, nazgul), 0);
        legolas.sendDoneActing();
        assertDoneActing(assertAndMerge(legolas, nazgul), 0);

        // nazgul's turn
        assertActNow(assertAndMerge(legolas, nazgul), 1);
        nazgul.sendDoneActing();
        assertDoneActing(assertAndMerge(legolas, nazgul), 1);

        // round 1 ends
        assertRoundEnd(assertAndMerge(legolas, nazgul), 1, 0);

        // round 2 begins
        assertRoundBegin(assertAndMerge(legolas, nazgul), 2);

        // nazgul's turn
        assertActNow(assertAndMerge(legolas, nazgul), 1);
        nazgul.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(legolas, nazgul), 1, Direction.EAST);
        assertActNow(assertAndMerge(legolas, nazgul), 1);
        nazgul.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(legolas, nazgul), 1, Direction.EAST);
        assertActNow(assertAndMerge(legolas, nazgul), 1);
        nazgul.sendDoneActing();
        assertDoneActing(assertAndMerge(legolas, nazgul), 1);

        // legolas' turn
        assertActNow(assertAndMerge(legolas, nazgul), 0);
        legolas.sendShoot(Direction.EAST);
        assertKicked(assertAndMerge(legolas, nazgul), 0);

        // round 2 ends
        assertRoundEnd(assertAndMerge(legolas, nazgul), 2, 0);

        // winner: nazgul
        assertWinner(assertAndMerge(legolas, nazgul), "Mordor");
    }

    @Override
    protected String getMapFile() {
        return "0^    1  ";
    }

    @Override
    protected String getFightFile() {
        return "Bruchtal, Mordor\n"
             + "Bruchtal, Legolas, Elf\n"
             + "Mordor, Nazgul, Wraith";
    }
}
