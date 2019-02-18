package com.zetcode;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FibonacciStreamEx {

    public static List<BigInteger> fibonacci(int limit) {

        var vals = Stream.iterate(new BigInteger[] { BigInteger.ZERO, BigInteger.ONE },
                t -> new BigInteger[]{t[1], t[0].add(t[1])})
                .limit(limit)
                .map(n -> n[1])
                .collect(Collectors.toList());

        return vals;
    }

    public static void main(String[] args) {

        System.out.println(fibonacci(99));
    }
}
