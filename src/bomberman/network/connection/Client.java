package bomberman.network.connection;

import bomberman.Main;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client extends Connection {

    private ConnectionData server;
    private List<ServerConnectionData> serverList;

    private Refreshable refreshable;

    public Client(NetworkController controller) throws IOException {
        super(controller);

        setSocket(new DatagramSocket());
        getSocket().setBroadcast(true);

        init();

        serverList = new ArrayList<>();

        refreshServers(null);

        System.out.println("Client initialized");
    }

    @Override
    public void message(String message) {
        getController().getNetworkPlayerMap().forEach((key, value) -> {
            send("message§" + value.getConnectionData().encrypt(message), value.getConnectionData().getNetworkData(), true);
        });
    }

    @Override
    public void listen() {
        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);

        try {
            getSocket().receive(packet);
        } catch (IOException e) {}

        NetworkData sender = new NetworkData(packet.getAddress(), packet.getPort());

        String message = new String(packet.getData(), 0, packet.getLength());

        String[] splittedChecksum = message.split("§", 2);

        if (checksum(splittedChecksum)) {
            String[] splittedMessage = splittedChecksum[1].split("§", 2);

            Gson gson = new Gson();

            switch (splittedMessage[0]) {
                case "hello":
                    Type typeHello = new TypeToken<Map<String, String>>() {}.getType();
                    Map<String, String> jsonMap = gson.fromJson(splittedMessage[1], typeHello);

                    ServerConnectionData connectionData = new ServerConnectionData(sender, jsonMap.get("connectionData"), jsonMap.get("name"));

                    serverList.add(connectionData);

                    if (refreshable != null){
                        refreshable.refreshListView(serverList);
                    }

                    System.out.println("ConnectionData from Server");

                    break;
                case "message":
                    String stringMessage = decrypt(splittedMessage[1]);

                    System.out.println("Message from " + packet.getAddress() + " " + packet.getPort() + "\n" + stringMessage);

                    break;

                case "error":
                    error(sender);
                    break;
                case "playerlist":
                    String playerlist = decrypt(splittedMessage[1]);
                    Type typePlayerlist = new TypeToken<Map<NetworkData, NetworkPlayer>>(){}.getType();
                    getController().setNetworkPlayerMap(gson.fromJson(playerlist, typePlayerlist));
                    break;
                case "startGame":
                    String map = decrypt(splittedMessage[1]);
                    Main.instance.getGameplayManager().getCurrentSession().setMapIndex(Integer.parseInt(map));
                    break;
                case "position":
                    String movement = decrypt(splittedMessage[1]);
                    Type typeMovement = new TypeToken<Map<String, String>>(){}.getType();
                    Map<String, String> jsonMapMovement = gson.fromJson(movement,typeMovement);
                    movePlayer(sender, jsonMapMovement.get("location"), Integer.parseInt(jsonMapMovement.get("id")));
                    break;
                case "playerGone":
                    String player = decrypt(splittedMessage[1]);
                    System.out.println("Der Spieler "+ player +" hat das Spiel verlassen.");
                    break;
                case "plantBomb":
                    //TODO: DI DIS

                    break;
                case "bombExplode":
                    // TODO: DO DIS

                    break;

            }
        } else if (sender.getPort() != -1){
            send("error", sender, true);
        }
    }

    @Override
    public void move(Location location, int playerId) {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("location", location.toJson());
        jsonMap.put("id", String.valueOf(playerId));

        Gson gson = new Gson();

        send("movement§" + gson.toJson(jsonMap), server.getNetworkData(), false);
    }

    @Override
    public void plantBomb(Location location) {
        send("movement§" + location.toJson(), server.getNetworkData(), true);
    }

    @Override
    public void explodedBomb(Location location) {
        send("bombExplode§" + location.toJson(), server.getNetworkData(), false);
    }

    @Override
    public void hit(double health, int playerId) {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("health", String.valueOf(health));
        jsonMap.put("id", String.valueOf(playerId));

        Gson gson = new Gson();

        send("gotHit" + gson.toJson(jsonMap), server.getNetworkData(), false);
    }

    @Override
    public void leave() {
        getSocket().close();

    }

    public void join(NetworkData data){
        String username = Main.instance.getGameplayManager().getCurrentSession().getLocalPlayer().getName();
        send("join§" + username,data,true);
    }

    public List<ServerConnectionData> getServerList() {
        return serverList;
    }

    public void refreshServers(Refreshable refreshable){
        this.refreshable = refreshable;

        serverList.clear();

        try {

            send("hello§" + getMyData().toJson(), new NetworkData(InetAddress.getByName("255.255.255.255"), 1638), true);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
