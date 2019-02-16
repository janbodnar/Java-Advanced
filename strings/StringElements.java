package com.zetcode;

public class StringElements {

    public static void main(String[] args) {
    
        char[] crs = {'Z', 'e', 't', 'C', 'o', 'd', 'e' };
        String s = new String(crs);
        
        char c1 = s.charAt(0);
        char c2 = s.charAt(s.length()-1);
        
        System.out.println(c1);
        System.out.println(c2);
        
        int i1 = s.indexOf('e');
        int i2 = s.lastIndexOf('e');
        
        System.out.println("The first index of character e is " + i1);
        System.out.println("The last index of character e is " + i2);
        
        System.out.println(s.contains("t"));
        System.out.println(s.contains("f"));
        
        char[] elements = s.toCharArray();
        
        for (char el : elements) {
            
            System.out.println(el);
        }                
    }
}
