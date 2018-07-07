package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Wiebk on 19.09.2016.
 */
public class ClawCrushTestSimple extends OurSystemTest {

    public ClawCrushTestSimple() {
        super("ClawCrushTestSimple");
    }

    @Override
    public void executeTest() {
        //registration
        ClientConnection<Event> tucker = register(0, "Tucker", CreatureType.YETI, "Tucker and Dale", 0, 1);
        ClientConnection<Event> allison = register(1, "Allison", CreatureType.ROOK, "Evil", 1, 1);
        Event registertucker = allison.nextEvent();
        assertRegisterEvent(registertucker, 0, "Tucker", CreatureType.YETI, "Tucker and Dale", 0, 1);
        assertRegisterEvent(tucker.nextEvent(), 1, "Allison", CreatureType.ROOK, "Evil", 1, 1);

        assertRoundBegin(assertAndMerge(tucker, allison), 1);

        //tucker's turn, claw at allison, then crush
        assertActNow(assertAndMerge(tucker, allison), 0);
        tucker.sendClaw(Direction.EAST);
        assertClaw(assertAndMerge(tucker, allison), 0, Direction.EAST);
        assertActNow(assertAndMerge(tucker, allison), 0);
        tucker.sendCrush(Direction.EAST);
        assertCrush(assertAndMerge(tucker, allison), 0, Direction.EAST);
        assertActNow(assertAndMerge(tucker, allison), 0);
        tucker.sendDoneActing();
        assertDoneActing(assertAndMerge(tucker, allison), 0);

        //allison's turn, stare at tucker
        assertActNow(assertAndMerge(tucker, allison), 1);
        allison.sendStare(Direction.WEST);
        assertStared(assertAndMerge(tucker, allison), 1, Direction.WEST);
        assertActNow(assertAndMerge(tucker, allison), 1);
        allison.sendDoneActing();
        assertDoneActing(assertAndMerge(tucker, allison), 1);

        assertRoundEnd(assertAndMerge(tucker, allison), 1, 0);
        assertRoundBegin(assertAndMerge(tucker, allison), 2);

        //allison's turn: no action
        assertActNow(assertAndMerge(tucker, allison), 1);
        allison.sendDoneActing();
        assertDoneActing(assertAndMerge(tucker, allison), 1);

        //tucker's turn: sends burn, gets kicked
        assertActNow(assertAndMerge(tucker, allison), 0);
        tucker.sendCrush(Direction.EAST);
        assertCrush(assertAndMerge(tucker, allison), 0, Direction.EAST);
        assertActNow(assertAndMerge(tucker, allison), 0);
        tucker.sendBurn();
        assertKicked(assertAndMerge(tucker, allison), 0);

        assertRoundEnd(assertAndMerge(tucker, allison), 2, 0);

        //Evil wins
        assertWinner(assertAndMerge(tucker, allison), "Evil");

    }

    @Override
    public String getMapFile() {
        return "tttt\n"
                + "01tt\n"
                + "tttt\n"
                + "tttt";
    }

    public String getFightFile() {
        return "Tucker and Dale, Evil\n"
                + "Tucker and Dale, Tucker, YETI\n"
                + "Evil, Allison, rook";
    }

}
