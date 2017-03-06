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

    public void onMouseDown(){

    }

    public void onMouseUp(){

    }

    public void onKeyDown(){

    }

    public void onKeyUp(){

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
