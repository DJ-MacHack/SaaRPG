package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.CommandFactory;
import de.unisaarland.sopra.messages.attack.*;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.MonsterType;

/**
 * Created by Team 14 on 12.09.2016.
 */
public class CommandFactoryImpl implements CommandFactory<Command> {

    private CommIds commIds;
    private int playerCount;

    public CommandFactoryImpl(CommIds commIds) {
        this.commIds = commIds;
        playerCount = 0;
    }

    @Override
    public Command createRegister(int commId, String name, MonsterType monsterType, String teamName) {
        CreatureType creatureType = CreatureType.valueOf(monsterType.name());
        Command cmd = new Register(commIds, commId, name, creatureType, teamName, playerCount, 0, 0);
        playerCount++;
        return cmd;
    }

    @Override
    public Command createWatch(int commId) {
        return new Watch(commIds, commId);
    }

    @Override
    public Command createMove(int commId, Direction direction) {
        int actorId = commIds.getMonsterId(commId);
        return new Move(commIds, actorId, direction);
    }

    @Override
    public Command createStab(int commId, Direction direction) {
        int actorId = commIds.getMonsterId(commId);
        return new Stab(commIds, actorId, direction);
    }

    @Override
    public Command createSlash(int commId, Direction direction) {
        int actorId = commIds.getMonsterId(commId);
        return new Slash(commIds, actorId, direction);
    }

    @Override
    public Command createCut(int commId, Direction direction) {
        int actorId = commIds.getMonsterId(commId);
        return new Cut(commIds, actorId, direction);
    }

    @Override
    public Command createBite(int commId, Direction direction) {
        int actorId = commIds.getMonsterId(commId);
        return new Bite(commIds, actorId, direction);
    }

    @Override
    public Command createStare(int commId, Direction direction) {
        int actorId = commIds.getMonsterId(commId);
        return new Stare(commIds, actorId, direction);
    }

    @Override
    public Command createClaw(int commId, Direction direction) {
        int actorId = commIds.getMonsterId(commId);
        return new Claw(commIds, actorId, direction);
    }

    @Override
    public Command createCrush(int commId, Direction direction) {
        int actorId = commIds.getMonsterId(commId);
        return new Crush(commIds, actorId, direction);
    }

    @Override
    public Command createSinge(int commId, Direction direction) {
        int actorId = commIds.getMonsterId(commId);
        return new Singe(commIds, actorId, direction);
    }

    @Override
    public Command createShoot(int commId, Direction direction) {
        int actorId = commIds.getMonsterId(commId);
        return new Shoot(commIds, actorId, direction);
    }

    @Override
    public Command createBurn(int commId) {
        int actorId = commIds.getMonsterId(commId);
        return new Burn(commIds, actorId);
    }

    @Override
    public Command createCast(int commId, int x, int y) {
        int actorId = commIds.getMonsterId(commId);
        return new Cast(commIds, actorId, x, y);
    }

    @Override
    public Command createBlink(int commId, int x, int y) {
        int actorId = commIds.getMonsterId(commId);
        return new Blink(commIds, actorId, x, y);
    }

    @Override
    public Command createSwap(int commId, int x, int y) {
        int actorId = commIds.getMonsterId(commId);
        return new Swap(commIds, actorId, x, y);
    }

    @Override
    public Command createWarCry(int commId, String warCry) {
        int actorId = commIds.getMonsterId(commId);
        return new WarCry(commIds, actorId, warCry);
    }

    @Override
    public Command createDoneActing(int commId) {
        int actorId = commIds.getMonsterId(commId);
        return new DoneActing(commIds, actorId);
    }

    public CommIds getCommIds(){
        return commIds;
    }

    public int getPlayerCount() {
        return this.playerCount;
    }
}
