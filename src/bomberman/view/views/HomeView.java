package bomberman.view.views;

import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;

/** View for Start **/
/** leads to OptionsView & PlayMenuView**/
public class HomeView extends LightingView {


    public HomeView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);
    }

    @Override
    public void renderOccluders(Batch batch) {

    }

    @Override
    public void renderNonOccluders(Batch batch) {

    }
}
