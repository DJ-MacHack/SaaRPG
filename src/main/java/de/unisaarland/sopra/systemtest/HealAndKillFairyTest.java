package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Lukas Kirschner on 9/19/16.
 */
public class HealAndKillFairyTest extends OurSystemTest {

    public HealAndKillFairyTest() {
        super("HealAndKillFairyTests");
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> bomber = register(0, "Bomber", CreatureType.WRAITH, "TeamSuicide", 0, 0);
        ClientConnection<Event> troll = register(1, "Troll", CreatureType.WRAITH, "TeamTroll", 14, 0);
        assertRegisterEvent(troll.nextEvent(), 0, "Bomber", CreatureType.WRAITH, "TeamSuicide", 0, 0);
        assertRegisterEvent(bomber.nextEvent(), 1, "Troll", CreatureType.WRAITH, "TeamTroll", 14, 0);

        assertFairySpawned(assertAndMerge(troll, bomber), 2, 1, 0);

        // Round 1
        assertRoundBegin(assertAndMerge(troll, bomber), 1);

        for (int i = 0; i < 4; i++) {
            assertMoved(assertAndMerge(troll, bomber), 2, Direction.EAST);
        } //New coord: 5,0

        assertActNow(assertAndMerge(troll, bomber), 0);
        bomber.sendMove(Direction.EAST); //Bomber moves onto the heal tile
        assertMoved(assertAndMerge(troll, bomber), 0, Direction.EAST);
        assertActNow(assertAndMerge(troll, bomber), 0);

        bomber.sendSwap(1, 0); //Bomber swaps with himself, draining 0 energy and 6 HP
        assertSwapped(assertAndMerge(troll, bomber), 0, 1, 0);
        assertActNow(assertAndMerge(troll, bomber), 0);
        bomber.sendDoneActing();
        assertDoneActing(assertAndMerge(troll, bomber), 0);
        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(troll, bomber), 1);
            troll.sendMove(Direction.WEST);
            assertMoved(assertAndMerge(troll, bomber), 1, Direction.WEST); //New Coord Troll: 12,0
        }
        assertActNow(assertAndMerge(troll, bomber), 1);
        troll.sendDoneActing();
        assertDoneActing(assertAndMerge(troll, bomber), 1);
        assertFieldEffect(assertAndMerge(troll, bomber), 1, 0, -6);
        assertRoundEnd(assertAndMerge(troll, bomber), 1, 1);

        // Round 2
        assertRoundBegin(assertAndMerge(troll, bomber), 2);
        for (int i = 0; i < 4; i++) {
            assertMoved(assertAndMerge(troll, bomber), 2, Direction.EAST);
        } //New coord: 9,0

        assertActNow(assertAndMerge(troll, bomber), 0);
        bomber.sendSwap(1, 0);//Bomber swaps with himself again
        assertSwapped(assertAndMerge(troll, bomber), 0, 1, 0);
        assertActNow(assertAndMerge(troll, bomber), 0);
        bomber.sendDoneActing();
        assertDoneActing(assertAndMerge(troll, bomber), 0);

        assertActNow(assertAndMerge(troll, bomber), 1);
        troll.sendSwap(9, 0);
        assertSwapped(assertAndMerge(troll, bomber), 1, 9, 0);
        assertActNow(assertAndMerge(troll, bomber), 1);
        troll.sendDoneActing();
        assertDoneActing(assertAndMerge(troll, bomber), 1);
        assertFieldEffect(assertAndMerge(troll, bomber), 1, 0, -6);
        assertRoundEnd(assertAndMerge(troll, bomber), 2, 0);

        // Round 3
        assertRoundBegin(assertAndMerge(troll, bomber), 3);
        assertDied(assertAndMerge(troll, bomber), 2);

        assertActNow(assertAndMerge(troll, bomber), 0);
        bomber.sendSwap(1, 0);//Bomber swaps with himself again
        assertSwapped(assertAndMerge(troll, bomber), 0, 1, 0);
        assertActNow(assertAndMerge(troll, bomber), 0);
        bomber.sendDoneActing();
        assertDoneActing(assertAndMerge(troll, bomber), 0);

        assertActNow(assertAndMerge(troll, bomber), 1);
        troll.sendSwap(12, 0); //Kick
        assertKicked(assertAndMerge(troll, bomber), 1);

        assertRoundEnd(assertAndMerge(troll, bomber), 3, 0);
        assertWinner(assertAndMerge(troll, bomber), "TeamSuicide");

    }

    @Override
    protected String getMapFile() {
        return "0+            1";
    }

    @Override
    protected String getFightFile() {
        return "TeamSuicide, TeamTroll\n"+
                "TeamSuicide, Bomber, Wraith\n"+
                "TeamTroll, Troll, Wraith";
    }
}
