package bomberman.gameplay;

import java.util.ArrayList;

/**
 * Created by Daniel on 05.03.2017.
 */
public class Level {
    private Tile[][] map;
    private ArrayList<Player> players;

    public void update(float delta){

    }

    public void onMouseDown(int button, int mouseX, int mouseY){

    }

    public void onMouseUp(int button, int mouseX, int mouseY){

    }

    public void onKeyDown(int key, char c){

    }

    public void onKeyUp(int key, char c){

    }

    public Tile[][] getMap() {
        return map;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Tile getTile(int x,int y){
        if(x < map.length && y < map[x].length) {
            return map[x][y];
        }else{
            return null;
        }
    }
}
