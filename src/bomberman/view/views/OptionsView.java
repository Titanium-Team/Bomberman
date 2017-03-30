package bomberman.view.views;

import bomberman.Main;
import bomberman.view.engine.Config;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.CheckBox;
import bomberman.view.engine.components.ClickListener;
import bomberman.view.engine.components.Label;
import bomberman.view.engine.components.LayoutParams;

/**
 * shows gamesettings
 **/
public class OptionsView extends BaseMenuView {

    private Label titleLabel;
    private CheckBox fpsLabelCheckBox;
    private CheckBox vSyncCheckBox;

    public OptionsView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);

        this.titleLabel = new Label(LayoutParams.obtain(0.4f, 0.1f, 0.2f, 0.1f), this, "Options");
        this.getRoot().addChild(titleLabel);

        Config config = Main.instance.getConfig();

        this.fpsLabelCheckBox = new CheckBox(LayoutParams.obtain(0.4f, 0.3f, 0.1f, 0.1f), this, "Show FPS");
        this.fpsLabelCheckBox.setChecked(config.isShowFPS());
        this.fpsLabelCheckBox.addListener(() -> config.setShowFPS(fpsLabelCheckBox.isChecked()));
        this.getRoot().addChild(fpsLabelCheckBox);

        this.vSyncCheckBox = new CheckBox(LayoutParams.obtain(0.4f, 0.45f, 0.1f, 0.1f), this, "vSync");
        this.vSyncCheckBox.setChecked(config.isvSync());
        this.vSyncCheckBox.addListener(() -> config.setvSync(vSyncCheckBox.isChecked()));
        this.getRoot().addChild(vSyncCheckBox);
    }

}