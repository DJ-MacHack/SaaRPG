package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.publictest.TestEventFactory;

/**
 * Created by Wiebk on 19.09.2016.
 */
public class SingeBurnTestSimple extends OurSystemTest {

    public SingeBurnTestSimple() {
        super("SingeBurnTestSimple");
    }

    @Override
    public void executeTest() {
        //registration
        ClientConnection<Event> trump = register(0, "Donald", CreatureType.KOBOLD, "Trump", 1, 1);
        ClientConnection<Event> hilary = register(1, "Hilary", CreatureType.IFRIT, "Clinton", 2, 1);
        Event registerTrump = trump.nextEvent();
        assertRegisterEvent(registerTrump, 1, "Hilary", CreatureType.IFRIT, "Clinton", 2, 1);
        assertRegisterEvent(hilary.nextEvent(), 0, "Donald", CreatureType.KOBOLD, "Trump", 1, 1);

        //no boars or fairies on the map, RoundBegin event
        assertRoundBegin(assertAndMerge(trump, hilary), 1);

        //trump's turn: stab at Hilary, then slash at Hilary
        assertActNow(assertAndMerge(trump, hilary), 0);
        trump.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(trump, hilary), 0, Direction.EAST);
        assertActNow(assertAndMerge(trump, hilary), 0);
        trump.sendSlash(Direction.EAST);
        assertSlashed(assertAndMerge(trump, hilary), 0, Direction.EAST);
        assertActNow(assertAndMerge(trump, hilary), 0);
        trump.sendDoneActing();
        assertDoneActing(assertAndMerge(hilary, trump), 0);

        //hilary's turn: singe at trump
        assertActNow(assertAndMerge(trump, hilary), 1);
        hilary.sendSinge(Direction.WEST);
        assertSinged(assertAndMerge(hilary, trump), 1, Direction.WEST);
        assertActNow(assertAndMerge(trump, hilary), 1);
        hilary.sendDoneActing();
        assertDoneActing(assertAndMerge(hilary, trump), 1);

        assertRoundEnd(assertAndMerge(trump, hilary), 1, 0);

        assertRoundBegin(assertAndMerge(trump, hilary), 2);

        //hilary's turn, burn at trump
        assertActNow(assertAndMerge(trump, hilary), 1);
        hilary.sendBurn();
        assertBurned(assertAndMerge(trump, hilary), 1);
        assertActNow(assertAndMerge(trump, hilary), 1);
        hilary.sendDoneActing();
        assertDoneActing(assertAndMerge(hilary, trump), 1);

        //trump's turn: sends rude WarCry, then stab towards North_East, gets kicked
        assertActNow(assertAndMerge(trump, hilary), 0);
        trump.sendWarCry("You, all, everybody! I hate you! Vote for me!");
        assertWarCry(assertAndMerge(trump, hilary), 0, "You, all, everybody! I hate you! Vote for me!");
        assertActNow(assertAndMerge(trump, hilary), 0);
        trump.sendStab(Direction.NORTH_EAST);
        assertKicked(assertAndMerge(trump, hilary), 0);

        //RoundEnd, hilary wins
        assertRoundEnd(assertAndMerge(trump, hilary), 2, 0);
        assertWinner(assertAndMerge(trump, hilary), "Clinton");
    }

    @Override
    public String getFightFile() {
        return "Trump, Clinton\n"
                + "Clinton, Hilary, Ifrit\n"
                + "Trump, Donald, Kobold";
    }

    @Override
    public String getMapFile() {
        return "____\n"
                + "_01_\n"
                + "____\n"
                + "____";
    }
}
