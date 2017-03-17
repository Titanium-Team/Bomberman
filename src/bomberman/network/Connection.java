package bomberman.network;

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
            requestMap.put(message, new Request(message, getController().getNetworkPlayerMap().keySet()));

            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, networkData.getIp(), networkData.getPort());

            getSocket().send(packet);

        } catch (IOException e){
            e.printStackTrace();
        }
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
}
