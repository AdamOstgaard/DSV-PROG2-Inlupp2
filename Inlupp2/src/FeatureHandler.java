import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;

public class FeatureHandler implements IMapIsReadyListener, MouseListener, SelectedStateListener {
    private Map map;
    private Dimension clickBoundries;
    private ArrayList<FeatureCategory> categories;
    private FeatureCollection featureCollection;

    public FeatureHandler() {
        featureCollection = new FeatureCollection();
        categories = new ArrayList<>();
    }

    public void attach(Map map) {
        this.map = map;
        map.addMapIsReadyListener(this);
        map.addMouseListener(this);
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

        Feature feature = new NamedFeature(new Position(e.getX(), e.getY()), new FeatureCategory("buss", Color.BLUE), "Buss");
        feature.addSelectedStateEventListener(this);
        featureCollection.add(feature);
        map.add(feature.getMarker());
        map.updateUI();
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

    @Override
    public void selectedStateChanged(Feature sender, boolean newState) {
        if (newState)
            featureCollection.addSelectedFeature(sender);
        else
            featureCollection.removeSelectedFeature(sender);
    }
}
