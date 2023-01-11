package org.alive.learn.algorithm.search;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SearchTest {

    /**
     * 不使用递归的二分查找
     *
     * @param array
     * @param value
     * @return 关键字下标，没找到则返回-1
     */
    public static int binarySearch(int[] array, int value) {
        if (array == null || array.length == 0) {
            return -1;
        }
        int low = 0;
        int high = array.length - 1;
        int middle = 0;

        while (low <= high) {
            middle = low + (high - low) / 2;
            if (array[middle] > value) {
                //比关键字大则关键字在左区域
                high = middle - 1;
            } else if (array[middle] < value) {
                //比关键字小则关键字在右区域
                low = middle + 1;
            } else {
                return middle;
            }
        }
        return -1;        //最后仍然没有找到，则返回-1
    }


    @Test
    public void testBinarySearch() {
        int[] array = {1, 3, 5, 6, 8, 20};
        Assertions.assertEquals(2, binarySearch(array, 5));
        Assertions.assertEquals(-1, binarySearch(array, 7));
    }
}
