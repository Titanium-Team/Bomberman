package bomberman.gameplay;

import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileObject;
import bomberman.gameplay.tile.TileType;
import bomberman.gameplay.tile.TileTypes;
import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.utils.BoundingBox;

public class GameMap {

    private final Tile[][] tiles;

    private final int width;
    private final int height;


    public GameMap(Tile[][] tiles) {

        assert tiles.length > 0 && tiles[0].length > 0;

        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;

    }

    public Tile[][] getTiles() {
        return this.tiles;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Tile get(int x, int y) {

        assert x >= 0 && x < this.width;
        assert y >= 0 && y < this.height;

        return this.tiles[x][y];

    }

    public void spawn(TileObject tileObject, int x, int y) {

        assert x >= 0 && x < this.width;
        assert y >= 0 && y < this.height;

        this.tiles[x][y].spawn(tileObject);

    }

    public boolean checkCollision(Player player) {

        BoundingBox boundingBox = player.getBoundingBox();

        for(int x = (int) boundingBox.getMin().getX(); x < boundingBox.getMax().getX(); x++) {
            for(int y = (int) boundingBox.getMin().getY(); y < boundingBox.getMax().getY(); y++) {

                Tile tile = this.tiles[x][y];

                if(
                    tile.getBoundingBox().intersects(boundingBox) &&
                    (
                        !(tile.getTileType().isWalkable()) ||
                        (tile.getTileObject() instanceof Bomb && ((Bomb) tile.getTileObject()).isTriggered())
                    )
                ) {
                    return true;
                }
            }
        }

        return false;
    }

    public void checkInteraction(Player player) {

        BoundingBox boundingBox = player.getBoundingBox();

        for(int x = (int) boundingBox.getMin().getX(); x < boundingBox.getMax().getX(); x++) {
            for(int y = (int) boundingBox.getMin().getY(); y < boundingBox.getMax().getY(); y++) {

                Tile tile = this.tiles[x][y];

                if(tile.getTileType().isWalkable() && !(tile.getTileObject() == null)) {

                    if(tile.getTileObject() instanceof Bomb && !(((Bomb) tile.getTileObject()).isTriggered())) {
                        continue;
                    }

                    tile.getTileObject().interact(player);
                    tile.destroyObject();
                }
            }
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Tile[][] tiles;

        public Builder() {}

        public int width() {
            return this.tiles.length;
        }

        public int height() {
            return this.tiles[0].length;
        }

        public Builder dimension(int width, int height) {

            assert width > 0 && height > 0;

            this.tiles = new Tile[width][height];
            return this;

        }

        public Builder frame(TileType tileType) {

            assert !(tileType == null);

            this.verticalRow(TileTypes.WALL, 0);
            this.verticalRow(TileTypes.WALL, this.width() - 1);
            this.horizontalRow(TileTypes.WALL, 0);
            this.horizontalRow(TileTypes.WALL, this.height() - 1);

            return this;

        }

        public Builder verticalRow(TileType tileType, int x) {

            assert !(tileType == null);
            assert x >= 0 && x < this.width();

            for (int y = 0; y < this.height(); y++) {
                this.at(tileType, x, y);
            }

            return this;

        }

        public Builder verticalPattern(String pattern, int x) {

            assert !(pattern == null);
            assert ((pattern.length() == this.width()) && !(pattern.isEmpty()));
            assert x >= 0 && x < this.width();

            for (int y = 0; y < this.height(); y++) {
                this.at(Builder.tileTypeByChar(pattern.charAt(y)), x, y);
            }

            return this;

        }

        public Builder horizontalRow(TileType tileType, int y) {

            assert !(tileType == null);
            assert y >= 0 && y < this.height();

            for (int x = 0; x < this.width(); x++) {
                this.at(tileType, x, y);
            }

            return this;

        }

        public Builder horizontalPattern(String pattern, int y) {

            assert !(pattern == null);
            assert ((pattern.length() == this.width()) && !(pattern.isEmpty()));
            assert y >= 0 && y < this.height();

            for (int x = 0; x < this.width(); x++) {
                this.at(Builder.tileTypeByChar(pattern.charAt(x)), x, y);
            }

            return this;

        }

        public Builder fill(TileType tileType, BoundingBox area) {

            assert !(tileType == null);
            assert !(area == null);

            for (int x = (int) area.getMin().getX(); x <= area.getMax().getX(); x++) {
                for (int y = (int) area.getMin().getY(); y <= area.getMax().getY(); y++) {
                    this.at(tileType, x, y);
                }
            }

            return this;

        }

        public Builder fillEmpty(TileType tileType) {

            assert !(tileType == null);

            for (int x = 0; x < this.width(); x++) {
                for (int y = 0; y < this.height(); y++) {
                    if (this.tiles[x][y] == null) {
                        this.at(tileType, x, y);
                    }
                }
            }

            return this;

        }

        public Builder at(TileType tileType, int x, int y) {

            assert !(tileType == null);

            this.tiles[x][y] = new Tile(tileType, new BoundingBox(x, y, x + .9999D, y + .9999D));
            return this;

        }

        public GameMap build() {
            return new GameMap(this.tiles);
        }

        private static TileType tileTypeByChar(char c) {
            switch (c) {

                case 'G':
                    return TileTypes.GROUND;
                case 'W':
                    return TileTypes.WALL;
                case 'B':
                    return TileTypes.WALL_BREAKABLE;

                default:
                    throw new IllegalArgumentException("Unknown pattern char. Allowed: G (Ground), W (Wall) " +
                            "B (WALL_BREAKABLE).");

            }
        }

    }

}
