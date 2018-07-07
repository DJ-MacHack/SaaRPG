package de.unisaarland.sopra.publictest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.MonsterType;
import de.unisaarland.sopra.comm.ClientConnection;

/**
 * Der BoarAttackOnWaterTest testet ob ein Schwein auch Gegner auf Wasser angreift.
 */
public class BoarAttackOnWaterTest extends PublicTest {


    public BoarAttackOnWaterTest() {
        super("BoarAttackOnWaterTest");
    }

    @Override
    protected String getMapFile() {
        return ".       ~0\n" +
               ".      ~1 ";
    }

    @Override
    protected String getFightFile() {
        return "A,B\n\n" + "A,Dobby,ELF\n" + "B,Flobby,ELF";
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void executeTest() {
        ClientConnection<String>[] ccs = new ClientConnection[2];
        ccs[0] = register(0, "Dobby", MonsterType.ELF, "A");
        ccs[1] = register(1, "Flobby", MonsterType.ELF, "B");

        assertSameEventStartsWith("Registered(", ccs);

        // Anfang Runde 1
        assertSameEvent("RoundBegin(1)", ccs);

        assertSameEvent("BoarSpawned(2, 0, 0)", ccs);
        assertSameEvent("BoarSpawned(3, 0, 1)", ccs);

        for (int j = 0; j < 6; j++) {
            assertSameEvent("Moved(2, EAST)", ccs);
        }
        for (int j = 0; j < 6; j++) {
            assertSameEvent("Moved(3, EAST)", ccs);
        }

        for (int i = 0; i < 2; i++) {
            assertSameEvent(String.format("ActNow(%d)", i), ccs);
            ccs[i].sendMove(Direction.WEST);
            assertSameEvent(String.format("Moved(%d, WEST)", i), ccs);
            assertSameEvent(String.format("ActNow(%d)", i), ccs);
            ccs[i].sendDoneActing();
            assertSameEvent(String.format("DoneActing(%d)", i), ccs);
        }
        
        // FeldEffekte werden in der Reihenfolge ihrer Ids angewendet
        assertSameEvent("FieldEffect(8, 0, 20)", ccs);
        assertSameEvent("FieldEffect(7, 1, 20)", ccs);
        
        assertSameEvent("RoundEnd(1, 0)", ccs);

        // Anfang Runde 2
        assertSameEvent("RoundBegin(2)", ccs);
        
        // erstes Schwein muss noch ein Feld laufen bevor es angreift.
        assertSameEvent("Moved(2, EAST)", ccs);
        assertSameEvent("BoarAttack(2, 0)", ccs);
        for (int i = 1; i < 6; i++) {
            assertSameEvent("Moved(2, WEST)", ccs);
        }
        
        // zweites Schwein greift erst an und dreht sich zum weiterlaufen;
        assertSameEvent("BoarAttack(3, 1)", ccs);
        assertSameEvent("Moved(3, NORTH_EAST)", ccs);
        for (int i = 1; i < 5; i++) {
            assertSameEvent("Moved(3, WEST)", ccs);
        }
        assertSameEvent("BoarAttack(3, 2)", ccs);
        assertSameEvent("Moved(3, SOUTH_WEST)", ccs);
        
        assertSameEvent("ActNow(1)", ccs);
        ccs[1].sendBite(Direction.WEST);
        assertSameEventStartsWith("Kicked(1, ", ccs);
        assertSameEvent("ActNow(0)", ccs);
        ccs[0].sendBite(Direction.WEST);
        assertSameEventStartsWith("Kicked(0, ", ccs);
        
        assertSameEvent("RoundEnd(2, 0)", ccs);
        
        assertSameEvent("Winner()", ccs);
        
    }
}
