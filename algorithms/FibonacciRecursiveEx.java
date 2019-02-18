package com.zetcode;

import java.math.BigInteger;

public class FibonacciRecursiveEx {

    private static final BigInteger[] fibCache = new BigInteger[100000];

    static {
        fibCache[0] = BigInteger.ONE;
        fibCache[1] = BigInteger.ONE;
    }

    public static BigInteger fibonacci(int b) {

        if (fibCache[b] == null) {
            fibCache[b] = fibonacci(b - 1).add(fibonacci(b - 2));
        }

        return fibCache[b];
    }

    public static void main(String[] args) {

        System.out.println(fibonacci(99));
    }
}
