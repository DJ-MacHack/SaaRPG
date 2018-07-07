package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.messages.RoundBegin;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Karla on 19.09.2016.
 */
public class TreeFieldEffectTest extends OurSystemTest {
    private static final String MAPFILE =
            " tt0t\n"
                    + "tttxx\n"
                    + "  xxx\n"
                    + "xxxxx\n"
                    + "xx  x\n"
                    + "ttttx\n"
                    + "~~~~x\n"
                    + "~~~~x\n"
                    + "1   x";

    private static final String FIGHTFILE = "Targaryen, Lannister \n"
                    + "Targaryen, Daenerys, Elf\n"
                    + "Lannister, Cersei, Naga";

    public TreeFieldEffectTest() {
        super("TreeTest");
    }

    @Override
    protected void executeTest() {

        ClientConnection<Event> dany = register(0, "Daenerys", CreatureType.ELF, "Targaryen", 3, 0);
        ClientConnection<Event> cersei = register(1, "Cersei", CreatureType.NAGA, "Lannister", -4, 8);

        assertRegisterEvent(cersei.nextEvent(), 0, "Daenerys", CreatureType.ELF, "Targaryen", 3, 0);
        assertRegisterEvent(dany.nextEvent(), 1, "Cersei", CreatureType.NAGA, "Lannister", -4, 8);

        assertRoundBegin(assertAndMerge(dany, cersei), 1);

        //Everybody is registered, Dany: HP:100, Cersei: HP:100

        //Dany's turn
        assertActNow(assertAndMerge(dany, cersei), 0);
        dany.sendWarCry("I am Daenerys Stormborn, of House Targaryen. " +
                "Rightful heir to the Iron Throne, Queen of the Seven Kingdoms of Westeros!");
        assertWarCry(assertAndMerge(dany, cersei), 0, "I am Daenerys Stormborn, of House Targaryen. " +
                "Rightful heir to the Iron Throne, Queen of the Seven Kingdoms of Westeros!");
        //Dany moves to a Tree Field E:900, HP:100
        assertActNow(assertAndMerge(dany, cersei), 0);
        dany.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(dany, cersei), 0, Direction.EAST);
        //Dany attacks E:60, HP:100
        assertActNow(assertAndMerge(dany, cersei), 0);
        dany.sendShoot(Direction.SOUTH_WEST);
        assertShot(assertAndMerge(dany, cersei), 0, Direction.SOUTH_WEST);         //Cersei HP:89
        //Dany passes
        assertActNow(assertAndMerge(dany, cersei), 0);
        dany.sendDoneActing();
        assertDoneActing(assertAndMerge(dany, cersei), 0);

