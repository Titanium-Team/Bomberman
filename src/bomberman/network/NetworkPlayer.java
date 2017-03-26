package bomberman.network;

import bomberman.gameplay.GameMap;
import bomberman.gameplay.GameSession;
import bomberman.gameplay.Player;
import bomberman.gameplay.utils.Location;


public class NetworkPlayer extends Player {

    private ConnectionData connectionData;

    public NetworkPlayer(GameSession gameSession, String name, Location center, ConnectionData connectionData) {
        super(gameSession, PlayerType.NETWORK, name, center);

        this.connectionData = connectionData;
    }

    //@Override
    public void update(float delta) {

    }

    public ConnectionData getConnectionData() {
        return connectionData;
    }
}
