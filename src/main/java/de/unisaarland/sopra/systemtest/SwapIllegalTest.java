package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Lukas Kirschner on 9/19/16.
 * This test checks if only Wraith has access to the Swap attacktest - Please create an instance of this test for every other creature.
 */
public class SwapIllegalTest extends OurSystemTest {
    private CreatureType t;

    public SwapIllegalTest(CreatureType t) {
        super("SwapIllegalTest" + t.toString());
        this.t = t;
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> legalCreature = register(0, "Boss", CreatureType.WRAITH, "TeamLegal", 0, 0);
        ClientConnection<Event> illegalCreature = register(1, "ein" + t.toString(), t, "TeamIllegal", 1, 0);
        assertRegisterEvent(illegalCreature.nextEvent(), 0, "Boss", CreatureType.WRAITH, "TeamLegal", 0, 0);
        assertRegisterEvent(legalCreature.nextEvent(), 1, "ein" + t.toString(), t, "TeamIllegal", 1, 0);

        //Round 1 begins
        assertRoundBegin(assertAndMerge(legalCreature, illegalCreature), 1);

        //legalCreature is on turn
        assertActNow(assertAndMerge(legalCreature, illegalCreature), 0);

        legalCreature.sendSwap(1, 0); //LegalCreature swaps with IllegalCreature
        assertSwapped(assertAndMerge(legalCreature, illegalCreature), 0, 1, 0);

        assertActNow(assertAndMerge(legalCreature, illegalCreature), 0);
        legalCreature.sendDoneActing();
        assertDoneActing(assertAndMerge(legalCreature, illegalCreature), 0);

        assertActNow(assertAndMerge(legalCreature, illegalCreature), 1);
        illegalCreature.sendSwap(1, 0); //IllegalCreature tries to swap back
        assertKicked(assertAndMerge(legalCreature, illegalCreature), 1);

        assertRoundEnd(assertAndMerge(legalCreature, illegalCreature), 1, 0);
        assertWinner(assertAndMerge(legalCreature, illegalCreature), "TeamLegal");
    }

    @Override
    protected String getMapFile() {
        return "01\n  ";
    }

    @Override
    protected String getFightFile() {
        return "TeamLegal, TeamIllegal\nTeamLegal, Boss, Wraith\nTeamIllegal, ein" + t.toString() + ", " + t.toString();
    }
}
