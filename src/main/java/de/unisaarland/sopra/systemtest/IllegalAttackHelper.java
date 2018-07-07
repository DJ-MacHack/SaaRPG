package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.model.CreatureType;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Team14 on 9/19/16.
 */
public class IllegalAttackHelper {
    private IllegalAttackHelper() {

    }

    public static Collection<OurSystemTest> getTests(){
        ArrayList<OurSystemTest> sysTests = new ArrayList<>();
        sysTests.add(new StabIllegalTest(CreatureType.NAGA)); //These tests seem to cause a crash in our reference implementation
        sysTests.add(new StabIllegalTest(CreatureType.ROOK));
        sysTests.add(new StabIllegalTest(CreatureType.YETI));
        sysTests.add(new StabIllegalTest(CreatureType.IFRIT));
        sysTests.add(new CutIllegalTest(CreatureType.KOBOLD));
        sysTests.add(new CutIllegalTest(CreatureType.ELF));
        sysTests.add(new CutIllegalTest(CreatureType.ROOK));
        sysTests.add(new CutIllegalTest(CreatureType.YETI));
        sysTests.add(new CutIllegalTest(CreatureType.IFRIT));
        sysTests.add(new CutIllegalTest(CreatureType.WRAITH));
        sysTests.add(new BiteIllegalTest(CreatureType.KOBOLD));
        sysTests.add(new BiteIllegalTest(CreatureType.ELF));
        sysTests.add(new BiteIllegalTest(CreatureType.ROOK));
        sysTests.add(new BiteIllegalTest(CreatureType.YETI));
        sysTests.add(new BiteIllegalTest(CreatureType.IFRIT));
        sysTests.add(new BiteIllegalTest(CreatureType.WRAITH));
        sysTests.add(new SlashIllegalTest(CreatureType.ELF));
        sysTests.add(new SlashIllegalTest(CreatureType.NAGA));
        sysTests.add(new SlashIllegalTest(CreatureType.ROOK));
        sysTests.add(new SlashIllegalTest(CreatureType.YETI));
        sysTests.add(new SlashIllegalTest(CreatureType.IFRIT));
        sysTests.add(new SlashIllegalTest(CreatureType.WRAITH));
        sysTests.add(new ShootIllegalTest(CreatureType.KOBOLD));
        sysTests.add(new ShootIllegalTest(CreatureType.NAGA));
        sysTests.add(new ShootIllegalTest(CreatureType.ROOK));
        sysTests.add(new ShootIllegalTest(CreatureType.YETI));
        sysTests.add(new ShootIllegalTest(CreatureType.IFRIT));
        sysTests.add(new ShootIllegalTest(CreatureType.WRAITH));
        sysTests.add(new StareIllegalTest(CreatureType.KOBOLD));
        sysTests.add(new StareIllegalTest(CreatureType.ELF));
        sysTests.add(new StareIllegalTest(CreatureType.NAGA));
        sysTests.add(new StareIllegalTest(CreatureType.YETI));
        sysTests.add(new StareIllegalTest(CreatureType.IFRIT));
        sysTests.add(new StareIllegalTest(CreatureType.WRAITH));
        sysTests.add(new CastIllegalTest(CreatureType.KOBOLD));
        sysTests.add(new CastIllegalTest(CreatureType.ELF));
        sysTests.add(new CastIllegalTest(CreatureType.NAGA));
        sysTests.add(new CastIllegalTest(CreatureType.YETI));
        sysTests.add(new CastIllegalTest(CreatureType.IFRIT));
        sysTests.add(new CastIllegalTest(CreatureType.WRAITH));
        sysTests.add(new ClawIllegalTest(CreatureType.KOBOLD));
        sysTests.add(new ClawIllegalTest(CreatureType.ELF));
        sysTests.add(new ClawIllegalTest(CreatureType.NAGA));
        sysTests.add(new ClawIllegalTest(CreatureType.ROOK));
        sysTests.add(new ClawIllegalTest(CreatureType.IFRIT));
        sysTests.add(new ClawIllegalTest(CreatureType.WRAITH));
        sysTests.add(new CrushIllegalTest(CreatureType.KOBOLD));
        sysTests.add(new CrushIllegalTest(CreatureType.ELF));
        sysTests.add(new CrushIllegalTest(CreatureType.NAGA));
        sysTests.add(new CrushIllegalTest(CreatureType.ROOK));
        sysTests.add(new CrushIllegalTest(CreatureType.IFRIT));
        sysTests.add(new CrushIllegalTest(CreatureType.WRAITH));
        sysTests.add(new SingeIllegalTest(CreatureType.KOBOLD));
        sysTests.add(new SingeIllegalTest(CreatureType.ELF));
        sysTests.add(new SingeIllegalTest(CreatureType.NAGA));
        sysTests.add(new SingeIllegalTest(CreatureType.ROOK));
        sysTests.add(new SingeIllegalTest(CreatureType.YETI));
        sysTests.add(new SingeIllegalTest(CreatureType.WRAITH));
        sysTests.add(new BurnIllegalTest(CreatureType.KOBOLD));
        sysTests.add(new BurnIllegalTest(CreatureType.ELF));
        sysTests.add(new BurnIllegalTest(CreatureType.NAGA));
        sysTests.add(new BurnIllegalTest(CreatureType.ROOK));
        sysTests.add(new BurnIllegalTest(CreatureType.YETI));
        sysTests.add(new BurnIllegalTest(CreatureType.WRAITH));
        sysTests.add(new BlinkIllegalTest(CreatureType.KOBOLD));
        sysTests.add(new BlinkIllegalTest(CreatureType.ELF));
        sysTests.add(new BlinkIllegalTest(CreatureType.NAGA));
        sysTests.add(new BlinkIllegalTest(CreatureType.ROOK));
        sysTests.add(new BlinkIllegalTest(CreatureType.YETI));
        sysTests.add(new BlinkIllegalTest(CreatureType.IFRIT));
        sysTests.add(new SwapIllegalTest(CreatureType.KOBOLD));
        sysTests.add(new SwapIllegalTest(CreatureType.ELF));
        sysTests.add(new SwapIllegalTest(CreatureType.NAGA));
        sysTests.add(new SwapIllegalTest(CreatureType.ROOK));
        sysTests.add(new SwapIllegalTest(CreatureType.YETI));
        sysTests.add(new SwapIllegalTest(CreatureType.IFRIT));
        return sysTests;
    }
}
