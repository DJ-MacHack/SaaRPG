package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Wiebk on 21.09.2016.
 */
public class BoarAttacksFairyTest extends OurSystemTest {

    public BoarAttacksFairyTest (){
        super ("BoarAttacksFairyTest");
    }

    @Override
    public void executeTest(){
        ClientConnection<Event> io = register(0, "Io", CreatureType.KOBOLD, "Monde", 0, 2);
        ClientConnection<Event> stratocummulus = register(1, "Stratocummulus", CreatureType.ELF, "Wolken", 1, 2);
        assertRegisterEvent(io.nextEvent(), 1, "Stratocummulus", CreatureType.ELF, "Wolken", 1, 2);
        assertRegisterEvent(stratocummulus.nextEvent(), 0, "Io", CreatureType.KOBOLD, "Monde", 0, 2);

        //FairySpawnEvents:
        assertFairySpawned(assertAndMerge(io, stratocummulus), 2, 0, 0);

        //RoundBegin
        assertRoundBegin(assertAndMerge(io, stratocummulus), 1);

        //BoarSpawnEvents
        assertBoarSpawned(assertAndMerge(io, stratocummulus), 3, 0, 1);

        //FairyMovement: 4 fields towards east
        assertMoved(assertAndMerge(io, stratocummulus), 2, Direction.EAST);
        assertMoved(assertAndMerge(io, stratocummulus), 2, Direction.EAST);
        assertMoved(assertAndMerge(io, stratocummulus), 2, Direction.EAST);
        assertMoved(assertAndMerge(io, stratocummulus), 2, Direction.EAST);

        //BoarMovement: 3 fields towards east, hits rock, turns north_east, attacks fairy
        //turns north_west, walks, hits rock, turns west, walks two more fields
        assertMoved(assertAndMerge(io, stratocummulus), 3, Direction.EAST);
        assertMoved(assertAndMerge(io, stratocummulus), 3, Direction.EAST);
        assertMoved(assertAndMerge(io, stratocummulus), 3, Direction.EAST);
        assertBoarAttack(assertAndMerge(io, stratocummulus), 3, 2);
        assertMoved(assertAndMerge(io, stratocummulus), 3, Direction.NORTH_WEST);
        assertMoved(assertAndMerge(io, stratocummulus), 3, Direction.WEST);
        assertMoved(assertAndMerge(io, stratocummulus), 3, Direction.WEST);

        //io's turn: tries to use burn, gets kicked
        assertActNow(assertAndMerge(io, stratocummulus), 0);
        io.sendBurn();
        assertKicked(assertAndMerge(io, stratocummulus), 0);

        //stratocummulus' turn: stab at io's former position, gets also kicked
        assertActNow(assertAndMerge(io, stratocummulus), 1);
        stratocummulus.sendStab(Direction.WEST);
        assertKicked(assertAndMerge(io, stratocummulus), 1);

        //Winner: no Winner
        assertWinner(assertAndMerge(io, stratocummulus), "");
    }

    @Override
    public String getMapFile(){
        return "+    " +
               ".   #\n"
              +"01   ";
    }

    public String getFightFile(){
        return "Monde, Wolken"
              +"Monde, Io, Kobold"
              +"Wolken, Stratocummulus, Elf";
    }

}
