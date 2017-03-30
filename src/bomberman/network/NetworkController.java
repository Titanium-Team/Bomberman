package bomberman.network;

import bomberman.Main;
import bomberman.gameplay.Player;
import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.tile.objects.PowerUp;
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

    private Queue<Runnable> requestDataQueue;

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
                Runnable runnable = requestDataQueue.poll();
                if (runnable != null){
                    runnable.run();
                }

                Thread.sleep(0);
            }
        } catch (InterruptedException e) {
            connection.close();
        }

    }

    public void chatMessage(String message) {
        requestDataQueue.add(() -> connection.message(message));
    }

    public void move(Location location, int playerId, Player.FacingDirection facingDirection) {
        requestDataQueue.add(() -> connection.move(location, facingDirection, playerId));
    }

    public void plantBomb(Bomb blomb) {
        requestDataQueue.add(() -> connection.plantBomb(blomb));
    }

    public void explodedBomb(Location location) {
        requestDataQueue.add(() -> connection.explodedBomb(location));
    }

    public void hit(int playerId, double healthLeft) {
        requestDataQueue.add(() -> connection.hit(healthLeft, playerId));
    }

    public void joinServer(ServerConnectionData data) {
        if (!host) {
            requestDataQueue.add(() -> ((Client) connection).join(data));
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
            requestDataQueue.add(() -> ((Client) connection).refreshServers(refreshable));
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
        requestDataQueue.add(() -> {
            connection.close();
            try {
                connection = new Server(Main.instance.getNetworkController(), customPort, serverName);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        });
    }

    public void startClient(){
        host = false;
        requestDataQueue.add(() -> {
            connection.close();
            try {
                connection = new Client(Main.instance.getNetworkController());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
            requestDataQueue.add(() -> ((Server) connection).startGame(mapIndex));
        }
    }

    public void powerUpSpawn(PowerUp powerUp) {
        if (host){
            requestDataQueue.add(() -> ((Server) connection).powerUpSpawn(powerUp));
        }
    }
}
