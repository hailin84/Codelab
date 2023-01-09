package org.alive.idealab.algorithm;

/**
 * 搜索算法
 */
public class SearchAlg {

    /**
     * 二分查找
     * @param array
     * @param target
     * @return
     */
    public static int binarySearch(int[] array, int target) {
        if (array == null) {
            return -1;
        }
        int start = 0;
        int end = array.length - 1;
        // 注意条件是小于等于，如果end取array.length，则条件为小于
        while (start <= end) {
            int mid = start + (end - start) / 2;
            if (array[mid] == target) {
                return mid;
            } else if (target < array[mid]) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        return -1;
    }
}
