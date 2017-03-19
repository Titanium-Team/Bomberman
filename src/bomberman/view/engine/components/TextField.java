package bomberman.view.engine.components;

import bomberman.view.engine.View;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.utility.Utility;
import org.lwjgl.input.Keyboard;

public class TextField extends ViewComponent {

    public enum State {
        Focussed, Unfocussed;
    }

    private String text, backText;
    private State state = State.Unfocussed;
    private int pointer;

    public TextField(LayoutParams params, View v) {
        this(params, v, "");
    }

    public TextField(LayoutParams params, View v, String text, String backText) {
        super(params, v);
        if(text != null) {
            this.text = text;
        }else{
            this.text = "";
        }
        this.backText = backText;

    }

    public TextField(LayoutParams params, View v, String text ) {this(params,v,text,"");
    }



    public void addChar(char c) {
        this.text = text.substring(0, pointer) + c + text.substring(pointer, text.length());
        pointer++;
    }

    @Override
    public void onMouseDown(int button, int mouseX, int mouseY) {
        super.onMouseDown(button, mouseX, mouseY);
        if (Utility.viewComponentIsCollidingWithMouse(this, mouseX, mouseY)) {
            if (button == 0) {
                state = State.Focussed;
                pointer = text.length();
            }
        } else {
            state = State.Unfocussed;
        }
    }

    @Override
    public void onKeyDown(int key, char c) {
        super.onKeyDown(key, c);
        if (state == State.Focussed) {
            if (key == Keyboard.KEY_BACK) {
                if (text != null && !text.isEmpty()) {
                    this.text = text.substring(0, pointer - 1) + text.substring(pointer, text.length());
                    if (pointer > 0) {
                        pointer--;
                    }
                }
            } else if (key == Keyboard.KEY_LEFT) {
                if (pointer > 0) {
                    pointer--;
                }
            } else if (key == Keyboard.KEY_RIGHT) {
                if (pointer < text.length()) {
                    pointer++;
                }
            } else {
                addChar(c);
            }
        }
    }

    @Override
    public void draw(Batch batch) {
        if (state == State.Unfocussed) {
            batch.draw(null, (getX()), (getY()), (getWidth()), (getHeight()), 1f, 1f, 1f, 1f);
            batch.draw(null, (getX() + 5), (getY() + 5), (getWidth() - 10), (getHeight() - 10), .4f, .4f, .4f, 1f);
        } else {
            batch.draw(null, (getX()), (getY()), (getWidth()), (getHeight()), 1f, 1f, 1f, 1f);
            batch.draw(null, (getX() + 5), (getY() + 5), (getWidth() - 10), (getHeight() - 10), .2f, .2f, .2f, 1f);
            batch.draw(null, (getX() + 5) + ViewManager.font.getWidth(text.substring(0, pointer)), (getY() + 7), 3, (getHeight() - 15), 1f, 1f, 1f, 1f);

        }

        if (text.length() != 0 && text != null) {
            ViewManager.font.drawText(batch, text, (int) getX() + 5, (int) ((getY()) + (getHeight()) / 2 - ViewManager.font.getLineHeight() / 2));

        }else if (text.length() == 0 && state == State.Unfocussed)
            ViewManager.font.drawText(batch, backText, (int) getX() + 5, (int) ((getY()) + (getHeight()) / 2 - ViewManager.font.getLineHeight() / 2));
    }
}
