package bomberman.ai;

import bomberman.ai.utility.PlayerRelevance;
import bomberman.gameplay.GameMap;
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
    private ArrayList<AiPlayer> aiPlayers;
    private ArrayList<PlayerRelevance> players;
    private float updateTime;
    private float halveUpdateTime;
    private GameMap map;
    private Random random;
    private boolean[][] dangerTiles;

    private final static double wallWeight = 20;

    public AiManager(GameMap map, ArrayList<Player> nonAiPlayers) {
        this.map = map;
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

        calcDangerTiles();
    }

    public Player createAi(String name, Location center) {
        AiPlayer aiPlayer = new AiPlayer(name, center, map, players, dangerTiles);
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

    public void update(float dt) {
        updateTime = -dt;
        if(updateTime < halveUpdateTime){
            halveUpdateTime = Float.NEGATIVE_INFINITY;
            calcDangerTiles();
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

    private void calcDangerTiles(){
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
                        for(int x = i+1; x < i+range && tiles[x-1][j].getTileType().isWalkable(); x++){
                            dangerTiles[x][j] = true;
                        }
                        for(int x = i-1; x > i-range && tiles[x+1][j].getTileType().isWalkable(); x--){
                            dangerTiles[x][j] = true;
                        }
                        for(int y = i+1; y < i+range && tiles[i][y-1].getTileType().isWalkable(); y++){
                            dangerTiles[i][y] = true;
                        }
                        for(int y = i-1; y > i-range && tiles[i][y+1].getTileType().isWalkable(); y--){
                            dangerTiles[i][y] = true;
                        }
                    }
                }
            }
        }
    }
}
