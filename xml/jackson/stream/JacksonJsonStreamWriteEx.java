package com.zetcode;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;

import java.io.File;
import java.io.IOException;

public class JacksonJsonStreamWriteEx {

    public static void main(String[] args) throws IOException {

        var factory = new JsonFactory();

        var fileName = new File("src/main/resources/users.json");

        try (var generator = factory.createGenerator(fileName, JsonEncoding.UTF8)) {

            generator.useDefaultPrettyPrinter();

            generator.writeStartArray();

            generator.writeStartObject();
            generator.writeNumberField("id", 1);
            generator.writeStringField("firstname", "John");
            generator.writeStringField("lastname", "Doe");
            generator.writeStringField("occupation", "gardenter");
            generator.writeEndObject();

            generator.writeStartObject();
            generator.writeNumberField("id", 2);
            generator.writeStringField("firstname", "Billy");
            generator.writeStringField("lastname", "Smith");
            generator.writeStringField("occupation", "teacher");
            generator.writeEndObject();

            generator.writeEndArray();
        }
    }
}
