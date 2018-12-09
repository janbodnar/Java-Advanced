package com.zetcode;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JacksonReadJsonEx {

    public static void main(String[] args) throws IOException {

        var mapper = new ObjectMapper();
        var fileName = new File("src/main/resources/users.json");

        List<User> users =  mapper.readValue(fileName, new TypeReference<List<User>>(){});

        users.forEach(System.out::println);
    }
}
