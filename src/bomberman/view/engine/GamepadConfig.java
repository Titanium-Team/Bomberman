package bomberman.view.engine;

import net.java.games.input.Controller;
import net.java.games.input.Event;

public class GamepadConfig {

    public enum Action {
        Accept, Back;
    }

    private Controller controller;

    public GamepadConfig(Controller controller) {
        this.controller = controller;
    }

    public Action getAction(Event event) {
        switch (event.getComponent().getIdentifier().getName()) {
            case "0":
                return Action.Accept;
            case "1":
                return Action.Back;
        }

        return null;
    }
}
