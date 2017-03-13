package bomberman.gameplay;

import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileObject;
import bomberman.gameplay.tile.TileType;
import bomberman.gameplay.tile.TileTypes;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Tile[][] tiles;

        public Builder() {
        }

        public Builder dimension(int width, int height) {
            this.tiles = new Tile[width][height];
            /*for(int x = 0; x < width; x++) {
                for(int y = 0; y < height; y++) {
                    this.tiles[x][y] = new Tile(TileTypes.GROUND);
                }
            }*/
            return this;
        }

        public Builder verticalRow(TileType tileType, int y) {

            assert !(tileType == null);
            assert y >= 0 && y < this.tiles[0].length;

            for (int x = 0; x < this.tiles[0].length; x++) {
                this.tiles[x][y] = new Tile(tileType);
            }

            return this;

        }

        public Builder horizontalRow(TileType tileType, int x) {

            assert !(tileType == null);
            assert x >= 0 && x < this.tiles.length;

            for (int y = 0; y < this.tiles.length; y++) {
                this.tiles[x][y] = new Tile(tileType);
            }

            return this;

        }

        public Builder fill(TileType tileType, BoundingBox area) {

            assert !(tileType == null);
            assert !(area == null);

            for (int x = (int) area.getMin().getX(); x <= area.getMax().getX(); x++) {
                for (int y = (int) area.getMin().getY(); y <= area.getMax().getY(); y++) {
                    this.tiles[x][y] = new Tile(tileType);
                }
            }

            return this;

        }

        public Builder at(TileType tileType, int x, int y) {

            assert !(tileType == null);

            this.tiles[x][y] = new Tile(tileType);
            return this;

        }

        public GameMap build() {
            return new GameMap(this.tiles);
        }


    }

}
