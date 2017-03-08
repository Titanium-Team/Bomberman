package bomberman.view.engine;

import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.rendering.FrameBuffer;
import bomberman.view.engine.utility.Camera;
import bomberman.view.engine.utility.Matrix4;
import bomberman.view.engine.utility.Vector2;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;

public class Light {

    private float x;
    private float y;
    private int radius;
    private float r, g, b;

    private FrameBuffer shadowMap;
    private FrameBuffer occludersMap;

    private Camera lightCamera;

    public Light(float x, float y, int radius, float r, float g, float b) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.r = r;
        this.g = g;
        this.b = b;

        try {
            this.shadowMap = new FrameBuffer(300, 1, GL11.GL_NEAREST);
            this.occludersMap = new FrameBuffer(radius * 2, radius * 2, GL11.GL_LINEAR);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public void cleanUp() {
        this.shadowMap.dispose();
        this.occludersMap.dispose();
    }

    public FrameBuffer getShadowMap() {
        return shadowMap;
    }

    public FrameBuffer getOccludersMap() {
        return occludersMap;
    }

    public Camera getLightCamera() {
        if (this.lightCamera == null) {
            this.lightCamera = new Camera(radius * 2, radius * 2);
        }

        this.lightCamera.setTranslation(new Vector2(x, y));

        return lightCamera;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public float getR() {
        return r;
    }

    public float getG() {
        return g;
    }

    public float getB() {
        return b;
    }
}