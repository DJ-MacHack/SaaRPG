package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created on 20.09.16.
 * @author Henrik Paul Köhn
 */
public class AnotherAttackOnCurseFieldTest extends OurSystemTest {

    public AnotherAttackOnCurseFieldTest() {
        super("AnotherAttackOnCurseFieldTest");
    }

    @Override
    protected void executeTest() {
        // registrations
        ClientConnection<Event> sonic = register(0, "Sonic", CreatureType.KOBOLD, "Sega", 0, 0);
        ClientConnection<Event> mario = register(1, "Mario", CreatureType.KOBOLD, "Nintendo", 1, 0);
        assertRegisterEvent(sonic.nextEvent(), 1, "Mario", CreatureType.KOBOLD, "Nintendo", 1, 0);
        assertRegisterEvent(mario.nextEvent(), 0, "Sonic", CreatureType.KOBOLD, "Sega", 0, 0);

        // round begin since no fairys or boars
        assertRoundBegin(assertAndMerge(sonic, mario), 1);

        // act now sonic
        assertActNow(assertAndMerge(sonic, mario), 0);
        //sonic moves south east: 900 Energy (E) left
        sonic.sendMove(Direction.SOUTH_EAST);
        assertMoved(assertAndMerge(sonic, mario), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(sonic, mario), 0);
        // sonic stabs mario from a curse field: 300E left
        sonic.sendStab(Direction.NORTH_EAST);
        assertStabbed(assertAndMerge(sonic, mario), 0, Direction.NORTH_EAST);
        assertActNow(assertAndMerge(sonic, mario), 0);
        // tries the same again but gets kicked for not having enough energy (-300E)
        sonic.sendStab(Direction.NORTH_EAST);
        assertKicked(assertAndMerge(sonic, mario), 0);

        // now it is marios turn, who cries and then does nothing
        assertActNow(assertAndMerge(sonic, mario), 1);
        mario.sendWarCry("It's a me, Mario!");
        assertWarCry(assertAndMerge(sonic, mario), 1, "It's a me, Mario!");
        assertActNow(assertAndMerge(sonic, mario), 1);
        mario.sendDoneActing();
        assertDoneActing(assertAndMerge(sonic, mario), 1);

        // round ends with no boredom
        assertRoundEnd(assertAndMerge(sonic, mario), 1, 0);
        // and the winner is presented
        assertWinner(assertAndMerge(sonic, mario), "Nintendo");
        passed();

    }

    @Override
    protected String getMapFile() {
        return "01\n" +
               "$ ";
    }

    @Override
    protected String getFightFile() {
        return "Sega, Nintendo\n" +
                "Sega, Sonic, Kobold\n" +
                "Nintendo, Mario, Kobold";
    }
}
