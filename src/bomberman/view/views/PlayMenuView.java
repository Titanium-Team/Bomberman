package bomberman.view.views;

import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.Button;
import bomberman.view.engine.components.ButtonListener;

/**
 * Choose character and "host game" or "join game"
 **/

/**
 * leads to LobbyView
 **/
public class PlayMenuView extends BaseMenuView {

    private Button hostGameButton, joinGameButton;


    public PlayMenuView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);

        this.hostGameButton = new Button(2/3, 1/3, 1/4, 1/6, this, "Host Game");
        this.hostGameButton.setListener(new ButtonListener() {
            @Override
            public void onClick() {
                PlayMenuView.this.changeView(LobbyView.class);
            }
        });
        this.addComponent(hostGameButton);

        this.joinGameButton = new Button(1/3, 1/3, 1/4, 1/6, this, "Join Game");
        this.joinGameButton.setListener(new ButtonListener() {
            @Override
            public void onClick() {

            }
        });
        this.addComponent(joinGameButton);
    }

}