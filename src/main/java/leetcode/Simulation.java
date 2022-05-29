package leetcode;


import java.util.*;
import java.util.stream.Collectors;

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
        StringBuilder builder = new StringBuilder();
        while (columnNumber > 0) {
            columnNumber--; //左移一位
            builder.append((char) (columnNumber % 26 + 'A'));   //取余，依次追加，故后面要倒序
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


    /**
     * 434. 字符串中的单词数
     */
    public int countSegments(String s) {
        long count = Arrays.stream(s.split(" "))
                .filter(t -> !t.equals(""))
                .count();
        return (int) count;
    }

    /**
     * 495. 提莫攻击
     * 暴力解法：超时
     */
    public int findPoisonedDuration(int[] timeSeries, int duration) {
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
    // 时间复杂度低，偶数区间无序遍历，奇数区间直接反转，时间复杂度不到 Q(n)
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
            //    1、根据 i 搭配的人 match[i]，获取 i 对 match[i] 的亲密程度 index
            //    2、对于 i，查询与其亲密程度 小于 index的人的搭配情况
            //--------------------------------------
            int maxIndex = orders[i][match[i]];    // i当前搭配的是 match[i]，故要从 i的亲密列表中找到此人的位置（亲密程度）
            // 故从 i的亲密列表中（亲密程度截至当前搭配人的亲密程度 maxIndex）
            for (int j = 0; j < maxIndex; j++) {   // maxIndex是亲密程度，而不是个人
                int u = preferences[i][j];         // 对于 i，其亲密程度小于 maxIndex的人 u
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


}
