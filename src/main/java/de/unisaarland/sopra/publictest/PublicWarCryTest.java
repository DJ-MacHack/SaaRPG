package de.unisaarland.sopra.publictest;

import de.unisaarland.sopra.MonsterType;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.systemtest.SystemTest;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class PublicWarCryTest extends PublicTest {

    private final String cry;

    private PublicWarCryTest(String name, int length) {
        super(name);
        if (length < 0) {
            throw new IllegalArgumentException("Länge darf nicht negativ sein!");
        }
        cry = Stream.generate(() -> "#")
                .limit(length)
                .collect(Collectors.joining());
    }

    public String getCry() {
        return cry;
    }

    public static Collection<SystemTest> getTests() {
        return Arrays.asList(new MaxWarCryTest(),
                new EmptyWarCryTest(),
                new TooLongWarCryTest(),
                new DoubleWarCryTest());
    }

    @Override
    protected void executeTest() {
        ClientConnection<String> dobby = openClient(new TestEventFactory());
        dobby.sendRegister("Dobby", MonsterType.ELF, "A");
        Assert.assertEquals("Map(" + getMapFile() + ')', dobby.nextEvent());
        assertSameEventStartsWith("Fight(", dobby);
        Assert.assertEquals("Registered(0, Dobby, ELF, A, 0, 0, 0)", dobby.nextEvent());

        // öffne eine Verbindung und registriere Robby
        ClientConnection<String> robby = openClient(new TestEventFactory());
        robby.sendRegister("Robby", MonsterType.ROOK, "B");
        Assert.assertEquals("Map(" + getMapFile() + ')', robby.nextEvent());
        // String equality is not enough to compare fight files
        Assert.assertTrue(robby.nextEvent().startsWith("Fight"));
        Assert.assertEquals("Registered(1, Robby, ROOK, B, 1, 1, 0)", robby.nextEvent());

        // beide erhalten die Registrierung des anderen
        Assert.assertEquals("Registered(0, Dobby, ELF, A, 0, 0, 0)", robby.nextEvent());
        Assert.assertEquals("Registered(1, Robby, ROOK, B, 1, 1, 0)", dobby.nextEvent());

        // jetzt fängt das Spiel an
        // auf der Karte gibt es keine Fee, also kommt ein RoundBegin
        Assert.assertEquals("RoundBegin(1)", robby.nextEvent());
        Assert.assertEquals("RoundBegin(1)", dobby.nextEvent());

        // Dobby wird zum Handeln aufgefordert
        Assert.assertEquals("ActNow(0)", robby.nextEvent());
        Assert.assertEquals("ActNow(0)", dobby.nextEvent());

        dobby.sendWarCry(getCry());

        expectation(dobby, robby);
    }

    protected abstract void expectation(ClientConnection<String> dobby, ClientConnection<String> robby);

    @Override
    protected String getMapFile() {
        return "01";
    }

    @Override
    protected String getFightFile() {
        return "A,B\nA,Dobby,Elf\nB,Robby,Rook";
    }

    private static class MaxWarCryTest extends PublicWarCryTest {

        private MaxWarCryTest() {
            super("MaxWarCryTest", 140);
        }

        protected MaxWarCryTest(String name, int length) {
            super(name, length);
        }

        @Override
        protected void expectation(ClientConnection<String> dobby, ClientConnection<String> robby) {
            String expected = String.format("WarCry(0, %s)", getCry());
            Assert.assertEquals(expected, dobby.nextEvent());
            Assert.assertEquals(expected, robby.nextEvent());
            Assert.assertEquals("ActNow(0)", dobby.nextEvent());
            Assert.assertEquals("ActNow(0)", robby.nextEvent());
        }
    }

    private static final class EmptyWarCryTest extends PublicWarCryTest {

        private EmptyWarCryTest() {
            super("EmptyWarCryTest", 0);
        }

        @Override
        protected void expectation(ClientConnection<String> dobby, ClientConnection<String> robby) {
            Assert.assertEquals("WarCry(0, )", dobby.nextEvent());
            Assert.assertEquals("WarCry(0, )", robby.nextEvent());
        }
    }

    private static final class TooLongWarCryTest extends PublicWarCryTest {

        private TooLongWarCryTest() {
            super("TooLongWarCryTest", 141);
        }

        @Override
        protected void expectation(ClientConnection<String> dobby, ClientConnection<String> robby) {
            Assert.assertTrue(dobby.nextEvent().startsWith("Kicked(0, "));
            Assert.assertTrue(robby.nextEvent().startsWith("Kicked(0, "));

            Assert.assertEquals("ActNow(1)", dobby.nextEvent());
            Assert.assertEquals("ActNow(1)", robby.nextEvent());

            robby.sendDoneActing();
            Assert.assertEquals("DoneActing(1)", dobby.nextEvent());
            Assert.assertEquals("DoneActing(1)", robby.nextEvent());

            Assert.assertEquals("RoundEnd(1, 0)", dobby.nextEvent());
            Assert.assertEquals("RoundEnd(1, 0)", robby.nextEvent());

            Assert.assertEquals("Winner(B)", dobby.nextEvent());
            Assert.assertEquals("Winner(B)", robby.nextEvent());
        }
    }

    private static final class DoubleWarCryTest extends MaxWarCryTest {

        private DoubleWarCryTest() {
            super("DoubleWarCryTest", 10);
        }

        @Override
        protected void expectation(ClientConnection<String> dobby, ClientConnection<String> robby) {
            super.expectation(dobby, robby);
            dobby.sendWarCry(getCry());
            Assert.assertTrue(dobby.nextEvent().startsWith("Kicked(0, "));
            Assert.assertTrue(robby.nextEvent().startsWith("Kicked(0, "));
        }
    }

}
