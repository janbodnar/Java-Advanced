package com.zetcode;

import javax.json.Json;
import java.time.LocalDate;

public class JsonObjectBuilderEx {

    public static void main(String[] args) {

        var born = LocalDate.of(1992, 3, 2).toString();

        var json = Json.createObjectBuilder()
                .add("name", "John Doe")
                .add("occupation", "gardener")
                .add("born", born).build();

        var result = json.toString();

        System.out.println(result);
    }
}
