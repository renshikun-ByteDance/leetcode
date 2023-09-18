package utils.review;

import java.util.*;
import java.util.stream.Collectors;

public class leetcode {

    /**
     * 基于Streaming来实现排序、过滤、拼接、转换
     *
     * @param typeAndWeight
     * @return
     */
    public static String mapSorted(HashMap<String, Float> typeAndWeight) {
        String collect = typeAndWeight.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                .map(t -> t.getKey() + ":" + t.getValue())
                .limit(3)
                .collect(Collectors.joining("~"));
        return collect;
    }

    public static String mapFilterSorted(HashMap<String, Float> typeAndWeith) {
        String collect = typeAndWeith.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                .map(t -> t.getKey() + ":" + t.getValue())
                .filter(t -> !t.isEmpty())
                .limit(3)
                .collect(Collectors.joining("~"));
        return collect;
    }


    public static String switchOne(String level) {
        String result = "";
        switch (level) {
            case "A":
                result = "优秀";
                break;
            case "B":
            case "C":
                result = "中等";
                break;
            case "D":
                result = "良好";
                break;
//            default:
//                result="非法输入:";
        }
        return result;
    }

    /**
     * 动态规划
     * 题目：盛最多水的容器
     *
     * @param height
     * @return
     */
    public static int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxArea = 0;
        while (left < right) {
            int Area = 0;
            if (height[left] < height[right]) {
                Area = (right - left) * height[left];
                left++;
            } else {
                Area = (right - left) * height[right];
                right--;
            }
            maxArea = Math.max(maxArea, Area);
        }
        return maxArea;
    }


    /**
     * 动态规划
     * 题目：最小路径和
     *
     * @param grid
     * @return
     */
    public int minPathSum(int[][] grid) {
        int height = grid.length;
        int weight = grid[0].length;
        int[][] gridsMin = new int[height][weight];
        for (int i = height - 1; i >= 0; i--) {
            for (int j = weight - 1; j >= 0; j--) {
                if (i == height - 1 && j == weight - 1) {
                    gridsMin[i][j] = grid[i][j];
                    int a = gridsMin[i][j];
                } else if (i == height - 1) {
                    gridsMin[i][j] = grid[i][j] + gridsMin[i][j + 1];
                } else if (j == weight - 1) {
                    gridsMin[i][j] = grid[i][j] + gridsMin[i + 1][j];
                } else {
                    gridsMin[i][j] = grid[i][j] + Math.min(gridsMin[i][j + 1], gridsMin[i + 1][j]);
                }
            }
        }
        return gridsMin[0][0];
    }


    /**
     * 动态规划
     * 题目：不同路径
     *
     * @param m
     * @param n
     * @return
     */
    public int uniquePaths(int m, int n) {
        int[][] sumPointPaths = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) {
                    sumPointPaths[i][j] = 1;
                } else if (i == 0) {
                    sumPointPaths[0][j] = 1;
                } else if (j == 0) {
                    sumPointPaths[i][0] = 1;
                } else {
                    sumPointPaths[i][j] = sumPointPaths[i - 1][j] + sumPointPaths[i][j - 1];
                }
            }
        }
        return sumPointPaths[m - 1][n - 1];
    }


    /**
     * 动态规划
     * 题目：不同路径 II
     *
     * @param obstacleGrid
     * @return
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        int[][] sumPointPaths = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (obstacleGrid[0][0] == 1) {
                    return 0;
                } else if (i == 0 && j == 0) {
                    sumPointPaths[i][j] = 1;
                } else if (obstacleGrid[i][j] == 1) {                 //由于自身原因导致此点不通/0
                    sumPointPaths[i][j] = 0;
                } else if (i == 0) {
                    sumPointPaths[0][j] = sumPointPaths[0][j - 1];   //第0行，由于左侧不通，导致此点不通/0
                } else if (j == 0) {
                    sumPointPaths[i][0] = sumPointPaths[i - 1][0];   //第0列，由于上侧不通，导致此点不通/0
                } else {
                    sumPointPaths[i][j] = sumPointPaths[i - 1][j] + sumPointPaths[i][j - 1];
                    //第i行j列，通就是通（上方或左侧不为0），不通就是不通0（上方和左侧均为0）
                }
            }
        }
        return sumPointPaths[m - 1][n - 1];
    }

    /**
     * 55. 跳跃游戏
     */
    public boolean canJump(int[] nums) { //动态规划
        int maxIndex = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > maxIndex) {
                return false;
            }
            maxIndex = Math.max(maxIndex, i + nums[i]);
        }
        return true;
    }

    public boolean canJump01(int[] nums) {
        int minStep = 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            if (nums[i] < minStep) {
                minStep++;
            } else {
                minStep = 1;
            }
            if (i == 0 && minStep > 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 数组
     * 题目：只出现一次的数字
     *
     * @param nums
     * @return
     */
    public int singleNumber(int[] nums) {
        int single = 0;
        for (int i = 0; i < nums.length; i++) {
            single = single ^ nums[i];
        }
        return single;
    }

    public int singleNumber01(int[] nums) {
        HashMap<Integer, Integer> digitnum = new HashMap<>();
        for (int i : nums) {
            Integer dNum = digitnum.get(i);
            dNum = dNum == null ? 1 : dNum + 1;
            digitnum.put(i, dNum);
        }
        for (Map.Entry<Integer, Integer> entry : digitnum.entrySet()) {
            if (entry.getValue() == 1) {
                return entry.getKey();
            }
        }
        return 0;
    }






    public int singleNumber1(int[] nums) {
        HashMap<Integer, Integer> digitNum = new HashMap<>();
        for (int num : nums) {
            digitNum.put(num, digitNum.getOrDefault(num, 0) + 1);
        }
        for (int num : digitNum.keySet()) {
            if (digitNum.get(num) == 1) {
                return num;
            }
        }
        return -1;
    }


    public int removeDuplicates(int[] nums) {
        int slow = 1;
        if (nums.length == 1) {
            return 1;
        }
        for (int k = 1; k < nums.length; k++) {
            if (nums[k] != nums[k - 1]) {
                nums[slow] = nums[k];
                slow++;
            }
        }
        return slow;
    }

    /**
     */

    public void rotate(int[] nums, int k) {
        int[] temp = new int[nums.length];
        System.arraycopy(nums, 0, temp, 0, nums.length);
        for (int i = 0; i < nums.length; i++) {
            nums[(i + k) % nums.length] = temp[i];
        }
    }

    public boolean containsDuplicate(int[] nums) {
        Arrays.sort(nums);
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1]) {
                return true;
            }
        }
        return false;
    }


    public int singleNumber03(int[] nums) {
        int single = 0;
        for (int num : nums) {
            single ^= num;
        }
        return single;
    }

    public int singleNumber04(int[] nums) {
        HashMap<Integer, Integer> digitAndnum = new HashMap<>();
        for (int num : nums) {
            digitAndnum.put(num, digitAndnum.getOrDefault(num, 0) + 1);
        }
        for (int key : digitAndnum.keySet()) {
            if (digitAndnum.get(key) == 1) {
                return key;
            }
        }
        return -1;
    }

    public int singleNumber05(int[] nums) {
        Arrays.sort(nums);
        int length = nums.length;
        if (length == 1) {
            return nums[0];
        }
        for (int i = 1; i < nums.length; i = i + 2) {
            if (nums[i] != nums[i - 1]) {
                return nums[i - 1];
            }
        }
        if (nums[length - 1] != nums[length - 2]) {
            return nums[length - 1];
        }
        return -1;
    }

    public int[] plusOne(int[] digits) {
        int length = digits.length;
        int freq = 0;
        for (int i = length - 1; i >= 0; i--) {
            if (digits[i] != 9) {
                digits[i]++;
                break;
            } else {
                digits[i] = 0;
                freq++;
            }
        }
        if (freq == length) {
            int[] newdigits = new int[length + 1];
            newdigits[0] = 1;
            return newdigits;
        } else {
            return digits;
        }
    }

    public int[] twoSum(int[] nums, int target) {
        int[] index = new int[2];
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    index[0] = i;
                    index[1] = j;
                    break;
                }
            }
        }
        return index;
    }

    public int[] twoSumHash(int[] nums, int target) {
        HashMap<Integer, Integer> hashtale = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (hashtale.containsKey(target - nums[i])) {
                return new int[]{hashtale.get(target - nums[i]), i};
            }
            hashtale.put(nums[i], i);
        }
        return new int[]{0};
    }


    public int[] twoSum2(int[] nums, int target) {
        HashMap<Integer, Integer> convertMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            convertMap.put(nums[i], i);
            if (convertMap.containsKey(target - nums[i])) {
                return new int[]{i, convertMap.get(target - nums[i])};
            }
        }
        return new int[]{0};
    }


    /**
     * 36. 有效的数独
     * <p>
     * rows [i行][某个数字]=在i行，某个数字出现的次数   数字范围1-9
     * columns[j列][某个数字]=在j列，某个数字出现的次数 数字范围1-9
     * subboxes[i/3][j/3][某个数字]=在横向第i/3纵向第j/3的3*3宫格内，某个数字出现的次数，数字范围1-9
     *
     * @param board
     * @return
     */
    public boolean isValidSudoku(char[][] board) {
        int[][] rows = new int[9][9];
        int[][] columns = new int[9][9];
        int[][][] subboxes = new int[3][3][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                char digit = board[i][j];
                if (digit != '.') {
                    int cdigit = digit - '0' - 1;
                    rows[i][cdigit]++;
                    columns[j][cdigit]++;
                    subboxes[i / 3][j / 3][cdigit]++;
                    if (rows[i][cdigit] > 1 || columns[j][cdigit] > 1 || subboxes[i / 3][j / 3][cdigit] > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 输入: [0,1,0,3,12]
     * 输出: [1,3,12,0,0]
     *
     * @param nums
     */
    public void moveZeroes(int[] nums) {
        int slow = 0;
        int zeronum = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[slow] = nums[i];
                slow++;
            } else {
                zeronum++;
            }
        }
        if (zeronum != 0) {
            for (int i = nums.length - zeronum; i < nums.length; i++) {
                nums[i] = 0;
            }
        }
    }

    public void rotate01(int[] nums, int k) {
        int[] temp = new int[nums.length];
        System.arraycopy(nums, 0, temp, 0, nums.length);
        for (int i = 0; i < nums.length; i++) {
            nums[(i + k) % nums.length] = temp[i];
        }
    }


    public int removeElement(int[] nums, int val) {
        int slow = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != val) {
                nums[slow] = nums[i];
                slow++;
            }
        }
        return slow;
    }

    public int removeElement01(int[] nums, int val) {
        int left = 0;
        int right = nums.length;
        for (int i = 0; i < right; ) {
            if (nums[i] == val) {
                nums[i] = nums[right - 1];
                right--;
            } else {
                left++;
                i++;
            }
        }
        return left;
    }

    public int removeElement02(int[] nums, int val) {
        int left = 0;
        int right = nums.length;
        while (left < right) {
            if (nums[left] == val) {
                nums[left] = nums[right - 1];
                right--;
            } else {
                left++;
            }

        }
        return left;
    }


    public void rotate(int[][] matrix) {
        int rows = matrix.length;
        int columms = matrix[0].length;
        int[][] newMatrix = new int[rows][columms];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columms; j++) {
                newMatrix[j][columms - 1 - i] = matrix[i][j];
            }
        }
        for (int i = 0; i < rows; i++) {
            System.arraycopy(newMatrix[i], 0, matrix[i], 0, rows);
        }
    }


    public int[][] generateMatrix01(int n) {
        int l = 0, r = n - 1, t = 0, b = n - 1;
        int[][] mat = new int[n][n];
        int num = 1, tar = n * n;
        while (num <= tar) {
            for (int i = l; i <= r; i++) mat[t][i] = num++; // left to right.
            t++;
            for (int i = t; i <= b; i++) mat[i][r] = num++; // top to bottom.
            r--;
            for (int i = r; i >= l; i--) mat[b][i] = num++; // right to left.
            b--;
            for (int i = b; i >= t; i--) mat[i][l] = num++; // bottom to top.
            l++;
        }
        return mat;
    }


    public int[][] generateMatrix(int n) {
        int left = 0;
        int right = n - 1;
        int top = 0;
        int bottom = n - 1;
        int num = 1;
        int sumTime = n * n;
        int[][] result = new int[n][n];
        while (num <= sumTime) {
            //top-横向，left->right
            for (int j = left; j <= right; j++) {
                result[top][j] = num++;
            }
            top++;
            //right-纵向，top->bottom
            for (int i = top; i <= bottom; i++) {
                result[i][right] = num++;
            }
            right--;
            //bottom-横向，right->left
            for (int j = right; j >= left; j--) {
                result[bottom][j] = num++;
            }
            bottom--;
            //left-纵向，bottom->top
            for (int i = bottom; i >= top; i--) {
                result[i][left] = num++;
            }
            left++;
        }
        return result;
    }

    public boolean isValidSudoku01(char[][] board) {
        int[][] rowValid = new int[9][9];
        int[][] columnValid = new int[9][9];
        int[][][] sudoValid = new int[3][3][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    char c = board[i][j];
                    int value = c - '0' - 1;   //0代表1
                    rowValid[i][value]++;     //value=0代表1出现次数
                    columnValid[j][value]++;
                    sudoValid[i / 3][j / 3][value]++;
                    if (rowValid[i][value] > 1 || columnValid[j][value] > 1 || sudoValid[i / 3][j / 3][value] > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public int search(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target) {
                return i;
            }
        }
        return -1;
    }


    /**
     * 二分法
     *
     * @param nums
     * @param target
     * @return
     */
    public int search0(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[left] <= nums[mid]) {  //左侧有序
                if (nums[left] <= target && target < nums[mid]) {  //目标值在此范围内
                    right = mid - 1;   //向左靠，因为到此target一定不为nums[mid]
                } else {
                    left = mid + 1;
                }
            } else {   //右侧有序
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return -1;
    }

    public List<Integer> spiralOrder(int[][] matrix) {
        ArrayList<Integer> result = new ArrayList<>();
        int top = 0;
        int bottom = matrix.length - 1;
        int left = 0;
        int right = matrix[0].length - 1;
        int n = matrix.length * matrix[0].length;
        int times = 1;
        while (times <= n) {
            for (int j = left; j <= right; j++) {
                result.add(matrix[top][j]);
                times++;
            }
            if (times > n) break;
            top++;
            for (int i = top; i <= bottom; i++) {
                result.add(matrix[i][right]);
                times++;
            }
            if (times > n) break;
            right--;
            for (int j = right; j >= left; j--) {
                result.add(matrix[bottom][j]);
                times++;
            }
            if (times > n) break;
            bottom--;
            for (int i = bottom; i >= top; i--) {
                result.add(matrix[i][left]);
                times++;
            }
            if (times > n) break;
            left++;
        }
        return result;
    }


    public String truncateSentence(String s, int k) {
        StringBuilder result = new StringBuilder();
        String[] splitWord = s.split(" ");
        for (int i = 0; i < k; i++) {
            result.append(splitWord[i]).append(" ");
        }
        return result.toString().trim();
    }


    public int search01(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (target < nums[mid]) {
                right = mid - 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }


    public int search02(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            //基于有序区间判断： target是否在此区间内
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[left] <= nums[mid]) { //左侧有序
                if (nums[left] <= target && target < nums[mid]) {   //target在左侧有序区间内
                    right = mid - 1;
                } else { //target不在左侧有序区间内
                    left = mid + 1;
                }
            } else {  //右侧有序
                if (nums[mid] < target && target <= nums[right]) {  //target在右侧有序区间内
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return -1;
    }


    public int maxArea01(int[] height) {
        int maxArea = 0;
        int left = 0;
        int right = height.length - 1;
        while (left < right) {
            int tempArea = (right - left) * Math.min(height[left], height[right]);
            maxArea = Math.max(maxArea, tempArea);
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxArea;
    }


    public String replaceSpace(String s) {
        return s.replace(" ", "%20");
    }


    public void reverseString(char[] s) {
        int right = s.length - 1;
        int left = 0;
        while (left < right) {
            char temp = s[left];
            s[left] = s[right];
            s[right] = temp;
            left++;
            right--;
        }
    }


    public int removeElement06(int[] nums, int val) {
        int slow = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != val) {
                nums[slow] = nums[i];
                slow++;
            }
        }
        return slow + 1;
    }


    public int minSubArrayLen(int target, int[] nums) {
        int minLen = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length; i++) {
            int sum = 0;
            for (int j = i; j < nums.length; j++) {
                sum += nums[j];
                if (sum >= target) {
                    minLen = Math.min(minLen, j - i + 1);
                    break;
                }
            }
        }
        if (minLen == Integer.MAX_VALUE) {
            return 0;
        } else {
            return minLen;
        }
    }


    public int minSubArrayLen01(int target, int[] nums) {
        int minLen = Integer.MAX_VALUE;
        if (nums.length == 0) {
            return 0;
        }
        int top = 0;
        int end = 0;
        int sum = 0;
        while (top < nums.length) {
            sum += nums[top];
            while (sum >= target) {
                minLen = Math.min(minLen, top - end + 1);
                sum -= nums[end];
                end++;
            }
            top++;
        }
        if (minLen == Integer.MAX_VALUE) {
            return 0;
        } else {
            return minLen;
        }
    }

    public int minSubArrayLen99(int s, int[] nums) {
        int n = nums.length;
        if (n == 0) {
            return 0;
        }
        int ans = Integer.MAX_VALUE;
        int start = 0, end = 0;
        int sum = 0;
        while (end < n) {
            sum += nums[end];
            while (sum >= s) {
                ans = Math.min(ans, end - start + 1);
                sum -= nums[start];
                start++;
            }
            end++;
        }
        return ans == Integer.MAX_VALUE ? 0 : ans;
    }


    public int minSubArrayLen06(int s, int[] nums) {
        int sum = 0;
        int left = 0;
        int right = 0;
        int minLen = Integer.MAX_VALUE;
        while (right < nums.length) {
            sum += nums[right];
            while (sum >= s) {
                minLen = Math.min(minLen, right - left + 1);
                sum -= nums[left];
                left++;
            }
            right++;
        }


        return minLen = minLen == Integer.MAX_VALUE ? 0 : minLen;
    }


    public int totalFruit(int[] fruits) {
        int left = 0;
        int right = 0;
        int treasLen = 0;
        HashMap<Integer, Integer> f = new HashMap<>();
        while (right < fruits.length) {
            if (f.get(fruits[right]) != null) {
                f.put(fruits[right], f.get(fruits[right]) + 1);
            } else {
                f.put(fruits[right], 1);
            }
            while (f.size() > 2) {
                f.put(fruits[left], f.get(fruits[left]) - 1);
                if (f.get(fruits[left]) == 0) {
                    f.remove(fruits[left]);
                }
                left++;
            }
            treasLen = Math.max(treasLen, right - left + 1);
            right++;
        }
        return treasLen;
    }


    public boolean isAnagram(String s, String t) {
        HashMap<String, Integer> result = new HashMap<>();
        String[] s_split = s.split("");
        String[] t_split = t.split("");
        if (s.length() != t.length()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            result.put(s_split[i], result.getOrDefault(s_split[i], 0) + 1);
            result.put(t_split[i], result.getOrDefault(t_split[i], 0) - 1);
            System.out.println(s_split[i] + ":" + result.getOrDefault(s_split[i], 0) + ":" + result);
        }

        for (String key : result.keySet()) {
            if (result.get(key) != 0) {
                return false;
            }
        }
        return true;
    }


    public boolean isAnagram1(String s, String t) {
        HashMap<Character, Integer> result = new HashMap<>();
        char[] s_chars = s.toCharArray();
        char[] t_chars = t.toCharArray();
        if (s.length() != t.length()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            result.put(s_chars[i], result.getOrDefault(s_chars[i], 0) + 1);
            result.put(t_chars[i], result.getOrDefault(t_chars[i], 0) - 1);
        }

        for (char key : result.keySet()) {
            if (result.get(key) != 0) {
                return false;
            }
        }
        return true;
    }


    public boolean isAnagram2(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        char[] s_chars = s.toCharArray();
        char[] t_chars = t.toCharArray();
        Arrays.sort(s_chars);
        Arrays.sort(t_chars);
        return Arrays.equals(s_chars, t_chars);
    }


    public boolean isAnagram3(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        int[] aToz = new int[26];
        for (int i = 0; i < s.length(); i++) {
            aToz[s.charAt(i) - 'a']++;
            aToz[t.charAt(i) - 'a']--;
        }
        for (int i = 0; i < aToz.length; i++) {
            if (aToz[i] != 0) {
                return false;
            }
        }
        return true;
    }


    public boolean containsDuplicate1(int[] nums) {
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            hTable.put(nums[i], hTable.getOrDefault(nums[i], 0) + 1);
        }
        for (Integer num : hTable.values()) {
            if (num > 1) {
                return true;
            }
        }
        return false;
    }

    public boolean containsDuplicate2(int[] nums) {
        Arrays.sort(nums);
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1]) {
                return true;
            }
        }
        return false;
    }

    public boolean containsDuplicate00(int[] nums) {

        Set<Integer> set = new HashSet<Integer>();
        for (int x : nums) {
            if (!set.add(x)) {
                return true;
            }
        }
        return false;

    }

    public boolean containsDuplicate3(int[] nums) {
        HashSet<Integer> hTable = new HashSet<>();
        for (int m : nums) {
            if (!hTable.add(m)) {
                return true;
            }
        }
        return false;
    }


    public int lengthOfLongestSubstring(String s) {
        HashMap<Character, Integer> hTable = new HashMap<>();
        int maxLength = 0;
        int left = 0;
        for (int right = 0; right < s.length(); right++) {
            if (hTable.containsKey(s.charAt(right))) {
                left = Math.max(left, hTable.get(s.charAt(right)) + 1);
            }
            hTable.put(s.charAt(right), right);
            maxLength = Math.max(maxLength, right - left + 1);
        }
        return maxLength;
    }


    public boolean checkInclusion00(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        if (n > m) {
            return false;
        }
        int[] cnt1 = new int[26];
        int[] cnt2 = new int[26];
        for (int i = 0; i < n; ++i) {
            ++cnt1[s1.charAt(i) - 'a'];
            ++cnt2[s2.charAt(i) - 'a'];
        }
        if (Arrays.equals(cnt1, cnt2)) {
            return true;
        }
        for (int i = n; i < m; ++i) {
            ++cnt2[s2.charAt(i) - 'a'];
            --cnt2[s2.charAt(i - n) - 'a'];
            if (Arrays.equals(cnt1, cnt2)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkInclusion(String s1, String s2) {
        int minLength = s1.length();
        int maxLength = s2.length();
        if (minLength > maxLength) {
            return false;
        }
        int[] s1Array = new int[26];
        int[] s2Array = new int[26];
        for (int i = 0; i < minLength; i++) {
            s1Array[s1.charAt(i) - 'a']++;
            s2Array[s2.charAt(i) - 'a']++;
        }
        if (Arrays.equals(s1Array, s2Array)) {
            return true;
        }
        for (int i = minLength; i < maxLength; i++) {
            s2Array[s2.charAt(i) - 'a']++;
            s2Array[s2.charAt(i - minLength) - 'a']--;
            if (Arrays.equals(s1Array, s2Array)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAnagram00(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        int[] sCharNums = new int[26];
        int[] tCharNums = new int[26];
        for (int i = 0; i < t.length(); i++) {
            sCharNums[s.charAt(i) - 'a']++;
            tCharNums[t.charAt(i) - 'a']++;
        }
        if (Arrays.equals(sCharNums, tCharNums)) {
            return true;
        }
        return false;
    }


    public int lengthOfLongestSubstring00(String s) {
        //记录不重复字串中各个字符的位置,{字符:index}
        HashMap<Character, Integer> charIndex = new HashMap<>();
        int left = 0;
        int maxLength = 0;
        //循环结束条件：滑动窗口右侧right顶格/到头
        for (int right = 0; right < s.length(); right++) {
            if (charIndex.containsKey(s.charAt(right))) {
                //如果滑动窗口右侧right遇到字串中已有字符，则将滑动窗口左侧移动至已有字符的右侧一位
                left = Math.max(left, charIndex.get(s.charAt(right)) + 1); //max的原因是left只能朝后跳
            }
            //无论将char写入哈希表，可覆盖/更新至最新位置***
            charIndex.put(s.charAt(right), right);
            maxLength = Math.max(maxLength, right - left + 1);
        }
        return maxLength;
    }


    /**
     * 1748. 唯一元素的和
     */
    public int sumOfUnique(int[] nums) {  //时间复杂度低
        int ans = 0;
        int[] counter = new int[101];
        for (int num : nums) {
            counter[num]++;
        }
        for (int i = 0; i < counter.length; i++) {
            if (counter[i] == 1)
                ans += i;
        }
        return ans;
    }


    public int sumOfUnique01(int[] nums) {   //时间复杂度高
        int ans = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int num : nums) {
            hTable.put(num, hTable.getOrDefault(num, 0) + 1);
        }
        ans = Arrays.stream(nums).filter(o -> hTable.get(o) == 1).sum();
        return ans;
    }

}


