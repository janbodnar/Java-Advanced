package com.zetcode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JavaCollectGroupByCategory {

    public static void main(String[] args) {

        Map<String, List<Product>> productsByCategories =
                products().stream().collect(Collectors.groupingBy(Product::getCategory));

        productsByCategories.forEach((k, v) -> {

            System.out.println(k);

            for (var name : v) {
                System.out.println(name);
            }
        });

    }

    private static List<Product> products() {

        return List.of(
                new Product("apple", "fruit", new BigDecimal("4.50")),
                new Product("banana", "fruit", new BigDecimal("3.76")),
                new Product("carrot", "vegetables", new BigDecimal("2.98")),
                new Product("potato", "vegetables", new BigDecimal("0.92")),
                new Product("garlic", "vegetables", new BigDecimal("1.32")),
                new Product("ginger", "vegetables", new BigDecimal("2.45")),
                new Product("white bread", "bakery", new BigDecimal("1.50")),
                new Product("roll", "bakery", new BigDecimal("0.08")),
                new Product("bagel", "bakery", new BigDecimal("0.15"))
        );
    }
}
