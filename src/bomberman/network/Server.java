package bomberman.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

public class Server extends Connection {

    public Server(NetworkController controller) throws IOException {
        super(controller);

        setSocket(new DatagramSocket(1638));
        getListener().start();
    }

    @Override
    public void update() {

    }

    @Override
    public void message(String message) {
        getController().getNetworkPlayerMap().forEach((key, value) -> {
            try {

                byte[] messageBytes = message.getBytes();
                DatagramPacket send = new DatagramPacket(messageBytes, messageBytes.length, value.getConnectionData().getIp(), value.getConnectionData().getPort());

                getSocket().send(send);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        int len = packet.getLength();
        byte[] data = packet.getData();

        getController().getNetworkPlayerMap().putIfAbsent(address.getHostAddress() + port, new NetworkPlayer(0, 0, 0, null, new ConnectionData(address, port)));

        System.out.printf("Anfrage von %s vom Port %d mit der Länge %d:%n%s%n", address, port, len, new String(data, 0, len));

        message("THX");
    }
}
