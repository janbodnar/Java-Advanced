package com.zetcode;

@FunctionalInterface
interface MathOperation {
    int mdo(int a, int b);
}

public class JavaLambdaEx {

    public static void main(String[] args) {

        // with type declaration
        MathOperation add = (int a, int b) -> a + b;

        // without type declaration
        MathOperation sub = (a, b) -> a - b;

        // with return statement along with curly braces
        MathOperation mul = (int a, int b) -> {
            return a * b;
        };

        // without return statement and without curly braces
        MathOperation div = (int a, int b) -> a / b;

        System.out.println(add.mdo(4, 5));
        System.out.println(sub.mdo(6, 5));
        System.out.println(mul.mdo(4, 5));
        System.out.println(div.mdo(4, 2));
    }
}
