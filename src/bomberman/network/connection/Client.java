package bomberman.network.connection;

import bomberman.gameplay.utils.Location;
import bomberman.network.ConnectionData;
import bomberman.network.NetworkController;
import bomberman.network.NetworkData;
import bomberman.network.ServerConnectionData;
import bomberman.view.engine.utility.Vector2;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Client extends Connection {

    private ConnectionData server;
    private List<ServerConnectionData> serverList;

    private Refreshable refreshable;

    public Client(NetworkController controller) throws IOException {
        super(controller);

        setSocket(new DatagramSocket());
        getSocket().setBroadcast(true);

        init();

        serverList = new ArrayList<>();

        refreshServers(null);

        System.out.println("Client initialized");
    }

    @Override
    public void message(String message) {
        getController().getNetworkPlayerMap().forEach((key, value) -> {
            send("message§" + value.getConnectionData().encrypt(message), value.getConnectionData().getNetworkData(), true);
        });
    }

    @Override
    public void listen() {
        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);

        try {
            getSocket().receive(packet);
        } catch (IOException e) {}

        NetworkData sender = new NetworkData(packet.getAddress(), packet.getPort());

        String message = new String(packet.getData(), 0, packet.getLength());

        String[] splittedChecksum = message.split("§", 2);

        if (checksum(splittedChecksum)) {
            String[] splittedMessage = splittedChecksum[1].split("§", 2);

            Gson gson = new Gson();

            switch (splittedMessage[0]) {
                case "hello":
                    Type type = new TypeToken<Map<String, String>>() {}.getType();
                    Map<String, String> jsonMap = gson.fromJson(splittedMessage[1], type);

                    ServerConnectionData connectionData = new ServerConnectionData(sender, jsonMap.get("connectionData"), jsonMap.get("name"));

                    serverList.add(connectionData);

                    if (refreshable != null){
                        refreshable.refreshListView(serverList);
                    }

                    System.out.println("ConnectionData from Server");

                    break;
                case "message":
                    String stringMessage = decrypt(splittedMessage[1]);

                    System.out.println("Message from " + packet.getAddress() + " " + packet.getPort() + "\n" + stringMessage);

                    break;
            }
        } else if (sender.getPort() != -1){
            send("error", sender, true);
        }
    }

    @Override
    public void move(Location location, int playerId) {

    }

    @Override
    public void plantBomb(Location location) {

    }

    @Override
    public void explodedBomb(Location location) {

    }

    @Override
    public void hit(double health, int playerId) {

    }

    @Override
    public void leave() {

    }


    public List<ServerConnectionData> getServerList() {
        return serverList;
    }

    public void refreshServers(Refreshable refreshable){
        this.refreshable = refreshable;

        serverList.clear();

        try {

            send("hello§" + getMyData().toJson(), new NetworkData(InetAddress.getByName("255.255.255.255"), 1638), true);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