        //Cersei's turn
        //Cersei E:1000, HP:89
        assertActNow(assertAndMerge(dany, cersei), 1);
        cersei.sendWarCry("The Lannisters send their regards!");
        assertWarCry(assertAndMerge(dany, cersei), 1, "The Lannisters send their regards!");
        //Cersei moves 5 Fields
        for (int i = 0; i < 5; i++) {
            assertActNow(assertAndMerge(dany, cersei), 1);
            cersei.sendMove(Direction.NORTH_EAST);//2xWater, Tree, Grass, 2xBush
            assertMoved(assertAndMerge(dany, cersei), 1, Direction.NORTH_EAST);
        }
        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(dany, cersei), 1);
            cersei.sendMove(Direction.NORTH_WEST); //Bush
            assertMoved(assertAndMerge(dany, cersei), 1, Direction.NORTH_WEST);
        }
        //Cersei E: 10, HP: 89 Passes
        assertActNow(assertAndMerge(dany, cersei), 1);
        cersei.sendDoneActing();
        assertDoneActing(assertAndMerge(dany, cersei), 1);
        //Round 1 ends
        assertRoundEnd(assertAndMerge(dany, cersei), 1, 0);

        //Round 2 Starts--------------------------------------------

        assertRoundBegin(assertAndMerge(cersei, dany), 2);

        //Dany's Turn E:900, HP:100
        assertActNow(assertAndMerge(dany, cersei), 0);
        dany.sendMove(Direction.SOUTH_EAST);
        assertMoved(assertAndMerge(dany, cersei), 0, Direction.SOUTH_EAST);
        //Dany attacks E:60, HP:100
        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(dany, cersei), 0);
            dany.sendShoot(Direction.WEST);
            assertShot(assertAndMerge(dany, cersei), 0, Direction.WEST); //Cersei: HP:75
        }
        //Dany passes
        assertActNow(assertAndMerge(dany, cersei), 0);
        dany.sendDoneActing();
        assertDoneActing(assertAndMerge(dany, cersei), 0);

        //Cersei's turn
        //Cersei E:780, HP:75
        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(dany, cersei), 1);
            cersei.sendMove(Direction.EAST);
            assertMoved(assertAndMerge(dany, cersei), 1, Direction.EAST);
        }
        //Cersei attacks E:430, HP:75
        assertActNow(assertAndMerge(dany, cersei), 1);
        cersei.sendBite(Direction.EAST);
        assertBitten(assertAndMerge(dany, cersei), 1, Direction.EAST);      //Dany: HP:94**

        //Cersei attacks E:30 HP:75
        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(dany, cersei), 1);
            cersei.sendCut(Direction.EAST);
            assertCut(assertAndMerge(dany, cersei), 1, Direction.EAST);     //Dany HP:84**
        }
        //Cersei passes
        assertActNow(assertAndMerge(dany, cersei), 1);
        cersei.sendDoneActing();
        assertDoneActing(assertAndMerge(dany, cersei), 1);
        //Round ends
        assertRoundEnd(assertAndMerge(cersei, dany), 2, 0);

        //Round 3 starts-----------------------------------------------
        assertRoundBegin(assertAndMerge(dany, cersei), 3);

        //Dany's turn E:1000, HP:84**
        //Dany attacks E:250, HP: 84**
        for (int i = 0; i < 3; i++) {
            assertActNow(assertAndMerge(dany, cersei), 0);
            dany.sendStab(Direction.WEST);
            assertStabbed(assertAndMerge(dany, cersei), 0, Direction.WEST);//Cersei HP:54
        }
        //Dany moves E:50, HP:84**
        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(dany, cersei), 0);
            dany.sendMove(Direction.SOUTH_WEST);
            assertMoved(assertAndMerge(dany, cersei), 0, Direction.SOUTH_WEST);
        }
        //Dany passes
        assertActNow(assertAndMerge(dany, cersei), 0);
        dany.sendDoneActing();
        assertDoneActing(assertAndMerge(dany, cersei), 0);

        //Cersei's turn
        //Cersei moves E:890,HP:54
        assertActNow(assertAndMerge(dany, cersei), 1);
        cersei.sendMove(Direction.SOUTH_EAST);
        assertMoved(assertAndMerge(dany, cersei), 1, Direction.SOUTH_EAST);
        //Cersei Attacks E:90, HP:54
        for (int i = 0; i < 4; i++) {
            assertActNow(assertAndMerge(dany, cersei), 1);
            cersei.sendCut(Direction.SOUTH_WEST);
            assertCut(assertAndMerge(dany, cersei), 1, Direction.SOUTH_WEST);//Dany HP: 64**
        }
        //Cersei Passes
        assertActNow(assertAndMerge(dany,cersei),1);
        cersei.sendDoneActing();
        assertDoneActing(assertAndMerge(dany,cersei),1);
        //PoisonEffect
        assertPoisonEffect(assertAndMerge(dany,cersei),0,4);        //Dany HP:60*

        //Round 3 ends
        assertRoundEnd(assertAndMerge(dany,cersei),3,0);

        //Round 4 begins -------------------------------------------
        assertRoundBegin(assertAndMerge(dany,cersei),4);

        //Cersei's turn
        assertActNow(assertAndMerge(dany, cersei), 1);
        cersei.sendWarCry("Hear me roar!");
        assertWarCry(assertAndMerge(dany, cersei), 1, "Hear me roar!");
        //Cersei attacks E:0 HP:54
        for(int i = 0; i < 5; i++){
            assertActNow(assertAndMerge(dany,cersei),1);
            cersei.sendCut(Direction.SOUTH_WEST);
            assertCut(assertAndMerge(dany,cersei),1, Direction.SOUTH_WEST); //Dany HP:35*
        }
        //Cersei passes
        assertActNow(assertAndMerge(dany,cersei),1);
        cersei.sendDoneActing();
        assertDoneActing(assertAndMerge(dany,cersei),1);

        //Dany's turn
        assertActNow(assertAndMerge(dany, cersei), 0);
        dany.sendWarCry("I am Daenerys Stormborn and I will take what is mine with fire and blood.");
        assertWarCry(assertAndMerge(dany, cersei), 0, "I am Daenerys Stormborn and I will take what is mine with fire and blood.");
        //Dany attacks E:580, HP: 35*
        assertActNow(assertAndMerge(dany, cersei), 0);
        dany.sendShoot(Direction.NORTH_EAST);
        assertShot(assertAndMerge(dany,cersei),0,Direction.NORTH_EAST);     //Cersei HP:43
        //Dany attacks E:80, HP: 35*
        for(int i = 0; i < 2;i++) {
            assertActNow(assertAndMerge(dany, cersei), 0);
            dany.sendStab(Direction.NORTH_EAST);
            assertStabbed(assertAndMerge(dany,cersei),0, Direction.NORTH_EAST); //Cersei HP:29
        }
        //Dany passes
        assertActNow(assertAndMerge(dany, cersei), 0);
        dany.sendDoneActing();
        assertDoneActing(assertAndMerge(dany, cersei), 0);
        //PoisonEffect
        assertPoisonEffect(assertAndMerge(dany,cersei),0,2);        //Dany HP: 33
        //Round ends
        assertRoundEnd(assertAndMerge(dany,cersei),4,0);

        //Round 5 starts----------------------------------------------------
        assertRoundBegin(assertAndMerge(dany,cersei),5);

        //Dany'S turn
        //Dany attacks E:250, HP:33
        for(int i = 0; i < 3;i++) {
            assertActNow(assertAndMerge(dany, cersei), 0);
            dany.sendStab(Direction.NORTH_EAST);
            assertStabbed(assertAndMerge(dany,cersei),0, Direction.NORTH_EAST); //Cersei HP:8
        }
        //Dany passes
        assertActNow(assertAndMerge(dany, cersei), 0);
        dany.sendDoneActing();
        assertDoneActing(assertAndMerge(dany, cersei), 0);

        //Cersei's turn
        //Cersei attacks E:0, HP: 8
        for(int i = 0; i < 5; i++){
            assertActNow(assertAndMerge(dany,cersei),1);
            cersei.sendCut(Direction.SOUTH_WEST);
            assertCut(assertAndMerge(dany,cersei),1, Direction.SOUTH_WEST); //Dany HP:8
        }
        //Cersei passes
        assertActNow(assertAndMerge(dany,cersei),1);
        cersei.sendDoneActing();
        assertDoneActing(assertAndMerge(dany,cersei),1);
        //Round ends
        assertRoundEnd(assertAndMerge(dany,cersei),5,0);

        //Round 6 starts --------------------------------------------------------
        assertRoundBegin(assertAndMerge(dany,cersei),6);
        //Dany's turn
        //Dany attacks E:580, HP:8
        assertActNow(assertAndMerge(dany, cersei), 0);
        dany.sendShoot(Direction.NORTH_EAST);
        assertShot(assertAndMerge(dany,cersei),0,Direction.NORTH_EAST); //Cersei dies
        assertDied(assertAndMerge(dany,cersei),1);

        assertActNow(assertAndMerge(dany, cersei), 0);
        dany.sendDoneActing();
        assertDoneActing(assertAndMerge(dany,cersei),0);

        assertRoundEnd(assertAndMerge(dany,cersei),6,0);

        assertWinner(assertAndMerge(dany,cersei),"Targaryen");
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
