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

    }

}
