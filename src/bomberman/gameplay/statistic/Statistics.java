package bomberman.gameplay.statistic;

public enum Statistics implements Statistic {

    KILLS {

        @Override
        public String getName() {
            return "Kills";
        }

        @Override
        public boolean isMax() {
            return false;
        }

        @Override
        public boolean isAdd() {
            return true;
        }

        @Override
        public double defaultValue() {
            return 0;
        }

    },
    COLLECTED_POWERUPS {

        @Override
        public String getName() {
            return "Collected Power Ups";
        }

        @Override
        public boolean isMax() {
            return false;
        }

        @Override
        public boolean isAdd() {
            return true;
        }

        @Override
        public double defaultValue() {
            return 0;
        }

    },
    BOMBS_PLANTED {

        @Override
        public String getName() {
            return "Bombs Planted";
        }

        @Override
        public boolean isMax() {
            return false;
        }

        @Override
        public boolean isAdd() {
            return true;
        }

        @Override
        public double defaultValue() {
            return 0;
        }

    },
    DISTANCE_TRAVELLED {

        @Override
        public String getName() {
            return "Distance Travelled";
        }

        @Override
        public boolean isMax() {
            return false;
        }

        @Override
        public boolean isAdd() {
            return true;
        }

        @Override
        public double defaultValue() {
            return 0;
        }

    },
    SUICIDES {

        @Override
        public String getName() {
            return "Suicides";
        }

        @Override
        public boolean isMax() {
            return false;
        }

        @Override
        public boolean isAdd() {
            return true;
        }

        @Override
        public double defaultValue() {
            return 0;
        }

    }

}
