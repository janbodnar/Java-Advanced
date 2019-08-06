package com.zetcode;

import java.util.Arrays;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

public class ForEachConsSpec {

    public static void main(String[] args) {

        int[] inums = { 3, 5, 6, 7, 5 };
        IntConsumer icons = i -> System.out.print(i + " ");
        Arrays.stream(inums).forEach(icons);

        System.out.println();

        long[] lnums = { 13L, 3L, 6L, 1L, 8L };
        LongConsumer lcons = l -> System.out.print(l + " ");
        Arrays.stream(lnums).forEach(lcons);

        System.out.println();

        double[] dnums = { 3.4d, 9d, 6.8d, 10.3d, 2.3d };
        DoubleConsumer dcons = d -> System.out.print(d + " ");
        Arrays.stream(dnums).forEach(dcons);

        System.out.println();
    }
}
