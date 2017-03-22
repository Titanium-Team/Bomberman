package bomberman.view.views;

import bomberman.Main;
import bomberman.network.ConnectionData;
import bomberman.network.NetworkData;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.*;

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

        this.portTextField = new TextField(LayoutParams.obtain(0f, 0.5f, 0.25f, 0.1f), this, "", "Port Number");

        this.getRoot().addChild(portTextField);

        this.scurrMitDemVert = new VerticalView(LayoutParams.obtain(0.7f,0f,0.3f,1f), this);
        this.getRoot().addChild(scurrMitDemVert);



       updateVerticalView(Main.instance.getNetworkController().getServerList());


    }

    public void updateVerticalView(List<ConnectionData> list){
        for(int i = 0 ; i < list.size() ; i++){
            String text = list.get(i).getNetworkData().getIp().getHostAddress();
            NetworkData data = list.get(i).getNetworkData();
            if(!serverButtons.contains(text)) {
                Button button = new Button(LayoutParams.obtain(0f, 0f, 0f, 0f), this, text);
                button.addListener(new ClickListener() {
                    @Override
                    public void onClick() {
                        Main.instance.getNetworkController().joinServer(data);
                    }
                });
                serverButtons.add(button);
            }
        }
        for(int i = 0 ; i < serverButtons.size() ; i++){
            if(!scurrMitDemVert.getChildren().contains(serverButtons.get(i)))
                scurrMitDemVert.addChild(serverButtons.get(i));
        }
    }

}