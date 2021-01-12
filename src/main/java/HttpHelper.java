import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class HttpHelper {
    private static final String BASE_URL = "http://localhost:8080";

    static String getToken(String name, String password) throws IOException {

        String token = "";
        String parameters = "{\"name\":\"" + name + "\",\"password\":\"" + password + "\"}";

        URL tokenURL = new URL(BASE_URL + "/tokens");
        HttpURLConnection connection = (HttpURLConnection) tokenURL.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        OutputStreamWriter writer = new OutputStreamWriter(
                connection.getOutputStream());
        writer.write(parameters);
        writer.flush();

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            StringBuilder response = new StringBuilder();
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
                response.append("\n");
            }
            JSONObject jsonObject = new JSONObject(response.toString());
            token = jsonObject.get("token")
                              .toString();
        } else {
            System.out.println("Login failed: " + responseCode);
        }

        return token;
    }

    static List<Product> getOrders(String token, String order) throws IOException {
        List<Product> orders = null;

        URL orderURL = new URL(BASE_URL + "/order?identifier=" + order);
        HttpURLConnection connection = (HttpURLConnection) orderURL.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestProperty("Authorization", token);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            StringBuilder response = new StringBuilder();
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
                response.append("\n");
            }
            JSONObject jsonObject = new JSONObject(response.toString());
            String jsonList = jsonObject.get("products")
                                        .toString();
            ObjectMapper objectMapper = new ObjectMapper();
            orders = Arrays.asList(objectMapper.readValue(jsonList, Product[].class));
        }

        return orders;
    }
}
