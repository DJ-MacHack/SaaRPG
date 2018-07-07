package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;
import org.junit.Assert;

/**
 * Created by DJ MacHack on 16.09.2016.
 */
public class HSystemTest extends OurSystemTest {

    public HSystemTest()
    {super("HSystemTest");}


    @Override
    protected void executeTest() {
        ClientConnection<Event> aspieler = register(0, "A", CreatureType.KOBOLD, "Good", 10, 1);
        ClientConnection<Event> zspieler = register(1, "Z", CreatureType.YETI, "Evil", 16, 0);
        assertRegisterEvent(aspieler.nextEvent(), 1, "Z", CreatureType.YETI, "Evil", 16, 0);
        assertRegisterEvent(zspieler.nextEvent(), 0, "A", CreatureType.KOBOLD, "Good", 10, 1);

        assertRoundBegin(assertAndMerge(aspieler, zspieler), 1);
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendMove(Direction.SOUTH_EAST);
        assertMoved(assertAndMerge(aspieler, zspieler), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendWarCry("Trololololololololololololololololololololololololololololololol!");
        assertWarCry(assertAndMerge(aspieler, zspieler), 0, "Trololololololololololololololololololololololololololololololol!");
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(aspieler, zspieler), 0, Direction.EAST);
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(aspieler, zspieler), 0, Direction.EAST);
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendDoneActing();
        assertDoneActing(assertAndMerge(aspieler, zspieler), 0);

        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendMove(Direction.WEST);
        assertMoved(assertAndMerge(aspieler, zspieler), 1, Direction.WEST);
        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendMove(Direction.WEST);
        assertMoved(assertAndMerge(aspieler, zspieler), 1, Direction.WEST);
        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendWarCry("#IdoNOTcare!");
        assertWarCry(assertAndMerge(aspieler, zspieler), 1, "#IdoNOTcare!");
        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendDoneActing();                                          //-25
        assertDoneActing(assertAndMerge(aspieler, zspieler), 1);

        assertFieldEffect(assertAndMerge(aspieler, zspieler), 14, 0, 25);
        assertRoundEnd(assertAndMerge(aspieler, zspieler), 1, 0);

