package bomberman.gameplay;

import bomberman.gameplay.tile.TileTypes;
import net.java.games.input.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class GameplayManager {

    private GameState gameState = GameState.IN_MENU;

    private final static float POWERUP_TIME = 25;
    private float powerupTimer = POWERUP_TIME;

    private final List<GameMap> maps = new LinkedList<>();
    private final List<Player> players = new LinkedList<>();

    private int mapIndex = 0;

    public GameplayManager() {
        //map 0
        this.addMap(
            GameMap.builder()
                .dimension(15, 13)
                .frame(TileTypes.WALL)
                .fillEmpty(TileTypes.GROUND)
                .horizontalPattern("WGBGBGBGBGBGBGW", 2)
                .horizontalPattern("WGBGBGBGBGBGBGW", 4)
                .horizontalPattern("WGBGBGBGBGBGBGW", 6)
                .horizontalPattern("WGBGBGBGBGBGBGW", 8)
                .horizontalPattern("WGBGBGBGBGBGBGW", 10)
                .startPosition(1, 1)
                .startPosition(13, 1)
                .startPosition(1, 11)
                .startPosition(12, 11)
            .build()
        );

        //map 1
        this.addMap(
            GameMap.builder()
                .dimension(15, 13)
                .frame(TileTypes.WALL)
                .fillEmpty(TileTypes.GROUND)
                .horizontalPattern("WGGBBBBBBBBBGGW", 1)
                .horizontalPattern("WGWBWBWBWBWBWGW", 2)
                .horizontalPattern("WBBBBBBBBBBBBBW", 3)
                .horizontalPattern("WBBBBBBBBBBBBBW", 4)
                .horizontalPattern("WBBBBBGGGBBBBBW", 5)
                .horizontalPattern("WBWBWBWGWBWBWBW", 6)
                .horizontalPattern("WBBBBBGGGBBBBBW", 7)
                .horizontalPattern("WBBBBBBBBBBBBBW", 8)
                .horizontalPattern("WBBBBBBBBBBBBBW", 9)
                .horizontalPattern("WGWBWBWBWBWBWGW", 10)
                .horizontalPattern("WGGBBBBBBBBBGGW", 11)
                .startPosition(1, 1)
                .startPosition(13, 1)
                .startPosition(1, 11)
                .startPosition(12, 11)
            .build()
        );

        //map 2
        this.addMap(
            GameMap.builder()
                .dimension(15, 13)
                .frame(TileTypes.WALL)
                .fillEmpty(TileTypes.GROUND)
                .horizontalPattern("WGPBBBBBBBBBGGW", 3)
                .horizontalPattern("WGPBBBBBBBBBGGW", 4)
                .horizontalPattern("WGPBBGGGGGBBGGW", 5)
                .horizontalPattern("WGPBBGGGGGBBGGW", 6)
                .horizontalPattern("WGPBBGGGGGBBGGW", 7)
                .horizontalPattern("WGPBBBBBBBBBGGW", 8)
                .horizontalPattern("WGPBBBBBBBBBGGW", 9)
                .startPosition(1, 1)
                .startPosition(12, 11)
            .build()

        );

        //@TODO
        this.setMapIndex(1);
        this.addPlayer(new Player(Player.PlayerType.LOCAL, this.getCurrentMap(), "FizzBuzz", this.getCurrentMap().getRandomStartPosition()));

    }

    private void getGameSummary() {
        //--- Most Deadly       -> Player with the most kills.
        //--- Most Professional -> Player with the highest kill to death ratio
        //--- Longest Innings   -> Player with the least deaths.
        //--- Most Cowardly     -> Player with the least kills and least deaths.
        //--- Lemming Award     -> Player with the most suicides.
        //--- Shortest Innings  -> Player with the most deaths.
        //--- Mostly Harmless   -> Player with the least kills.
        //--- Gandhi            -> Player winning without killing.
    }

    public synchronized void addPlayer(Player player) {

        if(this.players.contains(player)) {
            throw new IllegalStateException("Do not add the same instance more than once.");
        }

        this.players.add(player);
        player.setIndex(this.players.indexOf(player));

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
        return this.getMap(this.mapIndex);
    }

    public GameMap getMap(int index) {
        assert index >= 0 && index < this.maps.size();
        return this.maps.get(index);
    }

    public void addMap(GameMap map) {
        assert !(this.maps.contains(map));
        this.maps.add(map);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setMapIndex(int mapIndex) {

        if(!(this.gameState == GameState.IN_MENU)) {
            throw new IllegalStateException();
        }

        this.mapIndex = mapIndex;
    }

    public void update(float delta) {

        if(!(this.gameState == GameState.IN_GAME)) {
            return;
        }

        this.players.forEach(e -> e.update(delta));
        Stream.of(this.getCurrentMap().getTiles()).forEach(e -> Stream.of(e).forEach(t -> t.update(delta)));

        //--- Powerup Spawn Timer
        this.powerupTimer -= delta;
        if (this.powerupTimer <= 0) {
            this.checkPowerups();
            this.powerupTimer = POWERUP_TIME;
        }

    }

    //powerup start
    private void checkPowerups() {
        int x = (int) (Math.random() * this.getCurrentMap().getWidth());
        int y = (int) (Math.random() * this.getCurrentMap().getHeight());
        if (this.getCurrentMap().getTile(x, y).get().getTileType() == TileTypes.GROUND && this.getCurrentMap().getTile(x, y).get().getTileObject() == null) {
            this.getCurrentMap().getTile(x, y).get().spawnPowerup();
        } else {
            this.checkPowerups();
        }
    }
    //powerup end

    public void onKeyDown(int key, char c) {
        if(!(this.gameState == GameState.IN_GAME)) {
            return;
        }
        this.players.forEach(e -> e.keyDown(key, c));
    }

    public void onKeyUp(int key, char c) {
        if(!(this.gameState == GameState.IN_GAME)) {
            return;
        }
        this.players.forEach(e -> e.keyUp(key, c));
    }

    public void onMouseDown(int button, int mouseX, int mouseY) {}

    public void onMouseUp(int button, int mouseX, int mouseY) {}

    public void onGamepadEvent(Component component, float value) {
        // TODO: Implementiert das plz
    }

    public static enum GameState {

        IN_MENU,
        IN_GAME

    }

}
