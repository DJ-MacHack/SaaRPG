package de.unisaarland.sopra;
import de.unisaarland.sopra.comm.ClientConnection;
import de.unisaarland.sopra.messages.Event;
import de.unisaarland.sopra.messages.EventFactoryImpl;
import de.unisaarland.sopra.utility.InvalidMapFileException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DJ MacHack on 12.09.2016.
 */
public abstract class Client extends Master {
    protected ClientConnection<Event> clientConnection;
    protected int ownCreatureId;
    protected List<String> warcrys = new ArrayList<>();
    /**
     * create a new client
     * @param ip        IP or host
     * @param port      port number
     * @param timeout   timeout in ms
     */
    public Client(String ip, int port, int timeout) {
        super();
        this.clientConnection = new ClientConnection<>(ip, port, timeout, new EventFactoryImpl());
        this.ownCreatureId = -1;
    }

    /**
     * get client connection
     * @return
     */
    public ClientConnection getClientConnection() {
        return this.clientConnection;
    }

    /**
     * get own creature
     * @return
     */
    public int getOwnCreatureId() {
        return this.ownCreatureId;
    }

    /**
     * set own creature
     * @param id
     */
    public void setOwnCreature(int id) {
            this.ownCreatureId = id;
    }

    /**
     * act now
     */
    public abstract void actNow();

    /**
     * run
     */
    public void run() {
        while(isRunning()) {
            Event event = this.clientConnection.nextEvent();
            if (event.validateEvent(game, this)){
                event.executeEvent(game, this);
            }
        }
        clientConnection.close();
    }

    /**
     * listens to the war cry
     * @param cry
     */
    public void listenWarCry(String cry) {
        this.warcrys.add(cry);
    }

    public boolean isRunning(){
        return this.running;
    }

    public void setRunning(boolean run){
        this.running = run;
    }

    public abstract String getName();

    public abstract String getTeamName();

}
