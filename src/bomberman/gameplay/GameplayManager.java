package bomberman.gameplay;

import bomberman.gameplay.statistic.Statistics;
import bomberman.gameplay.tile.TileTypes;
import bomberman.gameplay.utils.BoundingBox;

import java.util.LinkedList;
import java.util.List;

public class GameplayManager {

	private final List<GameMap> maps = new LinkedList<>();
	private final List<Player> players = new LinkedList<>();

	public GameplayManager() {

		this.add(
				GameMap.builder()
						.dimension(16, 12)
						.frame(TileTypes.WALL)
						.fillEmpty(TileTypes.GROUND)
						.build()
		);

	}

	public void add(Player player) {
		this.players.add(player);
	}

	public GameMap get(int index) {

		assert index >= 0 && index < this.maps.size();
		return this.maps.get(index);

	}

	public void add(GameMap map) {

		assert !(this.maps.contains(map));
		this.maps.add(map);

	}

	public void onKeyDown(int key, char c) {
	}

	public void onKeyUp(int key, char c) {
	}

	public void onMouseDown(int button, int mouseX, int mouseY) {
	}

	public void onMouseUp(int button, int mouseX, int mouseY) {
	}

}
