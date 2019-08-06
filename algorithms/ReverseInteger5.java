package com.zetcode;

public class ReverseInteger5 {

    public static void main(String[] args) {

        int value = 73203000;

        int value2 = reverse(value);

        System.out.println(value2);
    }

    private static int reverse(int number) {

        int reverse = 0;
        int remainder;

        do {
            remainder = number % 10;
            reverse = reverse * 10 + remainder;
            number = number / 10;

        } while (number > 0);

        return reverse;
    }
}
