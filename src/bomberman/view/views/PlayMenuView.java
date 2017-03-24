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

    private Button hostGameButton;
    private Button refreshServerListButton;
    private TextField portTextField;

    private VerticalView serverList;
    private List<Button> serverButtons = new ArrayList<>();

    public PlayMenuView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);

        this.portTextField = new TextField(LayoutParams.obtain(0.1f, 0.25f, 0.2f, 0.1f), this, "1638", "Port Number");
        this.getRoot().addChild(portTextField);

        this.hostGameButton = new Button(LayoutParams.obtain(0.1f, 0.4f, 0.2f, 0.1f), this, "Host Game");
        this.hostGameButton.addListener(() -> {
            PlayMenuView.this.changeView(LobbyView.class);

            if (portTextField.getText() != "") {
                Main.instance.getNetworkController().startServer(Integer.parseInt(portTextField.getText()));
            }else {
                Main.instance.getNetworkController().startServer();
            }
        });
        this.getRoot().addChild(hostGameButton);

        this.serverList = new VerticalView(LayoutParams.obtain(0.55f, 0.05f, 0.4f, 0.8f), this);
        this.getRoot().addChild(serverList);

        this.refreshServerListButton = new Button(LayoutParams.obtain(0.55f, 0.85f, 0.4f, 0.1f), this, "Refresh Server List");
        this.refreshServerListButton.addListener(() -> {
            updateVerticalView();
        });
        this.getRoot().addChild(refreshServerListButton);

        updateVerticalView();
    }

    private void updateVerticalView() {
        List<ConnectionData> list = Main.instance.getNetworkController().getServerList();

        for (int i = 0; i < serverButtons.size(); i++) {
            if (serverList.getChildren().contains(serverButtons.get(i)))
                serverList.removeChild(serverButtons.get(i));
        }
        serverButtons.clear();

        for (int i = 0; i < list.size(); i++) {
            String text = list.get(i).getNetworkData().getIp().getHostAddress();
            NetworkData data = list.get(i).getNetworkData();
            if (!serverButtons.contains(text)) {
                Button button = new Button(LayoutParams.obtain(0f, 0f, 0f, 0f), this, text);
                button.addListener(() -> Main.instance.getNetworkController().joinServer(data));

                this.changeView(LobbyView.class);

                serverButtons.add(button);
            }
        }
        for (int i = 0; i < serverButtons.size(); i++) {
            if (!serverList.getChildren().contains(serverButtons.get(i)))
                serverList.addChild(serverButtons.get(i));
        }
    }

}