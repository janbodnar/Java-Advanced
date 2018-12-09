package com.zetcode;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JacksonJsonWriteEx {

    public static void main(String[] args) throws IOException {

        var mapper = new ObjectMapper();
        var fileName = new File("src/main/resources/users.json");

        var users = List.of(new User(1L, "Robert", "Brown", "programmer"),
                new User(2L, "Pamel", "Kyle", "writer"),
                new User(3L, "Lucy", "Smith", "teacher"),
                new User(4L, "John", "Doe", "gardener"));

        mapper.writerWithDefaultPrettyPrinter().writeValue(fileName, users);

        System.out.println("Data written to JSON file");
    }
}
