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
public class RegistrationAbortedTestOne extends OurSystemTest {
    /**
     * Wrong Name
     */
    public RegistrationAbortedTestOne() {
        super("RegistrationAbortedTestWrongName(1)");
    }

    @Override
    protected void executeTest() {

        expectServerFail();
        ClientConnection<Event> shy = register(0, "Shiggy", CreatureType.NAGA, "Wasser", 1, 0);
        ClientConnection<Event> glu = openClient(new EventFactoryImpl());
        glu.sendRegister("glumanda", MonsterType.valueOf(CreatureType.IFRIT.name()), "Feuer");
        assertRegistrationAborted(shy.nextEvent());
        try {
            glu.nextEvent(); // needs to be a timeout
            failed("Players who failed a registration shall not get events!");
        } catch (TimeoutException e){
            passed();
        }
    }

    @Override
    protected String getMapFile() {
        return "01";
    }

    @Override
    protected String getFightFile() {
        return "Feuer, Wasser\n" +
                "Feuer, Glumanda, Ifrit\n" +
                "Wasser, Shiggy, Naga";
    }
}
