package de.unisaarland.sopra.messagestest.attacktest;

/**
 * Created by Wiebk on 13.09.2016.
 */

import de.unisaarland.sopra.*;
import de.unisaarland.sopra.messages.attack.Blink;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.model.Pc;
import de.unisaarland.sopra.model.RoundState;
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

public class BlinkTest {

        public final static String FIGHTFILE = "Red, Blue\n" +
                "\n" +
                "Red, A, Wraith\n" +
                "\n" +
                "Blue, B, ELF";

        public final static String MAPFILE = "xtXt~ &.^t\n" +
                "01        ";

        private Game game;

        @Before
        public void setupGame() throws InvalidMapFileException, InvalidFightFileException {
            Game game = new Game();
            game.handleFightfile(FIGHTFILE);
            game.handleMapfile(MAPFILE);
            game.addMonster(new Pc(1, new GameVector(0,1), 100, CreatureType.WRAITH));
            game.addMonster(new Pc(2, new GameVector(1,1), 100, CreatureType.ELF));

            List<Integer> initOrder = new ArrayList();
            initOrder.add(1);
            initOrder.add(2);
            game.setInitOrder(initOrder);
            game.setCurrentMonsterIndex(0);
            game.setRoundState(RoundState.ACTNOW);
            this.game = game;
        }

        private Blink getBlink(){
            return new Blink(new CommIds(), 1, 2, 1);
        }

        @Test
        public void testNormalBlink() throws InvalidMapFileException, InvalidFightFileException{
            Blink blink = getBlink();
            assertTrue(blink.validateCommand(game));

            blink.executeCommand(game);
            assertEquals(game.getCreatureByPosition(new GameVector(2, 1)).getCreatureType(), CreatureType.WRAITH);
        }

         @Test
         public void creatureIsNoWraithTest(){
            Blink blink = new Blink(new CommIds(), 2, 0, 1);
            assertFalse(blink.validateCommand(game));
         }

         @Test
        public void noMoreEngergyTest(){
            Blink blink = getBlink();
            assertTrue(blink.validateCommand(game));

            Pc monster = game.getMonsterById(1);
            monster.setEnergy(0);

            assertFalse(blink.validateCommand(game));
        }

        @After
        public void destroy(){
        game = null;
    }

}
