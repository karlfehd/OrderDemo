import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;

public class Product {

    @Getter
    @Setter
    private String ean;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String location;

    static void printProducts(List<Product> products) {
        products.sort(Comparator.comparing(Product::getLocation)
                                .thenComparing(Product::getEan));
        products.forEach(product -> System.out.println(product.toString()));
    }

    public String toString() {
        return String.format("ID: %s\nName: %s\nLocation: %s\n", ean, name, location);
    }
}
