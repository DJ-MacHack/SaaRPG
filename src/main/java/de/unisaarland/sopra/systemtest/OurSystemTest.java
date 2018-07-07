package de.unisaarland.sopra.systemtest;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.MonsterType;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.*;
import de.unisaarland.sopra.messages.attack.*;
import de.unisaarland.sopra.model.CreatureType;
import org.junit.Assert;

import java.util.List;

/**
 * Created by Team 14 on 16.09.2016.
 */
public abstract class OurSystemTest extends SystemTest {

    /**
     * @param name der Name des Tests.
     */
    protected OurSystemTest(String name) {
        super(name);
    }

    public ClientConnection<Event> register(int id, String name, CreatureType creatureType, String team, int x, int y) {
        ClientConnection<Event> cc = openClient(new EventFactoryImpl());
        cc.sendRegister(name, MonsterType.valueOf(creatureType.name()), team);

        Event mapEvent = cc.nextEvent();
        assertMap(mapEvent, getMapFile());

        Event fightEvent = cc.nextEvent();
        assertFight(fightEvent, getFightFile());

        Event regEvent = cc.nextEvent();
        assertRegisterEvent(regEvent, id, name, creatureType, team, x, y);

        return cc;
    }

    public final void assertRegisterEvent(Event event, int id, String name, CreatureType creatureType, String team, int x, int y) {
        Assert.assertEquals(Register.class, event.getClass());
        Register regEvent = (Register) event;

        Assert.assertEquals(id, regEvent.getId());
        Assert.assertEquals(name, regEvent.getName());
        Assert.assertEquals(creatureType, regEvent.getCreatureType());
        Assert.assertEquals(team, regEvent.getTeamName());
        Assert.assertEquals(x, regEvent.getxCoord());
        Assert.assertEquals(y, regEvent.getyCoord());
    }

    public final void assertRegistrationAborted(Event event) {
        Assert.assertEquals(RegistrationAborted.class, event.getClass());
    }

    public final void assertMoved(Event event, int actorId, Direction direction) {
        Assert.assertEquals(Move.class, event.getClass());
        Move moveEvent = (Move) event;

        Assert.assertEquals(actorId, moveEvent.getActorId());
        Assert.assertEquals(direction, moveEvent.getDirection());
    }

    public final void assertStabbed(Event event, int actorId, Direction direction) {
        Assert.assertEquals(Stab.class, event.getClass());
        Stab stabEvent = (Stab) event;

        Assert.assertEquals(actorId, stabEvent.getActorId());
        Assert.assertEquals(direction, stabEvent.getDirection());
    }

    public final void assertSlashed(Event event, int actorId, Direction direction) {
        Assert.assertEquals(Slash.class, event.getClass());
        Slash slashEvent = (Slash) event;

        Assert.assertEquals(actorId, slashEvent.getActorId());
        Assert.assertEquals(direction, slashEvent.getDirection());
    }

    public final void assertStared(Event event, int actorId, Direction direction) {
        Assert.assertEquals(Stare.class, event.getClass());
        Stare attackEvent = (Stare) event;

        Assert.assertEquals(actorId, attackEvent.getActorId());
        Assert.assertEquals(direction, attackEvent.getDirection());
    }

    public final void assertClaw(Event event, int actorId, Direction direction) {
        Assert.assertEquals(Claw.class, event.getClass());
        Claw attackEvent = (Claw) event;

        Assert.assertEquals(actorId, attackEvent.getActorId());
        Assert.assertEquals(direction, attackEvent.getDirection());
    }

    public final void assertCrush(Event event, int actorId, Direction direction) {
        Assert.assertEquals(Crush.class, event.getClass());
        Crush attackEvent = (Crush) event;

        Assert.assertEquals(actorId, attackEvent.getActorId());
        Assert.assertEquals(direction, attackEvent.getDirection());
    }

    public final void assertSinged(Event event, int actorId, Direction direction) {
        Assert.assertEquals(Singe.class, event.getClass());
        Singe attackEvent = (Singe) event;

        Assert.assertEquals(actorId, attackEvent.getActorId());
        Assert.assertEquals(direction, attackEvent.getDirection());
    }

