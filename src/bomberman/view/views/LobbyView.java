package bomberman.view.views;

import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.Button;
import bomberman.view.engine.components.LayoutParams;
import bomberman.view.engine.components.Panel;
import bomberman.view.engine.components.TextField;

/**
 * shows Players and GameSettings (not the Options)
 **/

/**
 * leads to GameView
 **/
public class LobbyView extends BaseMenuView {

    private Button startButton;

    private TextField mapIndexTextField, playerColorTextField;
    private Panel gameOptionsPanel;

    public LobbyView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);

        this.startButton = new Button(LayoutParams.obtain(0.4f, 0.6f, 0.2f, 0.1f), this, "Start Game");
        this.startButton.setListener(() -> LobbyView.this.changeView(GameView.class));
        this.getRoot().addChild(startButton);


        //Game Options Panel :
        this.gameOptionsPanel = new Panel(LayoutParams.obtain(0.7f,0f,0.3f,1f),this);
        this.getRoot().addChild(gameOptionsPanel);

        this.mapIndexTextField = new TextField(LayoutParams.obtain(0f , 0f , 1f , 0f ),this,"","Map Index");
        this.gameOptionsPanel.addChild(mapIndexTextField);

        //TODO Color anzeigen, und so .... mit Buttón eventuell.
        this.playerColorTextField = new TextField(LayoutParams.obtain(0f , 0f , 1f , 0f ),this,"","Player Color");
        this.gameOptionsPanel.addChild(playerColorTextField);



        float gameOptionsPanelChildrenHeight = (1/(float)gameOptionsPanel.getChildren().size());
        for(int i = 0 ; i < gameOptionsPanel.getChildren().size(); i++){
            gameOptionsPanel.getChildren().get(i).setParams(LayoutParams.obtain(0f,( i * gameOptionsPanel.getParams().h * gameOptionsPanelChildrenHeight), 1f, gameOptionsPanelChildrenHeight));
        }
        //Game Options Panel End


    }

}