package de.unisaarland.sopra.messagestest.attacktest;
import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.messages.attack.Slash;
import de.unisaarland.sopra.messages.attack.Stab;
import de.unisaarland.sopra.model.*;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.utility.InvalidFightFileException;
import de.unisaarland.sopra.utility.InvalidMapFileException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by DJ MacHack on 13.09.2016.
 */
public class SlashTest {
    CommIds a = new CommIds();
    CommIds b = new CommIds();
    Game game = new Game();

    private final static String FIGHTFILE = "Rot, Blau\n" +
            "Rot, Groudon, Kobold\n" +
            "Blau, Kyogre, Wraith";

    private final static String map = " x01 #\n" +   //6 lang
                 "##%xt \n" +
                 "tX~~AA\n" +
                 "^_*$  \n" +
                 "+ ####";

    private final static String map2 = "  0 1#\n" +   //6 lang
            "##%xt \n" +
            "tX~~AA\n" +
            "^_*$  \n" +
            "+ ####";
    //Map m = new Map();
    GameVector vec1 = new GameVector(2,0);
    GameVector vec2 = new GameVector(3,0);
    GameVector vec3 = new GameVector(4,0);
    Pc pc1 = new Pc(0,vec1, 100, CreatureType.KOBOLD);
    Pc pc2 = new Pc(1,vec2, 100, CreatureType.WRAITH);
    Pc pc3 = new Pc(1, vec3, 100, CreatureType.WRAITH);

    @Before
    public void setup() throws InvalidMapFileException, InvalidFightFileException
    {
        game.handleFightfile(FIGHTFILE);
        game.setRoundState(RoundState.ACTNOW);
        //this.m = game.getMap();
        a.addCommLibId(12);
        b.addCommLibId(13);

        game.addMonster(pc1);

        List<Integer> initOrder = new ArrayList<>();
        initOrder.add(0);
        initOrder.add(1);
        game.setInitOrder(initOrder);
    }

    @Test
    public void canStabtrue() throws InvalidMapFileException
    {
        this.game.handleMapfile(map);
        game.addMonster(pc2);
        Stab stab = new Stab(a,0, Direction.EAST);
        assertEquals(stab.validateCommand(game), true);

    }

    @Test
    public void stabDamage() throws InvalidMapFileException
    {
        this.game.handleMapfile(map);
        game.addMonster(pc2);
        int hp1 = pc2.getHp();
        Stab stab = new Stab(a,0,Direction.EAST);
        stab.executeCommand(game);
        int hp2 = pc2.getHp();
        assertEquals(hp1 > hp2 && hp2==93, true);
    }

    public void canSlashTrue() throws InvalidMapFileException
    {
        this.game.handleMapfile(map);
        game.addMonster(pc2);
        Slash slash = new Slash(a,0,Direction.EAST);
        assertEquals(slash.validateCommand(game), true);

    }

    @Test
    public void slashDamage() throws InvalidMapFileException
    {
        this.game.handleMapfile(map);
        game.addMonster(pc2);
        int hp1 = pc2.getHp();
        Slash slash = new Slash(a,0,Direction.EAST);
        slash.executeCommand(game);
        int hp2 = pc2.getHp();
        assertEquals(hp1 > hp2 && hp2==88, true);
    }

    @Test
    public void canSlashFalse() throws InvalidMapFileException
    {
        this.game.handleMapfile(map2);
        game.addMonster(pc3);
        Slash slash = new Slash(a,0,Direction.EAST);
        assertEquals(slash.validateCommand(game), false);
    }

    public void canStabFalse () throws InvalidMapFileException
    {
        this.game.handleMapfile(map2);
        game.addMonster(pc3);
        Stab stab = new Stab(a,0,Direction.EAST);
        assertEquals(stab.validateCommand(game), false);
    }
}
