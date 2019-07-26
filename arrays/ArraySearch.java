package com.zetcode;

import java.util.Arrays;

public class ArraySearch {

    // Arrays.binarySearch requires a sorted array
    public static void main(String[] args) {

        String[] planets = {"Mercury", "Venus", "Mars", "Earth", "Jupiter",
                "Saturn", "Uranus", "Neptune", "Pluto"};

        Arrays.sort(planets);

        var planet = "Mars";

        int r = Arrays.binarySearch(planets, planet);

        String msg;

        if (r >= 0) {

            msg = String.format("%s was found at position %d of the "
                    + "sorted array", planet, r);
        } else {

            msg = planet + " was not found";
        }

        System.out.println(msg);
    }
}
