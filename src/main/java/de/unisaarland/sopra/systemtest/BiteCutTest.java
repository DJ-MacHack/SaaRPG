package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.messages.PoisonEffect;
import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Wiebk on 19.09.2016.
 */
public class BiteCutTest extends OurSystemTest {

    public BiteCutTest() { super ("BiteCutTest");}

    @Override
    public void executeTest(){
        //register
        ClientConnection<Event> brutus = register(0, "Brutus", CreatureType.NAGA, "Krokodil", 0, 1);
        ClientConnection<Event> linda = register(1, "Linda", CreatureType.IFRIT, "Giraffe", 1, 1);
        Event registerbrutus = linda.nextEvent();
        assertRegisterEvent(registerbrutus, 0, "Brutus", CreatureType.NAGA, "Krokodil", 0, 1);
        assertRegisterEvent(brutus.nextEvent(), 1, "Linda", CreatureType.IFRIT, "Giraffe", 1, 1);

        assertRoundBegin(assertAndMerge(brutus, linda), 1);

        //Brutus: Move towards North_East, Cut to Linda, then bite
        assertActNow(assertAndMerge(brutus, linda), 0);
        brutus.sendMove(Direction.NORTH_EAST);
        assertMoved(assertAndMerge(brutus, linda), 0, Direction.NORTH_EAST);

        assertActNow(assertAndMerge(brutus, linda), 0);
        brutus.sendCut(Direction.SOUTH_EAST);
        assertCut(assertAndMerge(brutus, linda), 0, Direction.SOUTH_EAST);

        assertActNow(assertAndMerge(brutus, linda), 0);
        brutus.sendBite(Direction.SOUTH_EAST);
        assertBitten(assertAndMerge(brutus, linda), 0, Direction.SOUTH_EAST);

        assertActNow(assertAndMerge(brutus, linda), 0);
        brutus.sendDoneActing();
        assertDoneActing(assertAndMerge(brutus, linda), 0);

        //Linda: move to North_East (3x energy, because of swamp field) 87-5-6 = 76 HP
        assertActNow(assertAndMerge(brutus, linda), 1);
        linda.sendMove(Direction.NORTH_EAST);
        assertMoved(assertAndMerge(brutus, linda), 1, Direction.NORTH_EAST);

        assertActNow(assertAndMerge(brutus, linda), 1);
        linda.sendDoneActing();
        assertDoneActing(assertAndMerge(brutus, linda), 1);
        assertRoundEnd(assertAndMerge(brutus, linda), 1, 0);

        assertRoundBegin(assertAndMerge(brutus, linda), 2);

        //Linda: does nothing, done acting
        assertActNow(assertAndMerge(brutus, linda), 1);
        linda.sendDoneActing();
        assertDoneActing(assertAndMerge(brutus, linda), 1);

        //Brutus: cut and 2xbite to Linda (76-6-6-5 = 59 HP)
        assertActNow(assertAndMerge(brutus, linda), 0);
        brutus.sendBite(Direction.EAST);
        assertBitten(assertAndMerge(brutus, linda), 0, Direction.EAST);

        assertActNow(assertAndMerge(brutus, linda), 0);
        brutus.sendBite(Direction.EAST);
        assertBitten(assertAndMerge(brutus, linda), 0, Direction.EAST);

        assertActNow(assertAndMerge(brutus, linda), 0);
        brutus.sendCut(Direction.EAST);
        assertCut(assertAndMerge(brutus, linda), 0, Direction.EAST);

        assertActNow(assertAndMerge(brutus, linda), 0);
        brutus.sendDoneActing();
        assertDoneActing(assertAndMerge(brutus, linda), 0);

        //Poisoneffect: Linda 59-4 = 55 HP
        assertPoisonEffect(assertAndMerge(brutus, linda), 1, 4);
        assertRoundEnd(assertAndMerge(brutus, linda), 2, 0);

        assertRoundBegin(assertAndMerge(brutus, linda), 3);

        //Linda: does nothing, 55-6-6-5 = 38 HP
        assertActNow(assertAndMerge(brutus, linda), 1);
        linda.sendDoneActing();
        assertDoneActing(assertAndMerge(brutus, linda), 1);

        //Brutus: 2x bite, 1x cut
        assertActNow(assertAndMerge(brutus, linda), 0);
        brutus.sendBite(Direction.EAST);
        assertBitten(assertAndMerge(brutus, linda), 0, Direction.EAST);

        assertActNow(assertAndMerge(brutus, linda), 0);
        brutus.sendBite(Direction.EAST);
        assertBitten(assertAndMerge(brutus, linda), 0, Direction.EAST);

        assertActNow(assertAndMerge(brutus, linda), 0);
        brutus.sendCut(Direction.EAST);
        assertCut(assertAndMerge(brutus, linda), 0, Direction.EAST);

        assertActNow(assertAndMerge(brutus, linda), 0);
        brutus.sendDoneActing();
        assertDoneActing(assertAndMerge(brutus, linda), 0);

        //Poisoneffect: 2-4-4 = 28 HP
        List<Event> events = new ArrayList<>();
        events.add(assertAndMerge(brutus, linda));
        events.add(assertAndMerge(brutus, linda));
        events.add(assertAndMerge(brutus, linda));
        assertEquals(events.size(), 3);
        for (Event e : events) {
            if (!(e instanceof PoisonEffect)) {
                failed("PoisonEffect was expected but Event was of other kind.");
            }
        }
        if (!events.contains(new PoisonEffect(1, 2))) {
            failed("PoisonEffect on Linda (id: 1) and value of 2 was expected but not included!");
        }
        if (!events.contains(new PoisonEffect(1, 4))) {
            failed("PoisonEffect on Linda (id: 1) and value of 4 was expected but not included!");
        }
        if (!events.contains(new PoisonEffect(1, 4))) {
            failed("PoisonEffect on Linda (id: 1) and value of 4 was expected but not included!");
        }

        assertRoundEnd(assertAndMerge(brutus, linda), 3, 0);

        assertRoundBegin(assertAndMerge(brutus, linda), 4);

        //Linda: does nothing
        assertActNow(assertAndMerge(brutus, linda), 1);
        linda.sendDoneActing();
        assertDoneActing(assertAndMerge(brutus, linda), 1);

        //Brutus: 2x bite, 1x cut, Linda: 28-6-6-5 = 11 HP
        assertActNow(assertAndMerge(brutus, linda), 0);
        brutus.sendBite(Direction.EAST);
        assertBitten(assertAndMerge(brutus, linda), 0, Direction.EAST);

        assertActNow(assertAndMerge(brutus, linda), 0);
        brutus.sendBite(Direction.EAST);
        assertBitten(assertAndMerge(brutus, linda), 0, Direction.EAST);

        assertActNow(assertAndMerge(brutus, linda), 0);
        brutus.sendCut(Direction.EAST);
        assertCut(assertAndMerge(brutus, linda), 0, Direction.EAST);

        assertActNow(assertAndMerge(brutus, linda), 0);
        brutus.sendDoneActing();
        assertDoneActing(assertAndMerge(brutus, linda), 0);

        //Poisoneffect:
        events = new ArrayList<>();
        events.add(assertAndMerge(brutus, linda));
        events.add(assertAndMerge(brutus, linda));
        events.add(assertAndMerge(brutus, linda));
        events.add(assertAndMerge(brutus, linda));
        assertEquals(events.size(), 4);
        for (Event e : events) {
            if (!(e instanceof PoisonEffect)) {
                failed("PoisonEffect was expected but Event was of other kind.");
            }
        }
        if (!events.contains(new PoisonEffect(1, 2))) {
            failed("PoisonEffect on Linda (id: 1) and value of 2 was expected but not included!");
        }
        if (!events.contains(new PoisonEffect(1, 2))) {
            failed("PoisonEffect on Linda (id: 1) and value of 2 was expected but not included!");
        }
        if (!events.contains(new PoisonEffect(1, 4))) {
            failed("PoisonEffect on Linda (id: 1) and value of 4 was expected but not included!");
        }
        if (!events.contains(new PoisonEffect(1, 4))) {
            failed("PoisonEffect on Linda (id: 1) and value of 4 was expected but not included!");
        }

        //Linda: 11-12 = 0 HP
        assertDied(assertAndMerge(brutus, linda), 1);
        assertRoundEnd(assertAndMerge(brutus, linda), 4, 0);

        assertWinner(assertAndMerge(brutus, linda), "Krokodil");
    }

    @Override
    public String getMapFile(){
        return "%%%%\n"
              +"01%%\n"
              +"%%%%\n"
              +"%%%%";

    }

    @Override
    public String getFightFile(){
        return "Krokodil, Giraffe\n"
               +"Krokodil, Brutus, Naga\n"
               +"Giraffe, Linda, Ifrit" ;
    }

}
