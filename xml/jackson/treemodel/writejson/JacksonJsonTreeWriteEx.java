package com.zetcode;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JacksonJsonTreeWriteEx {

    public static void main(String[] args) throws IOException {

        var mapper = new ObjectMapper();
        var factory = mapper.getFactory();

        var fileName = new File("src/main/resources/users.json");

        try (var generator = factory.createGenerator(fileName, JsonEncoding.UTF8)) {

            var treeRootNode = mapper.createObjectNode();
            var arrayNode = treeRootNode.putArray("users");

            var user1 = mapper.createObjectNode();
            user1.put("id", 1);
            user1.put("firstname", "Robert");
            user1.put("lastname", "Brown");
            user1.put("occupation", "programmer");

            arrayNode.add(user1);

            var user2 = mapper.createObjectNode();
            user2.put("id", 2);
            user2.put("firstname", "Pamela");
            user2.put("lastname", "Kyle");
            user2.put("occupation", "writer");

            arrayNode.add(user2);

            var user3 = mapper.createObjectNode();
            user3.put("id", 3);
            user3.put("firstname", "Lucy");
            user3.put("lastname", "Smith");
            user3.put("occupation", "teacher");

            arrayNode.add(user3);

            var user4 = mapper.createObjectNode();
            user4.put("id", 4);
            user4.put("firstname", "John");
            user4.put("lastname", "Doe");
            user4.put("occupation", "gardener");

            arrayNode.add(user4);

            // the remove() method deletes the users field thus
            // creating an unnamed JSON array
            mapper.writerWithDefaultPrettyPrinter().writeValue(generator,
                    treeRootNode.remove("users"));
        }

        System.out.println("JSON data written to file users.json");
    }
}
