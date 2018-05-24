import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

public class Gui extends JFrame{
    private Map map;
    private FeatureHandler featureHandler;

    public Gui(){

        map = new Map();
        featureHandler = new FeatureHandler();
        featureHandler.attach(map);

        setLayout(new BorderLayout());
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

        JRadioButton namedPlace = new JRadioButton("Named place");
        namedPlace.setActionCommand("named");

        JRadioButton descriptedPlace = new JRadioButton("Descripted place");
        descriptedPlace.setActionCommand("described");

        ButtonGroup placeTypeGroup = new ButtonGroup();
        placeTypeGroup.add(namedPlace);
        placeTypeGroup.add(descriptedPlace);

        JButton newButton = new JButton("New");
        newButton.addActionListener((ActionEvent) -> newFeature(categories.getSelectedValue(), namedPlace.isSelected()));

        north.add(namedPlace);
        north.add(descriptedPlace);

        East.add(categories);
        JButton hideCategoryButton = new JButton("Hide");
        hideCategoryButton.addActionListener((ActionEvent) -> hideCategory(categories.getSelectedValue()));
        East.add(hideCategoryButton);

        JTextField searchText = new JTextField();
        searchText.setColumns(100);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener((ActionEvent) -> search(searchText.getText()));

        north.add(searchText);
        north.add(searchButton);
        north.add(newButton);

        add(map, BorderLayout.WEST);
        add(East, BorderLayout.EAST);
        add(north, BorderLayout.NORTH);
    }

    private void search(String text) {
        featureHandler.getSelectedFeatures().forEach(p -> p.setState(FeatureState.UNSELECTED));
        if (text.isEmpty())
            return;

        HashSet<Feature> features = featureHandler.getFeatures(text);

        if (!features.isEmpty())
            features.forEach(p -> p.setState(FeatureState.SELECTED));
    }

    private void hideCategory(FeatureCategory category) {
        HashSet<Feature> features = featureHandler.getFeatures(category);

        if (!features.isEmpty())
            features.forEach(p -> p.setState(FeatureState.HIDDEN));
    }

    private void newFeature(FeatureCategory category, boolean isDescripted) {
        featureHandler.startListenForNewFeature(category, isDescripted);
    }
}
