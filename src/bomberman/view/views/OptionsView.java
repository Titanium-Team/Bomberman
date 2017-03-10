package bomberman.view.views;

import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.Button;
import bomberman.view.engine.components.ButtonListener;
import bomberman.view.engine.components.Label;

/**
 * shows gamesettings
 **/
public class OptionsView extends BaseMenuView {

    private Label titleLabel;
    private Button backButton;

    public OptionsView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);

        this.titleLabel = new Label(200, 50, 200, 50, this, "Options");
        this.addComponent(titleLabel);

        this.backButton = new Button(50, 50, 100, 50, this, "Back");
        this.backButton.setListener(new ButtonListener() {
            @Override
            public void onClick() {
                OptionsView.this.navigateBack();
            }
        });
        this.addComponent(backButton);
    }

}