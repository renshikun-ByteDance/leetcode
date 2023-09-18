package leetcode.algorithm;


import utils.pojo.ListNode;

import java.util.*;

//-----------------
// 模拟
//-----------------
public class Simulation {

    /**
     * 1. 两数之和
     */
    public int[] twoSum(int[] nums, int target) {  //暴力
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target)
                    return new int[]{i, j};
            }
        }
        return new int[]{-1, -1};
    }

    public int[] twoSum01(int[] nums, int target) {  //基于HashMap
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (hTable.containsKey(target - nums[i])) {
                return new int[]{hTable.get(target - nums[i]), i};
            }
            hTable.put(nums[i], i);  //先判断，后插入
        }
        return new int[]{-1, -1};
    }


    /**
     * 58. 最后一个单词的长度
     */
    public int lengthOfLastWord(String s) {
        String[] words = Arrays.stream(s.split(" "))
                .filter(t -> !t.equals(""))
                .toArray(String[]::new);
        int length = words.length;
        return words[length - 1].length();
    }

    public int lengthOfLastWord01(String s) {
        int pos = s.length() - 1;
        while (pos >= 0 && s.charAt(pos) == ' ')
            pos--;
        int right = pos;
        while (pos >= 0 && s.charAt(pos) != ' ')
            pos--;
        int left = pos;

        return right - left;
    }


    /**
     * 14. 最长公共前缀
     */
    public String longestCommonPrefix(String[] strs) {
        //两个两个比较
        String commonPrefix = strs[0];
        for (int i = 1; i < strs.length; i++) {
            commonPrefix = longestCommonPrefix(commonPrefix, strs[i]);
            if (commonPrefix.equals(""))
                return "";
        }
        return commonPrefix;
    }

    private String longestCommonPrefix(String str1, String str2) {
        StringBuilder commonPrefix = new StringBuilder();
        for (int i = 0; i < Math.min(str1.length(), str2.length()); i++) {
            if (str1.charAt(i) == str2.charAt(i))
                commonPrefix.append(str1.charAt(i));
            else
                return commonPrefix.toString();
        }
        return commonPrefix.toString();
    }



    /**
     * 14. 最长公共前缀
     */
    public String longestCommonPrefix01(String[] strs) {
        if (strs.length == 1) return strs[0];
        Arrays.sort(strs, (o1, o2) -> o1.length() - o2.length());
        int currIndex = 0;
        int n = strs[0].length();
        if (n == 0) return "";
        while (currIndex < n) {
            char target = strs[0].charAt(currIndex);
            for (int i = 1; i < strs.length; i++) {
                if (strs[i].charAt(currIndex) != target) {
                    return strs[0].substring(0, currIndex);
                }
            }
            currIndex++;
        }
        return strs[0].charAt(0) == strs[1].charAt(0) ? strs[0] : "";
    }


    /**
     * 38. 外观数列
     */
    public String countAndSay(int n) {
        String targetItem = "1";
        for (int i = 2; i <= n; i++) {
            targetItem = countAndSayByLast(targetItem);
        }
        return targetItem;
    }

    private String countAndSayByLast(String lastSentence) {
        if (lastSentence.length() == 1)
            return "11";
        StringBuilder target = new StringBuilder();
        char[] lastArrays = lastSentence.toCharArray();
        int left = 0;
        int right = 1;
        while (right <= lastArrays.length) {
            if ((right == lastArrays.length) || (lastArrays[right] != lastArrays[left])) {
                target.append(right - left)  //元素个数
                        .append(lastArrays[left]); //元素值
                left = right;
            }
            right++;
        }
        return target.toString();
    }


    /**
     * 66. 加一
     */
    public int[] plusOne(int[] digits) {
        int len = digits.length - 1;
        for (int i = len; i >= 0; i--) {
            if (digits[i] == 9) {
                digits[i] = 0;  //+1后变为0，进入下一轮循环，+1 相当于上一位进 1
            } else {            //+1后不为0，则+1消耗完了，直接返回
                digits[i]++;
                return digits;
            }
        }
        int[] target = new int[len + 2];
        target[0]++;
        return target;
    }


    /**
     * 268. 丢失的数字
     */
    public int missingNumber(int[] nums) {
        Arrays.sort(nums);
        int len = nums.length;
        if (nums[len - 1] != len)
            return len;
        for (int i = 0; i < len; i++) {
            if (nums[i] != i)
                return i;
        }
        return 0;
    }

    public int missingNumber00(int[] nums) {
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            if (i != nums[i])
                return i;
        }
        return nums.length;
    }


    /**
     * 383. 赎金信
     */
    public boolean canConstruct(String ransomNote, String magazine) {
        HashMap<Character, Integer> mTable = new HashMap<>();
        for (int i = 0; i < magazine.length(); i++) {
            mTable.put(magazine.charAt(i), mTable.getOrDefault(magazine.charAt(i), 0) + 1);
        }
        for (int i = 0; i < ransomNote.length(); i++) {
            if (mTable.getOrDefault(ransomNote.charAt(i), 0) == 0)
                return false;
            mTable.put(ransomNote.charAt(i), mTable.get(ransomNote.charAt(i)) - 1);
        }
        return true;
    }

    public boolean canConstruct01(String ransomNote, String magazine) {
        int[] hTable = new int[26];
        for (int i = 0; i < magazine.length(); i++) {
            hTable[magazine.charAt(i) - 'a']++;
        }
        for (int i = 0; i < ransomNote.length(); i++) {
            hTable[ransomNote.charAt(i) - 'a']--;
            if (hTable[ransomNote.charAt(i) - 'a'] < 0)
                return false;
        }
        return true;
    }


    /**
     * 6. Z 字形变换
     */
    public String convert(String s, int numRows) {
        if (numRows == 1)
            return s;
        int numColumns = s.length();  //最长的情况下
        Character[][] matrix = new Character[numRows][numColumns];
        //起点
        int row = 0;
        int column = 0;
        int pos = 0;
        //开始写入二维数组
        while (pos < s.length()) {
            //自上而下
            while (row + 1 <= numRows && pos < s.length()) {
                matrix[row][column] = s.charAt(pos);
                pos++;
                row++;
            }
            //跳动至下一个待写入的位置
            row = row - 2;
            column++;
            //左下至右上
            while (row >= 0 && pos < s.length()) {
                matrix[row][column] = s.charAt(pos);
                row--;
                column++;
                pos++;
            }
            row = row + 2;
            column--;
        }

        StringBuilder target = new StringBuilder();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j <= column; j++) {
                if (matrix[i][j] == null)
                    continue;
                target.append(matrix[i][j]);
            }
        }

        return target.toString();
    }


    /**
     * 71. 简化路径
     * 未通过的案例：String path = "/a/../../b/../c//.//";    //官方预期 "/c"，确定？
     */
    public String simplifyPath(String path) {
        StringBuilder simplify = new StringBuilder();
        String[] pathWords = path.split("/", -1);
        String[] vaildWords = new String[pathWords.length];
        int jump = 0;
        //倒着走
        for (int right = pathWords.length - 1; right >= 0; right--) {
            if (pathWords[right].equals("") || pathWords[right].equals("."))
                continue;
            if (pathWords[right].equals(".."))
                jump++;
            else {
                if (jump > 0) {
                    jump--;
                    continue;
                }
                vaildWords[right] = pathWords[right];
            }
        }
        if (jump > 0)    //跳出根路径，返回根路径
            return "/";
        for (int i = 0; i < vaildWords.length; i++) {
            if (vaildWords[i] != null)
                simplify.append("/").append(vaildWords[i]);
        }
        return simplify.toString();
    }


    /**
     * 73. 矩阵置零
     */
    public void setZeroes(int[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        boolean[] rowsZero = new boolean[rows];
        boolean[] columnsZero = new boolean[columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (matrix[i][j] == 0) {
                    rowsZero[i] = true;
                    columnsZero[j] = true;
                }
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (rowsZero[i] || columnsZero[j])
                    matrix[i][j] = 0;
            }
        }
    }

    public void setZeroes01(int[][] matrix) {
        HashSet<Integer> zeroRows = new HashSet<>();
        HashSet<Integer> zeroColumns = new HashSet<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    zeroRows.add(i);
                    zeroColumns.add(j);
                }
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (zeroRows.contains(i) || zeroColumns.contains(j))
                    matrix[i][j] = 0;
            }
        }
    }


    /**
     * 168. Excel表列名称
     */
    public String convertToTitle(int columnNumber) {
        //--------------------------------------------
        // 对于一般性的进制转换题目，只需要不断地对 columnNumber 进行 % 运算取得最后一位，然后对 columnNumber 进行 / 运算，将已经取得的位数去掉，直到 columnNumber 为 0 即可。
        // 一般性的进制转换题目无须进行额外操作，是因为我们是在「每一位数值范围在 [0,x)的前提下进行「逢 x 进一」。
        // 但本题需要我们将从 1 开始，因此在执行「进制转换」操作前，我们需要先对 columnNumber 执行减一操作，从而实现整体偏移。
        //--------------------------------------------


        //使用-1 熨平取模的问题。 因为x%26=[0,25] 但是题目对应关系为 [1-26] <==> [A-Z] 直接取模 26%26=0 不在我们范围内 所以我们采用-1 [0-25]对应 [A-Z] (26-1)%26 =25 25+'A'='Z'
        StringBuilder builder = new StringBuilder();
        while (columnNumber > 0) {
            columnNumber--; //左移一位
            builder.append((char) (columnNumber % 26 + 'A'));   //'A'本身就是 'A'，即取余结果为 0 的话对应 'A'
            columnNumber = columnNumber / 26;
        }
        builder.reverse();   //反转
        return builder.toString();
    }


    /**
     * 165. 比较版本号
     */
    public int compareVersion(String version1, String version2) {
        String[] ver1 = version1.split("\\.");
        String[] ver2 = version2.split("\\.");
        int maxLen = Math.max(ver1.length, ver2.length);
        int[] comVer1 = new int[maxLen];
        int[] comVer2 = new int[maxLen];
        for (int i = 0; i < maxLen; i++) {
            if (i < ver1.length)
                comVer1[i] = Integer.parseInt(ver1[i]);
            if (i < ver2.length)
                comVer2[i] = Integer.parseInt(ver2[i]);
        }
        for (int i = 0; i < maxLen; i++) {
            if (comVer1[i] > comVer2[i])
                return 1;
            if (comVer1[i] < comVer2[i])
                return -1;
        }
        return 0;
    }

    public int compareVersion01(String version1, String version2) {
        String[] ver1 = version1.split("\\.");
        String[] ver2 = version2.split("\\.");
        int maxLen = Math.max(ver1.length, ver2.length);
        for (int i = 0; i < maxLen; i++) {
            int v1 = 0;
            int v2 = 0;
            if (i < ver1.length)
                v1 = Integer.parseInt(ver1[i]);
            if (i < ver2.length)
                v2 = Integer.parseInt(ver2[i]);
            if (v1 != v2)
                return v1 > v2 ? 1 : -1;
        }
        return 0;
    }

    public int compareVersion02(String version1, String version2) {
        int[] convert1 = Arrays.stream(version1.split("\\.")).mapToInt(Integer::valueOf).toArray();
        int[] convert2 = Arrays.stream(version2.split("\\.")).mapToInt(Integer::valueOf).toArray();
        int maxLength = Math.max(convert1.length, convert2.length);
        int[] current1 = new int[maxLength];
        int[] current2 = new int[maxLength];
        System.arraycopy(convert1, 0, current1, 0, convert1.length);
        System.arraycopy(convert2, 0, current2, 0, convert2.length);
        for (int i = 0; i < maxLength; i++) {
            if (current1[i] > current2[i])
                return 1;
            if (current1[i] < current2[i])
                return -1;
        }
        return 0;
    }


    /**
     * 434. 字符串中的单词数
     */
    public int countSegments(String s) {
        long count = Arrays.stream(s.split(" "))
                .filter(t -> !t.equals(""))
                .count();
        return (int) count;
    }

    public int countSegments01(String str) {
        String[] arr = str.trim().split("\\s+");
        return arr.length == 1 && arr[0].equals("") ? 0 : arr.length;
    }




    /**
     * 495. 提莫攻击
     */
    public int findPoisonedDuration(int[] times, int duration) {
        int sumTimes = 0;
        int nextTime = times[0] + duration - 1;
        //------------------------------------------------
        // 每次均记录本次中毒结束的时间，将其与下次攻击的时间进行比较，从而计算本次攻击导致的中毒"净"时间
        //------------------------------------------------
        for (int i = 1; i < times.length; i++) {
            if (nextTime < times[i]) sumTimes += duration;
            else sumTimes += times[i] - times[i - 1];
            nextTime = times[i] + duration - 1;
        }
        sumTimes += duration;
        return sumTimes;
    }


    public int findPoisonedDuration00(int[] timeSeries, int duration) {  //暴力解法：超时
        int len = timeSeries.length;
        int last = timeSeries[len - 1];
        int[] timeAndPoisoned = new int[last + duration];
        for (int time : timeSeries) {
            int right = time;
            while (right <= time + duration - 1) {
                if (timeAndPoisoned[right] == 0)
                    timeAndPoisoned[right]++;
                right++;
            }
        }
        int sum = Arrays.stream(timeAndPoisoned)
                .filter(t -> t != 0)
                .sum();
        return sum;
    }

    //基于watermark水位线来判断，是否有交叉
    public int findPoisonedDuration01(int[] timeSeries, int duration) {
        int waterMark = -1;   //一定要是 -1
        int sum = 0;
        for (int time : timeSeries) {
            int nextWaterMark = time + duration - 1;
            if (time <= waterMark)
                sum += nextWaterMark - waterMark + 1 - 1;   //取差集
            else
                sum += duration;  //取间距
//                sum += nextWaterMark - time + 1 - 1;  //取间距
            waterMark = nextWaterMark;
        }
        return sum;
    }

    /**
     * 299. 猜数字游戏
     */
    public String getHint(String secret, String guess) {
        int[] secretArray = new int[10];
        int[] guessArray = new int[10];
        int x = 0;
        int y = 0;
        for (int i = 0; i < secret.length(); i++) {
            if (secret.charAt(i) == guess.charAt(i))
                x++;
            else {
                secretArray[secret.charAt(i) - '0']++;
                guessArray[guess.charAt(i) - '0']++;
            }
        }
        for (int i = 0; i < 10; i++) {
            y += Math.min(secretArray[i], guessArray[i]);
        }
        return x + "A" + y + "B";
    }


    /**
     * 54. 螺旋矩阵
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        ArrayList<Integer> target = new ArrayList<>();
        //纵坐标的范围
        int top = 0;
        int bottom = matrix.length - 1;
        //横坐标的范围
        int left = 0;
        int right = matrix[0].length - 1;
        //元素个数
        int times = 0;
        //
        int sums = matrix.length * matrix[0].length;
        while (times < sums) {
            for (int i = left; i <= right; i++) {
                target.add(matrix[top][i]);
                times++;
            }
            if (times == sums) break;
            top++;
            for (int i = top; i <= bottom; i++) {
                target.add(matrix[i][right]);
                times++;
            }
            if (times == sums) break;
            right--;
            for (int i = right; i >= left; i--) {
                target.add(matrix[bottom][i]);
                times++;
            }
            if (times == sums) break;
            bottom--;
            for (int i = bottom; i >= top; i--) {
                target.add(matrix[i][left]);
                times++;
            }
            if (times == sums) break;
            left++;
        }
        return target;
    }

    public List<Integer> spiralOrder01(int[][] matrix) {
        ArrayList<Integer> ans = new ArrayList<>();
        //坐标点
        int rows = 0;
        int cols = 0;
        //四个边界
        int left = 0;                      //列：左边界
        int right = matrix[0].length - 1;  //列：右边界
        int top = 0;                       //行：上边界
        int bottom = matrix.length - 1;    //行：下边界
        int times = matrix[0].length * matrix.length;  //元素总个数
        int current = 1; //当前元素个数
        while (current <= times) {
            //从左往右
            while (cols <= right && current <= times) {
                ans.add(matrix[rows][cols]);
                cols++;
                current++;
            }
            cols--; //列：左移（回退）
            rows++; //行：下移
            top++;  //上边界：行下移

            //从上往下
            while (rows <= bottom && current <= times) {
                ans.add(matrix[rows][cols]);
                rows++;
                current++;
            }
            rows--;  //行：上移
            cols--;  //列：左移
            right--; //右边界：列左移

            //从右往左
            while (cols >= left && current <= times) {
                ans.add(matrix[rows][cols]);
                cols--;
                current++;
            }
            cols++;   //列：右移
            rows--;   //行：上移
            bottom--; //下边界：行上移

            //从下往上
            while (rows >= top && current <= times) {
                ans.add(matrix[rows][cols]);
                rows--;
                current++;
            }
            rows++;   //行：下移
            cols++;   //列：右移
            left++;   //左边界：列右移
        }
        return ans;
    }


    /**
     * 59. 螺旋矩阵 II
     */
    public int[][] generateMatrix(int n) {
        int[][] spiralMatrix = new int[n][n];
        int left = 0;
        int right = n - 1;
        int top = 0;
        int bottom = n - 1;
        int k = 1;
        while (k <= n * n) {
            //横向：从左至右，top行固定 + 列遍历
            for (int j = left; j <= right; j++) {
                spiralMatrix[top][j] = k;
                k++;
            }
            top++; //将已使用的边界剔除
            if (k > n * n) break;
            //纵向：从上至下，tight列固定 + 行遍历
            for (int i = top; i <= bottom; i++) {
                spiralMatrix[i][right] = k;
                k++;
            }
            right--; //将已使用的边界剔除
            if (k > n * n) break;
            //横向：从右至左，bottom行固定 + 列遍历
            for (int j = right; j >= left; j--) {
                spiralMatrix[bottom][j] = k;
                k++;
            }
            bottom--;
            if (k > n * n) break;
            //纵向：从下至上，left列固定 + 行遍历
            for (int i = bottom; i >= top; i--) {
                spiralMatrix[i][left] = k;
                k++;
            }
            left++;
            if (k > n * n) break;
        }
        return spiralMatrix;
    }


    /**
     * 492. 构造矩形
     * 备注：成就感很高的一道题，一把过，而且和标准答案写的几乎一致，从中间向两端计算，遇到的第一个即为目标答案
     */
    public int[] constructRectangle(int area) {
        double sqrt = Math.sqrt(area);
        int sqrtArea = (int) (sqrt + 1);
        for (int t = sqrtArea; t >= 0; t--) {   //从中间向两边计算
            if (area % t == 0) {                //第一个满足条件的场景，即为目标值
                int m = area / t;
                return m >= t ? new int[]{m, t} : new int[]{t, m};
            }
        }
        return new int[]{-1, -1};
    }


    /**
     * 31. 下一个排列
     */
    public void nextPermutation(int[] nums) {
        int right = nums.length - 1;
        for (int i = right; i > 0; i--) {
            if (nums[i] > nums[i - 1]) {
                //-------------------------------------------------------------------------------------------
                // 从右至左，找到第一个 “塔尖”，由于是第一个，故 “塔尖”左侧一位pos一定小于塔尖top，右侧区间从左至右一定为降序
                // 具体分析：
                //   “塔尖”右侧当前一定是最大值，右侧元素无法通过位置的移动，使得右侧区间值更大，故需要通过调整pos位置和 “塔尖”右侧区间元素的值，使得整体提升
                // 要做得共有两个事：
                //    1、在“塔尖”右侧降序区间，找到大于"pos"的最小值，用于交换两者的位置，将 “坑pos”填补
                //    2、将“塔尖”右侧降序区间，变为升序区间，使得越小的数越接近“pos”底，使得pos位置右侧为区间最小值
                //-------------------------------------------------------------------------------------------
                Arrays.sort(nums, i, nums.length);
                int pos = i;
                while (pos < nums.length) {
                    if (nums[pos] > nums[i - 1]) { //已经排完序后的结果，从左至右，第一个一定是大于pos的最小值
                        int temp = nums[pos];
                        nums[pos] = nums[i - 1];
                        nums[i - 1] = temp;
                        return;  //程序结束
                    }
                    pos++;
                }
            }
        }
        Arrays.sort(nums);  //整体为降序，则重新排序返回
    }


    /**
     * 43. 字符串相乘
     * 模拟：做乘法
     */
    public String multiply(String num1, String num2) {
        int m = num1.length();
        int n = num2.length();
        //维护多次乘积的累加状态
        int[] result = new int[m + n];
        for (int i = m - 1; i >= 0; i--) {
            //每次迭代，创建一次
            int a = num1.charAt(i) - '0';
            for (int j = n - 1; j >= 0; j--) {
                //内循环，每次创建遍历到的元素值
                int b = num2.charAt(j) - '0';
                int res = a * b;
                //当前位的值需要与上一轮进位结果相加
                res += result[i + j + 1];
                //当前位的值，看是否要进位
                result[i + j + 1] = res % 10;
                result[i + j] += res / 10;     //加号
            }
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            if (builder.length() == 0 && result[i] == 0) continue;
            builder.append(result[i]);
        }
        return builder.length() == 0 ? "0" : builder.toString();
    }

    /**
     * 43. 字符串相乘
     * 模拟：做加法
     */
    public String multiply01(String num1, String num2) {
        int m = num1.length();
        int n = num2.length();
        if (num1.equals("0") || num2.equals("0"))
            return "0";
        //维护多次乘积的累加状态
        String result = "0";
        for (int i = m - 1; i >= 0; i--) {
            int plus = 0;   //必须在里面，每次循环重新置零
            //每次迭代，创建一次
            int a = num1.charAt(i) - '0';
            StringBuilder builder = new StringBuilder();
            int appendTimes = m - 1 - i;
            while (appendTimes > 0) {   //补零
                builder.append(0);
                appendTimes--;
            }
            for (int j = n - 1; j >= 0; j--) {
                //内循环，每次创建遍历到的元素值
                int b = num2.charAt(j) - '0';
                int res = a * b + plus;   //当前位的值需要与上一轮进位结果相加
                //当前位的值，看是否要进位
                builder.append(res % 10);
                plus = res / 10;     //加号
            }
            if (plus != 0)
                builder.append(plus);
            result = addStrings(result, builder.reverse().toString());
        }
        return result;
    }


    /**
     * 415. 字符串相加
     */
    public String addStrings(String num1, String num2) {
        StringBuilder ans = new StringBuilder();
        int index1 = num1.length() - 1;
        int index2 = num2.length() - 1;
        int add = 0;
        while (index1 >= 0 || index2 >= 0 || add > 0) {
            int xx = index1 >= 0 ? num1.charAt(index1) - '0' : 0;   //此处为减字符 '0'
            int yy = index2 >= 0 ? num2.charAt(index2) - '0' : 0;   //此处为减字符 '0'
            int sum = xx + yy + add;
            ans.append(sum % 10);
            add = sum / 10;
            index1--;
            index2--;
        }
        return ans.reverse().toString();
    }

    public String addStrings01(String num1, String num2) {
        StringBuilder builder = new StringBuilder();
        int plus = 0;
        int right_1 = num1.length() - 1;
        int right_2 = num2.length() - 1;
        int times = Math.max(right_1, right_2);
        while (times >= 0) {
            int a = 0;
            int b = 0;
            if (right_1 >= 0)
                a = num1.charAt(right_1--) - '0';
            if (right_2 >= 0)
                b = num2.charAt(right_2--) - '0';
            int res = a + b + plus;
            builder.append(res % 10);
            plus = res / 10;
            times--;
        }
        if (plus != 0)
            builder.append(plus);
        return builder.reverse().toString();
    }


    /**
     * 747. 至少是其他数字两倍的最大数
     */
    public int dominantIndex(int[] nums) {
        int len = nums.length;
        if (len == 1) return 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++)
            hTable.put(nums[i], i);
        Arrays.sort(nums);
        if (nums[len - 1] >= 2 * nums[len - 2])
            return hTable.get(nums[len - 1]);
        return -1;
    }


    /**
     * 500. 键盘行
     */
    public String[] findWords(String[] words) {
        ArrayList<String> res = new ArrayList<>();
        String row1 = "qwertyuiop";
        String row2 = "asdfghjkl";
        String row3 = "zxcvbnm";
        for (String word : words) {
            String lowerWord = word.toLowerCase();
            if (isContain(lowerWord, row1) || isContain(lowerWord, row2) || isContain(lowerWord, row3))
                res.add(word);
        }
        return res.toArray(new String[]{});
    }

    private boolean isContain(String word, String target) {
        String[] chars = word.split("");
        for (int i = 0; i < word.length(); i++) {
            if (!target.contains(chars[i]))
                return false;
        }
        return true;
    }

    //---------------------------------------------
    // 转换行号，将在同一行的字母，给定同一个数字标识，并存储在Hash表中
    //     因此，row1、row2、row3仅访问一遍，后续仅需要从Hash表中O(1)的获取字母对应的行号
    //---------------------------------------------
    public String[] findWords01(String[] words) {
        HashMap<Character, Integer> hTable = new HashMap<>();
        String row1 = "qwertyuiop";
        String row2 = "asdfghjkl";
        String row3 = "zxcvbnm";
        for (int i = 0; i < row1.length(); i++)
            hTable.put(row1.charAt(i), 1);
        for (int i = 0; i < row2.length(); i++)
            hTable.put(row2.charAt(i), 2);
        for (int i = 0; i < row3.length(); i++)
            hTable.put(row3.charAt(i), 3);
        StringBuilder res = new StringBuilder();
        for (String word : words) {
            String lowerWord = word.toLowerCase();
            //单词第一个字母所在的行号
            Integer row = hTable.get(lowerWord.charAt(0));
            //单词后面每个字母所在的行号是否与此row一致
            boolean isContain = true;
            for (int i = 1; i < lowerWord.length(); i++) {
                if (!hTable.get(lowerWord.charAt(i)).equals(row)) {
                    isContain = false;
                    break;
                }
            }
            if (isContain)
                res.append(word).append(",");
        }
        if (res.length() != 0)
            return res.toString().split(",");
        return new String[]{};
    }


    /**
     * 541. 反转字符串 II
     */
    public String reverseStr(String s, int k) {
        StringBuilder res = new StringBuilder();
        int right = 0;
        while (right < s.length()) {
            int t = k;
            StringBuilder builder = new StringBuilder();
            //第一轮：反转
            while (t > 0 && right < s.length()) {
                builder.append(s.charAt(right));
                right++;
                t--;
            }
            res.append(builder.reverse());  //时间复杂度略高，因为要先遍历一遍后，再反转
            t = k;
            //第二轮：不反转
            while (t > 0 && right < s.length()) {
                res.append(s.charAt(right));
                right++;
                t--;
            }
        }
        return res.toString();
    }

    //-----------------------------------------------------
    // 时间复杂度低，偶数区间无需遍历，奇数区间直接反转，时间复杂度不到 Q(n)
    //-----------------------------------------------------
    public String reverseStr01(String s, int k) {
        int pos = 0;
        char[] arr = s.toCharArray();
        while (pos < s.length()) {
            //在pos的基础上，无论区间 [pos, pos + k ]
            //在pos的基础上，向前移动 K，不满 K则移动至结尾，对这部分字串进行反转
            reverse(arr, pos, Math.min(pos + k, arr.length) - 1);
            //在pos的基础上，无论区间 [pos + k , pos + 2k]是否完整，对此均不处理
            pos += 2 * k;
        }
        return String.copyValueOf(arr);
    }

    private void reverse(char[] arr, int left, int right) {
        while (left < right) {
            char temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }


    /**
     * 551. 学生出勤记录 I
     */
    public boolean checkRecord(String s) {
        if (s.contains("LLL")) //连续超过三个，就包含了三个的情况，直接判断
            return false;
        int times = 0;         //记录A出现的次数
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'A')
                times++;
            if (times == 2)
                return false;
        }
        return true;
    }


    /**
     * 693. 交替位二进制数
     */
    public boolean hasAlternatingBits(int n) {
        int a = n ^ (n >> 1);  //异或运算符:  对两个整型操作数中对应位执行布尔代数，两个位相等则为 0，不相等则为 1
        return (a & (a + 1)) == 0;   //与运算符: 对两个整型操作数中对应位执行布尔代数，两个位都为 1时输出 1，否则 0
    }

    public boolean hasAlternatingBits01(int n) {
        int prev = -1;
        while (n != 0) {
            int curr = n % 2;
            if (curr == prev)
                return false;
            prev = curr;
            n /= 2;
        }
        return true;
    }

    public int missingNumber0(int[] nums) {
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            if (i != nums[i])
                return i;
        }
        return nums.length;
    }


    /**
     * 1583. 统计不开心的朋友
     */
    public int unhappyFriends(int n, int[][] preferences, int[][] pairs) {
        int ans = 0;
        int[][] orders = new int[n][n];  //不能是 int[n][n-1] ，因为 preferences[i][j] 会等于 n
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                // 二维数组，每人与其他人的亲密程度用一行来表示，值越小越亲密
                orders[i][preferences[i][j]] = j;   //每个里面有两个 0，一个是自己，另一个是最亲密的
                // 下面取得时候会不会拿到自己？？从而增加不必要得值************
                // preferences[i][j]，含义是对于 i 其亲密程度第 j 位的是 preferences[i][j]
                // orders[i][j]，含义是对于 i ,其对 j 的亲密程度是 orders[i][j]（值越小越亲密）
            }
        }
        //匹配结果
        int[] match = new int[n];
        for (int[] pair : pairs) {
            match[pair[0]] = pair[1];   //pair[0]搭配 pair[1]
            match[pair[1]] = pair[0];   //pair[1]搭配 pair[0]
        }
        for (int i = 0; i < n; i++) {
            //--------------------------------------
            // 依次检查每个人 i 的搭配情况
            //    1、根据 i 搭配的人 match[i] 获取 i 对 match[i] 的亲密程度 index
            //    2、对于 i 查询与其亲密程度 小于 index的人的搭配情况
            //--------------------------------------
            int maxIndex = orders[i][match[i]];    // i当前搭配的是 match[i]，故要从 i的亲密列表中找到此人的位置（亲密程度）
            // 故从 i的亲密列表中（亲密程度截至当前搭配人的亲密程度 maxIndex）
            for (int j = 0; j < maxIndex; j++) {   // maxIndex是亲密程度，而不是个人
                int u = preferences[i][j];         // 对于 i 其亲密程度小于 maxIndex的人 u
                int v = match[u];                  // 获取 u 的搭配情况
                if (orders[u][i] < orders[u][v]) { // 前提：当前 i 对 u更加亲密，如果此时 u 对 i的亲密程度值"小于"对当前搭档的亲密程度，则 u 对 i 更加亲密
                    //因此，u 与 i 就是不开心的
                    ans++;
                    break;
                }
            }
        }
        return ans;
    }


    public int unhappyFriends01(int n, int[][] preferences, int[][] pairs) {
        int ans = 0;
        int[][] close_level = new int[n][n];
        int[] match = new int[n];
        //----------------------------------------------------------------------------------
        // 将 preferences二级索引与 value互换，从而通过 close_level[x][y] 直接获取其亲密程度的值
        //----------------------------------------------------------------------------------
        for (int i = 0; i < preferences.length; i++) {
            for (int j = 0; j < preferences[0].length; j++) {
                // 值越小，二者越亲密
                close_level[i][preferences[i][j]] = j;  //互换 二级索引 与 value
            }
        }
        //-------------------------------------------------------------------------------------------------
        // 基于 pairs获取 match，从而快速获取搭档关系，因为 pairs一级索引无意义、二级索引与 value也仅是单向，不方便
        //-------------------------------------------------------------------------------------------------
        for (int i = 0; i < pairs.length; i++) {
            for (int j = 0; j < pairs[0].length; j++) {
                match[pairs[i][0]] = pairs[i][1];
                match[pairs[i][1]] = pairs[i][0];
            }
        }
        //---------------------------------------------------------------------------
        // 上面仅仅是数据预处理，方便获取对应的数据，同时可以基于 match来逐一验证搭配的情况
        //---------------------------------------------------------------------------
        for (int x = 0; x < match.length; x++) {  //逐一验证每个人"x"是否是不开心的
            int y = match[x];  //获取当前 x 的搭档 y
            int maxClose = close_level[x][y];  //获取 x 对 y 的亲密程度
            for (int close = 0; close < maxClose; close++) { //基于递减的亲密程度，逐个获取相对于 y来讲，x更亲密的朋友 u
                //------------------------------------------------
                // 对于 x 来讲，u 比 y更受欢迎，剩下校验 u 的搭配情况
                //------------------------------------------------
                int u = preferences[x][close]; //
                int v = match[u];
                if (close_level[u][x] < close_level[u][v]) {
                    ans++;
                    break;
                }
            }
        }
        return ans;
    }


    /**
     * 49. 字母异位词分组
     */
    public List<List<String>> groupAnagrams(String[] strs) { //错误写法
        List<List<String>> ans = new ArrayList<>();
        HashMap<String, ArrayList<String>> hTable = new HashMap<>();
        for (String word : strs) {
            char[] array = word.toCharArray();
            Arrays.sort(array);
            String key = String.valueOf(array);
            ArrayList<String> value = hTable.getOrDefault(key, new ArrayList<>());
            value.add(word);
            hTable.put(key, value);
        }
        return new ArrayList<>(hTable.values());
    }


    public List<List<String>> groupAnagrams01(String[] strs) { //错误写法
        List<List<String>> ans = new ArrayList<>();
        HashMap<int[], ArrayList<String>> hTable = new HashMap<>();
        for (String word : strs) {
            char[] wordArray = word.toCharArray();
            int[] temp = new int[26];
            for (char ca : wordArray) {
                temp[ca - 'a']++;
            }
            if (hTable.containsKey(temp)) {   //无法捕获已有数组
                ArrayList<String> strings = hTable.get(temp);
                strings.add(word);
                hTable.put(temp, strings);
            } else {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(word);
                hTable.put(temp, arrayList);
            }
        }
        for (Map.Entry<int[], ArrayList<String>> tuple : hTable.entrySet()) {
            ans.add(tuple.getValue());
        }
        return ans;
    }


    public List<List<String>> groupAnagrams02(String[] strs) { //错误写法的改进
        List<List<String>> ans = new ArrayList<>();
        HashMap<String, ArrayList<String>> hTable = new HashMap<>();
        for (String word : strs) {
            char[] wordArray = word.toCharArray();
            int[] temp = new int[26];
            for (char ca : wordArray) {
                temp[ca - 'a']++;
            }
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 26; i++) {
                if (temp[i] != 0)
                    builder.append((char) (i + 'a')).append(temp[i]);  //a2的形式
            }
            String key = builder.toString();
            ArrayList<String> value = hTable.getOrDefault(key, new ArrayList<>());
            value.add(word);
            hTable.put(key, value);
        }
        return new ArrayList<>(hTable.values());
    }

    public List<List<String>> groupAnagrams03(String[] strs) {  //错误写法，如果单个字符串中有重复字符，则会错误
        HashMap<Integer, ArrayList<String>> map = new HashMap<>();
        for (String str : strs) {
            int digit = 0;
            for (int i = 0; i < str.length(); i++) {
                int nums = str.charAt(i) - 'a';
                digit |= (1 << nums);
            }
            ArrayList<String> arr = map.getOrDefault(digit, new ArrayList<>());
            arr.add(str);
            map.put(digit, arr);
        }
        List<List<String>> ans = new ArrayList<>();
        for (int digit : map.keySet()) {
            ans.add(map.get(digit));
        }
        return ans;
    }


    /**
     * 56. 合并区间
     */
    public int[][] merge(int[][] intervals) {
        if (intervals.length == 1)
            return intervals;
        ArrayList<int[]> ans = new ArrayList<>();
        Arrays.sort(intervals, (o1, o2) -> {
            if (o1[0] != o2[0]) {
                return o1[0] - o2[0];
            }
            return o1[1] - o2[1];
        });
        for (int i = 0; i < intervals.length - 1; i++) {
            if (intervals[i][1] < intervals[i + 1][0]) {
                ans.add(intervals[i]);    //无重叠
                if (i == intervals.length - 2)
                    ans.add(intervals[i + 1]);
            } else {  //有重叠
                int[] overlapping = {intervals[i][0], Math.max(intervals[i][1], intervals[i + 1][1])};
                intervals[i] = new int[]{-1, -1};
                intervals[i + 1] = overlapping; //更新
            }
            //忽略 [2,6] 和 [3,5] 双边重叠的情况
        }
        int count = (int) Arrays.stream(intervals).filter(o -> o[0] != -1).count();
        int[][] res = new int[count][2];
        int curr = 0;
        for (int[] interval : intervals) {
            if (interval[0] != -1) {
                res[curr] = interval;
                curr++;
            }
        }
        return res;
    }

    public int[][] merge01(int[][] intervals) {
        if (intervals.length == 1)
            return intervals;
        ArrayList<int[]> ans = new ArrayList<>();
        Arrays.sort(intervals, (o1, o2) -> o1[0] - o2[0]);
        for (int[] interval : intervals) {
            if (ans.size() == 0 || ans.get(ans.size() - 1)[1] < interval[0]) {  //无重叠
                ans.add(interval);
            } else { //有重叠
                int[] last = ans.get(ans.size() - 1);
                last[1] = Math.max(last[1], interval[1]);
                ans.remove(ans.size() - 1);  //更新
                ans.add(last);
            }
        }
        return ans.toArray(new int[ans.size()][]);
    }

    /**
     * 57. 插入区间
     */
    public int[][] insert(int[][] intervals, int[] newInterval) {
        ArrayList<int[]> ans = new ArrayList<>();
        int len = intervals.length;
        int i = 0;
        //不重叠的情况
        while (i < len && intervals[i][1] < newInterval[0]) { //newInterval区间 均处于 intervals[i] 的右侧
            ans.add(intervals[i]);
            i++;
        }
        //重叠的情况
        while (i < len && intervals[i][0] <= newInterval[1]) { //横跨多个，newInterval逐渐扩展（更新）区间
            newInterval[0] = Math.min(intervals[i][0], newInterval[0]);
            newInterval[1] = Math.max(intervals[i][1], newInterval[1]);
            i++;
        }
        //添加最终的重叠区间
        ans.add(newInterval);
        //后续逐一添加，不会有重叠
        for (int m = i; m < len; m++) {
            ans.add(intervals[m]);
        }
        return ans.toArray(new int[ans.size()][]);
    }


    public int[][] insert01(int[][] intervals, int[] newInterval) {
        ArrayList<int[]> ans = new ArrayList<>();
        if (intervals.length == 0) return new int[][]{newInterval};
        boolean neverUsed = true;
        for (int i = 0; i < intervals.length; i++) {
            if (neverUsed) {
                if (intervals[i][1] < newInterval[0])  //1、interval[i] 完全在 newInterval左侧
                    ans.add(intervals[i]);
                else {
                    if (intervals[i][0] <= newInterval[1]) {  //2、interval[i] 与 newInterval有交集
                        int left = Math.min(intervals[i][0], newInterval[0]);
                        int right = Math.max(intervals[i][1], newInterval[1]); //合并
                        ans.add(new int[]{left, right});
                    } else {                               //3、interval[i] 完全在 newInterval 右侧
                        ans.add(newInterval); //均添加
                        ans.add(intervals[i]);
                    }
                    neverUsed = false;  //newInterval已经合并使用，但合并结果可能会与后续的重叠，以后均执行以下分支
                }
            } else {
                if (ans.get(ans.size() - 1)[1] < intervals[i][0]) { //ans中最后的数组区间，与 intervals[i] 无重叠
                    ans.add(intervals[i]);
                } else { //有重叠
                    int[] last = ans.get(ans.size() - 1);
                    last[1] = Math.max(last[1], intervals[i][1]);
                    ans.remove(ans.size() - 1);
                    ans.add(last);
                }
            }
        }
        if (neverUsed) ans.add(newInterval);  //newInterval在区间右侧，从未被使用，此时应该更新
        return ans.toArray(new int[ans.size()][]);
    }


    public int[][] insert02(int[][] intervals, int[] newInterval) {
        ArrayList<int[]> ans = new ArrayList<>();
        if (intervals.length == 0) return new int[][]{newInterval};
        for (int i = 0; i < intervals.length; i++) {
            if (newInterval[0] != -2) {
                if (intervals[i][1] < newInterval[0])  // 判断是否进行 interval[i] 与 newInterval合并
                    ans.add(intervals[i]);
                else {
                    if (intervals[i][0] <= newInterval[1]) {  // interval[i] 与 newInterval有交集
                        int left = Math.min(intervals[i][0], newInterval[0]);
                        int right = Math.max(intervals[i][1], newInterval[1]);
                        ans.add(new int[]{left, right});
                    } else {
                        ans.add(newInterval);
                        ans.add(intervals[i]);
                    }
                    newInterval[0] = -2;   //.......上面已经插入，但此处更新后，ans中的也会更新，注意.......
                }
            } else {
                if (ans.get(ans.size() - 1)[1] < intervals[i][0]) { //无重叠
                    ans.add(intervals[i]);
                } else { //有重叠
                    int[] last = ans.get(ans.size() - 1);
                    last[1] = Math.max(last[1], intervals[i][1]);
                    ans.remove(ans.size() - 1);
                    ans.add(last);
                }
            }
        }
        return ans.toArray(new int[ans.size()][]);
    }


    /**
     * 557. 反转字符串中的单词 III
     */
    public String reverseWords(String str) {
        StringBuilder builder = new StringBuilder();
        int current = 0;
        while (current < str.length()) {
            StringBuilder append = new StringBuilder();
            while (current < str.length() && str.charAt(current) != ' ') {
                append.append(str.charAt(current));
                current++;
            }
            builder.append(append.reverse());
            if (current < str.length() && str.charAt(current) == ' ') {
                builder.append(" ");
            }
            current++;
        }
        return builder.toString();
    }


    /**
     * 482. 密钥格式化
     */
    public String licenseKeyFormatting(String str, int k) {
        StringBuilder builder = new StringBuilder();
        int currentIndex = str.length() - 1;
        while (currentIndex >= 0) {
            int nums = 0;
            while (currentIndex >= 0 && nums < k) {
                if (str.charAt(currentIndex) != '-') {
                    char xx = str.charAt(currentIndex);
                    if (Character.isLetter(xx)) {
                        xx = Character.toUpperCase(xx);
                    }
                    builder.append(xx);
                    nums++;
                }
                currentIndex--;
            }
            builder.append('-');  //无论因为什么条件导致 while 结束，此处都添加
        }
        String target = builder.reverse().toString();
        while (target.startsWith("-")) {     // 用 while ,因为有 "--a-a-a-a--"
            target = target.substring(1);
        }
        while (target.endsWith("-")) {
            target = target.substring(0, target.length() - 1);
        }
        return target;
    }


    public String licenseKeyFormatting00(String str, int k) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char xx = str.charAt(i);
            if (xx == '-')
                continue;
            if (Character.isLetter(xx)) {
                xx = Character.toUpperCase(xx);
            }
            builder.append(xx);
        }
        int up = builder.toString().length() % k;
        int carry = (builder.toString().length() / k) - 1;
        char[] array = builder.toString().toCharArray();
        StringBuilder target = new StringBuilder();
        int currentIndex = 0;
        while (currentIndex < array.length) {
            while (currentIndex < array.length && up > 0) {   //先处理前几位
                target.append(array[currentIndex]);
                up--;
                currentIndex++;
                if (up == 0 && currentIndex != array.length) {  // currentIndex != array.length 也要判断，防止 "2",2
                    target.append('-');
                }
            }
            int count = k;
            while (currentIndex < array.length && count > 0) {
                target.append(array[currentIndex]);
                currentIndex++;
                count--;
            }
            if (carry > 0) {
                target.append('-');
                carry--;
            }
        }
        return target.toString();
    }


    public String licenseKeyFormatting01(String s, int k) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c == '-') {
                continue;
            }
            sb.append((c >= 'a' && c <= 'z') ? (char) (c - 32) : c);
        }
        int len = sb.length() - k;
        while (len > 0) {
            sb.insert(len, "-");
            len -= k;
        }
        return sb.toString();
    }


    /**
     * 7. 整数反转
     */
    public int reverse(int x) {
        StringBuilder ans = new StringBuilder();
        try {
            //基于抛出异常的方法，来避免 int 溢出
            Integer result = Integer.valueOf(ans.append(Math.abs(x)).reverse().toString());
            return x > 0 ? result : -result;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int reverse01(int x) {
        int ans = 0;
        while (x != 0) {
            int tmp = x % 10;
            //提前判断正数溢出
            if (ans > Integer.MAX_VALUE / 10 || (ans == Integer.MAX_VALUE / 10 && tmp > 7)) {
                return 0;
            }
            //提前判断负数溢出
            if (ans < Integer.MIN_VALUE / 10 || (ans == Integer.MIN_VALUE / 10 && tmp < -8)) {
                return 0;
            }
            ans = ans * 10 + tmp;   //反转核心，将最后一位 放置 最后一位（后续会乘 10向上推）
            x /= 10;
        }
        return ans;
    }


    public int reverse02(int x) {
        int ans = 0;
        while (x != 0) {
            int tmp = x % 10;
            //提前判断溢出
            if (ans > Integer.MAX_VALUE / 10 || ans < Integer.MIN_VALUE / 10) {
                //因为 x本身会被int限制，当 x为正数并且位数和 Integer.MAX_VALUE的位数相等时首位最大只能为2
                //所以逆转后不会出现 res = Integer.MAX_VALUE / 10 && tmp > 2的情况，自然也不需要判断res==214748364 && tmp > 7了
                //反之负数情况也一样
                return 0;
            }
            ans = ans * 10 + tmp;   //反转核心，将最后一位 放置 最后一位（后续会乘 10向上推）
            x /= 10;
        }
        return ans;
    }

    /**
     * 1582. 二进制矩阵中的特殊位置
     */
    public int numSpecial(int[][] mat) {   //自己写的，和标准答案一摸一样
        int ans = 0;
        int[] rowsTotal = new int[mat.length];
        int[] colsTotal = new int[mat[0].length];
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                rowsTotal[i] += mat[i][j];
                colsTotal[j] += mat[i][j];
            }
        }
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                if (mat[i][j] == 1 && rowsTotal[i] == 1 && colsTotal[j] == 1)
                    ans++;
            }
        }
        return ans;
    }


    /**
     * 748. 最短补全词
     */
    public String shortestCompletingWord(String licensePlate, String[] words) {
        int[] target = new int[26];
        for (int i = 0; i < licensePlate.length(); i++) {
            if (Character.isLetter(licensePlate.charAt(i))) {
                target[Character.toLowerCase(licensePlate.charAt(i)) - 'a']++;
            }
        }
        wordAndIndex[] sortedQueue = new wordAndIndex[words.length];
        for (int i = 0; i < words.length; i++) {
            sortedQueue[i] = new wordAndIndex(words[i], i);
        }
        Arrays.sort(sortedQueue, (o1, o2) -> {
            return o1.word.length() != o2.word.length() ? o1.word.length() - o2.word.length() : o1.index - o2.index;
        });
        for (wordAndIndex next : sortedQueue) {
            String word = next.word;
            int[] current = new int[26];
            for (int i = 0; i < word.length(); i++) {
                current[word.charAt(i) - 'a']++;
            }
            boolean isTarget = true;
            for (int i = 0; i < 26; i++) {
                if (target[i] > current[i]) {
                    isTarget = false;
                    break;
                }
            }
            if (isTarget) return word;
        }
        return "0";
    }

    private class wordAndIndex {
        String word;
        Integer index;

        public wordAndIndex(String word, Integer index) {
            this.word = word;
            this.index = index;
        }
    }


    /**
     * 1694. 重新格式化电话号码
     */
    public String reformatNumber(String number) {
        StringBuilder ans = new StringBuilder();
        number = number.replaceAll(" ", "").replaceAll("-", "");
        int xx = number.length() % 3;
        int num = number.length() / 3;
        int currentIndex = 0;
        while (num > 0) {
            int time = 3;
            while (time > 0) {
                ans.append(number.charAt(currentIndex));
                time--;
                currentIndex++;
            }
            ans.append("-");
            num--;
        }
        if (xx == 0 || xx == 1) {
            ans.deleteCharAt(ans.lastIndexOf("-"));
        }
        if (xx == 1) {
            String base = ans.substring(0, ans.length() - 1);
            String ch = ans.substring(ans.length() - 1);
            return base + "-" + ch + number.charAt(currentIndex);
        }
        if (xx == 2) {
            while (xx > 0) {
                ans.append(number.charAt(currentIndex));
                currentIndex++;
                xx--;
            }
        }
        return ans.toString();
    }


    /**
     * 1790. 仅执行一次字符串交换能否使两个字符串相等
     */
    public boolean areAlmostEqual(String s1, String s2) {
        int currentIndex = 0;
        char[] xx = new char[2];
        char[] yy = new char[2];
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                if (currentIndex == 2) return false;
                xx[currentIndex] = s1.charAt(i);
                yy[currentIndex] = s2.charAt(i);
                if (currentIndex == 1 && (xx[0] != yy[1] || xx[1] != yy[0])) return false;
                currentIndex++;
            }
        }
        return currentIndex != 1;  //判断是否仅有一个字符不同
    }


    /**
     * 6207. 统计定界子数组的数目
     */
    public long countSubarrays(int[] nums, int minK, int maxK) {   //最终自己写的，通过，但没来得及提交
        long ans = 0;
        int left = 0;
        int right = 0;
        int[] both = {-1, -1};
        int end = 0;
        while (right < nums.length) {
            if (nums[right] == maxK) {
                both[0] = right;
            }
            if (nums[right] == minK) {  //和上面分开写，保证最大和最小值一致的情况
                both[1] = right;
            }
            if (nums[right] > maxK || nums[right] < minK) {
                both[0] = -1;
                both[1] = -1;
                left = right + 1;
            }
            if (both[0] != -1 && both[1] != -1) {
                end = Math.max(end, Math.min(both[0], both[1]));  //关键
                long value = end - left + 1;
                ans += value;
            }
            right++;
        }
        return ans;
    }

    public long countSubarrays01(int[] nums, int minK, int maxK) {  //错误，本来像转换为 连续为1的区间内的数组个数，但题目其实是要包含最大和最小值的
        long ans = 0;
        int n = nums.length;
        int[] target = new int[n];
        Arrays.fill(target, 1);
        for (int i = 0; i < n; i++) {
            if (nums[i] < minK || nums[i] > maxK) target[i] = -1;
        }
        int right = 0;
        long currentValue = 1;
        while (right < n) {
            if (target[right] == 1) {
                ans += currentValue;
                currentValue++;
            } else {
                currentValue = 1;
            }
            right++;
        }
        return ans;
    }


    /**
     * 1620. 网络信号最好的坐标
     */
    public int[] bestCoordinate(int[][] towers, int radius) {
        int minX = 0;          //不能给 51，横纵坐标都要从 0 开始
        // 因为题目中有个一特殊的案例 int[][] towers = {{42, 0, 0}}; int radius = 7;
        // 在所有信号强度为 0 时，不是在辐射范围内找字典序最小的坐标（如果这种情况，则[35,0]），而是在坐标轴第一象限中找，即 [0,0]
        int minY = 0;
        int maxX = -1;
        int maxY = -1;
        for (int[] tower : towers) {
            minX = Math.min(minX, tower[0]);
            minY = Math.min(minY, tower[1]);
            maxX = Math.max(maxX, tower[0]);
            maxY = Math.max(maxY, tower[1]);
        }
        int targetX = -1;
        int targetY = -1;
        int maxSignal = -1;
        int maxDistance = radius * radius;
        //二维表格暴力搜索
        for (int i = minX - radius; i <= maxX + radius; i++) {
            if (i < 0) continue;
            for (int j = minY - radius; j <= maxY + radius; j++) {
                if (j < 0) continue;
                int signal = 0; //针对当前位点，累加满足条件的信号强度
                //遍历各个信号塔
                for (int[] tower : towers) {
                    int distance = (i - tower[0]) * (i - tower[0]) + (j - tower[1]) * (j - tower[1]);
                    //-----------------------------------------------
                    // 判断此信号塔是否能够为当前节点提供信号
                    //-----------------------------------------------

                    //1、不能提供信号，直接跳出
                    if (distance > maxDistance) {
                        continue;
                    }
                    //2、可提供信号，累加信号强度
                    signal += Math.floor(tower[2] / (1 + Math.sqrt(distance)));
                }
                //判断当前位点是否是信号强度最强的点
                if (signal > maxSignal) {
                    maxSignal = signal;
                    targetX = i;
                    targetY = j;
                }
            }
        }
        return new int[]{targetX, targetY};
    }


    /**
     * 1668. 最大重复子字符串
     */
    public int maxRepeating(String sequence, String word) {
        if (!sequence.contains(word)) {
            return 0;
        }
        int nums = 0;
        String temp = word;
        while (sequence.contains(word)) {
            word += temp;
            nums++;
        }
        return nums;
    }

    public int maxRepeating01(String sequence, String word) {  //错误写法，题目要求连续
        if (!sequence.contains(word)) {
            return 0;
        }
        int nums = 0;
        while (sequence.contains(word)) {
            sequence = sequence.replaceFirst(word, "#");   //替换为特殊字符，防止前后合并
            nums++;
        }
        return nums;
    }


    /**
     * 816. 模糊坐标
     */
    public List<String> ambiguousCoordinates(String str) {
        List<String> ans = new ArrayList<>();
        str = str.replaceAll("\\(", "").replaceAll("\\)", "");
        for (int i = 0; i < str.length() - 1; i++) {  //枚举插入","的位置
            List<String> arrLeft = ambiguousCoordinatesHelper(str, 0, i);
            List<String> arrRight = ambiguousCoordinatesHelper(str, i + 1, str.length() - 1);
            for (String left : arrLeft) {  //组合拼接
                for (String right : arrRight) {
                    ans.add("(" + left + ", " + right + ")");
                }
            }
        }
        return ans;
    }

    private List<String> ambiguousCoordinatesHelper(String str, int start, int end) {  //枚举"."添加的位置
        List<String> ans = new ArrayList<>();
        //1、待处理区间长度为 1，无需添加"."
        if (start == end) {
            ans.add(String.valueOf(str.charAt(start)));
            return ans;
        }
        //2、待处理区间长度大于 1，合理的添加"."
        if (str.charAt(start) != '0') {      //首先，添加"."到最左侧，等效于不添加"."，保持原样
            ans.add(str.substring(start, end + 1));
        }
        for (int i = start; i < end; i++) {  //其次，依次在各位置后面添加"."，虽然 end 可以取到，但此处不能有等号，因为我们枚举的在 i 位后添加"."，肯定不能再最后添加"."
            String left = str.substring(start, i + 1);
            String right = str.substring(i + 1, end + 1);
            //剪枝一：剔除整数位不合理的情况
            if (left.startsWith("0") && left.length() > 1) {  //整数位以 0 开头，但长度大于 1
                continue;
            }
            if (right.endsWith("0")) {  //小数位以 0 结尾，无论长度是否大于 1，均不合理
                continue;
            }

            //将合理的情况添加到结果集中
            ans.add(left + "." + right);
        }
        return ans;
    }

    /**
     * 764. 最大加号标志
     */
    public int orderOfLargestPlusSign(int n, int[][] mines) {  //四个方向的前缀和
        //生成二维矩阵
        int[][] grid = new int[n + 2][n + 2];
        for (int i = 1; i <= n; i++) {
            Arrays.fill(grid[i], 1);
        }
        for (int[] arr : mines) {
            grid[arr[0] + 1][arr[1] + 1] = 0;
        }
        for (int i = 0; i <= n + 1; i++) {
            for (int j = 0; j <= n + 1; j++) {
                if (i == 0 || i == n + 1 || j == 0 || j == n + 1) {
                    grid[i][j] = 0;
                }
            }
        }
        //定义四个方向的前缀和
        int[][] up = new int[n + 2][n + 2];
        int[][] down = new int[n + 2][n + 2];
        int[][] left = new int[n + 2][n + 2];
        int[][] right = new int[n + 2][n + 2];
        //正向
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (grid[i][j] == 1) {  //关键：如果当前点值为 0，则不计算前缀和，即为默认值 0
                    down[i][j] = down[i - 1][j] + 1;     //从上向下
                    right[i][j] = right[i][j - 1] + 1;   //从左向右
                }
            }
        }
        //反向
        for (int i = n; i >= 1; i--) {
            for (int j = n; j >= 1; j--) {
                if (grid[i][j] == 1) {  //关键：如果当前点值为 0，则不计算前缀和，即为默认值 0
                    up[i][j] = up[i + 1][j] + 1;         //从下向上
                    left[i][j] = left[i][j + 1] + 1;     //从右向左
                }
            }
        }
        int ans = 0;
        //遍历各个点，计算当前点在四个方向上的前缀和的最小值，即为当前点可构成的最大十字架臂长
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                ans = Math.max(ans, Math.min(Math.min(down[i][j], up[i][j]), Math.min(left[i][j], right[i][j])));
            }
        }
        return ans;
    }


    /**
     * 791. 自定义字符串排序
     */
    public String customSortString(String order, String str) {
        int[] bucket1 = new int[26];
        int[] bucket2 = new int[26];
        for (int i = 0; i < order.length(); i++) {
            bucket1[order.charAt(i) - 'a']++;
        }
        for (int i = 0; i < str.length(); i++) {
            if (bucket1[str.charAt(i) - 'a'] != 0) {  //如果二者共有，位置需要具有相对性
                bucket2[str.charAt(i) - 'a']++;
            } else {                //独有，顺序任意
                bucket2[str.charAt(i) - 'a']--;
            }
        }
        ArrayList<Character> sorted = new ArrayList<>();
        for (int i = 0; i < order.length(); i++) {
            if (bucket2[order.charAt(i) - 'a'] > 0) {
                sorted.add(order.charAt(i));
            }
        }
        StringBuilder ans = new StringBuilder();
        for (Character current : sorted) {
            while (bucket2[current - 'a'] > 0) {
                ans.append(current);
                bucket2[current - 'a']--;
            }
        }
        for (int i = 0; i < 26; i++) {
            char current = (char) (i + 'a');
            while (bucket2[i] < 0) {
                ans.append(current);
                bucket2[i]++;
            }
        }
        return ans.toString();
    }

    public String customSortString01(String order, String str) {  //自定义排序，比上面的慢一些
        int[] priority = new int[26];
        for (int i = 0; i < order.length(); i++) {  //按照 order 中的顺序，进行优先级标识
            priority[order.charAt(i) - 'a'] = i + 1;
        }
        Character[] strArray = new Character[str.length()];  //将 str 中字符的顺序按照 order中的优先级排序
        for (int i = 0; i < str.length(); i++) {
            strArray[i] = str.charAt(i);
        }
        Arrays.sort(strArray, (o1, o2) -> priority[o1 - 'a'] - priority[o2 - 'a']);  //按照字符的优先级，升序排序
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < strArray.length; i++) {
            ans.append(strArray[i]);
        }
        return ans.toString();
    }


    /**
     * 1023. 驼峰式匹配
     */
    public List<Boolean> camelMatch(String[] queries, String pattern) {
        List<Boolean> ans = new ArrayList<>();
        for (String query : queries) {
            ans.add(camelMatchHelper(query, pattern));
        }
        return ans;
    }

    private boolean camelMatchHelper(String query, String pattern) {
        if (query.length() < pattern.length()) {
            return false;
        }
        int parIndex = 0;
        int queIndex = 0;
        while (queIndex < query.length()) {
            if (query.charAt(queIndex) == pattern.charAt(parIndex)) {      //相等，两个指针均向后移动一位
                queIndex++;
                parIndex++;
            } else if (Character.isLowerCase(query.charAt(queIndex))) {
                //不相等，此时 pattern 为大写、query 为小写，通过向 pattern 中填充小写字母，来满足条件
                queIndex++;  //需要可以于 pattern 当前匹配的大写字母
            } else if (Character.isUpperCase(query.charAt(queIndex))) {
                return false;
            }
            if (parIndex == pattern.length()) {  // pattern 匹配完了，如果 query 后续有小写，仍可向 pattern 中添加，以使得满足条件
                while (queIndex < query.length()) {
                    if (Character.isUpperCase(query.charAt(queIndex))) {  //但如果 query 后续有大写，则无法完成匹配
                        return false;
                    }
                    queIndex++;
                }
            }

        }
        return parIndex == pattern.length();
    }


    /**
     * 860. 柠檬水找零
     */
    public boolean lemonadeChange(int[] bills) {
        int[] bucket = new int[21];
        for (int money : bills) {
            if (money == 5) bucket[money]++;
            if (money == 10) {
                bucket[money]++;
                if (bucket[5] == 0) return false;
                bucket[5]--;
            }
            if (money == 20) {
                bucket[money]++;
                if ((bucket[5] > 0 && bucket[10] > 0)) {
                    bucket[5]--;
                    bucket[10]--;
                } else if (bucket[5] >= 3) {
                    bucket[5] -= 3;
                } else {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 891. 子序列宽度之和
     */
    public int sumSubseqWidths(int[] nums) {
        Arrays.sort(nums); // 排序
        long res = 0;
        int mod = (int) Math.pow(10, 9) + 7;
        int n = nums.length;
        //2的快速幂
        long[] pow = new long[n];
        pow[0] = 1;
        for (int i = 1; i < n; i++) {
            pow[i] = (pow[i - 1] << 1) % mod;
        }
        for (int i = 0; i < n; i++) {
            //-------------------------------------------------------------
            // 计算每个数 nums[i] 对最终结果的贡献
            //    1、nums[i] 作为子序列的最大值，计算子序列的宽度
            //      需要用 nums[i] 减去子序列的最小值，且其可以作为 2^i 个子序列的最大值，所以会被加 2^i 次
            //    2、nums[i] 作为子序列的最小值，计算子序列的宽度
            //      需要用子序列的最大值减去 nums[i] ，且其可以作为 2^(n-1-i) 个子序列的最小值，所以会被减 2^(n-1-i) 次
            //-------------------------------------------------------------
            res = (res + (pow[i] - pow[n - i - 1]) * nums[i] % mod) % mod;
        }
        return (int) res;
    }

    public int sumSubseqWidths01(int[] nums) { //理论是超时，但结果错误
        //[5,69,89,92,31,16,25,45,63,40,16,56,24,40,75,82,40,12,50,62,92,44,67,38,92,22,91,24,26,21,100,42,23,56,64,43,95,76,84,79,89,4,16,94,16,77,92,9,30,13]
        Arrays.sort(nums);
        long result = 0;
        int mod = (int) 1e9 + 7;
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                result += (((nums[j] - nums[i]) * Math.pow(2, j - i - 1) % mod) % mod);
            }
        }
        return (int) result;
    }


    /**
     * 48. 旋转图像
     */
    public void rotate(int[][] matrix) {  //顺时针90度
        int n = matrix.length;
        int[][] grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            grid[i] = matrix[i].clone();             //深拷贝，关键
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[j][n - i - i] = grid[i][j];     //关键
//                matrix[i][j] = grid[j][n - i - i];   //不能写反
            }
        }
    }

    public void rotate01(int[][] matrix) {
        int n = matrix.length;
        //1、水平翻转
        for (int i = 0; i < n / 2; i++) {    //向下取整
            for (int j = 0; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[n - 1 - i][j];
                matrix[n - 1 - i][j] = temp;
            }
        }
        //2、沿主对角线翻转
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
    }


    /**
     * 809. 情感丰富的文字
     */
    public int expressiveWords(String str, String[] words) {
        int ans = 0;
        for (String word : words) {
            if (verifyExpand(str, word)) {
                ans++;
            }
        }
        return ans;
    }

    private boolean verifyExpand(String target, String word) {
        int m = target.length();
        int n = word.length();
        if (m < n) return false;
        int index1 = 0;
        int index2 = 0;
        while (index1 < m && index2 < n) {
            if (target.charAt(index1) != word.charAt(index2)) {
                return false;
            }
            char ch = target.charAt(index1);
            int nums1 = 0;
            while (index1 < m && target.charAt(index1) == ch) {
                nums1++;     //相同字符的个数
                index1++;    //下一个索引位置
            }
            int nums2 = 0;
            while (index2 < n && word.charAt(index2) == ch) {
                nums2++;
                index2++;
            }

            if (nums1 < nums2) return false;
            if (nums1 > nums2 && nums1 < 3) return false;

        }

        return index1 == m && index2 == n;
    }

    public int expressiveWords01(String str, String[] words) {  //自己写的，虽然通过，但逻辑没有上面的清晰
        int ans = 0;
        int m = str.length();
        for (String word : words) {
            int n = word.length();
            if (m < n) continue;
            int index1 = 0;
            int index2 = 0;
            while (index1 < m && index2 < n) {
                //判断当前位的字符是否相等，不相等直接退出
                if (str.charAt(index1) != word.charAt(index2)) {
                    break;
                }
                //分别计算当前位置字符的个数
                int[] tuple1 = expressiveHelper(str, index1);
                int nums1 = tuple1[0];
                index1 = tuple1[1];

                int[] tuple2 = expressiveHelper(word, index2);
                int nums2 = tuple2[0];
                index2 = tuple2[1];

                if (nums1 > nums2 && nums1 < 3) {
                    break;
                }
                if (nums1 < nums2) {
                    break;
                }
                if (index1 == m && index2 == n) {   //放这里，不能放外面，放外面会误判 "lee" 和 "le" 满足条件
                    ans++;
                }
            }
        }
        return ans;
    }

    private int[] expressiveHelper(String str, int startIndex) {
        if (startIndex == str.length()) return new int[]{0, 0};
        int nums = 1;
        startIndex++;  //朝后窜一位
        while (startIndex < str.length()) {
            if (str.charAt(startIndex) != str.charAt(startIndex - 1)) {
                break;
            }
            nums++;
            startIndex++;
        }
        return new int[]{nums, startIndex};
    }


    /**
     * 1267. 统计参与通信的服务器
     */
    public int countServers(int[][] grid) {   //并查集
        int m = grid.length;
        int n = grid[0].length;
        HashMap<Integer, Integer> rows = new HashMap<>();
        HashMap<Integer, Integer> cols = new HashMap<>();
        //--------------------------------------------------
        // 巧妙，按照行和列进行编号，作为并查集的节点
        //--------------------------------------------------
        countServersUFS ufs = new countServersUFS(grid);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {   //遇到任意一个服务器，则合并同行同列对应的集合
                    if (rows.containsKey(i)) {
                        ufs.union(i * n + j, rows.get(i));
                    }
                    if (cols.containsKey(j)) {
                        ufs.union(i * n + j, cols.get(j));
                    }
                    rows.put(i, i * n + j);
                    cols.put(j, i * n + j);
                }
            }
        }
        return ufs.getNums();
    }

    static class countServersUFS {
        int[] nodes;
        int[] sizes;

        countServersUFS(int[][] grid) {
            int m = grid.length;
            int n = grid[0].length;
            this.nodes = new int[m * n];
            this.sizes = new int[m * n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (grid[i][j] == 1) {   //服务器节点进行赋值
                        int nodeIndex = i * n + j;
                        nodes[nodeIndex] = nodeIndex;
                        sizes[nodeIndex] = 1;
                    }
                }
            }

        }

        private int findSet(int xx) {
            if (nodes[xx] != xx) {
                nodes[xx] = findSet(nodes[xx]);
            }
            return nodes[xx];
        }

        private void union(int xx, int yy) {
            int xRoot = findSet(xx);
            int yRoot = findSet(yy);
            if (xRoot == yRoot) return;
            nodes[xRoot] = yRoot;
            sizes[yRoot] += sizes[xRoot];
        }

        private int getNums() {
            int ans = 0;
            for (int i = 0; i < sizes.length; i++) {
                //汇总集合大小为 1 的则说明只有一台服务器，不可互联互通
                if (nodes[i] == i && sizes[i] >= 2) {  //汇总集合大小大于 1 的集合中的所有元素的个数
                    ans += sizes[i];  //注意，一定判断根节点
                }
            }
            return ans;
        }

    }

    public int countServers01(int[][] grid) {  //模拟
        int m = grid.length;
        int n = grid[0].length;
        int[] count_rows = new int[m];
        int[] count_cols = new int[n];
        //---------------------------
        // 统计各行各列中服务器的个数
        //---------------------------
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    count_rows[i]++;
                    count_cols[j]++;
                }
            }
        }
        int ans = 0;
        //--------------------------------------------------------------------------------
        // 顺序遍历，如果当前节点的为服务器，同时其所在的列或所在的行中有其他服务器，则其满足条件
        //--------------------------------------------------------------------------------
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1 && (count_rows[i] > 1 || count_cols[j] > 1)) {
                    ans++;
                }
            }
        }
        return ans;
    }


    /**
     * 1779. 找到最近的有相同 X 或 Y 坐标的点
     */
    public int nearestValidPoint(int x, int y, int[][] points) {
        int ans = -1;
        int minDistance = Integer.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            if (points[i][0] == x || points[i][1] == y) {
                int currDistance = Math.abs(x - points[i][0]) + Math.abs(y - points[i][1]);
                if (currDistance < minDistance) {
                    minDistance = currDistance;
                    ans = i;
                }
            }
        }
        return ans;
    }


    /**
     * 1781. 所有子字符串美丽值之和
     */
    public int beautySum(String str) {
        int ans = 0;
        int n = str.length();
        for (int i = 0; i < n; i++) {      //枚举区间左端点
            int[] buckets = new int[26];
            for (int j = i; j < n; j++) {  //枚举区间右端点
                buckets[str.charAt(j) - 'a']++;
                //初始化区间内最大频次数和最小频次数
                int maxFreq = 0;
                int minFreq = n;
                for (int m = 0; m < 26; m++) {  //计算最大和最小频次
                    maxFreq = Math.max(maxFreq, buckets[m]);
                    minFreq = buckets[m] == 0 ? minFreq : Math.min(minFreq, buckets[m]);
                }
                ans += maxFreq - minFreq;
            }
        }
        return ans;
    }


    /**
     * 1812. 判断国际象棋棋盘中一个格子的颜色
     */
    public boolean squareIsWhite(String coordinates) {
        int xx = coordinates.charAt(0) - 'a';
        int yy = coordinates.charAt(1) - '1';
        return ((xx & 1) == 1 && (yy & 1) == 0) || ((xx & 1) == 0 && (yy & 1) == 1);  //两种情况
    }


    /**
     * 1769. 移动所有球到每个盒子所需的最小操作数
     */
    public int[] minOperations(String boxes) {
        int n = boxes.length();
        char[] nums = boxes.toCharArray();
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            int currAns = 0;
            for (int j = 0; j < n; j++) {
                if (j == i) continue;
                if (nums[j] == '1') {
                    currAns += Math.abs(j - i);
                }
            }
            ans[i] = currAns;
        }
        return ans;
    }


    /**
     * 1945. 字符串转化后的各位数字之和
     */
    public int getLucky(String str, int k) {
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < str.length(); i++) {
            String nums = String.valueOf(str.charAt(i) - 'a' + 1);
            for (int j = 0; j < nums.length(); j++) {
                queue.addLast(nums.charAt(j) - '0');
            }
        }
        int ans = 0;
        for (int i = 0; i < k; i++) {
            int curr = 0;
            while (!queue.isEmpty()) {
                curr += queue.pollFirst();
            }
            ans = curr;
            if (ans < 10) return ans;

            //重新初始化队列，用于下次迭代
            String nums = String.valueOf(ans);
            for (int j = 0; j < nums.length(); j++) {
                queue.addLast(nums.charAt(j) - '0');
            }
        }
        return ans;
    }


    /**
     * 1759. 统计同构子字符串的数目
     */
    public int countHomogenous(String str) {  //模拟
        int mod = (int) 1e9 + 7;
        long ans = 0;   //使用长整型
        int freq = 1;
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) == str.charAt(i - 1)) {
                freq++;
            } else {
                ans += (long) (1 + freq) * freq / 2;  //长整型
                ans %= mod;
                freq = 1;
            }
        }
        ans += (long) (1 + freq) * freq / 2;  //长整型
        return (int) (ans % mod);
    }

    /**
     * 2027. 转换字符串的最少操作次数
     */
    public int minimumMoves(String str) {
        int ans = 0;
        int currIndex = 0;
        while (currIndex < str.length()) {
            if (str.charAt(currIndex) == 'O') {
                currIndex++;
            } else {
                ans++;
                currIndex += 3;
            }
        }
        return ans;
    }


    /**
     * 999. 可以被一步捕获的棋子数
     */
    public int numRookCaptures(char[][] board) {
        int ans = 0;
        int row = -1;
        int col = -1;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        //寻找出发点
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 'R') {
                    row = i;
                    col = j;
                    break;
                }
            }
            if (row != -1) break;
        }
        //从起点向四个方向搜索
        for (int[] dir : directions) {
            int steps = 1;
            while (steps <= 8) {
                int nextRow = row + steps * dir[0];
                int nextCol = col + steps * dir[1];
                //剪枝一：此方向越界
                if (nextRow < 0 || nextRow >= 8 || nextCol < 0 || nextCol >= 8) {
                    break;
                }
                //剪枝二：此方向阻碍
                if (board[nextRow][nextCol] == 'B') {
                    break;
                }
                //目标点
                if (board[nextRow][nextCol] == 'p') {
                    ans++;
                    break;
                }
                steps++;
            }
        }
        return ans;
    }

    /**
     * 1260. 二维网格迁移
     */
    public List<List<Integer>> shiftGrid(int[][] grid, int k) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] graph = new int[m][n];
        List<List<Integer>> ans = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int index = i * n + j + k;
                int row = (index / n) % m;
                int col = index % n;
                graph[row][col] = grid[i][j];
            }
        }
        for (int i = 0; i < m; i++) {
            ArrayList<Integer> currRow = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                currRow.add(graph[i][j]);
            }
            ans.add(currRow);
        }
        return ans;
    }


    /**
     * 1496. 判断路径是否相交
     */
    public boolean isPathCrossing(String path) {
        HashMap<Integer, HashSet<Integer>> hTable = new HashMap<>();
        HashMap<Character, int[]> directions = new HashMap<>();
        directions.put('N', new int[]{-1, 0});
        directions.put('W', new int[]{0, -1});
        directions.put('S', new int[]{1, 0});
        directions.put('E', new int[]{0, 1});
        HashSet<Integer> start = new HashSet<>();
        start.add(0);
        hTable.put(0, start);
        int row = 0;
        int col = 0;
        int index = 0;
        while (index < path.length()) {
            char ch = path.charAt(index);
            int[] dir = directions.get(ch);
            row = row + dir[0];
            col = col + dir[1];
            HashSet<Integer> colsIndex = hTable.getOrDefault(row, new HashSet<>());
            if (colsIndex.contains(col)) {
                return true;
            }
            colsIndex.add(col);
            hTable.put(row, colsIndex);
            index++;
        }
        return false;
    }


    /**
     * 2032. 至少在两个数组中出现的值
     */
    public List<Integer> twoOutOfThree(int[] nums1, int[] nums2, int[] nums3) {
        List<Integer> ans = new ArrayList<>();
        int[][] bucket = new int[101][3];
        int n = Math.max(Math.max(nums1.length, nums2.length), nums3.length);
        //每行代表一个数，其对应的三位数组，分别表示三个数组中是否包含当前数
        for (int i = 0; i < n; i++) {
            if (i < nums1.length) bucket[nums1[i]][0] = 1;
            if (i < nums2.length) bucket[nums2[i]][1] = 1;
            if (i < nums3.length) bucket[nums3[i]][2] = 1;
        }
        for (int i = 0; i < 101; i++) {
            int sum = 0;
            for (int j = 0; j < 3; j++) {
                sum += bucket[i][j];
            }
            if (sum >= 2) ans.add(i);
        }
        return ans;
    }


    /**
     * 2042. 检查句子中的数字是否递增
     */
    public boolean areNumbersAscending(String str) {
        int prev = -1;
        String[] arr = str.split(" ");
        for (int i = 0; i < arr.length; i++) {
            if (Character.isDigit(arr[i].charAt(0))) {
                int curr = Integer.parseInt(arr[i]);
                if (prev >= curr) {
                    return false;
                }
                prev = curr;
            }
        }
        return true;
    }


    /**
     * 2351. 第一个出现两次的字母
     */
    public char repeatedCharacter(String str) {
        int[] buckets = new int[26];
        for (int i = 0; i < str.length(); i++) {
            int cur = str.charAt(i) - 'a';
            if (buckets[cur] == 1) return str.charAt(i);
            buckets[cur]++;
        }
        return '0';
    }


    /**
     * 2180. 统计各位数字之和为偶数的整数个数
     */
    public int countEven(int num) {
        int ans = 0;
        while (num > 0) {
            int sum = getSum(num);
            if ((sum & 1) == 0) ans++;
            num--;
        }
        return ans;
    }

    private int getSum(int num) {
        int sum = 0;
        while (num > 0) {
            sum += (num % 10);
            num /= 10;
        }
        return sum;
    }


    /**
     * 1806. 还原排列的最少操作步数
     */
    public int reinitializePermutation(int n) {
        int ans = 0;
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = i;
        }
        //目标数组
        int[] target = nums.clone();
        while (true) {
            ans++;
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                if (i % 2 == 0) arr[i] = nums[i / 2];
                if (i % 2 == 1) arr[i] = nums[n / 2 + (i - 1) / 2];
            }
            if (Arrays.equals(arr, target)) return ans;
            nums = arr.clone();
        }
    }


    /**
     * 289. 生命游戏
     */
    public void gameOfLife(int[][] board) {
        int m = board.length;
        int n = board[0].length;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        int[][] ans = new int[m][n];
        for (int i = 0; i < m; i++) {
            ans[i] = board[i].clone();
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int lives = 0;
                for (int[] dir : directions) {
                    int nextRow = i + dir[0];
                    int nextCol = j + dir[1];
                    if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                        if (board[nextRow][nextCol] == 1) lives++;
                    }
                }
                if (board[i][j] == 1) {  //活细胞
                    if (lives < 2 || lives > 3) ans[i][j] = 0;  //变为死细胞
                }
                if (board[i][j] == 0) {  //死细胞
                    if (lives == 3) ans[i][j] = 1;    //变为活细胞
                }
            }
        }
        for (int i = 0; i < m; i++) {
            board[i] = ans[i].clone();
        }
    }

    /**
     * 498. 对角线遍历
     */
    public int[] findDiagonalOrder(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[] ans = new int[m * n];
        int row = 0;
        int col = 0;
        int curIndex = 0;
        while (curIndex < m * n) {
            //1、斜上方
            while (row >= 0 && col < n) {
                ans[curIndex] = grid[row][col];
                row--;
                col++;
                curIndex++;
            }
            if (col == n) {   //位置重置
                row++;
                row++;
                col--;
            } else {
                row = 0;
            }

            //2、斜下方
            while (row < m && col >= 0) {
                ans[curIndex] = grid[row][col];
                row++;
                col--;
                curIndex++;
            }
            if (row == m) {   //位置重置
                col++;
                col++;
                row--;
            } else {
                col = 0;
            }
        }
        return ans;
    }


    /**
     * 640. 求解方程
     */
    public String solveEquation(String equation) {  //模拟，移项 + 合并同类项
        String[] arr = equation.split("=");
        String[] arr1 = arr[0].replace("-", "+-").split("\\+");
        String[] arr2 = arr[1].replace("-", "+-").split("\\+");
        int prefix = 0;  //记录 x 的系数
        int sum = 0;     //记录累加和
        //1、等号左侧
        int[] tuple1 = solveEquationHelper(arr1);
        prefix += tuple1[0];
        sum -= tuple1[1];

        //2、等号右侧
        int[] tuple2 = solveEquationHelper(arr2);
        prefix -= tuple2[0];
        sum += tuple2[1];

        if (prefix == 0) {
            if (sum == 0) return "Infinite solutions";
            else return "No solution";
        }
        if (sum % prefix != 0) return "No solution";
        return "x=" + sum / prefix;
    }

    private int[] solveEquationHelper(String[] arr) {
        int sum = 0;
        int prefix = 0;
        for (String cur : arr) {
            if (cur.equals("")) {   //不能忽略
                continue;
            }
            if (cur.equals("x")) {
                prefix++;
            } else if (cur.equals("-x")) {
                prefix--;
            } else if (cur.contains("x")) {
                if (cur.startsWith("-")) {
                    cur = cur.replace("-", "").replace("x", "");
                    prefix -= Integer.parseInt(cur);
                } else {
                    cur = cur.replace("x", "");
                    prefix += Integer.parseInt(cur);
                }
            } else {
                if (cur.startsWith("-")) {
                    cur = cur.replace("-", "");
                    sum -= Integer.parseInt(cur);
                } else {
                    sum += Integer.parseInt(cur);
                }
            }
        }
        return new int[]{prefix, sum};
    }


    /**
     * 1706. 球会落何处
     */
    public int[] findBall(int[][] grid) {  //模拟
        int m = grid.length;
        int n = grid[0].length;
        int[] curr = new int[n];
        for (int i = 0; i < n; i++) {
            curr[i] = i + 10;
        }
        for (int i = 0; i < m; i++) {
            int[] next = new int[n];
            for (int j = 0; j < n; j++) {
                if (curr[j] != 0) {
                    if (grid[i][j] == 1) {
                        if (j < n - 1 && grid[i][j + 1] == 1) {
                            next[j + 1] = curr[j];
                        }
                    } else {
                        if (j > 0 && grid[i][j - 1] == -1) {
                            next[j - 1] = curr[j];
                        }
                    }
                }
            }
            curr = next.clone();
        }
        int[] ans = new int[n];
        Arrays.fill(ans, -1);
        for (int i = 0; i < n; i++) {
            if (curr[i] > 0) {
                ans[curr[i] - 10] = i;
            }
        }
        return ans;
    }

    /**
     * 874. 模拟行走机器人
     */
    public int robotSim(int[] commands, int[][] obstacles) {
        int ans = 0;
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};  //按照顺时针方向，北、东、南、西
        //记录每行中哪些点为障碍物
        HashMap<Integer, HashSet<Integer>> obs = new HashMap<>();
        //初始化
        for (int[] obstacle : obstacles) {
            int row = obstacle[0];
            int col = obstacle[1];
            HashSet<Integer> set = obs.getOrDefault(row, new HashSet<>());
            set.add(col);
            obs.put(row, set);
        }
        int currDir = 0; //初始方向为向北
        int currRow = 0;
        int currCol = 0;
        for (int i = 0; i < commands.length; i++) {
            int steps = commands[i];
            if (steps == -1) {           //向右转向
                currDir = (currDir + 1) % 4;
            } else if (steps == -2) {    //向左转向
                currDir = (currDir + 3) % 4;
            } else {                     //沿着当前方向行走
                for (int m = 1; m <= steps; m++) {  //仅计数
                    int nextRow = currRow + directions[currDir][0];
                    int nextCol = currCol + directions[currDir][1];
                    if (obs.containsKey(nextRow) && obs.get(nextRow).contains(nextCol)) {
                        break;   //遇到障碍物，直接跳出
                    }
                    currRow = nextRow;
                    currCol = nextCol;
                    ans = Math.max(ans, currRow * currRow + currCol * currCol);
                }
            }
        }
        return ans;
    }


    /**
     * 946. 验证栈序列
     */
    public boolean validateStackSequences(int[] pushed, int[] popped) {
        int n = pushed.length;
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        int index1 = 0;
        int index2 = 0;
        while (index1 < n) {
            //1、首先尝试剔除
            while (!stack.isEmpty() && stack.peekLast() == popped[index2]) {
                stack.pollLast();
                index2++;
            }
            //2、其次添加元素
            stack.addLast(pushed[index1]);
            index1++;
        }
        while (!stack.isEmpty()) {
            if (stack.peekLast() != popped[index2]) {
                return false;
            }
            stack.pollLast();
            index2++;
        }
        return index2 == n;
    }


    /**
     * 2283. 判断一个数的数字计数是否等于数位的值
     */
    public boolean digitCount(String num) {
        int n = num.length();
        int[] target = new int[11];
        for (int i = 0; i < n; i++) {
            target[i] = num.charAt(i) - '0';
        }
        int[] buckets = new int[11];
        for (int i = 0; i < n; i++) {
            buckets[num.charAt(i) - '0']++;
        }
        return Arrays.equals(target, buckets);
    }


    /**
     * 1807. 替换字符串中的括号内容
     */
    public String evaluate(String str, List<List<String>> knowledge) {
        StringBuilder ans = new StringBuilder();
        HashMap<String, String> hTable = new HashMap<>();
        for (List<String> tuple : knowledge) {
            String key = tuple.get(0);
            String val = tuple.get(1);
            hTable.put(key, val);
        }
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                int index = i;
                while (str.charAt(index) != ')') {
                    index++;
                }
                String word = str.substring(i + 1, index);
                ans.append(hTable.getOrDefault(word, "?"));
                i = index;
            } else {
                ans.append(str.charAt(i));
            }
        }
        return ans.toString();
    }


    public String evaluate01(String str, List<List<String>> knowledge) {  //超时
        HashMap<String, String> hTable = new HashMap<>();
        for (List<String> tuple : knowledge) {
            String key = tuple.get(0);
            String val = tuple.get(1);
            hTable.put("(" + key + ")", val);
        }
        ArrayDeque<Integer> left = new ArrayDeque<>();
        ArrayDeque<Integer> right = new ArrayDeque<>();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') left.addLast(i);
            if (str.charAt(i) == ')') right.addLast(i);
        }
        //记录所有括号中的内容
        HashSet<String> list = new HashSet<>();
        while (!left.isEmpty() && !right.isEmpty()) {
            Integer index1 = left.pollFirst();
            Integer index2 = right.pollFirst();
            list.add(str.substring(index1, index2 + 1));
        }
        for (String curr : list) {
            str = str.replace(curr, hTable.getOrDefault(curr, "?"));
        }
        return str;
    }


    /**
     * 2287. 重排字符形成目标字符串
     */
    public int rearrangeCharacters(String str, String target) {
        int[] buckets = new int[26];
        for (int i = 0; i < str.length(); i++) {
            buckets[str.charAt(i) - 'a']++;
        }
        int ans = 0;
        while (true) {  //每轮都要处理一遍字符
            for (int i = 0; i < target.length(); i++) {   //逐一，target 字符有重复
                char ch = target.charAt(i);
                if (--buckets[ch - 'a'] < 0) {
                    return ans;
                }
            }
            ans++;
        }
    }

    public int rearrangeCharacters01(String str, String target) {  //错误写法
        int[] buckets = new int[26];
        for (int i = 0; i < str.length(); i++) {
            buckets[str.charAt(i) - 'a']++;
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < target.length(); i++) {
            min = Math.min(min, buckets[target.charAt(i) - 'a']);
        }
        return min;
    }


    /**
     * 885. 螺旋矩阵 III
     */
    public int[][] spiralMatrixIII(int m, int n, int currRow, int currCol) {
        int nums = m * n;
        int[][] ans = new int[nums][2];
        ans[0] = new int[]{currRow, currCol};
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};  //顺时针方向，东、南、西、北
        int steps = 1;     //当前在每个方向可行走的步数
        int currDir = 0;   //当前的方向
        int currNums = 1;  //当前满足条件位点数
        while (currNums < nums) {
            //1、沿着当前方向行走 steps 步
            for (int i = 0; i < steps; i++) {
                //1.1、沿着当前方向，继续向前移动
                currRow += dirs[currDir][0];
                currCol += dirs[currDir][1];
                //1.2、如果是有效位点
                if (currRow < m && currRow >= 0 && currCol < n && currCol >= 0) {
                    ans[currNums] = new int[]{currRow, currCol};
                    currNums++;
                }
            }
            //2、沿着顺时针，更改方向
            currDir++;
            //3、判断步长是否需要增加
            currDir %= 4;  //必须 %4，因为一共 4 个方向，如果 %2，则只是使用了两个方向
            if (currDir == 0 || currDir == 2) steps++;   //每改变两次方向，每个边的步长增加 1
            //以下为错误写法
//            currDir %= 2;
//            if (currDir == 0) steps++;   //每改变两次方向，每个边的步长增加 1
        }
        return ans;
    }


    /**
     * 2319. 判断矩阵是否是一个 X 矩阵
     */
    public boolean checkXMatrix(int[][] grid) {
        int n = grid.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j || i == n - j - 1) {
                    if (grid[i][j] == 0) return false;
                } else {
                    if (grid[i][j] != 0) return false;
                }
            }
        }
        return true;
    }


    /**
     * 2309. 兼具大小写的最好英文字母
     */
    public String greatestLetter(String str) {
        for (int i = 0; i < 26; i++) {
            if (str.contains(String.valueOf((char) ('z' - i))) && str.contains(String.valueOf((char) ('Z' - i))))
                return String.valueOf((char) ('Z' - i));
        }
        return "";
    }


    /**
     * 1828. 统计一个圆中点的数目
     */
    public int[] countPoints(int[][] points, int[][] queries) {
        int m = points.length;
        int n = queries.length;
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            int row = queries[i][0];
            int col = queries[i][1];
            int dis = queries[i][2];
            for (int j = 0; j < m; j++) {
                int nextRow = points[j][0];
                int nextCol = points[j][1];
                if ((nextRow - row) * (nextRow - row) + (nextCol - col) * (nextCol - col) <= dis * dis) {
                    ans[i]++;
                }
            }
        }
        return ans;
    }


    /**
     * 2315. 统计星号
     */
    public int countAsterisks(String str) {
        int ans = 0;
        int nums = 0;
        int n = str.length();
        for (int i = 0; i < n; i++) {
            //记录当前 '|' 的个数
            if (str.charAt(i) == '|') nums++;
            //只有当前字符为 '*'，同时当前字符位于偶数个 '|' 后，才为满足条件的 '*'
            if (str.charAt(i) == '*' && (nums & 1) == 0) {
                ans++;
            }
        }
        return ans;
    }


    /**
     * 2303. 计算应缴税款总额
     */
    public double calculateTax(int[][] brackets, int income) {  //模拟
        double ans = 0.0;
        int n = brackets.length;
        for (int i = 0; i < n; i++) {
            int diff = Math.min(income, brackets[i][0]) - (i == 0 ? 0 : brackets[i - 1][0]);
            ans += diff * brackets[i][1] * 1.0 / 100;    //按照高税率纳税
            if (income < brackets[i][0]) {  //提前剪枝
                break;
            }
        }
        return ans;
    }

    public double calculateTax01(int[][] brackets, int income) {  //上下两种写法的差异在于是否通过变量记录上一个税收区间上限
        int lower = 0;
        double ans = 0.0;
        int n = brackets.length;
        for (int[] bracket : brackets) {
            int upper = bracket[0];
            int percent = bracket[1];
            int diff = Math.min(income, bracket[0]) - lower;
            ans += diff * percent * 1.0 / 100;    //按照高税率纳税
            if (income < bracket[0]) {  //提前剪枝
                break;
            }
            lower = upper;
        }
        return ans;
    }

    /**
     * 720. 词典中最长的单词
     */
    public String longestWord(String[] words) {
        String ans = "";
        Arrays.sort(words);
        HashSet<String> set = new HashSet<>();
        for (String word : words) {
            if (word.length() == 1 || set.contains(word.substring(0, word.length() - 1))) {
                set.add(word);
                //只有在长度更长时更新 ans，长度相等时，不更新 ans，因为当前 ans的字典序更小
                ans = word.length() > ans.length() ? word : ans;
            }
        }
        return ans;
    }


    /**
     * 2299. 强密码检验器 II
     */
    public boolean strongPasswordCheckerII(String password) {
        int n = password.length();
        if (n < 8) return false;
        int upperFlag = 0;
        int lowerFlag = 0;
        int digitFlag = 0;
        int signsFlag = 0;
        String signs = "!@#$%^&*()-+";
        char prev = ' ';
        for (int i = 0; i < n; i++) {
            char ch = password.charAt(i);
            if (ch == prev) return false;
            if (Character.isUpperCase(ch)) upperFlag = 1;
            if (Character.isLowerCase(ch)) lowerFlag = 1;
            if (Character.isDigit(ch)) digitFlag = 1;
            if (signs.contains(String.valueOf(ch))) signsFlag = 1;
            prev = ch;
        }
        return upperFlag == 1 && lowerFlag == 1 && digitFlag == 1 && signsFlag == 1;
    }

    /**
     * 1817. 查找用户活跃分钟数
     */
    public int[] findingUsersActiveMinutes(int[][] logs, int k) {
        int[] ans = new int[k];
        HashMap<Integer, HashSet<Integer>> map = new HashMap<>();
        for (int[] log : logs) {
            int userID = log[0];
            int activeTime = log[1];
            HashSet<Integer> set = map.getOrDefault(userID, new HashSet<>());
            set.add(activeTime);
            map.put(userID, set);
        }
        for (int userID : map.keySet()) {
            HashSet<Integer> userActiveTimes = map.get(userID);
            ans[userActiveTimes.size() - 1]++;
        }
        return ans;
    }


    /**
     * 2325. 解密消息
     */
    public String decodeMessage(String key, String message) {  //哈希表
        char flag = 'a';
        HashMap<Character, Character> map = new HashMap<>();
        int n = key.length();
        for (int i = 0; i < n; i++) {
            char ch = key.charAt(i);
            if (ch == ' ') continue;
            if (map.containsKey(ch)) continue;
            map.put(ch, flag);
            flag++;
        }
        map.put(' ', ' ');
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            ans.append(map.get(message.charAt(i)));
        }
        return ans.toString();
    }


    /**
     * 1604. 警告一小时内使用相同员工卡大于等于三次的人
     */
    public List<String> alertNames(String[] keyName, String[] keyTime) {
        int n = keyName.length;
        List<String> ans = new ArrayList<>();
        HashMap<String, ArrayList<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String[] tuple = keyTime[i].split(":");
            int timeNums = Integer.parseInt(tuple[0]) * 60 + Integer.parseInt(tuple[1]);
            ArrayList<Integer> times = map.getOrDefault(keyName[i], new ArrayList<>());
            times.add(timeNums);
            map.put(keyName[i], times);
        }
        Set<String> names = map.keySet();
        for (String name : names) {
            ArrayList<Integer> times = map.get(name);
            times.sort((o1, o2) -> o1 - o2);  //升序排序
            int left = 0;
            int right = 0;
            while (right < times.size()) {
                Integer curr = times.get(right);
                while (left < right && curr - times.get(left) > 60) {
                    left++;
                }
                if (right - left + 1 >= 3) {
                    ans.add(name);
                    break;
                }
                right++;
            }
        }
        Collections.sort(ans);
        return ans;
    }

    public List<String> alertNames01(String[] keyName, String[] keyTime) {
        int n = keyName.length;
        List<String> ans = new ArrayList<>();
        HashMap<String, ArrayList<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String[] tuple = keyTime[i].split(":");
            int timeNums = Integer.parseInt(tuple[0]) * 60 + Integer.parseInt(tuple[1]);
            ArrayList<Integer> times = map.getOrDefault(keyName[i], new ArrayList<>());
            times.add(timeNums);
            map.put(keyName[i], times);
        }
        Set<String> names = map.keySet();
        for (String name : names) {
            ArrayList<Integer> times = map.get(name);
            times.sort((o1, o2) -> o1 - o2);  //升序排序
            for (int i = 2; i < times.size(); i++) {
                if (times.get(i) - times.get(i - 2) <= 60) {
                    ans.add(name);
                    break;
                }
            }
        }
        Collections.sort(ans);
        return ans;
    }

    /**
     * 1233. 删除子文件夹
     */
    public List<String> removeSubfolders(String[] folder) {   //模拟
        Arrays.sort(folder);
        List<String> ans = new ArrayList<>();
        ans.add(folder[0]);
        int startIndex = 0;
        int n = folder.length;
        for (int i = 1; i < n; i++) {
            String str = folder[i];
            if (folder[i].startsWith(folder[startIndex]) && folder[i].charAt(folder[startIndex].length()) == '/') {
                continue;
            } else {
                startIndex = i;
                ans.add(folder[i]);
            }
        }
        return ans;
    }


    /**
     * 12. 整数转罗马数字
     */
    public String intToRoman(int num) {
        StringBuilder ans = new StringBuilder();
        ArrayList<Roman> map = new ArrayList<>();
        map.add(new Roman(1000, "M"));
        map.add(new Roman(900, "CM"));
        map.add(new Roman(500, "D"));
        map.add(new Roman(400, "CD"));
        map.add(new Roman(100, "C"));
        map.add(new Roman(90, "XC"));
        map.add(new Roman(50, "L"));
        map.add(new Roman(40, "XL"));
        map.add(new Roman(10, "X"));
        map.add(new Roman(9, "IX"));
        map.add(new Roman(5, "V"));
        map.add(new Roman(4, "IV"));
        map.add(new Roman(1, "I"));
        //贪心从大到小消耗和匹配
        for (Roman roman : map) {
            while (num - roman.nums >= 0) {
                num -= roman.nums;
                ans.append(roman.flag);
            }
        }
        return ans.toString();
    }

    static class Roman {
        int nums;
        String flag;

        Roman(int nums, String flag) {
            this.nums = nums;
            this.flag = flag;
        }
    }


    /**
     * 1221. 分割平衡字符串
     */
    public int balancedStringSplit(String s) {
        int ans = 0;
        int numsL = 0;
        int numsR = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'L')
                numsL++;
            else
                numsR++;
            if (numsL == numsR) {
                ans++;
            }
        }
        return ans;
    }

    /**
     * 1437. 是否所有 1 都至少相隔 k 个元素
     */
    public boolean kLengthApart(int[] nums, int k) {
        int n = nums.length;
        int prev = -1;
        for (int i = 0; i < n; i++) {
            if (nums[i] == 1) {
                if (prev != -1 && i - prev - 1 < k) {
                    return false;
                }
                prev = i;
            }
        }
        return true;
    }


    public boolean kLengthApart01(int[] nums, int k) {  //错误写法
        int window = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                window++;
            } else {
                if (i > 0 && window < k) {
                    return false;
                }
                window = 0;
            }
        }
        return true;
    }

    /**
     * 575. 分糖果
     */
    public int distributeCandies(int[] candyType) {
        int total = candyType.length / 2;
        HashSet<Integer> candyTypeDistinct = new HashSet<>();
        for (int candy : candyType)
            candyTypeDistinct.add(candy);
        return Math.min(candyTypeDistinct.size(), total);
    }

    /**
     * 575. 分糖果
     */
    public int distributeCandies01(int[] candyType) {
        return Math.min(candyType.length / 2, (int) Arrays.stream(candyType).distinct().count());
    }

    /**
     * 561. 数组拆分
     */
    public int arrayPairSum(int[] nums) {
        int ans = 0;
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i += 2) {
            ans += nums[i];
        }
        return ans;
    }


    /**
     * 2347. 最好的扑克手牌
     */
    public String bestHand(int[] ranks, char[] suits) {
        int[] buckets01 = new int[14];
        int[] buckets02 = new int[4];
        for (int i = 0; i < 5; i++) {
            buckets01[ranks[i]]++;
            buckets02[suits[i] - 'a']++;
        }
        for (int i = 0; i < 4; i++) {
            if (buckets02[i] == 5) return "Flush";
        }
        int existMoreThree = 0;
        int equalTwo = 0;
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < 14; i++) {
            if (buckets01[i] >= 3) existMoreThree = 1;
            if (buckets01[i] == 2) equalTwo = 1;
            if (buckets01[i] > 0) set.add(i);
        }
        if (existMoreThree == 1) return "Three of a Kind";
        if (equalTwo == 1) return "Pair";
        if (set.size() == 5) return "High Card";
        return "";
    }

    /**
     * 771. 宝石与石头
     */
    public int numJewelsInStones(String jewels, String stones) {
        HashSet<Character> set = new HashSet<>();
        for (int i = 0; i < jewels.length(); i++) {
            set.add(jewels.charAt(i));
        }
        int ans = 0;
        for (int i = 0; i < stones.length(); i++) {
            if (set.contains(stones.charAt(i))) ans++;
        }
        return ans;
    }


    /**
     * 2357. 使数组中所有元素都等于零
     */
    public int minimumOperations(int[] nums) {  //自己写的，略复杂
        int ans = 0;
        PriorityQueue<Integer> sortedQueue = new PriorityQueue<>((o1, o2) -> o1 - o2);  //升序排序
        int zeroNums = 0;
        for (int num : nums) {
            if (num > 0) {
                sortedQueue.add(num);
                zeroNums++;
            }
        }
        int prev = 0;
        while (zeroNums > 0 && !sortedQueue.isEmpty()) {
            //更新被减数的值，累加之前剪掉的值
            while (!sortedQueue.isEmpty() && sortedQueue.peek() - prev == 0) {
                sortedQueue.poll();
            }
            Integer min = sortedQueue.poll() - prev;
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] > 0) {
                    nums[i] -= min;
                    if (nums[i] == 0) zeroNums--;
                }
            }
            prev += min;
            ans++;
        }
        return ans;
    }


    public int minimumOperations01(int[] nums) {  //排序 + 模拟
        int ans = 0;
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                reduceAll(nums, nums[i], i);
                ans++;
            }
        }
        return ans;
    }

    private void reduceAll(int[] nums, int reduce, int startIndex) {
        for (int i = startIndex; i < nums.length; i++) {
            nums[i] -= reduce;
        }
    }

    public int minimumOperations02(int[] nums) {  //哈希表
        HashSet<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (num > 0) set.add(num);
        }
        return set.size();
    }


    /**
     * 1144. 递减元素使数组呈锯齿状
     */
    public int movesToMakeZigzag(int[] nums) {
        return Math.min(moveHelp(nums, 0), moveHelp(nums, 1));
    }

    private int moveHelp(int[] nums, int startIndex) {
        int ans = 0;
        for (int i = startIndex; i < nums.length; i += 2) {
            int min1 = i - 1 >= 0 ? nums[i - 1] : Integer.MAX_VALUE;
            int min2 = i + 1 < nums.length ? nums[i + 1] : Integer.MAX_VALUE;
            int min = Math.min(min1, min2);
            ans += nums[i] < min ? 0 : nums[i] - min + 1;
        }
        return ans;
    }


    /**
     * 2363. 合并相似的物品
     */
    public List<List<Integer>> mergeSimilarItems(int[][] items1, int[][] items2) {
        List<List<Integer>> ans = new ArrayList<>();
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int[] edge : items1) {
            int value = edge[0];
            int weight = edge[1];
            Integer prefix = map.getOrDefault(value, 0);
            map.put(value, prefix + weight);
        }
        for (int[] edge : items2) {
            int value = edge[0];
            int weight = edge[1];
            Integer prefix = map.getOrDefault(value, 0);
            map.put(value, prefix + weight);
        }
        for (int value : map.keySet()) {
            ans.add(Arrays.asList(value, map.get(value)));
        }
        ans.sort((o1, o2) -> o1.get(0) - o2.get(0));
        return ans;
    }


    /**
     * 1002. 查找共用字符
     */
    public List<String> commonChars(String[] words) {
        int m = words.length;
        int[][] buckets = new int[m][26];
        ArrayList<String> ans = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            String word = words[i];
            for (int j = 0; j < word.length(); j++) {
                buckets[i][word.charAt(j) - 'a']++;
            }
        }
        for (int j = 0; j < 26; j++) {
            int freq = Integer.MAX_VALUE;
            for (int i = 0; i < m; i++) {
                freq = Math.min(freq, buckets[i][j]);
                if (buckets[i][j] == 0) break;
            }
            while (freq > 0) {
                ans.add(String.valueOf((char) (j + 'a')));
                freq--;
            }
        }
        return ans;
    }


    /**
     * 997. 找到小镇的法官
     */
    public int findJudge(int n, int[][] trust) {
        if (n == 1) return 1;  //没想到
        //人员编号，相信他的人
        HashMap<Integer, HashSet<Integer>> map = new HashMap<>();
        //相信其他人的人员编号
        HashSet<Integer> set = new HashSet<>();
        for (int[] edge : trust) {
            int node1 = edge[0];
            int node2 = edge[1];
            HashSet<Integer> curr = map.getOrDefault(node2, new HashSet<>());
            curr.add(node1);
            map.put(node2, curr);
            set.add(node1);
        }
        for (int i = 1; i <= n; i++) {
            if (!set.contains(i) && map.containsKey(i) && map.get(i).size() == n - 1) {
                return i;
            }
        }
        return -1;
    }


    /**
     * 1122. 数组的相对排序
     */
    public int[] relativeSortArray(int[] arr1, int[] arr2) {
        int[] buckets = new int[1001];
        ArrayList<Integer> ans = new ArrayList<>();
        //记录频次
        for (int nums : arr1) {
            buckets[nums]++;
        }
        //排序，针对 arr2 中的元素，按照 arr2 中顺序排序
        for (int nums : arr2) {
            while (buckets[nums] > 0) {
                ans.add(nums);
                buckets[nums]--;
            }
        }
        //排序，针对非 arr2 中的元素，按照 升序排序
        for (int i = 0; i < 1001; i++) {
            while (buckets[i] > 0) {
                ans.add(i);
                buckets[i]--;
            }
        }
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = ans.get(i);
        }
        return arr1;
    }

    public int[] relativeSortArray01(int[] arr1, int[] arr2) {   //自定义排序
        ArrayList<Integer> list = new ArrayList<>();
        for (int num : arr1) {
            list.add(num);
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < arr2.length; i++) {
            map.put(arr2[i], i);
        }
        list.sort((o1, o2) -> {
            //1、针对 arr2 中存在的元素，优先按照 arr2 中的顺序升序排序
            if (map.containsKey(o1) || map.containsKey(o2)) {
                return map.getOrDefault(o1, 1001) - map.getOrDefault(o2, 1001);
            }
            //2、针对非 arr2 中的元素，按照升序排序
            return o1 - o2;
        });
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = list.get(i);
        }
        return arr1;
    }


    public int[] relativeSortArray02(int[] arr1, int[] arr2) {   //哈希表
        ArrayList<Integer> ans = new ArrayList<>();
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int nums : arr1) {
            Integer freq = map.getOrDefault(nums, 0);
            map.put(nums, freq + 1);
        }
        for (int nums : arr2) {
            Integer freq = map.get(nums);
            while (freq > 0) {
                ans.add(nums);
                freq--;
            }
            map.remove(nums);
        }
        ArrayList<Map.Entry<Integer, Integer>> entries = new ArrayList<>(map.entrySet());
        entries.sort(Comparator.comparing(Map.Entry::getKey));
        for (Map.Entry<Integer, Integer> tuple : entries) {
            Integer nums = tuple.getKey();
            Integer freqs = tuple.getValue();
            while (freqs > 0) {
                ans.add(nums);
                freqs--;
            }
        }
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = ans.get(i);
        }
        return arr1;
    }


    /**
     * 1078. Bigram 分词
     */
    public String[] findOcurrences(String text, String first, String second) {
        ArrayList<String> ans = new ArrayList<>();
        String[] split = text.split(" ");
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals(first) && i + 2 < split.length && split[i + 1].equals(second)) {
                ans.add(split[i + 2]);
            }
        }
        return ans.toArray(new String[0]);
    }


    /**
     * 2373. 矩阵中的局部最大值
     */
    public int[][] largestLocal(int[][] grid) {
        int n = grid.length;
        int[][] ans = new int[n - 2][n - 2];
        int[][] dirs = {{1, 0}, {2, 0}, {0, 1}, {1, 1}, {2, 1}, {0, 2}, {1, 2}, {2, 2}};
        for (int i = 0; i < n - 2; i++) {
            for (int j = 0; j < n - 2; j++) {
                int max = grid[i][j];
                for (int[] dir : dirs) {
                    int nextRow = i + dir[0];
                    int nextCol = j + dir[1];
                    max = Math.max(max, grid[nextRow][nextCol]);
                }
                ans[i][j] = max;
            }
        }
        return ans;
    }


    /**
     * 1189. “气球” 的最大数量
     */
    public int maxNumberOfBalloons(String str) {
        int ans = Integer.MAX_VALUE;
        int[] buckets = new int[26];
        for (int i = 0; i < str.length(); i++) {
            buckets[str.charAt(i) - 'a']++;
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put('b' - 'a', 1);
        map.put('a' - 'a', 1);
        map.put('l' - 'a', 2);
        map.put('o' - 'a', 2);
        map.put('n' - 'a', 1);
        for (int index : map.keySet()) {
            ans = Math.min(ans, buckets[index] / map.get(index));
        }
        return ans;
    }


    /**
     * 1417. 重新格式化字符串
     */
    public String reformat(String str) {   //模拟
        int nums1 = 0;
        int nums2 = 0;
        int[] buckets1 = new int[26];
        int[] buckets2 = new int[10];
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isDigit(ch)) {
                nums2++;
                buckets2[ch - '0']++;
            } else if (Character.isLowerCase(ch)) {
                nums1++;
                buckets1[ch - 'a']++;
            }
        }
        if (Math.abs(nums1 - nums2) > 1) return "";
        if (nums1 >= nums2) return reformatHelper(buckets1, buckets2);
        return reformatHelper(buckets2, buckets1);
    }

    private String reformatHelper(int[] buckets1, int[] buckets2) {
        int n1 = buckets1.length;
        int n2 = buckets2.length;
        StringBuilder ans = new StringBuilder();
        int index1 = 0;
        int index2 = 0;
        while (index1 < n1 || index2 < n2) {
            //1、优先处理字符含量多的类型
            while (index1 < n1 && buckets1[index1] == 0) {
                index1++;
            }
            if (index1 < n1) {
                buckets1[index1]--;
                if (n1 == 10) ans.append(index1);
                else ans.append((char) (index1 + 'a'));
            }

            //2、其次处理字符含量少的类型
            while (index2 < n2 && buckets2[index2] == 0) {
                index2++;
            }
            if (index2 < n2) {
                buckets2[index2]--;
                if (n2 == 10) ans.append(index2);
                else ans.append((char) (index2 + 'a'));
            }
        }
        return ans.toString();
    }


    public String reformat01(String str) {   //模拟
        StringBuilder ans = new StringBuilder();
        char[] arr = str.toCharArray();
        int n = arr.length;
        Arrays.sort(arr);
        for (int i = 0, j = n - 1; i < j; i++, j--) {
            //前半部分出现字母，后半部分出现数字
            if (Character.isLowerCase(arr[i]) || Character.isDigit(arr[j])) {
                return "";
            }
            ans.append(arr[i]);   //数字
            ans.append(arr[j]);   //字母
        }
        //1、偶数位
        if (n % 2 == 0) return ans.toString();
        //2、奇数位
        char ch = arr[n / 2];
        if (Character.isLowerCase(ch)) ans.insert(0, ch);  //若是字符，插入最前面
        else ans.append(ch);  //若是数字，插入最后
        return ans.toString();
    }


    /**
     * 1701. 平均等待时间
     */
    public double averageWaitingTime(int[][] customers) {
        int n = customers.length;
        double diff = customers[0][1];
        int currTime = customers[0][0] + customers[0][1];
        for (int i = 1; i < n; i++) {
            currTime = Math.max(currTime, customers[i][0]);    //如果存在空挡期
            currTime += customers[i][1];
            diff += currTime - customers[i][0];
        }
        return diff * 1.0 / n;
    }


    /**
     * 1324. 竖直打印单词
     */
    public List<String> printVertically(String str) {
        List<String> ans = new ArrayList<>();
        String[] words = str.split(" ");
        int m = words.length;
        int n = 0;
        for (String word : words) {
            n = Math.max(n, word.length());
        }
        char[][] arr = new char[m][n];
        for (int i = 0; i < m; i++) {
            String word = words[i];
            for (int j = 0; j < word.length(); j++) {
                arr[i][j] = word.charAt(j);
            }
        }
        //遍历和拼接
        for (int j = 0; j < n; j++) {
            int row = m - 1;
            //过滤掉末尾空格
            while (row >= 0 && arr[row][j] == 0) {
                row--;
            }
            StringBuilder builder = new StringBuilder();
            for (int i = row; i >= 0; i--) {
                builder.append(arr[i][j] == 0 ? ' ' : arr[i][j]);
            }
            //反转回正序
            ans.add(builder.reverse().toString());
        }
        return ans;
    }

    /**
     * 面试题 05.02. 二进制数转字符串
     */
    public String printBin(double num) {
        StringBuilder ans = new StringBuilder("0.");
        while (ans.length() <= 32 && num != 0) {
            num *= 2;
            int digit = (int) num;
            ans.append(digit);
            num -= digit;
        }
        return num == 0 ? ans.toString() : "ERROR";
    }


    /**
     * 2109. 向字符串添加空格
     */
    public String addSpaces(String str, int[] spaces) {
        StringBuilder ans = new StringBuilder(str);
        for (int i = 0; i < spaces.length; i++) {
            ans.insert(i + spaces[i], " ");   //注意：ans的长度是在动态变化的，所以要加 i
        }
        return ans.toString();
    }

    public String addSpaces01(String str, int[] spaces) {
        StringBuilder ans = new StringBuilder();
        int left = 0;
        for (int right : spaces) {
            ans.append(str.substring(left, right));
            ans.append(" ");
            left = right;
        }
        ans.append(str.substring(left));
        return ans.toString();
    }


    /**
     * 2390. 从字符串中移除星号
     */
    public String removeStars(String str) {
        int n = str.length();
        ArrayDeque<Character> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (Character.isLowerCase(str.charAt(i))) {
                queue.addLast(str.charAt(i));
                continue;
            }
            if (!queue.isEmpty()) {
                queue.pollLast();   //题目保证左侧一定有非零星号，不会存在队列中是 * 的情况，此处不判断队列中的情况
            }
        }
        StringBuilder ans = new StringBuilder();
        while (!queue.isEmpty()) {
            ans.append(queue.pollFirst());
        }
        return ans.toString();
    }

    /**
     * 2120. 执行所有后缀指令
     */
    public int[] executeInstructions(int n, int[] startPos, String str) {
        int m = str.length();
        int[] ans = new int[m];
        HashMap<Character, int[]> dirs = new HashMap<>();
        dirs.put('L', new int[]{0, -1});
        dirs.put('R', new int[]{0, 1});
        dirs.put('U', new int[]{-1, 0});
        dirs.put('D', new int[]{1, 0});
        for (int i = 0; i < m; i++) {
            int steps = 0;
            String path = str.substring(i);
            int row = startPos[0];
            int col = startPos[1];
            for (int j = 0; j < path.length(); j++) {
                int[] dir = dirs.get(path.charAt(j));
                row += dir[0];
                col += dir[1];
                if (row < 0 || row >= n || col < 0 || col >= n) {
                    break;
                }
                steps++;
            }
            ans[i] = steps;
        }
        return ans;
    }


    /**
     * 2105. 给植物浇水 II
     */
    public int minimumRefill(int[] plants, int capacityA, int capacityB) {
        int ans = 0;
        int indexA = 0;
        int indexB = plants.length - 1;
        int currA = capacityA;
        int currB = capacityB;
        while (indexA < indexB) {
            if (currA < plants[indexA]) {
                ans++;
                currA = capacityA;
            }
            currA -= plants[indexA];
            indexA++;

            if (currB < plants[indexB]) {
                ans++;
                currB = capacityB;
            }
            currB -= plants[indexB];
            indexB--;
        }
        if (indexA == indexB) {
            if (Math.max(currA, currB) < plants[indexA]) {
                ans++;
            }
        }
        return ans;
    }


    /**
     * 1487. 保证文件名唯一
     */
    public String[] getFolderNames(String[] names) {
        int n = names.length;
        String[] ans = new String[n];
        HashMap<String, Integer> hTable = new HashMap<>();   //以 key 为前缀的文件夹名，当前可分配的后缀编号为 value
        for (int i = 0; i < n; i++) {
            if (hTable.containsKey(names[i])) {
                Integer k = hTable.get(names[i]);
                //过滤掉已经存在的文件夹名称
                while (hTable.containsKey(addSuffix(names[i], k))) {   //关键
                    k++;
                }
                String nextName = addSuffix(names[i], k);
                ans[i] = nextName;
                hTable.put(nextName, 1);   //维护新增的文件名，及以其作为前缀的下一个可用的后缀编号
                hTable.put(names[i], k + 1);   //维护当前的文件夹名，及以其作为前缀的下一个可用的后缀编号
            } else {
                ans[i] = names[i];
                hTable.put(names[i], 1);
            }
        }
        return ans;
    }

    private String addSuffix(String name, int k) {
        return name + "(" + k + ")";
    }


    public String[] getFolderNames01(String[] names) {  //错误写法
        int n = names.length;
        String[] ans = new String[n];
        HashMap<String, Integer> hTable = new HashMap<>();   //以 key 为前缀的文件夹名，当前可分配的后缀编号为 value
        for (int i = 0; i < n; i++) {
            if (hTable.containsKey(names[i])) {
                String nextName = names[i] + "(" + hTable.get(names[i]) + ")";
                ans[i] = nextName;
                hTable.put(nextName, 1);   //维护新增的文件名，及以其作为前缀的下一个可用的后缀编号
                hTable.put(names[i], hTable.get(names[i]) + 1);   //维护当前的文件夹名，及以其作为前缀的下一个可用的后缀编号
            } else {
                ans[i] = names[i];
                hTable.put(names[i], 1);
            }
        }
        return ans;
    }


    /**
     * 1535. 找出数组游戏的赢家
     */
    public int getWinner(int[] arr, int k) {
        int winner = arr[0];
        int freq = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < winner) {
                ++freq;
            } else {
                winner = arr[i];
                freq = 1;
            }
            if (freq == k) return winner;  //一定放外面，因为 if/else 均可能满足条件
        }
        return winner;  //数组遍历结束，仍没有返回结果，则说明当前记录的最大值，就是 winner，其将大于头部的数字
    }

    public int getWinner01(int[] arr, int k) {    //暴力，超时
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int num : arr) {
            queue.addLast(num);
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        while (true) {
            Integer xx = queue.pollFirst();
            Integer yy = queue.pollFirst();
            if (xx > yy) {
                //为胜利者添加胜利次数
                Integer freq = map.getOrDefault(xx, 0);
                freq++;
                if (freq == k) return xx;
                map.put(xx, freq);
                map.put(yy, 0);
                //有序放回队列
                queue.addFirst(xx);
                queue.addLast(yy);
            }
            if (yy > xx) {
                //为胜利者添加胜利次数
                Integer freq = map.getOrDefault(yy, 0);
                freq++;
                if (freq == k) return yy;
                map.put(yy, freq);
                map.put(xx, 0);
                //有序放回队列
                queue.addFirst(yy);
                queue.addLast(xx);
            }
        }
    }


    /**
     * 1914. 循环轮转矩阵
     */
    public int[][] rotateGrid(int[][] grid, int k) {
        int m = grid.length;
        int n = grid[0].length;
        int layers = Math.min(m / 2, n / 2);
        for (int layer = 0; layer < layers; layer++) {
            ArrayDeque<Integer> queue = new ArrayDeque<>();
            //1、从左向右，逐个添加元素
            for (int j = layer; j < n - layer; j++) {
                queue.addLast(grid[layer][j]);
            }
            //2、从上向下，逐个添加元素
            for (int i = layer + 1; i < m - layer; i++) {
                queue.addLast(grid[i][n - layer - 1]);
            }
            //3、从右向左，逐个添加元素
            for (int j = n - layer - 2; j >= layer; j--) {
                queue.addLast(grid[m - layer - 1][j]);
            }
            //4、从下向上，逐个添加元素
            for (int i = m - layer - 2; i > layer; i--) {
                queue.addLast(grid[i][layer]);
            }

            //---------------------------------------------
            // 逆时针移动 K 个位置
            //---------------------------------------------
            int kk = k % queue.size();   //否则超时
            for (int i = 0; i < kk && !queue.isEmpty(); i++) {
                queue.addLast(queue.pollFirst());
            }

            //1、从左向右，逐个添加元素
            for (int j = layer; j < n - layer; j++) {
                grid[layer][j] = queue.pollFirst();
            }
            //2、从上向下，逐个添加元素
            for (int i = layer + 1; i < m - layer; i++) {
                grid[i][n - layer - 1] = queue.pollFirst();
            }
            //3、从右向左，逐个添加元素
            for (int j = n - layer - 2; j >= layer; j--) {  //等于号
                grid[m - layer - 1][j] = queue.pollFirst();
            }
            //4、从下向上，逐个添加元素
            for (int i = m - layer - 2; i > layer; i--) {
                grid[i][layer] = queue.pollFirst();
            }
        }
        return grid;
    }

    /**
     * 867. 转置矩阵
     */
    public int[][] transpose(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int[][] grids = new int[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                grids[j][i] = matrix[i][j];
            }
        }
        return grids;
    }


    /**
     * 883. 三维形体投影面积
     */
    public int projectionArea(int[][] grid) {
        int sumArea = 0;
        int m = grid.length;
        int n = grid[0].length;
        int[] buckets1 = new int[m];
        int[] buckets2 = new int[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                buckets1[i] = Math.max(buckets1[i], grid[i][j]);
                buckets2[j] = Math.max(buckets2[j], grid[i][j]);
                if (grid[i][j] > 0) sumArea++;
            }
        }
        for (int i = 0; i < m; i++) {
            sumArea += buckets1[i];
        }
        for (int j = 0; j < n; j++) {
            sumArea += buckets2[j];
        }
        return sumArea;
    }


    /**
     * 892. 三维形体的表面积
     */
    public int surfaceArea(int[][] grid) {
        int sumArea = 0;
        int m = grid.length;
        int n = grid[0].length;
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] > 0) {
                    //1、四周无覆盖的面积
                    int currArea = grid[i][j] * 4 + 2;
                    //2、移除四周覆盖的面积
                    for (int[] dir : dirs) {
                        int nextRow = i + dir[0];
                        int nextCol = j + dir[1];
                        if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                            currArea -= Math.min(grid[i][j], grid[nextRow][nextCol]);
                        }
                    }
                    sumArea += currArea;
                }
            }
        }
        return sumArea;
    }

    public int surfaceArea03(int[][] grid) {   //错误写法
        int nums = 0;
        int sumArea = 0;
        int innerArea = 0;
        int m = grid.length;
        int n = grid[0].length;
        int[] buckets1 = new int[m];
        int[] buckets2 = new int[n];
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        //内部凹陷的面积
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                //记录投影面积（侧面）
                buckets1[i] = Math.max(buckets1[i], grid[i][j]);
                buckets2[j] = Math.max(buckets2[j], grid[i][j]);
                //记录投影面积（底面）
                if (grid[i][j] > 0) sumArea++;
                //记录内部凹陷导致未投影的面积（只能考虑到相邻的，远距离的考虑不到，错误写法点）
                if (i > 0 && i < m - 1 && j > 0 && j < n - 1) {
                    int minHeight = Integer.MAX_VALUE;
                    for (int[] dir : dirs) {
                        int nextRow = i + dir[0];
                        int nextCol = j + dir[1];
                        minHeight = Math.min(minHeight, grid[nextRow][nextCol]);
                    }
                    if (grid[i][j] < minHeight) {
                        nums += minHeight - grid[i][j];
                    }
                }

                //记录边界凹陷导致未投影的面积（只能考虑到相邻的，远距离考虑不到，同时四个顶点重复计算，错误写法点）
                if ((i == 0 || i == m - 1) && grid[i][j] == 0) {
                    if (j - 1 >= 0) innerArea += grid[i][j - 1];
                    if (j + 1 < n) innerArea += grid[i][j + 1];
                }
                if ((j == 0 || j == n - 1) && grid[i][j] == 0) {
                    if (i - 1 >= 0) innerArea += grid[i - 1][j];
                    if (i + 1 < m) innerArea += grid[i + 1][j];
                }
            }
        }
        for (int i = 0; i < m; i++) {
            sumArea += buckets1[i];
        }
        for (int j = 0; j < n; j++) {
            sumArea += buckets2[j];
        }
        return sumArea * 2 + nums * 4 + innerArea;
    }


    /**
     * 1599. 经营摩天轮的最大利润
     */
    public int minOperationsMaxProfit(int[] customers, int boardingCost, int runningCost) {
        int waitNums = 0;
        int n = customers.length;
        ArrayList<Integer> everyProfit = new ArrayList<>();
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.addLast(0);
        queue.addLast(0);
        queue.addLast(0);
        queue.addLast(0);
        int currIndex = 0;
        while (currIndex < n || waitNums > 0) {
            //后续仍有新到游客，进入等待区
            if (currIndex < n) {
                waitNums += customers[currIndex];    //当前等候的人数
            }
            int upNums = Math.min(4, waitNums);      //当前上仓的人数
            waitNums -= upNums;                      //更新等候的人数

            everyProfit.add(upNums * boardingCost - runningCost);    //此轮收益

            //1、着陆的人下来，无论是多少
            queue.pollFirst();
            //2、本轮登仓
            queue.addLast(upNums);

            currIndex++;
        }
        int ans = 0;
        int maxProfit = 0;
        int m = everyProfit.size();
        int[] prefix = new int[m + 1];
        for (int i = 0; i < m; i++) {
            prefix[i + 1] = prefix[i] + everyProfit.get(i);
            if (prefix[i + 1] > maxProfit) {
                maxProfit = prefix[i + 1];
                ans = i + 1;
            }
        }
        return maxProfit <= 0 ? -1 : ans;
    }


    /**
     * 2073. 买票需要的时间
     */
    public int timeRequiredToBuy(int[] tickets, int k) {
        int ans = 0;
        while (true) {
            for (int i = 0; i < tickets.length; i++) {
                if (tickets[i] > 0) {
                    tickets[i]--;
                    ans++;
                }
                if (i == k && tickets[k] == 0) return ans;
            }
        }
    }


    /**
     * 1572. 矩阵对角线元素的和
     */
    public int diagonalSum(int[][] mat) {
        int n = mat.length;
        int sum = 0;
        int row = 0;
        int col = 0;
        while (row < n && col < n) {
            sum += mat[row][col];
            row++;
            col++;
        }
        row = 0;
        col = n - 1;
        while (row < n && col >= 0) {
            sum += row == col ? 0 : mat[row][col];
            row++;
            col--;
        }
        return sum;
    }


    /**
     * 1380. 矩阵中的幸运数
     */
    public List<Integer> luckyNumbers(int[][] matrix) {
        List<Integer> ans = new ArrayList<>();
        int m = matrix.length;
        int n = matrix[0].length;
        int[] min = new int[m];
        Arrays.fill(min, Integer.MAX_VALUE);
        int[] max = new int[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                min[i] = Math.min(min[i], matrix[i][j]);
                max[j] = Math.max(max[j], matrix[i][j]);
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == min[i] && matrix[i][j] == max[j]) {
                    ans.add(matrix[i][j]);
                }
            }
        }
        return ans;
    }


    /**
     * 922. 按奇偶排序数组 II
     */
    public int[] sortArrayByParityII(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        ArrayList<Integer> one = new ArrayList<>();
        ArrayList<Integer> two = new ArrayList<>();
        for (int num : nums) {
            if ((num & 1) == 0) two.add(num);
            else one.add(num);
        }
        int index = 0;
        for (int i = 0; i < n; i += 2) {
            nums[i] = two.get(index);
            nums[i + 1] = one.get(index);
            index++;
        }
        return nums;
    }

    public int[] sortArrayByParityII01(int[] nums) {
        int n = nums.length;
        int[] ans = new int[n];
        int index = 0;
        for (int num : nums) {
            if (num % 2 == 0) {
                ans[index] = num;
                index += 2;
            }
        }
        index = 1;
        for (int num : nums) {
            if (num % 2 != 0) {
                ans[index] = num;
                index += 2;
            }
        }
        return ans;
    }

    /**
     * 2500. 删除每行中的最大值
     */
    public int deleteGreatestValue(int[][] grid) {
        int ans = 0;
        int m = grid.length;
        int n = grid[0].length;
        for (int[] nums : grid) {
            Arrays.sort(nums);
        }
        for (int j = 0; j < n; j++) {
            int max = 0;
            for (int i = 0; i < m; i++) {
                max = Math.max(max, grid[i][j]);
            }
            ans += max;
        }
        return ans;
    }

    /**
     * 2379. 得到 K 个黑块的最少涂色次数
     */
    public int minimumRecolors(String blocks, int k) {
        int left = 0;
        int right = 0;
        int n = blocks.length();
        int num = 0;
        int min = n;
        while (right < n) {
            num += blocks.charAt(right) == 'W' ? 1 : 0;
            if (right - left + 1 > k) {
                num += blocks.charAt(left) == 'W' ? -1 : 0;
                left++;
            }
            if (right - left + 1 == k) {
                min = Math.min(min, num);
            }
            right++;
        }
        return min;
    }


    /**
     * 2133. 检查是否每一行每一列都包含全部整数
     */
    public boolean checkValid(int[][] matrix) {
        int n = matrix.length;
        HashSet<Integer> set = new HashSet<>();
        //逐行校验
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (set.contains(matrix[i][j])) return false;
                set.add(matrix[i][j]);
            }
            set.clear();
        }
        //逐列校验
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                if (set.contains(matrix[i][j])) return false;
                set.add(matrix[i][j]);
            }
            set.clear();
        }
        return true;
    }


    /**
     * 1576. 替换所有的问号
     */
    public String modifyString(String str) {
        int n = str.length();
        char[] arr = str.toCharArray();
        for (int i = 0; i < n; i++) {
            if (arr[i] == '?') {
                for (char xx = 'a'; xx <= 'z'; xx++) {
                    if ((i > 0 && arr[i - 1] == xx) || (i < n - 1 && arr[i + 1] == xx)) {
                        continue;
                    }
                    arr[i] = xx;
                    break;
                }
            }
        }
        return new String(arr);
    }


    /**
     * 1422. 分割字符串的最大得分
     */
    public int maxScore(String str) {
        int max = 0;
        int nums1 = (str.charAt(0) == '0' ? 1 : 0);
        int nums2 = 0;
        int n = str.length();
        for (int i = 1; i < n; i++) {
            if (str.charAt(i) == '1') nums2++;
        }
        for (int i = 1; i < n - 1; i++) {
            max = Math.max(max, nums1 + nums2);
            if (str.charAt(i) == '0') nums1++;
            if (str.charAt(i) == '1') nums2--;
        }
        return Math.max(max, nums1 + nums2);
    }

    /**
     * 1252. 奇数值单元格的数目
     */
    public int oddCells(int m, int n, int[][] indices) {
        int[] row = new int[m];
        int[] col = new int[n];
        for (int[] edge : indices) {
            int xx = edge[0];
            int yy = edge[1];
            row[xx]++;
            col[yy]++;
        }
        int[][] grids = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(grids[i], row[i]);
        }
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                grids[i][j] += col[j];
            }
        }
        int ans = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if ((grids[i][j] & 1) != 0) ans++;
            }
        }
        return ans;
    }


    /**
     * 1389. 按既定顺序创建目标数组
     */
    public int[] createTargetArray(int[] nums, int[] index) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < index.length; i++) {
            list.add(index[i], nums[i]);
        }
        int[] ans = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ans[i] = list.get(i);
        }
        return ans;
    }


    /**
     * 1184. 公交站间的距离
     */
    public int distanceBetweenBusStops(int[] distance, int start, int destination) {
        int sum1 = 0;
        int sum2 = 0;
        int left = Math.min(start, destination);
        int right = Math.max(start, destination);
        for (int i = 0; i < distance.length; i++) {
            if (left <= i && i < right) sum1 += distance[i];
            else sum2 += distance[i];
        }
        return Math.min(sum1, sum2);
    }


    /**
     * 1207. 独一无二的出现次数
     */
    public boolean uniqueOccurrences(int[] arr) {
        int[] buckets = new int[2002];
        for (int num : arr) {
            buckets[num + 1000]++;
        }
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < 2002; i++) {
            if (buckets[i] == 0) continue;
            if (set.contains(buckets[i])) return false;
            set.add(buckets[i]);
        }
        return true;
    }


    /**
     * 1450. 在既定时间做作业的学生人数
     */
    public int busyStudent(int[] startTime, int[] endTime, int queryTime) {
        int ans = 0;
        int n = startTime.length;
        ArrayList<int[]> sort = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            sort.add(new int[]{startTime[i], endTime[i]});
        }
        sort.sort((o1, o2) -> o1[0] - o2[0]);  //按照开始时间升序排序
        for (int i = 0; i < n; i++) {
            if (sort.get(i)[0] > queryTime) return ans;
            if (sort.get(i)[1] >= queryTime) ans++;
        }
        return ans;
    }

    public int busyStudent01(int[] startTime, int[] endTime, int queryTime) {
        int ans = 0;
        int n = startTime.length;
        for (int i = 0; i < n; i++) {
            if (startTime[i] <= queryTime && queryTime <= endTime[i]) ans++;
        }
        return ans;
    }

    /**
     * 2469. 温度转换
     */
    public double[] convertTemperature(double celsius) {
        return new double[]{celsius + 273.15, celsius * 1.80 + 32.00};
    }


    /**
     * 1861. 旋转盒子
     */
    public char[][] rotateTheBox(char[][] box) {
        int m = box.length;
        int n = box[0].length;
        char[][] ans = new char[n][m];
        for (int i = 0; i < n; i++) {
            Arrays.fill(ans[i], '.');
        }
        int col = m - 1;
        for (int i = 0; i < m; i++) {
            int row = n - 1;
            for (int j = n - 1; j >= 0; j--) {
                if (box[i][j] == '*') {
                    ans[j][col] = '*';
                    row = j - 1;  //下一个可写入的位点
                }
                if (box[i][j] == '#') {
                    ans[row][col] = '#';
                    row--;
                }
            }
            col--;
        }
        return ans;
    }


    /**
     * 2169. 得到 0 的操作数
     */
    public int countOperations(int num1, int num2) {
        int max = Math.max(num1, num2);
        int min = Math.min(num1, num2);
        int time = 0;
        while (max > 0 && min > 0) {
            int next = max - min;
            max = Math.max(next, min);
            min = Math.min(next, min);
            time++;
        }
        return time;
    }


    private static int[] prefix = new int[13];
    private static int[] monthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    static {
        for (int i = 0; i < 12; i++) {
            prefix[i + 1] = prefix[i] + monthDays[i];
        }
    }

    /**
     * 2409. 统计共同度过的日子数
     */
    public int countDaysTogether(String arriveAlice, String leaveAlice, String arriveBob, String leaveBob) {
        int[] edge1 = new int[]{DaysHelper(arriveAlice), DaysHelper(leaveAlice)};
        int[] edge2 = new int[]{DaysHelper(arriveBob), DaysHelper(leaveBob)};
        int[][] edge = new int[][]{edge1, edge2};
        Arrays.sort(edge, (o1, o2) -> o1[0] - o2[0]);  //按照开始时间，升序排序
        if (edge[0][1] < edge[1][0]) return 0;
        else if (edge[1][1] <= edge[0][1]) return edge[1][1] - edge[1][0] + 1;
        else return edge[0][1] - edge[1][0] + 1;
    }

    private int DaysHelper(String str) {
        String[] tuple = str.split("-");
        int month = Integer.parseInt(tuple[0]);
        int day = Integer.parseInt(tuple[1]);
        return prefix[month - 1] + day;  //关键：month - 1
    }


    /**
     * 6200. 处理用时最长的那个任务的员工
     */
    public int hardestWorker(int n, int[][] logs) {
        int m = logs.length;
        int[][] edges = new int[m][2];
        edges[0][0] = logs[0][0];
        edges[0][1] = logs[0][1];
        for (int i = 1; i < m; i++) {
            edges[i][0] = logs[i][0];
            edges[i][1] = logs[i][1] - logs[i - 1][1];
        }
        Arrays.sort(edges, (o1, o2) -> {
            if (o1[1] != o2[1]) return o2[1] - o1[1];
            return o1[0] - o2[0];
        });
        return edges[0][0];
    }

    public int hardestWorker01(int n, int[][] logs) {
        int targetID = logs[0][0];
        int maxTimes = logs[0][1];
        for (int i = 1; i < logs.length; i++) {
            int curID = logs[i][0];
            int curTimes = logs[i][1] - logs[i - 1][1];
            if (curTimes > maxTimes || (curTimes == maxTimes && curID < targetID)) {
                targetID = curID;
                maxTimes = curTimes;
            }
        }
        return targetID;
    }


    /**
     * 2437. 有效时间的数目
     */
    public int countTime(String time) {
        int res = 1;
        char a = time.charAt(0);
        char b = time.charAt(1);
        char c = time.charAt(3);
        char d = time.charAt(4);
        if (d == '?') res *= 10;
        if (c == '?') res *= 6;
        if (b == '?' && a == '?') res *= 24;
        else if (b == '?') {
            if (a == '0' || a == '1') res *= 10;
            else res *= 4;
        } else if (a == '?') {
            if (b == '0' || b == '1' || b == '2' || b == '3') res *= 3;
            else res *= 2;
        }
        return res;
    }


    /**
     * 949. 给定数字能组成的最大时间
     */
    public String largestTimeFromDigits(int[] arr) {
        int maxTime = -1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == j) continue;
                for (int m = 0; m < 4; m++) {
                    if (m == i || m == j) continue;
                    int n = 6 - i - j - m;
                    int hour = 10 * arr[i] + arr[j];
                    int minute = 10 * arr[m] + arr[n];
                    if (hour < 24 && minute < 60) {
                        maxTime = Math.max(maxTime, 60 * hour + minute);
                    }
                }
            }
        }
        return maxTime == -1 ? "" : String.format("%02d:%02d", maxTime / 60, maxTime % 60);
    }


    /**
     * 204. 计数质数
     */
    public int countPrimes(int n) {
        int ans = 0;
        for (int i = 2; i < n; i++) {
            ans += isPrimes(i) ? 1 : 0;
        }
        return ans;
    }

    private boolean isPrimes(int x) {
        for (int i = 2; i * i <= x; i++) {
            if (x % i == 0) return false;
        }
        return true;
    }


    /**
     * 2201. 统计可以提取的工件
     */
    public int digArtifacts(int n, int[][] artifacts, int[][] dig) {
        int ans = 0;
        int[][] grids = new int[n][n];
        for (int[] edge : dig) {
            grids[edge[0]][edge[1]] = 1;
        }
        for (int[] edge : artifacts) {
            int xx1 = edge[0];
            int yy1 = edge[1];
            int xx2 = edge[2];
            int yy2 = edge[3];
            int ready = 1;
            for (int i = xx1; i <= xx2; i++) {
                for (int j = yy1; j <= yy2; j++) {
                    if (grids[i][j] == 0) {
                        ready = 0;
                        break;
                    }
                }
                if (ready == 0) break;
            }
            ans += ready;
        }
        return ans;
    }

    /**
     * 1016. 子串能表示从 1 到 N 数字的二进制串
     */
    public boolean queryString(String s, int n) {
        for (int i = 1; i <= n; i++) {
            if (!s.contains(Integer.toBinaryString(i))) return false;
        }
        return true;
    }


    /**
     * 2086. 从房屋收集雨水需要的最少水桶数
     */
    public int minimumBuckets(String hamsters) {
        if (hamsters.equals("H") || hamsters.contains("HHH") || hamsters.startsWith("HH") || hamsters.endsWith("HH")) {
            return -1;
        }
        int ans = 0;
        for (int i = 0; i < hamsters.length(); i++) {
            if (hamsters.charAt(i) == 'H') ans++;
        }
        for (int i = 0; i < hamsters.length() - 2; i++) {
            if (hamsters.charAt(i) == 'H' && hamsters.charAt(i + 1) == '.' && hamsters.charAt(i + 2) == 'H') {
                ans--;
                i += 2;
            }
        }
        return ans;
    }

    public int minimumBuckets01(String hamsters) {  //超时，通过上面方法优化
        if (hamsters.equals("H") || hamsters.contains("HHH") || hamsters.startsWith("HH") || hamsters.endsWith("HH")) {
            return -1;
        }
        int ans = 0;
        while (hamsters.contains("H.H")) {
            hamsters = hamsters.replaceFirst("H\\.H", "...");
            ans++;
        }
        for (int i = 0; i < hamsters.length(); i++) {
            if (hamsters.charAt(i) == 'H') ans++;
        }
        return ans;
    }

    public int minimumBuckets02(String hamsters) {
        int res = 0;
        char[] arr = hamsters.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 'H') {
                //1、左侧已经放置了水桶
                if (i > 0 && arr[i - 1] == 'B') {
                    continue;
                } else if (i < arr.length - 1 && arr[i + 1] == '.') {  //2、左侧无桶，首先，尝试在右侧新增桶
                    res++;
                    arr[i + 1] = 'B';
                } else if (i > 0 && arr[i - 1] == '.') {   //2、左侧无桶，其次，尝试在左侧新增桶
                    res++;
                    arr[i - 1] = 'B';
                } else {
                    return -1;
                }
            }
        }
        return res;
    }

    /**
     * 1455. 检查单词是否为句中其他单词的前缀
     */
    public int isPrefixOfWord(String sentence, String searchWord) {
        String[] arr = sentence.split("\\s+");
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].startsWith(searchWord)) return i;
        }
        return -1;
    }


    /**
     * 1431. 拥有最多糖果的孩子
     */
    public List<Boolean> kidsWithCandies(int[] candies, int extraCandies) {
        int max = 0;
        for (int nums : candies) {
            max = Math.max(max, nums);
        }
        ArrayList<Boolean> res = new ArrayList<>();
        for (int i = 0; i < candies.length; i++) {
            if (candies[i] + extraCandies >= max) res.add(true);
            else res.add(false);
        }
        return res;
    }


    /**
     * 6430. 找出转圈游戏输家
     */
    public int[] circularGameLosers(int n, int k) {
        int[] buckets = new int[n];
        int currIndex = 0;
        int times = 1;
        buckets[currIndex]++;
        while (true) {
            int nextIndex = (currIndex + times * k) % n;
            buckets[nextIndex]++;
            if (buckets[nextIndex] == 2) break;
            currIndex = nextIndex;
            times++;
        }
        ArrayList<Integer> ans = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (buckets[i] == 0) ans.add(i);
        }
        int[] res = new int[ans.size()];
        for (int i = 0; i < ans.size(); i++) {
            res[i] = ans.get(i) + 1;
        }
        return res;
    }

    /**
     * 2544. 交替数字和
     */
    public int alternateDigitSum(int n) {
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        while (n > 0) {
            int res = n % 10;
            queue.addFirst(res);
            n /= 10;
        }
        int ans = 0;
        int freq = 1;
        while (!queue.isEmpty()) {
            Integer curr = queue.pollFirst();
            if ((freq & 1) == 0) {
                ans -= curr;
            } else {
                ans += curr;
            }
            freq++;
        }
        return ans;
    }




}
