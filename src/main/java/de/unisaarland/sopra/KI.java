package de.unisaarland.sopra;

import de.unisaarland.sopra.model.CreatureType;

/**
 * Created by Wiebk on 12.09.2016.
 */
public abstract class KI extends Client {

    private String name;
    private String teamName;

    public KI(String name, int port, int timeout, String teamname, String host, CreatureType creaturetype){
        super(host, port, timeout);
        super.clientConnection.sendRegister(name, MonsterType.valueOf(creaturetype.name()), teamname);
        this.name = name;
        this.teamName = teamname;
    }

    public abstract void actNow();

    public String getName() {
        return this.name;
    }

    public String getTeamName() {
        return this.teamName;
    }
}
