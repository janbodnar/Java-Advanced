package com.zetcode;

import java.math.BigInteger;
import java.util.Scanner;

public class FactorialEx {

    public static void main(String[] args) {

        var scanner = new Scanner(System.in);

        System.out.print("Enter a number: ");

        int n = scanner.nextInt();
        var fact = factorial(n);

        System.out.printf("%d! is %d", n, fact);
    }

    private static BigInteger factorial(int value) {

        var result = BigInteger.ONE;

        if (value != 0 && value != 1) {

            for (int i = 2; i <= value; i++) {

                result = result.multiply(BigInteger.valueOf(i));
            }
        }

        return result;
    }
}
