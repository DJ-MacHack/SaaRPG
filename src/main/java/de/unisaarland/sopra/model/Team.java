package de.unisaarland.sopra.model;

import de.unisaarland.sopra.utility.GameVector;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created and implemented by Karla on 12.09.2016.
 */
public class Team {

    private String name;
    private Queue<GameVector> startPosition;

    /**
     * @param name
     */
    public Team(String name) {
        this.startPosition = new LinkedList<>();
        this.name = name;
    }

    /**
     * copy constructor of {@link Team}
     * @param team team to copy
     */
    public Team(Team team) {
        this.name = team.getName();
        this.startPosition = new LinkedList<>();
        for (GameVector v : team.startPosition) {
            this.startPosition.add(v);
        }
    }

    /**
     * Gets name of the Team
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets new name of the {@link Team}
     *
     * @param newName
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Adds the start position of the {@link Team}
     *
     * @param vec
     */
    public void addStartPosition(GameVector vec) {
        startPosition.add(vec);
    }

    /**
     * Gets the position of the next Player in line
     *
     * @return new position
     */
    public GameVector nextStartPosition() {
        return startPosition.poll();
    }

    /*
    public boolean equals(Object o) {
        if (!(o instanceof Team)) {
            return false;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Team team = (Team) o;

        if (!team.name.equals(name)) {
            return false;
        }
        if (!team.startPosition.peek().equals(startPosition.peek())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (this.name.hashCode() ^ this.startPosition.peek().hashCode());
    }
    */
}
