package bomberman.view.engine.components;

import bomberman.view.engine.View;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.utility.Utility;
import org.lwjgl.input.Mouse;

/**
 * Created by Tim Bolz on 17.03.2017.
 */

public class Scrollbar extends ViewGroup {

	private VerticalList parent;
	private ScrollTab tab;
	private int elements, visibleElements;


	public Scrollbar(LayoutParams params, View v, VerticalList parent) {
		super(params, v);
		this.elements = 0;
		this.visibleElements = 0;
		this.parent = parent;
		this.tab = new ScrollTab(LayoutParams.obtain(0f, 0f, 1f, 1f), v);
		this.addChild(tab);
	}

	private class ScrollTab extends ViewComponent {

		private float scrollHeight, pos;
		private float mouseOffset, previousPos = 0;

		public ScrollTab(LayoutParams params, View v) {
			super(params, v);
			this.scrollHeight = 0;
			this.pos = 0;
			this.mouseOffset = -1;
		}

		/**
		 * Zeichnet den ScrollTab.
		 * @param batch Der vom Viewmanager übergebene Batch.
		 */
		@Override
		public void draw(Batch batch) {

			if (this.mouseOffset != -1) {
				this.setPos(Math.max(0, Math.min((float)(this.getView().getHeight() - Mouse.getY() - Scrollbar.this.getY() - mouseOffset) / ((float) Scrollbar.this.getHeight()), 1 - (scrollHeight))));
				if (previousPos != pos) {
					parent.updateChildren();
					this.getView().requestLayout();
					previousPos = pos;
				}
			}
			batch.draw(null, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0.2f, 0.7f, 0.7f, 0.7f);
		}

		/**
		 * Setzt die Größe des Tabs und überprüft, ob der Balken über die Anzeige hinausragt. Tut er dies wird er an die oberste Stelle zurückgesetzt.
		 * @param scrollHeight die neue Größe.
		 */
		public void setScrollHeight(float scrollHeight) {
			this.scrollHeight = scrollHeight;
			if(this.getY() + Scrollbar.this.getHeight()*scrollHeight > Scrollbar.this.getY() + Scrollbar.this.getHeight()){
				this.setPos(0f);
			}else {
				this.setParams(LayoutParams.obtain(0, pos, 1, scrollHeight));
			}
		}

		public float getPos() {
			return pos;
		}

		public void setPos(float pos) {
			this.pos = pos;
			this.setParams(LayoutParams.obtain(0, pos, 1, scrollHeight));
		}

		/**
		 *
		 * @param button Der Index des Maus-Buttons der geklickt wurde.
		 * @param mouseX Die x-Koordinate des Mauszeigers .
		 * @param mouseY Die y-Koordinate des Mauszeigers.
		 */
		@Override
		public void onMouseDown(int button, int mouseX, int mouseY) {
			super.onMouseDown(button, mouseX, mouseY);
			if (Utility.viewComponentIsCollidingWithMouse(this, mouseX, mouseY)) {
				if (mouseOffset == -1) {
					this.mouseOffset = mouseY - this.getY();
				}
			}
		}

		/**
		 * 
		 * @param button Der Index des Maus-Buttons der geklickt wurde.
		 * @param mouseX Die x-Koordinate des Mauszeigers .
		 * @param mouseY Die y-Koordinate des Mauszeigers.
		 */
		@Override
		public void onMouseUp(int button, int mouseX, int mouseY) {
			super.onMouseUp(button, mouseX, mouseY);
			mouseOffset = -1;
		}
	}

	@Override
	public void draw(Batch batch) {
		batch.draw(null, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0.3f, 0.3f, 0.3f, 1);
		tab.draw(batch);
	}

	public void setScrollHeight(float scrollHeight) {
		this.tab.setScrollHeight(scrollHeight);
	}

	public float getScrollPos() {
		return tab.getPos();
	}

	public float getScrollDistance(){
		return tab.pos-tab.previousPos;
	}

}