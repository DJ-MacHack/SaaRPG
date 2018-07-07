package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Karla on 22.09.2016.
 * Boar kills someone and moves forward
 */
public class BoarKilledThenInvisibleTest extends OurSystemTest {

    private static final String FIGHTFILE =
                        "Red, Blue\n"
                    + "Red, Lukas, Wraith\n"
                    + "Blue, Chris, Yeti";
    //MAP
    private static final String MAPFILE = "X      01";

    public BoarKilledThenInvisibleTest(){
        super("Boar kills, then moves forward");
    }
    @Override
    protected void executeTest() {

        ClientConnection<Event> chris = register(0,"Chris",CreatureType.YETI,"Blue",8,0);
        ClientConnection<Event> lukas = register(1,"Lukas", CreatureType.WRAITH,"Red",7,0);

        assertRegisterEvent(chris.nextEvent(),1,"Lukas", CreatureType.WRAITH,"Red",7,0);
        assertRegisterEvent(lukas.nextEvent(),0,"Chris",CreatureType.YETI,"Blue",8,0);

        assertRoundBegin(assertAndMerge(chris,lukas),1);

        //Boar lives
        assertBoarSpawned(assertAndMerge(chris,lukas),2,0,0);
        for(int i = 0; i < 6; i++) {
            assertMoved(assertAndMerge(chris, lukas), 2, Direction.EAST);
        }
        //Chris passes
        assertActNow(assertAndMerge(chris,lukas),0);
        chris.sendDoneActing();
        assertDoneActing(assertAndMerge(chris,lukas),0);

        //Lukas E:1000, HP:4
        for(int i = 0; i < 16;i++ ){
            assertActNow(assertAndMerge(chris,lukas),1);
            lukas.sendSwap(7,0);
            assertSwapped(assertAndMerge(chris,lukas),1,7,0);
        }
        assertActNow(assertAndMerge(chris,lukas),1);
        lukas.sendDoneActing();
        assertDoneActing(assertAndMerge(chris,lukas),1);

        assertRoundEnd(assertAndMerge(chris,lukas),1,0);

        //Round 2 starts
        assertRoundBegin(assertAndMerge(chris,lukas),2);
        //Boar attacks
        assertBoarAttack(assertAndMerge(chris,lukas),2,1);
        assertDied(assertAndMerge(chris,lukas),1);
        for(int i = 0; i < 6;i++ ) {
            assertMoved(assertAndMerge(chris, lukas), 2, Direction.WEST);
        }
        //Chris passes
        assertActNow(assertAndMerge(chris,lukas),0);
        chris.sendDoneActing();
        assertDoneActing(assertAndMerge(chris,lukas),0);

        assertRoundEnd(assertAndMerge(chris,lukas),2,0);
        assertWinner(assertAndMerge(chris,lukas),"Blue");
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
