package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Team14 on 23/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class BurnFourTargetsTest extends OurSystemTest {
    public BurnFourTargetsTest() {
        super("BurnFourTargetsTest");
    }

    @Override
    protected void executeTest() {
        // register phase
        List<ClientConnection<Event>> clientList = new ArrayList<>();
        ClientConnection<Event> vashjir = register(0, "Vashjir", CreatureType.NAGA, "Water", 1, 0);
        clientList.add(vashjir);

        ClientConnection<Event> hakkar = register(1, "Hakkar", CreatureType.WRAITH, "Void", 0, 1);
        clientList.add(hakkar);
        assertRegisterEvent(vashjir.nextEvent(), 1, "Hakkar", CreatureType.WRAITH, "Void", 0, 1);
        assertRegisterEvent(hakkar.nextEvent(), 0, "Vashjir", CreatureType.NAGA, "Water", 1, 0);

        ClientConnection<Event> tyrande = register(2, "Tyrande", CreatureType.ELF, "Wood", 2, 1);
        assertRegisterEvent(assertAndMerge(clientList), 2, "Tyrande", CreatureType.ELF, "Wood", 2, 1);
        assertRegisterEvent(tyrande.nextEvent(), 0, "Vashjir", CreatureType.NAGA, "Water", 1, 0);
        assertRegisterEvent(tyrande.nextEvent(), 1, "Hakkar", CreatureType.WRAITH, "Void", 0, 1);
        clientList.add(tyrande);

        ClientConnection<Event> therazane = register(3, "Therazane", CreatureType.KOBOLD, "Earth", 0, 2);
        assertRegisterEvent(assertAndMerge(clientList), 3, "Therazane", CreatureType.KOBOLD, "Earth", 0, 2);
        assertRegisterEvent(therazane.nextEvent(), 0, "Vashjir", CreatureType.NAGA, "Water", 1, 0);
        assertRegisterEvent(therazane.nextEvent(), 1, "Hakkar", CreatureType.WRAITH, "Void", 0, 1);
        assertRegisterEvent(therazane.nextEvent(), 2, "Tyrande", CreatureType.ELF, "Wood", 2, 1);
        clientList.add(therazane);

        ClientConnection<Event> ragnaros = register(4, "Ragnaros", CreatureType.IFRIT, "Fire", 1, 1);
        assertRegisterEvent(assertAndMerge(clientList), 4, "Ragnaros", CreatureType.IFRIT, "Fire", 1, 1);
        assertRegisterEvent(ragnaros.nextEvent(), 0, "Vashjir", CreatureType.NAGA, "Water", 1, 0);
        assertRegisterEvent(ragnaros.nextEvent(), 1, "Hakkar", CreatureType.WRAITH, "Void", 0, 1);
        assertRegisterEvent(ragnaros.nextEvent(), 2, "Tyrande", CreatureType.ELF, "Wood", 2, 1);
        assertRegisterEvent(ragnaros.nextEvent(), 3, "Therazane", CreatureType.KOBOLD, "Earth", 0, 2);
        clientList.add(ragnaros);

        for (int round = 1; round < 34; round++) {
            // round begins
            assertRoundBegin(assertAndMerge(clientList), round);

            // acting phase all passive players
            for (int actingId = 0; actingId < 4; actingId++) {
                assertActNow(assertAndMerge(clientList), actingId);
                clientList.get(actingId).sendDoneActing();
                assertDoneActing(assertAndMerge(clientList), actingId);
            }

            // ragnaros' turn: Let's burn some $%&@=!
            assertActNow(assertAndMerge(clientList), 4);
            ragnaros.sendBurn();
            assertBurned(assertAndMerge(clientList), 4);
            assertActNow(assertAndMerge(clientList), 4);
            ragnaros.sendDoneActing();
            assertDoneActing(assertAndMerge(clientList), 4);

            // round i ends
            assertRoundEnd(assertAndMerge(clientList), round, 0);
        }

        // round 34 begins
        assertRoundBegin(assertAndMerge(clientList), 34);

        // acting phase all passive players
        for (int actingId = 0; actingId < 4; actingId++) {
            assertActNow(assertAndMerge(clientList), actingId);
            clientList.get(actingId).sendDoneActing();
            assertDoneActing(assertAndMerge(clientList), actingId);
        }

        // ragnaros' turn: Let's burn some $%&@=!
        assertActNow(assertAndMerge(clientList), 4);
        ragnaros.sendBurn();
        assertBurned(assertAndMerge(clientList), 4);
        for (int actingId = 0; actingId < 4; actingId++) {
            assertDied(assertAndMerge(clientList), actingId);
        }
        assertActNow(assertAndMerge(clientList), 4);
        ragnaros.sendDoneActing();
        assertDoneActing(assertAndMerge(clientList), 4);

        // round 34 ends
        assertRoundEnd(assertAndMerge(clientList), 34, 0);

        assertWinner(assertAndMerge(clientList), "Fire");
    }

    @Override
    protected String getMapFile() {
        return " 1 \n"
             + "203\n"
             + " 4 ";
    }

    @Override
    protected String getFightFile() {
        return "Fire, Water, Void, Wood, Earth\n"
                + "Fire, Ragnaros, Ifrit\n"
                + "Water, Vashjir, Naga\n"
                + "Void, Hakkar, Wraith\n"
                + "Wood, Tyrande, Elf\n"
                + "Earth, Therazane, Kobold";
    }
}
