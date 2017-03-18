package bomberman.view.views;

import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.Button;
import bomberman.view.engine.components.LayoutParams;
import bomberman.view.engine.components.TextField;

/**
 * shows Players and GameSettings (not the Options)
 **/

/**
 * leads to GameView
 **/
public class LobbyView extends BaseMenuView {

    private Button startButton;

    private TextField mapSizeTextField;

    public LobbyView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);

        this.startButton = new Button(LayoutParams.obtain(0.4f, 0.6f, 0.2f, 0.1f), this, "Start Game");
        this.startButton.setListener(() -> LobbyView.this.changeView(GameView.class));
        this.getRoot().addChild(startButton);
    }

}