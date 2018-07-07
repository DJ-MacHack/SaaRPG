package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;
import org.junit.Assert;

/**
 * Created by Karla on 16.09.2016.
 */
public class BushFieldEffectTest extends OurSystemTest {

    private static final String MAPFILE =
            "0xx\n"
            +"x  \n"
            +"1 x";

    private static final String FIGHTFILE =
            "Snow, Bolton\n"
            +"Snow, John, Kobold\n"
            +"Bolton, Ramsay, Ifrit";


    public BushFieldEffectTest(){
        super("FieldEffects Bush Test");
    }
    @Override
    protected void executeTest() {
        //Register
        ClientConnection<Event> john = register(0,"John", CreatureType.KOBOLD, "Snow",0,0);
        ClientConnection<Event> ramsay = register(1, "Ramsay", CreatureType.IFRIT, "Bolton", -1,2);

        assertRegisterEvent(ramsay.nextEvent(),0,"John", CreatureType.KOBOLD, "Snow",0,0);
        assertRegisterEvent(john.nextEvent(),1,"Ramsay",CreatureType.IFRIT,"Bolton",-1,2);

        //Everyone is Registered, no Boars or Fairies in Map---> RoundBeginn
        //Ramsay: HP:87, John: HP:100
        assertRoundBegin(assertAndMerge(john,ramsay),1);

        //John's turn E: 1000, HP:100
        assertActNow(assertAndMerge(john,ramsay),0);
        // John moves E:900, HP :100
        john.sendMove(Direction.SOUTH_EAST);
        assertMoved(assertAndMerge(john,ramsay),0,Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(john,ramsay),0);
        //John attacks E:480, HP:100
        john.sendSlash(Direction.SOUTH_WEST);
        assertSlashed(assertAndMerge(john,ramsay),0,Direction.SOUTH_WEST);      //Ramsay HP:75
        assertActNow(assertAndMerge(john,ramsay),0);
        //John attacks again E:60, HP: 100
        john.sendSlash(Direction.SOUTH_WEST);
        assertSlashed(assertAndMerge(john,ramsay),0,Direction.SOUTH_WEST);      //Ramsay HP:63
        assertActNow(assertAndMerge(john,ramsay),0);
        // John Passes
        john.sendDoneActing();
        assertDoneActing(assertAndMerge(john,ramsay),0);

        //Ramsay's Turn E:1000, HP: 63
        assertActNow(assertAndMerge(john,ramsay),1);
        //Ramsay attacks E:550, HP:63
        ramsay.sendBurn();
        assertBurned(assertAndMerge(john,ramsay),1);                            //John HP:94
        assertActNow(assertAndMerge(john,ramsay),1);
        //Ramsay attacks E:295, HP:63
        ramsay.sendSinge(Direction.NORTH_EAST);
        assertSinged(assertAndMerge(john,ramsay),1,Direction.NORTH_EAST);       //John HP: 91
        assertActNow(assertAndMerge(john,ramsay),1);
        //Ramsay attacks E:40, HP:63
        ramsay.sendSinge(Direction.NORTH_EAST);
        assertSinged(assertAndMerge(john,ramsay),1,Direction.NORTH_EAST);       //John HP: 88
        assertActNow(assertAndMerge(john,ramsay),1);
        //Ramsay passes
        ramsay.sendDoneActing();
        assertDoneActing(assertAndMerge(john,ramsay),1);

        //Round 1 done
        assertRoundEnd(assertAndMerge(john,ramsay),1,0);

        //Round 2 starts-------------------------------------------------
        assertRoundBegin(assertAndMerge(john,ramsay),2);

        //John's turn E:1000, HP: 88
        assertActNow(assertAndMerge(john,ramsay),0);
        //John attacks E:750, HP:88
        john.sendStab(Direction.SOUTH_WEST);
        assertStabbed(assertAndMerge(john,ramsay),0,Direction.SOUTH_WEST);      //Ramsay:56
        assertActNow(assertAndMerge(john,ramsay),0);
        //John attacks E:500, HP:88
        john.sendStab(Direction.SOUTH_WEST);
        assertStabbed(assertAndMerge(john,ramsay),0,Direction.SOUTH_WEST);      //Ramsay:49
        assertActNow(assertAndMerge(john,ramsay),0);
        //John attacks E:250, HP:88
        john.sendStab(Direction.SOUTH_WEST);
        assertStabbed(assertAndMerge(john,ramsay),0,Direction.SOUTH_WEST);      //Ramsay:42
        assertActNow(assertAndMerge(john,ramsay),0);
        //John attacks E:0, HP:88
        john.sendStab(Direction.SOUTH_WEST);
        assertStabbed(assertAndMerge(john,ramsay),0,Direction.SOUTH_WEST);      //Ramsay:35
        assertActNow(assertAndMerge(john,ramsay),0);
        //John passes
        john.sendDoneActing();
        assertDoneActing(assertAndMerge(john,ramsay),0);

        //Ramsay's turn E:1000 HP:35
        assertActNow(assertAndMerge(john, ramsay),1);
        //Ramsay attacks E:550, HP:35
        ramsay.sendBurn();
        assertBurned(assertAndMerge(john,ramsay),1);                            //John HP:82
        assertActNow(assertAndMerge(john,ramsay),1);
        //Ramsay attacks E:100, HP:35
        ramsay.sendBurn();
        assertBurned(assertAndMerge(john,ramsay),1);                            //John HP:74
        assertActNow(assertAndMerge(john,ramsay),1);
        //Ramsay passes
        ramsay.sendDoneActing();
        assertDoneActing(assertAndMerge(john,ramsay),1);

        //Round 1 done
        assertRoundEnd(assertAndMerge(john,ramsay),2,0);

        //Round 3 starts-------------------------------------------------
        assertRoundBegin(assertAndMerge(john,ramsay),3);

        //Ramsay's turn E:1000 HP:35
        assertActNow(assertAndMerge(john, ramsay),1);
        //Ramsay attacks E:550, HP:35
        ramsay.sendBurn();
        assertBurned(assertAndMerge(john,ramsay),1);                            //John HP:68
        assertActNow(assertAndMerge(john,ramsay),1);
        //Ramsay attacks E:100, HP:35
        ramsay.sendBurn();
        assertBurned(assertAndMerge(john,ramsay),1);                            //John HP:62
        assertActNow(assertAndMerge(john,ramsay),1);
        //Ramsay moves E:0, HP: 35
        ramsay.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(john,ramsay),1,Direction.EAST);
        assertActNow(assertAndMerge(john,ramsay),1);
        //Ramsay passes
        ramsay.sendDoneActing();
        assertDoneActing(assertAndMerge(john,ramsay),1);

