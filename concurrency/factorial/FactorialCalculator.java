package com.zetcode;

import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class FactorialCalculator implements Callable<BigInteger> {

    private int value;

    public FactorialCalculator(int value) {

        this.value = value;
    }

    @Override
    public BigInteger call() throws Exception {

        var result = BigInteger.valueOf(1);

        if (value == 0 || value == 1) {

            result = BigInteger.valueOf(1);
        } else {

            for (int i = 2; i <= value; i++) {

                result = result.multiply(BigInteger.valueOf(i));
            }
        }

        TimeUnit.MILLISECONDS.sleep(500);

        return result;
    }
}
