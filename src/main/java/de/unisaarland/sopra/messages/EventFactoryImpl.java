package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.EventFactory;
import de.unisaarland.sopra.messages.attack.*;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.MonsterType;

/**
 * Created by DJ MacHack on 12.09.2016.
 */
public class EventFactoryImpl implements EventFactory<Event> {

    public EventFactoryImpl()
    {}


    /**
     *
     * @param i
     * @return
     */
    @Override
    public Event createActNow(int i) {
        return new ActNow(i);
    }

    /**
     *
     * @param i
     * @param direction
     * @return
     */
    @Override
    public Event createBitten(int i, Direction direction) {
        return new Bite(null, i, direction);
    }


    /**
     *
     * @param i
     * @param direction
     * @return
     */
    @Override
    public Event createStared(int i, Direction direction) {
        return new Stare(null, i, direction);
    }

    /**
     *
     * @param i
     * @param i1
     * @param i2
     * @return
     */
    @Override
    public Event createBlinked(int i, int i1, int i2) {
        return new Blink(null, i, i1, i2);
    }

    /**
     *
     * @param i
     * @param i1
     * @param i2
     * @return
     */
    @Override
    public Event createSwapped(int i, int i1, int i2) {
        return new Swap(null, i, i1, i2);
    }

    /**
     *
     * @param i
     * @param s
     * @return
     */
    @Override
    public Event createWarCry(int i, String s) {
        return new WarCry(null, i, s);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Event createBoarAttack(int i, int i1) {
        return new BoarAttack(i, i1);
    }

    /**
     *
     * @param i
     * @param i1
     * @param i2
     * @return
     */
    @Override
    public Event createBoarSpawned(int i, int i1, int i2) {
        return new BoarSpawned(i, i1, i2);
    }

    /**
     *
     * @param i
     * @return
     */
    @Override
    public Event createBurned(int i) {
        return new Burn(null, i);
    }

    /**
     *
     * @param i
     * @param i1
     * @param i2
     * @return
     */
    @Override
    public Event createCast(int i, int i1, int i2) {
        return new Cast(null, i, i1, i2);
    }

    /**
     *
     * @param i
     * @param direction
     * @return
     */
    @Override
    public Event createClawed(int i, Direction direction) {
        return new Claw(null, i, direction);
    }

    /**
     *
     * @param i
     * @param direction
     * @return
     */
    @Override
    public Event createCrushed(int i, Direction direction) {
        return new Crush(null, i, direction);
    }

    /**
     *
     * @param i
     * @param direction
     * @return
     */
    @Override
    public Event createSinged(int i, Direction direction) {
        return new Singe(null, i, direction);
    }

    /**
     *
     * @param i
     * @param direction
     * @return
     */
    @Override
    public Event createShot(int i, Direction direction) {
        return new Shoot(null, i, direction);
    }

    /**
     *
     * @param i
     * @param direction
     * @return
     */
    @Override
    public Event createCut(int i, Direction direction) {
        return new Cut(null, i, direction);
    }

    /**
     *
     * @param i
     * @return
     */
    @Override
    public Event createDied(int i) {
        return new Died(i);
    }

    /**
     *
     * @param i
     * @return
     */
    @Override
    public Event createDoneActing(int i) {
        return new DoneActing(null, i);
    }

    /**
     *
     * @param i
     * @param i1
     * @param i2
     * @return
     */
    @Override
    public Event createFairySpawned(int i, int i1, int i2) {
        return new FairySpawned(i, i1, i2);
    }

    /**
     *
     * @param i
     * @param i1
     * @param i2
     * @return
     */
    @Override
    public Event createFieldEffect(int i, int i1, int i2) {
        return new FieldEffect(i,i1, i2);
    }

    /**
     *
     * @param s
     * @return
     */
    @Override
    public Event createFight(String s) {
        return new Fight(s);
    }

    /**
     *
     * @param i
     * @return
     */
    @Override
    public Event createRoundBegin(int i) {
        return new RoundBegin(i);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Event createRoundEnd(int i, int i1) {
        return new RoundEnd(i, i1);
    }

    /**
     *
     * @param s
     * @return
     */
    @Override
    public Event createWinner(String s) {
        return new Winner(s);
    }

    /**
     *
     * @param i
     * @param s
     * @return
     */
    @Override
    public Event createKicked(int i, String s) {
        return new Kicked(i, s);
    }

    /**
     *
     * @param s
     * @return
     */
    @Override
    public Event createMap(String s) {
        return new MapFile(s);
    }

    /**
     *
     * @param i
     * @param direction
     * @return
     */
    @Override
    public Event createMoved(int i, Direction direction) {
        return new Move(null, i, direction);
    }

    /**
     *
     * @param i
     * @param direction
     * @return
     */
    @Override
    public Event createStabbed(int i, Direction direction) {
        return new Stab(null, i, direction);
    }

    /**
     *
     * @param i
     * @param direction
     * @return
     */
    @Override
    public Event createSlashed(int i, Direction direction) {
        return new Slash(null, i, direction);
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Event createPoisonEffect(int i, int i1) {
        return new PoisonEffect(i, i1);
    }

    /**
     *
     * @param i
     * @param s
     * @param monsterType
     * @param s1
     * @param i1
     * @param i2
     * @param i3
     * @return
     */
    @Override
    public Event createRegistered(int i, String s, MonsterType monsterType, String s1, int i1, int i2, int i3) {
        CreatureType c = CreatureType.valueOf(monsterType.name());
        return new Register(null, i, s, c, s1, i1, i2, i3);
    }

    /**
     * 
     * @return
     */
    @Override
    public Event createRegistrationAborted() {
        return new RegistrationAborted();
    }
}
