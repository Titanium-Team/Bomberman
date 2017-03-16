package bomberman.gameplay;

import bomberman.gameplay.tile.TileTypes;
import bomberman.gameplay.utils.Location;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class GameplayManager {

    private final List<GameMap> maps = new LinkedList<>();
    private final List<Player> players = new LinkedList<>();

    public GameplayManager() {

        this.add(
            GameMap.builder()
                .dimension(15, 13)
                .frame(TileTypes.WALL)
                .fillEmpty(TileTypes.GROUND)
                .horizontalPattern("WGBGBGBGBGBGBGW", 2)
                .horizontalPattern("WGBGBGBGBGBGBGW", 4)
                .horizontalPattern("WGBGBGBGBGBGBGW", 6)
                .horizontalPattern("WGBGBGBGBGBGBGW", 8)
                .horizontalPattern("WGBGBGBGBGBGBGW", 10)
            .build()
        );

        this.add(
            GameMap.builder()
                .dimension(11, 9)
                .frame(TileTypes.WALL)
                .fillEmpty(TileTypes.GROUND)
            .build()
        );

        //@TODO
        this.players.add(new Player(Player.PlayerType.LOCAL, this.getMap(0), "FizzBuzz", new Location(1.5, 1.5)));

    }

    public void add(Player player) {
        this.players.add(player);
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public Player getLocalPlayer() {
        return this.players.stream().filter(e -> e.getPlayerType() == Player.PlayerType.LOCAL).findAny().orElseGet(null);
    }

    public Player getPlayer(int index) {
        return this.players.get(index);
    }

    public GameMap getCurrentMap() {
        return this.getMap(0); //@TODO
    }

    public GameMap getMap(int index) {

        assert index >= 0 && index < this.maps.size();
        return this.maps.get(index);

    }

    public void add(GameMap map) {

        assert !(this.maps.contains(map));
        this.maps.add(map);

    }

    public void update(float delta) {
        this.players.forEach(e -> e.update(delta));
        Stream.of(this.getCurrentMap().getTiles()).forEach(e -> Stream.of(e).forEach(t -> t.update(delta)));
    }

    public void onKeyDown(int key, char c) {
        this.players.forEach(e -> e.keyDown(key, c));
    }

    public void onKeyUp(int key, char c) {
        this.players.forEach(e -> e.keyUp(key, c));
    }

    public void onMouseDown(int button, int mouseX, int mouseY) {
    }

    public void onMouseUp(int button, int mouseX, int mouseY) {
    }

}
