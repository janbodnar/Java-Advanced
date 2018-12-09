package com.zetcode;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

class User {

    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


public class JacksonObject2JsonEx {

    public static void main(String[] args) throws IOException {

        var user = new User("Peter", "peter232@gmail.com");

        var mapper = new ObjectMapper();

        var output = mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(user);

        System.out.println(output);

        var users = List.of(new User("Lucia", "luccia@gmail.com"),
                new User("Roman", "roman12@hotmail.com"),
                new User("jan", "jan234@gmail.com"));

        var output2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(users);
        System.out.println(output2);
    }
}
