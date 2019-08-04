package com.zetcode;

@FunctionalInterface
interface Concat<X>
{
    X concat(X arg1, X arg2);
}

public class CustomFunctionalInterface {

    public static void main(String[] args) {

        // String::concat
        Concat<String> concatFun = (s, str) -> s.concat(str);

        var output = concatFun.concat("golden", " eagle");
        System.out.println(output);

        // Integer::sum
        Concat<Integer> concatFun2 = (a, b) -> Integer.sum(a, b);
        var output2 = concatFun2.concat(2, 3);
        System.out.println(output2);
    }
}
