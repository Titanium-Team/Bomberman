package bomberman.gameplay;

import java.util.LinkedList;
import java.util.List;

public class GameplayManager {

    private final List<GameMap> maps = new LinkedList<>();
    private final List<Player> players = new LinkedList<>();

    public GameplayManager() {

        this.add(
            GameMap.builder()
                .dimension(11, 11)
                .verticalRow(TileTypes.WALL, 0)
                .horizontalRow(TileTypes.WALL, 0)
                .horizontalRow(TileTypes.WALL, 10)
                .verticalRow(TileTypes.WALL, 10)
                .fill(TileTypes.GROUND, new BoundingBox(1, 1, 9, 9))
            .build()
        );

    }

    public void add(Player player) {
        this.players.add(player);
    }

    public GameMap get(int index) {

        assert index > 0 && index < this.maps.size();
        return this.maps.get(index);

    }

    public void add(GameMap map) {

        assert !(this.maps.contains(map));
        this.maps.add(map);

    }

}
