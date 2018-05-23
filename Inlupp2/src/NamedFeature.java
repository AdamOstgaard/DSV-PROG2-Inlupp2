import javax.swing.*;
import java.awt.event.MouseEvent;

public class NamedFeature extends Feature {

    public NamedFeature(Position position, FeatureCategory type, String name) {
        super(position, type, name);
    }

    @Override
    protected void leftMouseButtonClicked(MouseEvent e) {

    }

    @Override
    protected void rightMouseButtonClicked(MouseEvent e) {
        JOptionPane.showMessageDialog(getMarker(), getName() + "(" + getPosition().toString() + ")");
    }
}
