package com.zetcode;

public class AutoboxingEx2 {

    public static void main(String[] args) {
        
        Integer a = new Integer(5);
        Integer b = new Integer(7);
        
        Integer add = a + b;
        Integer mul = a * b;
        
        System.out.println(add);
        System.out.println(mul);    
    }
}
