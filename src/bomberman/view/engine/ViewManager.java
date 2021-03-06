package bomberman.view.engine;

import bomberman.Main;
import bomberman.gameplay.GameplayManager;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.rendering.BitmapFont;
import bomberman.view.engine.rendering.ITexture;
import bomberman.view.engine.rendering.Texture;
import bomberman.view.views.GameView;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.DisplayMode;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

import net.java.games.input.EventQueue;

public class ViewManager {

    public static BitmapFont font;
    public static final HashMap<String, ITexture> textureMap = new HashMap<>();
    public static Sound clickSound;

    private Queue<Runnable> runnableQueue = new ArrayDeque<>();
    private Batch batch;

    private MSMode msMode = MSMode.OFF;

    public static void load() {
        try {
            font = new BitmapFont(ViewManager.class.getResource("/bomberman/resources/font/font.fnt"), ViewManager.class.getResource("/bomberman/resources/font/font.png"));

            clickSound = new Sound(ViewManager.class.getResourceAsStream("/bomberman/resources/sounds/click.wav"));

            //Explosion
            loadTexture("explosion.png");


            //Player
            loadTexture("DrunkBeer.png");
            loadTexture("BrownBear.png");
            loadTexture("SwagBear.png");
            loadTexture("YellowBear.png");


            //Bombs
            loadTexture("bomb.png");
            loadTexture("PowerBomb.png");
            loadTexture("SpikeBomb.png");



            //Tiles
            loadTexture("Oil.png");
            loadTexture("BreakableImprovedWall.png");
            loadTexture("wall.png");
            loadTexture("breakableWall.png");
            loadTexture("ground.png");
            loadTexture("teleport.png");
            loadTexture("arrow.png");



            //PowerUps
            loadTexture("BombUp.png");
            loadTexture("BombDown.png");
            loadTexture("FireUp.png");
            loadTexture("SpeedUp.png");
            loadTexture("SpeedDown.png");
            loadTexture("firedown.png");

        } catch (IOException e) {
            e.printStackTrace();
            font = null;
        }
    }

    private static void loadTexture(String id) {
        ITexture tex = new Texture(ViewManager.class.getResource("/bomberman/resources/textures/" + id));

        textureMap.put(id, tex);
    }

    public static ITexture getTexture(String textureID) {
        if (textureID == null || textureID.isEmpty())
            return null;

        if (!textureMap.containsKey(textureID)) {
            loadTexture(textureID);
        }

        return textureMap.get(textureID);
    }

    private View currentView;
    private GameplayManager gameplayManager;
    private boolean fullscreen = false;

    private Controller selectedGamepad = null;

