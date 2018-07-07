package de.unisaarland.sopra.publictest;

import de.unisaarland.sopra.MonsterType;
import de.unisaarland.sopra.comm.ClientConnection;

/**
 * Dieser Test testst wie Schweine auf Eis angreifen.
 *
 * Created by akampmann on 26/09/16.
 */
@SuppressWarnings("unchecked")
public class BoarAttackOnIceTest extends PublicTest {

    public BoarAttackOnIceTest() {
        super("BoarAttackOnIceTest");
    }

    @Override
    protected void executeTest() {
        // register alle und schreib die Connections in ein Array
        ClientConnection<String>[] ccs = new ClientConnection[5];
        ccs[0] = register(0, "A1", MonsterType.ELF, "A");
        ccs[1] = register(1, "A2", MonsterType.ELF, "A");
        ccs[2] = register(2, "A3", MonsterType.ELF, "A");

        ccs[3] = register(3, "B1", MonsterType.ELF, "B");
        ccs[4] = register(4, "B2", MonsterType.ELF, "B");

        // alle bekommen die Registrierungen der anderen
        // das heißt es kommen noch 7 Registrierungen, deren Korrektheit ich nicht prüfe
        for (int i = 0; i < 4; i++) {
            assertSameEventStartsWith("Registered(", ccs);
        }

        assertSameEvent("RoundBegin(1)", ccs);

        // das Wildschwein erscheint
        assertSameEvent("BoarSpawned(5, 0, 1)", ccs);

        // es läuft zwei Schritte
        assertSameEvent("Moved(5, EAST)", ccs);
        assertSameEvent("Moved(5, EAST)", ccs);

        // dann greift es an
        assertSameEvent("BoarAttack(5, 2)", ccs);

        // auch auf Eis greift es Umstehende an
        assertSameEvent("BoarAttack(5, 1)", ccs);
        assertSameEvent("BoarAttack(5, 0)", ccs);
        assertSameEvent("BoarAttack(5, 3)", ccs);
        assertSameEvent("BoarAttack(5, 4)", ccs);

        allPlayersMove(ccs);

        assertSameEvent("RoundEnd(1, 0)", ccs);

        for(int round = 2; round <= 10; round ++) {

            assertSameEvent(String.format("RoundBegin(%d)", round), ccs);

            // das Wildschwein greift an
            assertSameEvent("BoarAttack(5, 2)", ccs);

            // auch auf Eis greift es Umstehende an
            assertSameEvent("BoarAttack(5, 1)", ccs);
            assertSameEvent("BoarAttack(5, 0)", ccs);

            // da es noch keine Richtung hatte, lāuft es jetzt vier Schritte
            assertSameEvent("Moved(5, WEST)", ccs);
            assertSameEvent("Moved(5, WEST)", ccs);
            // auf Gras darf es drehen
            assertSameEvent("Moved(5, EAST)", ccs);
            assertSameEvent("Moved(5, EAST)", ccs);

            // dann greift es an
            assertSameEvent("BoarAttack(5, 2)", ccs);

            // auch auf Eis greift es Umstehende an
            assertSameEvent("BoarAttack(5, 1)", ccs);
            assertSameEvent("BoarAttack(5, 0)", ccs);
            assertSameEvent("BoarAttack(5, 3)", ccs);
            assertSameEvent("BoarAttack(5, 4)", ccs);

            allPlayersMove(ccs);

            assertSameEvent(String.format("RoundEnd(%d, 0)", round), ccs);
        }

        assertSameEvent("RoundBegin(11)", ccs);
        // Eine neue Runde beginnt -- das Schwein kann sich also frei bewegen, es sucht sich eine Richtung, in die es gehen kann.

        // Zuerst ist das die alte Richtung -- EAST, diese ist aber versperrt -- also greift es an
        assertSameEvent("BoarAttack(5, 2)", ccs);
        assertSameEvent("Died(2)", ccs);

        // auch auf Eis greift es Umstehende an und dreht sich weiter nach links
        assertSameEvent("BoarAttack(5, 1)", ccs);
        assertSameEvent("Died(1)", ccs);
        assertSameEvent("BoarAttack(5, 0)", ccs);
        assertSameEvent("Died(0)", ccs);

        // es findet nun wieder den weg nach WEST
        assertSameEvent("Moved(5, WEST)", ccs);
        assertSameEvent("Moved(5, WEST)", ccs);
        // auf Gras darf es drehen
        assertSameEvent("Moved(5, EAST)", ccs);
        assertSameEvent("Moved(5, EAST)", ccs);
        // das Feld ist nun frei
        assertSameEvent("Moved(5, EAST)", ccs);
        // steinwand -- also drehen
        assertSameEvent("Moved(5, NORTH_WEST)", ccs);
        // und das waren 6 schritte

        assertSameEvent("ActNow(3)", ccs);
        ccs[3].sendDoneActing();
        assertSameEvent("DoneActing(3)", ccs);

        assertSameEvent("ActNow(4)", ccs);
        ccs[4].sendDoneActing();
        assertSameEvent("DoneActing(4)", ccs);

        assertSameEvent("RoundEnd(11, 0)", ccs);

        assertSameEvent("Winner(B)", ccs);
    }

    @Override
    protected String getMapFile() {
        return "##00\n" +
               ".__0\n" +
               "##11";
    }

    @Override
    protected String getFightFile() {
        return "A,B\n\n"
            + "A,A1,Elf\n"
            + "A,A2,Elf\n"
            + "A,A3,Elf\n"
            + "B,B1,Elf\n"
            + "B,B2,Elf\n";
    }
}
