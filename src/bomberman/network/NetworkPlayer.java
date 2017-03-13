package bomberman.network;

import bomberman.gameplay.*;
import bomberman.gameplay.utils.Location;

/**
 * Created by Daniel on 05.03.2017.
 */
public class NetworkPlayer extends Player {

	public NetworkPlayer(String name, Location center, GameMap map) {
		//super(x, y, hp, level);
		super(map, name, center);
	}

	//@Override
	public void update(float delta) {

	}
}