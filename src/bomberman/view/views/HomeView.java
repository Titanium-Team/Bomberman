package bomberman.view.views;

import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.Button;
import bomberman.view.engine.components.LayoutParams;

/**
 * View for Start
 **/

/**
 * leads to OptionsView & PlayMenuView
 **/
public class HomeView extends BaseMenuView {

    private Button optionsButton, playButton;

    public HomeView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);

        this.getRoot().removeChild(this.backButton);

        this.playButton = new Button(LayoutParams.obtain(0.4f, 0.4f, 0.2f, 0.1f), this, "Play");
        this.playButton.setListener(() -> HomeView.this.changeView(PlayMenuView.class));
        this.getRoot().addChild(playButton);

        this.optionsButton = new Button(LayoutParams.obtain(0.4f, 0.6f, 0.2f, 0.1f), this, "Options");
        this.optionsButton.setListener(() -> HomeView.this.changeView(OptionsView.class));
        this.getRoot().addChild(optionsButton);
    }

}
