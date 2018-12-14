package com.zetcode;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class JsonGeneratorEx {

    public static void main(String[] args) throws IOException {

        var myPath = Paths.get("src/main/resources/users.json");

        var config = new HashMap<String, Boolean>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);

        var factory = Json.createGeneratorFactory(config);
        var generator = factory.createGenerator(Files.newBufferedWriter(myPath,
                StandardCharsets.UTF_8));

        generator.writeStartArray();

        var users = generateUsers();

        users.forEach(user -> {

            generator.writeStartObject();

            generator.write("name", user.getName());
            generator.write("occupation", user.getOccupation());
            generator.write("born", user.getBorn().toString());

            generator.writeEnd();
        });

        generator.writeEnd();

        generator.flush();
    }

    public static List<User> generateUsers() {

        var born1 = LocalDate.of(1992, 3, 2);
        var u1 = new User("John Doe", "gardener", born1);

        var born2 = LocalDate.of(1967, 11, 22);
        var u2 = new User("Brian Flemming", "teacher", born2);

        var born3 = LocalDate.of(1995, 4, 7);
        var u3 = new User("Lucy Black", "accountant", born3);

        var born4 = LocalDate.of(1977, 10, 31);
        var u4 = new User("William Bean", "pilot", born4);

        return List.of(u1, u2, u3, u4);
    }
}
