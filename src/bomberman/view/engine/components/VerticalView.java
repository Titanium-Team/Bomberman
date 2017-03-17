package bomberman.view.engine.components;

import bomberman.view.engine.View;
import bomberman.view.engine.rendering.Batch;

/**
 * Created by 204g13 on 17.03.2017.
 */
public class VerticalView extends Panel {

    public VerticalView(LayoutParams params, View v) {
        super(params, v);

        this.setBackgroundColor(0.2f,0.3f,0.5f,0);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        batch.draw(null,0.9f,0,0.1f,1,0.5f,0.5f,0.5f,1);
    }

    @Override
    public void addChild(ViewComponent child) {
        super.addChild(child);
        this.updateChildren();
    }

    @Override
    public void removeChild(ViewComponent child) {
        super.removeChild(child);
        this.updateChildren();
    }

    private void updateChildren() {
        float size = 1 / (float) this.getChildren().size();
        for (int i = 0; i < this.getChildren().size(); i++) {
            ViewComponent childComponent = this.getChildren().get(i);
            childComponent.setParams(LayoutParams.obtain(0, i * size, 0.9f, size));
        }

        getView().requestLayout();
    }
}
