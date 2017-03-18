package bomberman;

import bomberman.gameplay.GameplayManager;
import bomberman.view.engine.Config;
import bomberman.view.engine.ViewManager;
import bomberman.view.views.HomeView;
import com.google.gson.Gson;

import java.io.*;

public class Main {

    public static Main instance;

    public static void main(String[] args) {
        instance = new Main();
        instance.mainLoop();
    }

    private ViewManager viewManager;
    private GameplayManager gameplayManager;

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

        this.gameplayManager = new GameplayManager();
        this.viewManager = new ViewManager(this.gameplayManager);

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

            gameplayManager.update(deltaTime);

            viewManager.render(deltaTime, fpsCounter);
        }

        viewManager.dispose();

        Gson gson = new Gson();
        try {
            FileWriter writer = new FileWriter(configFile);
            gson.toJson(this.config, writer);
            writer.close();
        } catch (IOException e) {
        }
    }

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
}
