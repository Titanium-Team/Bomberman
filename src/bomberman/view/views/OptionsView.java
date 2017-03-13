package bomberman.view.views;

import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.Label;

/**
 * shows gamesettings
 **/
public class OptionsView extends BaseMenuView {

	private Label titleLabel;

	public OptionsView(int width, int height, ViewManager viewManager) {
		super(width, height, viewManager);

		this.titleLabel = new Label(200, 50, 200, 50, this, "Options");
		this.addComponent(titleLabel);


	}

}