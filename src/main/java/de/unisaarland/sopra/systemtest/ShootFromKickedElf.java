package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wiebk on 23.09.2016.
 */
public class ShootFromKickedElf extends OurSystemTest {

    public ShootFromKickedElf(){
        super("ShootFromKickedElf");
    }

    @Override
    public void executeTest(){
        List<ClientConnection<Event>> clients = new ArrayList<>();

        //register
        ClientConnection<Event> a = register(0, "Andreas", CreatureType.ELF, "a", 0, 0);
        ClientConnection<Event> b = register(1, "Benni", CreatureType.ELF, "b", 1, 0);
        assertRegisterEvent(a.nextEvent(), 1, "Benni", CreatureType.ELF, "b", 1, 0);
        assertRegisterEvent(b.nextEvent(), 0, "Andreas", CreatureType.ELF, "a", 0, 0);
        clients.add(a);
        clients.add(b);

        ClientConnection<Event> c = register(2, "Chris", CreatureType.ELF, "c", 2, 0);
        assertRegisterEvent(assertAndMerge(clients), 2, "Chris", CreatureType.ELF, "c", 2, 0);
        assertRegisterEvent(c.nextEvent(), 0, "Andreas", CreatureType.ELF, "a", 0, 0);
        assertRegisterEvent(c.nextEvent(), 1, "Benni", CreatureType.ELF, "b", 1, 0);
        clients.add(c);

        //roundbegin
        assertRoundBegin(assertAndMerge(a, b, c), 1);

        //a gets kicked for shoot at grass field 0, 1
        assertActNow(assertAndMerge(a, b, c), 0);
        a.sendShoot(Direction.SOUTH_EAST);
        assertKicked(assertAndMerge(a, b, c), 0);

        //b's turn: done acting
        assertActNow(assertAndMerge(a, b, c), 1);
        b.sendDoneActing();
        assertDoneActing(assertAndMerge(a, b, c), 1);

        //c: done acting
        assertActNow(assertAndMerge(a, b, c), 2);
        b.sendDoneActing();
        assertDoneActing(assertAndMerge(a, b, c), 2);

        //roundend
        assertRoundEnd(assertAndMerge(a, b, c), 2, 1);

        //round2
        //b's turn, but a sends shoot, should be ignored
        assertActNow(assertAndMerge(a, b, c), 1);
        a.sendShoot(Direction.EAST);
        b.sendDoneActing();
        assertDoneActing(assertAndMerge(a, b, c), 1);

        //c: shoot at rock, gets kicked
        assertActNow(assertAndMerge(a, b, c), 2);
        c.sendShoot(Direction.EAST);
        assertKicked(assertAndMerge(a, b, c), 2);

        //Roundend
        assertRoundEnd(assertAndMerge(a, b, c), 2, 2);

        //Winner: b
        assertWinner(assertAndMerge(a, b, c), "b");
    }

    @Override
    public String getFightFile(){
        return "a, b, c\n"
              +"a, Andreas, Elf\n"
              +"b, Benni, ELF\n"
              +"c, Chris, ELF";
    }

    @Override
    public String getMapFile(){
        return "012\n"
              +"   \n"
              +"   ";
    }

}
