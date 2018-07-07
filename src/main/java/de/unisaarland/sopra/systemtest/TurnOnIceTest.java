package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Lukas Kirschner on 9/16/16.
 * <p>
 * This test first tests if Yeti can turn on ice, then tests if Naga can turn (Naga is kicked at the end). Map is 5x5 ice except for spawn areas
 */
public class TurnOnIceTest extends OurSystemTest {
    private CreatureType t;

    public TurnOnIceTest(CreatureType t) {
        super("TurnOnIceTest" + t.toString());
        this.t = t;
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> derYeti = register(0, "derYeti", CreatureType.YETI, "TeamFrost", 0, 0);
        ClientConnection<Event> derNaga = register(1, "der" + t.toString(), t, "TeamSumpf" + t.toString(), 4, 0);
        assertRegisterEvent(derNaga.nextEvent(), 0, "derYeti", CreatureType.YETI, "TeamFrost", 0, 0);
        assertRegisterEvent(derYeti.nextEvent(), 1, "der" + t.toString(), t, "TeamSumpf" + t.toString(), 4, 0);

        //Round 1 begins
        assertRoundBegin(assertAndMerge(derYeti, derNaga), 1);

        //derYeti is on turn
        assertActNow(assertAndMerge(derYeti, derNaga), 0);

        derYeti.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(derYeti, derNaga), 0, Direction.EAST);
        assertActNow(assertAndMerge(derYeti, derNaga), 0);
        //derYeti changes move direction (because he can do)
        derYeti.sendMove(Direction.SOUTH_EAST);
        assertMoved(assertAndMerge(derYeti, derNaga), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(derYeti, derNaga), 0);
        derYeti.sendDoneActing();
        assertDoneActing(assertAndMerge(derYeti, derNaga), 0);

        assertActNow(assertAndMerge(derYeti, derNaga), 1);
        derNaga.sendMove(Direction.WEST);
        assertMoved(assertAndMerge(derYeti, derNaga), 1, Direction.WEST);
        assertActNow(assertAndMerge(derYeti, derNaga), 1);
        derNaga.sendMove(Direction.SOUTH_WEST);

        //assertMoved(assertAndMerge(derYeti,derNaga), 1, Direction.SOUTH_WEST); //Not sure if this is an issue of our server implementation

        assertKicked(assertAndMerge(derYeti, derNaga), 1); //t gets kicked because he is not allowed to change moving direction while on ice
        assertRoundEnd(assertAndMerge(derYeti, derNaga), 1, 0);
        assertWinner(assertAndMerge(derYeti, derNaga), "TeamFrost");
    }

    @Override
    protected String getMapFile() {
        return "0___1\n_____\n_____\n_____\n_____";
    }

    @Override
    protected String getFightFile() {
        return "TeamFrost, TeamSumpf" + t.toString() + "\nTeamFrost, derYeti, YeTi\nTeamSumpf" + t.toString() + ", der" + t.toString() + ", " + t.toString() + "";
    }

    public static Collection<OurSystemTest> getTests(){
        ArrayList<OurSystemTest> sysTests = new ArrayList<>();
        sysTests.add(new TurnOnIceTest(CreatureType.KOBOLD));
        sysTests.add(new TurnOnIceTest(CreatureType.ELF));
        sysTests.add(new TurnOnIceTest(CreatureType.NAGA));
        sysTests.add(new TurnOnIceTest(CreatureType.ROOK));
        sysTests.add(new TurnOnIceTest(CreatureType.IFRIT));
        sysTests.add(new TurnOnIceTest(CreatureType.WRAITH));
        return sysTests;
    }
}
