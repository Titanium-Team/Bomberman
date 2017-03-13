package bomberman.view.views;

import bomberman.view.engine.View;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.Button;
import bomberman.view.engine.components.ButtonListener;

public abstract class BaseMenuView extends View {

	protected final Button backButton;

	public BaseMenuView(int width, int height, ViewManager viewManager) {
		super(width, height, viewManager);

		this.backButton = new Button(50, 50, 100, 50, this, "Back");
		this.backButton.setListener(new ButtonListener() {
			@Override
			public void onClick() {
				BaseMenuView.this.navigateBack();
			}
		});
		this.addComponent(backButton);
	}

}