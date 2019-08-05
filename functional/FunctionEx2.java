package com.zetcode;

import java.util.function.Function;

public class FunctionEx2 {

    public static void main(String[] args) {

        // String::toUpperCase
        Function<String, String> upperFun = s -> s.toUpperCase();

        var res = upperFun.apply("falcon");
        System.out.println(res);
    }
}
