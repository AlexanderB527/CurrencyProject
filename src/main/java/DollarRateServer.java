import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class DollarRateServer {
    private static final int PORT = 8080;
    private static final String RESPONSE_TEMPLATE = "{\"rate\": %.2f}";

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/", new RateHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("Сервер запущен");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class RateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                // Получение значения, переданного клиентом
                String requestedValue = exchange.getRequestURI().getQuery();
                System.out.println(requestedValue);

                // Обработка значения requestedValue
                String currency = "";
                if (requestedValue != null && requestedValue.contains("currency=")) {
                    int index = requestedValue.indexOf("=") + 1;
                    currency = requestedValue.substring(index);
                }

                System.out.println(currency);

                // Здесь API для получения актуального курса валют относительно рубля
                String filePath = "C:\\Users\\Alexander\\IdeaProjects\\ParserMoney\\src\\main\\java\\Currency_codes"; // путь к файлу

                boolean isStringExists = CurrencyConverter.FileStringExistenceChecker.checkStringExistence(filePath, currency);
                if (isStringExists) {
                    String rate = CurrencyConverter.getCurrencyRate(currency);
                    System.out.println(rate);
                    double parsedRate = Double.parseDouble(rate); // Преобразование строки с курсом в число
                    String response = "{\"rate\": " + String.format("\"%.2f\"", parsedRate) + "}"; // Исправленный шаблон ответа
                    exchange.getResponseHeaders().set("Content-Type", "application/json"); // Установка типа контента в JSON
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write(response.getBytes());
                    outputStream.close();
                } else {
                    String response = "{\"error\": \"Заданной валюты не существует\"}"; // Ответ при отсутствии строки
                    exchange.getResponseHeaders().set("Content-Type", "application/json"); // Установка типа контента в JSON
                    exchange.sendResponseHeaders(404, response.getBytes().length); // Код 404 для отсутствующей строки
                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write(response.getBytes());
                    outputStream.close();
                }
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        }
    }

}