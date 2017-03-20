package bomberman.network;

import bomberman.view.engine.utility.Vector2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class NetworkController implements Runnable {

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
        while (true) {
            Scanner scanner = new Scanner(System.in);
            chatMessage(scanner.nextLine());
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
}
