package bomberman.network;

import java.io.IOException;
import java.net.*;
import java.util.Date;

public class NetworkController {
    public static void main(String[] args) throws IOException {
        new NetworkController();
    }

    public NetworkController() throws IOException {
        String s = new Date().toString();
        byte[] raw = s.getBytes();

        //Alle IPs
        InetAddress inetAddress = InetAddress.getByName("255.255.255.255");
        DatagramPacket packetB = new DatagramPacket(raw, raw.length, inetAddress, 1638);
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
        socket.send(packetB);

        //Get
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);
        String modifiedSentence = new String(receivePacket.getData());
        System.out.println(modifiedSentence);
        socket.close();
    }
}
