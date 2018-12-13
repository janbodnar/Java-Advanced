package com.zetcode;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class JsonArrayBuilderEx {

    public static void main(String[] args) {

        var ab = Json.createArrayBuilder();

        var users = createUsers();

        users.forEach(user -> {

            var ob = Json.createObjectBuilder();
            ob.add("name", user.getName());
            ob.add("occupation", user.getOccupation());
            ob.add("born", user.getBorn().toString());

            ab.add(ob);
        });

        var jsonArray = ab.build();
        
        var config = new HashMap<String, Boolean>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);

        var jwf = Json.createWriterFactory(config);
        var sw = new StringWriter();

        try (var jsonWriter = jwf.createWriter(sw)) {

            jsonWriter.writeArray(jsonArray);

            System.out.println(sw);
        }
    }

    public static List<User> createUsers() {

        var born1 = LocalDate.of(1992, 3, 2);
        var u1 = new User("John Doe", "gardener", born1);

        var born2 = LocalDate.of(1967, 11, 22);
        var u2 = new User("Brian Flemming", "teacher", born2);

        var born3 = LocalDate.of(1995, 4, 7);
        var u3 = new User("Lucy Black", "accountant", born3);

        var born4 = LocalDate.of(1972, 8, 30);
        var u4 = new User("John Doe", "gardener", born4);

        return List.of(u1, u2, u3, u4);
    }
}
