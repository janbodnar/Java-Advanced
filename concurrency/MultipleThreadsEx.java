package com.zetcode;

import java.util.stream.IntStream;

public class MultipleThreadsEx {

    public static void main(String[] args) {

        var t1 = new Thread(MultipleThreadsEx::printValues);
        var t2 = new Thread(MultipleThreadsEx::printValues);
        var t3 = new Thread(MultipleThreadsEx::printValues);

        t1.start();
        t2.start();
        t3.start();
    }

    private static void printValues() {

        IntStream.range(1, 9).forEach(e -> System.out.print(" " + e));
    }
}
