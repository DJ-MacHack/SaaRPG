package de.unisaarland.sopra.publictest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.MonsterType;
import de.unisaarland.sopra.comm.ClientConnection;
import org.junit.Assert;

/**
 * In diesem Test steht ein Wildschwein so, dass es an allen Seiten von Felsen umgeben ist.
 * <p>
 * Es kann dadurch gar nicht laufen.
 * <p>
 * Created by akampmann on 07/09/16.
 */
public class TrappedBoarTest extends PublicTest {
    /**
     * Mapfile für diesen Test.
     */
    private static final String MAP =
            "##########\n"
                    + "#0##.###1#\n"
                    + "##########";
    /**
     * FightFile für diesen Test.
     */
    private static final String FIGHT = "A,B\n\n"
            + "A,Balrog,ROOK\n"
            + "B,Legolegolas,ELF";

    public TrappedBoarTest() {
        super("TrappedBoarTest");
    }

    @Override
    protected void executeTest() {
        // öffne eine Verbindung und registriere Balrog
        ClientConnection<String> balrog = register(0, "Balrog", MonsterType.ROOK, "A");

        // öffne eine Verbindung und registriere Legolegolas
        ClientConnection<String> legolas = register(1, "Legolegolas", MonsterType.ELF, "B");

        // beide erhalten die Registrierung des anderen
        Assert.assertEquals("Registered(1, Legolegolas, ELF, B, 1, 8, 1)", balrog.nextEvent());
        Assert.assertEquals("Registered(0, Balrog, ROOK, A, 0, 1, 1)", legolas.nextEvent());

        // jetzt fängt das Spiel an
        // auf der Karte gibt es keine Fee, also kommt ein RoundBegin
        assertSameEvent("RoundBegin(1)", balrog, legolas);

        // das Wildschwein spawnt
        assertSameEvent("BoarSpawned(2, 4, 1)", balrog, legolas);

        // keine Feen, also bewegt sich auch keine Fee
        // das Wildschwein ist ringsum von Felsen umgeben, es kann sich also nicht bewegen und bleibt stehen

        // deshalb ist der Balrog dran
        assertSameEvent("ActNow(0)", balrog, legolas);
        // der Balrog greift das Schwein an
        // da Cast ein Zauber ist, braucht er keine Sichtlinie, kommt also über den Felsen
        // drüber
        balrog.sendCast(4, 1);
        assertSameEvent("Cast(0, 4, 1)", balrog, legolas);
        // das Schwein hat noch 10 Lebenspunkte

        // nochmal der Balrog, es sind 550 Lebenspunkte übrig
        assertSameEvent("ActNow(0)", balrog, legolas);
        balrog.sendCast(4, 1);
        assertSameEvent("Cast(0, 4, 1)", balrog, legolas);
        // jetzt ist das Schwein tot
        assertSameEvent("Died(2)", balrog, legolas);

        assertSameEvent("ActNow(0)", balrog, legolas);
        balrog.sendDoneActing();
        assertSameEvent("DoneActing(0)", balrog, legolas);

        // jetzt ist Legolegolas dran
        assertSameEvent("ActNow(1)", balrog, legolas);
        // er probiert auf den Balrog zu schießen, aber da ist Fels im Weg
        legolas.sendShoot(Direction.WEST);
        assertSameEventStartsWith("Kicked(1,", legolas, balrog);
        // also wird er gekickt

        // damit endet die Runde
        assertSameEvent("RoundEnd(1, 0)", legolas, balrog);

        assertSameEvent("Winner(A)", legolas, balrog);
    }

    @Override
    protected String getMapFile() {
        return MAP;
    }

    @Override
    protected String getFightFile() {
        return FIGHT;
    }
}
