package com.zetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Compare lists by traversing lists with for loops

public class CompareListsEx4 {

    public static void main(String[] args) {

        // Size & values of elements are same but only order is different
        var vals1 = new ArrayList<>(Arrays.asList(65L, 74L, 89L));
        var vals2 = new ArrayList<>(Arrays.asList(65L, 89L, 74L));
        System.out.printf("Output: %s%n", compareLists(vals1, vals2));

        // Values are different
        vals1 = new ArrayList<>(Arrays.asList(6L, 7L, 89L));
        vals2 = new ArrayList<>(Arrays.asList(6L, 89L, 74L));
        System.out.printf("Output: %s%n", compareLists(vals1, vals2));

        // Size is different
        vals1 = new ArrayList<>(Arrays.asList(6L, 7L, 89L));
        vals2 = new ArrayList<>(Arrays.asList(6L, 7L, 89L, 7L));
        System.out.printf("Output: %s%n", compareLists(vals1, vals2));

        // Both lists are null
        vals1 = null;
        vals2 = null;
        System.out.printf("Output: %s%n", compareLists(vals1, vals2));

        // Both lists are empty
        vals1 = new ArrayList<>();
        vals2 = new ArrayList<>();
        System.out.printf("Output: %s%n", compareLists(vals1, vals2));
    }

    public static boolean compareLists(List<Long> l1, List<Long> l2) {

        if (l1 == null && l2 == null) {

            return true;
        }

        if (l1 != null && l2 != null) {

            if (l1.size() == l2.size()) {

                for (Long val1 : l1) {

                    boolean isEqual = false;

                    for (Long val2 : l2) {

                        if (val1.equals(val2)) {

                            isEqual = true;
                            break;
                        }
                    }

                    if (!isEqual) {

                        return false;
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }
}
