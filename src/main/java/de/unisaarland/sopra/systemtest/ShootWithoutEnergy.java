package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Wiebk on 23.09.2016.
 */
public class ShootWithoutEnergy extends OurSystemTest {

    public ShootWithoutEnergy(){
        super ("ShootWithoutEnergy");
    }

    @Override
    public void executeTest(){
        //register
        ClientConnection<Event> a = register(0, "Alex", CreatureType.ELF, "a", 0, 0);
        ClientConnection<Event> b = register(1, "Bertha", CreatureType.IFRIT, "b", 1, 0);
        assertRegisterEvent(a.nextEvent(), 1, "Bertha", CreatureType.IFRIT, "b", 1, 0);
        assertRegisterEvent(b.nextEvent(), 0, "Alex", CreatureType.ELF, "a", 0, 0);

        //Roundbegin (no fairies or boars)
        assertRoundBegin(assertAndMerge(a, b), 1);
        //a: moves around, until no energy's left
        //1
        assertActNow(assertAndMerge(a, b), 0);
        a.sendMove(Direction.SOUTH_EAST);
        //2
        assertActNow(assertAndMerge(a, b), 0);
        a.sendMove(Direction.SOUTH_WEST);
        //3
        assertActNow(assertAndMerge(a, b), 0);
        a.sendMove(Direction.EAST);
        //4
        assertActNow(assertAndMerge(a, b), 0);
        a.sendMove(Direction.EAST);
        //5
        assertActNow(assertAndMerge(a, b), 0);
        a.sendMove(Direction.NORTH_WEST);
        //6
        assertActNow(assertAndMerge(a, b), 0);
        a.sendMove(Direction.EAST);
        //7
        assertActNow(assertAndMerge(a, b), 0);
        a.sendMove(Direction.NORTH_WEST);
        //8
        assertActNow(assertAndMerge(a, b), 0);
        a.sendMove(Direction.SOUTH_EAST);
        //9
        assertActNow(assertAndMerge(a, b), 0);
        a.sendMove(Direction.WEST);
        //10
        assertActNow(assertAndMerge(a, b), 0);
        a.sendMove(Direction.SOUTH_WEST);
        //No energy left, next move is invalid, gets kicked
        assertActNow(assertAndMerge(a, b), 0);
        a.sendMove(Direction.EAST);
        assertKicked(assertAndMerge(a, b), 0);

        //b: does nothing
        assertActNow(assertAndMerge(a, b), 1);
        b.sendDoneActing();
        assertDoneActing(assertAndMerge(a, b), 1);

        //Roundend
        assertRoundEnd(assertAndMerge(a, b), 1, 1);
        //Winner: b
        assertWinner(assertAndMerge(a, b), "b");
    }

    @Override
    public String getFightFile(){
        return "a, b\n"
              +"a, Alex, Elf\n"
              +"b, Bertha, Ifrit";
    }

    @Override
    public String getMapFile(){
        return "01 \n"
              +"   \n"
              +"   ";
    }

}
