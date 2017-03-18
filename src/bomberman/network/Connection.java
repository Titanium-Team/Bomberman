package bomberman.network;

import bomberman.gameplay.Player;
import bomberman.view.engine.utility.Vector2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

public abstract class Connection {

    private DatagramSocket socket;

    private Thread listener;
    private NetworkController controller;

    private ConnectionData myData;
    private final KeyPair keyPair;

    private Map<String, Request> requestMap;

    public Connection(NetworkController controller) {
        requestMap = new HashMap<>();

        KeyPair tempKeys = null;

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);

            tempKeys = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        keyPair = tempKeys;


        this.controller = controller;

        listener = new Thread(() -> {
            while (true){
                try {
                    listen();
                    Thread.sleep(0);
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    public void init(){
        try {
            myData = new ConnectionData(new NetworkData(InetAddress.getLocalHost(), socket.getLocalPort()), keyPair.getPublic());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        getListener().start();
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public Thread getListener() {
        return listener;
    }

    public NetworkController getController() {
        return controller;
    }


    public ConnectionData getMyData() {
        return myData;
    }

    public void close() {
        getSocket().close();
        getListener().interrupt();
    }

    public void send(String message, NetworkData networkData){
        try {
            Adler32 checksum = new Adler32();
            checksum.update(message.getBytes());

            String checkedMessage = checksum.getValue() + "§" + message;
            requestMap.put(checkedMessage, new Request(checkedMessage, getController().getNetworkPlayerMap().keySet()));

            DatagramPacket packet = new DatagramPacket(checkedMessage.getBytes(), checkedMessage.getBytes().length, networkData.getIp(), networkData.getPort());

            getSocket().send(packet);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean checksum(String[] message){
        Adler32 checksum = new Adler32();
        checksum.update(message[1].getBytes());
        long check = checksum.getValue();
        long checkOriginal = Long.parseLong(message[0]);

        return check == checkOriginal;
    }

    public String decrypt(String message){
        return myData.decrpyt(message, keyPair.getPrivate());
    }

    public void recieved(String message, NetworkData reciever){
        requestMap.get(message).setRecieved(reciever);
    }

    abstract void update();
    abstract void message(String message);
    abstract void listen();
    abstract void move(Vector2 position);
    abstract void plantBomb();
    abstract void explodedBomb();
    abstract void hit(double health);

}
