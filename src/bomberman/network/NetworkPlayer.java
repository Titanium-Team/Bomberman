package bomberman.network;

import bomberman.gameplay.GameMap;
import bomberman.gameplay.Player;
import bomberman.gameplay.utils.Location;

/**
 * Created by Daniel on 05.03.2017.
 */
public class NetworkPlayer extends Player {

    public NetworkPlayer(String name, Location center, GameMap map) {
        //super(x, y, hp, level);
        super(PlayerType.NETWORK, map, name, center);
    }

    //@Override
    public void update(float delta) {

    }
}