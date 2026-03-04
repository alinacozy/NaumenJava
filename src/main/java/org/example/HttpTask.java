package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Задание 4, вариант 2. Вывести только значение идентификационной строки приложения
 * с которого выполняется запрос (запрос выполняется по адресу
 * “https://httpbin.org/user-agent”).
 */
public class HttpTask {
    public static void main(String[] args){
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://httpbin.org/user-agent"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Проверяем статус ответа
            if (response.statusCode() != 200) {
                System.err.println("Ошибка HTTP: " + response.statusCode());
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            // Проверяем наличие поля user-agent
            JsonNode userAgentNode = root.get("user-agent");
            if (userAgentNode == null) {
                System.err.println("Поле user-agent не найдено в ответе");
                return;
            }
            String userAgent = userAgentNode.asText();
            System.out.println(userAgent);

        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
            System.err.println("Тип ошибки: " + e.getClass().getSimpleName());
            System.err.println("Детали: " + e);
        } catch (InterruptedException e) {
            System.err.println("Запрос был прерван: " + e.getMessage());
            // Восстанавливаем статус прерывания
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Неожиданная ошибка: " + e.getMessage());
            System.err.println("Тип ошибки: " + e.getClass().getSimpleName());
            System.err.println("Детали: " + e);
        }
    }
}
