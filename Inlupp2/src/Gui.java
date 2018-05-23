import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame{
    private Map map;
    private FeatureHandler featureHandler;

    public Gui(){
        this.setLayout(new BorderLayout());
        map = new Map();
        featureHandler = new FeatureHandler();
        featureHandler.attach(map);
        add(map, BorderLayout.WEST);
        setMinimumSize(new Dimension(map.getMapDimension()));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel north = new JPanel();
        JPanel South = new JPanel();
        JPanel East = new JPanel();

        JList<FeatureCategory> categories = new JList<>(
                new FeatureCategory[]{
                        new FeatureCategory("Buss", Color.CYAN),
                        new FeatureCategory("Underground", Color.RED),
                        new FeatureCategory("Train", Color.green)
                });

        East.add(categories);
        JButton hideCategoryButton = new JButton("Hide");
        East.add(hideCategoryButton);
        add(East, BorderLayout.EAST);

        JTextField searchText = new JTextField();
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener((ActionEvent) -> search(searchText.getText()));
        add(searchButton, BorderLayout.SOUTH);

    }

    private void search(String text) {
        featureHandler.getSelectedFeatures().forEach(p -> p.setSelected(false));
        if (text.isEmpty())
            return;

        featureHandler.getFeatures(text).forEach(p -> p.setSelected(true));
    }
}
