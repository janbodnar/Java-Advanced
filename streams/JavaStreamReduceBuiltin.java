package com.zetcode;

import java.util.Arrays;

public class JavaStreamReduceBuiltin {

    public static void main(String[] args) {

        int vals[] = {2, 4, 6, 8, 10, 12, 14, 16};

        int sum = Arrays.stream(vals).sum();
        System.out.printf("The sum of values: %d%n", sum);

        long n = Arrays.stream(vals).count();
        System.out.printf("The number of values: %d%n", n);
    }
}
