package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import static de.unisaarland.sopra.Direction.WEST;
import static de.unisaarland.sopra.Direction.EAST;

/**
 * Created by Lukas Kirschner on 9/16/16.
 *
 * Simple fight on a grass map 4x4, ELF against YETI (rather unfair?)
 */
public class FightSimpleGrassTest extends OurSystemTest {
    public FightSimpleGrassTest() {
        super("FightSimpleGrassTest");
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> cersei = register(0, "Cersei", CreatureType.ELF, "Lannister", 0, 0);
        ClientConnection<Event> eddard = register(1, "Eddard", CreatureType.YETI, "Stark", 3, 0);

        assertRegisterEvent(eddard.nextEvent(), 0, "Cersei", CreatureType.ELF, "Lannister", 0, 0);
        assertRegisterEvent(cersei.nextEvent(), 1, "Eddard", CreatureType.YETI, "Stark", 3, 0);

        //Everyone is registered now, there are neither boars nor fairies on the map --> Next expected event: RoundBegin
        assertRoundBegin(assertAndMerge(cersei, eddard), 1);
        //###STATUS Cersei: 100HP, Eddard: 200HP---------------------------------------

        //Round has begun --> It's Cersei's turn
        assertActNow(assertAndMerge(cersei, eddard), 0);

        //Cersei (1000) shoots Eddard
        cersei.sendShoot(EAST);

        assertShot(assertAndMerge(cersei, eddard),0,EAST);

        //Cersei (580) shoots again
        assertActNow(assertAndMerge(cersei, eddard), 0);
        cersei.sendShoot(EAST);

        assertShot(assertAndMerge(cersei, eddard),0,EAST);

        //Cersei (160) passes
        assertActNow(assertAndMerge(cersei, eddard), 0);
        cersei.sendDoneActing();

        assertDoneActing(assertAndMerge(cersei, eddard), 0);

        //It's Eddard's turn

        //Eddard (1000) moves west in order to reach Cersei
        assertActNow(assertAndMerge(cersei, eddard),1);
        eddard.sendMove(WEST);

        assertMoved(assertAndMerge(cersei, eddard),1, WEST);

        //Eddard(800) moves west again
        assertActNow(assertAndMerge(cersei, eddard),1);
        eddard.sendMove(WEST);

        assertMoved(assertAndMerge(cersei, eddard),1, WEST);

        //Eddard (600) crushes Cersei
        assertActNow(assertAndMerge(cersei, eddard),1);
        eddard.sendCrush(WEST);

        assertCrush(assertAndMerge(cersei, eddard),1, WEST);

        //Eddard (140) passes
        assertActNow(assertAndMerge(cersei, eddard),1);
        eddard.sendDoneActing();

        assertDoneActing(assertAndMerge(cersei, eddard),1);

        //Round 1 finished
        assertRoundEnd(assertAndMerge(cersei, eddard),1,0);

        assertRoundBegin(assertAndMerge(cersei, eddard),2);
        //###STATUS: Cersei: 84HP, Eddard: 178HP-------------------------------------

        //It's Cersei's turn

        //Cersei (1000) stabs Eddard four times
        for (int i = 0; i < 4; i++){
            assertActNow(assertAndMerge(cersei, eddard),0);
            cersei.sendStab(EAST);
            assertStabbed(assertAndMerge(cersei, eddard),0,EAST);
        }

        //Cersei (0) passes
        assertActNow(assertAndMerge(cersei, eddard),0);
        cersei.sendDoneActing();

        assertDoneActing(assertAndMerge(cersei, eddard),0);

        //It's Eddard's turn
        assertActNow(assertAndMerge(cersei, eddard),1);

        //Eddard (1000) crushes Cersei twice
        eddard.sendCrush(WEST);

        assertCrush(assertAndMerge(cersei, eddard),1, WEST);

        assertActNow(assertAndMerge(cersei, eddard),1);
        eddard.sendCrush(WEST);

        assertCrush(assertAndMerge(cersei, eddard),1, WEST);

        //Eddard (80) passes
        assertActNow(assertAndMerge(cersei, eddard),1);
        eddard.sendDoneActing();

        assertDoneActing(assertAndMerge(cersei, eddard),1);

        //Round 2 finished
        assertRoundEnd(assertAndMerge(cersei, eddard),2,0);

        assertRoundBegin(assertAndMerge(cersei, eddard),3);
        //###STATUS: Eddard: 150HP, Cersei: 52HP-----------------------------------------

        //It's Eddard's turn
        assertActNow(assertAndMerge(cersei, eddard),1);

        //Eddard (1000) crushes Cersei twice
        eddard.sendCrush(WEST);

        assertCrush(assertAndMerge(cersei, eddard),1, WEST);

        assertActNow(assertAndMerge(cersei, eddard),1);
        eddard.sendCrush(WEST);

        assertCrush(assertAndMerge(cersei, eddard),1, WEST);

        //Eddard (80) passes
        assertActNow(assertAndMerge(cersei, eddard),1);
        eddard.sendDoneActing();

        assertDoneActing(assertAndMerge(cersei, eddard),1);

        //It's Cersei's turn

        //Cersei (1000) stabs Eddard four times
        for (int i = 0; i < 4; i++){
            assertActNow(assertAndMerge(cersei, eddard),0);
            cersei.sendStab(EAST);
            assertStabbed(assertAndMerge(cersei, eddard),0,EAST);
        }

        //Cersei (0) passes
        assertActNow(assertAndMerge(cersei, eddard),0);
        cersei.sendDoneActing();

        assertDoneActing(assertAndMerge(cersei, eddard),0);

        //Finished Round 3
        assertRoundEnd(assertAndMerge(cersei, eddard),3,0);

        assertRoundBegin(assertAndMerge(cersei, eddard),4);
        //###STATUS: Eddard: 122HP, Cersei: 20HP----------------------------------------

        //It's Eddard's turn
        assertActNow(assertAndMerge(cersei, eddard),1);

        //Eddard (1000) crushes Cersei twice
        eddard.sendCrush(WEST);

        assertCrush(assertAndMerge(cersei, eddard),1, WEST);

        assertActNow(assertAndMerge(cersei, eddard),1);
        eddard.sendCrush(WEST);

        assertCrush(assertAndMerge(cersei, eddard),1, WEST);

        //Cersei has 0 HP and dies
        assertDied(assertAndMerge(cersei, eddard),0);

        assertActNow(assertAndMerge(cersei, eddard),1);
        eddard.sendDoneActing();

        assertDoneActing(assertAndMerge(cersei, eddard),1);

        assertRoundEnd(assertAndMerge(cersei, eddard),4,0);

        //Eddard wins
        assertWinner(assertAndMerge(cersei, eddard),"Stark");
    }

    @Override
    protected String getMapFile() {
        return "0  1\n    \n    \n    ";
    }

    @Override
    protected String getFightFile() {
        return "Lannister, Stark\n\n\nLannister, Cersei, Elf\nStark, Eddard, Yeti";
    }
}
