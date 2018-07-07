package de.unisaarland.sopra.model;

import de.unisaarland.sopra.MonsterType;
import de.unisaarland.sopra.utility.GameVector;
import de.unisaarland.sopra.utility.InvalidFightFileException;
import de.unisaarland.sopra.utility.InvalidMapFileException;
import java.util.*;

/**
 * Created by Team 14 on 12.09.2016.
 * Implemented by Wiebk and Lukas Kirschner
 */

public class Game {

    private Map map;
    private List<Player> players;
    private List<Team> teams;
    private HashMap<Integer, Npc> npcs;
    private HashMap<Integer, Pc> monsters;
    private RoundState roundstate;
    private int boringRounds;
    private int roundCounter;
    private List<Integer> initorder;
    private int currentMonsterIndex;
    private String fightFile;
    private String mapFile;
    private int mapHeight, mapWidth;

    public Game() {
        this.map = new Map();
        this.players = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.npcs = new HashMap<>();
        this.monsters = new HashMap<>();
        this.roundstate = RoundState.REGISTER;
        this.boringRounds = 0;
        this.roundCounter = 0;
        this.initorder = new ArrayList<>();
        this.currentMonsterIndex = 0;
        this.mapHeight = 0;
        this.mapWidth = 0;
   }

    public Game(Game gameToCopy) {
        // copy map
        this.map = new Map(gameToCopy.map);

        // copy all players
        this.players = new ArrayList<>();
        for (Player player : gameToCopy.getPlayers()) {
            this.players.add(new Player(player));
        }

        // copy all teams
        this.teams = new ArrayList<>();
        for (Team team : gameToCopy.getTeams()) {
            this.teams.add(team);
        }

        // copy all npcs
        this.npcs = new HashMap<>();
        Collection<Npc> npcs = gameToCopy.npcs.values();
        for (Npc t : npcs) {
            this.npcs.put(t.getId(), new Npc(t));
        }

        // copy all monsters
        this.monsters = new HashMap<>();
        Collection<Pc> pcs = gameToCopy.monsters.values();
        for (Pc t : pcs) {
            this.monsters.put(t.getId(), new Pc(t));
        }

        this.roundstate = gameToCopy.roundstate;
        this.boringRounds = gameToCopy.boringRounds;
        this.roundCounter = gameToCopy.roundCounter;
        this.initorder = new ArrayList<>(gameToCopy.initorder);
        this.currentMonsterIndex = gameToCopy.currentMonsterIndex;
        this.fightFile = gameToCopy.fightFile;
        this.mapFile = gameToCopy.mapFile;
    }

    public Map getMap() {
        return map;
    }

    public Field getFieldAt(GameVector v) {
        return map.getFieldAt(v);
    }

    public void setFieldAt(GameVector v, Field f) {
        map.setFieldAt(v, f);
    }

    public List<GameVector> getFairySpawns() {
        return map.getFairySpawns();
    }

    public List<GameVector> getBoarSpawns() {
        return map.getBoarSpawns();
    }

