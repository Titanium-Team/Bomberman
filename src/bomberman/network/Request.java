package bomberman.network;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Request {

    private Map<String, Boolean> recieved;
    private final String request;

    public Request(String request, Set<String> send) {
        this.request = request;
        this.recieved = new HashMap<>();

        for (String s: send){
            recieved.put(s, false);
        }
    }

    public void setRecieved(String key){
        recieved.put(key, true);
    }

    public String getRequest() {
        return request;
    }
}
