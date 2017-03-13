package bomberman.gameplay;

public class BoundingBox {

    private final Location min;
    private final Location max;

    private final double width;
    private final double height;

    public BoundingBox(Location a, Location b) {
        this(
                Math.min(a.getX(), b.getX()),
                Math.min(a.getY(), b.getY()),
                Math.max(a.getX(), b.getX()),
                Math.max(a.getY(), b.getY())
        );
    }

    public BoundingBox(double minX, double minY, double maxX, double maxY) {
        this.min = new Location(minX, minY);
        this.max = new Location(maxX, maxY);
        this.width = Math.abs(this.max.getX() - this.min.getX());
        this.height = Math.abs(this.max.getY() - this.min.getY());
    }

    public void move(double x, double y) {
        this.max.add(x, y);
        this.min.add(x, y);
    }

    public void setCenter(double x, double y) {
        this.min.set(
                x - (this.getWidth() / 2),
                y - (this.getHeight() / 2)
        );
        this.max.set(
                x + (this.getWidth() / 2),
                y + (this.getHeight() / 2)
        );
    }

    public void setCenter(Location center) {
        this.setCenter(center.getX(), center.getY());
    }

    public Location getMax() {
        return this.max;
    }

    public Location getCenter() {
        return new Location(((this.getMin().getX() + this.getMax().getX()) / 2.0D), ((this.getMin().getY() + this.getMax().getY()) / 2.0D));
    }

    public Location getMin() {
        return this.min;
    }

    public double getHeight() {
        return this.height;
    }

    public double getWidth() {
        return this.width;
    }

    public boolean contains(BoundingBox other) {
        return (
                this.getMin().getX() <= other.getMin().getX() &&
                        this.getMax().getX() >= other.getMax().getX() &&
                        this.getMin().getY() <= other.getMin().getY() &&
                        this.getMax().getY() >= other.getMax().getY()
        );
    }

    public boolean intersects(BoundingBox other) {
        return (
                this.getMin().getX() < other.getMax().getX() &&
                        this.getMax().getX() > other.getMin().getX() &&
                        this.getMin().getY() < other.getMax().getY() &&
                        this.getMax().getY() > other.getMin().getY()
        );
    }


    public boolean contains(Location location) {
        return (
                location.getX() >= this.min.getX() &&
                        location.getX() <= this.max.getX() &&
                        location.getY() >= this.min.getY() &&
                        location.getY() <= this.max.getY()
        );
    }

}
