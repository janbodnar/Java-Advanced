package com.zetcode;

import java.util.Arrays;

public class ArrayToString {

    public static void main(String[] args) {

        int[][] a = {
                {1, 1, 2, 1, 1},
                {0, 0, 3, 0, 0}
        };

        System.out.println(Arrays.toString(a));
        System.out.println(Arrays.deepToString(a));
    }
}
