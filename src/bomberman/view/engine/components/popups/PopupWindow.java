package bomberman.view.engine.components.popups;

import bomberman.view.engine.View;
import bomberman.view.engine.components.Button;
import bomberman.view.engine.components.LayoutParams;
import bomberman.view.engine.components.Panel;

public abstract class PopupWindow extends Panel {

    private Button exitButton;

    public PopupWindow(LayoutParams params, View v) {
        super(params, v);

        this.setBackgroundColor(0.5f, 0.5f, 0.5f, 0.5f);

        this.exitButton = new Button(LayoutParams.obtain(0.7f, 0f, 0.25f, 0.15f), v, "Exit");
        this.exitButton.addListener(() -> this.closeSelf());
        this.addChild(exitButton);
    }

    public void showSelf() {
        this.getView().getRoot().addChild(this);
        this.getView().requestLayout();
    }

    public void closeSelf() {
        this.getView().getRoot().removeChild(this);
        this.getView().requestLayout();
    }

    public boolean isShown() {
        return getView().getRoot().getChildren().contains(this);
    }

    protected Button getExitButton(){
        return exitButton;
    }

}
