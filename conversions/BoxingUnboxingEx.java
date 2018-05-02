package com.zetcode;

public class BoxingUnboxingEx {

    public static void main(String[] args) {
        
        long a = 124235L;        
        
        Long b = new Long(a);
        long c = b.longValue();
        
        System.out.println(c);
    }
}
