package com.zetcode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConsumerEx5 {

    private static RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    private static int DECIMALS = 2;

    public static void main(String[] args) {

        List<Product> products = new ArrayList<>();
        products.add(new Product("A", new BigDecimal("2.54")));
        products.add(new Product("B", new BigDecimal("3.89")));
        products.add(new Product("C", new BigDecimal("5.99")));
        products.add(new Product("D", new BigDecimal("9.99")));

        Consumer<Product> incPrice = p -> {
            p.setPrice(rounded(p.getPrice().multiply(new BigDecimal("1.1"))));
        };

        process(products, incPrice.andThen(System.out::println));
    }

    private static BigDecimal rounded(BigDecimal number){
        return number.setScale(DECIMALS, ROUNDING_MODE);
    }

    private static void process(List<Product> data, Consumer<Product> cons) {

        for (var e : data) {

            cons.accept(e);
        }
    }
}
