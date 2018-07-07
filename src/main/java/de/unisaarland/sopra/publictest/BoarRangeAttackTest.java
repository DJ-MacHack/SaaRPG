package de.unisaarland.sopra.publictest;

import de.unisaarland.sopra.MonsterType;
import de.unisaarland.sopra.comm.ClientConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * The BoarRangeAttackTest tests whether a boar behaves legally when
 * he reaches his limit of 20 steps from the anchor point and 21 steps
 * away is a monster.
 */
public class BoarRangeAttackTest extends PublicTest {

    public BoarRangeAttackTest() {
        super(BoarRangeAttackTest.class.getName());
    }

    @Override
    protected String getMapFile() {
        return  ".                    0\n" +
            "1111111111111111111111";
    }

    @Override
    protected String getFightFile() {
        return  "A,B\n\n" +
            "A,Dobby,ELF\n" +
            "B,Flobby,ELF";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void executeTest() {
        ClientConnection<String>[] ccs = new ClientConnection[2];
        ccs[0] = register(0, "Dobby", MonsterType.ELF, "A");
        ccs[1] = register(1, "Flobby", MonsterType.ELF, "B");

        List<String> actNowList = new ArrayList<>();
        List<String> doneActingList = new ArrayList<>();
        for (int i = 0; i < ccs.length; i++) {
            actNowList.add(String.format("ActNow(%d)", i));
            doneActingList.add(String.format("DoneActing(%d)", i));
        }

        assertSameEventStartsWith("Registered(", ccs);

        assertSameEvent("RoundBegin(1)", ccs);

        assertSameEvent("BoarSpawned(2, 0, 0)", ccs);


        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 6; j++) {
                assertSameEvent("Moved(2, EAST)", ccs);
            }

            checkAndHandleLazyMonster(ccs, actNowList, doneActingList);

            assertSameEvent(String.format("RoundEnd(%d, %d)", i + 1, i + 1), ccs);

            assertSameEvent(String.format("RoundBegin(%d)", i + 2), ccs);

        }

        for (int j = 0; j < 2; j++) {
            assertSameEvent("Moved(2, EAST)", ccs);
        }
        // The boar went 20 steps in the EAST direction, now one monster is reachable
        // so he attacks it and continues on the WEST direction
        assertSameEvent("BoarAttack(2, 0)", ccs);

        for (int j = 0; j < 4; j++) {
            assertSameEvent("Moved(2, WEST)", ccs);
        }

        assertSameEvent("ActNow(1)", ccs);
        ccs[1].sendBurn();
        assertSameEventStartsWith("Kicked(1, ", ccs);
        assertSameEvent("ActNow(0)", ccs);
        ccs[0].sendBurn();
        assertSameEventStartsWith("Kicked(0, ", ccs);

        assertSameEvent("RoundEnd(4, 0)", ccs);

        assertSameEventStartsWith("Winner(", ccs);

    }

}
