package bomberman.view.views;

import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;

/** Choose character and "host game" or "join game" **/
/** leads to LobbyView**/
public class PlayMenuView extends LightingView {


    public PlayMenuView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);
    }

    @Override
    public void renderOccluders(Batch batch) {

    }

    @Override
    public void renderNonOccluders(Batch batch) {

    }
}