package com.zetcode;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

// calculate an average price and then 
// format it
public class JavaCollectAndThenEx {

    public static void main(String[] args) {

        var vals = List.of(230, 210, 120, 250, 300);

        var avgPrice = vals.stream().collect(Collectors.collectingAndThen(
                Collectors.averagingInt(Integer::intValue),
                avg -> {
                    var nf = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
                    return nf.format(avg);
                })
        );

        System.out.printf("The average price is %s%n", avgPrice);
    }
}
