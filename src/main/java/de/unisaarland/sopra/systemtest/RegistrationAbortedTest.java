package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Karla on 22.09.2016.
 */
public class RegistrationAbortedTest extends OurSystemTest {

    private static final String MAPFILE = "0 ttt\n"
                                        + "1   x";

    private static final String FIGHTFILE = "Targaryen, Lannister \n"
            + "Targaryen, Daenerys, Elf\n"
            + "Lannister, Cersei, Naga";

    public RegistrationAbortedTest() {
        super("RegistrationAbortedTest");
    }

    @Override
    protected void executeTest() {
        expectServerFail();
        ClientConnection<Event> dany = register(0, "Daenerys", CreatureType.ELF, "Targaryen", 0, 0);
        dany.sendSlash(Direction.NORTH_WEST);
        assertRegistrationAborted(assertAndMerge(dany));
    }

    @Override
    protected String getMapFile() {
        return MAPFILE;
    }

    @Override
    protected String getFightFile() {
        return FIGHTFILE;
    }
}
