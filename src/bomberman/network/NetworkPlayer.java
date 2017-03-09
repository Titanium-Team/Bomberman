package bomberman.network;

import bomberman.gameplay.*;

import java.net.InetAddress;

public class NetworkPlayer extends Player {

    private ConnectionData connectionData;

    public NetworkPlayer(float x, float y, int hp, Level level, ConnectionData connectionData) {
        super(x, y, hp, level);
        this.connectionData = connectionData;
    }

    @Override
    public void update(float delta) {

    }

    public ConnectionData getConnectionData() {
        return connectionData;
    }
}
