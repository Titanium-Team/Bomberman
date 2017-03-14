package bomberman.gameplay;

import bomberman.gameplay.tile.TileTypes;
import bomberman.gameplay.utils.Location;

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

        //@TODO
        this.players.add(new Player(Player.PlayerType.LOCAL, this.getMap(0), "FizzBuzz", new Location(3, 3)));

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

    public GameMap getMap(int index) {

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
