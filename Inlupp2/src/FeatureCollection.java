import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class FeatureCollection {

    private final HashMap<Position, Feature> featuresPosition;
    private final HashMap<String, HashSet<Feature>> featuresName;
    private final HashMap<FeatureCategory, HashSet<Feature>> featuresCategory;
    private final HashSet<Feature> selectedFeatures;
    private final HashSet<Feature> hiddenFeatures;

    public FeatureCollection() {
        featuresPosition = new HashMap<>();
        featuresName = new HashMap<>();
        featuresCategory = new HashMap<>();
        selectedFeatures = new HashSet<>();
        hiddenFeatures = new HashSet<>();
    }

    public HashSet<Feature> getFeatures() {
        return new HashSet<>(featuresPosition.values());
    }

    public HashSet<Feature> getFeatures(FeatureCategory category) {
        return featuresCategory.get(category);
    }

    public HashSet<Feature> getFeatures(String name) {
        return featuresName.get(name);
    }

    public Feature getFeature(Position position) {
        return featuresPosition.get(position);
    }

    public void add(Feature feature) {
        if (feature == null) return;

        featuresPosition.put(feature.getPosition(), feature);

        if (!featuresName.containsKey(feature.getName()))
            featuresName.put(feature.getName(), new HashSet<>());

        featuresName.get(feature.getName()).add(feature);

        if (!featuresCategory.containsKey(feature.getCategory()))
            featuresCategory.put(feature.getCategory(), new HashSet<>());

        featuresCategory.get(feature.getCategory()).add(feature);
    }

    public void remove(Feature feature) {
        if (feature == null) throw new NullPointerException();

        featuresPosition.remove(feature.getPosition());
        featuresCategory.get(feature.getCategory()).remove(feature);
        featuresName.get(feature.getName()).remove(feature);
        selectedFeatures.remove(feature);
    }

    public void addSelectedFeature(Feature feature) {
        selectedFeatures.add(feature);
    }

    public void addSelectedFeature(Collection<Feature> features) {
        selectedFeatures.addAll(features);
    }

    public void removeSelectedFeature(Feature feature) {
        selectedFeatures.remove(feature);
    }

    public HashSet<Feature> getSelectedFeatures() {
        return selectedFeatures;
    }

    public void addHiddenFeature(Feature feature) {
        hiddenFeatures.add(feature);
    }

    public void addHiddenFeature(Collection<Feature> features) {
        hiddenFeatures.addAll(features);
    }

    public void removeHiddenFeature(Feature feature) {
        hiddenFeatures.remove(feature);
    }

    public HashSet<Feature> getHiddenFeatures() {
        return hiddenFeatures;
    }

}
