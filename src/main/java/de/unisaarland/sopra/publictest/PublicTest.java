package de.unisaarland.sopra.publictest;

    import de.unisaarland.sopra.MonsterType;
    import de.unisaarland.sopra.comm.ClientConnection;
    import de.unisaarland.sopra.systemtest.SystemTest;
    import org.junit.Assert;

    import java.util.Arrays;
    import java.util.List;

/**
 * Diese Klasse enthält Code, den wir in den public tests immer wieder gebraucht haben.
 * <p>
 * Diese Helfer machen nur für die String TestEventFactory Sinn, von deren Verwendung wir abraten. Wir raten also
 * auch davon ab diese Klasse zu benutzen, Sie können aber für Ihre eigenen Tests einen
 * ähnlichen Mechanismus bauen.
 * <p>
 * Created by akampmann on 08/09/16.
 */
@SuppressWarnings("unchecked")
public abstract class PublicTest extends SystemTest {

    /**
     * @param name der Name des Tests.
     */
    protected PublicTest(String name) {
        super(name);
    }

    /**
     * Diese Methode registriert einen Spieler und gibt die Connection zurück.
     * <p>
     * Diese Methode prüft NICHT, ob die Koordinaten des Spielers korrekt sind.
     *
     * @param id          die Id des Spielers.
     * @param name        der Name des Spielers.
     * @param monsterType der Monstertyp, der registiret werden soll.
     * @param team        das Team, zu dem der Spieler gehört
     * @return die entsprechende Verbindung
     */
    public ClientConnection<String> register(int id, String name, MonsterType monsterType, String team) {
        ClientConnection<String> cc = openClient(new TestEventFactory());
        cc.sendRegister(name, monsterType, team);
        Assert.assertEquals("Map(" + getMapFile() + ')', cc.nextEvent());
        // String equality is not enough to compare fight files
        Assert.assertTrue(cc.nextEvent().startsWith("Fight"));
        String regEvent = cc.nextEvent();
        Assert.assertTrue(regEvent.startsWith(String.format("Registered(%d, %s, %s, %s, %d, ", id, name, monsterType, team, id)));
        return cc;
    }

    /**
     * Diese Methode erwartet, dass alle übergebenen ClientConnections das selbe Event empfangen.
     *
     * @param expected das erwartete Event.
     * @param ccs      die ClientConnections, auf denen dieses Event kommen sollte
     */
    @SafeVarargs
    public final void assertSameEvent(String expected, ClientConnection<String>... ccs) {
        for (ClientConnection<String> cc : ccs) {
            String actual = cc.nextEvent();
            Assert.assertEquals(expected, actual);
        }
    }

    /**
     * Checks whether the given client list contains one of the expected items.
     * The method returns the index of the matching value in the expected list,
     * because the order of the entities may change.
     *
     */
    public final int assertSameEvent(List<String> expected, ClientConnection<String>[] ccs) {
        int index = 0;
        for (ClientConnection<String> cc : ccs) {
            String actual = cc.nextEvent();
            boolean hitExpectedVal = false;
            for (int i = 0; i < expected.size(); i++) {
                if (expected.get(i).equals(actual)) {
                    hitExpectedVal = true;
                    index = i;
                    break;
                }
            }
            Assert.assertTrue("actual was " + actual, hitExpectedVal);
        }
        return index;
    }

    /**
     * Diese Methode lässt alle Spieler nichts machen.
     *
     */
    protected void allPlayersMove(ClientConnection<String>[] ccs) {
        // durch die Schleife kann ich nicht asserten,
        // dass die Initiativreihenfolge korrekt ist,
        // aber da gibt es ja andere Tests für
        for (int i = 0; i < ccs.length; i++) {
            int actorId = receiveActNow(ccs);
            // die Connection dieses Monsters ist in ccs[actorId]
            ccs[actorId].sendDoneActing();
            assertSameEvent(String.format("DoneActing(%d)", actorId), ccs);
        }
    }

    /**
     * Diese Methode erwartet, dass alle Monster ein ActNow bekommen. Sie
     * returned, wer dran ist.
     *
     * @param ccs die Connections der Monster, die das ActNow bekommen sollen.
     * @return die Id des Monsters, dass dran ist.
     */
    int receiveActNow(ClientConnection<String>[] ccs) {
        // Diese Methode ist wieder ein Grund, NICHT die TestEventFactory zu benutzen.
        // Ein Wunder, dass weder PMD noch FindBugs mich umbringen

        // erstmal eins holen und schauen, ob es ein ActNow ist
        String actNow = ccs[0].nextEvent();
        Assert.assertTrue(String.format("Habe unerwarteterweise '%s' statt ActNow bekommen", actNow), actNow.startsWith("ActNow("));
        // dann gucken, ob alle anderen das auch bekommen haben
        assertSameEvent(actNow, Arrays.copyOfRange(ccs, 1, ccs.length));

        // jetzt herausfinden, wer denn dran ist, das steht an der 7. Stelle
        return Integer.parseInt(String.valueOf(actNow.charAt(7)));
    }

    /**
     * Diese Methode erwartet, dass alle übergebenen ClientConnections das selbe Event empfangen.
     * Dabei wird nur der Anfang des Event-String verglichen.
     *
     * @param expected das erwartete Event.
     * @param ccs      die ClientConnections, auf denen dieses Event kommen sollte
     */
    @SafeVarargs
    public final void assertSameEventStartsWith(String expected, ClientConnection<String>... ccs) {
        for (ClientConnection<String> cc : ccs) {
            String actual = cc.nextEvent();
            Assert.assertTrue(String.format("Ich habe '%s' statt '%s' bekommen", actual, expected), actual.startsWith(expected));
        }
    }

    /**
     * Checks ActNow and DoneActing events on the given client list. This is a helper
     * method for lazy monster actions.
     *
     */
    protected void checkAndHandleLazyMonster(ClientConnection<String>[] ccs, List<String> actNowList, List<String> doneActingList) {
        for (int j = 0; j < ccs.length; j++) {
            int index = assertSameEvent(actNowList, ccs);

            ClientConnection<String> cc = ccs[index];
            cc.sendDoneActing();

            assertSameEvent(doneActingList, ccs);
        }
    }
}
