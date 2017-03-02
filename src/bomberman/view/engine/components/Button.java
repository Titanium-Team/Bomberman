package bomberman.view.engine.components;

import bomberman.view.engine.View;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.rendering.ITexture;
import bomberman.view.engine.utility.Utility;


public class Button extends ViewComponent {

    private ButtonListener listener;
    private String buttontext;
    private ITexture texture, buttonMainTexture, buttonPressedTexture;
    private boolean down;

    public Button(float x, float y, float width, float height, View v, String buttontext) {
        this(x, y, width, height, v, buttontext, ViewManager.getTexture("viewTextures/mainButton.png"), ViewManager.getTexture("viewTextures/pressedButton.png"));
    }

    public Button(float x, float y, float width, float height, View v, String buttontext, ITexture mainTexture, ITexture pressedTexture) {
        super(x, y, width, height, v);
        this.buttontext = buttontext;
        buttonMainTexture = mainTexture;
        buttonPressedTexture = pressedTexture;
        texture = buttonMainTexture;
        down = false;
    }

    @Override
    public void onMouseDown(int button, int mouseX, int mouseY) {
        super.onMouseDown(button, mouseX, mouseY);
        if (button == 0 && Utility.viewComponentIsCollidingWithMouse(this, mouseX, mouseY)) {
            down = true;
            texture = buttonPressedTexture;
        } else {
            texture = buttonMainTexture;
            down = false;
        }
    }

    @Override
    public void onMouseUp(int button, int mouseX, int mouseY) {
        super.onMouseUp(button, mouseX, mouseY);

        if (button == 0 && down) {
            texture = buttonMainTexture;
            down = false;

            if (listener != null) {
                listener.onClick();
            }
        }
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(getTexture(), (getX()), (getY()), (getWidth()), (getHeight()), getWidth() / 2, getHeight() / 2, (float) Math.toRadians(0), 1f, 1f, 1f, 1f);
        if (buttontext != null)
            ViewManager.font.drawText(batch, buttontext, (int) ((getX()) + (getWidth()) / 2 - ViewManager.font.getWidth(buttontext) / 2), (int) ((getY()) + (getHeight()) / 2 - ViewManager.font.getLineHeight() / 2));
    }

    public ButtonListener getListener() {
        return listener;
    }

    public void setListener(ButtonListener listener) {
        this.listener = listener;
    }

    public ITexture getTexture() {
        return texture;
    }

    public String getButtontext() {
        return buttontext;
    }

    public void setButtontext(String buttontext) {
        this.buttontext = buttontext;
    }

    public boolean isDown() {
        return down;
    }
}
