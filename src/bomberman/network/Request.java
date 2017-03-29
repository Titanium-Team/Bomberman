package bomberman.network;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Request {

    private Map<NetworkData, Boolean> recieved;
    private final String request;
    private final boolean resend;

    public Request(String request, Set<NetworkData> send, boolean resend) {
        this.request = request;
        this.recieved = new HashMap<>();
        this.resend = resend;

        for (NetworkData s : send) {
            recieved.put(s, false);
        }
    }

    public void setRecieved(NetworkData key) {
        recieved.put(key, true);
    }

    public boolean isRecieved(NetworkData key) {
        Boolean b = recieved.get(key);

        return b != null ? b : true;
    }

    public String getRequest() {
        return request;
    }

    public boolean isResend() {
        return resend;
    }

    public boolean allRecieved(){
        for (boolean b : recieved.values()){
            if (!b){
                return false;
            }
        }

        return true;
    }
}
