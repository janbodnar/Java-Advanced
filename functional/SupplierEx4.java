package com.zetcode;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class SupplierEx4 {

    public static void main(String[] args) {

        Supplier<Stream<String>> streamSupplier =
                () -> Stream.of("cloud", "wood", "falcon", "sky", "forest")
                        .filter(s -> s.startsWith("w"));

        var res = streamSupplier.get().anyMatch(s -> true);
        System.out.println(res);

        var res2 = streamSupplier.get().noneMatch(s -> true);
        System.out.println(res2);
    }
}
