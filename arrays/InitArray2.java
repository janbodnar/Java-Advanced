package com.zetcode;

import java.util.Arrays;

public class InitArray2 {

    public static void main(String[] args) {

        // we can use var keyword
        var a = new int[] { 2, 4, 5, 6, 7, 3, 2 };

        System.out.println(Arrays.toString(a));
    }
}
