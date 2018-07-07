package de.unisaarland.sopra.publictest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.MonsterType;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.systemtest.SystemTest;
import org.junit.Assert;

public class PublicBeispielTest extends SystemTest {

    private static final String FIGHT = "A,B\n\nA,Dobby,ELF\nB,Flobby,ELF";
    private static final String MAP = "#0  1t";

    public PublicBeispielTest() {
        super("PublicBeispielTest");
    }

    @Override
    public String getFightFile() {
        return FIGHT;
    }

    @Override
    protected void executeTest() {
        // öffne eine Verbindung und registriere Dobby
        ClientConnection<String> dobby = openClient(new TestEventFactory());
        dobby.sendRegister("Dobby", MonsterType.ELF, "A");
        Assert.assertEquals("Map(" + MAP + ')', dobby.nextEvent());
        // String equality is not enough to compare fight files
        Assert.assertTrue(dobby.nextEvent().toString().startsWith("Fight"));
        Assert.assertEquals("Registered(0, Dobby, ELF, A, 0, 1, 0)", dobby.nextEvent());

        // öffne eine Verbindung und registriere Flobby
        ClientConnection<String> flobby = openClient(new TestEventFactory());
        flobby.sendRegister("Flobby", MonsterType.ELF, "B");
        Assert.assertEquals("Map(" + MAP + ')', flobby.nextEvent());
        // String equality is not enough to compare fight files
        Assert.assertTrue(flobby.nextEvent().toString().startsWith("Fight"));
        Assert.assertEquals("Registered(1, Flobby, ELF, B, 1, 4, 0)", flobby.nextEvent());

        // beide erhalten die Registrierung des anderen
        Assert.assertEquals("Registered(0, Dobby, ELF, A, 0, 1, 0)", flobby.nextEvent());
        Assert.assertEquals("Registered(1, Flobby, ELF, B, 1, 4, 0)", dobby.nextEvent());

        // jetzt fängt das Spiel an
        // auf der Karte gibt es keine Fee, also kommt ein RoundBegin
        Assert.assertEquals("RoundBegin(1)", flobby.nextEvent());
        Assert.assertEquals("RoundBegin(1)", dobby.nextEvent());

        // Dobby wird zum Handeln aufgefordert
        Assert.assertEquals("ActNow(0)", flobby.nextEvent());
        Assert.assertEquals("ActNow(0)", dobby.nextEvent());

        // auf der Karte ist westlich von Dobby ein Felsen, er darf also nicht nach WEST
        // gehen und wird gekickt, weil er es probiert.
        dobby.sendMove(Direction.WEST);
        Assert.assertTrue(dobby.nextEvent().startsWith("Kicked(0, "));
        Assert.assertTrue(flobby.nextEvent().startsWith("Kicked(0, "));

        // dann ist Flobby dran
        Assert.assertEquals("ActNow(1)", flobby.nextEvent());
        Assert.assertEquals("ActNow(1)", dobby.nextEvent());

        // der macht aber nichts
        flobby.sendDoneActing();
        Assert.assertEquals("DoneActing(1)", flobby.nextEvent());
        Assert.assertEquals("DoneActing(1)", dobby.nextEvent());

        // die Runde endet
        Assert.assertEquals("RoundEnd(1, 0)", flobby.nextEvent());
        Assert.assertEquals("RoundEnd(1, 0)", dobby.nextEvent());

        // und Team B hat gewonnen, weil alle Monster aus Team A gekickt wurden
        Assert.assertEquals("Winner(B)", flobby.nextEvent());
        Assert.assertEquals("Winner(B)", dobby.nextEvent());
    }

    @Override
    protected String getMapFile() {
        return MAP;
    }
}
