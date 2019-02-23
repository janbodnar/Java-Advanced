package com.zetcode;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintStream;

class User {
    
    private final String firstName;
    private final String lastName;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

public class GsonBuilderEx {

    public static void main(String[] args) throws IOException {

        try (PrintStream prs = new PrintStream(System.out, true, 
                "UTF8")) {
            
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                    .create();
            
            User user = new User("Peter", "Flemming");
            gson.toJson(user, prs);
        }
    }
}
