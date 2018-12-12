package com.zetcode;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.HashMap;

public class JsonPrettyPrintEx {

    public static void main(String[] args) {

        var born = LocalDate.of(1992, 3, 2).toString();

        var json = Json.createObjectBuilder()
                .add("name", "John Doe")
                .add("occupation", "gardener")
                .add("born", born).build();

        var config = new HashMap<String, Boolean>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);

        var jwf = Json.createWriterFactory(config);
        var sw = new StringWriter();

        try (var jsonWriter = jwf.createWriter(sw)) {

            jsonWriter.writeObject(json);
            System.out.println(sw.toString());
        }
    }
}
