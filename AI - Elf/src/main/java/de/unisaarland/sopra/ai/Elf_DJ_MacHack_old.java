package de.unisaarland.sopra.ai;

import de.unisaarland.sopra.Direction;
import de.unisaarland.sopra.KI;
import de.unisaarland.sopra.MonsterType;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.messages.attack.Move;
import de.unisaarland.sopra.messages.attack.Shoot;
import de.unisaarland.sopra.messages.attack.Stab;
import de.unisaarland.sopra.model.CreatureType;
import de.unisaarland.sopra.model.Field;
import de.unisaarland.sopra.model.Npc;
import de.unisaarland.sopra.model.Pc;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.utility.KiHelper;
import de.unisaarland.sopra.utility.Pathfinder;

import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Created by DJ MacHack on 26.09.2016.
 */
public class Elf_DJ_MacHack_old extends KI {

    private String name, team;
    private MonsterType monster;
    private int energy;
    private GameVector position;
    private Direction nextEnemy;
    private boolean cried;
    private Pc nextPc;
    //private GameVector killEnemy;
    private GameVector nearHeal;
    private Direction last;
    private Npc nearHealFairy;
    private Npc fairyAway;
    private Npc nextBoar;
    private int boring;
    private Cry cry;
    private boolean wasBoring = false;
    // private EventFactoryImpl = new EventFactoryImpl();
    //private boolean done = false;

    public Elf_DJ_MacHack_old(String name, int port, int timeout, String teamname, String host, CreatureType creaturetype) {
        super(name, port, timeout, teamname, host, creaturetype);
        this.name = name;
        this.team = teamname;
        this.monster = MonsterType.valueOf(creaturetype.toString());
        this.energy = 1000;
        Object[] key = game.getMap().getFields().keySet().toArray();
        Random r = new Random();
        this.cry = new Cry(r);
        this.cried = false;
    }

    public void run() {
        while (isRunning()) {
            Event event = this.clientConnection.nextEvent();
            //if (event.validateEvent(game, this)) {
            event.executeEvent(game, this);
            //LogString.printEventLog(event, this);
            //}
             /* else {
                if(!(event instanceof ActNow)) {
                    System.out.println("Error in event!");
                }
            }*/
        }
        clientConnection.close();
    }

    @Override
    public void actNow() {
        /*if (this.game.getCurrentMonsterIndex() != this.getOwnCreatureId()) {
            return;
        }*/
        this.boring = getGame().getBoringRounds();
        if (this.boring > 30) {
            this.wasBoring = true;
        }
        this.position = game.getMonsterById(this.getOwnCreatureId()).getPosition();
        //try {
        this.nextPc = KiHelper.nearestEnemy(getGame(), getGame().getMonsterById(getOwnCreatureId()), this.position, Integer.MAX_VALUE);
        if (this.nextPc == null) {
            done();
        }
        this.nextEnemy = findNextEnemy();
        this.nearHeal = KiHelper.nearestField(getGame(), getGame().getMonsterById(getOwnCreatureId()), this.position, Field.HEAL);
        this.nearHealFairy = findFairyToAnchor(this.nearHeal);
        this.fairyAway = findFairy();
        this.nextBoar = KiHelper.nearestNpc(getGame(), this.position, Integer.MAX_VALUE);
        nextAction();
            /*} catch(NullPointerException e) {
                done();
            }*/


    }

    private void move(Direction direction) {
        if (getGame().getFieldAt(nextVec(direction)) == Field.WATER) {
            this.energy -= 400;
            this.last = direction;
            super.clientConnection.sendMove(direction);
        } else {
            if (getGame().getFieldAt(nextVec(direction)) == Field.SWAMP) {
                this.energy -= 300;
                this.last = direction;
                super.clientConnection.sendMove(direction);
            } else {
                this.energy -= 100;
                super.clientConnection.sendMove(direction);
                this.last = direction;
            }
        }
    }

