package bomberman.network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.PublicKey;
import java.util.Map;

public class ServerConnectionData extends ConnectionData{

    private String name;

    public ServerConnectionData(NetworkData networkData, PublicKey publicKey, String name) {
        super(networkData, publicKey);

        this.name = name;
    }

    public ServerConnectionData(NetworkData networkData, String json, String name) {
        super(networkData, json);

        this.name = name;
    }

    public String getName() {
        return name;
    }
}
