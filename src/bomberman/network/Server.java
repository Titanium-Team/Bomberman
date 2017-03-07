package bomberman.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {

    public Server() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(1638);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        while (true){
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            int len = packet.getLength();
            byte[] data = packet.getData();

            System.out.printf("Anfrage von %s vom Port %d mit der Länge %d:%n%s%n", address, port, len, new String(data, 0, len));

            byte[] send = "Thx".getBytes();
            DatagramPacket sendBack = new DatagramPacket(send, send.length, address, port);
            try {
                socket.send(sendBack);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        new Server();
    }
}
