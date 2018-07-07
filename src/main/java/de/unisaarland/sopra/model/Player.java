package de.unisaarland.sopra.model;


/**
 * Created and implemented by Karla on 12.09.2016.
 */
public class Player {

    private String name, teamname;
    private int monsterId;
    private boolean registered;
    private CreatureType monstertype;

    /**
     * @param name
     * @param TeamName
     * @param MonsterType
     */
    public Player(String name, String TeamName, CreatureType MonsterType) {
        this.name = name;
        this.teamname = TeamName;
        this.monstertype = MonsterType;
    }

    /**
     * copy constructor of {@link Player}
     * @param player {@link Player} instance to copy values from
     */
    public Player(Player player) {
        this.name = player.getName();
        this.teamname = player.getTeamName();
        this.monsterId = player.getId();
        this.registered = player.getRegistered();
        this.monstertype = player.getMonsterType();
    }

    /**
     * Gets Name of {@link Player}
     *
     * @return name of the Player
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets team name of the {@link Player}
     *
     * @return TeamnName
     */
    public String getTeamName() {
        return this.teamname;
    }

    /**
     * Gets type of monster of the {@link Player}
     *
     * @return MonsterType
     */
    public CreatureType getMonsterType() {
        return this.monstertype;
    }

    /**
     * Gets Id of the {@link Player}
     *
     * @return monsterId
     */
    public int getId() {
        return this.monsterId;
    }

    /**
     * Changes the Id of the {@link Player}
     *
     * @param id
     */
    public void setId(int id) {
        this.monsterId = id;
    }

    /**
     * Gets the Registration Status of the {@link Player}
     *
     * @return true, if the {@link Player} is already registered.
     */
    public boolean getRegistered() {
        return this.registered;
    }

    /**
     * Sets the Registration status of a {@link Player}
     *
     * @param register
     */
    public void setRegistered(boolean register) {
        this.registered = register;
    }

    /**
     * Changes the Registration status of a {@link Player}
     *
     * @param pcId, ID of the {@link Player}
     */

    public void register(int pcId) {
        setId(pcId);
        if (!getRegistered())
            setRegistered(true);
    }

    /*
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Player)) {
            return false;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Player p = (Player) o;

        if (!p.getName().equals(name) || !p.getTeamName().equals(teamname) || !p.getMonsterType().equals(monstertype)) {
            return false;
        }

        if (!(p.getRegistered() == registered)) {
            return false;
        }

        return p.getId() == monsterId;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        final int MAGICPRIM = 31;

        result = MAGICPRIM * result + (teamname != null ? teamname.hashCode() : 0);
        result = MAGICPRIM * result + monsterId;
        result = MAGICPRIM * result + (registered ? 1 : 0);
        result = MAGICPRIM * result + (monstertype != null ? monstertype.hashCode() : 0);
        return result;
    }
    */
}

