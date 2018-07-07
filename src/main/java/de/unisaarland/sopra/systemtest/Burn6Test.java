package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Died;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created on 23.09.16.
 *
 * @author Henrik Paul Koehn
 */
public class Burn6Test extends OurSystemTest {

    public Burn6Test() {
        super("Burn6Test");
    }

    @Override
    protected void executeTest() {
        List<Integer> idList = new LinkedList<>();
        for (int k = 0; k < 6; k++) {
            idList.add(k);
        }

        List<ClientConnection<Event>> ccs = new ArrayList<>();

        ClientConnection<Event> rooko = register(0, "rooko", CreatureType.ROOK, "b", 1, 0);
        ccs.add(rooko);
        ClientConnection<Event> kobolto = register(1, "kobolto", CreatureType.KOBOLD, "b", 2, 0);
        ccs.add(kobolto);

        assertRegisterEvent(rooko.nextEvent(), 1, "kobolto", CreatureType.KOBOLD, "b", 2, 0);
        assertRegisterEvent(kobolto.nextEvent(), 0, "rooko", CreatureType.ROOK, "b", 1, 0);

        ClientConnection<Event> yetio = register(2, "yetio", CreatureType.YETI, "b", 0, 1);
        assertRegisterEvent(yetio.nextEvent(), 0, "rooko", CreatureType.ROOK, "b", 1, 0);
        assertRegisterEvent(yetio.nextEvent(), 1, "kobolto", CreatureType.KOBOLD, "b", 2, 0);
        assertRegisterEvent(assertAndMerge(ccs), 2, "yetio", CreatureType.YETI, "b", 0, 1);
        ccs.add(yetio);


        ClientConnection<Event> nagao = register(3, "nagao", CreatureType.NAGA, "b", 2, 1);
        assertRegisterEvent(assertAndMerge(ccs), 3, "nagao", CreatureType.NAGA, "b", 2, 1);
        assertRegisterEvent(nagao.nextEvent(), 0, "rooko", CreatureType.ROOK, "b", 1, 0);
        assertRegisterEvent(nagao.nextEvent(), 1, "kobolto", CreatureType.KOBOLD, "b", 2, 0);
        assertRegisterEvent(nagao.nextEvent(), 2, "yetio", CreatureType.YETI, "b", 0, 1);
        ccs.add(nagao);

        ClientConnection<Event> elfo = register(4, "elfo", CreatureType.ELF, "b", 0, 2);
        assertRegisterEvent(assertAndMerge(ccs), 4, "elfo", CreatureType.ELF, "b", 0, 2);
        assertRegisterEvent(elfo.nextEvent(), 0, "rooko", CreatureType.ROOK, "b", 1, 0);
        assertRegisterEvent(elfo.nextEvent(), 1, "kobolto", CreatureType.KOBOLD, "b", 2, 0);
        assertRegisterEvent(elfo.nextEvent(), 2, "yetio", CreatureType.YETI, "b", 0, 1);
        assertRegisterEvent(elfo.nextEvent(), 3, "nagao", CreatureType.NAGA, "b", 2, 1);
        ccs.add(elfo);

        ClientConnection<Event> wraitho = register(5, "wraitho", CreatureType.WRAITH, "b", 1, 2);
        assertRegisterEvent(assertAndMerge(ccs), 5, "wraitho", CreatureType.WRAITH, "b", 1, 2);
        assertRegisterEvent(wraitho.nextEvent(), 0, "rooko", CreatureType.ROOK, "b", 1, 0);
        assertRegisterEvent(wraitho.nextEvent(), 1, "kobolto", CreatureType.KOBOLD, "b", 2, 0);
        assertRegisterEvent(wraitho.nextEvent(), 2, "yetio", CreatureType.YETI, "b", 0, 1);
        assertRegisterEvent(wraitho.nextEvent(), 3, "nagao", CreatureType.NAGA, "b", 2, 1);
        assertRegisterEvent(wraitho.nextEvent(), 4, "elfo", CreatureType.ELF, "b", 0, 2);
        ccs.add(wraitho);

        ClientConnection<Event> burna = register(6, "burna", CreatureType.IFRIT, "a", 1, 1);
        assertRegisterEvent(assertAndMerge(ccs), 6, "burna", CreatureType.IFRIT, "a", 1, 1);
        assertRegisterEvent(burna.nextEvent(), 0, "rooko", CreatureType.ROOK, "b", 1, 0);
        assertRegisterEvent(burna.nextEvent(), 1, "kobolto", CreatureType.KOBOLD, "b", 2, 0);
        assertRegisterEvent(burna.nextEvent(), 2, "yetio", CreatureType.YETI, "b", 0, 1);
        assertRegisterEvent(burna.nextEvent(), 3, "nagao", CreatureType.NAGA, "b", 2, 1);
        assertRegisterEvent(burna.nextEvent(), 4, "elfo", CreatureType.ELF, "b", 0, 2);
        assertRegisterEvent(burna.nextEvent(), 5, "wraitho", CreatureType.WRAITH, "b", 1, 2);
        ccs.add(burna);

        int round;
        final int  targetround = 50;
        for (round = 1; round <= targetround; round++){
            assertRoundBegin(assertAndMerge(ccs), round);

            for (int id = 0; id < 7; id++) {
                if (id == 6){
                    assertActNow(assertAndMerge(ccs), id);
                    ccs.get(id).sendBurn();
                    assertBurned(assertAndMerge(ccs), id);

                    if (round == targetround){
                        // Now everony but yeti dies, so gonna check this
                        for (int i = 0; i < 5; i++) {
                            Event newone = assertAndMerge(ccs);
                            if (newone instanceof Died) {
                                Died died = (Died) newone;
                                if (idList.contains(died.getEntityId())){
                                    idList.remove(Integer.valueOf(died.getEntityId()));
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
                        // Now check if Yeti is the sole survivor
                        if (!(idList.size() == 1 && idList.get(0).equals(2))){
                            failed("Killed wrong monster");
                        }
                    }
                }
                assertActNow(assertAndMerge(ccs), id);
                ccs.get(id).sendDoneActing();
                assertDoneActing(assertAndMerge(ccs), id);
            }
            assertRoundEnd(assertAndMerge(ccs), round, 0);
        }

        int newtargetround = targetround + 9;

        for(; round <= newtargetround; round++) {
            assertRoundBegin(assertAndMerge(ccs), round);
            //act phase
            for (int i = 0; i < 2 ; i++){
                int chosen = 2 + 4 * i;
                assertActNow(assertAndMerge(ccs), chosen);
                if (i == 1){
                    burna.sendBurn();
                    assertBurned(assertAndMerge(ccs), chosen);
                    if (round == newtargetround) {
                        assertDied(assertAndMerge(ccs), 2);
                    }
                    assertActNow(assertAndMerge(ccs), chosen);
                }
                ccs.get(chosen).sendDoneActing();
                assertDoneActing(assertAndMerge(ccs), chosen);
            }
            assertRoundEnd(assertAndMerge(ccs), round, 0);
        }
        assertWinner(assertAndMerge(ccs),"a");
    }

    @Override
    protected String getMapFile() {
        return "#11#\n" +
                "101#\n" +
               "#11#";
    }

    @Override
    protected String getFightFile() {
        return "a, b\n" +
               "a, burna, Ifrit\n" +
               "b, rooko, Rook\n" +
               "b, kobolto, Kobold\n" +
               "b, yetio, Yeti\n" +
               "b, nagao, Naga\n" +
               "b, elfo, Elf\n" +
               "b, wraitho, Wraith";
    }
}
