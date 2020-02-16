package com.zetcode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaRegexExpressions {

    public static void main(String[] args) {

        String[] expressions = {"16 + 11", "12 * 5", "27 / 3", "2 - 8"};
        String pattern = "(\\d+)\\s+([-+*/])\\s+(\\d+)";

        for (var expression : expressions) {

            Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(expression);

            while (matcher.find()) {

                int val1 = Integer.parseInt(matcher.group(1));
                int val2 = Integer.parseInt(matcher.group(3));
                String oper = matcher.group(2);

                var result = switch (oper) {

                    case "+" -> String.format("%s = %d", expression, val1 + val2);
                    case "-" -> String.format("%s = %d", expression, val1 - val2);
                    case "*" -> String.format("%s = %d", expression, val1 * val2);
                    case "/" -> String.format("%s = %d", expression, val1 / val2);
                    default -> "Unknown operator";
                };

                System.out.println(result);
            }
        }
    }
}
