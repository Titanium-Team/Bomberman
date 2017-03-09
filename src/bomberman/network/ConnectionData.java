package bomberman.network;

import java.net.InetAddress;

public class ConnectionData {

    private InetAddress ip;
    private int port;

    public ConnectionData(InetAddress ip, int port) {
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
