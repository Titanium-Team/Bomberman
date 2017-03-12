package bomberman.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public abstract class Connection {

    private DatagramSocket socket;

    private Thread listener;
    private NetworkController controller;

    private ConnectionData myData;
    private final KeyPair keyPair;

    public Connection(NetworkController controller) {
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
            myData = new ConnectionData(InetAddress.getLocalHost(), socket.getLocalPort(), keyPair.getPublic());
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

    public void send(String message, InetAddress address, int port){
        try {
            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, address, port);
            getSocket().send(packet);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public String decrypt(String message){
        return myData.decrpyt(message, keyPair.getPrivate());
    }

    abstract void update();
    abstract void message(String message);
    abstract void listen();

}
