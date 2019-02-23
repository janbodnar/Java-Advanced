package com.zetcode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

enum MaritalStatus {
    
    SINGLE,
    MARRIED,
    DIVORCED,
    UNKNOWN
}

class Person {
    
    @Expose
    private String firstName;
    
    @Expose
    private String lastName;

    private MaritalStatus maritalStatus;

    public Person(String firstName, String lastName, 
            MaritalStatus maritalStatus) {
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.maritalStatus = maritalStatus;
    }
    
    public Person() {}
}

public class GsonExcludeFileds {
    
    public static void main(String[] args) {
        
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
        
        Person p = new Person("Jack", "Sparrow", MaritalStatus.UNKNOWN);
        
        gson.toJson(p, System.out);        
    }
}
