package com.zetcode;

// The == operator compares object references and equals()
// object values. Java internally caches integers 
// from -128 to +127, so comparing them with == 'works'.
// we should use equals

public class IntegerEqual {

    public static void main(String[] args) {

        Integer a = 127;
        Integer b = 127;
        Integer c = 128;
        Integer d = 128;

        System.out.println(a == b);
        System.out.println(c == d);
    }
}
