package bomberman.view.views;

import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;

/** shows Players and GameSettings (not the Options)**/
/** leads to GameView**/
public class LobbyView extends LightingView {


    public LobbyView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);
    }

    @Override
    public void renderOccluders(Batch batch) {

    }

    @Override
    public void renderNonOccluders(Batch batch) {

    }
}