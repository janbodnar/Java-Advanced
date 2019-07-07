package com.zetcode;

public class ConstructorReferenceEx {

    public static void main(String[] args) {

        CatFactory catFactory = Cat::new;
        var cat = catFactory.getCat("Ollie", 2);

        System.out.println(cat);
    }
}


