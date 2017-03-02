package bomberman.view.engine;

import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.rendering.BitmapFont;
import bomberman.view.engine.rendering.ITexture;
import bomberman.view.engine.rendering.Texture;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.DisplayMode;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class ViewManager {

    private Batch batch;

    public static BitmapFont font;

    public static final HashMap<String, ITexture> textureMap = new HashMap<>();

    public static void load() {
        try {
            font = new BitmapFont(ViewManager.class.getResource("/bomberman/resources/font/font.fnt"), ViewManager.class.getResource("/bomberman/resources/font/font.png"));

            loadTexture("test0.png");
            loadTexture("test1.png");
            loadTexture("test2.png");
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
        if (!textureMap.containsKey(textureID)) {
            loadTexture(textureID);
        }

        return textureMap.get(textureID);
    }

    private View currentView;
    private boolean fullscreen = false;

    public ViewManager() {
        try {
            setDisplayMode(800, 600, false);

            Display.create(new PixelFormat(8, 0, 0, 4));

            System.out.println("OpenGL context created! Version: " + GL11.glGetString(GL11.GL_VERSION) + ", Vendor: " + GL11.glGetString(GL11.GL_VENDOR) + ", Renderer: " + GL11.glGetString(GL11.GL_RENDERER));
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        GL11.glEnable(GL13.GL_MULTISAMPLE);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        load();

        this.batch = new Batch();
        batch.resize(Display.getWidth(), Display.getHeight());
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
            Display.setTitle("Bomberman");
            Display.setVSyncEnabled(true);
        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
        }
    }

    public void render(float deltaTime) {
        if (Display.wasResized()) {
            onResize(Display.getWidth(), Display.getHeight());
        }

        GL11.glClearColor(0f, 0f, 0f, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        while (Mouse.next()) {
            if (Mouse.getEventButtonState()) {
                int button = Mouse.getEventButton();
                int mouseX = Mouse.getEventX();
                int mouseY = Display.getHeight() - Mouse.getEventY();

                currentView.onMouseDown(button, mouseX, mouseY);
            } else {
                int button = Mouse.getEventButton();
                int mouseX = Mouse.getEventX();
                int mouseY = Display.getHeight() - Mouse.getEventY();

                if (button != -1) {
                    currentView.onMouseUp(button, mouseX, mouseY);
                }
            }
        }
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                int key = Keyboard.getEventKey();
                char c = Keyboard.getEventCharacter();

                currentView.onKeyDown(key, c);

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
            }
        }

        batch.begin();

        if (currentView != null) {
            currentView.render(deltaTime, batch);
        }

        batch.end();

        Display.update();
        Display.sync(60);
    }

    private void onResize(int width, int height) {
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
        batch.resize(Display.getWidth(), Display.getHeight());

        if (currentView != null)
            currentView.layout(Display.getWidth(), Display.getHeight());

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

    public void setCurrentView(View currentView) {
        this.currentView = currentView;
        this.currentView.layout(Display.getWidth(), Display.getHeight());
    }
}
