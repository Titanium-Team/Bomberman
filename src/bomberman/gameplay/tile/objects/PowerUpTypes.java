package bomberman.gameplay.tile.objects;

/**
 * Created by 204g04 on 17.03.2017.
 */
public enum PowerUpTypes implements PowerUpType {

    SPEEDUP {
        @Override
        public double value() {
            return .1;
        }
    },
    SPEEDDOWN {
        @Override
        public double value() {
            return -.1;
        }
    },
    FIREUP {
        @Override
        public double value() {
            return 1;
        }
    },
    FIREDOWN {
        @Override
        public double value() {
            return -1;
        }
    },
    BOMBUP {
        @Override
        public double value() {
            return 1;
        }
    },
    BOMBDOWN {
        @Override
        public double value() {
            return -1;
        }
    };
}
