package bomberman.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class Client implements Connection{

    private DatagramSocket socket;

    public Client() throws IOException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);
    }

    @Override
    public void close() {
        socket.close();
    }

    @Override
    public void update() {

    }

    @Override
    public void message(String message) {
        try {
            InetAddress inetAddress = InetAddress.getByName("255.255.255.255");

            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, inetAddress, 1638);
            socket.send(packet);


            //Get
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getData().length);
            System.out.println(modifiedSentence + " \nIP: " + receivePacket.getAddress() + " Port: " + receivePacket.getPort());
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
