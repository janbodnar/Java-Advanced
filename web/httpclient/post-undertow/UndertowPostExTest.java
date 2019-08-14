package com.zetcode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UndertowPostExTest {

    @DisplayName("issue POST request")
    @Test
    void testPostRequest() throws IOException, InterruptedException {

        var PAYLOAD = "some data";

        byte[] data = PAYLOAD.getBytes();

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/data"))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofByteArray(data))
                .build();

        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(PAYLOAD, response.body().toString());
    }
}
