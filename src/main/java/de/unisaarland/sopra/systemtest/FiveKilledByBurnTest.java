package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wiebk on 23.09.2016.
 */
public class FiveKilledByBurnTest extends OurSystemTest {

    public FiveKilledByBurnTest(){
        super ("FiveKilledByBurnTest");
    }

    @Override
    public void executeTest(){
        //register
        List<ClientConnection<Event>> clients = new ArrayList<>();

        ClientConnection<Event> a = register(0, "Candy", CreatureType.KOBOLD, "a", 1, 0);
        ClientConnection<Event> b = register(1, "Nina", CreatureType.NAGA, "b", 2, 0);
        assertRegisterEvent(a.nextEvent(), 1, "Nina", CreatureType.NAGA, "b", 2, 0);
        assertRegisterEvent(b.nextEvent(), 0, "Candy", CreatureType.KOBOLD, "a", 1, 0);
        clients.add(a);
        clients.add(b);

        ClientConnection<Event> c = register(2, "Emir", CreatureType.ELF, "c", 0, 1);
        assertRegisterEvent(assertAndMerge(clients), 2, "Emir", CreatureType.ELF, "c", 0, 1);
        assertRegisterEvent(c.nextEvent(), 0, "Candy", CreatureType.KOBOLD, "a", 1, 0);
        assertRegisterEvent(c.nextEvent(), 1, "Nina", CreatureType.NAGA, "b", 2, 0);
        clients.add(c);

        ClientConnection<Event> d = register(3, "Rusty", CreatureType.ROOK, "d", 2, 1);
        assertRegisterEvent(assertAndMerge(clients), 3, "Rusty", CreatureType.ROOK, "d", 2, 1);
        assertRegisterEvent(d.nextEvent(), 0, "Candy", CreatureType.KOBOLD, "a", 1, 0);
        assertRegisterEvent(d.nextEvent(), 1, "Nina", CreatureType.NAGA, "b", 2, 0);
        assertRegisterEvent(d.nextEvent(), 2, "Emir", CreatureType.ELF, "c", 0, 1);
        clients.add(d);


        ClientConnection<Event> e = register(4, "Werner", CreatureType.WRAITH, "e", 1, 2);
        assertRegisterEvent(assertAndMerge(clients), 4, "Werner", CreatureType.WRAITH, "e", 1, 2);
        assertRegisterEvent(e.nextEvent(), 0, "Candy", CreatureType.KOBOLD, "a", 1, 0);
        assertRegisterEvent(e.nextEvent(), 1, "Nina", CreatureType.NAGA, "b", 2, 0);
        assertRegisterEvent(e.nextEvent(), 2, "Emir", CreatureType.ELF, "c", 0, 1);
        assertRegisterEvent(e.nextEvent(), 3, "Rusty", CreatureType.ROOK, "d", 2, 1);
        clients.add(e);

        ClientConnection<Event> f = register(5, "Ina", CreatureType.IFRIT, "f", 1, 1);
        assertRegisterEvent(assertAndMerge(clients), 5, "Ina", CreatureType.IFRIT, "f", 1, 1);
        assertRegisterEvent(f.nextEvent(), 0, "Candy", CreatureType.KOBOLD, "a", 1, 0);
        assertRegisterEvent(f.nextEvent(), 1, "Nina", CreatureType.NAGA, "b", 2, 0);
        assertRegisterEvent(f.nextEvent(), 2, "Emir", CreatureType.ELF, "c", 0, 1);
        assertRegisterEvent(f.nextEvent(), 3, "Rusty", CreatureType.ROOK, "d", 2, 1);
        assertRegisterEvent(f.nextEvent(), 4, "Werner", CreatureType.WRAITH, "e", 1, 2);
        clients.add(f);

        //no boars or fairies: roundbegin

        for(int i = 1; i <= 50; i++) {
            assertRoundBegin(assertAndMerge(a, b, c, d, e, f), i);
            //a: done acting
            assertActNow(assertAndMerge(a, b, c, d, e, f), 0);
            a.sendDoneActing();
            assertDoneActing(assertAndMerge(a, b, c, d, e, f), 0);
            //b: done acting
            assertActNow(assertAndMerge(a, b, c, d, e, f), 1);
            b.sendDoneActing();
            assertDoneActing(assertAndMerge(a, b, c, d, e, f), 1);
            //c: done acting
            assertActNow(assertAndMerge(a, b, c, d, e, f), 2);
            c.sendDoneActing();
            assertDoneActing(assertAndMerge(a, b, c, d, e, f), 2);
            //d: done acting
            assertActNow(assertAndMerge(a, b, c, d, e, f), 3);
            d.sendDoneActing();
            assertDoneActing(assertAndMerge(a, b, c, d, e, f), 3);
            //e: done acting
            assertActNow(assertAndMerge(a, b, c, d, e, f), 4);
            e.sendDoneActing();
            assertDoneActing(assertAndMerge(a, b, c, d, e, f), 4);
            //f: burn
            assertActNow(assertAndMerge(a, b, c, d, e, f), 5);
            f.sendBurn();
            assertBurned(assertAndMerge(a, b, c, d, e, f), 5);
            assertActNow(assertAndMerge(a, b, c, d, e, f), 5);
            f.sendDoneActing();
            assertDoneActing(assertAndMerge(a, b, c, d, e, f), 5);
            //roundend
            assertRoundEnd(assertAndMerge(a, b, c, d, e, f), i, 0);
        }

        assertDied(assertAndMerge(a, b, c, d, e, f), 0);
        assertDied(assertAndMerge(a, b, c, d, e, f), 1);
        assertDied(assertAndMerge(a, b, c, d, e, f), 2);
        assertDied(assertAndMerge(a, b, c, d, e, f), 3);
        assertDied(assertAndMerge(a, b, c, d, e, f), 4);
        assertWinner(assertAndMerge(a, b, c, d, e, f), "f");
    }

    @Override
    public String getMapFile(){
        return " 01\n"
              +"234\n"
              +" 5 ";
    }

    @Override
    public String getFightFile(){
        return "a, b, c, d, e, f\n"
              +"a, Candy, Kobold\n"
              +"b, Nina, Naga\n"
              +"c, Emir, Elf\n"
              +"d, Rusty, Rook\n"
              +"e, Werner, Wraith\n"
              +"f, Ina, Ifrit";
    }
}
