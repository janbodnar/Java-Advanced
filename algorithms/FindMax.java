package com.zetcode;

// brute force algorithm to find maximum value
// in array

public class FindMax {

    public static void main(String[] args) {

        int[] values = {3, 7, 1, 15, 12, 6, 11, 9};

        int max = values[0];

        for (int i = 1; i < values.length; i++) {

            if (values[i] > max) {
                max = values[i];
            }
        }

        System.out.printf("The maximum is %d%n", max);
    }
}
