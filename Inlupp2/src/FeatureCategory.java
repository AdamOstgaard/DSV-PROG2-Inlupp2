import java.awt.*;

public class FeatureCategory {
    private String name;
    private Color color;

    FeatureCategory(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color != null ? color : Color.BLACK;
    }

    @Override
    public String toString() {
        return getName();
    }
}
