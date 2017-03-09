package bomberman.network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public abstract class Connection {

    private DatagramSocket socket;

    private Thread listener;
    private NetworkController controller;

    public Connection(NetworkController controller) {
        this.controller = controller;

        listener = new Thread(() -> {
            while (true){
                try {
                    listen();
                    Thread.sleep(0);
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public Thread getListener() {
        return listener;
    }

    public NetworkController getController() {
        return controller;
    }

    public void close() {
        getSocket().close();
        getListener().interrupt();
    }

    abstract void update();
    abstract void message(String message);
    abstract void listen();

}
