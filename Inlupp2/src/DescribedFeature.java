import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class DescribedFeature extends NamedFeature {
    private final String description;

    public DescribedFeature(Position position, FeatureCategory type, String name, String description) {
        super(position, type, name);

        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    protected void rightMouseButtonClicked(MouseEvent e) {
        JTextArea nameText = new JTextArea(getName() + "(" + getPosition().toString() + ")");
        JTextArea descriptionText = new JTextArea(getDescription());

        JLabel nameLabel = new JLabel("Name", SwingConstants.RIGHT);
        JLabel descriptionLabel = new JLabel("Description", SwingConstants.RIGHT);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));
        panel.add(nameLabel);
        panel.add(nameText);
        panel.add(descriptionLabel);
        panel.add(descriptionText);

        JOptionPane.showMessageDialog(getMarker(), panel);
    }

    @Override
    public String serialize() {
        return String.format("Described,%1$s,%2$d,%3$d,%4$s,%5$s",
                getCategory().getName(),
                getPosition().getX(),
                getPosition().getY(),
                getName(),
                getDescription());
    }
}
