import java.util.HashMap;
import java.util.HashSet;

public class FeatureCollection {

    private HashMap<Position, Feature> FeaturesPosition;
    private HashMap<String, HashSet<Feature>> FeaturesName;
    private HashMap<FeatureCategory, HashSet<Feature>> FeaturesType;

    public FeatureCollection() {
        FeaturesPosition = new HashMap<>();
        FeaturesName = new HashMap<>();
        FeaturesType = new HashMap<>();
    }

    public HashSet<Feature> getFeatures() {
        return new HashSet<>(FeaturesPosition.values());
    }

    public HashSet<Feature> getFeatures(FeatureCategory category) {
        return FeaturesType.get(category);
    }

    public HashSet<Feature> getFeatures(String name) {
        return FeaturesName.get(name);
    }

    public Feature getFeature(Position position) {
        return FeaturesPosition.get(position);
    }
}
