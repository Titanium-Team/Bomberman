package bomberman.ai;

import bomberman.ai.utility.NavigationNode;
import bomberman.ai.utility.PlayerRelevance;
import bomberman.ai.utility.Stack;
import bomberman.gameplay.GameMap;
import bomberman.gameplay.Player;
import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.utils.Location;
import bomberman.view.engine.utility.Vector2;

import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by Daniel on 05.03.2017.
 */
public class AiPlayer extends Player {
    private static ArrayList<PlayerRelevance> playerRelevances;
    private PlayerRelevance target;
    private Stack<Step> steps;
    private NavigationNode[][] navigationMap;
    private boolean ignoreBombs;
    private static boolean[][] dangerTiles;

    private Vector2 moveTo;

    private final static double UPDATE_DISTANCE = 5;
    private final static int WALL_DIST_BASE = 20;

    public AiPlayer(String name, Location center, GameMap map, ArrayList<PlayerRelevance> playerRelevances, boolean[][] dangerTiles) {
        super(Player.PlayerType.AI, map, name, center);
        AiPlayer.dangerTiles = dangerTiles;
        AiPlayer.playerRelevances = playerRelevances;
        searchTarget();
        moveTo = null;
        //TODO: Gegner greift sich selber an
        if (target.getPlayer() == this) {
            target = playerRelevances.get(1);
        }
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
        if(!ignoreBombs && dangerTiles[currX][currY]){
            planEvade();
        }else if(moveTo != null) {
            moveTo(delta);
        }else{
            if(!steps.isEmpty()){
                if(targetDistance() < UPDATE_DISTANCE){
                    switch (steps.top()){
                        case MOVEUP: //Negative Y
                            if(ignoreBombs || !dangerTiles[currX][currY-1]){
                                moveTo = new Vector2(currX,currY-1);
                            }else{
                                findPath();
                            }
                            break;
                        case MOVEDOWN: //Positive X
                            if(ignoreBombs || !dangerTiles[currX][currY+1]){
                                moveTo = new Vector2(currX,currY+1);
                            }else{
                                findPath();
                            }
                            break;
                        case MOVELEFT: //Negative Y
                            if(ignoreBombs || !dangerTiles[currX-1][currY]){
                                moveTo = new Vector2(currX-1,currY);
                            }else{
                                findPath();
                            }
                            break;
                        case MOVERIGHT: //Positive X
                            if(ignoreBombs || !dangerTiles[currX+1][currY]){
                                moveTo = new Vector2(currX+1,currY);
                            }else{
                                findPath();
                            }
                            break;
                        case PLACEBOMB:
                            //TODO: Bombe platzieren
                            System.out.println("Bombe platzieren");
                            break;
                        case EVADE:
                            planEvade();
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

    private void moveTo(float dt){
        try {
            Vector2 currPos = new Vector2((float) getBoundingBox().getCenter().getX(),(float) getBoundingBox().getCenter().getY());
            Vector2 maxMovement = new Vector2(moveTo.getX()-currPos.getX(),moveTo.getY()-currPos.getY());
            Vector2 dtMovement = maxMovement.clone();
            dtMovement.normalize();
            dtMovement.multiply(dt*this.getPropertyRepository().getValue(PropertyTypes.SPEED_FACTOR));
            if(dtMovement.getLength() > maxMovement.getLength()){
                this.getBoundingBox().getCenter().set(maxMovement.getX(),maxMovement.getY());
                moveTo = null;
            }else{
                this.getBoundingBox().getCenter().set(dtMovement.getX(),dtMovement.getY());
            }
        }catch (CloneNotSupportedException e){
            System.out.println("FEHLER!!!!!!!!!!!");
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
            int dist = navigationMap[currX][currY].getDist();
            for (int j = Math.max(0, currY - 1); j < Math.min(navigationMap[currX].length, currY + 1); j++) {
                updateTile(currX,j,dist,currX,currY,true);
            }
            for (int i = Math.max(0, currX - 1); i < Math.min(navigationMap.length, currX + 1); i++) {
                updateTile(i,currY,dist,currX,currY,true);
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

        goBack(currX,currY,startX,startY);
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
        int startX = (int) Math.round(this.getBoundingBox().getCenter().getX());
        int startY = (int) Math.round(this.getBoundingBox().getCenter().getY());
        int currX = startX;
        int currY = startY;

        setUnmarked();
        navigationMap[currX][currY].setDist(0);

        while (dangerTiles[currX][currY]) {
            navigationMap[currX][currY].setMarked(true);
            int dist = navigationMap[currX][currY].getDist();
            for (int j = Math.max(0, currY - 1); j < Math.min(navigationMap[currX].length, currY + 1); j++) {
                updateTile(currX,j,dist,currX,currY,false);
            }
            for (int i = Math.max(0, currX - 1); i < Math.min(navigationMap.length, currX + 1); i++) {
                updateTile(i,currY,dist,currX,currY,false);
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

        steps = new Stack<>();

        goBack(currX,currY,startX,startY);
    }

    private void planFlee() {

    }

    //----- DIJKSTRA HELPER METHODS -----

    private void updateTile(int x,int y,int dist,int srcX,int srcY, boolean useBombs){
        if(ignoreBombs || !dangerTiles[x][y]){
            dist =+ ((navigationMap[x][y].getTile().getTileType().isWalkable()) ? (1):((navigationMap[x][y].getTile().getTileType().getHealth() > 100 || !useBombs) ? (Integer.MAX_VALUE):(WALL_DIST_BASE)));
            if(navigationMap[x][y].getDist() > dist){
                navigationMap[x][y].setDist(dist);
                navigationMap[x][y].setPrevX(srcX);
                navigationMap[x][y].setPrevY(srcY);
            }
        }
    }

    private void goBack(int currX,int currY,int startX,int startY){
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
