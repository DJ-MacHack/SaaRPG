package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Wiebk on 23.09.2016.
 */
public class ShootInWrongActNowPhase extends OurSystemTest {

    public ShootInWrongActNowPhase(){
        super ("ShootInWrongActNowPhase");
    }

    @Override
    public void executeTest(){
        //register
        ClientConnection<Event> a = register(0, "Andreas", CreatureType.ELF, "a", 0, 0);
        ClientConnection<Event> b = register(1, "Benni", CreatureType.IFRIT, "b", 1, 0);
        assertRegisterEvent(a.nextEvent(), 1, "Benni", CreatureType.IFRIT, "b", 1, 0);
        assertRegisterEvent(b.nextEvent(), 0, "Andreas", CreatureType.ELF, "a", 0, 0);

        //roundbegin
        assertRoundBegin(assertAndMerge(a, b), 1);

        //a: sends done acting
        assertActNow(assertAndMerge(a, b), 0);
        a.sendDoneActing();
        assertDoneActing(assertAndMerge(a, b), 0);

        //b: does nothing, but a sends Shoot
        assertActNow(assertAndMerge(a, b), 1);
        a.sendShoot(Direction.EAST);
        assertKicked(assertAndMerge(a, b), 0);
        b.sendDoneActing();
        assertDoneActing(assertAndMerge(a, b), 1);

        //roundend
        assertRoundEnd(assertAndMerge(a, b), 1, 1);
        assertWinner(assertAndMerge(a, b), "b");
    }

    @Override
    public String getFightFile(){
        return "a, b\n"
              +"a, Andreas, Elf\n"
              +"b, Benni, Kobold";
    }

    @Override
    public String getMapFile(){
        return "01 \n"
              +"   \n"
              +"   ";
    }
}
