package bomberman.network;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Request {

    private Map<NetworkData, Boolean> recieved;
    private final String request;

    public Request(String request, Set<NetworkData> send) {
        this.request = request;
        this.recieved = new HashMap<>();

        for (NetworkData s : send) {
            recieved.put(s, false);
        }
    }

    public void setRecieved(NetworkData key) {
        recieved.put(key, true);
    }

    public String getRequest() {
        return request;
    }
}
