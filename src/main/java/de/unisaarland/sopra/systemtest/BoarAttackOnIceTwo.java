package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Team 14 on 21.09.2016.
 */
public class BoarAttackOnIceTwo extends OurSystemTest{

    public BoarAttackOnIceTwo(){
        super("BoarAttackOnIceTwo");
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> p1 = register(0, "A", CreatureType.ROOK, "ATeam", 5, 0);
        ClientConnection<Event> p2 = register(1, "B", CreatureType.ROOK, "BTeam", 1, 1);

        assertRegisterEvent(p1.nextEvent(), 1, "B", CreatureType.ROOK, "BTeam", 1, 1);
        assertRegisterEvent(p2.nextEvent(), 0, "A", CreatureType.ROOK, "ATeam", 5, 0);

        // Round 1 begins
        assertRoundBegin(assertAndMerge(p1, p2), 1);

        // Boar spawns
        assertBoarSpawned(assertAndMerge(p1, p2), 2, 0, 0);

        // Boar moves - 4 east, cant move anymore because of ice and player1
        for(int i = 0; i < 4; i++){
            assertMoved(assertAndMerge(p1, p2), 2, Direction.EAST);
        }
        // Boar attacks player1
        assertBoarAttack(assertAndMerge(p1, p2), 2, 0);

        // Player1 can act
        assertActNow(assertAndMerge(p1, p2), 0);
        p1.sendDoneActing();
        assertDoneActing(assertAndMerge(p1, p2), 0);

        // Player2 can act
        assertActNow(assertAndMerge(p1, p2), 1);
        p2.sendDoneActing();
        assertDoneActing(assertAndMerge(p1, p2), 1);

        // Round 1 ends and Round 2 starts
        assertRoundEnd(assertAndMerge(p1, p2), 1, 0);
        assertRoundBegin(assertAndMerge(p1, p2), 2);

        // Boar attacks player1
        assertBoarAttack(assertAndMerge(p1, p2), 2, 0);

        // Boar moves 4 west, cant move because of map edge
        for(int i = 0; i < 4; i++){
            assertMoved(assertAndMerge(p1, p2), 2, Direction.WEST);
        }

        // Turns runs back, 2 east
        assertMoved(assertAndMerge(p1, p2), 2, Direction.EAST);
        assertMoved(assertAndMerge(p1, p2), 2, Direction.EAST);

        // Player2 can act
        assertActNow(assertAndMerge(p1, p2), 1);
    }

    @Override
    protected String getMapFile() {
        return ".____0\n"+
                "#1####";
    }

    @Override
    protected String getFightFile() {
        return "ATeam, BTeam\n"+
                "ATeam, A, Rook\n"+
                "BTeam, B, Rook";
    }
}
