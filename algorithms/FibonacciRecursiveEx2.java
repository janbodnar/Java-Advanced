package com.zetcode;

import java.math.BigInteger;

public class FibonacciRecursiveEx2 {

    public static BigInteger fibonacci2(int n) {
    
        if (n == 0 || n == 1) {
            return BigInteger.ONE;
        }
        
        return fibonacci2(n - 2).add(fibonacci2(n - 1));
    }

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
        
            System.out.println(fibonacci2(i));
        }
    }
}
