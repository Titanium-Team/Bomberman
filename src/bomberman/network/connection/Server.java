package bomberman.network.connection;

import bomberman.Main;
import bomberman.gameplay.Player;
import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.tile.objects.PowerUp;
import bomberman.gameplay.utils.Location;
import bomberman.network.ConnectionData;
import bomberman.network.NetworkController;
import bomberman.network.NetworkData;
import bomberman.network.NetworkPlayer;
import bomberman.view.engine.LightingView;
import bomberman.view.engine.utility.Vector2;
import bomberman.view.views.LobbyView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.*;

public class Server extends Connection {

    private String name;

    private boolean gameStarted = false;

    private Map<NetworkData, ConnectionData> dataConnectionMap;


    public Server(NetworkController networkController, String name) throws SocketException {
        this(networkController, 1638, name);
    }

    public Server(NetworkController networkController, int customPort, String name) throws SocketException {
        super(networkController);

        setGameplayManager(Main.instance.getGameplayManager());

        getGameplayManager().getCurrentSession().setPowerupSpawning(true);

        dataConnectionMap = new HashMap<>();

        this.name = name;

        setSocket(new DatagramSocket(customPort));

        init();

        System.out.println("Custom Server initialized on Port: " + customPort);
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
                    if (!gameStarted) {
                        ConnectionData connectionData = new ConnectionData(sender, splittedMessage[1]);

                        if (!getController().getNetworkPlayerMap().containsKey(sender)) {
                            dataConnectionMap.put(sender, connectionData);

                            Map<String, String> jsonMap = new HashMap<>();
                            jsonMap.put("connectionData", getMyData().toJson());
                            jsonMap.put("name", name);

                            send("hello§" + gson.toJson(jsonMap), sender, true);

                            System.out.println("ConnectionData from " + packet.getAddress() + " " + packet.getPort());
                        }
                    }

                    break;
                case "message":
                    String stringMessage = decrypt(splittedMessage[1]);

                    sendToAll("message§", stringMessage, sender, true, true);

                    System.out.println("Message from " + packet.getAddress() + " " + packet.getPort() + "\n" + stringMessage);


                    sendRecieved(message, dataConnectionMap.get(sender));
                    break;
                case "join":
                    String name = decrypt(splittedMessage[1]);

                    NetworkPlayer player = new NetworkPlayer(name, new Location(0, 0), dataConnectionMap.get(sender));
                    getController().getNetworkPlayerMap().put(sender, player);

                    if (Main.instance.getViewManager().getCurrentView() instanceof LobbyView){
                        List<String> stringList = new ArrayList<String>();
                        for (Player p : getController().getNetworkPlayerMap().values()){
                            stringList.add(p.getName());
                        }
                        stringList.add(getGameplayManager().getCurrentSession().getLocalPlayer().getName());

                        Main.instance.getViewManager().postOnUIThread(() -> ((LobbyView) Main.instance.getViewManager().getCurrentView()).refreshListView(stringList));
                    }

                    System.out.println("Joined Player: " + name);

                    sendRecieved(message, dataConnectionMap.get(sender));
                    break;
                case "error":
                    if (sender != null) {
                        error(sender);
                    }

                    System.out.println("ERROR");

                    sendRecieved(message, dataConnectionMap.get(sender));
                    break;
                case "ok":
                    recieved(splittedMessage[1], sender);

                    break;
                case "position":
                    String jsonMove = splittedMessage[1];
                    sendToAll("position§", jsonMove, sender, false, false);

                    Type typeMove = new TypeToken<Map<String, String>>(){}.getType();

                    Map<String, String> jsonMapMove = gson.fromJson(jsonMove, typeMove);

                    Type typeFacing = new TypeToken<Player.FacingDirection>(){}.getType();
                    Player.FacingDirection facingDirection = gson.fromJson(jsonMapMove.get("facingDirection"), typeFacing);
                    movePlayer(sender, jsonMapMove.get("location"), facingDirection);

                    sendRecieved(message, dataConnectionMap.get(sender));
                    break;
                case "plant":
                    String bombPlant = splittedMessage[1];
                    sendToAll("plant§", bombPlant, sender, false, false);

                    Bomb bomb = Bomb.fromJson(bombPlant);
                    getGameplayManager().getCurrentSession().getGameMap().spawn(bomb);

                    sendRecieved(message, dataConnectionMap.get(sender));
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

        sendToAll("position§", gson.toJson(jsonMap), getMyData().getNetworkData(), false, false);
    }

    @Override
    public void plantBomb(Bomb bomb) {
        sendToAll("plant§", bomb.toJson(), getMyData().getNetworkData(), true, false);
    }

    @Override
    public void leave() {
        sendToAll("close", "", getMyData().getNetworkData(), false, true);

        close();
        getController().startClient();
    }


    private void sendToAll(String prefix, String message, NetworkData networkData, boolean resend, boolean encrypt) {
        getController().getNetworkPlayerMap().forEach((key, value) -> {
            if (!value.getConnectionData().getNetworkData().getIp().getHostAddress().equals(networkData.getIp().getHostAddress()) || value.getConnectionData().getNetworkData().getPort() != networkData.getPort()) {
                String messageSend = message;
                if (encrypt){
                    value.getConnectionData().encrypt(message);
                }

                send(prefix + message, value.getConnectionData().getNetworkData(), resend);
            }
        });
    }

    public void startGame(int mapIndex) {
        synchronized (getGameplayManager().getCurrentSession()){
            for (NetworkPlayer player : getController().getNetworkPlayerMap().values()){
                player.getBoundingBox().setCenter(getGameplayManager().getCurrentSession().getGameMap().getRandomStartPosition());

                getGameplayManager().getCurrentSession().addPlayer(player);
            }

            gameStarted = true;
        }


        sendToAll("startGame§", String.valueOf(mapIndex), getMyData().getNetworkData(), true, false);

        sendUserList();
    }

    private void sendUserList(){
        Player local = getGameplayManager().getCurrentSession().getLocalPlayer();
        Collection<NetworkPlayer> networkPlayers = getController().getNetworkPlayerMap().values();
        NetworkPlayer localNetwork = new NetworkPlayer(local.getName(), local.getBoundingBox().getCenter(), getMyData());

        List<String> playerList = new ArrayList<>();
        for (NetworkPlayer networkPlayer : networkPlayers){
            String player = networkPlayer.toJson();

            playerList.add(player);
        }
        playerList.add(localNetwork.toJson());

        for (String s : playerList){
            sendToAll("playerList§", s, getMyData().getNetworkData(), true, false);
        }
    }

    public void powerUpSpawn(PowerUp powerUp) {
        sendToAll("powerUpSpawn§", powerUp.toJson(), getMyData().getNetworkData(), false, false);
    }
}
