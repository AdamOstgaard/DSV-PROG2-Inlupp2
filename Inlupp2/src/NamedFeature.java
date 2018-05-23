import javax.swing.*;
import java.awt.event.MouseEvent;

public class NamedFeature extends Feature {

    private String name;

    public NamedFeature(Position position, FeatureCategory type, String name) {
        super(position, type);
        this.name = name;
    }

    @Override
    protected void leftMouseButtonClicked(MouseEvent e) {

    }

    @Override
    protected void rightMouseButtonClicked(MouseEvent e) {
        JOptionPane.showMessageDialog(this, name + "(" + getPosition().toString() + ")");
    }
}
