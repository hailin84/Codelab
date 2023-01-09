package org.alive.idealab.leetcode;

/**
 * 二维数组中查询指定的数字
 *
 * <a href="https://leetcode.cn/problems/er-wei-shu-zu-zhong-de-cha-zhao-lcof/description/">剑指 Offer 04. 二维数组中的查找 </a>
 *
 */
public class SearchInMatrix {

    /**
     * 下楼梯找法，类似于跳跃表的查找方法
     *
     * @param matrix
     * @param target
     * @return
     */
    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        if (matrix.length == 0) {
            return false;
        }
        int rows = matrix.length;
        int cols = matrix[0].length;
        int x = 0;
        int y = cols - 1;
        boolean founded = false;
        while (x < rows && y >= 0) {
            if (target > matrix[x][y]) {
                x++;
            } else if (target < matrix[x][y]) {
                y--;
            } else {
                founded = true;
                break;
            }
        }
        return founded;
    }
}
