package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team 14 on 23.09.2016.
 */
public class CanBlinkTest extends OurSystemTest {

    public CanBlinkTest(){
        super("CanBlinkTest");
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> w = register(0, "Walter", CreatureType.WRAITH, "U1", 0, 0);
        ClientConnection<Event> w2 = register(1, "Walternativ", CreatureType.WRAITH, "U2", 8, 0);
        ClientConnection<Event> p = register(2, "Peter", CreatureType.WRAITH, "U1", 1, 0);

        assertRegisterEvent(w.nextEvent(), 1, "Walternativ", CreatureType.WRAITH, "U2", 8, 0);
        assertRegisterEvent(w.nextEvent(), 2, "Peter", CreatureType.WRAITH, "U1", 1, 0);

        assertRegisterEvent(w2.nextEvent(), 0, "Walter", CreatureType.WRAITH, "U1", 0, 0);
        assertRegisterEvent(w2.nextEvent(), 2, "Peter", CreatureType.WRAITH, "U1", 1, 0);

        assertRegisterEvent(p.nextEvent(), 0, "Walter", CreatureType.WRAITH, "U1", 0, 0);
        assertRegisterEvent(p.nextEvent(), 1, "Walternativ", CreatureType.WRAITH, "U2", 8, 0);

        assertRoundBegin(assertAndMerge(w, w2, p), 1);

        assertActNow(assertAndMerge(w, w2, p), 0);
        w.sendBlink(7, 0);
        assertKicked(assertAndMerge(w, w2, p), 0);

        assertActNow(assertAndMerge(w, w2, p), 1);
        w2.sendBlink(6, 0);
        assertBlinked(assertAndMerge(w, w2, p), 1, 6, 0);
        assertActNow(assertAndMerge(w, w2, p), 1);
        w2.sendDoneActing();
        assertDoneActing(assertAndMerge(w, w2, p), 1);

        assertActNow(assertAndMerge(w, w2, p), 2);
        w2.sendBlink(6, 0);
        assertKicked(assertAndMerge(w, w2, p), 1);
        p.sendBlink(2, 0);
        assertBlinked(assertAndMerge(w, w2, p), 2, 2, 0);
        assertActNow(assertAndMerge(w, w2, p), 2);
        p.sendDoneActing();
        assertDoneActing(assertAndMerge(w, w2, p), 2);

        w.sendBlink(-1, -1);

        assertRoundEnd(assertAndMerge(w, w2, p), 1, 0);
    }

    @Override
    protected String getMapFile() {
        return "00      1";
    }

    @Override
    protected String getFightFile() {
        return "U1, U2\n"+
                "U1, Walter, Wraith\n"+
                "U1, Peter, Wraith\n"+
                "U2, Walternativ, Wraith";
    }
}
