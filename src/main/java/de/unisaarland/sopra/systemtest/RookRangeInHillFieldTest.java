package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Karla on 20.09.2016.
 * This Class tests the field effects of a hill field on the attack range of a Rook
 */
public class RookRangeInHillFieldTest extends OurSystemTest {

    private static final String FIGHTFILE =
                 "Hola, Chau\n"
                    + "Hola, Lukas, Rook\n"
                    + "Chau, Chris, Wraith";

    private static final String MAPFILE =
                      "^0ttt\n"
                    + "tttxx\n"
                    + "  xxx\n"
                    + "xxxxx\n"
                    + "xx  x\n"
                    + "ttttx\n"
                    + "~~~~x\n"
                    + "~~~~x\n"
                    + "1   x\n"
                    + "xxxxx";

    public RookRangeInHillFieldTest(){
        super("Rook Attack in Hill Field Test");
    }

    @Override
    protected void executeTest() {

        ClientConnection<Event> lukas = register(0,"Lukas", CreatureType.ROOK,"Hola",1,0);
        ClientConnection<Event> chris = register(1,"Chris",CreatureType.WRAITH,"Chau",-4,8);

        assertRegisterEvent(chris.nextEvent(),0,"Lukas", CreatureType.ROOK,"Hola",1,0);
        assertRegisterEvent(lukas.nextEvent(),1,"Chris",CreatureType.WRAITH,"Chau",-4,8);

        assertRoundBegin(assertAndMerge(chris,lukas),1);

        //Lukas' turn
        //Lukas moves E:770, HP:100
        assertActNow(assertAndMerge(chris,lukas),0);
        lukas.sendMove(Direction.WEST);
        assertMoved(assertAndMerge(chris,lukas),0,Direction.WEST);
        //Lukas attacks E:320, HP:100
        assertActNow(assertAndMerge(chris,lukas),0);
        lukas.sendCast(-4,8);
        assertCast(assertAndMerge(chris,lukas),0,-4,8);     //Chris HP:90
        //Lukas passes
        assertActNow(assertAndMerge(chris,lukas),0);
        lukas.sendDoneActing();
        assertDoneActing(assertAndMerge(chris,lukas),0);

        //Chris' turn
        //Chris moves E:0 HP:90
        assertActNow(assertAndMerge(chris,lukas),1);
        chris.sendBlink(0,9);
        assertBlinked(assertAndMerge(chris,lukas),1,0,9);
        //Chris passes
        assertActNow(assertAndMerge(chris,lukas),1);
        chris.sendDoneActing();
        assertDoneActing(assertAndMerge(chris,lukas),1);

        assertRoundEnd(assertAndMerge(chris,lukas),1,0);
        //Round 2 starts----------------------------------------
        assertRoundBegin(assertAndMerge(chris,lukas),2);

        //Lukas tries to attacks, is kicked out
        assertActNow(assertAndMerge(chris,lukas),0);
        lukas.sendCast(0,9);
        assertKicked(assertAndMerge(chris,lukas),0);
        //Chris wins!
        assertActNow(assertAndMerge(chris,lukas),1);
        chris.sendDoneActing();
        assertDoneActing(assertAndMerge(chris,lukas),1);

        assertRoundEnd(assertAndMerge(chris,lukas),2,0);
        assertWinner(assertAndMerge(chris,lukas),"Chau");
        passed();

    }

    @Override
    protected String getMapFile() {
        return MAPFILE;
    }

    @Override
    protected String getFightFile() {
        return FIGHTFILE;
    }
}
