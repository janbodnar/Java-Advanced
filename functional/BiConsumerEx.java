package com.zetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

// BiConsumer represents an operation that accepts two input arguments and
// returns no result.  This is the two-arity specialization of Consumer.
// Unlike most other functional interfaces, BiConsumer is expected
// to operate via side-effects.

public class BiConsumerEx {

    public static void main(String[] args) {

        Map<Integer,String> map = new HashMap<>() {{
            put(1, "a");
            put(2, "b");
            put(3, "c");
        }};

        BiConsumer<Integer,String> biConsumer = (key, value) ->
                System.out.printf("%d: %s%n", key, value);
        map.forEach(biConsumer);
    }
}
