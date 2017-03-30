package bomberman.gameplay.properties;

public enum PropertyTypes implements PropertyType {

    SPEED_FACTOR {

        @Override
        public float defaultValue() {
            return 1F;
        }

        @Override
        public float maxValue() {
            return 1.5F;
        }

        @Override
        public float minValue() {
            return 0.8F;
        }

    },
    BOMB_BLAST_RADIUS {
        @Override
        public float defaultValue() {
            return 1;
        }

        @Override
        public float maxValue() {
            return 5;
        }

        @Override
        public float minValue() {
            return 1;
        }
    },
    BOMB_AMOUNT {
        @Override
        public float defaultValue() {
            return 1F;
        }

        @Override
        public float maxValue() {
            return 3;
        }

        @Override
        public float minValue() {
            return 0;
        }

    },

    HEALTH {

        @Override
        public float defaultValue() {
            return 3;
        }

        @Override
        public float maxValue() {
            return 5;
        }

        @Override
        public float minValue() {
            return 0;
        }

    },

    INVINCIBILITY{
        @Override
        public float defaultValue() {
            return 0F;
        }

        @Override
        public float maxValue() {
            return 3;
        }

        @Override
        public float minValue() {
            return 0;
        }

    },
    //0 = normale bombe, 1= powerbomb, 2 = spikebomb
    BOMBTYPE{
        @Override
        public float defaultValue() {
            return 0F;
        }

        @Override
        public float maxValue() {
            return 2;
        }

        @Override
        public float minValue() {
            return 0;
        }

    },

    //maxValue muss selber Wert sein wie maxValue bei BombAmount
    BOMBSDOWN{
        @Override
        public float defaultValue() {
            return 0F;
        }

        @Override
        public float maxValue() {
            return 3;
        }

        @Override
        public float minValue() {
            return 0;
        }

    }

}
