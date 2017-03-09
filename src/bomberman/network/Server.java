package bomberman.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server implements Connection {

    public Server() throws IOException {
        DatagramSocket socket = null;
        socket = new DatagramSocket(1638);

        while (true){
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            socket.receive(packet);

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            int len = packet.getLength();
            byte[] data = packet.getData();

            System.out.printf("Anfrage von %s vom Port %d mit der Länge %d:%n%s%n", address, port, len, new String(data, 0, len));

            byte[] send = "Thx".getBytes();
            DatagramPacket sendBack = new DatagramPacket(send, send.length, address, port);
            socket.send(sendBack);
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void update() {

    }

    @Override
    public void message(String message) {

    }
}
