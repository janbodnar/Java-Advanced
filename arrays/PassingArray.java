package com.zetcode;

import java.util.Arrays;

public class PassingArray {

    public static void main(String[] args) {

        int[] a = { 3, 4, 5, 6, 7 };
        int[] r = reverseArray(a);

        System.out.println(Arrays.toString(a));
        System.out.println(Arrays.toString(r));
    }

    private static int[] reverseArray(int[] b) {

        int[] c = new int[b.length];

        for (int i=b.length-1, j=0; i>=0; i--, j++) {

            c[j] = b[i];
        }

        return c;
    }
}
