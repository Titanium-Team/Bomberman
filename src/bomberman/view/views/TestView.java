package bomberman.view.views;

import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.Button;
import bomberman.view.engine.components.Label;
import bomberman.view.engine.components.LayoutParams;
import bomberman.view.engine.components.VerticalList;

/**
 * Created by Tim Bolz on 22.03.2017.
 */
public class TestView extends BaseMenuView {

	private VerticalList verticalList;
	private Button addButton, deleteButton;

    public TestView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);

		this.verticalList = new VerticalList(LayoutParams.obtain(0.65f,0.2f,0.35f,0.7f),this);
		this.getRoot().addChild(verticalList);

		this.addButton = new Button(LayoutParams.obtain(0.5f,0.2f,0.1f,0.1f),this,"Add");
		addButton.addListener(() -> {
			verticalList.addChild(new Label(LayoutParams.obtain(0,0,0.9f,1),this,""+ ((int) (Math.random() * 10))));
			verticalList.updateChildren();
		});
		this.getRoot().addChild(addButton);

		this.deleteButton = new Button(LayoutParams.obtain(0.5f,0.4f,0.1f,0.1f),this,"Delete");
		deleteButton.addListener(() -> {
			if(verticalList.getChildren().size() > 1) {
				verticalList.removeChild(verticalList.getChildren().get(verticalList.getChildren().size() - 1));
				verticalList.updateChildren();
			}
		});
		this.getRoot().addChild(deleteButton);
	}
}
