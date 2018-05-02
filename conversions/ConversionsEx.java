package com.zetcode;

import java.util.Objects;

public class ConversionsEx {

    public static void main(String[] args) {
        
        // https://stackoverflow.com/questions/1514910/how-to-properly-compare-two-integers-in-java?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa

        Integer m = 4;
        Integer n = 5;
        
        if (Objects.equals(m, n)) {
            
            System.out.println("Variables are equal");
        } else {
            System.out.println("Variables are not equal");
        }
        
        if (m.intValue() == n.intValue()) {
            
            System.out.println("Variables are equal");
        } else {
            System.out.println("Variables are not equal");
        }
        

        // you can't convert-and-autobox in one step
        // you can't implicitly narrow a type
        // 1L
        Long num = 1L;

        // Why new Long(10) compiles? Because there is a suitable constructor 
        // available that excepts integer arguments. 
        Long num2 = new Long(2);

        Byte b = new Byte((byte) 10);
        byte b2 = 34;

        Long x = (long) 10;

        int x2 = (int) 23.4;
        double x3 = 23;

        double x4 = 32f;
        float x5 = (float) 35.567d;

        byte g = (12) * 3;
        //byte g2 = 3 * g; 
    }

}
