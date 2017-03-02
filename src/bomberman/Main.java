package bomberman;

import bomberman.view.engine.TestView;
import bomberman.view.engine.View;
import bomberman.view.engine.ViewManager;

public class Main {

    public static Main instance;

    public static void main(String[] args) {
        instance = new Main();
        instance.mainLoop();
    }

    private ViewManager viewManager;

    public void mainLoop() {
        viewManager = new ViewManager();

        View view = new TestView(viewManager.getViewportWidth(), viewManager.getViewportHeight(), viewManager);
        viewManager.setCurrentView(view);

        float deltaTime;
        long startTime = System.nanoTime();
        long totalTime = startTime;
        int frames = 0;
        while (!viewManager.isCloseRequested()) {
            long currentTime = System.nanoTime();
            deltaTime = (float) ((currentTime - startTime) / 1000000000D);
            startTime = currentTime;
            frames++;
            if (currentTime - totalTime > 1000000000) {
                System.out.println("fps: " + frames);
                frames = 0;

                totalTime = currentTime;
            }

            viewManager.render(deltaTime);
        }

        viewManager.dispose();
    }

    public ViewManager getViewManager() {
        return viewManager;
    }

}
