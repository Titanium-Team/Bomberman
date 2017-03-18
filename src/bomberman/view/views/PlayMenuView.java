package bomberman.view.views;

import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.Button;
import bomberman.view.engine.components.LayoutParams;
import bomberman.view.engine.components.TextField;

/**
 * Choose character and "host game" or "join game"
 **/

/**
 * leads to LobbyView
 **/
public class PlayMenuView extends BaseMenuView {

    private Button hostGameButton, joinGameButton;
    private TextField portTextField;

    public PlayMenuView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);

        this.hostGameButton = new Button(LayoutParams.obtain(0.4f, 0.4f, 0.2f, 0.1f), this, "Host Game");
        this.hostGameButton.setListener(() -> PlayMenuView.this.changeView(LobbyView.class));
        this.getRoot().addChild(hostGameButton);

        this.joinGameButton = new Button(LayoutParams.obtain(0.4f, 0.6f, 0.2f, 0.1f), this, "Join Game");
        this.joinGameButton.setListener(() -> PlayMenuView.this.changeView(LobbyView.class));
        this.getRoot().addChild(joinGameButton);

        this.portTextField = new TextField(LayoutParams.obtain(0.7f, 0.5f, 0.2f, 0.1f), this, "PORT");

        this.getRoot().addChild(portTextField);
    }

}