    private void register() {
        super.clientConnection.sendRegister(this.name, this.monster, this.team);
    }

    private void watch() {
        super.clientConnection.sendWatch();
    }

    private void stab(Direction direction) {
        if (getGame().getFieldAt(getGame().getCreatureById(getOwnCreatureId()).getPosition()) == Field.TREE) {
            this.energy -= 500;
            super.clientConnection.sendStab(direction);
        } else {
            if (getGame().getFieldAt(getGame().getCreatureById(getOwnCreatureId()).getPosition()) == Field.CURSE) {
                this.energy -= 750;
                super.clientConnection.sendStab(direction);
            } else {
                this.energy -= 250;
                super.clientConnection.sendStab(direction);
            }
        }
    }

    private void cry() {
        this.cried = true;
        String cry = this.cry.getCry();
        super.clientConnection.sendWarCry(cry);
    }

    private void shoot(Direction direction) {
        if (getGame().getFieldAt(getGame().getCreatureById(getOwnCreatureId()).getPosition()) == Field.TREE) {
            this.energy -= 900;
            super.clientConnection.sendShoot(direction);
        } else {
            if (getGame().getFieldAt(getGame().getCreatureById(getOwnCreatureId()).getPosition()) == Field.CURSE) {
                done();
            } else {
                this.energy -= 450;
                super.clientConnection.sendShoot(direction);
            }
        }

    }

    private void done() {
        this.energy = 1000;
        this.cried = false;
        //if(!this.cried){
        // cry();
        //}
        super.clientConnection.sendDoneActing();
    }

    private Direction findNextEnemy() {
        if (this.nextPc == null) {
            return null;
        }

        return toDirection(this.nextPc.getPosition());
    }

    private Direction toDirection(GameVector vector) {
        if (vector == null) {
            return null;
        }
        int xthis = this.position.getX();
        int ythis = this.position.getY();
        int xvec = vector.getX();
        int yvec = vector.getY();

        if (ythis == yvec) {
            if (xthis > xvec) {
                return Direction.WEST;
            } else {
                return Direction.EAST;
            }
        }
        if (xthis == xvec) {
            if (ythis > yvec) {
                return Direction.NORTH_WEST;
            } else {
                return Direction.SOUTH_EAST;
            }
        }
        if (xthis > xvec && ythis < yvec) {
            return Direction.SOUTH_WEST;
        }
        if (xthis < xvec && ythis > yvec) {
            return Direction.NORTH_EAST;
        } else {
            System.out.print("Error! \n");
            return null;
        }
    }

    private Direction oppositeDirection(Direction direction) {
        if (direction == null) {
            return null;
        }
        if (direction.equals(Direction.SOUTH_EAST)) {
            return Direction.NORTH_WEST;
        }
        if (direction.equals(Direction.SOUTH_WEST)) {
            return Direction.NORTH_EAST;
        }
        if (direction.equals(Direction.NORTH_EAST)) {
            return Direction.SOUTH_WEST;
        }
        if (direction.equals(Direction.NORTH_WEST)) {
            return Direction.SOUTH_EAST;
        }
        if (direction.equals(Direction.EAST)) {
            return Direction.WEST;
        }
        if (direction.equals(Direction.WEST)) {
            return Direction.EAST;
        } else {
            return null;
        }
    }

    private GameVector nextVec(Direction direction) {
        if (direction == Direction.SOUTH_WEST) {
            return new GameVector(this.position.getX() - 1, this.position.getY() + 1);
        }
        if (direction == Direction.SOUTH_EAST) {
            return new GameVector(this.position.getX(), this.position.getY() + 1);
        }
        if (direction == Direction.NORTH_WEST) {
            return new GameVector(this.position.getX(), this.position.getY() - 1);
        }
        if (direction == Direction.NORTH_EAST) {
            return new GameVector(this.position.getX() + 1, this.position.getY() - 1);
        }
        if (direction == Direction.WEST) {
            return new GameVector(this.position.getX() - 1, this.position.getY());
        }
        if (direction == Direction.EAST) {
            return new GameVector(this.position.getX() + 1, this.position.getY());
        } else {
            return null;
        }
    }

