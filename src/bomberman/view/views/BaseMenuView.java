package bomberman.view.views;

import bomberman.view.engine.View;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.Button;
import bomberman.view.engine.components.ButtonListener;
import bomberman.view.engine.components.LayoutParams;

public abstract class BaseMenuView extends View {

    protected final Button backButton;

    public BaseMenuView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);

        this.backButton = new Button(LayoutParams.obtain(0.05f, 0.05f, 0.1f, 0.1f), this, "Back");
        this.backButton.setListener(() -> BaseMenuView.this.navigateBack());
        this.getRoot().addChild(backButton);
    }

}