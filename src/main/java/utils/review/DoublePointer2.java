package utils.review;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoublePointer2 {


    /**
     * 11. 盛最多水的容器
     */
    public int maxArea(int[] height) {
        int maxArea = 0;
        for (int left = 0; left < height.length - 1; left++) {
            for (int right = left + 1; right < height.length; right++) {
                int hwater = Math.min(height[left], height[right]);
                maxArea = Math.max(maxArea, (right - left) * hwater);
            }
        }
        return maxArea;
    }

    //双指针的解法（非常类似二分法）
    public int maxArea01(int[] height) {
        int maxArea = 0;
        int left = 0;
        int right = height.length - 1;
        while (left < right) {
            int hwater = Math.min(height[left], height[right]);
            maxArea = Math.max(maxArea, (right - left) * hwater);
            if (height[left] < height[right])
                left++;
            else
                right--;
        }
        return maxArea;
    }


    /**
     * 15. 三数之和
     * 本题的核心是：两个数确定，第三个数用二分查找来做，会有较多的无效遍历，近似枚举的时间复杂度
     */
    public List<List<Integer>> threeSum(int[] nums) {
        //定义一个结果集
        ArrayList<List<Integer>> resList = new ArrayList<>();
        Arrays.sort(nums);
        HashSet<HashMap<Integer, Integer>> htable = new HashSet<>();
        for (int first = 0; first < nums.length - 2; first++) {
            for (int second = first + 1; second < nums.length - 1; second++) {
                int left = second + 1;
                int right = nums.length - 1;
                while (left <= right) {
                    int mid = left + ((right - left) >> 1);
                    int tempsum = nums[first] + nums[second] + nums[mid];
                    if (tempsum < 0)
                        left = mid + 1;
                    else
                        right = mid - 1;
                }
                if (left == nums.length) continue;
                else if (nums[first] + nums[second] + nums[left] == 0) {
                    //当前满足条件的数组
                    HashMap<Integer, Integer> arr = new HashMap<>();
                    arr.put(nums[first], nums[second]);
                    //如果此数组已经记录，则跳过
                    if (htable.contains(arr))
                        continue;
                    //返回值
                    resList.add(Arrays.asList(nums[first], nums[second], nums[left]));
                    htable.add(arr);
                }
            }
        }
        return resList;
    }


    /**
     * 443. 压缩字符串
     */
    public int compress(char[] chars) {
        int left = 0;
        int right = 1;
        int pos = 0;
        while (right < chars.length) {
            //right一直向前移动，探索其他不同数值的区间
            if (chars[right] != chars[right - 1] || right == chars.length - 1) {
                //left移送到和right-1同一个区间内
                while (left <= right - 1 && chars[left] != chars[right - 1]) {
                    left++;
                }
                chars[pos++] = chars[left];
                int window = right - left;    //right - 1 - left + 1
                if (window > 1) {
                    String times = String.valueOf(window);
                    char[] chartimes = times.toCharArray();
                    for (char c : chartimes)
                        chars[pos++] = c;
                }
            }
            right++;
        }
        return pos;
    }


    public int compress00(char[] chars) {
        int left = 0;
        int right = 0;
        int pos = 0;
        while (right < chars.length) {
            //right一直向前移动，探索其他不同数值的区间
            if (right == chars.length - 1 || chars[right] != chars[right + 1]) {
//                if (chars[right] != chars[right + 1] || right == chars.length - 1) {  //对比上述区别，此处报错数组越界
                //left移送到和right同一个区间内（有问题，应该是和right+1一个区间）
//                while (left <= right && chars[left] != chars[right]) {
//                    left++;
//                }
                chars[pos++] = chars[right];
                //压缩区间长度
                int window = right - left + 1;    //right - 1 - left + 1
                if (window > 1) {
                    String times = String.valueOf(window);
                    char[] chartimes = times.toCharArray();
                    for (char c : chartimes)
                        chars[pos++] = c;
                }
                //left站在下一个压缩区间的起始位置
                left = right + 1;
            }
            right++;
        }
        return pos;
    }

    public int compress01(char[] chars) {
        int left = 0;
        int right = 0;
        int pos = 0;
        while (right < chars.length) {
            //right一直向前移动，探索其他不同数值的区间
            if (right == chars.length - 1 || chars[right] != chars[right + 1]) {
//                if (chars[right] != chars[right + 1] || right == chars.length - 1) {  //对比上述区别，此处报错数组越界
                //left移送到和right同一个区间内（有问题，应该是和right+1一个区间）
                while (left <= right && chars[left] != chars[right]) {
                    left++;
                }
                chars[pos++] = chars[right];
                //压缩区间长度
                int window = right - left;    //right - 1 - left + 1
                if (window > 1) {
                    String times = String.valueOf(window);
                    char[] chartimes = times.toCharArray();
                    for (char c : chartimes)
                        chars[pos++] = c;
                }
                //left站在下一个压缩区间的起始位置
//                left = right + 1;
            }
            right++;
        }
        return pos;
    }





    /**
     * 825. 适龄的朋友
     */
    public int numFriendRequests(int[] ages) {
        int sumRequests = 0;
        for (int left = 0; left < ages.length - 1; left++) {  //边界条件
            for (int right = left + 1; right < ages.length; right++) {
                if (ages[right] <= 0.5 * ages[left] + 7 || ages[right] > ages[left] || ages[right] > 100 && ages[left] < 100)
                    sumRequests += 0;
                else
                    sumRequests++;
                if (ages[left] <= 0.5 * ages[right] + 7 || ages[left] > ages[right] || ages[left] > 100 && ages[right] < 100)
                    sumRequests += 0;
                else
                    sumRequests++;
            }
        }
        return sumRequests;
    }
//    20 的区间范围为 [17.0, 20]
//    30 的区间范围为 [22.0, 30]
//    100 的区间范围为 [57.0, 100]
//    110 的区间范围为 [62.0, 110]
//    120 的区间范围为 [67.0, 120]


    /**
     * 逻辑正确，但时间复杂度高
     * 问题在于：
     * ####虽然是双指针，但是每次都要从两头向中间寻找目标区间，循环次数较多
     */
    public int numFriendRequests00(int[] ages) {
        int sumRequests = 0;
        Arrays.sort(ages);
        for (int age : ages) {
            int left = 0;
            int right = ages.length - 1;
            if (age <= 14) {
                continue;
            }
            //针对当前的agePos的用户，其能发送请求的年龄区间的左侧边界如下
            while (ages[left] <= 0.5 * age + 7)          //不满足条件，能移动left以寻找满足条件的左边界
                left++;
            while (ages[right] > age)    //不满足条件，能移动right以寻找满足条件的右边界，右侧可等于
                right--;
            if (left <= right)
                sumRequests += right - left;
        }
        return sumRequests;
    }


    /**
     * 由于left和right具有延续性，相邻两轮的迭代，left和right均递增，所以left和right在各轮循环中持续增长，不重新归零
     */
    public int numFriendRequests01(int[] ages) {
        Arrays.sort(ages);
        int left = 0;
        int right = 0;
        int sumRequests = 0;
        for (int age : ages) {
            if (age <= 14) {
                continue;
            }
            //针对当前的agePos的用户，其能发送请求的年龄区间的左侧边界如下
            while (ages[left] <= 0.5 * age + 7)         //不满足条件，能移动left以寻找满足条件的左边界
                left++;
            while (right + 1 < ages.length && ages[right + 1] <= age) {
                //必须是<=age，必须有等号，特别是遇到多个age相同的情况
                right++;
                //1.在即将越界时，会压在边界上，而不会越界，即此时 right==ages.length
                //2.在age存在多个相同的情况下，right压在右侧age上
                //这种写法，无论何种情况，其自身一定是在区间内（多个age相同时）或位于区间右边界，都应将自身剔除
            }
            sumRequests += right - left + 1 - 1;  //-1剔除自身
        }
        return sumRequests;
    }

    //相对上面的方法，区别在于：第二个while种的right+1改为right的
    public int numFriendRequests02(int[] ages) {
        Arrays.sort(ages);
        int left = 0;
        int right = 0;
        int sumRequests = 0;
        for (int age : ages) {
            if (age <= 14) {
                continue;
            }
            //针对当前的agePos的用户，其能发送请求的年龄区间的左侧边界如下
            while (ages[left] <= 0.5 * age + 7)         //不满足条件，能移动left以寻找满足条件的左边界
                left++;
            while (right < ages.length && ages[right] <= age) {
                //必须是<=age，必须有等号，特别是遇到多个age相同的情况
                right++;
                //1.right会越界，即此时 right==ages.length
                //2.在age存在多个相同的情况下，right压在age右侧"外一位"上，即ages[right] > age
                //####1.多个age相同的情况下（重点考虑，容易忽略的点）
                //#########right最终状态为ages[right] > age，right位于age右侧外一位
                //####2.单个age的情况下
                //#########right最终状态为ages[right] > age，right位于age右侧外一位
                //无论何种情况，right使得枚举的元素（自身）一定在区间内，其次right也在使得ages[right] = age的右侧一位
            }
            sumRequests += right - 1 - left + 1 - 1;  //第一个"-1"是收缩区间，第二个"-1"是剔除枚举元素自身
        }
        return sumRequests;
    }


    public int numFriendRequests10(int[] ages) {
        int[] cnts = new int[120], presum = new int[121];
        for (int age : ages)
            cnts[age - 1]++;
        for (int i = 1; i < 121; i++)
            presum[i] = presum[i - 1] + cnts[i - 1];
        int ans = 0;
        for (int age : ages)
            ans += Math.max(0, presum[age] - presum[age / 2 + 7] - 1);
        return ans;
    }


    public int numFriendRequests11(int[] ages) {
        int numFriendRequests = 0;
        int[] ageNum = new int[120];
        int[] preSum = new int[121];
        //因为年龄区间为0-120，故计算ages数组中，各个年龄的人数
        for (int age : ages) {
            ageNum[age - 1]++;
        }
        //计算各个年龄的前缀和（含自身年龄）
        for (int i = 1; i < 121; i++) {  //本位，记录ageNum上一位（含）之前的累计和
            preSum[i] = preSum[i - 1] + ageNum[i - 1];
        }
        for (int age : ages) {
            numFriendRequests += Math.max(0, preSum[age] - preSum[age / 2 + 7] - 1);//此处取最大值，是为了剔除年龄小于15岁的请求
        }
        return numFriendRequests;
    }


    /**
     * 2047. 句子中的有效单词数
     */
    public int countValidWords(String sentence) {
        String[] words = sentence.split(" ");
        int vaildWords = 0;
        for (String word : words) {
            if (!word.equals("") && checkValidWords00(word))
                vaildWords++;
        }
        return vaildWords;
    }

//    仅由小写字母、连字符和/或标点（不含数字）组成。
//    至多一个 连字符 '-' 。如果存在，连字符两侧应当都存在小写字母（"a-b" 是一个有效单词，但 "-ab" 和 "ab-" 不是有效单词）。
//    至多一个 标点符号。如果存在，标点符号应当位于 token 的 末尾 。

    private boolean checkValidWords(String word) {
        String regular1 = "-";
        String regular2 = "!., ";
        String endChar = "";
        String reg = "[!., ]";
        Pattern compile = Pattern.compile(reg);
        Matcher matcher = compile.matcher(word);
        String regularWord = matcher.replaceAll("");

//        String replace1 = word.replaceAll("!", "");
//        String replace2 = replace1.replaceAll(".", "");    //为什么这个会把 "-" 剔除掉
//        String regularWord = replace2.replaceAll(",", "");

        //如果以"-"结尾，不满足题意
        if (regularWord.startsWith("-") || regularWord.endsWith("-"))
            return false;
        HashMap<String, Integer> hTable = new HashMap<>();
        for (int i = 0; i < word.length(); i++) {
            //如果包含数据，不满足题意
            if (Character.isDigit(word.charAt(i)))
                return false;
            if (regular1.contains(String.valueOf(word.charAt(i)))) {
                if (hTable.getOrDefault("-", 0) == 1)
                    return false;
                hTable.put("-", hTable.getOrDefault("-", 0) + 1);
            }
            if (regular2.contains(String.valueOf(word.charAt(i)))) {
                if (hTable.getOrDefault("regular2", 0) == 1)
                    return false;
                hTable.put("regular2", hTable.getOrDefault("regular2", 0) + 1);
                endChar = String.valueOf(word.charAt(i));
            }
            if (!endChar.equals("") && !word.endsWith(endChar) && hTable.getOrDefault("regular2", 0) == 1)
                return false;
        }
        return true;
    }


    private boolean checkValidWords00(String word) {
        int len = word.length();
        //判断是否存在"-"
        boolean hasHyphens = false;
        for (int pos = 0; pos < len; pos++) {
            if (Character.isDigit(word.charAt(pos))) {
                return false;
            } else if (word.charAt(pos) == '-') {
                if (hasHyphens == true || pos == 0 || pos == len - 1 || !Character.isLetter(word.charAt(pos - 1)) || !Character.isLetter(word.charAt(pos + 1)))
                    return false;
                hasHyphens = true;
            } else if ((word.charAt(pos) == '!' || word.charAt(pos) == '.' || word.charAt(pos) == ',') && pos != len - 1)
                return false;
        }
        return true;
    }


    public int countValidWords06(String sentence) {
        String[] words = sentence.split(" ");
        int totalWords = 0;
        for (String word : words) {
            if (!word.equals("") && checkValidWord(word))
                totalWords++;
        }
        return totalWords;
    }


    public boolean checkValidWord(String word) {
        boolean hasHyphen = false;
        boolean hasSymbol = false;
        if (word.startsWith("-") || word.endsWith("-"))
            return false;
        for (int i = 0; i < word.length(); i++) {
            if (Character.isDigit(word.charAt(i)))
                return false;
            else if (word.charAt(i) == '-') {
                if (hasHyphen == true || i == 0 || i == word.length() - 1 || !Character.isLetter(word.charAt(i - 1)) || !Character.isLetter(word.charAt(i + 1)))
                    return false;
                hasHyphen = true;
            } else if (word.charAt(i) == '!' || word.charAt(i) == '.' || word.charAt(i) == ',') {
                if (hasSymbol == true || i != word.length() - 1)
                    return false;
                hasSymbol = true;
            }
        }
        return true;
    }


}
