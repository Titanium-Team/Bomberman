package bomberman.gameplay.properties;

public enum PropertyTypes implements PropertyType {

    SPEED_FACTOR {
        @Override
        public Float defaultValue() {
            return 1F;
        }

    },
    BOMB_BLAST_RADIUS {
        @Override
        public Integer defaultValue() {
            return 1;
        }
    },
    BOMB_AMOUNT {
        @Override
        public Integer defaultValue() {
            return 1;
        }

    }

}
