package bomberman.network;

import bomberman.view.engine.utility.Vector2;

import java.io.IOException;
import java.util.*;

public class NetworkController implements Runnable {

    private final long resendWaitTime = 1000;

    private final boolean hosting;
    private Connection connection;

    private Map<NetworkData, NetworkPlayer> networkPlayerMap;

    public NetworkController(boolean hosting) {
        this.hosting = hosting;
        networkPlayerMap = new HashMap<>();

        try {
            if (hosting) {
                connection = new Server(this);
            } else {
                connection = new Client(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetworkController(int customPort) {
        hosting = true;
        networkPlayerMap = new HashMap<>();

        try {
            connection = new Server(this, customPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<NetworkData, NetworkPlayer> getNetworkPlayerMap() {
        return networkPlayerMap;
    }

    @Override
    public void run() {
        try {

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                }
            }, 0, resendWaitTime);

            Thread.sleep(0);
        } catch (InterruptedException e) {
            connection.close();

            e.printStackTrace();
        }

    }

    public void chatMessage(String message) {
        connection.message(message);
    }

    public void move(Vector2 position) {
        connection.move(position);
    }

    public void plantBomb() {
        connection.plantBomb();
    }

    public void explodedBomb() {
        connection.explodedBomb();
    }

    public void hit(double healthLeft) {
        connection.hit(healthLeft);
    }

    public void joinServer(String ip, int port) {

    }

    public List<ConnectionData> getServerList(){
        if (!hosting){
            return ((Client) connection).getServerList();
        }

        return null;
    }

    public void refreshServers(){
        if (!hosting){
            ((Client) connection).refreshServers();
        }
    }
}
