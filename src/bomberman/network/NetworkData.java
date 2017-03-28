package bomberman.network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkData {

    private final InetAddress ip;
    private final int port;

    public NetworkData(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public NetworkData(String json){
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();

        Map<String, String> jsonMap = gson.fromJson(json, type);

        InetAddress tempIp = null;
        try {
            tempIp = InetAddress.getByName(jsonMap.get("ip"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        ip = tempIp;

        port = Integer.parseInt(jsonMap.get("port"));

    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 37 * hash + ip.getHostAddress().hashCode();
        hash = 37 * hash + port;

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof NetworkData)){
            return false;
        }

        NetworkData networkData = ((NetworkData) obj);

        return ip.getHostAddress().equals(networkData.getIp().getHostAddress()) && port == networkData.getPort();
    }

    public String toJson() {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("ip", ip.getHostAddress());
        jsonMap.put("port", String.valueOf(port));

        Gson gson = new Gson();
        return gson.toJson(jsonMap);
    }
}
