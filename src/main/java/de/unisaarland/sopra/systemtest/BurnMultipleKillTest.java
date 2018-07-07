package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Died;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 22.09.16.
 *
 * @author Henrik Paul Koehn
 */
public class BurnMultipleKillTest extends OurSystemTest {

    public BurnMultipleKillTest () {
        super("BurnMultipleKillTest");
    }

    @Override
    protected void executeTest() {
        List<ClientConnection<Event>> ccs = new ArrayList<>();

        // Registration Phase
        ClientConnection<Event> ko1 = register(0, "b", CreatureType.KOBOLD, "B", 1, 0);
        ClientConnection<Event> ko2 = register(1, "c", CreatureType.KOBOLD, "B", 2, 0);
        assertRegisterEvent(ko1.nextEvent(), 1, "c", CreatureType.KOBOLD, "B", 2, 0);
        assertRegisterEvent(ko2.nextEvent(), 0, "b", CreatureType.KOBOLD, "B", 1, 0);

        ccs.add(ko1);
        ccs.add(ko2);

        ClientConnection<Event> ifb = register(2, "d", CreatureType.IFRIT, "B", 0, 1);
        assertRegisterEvent(assertAndMerge(ccs), 2, "d", CreatureType.IFRIT, "B", 0, 1);
        assertRegisterEvent(ifb.nextEvent(), 0, "b", CreatureType.KOBOLD, "B", 1, 0);
        assertRegisterEvent(ifb.nextEvent(), 1, "c", CreatureType.KOBOLD, "B", 2, 0);

        ccs.add(ifb);

        ClientConnection<Event> ifa = register(3, "a", CreatureType.IFRIT, "A", 1, 1);
        assertRegisterEvent(assertAndMerge(ccs), 3, "a", CreatureType.IFRIT, "A", 1, 1);
        assertRegisterEvent(ifa.nextEvent(), 0, "b", CreatureType.KOBOLD, "B", 1, 0);
        assertRegisterEvent(ifa.nextEvent(), 1, "c", CreatureType.KOBOLD, "B", 2, 0);
        assertRegisterEvent(ifa.nextEvent(), 2, "d", CreatureType.IFRIT, "B", 0, 1);

        ccs.add(ifa);

        // No fairies or boars

        int round;
        int targetround = 23;
        for (round = 1; round < targetround; round++){
            assertRoundBegin(assertAndMerge(ccs), round);

            // Automation of turns team b does nothing and ifa burns everyone until ifb dies

            for (int teamb = 0; teamb < 3; teamb++){
                int chosen = teamb;
                assertActNow(assertAndMerge(ccs), chosen);
                ccs.get(chosen).sendDoneActing();
                assertDoneActing(assertAndMerge(ccs), (chosen));
            }
            assertActNow(assertAndMerge(ccs), 3);
            ifa.sendBurn();
            assertBurned(assertAndMerge(ccs), 3);

            if(round == targetround - 1){
                // Now ifb shall die
                assertDied(assertAndMerge(ccs), 2);
            }

            assertActNow(assertAndMerge(ccs), 3);
            ifa.sendDoneActing();
            assertDoneActing(assertAndMerge(ccs), 3);

            assertRoundEnd(assertAndMerge(ccs), round, 0);
        }

        // Now after one died the procedure is repeated
        for (;round < targetround + 2; round++){
            assertRoundBegin(assertAndMerge(ccs), round);
            for (int teamb = 0; teamb < 2; teamb++){
                int chosen = teamb;
                assertActNow(assertAndMerge(ccs), chosen);
                ccs.get(chosen).sendDoneActing();
                assertDoneActing(assertAndMerge(ccs), chosen);
            }
            // He burns
            assertActNow(assertAndMerge(ccs), 3);
            ifa.sendBurn();
            assertBurned(assertAndMerge(ccs), 3);
            if(round == targetround + 1){
                // Now check if two died events were received
                for (int i = 0; i < 2; i++) {
                    Event newone = assertAndMerge(ccs);
                    if (newone instanceof Died) {
                        Died died = (Died) newone;
                        if (died.getEntityId() == 1 || died.getEntityId() == 0){
                            passed();
                        }
                        else{
                            failed("wrong id");
                            break;
                        }
                    }
                    else{
                        failed("Wrong Event");
                        break;
                    }
                }
            }
            assertActNow(assertAndMerge(ccs), 3);
            ifa.sendDoneActing();
            assertDoneActing(assertAndMerge(ccs), 3);

            assertRoundEnd(assertAndMerge(ccs), round, 0);
        }
        assertWinner(assertAndMerge(ccs), "A");
    }

    @Override
    protected String getMapFile() {
        return " 11\n" +
                "10 ";
    }

    @Override
    protected String getFightFile() {
        return "A, B\n" +
                "A, a, Ifrit\n" +
                "B, b, Kobold\n" +
                "B, c, Kobold\n" +
                "B, d, Ifrit";
    }
}
