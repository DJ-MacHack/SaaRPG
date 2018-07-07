package de.unisaarland.sopra.publictest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.EventFactory;
import de.unisaarland.sopra.MonsterType;

/**
 * Diese EventFactory verwenden wir in unseren Public-Tests.
 * <p>
 * Wir halten es für eine sehr schlechte Idee, die EventFactory Strings returnen zu lassen und machen es
 * hier nur, weil es für die Tests die Variante ist, mit der wir Ihnen am wenigsten Code, und damit am besten
 * verständliche Tests vorlegen können. Wir empfehlen Ihnen, für Ihre eigenen Tests, und erst Recht
 * für Ihre Implementierung, etwas eigenes zu schreiben.
 * <p>
 * Created by akampmann on 19/08/16.
 */
public class TestEventFactory implements EventFactory<String> {


    @Override
    public String createRegistered(int playerId, String name, MonsterType monsterType, String teamName, int startNumber, int x, int y) {

        return "Registered(" + playerId + ", " + name + ", " + monsterType + ", " + teamName + ", " + startNumber + ", " + x + ", " + y + ")";

    }

    @Override
    public String createRegistrationAborted() {

        return "RegistrationAborted()";

    }

    @Override
    public String createMoved(int actorId, Direction direction) {

        return "Moved(" + actorId + ", " + direction + ")";

    }

    @Override
    public String createStabbed(int actorId, Direction direction) {

        return "Stabbed(" + actorId + ", " + direction + ")";

    }

    @Override
    public String createSlashed(int actorId, Direction direction) {

        return "Slashed(" + actorId + ", " + direction + ")";

    }

    @Override
    public String createCut(int actorId, Direction direction) {

        return "Cut(" + actorId + ", " + direction + ")";

    }

    @Override
    public String createBitten(int actorId, Direction direction) {

        return "Bitten(" + actorId + ", " + direction + ")";

    }

    @Override
    public String createStared(int actorId, Direction direction) {

        return "Stared(" + actorId + ", " + direction + ")";

    }

    @Override
    public String createClawed(int actorId, Direction direction) {

        return "Clawed(" + actorId + ", " + direction + ")";

    }

    @Override
    public String createCrushed(int actorId, Direction direction) {

        return "Crushed(" + actorId + ", " + direction + ")";

    }

    @Override
    public String createSinged(int actorId, Direction direction) {

        return "Singed(" + actorId + ", " + direction + ")";

    }

    @Override
    public String createShot(int actorId, Direction direction) {

        return "Shoot(" + actorId + ", " + direction + ")";

    }

    @Override
    public String createBurned(int actorId) {

        return "Burned(" + actorId + ")";

    }

    @Override
    public String createCast(int actorId, int x, int y) {

        return "Cast(" + actorId + ", " + x + ", " + y + ")";

    }

    @Override
    public String createBlinked(int actorId, int x, int y) {

        return "Blinked(" + actorId + ", " + x + ", " + y + ")";

    }

    @Override
    public String createSwapped(int actorId, int x, int y) {

        return "Swapped(" + actorId + ", " + x + ", " + y + ")";

    }

    @Override
    public String createWarCry(int actorId, String cry) {

        return "WarCry(" + actorId + ", " + cry + ")";

    }

    @Override
    public String createDied(int entityId) {

        return "Died(" + entityId + ")";

    }

    @Override
    public String createKicked(int monsterId, String message) {

        return "Kicked(" + monsterId + ", " + message + ")";

    }

    @Override
    public String createBoarSpawned(int creatureId, int x, int y) {

        return "BoarSpawned(" + creatureId + ", " + x + ", " + y + ")";

    }

    @Override
    public String createFairySpawned(int creatureId, int x, int y) {

        return "FairySpawned(" + creatureId + ", " + x + ", " + y + ")";

    }

    @Override
    public String createActNow(int monsterId) {

        return "ActNow(" + monsterId + ")";

    }

    @Override
    public String createFieldEffect(int x, int y, int value) {

        return "FieldEffect(" + x + ", " + y + ", " + value + ")";

    }

    @Override
    public String createPoisonEffect(int entityId, int value) {

        return "PoisonEffect(" + entityId + ", " + value + ")";

    }

    @Override
    public String createDoneActing(int actorId) {

        return "DoneActing(" + actorId + ")";

    }

    @Override
    public String createMap(String value) {

        return "Map(" + value + ")";

    }

    @Override
    public String createFight(String value) {

        return "Fight(" + value + ")";

    }

    @Override
    public String createRoundBegin(int value) {

        return "RoundBegin(" + value + ")";

    }

    @Override
    public String createRoundEnd(int value, int boredom) {

        return "RoundEnd(" + value + ", " + boredom + ")";

    }

    @Override
    public String createWinner(String teamName) {

        return "Winner(" + teamName + ")";

    }

    @Override
    public String createBoarAttack(int boarId, int victimId) {

        return "BoarAttack(" + boarId + ", " + victimId + ")";

    }


}
