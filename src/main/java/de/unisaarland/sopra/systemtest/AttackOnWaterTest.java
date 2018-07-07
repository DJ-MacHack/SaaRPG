package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Wiebk on 19.09.2016.
 */
public class AttackOnWaterTest extends OurSystemTest {


    public AttackOnWaterTest(){
        super("AttackOnWaterTest");
    }

    @Override
    public void executeTest(){
        //register
        ClientConnection<Event> flamara = register(0, "Flamara", CreatureType.IFRIT, "Fire", 1, 1);
        ClientConnection<Event> aquana = register(1, "Aquana", CreatureType.ELF, "Water", 3, 1);
        Event registerflamara = aquana.nextEvent();
        assertRegisterEvent(registerflamara, 0, "Flamara", CreatureType.IFRIT, "Fire", 1, 1);
        assertRegisterEvent(flamara.nextEvent(), 1, "Aquana", CreatureType.ELF, "Water", 3, 1);

        assertRoundBegin(assertAndMerge(flamara, aquana), 1);

        //flamara: moves west on water, then singe east, gets Kicked
        assertActNow(assertAndMerge(flamara, aquana), 0);
        flamara.sendMove(Direction.WEST);
        assertMoved(assertAndMerge(flamara, aquana), 0, Direction.WEST);

        assertActNow(assertAndMerge(flamara, aquana), 0);
        flamara.sendSinge(Direction.WEST);
        assertKicked(assertAndMerge(flamara, aquana), 0);

        assertActNow(assertAndMerge(flamara, aquana), 1);
        aquana.sendDoneActing();
        assertDoneActing(assertAndMerge(flamara, aquana), 1);

        assertRoundEnd(assertAndMerge(flamara, aquana), 1, 0);

        //Water wins
        assertWinner(assertAndMerge(flamara, aquana), "Water");
    }

    @Override
    public String getMapFile(){
        return "~~~~\n"
              +"~0~1\n"
              +"~~~~\n"
              +"~~~~";
    }

    public String getFightFile(){
        return "Fire, Water\n"
              +"Fire, Flamara, Ifrit\n"
              +"Water, Aquana, Elf";
    }
}
