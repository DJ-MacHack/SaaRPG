package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Team14 on 16/09/16.
 * Responsible for creation and implementation: Lukas Schaefer
 */
public class FightFileTests extends OurSystemTest {

    private final static String map = "0 1 2 \n" +
            "~~^^**\n" +
            "2 1 0 ";

    private final static String fight = "Aqua, aqua\n" +
            "Aqua, Kyogre, Yeti\n" +
            "Aqua, Garados, Naga\n" +
            "aqua, kyogre, Kobold\n" +
            "aqua, Vipitis, Naga";

    public FightFileTests() {
        super("FightFileTests");
    }

    @Override
    protected void executeTest() {
        List<ClientConnection<Event>> ccs = new ArrayList<>();

        // Team Aqua registration
        ClientConnection<Event> kyogre1 = register(0, "Kyogre", CreatureType.YETI, "Aqua", 0, 0);
        ccs.add(kyogre1);

        ClientConnection<Event> garados = register(1, "Garados", CreatureType.NAGA, "Aqua", 3, 2);
        assertRegisterEvent(kyogre1.nextEvent(), 1, "Garados", CreatureType.NAGA, "Aqua", 3, 2);
        //assertRegisterEvent(assertAndMerge(ccs), 1, "Garados", CreatureType.NAGA, "Aqua", 3, 2);
        assertRegisterEvent(garados.nextEvent(), 0, "Kyogre", CreatureType.YETI, "Aqua", 0, 0);
        ccs.add(garados);

        // Team Rocket registration
        ClientConnection<Event> kyogre2 = register(2, "kyogre", CreatureType.KOBOLD, "aqua", 2, 0);
        assertRegisterEvent(assertAndMerge(ccs), 2, "kyogre", CreatureType.KOBOLD, "aqua", 2, 0);
        assertRegisterEvent(kyogre2.nextEvent(), 0, "Kyogre", CreatureType.YETI, "Aqua", 0, 0);
        assertRegisterEvent(kyogre2.nextEvent(), 1, "Garados", CreatureType.NAGA, "Aqua", 3, 2);
        ccs.add(kyogre2);

        ClientConnection<Event> vipitis = register(3, "Vipitis", CreatureType.NAGA, "aqua", 1, 2);
        assertRegisterEvent(assertAndMerge(ccs), 3, "Vipitis", CreatureType.NAGA, "aqua", 1, 2);
        assertRegisterEvent(vipitis.nextEvent(), 0, "Kyogre", CreatureType.YETI, "Aqua", 0, 0);
        assertRegisterEvent(vipitis.nextEvent(), 1, "Garados", CreatureType.NAGA, "Aqua", 3, 2);
        assertRegisterEvent(vipitis.nextEvent(), 2, "kyogre", CreatureType.KOBOLD, "aqua", 2, 0);
        ccs.add(vipitis);

        // Registration completed -> Roundbegin
        //assertRoundBegin(assertAndMerge(ccs), 1);
        assertRoundBegin(kyogre1.nextEvent(), 1);
        assertRoundBegin(garados.nextEvent(), 1);
        assertRoundBegin(kyogre2.nextEvent(), 1);
        assertRoundBegin(vipitis.nextEvent(), 1);

        assertActNow(assertAndMerge(ccs), 0);
        assertKicked(assertAndMerge(ccs), 0);

        assertActNow(assertAndMerge(ccs), 1);
        assertKicked(assertAndMerge(ccs), 1);


        for (int id = 2; id < 4; id++) {
            assertActNow(assertAndMerge(ccs), id);
            assertKicked(assertAndMerge(ccs), id);
        }

        assertRoundEnd(assertAndMerge(ccs), 1, 0);
        assertWinner(assertAndMerge(ccs), "");

        passed();
    }

    @Override
    protected String getMapFile() {
        return map;
    }

    @Override
    protected String getFightFile() {
        return fight;
    }
}
