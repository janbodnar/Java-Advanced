package com.zetcode;

// the == operator compares object references
// string literals are interned

// String interning is an optimization technique by the compiler.
// If we have two identical string literals in one compilation unit then
// the code generated ensures that there is only one string object created
// for all the instances of that literal.

public class StringIntern {

    public static void main(String[] args) {

        String val1 = "70";
        String val2 = "70";
        String val3 = "7" + "0";

        System.out.println(val1 == val2);
        System.out.println(val2 == val3);
        System.out.println(val1 == val3);
    }
}
