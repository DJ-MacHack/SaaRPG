package de.unisaarland.sopra.publictest;

import de.unisaarland.sopra.MonsterType;
import de.unisaarland.sopra.comm.ClientConnection;

/**
 * Achtung, in diesem Test begegnen Sie einem panischen Schwein.
 * <p>
 * Created by akampmann on 08/09/16.
 */
@SuppressWarnings("unchecked")
public class AmokBoar2Test extends PublicTest {

    public AmokBoar2Test() {
        super("AmokBoar2Test");
    }

    @Override
    protected void executeTest() {
        // register alle und schreib die Connections in ein Array
        ClientConnection<String>[] ccs = new ClientConnection[6];
        ccs[0] = register(0, "A1", MonsterType.ELF, "A");
        ccs[1] = register(1, "A2", MonsterType.ELF, "A");
        ccs[2] = register(2, "A3", MonsterType.ELF, "A");
        ccs[3] = register(3, "A4", MonsterType.ELF, "A");
        ccs[4] = register(4, "B1", MonsterType.ELF, "B");
        ccs[5] = register(5, "B2", MonsterType.ELF, "B");

        // alle bekommen die Registrierungen der anderen
        // das heißt es kommen noch 5 Registrierungen, deren Korrektheit ich nicht prüfe
        for (int i = 0; i < 5; i++) {
            assertSameEventStartsWith("Registered(", ccs);
        }

        // keine Feen, also geht es jetzt los


        assertSameEvent("RoundBegin(1)", ccs);

        // das Wildschwein spawnt
        assertSameEvent("BoarSpawned(6, 1, 1)", ccs);

        // und das Wildschwein beginnt seinen Killing Spree
        // erstmal ein bisschen angreifen, weil da Leute im Weg stehen
        // das Schwein dreht sich im Kreis
        boarAttacks(ccs);

        // und jetzt sollten alle sechs Spieler dran sein
        allPlayersMove(ccs);

        // das waren alle sechs, also Rundenende
        assertSameEvent("RoundEnd(1, 0)", ccs);

        for(int round = 2; round <= 19; round ++) {
            assertSameEvent(String.format("RoundBegin(%d)", round), ccs);

            // und das Wildschwein beginnt seinen Killing Spree
            // erstmal ein bisschen angreifen, weil da Leute im Weg stehen
            // das Schwein dreht sich im Kreis
            boarAttacks(ccs);

            // und jetzt sollte alle sechs Spieler dran sein
            allPlayersMove(ccs);

            // das waren alle sechs, also Rundenende
            assertSameEvent(String.format("RoundEnd(%d, 0)", round), ccs);
        }

        assertSameEvent("RoundBegin(20)", ccs);

        assertSameEvent("BoarAttack(6, 3)", ccs);
        assertSameEvent("Died(3)", ccs);

        assertSameEvent("BoarAttack(6, 1)", ccs);
        assertSameEvent("Died(1)", ccs);

        assertSameEvent("BoarAttack(6, 0)", ccs);
        assertSameEvent("Died(0)", ccs);

        assertSameEvent("BoarAttack(6, 2)", ccs);
        assertSameEvent("Died(2)", ccs);

        assertSameEvent("BoarAttack(6, 4)", ccs);
        assertSameEvent("Died(4)", ccs);

        assertSameEvent("BoarAttack(6, 5)", ccs);
        assertSameEvent("Died(5)", ccs);

        assertSameEvent("RoundEnd(20, 0)", ccs);

        assertSameEvent("Winner()", ccs);
    }

    private void boarAttacks(ClientConnection<String>[] ccs) {
        assertSameEvent("BoarAttack(6, 3)", ccs);
        assertSameEvent("BoarAttack(6, 1)", ccs);
        assertSameEvent("BoarAttack(6, 0)", ccs);
        assertSameEvent("BoarAttack(6, 2)", ccs);
        assertSameEvent("BoarAttack(6, 4)", ccs);
        assertSameEvent("BoarAttack(6, 5)", ccs);
    }

    @Override
    protected String getMapFile() {
        return    "#00\n"
                + "0.0\n"
                + "#11";
    }

    @Override
    protected String getFightFile() {
        return "A,B\n\n"
                + "A,A1,Elf\n"
                + "A,A2,Elf\n"
                + "A,A3,Elf\n"
                + "A,A4,Elf\n"
                + "B,B1,Elf\n"
                + "B,B2,Elf\n";
    }
}
