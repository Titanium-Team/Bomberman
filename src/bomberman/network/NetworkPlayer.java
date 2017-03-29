package bomberman.network;

import bomberman.Main;
import bomberman.gameplay.GameMap;
import bomberman.gameplay.GameSession;
import bomberman.gameplay.Player;
import bomberman.gameplay.utils.Location;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class NetworkPlayer extends Player {

    private ConnectionData connectionData;

    public NetworkPlayer(String name, Location center, ConnectionData connectionData) {
        super(Main.instance.getGameplayManager().getCurrentSession(), PlayerType.NETWORK, name, center);

        this.connectionData = connectionData;
    }

    @Override
    public FacingDirection getFacingDirection() {
        //@TODO Bitte implementieren. Danke.
        throw new NotImplementedException();
    }

    //@Override
    public void update(float delta) {

    }

    public ConnectionData getConnectionData() {
        return connectionData;
    }

    public static NetworkPlayer fromJson(String jsonSting){
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();

        Map<String, String> jsonMap = gson.fromJson(jsonSting, type);

        NetworkPlayer player = new NetworkPlayer(jsonMap.get("name"), new Location(jsonMap.get("location")), new ConnectionData(new NetworkData(jsonMap.get("networkData"))));

        return player;
    }

    public String toJson() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("networkData", connectionData.getNetworkData().toJson());
        jsonMap.put("name", getName());
        jsonMap.put("location", getBoundingBox().getCenter().toJson());

        Gson gson = new Gson();
        return gson.toJson(jsonMap);
    }
}
