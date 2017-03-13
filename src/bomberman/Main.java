package bomberman;

import bomberman.gameplay.GameplayManager;
import bomberman.view.views.HomeView;
import bomberman.view.engine.View;
import bomberman.view.engine.ViewManager;

public class Main {

    public static Main instance;

    public static void main(String[] args) {

        new GameplayManager();
        instance = new Main();
        instance.mainLoop();
    }

    private ViewManager viewManager;

    public void mainLoop() {
        viewManager = new ViewManager();

        viewManager.setCurrentView(HomeView.class);

        float deltaTime;
        long startTime = System.nanoTime();
        long totalTime = startTime;
        int frames = 0;
        int fpsCounter = 0;
        while (!viewManager.isCloseRequested()) {
            long currentTime = System.nanoTime();
            deltaTime = (float) ((currentTime - startTime) / 1000000000D);
            startTime = currentTime;
            frames++;
            if (currentTime - totalTime > 1000000000) {
                fpsCounter = frames;
                frames = 0;

                totalTime = currentTime;
            }

            viewManager.render(deltaTime, fpsCounter);
        }

        viewManager.dispose();
    }

    public ViewManager getViewManager() {
        return viewManager;
    }

}
