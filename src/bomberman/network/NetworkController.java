package bomberman.network;

import java.io.IOException;
import java.net.*;
import java.util.Date;

public class NetworkController {
    public static void main(String[] args) throws IOException {
        new NetworkController();
    }

    public NetworkController() throws IOException {
        InetAddress ia = InetAddress.getByName( "localhost" );

        String s = new Date().toString();
        byte[] raw = s.getBytes();

        DatagramPacket packet = new DatagramPacket(raw, raw.length, ia, 1638);

        DatagramSocket dSocket = new DatagramSocket();

        dSocket.send(packet);

        //Alle IPs
        InetAddress inetAddress = InetAddress.getByName("255.255.255.255");
        DatagramPacket packetB = new DatagramPacket(raw, raw.length, inetAddress, 1638);
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
        socket.send(packetB);

        //Get
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        dSocket.receive(receivePacket);
        String modifiedSentence = new String(receivePacket.getData());
        System.out.println(modifiedSentence);
        dSocket.close();

        receiveData = new byte[1024];
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);
        modifiedSentence = new String(receivePacket.getData());
        System.out.println(modifiedSentence);
        socket.close();
    }
}
