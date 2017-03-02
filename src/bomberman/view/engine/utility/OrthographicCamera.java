package bomberman.view.engine.utility;

public class OrthographicCamera {

    public static Matrix4 getOrtho(float left, float right, float bottom, float top, float near, float far) {
        Matrix4 orthoMatrix = new Matrix4();
        orthoMatrix.setField(2 / (right - left), 0, 0);
        orthoMatrix.setField(2 / (top - bottom), 1, 1);
        orthoMatrix.setField(-2 / (far - near), 2, 2);
        orthoMatrix.setField(1, 3, 3);
        orthoMatrix.setField(-((right + left) / (right - left)), 0, 3);
        orthoMatrix.setField(-((top + bottom) / (top - bottom)), 1, 3);
        orthoMatrix.setField(-((far + near) / (far - near)), 2, 3);

        return orthoMatrix;
    }
}
