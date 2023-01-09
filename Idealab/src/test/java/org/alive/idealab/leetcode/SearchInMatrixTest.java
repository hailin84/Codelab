package org.alive.idealab.leetcode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SearchInMatrixTest {

    private SearchInMatrix sm = new SearchInMatrix();

    @Test
    void findNumberIn2DArray() {
        int[][] matrix =
                {
                        {1, 4, 7, 11, 15},
                        {2, 5, 8, 12, 19},
                        {3, 6, 9, 16, 22},
                        {10, 13, 14, 17, 24},
                        {18, 21, 23, 26, 30}
                };

        Assertions.assertTrue(sm.findNumberIn2DArray(matrix, 5));
        Assertions.assertFalse(sm.findNumberIn2DArray(matrix, 20));
    }
}