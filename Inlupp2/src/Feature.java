import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public abstract class Feature implements MouseListener {

    private final Position position;
    private final String name;
    private final FeatureCategory category;
    private final Marker marker;
    private final ArrayList<SelectedStateListener> selectedStateEventListeners;
    private FeatureState featureState;

    Feature(Position position, FeatureCategory category, String name) {
        this.position = position;
        this.name = name;
        this.category = category != null ? category : FeatureCategory.NONE;
        this.marker = new Marker(this);
        this.selectedStateEventListeners = new ArrayList<>();
        featureState = FeatureState.UNSELECTED;
        marker.addMouseListener(this);
    }

    static Feature deserialize(String serializedFeature) throws Exception {
        final String[] properties = serializedFeature.split(",");

        if (properties.length < 5)
            throw new Exception("Too few fields to be considered fit for deserialization");

        String name = properties[4];
        FeatureCategory category = parseFeatureCategory(properties[1]);
        Position position = parsePosition(properties[2], properties[3]);

        return (properties[0].equals("Described") && properties.length > 5)
                ? new DescribedFeature(position, category, name, properties[5])
                : new NamedFeature(position, category, name);
    }

    private static FeatureCategory parseFeatureCategory(String categoryString) throws Exception {
        FeatureCategory category;

        switch (categoryString) {
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
        return category;
    }

    private static Position parsePosition(String xStr, String yStr) throws Exception {
        Position position;
        try {
            int x = Integer.parseInt(xStr);
            int y = Integer.parseInt(yStr);
            position = new Position(x, y);
        } catch (Exception e) {
            throw new Exception("Unable to parse coordinates", e);
        }
        return position;
    }

    Position getPosition() {
        return position;
    }

    String getName() {
        return name;
    }

    FeatureCategory getCategory() {
        return category;
    }

    Marker getMarker() {
        return marker;
    }

    private FeatureState getState() {
        return featureState;
    }

    void setState(FeatureState featureState) {
        if (featureState == getState()) return;

        this.featureState = featureState;

        marker.setSelected(featureState == FeatureState.SELECTED);
        marker.setVisible(featureState != FeatureState.HIDDEN);

        selectedStateEventListeners.forEach(p -> p.selectedStateChanged(this, featureState));
    }

    private void toggleSelect() {
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

    void addSelectedStateEventListener(SelectedStateListener listener) {
        selectedStateEventListeners.add(listener);
    }

    public abstract String serialize();
}
