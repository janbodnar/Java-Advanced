package com.zetcode;

import java.util.Optional;

public class OptionalsEx {

    public static void main(String[] args) {

        if (getNullMessage().isPresent()) {
            System.out.println(getNullMessage().get());
        } else {
            System.out.println("n/a");
        }

        if (getEmptyMessage().isPresent()) {
            System.out.println(getEmptyMessage().get());
        } else {
            System.out.println("n/a");
        }

        if (getCustomMessage().isPresent()) {
            System.out.println(getCustomMessage().get());
        } else {
            System.out.println("n/a");
        }
    }
    private static Optional<String> getNullMessage() {
        return Optional.ofNullable(null);
    }

    private static Optional<String> getEmptyMessage() {
        return Optional.empty();
    }

    private static Optional<String> getCustomMessage() {
        return Optional.of("Hello there!");
    }
}

