package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Wiebk on 20.09.2016.
 */
public class AttackOutsideOfMap extends OurSystemTest {

    public AttackOutsideOfMap() {
        super("AttackOutsideOfMap");
    }

    @Override
    public void executeTest() {
        ClientConnection<Event> agathe = register(0, "Agathe", CreatureType.ROOK, "Wasser", 0, 0);
        ClientConnection<Event> ferdinand = register(1, "Ferdinand", CreatureType.IFRIT, "Feuer", 1, 0);
        assertRegisterEvent(agathe.nextEvent(), 1, "Ferdinand", CreatureType.IFRIT, "Feuer", 1, 0);
        assertRegisterEvent(ferdinand.nextEvent(), 0, "Agathe", CreatureType.ROOK, "Wasser", 0, 0);

        assertRoundBegin(assertAndMerge(agathe, ferdinand), 1);

        //agathe casts at field (-1, -1), should be kicked
        assertActNow(assertAndMerge(agathe, ferdinand), 0);
        agathe.sendCast(-1, -1);
        assertKicked(assertAndMerge(agathe, ferdinand), 0);

        //ferdinand casts burn and should also be kicked
        assertActNow(assertAndMerge(agathe, ferdinand), 1);
        ferdinand.sendBurn();
        assertKicked(assertAndMerge(agathe, ferdinand), 1);

        assertRoundEnd(assertAndMerge(agathe, ferdinand), 1, 0);

        //no winner
        assertWinner(assertAndMerge(agathe, ferdinand), "");
    }

    @Override
    public String getMapFile() {
        return "01\n"
                + "  ";
    }

    @Override
    public String getFightFile() {
        return "Wasser, Feuer\n"
                + "Wasser, Agathe, Rook\n"
                + "Feuer, Ferdinand, Ifrit";
    }

}
