package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;
import org.junit.Assert;

/**
 * Created by Wiebk on 16.09.2016.
 */
public class BlinkSwapTestSimple extends OurSystemTest {

    public BlinkSwapTestSimple() {
        super("BlinkSwapTestSimple");
    }

    @Override
    public void executeTest() {
        ClientConnection<Event> nero = register(0, "Nero", CreatureType.ELF, "Klingonen", 1, 2);
        ClientConnection<Event> spock = register(1, "Spock", CreatureType.WRAITH, "Enterprise", 2, 2);
        assertRegisterEvent(nero.nextEvent(), 1, "Spock", CreatureType.WRAITH, "Enterprise", 2, 2);
        assertRegisterEvent(spock.nextEvent(), 0, "Nero", CreatureType.ELF, "Klingonen", 1, 2);

        //Spielbeginn, weder Feen noch Schweine im Mapfile
        assertRoundBegin(assertAndMerge(nero, spock), 1);

        //Monster agieren: Nero ist an der Reihe, stab auf Spock
        assertActNow(assertAndMerge(nero, spock), 0);
        nero.sendStab(Direction.EAST);
        assertStabbed(assertAndMerge(nero, spock), 0, Direction.EAST);
        assertActNow(assertAndMerge(nero, spock), 0);
        nero.sendDoneActing();
        assertDoneActing(assertAndMerge(nero, spock), 0);

        //Spock ist an der Reihe, swap mit Nero, anschließend blink mit Nero, dann cut auf Nero, wird gekicked
        assertActNow(assertAndMerge(nero, spock), 1);
        spock.sendSwap(1, 2);
        assertSwapped(assertAndMerge(nero, spock), 1, 1, 2);

        assertActNow(assertAndMerge(nero, spock), 1);
        spock.sendBlink(2, 0);
        assertBlinked(assertAndMerge(nero, spock), 1, 2, 0);

        assertActNow(assertAndMerge(nero, spock), 1);
        spock.sendCut(Direction.WEST);
        assertKicked(assertAndMerge(nero, spock), 1);

        //Spock wurde gekickt, Nero gewinnt
        assertRoundEnd(assertAndMerge(nero, spock), 1, 0);
        assertWinner(assertAndMerge(nero, spock), "Klingonen");
    }

    @Override
    public String getMapFile() {
        return "## # ##\n"
                + "## # ##\n"
                + "  01   \n"
                + "       \n"
                + "       \n"
                + "ttttttt\n"
                + "$_~%x^^";
    }

    @Override
    public String getFightFile() {
        return "Klingonen, Enterprise\n"
                + "Klingonen, Nero, Elf\n"
                + "Enterprise, Spock, Wraith";

    }
}
