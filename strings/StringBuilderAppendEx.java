package com.zetcode;

import java.util.stream.LongStream;

public class StringBuilderAppendEx {
    
    private final static long MAX_VAL = 500;

    public static void main(String[] args) {

        var builder = new StringBuilder();

        var sum = LongStream.rangeClosed(0, MAX_VAL).sum();
        
        LongStream.rangeClosed(1, MAX_VAL).forEach(e -> {
        
            builder.append(e);
            
            if (e % 10 == 0) {
                builder.append("\n");
            }
            
            if (e < MAX_VAL) {
                builder.append(" + ");
            } else {
                builder.append(" = ");
            }
        });
        
        builder.append(sum);
        
        System.out.println(builder);
    }
}
