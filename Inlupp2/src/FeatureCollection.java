import java.util.HashMap;
import java.util.HashSet;

public class FeatureCollection {

    private HashMap<Position, Feature> featuresPosition;
    private HashMap<String, HashSet<Feature>> featuresName;
    private HashMap<FeatureCategory, HashSet<Feature>> featuresCategory;

    public FeatureCollection() {
        featuresPosition = new HashMap<>();
        featuresName = new HashMap<>();
        featuresCategory = new HashMap<>();
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

    public void addFeature(Feature feature) {
        if (feature == null) return;

        featuresPosition.put(feature.getPosition(), feature);

        if (!featuresName.containsKey(feature.getName()))
            featuresName.put(feature.getName(), new HashSet<>());

        featuresName.get(feature.getName()).add(feature);

        if (!featuresCategory.containsKey(feature.getType()))
            featuresCategory.put(feature.getType(), new HashSet<>());

        featuresCategory.get(feature.getType()).add(feature);
    }

    public void removeFeature(Feature feature) {
        if (feature == null) return;

        featuresPosition.remove(feature.getPosition());
        featuresCategory.get(feature.getType()).remove(feature);
        featuresName.get(feature.getName()).remove(feature);
    }
}
