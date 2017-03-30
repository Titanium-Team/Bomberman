package bomberman.gameplay;

import bomberman.Main;
import bomberman.gameplay.tile.*;
import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.tile.objects.PowerUp;
import bomberman.gameplay.utils.BoundingBox;
import bomberman.gameplay.utils.Location;


import java.util.*;
import java.util.List;

public class GameMap implements Cloneable {

    private final static Random random = new Random();

    private final String name;
    private final String thumbnailKey;


    private final Tile[][] tiles;
    private final List<Location> startPositions;

    private final int width;
    private final int height;


    public GameMap(String name, String thumbnailKey, Tile[][] tiles, List<Location> startPositions) {

        assert !(name == null);
        assert !(startPositions == null);
        //assert (startPositions.size() > 1);
        assert tiles.length > 0 && tiles[0].length > 0;

        this.name = name;
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.startPositions = startPositions;
        this.thumbnailKey = thumbnailKey;

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

    public String getName() {
        return name;
    }

    public String getThumbnailKey() {
        return thumbnailKey;
    }

    public List<Location> getStartPositions() {
        return this.startPositions;
    }

    public Location getRandomStartPosition() {

        if(this.startPositions.isEmpty()) {
            return null;
        }

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

    public void spawn(TileObject tileObject) {
        Location center = tileObject.getParent().getBoundingBox().getCenter();
        this.tiles[(int) center.getX()][(int) center.getY()].spawn(tileObject);
    }

    public Player.Direction checkCollision(LocalPlayer player) {

        BoundingBox playerBox = player.getBoundingBox();

        for (int x = (int) playerBox.getMin().getX(); x < playerBox.getMax().getX(); x++) {
            for (int y = (int) playerBox.getMin().getY(); y < playerBox.getMax().getY(); y++) {

                Optional<Tile> optional = this.getTile(x, y);

                if(!(optional.isPresent())) {
                    continue;
                }

                Tile tile = optional.get();

                if (!(tile.canVisit(player))) {

                    Player.FacingDirection facingDirection = player.getFacingDirection();
                    switch (facingDirection) {

                        case NORTH: {
                            return Player.Direction.UP;
                        }

                        case SOUTH: {
                            return Player.Direction.DOWN;
                        }

                        case EAST: {
                            return Player.Direction.RIGHT;
                        }

                        case WEST: {
                            return Player.Direction.LEFT;
                        }

                        /*
                        FOR LEGACY SAKE
                        case SOUTH_EAST:
                        case SOUTH_WEST:
                        case NORTH_WEST:
                        case NORTH_EAST: {

                            Optional<Tile> up = this.getTile(pX, pY - 1);
                            Optional<Tile> down = this.getTile(pX, pY + 1);
                            Optional<Tile> left = this.getTile(pX - 1, pY);
                            Optional<Tile> right = this.getTile(pX + 1, pY);

                            if (!(up.isPresent())) return Player.Direction.UP;
                            else if (!(down.isPresent())) return Player.Direction.DOWN;
                            else if (!(left.isPresent())) return Player.Direction.LEFT;
                            else if (!(right.isPresent())) return Player.Direction.RIGHT;

                            Player.Direction last = this.lastDirection.get(player);

                            switch (facingDirection) {

                                case NORTH_EAST: {
                                    Location loc = player.getTile().getBoundingBox().getCenter();
                                    Optional<Tile> diagonalTile = this.getTile((int) (loc.getX() + 1), (int) (loc.getY() - 1));
                                    Optional<Tile> diagonalTile2 = this.getTile((int) (loc.getX() - 1), (int) (loc.getY() - 1));
                                    if (last == Player.Direction.UP && player.getDirection()[0] == Player.Direction.RIGHT) {
                                        if (up.get().canVisit(player) || !diagonalTile.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.UP);
                                            return Player.Direction.RIGHT;
                                        } else if (right.get().canVisit(player) || !diagonalTile2.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.RIGHT);
                                            return Player.Direction.UP;
                                        }
                                    } else {
                                        if (right.get().canVisit(player) || !diagonalTile.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.RIGHT);
                                            return Player.Direction.UP;
                                        } else if (up.get().canVisit(player) || !diagonalTile2.get().canVisit(player)) {
                                            this.lastDirection.put(player, Player.Direction.UP);
                                            return Player.Direction.RIGHT;
                                        }
                                    }
                                }
                                break;

                                case SOUTH_EAST: {
                                    if (last == Player.Direction.DOWN && player.getDirection()[0] == Player.Direction.RIGHT) {
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
                                    if (last == Player.Direction.UP && player.getDirection()[0] == Player.Direction.LEFT) {
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
                                    if (last == Player.Direction.DOWN && player.getDirection()[0] == Player.Direction.LEFT) {
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
                        }
                        */

                        default: return Player.Direction.STOP_VERTICAL_MOVEMENT;
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

        player.getTile().interact(player);

    }

    @Override
    public GameMap clone() {
        return new GameMap(this.name, this.thumbnailKey, this.tiles.clone(), new ArrayList<>(this.startPositions));
    }

    private static int range(int min, int value, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private String thumbnailKey;

        private Tile[][] tiles;
        private List<Location> startPositions = new LinkedList<>();

        public Builder() {}

        public int width() {
            return this.tiles.length;
        }

        public int height() {
            return this.tiles[0].length;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
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

            this.verticalRow(TileTypes.WALL, TileAbility.NORMAL, 0);
            this.verticalRow(TileTypes.WALL, TileAbility.NORMAL, this.width() - 1);
            this.horizontalRow(TileTypes.WALL, TileAbility.NORMAL, 0);
            this.horizontalRow(TileTypes.WALL, TileAbility.NORMAL, this.height() - 1);

            return this;

        }

        public Builder verticalRow(TileType tileType, int x) {
            return this.verticalRow(tileType, TileAbility.NORMAL, x);
        }

        public Builder verticalRow(TileType tileType, TileAbility tileAbility, int x) {

            assert !(tileType == null);
            assert !(tileAbility == null);
            assert x >= 0 && x < this.width();

            for (int y = 0; y < this.height(); y++) {
                this.at(tileType, tileAbility, x, y);
            }

            return this;

        }

        public Builder verticalPattern(String pattern, int x) {
            return this.verticalPattern(pattern, TileAbility.NORMAL, x);
        }

        public Builder verticalPattern(String pattern, TileAbility tileAbility, int x) {

            assert !(pattern == null);
            assert ((pattern.length() == this.width()) && !(pattern.isEmpty()));
            assert x >= 0 && x < this.width();

            for (int y = 0; y < this.height(); y++) {
                this.at(Builder.tileTypeByChar(pattern.charAt(y)), tileAbility, x, y);
            }

            return this;

        }

        public Builder horizontalRow(TileType tileType, int y) {
            return this.horizontalRow(tileType, TileAbility.NORMAL, y);
        }

        public Builder horizontalRow(TileType tileType, TileAbility tileAbility, int y) {

            assert !(tileType == null);
            assert y >= 0 && y < this.height();

            for (int x = 0; x < this.width(); x++) {
                this.at(tileType, tileAbility, x, y);
            }

            return this;

        }

        public Builder horizontalPattern(String pattern, int y) {
            return this.horizontalPattern(pattern, TileAbility.NORMAL, y);
        }

        public Builder horizontalPattern(String pattern, TileAbility tileAbility, int y) {

            assert !(pattern == null);
            assert ((pattern.length() == this.width()) && !(pattern.isEmpty()));
            assert y >= 0 && y < this.height();

            for (int x = 0; x < this.width(); x++) {
                this.at(Builder.tileTypeByChar(pattern.charAt(x)), tileAbility, x, y);

                //wenn aktuelles feld 'P' ist, dann erzeuge ein zufälliges powerup
                if (pattern.charAt(x) == 'P') {
                    this.tiles[x][y].spawnPowerup(120);
                }
            }

            return this;

        }

        public Builder fill(TileType tileType, BoundingBox area) {
            return this.fill(tileType, TileAbility.NORMAL, area);
        }

        public Builder fill(TileType tileType, TileAbility tileAbility, BoundingBox area) {

            assert !(tileType == null);
            assert !(area == null);

            for (int x = (int) area.getMin().getX(); x <= area.getMax().getX(); x++) {
                for (int y = (int) area.getMin().getY(); y <= area.getMax().getY(); y++) {
                    this.at(tileType, tileAbility, x, y);
                }
            }

            return this;

        }

        public Builder fillEmpty(TileType tileType) {
            return this.fillEmpty(tileType, TileAbility.NORMAL);
        }

        public Builder fillEmpty(TileType tileType, TileAbility tileAbility) {

            assert !(tileType == null);

            for (int x = 0; x < this.width(); x++) {
                for (int y = 0; y < this.height(); y++) {
                    if (this.tiles[x][y] == null) {
                        this.at(tileType, tileAbility, x, y);
                    }
                }
            }

            return this;

        }

        public Builder at(TileType tileType, int x, int y) {
            return this.at(tileType, TileAbility.NORMAL, x, y);
        }

        public Builder at(TileType tileType, TileAbility tileAbility, int x, int y) {

            assert !(tileType == null);
            assert !(tileAbility == null);

            this.tiles[x][y] = new Tile(tileType, tileAbility, new BoundingBox(x, y, x + .9999D, y + .9999D));
            return this;

        }

        public Builder teleporter(Location from, Location to) {

            assert !(from == null);
            assert !(to == null);

            Tile fromTile = this.tiles[(int) from.getX()][(int) from.getY()];
            Tile toTile = this.tiles[(int) to.getX()][(int) to.getY()];

            assert !(toTile.getTileAbility() == TileAbility.TELEPORT || toTile.getTileAbility() == TileAbility.RANDOM_TELEPORT);

            fromTile.setTileAbility(TileAbility.TELEPORT);
            fromTile.setConnectedTile(toTile);

            return this;
        }

        public Builder treadmill(Location location, Player.FacingDirection facingDirection) {

            assert !(location == null);
            assert !(facingDirection == null);

            Tile tile = this.tiles[(int) location.getX()][(int) location.getY()];

            tile.setTileAbility(TileAbility.TREADMILL);
            tile.setTreadMillDirection(facingDirection);

            return this;

        }

        public Builder thumbnail(String thumbnailKey) {
            this.thumbnailKey = thumbnailKey;
            return this;
        }

        public GameMap build() {
            return new GameMap(this.name, this.thumbnailKey, this.tiles, this.startPositions);
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