package utils.review;

import java.util.HashMap;

public class BinSearch2 {

    /* The isBadVersion API is defined in the parent class VersionControl.
      boolean isBadVersion(int version); */
    public boolean isBadVersion(int version) {
        return true;
    }

    /**
     * 278. 第一个错误的版本
     *
     */
    //类型二中的单侧逼近的一种
    public int firstBadVersion(int n) {
        int left = 0;
        int right = n;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (isBadVersion(mid)) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    //类型二中的区间内逼近的一种
    public int firstBadVersion01(int n) {
        int left = 0;
        int right = n;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (isBadVersion(mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return right;
    }

    //整合型的写法
    public int firstBadVersion02(int n) {
        int left = 0;
        int right = n;
        int k = left;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (isBadVersion(mid)) {
                k = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return k;
    }


    /**
     * 33. 搜索旋转排序数组
     *
     */
    public int search(int[] nums, int target) {
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            hTable.put(nums[i], i);
            if (hTable.get(target) != null)
                return hTable.get(target);
        }
        return -1;
    }


    public int search01(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target)
                return mid;
            else if (nums[left] == target)
                return left;
            else if (nums[right] == target)
                return right;
            else if (nums[mid] <= nums[right]) {  //右侧区间内有序
                if (nums[mid] < target && target < nums[right]) {  //target在区间内
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            } else if (nums[left] <= nums[mid]) {
                if (nums[left] < target && target < nums[mid])
                    right = mid - 1;
                else
                    left = mid + 1;
            }
        }
        return -1;
    }


    /**
     * 74. 搜索二维矩阵
     *
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        int rows = matrix.length - 1;
        int columns = matrix[0].length - 1;
        for (int row = 0; row <= rows; row++) {
            if (matrix[row][0] <= target && target <= matrix[row][columns]) {
                int left = 0;
                int right = columns;
                while (left <= right) {
                    int mid = left + ((right - left) >> 1);
                    if (matrix[row][mid] == target)
                        return true;
                    else if (matrix[row][mid] < target)
                        left = mid + 1;
                    else
                        right = mid - 1;
                }
            }
        }
        return false;
    }

    public boolean searchMatrix01(int[][] matrix, int target) {
        int rows = matrix.length - 1;
        int columns = matrix[0].length - 1;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int row = 0; row <= rows; row++) {
            if (matrix[row][0] <= target && target <= matrix[row][columns]) {
                for (int column = 0; column <= columns; column++) {
                    if (matrix[row][column] == target) return true;
                }
            }
        }
        return false;
    }

    public boolean searchMatrix02(int[][] matrix, int target) {
        int rows = matrix.length - 1;
        int columns = matrix[0].length - 1;
        for (int row = 0; row <= rows; row++) {
            if (matrix[row][0] <= target && target <= matrix[row][columns]) {
                int left = 0;
                int right = columns;
                while (left <= right) {
                    int mid = left + ((right - left) >> 1);
                    if (matrix[row][mid] < target)
                        left = mid + 1;
                    else
                        right = mid - 1;
                }
                if (left == matrix[0].length) return false;
                else if (matrix[row][left] == target) return true;
            }
        }
        return false;
    }

    public boolean searchMatrix03(int[][] matrix, int target) {
        int rows = matrix.length - 1;
        int columns = matrix[0].length - 1;
        for (int row = 0; row <= rows; row++) {
            if (matrix[row][0] <= target && target <= matrix[row][columns]) {
                int left = 0;
                int right = columns;
                while (left <= right) {
                    int mid = left + ((right - left) >> 1);
                    if (matrix[row][mid] <= target)
                        left = mid + 1;
                    else
                        right = mid - 1;
                }
                if (right == -1) return false;
                else if (matrix[row][right] == target) return true;
            }
        }
        return false;
    }

    public boolean searchMatrix04(int[][] matrix, int target) {
        int rows = matrix.length - 1;
        int columns = matrix[0].length - 1;
        for (int row = 0; row <= rows; row++) {
            if (matrix[row][0] <= target && target <= matrix[row][columns]) {
                int left = 0;
                int right = columns;
                while (left < right) {
                    int mid = left + ((right - left + 1) >> 1);
                    if (matrix[row][mid] <= target)
                        left = mid;
                    else
                        right = mid - 1;
                }
                if (matrix[row][left] == target) return true;
            }
        }
        return false;
    }


    public boolean searchMatrix05(int[][] matrix, int target) {
        int rows = matrix.length - 1;
        int columns = matrix[0].length - 1;
        for (int row = 0; row <= rows; row++) {
            if (matrix[row][0] <= target && target <= matrix[row][columns]) {
                int left = 0;
                int right = columns;
                int k = 0;
                while (left <= right) {
                    int mid = left + ((right - left) >> 1);
                    if (matrix[row][mid] < target)
                        left = mid + 1;
                    else {
                        k = mid;
                        right = mid - 1;
                    }
                }
                if (matrix[row][k] == target) return true;
            }
        }
        return false;
    }


}
