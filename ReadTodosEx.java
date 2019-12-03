package com.zetcode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class Todo {

    private int userId;

    private int id;

    @Expose
    private String title;

    private boolean completed;

    public Todo(int userId, int id, String title, boolean completed) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Todo{");
        sb.append("userId=").append(userId);
        sb.append(", id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", completed=").append(completed);
        sb.append('}');
        return sb.toString();
    }
}

public class ReadTodosEx {

    public static void main(String[] args) throws IOException, InterruptedException {

        String url = "https://jsonplaceholder.typicode.com/todos";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        var jsonString = response.body();

        System.out.println(jsonString);

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();

        Todo[] todos = gson.fromJson(jsonString, Todo[].class);

        gson.toJson(todos, System.out);
    }
}
