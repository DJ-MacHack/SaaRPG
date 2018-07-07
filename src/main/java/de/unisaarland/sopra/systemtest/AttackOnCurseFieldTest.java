package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Wiebk on 20.09.2016.
 */
public class AttackOnCurseFieldTest extends OurSystemTest {

    public AttackOnCurseFieldTest(){
        super("AttackOnCurseFieldTest");
    }

    @Override
    public void executeTest(){
        ClientConnection<Event> expresso = register(0, "Expresso", CreatureType.WRAITH, "Kaffee", 0, 0);
        ClientConnection<Event> hagebutte = register(1, "Hagebutte", CreatureType.NAGA, "Tee", 1, 0);
        assertRegisterEvent(expresso.nextEvent(), 1, "Hagebutte", CreatureType.NAGA, "Tee", 1, 0);
        assertRegisterEvent(hagebutte.nextEvent(), 0, "Expresso", CreatureType.WRAITH, "Kaffee", 0, 0);

        //1st round:
        assertRoundBegin(assertAndMerge(expresso, hagebutte), 1);

        //expresso: moves onto cursefield, swapps hagebutte onto cursefield
        assertActNow(assertAndMerge(expresso, hagebutte), 0);
        expresso.sendMove(Direction.SOUTH_EAST);
        assertMoved(assertAndMerge(expresso, hagebutte), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(expresso, hagebutte), 0);
        expresso.sendSwap(1, 0);
        assertSwapped(assertAndMerge(expresso, hagebutte), 0, 1, 0);
        assertActNow(assertAndMerge(expresso, hagebutte), 0);
        expresso.sendDoneActing();
        assertDoneActing(assertAndMerge(expresso, hagebutte), 0);

        //hagebutte: attacks expresso with cut (cost: 600 energy)
        assertActNow(assertAndMerge(expresso, hagebutte), 1);
        hagebutte.sendCut(Direction.NORTH_EAST);
        assertCut(assertAndMerge(expresso, hagebutte), 1, Direction.NORTH_EAST);
        assertActNow(assertAndMerge(expresso, hagebutte), 1);
        hagebutte.sendDoneActing();
        assertDoneActing(assertAndMerge(expresso, hagebutte), 1);

        assertRoundEnd(assertAndMerge(expresso, hagebutte), 1, 0);

        //2nd round:
        assertRoundBegin(assertAndMerge(expresso, hagebutte), 2);
        //expresso sends swap at empty field - gets kicked
        assertActNow(assertAndMerge(expresso, hagebutte), 0);
        expresso.sendSwap(0, 0);
        assertKicked(assertAndMerge(expresso, hagebutte), 0);

        //hagebutte: does nothing
        assertActNow(assertAndMerge(expresso, hagebutte), 1);
        hagebutte.sendDoneActing();
        assertDoneActing(assertAndMerge(expresso, hagebutte), 1);

        assertRoundEnd(assertAndMerge(expresso, hagebutte), 2, 0);

        //Winner: Hagebutte
        assertWinner(assertAndMerge(expresso, hagebutte), "Tee");
    }

    @Override
    public String getMapFile(){
        return "01$\n"
              +"$$$\n"
              +"$$$";
    }

    @Override
    public String getFightFile(){
        return "Kaffee, Tee\n"
              +"Kaffee, Expresso, Wraith\n"
              +"Tee, Hagebutte, Naga";

    }
}
