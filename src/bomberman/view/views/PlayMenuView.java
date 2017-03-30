package bomberman.view.views;

import bomberman.Main;
import bomberman.network.ServerConnectionData;
import bomberman.network.connection.RefreshableServerList;
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
public class PlayMenuView extends BaseMenuView implements RefreshableServerList {

    private Button hostGameButton;
    private Button refreshServerListButton;
    private TextField portTextField;
    private TextField serverNameField;

    private VerticalList serverList;
    private List<Button> serverButtons = new ArrayList<>();

    public PlayMenuView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);

        this.portTextField = new TextField(LayoutParams.obtain(0.1f, 0.25f, 0.2f, 0.1f), this, "1638", "Port Number");
        this.getRoot().addChild(portTextField);

        this.serverNameField = new TextField(LayoutParams.obtain(0.1f, 0.4f, 0.2f, 0.1f), this, "", "Server Name");
        this.getRoot().addChild(serverNameField);

        this.hostGameButton = new Button(LayoutParams.obtain(0.1f, 0.55f, 0.2f, 0.1f), this, "Host Game");
        this.hostGameButton.addListener(() -> {
            String serverName = serverNameField.getText();
            if (portTextField.getText() != "") {
                Main.instance.getNetworkController().startServer(serverName, Integer.parseInt(portTextField.getText()));
            }else {
                Main.instance.getNetworkController().startServer(serverName);
            }

            PlayMenuView.this.changeView(LobbyView.class);
        });
        this.getRoot().addChild(hostGameButton);

        this.serverList = new VerticalList(LayoutParams.obtain(0.55f, 0.05f, 0.4f, 0.8f), this);
        this.getRoot().addChild(serverList);

        this.refreshServerListButton = new Button(LayoutParams.obtain(0.55f, 0.85f, 0.4f, 0.1f), this, "Refresh Server List");
        this.refreshServerListButton.addListener(() -> {
            Main.instance.getNetworkController().refreshServers(this);
        });
        this.getRoot().addChild(refreshServerListButton);

        refreshListView(Main.instance.getNetworkController().getServerList());
    }

    @Override
    public void refreshListView(List<ServerConnectionData> connectionDataList) {
        for (int i = 0; i < serverButtons.size(); i++) {
            if (serverList.getChildren().contains(serverButtons.get(i)))
                serverList.removeChild(serverButtons.get(i));
        }
        serverButtons.clear();

        for (int i = 0; i < connectionDataList.size(); i++) {
            String text = connectionDataList.get(i).getName();
            ServerConnectionData data = connectionDataList.get(i);
            if (!serverButtons.contains(text)) {
                Button button = new Button(LayoutParams.obtain(0f, 0f, 0f, 0f), this, text);
                button.addListener(() -> {
                    Main.instance.getNetworkController().joinServer(data);

                    this.changeView(LobbyView.class);
                });

                serverButtons.add(button);
            }
        }
        for (int i = 0; i < serverButtons.size(); i++) {
            if (!serverList.getChildren().contains(serverButtons.get(i)))
                serverList.addChild(serverButtons.get(i));
        }
    }
}