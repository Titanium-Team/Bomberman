package bomberman.gameplay.utils;

public class CircleBox {

    private Location center;
    private final double radius;

    public CircleBox(Location center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public Location getCenter() {
        return this.center;
    }

    public double getRadius() {
        return this.radius;
    }

    public boolean intersects(double x, double y, double radius) {
        return (Math.pow(radius - this.getRadius(), 2) >= (Math.pow(x - this.getCenter().getX(), 2) + Math.pow(y - this.getCenter().getY(), 2)));
    }

    public boolean intersects(Location center, double radius) {
        return this.intersects(new CircleBox(center, radius));
    }

    public boolean intersects(CircleBox circleBox) {
        return this.intersects(circleBox.getCenter().getX(), circleBox.getCenter().getY(), circleBox.getRadius());
    }

    public void setCenter(Location center) {
        this.center = center;
    }

    public void move(double x, double y) {
        this.center.add(x, y);
    }

}
