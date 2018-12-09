package com.zetcode;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonToken;

import java.io.File;
import java.io.IOException;

public class JacksonJsonReadEx {

    public static void main(String[] args) throws IOException {

        var factory = new JsonFactory();
        var fileName = new File("src/main/resources/users.json");
        var parser = factory.createParser(fileName);

        while (parser.nextToken() != JsonToken.END_ARRAY) {

            while (parser.nextToken() != JsonToken.END_OBJECT) {

                var fieldname = parser.getCurrentName();

                if ("id".equals(fieldname)) {

                    parser.nextToken();
                    System.out.println(parser.getIntValue());
                }

                if ("firstname".equals(fieldname)) {

                    parser.nextToken();
                    System.out.println(parser.getText());
                }

                if ("lastname".equals(fieldname)) {

                    parser.nextToken();
                    System.out.println(parser.getText());
                }

                if ("occupation".equals(fieldname)) {

                    parser.nextToken();
                    System.out.println(parser.getText());
                }
            }
        }
    }
}
