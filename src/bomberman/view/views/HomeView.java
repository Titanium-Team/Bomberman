package bomberman.view.views;

import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.Button;
import bomberman.view.engine.components.ButtonListener;

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

        this.removeComponent(this.backButton);

        this.optionsButton = new Button(400, 100, 200, 50, this, "Options");
        this.optionsButton.setListener(new ButtonListener() {
            @Override
            public void onClick() {
                HomeView.this.changeView(OptionsView.class);
            }
        });
        this.addComponent(optionsButton);

        this.playButton = new Button(400, 200, 200, 50, this, "Play");
        this.playButton.setListener(new ButtonListener() {
            @Override
            public void onClick() {
                HomeView.this.changeView(GameView.class);
            }
        });
        this.addComponent(playButton);
    }

}
