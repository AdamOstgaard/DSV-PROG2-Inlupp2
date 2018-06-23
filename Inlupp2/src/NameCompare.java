import java.util.Comparator;

class NameCompare implements Comparator<Feature> {
    public int compare(Feature obj1, Feature obj2) {
        if (obj1 == null) {
            return -1;
        }
        if (obj2 == null) {
            return 1;
        }
        if (obj1.getName().equals(obj2.getName())) {
            return 0;
        }
        return obj1.getName().compareTo(obj2.getName());
    }
}

