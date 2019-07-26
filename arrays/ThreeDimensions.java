package com.zetcode;

public class ThreeDimensions {

    public static void main(String[] args) {

        int[][][] n3 = {
                {{12, 2, 8}, {0, 2, 1}},
                {{14, 5, 2}, {0, 5, 4}},
                {{3, 26, 9}, {8, 7, 1}},
                {{4, 11, 2}, {0, 9, 6}}
        };

        int d1 = n3.length;
        int d2 = n3[0].length;
        int d3 = n3[0][0].length;

        for (int i = 0; i < d1; i++) {

            for (int j = 0; j < d2; j++) {

                for (int k = 0; k < d3; k++) {

                    System.out.print(n3[i][j][k] + " ");
                }
            }
        }

        System.out.print('\n');
    }
}
