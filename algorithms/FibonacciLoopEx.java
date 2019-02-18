package com.zetcode;

import java.math.BigInteger;

public class FibonacciLoopEx {

    public static BigInteger fibonacci(int n) {

        if (n <= 1) return BigInteger.valueOf(n);

        BigInteger previous = BigInteger.ZERO, next = BigInteger.ONE, sum;

        for (int i = 2; i <= n; i++) {

            sum = previous;
            previous = next;
            next = sum.add(previous);
        }

        return next;
    }

    public static void main(String[] args) {

        for (int i = 0; i <= 99; i++) {

            BigInteger val = fibonacci(i);
            System.out.println(val);
        }
    }
}
