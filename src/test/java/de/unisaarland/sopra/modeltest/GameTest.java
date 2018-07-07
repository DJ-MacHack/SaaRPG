package de.unisaarland.sopra.modeltest;

import de.unisaarland.sopra.model.*;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.utility.InvalidFightFileException;
import de.unisaarland.sopra.utility.InvalidMapFileException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Team 14 on 12.09.2016.
 * Responsible: Christopher K , Lukas Kirschner
 */
public class GameTest {

    private final static String FIGHTFILE = "Rot, Blau\n"
                                    + "Rot, Dobby, Elf\n"
                                    + "Rot,Flobby,kobold\n" + "\n"
                                    + "Blau,    Robby, RooK";

    private final static String FIGHTFILE1 = "Rot, rot\n"
            + "Rot, Dobby, Elf\n"
         //   + "Rot,Flobby,kobold\n" + "\n"
            + "rot,    dobby, RooK";

    private final static String FIGHTFILE2 = "Rot, Gelb\n"
            + "Rot, Dobby, Elf\n"
           // + "Rot,Flobby,kobold\n" + "\n"
            + "Gelb,    Pobby, Boar";

    private final static String FIGHTFILE3 = "Rot, Gelb\n"
            + "Rot, Dobby, Elf\n"
           // + "Rot,Flobby,kobold\n" + "\n"
            + "Gelb,    Pobby, Fairy";

    private final static String MAPFILE1 = "txXt%&.  t\n" +
            "txx%% ~~__\n" +
            "t^A0^^1###\n" +
            " +. #0#$##\n" +
            "1 .^A## ##\n" +
            "##### ####\n" +
            "+#~~~x~~~~\n" +
            "__#~~ ~~~~\n" +
            "_~##~^0~~~\n" +
            "$1.  **^~~";

    private Game game;

    @Before
    public void setup() throws InvalidMapFileException, InvalidFightFileException {
        game = new Game();
        game.handleFightfile(FIGHTFILE);
        game.handleMapfile(MAPFILE1);
    }

    /*
    @Test
    public void CopyTest(){
        Game game2 = new Game(game);

        assertEquals(game.getRoundState(), game2.getRoundState());
        assertEquals(game.getBoringRounds(), game2.getBoringRounds());
        assertEquals(game.getRoundCounter(), game2.getRoundCounter());
        assertEquals(game.getCurrentMonsterIndex(), game2.getCurrentMonsterIndex());
    }

    @Test
    public void CopyMapTest(){
        Game game2 = new Game(game);

        Map map1 = game.getMap();
        Map map2 = game2.getMap();
        assertTrue(map1 != map2);
    }

    @Test
    public void CopyTeamsTest(){
        Game game2 = new Game(game);

        List<Team> t1 = game.getTeams();
        List<Team> t2 = game2.getTeams();
        assertTrue(t1 != t2);

        for(int i = 0; i < t1.size(); i++){
            assertTrue(t1.get(i).equals(t2.get(i)));
        }
    }

    @Test
    public void CopyPlayerTest(){
        Game game2 = new Game(game);

        List<Player> p1 = game.getPlayers();
        List<Player> p2 = game2.getPlayers();
        assertTrue(p1 != p2);

        for(int i = 0; i < p1.size(); i++){
            assertEquals(p1.get(i), p2.get(i));
        }
    }

    @Test
    public void CopyPcTest(){
        Game game2 = new Game(game);

        List<Pc> pc1 = game.getMonsters();
        List<Pc> pc2 = game2.getMonsters();
        assertTrue(pc1 != pc2);

        for(int i = 0; i < pc1.size(); i++){
            assertEquals(pc1.get(i), pc2.get(i));
        }
    }

    @Test
    public void CopyNpcTest(){
        Game game2 = new Game(game);

        List<Npc> npc1 = game.getNpcs();
        List<Npc> npc2 = game2.getNpcs();
        assertTrue(npc1 != npc2);

        for(int i = 0; i < npc1.size(); i++){
            assertEquals(npc1.get(i), npc2.get(i));
        }
    }

    @Test
    public void CopyInitOrderTest(){
        Game game2 = new Game(game);

        List<Integer> init1 = game.getInitOrder();
        List<Integer> init2 = game2.getInitOrder();
        assertTrue(init1 != init2);

        for(int i = 0; i < init1.size(); i++){
            assertEquals(init1.get(i), init2.get(i));
        }
    }
    */

    @Test
    public void roundStateTest(){
        assertEquals(game.getRoundState(), RoundState.REGISTER);

        game.setRoundState(RoundState.ACTNOW);
        assertEquals(game.getRoundState(), RoundState.ACTNOW);
    }

    @Test
    public void boringRoundsTest(){
        assertEquals(game.getBoringRounds(), 0);

        game.increaseBoringRounds();
        assertEquals(game.getBoringRounds(), 1);
    }

    @Test
    public void roundCounterTest(){
        assertEquals(game.getRoundCounter(), 0);

        game.increaseRoundCounter();
        assertEquals(game.getRoundCounter(), 1);
    }

    @Test
    public void initOrderTest(){
        List<Integer> initOrder = new ArrayList<>();
        initOrder.add(0);
        initOrder.add(1);

        game.setInitOrder(initOrder);

        assertEquals(game.getInitOrder(), initOrder);

        assertEquals(game.getCurrentMonsterIndex(), 0);
        assertEquals(game.getMonsterActing(), 0);

        game.nextMonster();

        assertEquals(game.getMonsterActing(), 1);
    }

    @Test
    public void handleFightFile1Test() throws InvalidFightFileException {
        Game game1 = new Game();
        game1.handleFightfile(FIGHTFILE1);
       // Team rot1 = new Team("Rot");
       // Team rot2 = new Team("rot");
       // assertTrue(game1.getTeams().contains(rot1));
      //  assertTrue(game1.getTeams().contains(rot2));
        assertTrue(game1.getTeams().size() == 2);
    }

