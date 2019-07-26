package com.zetcode;

import java.util.Arrays;

public class InitArray {

    public static void main(String[] args) {

        int[] a = new int[5];

        a[0] = 1;
        a[1] = 2;
        a[2] = 3;
        a[3] = 4;
        a[4] = 5;

        System.out.println(Arrays.toString(a));
    }
}
