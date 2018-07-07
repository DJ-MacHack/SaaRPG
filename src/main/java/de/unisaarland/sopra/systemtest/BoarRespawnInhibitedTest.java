package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 22.09.16.
 *
 * @author Henrik Paul Koehn
 */
public class BoarRespawnInhibitedTest extends OurSystemTest {

    public BoarRespawnInhibitedTest() {
        super("BoarRespawnInhibitedTest");
    }

    @Override
    protected void executeTest() {
        List<ClientConnection<Event>> ccs = new ArrayList<>();

        // Registration Phase
        ClientConnection<Event> kob = register(0, "a", CreatureType.KOBOLD, "Alpha", 0, 0);
        ClientConnection<Event> elf = register(1, "b", CreatureType.ELF, "Beta", 2, 0);
        assertRegisterEvent(kob.nextEvent(), 1, "b", CreatureType.ELF, "Beta", 2, 0);
        assertRegisterEvent(elf.nextEvent(), 0, "a", CreatureType.KOBOLD, "Alpha", 0, 0);
        ccs.add(kob);
        ccs.add(elf);

        // Round Begin
        assertRoundBegin(assertAndMerge(ccs), 1);

        // Spawn Boar
        assertBoarSpawned(assertAndMerge(ccs), 2, 1, 0);
        assertBoarAttack(assertAndMerge(ccs), 2, 1);
        assertBoarAttack(assertAndMerge(ccs), 2, 0);

        // Move Boars does not happen since not possible

        // Slash twice
        for (int i = 0; i < 2; i++){
            assertActNow(assertAndMerge(ccs), 0);
            kob.sendSlash(Direction.EAST);
            assertSlashed(assertAndMerge(ccs), 0, Direction.EAST);
        }
        assertDied(assertAndMerge(ccs), 2);
        // Move on BoarSpawn
        assertActNow(assertAndMerge(ccs), 0);
        kob.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(ccs), 0, Direction.EAST);

        // Done acting
        assertActNow(assertAndMerge(ccs), 0);
        kob.sendDoneActing();
        assertDoneActing(assertAndMerge(ccs), 0);

        // Next Done acting
        assertActNow(assertAndMerge(ccs), 1);
        elf.sendDoneActing();
        assertDoneActing(assertAndMerge(ccs), 1);

        assertRoundEnd(assertAndMerge(ccs), 1, 0);

        for (int round = 2; round < 15; round++){
            assertRoundBegin(assertAndMerge(ccs), round);

            // Both do nothing

            assertActNow(assertAndMerge(ccs), (round + 1) % 2);
            ccs.get((round + 1) % 2).sendDoneActing();
            assertDoneActing(assertAndMerge(ccs) ,(round + 1) % 2);

            assertActNow(assertAndMerge(ccs), round % 2);
            ccs.get(round % 2).sendDoneActing();
            assertDoneActing(assertAndMerge(ccs) ,round % 2);

            assertRoundEnd(assertAndMerge(ccs), round, round-1);
        }

        // Now move away from boar spawn

        assertRoundBegin(assertAndMerge(ccs), 15);

        assertActNow(assertAndMerge(ccs), 0);
        kob.sendMove(Direction.WEST);
        assertMoved(assertAndMerge(ccs), 0, Direction.WEST);

        // Done acting
        assertActNow(assertAndMerge(ccs), 0);
        kob.sendDoneActing();
        assertDoneActing(assertAndMerge(ccs), 0);

        // Next Done acting
        assertActNow(assertAndMerge(ccs), 1);
        elf.sendDoneActing();
        assertDoneActing(assertAndMerge(ccs), 1);

        assertRoundEnd(assertAndMerge(ccs), 15, 14);
        assertRoundBegin(assertAndMerge(ccs), 16);

        assertBoarSpawned(assertAndMerge(ccs), 2, 1, 0);
        passed();

    }

    @Override
    protected String getMapFile() {
        return "0X1";
    }

    @Override
    protected String getFightFile() {
        return "Alpha, Beta\nAlpha, a, Kobold\nBeta, b, Elf";
    }
}
