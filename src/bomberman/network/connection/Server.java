package bomberman.network.connection;

import bomberman.gameplay.utils.Location;
import bomberman.network.ConnectionData;
import bomberman.network.NetworkController;
import bomberman.network.NetworkData;
import bomberman.network.NetworkPlayer;
import bomberman.view.engine.utility.Vector2;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Server extends Connection {

    private String name;


    public Server(NetworkController networkController, String name) throws SocketException {
        this(networkController, 1638, name);
    }

    public Server(NetworkController networkController, int customPort, String name) throws SocketException {
        super(networkController);

        this.name = name;

        setSocket(new DatagramSocket(customPort));

        init();

        System.out.println("Custom Server initialized on Port: " + customPort);
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
                    ConnectionData connectionData = new ConnectionData(sender, splittedMessage[1]);

                    if (!getController().getNetworkPlayerMap().containsKey(sender)) {
                        getController().getNetworkPlayerMap().put(sender, new NetworkPlayer(null, "", new Location(0, 0), connectionData));

                        Map<String, String> jsonMap = new HashMap<>();
                        jsonMap.put("connectionData", getMyData().toJson());
                        jsonMap.put("name", name);

                        send("hello§" + gson.toJson(jsonMap), sender, true);

                        System.out.println("ConnectionData from " + packet.getAddress() + " " + packet.getPort());
                    }

                    break;
                case "message":
                    String stringMessage = decrypt(splittedMessage[1]);

                    sendToAll("message§", stringMessage, sender, true);

                    System.out.println("Message from " + packet.getAddress() + " " + packet.getPort() + "\n" + stringMessage);

                    break;
                case "error":
                    if (sender != null) {
                        error(sender);
                    }

                    System.out.println("ERROR");
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
        sendToAll("close", "", getMyData().getNetworkData(), false);

        close();
        getController().startClient();
    }


    private void sendToAll(String prefix, String message, NetworkData networkData, boolean resend) {
        System.out.println(networkData.getPort());
        getController().getNetworkPlayerMap().forEach((key, value) -> {
            System.out.println(value.getConnectionData().getNetworkData().getPort());

            if (!value.getConnectionData().getNetworkData().getIp().getHostAddress().equals(networkData.getIp().getHostAddress()) || value.getConnectionData().getNetworkData().getPort() != networkData.getPort()) {
                send(prefix + value.getConnectionData().encrypt(message), value.getConnectionData().getNetworkData(), resend);
            }
        });
    }
}
