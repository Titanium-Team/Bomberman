package bomberman.gameplay;

import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileObject;
import bomberman.gameplay.tile.TileType;
import bomberman.gameplay.tile.TileTypes;
import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.tile.objects.PowerUp;
import bomberman.gameplay.utils.BoundingBox;
import bomberman.gameplay.utils.Location;

import java.util.*;

public class GameMap implements Cloneable {

    private final static Random random = new Random();

    private final Tile[][] tiles;
    private final List<Location> startPositions;
    private final Map<Player, Player.Direction> lastDirection = new HashMap<>();

    private final int width;
    private final int height;


    public GameMap(Tile[][] tiles, List<Location> startPositions) {

        assert !(startPositions == null);
        assert (startPositions.size() > 1);
        assert tiles.length > 0 && tiles[0].length > 0;

        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.startPositions = startPositions;

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

    public Location getRandomStartPosition() {
        return this.startPositions.remove(GameMap.random.nextInt(this.startPositions.size()));
    }

    public Optional<Tile> getTile(int x, int y) {

        if(x >= 0 && x < this.width && y >= 0 && y < this.height) {
            return Optional.ofNullable(this.tiles[x][y]);
        }

        return Optional.empty();

    }

    public Optional<Tile> getMin() {
        return this.getTile(0, 0);
    }

    public Optional<Tile> getMax() {
        return this.getTile(this.width - 1, this.height - 1);
    }

    public void spawn(TileObject tileObject, int x, int y) {

        assert x >= 0 && x < this.width;
        assert y >= 0 && y < this.height;

        this.tiles[x][y].spawn(tileObject);

    }

    public Player.Direction checkCollision(Player player) {

        BoundingBox playerBox = player.getBoundingBox();

        for (int x = (int) playerBox.getMin().getX(); x < playerBox.getMax().getX(); x++) {
            for (int y = (int) playerBox.getMin().getY(); y < playerBox.getMax().getY(); y++) {

                Tile tile = this.getTile(x, y).get();

                if (!(tile.canVisit(player))) {

                    int pX = (int) playerBox.getCenter().getX();
                    int pY = (int) playerBox.getCenter().getY();

                    Player.FacingDirection facingDirection = player.getFacingDirection();
                    switch (facingDirection) {

                        case NORTH: return Player.Direction.UP;
                        case SOUTH: return Player.Direction.DOWN;
                        case EAST: return Player.Direction.LEFT;
                        case WEST: return Player.Direction.RIGHT;

                        case SOUTH_EAST:
                        case SOUTH_WEST:
                        case NORTH_WEST:
                        case NORTH_EAST: {

                            Optional<Tile> up = this.getTile(pX, pY - 1);
                            Optional<Tile> down = this.getTile(pX, pY + 1);
                            Optional<Tile> left = this.getTile(pX - 1, pY);
                            Optional<Tile> right = this.getTile(pX + 1, pY);

                            if(!(up.isPresent())) return Player.Direction.UP;
                            else if (!(down.isPresent())) return Player.Direction.DOWN;
                            else if(!(left.isPresent())) return Player.Direction.LEFT;
                            else if(!(right.isPresent())) return Player.Direction.RIGHT;

                            Player.Direction last = this.lastDirection.get(player);

                            switch (facingDirection) {

                                case NORTH_EAST: {
                                    if(last == Player.Direction.UP && player.getDirection() == Player.Direction.RIGHT) {
                                        if (up.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.UP);
                                            return Player.Direction.RIGHT;
                                        } else if (right.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.RIGHT);
                                            return Player.Direction.UP;
                                        }
                                    } else {
                                        if (right.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.RIGHT);
                                            return Player.Direction.UP;
                                        } else if (up.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.UP);
                                            return Player.Direction.RIGHT;
                                        }
                                    }

                                }
                                break;

                                case SOUTH_EAST: {
                                    if(last == Player.Direction.DOWN && player.getDirection() == Player.Direction.RIGHT) {
                                        if (down.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.DOWN);
                                            return Player.Direction.RIGHT;
                                        } else if (right.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.RIGHT);
                                            return Player.Direction.DOWN;
                                        }
                                    } else {
                                        if (right.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.RIGHT);
                                            return Player.Direction.DOWN;
                                        } else if (down.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.DOWN);
                                            return Player.Direction.RIGHT;
                                        }
                                    }
                                }
                                break;

