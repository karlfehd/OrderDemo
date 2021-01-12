import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class OrderClient {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("*** Welcome to the Shop POC ***\n");
        System.out.println("Enter user name:");
        String name = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        try {
            String token = HttpHelper.getToken(name, password);
            if (token.length() > 0) {
                System.out.println("\nLogin success, enter order ID: ");
                String orderID = scanner.nextLine();
                while (!orderID.equalsIgnoreCase("Q")) {
                    List<Product> products = HttpHelper.getOrders(token, orderID);
                    if (products != null) {
                        List<ProductSummary> summaryList = getProductSummaries(products);
                        printSummary(orderID, summaryList);

                    } else {
                        System.out.println("Order " + orderID + " not found.");
                    }
                    System.out.println("\nEnter another order ID (Q to exit): ");
                    orderID = scanner.nextLine();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printSummary(String orderID, List<ProductSummary> summaryList) {
        String line = new String(new char[81]).replace('\0', '-');

        System.out.println("Order " + orderID + " found.");
        System.out.println(line);
        System.out.println(ProductSummary.makeHeader());
        System.out.println(line);
        summaryList.forEach(System.out::println);
        System.out.println(line);
    }

    private static List<ProductSummary> getProductSummaries(List<Product> products) {
        List<ProductSummary> summaryList = new ArrayList<>();
        for (Product product : products) {
            if (ProductSummary.containsProduct(summaryList, product.getEan())) {
                summaryList.stream()
                           .filter(p -> p.getId()
                                         .equals(product.getEan()))
                           .forEach(p -> p.addLocation(product.getLocation()));
            } else {
                summaryList.add(new ProductSummary(product.getEan(), product.getName(),
                                                   product.getLocation()));
            }

        }
        summaryList.sort(Comparator.comparing(ProductSummary::getId));

        return summaryList;
    }

}
