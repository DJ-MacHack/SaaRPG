package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Wiebk on 21.09.2016.
 */
public class YetiOnIceTest extends OurSystemTest{

    public YetiOnIceTest(){
        super ("YetiOnIceTest");
    }

    @Override
    public void executeTest(){
        ClientConnection<Event> gerolsteiner = register(0, "Gerolsteiner", CreatureType.YETI, "Wasser", 0, 0);
        ClientConnection<Event> bordeaux = register(1, "Bordeaux", CreatureType.YETI, "Wein", 1, 0);
        assertRegisterEvent(gerolsteiner.nextEvent(), 1, "Bordeaux", CreatureType.YETI, "Wein", 1, 0);
        assertRegisterEvent(bordeaux.nextEvent(), 0, "Gerolsteiner", CreatureType.YETI, "Wasser", 0, 0);
        assertRoundBegin(assertAndMerge(gerolsteiner, bordeaux), 1);

        //gerolsteiner moves South_East, then East and finally North_East
        assertActNow(assertAndMerge(gerolsteiner, bordeaux), 0);
        gerolsteiner.sendMove(Direction.NORTH_EAST);
        assertMoved(assertAndMerge(gerolsteiner, bordeaux), 0, Direction.NORTH_EAST);
        assertActNow(assertAndMerge(gerolsteiner, bordeaux), 0);
        gerolsteiner.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(gerolsteiner, bordeaux), 1, Direction.EAST);
        assertActNow(assertAndMerge(gerolsteiner, bordeaux), 0);
        gerolsteiner.sendMove(Direction.NORTH_EAST);
        assertMoved(assertAndMerge(gerolsteiner, bordeaux), 1, Direction.NORTH_EAST);
        assertActNow(assertAndMerge(bordeaux, gerolsteiner), 0);
        gerolsteiner.sendDoneActing();
        assertDoneActing(assertAndMerge(gerolsteiner, bordeaux), 0);

        //bordeaux: moves North_West, onto the wall around the map, gets kicked
        assertActNow(assertAndMerge(gerolsteiner, bordeaux), 1);
        bordeaux.sendMove(Direction.NORTH_WEST);
        assertKicked(assertAndMerge(bordeaux, gerolsteiner), 1);

        //winner: gerolsteiner
        assertWinner(assertAndMerge(gerolsteiner, bordeaux), "Wasser");
    }

    @Override
    public String getFightFile(){
        return "Wasser, Wein\n"
              +"Wasser, Gerolsteiner, Yeti\n"
              +"Wein, Bordeaux, Yeti";
    }

    @Override
    public String getMapFile(){
        return "01_\n"
              +"___\n"
              +"___";
    }


}
