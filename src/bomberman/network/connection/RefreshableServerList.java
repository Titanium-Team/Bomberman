package bomberman.network.connection;

import bomberman.network.ConnectionData;
import bomberman.network.ServerConnectionData;

import java.util.List;

public interface RefreshableServerList {
    void refreshListView(List<ServerConnectionData> connectionDataList);
}
