package bomberman.view.engine.utility;

public class Camera {

	private Matrix4 projectionMatrix;
	private Matrix4 translationMatrix;

	private int width;
	private int height;
	private Vector2 translation = new Vector2(0, 0);

	public Camera(int width, int height) {
		resize(width, height);

		this.translationMatrix = new Matrix4();
	}

	public void resize(int width, int height) {
		this.width = width;
		this.height = height;

		float left = -width / 2;
		float right = width / 2;
		float bottom = height / 2;
		float top = -height / 2;
		this.projectionMatrix = Matrix4.getOrthographicMatrix(left, right, bottom, top, 0, 1);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Vector2 getTranslation() {
		return translation;
	}

	public void setTranslation(Vector2 translation) {
		this.translation = translation;

		translationMatrix.set(-translation.getX(), 0, 3);
		translationMatrix.set(-translation.getY(), 1, 3);
	}

	public Matrix4 getCombined() {
		return projectionMatrix.mul(translationMatrix);
	}
}
