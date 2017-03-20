package bomberman.view.engine.components;

import bomberman.view.engine.View;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.utility.Utility;


public class Button extends ViewComponentClickable {

    private String text;

    public Button(LayoutParams params, View v, String text) {
        super(params, v);
        this.text = text;
    }

    @Override
    public void draw(Batch batch) {
        if ( this.state == ViewComponentClickable.State.Default) {
            batch.draw(null, (getX()), (getY()), (getWidth()), (getHeight()), 1f, 1f, 1f, 1f);
            batch.draw(null, (getX() + 5), (getY() + 5), (getWidth() - 10), (getHeight() - 10), .4f, .4f, .4f, 1f);
        } else {
            batch.draw(null, (getX()), (getY()), (getWidth()), (getHeight()), 1f, 1f, 1f, 1f);
            batch.draw(null, (getX() + 5), (getY() + 5), (getWidth() - 10), (getHeight() - 10), .2f, .2f, .2f, 1f);
        }


        if (text != null)
            ViewManager.font.drawText(batch, text, (int) ((getX()) + (getWidth()) / 2 - ViewManager.font.getWidth(text) / 2), (int) ((getY()) + (getHeight()) / 2 - ViewManager.font.getLineHeight() / 2));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
