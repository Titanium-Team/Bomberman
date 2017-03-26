package bomberman.ai.utility;

import bomberman.gameplay.Player;
import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.statistic.GameStatistic;
import bomberman.gameplay.statistic.Statistic;
import bomberman.gameplay.statistic.Statistics;
import bomberman.gameplay.utils.Location;

/**
 * Created by 204g12 on 13.03.2017.
 */
public class PlayerRelevance {
    private final static double HEALTH_MODIFIER = 1;
    private final static double BOMB_MODIFIER = 1;
    private final static double KILLS_MODIFIER = 5;

    private Player player;
    private double relevance;

    public PlayerRelevance(Player player) {
        this.player = player;
        update();
    }

    public void update() {
        //TODO: Inventar,Score etc.
        relevance = player.getPropertyRepository().getValue(PropertyTypes.HEALTH) * HEALTH_MODIFIER + player.getGameStatistic().get(Statistics.BOMBS_PLANTED) * BOMB_MODIFIER + player.getGameStatistic().get(Statistics.KILLS) * KILLS_MODIFIER;
    }

    public double getRelevance() {
        return relevance;
    }

    public double getRelevance(Location location) {
        double dist = player.getBoundingBox().getCenter().distanceTo(location);
        return relevance - dist;
    }

    public Player getPlayer() {
        return player;
    }
}