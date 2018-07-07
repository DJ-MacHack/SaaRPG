package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Karla on 23.09.2016.
 */
public class BurnOneDiedTest extends OurSystemTest {

    private static final String MAPFILE =
                     "0xx\n"
                    +"1 x";

    private static final String FIGHTFILE =
                    "Snow, Bolton\n"
                    +"Snow, John, Wraith\n"
                    +"Bolton, Ramsay, Ifrit";

    public BurnOneDiedTest(){
        super("Burn kills only one");
    }
    @Override
    protected void executeTest() {
        ClientConnection<Event> john = register(0,"John", CreatureType.WRAITH, "Snow",0,0);
        ClientConnection<Event> ramsay = register(1, "Ramsay", CreatureType.IFRIT, "Bolton", 0,1);

        assertRegisterEvent(ramsay.nextEvent(),0,"John", CreatureType.WRAITH, "Snow",0,0);
        assertRegisterEvent(john.nextEvent(),1,"Ramsay",CreatureType.IFRIT,"Bolton",0,1);

        assertRoundBegin(assertAndMerge(john,ramsay),1);
        //John's Turn
        //John attacks itself E:1000 HP:10
        for(int i = 0; i < 15; i++) {
            assertActNow(assertAndMerge(john, ramsay), 0);
            john.sendSwap(0, 0);
            assertSwapped(assertAndMerge(john, ramsay), 0, 0, 0);
        }
        assertActNow(assertAndMerge(john, ramsay), 0);
        john.sendDoneActing();
        assertDoneActing(assertAndMerge(john,ramsay),0);

        //Ramsay's turn
        //Ramsay attacks E:550, HP:87
        assertActNow(assertAndMerge(john, ramsay), 1);
        ramsay.sendBurn();
        assertBurned(assertAndMerge(john,ramsay),1);
        assertDied(assertAndMerge(john,ramsay),0);

        assertActNow(assertAndMerge(john, ramsay), 1);
        ramsay.sendDoneActing();
        assertDoneActing(assertAndMerge(john,ramsay),1);

        assertRoundEnd(assertAndMerge(john,ramsay),1,0);

        assertWinner(assertAndMerge(john,ramsay),"Bolton");
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
