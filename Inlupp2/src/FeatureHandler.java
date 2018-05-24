import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;

public class FeatureHandler implements IMapIsReadyListener, MouseListener, SelectedStateListener {
    private Map map;
    private Dimension clickBoundries;
    private FeatureCollection featureCollection;
    private FeatureCategory newFeatureCategory;
    private boolean newFeatureIsDescripted;

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
        if (!(e.getX() < clickBoundries.getWidth() && e.getY() < clickBoundries.getHeight()))
            return;

        String name = "";
        if (!newFeatureIsDescripted) {
            name = JOptionPane.showInputDialog(map, "Enter name", "New named feature");
        } else {

        }
        Feature feature = new NamedFeature(new Position(e.getX(), e.getY()), newFeatureCategory, name);
        feature.addSelectedStateEventListener(this);
        featureCollection.add(feature);
        map.add(feature.getMarker());
        map.updateUI();
        map.removeMouseListener(this);
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
        newFeatureCategory = category;
        newFeatureIsDescripted = isDescripted;
    }

}
