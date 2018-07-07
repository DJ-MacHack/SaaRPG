package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.MonsterType;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.comm.TimeoutException;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.messages.EventFactoryImpl;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created on 21.09.16.
 *
 * @author Henrik Paul Koehn
 */
public class RegistrationAbortedTestThree extends OurSystemTest {
    /**
     * Wrong Name
     */
    public RegistrationAbortedTestThree() {
        super("RegistrationAbortedTestWrongCreature(3)");
    }

    @Override
    protected void executeTest() {

        expectServerFail();

        // Valid registrations
        ClientConnection<Event> shy = register(0, "Shiggy", CreatureType.NAGA, "Wasser", 1, 0);
        ClientConnection<Event> bisa = register(1, "Bisasam", CreatureType.ELF, "Gras", 0, 1);
        assertRegisterEvent(bisa.nextEvent(), 0, "Shiggy", CreatureType.NAGA, "Wasser", 1, 0);
        assertRegisterEvent(shy.nextEvent(), 1, "Bisasam", CreatureType.ELF, "Gras", 0, 1);

        // Invalid registration
        ClientConnection<Event> glu = openClient(new EventFactoryImpl());
        glu.sendRegister("Glumanda", MonsterType.valueOf(CreatureType.YETI.name()), "Feuer");
        // Registration aborted expected
        assertRegistrationAborted(assertAndMerge(shy, bisa));
        try {
            glu.nextEvent(); // needs to be a timeout
            failed("Players who failed a registration shall not get events!");
        } catch (TimeoutException e){
            passed();
        }
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
