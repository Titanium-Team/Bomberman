package bomberman.view.engine;

public enum MSMode {

	OFF(0),
	MSAAx2(2),
	MSAAx4(4),;

	public final int samples;

	MSMode(int samples) {
		this.samples = samples;
	}

}
