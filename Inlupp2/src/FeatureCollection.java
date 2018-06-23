import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

class FeatureCollection {

    private final HashMap<Position, Feature> featuresPosition;
    private final HashMap<String, HashSet<Feature>> featuresName;
    private final HashMap<FeatureCategory, HashSet<Feature>> featuresCategory;
    private final HashSet<Feature> selectedFeatures;
    private final HashSet<Feature> hiddenFeatures;

    FeatureCollection() {
        featuresPosition = new HashMap<>();
        featuresName = new HashMap<>();
        featuresCategory = new HashMap<>();
        selectedFeatures = new HashSet<>();
        hiddenFeatures = new HashSet<>();
    }

    HashSet<Feature> getFeatures() {
        return new HashSet<>(featuresPosition.values());
    }

    HashSet<Feature> getFeatures(FeatureCategory category) {
        return featuresCategory.get(category);
    }

    HashSet<Feature> getFeatures(String name) {
        return featuresName.get(name);
    }

    Feature getFeature(Position position) {
        return featuresPosition.get(position);
    }

    void add(Feature feature) {
        if (feature == null) return;

        featuresPosition.put(feature.getPosition(), feature);

        if (!featuresName.containsKey(feature.getName()))
            featuresName.put(feature.getName(), new HashSet<>());

        featuresName.get(feature.getName()).add(feature);

        if (!featuresCategory.containsKey(feature.getCategory()))
            featuresCategory.put(feature.getCategory(), new HashSet<>());

        featuresCategory.get(feature.getCategory()).add(feature);
    }

    void remove(Feature feature) {
        if (feature == null) throw new NullPointerException();

        featuresPosition.remove(feature.getPosition());
        featuresCategory.get(feature.getCategory()).remove(feature);
        featuresName.get(feature.getName()).remove(feature);
        selectedFeatures.remove(feature);
    }

    void addSelectedFeature(Feature feature) {
        selectedFeatures.add(feature);
    }

    public void addSelectedFeature(Collection<Feature> features) {
        selectedFeatures.addAll(features);
    }

    void removeSelectedFeature(Feature feature) {
        selectedFeatures.remove(feature);
    }

    HashSet<Feature> getSelectedFeatures() {
        return selectedFeatures;
    }

    void addHiddenFeature(Feature feature) {
        hiddenFeatures.add(feature);
    }

    public void addHiddenFeature(Collection<Feature> features) {
        hiddenFeatures.addAll(features);
    }

    void removeHiddenFeature(Feature feature) {
        hiddenFeatures.remove(feature);
    }

    HashSet<Feature> getHiddenFeatures() {
        return hiddenFeatures;
    }

}
