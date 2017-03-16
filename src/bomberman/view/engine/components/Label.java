package bomberman.view.engine.components;

import bomberman.view.engine.View;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;


public class Label extends ViewComponent {

    private String text;

    public Label(LayoutParams params, View v, String text) {
        super(params, v);
        this.text = text;
    }

    @Override
    public void draw(Batch batch) {
        ViewManager.font.drawText(batch, text, (int) ((getX()) + (getWidth()) / 2 - ViewManager.font.getWidth(text) / 2), (int) ((getY()) + (getHeight()) / 2 - ViewManager.font.getLineHeight() / 2));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
