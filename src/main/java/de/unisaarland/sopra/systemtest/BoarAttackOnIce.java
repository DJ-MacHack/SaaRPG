package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.messages.PoisonEffect;
import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Team14 on 21/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class BoarAttackOnIce extends OurSystemTest {

    public BoarAttackOnIce() {
        super("BoarAttackOnIce");
    }

    @Override
    protected void executeTest() {
        // registration
        ClientConnection<Event> sansa = register(0, "Sansa", CreatureType.NAGA, "Stark", 3, 0);
        ClientConnection<Event> tyrion = register(1, "Tyrion", CreatureType.KOBOLD, "Lannister", 5, 1);
        assertRegisterEvent(sansa.nextEvent(), 1, "Tyrion", CreatureType.KOBOLD, "Lannister", 5, 1);
        assertRegisterEvent(tyrion.nextEvent(), 0, "Sansa", CreatureType.NAGA, "Stark", 3, 0);

        // round 1: init-Order: Sansa -> Tyrion
        assertRoundBegin(assertAndMerge(sansa, tyrion), 1);

        // boar moves to (3, 1)
        assertBoarSpawned(assertAndMerge(sansa, tyrion), 2, 1, 1);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.EAST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.EAST);

        // Sansa's turn: cut on boar (5 dmg)
        assertActNow(assertAndMerge(sansa, tyrion), 0);
        sansa.sendCut(Direction.SOUTH_EAST);
        assertCut(assertAndMerge(sansa, tyrion), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(sansa, tyrion), 0);
        sansa.sendDoneActing();
        assertDoneActing(assertAndMerge(sansa, tyrion), 0);

        // Tyrion's turn: moves to (4, 0) (BushField) in BoarAttack line for next round
        assertActNow(assertAndMerge(sansa, tyrion), 1);
        tyrion.sendMove(Direction.NORTH_WEST);
        assertMoved(assertAndMerge(sansa, tyrion), 1, Direction.NORTH_WEST);
        assertActNow(assertAndMerge(sansa, tyrion), 1);
        tyrion.sendMove(Direction.WEST);
        assertMoved(assertAndMerge(sansa, tyrion), 1, Direction.WEST);
        assertActNow(assertAndMerge(sansa, tyrion), 1);
        tyrion.sendDoneActing();
        assertDoneActing(assertAndMerge(sansa, tyrion), 1);

        assertRoundEnd(assertAndMerge(sansa, tyrion), 1, 1);
        // sansa: 100hp
        // tyrion: 100hp
        // boar: 15hp

        // round 2: init-Order: Sansa -> Tyrion
        assertRoundBegin(assertAndMerge(sansa, tyrion), 2);

        // boarattack on Tyrion (2 dmg) and on Sansa (5 dmg)
        assertBoarAttack(assertAndMerge(sansa, tyrion), 2, 1);
        assertBoarAttack(assertAndMerge(sansa, tyrion), 2, 0);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.WEST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.WEST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.WEST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.EAST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.EAST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.EAST);

        // Tyrion's turn: 2x slash on sansa (24 dmg)
        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(sansa, tyrion), 1);
            tyrion.sendSlash(Direction.WEST);
            assertSlashed(assertAndMerge(sansa, tyrion), 1, Direction.WEST);
        }
        assertActNow(assertAndMerge(sansa, tyrion), 1);
        tyrion.sendDoneActing();
        assertDoneActing(assertAndMerge(sansa, tyrion), 1);

        // Sansa's turn: 5x cut on tyrion (10 dmg)
        for (int i = 0; i < 5; i++) {
            assertActNow(assertAndMerge(sansa, tyrion), 0);
            sansa.sendCut(Direction.EAST);
            assertCut(assertAndMerge(sansa, tyrion), 0, Direction.EAST);
        }
        assertActNow(assertAndMerge(sansa, tyrion), 0);
        sansa.sendDoneActing();
        assertDoneActing(assertAndMerge(sansa, tyrion), 0);

        assertRoundEnd(assertAndMerge(sansa, tyrion), 2, 0);
        // sansa: 71hp
        // tyrion: 88hp
        // boar: 15hp

        // round 3: init-Order: Tyrion -> Sansa
        assertRoundBegin(assertAndMerge(sansa, tyrion), 3);

        // boarattack on Tyrion (2 dmg) and on Sansa (5 dmg)
        assertBoarAttack(assertAndMerge(sansa, tyrion), 2, 1);
        assertBoarAttack(assertAndMerge(sansa, tyrion), 2, 0);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.WEST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.WEST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.WEST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.EAST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.EAST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.EAST);

        // Tyrion's turn: 4x stab on Sansa (28 dmg)
        for (int i = 0; i < 4; i++) {
            assertActNow(assertAndMerge(sansa, tyrion), 1);
            tyrion.sendStab(Direction.WEST);
            assertStabbed(assertAndMerge(sansa, tyrion), 1, Direction.WEST);
        }
        assertActNow(assertAndMerge(sansa, tyrion), 1);
        tyrion.sendDoneActing();
        assertDoneActing(assertAndMerge(sansa, tyrion), 1);

        // Sansa's turn: 1x bite on boar (6 dmg + FIRSTROUND) and 1x bite on Tyrion (3 dmg + FIRSTROUND)
        assertActNow(assertAndMerge(sansa, tyrion), 0);
        sansa.sendBite(Direction.SOUTH_EAST);
        assertBitten(assertAndMerge(sansa, tyrion), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(sansa, tyrion), 0);
        sansa.sendBite(Direction.EAST);
        assertBitten(assertAndMerge(sansa, tyrion), 0, Direction.EAST);
        assertActNow(assertAndMerge(sansa, tyrion), 0);
        sansa.sendDoneActing();
        assertDoneActing(assertAndMerge(sansa, tyrion), 0);

        assertRoundEnd(assertAndMerge(sansa, tyrion), 3, 0);
        // sansa: 38hp
        // tyrion: 83hp [FIRSTROUND] -> [HEAVY]
        // boar: 9hp [FIRSTROUND] -> [HEAVY]

        // round 4: init-Order: Sansa -> Tyrion
        assertRoundBegin(assertAndMerge(sansa, tyrion), 4);

        // boar movement and attack on Tyrion (2 dmg) and Sansa (5 dmg)
        assertBoarAttack(assertAndMerge(sansa, tyrion), 2, 1);
        assertBoarAttack(assertAndMerge(sansa, tyrion), 2, 0);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.WEST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.WEST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.WEST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.EAST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.EAST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.EAST);

        // Sansa's turn: 1x bite on Tyrion (3 dmg + FIRSTROUND) and moves 3 steps west
        assertActNow(assertAndMerge(sansa, tyrion), 0);
        sansa.sendBite(Direction.EAST);
        assertBitten(assertAndMerge(sansa, tyrion), 0, Direction.EAST);
        for (int i = 0; i < 3; i++) {
            assertActNow(assertAndMerge(sansa, tyrion), 0);
            sansa.sendMove(Direction.WEST);
            assertMoved(assertAndMerge(sansa, tyrion), 0, Direction.WEST);
        }
        assertActNow(assertAndMerge(sansa, tyrion), 0);
        sansa.sendDoneActing();
        assertDoneActing(assertAndMerge(sansa, tyrion), 0);

        // Tyrions's turn: moves 3 steps west and slashes Sansa (12 dmg)
        for (int i = 0; i < 3; i++) {
            assertActNow(assertAndMerge(sansa, tyrion), 1);
            tyrion.sendMove(Direction.WEST);
            assertMoved(assertAndMerge(sansa, tyrion), 1, Direction.WEST);
        }
        assertActNow(assertAndMerge(sansa, tyrion), 1);
        tyrion.sendSlash(Direction.WEST);
        assertSlashed(assertAndMerge(sansa, tyrion), 1, Direction.WEST);
        assertActNow(assertAndMerge(sansa, tyrion), 1);
        tyrion.sendDoneActing();
        assertDoneActing(assertAndMerge(sansa, tyrion), 1);

        Event e = assertAndMerge(sansa, tyrion);
        if (!(e instanceof PoisonEffect)) {
            failed("PoisonEffect was expected but Event was of other kind.");
        }
        PoisonEffect pe = (PoisonEffect) e;

        if (pe.getValue() != 4) {
            throw new IllegalArgumentException("PoisonEffect was expected to have value 4 but was different.");
        } else {
            if (pe.getEntityId() == 1) {
                assertPoisonEffect(assertAndMerge(sansa, tyrion), 2, 4);
            } else {
                if (pe.getEntityId() == 2) {
                    assertPoisonEffect(assertAndMerge(sansa, tyrion), 1, 4);
                } else {
                    throw new IllegalArgumentException("PoisonEffect targetId was neither 1 nor 2 which were expected.");
                }
            }
        }
        assertRoundEnd(assertAndMerge(sansa, tyrion), 4, 0);
        // sansa: 21hp
        // tyrion: 74hp [HEAVY, FIRSTROUND] -> [MINOR, HEAVY]
        // boar: 5hp [HEAVY] -> [MINOR]

        // round 5: init-Order: Sansa -> Tyrion
        assertRoundBegin(assertAndMerge(sansa, tyrion), 5);

        // boar movement with attack on Tyrion (5 dmg)
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.NORTH_EAST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.WEST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.WEST);
        assertBoarAttack(assertAndMerge(sansa, tyrion), 2, 1);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.SOUTH_WEST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.EAST);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.EAST);

        // Sansa's turn: moves SE and uses bite and cut on Tyrion (11 dmg + FIRSTROUND) and moves 2 times east
        assertActNow(assertAndMerge(sansa, tyrion), 0);
        sansa.sendMove(Direction.SOUTH_EAST);
        assertMoved(assertAndMerge(sansa, tyrion), 0, Direction.SOUTH_EAST);
        assertActNow(assertAndMerge(sansa, tyrion), 0);
        sansa.sendBite(Direction.NORTH_EAST);
        assertBitten(assertAndMerge(sansa, tyrion), 0, Direction.NORTH_EAST);
        assertActNow(assertAndMerge(sansa, tyrion), 0);
        sansa.sendCut(Direction.NORTH_EAST);

        assertCut(assertAndMerge(sansa, tyrion), 0, Direction.NORTH_EAST);
        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(sansa, tyrion), 0);
            sansa.sendMove(Direction.EAST);
            assertMoved(assertAndMerge(sansa, tyrion), 0, Direction.EAST);
        }
        assertActNow(assertAndMerge(sansa, tyrion), 0);
        sansa.sendDoneActing();
        assertDoneActing(assertAndMerge(sansa, tyrion), 0);

        // Tyrions' turn: moves 2 steps east
        for (int i = 0; i < 2; i++) {
            assertActNow(assertAndMerge(sansa, tyrion), 1);
            tyrion.sendMove(Direction.EAST);
            assertMoved(assertAndMerge(sansa, tyrion), 1, Direction.EAST);
        }
        assertActNow(assertAndMerge(sansa, tyrion), 1);
        tyrion.sendDoneActing();
        assertDoneActing(assertAndMerge(sansa, tyrion), 1);

        List<Event> events = new ArrayList<>();
        events.add(assertAndMerge(sansa, tyrion));
        events.add(assertAndMerge(sansa, tyrion));
        events.add(assertAndMerge(sansa, tyrion));
        assertEquals(events.size(), 3);
        for (Event e1 : events) {
            if (!(e1 instanceof PoisonEffect)) {
                failed("PoisonEffect was expected but Event was of other kind.");
            }
        }
        if (!(events.contains(new PoisonEffect(2, 2)))) {
            throw new IllegalArgumentException("PoisonEffect (2, 2) was missing!");
        }
        if (!(events.contains(new PoisonEffect(1, 2)))) {
            throw new IllegalArgumentException("PoisonEffect (1, 2) was missing!");
        }
        if (!(events.contains(new PoisonEffect(1, 4)))) {
            throw new IllegalArgumentException("PoisonEffect (1, 4) was missing!");
        }

        assertRoundEnd(assertAndMerge(sansa, tyrion), 5, 0);
        // sansa: 21hp
        // tyrion: 52 [MINOR, HEAVY, FIRSTROUND] -> [MINOR, HEAVY]
        // boar: 3hp [MINOR] -> []

        // round 6: init-Order: Tyrion -> Sansa
        assertRoundBegin(assertAndMerge(sansa, tyrion), 6);

        // boar movement with attack on Tyrion (5 dmg)
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.NORTH_EAST);
        assertBoarAttack(assertAndMerge(sansa, tyrion), 2, 1);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.SOUTH_WEST);

        // Tyrion's turn: slash and stab on Sansa (19 dmg)
        assertActNow(assertAndMerge(sansa, tyrion), 1);
        tyrion.sendStab(Direction.SOUTH_WEST);
        assertStabbed(assertAndMerge(sansa, tyrion), 1, Direction.SOUTH_WEST);
        assertActNow(assertAndMerge(sansa, tyrion), 1);
        tyrion.sendSlash(Direction.SOUTH_WEST);
        assertSlashed(assertAndMerge(sansa, tyrion), 1, Direction.SOUTH_WEST);
        assertActNow(assertAndMerge(sansa, tyrion), 1);
        tyrion.sendDoneActing();
        assertDoneActing(assertAndMerge(sansa, tyrion), 1);

        // Sansa's turn: 5x Cut on Tyrion (25 dmg)
        for (int i = 0; i < 5; i++) {
            assertActNow(assertAndMerge(sansa, tyrion), 0);
            sansa.sendCut(Direction.NORTH_EAST);
            assertCut(assertAndMerge(sansa, tyrion), 0, Direction.NORTH_EAST);
        }
        assertActNow(assertAndMerge(sansa, tyrion), 0);
        sansa.sendDoneActing();
        assertDoneActing(assertAndMerge(sansa, tyrion), 0);

        events = new ArrayList<>();
        events.add(assertAndMerge(sansa, tyrion));
        events.add(assertAndMerge(sansa, tyrion));
        assertEquals(events.size(), 2);
        for (Event e2 : events) {
            if (!(e2 instanceof PoisonEffect)) {
                failed("PoisonEffect was expected but Event was of other kind.");
            }
        }
        if (!events.contains(new PoisonEffect(1, 2))) {
            failed("PoisonEffect on Tyrion (id: 1) and value of 2 was expected but not included!");
        }
        if (!events.contains(new PoisonEffect(1, 4))) {
            failed("PoisonEffect on Tyrion (id: 1) and value of 4 was expected but not included!");
        }

        assertRoundEnd(assertAndMerge(sansa, tyrion), 6, 0);
        // sansa: 2hp
        // tyrion: 16hp [MINOR, HEAVY] -> [MINOR]
        // boar: 3hp

        // round 7: init-Order: Sansa -> Tyrion
        assertRoundBegin(assertAndMerge(sansa, tyrion), 7);

        // boar movement with attack on Tyrion (5 dmg)
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.NORTH_EAST);
        assertBoarAttack(assertAndMerge(sansa, tyrion), 2, 1);
        assertMoved(assertAndMerge(sansa, tyrion), 2, Direction.SOUTH_WEST);

        // Tyrion's turn: WarCry and stab on Sansa
        assertActNow(assertAndMerge(sansa, tyrion), 1);
        tyrion.sendWarCry("Finish her - Fatality!");
        assertWarCry(assertAndMerge(sansa, tyrion), 1, "Finish her - Fatality!");
        assertActNow(assertAndMerge(sansa, tyrion), 1);
        tyrion.sendStab(Direction.SOUTH_WEST);
        assertStabbed(assertAndMerge(sansa, tyrion), 1, Direction.SOUTH_WEST);
        assertDied(assertAndMerge(sansa, tyrion), 0);
        assertActNow(assertAndMerge(sansa, tyrion), 1);
        tyrion.sendStab(Direction.SOUTH_EAST);
        assertStabbed(assertAndMerge(sansa, tyrion), 1, Direction.SOUTH_EAST);
        assertDied(assertAndMerge(sansa, tyrion), 2);
        assertActNow(assertAndMerge(sansa, tyrion), 1);
        tyrion.sendDoneActing();
        assertDoneActing(assertAndMerge(sansa, tyrion), 1);

        assertPoisonEffect(assertAndMerge(sansa, tyrion), 1, 2);
        assertRoundEnd(assertAndMerge(sansa, tyrion), 7, 0);

        assertWinner(assertAndMerge(sansa, tyrion), "Lannister");
    }

    @Override
    protected String getMapFile() {
        return "   0x \n"
             + " .__#1";
    }

    @Override
    protected String getFightFile() {
        return "Stark, Lannister\n"
             + "Stark, Sansa, Naga\n"
             + "Lannister, Tyrion, Kobold";
    }
}
