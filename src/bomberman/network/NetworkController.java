package bomberman.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class NetworkController implements Runnable {

    private boolean hosting = false;
    private Connection connection;

    private Map<NetworkData, NetworkPlayer> networkPlayerMap;

    public NetworkController(boolean hosting) throws IOException {
        this.hosting = hosting;
        networkPlayerMap = new HashMap<>();

        if (hosting) {
            connection = new Server(this);
        } else {
            connection = new Client(this);
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
}
