package de.unisaarland.sopra.publictest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.MonsterType;
import de.unisaarland.sopra.comm.ClientConnection;
import org.junit.Assert;

/**
 * Dieser Test testet Wechselwirkungen zwischen Blink, Swap und Ice.
 * <p>
 * Created by akampmann on 08/09/16.
 */
public class BlinkOnIceTest extends PublicTest {

    public BlinkOnIceTest() {
        super("BlinkOnIceTest");
    }

    @Override
    protected void executeTest() {
        // öffne eine Verbindung und registriere WA
        ClientConnection<String> wa = register(0, "WA", MonsterType.WRAITH, "A");

        // öffne eine Verbindung und registriere WB
        ClientConnection<String> wb = register(1, "WB", MonsterType.WRAITH, "B");

        // beide erhalten die Registrierung des anderen
        Assert.assertEquals("Registered(0, WA, WRAITH, A, 0, 1, 1)", wb.nextEvent());
        Assert.assertEquals("Registered(1, WB, WRAITH, B, 1, 2, 2)", wa.nextEvent());

        // keine Feen, also fängt die erste Runde an
        assertSameEvent("RoundBegin(1)", wa, wb);

        // WA läuft nach Osten, dann blinkt er zurück und sollte jetzt nach Nord-Osten laufen dürfen.
        assertSameEvent("ActNow(0)", wa, wb);
        wa.sendMove(Direction.EAST);
        assertSameEvent("Moved(0, EAST)", wa, wb);
        assertSameEvent("ActNow(0)", wa, wb);
        wa.sendBlink(0, 1);
        assertSameEvent("Blinked(0, 0, 1)", wa, wb);
        assertSameEvent("ActNow(0)", wa, wb);
        wa.sendMove(Direction.NORTH_EAST);
        assertSameEvent("Moved(0, NORTH_EAST)", wa, wb);
        assertSameEvent("ActNow(0)", wa, wb);
        wa.sendDoneActing();
        assertSameEvent("DoneActing(0)", wa, wb);

        // WB läuft zwei mal nach Nord-Westen, danach swappt er WA herum.
        assertSameEvent("ActNow(1)", wa, wb);
        wb.sendMove(Direction.NORTH_WEST);
        assertSameEvent("Moved(1, NORTH_WEST)", wa, wb);
        assertSameEvent("ActNow(1)", wa, wb);
        wb.sendMove(Direction.NORTH_WEST);
        assertSameEvent("Moved(1, NORTH_WEST)", wa, wb);
        assertSameEvent("ActNow(1)", wa, wb);
        wb.sendSwap(1, 0);
        assertSameEvent("Swapped(1, 1, 0)", wa, wb);
        assertSameEvent("ActNow(1)", wa, wb);
        wb.sendMove(Direction.SOUTH_WEST);
        assertSameEvent("Moved(1, SOUTH_WEST)", wa, wb);
        assertSameEvent("ActNow(1)", wa, wb);
        wb.sendDoneActing();
        assertSameEvent("DoneActing(1)", wa, wb);


        assertSameEvent("RoundEnd(1, 0)", wa, wb);

        // zweite Runde
        assertSameEvent("RoundBegin(2)", wa, wb);

        // WB ist dran
        assertSameEvent("ActNow(1)", wa, wb);
        // und lässt ein Timeout passieren
        assertSameEventStartsWith("Kicked(1,", wa, wb);

        // also ist WA dran
        assertSameEvent("ActNow(0)", wa, wb);
        // und lässt auch ein Timeout passieren
        assertSameEventStartsWith("Kicked(0,", wa, wb);
        assertSameEvent("RoundEnd(2, 0)", wa, wb);

        assertSameEvent("Winner()", wa, wb);
    }

    @Override
    protected String getMapFile() {
        return "_____\n"
                + "_0___\n"
                + "___1_";
    }

    @Override
    protected String getFightFile() {
        return "A,B\n\n"
                + "A,WA,Wraith\n"
                + "B,WB,wraith";
    }
}
