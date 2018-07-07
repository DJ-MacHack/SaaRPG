package de.unisaarland.sopra.messages;

import de.unisaarland.sopra.Client;
import de.unisaarland.sopra.CommIds;
import de.unisaarland.sopra.model.Game;
import de.unisaarland.sopra.model.Pc;
import de.unisaarland.sopra.model.RoundState;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.comm.ServerConnection;
import de.unisaarland.sopra.utility.LogString;

/**
 * WarCry {@link Command}/{@link Event}
 * Created on 12.09.16.
 * @author Henrik Paul Koehn
 * implemented by Lukas Schaefer
 */
public class WarCry implements Command, Event {

    //Fields
    private int actorId;      // Actor Id of the monster who cried
    private String cry;       // The cry itself
    private CommIds commIds;  // Class with all Commlib Ids

    /**
     * @param commIds {@link CommIds}
     * @param actorId Id of actor who cried
     * @param cry     The String of the {@link WarCry} itself
     */
    public WarCry(CommIds commIds, int actorId, String cry) {
        this.commIds = commIds;
        this.actorId = actorId;
        this.cry = cry;
    }

    /**
     * Executes as {@link Command} on a given {@link Game}
     *
     * @param game {@link Game} to manipulate
     */
    @Override
    public void executeCommand(Game game) {
        game.getMonsterById(this.actorId).setCriedInRound(true);
    }

    /**
     * Tests if this {@link Event}/{@link Command} is valid as {@link Command} for a given {@link Game}
     *
     * @param game {@link Game} to test
     * @return Returns true if valid else false
     */
    @Override
    public boolean validateCommand(Game game) {
        if(game.getRoundState() != RoundState.ACTNOW || game.getMonsterActing() != actorId){
            return false;
        }

        return canCry(game);
    }

    /**
     * Handles aftermath of execution and sends corresponding {@link Event}
     *
     * @param sc {@link ServerConnection} which shall send the {@link Event}
     */
    @Override
    public void sendResults(ServerConnection sc) {
        for (int commId: this.commIds.commLibIds)
            sc.sendWarCry(commId, this.actorId, this.cry);
    }

    /**
     * Sends this as {@link Command} via the given {@link ClientConnection}
     *
     * @param cc {@link ClientConnection} which shall send the {@link Command}
     */
    @Override
    public void sendCommand(ClientConnection cc) {
        cc.sendWarCry(this.cry);
    }

    @Override
    public int getMonsterId() {
        return actorId;
    }

    /**
     * Execute as {@link Event} for a given {@link Client}
     *
     * @param client {@link Client} to manipulate
     */
    @Override
    public void executeEvent(Game game, Client client) {
        client.listenWarCry(this.cry);
        game.getMonsterById(this.actorId).setCriedInRound(true);
    }

    /**
     * Tests if this {@link Event}/{@link Command} is valid as {@link Event} for a given {@link Client}
     *
     * @param client {@link Client} to test
     * @return returns true if valid else false
     */
    @Override
    public boolean validateEvent(Game game, Client client) {
        return canCry(game);
    }

    @Override
    public String getText(Client cli) {
        return String.format("%s has sent a WarCry!%n   Message: \033[37;1m%s\033[0;00m", LogString.createVictimName(cli.getGame(),this.actorId),this.cry);
        //return String.format("%s has sent a WarCry!%n   Message: %s", LogString.createVictimName(cli.getGame(),this.actorId),this.cry);
    }

    /**
     * Tests if the {@link de.unisaarland.sopra.model.Pc} with the selected id is allowed to cry. T_T
     *
     * @param game {@link Game} to be checked
     * @return Returns true if {@link WarCry} is valid, else false
     */
    private boolean canCry(Game game) {
        Pc pc = game.getMonsterById(actorId);

        if(pc == null || pc.isDead()){
            return false;
        }

        if (this.cry.length() > 140)
            return false;

        if (game.getMonsterById(this.actorId).getCriedInRound()) {
            return false;
        }
        
        return true;
    }


    public int getActorId() {
        return this.actorId;
    }

    public String getCry() {
        return this.cry;
    }

    public CommIds getCommIds() {
        return this.commIds;
    }
}
