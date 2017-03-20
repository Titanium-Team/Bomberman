package bomberman.network;

import bomberman.gameplay.utils.Location;
import bomberman.view.engine.utility.Vector2;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Server extends Connection {

    public Server(NetworkController controller) throws SocketException {
        super(controller);

        setSocket(new DatagramSocket(1638));

        init();

        System.out.println("Server initialized");
    }

    public Server(NetworkController networkController, int customPort) throws SocketException {
        super(networkController);

        setSocket(new DatagramSocket(customPort));

        init();

        System.out.println("Custom Server initialized");
    }

    @Override
    public void update() {

    }

    @Override
    public void message(String message) {
        getController().getNetworkPlayerMap().forEach((key, value) -> {
            send("message§" + value.getConnectionData().encrypt(message), value.getConnectionData().getNetworkData(), true);
        });
    }

    @Override
    void listen() {
        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);

        try {
            getSocket().receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                        getController().getNetworkPlayerMap().put(sender, new NetworkPlayer("", new Location(0, 0), null, connectionData));

                        send("hello§" + getMyData().toJson(), sender, true);

                        System.out.println("ConnectionData from " + packet.getAddress() + " " + packet.getPort());
                    }

                    break;
                case "message":
                    String stringMessage = decrypt(splittedMessage[1]);

                    sendToAll("message§", stringMessage, sender, true);

                    System.out.println("Message from " + packet.getAddress() + " " + packet.getPort() + "\n" + stringMessage);

                    break;
                case "error":
                    error(sender);

                    System.out.println("ERROR");
            }
        } else {
            send("error", sender, true);
        }
    }

    @Override
    void move(Vector2 position) {
        Map<String, Float> data = new HashMap<>();
        data.put("xCoord", position.getX());
        data.put("yCoord", position.getY());

        Gson gson = new Gson();

        String json = gson.toJson(data);
    }

    @Override
    void plantBomb() {

    }

    @Override
    void explodedBomb() {

    }

    @Override
    void hit(double health) {

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
