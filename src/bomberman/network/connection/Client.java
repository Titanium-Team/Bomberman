package bomberman.network.connection;

import bomberman.gameplay.utils.Location;
import bomberman.network.ConnectionData;
import bomberman.network.NetworkController;
import bomberman.network.NetworkData;
import bomberman.view.engine.utility.Vector2;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Client extends Connection {

    private ConnectionData server;
    private List<ConnectionData> serverList;

    public Client(NetworkController controller) throws IOException {
        super(controller);

        setSocket(new DatagramSocket());
        getSocket().setBroadcast(true);

        init();

        serverList = new ArrayList<>();

        refreshServers();

        System.out.println("Client initialized");
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

                    serverList.add(connectionData);

                    System.out.println("ConnectionData from Server");

                    break;
                case "message":
                    String stringMessage = decrypt(splittedMessage[1]);

                    System.out.println("Message from " + packet.getAddress() + " " + packet.getPort() + "\n" + stringMessage);

                    break;
            }
        } else {
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


    public List<ConnectionData> getServerList() {
        return serverList;
    }

    public void refreshServers(){
        serverList.clear();

        try {
            send("hello§" + getMyData().toJson(), new NetworkData(InetAddress.getByName("255.255.255.255"), 1638), true);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
