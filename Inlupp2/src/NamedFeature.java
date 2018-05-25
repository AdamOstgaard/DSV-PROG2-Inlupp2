import javax.swing.*;
import java.awt.event.MouseEvent;

public class NamedFeature extends Feature {

    public NamedFeature(Position position, FeatureCategory type, String name) {
        super(position, type, name);
    }

    @Override
    protected void rightMouseButtonClicked(MouseEvent e) {
        JOptionPane.showMessageDialog(getMarker(), "name: " + getName() + "(" + getPosition().toString() + ")");
    }

    @Override
    public String serialize() {
        return String.format("Described,%1$s,%2$d,%3$d,%4$s",
                getCategory().getName(),
                getPosition().getX(),
                getPosition().getY(),
                getName());
    }
}
