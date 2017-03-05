package bomberman.gameplay;

import java.util.ArrayList;

/**
 * Created by Daniel on 05.03.2017.
 */
public abstract class Player {
    protected float x,y;
    protected int hp;
    protected ArrayList<Item> items;
    protected Level level;

    protected boolean placeBomb(){
        return false;
    }

    protected boolean move(float x,float y){
        return false;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getHp() {
        return hp;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void collectItem(Item item){

    }

    public Player(float x, float y, int hp, Level level) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.level = level;
    }

    public abstract void update(float delta);
}
