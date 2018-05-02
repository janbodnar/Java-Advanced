package com.zetcode;

// This example demonstrates autoboxing and object interning

public class AutoboxingEx3 {

    public static void main(String[] args) {
        
        Integer a = 5; // new Integer(5);
        Integer b = 5; // new Integer(5);         

        System.out.println(a == b);
        System.out.println(a.equals(b));
        System.out.println(a.compareTo(b));

        Integer c = 155;
        Integer d = 155;

        System.out.println(c == d);
        System.out.println(c.equals(d));
        System.out.println(c.compareTo(d));
    }
}
