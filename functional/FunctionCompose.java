package com.zetcode;

import java.util.function.Function;

public class FunctionCompose {

    public static void main(String[] args) {

        Function<String, String> upperFun = (val) -> val.toUpperCase();

        Function<String, String> reverseFun = (val) -> {

            return new StringBuilder(val).reverse().toString();
        };

        var res = upperFun.compose(reverseFun).apply("falcon");
        System.out.println(res);
    }
}
