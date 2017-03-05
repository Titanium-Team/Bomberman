package bomberman.gameplay;

/**
 * Created by Daniel on 05.03.2017.
 */
public class Tile {
    private Item item;

    public Tile(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public Item collectItem(){
        if(item != null) {
            Item item = this.item;
            this.item = null;
            return item;
        }else{
            return null;
        }
    }
}