    private void nextAction() {
        boolean action = false;
        if (!this.cried) {
            action = true;
            cry();
        }
        if (this.energy < 100) {
            action = true;
            done();
        }
        if (this.boring > 74 && !action && this.nextEnemy == null) {
            if (this.nextBoar != null) {
                Queue<Direction> pathF = null;
                GameVector fairy = this.nextBoar.getPosition();
                pathF = Pathfinder.findPath(this.position, fairy, getGame(), CreatureType.ELF);
                Direction goF;
                if (pathF == null || pathF.isEmpty()) {
                    goF = null;
                } else {
                    goF = (Direction) pathF.toArray()[0];
                }
                if (!action && goF != null) {
                    Move move = new Move(null, getOwnCreatureId(), goF);
                    if (move.validateEvent(getGame(), null) && this.energy > 99) {
                        action = true;
                        move(goF);
                    }
                } else {
                    if (this.nextBoar != null && !action && this.nextBoar.getPosition().distanceTo(this.position) == 1 && getGame().getFieldAt(this.position) != Field.WATER) {
                        Stab stab = new Stab(null, getOwnCreatureId(), toDirection(this.nextBoar.getPosition()));
                        if (!action && this.nextBoar != null) {
                            if (stab.validateEvent(getGame(), null) && this.energy > 249) {
                                action = true;
                                stab(toDirection(this.nextBoar.getPosition()));
                            } else {
                                action = true;
                                done();
                            }
                        }
                    }
                    if (this.nextBoar != null && !action) {
                        Shoot shoot = new Shoot(null, getOwnCreatureId(), toDirection(this.nextBoar.getPosition()));
                        if (shoot.validateEvent(getGame(), null) && this.energy > 419 && getGame().getFieldAt(this.position) != Field.WATER) {
                            action = true;
                            shoot(toDirection(this.nextBoar.getPosition()));
                        }

                    }
                }
            }
        }
        if (getGame().getFieldAt(this.position) == Field.HEAL && this.boring < 70 && (!this.wasBoring || getGame().getCreatureByPosition(this.position).getHp() > 90)) {
            if (this.nextEnemy != null && !action && this.nextPc.getPosition().distanceTo(this.position) == 1) {
                Stab stab = new Stab(null, getOwnCreatureId(), this.nextEnemy);
                if (!action && this.nextEnemy != null) {
                    if (stab.validateEvent(getGame(), null) && this.energy > 249) {
                        action = true;
                        stab(this.nextEnemy);
                    }
                }
            }
            if (this.nextEnemy != null && !action) {
                Shoot shoot = new Shoot(null, getOwnCreatureId(), this.nextEnemy);
                if (shoot.validateEvent(getGame(), null) && this.energy > 419) {
                    action = true;
                    shoot(this.nextEnemy);
                } else {
                    Stab stab = new Stab(null, getOwnCreatureId(), this.nextEnemy);
                    if (!action && this.nextEnemy != null) {
                        if (stab.validateEvent(getGame(), null) && this.energy > 249) {
                            action = true;
                            stab(this.nextEnemy);
                        }
                    }
                }
            }
            if (!action) {
                action = true;
                done();
            }
        }
        if (getGame().getFieldAt(this.position) != Field.HEAL && this.nearHeal == null && this.boring < 70 && this.fairyAway != null) { //&& (!this.wasBoring || getGame().getCreatureByPosition(this.position).getHp() < 46)) {
            Queue<Direction> path = Pathfinder.findPath(this.position, this.fairyAway.getPosition(), getGame(), CreatureType.ELF, true);
            Direction go;
            if (path != null && !path.isEmpty()) {
                go = (Direction) path.toArray()[0];
            } else {
                go = null;
            }
            if (!action && go != null) {
                Move move = new Move(null, getOwnCreatureId(), go);
                if (move.validateEvent(getGame(), null) && this.energy > 99) {
                    action = true;
                    move(go);
                } else {
                    action = true;
                    done();
                }
            } else {
                if (!action) {
                    Queue<Direction> pathF = null;
                    if (this.fairyAway != null) {
                        GameVector fairy = this.fairyAway.getPosition();
                        pathF = Pathfinder.findPath(this.position, fairy, getGame(), CreatureType.ELF);
                    }
                    Direction goF;
                    if (pathF == null || pathF.isEmpty()) {
                        goF = null;
                    } else {
                        goF = (Direction) pathF.toArray()[0];
                    }
                    if (!action && goF != null) {
                        Move move = new Move(null, getOwnCreatureId(), goF);
                        if (move.validateEvent(getGame(), null) && this.energy > 99) {
                            action = true;
                            move(goF);
                        }
                    }

                    if (this.fairyAway != null && !action && this.fairyAway.getPosition().distanceTo(this.position) == 1 && getGame().getFieldAt(this.position) != Field.WATER) {
                        Stab stab = new Stab(null, getOwnCreatureId(), toDirection(this.fairyAway.getPosition()));
                        if (!action && this.fairyAway != null) {
                            if (stab.validateEvent(getGame(), null) && this.energy > 249) {
                                action = true;
                                stab(toDirection(this.fairyAway.getPosition()));
                            } else {
                                action = true;
                                done();
                            }
                        }
                    }
                    if (this.fairyAway != null && !action) {
                        Shoot shoot = new Shoot(null, getOwnCreatureId(), toDirection(this.fairyAway.getPosition()));
                        if (shoot.validateEvent(getGame(), null) && this.energy > 419 && getGame().getFieldAt(this.position) != Field.WATER) {
                            action = true;
                            shoot(toDirection(this.fairyAway.getPosition()));
                        }

                    }
                }
            }
        }
        if (getGame().getFieldAt(this.position) != Field.HEAL && this.nearHeal != null && this.boring < 70 && (!this.wasBoring || getGame().getCreatureByPosition(this.position).getHp() < 46)) {
            Queue<Direction> path = Pathfinder.findPath(this.position, this.nearHeal, getGame(), CreatureType.ELF, true);
            Direction go;
            if (path != null && !path.isEmpty()) {
                go = (Direction) path.toArray()[0];
            } else {
                go = null;
            }
            if (!action && go != null) {
                Move move = new Move(null, getOwnCreatureId(), go);
                if (move.validateEvent(getGame(), null) && this.energy > 99) {
                    action = true;
                    move(go);
                } else {
                    action = true;
                    done();
                }
            } else {
                if (!action) {
                    Queue<Direction> pathF = null;
                    if (this.nearHealFairy != null) {
                        GameVector fairy = this.nearHealFairy.getPosition();
                        pathF = Pathfinder.findPath(this.position, fairy, getGame(), CreatureType.ELF);
                    }
                    Direction goF;
                    if (pathF == null || pathF.isEmpty()) {
                        goF = null;
                    } else {
                        goF = (Direction) pathF.toArray()[0];
                    }
                    if (!action && goF != null) {
                        Move move = new Move(null, getOwnCreatureId(), goF);
                        if (move.validateEvent(getGame(), null) && this.energy > 99) {
                            action = true;
                            move(goF);
                        }
                    }

                    if (this.nearHealFairy != null && !action && this.nearHealFairy.getPosition().distanceTo(this.position) == 1 && getGame().getFieldAt(this.position) != Field.WATER) {
                        Stab stab = new Stab(null, getOwnCreatureId(), toDirection(this.nearHealFairy.getPosition()));
                        if (!action && this.nearHealFairy != null) {
                            if (stab.validateEvent(getGame(), null) && this.energy > 249) {
                                action = true;
                                stab(toDirection(this.nearHealFairy.getPosition()));
                            } else {
                                action = true;
                                done();
                            }
                        }
                    }
                    if (this.nearHealFairy != null && !action) {
                        Shoot shoot = new Shoot(null, getOwnCreatureId(), toDirection(this.nearHealFairy.getPosition()));
                        if (shoot.validateEvent(getGame(), null) && this.energy > 419 && getGame().getFieldAt(this.position) != Field.WATER) {
                            action = true;
                            shoot(toDirection(this.nearHealFairy.getPosition()));
                        }

                    } else {
                        Queue<Direction> pathE = Pathfinder.findPath(this.position, this.nextPc.getPosition(), getGame(), CreatureType.ELF);
                        Direction goE;
                        if (pathE == null || pathE.isEmpty()) {
                            goE = null;
                        } else {
                            goE = (Direction) pathE.toArray()[0];
                        }
                        if (!action && goE != null) {
                            Move move = new Move(null, getOwnCreatureId(), goE);
                            if (move.validateEvent(getGame(), null) && this.energy > 99) {
                                action = true;
                                move(goE);
                            }
                        } else {
                            if (this.nextEnemy != null && !action && this.nextPc.getPosition().distanceTo(this.position) == 1 && getGame().getFieldAt(this.position) != Field.WATER) {
                                Stab stab = new Stab(null, getOwnCreatureId(), this.nextEnemy);
                                if (!action && this.nextEnemy != null) {
                                    if (stab.validateEvent(getGame(), null) && this.energy > 249) {
                                        action = true;
                                        stab(this.nextEnemy);
                                    } else {
                                        action = true;
                                        done();
                                    }
                                }
                            }
                            if (this.nextEnemy != null && !action) {
                                Shoot shoot = new Shoot(null, getOwnCreatureId(), this.nextEnemy);
                                if (shoot.validateEvent(getGame(), null) && this.energy > 419 && getGame().getFieldAt(this.position) != Field.WATER) {
                                    action = true;
                                    shoot(this.nextEnemy);
                                } else {
                                    action = true;
                                    done();
                                }
                            }
                        }
                    }
                }
            }
           /* if (!action) {
                action = true;
                done();
            }*/
        } else {
            if (!action && (this.nearHeal == null || this.boring > 69)) {
                if (this.nextEnemy != null && !action && getGame().getFieldAt(this.position) != Field.WATER) {
                    Shoot shoot = new Shoot(null, getOwnCreatureId(), this.nextEnemy);
                    if (shoot.validateEvent(getGame(), null) && this.energy > 419 && this.nextPc.getPosition().distanceTo(this.position) > 1) {
                        action = true;
                        shoot(this.nextEnemy);
                    } else {
                        if (this.nextEnemy != null && !action && this.nextPc.getPosition().distanceTo(this.position) == 1) {
                            Stab stab = new Stab(null, getOwnCreatureId(), this.nextEnemy);
                            if (!action && this.nextEnemy != null) {
                                if (stab.validateEvent(getGame(), null) && this.energy > 249) {
                                    action = true;
                                    stab(this.nextEnemy);
                                } /*else {
                                    Queue<Direction> pathE1 = Pathfinder.findPath(this.position, this.nextPc.getPosition(), getGame(), CreatureType.ELF);
                                    Direction goE1;
                                    if (pathE1.isEmpty()) {
                                        goE1 = null;
                                    } else {
                                        goE1 = (Direction) pathE1.toArray()[0];
                                    }
                                    if (!action && goE1 != null) {
                                        Move move = new Move(null, getOwnCreatureId(), goE1);
                                        if (move.validateEvent(getGame(), null) && this.energy > 99) {
                                            action = true;
                                            move(goE1);
                                        }
                                    }
                                }*/
                            }
                        }
                    }
                }


            }
        }
        /*if (!action && (this.nextPc != null && this.boring > 70)) {
            if (this.nextPc == null) {
                action = true;
                done();
            } else {*/
        if (!action && this.nextPc != null){


          /*  if (this.nextEnemy != null && !action && this.nextPc.getPosition().distanceTo(this.position) == 1) {
                Stab stab = new Stab(null, getOwnCreatureId(), this.nextEnemy);
                if (!action && this.nextEnemy != null) {
                    if (stab.validateEvent(getGame(), null) && this.energy > 249) {
                        action = true;
                        stab(this.nextEnemy);
                    }
                }
            }
            if (this.nextEnemy != null && !action) {
                Shoot shoot = new Shoot(null, getOwnCreatureId(), this.nextEnemy);
                if (shoot.validateEvent(getGame(), null) && this.energy > 419) {
                    action = true;
                    shoot(this.nextEnemy);
                }
            }
            if (!action && this.nextEnemy != null) {*/
                Queue<Direction> pathE1 = Pathfinder.findPath(this.position, this.nextPc.getPosition(), getGame(), CreatureType.ELF);
                Direction goE1;
                if (pathE1 == null || pathE1.isEmpty()) {
                    goE1 = null;
                } else {
                    goE1 = (Direction) pathE1.toArray()[0];
                }
                if (this.last == oppositeDirection(goE1)) {
                    goE1 = (Direction) pathE1.toArray()[1];
                }
                if (!action && goE1 != null) {
                    Move move = new Move(null, getOwnCreatureId(), goE1);
                    if (move.validateEvent(getGame(), null) && this.energy > 99) {
                        action = true;
                        move(goE1);
                    }
                }
            }
       // }
        // }
        if (!action) {
            action = true;
            done();
        }
    }