    public final void assertShot(Event event, int actorId, Direction direction) {
        Assert.assertEquals(Shoot.class, event.getClass());
        Shoot attackEvent = (Shoot) event;

        Assert.assertEquals(actorId, attackEvent.getActorId());
        Assert.assertEquals(direction, attackEvent.getDirection());
    }

    public final void assertBurned(Event event, int actorId) {
        Assert.assertEquals(Burn.class, event.getClass());
        Burn attackEvent = (Burn) event;

        Assert.assertEquals(actorId, attackEvent.getActorId());
    }

    public final void assertCast(Event event, int actorId, int x, int y) {
        Assert.assertEquals(Cast.class, event.getClass());
        Cast attackEvent = (Cast) event;

        Assert.assertEquals(actorId, attackEvent.getActorId());
        Assert.assertEquals(x, attackEvent.getxCoord());
        Assert.assertEquals(y, attackEvent.getyCoord());
    }

    public final void assertBlinked(Event event, int actorId, int x, int y) {
        Assert.assertEquals(Blink.class, event.getClass());
        Blink attackEvent = (Blink) event;

        Assert.assertEquals(actorId, attackEvent.getActorId());
        Assert.assertEquals(x, attackEvent.getX());
        Assert.assertEquals(y, attackEvent.getY());
    }

    public final void assertSwapped(Event event, int actorId, int x, int y) {
        Assert.assertEquals(Swap.class, event.getClass());
        Swap attackEvent = (Swap) event;

        Assert.assertEquals(actorId, attackEvent.getActorId());
        Assert.assertEquals(x, attackEvent.getxCoord());
        Assert.assertEquals(y, attackEvent.getyCoord());
    }

    public final void assertCut(Event event, int actorId, Direction direction) {
        Assert.assertEquals(Cut.class, event.getClass());
        Cut attackEvent = (Cut) event;

        Assert.assertEquals(actorId, attackEvent.getActorId());
        Assert.assertEquals(direction, attackEvent.getDirection());
    }

    public final void assertBitten(Event event, int actorId, Direction direction) {
        Assert.assertEquals(Bite.class, event.getClass());
        Bite attackEvent = (Bite) event;

        Assert.assertEquals(actorId, attackEvent.getActorId());
        Assert.assertEquals(direction, attackEvent.getDirection());
    }

    public final void assertWarCry(Event event, int actorId, String warCry) {
        Assert.assertEquals(WarCry.class, event.getClass());
        WarCry attackEvent = (WarCry) event;

        Assert.assertEquals(actorId, attackEvent.getActorId());
        Assert.assertEquals(warCry, attackEvent.getCry());
    }

    public final void assertPoisonEffect(Event event, int entityId, int value) {
        Assert.assertEquals(PoisonEffect.class, event.getClass());
        PoisonEffect attackEvent = (PoisonEffect) event;

        Assert.assertEquals(entityId, attackEvent.getEntityId());
        Assert.assertEquals(value, attackEvent.getValue());
    }

    public final void assertBoarAttack(Event event, int boarId, int victimId) {
        Assert.assertEquals(BoarAttack.class, event.getClass());
        BoarAttack attackEvent = (BoarAttack) event;

        Assert.assertEquals(boarId, attackEvent.getBoardId());
        Assert.assertEquals(victimId, attackEvent.getVictimId());
    }

    public final void assertDied(Event event, int entityId) {
        Assert.assertEquals(Died.class, event.getClass());
        Died diedEvent = (Died) event;

        Assert.assertEquals(entityId, diedEvent.getEntityId());
    }

    public final void assertKicked(Event event, int monsterId) {
        Assert.assertEquals(Kicked.class, event.getClass());
        Kicked kickedEvent = (Kicked) event;

        Assert.assertEquals(monsterId, kickedEvent.getMonsterId());
    }

    public final void assertBoarSpawned(Event event, int creatureId, int x, int y) {
        Assert.assertEquals(BoarSpawned.class, event.getClass());
        BoarSpawned boarSpawnedEvent = (BoarSpawned) event;

        Assert.assertEquals(creatureId, boarSpawnedEvent.getCreatureId());
        Assert.assertEquals(x, boarSpawnedEvent.getX_coord());
        Assert.assertEquals(y, boarSpawnedEvent.getY_coord());
    }

