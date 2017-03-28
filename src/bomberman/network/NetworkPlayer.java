package bomberman.network;

import bomberman.gameplay.GameSession;
import bomberman.gameplay.Player;
import bomberman.gameplay.utils.Location;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class NetworkPlayer extends Player {

    private ConnectionData connectionData;

    public NetworkPlayer(GameSession gameSession, String name, Location center, ConnectionData connectionData) {
        super(gameSession, PlayerType.NETWORK, name, center);

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
}
