package bomberman.ai;

import bomberman.ai.utility.NavigationNode;
import bomberman.ai.utility.PlayerRelevance;
import bomberman.ai.utility.Stack;
import bomberman.gameplay.GameMap;
import bomberman.gameplay.Player;
import bomberman.gameplay.tile.TileType;
import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.utils.Location;

import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * Created by Daniel on 05.03.2017.
 */
public class AiPlayer extends Player {
    private static ArrayList<PlayerRelevance> playerRelevances;
    private PlayerRelevance target;
    private float timeTillUpdate;
    private Stack<Step> steps;
    private NavigationNode[][] navigationMap;
    private boolean ignoreBombs;
    private static boolean[][] dangerTiles;

    private final static float updateTime = 2;
    private final static double updateDistance = 5;
    private final static int wallDistBase = 20;

    public AiPlayer(String name, Location center, GameMap map, ArrayList<PlayerRelevance> playerRelevances, boolean[][] dangerTiles) {
        super(Player.PlayerType.AI, map, name, center);
        this.dangerTiles = dangerTiles;
        this.playerRelevances = playerRelevances;
        searchTarget();
        //TODO: Gegner greift sich selber an
        if (target.getPlayer() == this) {
            target = playerRelevances.get(1);
        }
        timeTillUpdate = updateTime;
        navigationMap = new NavigationNode[map.getTiles().length][map.getTiles()[0].length];
        for (int i = 0; i < map.getTiles().length; i++) {
            for (int j = 0; j < map.getTiles()[i].length; j++) {
                navigationMap[i][j] = new NavigationNode(map.getTiles()[i][j]);
            }
        }
    }

    public void update(float delta) {
        int currX = (int) Math.round(this.getBoundingBox().getCenter().getX());
        int currY = (int) Math.round(this.getBoundingBox().getCenter().getY());
        if(navigationMap[currX][currY].getTile().isExploding()){
            //Weiche Explosion aus
        }else{
            if(!ignoreBombs && dangerTiles[currX][currY]){
                //Weiche Bombe aus
                planEvade();
            }else{
                if(!steps.isEmpty()){
                    if(targetDistance() < updateDistance){
                        switch (steps.top()){
                            case MOVEUP:
                                break;
                            case MOVEDOWN:
                                break;
                            case MOVELEFT:
                                break;
                            case MOVERIGHT:
                                break;
                            case PLACEBOMB:
                                break;
                            case EVADE:
                                break;
                            case FLEE:
                                break;
                            case IGNORE:
                                if(ignoreBombs){
                                    ignoreBombs = false;
                                }else {
                                    ignoreBombs = true;
                                }
                                break;
                        }
                        steps.pop();
                    }else{
                        searchTarget();
                    }
                }else{
                    if(target == null){
                        searchTarget();
                    }
                    findPath();
                }
            }
        }
    }

    private double targetDistance(){
        return this.getBoundingBox().getCenter().distanceTo(target.getPlayer().getBoundingBox().getCenter());
    }

    private void searchTarget(){
        playerRelevances.sort((o1, o2) -> (int) Math.round(o1.getRelevance(this.getBoundingBox().getCenter()) - o2.getRelevance(this.getBoundingBox().getCenter())));
        target = playerRelevances.get(0);
    }

    private void findPath() {
        int targetX = (int) Math.round(target.getPlayer().getBoundingBox().getCenter().getX());
        int targetY = (int) Math.round(target.getPlayer().getBoundingBox().getCenter().getY());
        int startX = (int) Math.round(this.getBoundingBox().getCenter().getX());
        int startY = (int) Math.round(this.getBoundingBox().getCenter().getY());
        int currX = startX;
        int currY = startY;

        setUnmarked();
        navigationMap[currX][currY].setDist(0);

        while (currX != targetX || currY != targetY) {
            navigationMap[currX][currY].setMarked(true);
            int distBase;
            if (navigationMap[currX][currY].getTile().getTileType().isWalkable()) {
                distBase = 1 + navigationMap[currX][currY].getDist();
            } else if (navigationMap[currX][currY].getTile().getTileType().getHealth() > 500) {
                distBase = Integer.MAX_VALUE;
            } else {
                distBase = wallDistBase + navigationMap[currX][currY].getDist();
            }
            for (int j = Math.max(0, currY - 1); j < Math.min(navigationMap[currX].length, currY + 1); j++) {
                if (navigationMap[currX][j].getDist() > distBase) {
                    navigationMap[currX][j].setDist(distBase);
                    navigationMap[currX][j].setPrevX(currX);
                    navigationMap[currX][j].setPrevY(currY);
                }
            }
            for (int i = Math.max(0, currX - 1); i < Math.min(navigationMap.length, currX + 1); i++) {
                if (navigationMap[i][currY].getDist() > distBase) {
                    navigationMap[i][currY].setDist(distBase);
                    navigationMap[i][currY].setPrevX(currX);
                    navigationMap[i][currY].setPrevY(currY);
                }
            }
            currX = -1;
            currY = -1;
            for (int i = 0; i < navigationMap.length; i++) {
                for (int j = 0; j < navigationMap[i].length; j++) {
                    if (!navigationMap[i][j].isMarked()) {
                        if (currX == -1 || navigationMap[currX][currY].getDist() < navigationMap[i][j].getDist()) {
                            currX = i;
                            currY = j;
                        }
                    }
                }
            }
        }

        while (currX != startX || currY != startY) {
            int nextX = navigationMap[currX][currY].getPrevX();
            int nextY = navigationMap[currX][currY].getPrevY();

            if (nextX < currX) {
                steps.push(Step.MOVEUP);
            } else if (nextX > currX) {
                steps.push(Step.MOVEDOWN);
            } else if (nextY < currY) {
                steps.push(Step.MOVELEFT);
            } else if (nextY > currY) {
                steps.push(Step.MOVERIGHT);
            }

            if (!navigationMap[nextX][nextY].getTile().getTileType().isWalkable()) {
                steps.push(Step.EVADE);
                steps.push(Step.PLACEBOMB);
            }
        }
    }

    private void setUnmarked(){
        for(NavigationNode[] nodeArr : navigationMap){
            for(NavigationNode node : nodeArr){
                node.setMarked(false);
                node.setDist(Integer.MAX_VALUE);
            }
        }
    }

    private void planEvade() {

    }

    private void planFlee() {

    }

    private enum Step {
        MOVEUP, //Ai bewegt sich nach negativ Y
        MOVEDOWN, //Ai bewegt sich nach positiv Y
        MOVELEFT, //Ai bewegt sich nach negativ X
        MOVERIGHT, //Ai bewegt sich nach positiv X
        PLACEBOMB, //Ai platziert eine Bombe
        EVADE, //Ai plant Ausweichen vor Bombe
        FLEE, //Ai flieht vor anderen Spielern
        IGNORE, //Ai ignoriert Bomben oder hört auf Bomben zu ignorieren
    }
}
