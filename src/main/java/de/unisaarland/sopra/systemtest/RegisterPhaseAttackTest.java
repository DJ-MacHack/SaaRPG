package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created on 23.09.16.
 *
 * @author Henrik Paul Koehn
 */
public class RegisterPhaseAttackTest extends OurSystemTest {

    String attack;

    public RegisterPhaseAttackTest(String attack) {
        super("RegisterPhaseAttackTest: ".concat(attack));
        this.attack = attack;
    }

    public static Collection<OurSystemTest> getTests() {
        ArrayList<OurSystemTest> sysTests = new ArrayList<OurSystemTest>();

        sysTests.add(new RegisterPhaseAttackTest("bite"));
        sysTests.add(new RegisterPhaseAttackTest("blink"));
        sysTests.add(new RegisterPhaseAttackTest("claw"));
        sysTests.add(new RegisterPhaseAttackTest("crush"));
        sysTests.add(new RegisterPhaseAttackTest("cut"));
        sysTests.add(new RegisterPhaseAttackTest("shoot"));
        sysTests.add(new RegisterPhaseAttackTest("singe"));
        sysTests.add(new RegisterPhaseAttackTest("slash"));
        sysTests.add(new RegisterPhaseAttackTest("stab"));
        sysTests.add(new RegisterPhaseAttackTest("stare"));
        sysTests.add(new RegisterPhaseAttackTest("swap"));

        return sysTests;
    }

    @Override
    protected void executeTest() {

        expectServerFail();

        ClientConnection<Event> kobo = register(0, "alpha", CreatureType.KOBOLD, "A", 0, 0);
        switch(attack) {
            case "bite" :kobo.sendBite(Direction.NORTH_EAST);break;
            case "blink":kobo.sendBlink(0, 1);break;
            case "claw" :kobo.sendCast(0, 0);break;
            case "crush":kobo.sendCrush(Direction.SOUTH_EAST);break;
            case "cut"  :kobo.sendCut(Direction.NORTH_WEST);break;
            case "shoot":kobo.sendShoot(Direction.NORTH_EAST);break;
            case "singe":kobo.sendSinge(Direction.WEST);break;
            case "slash":kobo.sendSlash(Direction.EAST);break;
            case "stab" :kobo.sendStab(Direction.WEST); break;
            case "stare":kobo.sendStare(Direction.EAST);break;
            case "swap" :kobo.sendSwap(0, 0);break;
            default     :kobo.sendDoneActing();
        }
        assertRegistrationAborted(kobo.nextEvent());

    }

    @Override
    protected String getMapFile() {
        return "01";
    }

    @Override
    protected String getFightFile() {
        return "A, B\n" +
                "A, alpha, kobold\n" +
                "B, beta, wraith";
    }
}
