package bomberman.network;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server extends Connection {

    public Server(NetworkController controller) throws IOException {
        super(controller);

        setSocket(new DatagramSocket(1638));

        init();

        System.out.println("Server initialized");
    }

    @Override
    public void update() {

    }

    @Override
    public void message(String message) {
        getController().getNetworkPlayerMap().forEach((key, value) -> {
            send("message§" + value.getConnectionData().encrypt(message), value.getConnectionData().getNetworkData());
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

        String message = new String(packet.getData(), 0, packet.getLength());

        String[] splittedMessage = message.split("§", 1);

        Gson gson = new Gson();

        switch (splittedMessage[0]) {
            case "hello":
                NetworkData thisPlayer = new NetworkData(packet.getAddress(), packet.getPort());

                ConnectionData connectionData = new ConnectionData(thisPlayer, splittedMessage[1]);

                if (!getController().getNetworkPlayerMap().containsKey(packet.getAddress().getHostAddress() + packet.getPort())) {
                    //getController().getNetworkPlayerMap().put(thisPlayer, new NetworkPlayer(0, 0, 0, null, connectionData));

                    send("hello§" + getMyData().toJson(), new NetworkData(packet.getAddress(), packet.getPort()));

                    System.out.println("ConnectionData from " + packet.getAddress() + " " + packet.getPort());
                }

                break;
            case "message":
                String stringMessage = decrypt(splittedMessage[1]);

                sendToAll("message§" + stringMessage, new NetworkData(packet.getAddress(), packet.getPort()));

                System.out.println("Message from " + packet.getAddress() + " " + packet.getPort() + "\n" + stringMessage);

                break;
        }
    }


    private void sendToAll(String message, NetworkData networkData) {
        getController().getNetworkPlayerMap().forEach((key, value) -> {
            if (value.getConnectionData().getNetworkData() != networkData) {
                send("message§" + value.getConnectionData().encrypt(message), networkData);
            }
        });
    }
}
