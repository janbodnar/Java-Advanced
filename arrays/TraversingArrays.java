package com.zetcode;

import java.util.Arrays;

public class TraversingArrays {

    public static void main(String[] args) {

        String[] planets = {"Mercury", "Venus", "Mars", "Earth", "Jupiter",
                "Saturn", "Uranus", "Neptune", "Pluto"};

        for (int i = 0; i < planets.length; i++) {

            System.out.println(planets[i]);
        }

        System.out.println("*************************");

        for (String planet : planets) {

            System.out.println(planet);
        }

        System.out.println("*************************");

        Arrays.stream(planets).forEach(System.out::println);
    }
}
