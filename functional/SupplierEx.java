package com.zetcode;

import java.util.Random;
import java.util.function.Supplier;

class RandomIntSupplier implements Supplier<Integer> {

    @Override
    public Integer get() {

        return new Random().nextInt();
    }
}

public class SupplierEx {

    public static void main(String[] args) {

        var ris = new RandomIntSupplier();
        var res = ris.get();

        System.out.println(res);
    }
}
