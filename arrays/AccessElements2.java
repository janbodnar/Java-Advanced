package com.zetcode;

import java.util.Arrays;

public class AccessElements2 {

    public static void main(String[] args) {

        int[] vals = { 1, 2, 3 };

        vals[0] *= 2;
        vals[1] *= 2;
        vals[2] *= 2;

        System.out.println(Arrays.toString(vals));
    }
}
