package bomberman.network;

import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.utils.Location;
import bomberman.network.connection.Client;
import bomberman.network.connection.Connection;
import bomberman.network.connection.RefreshableServerList;
import bomberman.network.connection.Server;

import java.io.IOException;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class NetworkController implements Runnable {

    private final long resendWaitTime = 1000;

    private Connection connection;
    private Thread thread;

    private Map<NetworkData, NetworkPlayer> networkPlayerMap;

    private Queue<RequestData> requestDataQueue;

    private boolean host = false;

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
                            ((Client) connection).refreshServers((RefreshableServerList) requestData.getObjects()[0]);
                            break;

                        case "message":
                            connection.message((String) requestData.getObjects()[0]);
                            break;

                        case "position":
                            connection.move((Location) requestData.getObjects()[0], (Integer) requestData.getObjects()[1]);
                            break;

                        case "plant":
                            connection.plantBomb((Bomb) requestData.getObjects()[0]);
                            break;

                        case "exploded":
                            connection.explodedBomb((Location) requestData.getObjects()[0]);
                            break;

                        case "hit":
                            connection.hit((double) requestData.getObjects()[0], (Integer) requestData.getObjects()[1]);
                            break;

                        case "join":
                            ((Client) connection).join((ServerConnectionData) requestData.getObjects()[0]);
                            break;

                        case "startGame":
                            ((Server) connection).startGame((int) requestData.getObjects()[0]);
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

    public void plantBomb(Bomb blomb) {
        requestDataQueue.add(new RequestData("plant", new Object[]{blomb}));
    }

    public void explodedBomb(Location location) {
        requestDataQueue.add(new RequestData("exploded",new Object[]{location}));
    }

    public void hit(int playerId, double healthLeft) {
        requestDataQueue.add(new RequestData("hit", new Object[]{healthLeft, playerId}));
    }

    public void joinServer(ServerConnectionData data) {
        if (!host) {
            requestDataQueue.add(new RequestData("join", new Object[]{data}));
        }
    }


    public List<ServerConnectionData> getServerList(){
        if (!host){
            return ((Client) connection).getServerList();
        }

        return null;
    }

    public void refreshServers(RefreshableServerList refreshable){
        if (!host){
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
        host = true;
        requestDataQueue.add(new RequestData("startServer", new Object[]{customPort, serverName}));
    }

    public void startClient(){
        host = false;
        requestDataQueue.add(new RequestData("startClient", null));
    }

    public void leave() {
        connection.leave();
    }

    public void setNetworkPlayerMap(Map<NetworkData, NetworkPlayer> networkPlayerMap) {
        this.networkPlayerMap = networkPlayerMap;
    }

    public boolean isHost() {
        return host;
    }

    public void startGame(int mapIndex) {
        if (host){
            requestDataQueue.add(new RequestData("startGame", new Object[]{mapIndex}));
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
