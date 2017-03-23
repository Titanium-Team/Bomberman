package bomberman.network;

import bomberman.gameplay.utils.Location;
import bomberman.network.connection.Client;
import bomberman.network.connection.Connection;
import bomberman.network.connection.Server;
import bomberman.view.engine.utility.Vector2;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class NetworkController implements Runnable {

    private final long resendWaitTime = 1000;

    private Connection connection;
    private Thread thread;

    private Map<NetworkData, NetworkPlayer> networkPlayerMap;

    private Queue<RequestData> requestDataQueue;

    public NetworkController() {
        requestDataQueue = new LinkedBlockingQueue<>();

        thread = new Thread(this);
        thread.start();

        networkPlayerMap = new HashMap<>();

        try {
            connection = new Client(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<NetworkData, NetworkPlayer> getNetworkPlayerMap() {
        return networkPlayerMap;
    }

    @Override
    public void run() {
        try {
            while (true){
                RequestData requestData = requestDataQueue.poll();

                if (requestData != null) {
                    switch (requestData.getType()){
                        case "hello":
                            ((Client) connection).refreshServers();
                            break;

                        case "message":
                            connection.message((String) requestData.getObjects()[0]);
                            break;

                        case "position":
                            connection.move((Location) requestData.getObjects()[0], (Integer) requestData.getObjects()[1]);
                            break;

                        case "plant":
                            connection.plantBomb((Location) requestData.getObjects()[0]);
                            break;

                        case "exploded":
                            connection.explodedBomb((Location) requestData.getObjects()[0]);
                            break;

                        case "hit":
                            connection.hit((double) requestData.getObjects()[0], (Integer) requestData.getObjects()[1]);
                            break;

                    }

                }
                Thread.sleep(0);
            }
        } catch (InterruptedException e) {
            connection.close();
        }

    }

    public void chatMessage(String message) {
        requestDataQueue.add(new RequestData("message", new Object[]{message}));
    }

    public void move(Location location, int playerId) {
        requestDataQueue.add(new RequestData("position", new Object[]{location, playerId}));
    }

    public void plantBomb(Location location) {
        requestDataQueue.add(new RequestData("plant", new Object[]{location}));
    }

    public void explodedBomb(Location location) {
        requestDataQueue.add(new RequestData("exploded",new Object[]{location}));
    }

    public void hit(int playerId, double healthLeft) {
        requestDataQueue.add(new RequestData("hit", new Object[]{healthLeft, playerId}));
    }

    public void joinServer(NetworkData data) {

    }


    public List<ConnectionData> getServerList(){
        if (connection instanceof Client){
            return ((Client) connection).getServerList();
        }

        return null;
    }

    public void refreshServers(){
        if (connection instanceof Client){
            requestDataQueue.add(new RequestData("hello", new Object[]{connection.getMyData()}));
        }
    }

    public void close(){
        thread.interrupt();
    }

    public void startServer(){
        try {
            connection = new Server(this);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void startServer(int customPort){
        try {
            connection = new Server(this, customPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void startClient(){
        try {
            connection = new Client(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class RequestData{
        private String type;
        private Object[] objects;

        public RequestData(String type, Object[] objects) {
            this.type = type;
            this.objects = objects;
        }

        public String getType() {
            return type;
        }

        public Object[] getObjects() {
            return objects;
        }
    }
}
