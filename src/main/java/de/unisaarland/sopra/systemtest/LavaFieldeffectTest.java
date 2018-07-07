package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Wiebk on 20.09.2016.
 */
public class LavaFieldeffectTest extends OurSystemTest {

    public LavaFieldeffectTest(){
        super("LavaFieldeffectTest");
    }

    @Override
    public void executeTest(){
        ClientConnection<Event> george = register(0, "George", CreatureType.IFRIT, "Golems", 0, 1);
        ClientConnection<Event> campino = register(1, "Campino", CreatureType.KOBOLD, "Dämonen", 1, 1);
        assertRegisterEvent(campino.nextEvent(), 0, "George", CreatureType.IFRIT, "Golems", 0, 1);
        assertRegisterEvent(george.nextEvent(), 1, "Campino", CreatureType.KOBOLD, "Dämonen", 1, 1);

        assertRoundBegin(assertAndMerge(george, campino), 1);

        //both move North_East onto lava
        //George: 87 HP
        assertActNow(assertAndMerge(george, campino), 0);
        george.sendMove(Direction.NORTH_EAST);
        assertMoved(assertAndMerge(george, campino), 0, Direction.NORTH_EAST);
        assertActNow(assertAndMerge(george, campino), 0);
        george.sendDoneActing();
        assertDoneActing(assertAndMerge(george, campino), 0);

        //Campino: 100 HP
        assertActNow(assertAndMerge(george, campino), 1);
        campino.sendMove(Direction.NORTH_EAST);
        assertMoved(assertAndMerge(george, campino), 1, Direction.NORTH_EAST);
        assertActNow(assertAndMerge(george, campino), 1);
        campino.sendDoneActing();
        assertDoneActing(assertAndMerge(campino, george), 1);

        //Round ends, Lavaeffect: -25 for Campino but not for George
        assertFieldEffect(assertAndMerge(george, campino), 2, 0, 25);
        assertRoundEnd(assertAndMerge(george, campino), 1, 0);

        //2nd Round
        assertRoundBegin(assertAndMerge(george, campino), 2);

        //Campino: 63 HP
        assertActNow(assertAndMerge(george, campino), 1);
        campino.sendDoneActing();
        assertDoneActing(assertAndMerge(george, campino), 1);

        //George sends burn
        assertActNow(assertAndMerge(george, campino), 0);
        george.sendBurn();
        assertBurned(assertAndMerge(george, campino), 0);

        assertActNow(assertAndMerge(george, campino), 0);
        george.sendDoneActing();
        assertDoneActing(assertAndMerge(george, campino), 0);


        //Round ends, Lavaeffect: -25 HP Campino
        assertFieldEffect(assertAndMerge(george, campino), 2, 0, 25);
        assertRoundEnd(assertAndMerge(george, campino), 2, 0);

        assertRoundBegin(assertAndMerge(george, campino), 3);

        //Campino: 38 HP
        assertActNow(assertAndMerge(george, campino), 1);
        campino.sendDoneActing();
        assertDoneActing(assertAndMerge(george, campino), 1);

        // George:
        assertActNow(assertAndMerge(george, campino), 0);
        george.sendBurn();
        assertBurned(assertAndMerge(george, campino), 0);
        assertActNow(assertAndMerge(george, campino), 0);
        george.sendDoneActing();
        assertDoneActing(assertAndMerge(george, campino), 0);

        //Campino: 26 HP
        assertFieldEffect(assertAndMerge(george, campino), 2, 0, 25);
        assertRoundEnd(assertAndMerge(george, campino), 3, 0);

        assertRoundBegin(assertAndMerge(george, campino), 4);

        //Campino: 1 HP
        assertActNow(assertAndMerge(george, campino), 1);
        campino.sendDoneActing();
        assertDoneActing(assertAndMerge(george, campino), 1);

        //George
        assertActNow(assertAndMerge(george, campino), 0);
        george.sendDoneActing();
        assertDoneActing(assertAndMerge(george, campino), 0);

        //Roundend, Fieldeffects
        assertFieldEffect(assertAndMerge(george, campino), 2, 0, 25);
        //Campino is dead
        assertDied(assertAndMerge(george, campino), 1);

        assertRoundEnd(assertAndMerge(george, campino), 4, 0);

        assertWinner(assertAndMerge(george, campino), "Golems");
    }

    @Override
    public String getMapFile(){
        return "***\n"
              +"01*\n"
              +"***";
    }

    public String getFightFile(){
        return "Golems, Dämonen\n"
              +"Golems, George, Ifrit\n"
              +"Dämonen, Campino, Kobold";
    }
}
