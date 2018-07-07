package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Team14 on 22/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class MovementTest extends OurSystemTest {

    private final String map;
    private final CreatureType monstertype;
    private final int stepsBeforeTurn;
    private final Direction lastDirection;

    private MovementTest(String name, String map, CreatureType monsterType, int stepsBeforeTurn, Direction lastDirection) {
        super(name);
        this.map = map;
        this.monstertype = monsterType;
        this.stepsBeforeTurn = stepsBeforeTurn;
        this.lastDirection = lastDirection;
    }

    @Override
    protected void executeTest() {
        // registration of harry and statist23
        ClientConnection<Event> harry = register(0, "Harry", this.monstertype, "Active", 1, 0);
        ClientConnection<Event> statist23 = register(1, "Statist23", CreatureType.KOBOLD, "Passive", 0, 0);

        assertRegisterEvent(harry.nextEvent(), 1, "Statist23", CreatureType.KOBOLD, "Passive", 0, 0);
        assertRegisterEvent(statist23.nextEvent(), 0, "Harry", this.monstertype, "Active", 1, 0);

        // round 1 begins
        assertRoundBegin(assertAndMerge(harry, statist23), 1);

        for (int i = 0; i < this.stepsBeforeTurn; i++) {
            assertActNow(assertAndMerge(harry, statist23), 0);
            harry.sendMove(Direction.EAST);
            assertMoved(assertAndMerge(harry, statist23), 0, Direction.EAST);
        }
        assertActNow(assertAndMerge(harry, statist23), 0);
        harry.sendMove(this.lastDirection);
        assertKicked(assertAndMerge(harry, statist23), 0);

        assertActNow(assertAndMerge(harry, statist23), 1);
        statist23.sendDoneActing();
        assertDoneActing(assertAndMerge(harry, statist23), 1);

        assertRoundEnd(assertAndMerge(harry, statist23), 1, 0);
        assertWinner(assertAndMerge(harry, statist23), "Passive");
    }

    @Override
    protected String getMapFile() {
        return map;
    }

    @Override
    protected String getFightFile() {
        return "Active, Passive\n"
                + String.format("Active, Harry, %s %n", this.monstertype)
                + "Passive, Statist23, KOBOLD";
    }

    public static Collection<OurSystemTest> getTests() {
        ArrayList<OurSystemTest> sysTests = new ArrayList<>();
        //MovementTests
        // different maps to test
        final String grassMap = "10            ";
        final String iceMap = "10_____________";
        final String rockMap = "10#####";
        final String swampMap = "10%%%%%%%%%%%%%";
        final String waterMap = "10~~~~~~~~~~~~~~";

        // KoboldTests
        sysTests.add(new MovementTest("KoboldOnGrassMovementTest", grassMap, CreatureType.KOBOLD, 10, Direction.WEST));
        sysTests.add(new MovementTest("KoboldOnIceMovementTest", iceMap, CreatureType.KOBOLD, 5, Direction.WEST));
        sysTests.add(new MovementTest("KoboldOnRockMovementTest", rockMap, CreatureType.KOBOLD, 0, Direction.EAST));
        sysTests.add(new MovementTest("KoboldOnSwampMovementTest", swampMap, CreatureType.KOBOLD, 3, Direction.EAST));
        sysTests.add(new MovementTest("KoboldOnWaterMovementTest", waterMap, CreatureType.KOBOLD, 2, Direction.EAST));
        sysTests.add(new MovementTest("KoboldOutOfBoundsMovementTest", waterMap, CreatureType.KOBOLD, 0, Direction.SOUTH_EAST));
        sysTests.add(new MovementTest("KoboldOnCreatureMovementTest", waterMap, CreatureType.KOBOLD, 0, Direction.WEST));

        // ElfTests
        sysTests.add(new MovementTest("ElfOnGrassMovementTest", grassMap, CreatureType.ELF, 10, Direction.WEST));
        sysTests.add(new MovementTest("ElfOnIceMovementTest", iceMap, CreatureType.ELF, 5, Direction.WEST));
        sysTests.add(new MovementTest("ElfOnRockMovementTest", rockMap, CreatureType.ELF, 0, Direction.EAST));
        sysTests.add(new MovementTest("ElfOnSwampMovementTest", swampMap, CreatureType.ELF, 3, Direction.EAST));
        sysTests.add(new MovementTest("ElfOnWaterMovementTest", waterMap, CreatureType.ELF, 2, Direction.EAST));
        sysTests.add(new MovementTest("ElfOutOfBoundsMovementTest", waterMap, CreatureType.ELF, 0, Direction.SOUTH_EAST));
        sysTests.add(new MovementTest("ElfOnCreatureMovementTest", waterMap, CreatureType.ELF, 0, Direction.WEST));

        // NagaTests
        sysTests.add(new MovementTest("NagaOnGrassMovementTest", grassMap, CreatureType.NAGA, 9, Direction.WEST));
        sysTests.add(new MovementTest("NagaOnIceMovementTest", iceMap, CreatureType.NAGA, 5, Direction.WEST));
        sysTests.add(new MovementTest("NagaOnRockMovementTest", rockMap, CreatureType.NAGA, 0, Direction.EAST));
        sysTests.add(new MovementTest("NagaOnSwampMovementTest", swampMap, CreatureType.NAGA, 9, Direction.EAST));
        sysTests.add(new MovementTest("NagaOnWaterMovementTest", waterMap, CreatureType.NAGA, 4, Direction.EAST));
        sysTests.add(new MovementTest("NagaOutOfBoundsMovementTest", waterMap, CreatureType.NAGA, 0, Direction.SOUTH_EAST));
        sysTests.add(new MovementTest("NagaOnCreatureMovementTest", waterMap, CreatureType.NAGA, 0, Direction.WEST));

        // RookTests
        sysTests.add(new MovementTest("RookOnGrassMovementTest", grassMap, CreatureType.ROOK, 4, Direction.WEST));
        sysTests.add(new MovementTest("RookOnIceMovementTest", iceMap, CreatureType.ROOK, 3, Direction.WEST));
        sysTests.add(new MovementTest("RookOnRockMovementTest", rockMap, CreatureType.ROOK, 0, Direction.EAST));
        sysTests.add(new MovementTest("RookOnSwampMovementTest", swampMap, CreatureType.ROOK, 1, Direction.EAST));
        sysTests.add(new MovementTest("RookOnWaterMovementTest", waterMap, CreatureType.ROOK, 1, Direction.EAST));
        sysTests.add(new MovementTest("RookOutOfBoundsMovementTest", waterMap, CreatureType.ROOK, 0, Direction.SOUTH_EAST));
        sysTests.add(new MovementTest("RookOnCreatureMovementTest", waterMap, CreatureType.ROOK, 0, Direction.WEST));

        // YetiTests
        sysTests.add(new MovementTest("YetiOnGrassMovementTest", grassMap, CreatureType.YETI, 5, Direction.WEST));
        sysTests.add(new MovementTest("YetiOnRockMovementTest", rockMap, CreatureType.YETI, 0, Direction.EAST));
        sysTests.add(new MovementTest("YetiOnSwampMovementTest", swampMap, CreatureType.YETI, 1, Direction.EAST));
        sysTests.add(new MovementTest("YetiOnWaterMovementTest", waterMap, CreatureType.YETI, 1, Direction.EAST));
        sysTests.add(new MovementTest("YetiOutOfBoundsMovementTest", waterMap, CreatureType.YETI, 0, Direction.SOUTH_EAST));
        sysTests.add(new MovementTest("YetiOnCreatureMovementTest", waterMap, CreatureType.YETI, 0, Direction.WEST));

        // IfritTests
        sysTests.add(new MovementTest("IfritOnGrassMovementTest", grassMap, CreatureType.IFRIT, 10, Direction.WEST));
        sysTests.add(new MovementTest("IfritOnIceMovementTest", iceMap, CreatureType.IFRIT, 5, Direction.WEST));
        sysTests.add(new MovementTest("IfritOnRockMovementTest", rockMap, CreatureType.IFRIT, 0, Direction.EAST));
        sysTests.add(new MovementTest("IfritOnSwampMovementTest", swampMap, CreatureType.IFRIT, 3, Direction.EAST));
        sysTests.add(new MovementTest("IfritOnWaterMovementTest", waterMap, CreatureType.IFRIT, 2, Direction.EAST));
        sysTests.add(new MovementTest("IfritOutOfBoundsMovementTest", waterMap, CreatureType.IFRIT, 0, Direction.SOUTH_EAST));
        sysTests.add(new MovementTest("IfritOnCreatureMovementTest", waterMap, CreatureType.IFRIT, 0, Direction.WEST));

        // WraithTests
        sysTests.add(new MovementTest("WraithOnGrassMovementTest", grassMap, CreatureType.WRAITH, 10, Direction.WEST));
        sysTests.add(new MovementTest("WraithOnIceMovementTest", iceMap, CreatureType.WRAITH, 5, Direction.WEST));
        sysTests.add(new MovementTest("WraithOnRockMovementTest", rockMap, CreatureType.WRAITH, 0, Direction.EAST));
        sysTests.add(new MovementTest("WraithOnSwampMovementTest", swampMap, CreatureType.WRAITH, 3, Direction.EAST));
        sysTests.add(new MovementTest("WraithOnWaterMovementTest", waterMap, CreatureType.WRAITH, 2, Direction.EAST));
        sysTests.add(new MovementTest("WraithOutOfBoundsMovementTest", waterMap, CreatureType.WRAITH, 0, Direction.SOUTH_EAST));
        sysTests.add(new MovementTest("WraithOnCreatureMovementTest", waterMap, CreatureType.WRAITH, 0, Direction.WEST));
        return sysTests;
    }
}
