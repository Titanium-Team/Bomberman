package bomberman.gameplay;

import bomberman.Main;
import bomberman.ai.AiManager;
import bomberman.gameplay.tile.TileTypes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class GameSession {

    private AiManager aiManager;

    private GameMap gameMap;
    private final List<Player> players = new LinkedList<>();

    private final static float POWERUP_TIME = 25;
    private float powerupTimer = POWERUP_TIME;

    private boolean powerupSpawning = true;


    public GameSession(GameMap gameMap) {
        this.gameMap = gameMap;
        aiManager = new AiManager(this,new ArrayList<>(getPlayers()));
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public GameMap getGameMap() {
        return this.gameMap;
    }

    public Player getPlayer(int index) {
        return this.players.get(index);
    }

    public Player getLocalPlayer() {
        return this.players.stream().filter(e -> e.getPlayerType() == Player.PlayerType.LOCAL).findAny().orElseGet(null);
    }

    public void setMap(int index) {
        this.gameMap = Main.instance.getGameplayManager().getMap(index);
    }

    public boolean isPowerupSpawning() {
        return this.powerupSpawning;
    }

    public void setPowerupSpawning(boolean enabled) {
        this.powerupSpawning = enabled;
    }

    private void getGameSummary() {
        //--- Most Deadly       -> Player with the most kills.
        //--- Most Professional -> Player with the highest kill to death ratio
        //--- Longest Innings   -> Player with the least deaths.
        //--- Most Cowardly     -> Player with the least kills and least deaths.
        //--- Lemming Award     -> Player with the most suicides.
        //--- Shortest Innings  -> Player with the most deaths.
        //--- Mostly Harmless   -> Player with the least kills.
        //--- Gandhi            -> Player winning without killing.
    }

    public synchronized void addPlayer(Player player) {

        if(this.players.contains(player)) {
            throw new IllegalStateException("Do not add the same instance more than once.");
        }
        this.players.add(player);
        player.setIndex(this.players.indexOf(player));
        aiManager.addPlayer(player);
    }

    public void addAi(){
        this.players.add(aiManager.createAi("TestAi",gameMap.getRandomStartPosition()));
    }

    public void update(float delta) {
        this.players.iterator().forEachRemaining(e -> e.update(delta));
        Stream.of(this.gameMap.getTiles()).iterator().forEachRemaining(e -> Stream.of(e).iterator().forEachRemaining(t -> t.update(delta)));

        //--- Powerup Spawn Timer
        this.powerupTimer -= delta;
        if (this.powerupTimer <= 0 && this.powerupSpawning) {
            this.checkPowerups();
            this.powerupTimer = POWERUP_TIME;
        }

        aiManager.update(delta);
    }

    public void onKeyDown(int key, char c) {
        this.players.stream().filter(e -> (e instanceof LocalPlayer)).iterator().forEachRemaining(e -> ((LocalPlayer) e).keyDown(key, c));
    }

    public void onKeyUp(int key, char c) {
        this.players.stream().filter(e -> (e instanceof LocalPlayer)).iterator().forEachRemaining(e -> ((LocalPlayer) e).keyUp(key, c));
    }

    public void onMouseDown(int button, int mouseX, int mouseY) {}

    public void onMouseUp(int button, int mouseX, int mouseY) {}

    //powerup start
    private void checkPowerups() {
        int x = (int) (Math.random() * this.gameMap.getWidth());
        int y = (int) (Math.random() * this.gameMap.getHeight());
        if (this.gameMap.getTile(x, y).get().getTileType() == TileTypes.GROUND && this.gameMap.getTile(x, y).get().getTileObject() == null) {
            this.gameMap.getTile(x, y).get().spawnPowerup();
        } else {
            this.checkPowerups();
        }
    }
    //powerup end

    public void setMapIndex(int index){
        //TODO I don't care HuiBoo
    }

}
