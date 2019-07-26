package com.zetcode;

import java.util.Arrays;
import java.util.List;

public class ListToArray {

    public static void main(String[] args) {

        List<Double> vals = List.of(2.3, 2.5, 3.1, 6.7);

        Double[] vals2 = vals.toArray(new Double[0]);
        System.out.println(Arrays.toString(vals2));

        double[] vals3 = vals.stream().mapToDouble(Double::doubleValue).toArray();
        System.out.println(Arrays.toString(vals3));
    }
}
