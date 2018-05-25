import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Gui extends JFrame{
    private Map map;
    private FeatureHandler featureHandler;

    public Gui(){
        initialize();
    }

    private void search(String text) {
        if (featureHandler == null || map == null) return;

        if (text.isEmpty()) return;

        featureHandler.getSelectedFeatures().forEach(p -> p.setState(FeatureState.UNSELECTED));

        HashSet<Feature> features = featureHandler.getFeatures(text);

        if (features != null && !features.isEmpty())
            features.forEach(p -> p.setState(FeatureState.SELECTED));
    }

    private void hideCategory(FeatureCategory category) {
        if (featureHandler == null || map == null) return;

        if (category == null) return;

        HashSet<Feature> features = featureHandler.getFeatures(category);

        if (features != null && !features.isEmpty())
            features.forEach(p -> p.setState(FeatureState.HIDDEN));
    }

    private void newFeature(FeatureCategory category, boolean isDescribed) {
        if (featureHandler == null || map == null) return;

        featureHandler.startListenForNewFeature(category, isDescribed);
    }

    private void removeFeatures() {
        if (featureHandler == null || map == null) return;

        ArrayList<Feature> features = featureHandler.getSelectedFeatures();

        if (features != null && !features.isEmpty())
            featureHandler.removeFeatures(features);

    }

    private void selectCoordinates() {
        if (featureHandler == null || map == null) return;

        JPanel panel = new JPanel();

        JLabel xLabel = new JLabel("x:");
        JLabel yLabel = new JLabel("y:");

        JTextField xText = new JTextField();
        JTextField yText = new JTextField();

        panel.add(xLabel);
        panel.add(xText);
        panel.add(yLabel);
        panel.add(yText);

        JOptionPane.showMessageDialog(this, panel, "Enter Coordinates", JOptionPane.QUESTION_MESSAGE);

        String x = xText.getText();
        String y = yText.getText();

        Position position;
        try {
            position = new Position(Integer.parseInt(x), Integer.parseInt(y));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid coordinates");
            return;
        }

        Feature feature = featureHandler.getFeature(position);

        if (feature == null) {
            JOptionPane.showMessageDialog(this, "No feature on selected coordinate");
            return;
        }

        ArrayList<Feature> selectedFeatures = featureHandler.getSelectedFeatures();

        if (selectedFeatures != null && !selectedFeatures.isEmpty())
            selectedFeatures.forEach(f -> f.setState(FeatureState.UNSELECTED));

        feature.setState(FeatureState.SELECTED);
    }

    private void showLoadMapDialog() {
        JFileChooser filePicker = new JFileChooser();

        if (filePicker.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
            return;

        File selectedMap = filePicker.getSelectedFile();
        loadMap(selectedMap);
    }

    private void loadMap(File selectedMap) {
        featureHandler = null;
        map = null;

        try {
            map = new Map(selectedMap);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return;
        }

        featureHandler = new FeatureHandler();
        featureHandler.attach(map);

        setMinimumSize(new Dimension((int) map.getMapDimension().getWidth() + 100, map.getHeight() + 50));
        setPreferredSize(new Dimension((int) map.getMapDimension().getWidth() + 100, map.getHeight() + 50));

        add(map, BorderLayout.WEST);

    }

    private void initialize() {
        setLayout(new BorderLayout());
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel north = new JPanel();
        JPanel South = new JPanel();
        JPanel East = new JPanel(new GridLayout(2, 1));

        JMenuItem loadMapItem = new JMenuItem("Load map");
        loadMapItem.addActionListener((ActionEvent -> showLoadMapDialog()));

        JMenu archiveMenu = new JMenu("Archive");
        archiveMenu.add(loadMapItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(archiveMenu);

        JList<FeatureCategory> categories = new JList<>(
                new FeatureCategory[]{
                        FeatureCategory.BUS,
                        FeatureCategory.TRAIN,
                        FeatureCategory.UNDERGROUND
                });

        JRadioButton namedPlace = new JRadioButton("Named place");
        namedPlace.setActionCommand("named");
        namedPlace.setSelected(true);

        JRadioButton describedPlace = new JRadioButton("Described place");
        describedPlace.setActionCommand("described");

        ButtonGroup placeTypeGroup = new ButtonGroup();
        placeTypeGroup.add(namedPlace);
        placeTypeGroup.add(describedPlace);

        JButton newButton = new JButton("New");
        newButton.addActionListener((ActionEvent) -> newFeature(categories.getSelectedValue(), describedPlace.isSelected()));

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener((ActionEvent) -> removeFeatures());

        JButton hideCategoryButton = new JButton("Hide Category");
        hideCategoryButton.addActionListener((ActionEvent) -> hideCategory(categories.getSelectedValue()));

        JButton coordinates = new JButton("Coordinates");
        coordinates.addActionListener((ActionEvent) -> selectCoordinates());

        East.add(categories);
        East.add(hideCategoryButton);

        JTextField searchText = new JTextField();
        searchText.setColumns(30);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener((ActionEvent) -> search(searchText.getText()));

        north.add(namedPlace);
        north.add(describedPlace);
        north.add(searchText);
        north.add(searchButton);
        north.add(newButton);
        north.add(removeButton);
        north.add(coordinates);

        setJMenuBar(menuBar);
        add(East, BorderLayout.EAST);
        add(north, BorderLayout.NORTH);

    }
}
