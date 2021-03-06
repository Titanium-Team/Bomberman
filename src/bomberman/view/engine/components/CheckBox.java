package bomberman.view.engine.components;

import bomberman.view.engine.View;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;


public class CheckBox extends ViewComponentClickable {

    private String text;
    private boolean checked = false;

    public CheckBox(LayoutParams params, View v, String text) {
        super(params, v);
        this.text = text;

        this.addListener(() -> CheckBox.this.checked = !CheckBox.this.checked);
    }

    @Override
    public void draw(Batch batch) {
        updateState();

        if (this.state == State.Default) {
            if (!checked) {
                batch.draw(null, (getX()), (getY()), (getHeight()), (getHeight()), 1f, 1f, 1f, 1f);
                batch.draw(null, (getX() + 5), (getY() + 5), (getHeight() - 10), (getHeight() - 10), .4f, .4f, .4f, 1f);
            } else {
                batch.draw(null, (getX()), (getY()), (getHeight()), (getHeight()), 1f, 1f, 1f, 1f);
                batch.draw(null, (getX() + 5), (getY() + 5), (getHeight() - 10), (getHeight() - 10), 0f, .9f, 0f, 1f);
            }
        } else if (this.state == State.Pressed) {
            batch.draw(null, (getX()), (getY()), (getHeight()), (getHeight()), 1f, 1f, 1f, 1f);
            batch.draw(null, (getX() + 5), (getY() + 5), (getHeight() - 10), (getHeight() - 10), .2f, .2f, .2f, 1f);
        } else if (this.state == State.Hover) {
            batch.draw(null, (getX()), (getY()), (getHeight()), (getHeight()), 1f, 1f, 1f, 1f);
            batch.draw(null, (getX() + 5), (getY() + 5), (getHeight() - 10), (getHeight() - 10), .6f, .6f, .6f, 1f);
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
