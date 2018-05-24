import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public abstract class Feature implements MouseListener {

    private Position position;
    private String name;
    private FeatureCategory category;
    private Marker marker;
    private ArrayList<SelectedStateListener> selectedStateEventListeners;
    private FeatureState featureState;

    public Feature(Position position, FeatureCategory type, String name) {
        this.position = position;
        this.name = name;
        this.category = type;
        this.marker = new Marker(this);
        this.selectedStateEventListeners = new ArrayList<>();
        featureState = FeatureState.UNSELECTED;
        marker.addMouseListener(this);
    }

    public Position getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public FeatureCategory getCategory() {
        return category;
    }

    public Marker getMarker() {
        return marker;
    }

    public FeatureState getState() {
        return featureState;
    }

    public void setState(FeatureState featureState) {
        if (featureState == getState()) return;

        this.featureState = featureState;

        marker.setSelected(featureState == FeatureState.SELECTED);
        marker.setVisible(featureState != FeatureState.HIDDEN);

        selectedStateEventListeners.forEach(p -> p.selectedStateChanged(this, featureState));
    }

    public void toggleSelect() {
        if (getState() == FeatureState.SELECTED)
            setState(FeatureState.UNSELECTED);
        else if (getState() == FeatureState.UNSELECTED)
            setState(FeatureState.SELECTED);
    }

    private void onLeftClick(MouseEvent e) {
        toggleSelect();
        leftMouseButtonClicked(e);
    }

    protected abstract void leftMouseButtonClicked(MouseEvent e);

    protected abstract void rightMouseButtonClicked(MouseEvent e);

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)
            onLeftClick(e);
        else if (e.getButton() == MouseEvent.BUTTON3)
            rightMouseButtonClicked(e);
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

    public void addSelectedStateEventListener(SelectedStateListener listener) {
        selectedStateEventListeners.add(listener);
    }
}
