package com.zetcode;

public class Associativity {

    public static void main(String[] args) {

        // left-to-right associativity
        int val = 9 / 3 * 3;

        System.out.println(val);

        int val2 = 0;

        // right-to-left associativity
        val2 *= 3 + 1;

        System.out.println(val2);
    }
}
