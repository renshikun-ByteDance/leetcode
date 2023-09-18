package leetcode.algorithm;

import java.util.*;

/**
 * 递归算法
 */
public class BackTrack {

    /**
     * 2293. 极大极小游戏
     */
    public int minMaxGame(int[] nums) {
        int n = nums.length;
        //递归终止条件
        if (n == 1) {
            return nums[0];
        }
        int[] newNums = new int[n / 2];
        for (int i = 0; i < newNums.length; i++) {
            if ((i & 1) == 0) newNums[i] = Math.min(nums[2 * i], nums[2 * i + 1]);
            if ((i & 1) != 0) newNums[i] = Math.max(nums[2 * i], nums[2 * i + 1]);
        }
        return minMaxGame(newNums);  //进入下一轮递归
    }

    /**
     * 241. 为运算表达式设计优先级
     */
    public List<Integer> diffWaysToCompute(String expression) {
        return diffWaysToComputeDfs(expression, 0, expression.length() - 1);
    }

    private List<Integer> diffWaysToComputeDfs(String expression, int left, int right) {
        List<Integer> ans = new ArrayList<>();
        for (int i = left; i <= right; i++) {  //当前字符串中的多个符号，从左至右依次成为优先级最高的符号
            //跳过数字，逐一找到符号
            if (Character.isDigit(expression.charAt(i))) continue;
            List<Integer> xx = diffWaysToComputeDfs(expression, left, i - 1);  // i 为符号
            List<Integer> yy = diffWaysToComputeDfs(expression, i + 1, right);  // i 为符号
            for (int x : xx) {
                for (int y : yy) {
                    int current = 0;
                    if (expression.charAt(i) == '+')
                        current = x + y;
                    if (expression.charAt(i) == '-')
                        current = x - y;
                    if (expression.charAt(i) == '*')
                        current = x * y;
                    ans.add(current);
                }
            }
        }
        if (ans.isEmpty()) {   //结果集是空，说明递归到底了，即该字符串是个数字，将数字加入结果集
            int current = 0;
            while (left <= right) {
                current = current * 10 + (expression.charAt(left) - '0');  //单个字符串的加法
                left++;
            }
            ans.add(current);
        }
        return ans;
    }


    /**
     * 459. 重复的子字符串
     */
    public boolean repeatedSubstringPattern(String str) {
        int len = str.length();
        char start = str.charAt(0);
        char end = str.charAt(str.length() - 1);
        for (int i = 0; i < str.length() / 2; i++) {   //逐个字符遍历，寻找可作为子串结尾的字符
            if (str.charAt(i) == end) {
                String word = str.substring(0, i + 1);
                if (len % (i + 1) == 0 && repeatedSubstringPatternDfs(str, word)) {   //校验，以此结尾是否可行
                    return true;
                }
            }
        }
        return false;
    }

    private boolean repeatedSubstringPatternDfs(String str, String word) {
        String xx = str.replaceAll(word, "");
        return xx.length() == 0;
    }

    public boolean repeatedSubstringPattern01(String s) {
        return (s + s).indexOf(s, 1) != s.length();
    }


    /**
     * 面试题 01.09. 字符串轮转
     */
    public boolean isFlipedString(String s1, String s2) {
        return s1.length() == s2.length() && (s1 + s1).contains(s2);
    }


    /**
     * 395. 至少有 K 个重复字符的最长子串
     */
    public int longestSubstring(String str, int k) {
        return longestSubstringHelper(str, 0, str.length() - 1, k);
    }

    private int longestSubstringHelper(String str, int left, int right, int k) {
        //递归终止条件：整体长度不满足条件
        if (right - left + 1 < k) {
            return 0;
        }
        //记录当前字串中各个字符的频次，以此为基准进行切分，分治
        int[] freq = new int[26];
        for (int i = left; i <= right; i++) {
            freq[str.charAt(i) - 'a']++;
        }
        //跳过两端不满足情况的字符
        while (left < right && freq[str.charAt(left) - 'a'] < k) {
            left++;
        }
        while (left < right && freq[str.charAt(right) - 'a'] < k) {
            right--;
        }
        //横向搜索，寻找新的分割点
        for (int i = left; i <= right; i++) {
            if (freq[str.charAt(i) - 'a'] < k) {  //新的分割点，多个
                return Math.max(longestSubstringHelper(str, left, i - 1, k), longestSubstringHelper(str, i + 1, right, k));
            }
        }
        return right - left + 1;  //运行到这里说明，当前字符串满足条件（上面没有分割点），直接返回此字符串的长度
    }

