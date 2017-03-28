package bomberman.network;

import bomberman.gameplay.utils.Location;
import bomberman.network.connection.Client;
import bomberman.network.connection.Connection;
import bomberman.network.connection.Refreshable;
import bomberman.network.connection.Server;
import bomberman.view.engine.utility.Vector2;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

public class NetworkController implements Runnable {

    private final long resendWaitTime = 1000;

    private Connection connection;
    private Thread thread;

    private Map<NetworkData, NetworkPlayer> networkPlayerMap;

    private Queue<RequestData> requestDataQueue;

    public NetworkController() {
        requestDataQueue = new LinkedBlockingQueue<>();

        init();

        networkPlayerMap = new HashMap<>();
    }

    private void init(){
        thread = new Thread(this);
        thread.start();
    }

    public Map<NetworkData, NetworkPlayer> getNetworkPlayerMap() {
        return networkPlayerMap;
    }

    @Override
    public void run() {
        try {

            try {
                connection = new Client(this);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true){
                RequestData requestData = requestDataQueue.poll();



                if (requestData != null) {
                    switch (requestData.getType()){
                        case "startServer":
                            connection.close();
                            try {
                                connection = new Server(this, (Integer) requestData.getObjects()[0], (String) requestData.getObjects()[1]);
                            } catch (SocketException e) {
                                e.printStackTrace();
                            }
                            break;

                        case "startClient":
                            connection.close();
                            try {
                                connection = new Client(this);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;

                        case "hello":
                            ((Client) connection).refreshServers((Refreshable) requestData.getObjects()[0]);
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


    public List<ServerConnectionData> getServerList(){
        if (connection instanceof Client){
            return ((Client) connection).getServerList();
        }

        return null;
    }

    public void refreshServers(Refreshable refreshable){
        if (connection instanceof Client){
            requestDataQueue.add(new RequestData("hello", new Object[]{refreshable}));
        }
    }

    public void close(){
        thread.interrupt();
    }

    public void startServer(String serverName){
        startServer(serverName, 1638);
    }

    public void startServer(String serverName, int customPort){
        requestDataQueue.add(new RequestData("startServer", new Object[]{customPort, serverName}));
    }

    public void startClient(){
        requestDataQueue.add(new RequestData("startClient", null));
    }

    public void leave() {
        connection.leave();
    }

    public void setNetworkPlayerMap(Map<NetworkData, NetworkPlayer> networkPlayerMap) {
        this.networkPlayerMap = networkPlayerMap;
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
