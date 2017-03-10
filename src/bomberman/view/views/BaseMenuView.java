package bomberman.view.views;

import bomberman.view.engine.LightingView;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;

public abstract class BaseMenuView extends LightingView {

    public BaseMenuView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);
    }

    @Override
    public void renderOccluders(Batch batch) {

    }

    @Override
    public void renderNonOccluders(Batch batch) {

    }
}
