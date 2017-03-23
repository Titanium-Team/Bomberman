package bomberman.view.views;

import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.Button;
import bomberman.view.engine.components.Label;
import bomberman.view.engine.components.LayoutParams;
import bomberman.view.engine.components.VerticalView;

/**
 * Created by Tim Bolz on 22.03.2017.
 */
public class TestView extends BaseMenuView{

	private VerticalView verticalView;
	private Button addButton, deleteButton;

	public TestView(int width, int height, ViewManager viewManager) {
		super(width, height, viewManager);

		this.verticalView = new VerticalView(LayoutParams.obtain(0.65f,0,0.35f,0.9f),this);
		this.getRoot().addChild(verticalView);

		this.addButton = new Button(LayoutParams.obtain(0.5f,0.2f,0.1f,0.1f),this,"Add");
		addButton.addListener(() -> {
			verticalView.addChild(new Label(LayoutParams.obtain(0,0,0.9f,1),this,""+ ((int) (Math.random() * 10))));
			verticalView.updateChildren();
		});
		this.getRoot().addChild(addButton);

		this.deleteButton = new Button(LayoutParams.obtain(0.5f,0.4f,0.1f,0.1f),this,"Delete");
		deleteButton.addListener(() -> {
			verticalView.removeChild(verticalView.getChildren().get(verticalView.getChildren().size()-1));
			verticalView.updateChildren();
		});
		this.getRoot().addChild(deleteButton);
	}
}
