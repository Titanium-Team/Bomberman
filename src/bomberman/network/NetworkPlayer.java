package bomberman.network;

import bomberman.gameplay.GameMap;
import bomberman.gameplay.Player;
import bomberman.gameplay.utils.Location;

import java.net.InetAddress;

public class NetworkPlayer extends Player {
  
   private ConnectionData connectionData;

    public NetworkPlayer(String name, Location center, GameMap map, ConnectionData connectionData) {
        //super(x, y, hp, level);
   private ConnectionData connectionData;
      
      
        super(PlayerType.NETWORK, map, name, center);

    }

    //@Override
    public void update(float delta) {

    }


    public ConnectionData getConnectionData() {
        return connectionData;
    }
}

}
