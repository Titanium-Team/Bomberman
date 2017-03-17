package bomberman.ai;

import bomberman.ai.utility.PlayerRelevance;
import bomberman.gameplay.GameMap;
import bomberman.gameplay.Player;
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
    private GameMap map;
    private Random random;

    private final static double wallWeight = 20;

    public AiManager(GameMap map, ArrayList<Player> nonAiPlayers) {
        this.map = map;
        this.random = new Random();
        updateTime = random.nextFloat();

        this.players = new ArrayList<>();
        for (Player player : nonAiPlayers) {
            players.add(new PlayerRelevance(player));
        }
        players.sort((o1, o2) -> (int) Math.round(o1.getRelevance() - o2.getRelevance()));
        this.aiPlayers = new ArrayList<>();
    }

    public Player createAi(String name, Location center) {
        AiPlayer aiPlayer = new AiPlayer(name, center, map, players);
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
        if (updateTime < 0) {
            updateTime = random.nextFloat();
            players.get(random.nextInt(players.size())).update();
        }

        for (AiPlayer aiPlayer : aiPlayers) {
            aiPlayer.update(dt);
        }
    }
}
