package bomberman.view.views;

import bomberman.Main;
import bomberman.network.NetworkData;
import bomberman.network.ServerConnectionData;
import bomberman.network.connection.RefreshableServerList;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.*;
import org.lwjgl.input.Keyboard;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Choose character and "host game" or "join game"
 **/

/**
 * leads to LobbyView
 **/
public class PlayMenuView extends BaseMenuView implements RefreshableServerList {

    private TextField serverIpField;
    private TextField playerNameField;
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
        this.portTextField.setFilterOnlyNumbers();
        this.portTextField.addTypeListeners((key, c) -> {
            hostGameButton.setClickable(Main.instance.getNetworkController().isHostable(Integer.parseInt(portTextField.getText())));
        });

        this.serverNameField = new TextField(LayoutParams.obtain(0.1f, 0.4f, 0.2f, 0.1f), this, "", "Server Name");
        this.getRoot().addChild(serverNameField);

        this.playerNameField = new TextField(LayoutParams.obtain(0.1f,0.55f,0.2f,0.1f),this,"","Player Name");
        this.getRoot().addChild(playerNameField);

        this.serverIpField = new TextField(LayoutParams.obtain(0.1f,0.7f,0.2f,0.1f),this,"","Server Ip");
        this.getRoot().addChild(serverIpField);
        this.serverIpField.setFilterOnlyNumbers();
        this.serverIpField.addFilterChar('.');
        this.serverIpField.addTypeListeners((key, c) -> {
            if (key == Keyboard.KEY_RETURN){
                try {
                    Main.instance.getGameplayManager().getCurrentSession().getLocalPlayer().setName(playerNameField.getText());
                    Main.instance.getNetworkController().joinServer(new NetworkData(InetAddress.getByName(serverIpField.getText()), Integer.parseInt(portTextField.getText())));

                    PlayMenuView.this.changeView(LobbyView.class);
                } catch (UnknownHostException e) {
                    this.displayError("IP und Port passen nicht zusammen");
                }
            }
        });

        this.hostGameButton = new Button(LayoutParams.obtain(0.1f, 0.85f, 0.2f, 0.1f), this, "Host Game");
        this.hostGameButton.addListener(() -> {
            String serverName = serverNameField.getText();
            if (portTextField.getText() != "") {
                Main.instance.getNetworkController().startServer(serverName, Integer.parseInt(portTextField.getText()));
                Main.instance.getGameplayManager().getCurrentSession().getLocalPlayer().setName(playerNameField.getText());
            } else {
                Main.instance.getNetworkController().startServer(serverName);
                Main.instance.getGameplayManager().getCurrentSession().getLocalPlayer().setName(playerNameField.getText());
            }

            PlayMenuView.this.changeView(LobbyView.class);
        });
        this.getRoot().addChild(hostGameButton);
        this.hostGameButton.setClickable(Main.instance.getNetworkController().isHostable(Integer.parseInt(portTextField.getText())));

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
                    Main.instance.getGameplayManager().getCurrentSession().getLocalPlayer().setName(playerNameField.getText());

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