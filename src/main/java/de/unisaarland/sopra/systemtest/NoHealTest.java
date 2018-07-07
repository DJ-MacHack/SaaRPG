package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created on 20.09.16.
 *
 * @author Henrik Paul Köhn
 */
public class NoHealTest extends OurSystemTest {

    public NoHealTest(){
        super("NoHealTest");
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> lux = register(0, "Lux", CreatureType.ELF, "Light", 6, 0);
        ClientConnection<Event> noc = register(1, "Nocturne", CreatureType.WRAITH, "Shadow", 7, 0);
        assertRegisterEvent(lux.nextEvent(), 1, "Nocturne", CreatureType.WRAITH, "Shadow", 7, 0);
        assertRegisterEvent(noc.nextEvent(), 0, "Lux", CreatureType.ELF, "Light", 6, 0);

        //Spawn Fairy
        assertFairySpawned(assertAndMerge(lux, noc), 2, 5, 0);

        //Round begin
        assertRoundBegin(assertAndMerge(lux, noc), 1);

        //Move Fairy
        for (int i = 0; i < 4;  i++){
            assertMoved(assertAndMerge(lux, noc), 2, Direction.WEST);
        }

        //Act phase: lux moves west, noc does nothing
        //lux
        assertActNow(assertAndMerge(lux, noc), 0);
        lux.sendMove(Direction.WEST);
        assertMoved(assertAndMerge(lux, noc), 0, Direction.WEST);
        assertActNow(assertAndMerge(lux, noc), 0);
        lux.sendDoneActing();
        assertDoneActing(assertAndMerge(lux, noc), 0);
        //noc
        assertActNow(assertAndMerge(lux, noc), 1);
        noc.sendDoneActing();
        assertDoneActing(assertAndMerge(lux, noc), 1);

        // Round end since lux has full health (no heal event)
        assertRoundEnd(assertAndMerge(lux, noc), 1, 1);

        // Now we wanna check if the heal heals if lux gets damage
        assertRoundBegin(assertAndMerge(lux, noc), 2);
        // Move Fairy
        assertMoved(assertAndMerge(lux, noc), 2, Direction.WEST);
        for (int i = 0; i < 3;  i++){
            assertMoved(assertAndMerge(lux, noc), 2, Direction.EAST);
        }
        // Now it is nocturnes turn since lux moved last time
        assertActNow(assertAndMerge(lux, noc), 1);
        // Moves next to lux
        noc.sendMove(Direction.WEST);
        assertMoved(assertAndMerge(lux, noc), 1, Direction.WEST);
        assertActNow(assertAndMerge(lux, noc), 1);
        // And stabs her
        noc.sendStab(Direction.WEST);
        assertStabbed(assertAndMerge(lux, noc), 1, Direction.WEST);
        // Then he is done
        assertActNow(assertAndMerge(lux, noc), 1);
        noc.sendDoneActing();
        assertDoneActing(assertAndMerge(lux,noc), 1);

        // Now it is lux turn
        assertActNow(assertAndMerge(lux, noc), 0);
        // But she does nothing
        lux.sendDoneActing();
        assertDoneActing(assertAndMerge(lux, noc), 0);

        // Now lux will get healed since she stands in a healing field
        assertFieldEffect(assertAndMerge(lux, noc), 5, 0, -7);

        passed();
    }

    @Override
    protected String getMapFile() {
        return "     +01";
    }

    @Override
    protected String getFightFile() {
        return "Light, Shadow\n" +
                "Light, Lux, Elf\n" +
                "Shadow, Nocturne, Wraith";
    }
}
