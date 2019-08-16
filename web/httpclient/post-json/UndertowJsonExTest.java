package com.zetcode;

import com.zetcode.bean.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class UndertowJsonExTest {

    @DisplayName("send POST request with JSON data")
    @Test
    void testPostRequest() throws IOException, InterruptedException {

        final String DATA_RES = "{\"id\":1,\"name\":\"do the dishes\"}";

        var task = new Task(1L, "do the dishes");

        Jsonb jsonb = JsonbBuilder.create();
        var client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/data"))
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonb.toJson(task)))
                .build();

        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(DATA_RES, response.body());
    }
}
