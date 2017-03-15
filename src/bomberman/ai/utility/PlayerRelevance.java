package bomberman.ai.utility;

import bomberman.gameplay.Player;
import bomberman.gameplay.utils.*;

/**
 * Created by 204g12 on 13.03.2017.
 */
public class PlayerRelevance{
    private Player player;
    private double relevance;

    public PlayerRelevance(Player player){
        this.player = player;
        update();
    }

    public void update(){
        //TODO: Inventar,Score etc.
        relevance = player.getHealth();
    }

    public double getRelevance(){
        return relevance;
    }

    public double getRelevance(Location location){
        double dist = player.getBoundingBox().getCenter().distanceTo(location);
        return relevance - dist;
    }

    public Player getPlayer() {
        return player;
    }
}