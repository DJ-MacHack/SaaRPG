package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by DJ MacHack on 23.09.2016.
 */
public class CrushOnBoar extends OurSystemTest {

    public CrushOnBoar() {
        super("CrushOnBoar");
    }
    @Override
    protected void executeTest() {
        ClientConnection<Event> legalCreature = register(0, "Grima", CreatureType.YETI, "TeamLegal", 0, 0);
        ClientConnection<Event> illegalCreature = register(1, "Gamma", CreatureType.KOBOLD, "TeamIllegal", 2, 0);
        assertRegisterEvent(illegalCreature.nextEvent(), 0, "Grima", CreatureType.YETI, "TeamLegal", 0, 0);
        assertRegisterEvent(legalCreature.nextEvent(), 1, "Gamma", CreatureType.KOBOLD, "TeamIllegal", 2, 0);
        //Round 1 begins
        assertRoundBegin(assertAndMerge(legalCreature, illegalCreature), 1);
        assertBoarSpawned(assertAndMerge(legalCreature, illegalCreature),2,1,0);
        assertBoarAttack(assertAndMerge(legalCreature, illegalCreature),2,1);
        assertBoarAttack(assertAndMerge(legalCreature, illegalCreature),2,0);

        //legalCreature is on turn
        assertActNow(assertAndMerge(legalCreature, illegalCreature), 0);
        legalCreature.sendCrush(Direction.EAST); //LegalCreature crushs Boar
        assertCrush(assertAndMerge(legalCreature, illegalCreature), 0, Direction.EAST);
        assertActNow(assertAndMerge(legalCreature, illegalCreature), 0);
        legalCreature.sendCrush(Direction.EAST); //LegalCreature crushs Boar
        assertCrush(assertAndMerge(legalCreature, illegalCreature), 0, Direction.EAST);
        assertDied(assertAndMerge(legalCreature,illegalCreature),2);
        assertActNow(assertAndMerge(legalCreature, illegalCreature), 0);
        legalCreature.sendDoneActing();
        assertDoneActing(assertAndMerge(legalCreature, illegalCreature), 0);
        assertActNow(assertAndMerge(legalCreature, illegalCreature), 1);
        illegalCreature.sendCut(Direction.WEST); //IllegalCreature tries to cut back
        assertKicked(assertAndMerge(legalCreature, illegalCreature), 1);

        assertRoundEnd(assertAndMerge(legalCreature, illegalCreature), 1, 0);
        assertWinner(assertAndMerge(legalCreature, illegalCreature), "TeamLegal");
    }

    @Override
    protected String getMapFile() {
        return "0A1";
    }

    @Override
    protected String getFightFile() {
        return "TeamLegal, TeamIllegal\nTeamLegal, Grima, YETI\nTeamIllegal, Gamma, Kobold";
    }
}
