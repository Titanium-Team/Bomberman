package bomberman.view.engine.components.popups;

import bomberman.view.engine.View;
import bomberman.view.engine.components.Button;
import bomberman.view.engine.components.Label;
import bomberman.view.engine.components.LayoutParams;

/**
 * Created by Tim Bolz on 30.03.2017.
 */
public class ExceptionPopup extends PopupWindow {
	private Label text;
	private Button continueButton, closeButton;

	public ExceptionPopup(View v, String msg) {

		super(LayoutParams.obtain(0.3f, 0.3f, 0.4f, 0.4f), v);
		this.text = new Label(LayoutParams.obtain(0.2f,0.2f,0.6f,0.4f),v,msg);
		this.addChild(text);

		this.getExitButton().setText("Continue");
		this.getExitButton().setParams(LayoutParams.obtain(0.59f,0.01f,0.36f,0.16f));

	}}
