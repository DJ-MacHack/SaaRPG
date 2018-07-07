package de.unisaarland.sopra.messages.attack;

import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.MonsterType;
import de.unisaarland.sopra.messages.Command;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.comm.ServerConnection;
import de.unisaarland.sopra.model.Player;
import de.unisaarland.sopra.model.RoundState;
import de.unisaarland.sopra.utility.GameVector;

import java.util.List;

/**
 * Created by Wiebk on 12.09.2016.
 */
public class Watch implements Command {

    private CommIds commIds;
    private int commId;
    private Game game;

    public Watch(CommIds commids, int commId) {
        this.commIds = commids;
        this.commId = commId;
    }

    @Override
    public void executeCommand(Game game) {
        if(game.getRoundState() != RoundState.REGISTER){
            return;
        }

        this.game = game;
        if (!this.commIds.commLibIds.contains(this.commId))
            this.commIds.addCommLibId(this.commId);
    }

    @Override
    public boolean validateCommand(Game game) {
        return true;
    }

    @Override
    public void sendResults(ServerConnection sc) {
        if(game == null || game.getRoundState() != RoundState.REGISTER){
            return;
        }

        sc.sendMap(commId, game.getMapFile());
        sc.sendFight(commId, game.getFightFile());

        List<Player> players = this.game.getPlayers();
        for (Player player : players) {
            // Is player already registered and not the new player
            if (player.getRegistered()) {
                // Get x and y coord of target player
                GameVector vec = game.getCreatureById(player.getId()).getPosition();
                sc.sendRegistered(this.commId, player.getId(), player.getName(),
                        MonsterType.valueOf(player.getMonsterType().name()),
                        player.getTeamName(), player.getId(), vec.getX(), vec.getY());
            }
        }
    }

    @Override
    public int getMonsterId() {
        return -1;
    }
}
