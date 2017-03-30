package bomberman.view.engine.components;

import bomberman.view.engine.View;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.utility.Utility;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class TextField extends ViewComponentClickable {


    private enum TextFieldState {
        Focussed, Unfocussed;
    }

    private String text, backText;
    private int pointer;
    private TextFieldState textFieldState = TextFieldState.Unfocussed;

    private List<TypeListener> typeListeners = new ArrayList<>();

    private boolean filterChars = false;
    private List<Character> acceptedChars = new ArrayList<>();

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

        this.addTypeListeners((key, c) -> {
            if (textFieldState == TextFieldState.Focussed) {
                if (key == Keyboard.KEY_BACK) {
                    if (getText() != null && !getText().isEmpty()) {
                        int tmp = getPointer() - 1;
                        int tmp2 = getPointer();
                        if (getPointer() == 0) {
                            tmp++;
                            tmp2++;
                        }
                        setText(getText().substring(0, tmp) + getText().substring(tmp2, getText().length()));
                        if (getPointer() > 0) {
                            setPointer(tmp2-1);
                        }
                    }
                    //System.out.println(text.length());
                } else if (key == Keyboard.KEY_LEFT) {
                    if (getPointer() > 0) {
                        setPointer(getPointer()-1);
                    }
                } else if (key == Keyboard.KEY_RIGHT) {
                    if (getPointer() < getText().length()) {
                        setPointer(getPointer()+1);
                    }
                } else {
                    if (isFilterChars()){
                        for (Character character : getAcceptedChars()){
                            if (character == c){
                                addChar(c);
                                break;
                            }
                        }
                    }else {
                        addChar(c);
                    }
                }
            }
        });

    }

    private int getPointer() {
        return pointer;
    }

    private void setPointer(int pointer) {
        this.pointer = pointer;
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

        for (TypeListener typeListener : typeListeners){
            typeListener.onType(key, c);
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

    public void setText(String text) {
        this.text = text;
        this.pointer=text.length();
    }


    public List<TypeListener> getTypeListeners() {
        return typeListeners;
    }

    public void addTypeListeners(TypeListener listener){
        typeListeners.add(listener);
    }

    public boolean isFilterChars() {
        return filterChars;
    }

    public void setFilterChars(boolean filterChars) {
        this.filterChars = filterChars;
    }

    public List<Character> getAcceptedChars() {
        return acceptedChars;
    }

    public void setAcceptedChars(List<Character> acceptedChars) {
        this.acceptedChars = acceptedChars;
    }

    public void setFilterOnlyNumbers(){
        setFilterChars(true);

        acceptedChars.add('0');
        acceptedChars.add('1');
        acceptedChars.add('2');
        acceptedChars.add('3');
        acceptedChars.add('4');
        acceptedChars.add('5');
        acceptedChars.add('6');
        acceptedChars.add('7');
        acceptedChars.add('8');
        acceptedChars.add('9');
    }

    public void addFilterChar(char c){
        acceptedChars.add(c);
    }
}
