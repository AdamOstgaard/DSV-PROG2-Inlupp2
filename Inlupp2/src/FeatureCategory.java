import java.awt.*;

public enum FeatureCategory {
    BUS("Bus", Color.RED),
    TRAIN("Train", Color.GREEN),
    UNDERGROUND("Underground", Color.BLUE),
    NONE("None", Color.BLACK);

    private final String name;
    private final Color color;

    FeatureCategory(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return getName();
    }
}
