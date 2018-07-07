package de.unisaarland.sopra.publictest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.MonsterType;
import de.unisaarland.sopra.comm.ClientConnection;

/**
 * Achtung, in diesem Test begegnen Sie einem panischen Schwein.
 * <p>
 * Created by akampmann on 08/09/16.
 */
@SuppressWarnings("unchecked")
public class AmokBoarTest extends PublicTest {

    public AmokBoarTest() {
        super("AmokBoarTest");
    }

    @Override
    protected void executeTest() {
        // register alle und schreib die Connections in ein Array
        ClientConnection<String>[] ccs = new ClientConnection[8];
        ccs[0] = register(0, "A1", MonsterType.IFRIT, "A");
        ccs[1] = register(1, "A2", MonsterType.IFRIT, "A");
        ccs[2] = register(2, "A3", MonsterType.IFRIT, "A");
        ccs[3] = register(3, "A4", MonsterType.IFRIT, "A");

        ccs[4] = register(4, "B1", MonsterType.NAGA, "B");
        ccs[5] = register(5, "B2", MonsterType.NAGA, "B");
        ccs[6] = register(6, "B3", MonsterType.NAGA, "B");
        ccs[7] = register(7, "B4", MonsterType.NAGA, "B");

        // alle bekommen die Registrierungen der anderen
        // das heißt es kommen noch 7 Registrierungen, deren Korrektheit ich nicht prüfe
        for (int i = 0; i < 7; i++) {
            assertSameEventStartsWith("Registered(", ccs);
        }

        // keine Feen, also geht es jetzt los

        assertSameEvent("RoundBegin(1)", ccs);

        // das Wildschwein spawnt
        assertSameEvent("BoarSpawned(8, 2, 1)", ccs);

        // und das Wildschwein beginnt seinen Killing Spree
        // erstmal ein bisschen angreifen, weil da Leute im Weg stehen
        // das Schwein dreht sich im Kreis, bis es endlich laufen kann
        assertSameEvent("BoarAttack(8, 3)", ccs);
        assertSameEvent("BoarAttack(8, 1)", ccs);
        // das Schwein hat zwar schon zweimal angegrifen, aber noch keine Bewegung gemacht.
        // es kann also noch sechs Bewegungen, wie in jeder anderen Runde
        usualBoarMoves(ccs);


        // und jetzt sollte alle acht Spieler dran sein
        allPlayersMove(ccs);

        // das waren alle acht, also Rundenende
        assertSameEvent("RoundEnd(1, 0)", ccs);

        // Ifrit A4 hat 10 Schaden bekommen, ist also bei 77.

        // die nächsten 15 Runden sind alle gleich
        // dabei bekommt A4 5 * 15 = 75 Schaden
        for (int round = 2; round < 6; round++) {
            assertSameEvent(String.format("RoundBegin(%d)", round), ccs);

            usualBoarMoves(ccs);
            allPlayersMove(ccs);

            assertSameEvent(String.format("RoundEnd(%d, 0)", round), ccs);
        }

        // jetzt kommt Runde 17. A4 ist bei 2 Lebenspunkten.
        assertSameEvent("RoundBegin(6)", ccs);

        // das Schwein fängt an wie immer
        oneBoarLoop(ccs);
        // bei seiner zweiten Runde tötet es jemanden
        assertSameEvent("BoarAttack(8, 0)", ccs);
        assertSameEvent("BoarAttack(8, 2)", ccs);
        assertSameEvent("BoarAttack(8, 4)", ccs);
        // ah, da ist eine Lücke
        assertSameEvent("Moved(8, SOUTH_EAST)", ccs);
        // und wieder wer im Weg
        assertSameEvent("BoarAttack(8, 7)", ccs);
        assertSameEvent("BoarAttack(8, 5)", ccs);
        assertSameEvent("BoarAttack(8, 3)", ccs);
        // das ist A4, der jetzt stirbt
        assertSameEvent("Died(3)", ccs);
        // und das Schwein geht noch einen Schritt
        assertSameEvent("Moved(8, NORTH_WEST)", ccs);
        // und dann kommt seine nächste Runde
        assertSameEvent("BoarAttack(8, 0)", ccs);
        // Oh, wieder einer tot ...
        assertSameEvent("Died(0)", ccs);
        assertSameEvent("BoarAttack(8, 2)", ccs);
        // und noch einer
        assertSameEvent("Died(2)", ccs);
        assertSameEvent("BoarAttack(8, 4)", ccs);
        // ah, da ist eine Lücke
        assertSameEvent("Moved(8, SOUTH_EAST)", ccs);
        // und wieder wer im Weg
        assertSameEvent("BoarAttack(8, 7)", ccs);
        assertSameEvent("BoarAttack(8, 5)", ccs);
        // da A4 tot ist, kann das Schwein da jetzt hin gehen
        assertSameEvent("Moved(8, NORTH_EAST)", ccs);

        // es leben nurnoch 4 Monster
        for (int i = 0; i < 5; i++) {
            int actorId = receiveActNow(ccs);
            if (actorId <= 3) {
                // das ist einer aus Team A
                ccs[actorId].sendMove(Direction.NORTH_EAST);
                // da ist Felsen
                assertSameEventStartsWith(String.format("Kicked(%d,", actorId), ccs);
            } else {
                // das ist einer aus Team B
                ccs[actorId].sendDoneActing();
                assertSameEvent(String.format("DoneActing(%d)", actorId), ccs);
            }
        }

        assertSameEvent("RoundEnd(6, 0)", ccs);

        assertSameEvent("Winner(B)", ccs);
    }

    /**
     * Diese Methode assertet die Standardbewegungen, die das Schwein in diesem Test jede Runde macht.
     *
     * @param ccs
     */
    private void usualBoarMoves(ClientConnection<String>[] ccs) {
        // zwei Moves in der Schleife, das Boar macht 6 Bewegungen, also 3 Schleifendurchläufe
        for (int i = 0; i < 3; i++) {
            oneBoarLoop(ccs);
        }
    }

    private void oneBoarLoop(ClientConnection<String>[] ccs) {
        assertSameEvent("BoarAttack(8, 0)", ccs);
        assertSameEvent("BoarAttack(8, 2)", ccs);
        assertSameEvent("BoarAttack(8, 4)", ccs);
        // ah, da ist eine Lücke
        assertSameEvent("Moved(8, SOUTH_EAST)", ccs);
        // und wieder wer im Weg
        assertSameEvent("BoarAttack(8, 7)", ccs);
        assertSameEvent("BoarAttack(8, 5)", ccs);
        assertSameEvent("BoarAttack(8, 3)", ccs);
        // wieder eine Lücke
        assertSameEvent("Moved(8, NORTH_WEST)", ccs);
        // jetzt steht es so, dass es als nächstes 0 angreift, das ist dann in der nächsten Iteration der Schleife
    }

    @Override
    protected String getMapFile() {
        return    "##00#\n"
                + "#0.0#\n"
                + "##1 1\n"
                + "##11#";
    }

    @Override
    protected String getFightFile() {
        return "A,B\n\n"
                + "A,A1,Ifrit\n"
                + "A,A2,Ifrit\n"
                + "A,A3,Ifrit\n"
                + "A,A4,Ifrit\n"
                + "B,B1,naga\n"
                + "B,B2,naga\n"
                + "B,B3,naga\n"
                + "B,B4,naga\n";
    }
}
