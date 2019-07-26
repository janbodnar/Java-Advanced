package com.zetcode;

import java.util.Arrays;

public class ArraySetAll {

    public static void main(String[] args) {

        int[] a = new int[10];

        Arrays.setAll(a, idx -> idx);

        System.out.println(Arrays.toString(a));

        String[] vals = {"1", "2", "3", "4", "5"};

        int[] b = new int[vals.length];

        Arrays.setAll(b, idx -> Integer.parseInt(vals[idx]));

        System.out.println(Arrays.toString(b));
    }
}
