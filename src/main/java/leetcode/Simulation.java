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


}
