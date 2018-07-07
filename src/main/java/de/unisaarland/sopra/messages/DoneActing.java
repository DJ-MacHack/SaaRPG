package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.comm.ServerConnection;
import de.unisaarland.sopra.model.RoundState;
import de.unisaarland.sopra.utility.LogString;

/**
 * Created by Team14 on 9/12/16.
 * Responsibility: Lukas Kirschner
 * implemented by Karla
 */
public class DoneActing implements Command, Event {

    private int actorId;
    private CommIds commIds;

    public DoneActing(CommIds commIds, int actorId){
        this.actorId = actorId;
        this.commIds = commIds;
    }

    @Override
    public void executeCommand(Game game) {
        game.nextMonster();
    }

    @Override
    public boolean validateCommand(Game game) {
        if(game.getRoundState() != RoundState.ACTNOW){
            return false;
        }

        if(game.getMonsterActing() != actorId){
            return false;
        }

        return true;
    }

    @Override
    public void sendResults(ServerConnection sc) {
        for(int comm : commIds.getCommLibIds()){
            sc.sendDoneActing(comm,actorId);
        }
    }

    @Override
    public void sendCommand(ClientConnection cc) {
        cc.sendDoneActing();
    }

    @Override
    public int getMonsterId() {
        return actorId;
    }

    @Override
    public void executeEvent(Game game, Client client) {
        game.nextMonster();
    }

    @Override
    public boolean validateEvent(Game game, Client client) {
        return true;
        /*if(client.getGame().getMonsterActing() == actorId){
            return true;
        }
        return false;*/
    }

    @Override
    public String getText(Client cli) {
        return String.format("%s is done acting!", LogString.createVictimName(cli.getGame(),this.actorId));
    }

    public int getActorId() {
        return this.actorId;
    }

    public CommIds getCommIds() {
        return this.commIds;
    }
}
