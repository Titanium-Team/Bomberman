package bomberman.view.engine.components;

import bomberman.view.engine.View;
import bomberman.view.engine.rendering.Batch;

import java.util.ArrayList;
import java.util.List;

public abstract class ViewGroup extends ViewComponent {

    public List<ViewComponent> componentList;


    public ViewGroup(float x, float y, float width, float height, View v) {
        super(x, y, width, height, v);
        componentList = new ArrayList<>();
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        for (int i = 0; i < componentList.size(); i++) {
            componentList.get(i).draw(batch);
        }
    }

    public List<ViewComponent> getComponentList() {return componentList;}

    public void setComponentList(List<ViewComponent> componentList) {this.componentList = componentList;}

}
