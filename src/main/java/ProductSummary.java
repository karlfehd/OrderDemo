import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.TreeSet;

public class ProductSummary {

    public static final int COUNT_WIDTH = 7;
    public static final int ID_WIDTH = 15;
    public static final int NAME_WIDTH = 30;
    public static final int LOC_WIDTH = 24;

    @Getter
    private final String id;

    @Getter
    private final String name;

    @Getter
    private int count = 0;
    private final TreeSet<String> locations = new TreeSet<>();

    public ProductSummary(String id, String name, String location) {
        this.id = id;
        this.name = name;
        if (locations.add(location)) {
            count++;
        }
    }

    public void addLocation(String location) {
        if (locations.add(location)) {
            count++;
        }
    }

    public static boolean containsProduct(final List<ProductSummary> list, final String id) {
        return list.stream()
                   .anyMatch(o -> o.getId()
                                   .equals(id));
    }

    public static String makeHeader() {
        return String.format("|%s|%s|%s|%s|",
                             StringUtils.center("Count", COUNT_WIDTH),
                             StringUtils.center("ID", ID_WIDTH),
                             StringUtils.center("Name", NAME_WIDTH),
                             StringUtils.center("Locations", LOC_WIDTH));
    }

    public String toString() {
        return String.format("|%s|%s|%s|%s|",
                             StringUtils.center(String.valueOf(count), COUNT_WIDTH),
                             StringUtils.center(id, ID_WIDTH),
                             StringUtils.center(name, NAME_WIDTH),
                             StringUtils.center(locations.toString(), LOC_WIDTH));
    }

}
