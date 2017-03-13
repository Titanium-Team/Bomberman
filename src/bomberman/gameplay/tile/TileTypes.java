package bomberman.gameplay.tile;

public enum TileTypes implements TileType {

    GROUND {

        @Override
        public boolean isWalkable() {
            return true;
        }

        @Override
        public double getHealth() {
            return Double.NEGATIVE_INFINITY;
        }

    },
    WALL {

        @Override
        public boolean isWalkable() {
            return false;
        }

        @Override
        public double getHealth() {
            return Double.POSITIVE_INFINITY;
        }

    },
    WALL_BREAKABLE {

        @Override
        public boolean isWalkable() {
            return false;
        }

        @Override
        public double getHealth() {
            return 1;
        }

    };

}
