package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Lukas Kirschner on 9/19/16.
 * This test checks if only Yeti has access to the Claw attacktest - Please create an instance of this test for every other creature.
 */
public class CrushIllegalTest extends OurSystemTest {
    private CreatureType t;

    public CrushIllegalTest(CreatureType t) {
        super("CrushIllegalTest" + t.toString());
        this.t = t;
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> legalCreature = register(0, "Trololo", CreatureType.YETI, "TeamLegal", 0, 0);
        ClientConnection<Event> illegalCreature = register(1, "ein" + t.toString(), t, "TeamIllegal", 1, 0);
        assertRegisterEvent(illegalCreature.nextEvent(), 0, "Trololo", CreatureType.YETI, "TeamLegal", 0, 0);
        assertRegisterEvent(legalCreature.nextEvent(), 1, "ein" + t.toString(), t, "TeamIllegal", 1, 0);

        //Round 1 begins
        assertRoundBegin(assertAndMerge(legalCreature, illegalCreature), 1);

        //legalCreature is on turn
        assertActNow(assertAndMerge(legalCreature, illegalCreature), 0);

        legalCreature.sendCrush(Direction.EAST); //LegalCreature crushes IllegalCreature
        assertCrush(assertAndMerge(legalCreature, illegalCreature), 0, Direction.EAST);

        assertActNow(assertAndMerge(legalCreature, illegalCreature), 0);
        legalCreature.sendDoneActing();
        assertDoneActing(assertAndMerge(legalCreature, illegalCreature), 0);

        assertActNow(assertAndMerge(legalCreature, illegalCreature), 1);
        illegalCreature.sendCrush(Direction.WEST); //IllegalCreature tries to crush back
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
        return "TeamLegal, TeamIllegal\nTeamLegal, Trololo, Yeti\nTeamIllegal, ein" + t.toString() + ", " + t.toString();
    }
}
