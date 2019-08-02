package com.zetcode;

// there is a subtle difference between the 
// prefix and suffix increment operators

public class IncrementOperator {

    public static void main(String[] args) {

        int num = 0;

        num = num++;

        System.out.println(num);

        num = ++num;
        
        System.out.println(num);
    }
}
