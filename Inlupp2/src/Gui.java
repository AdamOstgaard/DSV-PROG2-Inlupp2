import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;

public class Gui extends JFrame {
    private Map map;
    private FeatureHandler featureHandler;

    public Gui() {
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

        Position position;

        try {
            position = showSelectCoordinatesDialog();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Input");
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

    private Position showSelectCoordinatesDialog() {
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

        return new Position(Integer.parseInt(x), Integer.parseInt(y));
    }

    private void showLoadMapDialog() {
        if (checkAndConfirmFeatureDiscard()) return;

        JFileChooser filePicker = new JFileChooser();

        if (filePicker.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
            return;

        File selectedMap = filePicker.getSelectedFile();
        loadMap(selectedMap);
    }

    private void loadMap(File selectedMap) {
        featureHandler = null;

        if (map != null)
            remove(map);

        map = null;

        try {
            map = new Map(selectedMap);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return;
        }

        featureHandler = new FeatureHandler();
        featureHandler.attach(map);

        setMinimumSize(new Dimension((int) map.getMapDimension().getWidth() + 150, (int) map.getMapDimension().getHeight() + 50));
        setPreferredSize(new Dimension((int) map.getMapDimension().getWidth() + 150, (int) map.getMapDimension().getHeight() + 50));

        add(map, BorderLayout.WEST);
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void showSaveFeaturesDialog() {
        if (featureHandler == null || map == null) return;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showSaveDialog(this);

        ArrayList<String> features = featureHandler.serializeAllFeatures();

        if (features == null || features.isEmpty())
            return;

        try (PrintWriter out = new PrintWriter(fileChooser.getSelectedFile())) {
            for (String s : features) {
                out.println(s);
            }

            featureHandler.setHasStagedChanges(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showLoadFeaturesDialog() {
        if (checkAndConfirmFeatureDiscard()) return;


        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(this);

        File file = fileChooser.getSelectedFile();

        try {
            ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(file.toPath());
            featureHandler.deserializeAndLoadFeatures(lines);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Can't read file", "Error opening file", JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean checkAndConfirmFeatureDiscard() {
        if (featureHandler != null) {
            if (featureHandler.isHasStagedChanges()) {
                if (showConfirmExitDialog() == JOptionPane.YES_OPTION) {
                    featureHandler.removeAllFeatures();
                    featureHandler.setHasStagedChanges(false);
                } else {
                    return true;
                }
            } else {
                featureHandler.removeAllFeatures();
            }
        }
        return false;
    }

    private int showConfirmExitDialog() {
        return JOptionPane.showConfirmDialog(null,
                "There are unsaved changes! Are you sure you want to discard them?", "Discard changes?",
                JOptionPane.YES_NO_OPTION);
    }

    private void hideSelected() {
        featureHandler.getSelectedFeatures().forEach(f -> f.setState(FeatureState.HIDDEN));
    }

    private void initialize() {
        setLayout(new BorderLayout());
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (featureHandler != null && featureHandler.isHasStagedChanges()) {
                    if (showConfirmExitDialog() == JOptionPane.YES_OPTION) {
                        dispose();
                        System.exit(0);
                    }
                } else {
                    dispose();
                    System.exit(0);
                }
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 500));
        setMinimumSize(new Dimension(1000, 500));

        JPanel north = new JPanel();
        JPanel South = new JPanel();
        JPanel East = new JPanel(new GridLayout(2, 1));

        JMenuItem loadMapItem = new JMenuItem("Load map");
        loadMapItem.addActionListener(ActionEvent -> showLoadMapDialog());

        JMenuItem saveFeaturesItem = new JMenuItem("Save features");
        saveFeaturesItem.addActionListener(ActionEvent -> showSaveFeaturesDialog());

        JMenuItem loadFeaturesItem = new JMenuItem("Open features");
        loadFeaturesItem.addActionListener(ActionEvent -> showLoadFeaturesDialog());

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(ActionEvent -> this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

        JMenu archiveMenu = new JMenu("Archive");
        archiveMenu.add(loadMapItem);
        archiveMenu.add(saveFeaturesItem);
        archiveMenu.add(loadFeaturesItem);
        archiveMenu.add(exitItem);

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
        newButton.addActionListener(ActionEvent -> newFeature(categories.getSelectedValue(), describedPlace.isSelected()));

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(ActionEvent -> removeFeatures());

        JButton hideCategoryButton = new JButton("Hide Category");
        hideCategoryButton.addActionListener((ActionEvent) -> hideCategory(categories.getSelectedValue()));

        JButton coordinates = new JButton("Coordinates");
        coordinates.addActionListener(ActionEvent -> selectCoordinates());

        JButton hideButton = new JButton("Hide");
        hideButton.addActionListener(ActionEvent -> hideSelected());

        East.add(categories);
        East.add(hideCategoryButton);

        JTextField searchText = new JTextField();
        searchText.setColumns(15);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(ActionEvent -> search(searchText.getText()));

        north.add(namedPlace);
        north.add(describedPlace);
        north.add(searchText);
        north.add(searchButton);
        north.add(newButton);
        north.add(removeButton);
        north.add(coordinates);
        north.add(hideButton);

        setJMenuBar(menuBar);
        add(East, BorderLayout.EAST);
        add(north, BorderLayout.NORTH);

        SwingUtilities.updateComponentTreeUI(this);
    }
}
