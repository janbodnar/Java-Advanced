package com.zetcode;

public class AutoboxingEx {
    
    private static int cube(int x) {
        
        return x * x * x;
    }

    public static void main(String[] args) {
        
        Integer i = 10;
        int j = i;
        
        System.out.println(i);
        System.out.println(j);        
        
        Integer a = cube(i);
        System.out.println(a);    
    }
}
