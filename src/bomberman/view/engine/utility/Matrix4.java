package bomberman.view.engine.utility;

import java.nio.FloatBuffer;

public class Matrix4 {

	private float[][] m;

	public Matrix4() {
		m = new float[4][4];
		identity();
	}

	public void identity() {
		m[0][0] = 1;
		m[0][1] = 0;
		m[0][2] = 0;
		m[0][3] = 0;
		m[1][0] = 0;
		m[1][1] = 1;
		m[1][2] = 0;
		m[1][3] = 0;
		m[2][0] = 0;
		m[2][1] = 0;
		m[2][2] = 1;
		m[2][3] = 0;
		m[3][0] = 0;
		m[3][1] = 0;
		m[3][2] = 0;
		m[3][3] = 1;
	}

	public float[][] getMatrix() {
		return m;
	}

	public float get(int i, int j) {
		if (i >= 0 && i <= 4 && j >= 0 && j <= 4) {
			return m[i][j];
		} else {
			throw new IllegalArgumentException();
		}
	}

	public void set(float value, int i, int j) {
		if (i >= 0 && i <= 4 && j >= 0 && j <= 4) {
			m[i][j] = value;
		}
	}

	public Matrix4 mul(Matrix4 r) {
		Matrix4 res = new Matrix4();

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				res.set(m[i][0] * r.get(0, j) + m[i][1] * r.get(1, j) + m[i][2] * r.get(2, j) + m[i][3] * r.get(3, j), i, j);
			}
		}

		return res;
	}

	public Matrix4 store(FloatBuffer buf) {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				buf.put(m[i][j]);
			}
		}

		return this;
	}

	public static Matrix4 getOrthographicMatrix(float left, float right, float bottom, float top, float near, float far) {
		Matrix4 orthoMatrix = new Matrix4();
		orthoMatrix.set(2 / (right - left), 0, 0);
		orthoMatrix.set(2 / (top - bottom), 1, 1);
		orthoMatrix.set(-2 / (far - near), 2, 2);
		orthoMatrix.set(1, 3, 3);
		orthoMatrix.set(-((right + left) / (right - left)), 0, 3);
		orthoMatrix.set(-((top + bottom) / (top - bottom)), 1, 3);
		orthoMatrix.set(-((far + near) / (far - near)), 2, 3);

		return orthoMatrix;
	}

}
