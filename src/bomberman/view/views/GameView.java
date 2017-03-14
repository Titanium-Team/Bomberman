package bomberman.view.views;

import bomberman.gameplay.GameMap;
import bomberman.gameplay.GameplayManager;
import bomberman.gameplay.Player;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.utils.Location;
import bomberman.view.engine.Light;
import bomberman.view.engine.LightingView;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.utility.Camera;
import bomberman.view.engine.utility.Vector2;
import org.lwjgl.input.Mouse;

import java.util.Random;

/**
 * Ingame View
 **/
public class GameView extends LightingView {

    private GameplayManager gameplayManager;
    private float time = 0f;
    private static final Random random = new Random();
    private int tileSize = 50;

    public GameView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);
    }

    public GameplayManager getGameplayManager() {
        return gameplayManager;
    }

    public void setGameplayManager(GameplayManager gameplayManager) {
        this.gameplayManager = gameplayManager;
    }

    public void update(float deltaTime) {
        time += deltaTime;

        Player localPlayer = gameplayManager.getLocalPlayer();
        Location center = localPlayer.getBoundingBox().getCenter();
        this.getSceneCamera().setTranslation(new Vector2((float) center.getX(), (float) center.getY()));
    }

    @Override
    public void renderOccluders(Batch batch, Camera camera) {
        GameMap map = gameplayManager.getMap(0);
        Tile[][] tiles = map.getTiles();
        for (int i = Math.max(0, (int) (camera.getTranslation().getX() - camera.getWidth() / 2) / this.tileSize); i < tiles.length &&
                i * this.tileSize < camera.getTranslation().getX() + camera.getWidth() / 2; i++) {
            if (tiles[i] != null) {
                for (int j = Math.max(0, (int) (camera.getTranslation().getY() - camera.getHeight() / 2) / this.tileSize); j < tiles[i].length &&
                        j * this.tileSize < camera.getTranslation().getY() + camera.getHeight() / 2; j++) {
                    if (tiles[i][j] != null) {
                        if (!tiles[i][j].getTileType().isWalkable()) {
                            batch.draw(null, i * this.tileSize, j * this.tileSize, this.tileSize, this.tileSize, 0.5f, 0.5f, 0.5f, 1);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void renderNonOccluders(Batch batch) {

    }

    public void onMouseDown(int button, int mouseX, int mouseY) {
        if (button == 0) {
            this.addLight(randomLight(getSceneCamera().getTranslation().getX() - (getSceneCamera().getWidth() / 2) + mouseX, getSceneCamera().getTranslation().getY() - (getSceneCamera().getHeight() / 2) + mouseY));
        }
        if (button == 1) {
            this.clearLights();
        }
    }

    public void onMouseUp(int button, int mouseX, int mouseY) {
    }

    private Light randomLight(float x, float y) {
        float r = random.nextFloat() / 2 + 0.5f;
        float g = random.nextFloat() / 2 + 0.5f;
        float b = random.nextFloat() / 2 + 0.5f;

        return new Light(x, y, 400, r, g, b);
    }


}