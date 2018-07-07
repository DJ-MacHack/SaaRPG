package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Wiebk on 21.09.2016.
 */
public class ShootWithoutHindranceTest extends OurSystemTest {

    public ShootWithoutHindranceTest(){
        super("ShootWithoutHindranceTest");
    }

    @Override
    public void executeTest(){
        ClientConnection<Event> edamer = register(0, "Edamer", CreatureType.ELF, "Kaese", 0, 0);
        ClientConnection<Event> zartbitter = register(1, "Zartbitter", CreatureType.ELF, "Schokolade", 2, 0);
        assertRegisterEvent(edamer.nextEvent(), 1, "Zartbitter", CreatureType.ELF, "Schokolade", 2, 0);
        assertRegisterEvent(zartbitter.nextEvent(), 0, "Edamer", CreatureType.ELF, "Kaese", 0, 0);

        //Roundbegin, no elves or boars
        assertRoundBegin(assertAndMerge(edamer, zartbitter), 1);

        //edamer: shoot at zartbitter
        assertActNow(assertAndMerge(edamer, zartbitter), 0);
        edamer.sendShoot(Direction.EAST);
        assertShot(assertAndMerge(edamer, zartbitter), 0, Direction.EAST);
        assertActNow(assertAndMerge(edamer, zartbitter), 0);
        edamer.sendDoneActing();
        assertDoneActing(assertAndMerge(edamer, zartbitter), 0);

        //zartbitter:
        assertActNow(assertAndMerge(edamer, zartbitter), 1);
        zartbitter.sendCrush(Direction.NORTH_EAST);
        assertKicked(assertAndMerge(edamer, zartbitter), 1);

        assertRoundEnd(assertAndMerge(edamer, zartbitter), 1, 0);
        //winner: kaese
        assertWinner(assertAndMerge(edamer, zartbitter), "Kaese");
    }

    @Override
    public String getMapFile(){
        return"0 1\n"
             +"   \n"
             +"   ";
    }

    @Override
    public String getFightFile(){
        return "Kaese, Schokolade\n"
              +"Kaese, Edamer, Elf\n"
              +"Schokolade, Zartbitter, Elf";

    }

}
