package bomberman.view.views;

import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.*;

/**
 * shows gamesettings
 **/
public class OptionsView extends BaseMenuView {

    private VerticalView verticalView;
    private Label titleLabel;
    private CheckBox fpsLabelCheckBox;
    private CheckBox vSyncCheckBox;
    private Button testButton;
    private Button delButton;

    public OptionsView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);
        this.verticalView = new VerticalView(LayoutParams.obtain(0.75f, 0.2f, 0.25f, 0.8f), this);
        this.getRoot().addChild(verticalView);

        this.titleLabel = new Label(LayoutParams.obtain(0.4f, 0.1f, 0.2f, 0.1f), this, "Options");
        this.getRoot().addChild(titleLabel);

        this.fpsLabelCheckBox = new CheckBox(LayoutParams.obtain(0.4f, 0.3f, 0.1f, 0.1f), this, "Show FPS");
        this.getRoot().addChild(fpsLabelCheckBox);

        this.vSyncCheckBox = new CheckBox(LayoutParams.obtain(0.4f, 0.45f, 0.1f, 0.1f), this, "vSync");
        this.getRoot().addChild(vSyncCheckBox);

        this.testButton = new Button(LayoutParams.obtain(0.2f, 0.3f, 0.2f, 0.1f), this, "Test");
        this.testButton.addListener(() -> this.verticalView.addChild(new Label(LayoutParams.obtain(0, 0, 0.9f, 1), this, "Test12345")));
        this.getRoot().addChild(testButton);

        this.delButton = new Button(LayoutParams.obtain(0.2f, 0.4f, 0.2f, 0.1f), this, "Delete");
        this.delButton.addListener(() -> this.verticalView.removeChild(this.verticalView.getChildren().size() > 1 ? this.verticalView.getChildren().get(verticalView.getChildren().size() - 1) : null));

        this.getRoot().addChild(delButton);


    }

}