    private GameVector toVector(GameVector vector, Direction direction) {
        GameVector res = vector;

        if (direction == Direction.WEST) {
            return res.add(new GameVector(-1, 0));
        }
        if (direction == Direction.EAST) {
            return res.add(new GameVector(1, 0));
        }
        if (direction == Direction.NORTH_EAST) {
            return res.add(new GameVector(1, -1));
        }
        if (direction == Direction.NORTH_WEST) {
            return res.add(new GameVector(0, -1));
        }
        if (direction == Direction.SOUTH_EAST) {
            return res.add(new GameVector(0, 1));
        }
        if (direction == Direction.SOUTH_WEST) {
            return res.add(new GameVector(-1, 1));
        }
        return null;
    }

    private boolean runAway() {
        if (this.last == null) {
            System.out.print("Passiert\n");
        } else {
            if (this.energy > 100) {
                Move move = new Move(null, getOwnCreatureId(), oppositeDirection(nextEnemy));
                if (move.validateEvent(getGame(), null)) {
                    move(oppositeDirection(nextEnemy));
                    return true;
                } else {
                    Random r = new Random();
                    Direction[] dir = Direction.values();
                    int i = r.nextInt(dir.length);
                    move = new Move(null, getOwnCreatureId(), dir[i]);
                    boolean val = move.validateEvent(getGame(), null);
                    if (val) {
                        move(dir[i]);
                        return true;
                    } else {
                        runAway();
                    }
                }
            }

        }
        return false;
    }

    private Npc findFairyToAnchor(GameVector vector) {

        Npc fairy = null;

        List<Npc> creatures = getGame().getNpcs();
        for (Npc creature : creatures) {
            if (creature.getCreatureType() == CreatureType.FAIRY) {
                if (creature.getAnchorPoint().equals(vector)) {
                    fairy = creature;
                }

            }
        }
        return fairy;
    }

    private boolean enemiesDead() {
        if (this.nextEnemy == null) {
            return true;
        } else {
            return false;
        }
    }

    private Npc findFairy() {
        Npc fairy = null;

        List<Npc> creatures = getGame().getNpcs();
        for (Npc creature : creatures) {
            if (creature.getCreatureType() == CreatureType.FAIRY) {
                fairy = creature;
            }

        }
        return fairy;
    }

}
