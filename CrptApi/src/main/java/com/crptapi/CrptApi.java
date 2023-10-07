package com.crptapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class CrptApi {
    private final TimeUnit timeUnit;
    private final int requestLimit;
    private final ReentrantLock lock = new ReentrantLock();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
    }

    public void createDocument(Document document, String signature) {
        try {
            if (lock.tryLock(requestLimit, TimeUnit.MILLISECONDS)) {
                // Преобразование объекта Document в JSON
                String requestBody = objectMapper.writeValueAsString(document);

                HttpClient httpClient = HttpClient.newBuilder().build();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("https://ismp.crpt.ru/api/v3/lk/documents/create"))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .header("Content-Type", "application/json")
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                // Обработка ответа
                if (response.statusCode() == 200) {
                    System.out.println("Запрос успешно выполнен.");
                    // Дополнительная обработка ответа, если необходимо
                } else {
                    System.err.println("Ошибка выполнения запроса. Код состояния: " + response.statusCode());
                    // Дополнительная обработка ошибки, если необходимо
                }
            } else {
                System.err.println("Превышен лимит запросов. Повторите попытку позже.");
            }
        } catch (Exception e) {
            System.err.println("Произошла ошибка при выполнении запроса: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public static class Document {
        @JsonProperty("participantInn")
        private String participantInn;
        private String docId;
        private String docStatus;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String uitCode;

        public Document(String participantInn, String docId, String docStatus) {
            this.participantInn = participantInn;
            this.docId = docId;
            this.docStatus = docStatus;
        }

        public Document() {
        }
    }

    // Для теста
    public static void main(String[] args) {
        CrptApi api = new CrptApi(TimeUnit.MINUTES, 5);
        CrptApi.Document document = new CrptApi.Document("24324", "145454", "OK");
        String signature = "example_signature";

        api.createDocument(document, signature);
    }
}
