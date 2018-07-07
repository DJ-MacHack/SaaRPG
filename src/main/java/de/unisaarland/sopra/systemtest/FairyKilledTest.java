package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Karla on 21.09.2016.
 */
public class FairyKilledTest extends OurSystemTest {

    private static final String FIGHTFILE =
                    "Red, Blue, Green\n"
                    + "Red, Lukas, Naga\n"
                    + "Green, Lukas2, Ifrit\n"
                    + "Blue, Chris, Wraith";
//MAP
    private static final String MAPFILE =
                      "^0   \n"
                    + "  xx1\n"
                    + "tt2xx\n"
                    + "ttt+x\n"
                    + "~~~~x\n"
                    + "~~~~x\n"
                    + "x  ~~";

    public FairyKilledTest(){
        super("Fairy killed Test");
    }
    @Override
    protected void executeTest() {

        ClientConnection<Event> lukas = register(0,"Lukas", CreatureType.NAGA,"Red",1,0);
        ClientConnection<Event> chris = register(1,"Chris",CreatureType.WRAITH,"Blue",4,1);
        ClientConnection<Event> lukas2 = register(2,"Lukas2",CreatureType.IFRIT,"Green",1,2);

        assertRegisterEvent(chris.nextEvent(),0,"Lukas", CreatureType.NAGA,"Red",1,0);
        assertRegisterEvent(chris.nextEvent(),2,"Lukas2",CreatureType.IFRIT,"Green",1,2);

        assertRegisterEvent(lukas.nextEvent(),1,"Chris",CreatureType.WRAITH,"Blue",4,1);
        assertRegisterEvent(lukas.nextEvent(),2,"Lukas2",CreatureType.IFRIT,"Green",1,2);

        assertRegisterEvent(lukas2.nextEvent(),0,"Lukas", CreatureType.NAGA,"Red",1,0);
        assertRegisterEvent(lukas2.nextEvent(),1,"Chris",CreatureType.WRAITH,"Blue",4,1);

        assertFairySpawned(assertAndMerge(chris,lukas,lukas2),3,2,3);
        //Round 1 starts------------------- Fairy HP:100
        assertRoundBegin(assertAndMerge(chris,lukas,lukas2),1);
        //Fairy moves
        assertMoved(assertAndMerge(chris,lukas2,lukas),3,Direction.EAST);
        for(int i =0; i <3; i++){
            assertMoved(assertAndMerge(chris,lukas2,lukas),3,Direction.NORTH_WEST);
        }

        //Lukas' turn
        //Lukas moves E:890, HP:100
        assertActNow(assertAndMerge(chris,lukas,lukas2),0);
        lukas.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(chris,lukas,lukas2),0,Direction.EAST);
        //Lukas attacks E:90, HP:100
        for(int i = 0; i < 4; i++){
            assertActNow(assertAndMerge(chris,lukas,lukas2),0);
            lukas.sendCut(Direction.EAST);
            assertCut(assertAndMerge(chris,lukas,lukas2),0,Direction.EAST);//Fairy HP:80
        }
        //Lukas passes
        assertActNow(assertAndMerge(chris,lukas,lukas2),0);
        lukas.sendDoneActing();
        assertDoneActing(assertAndMerge(chris,lukas,lukas2),0);

        //Chris' turn
        //Chris E:900, HP:100
        assertActNow(assertAndMerge(chris,lukas,lukas2),1);
        chris.sendMove(Direction.NORTH_WEST);
        assertMoved(assertAndMerge(chris,lukas,lukas2),1,Direction.NORTH_WEST);
        //Chris attacks E:150 HP:100
        for(int i = 0; i < 3; i++){
            assertActNow(assertAndMerge(chris,lukas,lukas2),1);
            chris.sendStab(Direction.WEST);
            assertStabbed(assertAndMerge(chris,lukas,lukas2),1,Direction.WEST);//Fairy HP:59
        }
        //Chris passes
        assertActNow(assertAndMerge(chris,lukas,lukas2),1);
        chris.sendDoneActing();
        assertDoneActing(assertAndMerge(chris,lukas,lukas2),1);

