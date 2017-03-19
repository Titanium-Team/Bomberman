package bomberman.network;

import bomberman.gameplay.utils.Location;
import bomberman.view.engine.utility.Vector2;
import com.google.gson.Gson;
import jdk.nashorn.api.scripting.JSObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class Client extends Connection{

    private ConnectionData server;

    public Client(NetworkController controller) throws IOException {
        super(controller);

        setSocket(new DatagramSocket());
        getSocket().setBroadcast(true);

        init();

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

                    getController().getNetworkPlayerMap().putIfAbsent(sender, new NetworkPlayer("", new Location(0, 0), null, connectionData));

                    System.out.println("ConnectionData from Server");

                    break;
                case "message":
                    String stringMessage = decrypt(splittedMessage[1]);

                    System.out.println("Message from " + packet.getAddress() + " " + packet.getPort() + "\n" + stringMessage);

                    break;
            }
        }else {
            send("error", sender, true);
        }
    }

    @Override
    void move(Vector2 position) {

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

    private void refreshServers(){
        try {
            send("hello§" + getMyData().toJson(), new NetworkData(InetAddress.getByName("255.255.255.255"), 1638), true);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
