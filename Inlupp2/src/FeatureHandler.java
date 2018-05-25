import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class FeatureHandler implements IMapIsReadyListener, MouseListener, SelectedStateListener {
    private Map map;
    private Dimension clickBoundries;
    private FeatureCollection featureCollection;
    private FeatureCategory newFeatureCategory;
    private boolean newFeatureIsDescribed;

    private boolean hasStagedChanges;

    public boolean isHasStagedChanges() {
        return hasStagedChanges;
    }

    public void setHasStagedChanges(boolean hasStagedChanges) {
        this.hasStagedChanges = hasStagedChanges;
    }

    public FeatureHandler() {
        featureCollection = new FeatureCollection();
    }

    public void attach(Map map) {
        this.map = map;
        map.addMapIsReadyListener(this);
    }

    @Override
    public void readyStateChanged(boolean isReady) {
        if (isReady)
            initiateBoundries();
    }

    private void initiateBoundries() {
        clickBoundries = map.getMapDimension();
    }

    private void addFeature(MouseEvent e) {
        Position position = new Position(e.getX(), e.getY());

        if (!isWithinMap(position)) return;

        if (featureCollection.getFeature(position) != null) {
            JOptionPane.showMessageDialog(
                    map,
                    "Feature already exists on this position!",
                    "Error!",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Feature feature = newFeatureIsDescribed ?
                createDescribedFeature(position, newFeatureCategory) :
                createNamedFeature(position, newFeatureCategory);

        add(feature);

        map.removeMouseListener(this);
        map.setCursor(Cursor.getDefaultCursor());
    }

    private void add(Feature feature) {
        feature.addSelectedStateEventListener(this);
        featureCollection.add(feature);

        map.add(feature.getMarker());
        map.updateUI();
        hasStagedChanges = true;
    }

    private DescribedFeature createDescribedFeature(Position position, FeatureCategory category) {
        JTextField nameField = new JTextField("Name", 20);
        JTextField descriptionField = new JTextField("Description", 50);

        JPanel panel = new JPanel();
        panel.add(nameField);
        panel.add(descriptionField);

        JOptionPane.showMessageDialog(map, panel);

        String name = nameField.getText();
        String description = descriptionField.getText();

        return new DescribedFeature(position, category, name, description);
    }

    private NamedFeature createNamedFeature(Position position, FeatureCategory category) {
        String name = JOptionPane.showInputDialog(
                map,
                "Enter name",
                "New named feature"
        );

        return new NamedFeature(position, category, name);
    }

    private boolean isWithinMap(Position position) {
        return (position.getX() < clickBoundries.getWidth() && position.getY() < clickBoundries.getHeight());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        addFeature(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public Feature getFeature(Position position) {
        return featureCollection.getFeature(position);
    }

    public HashSet<Feature> getFeatures(FeatureCategory category) {
        return featureCollection.getFeatures(category);
    }

    public HashSet<Feature> getFeatures() {
        return featureCollection.getFeatures();
    }

    public HashSet<Feature> getFeatures(String name) {
        return featureCollection.getFeatures(name);
    }

    public ArrayList<Feature> getSelectedFeatures() {
        return new ArrayList<Feature>(featureCollection.getSelectedFeatures());
    }

    public ArrayList<Feature> getHiddenFeatures() {
        return new ArrayList<Feature>(featureCollection.getHiddenFeatures());
    }

    @Override
    public void selectedStateChanged(Feature sender, FeatureState newState) {
        featureCollection.removeSelectedFeature(sender);
        featureCollection.removeHiddenFeature(sender);

        if (newState == FeatureState.SELECTED)
            featureCollection.addSelectedFeature(sender);

        else if (newState == FeatureState.HIDDEN)
            featureCollection.addHiddenFeature(sender);
    }

    public void startListenForNewFeature(FeatureCategory category, boolean isDescripted) {
        map.addMouseListener(this);
        map.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

        newFeatureCategory = category;
        newFeatureIsDescribed = isDescripted;
    }

    public void removeFeatures(Collection<Feature> features) {
        if (features == null || features.isEmpty())
            return;

        for (Feature feature : features) {
            featureCollection.remove(feature);
            map.remove(feature.getMarker());
        }

        hasStagedChanges = true;

        map.updateUI();
    }

    public ArrayList<String> serializeAllFeatures() {
        ArrayList<String> serializedFeatures = new ArrayList<>();

        HashSet<Feature> features = getFeatures();
        if (features != null && !features.isEmpty())
            features.forEach(f -> serializedFeatures.add(f.serialize()));
        return serializedFeatures;
    }

    public void removeAllFeatures() {
        removeFeatures(getFeatures());
    }

    public void deserializeAndLoadFeatures(ArrayList<String> serializedFeatures) {
        if (serializedFeatures == null || serializedFeatures.isEmpty())
            return;

        removeAllFeatures();

        int featuresLoaded = 0;
        int featuresFailedToLoad = 0;

        for (String serializedFeature : serializedFeatures) {
            try {
                add(Feature.deserialize(serializedFeature));
                featuresLoaded++;
            } catch (Exception e) {
                featuresFailedToLoad++;
            }
        }

        JOptionPane.showMessageDialog(map, "Features loaded: " + featuresLoaded + "\nfeatures failed to load: " + featuresFailedToLoad);
    }
}
