package bomberman;

import bomberman.gameplay.GameplayManager;
import bomberman.network.NetworkController;
import bomberman.view.engine.Config;
import bomberman.view.engine.ViewManager;
import bomberman.view.views.GameView;
import bomberman.view.views.HomeView;
import com.google.gson.Gson;

import java.io.*;

public class Main {

    public static Main instance;

    public static void main(String[] args) {
        instance = new Main();
        instance.mainLoop();
    }

    private NetworkController networkController;
    private ViewManager viewManager;
    private GameplayManager gameplayManager;
    private float lastDeltaTime;
    private boolean closeRequested = false;

    private File saveDir;
    private Config config;

    public void mainLoop() {
        this.saveDir = new File("save/");
        this.saveDir.mkdir();

        final File configFile = new File(saveDir, "config.json");
        if (configFile.exists()) {
            Gson gson = new Gson();
            try {
                this.config = gson.fromJson(new FileReader(configFile), Config.class);
            } catch (FileNotFoundException e) {
            }
        } else {
            this.config = new Config();
        }

        this.networkController = new NetworkController();

        this.gameplayManager = new GameplayManager();

        this.viewManager = new ViewManager(this.gameplayManager);

        viewManager.setCurrentView(HomeView.class);

        float deltaTime;
        long startTime = System.nanoTime();
        long totalTime = startTime;
        int frames = 0;
        int fpsCounter = 0;
        while (!viewManager.isCloseRequested() && !closeRequested) {
            long currentTime = System.nanoTime();
            deltaTime = (float) ((currentTime - startTime) / 1000000000D);
            startTime = currentTime;
            frames++;
            if (currentTime - totalTime > 1000000000) {
                fpsCounter = frames;
                frames = 0;

                totalTime = currentTime;
            }

            // #hacky
            this.lastDeltaTime = deltaTime;

            viewManager.processInput();

            if (viewManager.getCurrentView() instanceof GameView) {
                gameplayManager.update(deltaTime);
            }

            viewManager.render(deltaTime, fpsCounter);
        }

        networkController.close();

        viewManager.dispose();

        Gson gson = new Gson();
        try {
            FileWriter writer = new FileWriter(configFile);
            gson.toJson(this.config, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetworkController getNetworkController() {return networkController;}

    public ViewManager getViewManager() {
        return viewManager;
    }

    public GameplayManager getGameplayManager() {
        return gameplayManager;
    }

    public Config getConfig() {
        return config;
    }

    public File getSaveDir() {
        return saveDir;
    }

    public float getLastDeltaTime() {
        return lastDeltaTime;
    }

    public void requestClose() {
        closeRequested = true;
    }
}
