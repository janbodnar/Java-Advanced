package com.zetcode;

import java.util.Random;
import java.util.function.Supplier;

// Supplier represents a supplier of results.

public class SupplierEx2 {

    public static void main(String[] args) {

        Supplier<Long> randIntFun = () -> new Random().nextLong();

        var value = randIntFun.get();
        System.out.println(value);
    }
}