    public final void assertFairySpawned(Event event, int creatureId, int x, int y) {
        Assert.assertEquals(FairySpawned.class, event.getClass());
        FairySpawned fairySpawnedEvent = (FairySpawned) event;

        Assert.assertEquals(creatureId, fairySpawnedEvent.getCreatureId());
        Assert.assertEquals(x, fairySpawnedEvent.getX_coord());
        Assert.assertEquals(y, fairySpawnedEvent.getY_coord());
    }

    public final void assertActNow(Event event, int monsterId) {
        Assert.assertEquals(ActNow.class, event.getClass());
        ActNow actNowEvent = (ActNow) event;

        Assert.assertEquals(monsterId, actNowEvent.getMonsterId());
    }

    public final void assertFieldEffect(Event event, int x, int y, int value) {
        Assert.assertEquals(FieldEffect.class, event.getClass());
        FieldEffect fieldEffectEvent = (FieldEffect) event;

        Assert.assertEquals(x, fieldEffectEvent.getxCoord());
        Assert.assertEquals(y, fieldEffectEvent.getyCoord());
        Assert.assertEquals(value, fieldEffectEvent.getValue());
    }

    public final void assertDoneActing(Event event, int actorId) {
        Assert.assertEquals(DoneActing.class, event.getClass());
        DoneActing doneActingEvent = (DoneActing) event;

        Assert.assertEquals(actorId, doneActingEvent.getActorId());
    }

    public final void assertMap(Event event, String map) {
        Assert.assertEquals(MapFile.class, event.getClass());
        MapFile mapFileEvent = (MapFile) event;

        Assert.assertEquals(map, mapFileEvent.getValue());
    }

    public final void assertFight(Event event, String fight) {
        Assert.assertEquals(Fight.class, event.getClass());
        Fight fightEvent = (Fight) event;

        Assert.assertEquals(fight, fightEvent.getFightFile());
    }

    public final void assertRoundBegin(Event event, int round) {
        Assert.assertEquals(RoundBegin.class, event.getClass());
        RoundBegin roundBeginEvent = (RoundBegin) event;

        Assert.assertEquals(round, roundBeginEvent.getRoundNumber());
    }

    public final void assertRoundEnd(Event event, int round, int boredom) {
        Assert.assertEquals(RoundEnd.class, event.getClass());
        RoundEnd roundEndEvent = (RoundEnd) event;

        Assert.assertEquals(round, roundEndEvent.getRoundNumber());
        Assert.assertEquals(boredom, roundEndEvent.getBoredom());
    }

    public final void assertWinner(Event event, String winner) {
        Assert.assertEquals(Winner.class, event.getClass());
        Winner winnerEvent = (Winner) event;

        Assert.assertEquals(winner, winnerEvent.getTeamName());
    }

    private void assertSameEvents(Event e1, Event e2) {
        //TODO 1: Write Equals()-methods for events? Right now, only the class name is tested for equality
        //TODO 2: Noooo!
        Assert.assertEquals(e1.getClass(), e2.getClass());
    }

    public final Event assertAndMerge(List<ClientConnection<Event>> ccs) {
        Event tmp = null;
        for (ClientConnection<Event> cc : ccs) {
            if (tmp == null) {
                tmp = cc.nextEvent();
            } else {
                assertSameEvents(tmp, cc.nextEvent());
            }
        }
        return tmp;
    }

    @SafeVarargs
    public final Event assertAndMerge(ClientConnection<Event>... ccs) {
        Event tmp = null;
        for (ClientConnection<Event> cc : ccs) {
            if (tmp == null) {
                tmp = cc.nextEvent();
            } else {
                assertSameEvents(tmp, cc.nextEvent());
            }
        }
        return tmp;
    }

    public final Event assertAndMerge(ClientConnection<Event> c1, ClientConnection<Event> c2) {
        Event e1 = c1.nextEvent();
        Event e2 = c2.nextEvent();
        assertSameEvents(e1, e2);
        return e1;
    }

    public static final String buildValidFightFile(String teamA, String teamB, String playerA, String playerB, String creatureA, String creatureB){
        return teamA + ", " + teamB + "\n" + teamA + ", " + playerA + ", " + creatureA + "\n" + teamB + ", " + playerB + ", " + creatureB;
    }
}
