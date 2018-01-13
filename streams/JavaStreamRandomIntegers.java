package com.zetcode;

import java.util.Arrays;
import java.util.Random;

public class JavaStreamRandomIntegers {

    public static void main(String[] args) {

        int[] randInts = new Random().ints(-4, 5).limit(10).toArray();

        System.out.println(Arrays.toString(randInts));
    }
}
