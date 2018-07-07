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
public class RegistrationPhaseTest extends OurSystemTest {

    private final static String map = "0 1 2 \n" +
            "~~^^**\n" +
            "2 1 0 ";

    private final static String fight = "Aqua, Rocket, Magma\n" +
            "Aqua, Kyogre, Yeti\n" +
            "Aqua, Garados, Naga\n" +
            "Rocket, Mauzi, Kobold\n" +
            "Rocket, Vipitis, Naga\n" +
            "Magma, Slugma, Ifrit\n" +
            "Magma, Mewto, Wraith";

    public RegistrationPhaseTest() {
        super("RegistrationPhase");
    }

    @Override
    protected void executeTest() {
        List<ClientConnection<Event>> ccs = new ArrayList<>();

        // Team Aqua registration
        ClientConnection<Event> kyogre = register(0, "Kyogre", CreatureType.YETI, "Aqua", 0, 0);
        ccs.add(kyogre);

        ClientConnection<Event> garados = register(1, "Garados", CreatureType.NAGA, "Aqua", 3, 2);
        assertRegisterEvent(kyogre.nextEvent(), 1, "Garados", CreatureType.NAGA, "Aqua", 3, 2);
        //assertRegisterEvent(assertAndMerge(ccs), 1, "Garados", CreatureType.NAGA, "Aqua", 3, 2);
        assertRegisterEvent(garados.nextEvent(), 0, "Kyogre", CreatureType.YETI, "Aqua", 0, 0);
        ccs.add(garados);

        // Team Rocket registration
        ClientConnection<Event> mauzi = register(2, "Mauzi", CreatureType.KOBOLD, "Rocket", 2, 0);
        assertRegisterEvent(assertAndMerge(ccs), 2, "Mauzi", CreatureType.KOBOLD, "Rocket", 2, 0);
        assertRegisterEvent(mauzi.nextEvent(), 0, "Kyogre", CreatureType.YETI, "Aqua", 0, 0);
        assertRegisterEvent(mauzi.nextEvent(), 1, "Garados", CreatureType.NAGA, "Aqua", 3, 2);
        ccs.add(mauzi);

        ClientConnection<Event> vipitis = register(3, "Vipitis", CreatureType.NAGA, "Rocket", 1, 2);
        assertRegisterEvent(assertAndMerge(ccs), 3, "Vipitis", CreatureType.NAGA, "Rocket", 1, 2);
        assertRegisterEvent(vipitis.nextEvent(), 0, "Kyogre", CreatureType.YETI, "Aqua", 0, 0);
        assertRegisterEvent(vipitis.nextEvent(), 1, "Garados", CreatureType.NAGA, "Aqua", 3, 2);
        assertRegisterEvent(vipitis.nextEvent(), 2, "Mauzi", CreatureType.KOBOLD, "Rocket", 2, 0);
        ccs.add(vipitis);

        // Team Magma registration
        ClientConnection<Event> slugma = register(4, "Slugma", CreatureType.IFRIT, "Magma", 4, 0);
        assertRegisterEvent(assertAndMerge(ccs), 4, "Slugma", CreatureType.IFRIT, "Magma", 4, 0);
        assertRegisterEvent(slugma.nextEvent(), 0, "Kyogre", CreatureType.YETI, "Aqua", 0, 0);
        assertRegisterEvent(slugma.nextEvent(), 1, "Garados", CreatureType.NAGA, "Aqua", 3, 2);
        assertRegisterEvent(slugma.nextEvent(), 2, "Mauzi", CreatureType.KOBOLD, "Rocket", 2, 0);
        assertRegisterEvent(slugma.nextEvent(), 3, "Vipitis", CreatureType.NAGA, "Rocket", 1, 2);
        ccs.add(slugma);

        ClientConnection<Event> mewto = register(5, "Mewto", CreatureType.WRAITH, "Magma", -1, 2);
        assertRegisterEvent(assertAndMerge(ccs), 5, "Mewto", CreatureType.WRAITH, "Magma", -1, 2);
        assertRegisterEvent(mewto.nextEvent(), 0, "Kyogre", CreatureType.YETI, "Aqua", 0, 0);
        assertRegisterEvent(mewto.nextEvent(), 1, "Garados", CreatureType.NAGA, "Aqua", 3, 2);
        assertRegisterEvent(mewto.nextEvent(), 2, "Mauzi", CreatureType.KOBOLD, "Rocket", 2, 0);
        assertRegisterEvent(mewto.nextEvent(), 3, "Vipitis", CreatureType.NAGA, "Rocket", 1, 2);
        assertRegisterEvent(mewto.nextEvent(), 4, "Slugma", CreatureType.IFRIT, "Magma", 4, 0);
        ccs.add(mewto);

        // Registration completed -> Roundbegin
        //assertRoundBegin(assertAndMerge(ccs), 1);
        assertRoundBegin(kyogre.nextEvent(), 1);
        assertRoundBegin(garados.nextEvent(), 1);
        assertRoundBegin(mauzi.nextEvent(), 1);
        assertRoundBegin(vipitis.nextEvent(), 1);
        assertRoundBegin(slugma.nextEvent(), 1);
        assertRoundBegin(mewto.nextEvent(), 1);

        assertActNow(assertAndMerge(ccs), 0);
        assertKicked(assertAndMerge(ccs), 0);

        assertActNow(assertAndMerge(ccs), 1);
        assertKicked(assertAndMerge(ccs), 1);


        for (int id = 2; id < 6; id++) {
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
