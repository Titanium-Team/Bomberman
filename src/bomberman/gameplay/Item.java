package bomberman.gameplay;

/**
 * Created by Daniel on 05.03.2017.
 */
public class Item  <type>{
    private type type;

    public Item(type type) {
        if(type instanceof PowerUpType || type instanceof BombType) {
            this.type = type;
        }
    }

    public type getType() {
        return type;
    }
}
