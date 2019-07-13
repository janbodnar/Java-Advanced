package com.zetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HashMapConvert {

    public static void main(String[] args) {

        Map<String, String> colours = Map.of(
                "AliceBlue", "#f0f8ff",
                "GreenYellow", "#adff2f",
                "IndianRed", "#cd5c5c",
                "khaki", "#f0e68c"
        );

        Set<Map.Entry<String, String>> entries = colours.entrySet();
        System.out.println(entries);

        List<Map.Entry<String, String>> mylist = new ArrayList<>(entries);
        System.out.println(mylist);
    }
}
