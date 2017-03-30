package bomberman.network.connection;

import bomberman.Main;
import bomberman.gameplay.Player;
import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.utils.Location;
import bomberman.network.*;
import bomberman.view.views.GameView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class Client extends Connection {

    private ConnectionData server;
    private List<ServerConnectionData> serverList;

    private RefreshableServerList refreshable;

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
        DatagramPacket packet = new DatagramPacket(new byte[2048], 2048);

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

                    sendRecieved(message, server);
                    break;
                case "error":
                    error(sender);

                    sendRecieved(message, server);
                    break;
                case "playerList":
                    String playerString = splittedMessage[1];

                    NetworkPlayer player = NetworkPlayer.fromJson(playerString);

                    synchronized (Main.instance.getGameplayManager().getCurrentSession()) {
                        if (player.getConnectionData().getNetworkData().equals(getMyData().getNetworkData())) {
                            Main.instance.getGameplayManager().getCurrentSession().getLocalPlayer().getBoundingBox().setCenter(player.getBoundingBox().getCenter());
                        } else {
                            getController().getNetworkPlayerMap().put(sender, player);

                            Main.instance.getGameplayManager().getCurrentSession().addPlayer(player);
                        }
                    }

                    sendRecieved(message, server);
                    break;
                case "startGame":
                    Main.instance.getGameplayManager().getCurrentSession().setMapIndex(Integer.parseInt(splittedMessage[1]));
                    Main.instance.getViewManager().postOnUIThread(() -> Main.instance.getViewManager().getCurrentView().changeView(GameView.class));

                    sendRecieved(message, server);
                    break;
                case "position":
                    String movement = splittedMessage[1];
                    Type typeMovement = new TypeToken<Map<String, String>>(){}.getType();
                    Map<String, String> jsonMapMovement = gson.fromJson(movement,typeMovement);

                    Type typeFacing = new TypeToken<Player.FacingDirection>(){}.getType();
                    Player.FacingDirection facingDirection = gson.fromJson(jsonMapMovement.get("facingDirection"), typeFacing);
                    movePlayer(sender, jsonMapMovement.get("location"), facingDirection);

                    sendRecieved(message, server);
                    break;
                case "playerGone":
                    String playerGone = decrypt(splittedMessage[1]);
                    System.out.println("Der Spieler "+ playerGone +" hat das Spiel verlassen.");

                    sendRecieved(message, server);
                    break;
                case "plant":
                    String bombPlant = splittedMessage[1];

                    Bomb bomb = Bomb.fromJson(bombPlant);
                    Main.instance.getGameplayManager().getCurrentSession().getGameMap().spawn(bomb);

                    sendRecieved(message, server);
                    break;
                case "bombExplode":
                    String bombExplode = decrypt(splittedMessage[1]);

                    Bomb bomb1 = Bomb.fromJson(bombExplode);

                    //TODO: Schnauz leute an vernünftige Methoden zu schreiben.

                    break;

                case "ok":
                    recieved(splittedMessage[1], sender);

                    break;
            }
        } else if (sender.getPort() != -1){
            send("error", sender, true);
        }
    }

    @Override
    public void move(Location location, Player.FacingDirection facingDirection, int playerId) {
        Gson gson = new Gson();

        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("location", location.toJson());
        jsonMap.put("id", String.valueOf(playerId));
        jsonMap.put("facingDirection", gson.toJson(facingDirection));

        send("position§" + gson.toJson(jsonMap), server.getNetworkData(), false);
    }

    @Override
    public void plantBomb(Bomb bomb) {
        send("plant§" + bomb.toJson(), server.getNetworkData(), true);
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

    public void join(ServerConnectionData data){
        String username = Main.instance.getGameplayManager().getCurrentSession().getLocalPlayer().getName();
        send("join§" + data.encrypt(username),data.getNetworkData(),true);

        serverList.clear();
        server = data;
    }

    public List<ServerConnectionData> getServerList() {
        return serverList;
    }

    public void refreshServers(RefreshableServerList refreshable){
        this.refreshable = refreshable;

        serverList.clear();

        try {

            send("hello§" + getMyData().toJson(), new NetworkData(InetAddress.getByName("255.255.255.255"), 1638), true);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
