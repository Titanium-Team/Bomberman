package bomberman.network;

import bomberman.Main;
import bomberman.gameplay.GameMap;
import bomberman.gameplay.GameSession;
import bomberman.gameplay.Player;
import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.utils.Location;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class NetworkPlayer extends Player {

    private ConnectionData connectionData;
    private FacingDirection facingDirection = FacingDirection.NORTH;

    public NetworkPlayer(String name, Location center, ConnectionData connectionData) {
        super(Main.instance.getGameplayManager().getCurrentSession(), PlayerType.NETWORK, name, center);

        this.connectionData = connectionData;
    }

    public void setFacingDirection(FacingDirection facingDirection) {
        this.facingDirection = facingDirection;
    }

    @Override
    public FacingDirection getFacingDirection() {
        return facingDirection;
    }

    @Override
    public void update(float delta) {
        Main.instance.getGameplayManager().getCurrentSession().getGameMap().checkInteraction(this);

        //--- Update INVINCIBILITY Timer
        this.getPropertyRepository().setValue(
                PropertyTypes.INVINCIBILITY,
                this.getPropertyRepository().getValue(PropertyTypes.INVINCIBILITY) - delta
        );
    }

    public ConnectionData getConnectionData() {
        return connectionData;
    }

    public static NetworkPlayer fromJson(String jsonSting){
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();

        Map<String, String> jsonMap = gson.fromJson(jsonSting, type);

        NetworkPlayer player = new NetworkPlayer(jsonMap.get("name"), new Location(jsonMap.get("location")), new ConnectionData(new NetworkData(jsonMap.get("networkData"))));
        player.setIndex(Integer.parseInt(jsonMap.get("index")));

        return player;
    }

    public String toJson() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("networkData", connectionData.getNetworkData().toJson());
        jsonMap.put("name", getName());
        jsonMap.put("location", getBoundingBox().getCenter().toJson());
        jsonMap.put("index", String.valueOf(getIndex()));

        Gson gson = new Gson();
        return gson.toJson(jsonMap);
    }
}
