package de.unisaarland.sopra.messagestest;

import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.messages.WarCry;
import de.unisaarland.sopra.model.*;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.utility.InvalidFightFileException;
import de.unisaarland.sopra.utility.InvalidMapFileException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Karla on 13.09.2016.
 */
public class WarCryTest {

    private final static String fightFile = "Rot, Blau\n"
            + "Rot, Christopher, Elf\n"
            + "Rot, Lukas, Kobold\n" + "\n"
            + "Blau, Henrik, RooK";


    public final static String mapFile = "txXt%&.  t\n" +
            "txx%% ~~__\n" +
            "t^A0^^1###\n" +
            " +. #0#$##\n" +
            "1 .^A## ##\n" +
            "##### ####\n" +
            "+#~~~x~~~~\n" +
            "__#~~ ~~~~\n" +
            "_~##~^0~~~\n" +
            "$1.  **^~~";           // 10x10 map with 2 teams 3 players each

    private Game game;
    private CommIds commIds;


    @Before
    public void setUp() throws InvalidMapFileException, InvalidFightFileException {
        commIds = new CommIds();
        game = new Game();
        game.handleFightfile(fightFile);
        game.handleMapfile(mapFile);
        game.setRoundState(RoundState.ACTNOW);
        game.addMonster(new Pc(0, new GameVector(2, 2), 100, CreatureType.ELF));
        game.addMonster(new Pc(1, new GameVector(5, 2), 100, CreatureType.KOBOLD));
        List<Integer> initOrder = new ArrayList<>();
        initOrder.add(0);
        initOrder.add(1);
        game.setInitOrder(initOrder);
        game.setCurrentMonsterIndex(0);
    }

    @Test
    public void validWarCryTest() {
        assertEquals(RoundState.ACTNOW, game.getRoundState());

        WarCry cry = new WarCry(commIds , 0, "THIS IS SPARTAAAAAAA");
        assertTrue(cry.validateCommand(game));
        cry.executeCommand(game);

        game.nextMonster();

        WarCry cry1 = new WarCry(commIds, 1, "Luke... I'm your Father. NOOOOOUU!");
        assertTrue(cry1.validateCommand(game));
    }

    @Test
    public void invalidWarCryTooLong(){
        assertEquals(RoundState.ACTNOW, game.getRoundState());

        WarCry cry1 = new WarCry(commIds, 1, "ldsifbasldkafbadslkfbadslkhbfaldhskfdhksbfadklsjnbfgvdbcfvgbfdmskfvgbjdx,dcfhfvndmfvgbfjdcfvgbnfmkd,fjhbunfmd,lfrhfjfkcmrnhjfcemrnhtbjmfefmre");
        assertFalse(cry1.validateCommand(game));
    }

    @Test
    public void oneWarCryTooMuch(){
        assertEquals(RoundState.ACTNOW, game.getRoundState());

        WarCry cry = new WarCry(commIds , 0, "Never gonna give you up");
        assertTrue(cry.validateCommand(game));

        cry.executeCommand(game);

        WarCry cry1 = new WarCry(commIds, 0, "Never gonna let you done");
        assertFalse(cry1.validateCommand(game));
    }

    @After
    public void destroy(){
        game = null;
    }
}
