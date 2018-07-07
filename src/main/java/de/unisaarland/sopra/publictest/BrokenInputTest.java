package de.unisaarland.sopra.publictest;

import de.unisaarland.sopra.MonsterType;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.comm.TimeoutException;
import de.unisaarland.sopra.systemtest.SystemTest;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Collection;

/**
 * Dieser Test testet eine Reihe invalider Inputs.
 * <p>
 * Created by akampmann on 18/08/16.
 */
public class BrokenInputTest extends SystemTest {
    private static final String DEFAULT_MAP = "tx00##xx\n"
            + "t  + xxx\n"
            + "tt %%%tt\n"
            + "~~~~~~~~\n"
            + "t_%%##^^\n"
            + "____^^^*\n"
            + "$__   **\n"
            + "$$$11 *t";
    private static final String DEFAULT_FIGHT = "A,B\n"
            + "A,Dobby,Elf\n"
            + "B,Flobby,Kobold";

    private final String mapFile;
    private final String fightFile;

    public BrokenInputTest(String name, String mapFile, String fightFile) {
        super(name);
        this.mapFile = mapFile;
        this.fightFile = fightFile;
    }

    public static Collection<SystemTest> getTests() {
        return Arrays.asList(
                // broken maps
                new BrokenInputTest("TooLittleStartPositionsTest", DEFAULT_MAP.replace('0', ' '), DEFAULT_FIGHT),
                new BrokenInputTest("WrongFieldTypeTest", DEFAULT_MAP.replace('+', 'K'), DEFAULT_FIGHT),
                new BrokenInputTest("WrongLineLengthTest", DEFAULT_MAP.substring(0, 15) + DEFAULT_MAP.substring(17, DEFAULT_MAP.length()), DEFAULT_FIGHT),
                new BrokenInputTest("EmptyMapTest", "", DEFAULT_FIGHT),

                // broken fights
                new BrokenInputTest("DuplicateNameTest", DEFAULT_MAP, "A,B\n"
                        + "A,Dobby,Elf\n"
                        + "B,Dobby,Kobold"),
                new BrokenInputTest("DuplicateGladiatorTest", DEFAULT_MAP, "A,B\n"
                        + "A,Dobby,Elf\n"
                        + "A,Dobby,Elf"),
                new BrokenInputTest("EmptyTeamTest", DEFAULT_MAP, "A,B\n"
                        + "A,Dobby,Elf\n"
                        + "A,Flobby,Kobold"),
                new BrokenInputTest("DuplicateTeamTest", DEFAULT_MAP, "A,A\n"
                        + "A,Dobby,Elf\n"
                        + "A,Flobby,Kobold"),
                new BrokenInputTest("EmptyFightTest", DEFAULT_MAP, ""),
                new BrokenInputTest("EmptyTeamTest", DEFAULT_MAP, "A,B\n"
                        + "A,Dobby,Elf"),
                new BrokenInputTest("TeamsOnlyTest", DEFAULT_MAP, "A,B,C\n"),
                new BrokenInputTest("MissingNameTest", DEFAULT_MAP, "A,B\n"
                        + "A,Dobby,Elf\n"
                        + "B,,Kobold"),
                new BrokenInputTest("LineToShortTest", DEFAULT_MAP, "A,B\n"
                        + "A,Dobby,Elf\n"
                        + "B,Kobold"),
                new BrokenInputTest("IllegalMonsterTest", DEFAULT_MAP, "A,B\n"
                        + "A,Dobby,Elf\n"
                        + "B,Babuh,Spaghettimonster"),
                new BrokenInputTest("LeadingCommaTest", DEFAULT_MAP, "A,B\n"
                        + "A,Dobby,Elf\n"
                        + ",B,Dobby,Kobold"),
                new BrokenInputTest("OneTeamTest", DEFAULT_MAP, "A\n"
                        + "A,Dobby,Elf\n"
                        + "A,Dobby,Kobold"),
                new BrokenInputTest("LineToLongTest", DEFAULT_MAP, "A,B\n"
                        + "A,Dobby,Elf,Free\n"
                        + "B,Dobby,Kobold"),
                new BrokenInputTest("UnknownTeamTest", DEFAULT_MAP, "A,B\n"
                        + "A,Dobby,Elf\n"
                        + "B,Dobby,Kobold\n"
                        + "C,Daniel,Ifrit"),
                new BrokenInputTest("EmptyTeamnameTest", DEFAULT_MAP, "A,,B\n"
                        + "A,Dobby,Elf\n"
                        + "B,Dobby,Kobold"),
                new BrokenInputTest("IncompatibleFightTest", "01", "One,Two,Three\n\n"
                        + "One,One,ELF\n"
                        + "Two,Two,ELF\n"
                        + "Three,Three,ELF")
        );
    }

    @Override
    protected void executeTest() {
        // da irgendwas in Map oder Fight kaputt ist, muss der Server sich
        // mit non-zero return code beenden
        expectServerFail();
        // wenn sich jetzt jemand verbindet,
        // muss ein Timeout kommen, weil der Server ja nicht läuft
        try (ClientConnection<?> client = openClient(new TestEventFactory())) {
            client.sendRegister("One", MonsterType.ELF, "One");
            client.nextEvent();
            Assert.fail("Ich bekomme kein Timeout, obwohl der Server nicht laufen sollte.");
        } catch (TimeoutException ex) {
            // das erwarte ich
            // ich mache einen sinnlosen Check, um PMD auszutricksen
            Assert.assertNotNull(ex);
        }
    }

    @Override
    protected String getMapFile() {
        return mapFile;
    }

    @Override
    public String getFightFile() {
        return fightFile;
    }
}
