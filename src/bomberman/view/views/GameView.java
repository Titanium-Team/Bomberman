package bomberman.view.views;

import bomberman.gameplay.GameMap;
import bomberman.gameplay.GameplayManager;
import bomberman.gameplay.Player;
import bomberman.gameplay.properties.PropertyTypes;
import bomberman.gameplay.tile.Tile;
import bomberman.gameplay.tile.TileAbility;
import bomberman.gameplay.tile.TileTypes;
import bomberman.gameplay.tile.objects.Bomb;
import bomberman.gameplay.tile.objects.Explosion;
import bomberman.gameplay.tile.objects.PowerUp;
import bomberman.gameplay.utils.BoundingBox;
import bomberman.gameplay.utils.Location;
import bomberman.view.engine.Light;
import bomberman.view.engine.LightingView;
import bomberman.view.engine.View;
import bomberman.view.engine.ViewManager;
import bomberman.view.engine.components.*;
import bomberman.view.engine.components.Button;
import bomberman.view.engine.components.Label;
import bomberman.view.engine.components.popups.ChatWindow;
import bomberman.view.engine.components.popups.PopupWindow;
import bomberman.view.engine.rendering.Batch;
import bomberman.view.engine.rendering.ITexture;
import bomberman.view.engine.utility.Camera;
import bomberman.view.engine.utility.Vector2;
import net.java.games.input.Component;
import org.lwjgl.input.Keyboard;

import java.util.*;

/**
 * Ingame View
 **/
public class GameView extends LightingView {

	private ChatWindow chatWindow;
	private GameplayManager gameplayManager;
    private float time = 0f;
    private static final Random random = new Random();
    private int tileSize = 50;
    private HashMap<Player, Light> playerLightMap = new HashMap<>();

    private PausePopup pausePopup;
	private StatPopup statPopup;

    public GameView(int width, int height, ViewManager viewManager) {
        super(width, height, viewManager);
	    this.chatWindow = new ChatWindow(this);


        this.pausePopup = new PausePopup(this);

        this.setBlockBackNavigation(true);
    }

    public GameplayManager getGameplayManager() {
        return gameplayManager;
    }

    public void setGameplayManager(GameplayManager gameplayManager) {
        this.gameplayManager = gameplayManager;
        playerLightMap.clear();
        for (int i = 0; i < gameplayManager.getCurrentSession().getPlayers().size(); i++) {
            addPlayer(gameplayManager.getCurrentSession().getPlayer(i));
        }
        gameplayManager.setGameState(GameplayManager.GameState.IN_GAME);

	    this.statPopup = new StatPopup(this);
	    this.statPopup.showSelf();
	    
    }

    /**
     * Aktualisiert die GameView.
     *
     * @param deltaTime die Zeit, die seit dem letzten Frame vergangen ist.
     */
    public void update(float deltaTime) {
        time += deltaTime;

        Player localPlayer = gameplayManager.getCurrentSession().getLocalPlayer();
        Location center = localPlayer.getBoundingBox().getCenter();
        this.getSceneCamera().setTranslation(new Vector2((float) center.getX() * tileSize, (float) center.getY() * tileSize));

        for (Map.Entry<Player, Light> e : playerLightMap.entrySet()) {
            Light playerLight = e.getValue();
            Location playerLocation = e.getKey().getBoundingBox().getCenter();
            playerLight.setX(((float) playerLocation.getX()) * tileSize);
            playerLight.setY(((float) playerLocation.getY()) * tileSize);
        }
    }

    private void addPlayer(Player player) {
        Location playerLocation = player.getBoundingBox().getCenter();
        Light playerLight = randomLight(((float) playerLocation.getX()) * tileSize, ((float) playerLocation.getY()) * tileSize); // TODO: adjust color
        playerLight.setOwner(player);
        playerLightMap.put(player, playerLight);
        this.addLight(playerLight);
    }

