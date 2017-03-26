package bomberman.gameplay.tile;

public interface TileType {

    boolean isWalkable();

    double getHealth();

    default boolean isDestroyable() {
        return !(this.getHealth() == Double.NEGATIVE_INFINITY);
    }

}