        //John's turn E:1000, HP: 62
        assertActNow(assertAndMerge(john,ramsay),0);
        //John attacks E:580, HP:62
        john.sendSlash(Direction.SOUTH_EAST);
        assertSlashed(assertAndMerge(john,ramsay),0,Direction.SOUTH_EAST);      //Ramsay HP: 23
        assertActNow(assertAndMerge(john,ramsay),0);
        //John attacks E:330, HP:62
        john.sendStab(Direction.SOUTH_EAST);
        assertStabbed(assertAndMerge(john,ramsay),0,Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(john,ramsay),0);
        //John attacks E:80, HP:62
        john.sendStab(Direction.SOUTH_EAST);
        assertStabbed(assertAndMerge(john,ramsay),0,Direction.SOUTH_EAST);      //Ramsay HP:9
        assertActNow(assertAndMerge(john,ramsay),0);
        //John passes
        john.sendDoneActing();
        assertDoneActing(assertAndMerge(john,ramsay),0);



        //Round 3 ends
        assertRoundEnd(assertAndMerge(john,ramsay),3,0);

        //Round 4 starts------------------------------------------------------------
        assertRoundBegin(assertAndMerge(john,ramsay),4);

        //John starts
        assertActNow(assertAndMerge(john,ramsay),0);
        //John attacks E:1000, HP:62
        john.sendSlash(Direction.SOUTH_EAST);
        assertSlashed(assertAndMerge(john,ramsay),0,Direction.SOUTH_EAST);      //Ramsay 0
        assertDied(assertAndMerge(john,ramsay),1);
        assertActNow(assertAndMerge(john,ramsay),0);
        //Ramsay dies
        john.sendDoneActing();
        assertDoneActing(assertAndMerge(john,ramsay),0);

        assertRoundEnd(assertAndMerge(john,ramsay),4,0);
        //John wins
        assertWinner(assertAndMerge(john,ramsay),"Snow");
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