    public ViewManager(GameplayManager gameplayManager) {
        this.gameplayManager = gameplayManager;

        try {
            System.out.print("Loading natives...");
            LwjglNativesLoader.load();
            System.out.println("finished!");

            try {
                if (fullscreen) {
                    int w0 = Toolkit.getDefaultToolkit().getScreenSize().width;
                    int h0 = Toolkit.getDefaultToolkit().getScreenSize().height;
                    setDisplayMode(w0, h0, true);
                } else {
                    setDisplayMode(800, 600, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Display.create(new PixelFormat(8, 0, 0, msMode.samples));

            System.out.println("OpenGL context created! Version: " + GL11.glGetString(GL11.GL_VERSION) + ", Vendor: " + GL11.glGetString(GL11.GL_VENDOR) + ", Renderer: " + GL11.glGetString(GL11.GL_RENDERER));
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        System.out.println("Loading Controllers...");
        ControllerEnvironment e = ControllerEnvironment.getDefaultEnvironment();
        Controller[] found = e.getControllers();

        for (int i = 0; i < found.length; i++) {
            Controller controller = found[i];

            if (controller.getType() == Controller.Type.GAMEPAD) {
                System.out.println("Found suitable Gamepad: " + controller.getName());

                this.selectedGamepad = controller;

                break;
            }
        }

        GL11.glEnable(GL13.GL_MULTISAMPLE);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        load();

        this.batch = new Batch();
    }

    public void setDisplayMode(int width, int height, boolean fullscreen) {
        if ((Display.getDisplayMode().getWidth() == width) &&
                (Display.getDisplayMode().getHeight() == height) &&
                (Display.isFullscreen() == fullscreen)) {
            return;
        }

        try {
            DisplayMode targetDisplayMode = null;

            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;

                for (int i = 0; i < modes.length; i++) {
                    DisplayMode current = modes[i];

                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }

                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
                                (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            } else {
                targetDisplayMode = new DisplayMode(width, height);
            }

            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
                return;
            }

            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);
            Display.setResizable(true);
            Display.setTitle("Boom-Bär-Man");
            //Display.setVSyncEnabled(Main.instance.getConfig().isvSync());
        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
        }
    }

    public void render(float deltaTime, int fpsCount) {
        if (Display.wasResized()) {
            onResize(Display.getWidth(), Display.getHeight());
        }

        GL11.glClearColor(0f, 0f, 0f, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        batch.begin();

        if (currentView != null) {
            currentView.update(deltaTime);
            currentView.render(batch);
        }

        while (!runnableQueue.isEmpty()) {
            runnableQueue.poll().run();
        }

        if (Main.instance.getConfig().isShowFPS()) {
            ViewManager.font.drawText(batch, "FPS: " + fpsCount, 5, 5);
        }

        batch.end();

        Display.update();
        if (Main.instance.getConfig().isvSync())
            Display.sync(60);
    }

    public void processInput() {
        while (Mouse.next()) {
            if (Mouse.getEventButtonState()) {
                int button = Mouse.getEventButton();
                int mouseX = Mouse.getEventX();
                int mouseY = Display.getHeight() - Mouse.getEventY();

                currentView.onMouseDown(button, mouseX, mouseY);
                gameplayManager.onMouseDown(button, mouseX, mouseY);
            } else {
                int button = Mouse.getEventButton();
                int mouseX = Mouse.getEventX();
                int mouseY = Display.getHeight() - Mouse.getEventY();

                if (button != -1) {
                    currentView.onMouseUp(button, mouseX, mouseY);
                    gameplayManager.onMouseUp(button, mouseX, mouseY);
                }
            }

            int wheel = Mouse.getEventDWheel();
            if (wheel != 0) {
                currentView.onMouseWheel(wheel);
            }
        }
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                int key = Keyboard.getEventKey();
                char c = Keyboard.getEventCharacter();

                currentView.onKeyDown(key, c);
                gameplayManager.onKeyDown(key, c);

                if (key == Keyboard.KEY_F11) {
                    fullscreen = !fullscreen;

                    try {
                        if (fullscreen) {
                            int w0 = Toolkit.getDefaultToolkit().getScreenSize().width;
                            int h0 = Toolkit.getDefaultToolkit().getScreenSize().height;
                            setDisplayMode(w0, h0, true);
                            onResize(w0, h0);
                        } else {
                            setDisplayMode(800, 600, false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                int key = Keyboard.getEventKey();
                char c = Keyboard.getEventCharacter();

                currentView.onKeyUp(key, c);
                gameplayManager.onKeyUp(key, c);
            }
        }

        if (selectedGamepad != null) {
            selectedGamepad.poll();
            EventQueue gamepadEventQueue = selectedGamepad.getEventQueue();
            Event event = new Event();
            while (gamepadEventQueue.getNextEvent(event)) {
                float value = event.getValue();

                currentView.onGamepadEvent(event.getComponent(), value);
                gameplayManager.onGamepadEvent(event.getComponent(), value);
            }
        }
    }

    private void onResize(int width, int height) {
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());

        if (currentView != null)
            currentView.layout(Display.getWidth(), Display.getHeight());

    }

    public void postOnUIThread(Runnable runnable) {
        runnableQueue.add(runnable);
    }

    public int getViewportWidth() {
        return Display.getWidth();
    }

    public int getViewportHeight() {
        return Display.getHeight();
    }

    public void dispose() {
        Display.destroy();
    }

    public boolean isCloseRequested() {
        return Display.isCloseRequested();
    }

    public View getCurrentView() {
        return currentView;
    }

    public void setCurrentView(Class<? extends View> clazz) {
        setCurrentView(ViewFactory.instance().createView(clazz, this));
    }

    public void setCurrentView(View newView) {
        if (this.currentView != null)
            this.currentView.onDestroy();

        this.currentView = newView;
        this.currentView.layout(Display.getWidth(), Display.getHeight());

        if (currentView instanceof GameView) {
            GameView gameView = (GameView) currentView;
            gameView.setGameplayManager(this.gameplayManager);
        }
    }

    public Controller getSelectedGamepad() {
        return selectedGamepad;
    }
}
