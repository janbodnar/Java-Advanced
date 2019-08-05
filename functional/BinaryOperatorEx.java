package com.zetcode;

import java.util.function.BinaryOperator;

// BinaryOperator represents an operation upon two operands of the same type,
// producing a result of the same type as the operands.  This is a
// specialization of BiFunction for the case where the operands and
// the result are all of the same type.

public class BinaryOperatorEx {

    public static void main(String[] args) {

        BinaryOperator<Integer> op1 = (x, y) -> x + y;
        BinaryOperator<Integer> op2 = (x, y) -> x * y;

        var res = op1.apply(4, 5);
        System.out.println(res);

        var res2 = op2.apply(4, 5);
        System.out.println(res2);
    }
}
