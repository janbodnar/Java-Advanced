package com.zetcode;

import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

public class JsonParserSimpleEx {

    public static void main(String[] args) throws FileNotFoundException {

        var is = new FileInputStream("src/main/resources/users.json");

        var factory = Json.createParserFactory(null);
        var parser = factory.createParser(is, StandardCharsets.UTF_8);

        if (!parser.hasNext() && parser.next() != JsonParser.Event.START_ARRAY) {

            return;
        }

        while (parser.hasNext()) {

            var event = parser.next();

            // starting object
            if (event == JsonParser.Event.START_OBJECT) {

                // looping over object attributes
                while (parser.hasNext()) {

                    event = parser.next();

                    if (event == JsonParser.Event.KEY_NAME) {

                        var key = parser.getString();

                        switch (key) {

                            case "name":
                                parser.next();

                                System.out.printf("Name: %s%n", parser.getString());
                                break;

                            case "occupation":
                                parser.next();

                                System.out.printf("Occupation: %s%n", parser.getString());
                                break;

                            case "born":
                                parser.next();

                                System.out.printf("Born: %s%n%n", parser.getString());
                                break;
                        }
                    }
                }
            }
        }
    }
}
