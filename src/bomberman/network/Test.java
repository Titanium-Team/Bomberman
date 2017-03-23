package bomberman.network;

import java.io.IOException;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws IOException {
        NetworkController networkController = new NetworkController();

        if (args[0].equals("true")){
            networkController.startServer();
        }

        while (true) {
            Scanner scanner = new Scanner(System.in);
            networkController.chatMessage(scanner.nextLine());
        }
    }

}
