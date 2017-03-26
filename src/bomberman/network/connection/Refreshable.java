package bomberman.network.connection;

import bomberman.network.ConnectionData;

import java.util.List;

public interface Refreshable {
    void refreshListView(List<ConnectionData> connectionDataList);
}
