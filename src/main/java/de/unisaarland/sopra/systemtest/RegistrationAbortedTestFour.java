package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created on 21.09.16.
 *
 * @author Henrik Paul Koehn
 */
public class RegistrationAbortedTestFour extends OurSystemTest {

    public RegistrationAbortedTestFour() {
        super("RegistrationAbortedTestAnotherCommand(4)");
    }

    @Override
    protected void executeTest() {
        // Valid registrations
        expectServerFail();

        ClientConnection<Event> shy = register(0, "Shiggy", CreatureType.NAGA, "Wasser", 1, 0);
        ClientConnection<Event> bisa = register(1, "Bisasam", CreatureType.ELF, "Gras", 0, 1);
        assertRegisterEvent(bisa.nextEvent(), 0, "Shiggy", CreatureType.NAGA, "Wasser", 1, 0);
        assertRegisterEvent(shy.nextEvent(), 1, "Bisasam", CreatureType.ELF, "Gras", 0, 1);

        bisa.sendWarCry("Wann geht es los?");
        assertRegistrationAborted(assertAndMerge(shy, bisa));
    }

    @Override
    protected String getMapFile() {
        return "01\n" +
                "20";
    }

    @Override
    protected String getFightFile() {
        return "Feuer, Wasser, Gras\n" +
                "Feuer, Glumanda, Ifrit\n" +
                "Wasser, Shiggy, Naga\n" +
                "Gras, Bisasam, Elf";
    }
}
