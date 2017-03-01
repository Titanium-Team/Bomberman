package bomberman.view.engine;

import bomberman.view.engine.components.ViewComponent;
import bomberman.view.engine.rendering.Batch;

import java.util.ArrayList;
import java.util.List;

public abstract class View {

    protected List<ViewComponent> components;
    protected float originWidth,originHeight;
    protected final ViewManager viewManager;

    public View(float width, float height, ViewManager viewManager)
    {
        components = new ArrayList<ViewComponent>() ;
        originHeight = height;
        originWidth = width;
        this.viewManager = viewManager;
    }

    /**
     * zeichnet alle ViewComponents
     */
    public void render(float deltaTime, Batch batch){
        for(int i = 0; i < components.size(); i++){
            ViewComponent c = components.get(i);
            c.draw(batch);
        }
    }


    public void layout(float width, float height){
        originHeight = height;
        originWidth = width;
    }




    public List<ViewComponent> getComponents(){return components;}

    public ViewManager getViewManager() {
        return viewManager;
    }

    public void onKeyDown(int key, char c){
        for (ViewComponent v : components) {
            v.onKeyDown(key, c);
        }
    }

    public void onKeyUp(int key, char c){
        for (ViewComponent v : components) {
            v.onKeyUp(key, c);
        }
    }

    public void onMouseDown(int button, int mouseX, int mouseY){
        for (ViewComponent v : components) {
            v.onMouseDown(button, mouseX, mouseY);
        }
    }

    public void onMouseUp(int button, int mouseX, int mouseY){
        for (ViewComponent v : components) {
            v.onMouseUp(button, mouseX, mouseY);
        }
    }


}
