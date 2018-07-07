package de.unisaarland.sopra.publictest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.MonsterType;
import de.unisaarland.sopra.comm.ClientConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * The BoarRangeAttackTest tests whether a boar behaves legally when
 * he reaches his limit of 20 steps from the anchor point and 21 steps
 * away is a monster.
 * So the test has a map with a width of
 */
public class BoarAttackOnIceSameRound extends PublicTest {

    public BoarAttackOnIceSameRound() {
        super("BoarAttackOnIceSameRound");
    }

    @Override
    protected String getMapFile() {
        return  "##1\n" +
            "._0";
    }

    @Override
    protected String getFightFile() {
        return  "A,B\n\n" +
            "A,Dobby,ELF\n" +
            "B,Flobby,YETI";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void executeTest() {
        ClientConnection<String>[] ccs = new ClientConnection[2];
        ccs[0] = register(0, "Dobby", MonsterType.ELF, "A");
        ccs[1] = register(1, "Flobby", MonsterType.YETI, "B");

        assertSameEventStartsWith("Registered(", ccs);

        assertSameEvent("RoundBegin(1)", ccs);

        assertSameEvent("BoarSpawned(2, 0, 1)", ccs);

        assertSameEvent("Moved(2, EAST)", ccs);

        assertSameEvent("BoarAttack(2, 0)", ccs);

        assertSameEvent("BoarAttack(2, 1)", ccs);


        List<String> actNowList = new ArrayList<>();
        List<String> doneActingList = new ArrayList<>();
        for (int i = 0; i < ccs.length; i++) {
            actNowList.add(String.format("ActNow(%d)", i));
            doneActingList.add(String.format("DoneActing(%d)", i));
        }

        checkAndHandleLazyMonster(ccs, actNowList, doneActingList);

        assertSameEvent("RoundEnd(1, 0)", ccs);

        assertSameEvent("RoundBegin(2)", ccs);

        assertSameEvent("BoarAttack(2, 0)", ccs);

        assertSameEvent("BoarAttack(2, 1)", ccs);

        assertSameEvent("Moved(2, WEST)", ccs);

        assertSameEvent("Moved(2, EAST)", ccs);

        assertSameEvent("BoarAttack(2, 0)", ccs);

        assertSameEvent("BoarAttack(2, 1)", ccs);

        assertSameEvent("ActNow(1)", ccs);

        doInvalidMove(ccs);

        assertSameEvent("RoundEnd(2, 0)", ccs);

        assertSameEvent("Winner()", ccs);

    }

    private void doInvalidMove(ClientConnection<String>[] ccs){
        for (int j = 0; j < ccs.length; j++) {
            ClientConnection<String> cc = ccs[j];
            cc.sendBite(Direction.EAST);
            assertSameEventStartsWith("Kicked(", ccs);
        }
    }

}
