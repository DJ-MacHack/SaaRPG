package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team14 on 16/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class SimpleFightTest extends OurSystemTest {

    public SimpleFightTest() {
        super("SimpleFightTest");
    }

    @Override
    protected void executeTest() {
        //registration phase
        ClientConnection<Event> harry = register(0, "Harry", CreatureType.WRAITH, "Gryffindor", 0, 0);

        ClientConnection<Event> statist23 = register(1, "Statist23", CreatureType.KOBOLD, "Hufflepuff", 1, 2);
        assertRegisterEvent(harry.nextEvent(), 1, "Statist23", CreatureType.KOBOLD, "Hufflepuff", 1, 2);
        assertRegisterEvent(statist23.nextEvent(), 0, "Harry", CreatureType.WRAITH, "Gryffindor", 0, 0);

        ClientConnection<Event> malfoy = register(2, "Malfoy", CreatureType.ROOK, "Slytherin", 3, 4);
        assertRegisterEvent(assertAndMerge(harry, statist23), 2, "Malfoy", CreatureType.ROOK, "Slytherin", 3, 4);
        assertRegisterEvent(malfoy.nextEvent(), 0, "Harry", CreatureType.WRAITH, "Gryffindor", 0, 0);
        assertRegisterEvent(malfoy.nextEvent(), 1, "Statist23", CreatureType.KOBOLD, "Hufflepuff", 1, 2);

        //registration completed -> RoundBegin Round 1
        assertRoundBegin(assertAndMerge(harry, malfoy, statist23), 1);

        //harry blinks to (5, 0) -> malfoy too far and poor statist23 should not be hurt
        assertActNow(assertAndMerge(harry, malfoy, statist23), 0);
        harry.sendBlink(5, 0);
        assertBlinked(assertAndMerge(harry, malfoy, statist23), 0, 5, 0);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 0);
        harry.sendDoneActing();
        assertDoneActing(assertAndMerge(harry, malfoy, statist23), 0);

        //statist23 can't bare the pressure and just jumps into the water at (1, 1) (Move(NW)) shouting "Go away ;("
        assertActNow(assertAndMerge(harry, malfoy, statist23), 1);
        statist23.sendMove(Direction.NORTH_WEST);
        assertMoved(assertAndMerge(harry, malfoy, statist23), 1, Direction.NORTH_WEST);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 1);
        statist23.sendWarCry("Go away ;(");
        assertWarCry(assertAndMerge(harry, malfoy, statist23), 1, "Go away ;(");
        assertActNow(assertAndMerge(harry, malfoy, statist23), 1);
        statist23.sendDoneActing();
        assertDoneActing(assertAndMerge(harry, malfoy, statist23), 1);

        //malfoy uses 2 cast on statist23 (20 dmg)
        assertActNow(assertAndMerge(harry, malfoy, statist23), 2);
        malfoy.sendCast(1, 1);
        assertCast(assertAndMerge(harry, malfoy, statist23), 2, 1, 1);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 2);
        malfoy.sendCast(1, 1);
        assertCast(assertAndMerge(harry, malfoy, statist23), 2, 1, 1);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 2);
        malfoy.sendDoneActing();
        assertDoneActing(assertAndMerge(harry, malfoy, statist23), 2);

        //fieldeffect on statist23 (20 dmg)
        assertFieldEffect(assertAndMerge(harry, malfoy, statist23), 1, 1, 20);

        //round 1 ends -> statist23: 60hp, harry: 100hp, malfoy: 100hp
        assertRoundEnd(assertAndMerge(harry, malfoy, statist23), 1, 0);

        //round 2 begins -> initOrder: statist23 -> malfoy -> harry
        assertRoundBegin(assertAndMerge(harry, malfoy, statist23), 2);

        //statist23 just shouts "water is sooooo refreshing :)"
        assertActNow(assertAndMerge(harry, malfoy, statist23), 1);
        statist23.sendWarCry("water is soooooo refreshing :)");
        assertWarCry(assertAndMerge(harry, malfoy, statist23), 1, "water is soooooo refreshing :)");
        assertActNow(assertAndMerge(harry, malfoy, statist23), 1);
        statist23.sendDoneActing();
        assertDoneActing(assertAndMerge(harry, malfoy, statist23), 1);

        //malfoy casts again 2 casts on statist23 (20 dmg)
        assertActNow(assertAndMerge(harry, malfoy, statist23), 2);
        malfoy.sendCast(1, 1);
        assertCast(assertAndMerge(harry, malfoy, statist23), 2, 1, 1);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 2);
        malfoy.sendCast(1, 1);
        assertCast(assertAndMerge(harry, malfoy, statist23), 2, 1, 1);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 2);
        malfoy.sendDoneActing();
        assertDoneActing(assertAndMerge(harry, malfoy, statist23), 2);

        //harry moves on lava at (4, 1) (Move(SW)) and swaps with statist23 (6 dmg) shouting "I want to bath as well! <.<" and moves(E) out to (2,1)
        assertActNow(assertAndMerge(harry, malfoy, statist23), 0);
        harry.sendMove(Direction.SOUTH_WEST);
        assertMoved(assertAndMerge(harry, malfoy, statist23), 0, Direction.SOUTH_WEST);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 0);
        harry.sendSwap(1, 1);
        assertSwapped(assertAndMerge(harry, malfoy, statist23), 0, 1, 1);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 0);
        harry.sendWarCry("I want to bath as well! <.<");
        assertWarCry(assertAndMerge(harry, malfoy, statist23), 0, "I want to bath as well! <.<");
        assertActNow(assertAndMerge(harry, malfoy, statist23), 0);
        harry.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(harry, malfoy, statist23), 0, Direction.EAST);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 0);
        harry.sendDoneActing();
        assertDoneActing(assertAndMerge(harry, malfoy, statist23), 0);

        //fieldeffect on statist23 (25 dmg)
        assertFieldEffect(assertAndMerge(harry, malfoy, statist23), 4, 1, 25);

        //roundend 2 -> statist23: 9hp, harry: 100hp, malfoy: 100hp
        assertRoundEnd(assertAndMerge(harry, malfoy, statist23), 2, 0);

        //round 3 begins -> initOrder: statist23 -> malfoy -> harry
        assertRoundBegin(assertAndMerge(harry, malfoy, statist23), 3);

        //statist23 shouts "Enough of this %#@*!?/$" moves to (2,4) (Move(SW), Move(SE), Move(SW)) and attacks Malfoy with slash(E) and stab(E) (19 dmg)
        assertActNow(assertAndMerge(harry, malfoy, statist23), 1);
        statist23.sendWarCry("Enough of this %#@*!?/$");
        assertWarCry(assertAndMerge(harry, malfoy, statist23), 1, "Enough of this %#@*!?/$");
        assertActNow(assertAndMerge(harry, malfoy, statist23), 1);
        statist23.sendMove(Direction.SOUTH_WEST);
        assertMoved(assertAndMerge(harry, malfoy, statist23), 1, Direction.SOUTH_WEST);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 1);
        statist23.sendMove(Direction.SOUTH_EAST);
        assertMoved(assertAndMerge(harry, malfoy, statist23), 1, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 1);
        statist23.sendMove(Direction.SOUTH_WEST);
        assertMoved(assertAndMerge(harry, malfoy, statist23), 1, Direction.SOUTH_WEST);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 1);
        statist23.sendSlash(Direction.EAST);
        assertSlashed(assertAndMerge(harry, malfoy, statist23), 1, Direction.EAST);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 1);
        statist23.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(harry, malfoy, statist23), 1, Direction.EAST);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 1);
        statist23.sendDoneActing();
        assertDoneActing(assertAndMerge(harry, malfoy, statist23), 1);

        //malfoy ends statist23 off with cast (10 dmg -> died) and uses cast on harry
        assertActNow(assertAndMerge(harry, malfoy, statist23), 2);
        malfoy.sendCast(2, 4);
        assertCast(assertAndMerge(harry, malfoy, statist23), 2, 2, 4);
        assertDied(assertAndMerge(harry, malfoy, statist23), 1);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 2);
        malfoy.sendCast(2, 1);
        assertCast(assertAndMerge(harry, malfoy, statist23), 2, 2, 1);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 2);
        malfoy.sendDoneActing();
        assertDoneActing(assertAndMerge(harry, malfoy, statist23), 2);

        //harry moves to (4,1) (E, E) and swaps with malfoy (6 dmg)
        assertActNow(assertAndMerge(harry, malfoy, statist23), 0);
        harry.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(harry, malfoy, statist23), 0, Direction.EAST);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 0);
        harry.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(harry, malfoy, statist23), 0, Direction.EAST);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 0);
        harry.sendSwap(3, 4);
        assertSwapped(assertAndMerge(harry, malfoy, statist23), 0, 3, 4);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 0);
        harry.sendDoneActing();
        assertDoneActing(assertAndMerge(harry, malfoy, statist23), 0);

        //fieldeffect on malfoy (25 dmg)
        assertFieldEffect(assertAndMerge(harry, malfoy, statist23), 4, 1, 25);

        //roundend 3 -> statist23: /, harry: 90hp, malfoy: 50hp
        assertRoundEnd(assertAndMerge(harry, malfoy, statist23), 3, 0);

        //round 4 begins -> initOrder: malfoy -> harry
        assertRoundBegin(assertAndMerge(harry, malfoy, statist23), 4);

        //malfoy casts at (-1,4) -> kicked
        assertActNow(assertAndMerge(harry, malfoy, statist23), 2);
        malfoy.sendCast(-1, 4);
        assertKicked(assertAndMerge(harry, malfoy, statist23), 2);

        //harry blinks to (-2,4)
        assertActNow(assertAndMerge(harry, malfoy, statist23), 0);
        harry.sendBlink(-2, 4);
        assertBlinked(assertAndMerge(harry, malfoy, statist23), 0, -2, 4);
        assertActNow(assertAndMerge(harry, malfoy, statist23), 0);
        harry.sendDoneActing();
        assertDoneActing(assertAndMerge(harry, malfoy, statist23), 0);

        //roundend 4 -> statist23: /, harry: 100hp, malfoy: /
        assertRoundEnd(assertAndMerge(harry, malfoy, statist23), 4, 0);

        //winner(Gryffindor)
        assertWinner(assertAndMerge(harry, malfoy, statist23), "Gryffindor");
    }

    @Override
    protected String getMapFile() {
        return "0     \n"
                + "~~^^**\n"
                + "~~2^**\n"
                + "~~^^**\n"
                + "     1";
    }

    @Override
    protected String getFightFile() {
        return "Gryffindor, Slytherin, Hufflepuff\n"
                + "Gryffindor, Harry, Wraith\n"
                + "Slytherin, Malfoy, Rook\n"
                + "Hufflepuff, Statist23, Kobold";
    }
}
