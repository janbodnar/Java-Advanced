package com.zetcode;

import java.util.Arrays;

public class InitArray3 {

    public static void main(String[] args) {

        // we cannot use var keyword
        int[] a = { 2, 4, 5, 6, 7, 3, 2 };

        System.out.println(Arrays.toString(a));
    }
}
