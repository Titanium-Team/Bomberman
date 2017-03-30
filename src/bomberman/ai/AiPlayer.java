package bomberman.ai;

import bomberman.ai.utility.NavigationNode;
import bomberman.ai.utility.PlayerRelevance;
import bomberman.ai.utility.Stack;
import bomberman.gameplay.GameMap;
import bomberman.gameplay.GameSession;
import bomberman.gameplay.Player;
import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.statistic.Statistics;
import bomberman.gameplay.utils.Location;
import bomberman.view.engine.utility.Vector2;
import org.lwjgl.Sys;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

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

    private FacingDirection facingDirection;
    private Vector2 moveTo;

    private final static double UPDATE_DISTANCE = 100;
    private final static int WALL_DIST_BASE = 20;

    public AiPlayer(GameSession gameSession, String name, Location center, ArrayList<PlayerRelevance> playerRelevances, boolean[][] dangerTiles) {
        super(gameSession, Player.PlayerType.AI, name, center);
        AiPlayer.dangerTiles = dangerTiles;
        AiPlayer.playerRelevances = playerRelevances;
        searchTarget();
        GameMap map = gameSession.getGameMap();
        moveTo = null;
        navigationMap = new NavigationNode[map.getTiles().length][map.getTiles()[0].length];
        for (int i = 0; i < map.getTiles().length; i++) {
            for (int j = 0; j < map.getTiles()[i].length; j++) {
                navigationMap[i][j] = new NavigationNode(map.getTiles()[i][j]);
            }
        }
        facingDirection = FacingDirection.DEFAULT;
    }

    @Override
    public FacingDirection getFacingDirection() {
        return this.facingDirection;
    }

    public void update(float delta) {
        if(!playerRelevances.isEmpty()) {
            int currX = (int) Math.round(this.getBoundingBox().getCenter().getX());
            int currY = (int) Math.round(this.getBoundingBox().getCenter().getY());
            if (!ignoreBombs && dangerTiles[currX][currY]) {
                planEvade();
            } else if (moveTo != null) {
                moveTo(delta);
                if (moveTo == null && !dangerTiles[currX][currY]) {
                    if (canBomb()) {
                        steps = new Stack<>();
                        //Platziere Bombe
                        System.out.println("BOMBE!!!!!!!!");
                        planEvade();
                    }
                }
            } else {
                if (steps != null && !steps.isEmpty()) {
                    if (targetDistance() < UPDATE_DISTANCE) {
                        switch (steps.top().stepType) {
                            case MOVE: //Negative Y
                                if (ignoreBombs || !dangerTiles[((MoveStep)steps.top()).getX()][((MoveStep)steps.top()).getY()]) {
                                    moveTo = new Vector2(((MoveStep)steps.top()).getX()+0.5f, ((MoveStep)steps.top()).getY()+0.5f);
                                } else {
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
                                if (ignoreBombs) {
                                    ignoreBombs = false;
                                } else {
                                    ignoreBombs = true;
                                }
                                break;
                        }
                        steps.pop();
                    } else {
                        searchTarget();
                    }
                } else {
                    if (target == null) {
                        searchTarget();
                    }
                    findPath();
                }
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
            System.out.println("maxMove:("+maxMovement.getX()+"|"+maxMovement.getY()+") dtMove:"+dtMovement.getX()+"|"+dtMovement.getY()+")");
            if(dtMovement.getLength() > maxMovement.getLength()){
                this.getBoundingBox().setCenter(this.getBoundingBox().getCenter().getX()+maxMovement.getX(),this.getBoundingBox().getCenter().getY()+maxMovement.getY());
                moveTo = null;
            }else{
                this.getBoundingBox().setCenter(this.getBoundingBox().getCenter().getX()+dtMovement.getX(),this.getBoundingBox().getCenter().getY()+dtMovement.getY());
            }
            facingDirection = FacingDirection.from(maxMovement);
        }catch (CloneNotSupportedException e){
            System.out.println("FEHLER!!!!!!!!!!!");
        }

    }

    private boolean canBomb(){
        int currX = (int) Math.round(this.getBoundingBox().getCenter().getX());
        int currY = (int) Math.round(this.getBoundingBox().getCenter().getY());

        float range = this.getPropertyRepository().getValue(PropertyTypes.BOMB_BLAST_RADIUS);

        for(PlayerRelevance playerRelevance : playerRelevances){
            if(playerRelevance.getPlayer() != this){
                int playerX = (int) Math.round(playerRelevance.getPlayer().getBoundingBox().getCenter().getX());
                int playerY = (int) Math.round(playerRelevance.getPlayer().getBoundingBox().getCenter().getY());

                if(playerX == currX){
                    if(playerY-currY < range && playerY-currY >= 0){
                        boolean noWall = true;
                        for(int i = currY; i > playerY; i--){
                            if (!navigationMap[currX][i].getTile().getTileType().isWalkable()) {
                                noWall = false;
                                break;
                            }
                        }
                        if(noWall){
                            return true;
                        }
                    }else if(currY-playerY < range && currY-playerY >= 0){
                        boolean noWall = true;
                        for(int i = currY; i < playerY; i++){
                            if (!navigationMap[currX][i].getTile().getTileType().isWalkable()) {
                                noWall = false;
                                break;
                            }
                        }
                        if(noWall){
                            return true;
                        }
                    }
                }else if (playerY == currY){
                    if(playerX-currX < range && playerX-currX >= 0){
                        boolean noWall = true;
                        for(int i = currX; i > playerX; i--){
                            if (!navigationMap[i][currY].getTile().getTileType().isWalkable()) {
                                noWall = false;
                                break;
                            }
                        }
                        if(noWall){
                            return true;
                        }
                    }else if(currX-playerX < range && currX-playerX >= 0){
                        boolean noWall = true;
                        for(int i = currX; i < playerX; i++){
                            if (!navigationMap[i][currY].getTile().getTileType().isWalkable()) {
                                noWall = false;
                                break;
                            }
                        }
                        if(noWall){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private double targetDistance(){
        return this.getBoundingBox().getCenter().distanceTo(target.getPlayer().getBoundingBox().getCenter());
    }

    private void searchTarget(){
        if(!playerRelevances.isEmpty()) {
            playerRelevances.sort((o1, o2) -> (int) Math.round(o1.getRelevance(this.getBoundingBox().getCenter()) - o2.getRelevance(this.getBoundingBox().getCenter())));
            target = playerRelevances.get(0);
            if(target.getPlayer() == this){
                if(playerRelevances.size() > 1){
                    target = playerRelevances.get(1);
                }
            }
        }
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
            for (int j = Math.max(0, currY - 1); j < Math.min(navigationMap[currX].length, currY + 2); j++) {
                updateTile(currX,j,dist,currX,currY,true);
            }
            for (int i = Math.max(0, currX - 1); i < Math.min(navigationMap.length, currX + 2); i++) {
                updateTile(i,currY,dist,currX,currY,true);
            }
            currX = -1;
            currY = -1;
            for(int i = 0;i < navigationMap.length;i++){
                for(int j = 0; j < navigationMap[i].length;j++){
                    if (!navigationMap[i][j].isMarked()) {
                        if (currX == -1 || navigationMap[currX][currY].getDist() > navigationMap[i][j].getDist()) {
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
            for(int i = 0;i < navigationMap.length;i++){
                for(int j = 0; j < navigationMap[i].length;j++){
                    if (!navigationMap[i][j].isMarked()) {
                        if (currX == -1 || navigationMap[currX][currY].getDist() > navigationMap[i][j].getDist()) {
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
        System.out.println("Going back");
        while (currX != startX || currY != startY) {
            int nextX = navigationMap[currX][currY].getPrevX();
            int nextY = navigationMap[currX][currY].getPrevY();
            steps.push(new MoveStep(nextX,nextY,StepType.MOVE));
            System.out.println("curr:("+currX+"|"+currY+" start:("+startX+"|"+startY+")"+" next:("+nextX+"|"+nextY+")");
            if (!navigationMap[nextX][nextY].getTile().getTileType().isWalkable()) {
                System.out.println("Plane Bombe Legen");
                steps.push(new Step(StepType.EVADE));
                steps.push(new Step(StepType.PLACEBOMB));
            }
            currX = nextX;
            currY = nextY;
        }
    }

    private class Step {
        public StepType stepType;

        public Step(StepType stepType) {
            this.stepType = stepType;
        }
    }

    private class MoveStep extends Step{

        private int x,y;

        MoveStep(int x, int y, StepType stepType) {
            super(stepType);
            this.x = x;
            this.y = y;
        }

        private int getX(){
            return x;
        }

        private int getY(){
            return y;
        }
    }

    private enum StepType{
        PLACEBOMB, //Ai platziert eine Bombe
        EVADE, //Ai plant Ausweichen vor Bombe
        FLEE, //Ai flieht vor anderen Spielern
        IGNORE, //Ai ignoriert Bomben oder hört auf Bomben zu ignorieren
        MOVE; //Ai bewegt sich
    }
}
