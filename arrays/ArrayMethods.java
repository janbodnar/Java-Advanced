package com.zetcode;

import java.util.Arrays;

public class ArrayMethods {

    public static void main(String[] args) {

        int[] a = {5, 2, 4, 3, 1};

        Arrays.sort(a);

        System.out.println(Arrays.toString(a));

        Arrays.fill(a, 8);
        System.out.println(Arrays.toString(a));

        int[] b = Arrays.copyOf(a, 5);

        if (Arrays.equals(a, b)) {

            System.out.println("Arrays a, b are equal");
        } else {

            System.out.println("Arrays a, b are not equal");
        }
    }
}
