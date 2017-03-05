package bomberman.gameplay;

/**
 * Created by Daniel on 05.03.2017.
 */
public enum PowerUpType{

    DummyPowerUp("Hallo! Ich bin ein Dummy-Powerup!");

    private String description;

    PowerUpType(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
