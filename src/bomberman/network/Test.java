package bomberman.network;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        NetworkController networkController = new NetworkController(args[0].equals("true"));

        Thread thread = new Thread(networkController);
        thread.start();
    }

}
