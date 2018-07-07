package de.unisaarland.sopra;

/**
 * Created by Wiebk on 12.09.2016.
 */
public class Observer extends Client {

    public Observer(String ip, int port, int timeout){
        super(ip, port, timeout);
        super.clientConnection.sendWatch();
    }

    @Override
    public void actNow() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getTeamName() {
        return null;
    }

}
