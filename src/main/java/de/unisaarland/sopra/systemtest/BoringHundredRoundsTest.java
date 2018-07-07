package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Lukas Kirschner on 9/19/16.
 * This test checks if server kicks everyone after 100 boring rounds
 */
public class BoringHundredRoundsTest extends OurSystemTest {

    private final static int NUMBORINGROUNDS = 100;

    public BoringHundredRoundsTest() {
        super("BoringHundredRoundsTest");
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> legolas = register(0, "Legolas", CreatureType.ELF, "TeamA", 0, 0);
        ClientConnection<Event> tauriel = register(1, "Tauriel", CreatureType.ELF, "TeamB", 1, 0);
        assertRegisterEvent(tauriel.nextEvent(), 0, "Legolas", CreatureType.ELF, "TeamA", 0, 0);
        assertRegisterEvent(legolas.nextEvent(), 1, "Tauriel", CreatureType.ELF, "TeamB", 1, 0);
        int i;
        for (i = 1; i <= NUMBORINGROUNDS; i++) {
            assertRoundBegin(assertAndMerge(legolas, tauriel), i);
            if ((i % 2) == 0) {
                assertActNow(assertAndMerge(legolas, tauriel), 1);
                tauriel.sendDoneActing();
                assertDoneActing(assertAndMerge(legolas, tauriel), 1);
                assertActNow(assertAndMerge(legolas, tauriel), 0);
                legolas.sendDoneActing();
                assertDoneActing(assertAndMerge(legolas, tauriel), 0);
            } else {
                assertActNow(assertAndMerge(legolas, tauriel), 0);
                legolas.sendDoneActing();
                assertDoneActing(assertAndMerge(legolas, tauriel), 0);
                assertActNow(assertAndMerge(legolas, tauriel), 1);
                tauriel.sendDoneActing();
                assertDoneActing(assertAndMerge(legolas, tauriel), 1);
            }
            assertRoundEnd(assertAndMerge(legolas, tauriel), i, i);
        }
        if ((i % 2) == 0){
            assertKicked(assertAndMerge(legolas, tauriel), 1);
            assertKicked(assertAndMerge(legolas, tauriel), 0);
        } else {
            assertKicked(assertAndMerge(legolas, tauriel), 0);
            assertKicked(assertAndMerge(legolas, tauriel), 1);
        }

    }

    @Override
    protected String getMapFile() {
        return "01\n  ";
    }

    @Override
    protected String getFightFile() {
        return "TeamA, TeamB\nTeamA, Legolas, Elf\nTeamB, Tauriel, Elf";
    }
}
