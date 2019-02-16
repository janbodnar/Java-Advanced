package com.zetcode;

public class StringBuilderElements {

    public static void main(String[] args) {
        
        StringBuilder sb = new StringBuilder("Misty mountains");
        System.out.println(sb);
        
        sb.deleteCharAt(sb.length()-1);
        System.out.println(sb);
        
        sb.append('s');
        System.out.println(sb);
        
        sb.insert(0, 'T');
        sb.insert(1, 'h');
        sb.insert(2, 'e');
        sb.insert(3, ' ');
        System.out.println(sb);
        
        sb.setCharAt(4, 'm');
        System.out.println(sb);    
    }
}
