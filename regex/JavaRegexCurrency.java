package com.zetcode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaRegexCurrency {

    public static void main(String[] args) {

        String content = """
Currency symbols: ฿ Thailand bath, ₹ Indian rupee, ₾ Georgian lari, $ Dollar,
€ Euro, ¥ Yen, £ Pound Sterling""";

        String regex = "\\p{Sc}";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find())
        {
            System.out.printf("%s at %d-%d%n", matcher.group(), matcher.start(),
                    matcher.end());
        }
    }
}