    /**
     * Zeichnet die Objekte, die Schatten werfen.
     *
     * @param batch  Der zum zeichnen verwendete Batch.
     * @param camera Der aktuell sichtbare Bildschirmausschnit.
     */
    @Override
    public void renderOccluders(Batch batch, Camera camera) {
        GameMap map = gameplayManager.getCurrentSession().getGameMap();
        Tile[][] tiles = map.getTiles();
        for (int i = Math.max(0, (int) (camera.getTranslation().getX() - camera.getWidth() / 2) / this.tileSize); i < tiles.length &&
                i * this.tileSize < camera.getTranslation().getX() + camera.getWidth() / 2; i++) {
            if (tiles[i] != null) {
                for (int j = Math.max(0, (int) (camera.getTranslation().getY() - camera.getHeight() / 2) / this.tileSize); j < tiles[i].length &&
                        j * this.tileSize < camera.getTranslation().getY() + camera.getHeight() / 2; j++) {
                    if (tiles[i][j] != null) {
                        if (!tiles[i][j].getTileType().isWalkable()) {
                            if (tiles[i][j].getTileType().equals(TileTypes.WALL)) {
                                batch.draw(ViewManager.getTexture("wall.png"), i * this.tileSize, j * this.tileSize, this.tileSize + 1, this.tileSize + 1, 0.5f, 0.5f, 0.5f, 1);
                            } else if (tiles[i][j].getTileType().equals(TileTypes.WALL_BREAKABLE)) {
                                batch.draw(ViewManager.getTexture("breakableWall.png"), i * this.tileSize, j * this.tileSize, this.tileSize + 1, this.tileSize + 1, 0.25f, 0.25f, 0.25f, 1);
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < gameplayManager.getCurrentSession().getPlayers().size(); i++) {
            Player player = gameplayManager.getCurrentSession().getPlayer(i);

            if (!playerLightMap.containsKey(player)) {
                addPlayer(player);
            }

            if (playerLightMap.get(player).getLightCamera() != camera) {
                BoundingBox b = player.getBoundingBox();
                ITexture tex = ViewManager.getTexture("SwagBear.png");
                if (player.getIndex() == 2) {
                    tex = ViewManager.getTexture("YellowBear.png");
                } else if (player.getIndex() == 3) {
                    tex = ViewManager.getTexture("SwagBear.png");
                } else if (player.getIndex() == 4) {
                    tex = ViewManager.getTexture("DrunkBeer.png");
                }

                float rotation = 0;
                /** Für Timmy Tim, den ********* **/
                switch(player.getFacingDirection()){
                    case NORTH_EAST:
                        rotation = (float) Math.toRadians(45);
                        break;
                    case EAST:
                        rotation = (float) Math.toRadians(90);
                        break;
                    case SOUTH_EAST:
                        rotation = (float) Math.toRadians(90 + 45);
                        break;
                    case SOUTH:
                        rotation = (float) Math.toRadians(180);
                        break;
                    case SOUTH_WEST:
                        rotation = (float) Math.toRadians(180 + 45);
                        break;
                    case WEST:
                        rotation = (float) Math.toRadians(270);
                        break;
                    case NORTH_WEST:
                        rotation = (float) Math.toRadians(270 + 45);
                        break;
                }

                batch.draw(tex, (float) b.getMin().getX() * tileSize, (float) b.getMin().getY() * tileSize, (float) b.getWidth() * tileSize, (float) b.getHeight() * tileSize, rotation);
            }
        }
    }

    /**
     * Zeichnet die objekte, die keine Schatten werfen.
     *
     * @param batch  Der zum zeichnen verwendete Batch.
     * @param camera Der aktuell sichtbare Bildschirmausschnit.
     */
    @Override
    public void renderNonOccluders(Batch batch, Camera camera) {
        GameMap map = gameplayManager.getCurrentSession().getGameMap();
        Tile[][] tiles = map.getTiles();
        for (int i = Math.max(0, (int) (camera.getTranslation().getX() - camera.getWidth() / 2) / this.tileSize); i < tiles.length &&
                i * this.tileSize < camera.getTranslation().getX() + camera.getWidth() / 2; i++) {
            if (tiles[i] != null) {
                for (int j = Math.max(0, (int) (camera.getTranslation().getY() - camera.getHeight() / 2) / this.tileSize); j < tiles[i].length &&
                        j * this.tileSize < camera.getTranslation().getY() + camera.getHeight() / 2; j++) {
                    if (tiles[i][j] != null) {
                        if (tiles[i][j].getTileType().isWalkable()) {
                            batch.draw(ViewManager.getTexture("ground.png"), i * this.tileSize, j * this.tileSize, this.tileSize, this.tileSize, 1f, 1f, 1f, 1);
                        }
                        if (tiles[i][j].getTileType().isWalkable()) {
	                        if (tiles[i][j].getTileAbility().equals(TileAbility.TREADMILL)) {
		                        double rotation;
		                        switch (tiles[i][j].getTreadMillDirection()) {
			                        case NORTH:
				                        rotation = Math.toRadians(90.0);
				                        break;
			                        case EAST:
				                        rotation = Math.toRadians(180.);
				                        break;
			                        case SOUTH:
				                        rotation = Math.toRadians(270.);
				                        break;
			                        default:
				                        rotation = 0;
				                        break;
		                        }
		                        batch.draw(ViewManager.getTexture("arrow.png"), i * this.tileSize, j * this.tileSize, this.tileSize, this.tileSize, 0.5f * this.tileSize, 0.5f * this.tileSize, (float) rotation, 1, 1, 1, 1);
	                        }
	                        if (tiles[i][j].getTileObject() != null) {
                                if (tiles[i][j].getTileObject() instanceof Bomb) {
                                    batch.draw(ViewManager.getTexture("bomb.png"), i * this.tileSize, j * this.tileSize, this.tileSize, this.tileSize, 1, 1, 1, 1);
                                } else if (tiles[i][j].getTileObject() instanceof PowerUp) {
                                    //TODO:additional textures
                                    ITexture texture = null;
                                    switch (((PowerUp) (tiles[i][j].getTileObject())).getPowerUpType()) {
                                        case SPEEDUP:
                                            texture = ViewManager.getTexture("SpeedUp.png");
                                            break;
                                        case SPEEDDOWN:
                                            texture = ViewManager.getTexture("SpeedDown.png");
                                            break;
                                        case FIREUP:
                                            texture = ViewManager.getTexture("FireUp.png");
                                            break;
                                        case FIREDOWN:
                                            texture = ViewManager.getTexture("firedown.png");
                                            break;
                                        case BOMBUP:
                                            texture = ViewManager.getTexture("BombUp.png");
                                            break;
                                        case BOMBDOWN:
                                            texture = ViewManager.getTexture("BombDown.png");
                                            break;
                                        case POWERBOMB:
                                            texture = ViewManager.getTexture("PowerBomb.png");
                                            break;
                                        case SPIKEBOMB:
                                            texture = ViewManager.getTexture("SpikeBomb.png");
                                    }
                                    batch.draw(texture, i * this.tileSize, j * this.tileSize, this.tileSize, this.tileSize);
                                } else if (tiles[i][j].getTileObject() instanceof Explosion) {
                                    batch.draw(((Explosion) tiles[i][j].getTileObject()).getAnimation(), i * this.tileSize, j * this.tileSize, this.tileSize, this.tileSize, 1, 1, 1, 1);
                                }
                            } else if (tiles[i][j].getTileAbility().equals(TileAbility.TELEPORT) || tiles[i][j].getTileAbility().equals(TileAbility.RANDOM_TELEPORT)) {
                                batch.draw(ViewManager.getTexture("teleport.png"), i * this.tileSize, j * this.tileSize, this.tileSize, this.tileSize, 1, 1, 1, 1);
                            }
                        }
                    }
                }
            }
        }
    }

    public void onKeyUp(int key, char c) {
        super.onKeyUp(key, c);

        if (key == Keyboard.KEY_ESCAPE) {
            if (this.pausePopup.isShown()) {
                this.pausePopup.closeSelf();
            } else {
                this.pausePopup.showSelf();
            }
        }else if(key == Keyboard.KEY_T){
            if(chatWindow.isShown()){
	            if(!chatWindow.isTextFieldFocused()) {
		            this.chatWindow.closeSelf();
	            }
            }else{
                this.chatWindow.showSelf();
            }
        }
    }

    public void onGamepadEvent(Component component, float value) {
        super.onGamepadEvent(component, value);

        if (component.getIdentifier() == Component.Identifier.Button._7 && value == 0) {
            if (this.pausePopup.isShown()) {
                this.pausePopup.closeSelf();
            } else {
                this.pausePopup.showSelf();
            }
        }
    }

    /**
     * Generiert ein zufällig gefärbtes Licht.
     *
     * @param x Die x-Koordinate des Mittelpunktes.
     * @param y Die y-Koordinate des Mittelpunktes.
     * @return Ein zufällig gefärbtes Licht.
     */
    private Light randomLight(float x, float y) {
        float r = random.nextFloat() / 2 + 0.5f;
        float g = random.nextFloat() / 2 + 0.5f;
        float b = random.nextFloat() / 2 + 0.5f;

        return new Light(x, y, 400, r, g, b);
    }

    private class PausePopup extends PopupWindow {

        private Button quitGameButton;

        public PausePopup(View v) {
            super(LayoutParams.obtain(0.3f, 0.3f, 0.4f, 0.4f), v);

            this.quitGameButton = new Button(LayoutParams.obtain(0.3f, 0.3f, 0.4f, 0.4f), v, "Quit Game");
            this.quitGameButton.addListener(() -> {
                gameplayManager.setGameState(GameplayManager.GameState.IN_MENU);

                GameView.this.navigateBack();
            });
            this.addChild(quitGameButton);
        }
    }

	private class StatPopup extends PopupWindow {

        private Label range, powerup;

        public StatPopup(View v) {
            super(LayoutParams.obtain(0, 0, 0.2f, 1), v);
            this.range = new Label(LayoutParams.obtain(0, 0.2f, 1, 0.1f), v, "Range :" + (int) gameplayManager.getCurrentSession().getLocalPlayer().getPropertyRepository().getValue(PropertyTypes.BOMB_BLAST_RADIUS));
            this.addChild(range);
            this.powerup = new Label(LayoutParams.obtain(0, 0.3f, 1, 0.1f), this.getView(),"");
            this.addChild(powerup);
            this.getView().requestLayout();
        }

        @Override
        public void draw(Batch batch) {
            super.draw(batch);
            float max = this.getWidth() * 0.8f;
            batch.draw(null, this.getX() + 0.1f * this.getWidth(), this.getY() + 0.1f * this.getHeight(), max * ((float) gameplayManager.getCurrentSession().getLocalPlayer().getHealth() / gameplayManager.getCurrentSession().getLocalPlayer().getPropertyRepository().getMax(PropertyTypes.HEALTH)), 0.1f * this.getHeight(), 1, 0, 0, 0.4f);
            if (gameplayManager.getCurrentSession().getLocalPlayer().getLastPowerup() != null) {
                    powerup.setText("Picked up " + gameplayManager.getCurrentSession().getLocalPlayer().getLastPowerup().toString());

            }
            range.setText("Range :" + (int) gameplayManager.getCurrentSession().getLocalPlayer().getPropertyRepository().getValue(PropertyTypes.BOMB_BLAST_RADIUS));

        }

    }

	private class ChatNotificationPopup extends PopupWindow{
		private Label text;
		private Button showButton;

		public ChatNotificationPopup(View v, String msg) {

			super(LayoutParams.obtain(0.3f, 0.3f, 0.4f, 0.4f), v);
			this.text = new Label(LayoutParams.obtain(0.2f,0.2f,0.6f,0.4f),v,msg);
			this.addChild(text);

			this.showButton = new Button(LayoutParams.obtain(0.4f,0.65f,0.2f,0.2f),v,"Show");
			showButton.addListener(()->{
				this.closeSelf();
				chatWindow.showSelf();
			});
			this.addChild(showButton);
		}
	}

	public void receive(String msg,String name){
		this.chatWindow.addText(msg,name);
		if(!chatWindow.isShown()){
			ChatNotificationPopup popup = new ChatNotificationPopup(this,name + ": " + msg);
			popup.showSelf();
		}
	}

}