package bomberman.view.views;

import bomberman.gameplay.GameplayManager;
import bomberman.view.engine.LightingView;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;

/**
 * Ingame View
 **/
public class GameView extends LightingView {

    private GameplayManager gameplayManager;

    public GameView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);
    }

    public GameplayManager getGameplayManager() {
        return gameplayManager;
    }

    public void setGameplayManager(GameplayManager gameplayManager) {
        this.gameplayManager = gameplayManager;
    }

    @Override
    public void renderOccluders(Batch batch) {

    }

    @Override
    public void renderNonOccluders(Batch batch) {

    }
}