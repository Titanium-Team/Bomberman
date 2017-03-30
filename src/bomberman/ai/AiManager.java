package bomberman.ai;

import bomberman.ai.utility.PlayerRelevance;
import bomberman.gameplay.GameMap;
import bomberman.gameplay.GameSession;
import bomberman.gameplay.Player;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.utils.Location;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Daniel on 13.03.2017.
 */
public class AiManager {
    private GameSession gameSession;
    private ArrayList<AiPlayer> aiPlayers;
    private ArrayList<PlayerRelevance> players;
    private float updateTime;
    private float halveUpdateTime;
    private GameMap map;
    private Random random;
    private boolean[][] dangerTiles;

    private final static double wallWeight = 20;

    public AiManager(GameSession gameSession, ArrayList<Player> nonAiPlayers) {

        this.gameSession = gameSession;
        this.map = gameSession.getGameMap();
        this.random = new Random();
        updateTime = random.nextFloat();
        halveUpdateTime = updateTime/2;

        dangerTiles = new boolean[map.getTiles().length][map.getTiles()[0].length];

        this.players = new ArrayList<>();
        for (Player player : nonAiPlayers) {
            players.add(new PlayerRelevance(player));
        }
        players.sort((o1, o2) -> (int) Math.round(o1.getRelevance() - o2.getRelevance()));
        this.aiPlayers = new ArrayList<>();

        //calcDangerTiles();
    }

    public Player createAi(String name, Location center) {
        AiPlayer aiPlayer = new AiPlayer(gameSession, name, center, players, dangerTiles, this);
        aiPlayers.add(aiPlayer);
        players.add(new PlayerRelevance(aiPlayer));
        return aiPlayer;
    }

    public void removePlayer(Player player) {
        for (PlayerRelevance playerRelevance : players) {
            if (playerRelevance.getPlayer() == player) {
                players.remove(playerRelevance);
                break;
            }
        }

        if (player instanceof AiPlayer) {
            aiPlayers.remove(player);
        }
    }

    public void addPlayer(Player player){
        players.add(new PlayerRelevance(player));
    }

    public void update(float dt) {
        updateTime = -dt;
        if(updateTime < halveUpdateTime){
            halveUpdateTime = Float.NEGATIVE_INFINITY;
            //calcDangerTiles();
        }
        if (updateTime < 0) {
            updateTime = random.nextFloat();
            halveUpdateTime = updateTime/2;
            players.get(random.nextInt(players.size())).update();
        }

        for (AiPlayer aiPlayer : aiPlayers) {
            aiPlayer.update(dt);
        }
    }

    public void calcDangerTiles(){
        for(int i = 0; i < dangerTiles.length; i++){
            for(int j = 0; j < dangerTiles[i].length; j++){
                dangerTiles[i][j] = false;
            }
        }
        Tile[][] tiles = map.getTiles();
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[0].length; j++){
                if(tiles[i][j].isExploding()){
                    dangerTiles[i][j] = true;
                }else{
                    if(tiles[i][j].getTileObject() instanceof Bomb){
                        int range = ((Bomb) tiles[i][j].getTileObject()).getRange();
                        bombFound(i,j,range);
                    }
                }
            }
        }
    }

    public void bombFound(int x,int y,int range){
        Tile[][] tiles = map.getTiles();
        dangerTiles[x][y] = true;
        for(int i = x-1; i > Math.max(0,x-range) && tiles[i][y].getTileType().isWalkable();i--){
            dangerTiles[i][y] = true;
        }
        for(int i = x+1; i < Math.min(tiles.length,x+range) && tiles[i][y].getTileType().isWalkable();i++){
            dangerTiles[i][y] = true;
        }
        for(int j = y-1; j > Math.max(0,y-range) && tiles[x][j].getTileType().isWalkable();j--){
            dangerTiles[x][j] = true;
        }
        for(int j = y+1; j < Math.min(tiles[x].length,y+range) && tiles[x][j].getTileType().isWalkable();j++){
            dangerTiles[x][j] = true;
        }
    }
}
