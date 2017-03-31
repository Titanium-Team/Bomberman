package bomberman.ai;

import bomberman.Main;
import bomberman.ai.utility.NavigationNode;
import bomberman.ai.utility.PlayerRelevance;
import bomberman.ai.utility.Stack;
import bomberman.gameplay.GameMap;
import bomberman.gameplay.GameSession;
import bomberman.gameplay.Player;
import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.statistic.Statistics;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.tile.objects.PowerUp;
import bomberman.gameplay.utils.BoundingBox;
import bomberman.gameplay.utils.Location;
import bomberman.view.engine.utility.Vector2;

import java.util.ArrayList;

/**
 * Created by Daniel on 05.03.2017.
 */
public class AiPlayer extends Player {

    private static ArrayList<PlayerRelevance> playerRelevances;
    private PlayerRelevance target;
    private Stack<Step> steps;
    private NavigationNode[][] navigationMap;
    private boolean ignore;
    private static boolean[][] dangerTiles;
    private AiManager aiManager;
    private double lastHealth;

    private FacingDirection facingDirection;
    private Vector2 moveTo;

    private final static double UPDATE_DISTANCE = 100;
    private final static int WALL_DIST_BASE = 20;

    public AiPlayer(GameSession gameSession, String name, Location center, ArrayList<PlayerRelevance> playerRelevances, boolean[][] dangerTiles, AiManager aiManager) {
        super(gameSession, Player.PlayerType.AI, name, center);
        this.aiManager = aiManager;
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

    @Override
    public void update(float delta) {
        if(!playerRelevances.isEmpty()) {
            int currX = (int) Math.floor(this.getBoundingBox().getCenter().getX());
            int currY = (int) Math.floor(this.getBoundingBox().getCenter().getY());
            if(lastHealth != getHealth()){
                if(ignore || dangerTiles[currX][currY]){
                    planEvade();
                }else{
                    findPath();
                }
            }
            lastHealth = getHealth();
            if (moveTo != null) {
                moveTo(delta);
                if (moveTo == null) {
                    if (!ignore && !dangerTiles[currX][currY] && canBomb()) {
                        steps = new Stack<>();
                        placeBomb();
                    }else{
                        if(ignore || dangerTiles[currX][currY]){
                            planEvade();
                        }else {
                            findPath();
                        }
                    }
                }

            } else {
                if (!ignore && dangerTiles[currX][currY]) {
                    planEvade();
                } else if (steps != null && !steps.isEmpty()) {
                    if (targetDistance() < UPDATE_DISTANCE || ignore) {
                        switch (steps.top().stepType) {
                            case MOVE:
                                if (ignore || !dangerTiles[((MoveStep)steps.top()).getX()][((MoveStep)steps.top()).getY()]) {
                                    if(navigationMap[((MoveStep)steps.top()).getX()][((MoveStep)steps.top()).getY()].getTile().getTileType().isWalkable()) {
                                        moveTo = new Vector2(((MoveStep) steps.top()).getX() + 0.5f, ((MoveStep) steps.top()).getY() + 0.5f);
                                    } else {
                                        if(ignore){
                                            planEvade();
                                        }else{
                                            findPath();
                                        }
                                    }
                                } else {
                                    planEvade();
                                }
                                break;
                            case PLACEBOMB:
                                placeBomb();
                                break;
                            case EVADE:
                                planEvade();
                                break;
                            case IGNORE:
                                if (ignore) {
                                    ignore = false;
                                } else {
                                    ignore = true;
                                }
                                break;
                        }
                        steps.pop();
                    } else {
                        searchTarget();
                    }
                } else {
                    if(dangerTiles[currX][currY]){
                        planEvade();
                    }else {
                        if (target == null) {
                            searchTarget();
                        }
                        findPath();
                    }
                }
            }
        }

        BoundingBox boundingBox = this.getBoundingBox();

        for (int x = (int) boundingBox.getMin().getX(); x < boundingBox.getMax().getX(); x++) {
            for (int y = (int) boundingBox.getMin().getY(); y < boundingBox.getMax().getY(); y++) {

                Tile tile = getGameSession().getGameMap().getTiles()[x][y];

                if (tile.getTileType().isWalkable() && !(tile.getTileObject() == null)) {

                    if (tile.getTileObject() instanceof Bomb) {
                        continue;
                    }

                    tile.getTileObject().interact(this);
                    if (tile.getTileObject() instanceof PowerUp) {
                        tile.destroyObject();
                    }
                }
            }
        }

        this.getTile().interact(this);
    }

    private void placeBomb(){
        Tile tile = this.getTile();

        int bombsLeft = (int) this.getPropertyRepository().getValue(PropertyTypes.BOMB_AMOUNT);

        if(tile.getTileObject() instanceof Bomb || bombsLeft <= 0) {
            return;
        }

        this.getPropertyRepository().setValue(PropertyTypes.BOMB_AMOUNT, (float) (bombsLeft - 1));

        this.getGameStatistic().update(Statistics.BOMBS_PLANTED, 1);

        Bomb bomb = new Bomb(this, tile, 2, 1);
        getGameSession().getGameMap().spawn(bomb);
        aiManager.bombFound((int)Math.floor(getBoundingBox().getCenter().getX()),(int)Math.floor(getBoundingBox().getCenter().getY()),Math.round(this.getPropertyRepository().getValue(PropertyTypes.BOMB_BLAST_RADIUS)));
        planEvade();

        Main.instance.getNetworkController().plantBomb(bomb);
    }

    private void moveTo(float dt){
        try {
            Vector2 currPos = new Vector2((float) getBoundingBox().getCenter().getX(),(float) getBoundingBox().getCenter().getY());
            Vector2 maxMovement = new Vector2(moveTo.getX()-currPos.getX(),moveTo.getY()-currPos.getY());
            if(maxMovement.getLength() > 0) {
                Vector2 dtMovement = maxMovement.clone();
                dtMovement.normalize();
                dtMovement.multiply(dt * this.getPropertyRepository().getValue(PropertyTypes.SPEED_FACTOR));
                if (dtMovement.getLength() > maxMovement.getLength()) {
                    this.getBoundingBox().setCenter(this.getBoundingBox().getCenter().getX() + maxMovement.getX(), this.getBoundingBox().getCenter().getY() + maxMovement.getY());
                    moveTo = null;
                } else {
                    this.getBoundingBox().setCenter(this.getBoundingBox().getCenter().getX() + dtMovement.getX(), this.getBoundingBox().getCenter().getY() + dtMovement.getY());
                }
                facingDirection = FacingDirection.from(maxMovement);

                Main.instance.getNetworkController().move(getBoundingBox().getCenter(), getIndex(), getFacingDirection());
            }else{
                moveTo = null;
            }
        }catch (CloneNotSupportedException e){
            System.out.println("FEHLER!!!!!!!!!!!");
        }

    }

    private boolean canBomb(){
        int currX = (int) Math.floor(this.getBoundingBox().getCenter().getX());
        int currY = (int) Math.floor(this.getBoundingBox().getCenter().getY());

        float range = this.getPropertyRepository().getValue(PropertyTypes.BOMB_BLAST_RADIUS);

        for(PlayerRelevance playerRelevance : playerRelevances){
            if(playerRelevance.getPlayer() != this){
                int playerX = (int) Math.floor(playerRelevance.getPlayer().getBoundingBox().getCenter().getX());
                int playerY = (int) Math.floor(playerRelevance.getPlayer().getBoundingBox().getCenter().getY());
                if(playerX == currX){
                    if(playerY-currY <= range && playerY-currY >= 0){
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
                    }else if(playerY-currY >= range && playerY-currY <= 0){
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
                    if(playerX-currX <= range && playerX-currX >= 0){
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
                    }else if(playerX-currX >= range && playerX-currX <= 0){
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
        moveTo = null;

        int targetX = (int) Math.floor(target.getPlayer().getBoundingBox().getCenter().getX());
        int targetY = (int) Math.floor(target.getPlayer().getBoundingBox().getCenter().getY());
        int startX = (int) Math.floor(this.getBoundingBox().getCenter().getX());
        int startY = (int) Math.floor(this.getBoundingBox().getCenter().getY());
        int currX = startX;
        int currY = startY;

        setUnmarked();
        navigationMap[currX][currY].setDist(0);

        while (currX != targetX || currY != targetY) {
            int[] curr = dijsktraStep(currX,currY,true);
            if(curr == null){
                break;
            }
            currX = curr[0];
            currY = curr[1];
        }
        steps = new Stack<>();
        goBack(currX,currY,startX,startY);
    }

    private void planEvade() {
        moveTo = null;
        ignore = true;

        int startX = (int) Math.floor(this.getBoundingBox().getCenter().getX());
        int startY = (int) Math.floor(this.getBoundingBox().getCenter().getY());
        int currX = startX;
        int currY = startY;

        setUnmarked();
        navigationMap[currX][currY].setDist(0);

        while (dangerTiles[currX][currY]) {
            int[] curr = dijsktraStep(currX,currY,false);
            if(curr == null){
                break;
            }
            currX = curr[0];
            currY = curr[1];
        }

        steps = new Stack<>();
        steps.push(new Step(StepType.IGNORE));
        goBack(currX,currY,startX,startY);
    }

    //----- DIJKSTRA HELPER METHODS -----
    private int[] dijsktraStep(int currX,int currY,boolean useBombs){
        navigationMap[currX][currY].setMarked(true);
        int dist = navigationMap[currX][currY].getDist();
        for (int j = Math.max(1, currY - 1); j < Math.min(navigationMap[currX].length-1, currY + 2); j++) {
            updateTile(currX,j,dist,currX,currY,useBombs);
        }
        for (int i = Math.max(1, currX - 1); i < Math.min(navigationMap.length-1, currX + 2); i++) {
            updateTile(i,currY,dist,currX,currY,useBombs);
        }
        int nextX = -1;
        int nextY = -1;
        for(int i = 0;i < navigationMap.length;i++){
            for(int j = 0; j < navigationMap[i].length;j++){
                if (!navigationMap[i][j].isMarked()) {
                    if (nextX == -1 || navigationMap[nextX][nextY].getDist() > navigationMap[i][j].getDist()) {
                        nextX = i;
                        nextY = j;
                    }
                }
            }
        }
        if (navigationMap[nextX][nextY].getDist() > 200) {
            return null;
        }else {
            return new int[]{nextX, nextY};
        }
    }

    private void setUnmarked(){
        for(NavigationNode[] nodeArr : navigationMap){
            for(NavigationNode node : nodeArr){
                node.setMarked(false);
                node.setDist(Integer.MAX_VALUE);
                node.setPrevX(-1);
                node.setPrevY(-1);
            }
        }
    }

    private void updateTile(int x,int y,int dist,int srcX,int srcY, boolean useBombs){
        if(ignore || !dangerTiles[x][y]){
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
            steps.push(new MoveStep(currX,currY,StepType.MOVE));
            int nextX = navigationMap[currX][currY].getPrevX();
            int nextY = navigationMap[currX][currY].getPrevY();
            if(nextX >= 0 && nextX < navigationMap.length && nextY >= 0 && nextY < navigationMap[nextX].length) {
                if (!navigationMap[nextX][nextY].getTile().getTileType().isWalkable()) {
                    steps.push(new Step(StepType.EVADE));
                    steps.push(new Step(StepType.PLACEBOMB));
                }
            }else{
                return;
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
        IGNORE, //Ai ignoriert Bomben oder hört auf Bomben zu ignorieren
        MOVE; //Ai bewegt sich
    }
}
