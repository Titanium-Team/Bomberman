package bomberman.network;

import java.io.IOException;
import java.net.*;
import java.util.Date;

public class NetworkController implements Runnable{

    private boolean hosting = false;
    private Connection connection;

    public NetworkController(boolean hosting) throws IOException {
        this.hosting = hosting;

        if (hosting){
            connection = new Server();
        }else {
            connection = new Client();
        }
    }

    @Override
    public void run() {
        connection.message("Test");
    }
}
