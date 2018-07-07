package de.unisaarland.sopra;

import de.unisaarland.sopra.utility.InvalidFightFileException;
import de.unisaarland.sopra.utility.InvalidMapFileException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Team14 on 9/12/16.
 * Responsible for Player JUnit tests: Lukas Kirschner
 */
public class ServerTests {

    private final static String VALIDMAP = "10\n  ";
    private final static String VALIDFIGHT = "TeamJoko, TeamKlaas\n\nTeamJoko, Joko, Kobold\nTeamKlaas,Klaas, Elf";

    @Test
    public void testInitValidServer() throws InvalidFightFileException, InvalidMapFileException {
        Server mySrv = new Server(5000,1000,VALIDFIGHT,"1 #\n 0#\nx  ");
        assertNotNull(mySrv);
    }

    @Test(expected = InvalidFightFileException.class) //Expected Fail: At least two teams expected in FightFile
    public void testInvalidFightfile1TooFewTeams() throws InvalidFightFileException, InvalidMapFileException {
        Server mySrv = new Server(0xfffe, 1000, "TeamJoko\nTeamJoko, Joko, Elf", VALIDMAP);
        assertNull(mySrv);
    }

    @Test(expected = InvalidFightFileException.class) //Expected Fail: Double occurrence of Team Name
    public void testInvalidFightfile2DoubleTeamName() throws InvalidFightFileException, InvalidMapFileException {
        Server mySrv = new Server(1024, 1000, "TeamJoko, TeamJoko\nTeamJoko, Joko, Elf", VALIDMAP);
        assertNull(mySrv);
    }

    @Test(expected = InvalidFightFileException.class) //Expected Fail: Double Occurrence of PlayerName
    public void testInvalidFightfile3DoublePlayerName() throws InvalidFightFileException, InvalidMapFileException {
        Server mySrv = new Server(0xfffd, 1000, "TeamJoko, TeamKlaas\nTeamJoko, Joko, Elf\nTeamKlaas, Joko, Wraith", VALIDMAP);
        assertNull(mySrv);
    }

    @Test(expected = InvalidFightFileException.class) //Expected Fail: One player per team expected
    public void testInvalidFightfile4TooFewPlayers() throws InvalidFightFileException, InvalidMapFileException {
        Server mySrv = new Server(12344, 1000, "TeamJoko, TeamKlaas\nTeamJoko, Joko, Elf", VALIDMAP);
        assertNull(mySrv);
    }

    @Test //Valid Initialisation, because PlayerNames and TeamNames differ capitalization
    public void testValidServer1CapitalizedLetters() throws InvalidFightFileException, InvalidMapFileException {
        Server mySrv = new Server(10124,1000,"TeamJoko,\t   TeamJOko\n\n\nTeamJoko\t,\t    Joko , Kobold\n\n\n\n\n\nTeamJOko,JOko, Kobold",VALIDMAP);
        assertNotNull(mySrv);
    }
/* //IS A VALID MAPFILE!
    @Test(expected = InvalidMapFileException.class) //Expected Fail: Too many spawn areas defined in mapfile
    public void testInvalidMapfile1TooManySpawnAreas() throws InvalidFightFileException, InvalidMapFileException {
        Server mySrv = new Server(12345, 1000, VALIDFIGHT, "01\n2 ");
    }
*/
    @Test(expected = InvalidMapFileException.class) //Expected Fail: Too few spawn areas defined in mapfile
    public void testInvalidMapfile2TooFewSpawnAreas() throws InvalidFightFileException, InvalidMapFileException {
        Server mySrv = new Server(12335, 1000, VALIDFIGHT, "0#\n~ ");
        assertNull(mySrv);
    }

    @Test(expected = InvalidMapFileException.class) //Expected Fail: Illegal Character in Map
    public void testInvalidMapfile3IllegalCharacter() throws InvalidFightFileException, InvalidMapFileException {
        Server mySrv = new Server(12245, 1000, VALIDFIGHT, "0#\na ");
        assertNull(mySrv);
    }

    @Test(expected = InvalidMapFileException.class) //Expected Fail: Non-rectangular mapfile
    public void testInvalidMapfile4NonRectangularMap() throws InvalidFightFileException, InvalidMapFileException {
        Server mySrv = new Server(11345, 1000, VALIDFIGHT, "0# \n   \nxxxx");
        assertNull(mySrv);
    }

    @Test //Non-square map
    public void testValidMapfile1NonSquare() throws InvalidFightFileException, InvalidMapFileException {
        Server mySrv = new Server(0xfffa,1000,VALIDFIGHT,"01 \n#x \n^^ \nXxt");
        assertNotNull(mySrv);
    }

    @Test //All possible tiles
    public void testValidMapfile2AllPossibleLetters() throws InvalidFightFileException, InvalidMapFileException {
        Server mySrv = new Server(1025,1000,VALIDFIGHT,"01 +\n#%&~\nxXtT\n^A_*\n$+ #");
        assertNotNull(mySrv);
    }

    @Test
    public void testServerClosed() throws InvalidMapFileException,InvalidFightFileException{
        Server srv1 = new Server(1337,1000,VALIDFIGHT,VALIDMAP);
        srv1.close();
        Server srv2 = new Server(1337,1000,VALIDFIGHT,VALIDMAP);
        assertNotNull(srv2);
    }

    @Test
    public void testServerCloseAfterException() throws InvalidFightFileException,InvalidMapFileException{
        try{
            Server srv1 = new Server(3333, 1000, VALIDFIGHT, "lol falsche Datei");
            assertNotNull(srv1);
        } catch (InvalidMapFileException e){
            Server srv2 = new Server(3333, 1000, VALIDFIGHT, VALIDMAP);
            assertNotNull(srv2);
        }

    }
}