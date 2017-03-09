package bomberman.network;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        Thread thread = new Thread(new NetworkController(false));
        thread.start();
    }

}
