package bomberman.network.connection;

import bomberman.gameplay.Player;
import bomberman.gameplay.utils.Location;
import bomberman.network.*;
import bomberman.view.engine.utility.Vector2;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.zip.Adler32;
import java.util.zip.Checksum;
import java.util.HashMap;
import java.util.Map;


public abstract class Connection {

    private DatagramSocket socket;

    private Thread listener;
    private NetworkController controller;

    private ConnectionData myData;
    private final KeyPair keyPair;

    private Map<String, Request> requestMap;

    public Connection(NetworkController controller) {
        requestMap = new LinkedHashMap<>();

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
            while (true) {
                try {
                    listen();
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
    }

    public void init() {
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
        getListener().interrupt();
        getSocket().close();
    }

    public void send(String message, NetworkData networkData, boolean resend) {
        try {
            Adler32 checksum = new Adler32();
            checksum.update(message.getBytes());

            String checkedMessage = checksum.getValue() + "§" + message;
            requestMap.put(checkedMessage, new Request(checkedMessage, getController().getNetworkPlayerMap().keySet(), resend));

            DatagramPacket packet = new DatagramPacket(checkedMessage.getBytes(), checkedMessage.getBytes().length, networkData.getIp(), networkData.getPort());

            getSocket().send(packet);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checksum(String[] message) {
        Adler32 checksum = new Adler32();

        if (message.length >= 2) {
            checksum.update(message[1].getBytes());
            long check = checksum.getValue();
            long checkOriginal = Long.parseLong(message[0]);

            return check == checkOriginal;
        }

        return false;
    }

    public String decrypt(String message) {
        return myData.decrpyt(message, keyPair.getPrivate());
    }

    public void recieved(String message, NetworkData reciever) {
        if (requestMap.containsKey(message)) {
            requestMap.get(message).setRecieved(reciever);

            if (requestMap.get(message).allRecieved()) {
                requestMap.remove(message);
            }
        }
    }

    public void sendRecieved(String message, ConnectionData connectionData){
        send("ok§" + message, connectionData.getNetworkData(), false);
    }

    public void error(NetworkData fromWho) {
        String[] messages = new String[5];
        final int[] index = {0};

        requestMap.forEach((key, value) -> {
            if (value.isRecieved(fromWho) && value.isResend()) {
                messages[index[0]] = value.getRequest();
                index[0]++;
            }
        });

        for (String s : messages) {
            send(s, fromWho, true);
        }
    }

    public void movePlayer(NetworkData networkData, String locationJson){

        Location location = new Location(locationJson);

        controller.getNetworkPlayerMap().get(networkData).getBoundingBox().setCenter(location);
    }

    public abstract void message(String message);
    public abstract void listen();
    public abstract void move(Location location, int playerId);
    public abstract void plantBomb(Location location);
    public abstract void explodedBomb(Location location);
    public abstract void hit(double health, int playerId);
    public abstract void leave();
}
