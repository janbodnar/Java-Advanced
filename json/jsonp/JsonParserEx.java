package com.zetcode;

import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.IOException;
import java.net.URL;

public class JsonParserEx {

    public static void main(String[] args) throws IOException {

        var url = new URL("https://jsonplaceholder.typicode.com/posts");


        try (var in = url.openStream(); var parser = Json.createParser(in)) {

            // starting array
            parser.next();

            while (parser.hasNext()) {

                // starting object
                var event1 = parser.next();

                if (event1 == JsonParser.Event.START_OBJECT) {

                    while (parser.hasNext()) {

                        var event = parser.next();

                        if (event == JsonParser.Event.KEY_NAME) {

                            switch (parser.getString()) {

                                case "userId":
                                    parser.next();

                                    System.out.printf("User Id: %d%n", parser.getInt());
                                    break;

                                case "id":
                                    parser.next();

                                    System.out.printf("Post Id: %d%n", parser.getInt());
                                    break;

                                case "title":
                                    parser.next();

                                    System.out.printf("Post title: %s%n", parser.getString());
                                    break;

                                case "body":
                                    parser.next();

                                    if ("bar".equals(parser.getString())) {

                                        System.out.println("baaaaaar");

                                    }

                                    System.out.printf("Post body: %s%n%n", parser.getString());
                                    break;

                                default:
                                    parser.next();

                                    System.out.printf("Default: %s%n", parser.getString());
                                    break;

                            }
                        }
                    }
                }
            }
        }
    }
}
