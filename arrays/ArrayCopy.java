package com.zetcode;

import java.util.Arrays;

public class ArrayCopy {

    public static void main(String[] args) {

        int[] vals = {1, 2, 3, 4, 5, 6, 7, 8};

        int[] vals2 = Arrays.copyOf(vals, 8);
        int[] vals3 = Arrays.copyOfRange(vals, 2, 7);

        System.out.println(Arrays.toString(vals));
        System.out.println(Arrays.toString(vals2));
        System.out.println(Arrays.toString(vals3));
    }
}
