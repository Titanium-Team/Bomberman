package bomberman.view.views;

import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;

/** shows gamesettings **/
public class OptionsView extends LightingView {


    public OptionsView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);
    }

    @Override
    public void renderOccluders(Batch batch) {

    }

    @Override
    public void renderNonOccluders(Batch batch) {

    }
}