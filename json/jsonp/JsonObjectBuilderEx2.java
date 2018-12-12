package com.zetcode;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import java.io.StringWriter;
import java.util.HashMap;

public class JsonObjectBuilderEx2 {

    public static void main(String[] args) {

        var jsonObject = Json.createObjectBuilder()
                .add("firstName", "John")
                .add("lastName", "Doe")
                .add("occupation", "gardener")
                .add("address",
                        Json.createObjectBuilder()
                                .add("street", "Baker Street 20")
                                .add("city", "London"))
                .add("phoneNumbers", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("type", "fixed")
                                .add("number", "491 423-2344"))
                        .add(Json.createObjectBuilder()
                                .add("type", "mobile")
                                .add("number", "541 213-3435")))
                .build();

        var config = new HashMap<String, Boolean>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);

        var jwf = Json.createWriterFactory(config);
        var sw = new StringWriter();

        try (var jsonWriter = jwf.createWriter(sw)) {

            jsonWriter.writeObject(jsonObject);
            System.out.println(sw.toString());
        }

    }
}
