package com.zetcode;

@FunctionalInterface
interface Addition {  
    
    public int add(int a, int b);  
}  

public class JavaLambdaEx3 {

    public static void main(String[] args) {
        
        
        Addition addition = (x, y) -> (x + y);
        
        System.out.println(addition.add(4, 5));
        System.out.println(addition.add(5, 5));
        System.out.println(addition.add(6, 6));

    }
}
