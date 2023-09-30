import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.math.BigDecimal;

class CurrencyConverter {

    public static String getCurrencyRate(String baseCurrency) {
        String apiUrl = "https://api.exchangerate-api.com/v4/latest/" + baseCurrency;

        try {
            URL url = new URL(apiUrl);
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

                // Обработка ответа JSON
                String responseData = response.toString();
                // Парсинг JSON и извлечение нужных данных
                //System.out.println(responseData);

                return CurrencyParser.parser(responseData,baseCurrency);
            } else {
                System.out.println("Request failed with response code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Error"; // В случае ошибки возвращаем 0
    }

    public static class CurrencyParser {

        public static String parser(String response, String request)
        {
            JSONObject jsonObject = new JSONObject(response);
            BigDecimal rubRate = jsonObject.getJSONObject("rates").getBigDecimal("RUB");
            return rubRate.toString();
        }
    }
    public static class FileStringExistenceChecker {
        public static boolean checkStringExistence(String filePath, String searchString) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(searchString)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}