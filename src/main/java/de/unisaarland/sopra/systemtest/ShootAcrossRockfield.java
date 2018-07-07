package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Wiebk on 19.09.2016.
 */
public class ShootAcrossRockfield extends OurSystemTest {

    public ShootAcrossRockfield() {
        super("ShootAcrossRockfield");
    }

    @Override
    public void executeTest() {
        //register
        ClientConnection<Event> feanor = register(0, "Feanor", CreatureType.ELF, "Elf", 0, 0);
        ClientConnection<Event> lurtz = register(1, "Lurtz", CreatureType.ROOK, "Ork", 4, 0);
        Event registerfeanor = lurtz.nextEvent();
        assertRegisterEvent(registerfeanor, 0, "Feanor", CreatureType.ELF, "Elf", 0, 0);
        assertRegisterEvent(feanor.nextEvent(), 1, "Lurtz", CreatureType.ROOK, "Ork", 4, 0);

        assertRoundBegin(assertAndMerge(feanor, lurtz), 1);

        //Feanor's turn: shoot at Lurtz, get's kicked because of rocktile
        assertActNow(assertAndMerge(feanor, lurtz), 0);
        feanor.sendShoot(Direction.EAST);
        assertKicked(assertAndMerge(feanor, lurtz), 0);

        assertActNow(assertAndMerge(feanor, lurtz), 1);
        lurtz.sendDoneActing();
        assertDoneActing(assertAndMerge(feanor, lurtz), 1);

        assertRoundEnd(assertAndMerge(feanor, lurtz), 1, 0);

        //Orks win
        assertWinner(assertAndMerge(feanor, lurtz), "Ork");
    }

    @Override
    public String getFightFile() {
        return "Elf, Ork\n"
                + "Elf, Feanor, Elf\n"
                + "Ork, Lurtz, Rook";
    }

    @Override
    public String getMapFile() {
        return "0 # 1\n"
                + "     \n"
                + "     \n"
                + "     \n"
                + "     ";
    }
}
