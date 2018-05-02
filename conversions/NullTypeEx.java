package com.zetcode;

import java.util.Random;

public class NullTypeEx {
    
    private static String getName() {
        
        Random r = new Random();
        boolean n = r.nextBoolean();
        
        if (n == true) {
        
            return "John";
        } else {
            
            return null;
        }
    }

    public static void main(String[] args) {
        
        String name = getName();
        
        System.out.println(name);

        System.out.println(null == null);
        
        if ("John".equals(name)) {
            
            System.out.println("His name is John");
        }        
    }
}
