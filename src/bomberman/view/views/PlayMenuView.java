package bomberman.view.views;

import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.Button;
import bomberman.view.engine.components.LayoutParams;
import bomberman.view.engine.components.TextField;
import bomberman.view.engine.components.VerticalView;

import java.util.ArrayList;
import java.util.List;

/**
 * Choose character and "host game" or "join game"
 **/

/**
 * leads to LobbyView
 **/
public class PlayMenuView extends BaseMenuView {

    private Button hostGameButton, joinGameButton;
    private TextField portTextField;

    private VerticalView scurrMitDemVert;
    private List<Button> serverButtons = new ArrayList<>();


    public PlayMenuView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);

        this.hostGameButton = new Button(LayoutParams.obtain(0.4f, 0.4f, 0.2f, 0.1f), this, "Host Game");
        this.hostGameButton.addListener(() -> PlayMenuView.this.changeView(LobbyView.class));
        this.getRoot().addChild(hostGameButton);

        this.joinGameButton = new Button(LayoutParams.obtain(0.4f, 0.6f, 0.2f, 0.1f), this, "Join Game");
        this.joinGameButton.addListener(() -> PlayMenuView.this.changeView(LobbyView.class));
        this.getRoot().addChild(joinGameButton);

        this.portTextField = new TextField(LayoutParams.obtain(0f, 0.5f, 0.25f, 0.1f), this, "", "Port Number");

        this.getRoot().addChild(portTextField);

        this.scurrMitDemVert = new VerticalView(LayoutParams.obtain(0.7f,0f,0.3f,1f), this);
        this.getRoot().addChild(scurrMitDemVert);


    }

    public void listToVerticalView(ArrayList l){
        for(int i = 0 ; i < l.size() ; i++){
            String text = "test";
            serverButtons.add(new Button(LayoutParams.obtain(0f , 0f, 0f, 0f), this, text));
        }
        for(int i = 0 ; i < serverButtons.size() ; i++){
            scurrMitDemVert.addChild(serverButtons.get(i));
        }
    }

}