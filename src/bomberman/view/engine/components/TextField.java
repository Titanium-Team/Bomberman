package bomberman.view.engine.components;

import bomberman.view.engine.View;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.utility.Utility;
import org.lwjgl.input.Keyboard;

public class TextField extends ViewComponentClickable {

    public void setText(String text) {
        this.text = text;
    }

    private enum TextFieldState {
        Focussed, Unfocussed;
    }

    private String text, backText;
    private int pointer;
    private TextFieldState textFieldState = TextFieldState.Unfocussed;

    public TextField(LayoutParams params, View v) {
        this(params, v, "");
    }

    public TextField(LayoutParams params, View v, String text, String backText) {
        super(params, v);
        if (text != null) {
            this.text = text;
        } else {
            this.text = "";
        }
        this.backText = backText;

        this.addListener(() -> {

            //System.out.println(pointer + " :" + text.length());
            pointer = this.getText().length();

            textFieldState = TextFieldState.Focussed;

        });

    }

    public TextField(LayoutParams params, View v, String text) {
        this(params, v, text, "");
    }


    public void addChar(char c) {
        this.text = text.substring(0, pointer) + c + text.substring(pointer, text.length());
        pointer++;
    }

    @Override
    public void onKeyDown(int key, char c) {
        super.onKeyDown(key, c);
        if (textFieldState == TextFieldState.Focussed) {
            if (key == Keyboard.KEY_BACK) {
                if (text != null && !text.isEmpty()) {
                    int tmp = pointer - 1;
                    int tmp2 = pointer;
                    if (pointer == 0) {
                        tmp++;
                        tmp2++;
                    }
                    this.text = text.substring(0, tmp) + text.substring(tmp2, text.length());
                    if (pointer > 0) {
                        pointer--;
                    }
                }
                //System.out.println(text.length());
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
    public void onMouseDown(int button, int mouseX, int mouseY) {
        super.onMouseDown(button, mouseX, mouseY);
        if (!Utility.viewComponentIsCollidingWithMouse(this, mouseX, mouseY) && textFieldState == TextFieldState.Focussed) {
            textFieldState = TextFieldState.Unfocussed;
        }
    }

    @Override
    public void draw(Batch batch) {
        updateState();

        if (this.state == State.Default) {
            batch.draw(null, (getX()), (getY()), (getWidth()), (getHeight()), 1f, 1f, 1f, 1f);
            batch.draw(null, (getX() + 5), (getY() + 5), (getWidth() - 10), (getHeight() - 10), .4f, .4f, .4f, 1f);
        } else if (this.state == State.Hover) {
            batch.draw(null, (getX()), (getY()), (getWidth()), (getHeight()), 1f, 1f, 1f, 1f);
            batch.draw(null, (getX() + 5), (getY() + 5), (getWidth() - 10), (getHeight() - 10), .6f, .6f, .6f, 1f);
        } else if (this.state == State.Pressed) {
            batch.draw(null, (getX()), (getY()), (getWidth()), (getHeight()), 1f, 1f, 1f, 1f);
            batch.draw(null, (getX() + 5), (getY() + 5), (getWidth() - 10), (getHeight() - 10), .2f, .2f, .2f, 1f);
        }
        //System.out.println( text.length());
        if (textFieldState == TextFieldState.Focussed) {

            batch.draw(null, (getX() + 5) + ViewManager.font.getWidth(text.substring(0, pointer)), (getY() + 7), 3, (getHeight() - 15), 1f, 1f, 1f, 1f);
        }

        if (text.length() != 0 && text != null) {
            ViewManager.font.drawText(batch, text, (int) getX() + 5, (int) ((getY()) + (getHeight()) / 2 - ViewManager.font.getLineHeight() / 2));

        } else if (text.length() == 0 && textFieldState == TextFieldState.Unfocussed)
            ViewManager.font.drawText(batch, backText, (int) getX() + 5, (int) ((getY()) + (getHeight()) / 2 - ViewManager.font.getLineHeight() / 2));
    }

    public String getText() {
        return text;
    }
}
