package com.zetcode;

import com.google.gson.Gson;

class User {

    private final String firstName;
    private final String lastName;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    
    @Override
    public String toString() {
        return new StringBuilder().append("User{").append("First name: ")
                .append(firstName).append(", Last name: ")
                .append(lastName).append("}").toString();
    }
}

public class GsonFromJson {

    public static void main(String[] args) {

        String json_string = "{\"firstName\":\"Tom\", \"lastName\": \"Broody\"}";

        Gson gson = new Gson();

        User user = gson.fromJson(json_string, User.class);

        System.out.println(user);
    }
}
