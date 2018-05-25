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

    public Feature(Position position, FeatureCategory category, String name) {
        this.position = position;
        this.name = name;
        this.category = category != null ? category : FeatureCategory.NONE;
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
    }

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

    public static Feature deserialize(String serializedFeature) throws Exception {
        final String[] properties = serializedFeature.split(",");

        if (properties.length < 5)
            throw new Exception("Too few fields to be considered fit for deserialization");

        String name;
        FeatureCategory category;
        Position position;

        name = properties[4];
        switch (properties[1]) {
            case "Bus":
                category = FeatureCategory.BUS;
                break;
            case "Train":
                category = FeatureCategory.TRAIN;
                break;
            case "Underground":
                category = FeatureCategory.UNDERGROUND;
                break;
            case "None":
                category = FeatureCategory.NONE;
                break;
            default:
                throw new Exception("Category does not exist");
        }

        try {
            int x = Integer.parseInt(properties[2]);
            int y = Integer.parseInt(properties[3]);
            position = new Position(x, y);
        } catch (Exception e) {
            throw new Exception("Unable to parse coordinates", e);
        }

        return (properties[0].equals("Described") && properties.length > 5)
                ? new DescribedFeature(position, category, name, properties[5])
                : new NamedFeature(position, category, name);
    }

    public abstract String serialize();
}
