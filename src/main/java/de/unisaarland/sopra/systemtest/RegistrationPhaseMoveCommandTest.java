package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.messages.EventFactoryImpl;

/**
 * Created on 21.09.16.
 *
 * @author Henrik Paul Koehn
 */
public class RegistrationPhaseMoveCommandTest extends OurSystemTest {

    public RegistrationPhaseMoveCommandTest(){
        super("RegistrationPhaseMoveCommandTest");
    }

    @Override
    protected void executeTest() {
        expectServerFail();
        ClientConnection<Event> ab = openClient(new EventFactoryImpl());
        ab.sendMove(Direction.WEST);
    }

    @Override
    protected String getMapFile() {
        return "0 1";
    }

    @Override
    protected String getFightFile() {
        return "A, B\nA, ab, Naga\nB, ba, Naga";
    }
}
