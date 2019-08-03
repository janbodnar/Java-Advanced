package com.zetcode;

import java.util.Arrays;
import java.util.List;

public class ListToArray {

    public static void main(String[] args) {

        List<String> planets = Arrays.asList("Mercury", "Venus", "Earth",
                "Mars", "Jupiter", "Saturn", "Uranus", "Neptune");

        System.out.println(planets);

        String[] planets2 = planets.toArray(new String[0]);

        System.out.println(Arrays.toString(planets2));
    }
}
