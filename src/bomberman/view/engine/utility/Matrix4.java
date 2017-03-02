package bomberman.view.engine.utility;

import java.nio.FloatBuffer;

public class Matrix4 {

    private float[][] components;

    public Matrix4() {
        components = new float[4][4];
    }

    public void setField(float content, int i, int j) {
        if (i >= 0 && i <= 4 && j >= 0 && j <= 4) {
            components[i][j] = content;
        }
    }

    public float[][] getMatrix() {
        return components;
    }

    public float getField(int i, int j) {
        if (i >= 0 && i <= 4 && j >= 0 && j <= 4) {
            return components[i][j];
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Matrix4 store(FloatBuffer buf) {
        for (int i = 0; i < components.length; i++) {
            for (int j = 0; j < components[i].length; j++) {
                buf.put(components[i][j]);
            }
        }

        return this;
    }

}
