package bomberman.view.views;

import bomberman.gameplay.GameMap;
import bomberman.gameplay.GameplayManager;
import bomberman.gameplay.Player;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileTypes;
import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.tile.objects.Explosion;
import bomberman.gameplay.tile.objects.PowerUp;
import bomberman.gameplay.utils.BoundingBox;
import bomberman.gameplay.utils.Location;
import bomberman.view.engine.Light;
import bomberman.view.engine.LightingView;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.utility.Camera;
import bomberman.view.engine.utility.Vector2;

import java.util.*;

/**
 * Ingame View
 **/
public class GameView extends LightingView {

    private GameplayManager gameplayManager;
    private float time = 0f;
    private static final Random random = new Random();
    private int tileSize = 50;
    private HashMap<Player, Light> playerLightMap = new HashMap<>();
    private List<Light> explosions = new ArrayList<>();

    public GameView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);
    }

    public GameplayManager getGameplayManager() {
        return gameplayManager;
    }

    public void setGameplayManager(GameplayManager gameplayManager) {
        this.gameplayManager = gameplayManager;
        playerLightMap.clear();
        for (int i = 0; i < gameplayManager.getPlayers().size(); i++) {
            Location playerLocation = gameplayManager.getPlayer(i).getBoundingBox().getCenter();
            Light playerLight = randomLight(((float) playerLocation.getX()) * tileSize, ((float) playerLocation.getY()) * tileSize);
            playerLightMap.put(gameplayManager.getPlayer(i), playerLight);
            this.addLight(playerLight);
        }
        gameplayManager.setGameState(GameplayManager.GameState.IN_GAME);
    }

    public void update(float deltaTime) {
        time += deltaTime;

        Player localPlayer = gameplayManager.getLocalPlayer();
        Location center = localPlayer.getBoundingBox().getCenter();
        this.getSceneCamera().setTranslation(new Vector2((float) center.getX() * tileSize, (float) center.getY() * tileSize));

        for (Map.Entry<Player, Light> e : playerLightMap.entrySet()) {
            Light playerLight = e.getValue();
            Location playerLocation = e.getKey().getBoundingBox().getCenter();
            playerLight.setX(((float) playerLocation.getX()) * tileSize);
            playerLight.setY(((float) playerLocation.getY()) * tileSize);
        }
    }

    @Override
    public void renderOccluders(Batch batch, Camera camera) {
        GameMap map = gameplayManager.getCurrentMap();
        Tile[][] tiles = map.getTiles();
        for (int i = Math.max(0, (int) (camera.getTranslation().getX() - camera.getWidth() / 2) / this.tileSize); i < tiles.length &&
                i * this.tileSize < camera.getTranslation().getX() + camera.getWidth() / 2; i++) {
            if (tiles[i] != null) {
                for (int j = Math.max(0, (int) (camera.getTranslation().getY() - camera.getHeight() / 2) / this.tileSize); j < tiles[i].length &&
                        j * this.tileSize < camera.getTranslation().getY() + camera.getHeight() / 2; j++) {
                    if (tiles[i][j] != null) {
                        if (!tiles[i][j].getTileType().isWalkable()) {
                            if (tiles[i][j].getTileType().equals(TileTypes.WALL)) {
                                batch.draw(null, i * this.tileSize, j * this.tileSize, this.tileSize, this.tileSize, 0.5f, 0.5f, 0.5f, 1);
                            } else {
                                batch.draw(null, i * this.tileSize, j * this.tileSize, this.tileSize, this.tileSize, 0.25f, 0.25f, 0.25f, 1);
                            }
                        } else {
                        }
                    }
                }
            }
        }
    }

    @Override
    public void renderNonOccluders(Batch batch, Camera camera) {
        GameMap map = gameplayManager.getCurrentMap();
        Tile[][] tiles = map.getTiles();
        for (int i = Math.max(0, (int) (camera.getTranslation().getX() - camera.getWidth() / 2) / this.tileSize); i < tiles.length &&
                i * this.tileSize < camera.getTranslation().getX() + camera.getWidth() / 2; i++) {
            if (tiles[i] != null) {
                for (int j = Math.max(0, (int) (camera.getTranslation().getY() - camera.getHeight() / 2) / this.tileSize); j < tiles[i].length &&
                        j * this.tileSize < camera.getTranslation().getY() + camera.getHeight() / 2; j++) {
                    if (tiles[i][j] != null) {
                        if (tiles[i][j].getTileType().isWalkable()) {
                            //batch.draw(null, i * this.tileSize, j * this.tileSize, this.tileSize, this.tileSize, 0.25f, 0.5f, 0.75f, 1);
                        }
                        if (tiles[i][j].getTileType().isWalkable()) {
                            if (tiles[i][j].getTileObject() != null) {
                                if (tiles[i][j].getTileObject() instanceof Bomb) {
                                    //TODO: bomb texture
                                    batch.draw(null, i * this.tileSize, j * this.tileSize, this.tileSize, this.tileSize, 1, 1, 1, 1);
                                } else if (tiles[i][j].getTileObject() instanceof PowerUp) {
                                    //TODO:textures
                                    batch.draw(null, i * this.tileSize, j * this.tileSize, this.tileSize, this.tileSize, 0.478f, 0.567f, 0.32f, 1);
                                } else if (tiles[i][j].getTileObject() instanceof Explosion) {
                                    //TODO:Explosion textures
                                    batch.draw(((Explosion) tiles[i][j].getTileObject()).getAnimation(), i * this.tileSize, j * this.tileSize, this.tileSize, this.tileSize, 1, 1, 1, 1);

                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < gameplayManager.getPlayers().size(); i++) {
            Player player = gameplayManager.getPlayer(i);
            BoundingBox b = player.getBoundingBox();

            batch.draw(null, (float) b.getMin().getX() * tileSize, (float) b.getMin().getY() * tileSize, (float) b.getWidth() * tileSize, (float) b.getHeight() * tileSize);
        }
    }

    private Light randomLight(float x, float y) {
        float r = random.nextFloat() / 2 + 0.5f;
        float g = random.nextFloat() / 2 + 0.5f;
        float b = random.nextFloat() / 2 + 0.5f;

        return new Light(x, y, 400, r, g, b);
    }

}