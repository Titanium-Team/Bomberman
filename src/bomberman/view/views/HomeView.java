package bomberman.view.views;

import bomberman.Main;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.Button;
import bomberman.view.engine.components.ClickListener;
import bomberman.view.engine.components.LayoutParams;
import org.lwjgl.opengl.Display;

/**
 * View for Start
 **/

/**
 * leads to OptionsView & PlayMenuView
 **/
public class HomeView extends BaseMenuView {

    private Button optionsButton, playButton,testButton;

    public HomeView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);

        this.backButton.setText("Exit");
        this.backButton.addListener(() -> {
            Main.instance.requestClose();
        });

        this.playButton = new Button(LayoutParams.obtain(0.4f, 0.4f, 0.2f, 0.1f), this, "Play");
        this.playButton.addListener(() -> HomeView.this.changeView(PlayMenuView.class));
        this.getRoot().addChild(playButton);

        this.optionsButton = new Button(LayoutParams.obtain(0.4f, 0.6f, 0.2f, 0.1f), this, "Options");
        this.optionsButton.addListener(() -> HomeView.this.changeView(OptionsView.class));
        this.getRoot().addChild(optionsButton);

        this.testButton = new Button(LayoutParams.obtain(0.4f, 0.8f, 0.2f, 0.1f), this, "Test");
        this.testButton.addListener(() -> HomeView.this.changeView(TestView.class));
        this.getRoot().addChild(testButton);
    }

}
