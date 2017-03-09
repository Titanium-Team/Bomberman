package bomberman.network;

import bomberman.gameplay.*;

import java.net.InetAddress;

public class NetworkPlayer extends Player {

    private InetAddress ip;
    private int port;

    public NetworkPlayer(float x, float y, int hp, Level level, InetAddress ip, int port) {
        super(x, y, hp, level);
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void update(float delta) {

    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