    @Test (expected=InvalidFightFileException.class)
    public void handleFightFile2Test() throws InvalidFightFileException {
        Game game1 = new Game();
        game1.handleFightfile(FIGHTFILE2);

    }

    @Test (expected=InvalidFightFileException.class)
    public void handleFightFile3Test() throws InvalidFightFileException {
        Game game1 = new Game();
        game1.handleFightfile(FIGHTFILE3);

    }

    @Test
    public void handleMapTest(){
        assertEquals(game.getFieldAt(new GameVector(0, 0)), Field.TREE);

        assertNotEquals(game.getFieldAt(new GameVector(0, 1)), Field.ICE);
        assertNotEquals(game.getFieldAt(new GameVector(1, 1)), Field.CURSE);
    }

    @Test
    public void createTeamTest(){
        assertNotNull(game.getTeam("Rot"));
        assertNotNull(game.getTeam("Blau"));

        assertNull(game.getTeam("Grün"));
        assertNull(game.getTeam("Gelb"));
    }

    @Test
    public void createPlayerTest(){
        assertEquals(game.getPlayers().size(), 3);

        Player dobby = game.getPlayerByName("Dobby");
        Player robby = game.getPlayerByName("Robby");

        assertNotNull(dobby);
        assertNotNull(robby);
        assertNull(game.getPlayerByName("Test"));

        assertFalse(dobby.getRegistered());
        assertEquals(dobby.getMonsterType(), CreatureType.ELF);

        assertEquals(robby.getTeamName(), "Blau");
    }

    @Test
    public void addMonsterTest(){
        Pc monster = new Pc(0, new GameVector(0, 0), 1, CreatureType.KOBOLD);
        game.addMonster(monster);

        assertEquals(game.getMonsters().size(), 1);
        assertNotNull(game.getMonsterById(0));
        assertNull(game.getMonsterById(10));

        assertEquals(game.getMonsterByPosition(new GameVector(0, 0)), monster);
    }

    @Test
    public void removeMonsterTest(){
        Pc monster = new Pc(0, new GameVector(0, 0), 1, CreatureType.KOBOLD);
        game.addMonster(monster);
        game.removeMonster(monster);

        assertEquals(game.getNpcs().size(), 0);
        assertNull(game.getMonsterById(0));
        assertNull(game.getMonsterById(10));

        assertNull(game.getMonsterByPosition(new GameVector(0, 0)));
    }

    @Test
    public void addNpcTest(){
        Npc fairy = new Npc(10, new GameVector(2,2), 5, CreatureType.FAIRY, 4, 10);
        game.addNpc(fairy);

        assertEquals(game.getNpcs().size(), 1);
        assertNotNull(game.getNpcById(10));
        assertNull(game.getNpcById(0));
        assertNotNull(game.getCreatureByPosition(new GameVector(2, 2)));

        assertEquals(game.getNpcByPosition(new GameVector(2, 2)), fairy);
    }

    @Test
    public void removeNpcTest(){
        Npc fairy = new Npc(10, new GameVector(2,2), 4, CreatureType.FAIRY, 4, 10);
        game.addNpc(fairy);
        game.removeNpc(fairy);

        assertEquals(game.getNpcs().size(), 0);
        assertNull(game.getNpcById(10));
        assertNull(game.getNpcById(0));
        assertNull(game.getCreatureByPosition(new GameVector(2, 2)));

        assertNull(game.getNpcByPosition(new GameVector(2, 2)));
    }

    /*
    private Game setupCopy(Game gameToCopy){
        return new Game(gameToCopy);
    }
    */

    /*
    @Test
    public void testGameEqualsTeams(){
        Game game2 = setupCopy(game);
        assertTrue(game.equals(game2));
        game2.addTeam(new Team("TeamTestFail"));
        assertFalse(game.equals(game2));
    }

    @Test
    public void testGameEqualsNpc() {
        Game game2 = setupCopy(game);
        assertTrue(game.equals(game2));
        game2.addNpc(new Npc(33,new GameVector(3,3),100,CreatureType.FAIRY,4,10));
        assertFalse(game.equals(game2));
    }

    @Test
    public void testGameEqualsMonsters() {
        Game game2 = setupCopy(game);
        assertTrue(game.equals(game2));
        game2.addMonster(new Pc(12,new GameVector(4,3),100,CreatureType.KOBOLD));
        assertFalse(game.equals(game2));
    }

    @Test
    public void testGameEqualsFringe() {
        Game game3 = game;
        assertTrue(game.equals(game3));
        Game game4 = null;
        assertFalse(game.equals(game4));
        assertFalse(game.equals(new GameVector(1,1)));
        Game game2 = setupCopy(game);
        assertEquals(game.hashCode(),game2.hashCode());
    }

    @Test
    public void testGameCopyComplex() {
        game.addMonster(new Pc(50,new GameVector(1,2),100,CreatureType.ELF));
        game.addNpc(new Npc(51,new GameVector(2,2),100,CreatureType.FAIRY,4,10));
        Team joko = new Team("TeamJoko");
        joko.addStartPosition(new GameVector(3,3));
        game.addTeam(joko);
        Game game2 = new Game(game);
        assertTrue(game.equals(game2));
    }
    */

    @Test
    public void testGameGetMapFile(){
        game.setMapFile(MAPFILE1);
        assertEquals(MAPFILE1,game.getMapFile());
    }

    @Test
    public void testGameGetFightFile(){
        assertEquals(FIGHTFILE,game.getFightFile());
    }

    @After
    public void destroy(){
        game = null;
    }
}
