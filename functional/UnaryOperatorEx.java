package com.zetcode;

import java.util.function.UnaryOperator;

// UnaryOperator represents an operation on a single operand that produces
// a result of the same type as its operand.  This is a specialization
// of Function for the case where the operand and result are of the same type.

public class UnaryOperatorEx {

    public static void main(String[] args) {

        UnaryOperator<Integer> op1 = t -> t + 10;
        UnaryOperator<Integer> op2 = t -> t * 10;

        int a = op1.andThen(op2).apply(5);
        System.out.println(a);

        int b = op1.compose(op2).apply(5);
        System.out.println(b);
    }
}
