package bomberman.view.views;

import bomberman.Main;
import bomberman.gameplay.GameMap;
import bomberman.gameplay.GameplayManager;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.*;

/**
 * shows Players and GameSettings (not the Options)
 **/

/**
 * leads to GameView
 **/
public class LobbyView extends BaseMenuView {

    private Button startButton;

    private VerticalView mapVotingList;

    public LobbyView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);

        backButton.addListener(() -> {
            Main.instance.getNetworkController().leave();
        });

        this.startButton = new Button(LayoutParams.obtain(0.1f, 0.25f, 0.2f, 0.1f), this, "Start Game");
        this.startButton.addListener(() -> LobbyView.this.changeView(GameView.class));
        this.getRoot().addChild(startButton);

        this.mapVotingList = new VerticalView(LayoutParams.obtain(0.55f, 0.05f, 0.4f, 0.9f), this);
        this.getRoot().addChild(mapVotingList);

        GameplayManager gameplayManager = Main.instance.getGameplayManager();
        for (int i = 0; i < gameplayManager.getMapCount(); i++) {
            GameMap map = gameplayManager.getMap(i);
            final int index = i;

            Panel container = new Panel(LayoutParams.obtain(0, 0, 1, 1), this);

            Label nameLabel = new Label(LayoutParams.obtain(0f, 0f, 0.8f, 0.3f), this, map.getName());
            container.addChild(nameLabel);

            Image thumbnailImage = new Image(LayoutParams.obtain(0f, 0.3f, 0.8f, 0.7f), this, map.getThumbnailKey());
            container.addChild(thumbnailImage);

            Button voteButton = new Button(LayoutParams.obtain(0.8f, 0f, 0.2f, 1f), this, "Vote");
            voteButton.addListener(() -> {
                gameplayManager.setMapIndex(index);
            });
            container.addChild(voteButton);

            this.mapVotingList.addChild(container);
        }
    }

}