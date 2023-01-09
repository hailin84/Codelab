package org.alive.idealab.algorithm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SearchTest {

    @Test
    void binarySearch() {
        int[] array = new int[]{1, 3, 5, 7, 9, 11, 19};
        Assertions.assertEquals(1, SearchAlg.binarySearch(array, 3));
    }
}