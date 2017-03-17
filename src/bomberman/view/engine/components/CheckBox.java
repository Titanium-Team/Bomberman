package bomberman.view.engine.components;

import bomberman.view.engine.View;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.utility.Utility;


public class CheckBox extends ViewComponent {

    public enum State {
        Default, Pressed;
    }

    private String text;
    private State state = State.Default;
    private boolean checked = false;

    public CheckBox(LayoutParams params, View v, String text) {
        super(params, v);
        this.text = text;
    }

    @Override
    public void onMouseDown(int button, int mouseX, int mouseY) {
        super.onMouseDown(button, mouseX, mouseY);
        if (Utility.viewComponentIsCollidingWithMouse(this, mouseX, mouseY)) {
            if (button == 0) {
                state = State.Pressed;
            }
        }
    }

    @Override
    public void onMouseUp(int button, int mouseX, int mouseY) {
        super.onMouseUp(button, mouseX, mouseY);

        if (button == 0 && state == State.Pressed) {
            state = State.Default;
            this.checked = !this.checked;
        }
    }

    @Override
    public void draw(Batch batch) {
        if (!checked) {
            batch.draw(null, (getX()), (getY()), (getHeight()), (getHeight()), 1f, 1f, 1f, 1f);
            batch.draw(null, (getX() + 5), (getY() + 5), (getHeight() - 10), (getHeight() - 10), .4f, .4f, .4f, 1f);
        } else {
            batch.draw(null, (getX()), (getY()), (getHeight()), (getHeight()), 1f, 1f, 1f, 1f);
            batch.draw(null, (getX() + 5), (getY() + 5), (getHeight() - 10), (getHeight() - 10), 0f, .9f, 0f, 1f);
        }

        if (text != null)
            ViewManager.font.drawText(batch, text, (int) ((getX()) + (getHeight())), (int) ((getY()) + (getHeight()) / 2 - ViewManager.font.getLineHeight() / 2));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