    public Player getPlayerByName(String name) {
        //if(players.contains(name)){
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(name)) {
                return players.get(i);
            }
        }
        //}
        return null;
    }

    public Player getPlayerByMonsterId(int monster_id) {
        //if(players.contains(monster_id)) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId() == monster_id) {
                return players.get(i);
            }
        }
        //}
        return null;
    }

    public List<Player> getPlayers() {
        return players;
    }

    /*
    private void removePlayer(Player player) {
        players.remove(player);
    }
    */

    public void addTeam(Team team) {
        teams.add(team);
    }

    public Team getTeam(String teamName) {
        for (Team team : teams) {
            if (team.getName().equals(teamName)) {
                return team;
            }
        }

        return null;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void removeNpc(Npc npc) {
        if (npc.getCreatureType() == CreatureType.FAIRY) {
            map.setFieldAt(npc.getAnchorPoint(), Field.HEAL_DISABLED);
        }

        npcs.remove(npc.getId()); //Feel the magic happen
    }

    public Npc getNpcById(int id) {
        Npc npc = npcs.get(id);

        return npc;
    }

    public void setMapFile(String mapFile) {
        this.mapFile = mapFile;
    }

    public Npc getNpcByPosition(GameVector v) {
        Npc j = null;
        for (Npc i : npcs.values()) {
            if (i.getPosition().equals(v)) {
                if (i.isDead()) {
                    continue;
                }
                j = i;
                break;
            }
        }
        return j;

    }

    public List<Npc> getNpcs() {
        List<Npc> allNpcs = new ArrayList<Npc>();
        allNpcs.addAll(npcs.values());
        return allNpcs;
    }

    public void addNpc(Npc npc) {
        npcs.put(npc.getId(), npc);
    }

    public void removeMonster(Pc pc) {
        if (monsters.containsKey(pc.getId())) {
            monsters.remove(pc.getId());

            for (int i = 0; i < players.size(); i++) {
                if (pc.getId() == players.get(i).getId()) {
                    players.remove(i);
                    break;
                }
            }

            for (int i = 0; i < initorder.size(); i++) {
                if (pc.getId() == initorder.get(i)) {
                    if (currentMonsterIndex > i) {
                        currentMonsterIndex--;
                    }
                    initorder.remove(i);
                    break;
                }
            }
        }
    }


    public Pc getMonsterById(int id) {
        if(monsters.containsKey(id)){
            return monsters.get(id);
        }

        return null;
    }

    public Pc getMonsterByPosition(GameVector v) {

        for (Pc me : monsters.values()) {
            if (me.getPosition().equals(v)) {
                return me;
            }
        }
        return null;
    }

    public List<Pc> getMonsters() {
        List<Pc> allPcs = new ArrayList<>();
        allPcs.addAll(monsters.values());
        return allPcs;
    }

    public void addMonster(Pc pc) {
        monsters.put(pc.getId(), pc);
    }

    public RoundState getRoundState() {
        return roundstate;
    }

    public void setRoundState(RoundState roundstate) {
        this.roundstate = roundstate;

    }

    public int getBoringRounds() {
        return boringRounds;
    }

    public void setBoringRounds(int rounds) {
        boringRounds = rounds;
    }

    public int getRoundCounter() {
        return roundCounter;
    }

    public void increaseRoundCounter() {
        roundCounter++;
    }

    public void increaseBoringRounds() {
        boringRounds++;
    }

    public void removeCreature(Creature creature) {
        if (creature instanceof Pc) {
            this.removeMonster((Pc) creature);
        } else if (creature instanceof Npc) {
            this.removeNpc((Npc) creature);
        }

        /*
        if(creature instanceof Pc && monsters.containsValue(creature)){
            monsters.remove(creature);
        }
        if(creature instanceof Npc && monsters.containsValue(creature)){
            npcs.remove(creature);
        }*/
    }

    public List<Integer> getInitOrder() {
        return initorder;
    }

    public void setInitOrder(List<Integer> initorder) {
        this.initorder = initorder;
    }

    public int getCurrentMonsterIndex() {
        return currentMonsterIndex;
    }

    public void setCurrentMonsterIndex(int monsterindex) {
        currentMonsterIndex = monsterindex;
    }

    public int getMonsterActing() {
        return initorder.get(currentMonsterIndex);
    }

    public void nextMonster() {
        currentMonsterIndex++;
    }

    public Creature getCreatureById(int creatureId) {

        Creature tmp = this.getMonsterById(creatureId);
        if (tmp == null) {
            tmp = this.getNpcById(creatureId);
        }
        return tmp;

        /*List<Creature> allcreatures = getCreatures();
        for(int i = 0; i < allcreatures.size(); i++){
            if(allcreatures.get(i).getId() == creatureid){
                return allcreatures.get(i);
            }
        }
        return null;*/
    }

    public Creature getCreatureByPosition(GameVector v) {

        Creature tmp = this.getMonsterByPosition(v);
        if (tmp == null) {
            tmp = getNpcByPosition(v);
        }

        return tmp;

        /*List<Creature> allcreatures = getCreatures();
        for(int i = 0; i < allcreatures.size(); i++){
            if(allcreatures.get(i).getPosition() == v){
                return allcreatures.get(i);
            }
        }
        return null;*/

    }

    public List<Creature> getCreatures() {
        List<Creature> allCreatures = new ArrayList<Creature>();
        allCreatures.addAll(npcs.values());
        allCreatures.addAll(monsters.values());
        return allCreatures;
    }

    public void handleFightfile(String fightFile) throws InvalidFightFileException {
        this.fightFile = fightFile;
        String[] splittedFightFile = fightFile.split("\n");
        HashMap<String, Boolean> playerRegistered = new HashMap<>();

        // Teams
        String[] teamnames = splittedFightFile[0].split(",");
        if (teamnames.length > 10 || teamnames.length < 2) {
            throw new InvalidFightFileException("Wrong number of teams!");
        }

        for (int i = 0; i < teamnames.length; i++) {
            String teamName = teamnames[i].trim();
            if (getTeam(teamName) != null) {
                throw new InvalidFightFileException("Double registration of teams!");
            }
            Team newTeam = new Team(teamName);
            teams.add(newTeam);
            playerRegistered.put(teamName, false);
        }

        // Gladiators
        List<String> playerNames = new ArrayList<>();
        for (int i = 1; i < splittedFightFile.length; i++) {
            String[] playerAttributes = splittedFightFile[i].split(",");

            if (playerAttributes.length <= 1 && playerAttributes[0].trim().isEmpty()) {
                continue;
            }

            if (playerAttributes.length != 3) {
                throw new InvalidFightFileException("Wrong number of arguments for player!");
            }

            String playerTeam = playerAttributes[0].trim();
            String playerName = playerAttributes[1].trim();
            String playerCreatureType = playerAttributes[2].trim();
            if (playerTeam.isEmpty() || playerName.isEmpty() || playerCreatureType.isEmpty()) {
                throw new InvalidFightFileException("No name, team or creatureType!");
            }

            if (playerNames.contains(playerName)) {
                throw new InvalidFightFileException("Double registration of player!");
            }

            if (getTeam(playerTeam) == null) {
                throw new InvalidFightFileException("Wrong Team name!");
            }

            CreatureType creatureType;

            try {
                MonsterType monsterType = MonsterType.valueOf(playerCreatureType.toUpperCase());
                creatureType = CreatureType.valueOf(monsterType.name());
            } catch (IllegalArgumentException exception) {
                throw new InvalidFightFileException("Unknown MonsterType!", exception);
            }

            if (creatureType == CreatureType.BOAR || creatureType == CreatureType.FAIRY) {
                throw new InvalidFightFileException("Not allowed to spawn as boar or fairy!");
            }

            Player player = new Player(playerName, playerTeam, creatureType);
            players.add(player);
            playerRegistered.put(playerTeam, true);
            playerNames.add(playerName);
        }

        for (boolean hasPlayer : playerRegistered.values()) {
            if (!hasPlayer) {
                throw new InvalidFightFileException("One team has no players!");
            }
        }
    }

    public void handleMapfile(String mapFile) throws InvalidMapFileException {
        this.mapFile = mapFile;
        char[] mapString = mapFile.toCharArray();
        int x = 0;
        int y = 0;
        int widthCount = 0;
        int maxWidth = -1;

        HashMap<String, Integer> teamSpawns = new HashMap<>();
        boolean multiLine = false;

        for (int cnt = 0; cnt < mapFile.length(); cnt++) {
            if (mapString[cnt] == '\n') {
                if (cnt == mapFile.length() - 1) {
                    throw new InvalidMapFileException("Wrong number of chars!");
                }

                if (maxWidth < 0) {
                    maxWidth = widthCount;
                }
                multiLine = true;

                if (maxWidth != widthCount) {
                    throw new InvalidMapFileException("Wrong number of chars!");
                }

                y++;
                x = -(y / 2);
                widthCount = 0;
                continue;
            }

            GameVector pos = new GameVector(x, y);

            if (mapString[cnt] == Character.LINE_SEPARATOR) {
                continue;
            }

            Field f = Map.charToField(mapString[cnt]);

            this.map.setFieldAt(pos, f);
            if (Character.isDigit(mapString[cnt])) {
                int teamId = Character.getNumericValue(mapString[cnt]);
                if (teamId < 0 || teamId > 9) {
                    throw new InvalidMapFileException("Team id was too high or to low(<0)!");
                }

                if (teamId < teams.size()) {
                    Team team = teams.get(teamId);
                    team.addStartPosition(pos);

                    int spawns = 0;
                    if (teamSpawns.containsKey(team.getName())) {
                        spawns = teamSpawns.get(team.getName());
                    }

                    spawns++;
                    teamSpawns.put(team.getName(), spawns);
                }
            } else {
                if (f == Field.HEAL || f == Field.HEAL_DISABLED) {
                    map.addFairySpawn(pos);
                } else {
                    if (mapString[cnt] == 'X' || mapString[cnt] == 'A' || mapString[cnt] == 'T' || mapString[cnt] == '&' || mapString[cnt] == '.') {
                        map.addBoarSpawn(pos);
                    }
                }
            }

            x++;
            widthCount++;
        }

        if (!multiLine) {
            maxWidth = widthCount;
        }

        this.mapWidth = maxWidth;       // mapWidth hier setzen, da hier die Breite(maxWidth) auch für ein zeilige Maps gesetzt wurde
        this.mapHeight = y + 1;         // y bezeichnet den Index der letzten Zeile (bei einer Höhe von 8 also 7), d.h. man muss +1 rechnen

        if (maxWidth != widthCount) {
            throw new InvalidMapFileException("Wrong number of chars!");
        }

        for (int wX = -2; wX < maxWidth + 2; wX++) {
            int yOff = (y / 2);
            this.map.setFieldAt(new GameVector(wX, -2), Field.ROCK);
            this.map.setFieldAt(new GameVector(wX, -1), Field.ROCK);

            this.map.setFieldAt(new GameVector(wX - yOff, y + 1), Field.ROCK);
            this.map.setFieldAt(new GameVector(wX - yOff, y + 2), Field.ROCK);
        }

        for (int wY = -2; wY < y + 2; wY++) {
            int yOff = wY / 2;
            this.map.setFieldAt(new GameVector(-yOff - 2, wY), Field.ROCK);
            this.map.setFieldAt(new GameVector(-yOff - 1, wY), Field.ROCK);

            this.map.setFieldAt(new GameVector(maxWidth - yOff, wY), Field.ROCK);
            this.map.setFieldAt(new GameVector(maxWidth - yOff + 1, wY), Field.ROCK);
        }

        for (Player player : players) {
            int spawns = 0;
            if (teamSpawns.containsKey(player.getTeamName())) {
                spawns = teamSpawns.get(player.getTeamName());
            }
            spawns--;

            if (spawns < 0) {
                throw new InvalidMapFileException("Too few player spawns!");
            }

            teamSpawns.put(player.getTeamName(), spawns);
        }
    }

    public int getMapHeight(){
        return this.mapHeight; // Höhe der map
    }

    public int getMapWidth(){
        return this.mapWidth; // Breite der map
    }

    public String getFightFile() {
        return fightFile;
    }

    public String getMapFile() {
        return mapFile;
    }
}