                                case NORTH_WEST: {
                                    if(last == Player.Direction.UP && player.getDirection() == Player.Direction.LEFT) {
                                        if (up.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.UP);
                                            return Player.Direction.LEFT;
                                        } else if (left.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.LEFT);
                                            return Player.Direction.UP;
                                        }
                                    } else {
                                        if (left.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.LEFT);
                                            return Player.Direction.UP;
                                        } else if (up.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.UP);
                                            return Player.Direction.LEFT;
                                        }
                                    }
                                }
                                break;

                                case SOUTH_WEST: {
                                    if(last == Player.Direction.DOWN && player.getDirection() == Player.Direction.LEFT) {
                                        if (down.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.DOWN);
                                            return Player.Direction.LEFT;
                                        } else if (left.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.LEFT);
                                            return Player.Direction.DOWN;
                                        }
                                    } else {
                                        if (left.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.LEFT);
                                            return Player.Direction.DOWN;
                                        } else if (down.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.DOWN);
                                            return Player.Direction.LEFT;
                                        }
                                    }
                                }
                                break;

                            }

                            return Player.Direction.STOP_VERTICAL_MOVEMENT;
                        }

                    }

                }

            }
        }

        return Player.Direction.STOP_HORIZONTAL_MOVEMENT;

    }

    public void checkInteraction(Player player) {

        BoundingBox boundingBox = player.getBoundingBox();

        for (int x = (int) boundingBox.getMin().getX(); x < boundingBox.getMax().getX(); x++) {
            for (int y = (int) boundingBox.getMin().getY(); y < boundingBox.getMax().getY(); y++) {

                Tile tile = this.tiles[x][y];

                if (tile.getTileType().isWalkable() && !(tile.getTileObject() == null)) {

                    if (tile.getTileObject() instanceof Bomb) {
                        continue;
                    }

                    tile.getTileObject().interact(player);
                    if (tile.getTileObject() instanceof PowerUp) {
                        tile.destroyObject();
                    }
                }
            }
        }

    }

    @Override
    public GameMap clone() {
        return new GameMap(this.tiles.clone(), this.startPositions);
    }

    private static int range(int min, int value, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Tile[][] tiles;

        private List<Location> startPositions = new LinkedList<>();

        public Builder() {
        }

        public int width() {
            return this.tiles.length;
        }

        public int height() {
            return this.tiles[0].length;
        }

        public Builder startPosition(int x, int y) {

            Tile tile = this.tiles[x][y];

            if (!(tile.getTileType().isWalkable())) {
                throw new IllegalArgumentException();
            }

            this.startPositions.add(tile.getBoundingBox().getCenter());
            return this;

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

                //wenn aktuelles feld 'P' ist, dann erzeuge ein zufälliges powerup
                if (pattern.charAt(x) == 'P') {
                    this.tiles[x][y].spawnPowerup();
                }
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
            return new GameMap(this.tiles, this.startPositions);
        }

        private static TileType tileTypeByChar(char c) {
            switch (c) {

                case 'G':
                case 'P':
                    return TileTypes.GROUND;
                case 'W':
                    return TileTypes.WALL;
                case 'B':
                    return TileTypes.WALL_BREAKABLE;

                default:
                    throw new IllegalArgumentException("Unknown pattern char. Allowed: G (Ground), W (Wall), P (PowerUp) " +
                            "B (WALL_BREAKABLE).");

            }
        }

    }

}