        //Lukas2's turn
        //Lukas2 moves E:900, HP:100
        assertActNow(assertAndMerge(chris,lukas,lukas2),2);
        lukas2.sendMove(Direction.NORTH_EAST);
        assertMoved(assertAndMerge(chris,lukas2,lukas),2,Direction.NORTH_EAST);
        //Lukas2 attacks E:135 HP:100
        for(int i = 0; i < 3; i++){
            assertActNow(assertAndMerge(chris,lukas,lukas2),2);
            lukas2.sendSinge(Direction.NORTH_EAST);
            assertSinged(assertAndMerge(chris,lukas2,lukas),2,Direction.NORTH_EAST); //Fairy HP:38
        }
        //Lukas2 passes
        assertActNow(assertAndMerge(chris,lukas,lukas2),2);
        lukas2.sendDoneActing();
        assertDoneActing(assertAndMerge(chris,lukas2,lukas),2);
        //Round ends
        assertRoundEnd(assertAndMerge(chris,lukas2,lukas),1,0);

        //Round2 starts-------------------------------------------------------------
        assertRoundBegin(assertAndMerge(chris,lukas2,lukas),2);

        //Fairy moves
        for(int i =0; i <3; i++){
            assertMoved(assertAndMerge(chris,lukas2,lukas),3,Direction.SOUTH_EAST);
        }
        assertMoved(assertAndMerge(chris,lukas2,lukas),3,Direction.NORTH_WEST);

        //Chris' turn
        //Chris moves E:900,HP:100
        assertActNow(assertAndMerge(chris,lukas,lukas2),1);
        chris.sendMove(Direction.WEST);
        assertMoved(assertAndMerge(chris,lukas2,lukas),1,Direction.WEST);
        //Chris swaps E:400, HP:100
        assertActNow(assertAndMerge(chris,lukas,lukas2),1);
        chris.sendSwap(3,2);
        assertSwapped(assertAndMerge(chris,lukas2,lukas),1,3,2);    //Fairy HP:32
        //Chris moves E:300,HP:100
        assertActNow(assertAndMerge(chris,lukas,lukas2),1);
        chris.sendMove(Direction.NORTH_WEST);
        assertMoved(assertAndMerge(chris,lukas2,lukas),1,Direction.NORTH_WEST);
        //Chris attacks E:50, HP:100
        assertActNow(assertAndMerge(chris,lukas,lukas2),1);
        chris.sendStab(Direction.NORTH_WEST);
        assertStabbed(assertAndMerge(chris,lukas2,lukas),1,Direction.NORTH_WEST);//Fairy HP:25
        //Chris passes
        assertActNow(assertAndMerge(chris,lukas,lukas2),1);
        chris.sendDoneActing();
        assertDoneActing(assertAndMerge(chris,lukas,lukas2),1);

        //Lukas2' turn
        //Lukas2 attacks E:235,HP:100
        for(int i = 0; i < 3; i++){
            assertActNow(assertAndMerge(chris,lukas,lukas2),2);
            lukas2.sendSinge(Direction.NORTH_EAST);
            assertSinged(assertAndMerge(chris,lukas2,lukas),2,Direction.NORTH_EAST); //Fairy HP:4
        }
        //Lukas2 passes
        assertActNow(assertAndMerge(chris,lukas,lukas2),2);
        lukas2.sendDoneActing();
        assertDoneActing(assertAndMerge(chris,lukas2,lukas),2);

        //Lukas' turn
        //Lukas attacks
        assertActNow(assertAndMerge(chris,lukas,lukas2),0);
        lukas.sendCut(Direction.EAST);
        assertCut(assertAndMerge(chris,lukas,lukas2),0,Direction.EAST);//Fairy HP:0
        assertDied(assertAndMerge(chris,lukas2,lukas),3);
        //Lukas kicked out
        assertActNow(assertAndMerge(chris,lukas,lukas2),0);
        lukas.sendSlash(Direction.SOUTH_WEST);
        assertKicked(assertAndMerge(chris,lukas2,lukas),0);

        //Round ends
        assertRoundEnd(assertAndMerge(chris,lukas2,lukas),2,0);

        //Round3 starts------------------------------------------------------------------------------
        assertRoundBegin(assertAndMerge(chris,lukas2,lukas),3);

        //Lukas2' turn
        //Lukas kicked out
        assertActNow(assertAndMerge(chris,lukas,lukas2),2);
        lukas2.sendSlash(Direction.WEST);
        assertKicked(assertAndMerge(chris,lukas2,lukas),2);

        //Chris turns
        //Chris passes
        assertActNow(assertAndMerge(chris,lukas,lukas2),1);
        chris.sendDoneActing();
        assertDoneActing(assertAndMerge(chris,lukas,lukas2),1);

        assertRoundEnd(assertAndMerge(chris,lukas2,lukas),3,0);
        assertWinner(assertAndMerge(chris,lukas2,lukas),"Blue");
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