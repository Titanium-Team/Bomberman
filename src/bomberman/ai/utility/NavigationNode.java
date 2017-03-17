package bomberman.ai.utility;

import bomberman.gameplay.tile.Tile;

/**
 * Created by Daniel on 15.03.2017.
 */
public class NavigationNode {
    private Tile tile;
    private boolean marked;
    private int dist;
    private int prevX, prevY;

    public NavigationNode(Tile tile) {
        this.tile = tile;
        marked = false;
        dist = Integer.MAX_VALUE;
        prevX = -1;
        prevY = -1;
    }

    public Tile getTile() {
        return tile;
    }

    public boolean isMarked() {
        return marked;
    }

    public int getDist() {
        return dist;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public int getPrevX() {
        return prevX;
    }

    public int getPrevY() {
        return prevY;
    }

    public void setPrevX(int prevX) {
        this.prevX = prevX;
    }

    public void setPrevY(int prevY) {
        this.prevY = prevY;
    }
}
