package com.zetcode;

public class NinetyNineBottles {


    public static void main(String[] args) {

        // 99 bottles of beer on the wall, 99 bottles of beer.
        // Take one down, pass it around, 98 bottles of beer on the wall

        int nOfbottles = 99;

        for (int i = nOfbottles; i >= 1; i--) {

            if (i == 1) {

                System.out.printf("%d bottle of beer on the wall, %d bottle of beer.%n", i, i);
                System.out.println("Take one down, pass it around, no bottles of beer on the wall.");
                System.out.println();

            } else {

                System.out.printf("%d bottles of beer on the wall, %d bottles of beer.%n", i, i);
                System.out.printf("Take one down, pass it around, %d bottles of beer on the wall.%n", i - 1);
                System.out.println();
            }
        }
    }
}
