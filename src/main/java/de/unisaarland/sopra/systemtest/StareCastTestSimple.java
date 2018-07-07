package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;
import org.junit.Assert;

/**
 * Created by Wiebk on 16.09.2016.
 */
public class StareCastTestSimple extends OurSystemTest {

    public StareCastTestSimple() {
        super("StareCastTestSimple");
    }

    @Override
    public void executeTest() {

        //throw new UnsupportedOperationException();

        //Registrierungsphase
        ClientConnection<Event> harry = register(0, "Harry", CreatureType.ROOK, "Gryffindor", 1, 1);
        ClientConnection<Event> malfoy = register(1, "Malfoy", CreatureType.ROOK, "Slytherin", 2, 1);
        assertRegisterEvent(harry.nextEvent(), 1, "Malfoy", CreatureType.ROOK, "Slytherin", 2, 1);
        assertRegisterEvent(malfoy.nextEvent(), 0, "Harry", CreatureType.ROOK, "Gryffindor", 1, 1);

        //keine Feen/Wildschweine --> RoundBegin
        assertRoundBegin(assertAndMerge(harry, malfoy), 1);

        //Monster agieren: Harry ist am Zug, Stare auf Malfoy
        assertActNow(assertAndMerge(harry, malfoy), 0);
        harry.sendStare(Direction.EAST);
        assertStared(assertAndMerge(harry, malfoy), 0, Direction.EAST);
        assertActNow(assertAndMerge(harry, malfoy), 0);
        harry.sendDoneActing();
        assertDoneActing(assertAndMerge(harry, malfoy), 0);

        //Malfoy ist am Zug, Cast auf Harry
        assertActNow(assertAndMerge(harry, malfoy), 1);
        malfoy.sendCast(1, 1);
        assertCast(assertAndMerge(harry, malfoy), 1, 1, 1);
        assertActNow(assertAndMerge(harry, malfoy), 1);
        malfoy.sendDoneActing();
        assertDoneActing(assertAndMerge(harry, malfoy), 1);

        assertRoundEnd(assertAndMerge(harry, malfoy), 1, 0);
        assertRoundBegin(assertAndMerge(harry, malfoy), 2);

        //Harry ist am Zug, er schickt Cast auf ein Grasfeld, wird gekickt
        assertActNow(assertAndMerge(harry, malfoy), 0);
        harry.sendCast(2, 0);
        assertKicked(assertAndMerge(harry, malfoy), 0);

        //Malfoy ist am Zug, er schickt Stare auf ein Grasfeld und wird ebenfalls gekickt
        assertActNow(assertAndMerge(harry, malfoy), 1);
        malfoy.sendStare(Direction.NORTH_WEST);
        assertKicked(assertAndMerge(harry, malfoy), 1);

        assertRoundEnd(assertAndMerge(harry, malfoy), 2, 0);

        //Am Ende der Runde gibt es keine Spieler mehr und somit keinen Gewinner
        assertWinner(assertAndMerge(harry, malfoy), "");
    }

    @Override
    public String getFightFile() {
        return "Gryffindor, Slytherin\n"
                + "Gryffindor, Harry, Rook\n"
                + "Slytherin, Malfoy, Rook";
    }

    @Override
    public String getMapFile() {
        return "    \n"
                + " 01 \n"
                + "    ";
    }
}