        assertRoundBegin(assertAndMerge(aspieler, zspieler), 2);
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendWarCry("Muhahahahahahahaha!");
        assertWarCry(assertAndMerge(aspieler, zspieler), 0, "Muhahahahahahahaha!");
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendMove(Direction.WEST);
        assertMoved(assertAndMerge(aspieler, zspieler), 0, Direction.WEST);
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendDoneActing();
        assertDoneActing(assertAndMerge(aspieler, zspieler), 0);


        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(aspieler, zspieler), 1, Direction.EAST);
        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendWarCry("Wuuuuuaaaahhhhh!");
        assertWarCry(assertAndMerge(aspieler, zspieler), 1, "Wuuuuuaaaahhhhh!");
        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendDoneActing();                                          //-50
        assertDoneActing(assertAndMerge(aspieler, zspieler), 1);

        assertFieldEffect(assertAndMerge(aspieler, zspieler), 15, 0, 25);
        assertRoundEnd(assertAndMerge(aspieler, zspieler), 2, 0);

        assertRoundBegin(assertAndMerge(aspieler, zspieler), 3);
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendWarCry("Ghigigigigi");
        assertWarCry(assertAndMerge(aspieler, zspieler), 0, "Ghigigigigi");
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendMove(Direction.NORTH_WEST);
        assertMoved(assertAndMerge(aspieler, zspieler), 0, Direction.NORTH_WEST);
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendDoneActing();
        assertDoneActing(assertAndMerge(aspieler, zspieler), 0);

        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendWarCry("Utsch");
        assertWarCry(assertAndMerge(aspieler, zspieler), 1, "Utsch");
        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendMove(Direction.WEST);
        assertMoved(assertAndMerge(aspieler, zspieler), 1, Direction.WEST);
        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendDoneActing();                                          //-75
        assertDoneActing(assertAndMerge(aspieler, zspieler), 1);

        assertFieldEffect(assertAndMerge(aspieler, zspieler), 14, 0, 25);
        assertRoundEnd(assertAndMerge(aspieler, zspieler), 3, 0);

        assertRoundBegin(assertAndMerge(aspieler,zspieler), 4);
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendMove(Direction.NORTH_EAST);
        assertMoved(assertAndMerge(aspieler, zspieler), 0, Direction.NORTH_EAST);
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendWarCry("Silence! I will kill you!");
        assertWarCry(assertAndMerge(aspieler, zspieler), 0, "Silence! I will kill you!");
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendDoneActing();
        assertDoneActing(assertAndMerge(aspieler, zspieler), 0);

        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(aspieler, zspieler), 1, Direction.EAST);
        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendWarCry("Try it u N006");
        assertWarCry(assertAndMerge(aspieler, zspieler), 1, "Try it u N006");
        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendDoneActing();                                          //-100
        assertDoneActing(assertAndMerge(aspieler, zspieler), 1);

        assertFieldEffect(assertAndMerge(aspieler, zspieler), 15, 0, 25);
        assertRoundEnd(assertAndMerge(aspieler, zspieler), 4, 0);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        assertRoundBegin(assertAndMerge(aspieler, zspieler), 5);
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(aspieler, zspieler), 0, Direction.EAST);
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendWarCry("We will c!");
        assertWarCry(assertAndMerge(aspieler, zspieler), 0, "We will c!");
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendDoneActing();
        assertDoneActing(assertAndMerge(aspieler, zspieler), 0);

        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendDoneActing();                                          //-125
        assertDoneActing(assertAndMerge(aspieler, zspieler), 1);

        assertFieldEffect(assertAndMerge(aspieler, zspieler), 15, 0, 25);
        assertRoundEnd(assertAndMerge(aspieler, zspieler), 5, 0);

        assertRoundBegin(assertAndMerge(aspieler, zspieler), 6);
        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendDoneActing();
        assertDoneActing(assertAndMerge(aspieler, zspieler), 1);

        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendDoneActing();                                          //-150
        assertDoneActing(assertAndMerge(aspieler, zspieler), 0);

        assertFieldEffect(assertAndMerge(aspieler, zspieler), 15, 0, 25);
        assertRoundEnd(assertAndMerge(aspieler, zspieler), 6, 0);

        assertRoundBegin(assertAndMerge(aspieler, zspieler), 7);
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendDoneActing();
        assertDoneActing(assertAndMerge(aspieler, zspieler), 0);

        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendDoneActing();                                          //-175
        assertDoneActing(assertAndMerge(aspieler, zspieler), 1);

        assertFieldEffect(assertAndMerge(aspieler, zspieler), 15, 0, 25);
        assertRoundEnd(assertAndMerge(aspieler, zspieler), 7, 0);

        assertRoundBegin(assertAndMerge(aspieler, zspieler), 8);
        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendWarCry("I will survive!");
        assertWarCry(assertAndMerge(aspieler, zspieler), 1, "I will survive!");
        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendMove(Direction.WEST);
        assertMoved(assertAndMerge(aspieler, zspieler), 1, Direction.WEST);
        assertActNow(assertAndMerge(aspieler, zspieler), 1);
        zspieler.sendDoneActing();
        assertDoneActing(assertAndMerge(aspieler, zspieler), 1);

        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(aspieler, zspieler), 0, Direction.EAST);
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendWarCry("hihi");
        assertWarCry(assertAndMerge(aspieler, zspieler), 0, "hihi");
        assertActNow(assertAndMerge(aspieler, zspieler), 0);
        aspieler.sendDoneActing();
        assertDoneActing(assertAndMerge(aspieler, zspieler), 0);

        assertFieldEffect(assertAndMerge(aspieler, zspieler), 14, 0, 25);
        assertDied(assertAndMerge(aspieler, zspieler), 1);
        assertRoundEnd(assertAndMerge(aspieler, zspieler), 8, 0);

        assertWinner(aspieler.nextEvent(), "Good");
    }

    @Override
    protected String getMapFile() {
        String map = " *t *# _   t^ **1  ^\n" +
                "^%  %*#t_ 0^$_ $   *\n" +
                " ^%    ^* $t  ^  #  ";
        return map;
    }

    @Override
    protected String getFightFile() {
        String fight = "Good, Evil\n"
                + "Good, A, Kobold\n"
                + "Evil, Z, Yeti";
        return fight;
    }
}
