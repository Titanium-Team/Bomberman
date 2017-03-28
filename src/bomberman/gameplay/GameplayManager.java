package bomberman.gameplay;


import bomberman.gameplay.tile.TileAbility;
import bomberman.gameplay.tile.TileTypes;
import bomberman.gameplay.utils.Location;
import net.java.games.input.Component;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameplayManager {

    private GameState gameState = GameState.IN_MENU;
    private final List<GameMap> maps = new LinkedList<>();


    private int mapIndex = 0;

    private GameSession currentSession;

    public GameplayManager() {
        //map 0
        this.addMap(
            GameMap.builder()
                .name("Map 0")
                .dimension(15, 13)
                .frame(TileTypes.WALL)
                .fillEmpty(TileTypes.GROUND, TileAbility.NORMAL)
                .horizontalPattern("WGBGBGBGBGBGBGW", TileAbility.NORMAL, 2)
                .horizontalPattern("WGBGBGBGBGBGBGW", TileAbility.NORMAL, 4)
                .horizontalPattern("WGBGBGBGBGBGBGW", TileAbility.NORMAL, 6)
                .horizontalPattern("WGBGBGBGBGBGBGW", TileAbility.NORMAL, 8)
                .horizontalPattern("WGBGBGBGBGBGBGW", TileAbility.NORMAL, 10)
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
                .fillEmpty(TileTypes.GROUND, TileAbility.NORMAL)
                .horizontalPattern("WGGBBBBBBBBBGGW", TileAbility.NORMAL, 1)
                .horizontalPattern("WGWBWBWBWBWBWGW", TileAbility.NORMAL, 2)
                .horizontalPattern("WBBBBBBBBBBBBBW", TileAbility.NORMAL, 3)
                .horizontalPattern("WBBBBBBBBBBBBBW", TileAbility.NORMAL, 4)
                .horizontalPattern("WBBBBBGGGBBBBBW", TileAbility.NORMAL, 5)
                .horizontalPattern("WBWBWBWGWBWBWBW", TileAbility.NORMAL, 6)
                .horizontalPattern("WBBBBBGGGBBBBBW", TileAbility.NORMAL, 7)
                .horizontalPattern("WBBBBBBBBBBBBBW", TileAbility.NORMAL, 8)
                .horizontalPattern("WBBBBBBBBBBBBBW", TileAbility.NORMAL, 9)
                .horizontalPattern("WGWBWBWBWBWBWGW", TileAbility.NORMAL, 10)
                .horizontalPattern("WGGBBBBBBBBBGGW", TileAbility.NORMAL, 11)
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
                .fillEmpty(TileTypes.GROUND, TileAbility.NORMAL)
                .horizontalPattern("WGPBBBBBBBBBGGW", TileAbility.NORMAL, 3)
                .horizontalPattern("WGPBBBBBBBBBGGW", TileAbility.NORMAL, 4)
                .horizontalPattern("WGPBBGGGGGBBGGW", TileAbility.NORMAL, 5)
                .horizontalPattern("WGPBBGGGGGBBGGW", TileAbility.NORMAL, 6)
                .horizontalPattern("WGPBBGGGGGBBGGW", TileAbility.NORMAL, 7)
                .horizontalPattern("WGPBBBBBBBBBGGW", TileAbility.NORMAL, 8)
                .horizontalPattern("WGPBBBBBBBBBGGW", TileAbility.NORMAL, 9)
                //.startPosition(1, 1)
                .startPosition(12, 11)
                .treadmill(new Location(1, 1), Player.FacingDirection.WEST)
                .treadmill(new Location(2, 1), Player.FacingDirection.WEST)
                .treadmill(new Location(3, 1), Player.FacingDirection.WEST)
                .treadmill(new Location(4, 1), Player.FacingDirection.WEST)
                .treadmill(new Location(5, 1), Player.FacingDirection.WEST)
                .treadmill(new Location(6, 1), Player.FacingDirection.WEST)
                .treadmill(new Location(7, 1), Player.FacingDirection.WEST)
                .treadmill(new Location(8, 1), Player.FacingDirection.WEST)
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

    public int getMapIndex() {
        return mapIndex;
    }

    private void createGameSession() {
        this.currentSession = new GameSession(this.getMap(this.mapIndex));
        this.currentSession.addPlayer(new LocalPlayer(this.currentSession, "FizzBuzz", this.currentSession.getGameMap().getRandomStartPosition()));
        //this.currentSession.addAi(); Nur zum Testen der AI. Nicht nutzen, AI funktioniert nicht!
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

        if (component.getIdentifier() == Component.Identifier.Button._0 && value == 1) {
            onKeyDown(Keyboard.KEY_SPACE, ' ');
        }
        if (component.getIdentifier() == Component.Identifier.Axis.X) {
            if (value >= 0.5) {
                onKeyDown(Keyboard.KEY_RIGHT, ' ');
            } else if (value <= -0.5) {
                onKeyDown(Keyboard.KEY_LEFT, ' ');
            } else if(value < 0.5 && value >= -0.5) {
                onKeyUp(Keyboard.KEY_RIGHT, ' ');
                onKeyUp(Keyboard.KEY_LEFT, ' ');
            }
        }

        if (component.getIdentifier() == Component.Identifier.Axis.Y) {
            if (value >= 0.5) {
                onKeyDown(Keyboard.KEY_DOWN, ' ');
            } else if (value <= -0.5) {
                onKeyDown(Keyboard.KEY_UP, ' ');
            }else if(value < 0.5 && value >= -0.5) {
                onKeyUp(Keyboard.KEY_DOWN, ' ');
                onKeyUp(Keyboard.KEY_UP, ' ');
            }
        }

    }

    public enum GameState {

        IN_MENU,
        IN_GAME

    }

}
