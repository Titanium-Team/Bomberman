package bomberman.view.engine.components;

import bomberman.view.engine.View;

/**
 * Created by 204g13 on 29.03.2017.
 */
public class ChatWindow extends Panel {
    private VerticalList verticalList;
    private TextField textField;
    private Button sendButton;

    public ChatWindow(LayoutParams params, View v) {
        super(params, v);
        this.verticalList = new VerticalList(LayoutParams.obtain(0f,0f,1,0.8f),v);

        this.textField = new TextField(LayoutParams.obtain(0f,0.8f,0.8f,0.2f),v);

        this.sendButton = new Button(LayoutParams.obtain(0,0.8f,0.2f,0.2f),v,"Send");
        this.sendButton.addListener(()-> {
            verticalList.addChild(new Label(LayoutParams.obtain(0,0,1,1f/8f),this.getView(),textField.getText()));
            textField.setText("");
        });
    }

    public void addText(String text){
        verticalList.addChild(new Label(LayoutParams.obtain(0,0,1,1f/8f),this.getView(),text));
        verticalList.updateChildren();
    }
}
