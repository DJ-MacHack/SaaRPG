package de.unisaarland.sopra.publictest;

import de.unisaarland.sopra.MonsterType;
import de.unisaarland.sopra.comm.ClientConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Der BoarSurroundedTest prüft ob ein, von Spielern umzingeltes, Schwein sich 
 * im Kreis dreht und dabei jeden Gegner einmal angreift.
 */
public class BoarSurroundedTest extends PublicTest {

    public BoarSurroundedTest() {
        super("BoarSurroundedTest");
    }

    @Override
    protected String getMapFile() {
        return  " 00\n" +
                 "1.0\n" +
                " 10";
    }

    @Override
    protected String getFightFile() {
        return  "A,B\n\n" +
            "A,Player1,ELF\n" +
            "A,Player2,ELF\n" +
            "A,Player3,ELF\n" +
            "A,Player4,ELF\n" +
            "B,Player5,ELF\n" +
            "B,Player6,ELF\n";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void executeTest() {
        ClientConnection<String>[] ccs = new ClientConnection[6];
        ccs[0] = register(0, "Player1", MonsterType.ELF, "A");
        ccs[1] = register(1, "Player2", MonsterType.ELF, "A");
        ccs[2] = register(2, "Player3", MonsterType.ELF, "A");
        ccs[3] = register(3, "Player4", MonsterType.ELF, "A");
        ccs[4] = register(4, "Player5", MonsterType.ELF, "B");
        ccs[5] = register(5, "Player6", MonsterType.ELF, "B");

        List<String> actNowList = new ArrayList<>();
        List<String> doneActingList = new ArrayList<>();
        for (int i = 0; i < ccs.length; i++) {
            actNowList.add(String.format("ActNow(%d)", i));
            doneActingList.add(String.format("DoneActing(%d)", i));
        }

        assertSameEventStartsWith("Registered(", ccs);
        assertSameEventStartsWith("Registered(", ccs);
        assertSameEventStartsWith("Registered(", ccs);
        assertSameEventStartsWith("Registered(", ccs);
        assertSameEventStartsWith("Registered(", ccs);

        assertSameEvent("RoundBegin(1)", ccs);

        assertSameEvent("BoarSpawned(6, 1, 1)", ccs);


        for (int i = 0; i < (100 / 5) - 1; i++) {
            assertSameEvent("BoarAttack(6, 2)", ccs);
            assertSameEvent("BoarAttack(6, 1)", ccs);
            assertSameEvent("BoarAttack(6, 0)", ccs);
            assertSameEvent("BoarAttack(6, 4)", ccs);
            assertSameEvent("BoarAttack(6, 5)", ccs);
            assertSameEvent("BoarAttack(6, 3)", ccs);

            checkAndHandleLazyMonster(ccs, actNowList, doneActingList);

            assertSameEvent(String.format("RoundEnd(%d, %d)", i + 1, 0), ccs);
            assertSameEvent(String.format("RoundBegin(%d)", i + 2), ccs);
        }

        assertSameEvent("BoarAttack(6, 2)", ccs);
        assertSameEvent("Died(2)", ccs);
        assertSameEvent("BoarAttack(6, 1)", ccs);
        assertSameEvent("Died(1)", ccs);
        assertSameEvent("BoarAttack(6, 0)", ccs);
        assertSameEvent("Died(0)", ccs);
        assertSameEvent("BoarAttack(6, 4)", ccs);
        assertSameEvent("Died(4)", ccs);
        assertSameEvent("BoarAttack(6, 5)", ccs);
        assertSameEvent("Died(5)", ccs);
        assertSameEvent("BoarAttack(6, 3)", ccs);
        assertSameEvent("Died(3)", ccs);

        assertSameEvent("RoundEnd(20, 0)", ccs);

        assertSameEventStartsWith("Winner(", ccs);
    }

}
