package de.unisaarland.sopra.messagestest.attacktest;
import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.messages.attack.Bite;
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
 * Created by DJ MacHack on 12.09.2016.
 */
public class BiteTest {

    CommIds a = new CommIds();
    CommIds b = new CommIds();
    Game game = new Game();
    Game game2 = new Game();

    String fightFile = "Joko, Klaas\n" +
            "Joko, joko, Naga\n" +
            "Klaas, klaas, Wraith";

    String map = "10 + #\n" +   //6 lang
            "##%xt \n" +
            "tX~~AA\n" +
            "^_*$  \n" +
            "+ ####";
    String map2 = "1 0+ #\n" +   //6 lang
            "##%xt \n" +
            "tX~~AA\n" +
            "^_*$  \n" +
            "+ ####";
    Map m = new Map();
    GameVector vec1 = new GameVector(1,0);
    GameVector vec2 = new GameVector(0,0);
    GameVector vec4 = new GameVector(2,0);
    GameVector vec3 = new GameVector(0,1);
    Pc pc1 = new Pc(0,vec1, 100, CreatureType.NAGA);
    Pc pc2 = new Pc(1,vec2, 100, CreatureType.WRAITH);
    Pc pc3 = new Pc(0, vec4, 100, CreatureType.NAGA);

    @Before
    public void setup() throws InvalidMapFileException, InvalidFightFileException {
        game.handleFightfile(fightFile);
        game.handleMapfile(map);
        m = game.getMap();
        a.addCommLibId(12);
        b.addCommLibId(13);
        game.addMonster(pc1);
        game.addMonster(pc2);
        List<Integer> initOrder = new ArrayList<>();
        initOrder.add(0);
        initOrder.add(1);
        game.setInitOrder(initOrder);
        game.setRoundState(RoundState.ACTNOW);

        game2.handleFightfile(fightFile);
        game2.handleMapfile(map2);
        game2.addMonster(pc3);
        game2.addMonster(pc2);
        game2.setInitOrder(initOrder);
        game2.setRoundState(RoundState.ACTNOW);
    }

    @Test
    public void creaturetype1()
    {
        assertEquals(CreatureType.FAIRY.equals(CreatureType.BOAR), false);
    }

    @Test
    public void fieldTestEquals()
    {
        Field f1 = m.getFieldAt(vec3);
        assertEquals(f1, Field.ROCK);
    }

    @Test
    public void canBiteTrue() //Bite
    {
        Bite b1 = new Bite(this.a, 0, Direction.WEST);
        assertTrue(b1.validateCommand(this.game));
    }

    @Test
    public void getPoisonFirstRound() //Posion
    {Bite b1 = new Bite(this.a, 0, Direction.WEST);
        b1.executeCommand(this.game);
        assertEquals(pc2.getPoisons().get(0), Poison.FIRSTROUND);
    }

    @Test
    public void biteDamage() //PoisonEffect
    {   int hp1 = pc2.getHp();
        Bite b1 = new Bite(this.a, 0, Direction.WEST);
        b1.executeCommand(this.game);
        int hp2 = pc2.getHp();
        assertEquals(hp1 > hp2 && hp2 == 94, true);
    }

    @Test
    public void canBiteFalse() throws InvalidMapFileException
    {
        Bite b1 = new Bite(this.a, 0, Direction.WEST);
        assertFalse(b1.validateCommand(this.game2));
    }
}
