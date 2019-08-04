package com.zetcode;

import java.util.function.BiFunction;

public class BiFunctionEx {

    public static void main(String[] args) {

        // Integer::sum
        BiFunction<Integer, Integer, Integer> addFun = (x, y) -> x + y;
        var ret = addFun.apply(10, 20);
        System.out.println(ret);

        BiFunction<Integer, Integer, Integer> subFun = (x, y) -> x - y;
        var ret2 = subFun.apply(10, 20);
        System.out.println(ret2);
    }
}
