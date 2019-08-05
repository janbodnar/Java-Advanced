package com.zetcode;

import java.util.Comparator;
import java.util.function.BinaryOperator;

public class BinaryOperatorEx2 {

    public static void main(String[] args) {

//        Comparator<Integer> comparator = Comparator.naturalOrder();
        Comparator<Integer> comparator = (a, b) -> (a.compareTo(b));

        BinaryOperator<Integer> opMax = BinaryOperator.maxBy(comparator);
        System.out.println("Max: " + opMax.apply(7, 9));
        System.out.println("Max: " + opMax.apply(9, 5));

        BinaryOperator<Integer> opMin = BinaryOperator.minBy(comparator);
        System.out.println("Min: " + opMin.apply(5, 6));
        System.out.println("Min: " + opMin.apply(3, 6));
    }
}
