package com.zetcode;

import java.util.Arrays;

public class InitArray4 {

    public static void main(String[] args) {

        int[] a = new int[10];

        Arrays.fill(a, 1);

        System.out.println(Arrays.toString(a));

        int[] b = new int[10];

        Arrays.setAll(b, idx -> idx);

        System.out.println(Arrays.toString(b));
    }
}
