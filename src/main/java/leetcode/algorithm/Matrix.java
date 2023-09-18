package leetcode.algorithm;


import com.sun.org.apache.regexp.internal.RE;

import java.util.Arrays;

/**
 * 二维数组（矩阵）的处理，可通过 direction 来快速确定搜索范围和方向
 */
public class Matrix {


    /**
     * 661. 图片平滑器
     * 基于 矩阵前缀和 的思想
     */
    public int[][] imageSmoother(int[][] img) {   //基于矩阵的方向
        int rows = img.length;
        int cols = img[0].length;
        int[][] ans = new int[rows][cols];
        int[][] directions = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 0}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int sum = 0;
                int count = 0;
                for (int[] dir : directions) {
                    int xx = i + dir[0];
                    int yy = j + dir[1];
                    if (xx < 0 || xx >= rows || yy < 0 || yy >= cols)
                        continue;
                    sum += img[xx][yy];
                    count++;
                }
                ans[i][j] = sum / count;
            }
        }
        return ans;
    }


    public int[][] imageSmoother01(int[][] img) {  //前缀和 + 最值简化判断逻辑
        int rows = img.length;
        int columns = img[0].length;
        int[][] ans = new int[rows][columns];
        int[][] prefixSum = new int[rows + 1][columns + 1];  //列仅补齐左侧，左侧第一列为 0
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                prefixSum[i][j] = prefixSum[i - 1][j] + prefixSum[i][j - 1] - prefixSum[i - 1][j - 1] + img[i - 1][j - 1];
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                //四个点均为 img 矩阵中的坐标
                int left = Math.max(j - 1, 0);
                int right = Math.min(j + 1, columns - 1);
                int top = Math.max(i - 1, 0);
                int bottom = Math.min(i + 1, rows - 1);
                //矩阵的合理范围
                int window = (right - left + 1) * (bottom - top + 1);
                //矩阵的累加值
                int sum = prefixSum[bottom + 1][right + 1]
                        - prefixSum[bottom + 1][left]
                        - prefixSum[top][right + 1]
                        + prefixSum[top][left];
                //矩阵的平均值
                ans[i][j] = sum / window;
            }
        }
        return ans;
    }


    /**
     * 766. 托普利茨矩阵
     */
    public boolean isToeplitzMatrix(int[][] matrix) {
        //从第一列开始校验
        for (int i = 0; i < matrix.length; i++) {
            if (!checkPath(matrix, i, 0, matrix[i][0])) {
                return false;
            }
        }

        //从第一行开始校验
        for (int j = 0; j < matrix[0].length; j++) {
            if (!checkPath(matrix, 0, j, matrix[0][j])) {
                return false;
            }
        }
        return true;
    }

    private boolean checkPath(int[][] matrix, int row, int col, int target) {
        while (row < matrix.length && col < matrix[0].length) {
            if (matrix[row][col] != target) {
                return false;
            }
            row++;
            col++;
        }
        return true;
    }

    public boolean isToeplitzMatrix01(int[][] matrix) {
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 1; j < matrix[0].length; j++) {
                if (matrix[i][j] != matrix[i - 1][j - 1]) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 832. 翻转图像
     */
    public int[][] flipAndInvertImage(int[][] image) {
        for (int i = 0; i < image.length; i++) {
            int left = 0;
            int right = image[0].length - 1;
            while (left < right) {
                int xx = image[i][left];
                int yy = image[i][right];
                image[i][left] = yy == 1 ? 0 : 1;
                image[i][right] = xx == 1 ? 0 : 1;
                left++;
                right--;
            }
            if (left == right) {
                image[i][left] = image[i][left] == 1 ? 0 : 1;
            }
        }
        return image;
    }



}
