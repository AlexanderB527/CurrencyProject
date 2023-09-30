import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class DollarRateClient {
    private static final String SERVER_URL = "http://localhost:8080?currency=";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String currency = "EUR";
        while (true) {
            System.out.println("Введите код валюты (например, USD, EUR, GBP):");
            currency = scanner.nextLine();
            if (currency.isEmpty()) {
                break;
            }
            try {
                URL url = new URL(SERVER_URL + currency);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    System.out.println("Курс рубля относительно " + currency + ": " + response.toString());

                } else {
                    System.out.println("Ошибка при выполнении запроса. Код ошибки: " + responseCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}