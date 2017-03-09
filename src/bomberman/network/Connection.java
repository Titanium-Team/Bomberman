package bomberman.network;

public interface Connection {

    void close();

    void update();

    void message(String message);

}
