package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Lukas Kirschner on 9/19/16.
 * This test checks if only Ifrit has access to the Burn attacktest - Please create an instance of this test for every other creature.
 */
public class BurnIllegalTest extends OurSystemTest {
    private CreatureType t;

    public BurnIllegalTest(CreatureType t) {
        super("BurnIllegalTest" + t.toString());
        this.t = t;
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> legalCreature = register(0, "Fuchur", CreatureType.IFRIT, "TeamLegal", 0, 0);
        ClientConnection<Event> illegalCreature = register(1, "ein" + t.toString(), t, "TeamIllegal", 1, 0);
        assertRegisterEvent(illegalCreature.nextEvent(), 0, "Fuchur", CreatureType.IFRIT, "TeamLegal", 0, 0);
        assertRegisterEvent(legalCreature.nextEvent(), 1, "ein" + t.toString(), t, "TeamIllegal", 1, 0);

        //Round 1 begins
        assertRoundBegin(assertAndMerge(legalCreature, illegalCreature), 1);

        //legalCreature is on turn
        assertActNow(assertAndMerge(legalCreature, illegalCreature), 0);

        legalCreature.sendBurn(); //LegalCreature burns IllegalCreature
        assertBurned(assertAndMerge(legalCreature, illegalCreature), 0);

        assertActNow(assertAndMerge(legalCreature, illegalCreature), 0);
        legalCreature.sendDoneActing();
        assertDoneActing(assertAndMerge(legalCreature, illegalCreature), 0);

        assertActNow(assertAndMerge(legalCreature, illegalCreature), 1);
        illegalCreature.sendBurn(); //IllegalCreature tries to burn back
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
        return "TeamLegal, TeamIllegal\nTeamLegal, Fuchur, IFRIT\nTeamIllegal, ein" + t.toString() + ", " + t.toString();
    }
}
