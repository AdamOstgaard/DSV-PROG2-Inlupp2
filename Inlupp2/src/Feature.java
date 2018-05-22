import java.awt.*;

public class Feature {
    private Point point;
    private FeatureType type;
    private Triangle triangle;

    public Feature(int x, int y, FeatureType type) {
        point = new Point(x, y);
        triangle = new Triangle(50, Color.BLUE, point);

    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Triangle getTriangle() {
        return triangle;
    }

    public FeatureType getType() {
        return type;
    }

    public void setType(FeatureType type) {
        this.type = type;
    }
}
