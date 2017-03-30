package bomberman.view.engine.components;

import bomberman.Main;
import bomberman.view.engine.View;
import org.lwjgl.input.Keyboard;

/**
 * Created by 204g13 on 29.03.2017.
 */
public class ChatWindow extends PopupWindow {
    private VerticalList verticalList;
    private TextField textField;
    private Button sendButton;

    public ChatWindow(View v) {
        super(LayoutParams.obtain(0.7f,0,0.3f,1), v);
        this.verticalList = new VerticalList(LayoutParams.obtain(0f,0.11f,1,0.69f),v);
        this.addChild(verticalList);

        this.textField = new TextField(LayoutParams.obtain(0f,0.81f,0.8f,0.19f),v);
        this.addChild(textField);

        this.sendButton = new Button(LayoutParams.obtain(0.8f,0.81f,0.2f,0.19f),v,"Send");
        this.sendButton.addListener(this::send);
        this.addChild(sendButton);
    }

    public void addText(String text, String playerName){
	    Label label = new Label(LayoutParams.obtain(0, 0, 0.9f, 1f / verticalList.getMaxSize()), this.getView(), playerName + ": " + text);
	    label.setAlignLeft(true);
	    verticalList.addChild(label);
    }

    @Override
    public void onKeyUp(int key, char c) {
        if(key == Keyboard.KEY_RETURN){
            this.send();
        }
    }

    private void send(){
        Main.instance.getNetworkController().chatMessage(textField.getText());
        this.addText(textField.getText(),"Du");
        textField.setText("");
    }

	public boolean isTextFieldFocused(){
		return textField.isSelected();
	}
}
