package bomberman.view.engine.components;

import bomberman.view.engine.View;
import bomberman.view.engine.rendering.Batch;
import org.lwjgl.opengl.GL11;

public class VerticalView extends Panel {

	private final Scrollbar scrollbar;

	public VerticalView(LayoutParams params, View v) {
		super(params, v);

		this.setBackgroundColor(0.2f, 0.3f, 0.5f, 0.5f);
		this.scrollbar = new Scrollbar(LayoutParams.obtain(0.9f, 0, 0.1f, 1), v, this);
		super.addChild(scrollbar);
	}

	@Override
	public void addChild(ViewComponent child) {
		super.addChild(child);
		this.updateChildren();
	}

	@Override
	public void removeChild(ViewComponent child) {
		super.removeChild(child);
		this.updateChildren();
	}

	@Override
	public void draw(Batch batch) {
		batch.flush();
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor((int) getX(), (int) (getView().getHeight() - this.getY() - getHeight()), (int) getWidth(), (int) getHeight());

		super.draw(batch);

		batch.flush();
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	public void updateChildren() {
		int indexfirst = Math.round(scrollbar.getScrollPos() * (this.getChildren().size() - 1)) + 1;
		int indexlast = Math.min(this.getChildren().size() - 1, indexfirst + 7);
		scrollbar.setScrollHeight(Math.min(1, 8f / (float)((this.getChildren().size() - 1) > 0 ? (this.getChildren().size() - 1) : 1)));
		float size = Math.max(1f/8f, 1f / (float)((this.getChildren().size() - 1) > 0 ? (this.getChildren().size() - 1) : 1));
		int count = 0;
		for (int i = 0; i < this.getChildren().size(); i++) {
			ViewComponent childComponent = this.getChildren().get(i);
			if (!(childComponent instanceof Scrollbar)) {
				if (i >= indexfirst && i <= indexlast) {
					childComponent.setParams(LayoutParams.obtain(0, count * size, 0.9f, size));
					count++;
				} else {
					childComponent.setParams(LayoutParams.obtain(0, 1, 0.9f, size));
				}
			}
		}

		getView().requestLayout();
	}
}
