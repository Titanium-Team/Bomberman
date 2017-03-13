package bomberman.gameplay;

import bomberman.view.engine.utility.Vector2;

public class Player {

    private final static double COLLISION_WIDTH = .8;
    private final static double COLLISION_HEIGHT = .8;

    private final String name;
    private final BoundingBox boundingBox;

    private double health;

    private final Vector2 vector = new Vector2(0, 0);

    private double speedFactor = 1.0D;

    public Player(String name, Location center) {
        this.name = name;

        this.boundingBox = new BoundingBox(
                new Location(center.getX() - COLLISION_WIDTH, center.getY() - COLLISION_HEIGHT),
                new Location(center.getX() + COLLISION_WIDTH, center.getY() + COLLISION_HEIGHT)
        );

    }

    public String getName() {
        return this.name;
    }

    public Vector2 getVector() {
        return this.vector;
    }

    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }
}
