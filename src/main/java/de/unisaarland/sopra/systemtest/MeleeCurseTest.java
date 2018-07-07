package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Lukas Kirschner on 9/20/16.
 * This test checks first if a matching creature is allowed to attack with the corresponding attack
 * then another fitting creature attacks an empty field and gets kicked.
 */
public class MeleeCurseTest extends OurSystemTest {
    private CreatureType t;
    private AttackType a;
    private static String TEAMA = "TeamA";
    private static String TEAMB = "TeamB";
    private static String PLAYERA = "A";
    private static String PLAYERB = "B";
    private static String MAPFILE = " 01$\n    \n    \n    ";


    private MeleeCurseTest(AttackType a, CreatureType t) {
        super("MeleeCurseTest." + a.toString() + "." + t.toString());
        this.t = t;
        this.a = a;
    }

    @Override
    protected void executeTest() {
        ClientConnection<Event> pa = register(0, PLAYERA, this.t, TEAMA, 1, 0);
        ClientConnection<Event> pb = register(1, PLAYERB, this.t, TEAMB, 2, 0);
        assertRegisterEvent(pb.nextEvent(), 0, PLAYERA, this.t, TEAMA, 1, 0);
        assertRegisterEvent(pa.nextEvent(), 1, PLAYERB, this.t, TEAMB, 2, 0);

        assertRoundBegin(assertAndMerge(pa, pb), 1);
        assertMeleeAttack(pa, Direction.EAST, pb, 0, a, true); //Player A attacks Player B

        assertActNow(assertAndMerge(pa, pb), 0);
        pa.sendDoneActing();
        assertDoneActing(assertAndMerge(pa, pb), 0);

        assertActNow(assertAndMerge(pa,pb),1);
        pb.sendMove(Direction.EAST);
        assertMoved(assertAndMerge(pa,pb),1,Direction.EAST);

        assertMeleeAttack(pb, Direction.EAST, pa, 1, a, false); //Player B tries to attacktest an empty tile and gets kicked

        assertRoundEnd(assertAndMerge(pa, pb), 1, 0);
        assertWinner(assertAndMerge(pa, pb), TEAMA);

    }

    @Override
    protected String getMapFile() {
        return MAPFILE;
    }

    @Override
    protected String getFightFile() {
        return TEAMA + ", " + TEAMB + "\n" + TEAMA + ", " + PLAYERA + ", " + t.toString() + "\n" + TEAMB + ", " + PLAYERB + ", " + t.toString();
    }

    public static Collection<OurSystemTest> getTests() {
        ArrayList<OurSystemTest> sysTests = new ArrayList<>();
        sysTests.add(new MeleeCurseTest(AttackType.STAB, CreatureType.KOBOLD));
        sysTests.add(new MeleeCurseTest(AttackType.STAB, CreatureType.ELF));
        sysTests.add(new MeleeCurseTest(AttackType.STAB, CreatureType.WRAITH));
        sysTests.add(new MeleeCurseTest(AttackType.CUT, CreatureType.NAGA));
        sysTests.add(new MeleeCurseTest(AttackType.BITE, CreatureType.NAGA));
        sysTests.add(new MeleeCurseTest(AttackType.SLASH, CreatureType.KOBOLD));
        sysTests.add(new MeleeCurseTest(AttackType.STARE, CreatureType.ROOK));
        sysTests.add(new MeleeCurseTest(AttackType.CLAW, CreatureType.YETI));
        sysTests.add(new MeleeCurseTest(AttackType.CRUSH, CreatureType.YETI));
        sysTests.add(new MeleeCurseTest(AttackType.SINGE, CreatureType.IFRIT));
        //sysTests.add(new MeleeTest(AttackType.BURN,CreatureType.IFRIT));
        return sysTests;
    }

    private void assertMeleeAttack(ClientConnection<Event> cli, Direction d, ClientConnection<Event> cli2, int id, AttackType t, boolean allowed) {
        assertActNow(assertAndMerge(cli, cli2), id);
        switch (t) {
            case STAB:
                cli.sendStab(d);
                if (allowed) assertStabbed(assertAndMerge(cli, cli2), id, d);
                else assertKicked(assertAndMerge(cli, cli2), id);
                break;
            case CUT:
                cli.sendCut(d);
                if (allowed) assertCut(assertAndMerge(cli, cli2), id, d);
                else assertKicked(assertAndMerge(cli, cli2), id);
                break;
            case BITE:
                cli.sendBite(d);
                if (allowed) assertBitten(assertAndMerge(cli, cli2), id, d);
                else assertKicked(assertAndMerge(cli, cli2), id);
                break;
            case SLASH:
                cli.sendSlash(d);
                if (allowed) assertSlashed(assertAndMerge(cli, cli2), id, d);
                else assertKicked(assertAndMerge(cli, cli2), id);
                break;
            case STARE:
                cli.sendStare(d);
                if (allowed) assertStared(assertAndMerge(cli, cli2), id, d);
                else assertKicked(assertAndMerge(cli, cli2), id);
                break;
            case CLAW:
                cli.sendClaw(d);
                if (allowed) assertClaw(assertAndMerge(cli, cli2), id, d);
                else assertKicked(assertAndMerge(cli, cli2), id);
                break;
            case CRUSH:
                cli.sendCrush(d);
                if (allowed) assertCrush(assertAndMerge(cli, cli2), id, d);
                else assertKicked(assertAndMerge(cli, cli2), id);
                break;
            case SINGE:
                cli.sendSinge(d);
                if (allowed) assertSinged(assertAndMerge(cli, cli2), id, d);
                else assertKicked(assertAndMerge(cli, cli2), id);
                break;
            case BURN:
                cli.sendBurn();
                if (allowed) assertBurned(assertAndMerge(cli, cli2), id);
                else assertKicked(assertAndMerge(cli, cli2), id);
            default:
                break;
        }
    }

    private enum AttackType {
        STAB, CUT, BITE, SLASH, STARE, CLAW, CRUSH, SINGE, BURN;
    }

}