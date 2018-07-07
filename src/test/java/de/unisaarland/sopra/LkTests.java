package de.unisaarland.sopra;

import de.unisaarland.sopra.messages.attack.Cast;
import de.unisaarland.sopra.messages.attack.Move;
import de.unisaarland.sopra.messages.attack.Stab;
import de.unisaarland.sopra.messages.Command;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.model.Pc;
import de.unisaarland.sopra.model.RoundState;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.utility.InvalidFightFileException;
import de.unisaarland.sopra.utility.InvalidMapFileException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * JUnit-Tests by Lukas Kirschner
 */
public class LkTests {

    private final static int STABHPLOSS = 7;
    private final static int CASTHPLOSS = 10;

    private final static String MAPFILE = "0110\n    \n####\n    ";
    private final static String FIGHTFILE="Mordor, Eriador\nEriador, Frodo, KOBOLD\nMordor, Sauron, Wraith\nEriador, Sam, KOBOLD\nMordor, WitchKingOfAngmar, Rook";
    private Game myGame;
    private CommIds myCid;
    private Pc frodo, sam, sauron, witchKingOfAngmar;

    @Before
    public void initializeBattle() throws InvalidMapFileException, InvalidFightFileException {
        frodo = new Pc(0,new GameVector(1,0),100, CreatureType.KOBOLD);
        sam = new Pc(1,new GameVector(2,0),100,CreatureType.KOBOLD);
        sauron = new Pc(2,new GameVector(0,0),100,CreatureType.WRAITH);
        witchKingOfAngmar = new Pc(3,new GameVector(3,0),100,CreatureType.ROOK);

        myGame = new Game();
        myGame.handleFightfile(FIGHTFILE);
        myGame.handleMapfile(MAPFILE);
        myCid = new CommIds();

        frodo.setEnergy(1000);
        sam.setEnergy(1000);
        sauron.setEnergy(1000);
        witchKingOfAngmar.setEnergy(1000);

        myGame.addMonster(frodo);
        myGame.addMonster(sam);
        myGame.addMonster(sauron);
        myGame.addMonster(witchKingOfAngmar);
        /*
        SAU FRO SAM WIT
        ggg ggg ggg ggg
        ### ### ### ###
        ggg ggg ggg ggg
         */
        List<Integer> initOrder = new ArrayList();
        initOrder.add(0);
        initOrder.add(1);
        initOrder.add(2);
        initOrder.add(3);
        myGame.setInitOrder(initOrder);
        myGame.setCurrentMonsterIndex(0);
        myGame.setRoundState(RoundState.ACTNOW);
    }

    @Test
    public void testFightComplex(){
        myGame.setCurrentMonsterIndex(2);
        final int FR0 = frodo.getHp();
        Command sauronStab = new Stab(myCid,2,Direction.EAST);  //Sauron stabs Frodo
        assertTrue(sauronStab.validateCommand(myGame));
        final int FRE = FR0 - STABHPLOSS;
        sauronStab.executeCommand(myGame);
        final int FR1 = frodo.getHp();
        assertEquals(FRE,FR1);
        final int SA0 = sauron.getHp();
        myGame.setCurrentMonsterIndex(0);
        Command frodoStab = new Stab(myCid,0,Direction.WEST);   //Frodo stabs back Sauron
        assertTrue(frodoStab.validateCommand(myGame));
        final int SAE = SA0 - STABHPLOSS;
        frodoStab.executeCommand(myGame);
        final int SA1 = sauron.getHp();
        assertEquals(SAE,SA1);
        sam.receiveDamage(100);                                 //Sam commits suicide
        assertTrue(sam.isDead());
        final int FR2 = frodo.getHp();
        myGame.setCurrentMonsterIndex(3);
        Command witchCast = new Cast(myCid,3,1,0);              //The Witch King of Angmar casts against Frodo
        assertTrue(witchCast.validateCommand(myGame));
        final int FRX = FR2 - CASTHPLOSS;
        witchCast.executeCommand(myGame);
        final int FR3 = frodo.getHp();
        assertEquals(FRX,FR3);
    }

    @Test
    public void testStabToDeath(){
        int FR0,FRE,FR1;
        myGame.setCurrentMonsterIndex(2);
        while(!frodo.isDead()){
            Command sauronStab = new Stab(myCid,2,Direction.EAST);
            FR0 = frodo.getHp();
            assertTrue(sauronStab.validateCommand(myGame));
            FRE = (FR0 > 5)? FR0 - STABHPLOSS : 0;
            sauronStab.executeCommand(myGame);
            FR1 = frodo.getHp();
            assertEquals(FRE,FR1);
            sauron.setEnergy(1000); //Sauron is a cheater!
        }
    }

    @Test
    public void testBite() throws InvalidMapFileException, InvalidFightFileException {
        Game game = new Game();
        game.handleFightfile("Team1,Team2\nTeam1,Klaus,Naga\nTeam2,Thorsten,Wraith");
        game.handleMapfile("01\n##");
        Pc klaus = new Pc(0,new GameVector(0,0),100,CreatureType.NAGA);
        Pc thorsten = new Pc(1,new GameVector(1,0),100,CreatureType.WRAITH);
        game.addMonster(klaus);
        game.addMonster(thorsten);
    }

    @Test
    public void testGameCopyAndMove() throws InvalidFightFileException, InvalidMapFileException {
        Game game = new Game();
        game.handleFightfile("Team1,Team2\nTeam1,Klaus,Naga\nTeam2,Thorsten,Wraith");
        game.handleMapfile("01\n  ");
        Pc klaus = new Pc(0,new GameVector(0,0),100,CreatureType.NAGA);
        Pc thorsten = new Pc(1,new GameVector(1,0),100,CreatureType.WRAITH);
        game.addMonster(klaus);
        game.addMonster(thorsten);
        Move e = new Move(null,0,Direction.SOUTH_EAST);
        assertTrue(e.validateEvent(game,null));
        Game game2 = new Game(game);
        e.executeEvent(game2,null);
        Move e2 = new Move(null,0,Direction.EAST);
        assertTrue(e2.validateEvent(game2,null));

    }
}