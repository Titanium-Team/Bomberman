package bomberman.network;

import java.net.InetAddress;

public class NetworkData {

    private final InetAddress ip;
    private final int port;

    public NetworkData(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
