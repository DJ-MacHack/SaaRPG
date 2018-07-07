package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.messages.FieldEffect;
import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Wiebk on 19.09.2016.
 */
public class LoosingLifeOnWaterTest extends OurSystemTest{

    public LoosingLifeOnWaterTest(){
        super("LoosingLifeOnWaterTest");
    }

    @Override
    public void executeTest() {
        //register
        ClientConnection<Event> neuer = register(0, "Neuer", CreatureType.ROOK, "Deutschland", 0, 1);
        ClientConnection<Event> zidane = register(1, "Zidane", CreatureType.IFRIT, "Frankreich", 1, 1);
        Event neuerreg = zidane.nextEvent();
        assertRegisterEvent(neuerreg, 0, "Neuer", CreatureType.ROOK, "Deutschland", 0, 1);
        assertRegisterEvent(neuer.nextEvent(), 1, "Zidane", CreatureType.IFRIT, "Frankreich", 1, 1);

        assertRoundBegin(assertAndMerge(neuer, zidane), 1);

        //Neuer: move North_East onto water
        assertActNow(assertAndMerge(neuer, zidane), 0);
        neuer.sendMove(Direction.NORTH_EAST);
        assertMoved(assertAndMerge(neuer, zidane), 0, Direction.NORTH_EAST);
        assertActNow(assertAndMerge(neuer, zidane), 0);
        neuer.sendDoneActing();
        assertDoneActing(assertAndMerge(neuer, zidane), 0);

        //Zidane: move North_East onto water
        assertActNow(assertAndMerge(neuer, zidane), 1);
        zidane.sendMove(Direction.NORTH_EAST);
        assertMoved(assertAndMerge(neuer, zidane), 1, Direction.NORTH_EAST);
        assertActNow(assertAndMerge(neuer, zidane), 1);
        zidane.sendDoneActing();
        assertDoneActing(assertAndMerge(neuer, zidane), 1);

        Event e = assertAndMerge(neuer, zidane);
        if (!(e instanceof FieldEffect)) {
            throw new IllegalArgumentException("53: FieldEffect was expected but Event was of other kind.");
        }
        FieldEffect fe = (FieldEffect) e;
        if (fe.getValue() != 20) {
            throw new IllegalArgumentException("57: FieldEffect with value of 20 was expected but other value was found.");
        }
        if (fe.getyCoord() != 0) {
            throw new IllegalArgumentException("60: FieldEffect with y-Coord of 0 was expected but other value was found.");
        }
        if (fe.getxCoord() == 1) {
            assertFieldEffect(assertAndMerge(neuer, zidane), 2, 0, 20);
        } else {
            if (fe.getxCoord() == 2) {
                assertFieldEffect(assertAndMerge(neuer, zidane), 1, 0, 20);
            } else {
                throw new IllegalArgumentException("68: FieldEffect at invalid coord (x-Coord was neither 1 nor 2).");
            }
        }
        assertRoundEnd(assertAndMerge(neuer, zidane), 1, 0);

        //No Movement for 4 rounds, both loose 20HP each Round and die
        //Round 1 of 4
        assertRoundBegin(assertAndMerge(neuer, zidane), 2);

        assertActNow(assertAndMerge(neuer, zidane), 1);
        zidane.sendDoneActing();
        assertDoneActing(assertAndMerge(neuer, zidane), 1);

        assertActNow(assertAndMerge(neuer, zidane), 0);
        neuer.sendDoneActing();
        assertDoneActing(assertAndMerge(neuer, zidane), 0);

        e = assertAndMerge(neuer, zidane);
        if (!(e instanceof FieldEffect)) {
            throw new IllegalArgumentException("87: FieldEffect was expected but Event was of other kind.");
        }
        fe = (FieldEffect) e;
        if (fe.getValue() != 20) {
            throw new IllegalArgumentException("91: FieldEffect with value of 20 was expected but other value was found.");
        }
        if (fe.getyCoord() != 0) {
            throw new IllegalArgumentException("94: FieldEffect with y-Coord of 0 was expected but other value was found.");
        }
        if (fe.getxCoord() == 1) {
            assertFieldEffect(assertAndMerge(neuer, zidane), 2, 0, 20);
        } else {
            if (fe.getxCoord() == 2) {
                assertFieldEffect(assertAndMerge(neuer, zidane), 1, 0, 20);
            } else {
                throw new IllegalArgumentException("102: FieldEffect at invalid coord (x-Coord was neither 1 nor 2).");
            }
        }
        assertRoundEnd(assertAndMerge(neuer, zidane), 2, 0);

        //Round 2 of 4
        assertRoundBegin(assertAndMerge(neuer, zidane), 3);

        assertActNow(assertAndMerge(neuer, zidane), 0);
        neuer.sendDoneActing();
        assertDoneActing(assertAndMerge(neuer, zidane), 0);

        assertActNow(assertAndMerge(neuer, zidane), 1);
        zidane.sendDoneActing();
        assertDoneActing(assertAndMerge(neuer, zidane), 1);

        e = assertAndMerge(neuer, zidane);
        if (!(e instanceof FieldEffect)) {
            throw new IllegalArgumentException("120: FieldEffect was expected but Event was of other kind.");
        }
        fe = (FieldEffect) e;
        if (fe.getValue() != 20) {
            throw new IllegalArgumentException("124: FieldEffect with value of 20 was expected but other value was found.");
        }
        if (fe.getyCoord() != 0) {
            throw new IllegalArgumentException("127: FieldEffect with y-Coord of 0 was expected but other value was found.");
        }
        if (fe.getxCoord() == 1) {
            assertFieldEffect(assertAndMerge(neuer, zidane), 2, 0, 20);
        } else {
            if (fe.getxCoord() == 2) {
                assertFieldEffect(assertAndMerge(neuer, zidane), 1, 0, 20);
            } else {
                throw new IllegalArgumentException("135: FieldEffect at invalid coord (x-Coord was neither 1 nor 2).");
            }
        }
        assertRoundEnd(assertAndMerge(neuer, zidane), 3, 0);

        //Round 3 of 4
        assertRoundBegin(assertAndMerge(neuer, zidane), 4);

        assertActNow(assertAndMerge(neuer, zidane), 1);
        zidane.sendDoneActing();
        assertDoneActing(assertAndMerge(neuer, zidane), 1);

        assertActNow(assertAndMerge(neuer, zidane), 0);
        neuer.sendDoneActing();
        assertDoneActing(assertAndMerge(neuer, zidane), 0);

        e = assertAndMerge(neuer, zidane);
        if (!(e instanceof FieldEffect)) {
            throw new IllegalArgumentException("153: FieldEffect was expected but Event was of other kind.");
        }
        fe = (FieldEffect) e;
        if (fe.getValue() != 20) {
            throw new IllegalArgumentException("157: FieldEffect with value of 20 was expected but other value was found.");
        }
        if (fe.getyCoord() != 0) {
            throw new IllegalArgumentException("160: FieldEffect with y-Coord of 0 was expected but other value was found.");
        }
        if (fe.getxCoord() == 1) {
            assertFieldEffect(assertAndMerge(neuer, zidane), 2, 0, 20);
        } else {
            if (fe.getxCoord() == 2) {
                assertFieldEffect(assertAndMerge(neuer, zidane), 1, 0, 20);
            } else {
                throw new IllegalArgumentException("168: FieldEffect at invalid coord (x-Coord was neither 1 nor 2).");
            }
        }
        assertRoundEnd(assertAndMerge(neuer, zidane), 4, 0);

        //Round 4 of 4
        assertRoundBegin(assertAndMerge(neuer, zidane), 5);

        assertActNow(assertAndMerge(neuer, zidane), 0);
        neuer.sendDoneActing();
        assertDoneActing(assertAndMerge(neuer, zidane), 0);

        assertActNow(assertAndMerge(neuer, zidane), 1);
        zidane.sendDoneActing();
        assertDoneActing(assertAndMerge(neuer, zidane), 1);

        Event e1 = assertAndMerge(neuer, zidane);
        if (!(e1 instanceof FieldEffect)) {
            throw new IllegalArgumentException("166: Expected FieldEffect (Water) but was other event.");
        }
        FieldEffect fe1 = (FieldEffect) e1;
        assertEquals(fe1.getValue(), 20);
        assertEquals(fe1.getyCoord(), 0);
        if (fe1.getxCoord() == 1) {
            assertDied(assertAndMerge(neuer, zidane), 0);
            assertFieldEffect(assertAndMerge(neuer, zidane), 2, 0, 20);
            assertDied(assertAndMerge(neuer, zidane), 1);
        } else {
            if (fe1.getxCoord() == 2) {
                assertDied(assertAndMerge(neuer, zidane), 1);
                assertFieldEffect(assertAndMerge(neuer, zidane), 1, 0, 20);
                assertDied(assertAndMerge(neuer, zidane), 0);
            } else {
                throw new IllegalArgumentException("181: Expected FieldEffect (Water) at either (1, 0) or (2, 0) but was at invalid x-Position.");
            }
        }
        assertRoundEnd(assertAndMerge(neuer, zidane), 5, 0);

        //Both are dead, no winne
        assertWinner(assertAndMerge(neuer, zidane), "");
    }

    @Override
    public String getMapFile(){
        return "~~~\n"
              +"01~\n"
              +"~~~";
    }

    public String getFightFile(){
        return "Deutschland, Frankreich\n"
              +"Deutschland, Neuer, Rook\n"
              +"Frankreich, Zidane, Ifrit";
    }

}
