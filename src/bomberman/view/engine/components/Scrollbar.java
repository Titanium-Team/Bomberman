package bomberman.view.engine.components;

import bomberman.view.engine.View;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.utility.Utility;
import org.lwjgl.input.Mouse;

/**
 * Created by Tim Bolz on 17.03.2017.
 */
public class Scrollbar extends ViewComponent {
    private VerticalView parent;
    private float scrollPos = 0, scrollTabHeight = 1f;
    private int elements, visibleElements;
    private float verticalDistance = -1;

    public Scrollbar(LayoutParams params, View v, VerticalView parent) {
        super(params, v);
        this.elements = 0;
        this.visibleElements = 0;
        this.parent=parent;
    }

    public Scrollbar(LayoutParams params, View v) {
        super(params, v);
        this.elements = 0;
        this.visibleElements = 0;
    }


    @Override
    public void draw(Batch batch) {
        this.calculateScrolltabHeight();
        if(this.verticalDistance!=-1){
            scrollPos =  Math.max(0,Math.min((float)((this.getView().getHeight()-Mouse.getY()-this.getY()-verticalDistance)/ this.getHeight()) , 1-(scrollTabHeight)));
            parent.updateChildren();
        }
        batch.draw(null, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0.3f, 0.3f, 0.3f, 1);
        batch.draw(null, this.getX(), this.getY() + (this.getHeight() * (scrollPos)), this.getWidth(), this.getHeight() * scrollTabHeight<this.getY()+this.getHeight()? this.getHeight() * scrollTabHeight : this.getHeight() - (this.getHeight() * scrollPos), 0.2f, 0.7f, 0.7f, 0.7f);
        this.getView().requestLayout();

    }

    private void calculateScrolltabHeight() {
        if (elements > visibleElements) {
            this.scrollTabHeight = (float) (visibleElements) / (float) elements;
        }else{
            scrollTabHeight=1;
        }
    }

    public void setElements(int elements) {
        this.elements = elements;
        if (this.elements <= 8) {
            this.visibleElements = elements;
        }
    }

    @Override
    public void onMouseDown(int button, int mouseX, int mouseY) {
        super.onMouseDown(button, mouseX, mouseY);
        if (visibleElements < elements && Utility.viewComponentIsCollidingWithMouse(this, mouseX, mouseY) && mouseY > this.getY() + this.getHeight()*scrollPos && mouseY < this.getY() * (1 + scrollPos) + (this.getHeight() * scrollTabHeight)){
            if (verticalDistance == -1) {
                this.verticalDistance = ((int) (mouseY-this.getY()*(1+scrollPos)));
            }
        }
    }

    @Override
    public void onMouseUp(int button, int mouseX, int mouseY) {
        super.onMouseUp(button, mouseX, mouseY);
        verticalDistance = -1;
    }

    public int getIndexOfFirstElement(){
        return this.elements== visibleElements? 0: ((int) (elements * scrollPos));
    }

    public int getIndexOfLastElement(){
        return this.elements>visibleElements? this.getIndexOfFirstElement()+7:elements-1;
    }
}