    public int longestSubstring01(String str, int k) {  //分割字符串
        if (str.length() < k) {
            return 0;
        }
        //每一个递归层，都要统计当前字符串中各个字符出现的频次，从而寻找分割位点，进行分治递归
        int[] freq = new int[26];
        for (int i = 0; i < str.length(); i++) {
            freq[str.charAt(i) - 'a']++;
        }
        for (int i = 0; i < freq.length; i++) {
            if (freq[i] != 0 && freq[i] < k) {  //分割位点
                int maxWindow = 0;
                for (String xx : str.split(String.valueOf((char) (i + 'a')))) {
                    maxWindow = Math.max(maxWindow, longestSubstring01(xx, k));
                }
                return maxWindow;
            }
        }
        //在上面没有进入递归，说明当前字符串中没有可分割的位点，同时长度也满足条件
        return str.length();
    }


    public int longestSubstring00(String s, int k) {
        return dfs(s, k);
    }

    private int dfs(String s, int k) {
        //统计每个字符出现的次数
        int[] charCount = new int[26];
        for (int i = 0; i < s.length(); i++) {
            charCount[s.charAt(i) - 'a']++;
        }

        //统计<k的字符
        String split = "";
        for (int i = 0; i < charCount.length; i++) {
            int count = charCount[i];
            if (count > 0 && count < k) {
                split = String.valueOf((char) (i + 'a'));
                break;
            }
        }

        if (split.equals("")) {
            //全部都>k
            return s.length();
        }

        final String[] split1 = s.split(split);
        int max = 0;
        for (final String s1 : split1) {
            final int dfs = dfs(s1, k);
            max = Math.max(max, dfs);
        }

        return max;
    }


    int ans = 0;
    int currentIndex = 0;

    /**
     * 902. 最大为 N 的数字组合
     */
    public int atMostNGivenDigitSet(String[] digits, int n) {
        String str = String.valueOf(n);
        int nums = digits.length;
        //1、计算长度小于目标的数字个数
        for (int i = 1; i < str.length(); i++) {
            ans += (int) Math.pow(nums, str.length() - i);  //累加长度为 str.length() - i 的数字个数，每一位可以为 digits 中的任意一个元素
        }
        //2、计算长度等于目标的满足条件（小于等于 n）的数字的个数
        atMostNGivenDigitSetDfs(digits, str, nums);  //递归处理和累加
        return ans;
    }

    //针对长度相等的组合数字
    private void atMostNGivenDigitSetDfs(String[] digits, String str, int nums) {
        //递归终止条件
        if (currentIndex == str.length()) {
            ans++;    //一摸一样
            return;
        }
        char xx = str.charAt(currentIndex);
        for (String digit : digits) {  //非递减顺序排列
            //----------------------------------------------
            // 按顺序处理：
            //   1、针对当前位置小于目标中对应位置的情况，直接累加计算
            //   2、针对当前位置大于目标中对应位置的情况，直接结束
            //   3、针对当前位置等于目标中对应位置的情况，递归类似的处理，比较下一位与目标中对应位置的情况
            //----------------------------------------------
            if (digit.charAt(0) < xx) {
                ans += (int) Math.pow(nums, str.length() - 1 - (currentIndex + 1) + 1);  //-1是不包含当前位置的情况，上面的是包含当前位置的情况
            } else if (xx == digit.charAt(0)) {
                ++currentIndex;
                atMostNGivenDigitSetDfs(digits, str, nums);  //比较下一位的情况
            }
        }
    }

    /**
     * 779. 第K个语法符号
     */
    public int kthGrammar(int n, int k) {
        int reverseNums = 0;
        while (n > 0) {
            reverseNums += ((k & 1) == 1 ? 0 : 1);   // K 位于奇数位，则与上一轮的对应位置，值相等，无需反转，故为 0，否则为 1
            k = (k / 2 + (k % 2 == 1 ? 1 : 0));  //向上取整，迭代，取决于上一轮的第 K 位
            n--;
        }
        return (reverseNums & 1) == 1 ? 1 : 0;  //相对于 0 的反转次数，如果为奇数，则为 1 ，否则为 0
    }

    /**
     * 1003. 检查替换后的词是否有效
     */
    public boolean isValid(String str) {   //递归
        if (str.equals("")) return true;
        if (!str.contains("abc")) return false;
        return isValid(str.replaceAll("abc", ""));
    }

    public boolean isValid01(String str) {  //单调栈
        StringBuilder queue = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            queue.append(str.charAt(i));
            if (queue.toString().endsWith("abc")) {
                queue = new StringBuilder(queue.substring(0, queue.length() - 3));
            }
        }
        return queue.length() == 0;
    }


    /**
     * 1910. 删除一个字符串中所有出现的给定子字符串
     */
    public String removeOccurrences(String str, String part) {
        if (!str.contains(part)) return str;
        return removeOccurrences(str.replaceFirst(part, ""), part);  //从左至右
    }

    public String removeOccurrences01(String str, String part) {
        StringBuilder queue = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            queue.append(str.charAt(i));
            if (queue.toString().endsWith(part)) {
                queue = new StringBuilder(queue.substring(0, queue.length() - part.length()));
            }
        }
        return queue.toString();
    }


}
