package bomberman.gameplay;

import bomberman.gameplay.tile.TileTypes;
import net.java.games.input.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class GameplayManager {

    private GameState gameState = GameState.IN_MENU;
    private final List<GameMap> maps = new LinkedList<>();

    private int mapIndex = 0;

    private GameSession currentSession;

    public GameplayManager() {
        //map 0
        this.addMap(
            GameMap.builder()
                .name("Mao 0")
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
            .build().clone()
        );

        //map 1
        this.addMap(
            GameMap.builder()
                .name("Map 1")
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
                .name("Map 2")
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

        this.currentSession = new GameSession(this.getMap(this.mapIndex).clone());

        //@TODO
        this.setMapIndex(0);
        this.createGameSession();
    }

    public GameSession getCurrentSession() {
        return this.currentSession;
    }

    public GameMap getMap(int index) {
        assert index >= 0 && index < this.maps.size();
        return this.maps.get(index).clone();
    }

    public int getMapCount() {
        return maps.size();
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
        this.createGameSession();
    }

    private void createGameSession() {
        this.currentSession = new GameSession(this.getMap(this.mapIndex));
        this.currentSession.addPlayer(new Player(this.currentSession, Player.PlayerType.LOCAL, "FizzBuzz", this.currentSession.getGameMap().getRandomStartPosition()));
    }

    public void update(float delta) {

        if(!(this.gameState == GameState.IN_GAME)) {
            return;
        }

        this.currentSession.update(delta);

    }

    public void onKeyDown(int key, char c) {
        if(!(this.gameState == GameState.IN_GAME)) {
            return;
        }

        this.currentSession.onKeyDown(key, c);
    }

    public void onKeyUp(int key, char c) {
        if(!(this.gameState == GameState.IN_GAME)) {
            return;
        }

        this.currentSession.onKeyUp(key, c);
    }

    public void onMouseDown(int button, int mouseX, int mouseY) {
        if(!(this.gameState == GameState.IN_GAME)) {
            return;
        }

        this.currentSession.onMouseDown(button, mouseX, mouseY);
    }

    public void onMouseUp(int button, int mouseX, int mouseY) {
        if(!(this.gameState == GameState.IN_GAME)) {
            return;
        }

        this.currentSession.onMouseUp(button, mouseX, mouseY);
    }

    public void onGamepadEvent(Component component, float value) {
        // TODO: Implementiert das plz
        System.out.println(value);
    }

    public static enum GameState {

        IN_MENU,
        IN_GAME

    }

}
