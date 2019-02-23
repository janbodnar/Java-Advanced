package com.zetcode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class User {

    private String firstName;
    private String lastName;

    public User() {};
    
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("User{").append("First name: ")
                .append(firstName).append(", Last name: ")
                .append(lastName).append("}").toString();
    }
}

public class GsonSerializeNulls {

    public static void main(String[] args) {

        GsonBuilder builder = new GsonBuilder();

        builder.serializeNulls();

        Gson gson = builder.create();

        User user = new User();
        user.setFirstName("Norman");

        String json = gson.toJson(user);
        System.out.println(json);
    }
}
