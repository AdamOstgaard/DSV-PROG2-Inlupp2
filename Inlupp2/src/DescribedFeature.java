import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class DescribedFeature extends NamedFeature {
    private String description;

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
}
