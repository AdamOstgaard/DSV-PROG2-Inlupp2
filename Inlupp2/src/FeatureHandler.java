import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class FeatureHandler implements IMapIsReadyListener, MouseListener {
    private Map map;
    private Dimension clickBoundries;
    private ArrayList features;

    public FeatureHandler() {
        features = new ArrayList<Feature>();
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
        //JOptionPane.showMessageDialog(map, map.getMapDimension().width);
    }

    private void addFeature(MouseEvent e) {
        if (!(e.getX() < clickBoundries.getWidth() && e.getY() < clickBoundries.getHeight()))
            return;

        Feature feature = new Feature(e.getX(), e.getY(), null);
        features.add(feature);
        //feature.getTriangle().setAlignmentX(feature.getPoint().x);
        //feature.getTriangle().setAlignmentY(feature.getPoint().y);
        map.addFeature(feature);
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
}
