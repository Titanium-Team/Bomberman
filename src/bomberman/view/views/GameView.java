package bomberman.view.views;

import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;

/**Ingame View **/
public class GameView extends LightingView {


    public GameView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);
    }

    @Override
    public void renderOccluders(Batch batch) {

    }

    @Override
    public void renderNonOccluders(Batch batch) {

    }
}