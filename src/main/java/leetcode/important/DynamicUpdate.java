package leetcode.important;

import java.util.*;

/**
 * 在顺序遍历数组的过程中或者理解为随着时间的推移，在遍历过程中通过最值来记录最值，动态更新相关条件，从而简化处理逻辑，也会涉及基于最值来解决
 * 其中一个典型题目是：
 * 使用数据结构 HashMap 时，会存在 hash 冲突的问题，往往会结合  动态的处理，并及时返回，题目如下：
 * {@link #twoSum }
 * {@link #isIsomorphic001 }
 */
public class DynamicUpdate {

    /**
     * 55. 跳跃游戏
     */
    public boolean canJump(int[] nums) {   //反向更新目标位置，自己写的，正确
        if (nums.length <= 1) return true;
        int targetIndex = nums.length - 1;
        int i = nums.length - 2;
        while (i >= 0) {
            if (i + nums[i] >= targetIndex) {  //只有在当前位点可以到达目标索引处时，才会更新新的目标索引位置为当前点
                targetIndex = i;
            }
            i--;
        }
        return targetIndex == 0;   //判断目标索引是否为 0
    }

    public boolean canJump01(int[] nums) {  //正向更新目标位置
        int maxIndex = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > maxIndex) {   //当前搜索的位点已经超过可以到达的最远距离，即通过跳跃无法到达此处
                return false;
            }
            maxIndex = Math.max(maxIndex, i + nums[i]);   //动态更新当前可以到达的最远索引位置
        }
        return true;
    }

    public boolean canJump02(int[] nums) {   //基于所需要的步数
        int stepsNeed = 1;
        if (nums.length == 1) return true;
        for (int i = nums.length - 2; i >= 0; i--) {
            if (nums[i] < stepsNeed) {   //1.此位点无法跳到下一个目标位点
                stepsNeed++;                      //不更新目标位点：此时如果想要满足条件，则下一个位置就要有能力跨过当前位点，故需要多一步的跳跃数
            } else {                     //2.此位点可以跳到下一个目标位点
                stepsNeed = 1;                    //更新目标位点：此时下一个位置的目标位点就是"当前位点"，下一个位置只需要能够跳一步即可满足条件
            }
        }
        return nums[0] >= stepsNeed;
    }


    /**
     * 45. 跳跃游戏 II
     */
    public int jump(int[] nums) {    //寻找最少的起跳点
        int steps = 0;
        int end = 0;
        int maxPosition = 0;
        //如果访问最后一个元素，在边界正好为最后一个位置的情况下，我们会增加一次「不必要的跳跃次数」，因此我们不必访问最后一个元素
        for (int i = 0; i < nums.length - 1; i++) {  //起跳点不能是 nums.length - 1，否则就相当于原地跳
            maxPosition = Math.max(maxPosition, i + nums[i]);
            //--------------------------------------------
            // 大循环通过 end 将一个连续的区间分割为了多个区间段
            //     在循环过程中，其实就是在遍历每个区间段，截至条件为 i == end
            //     在循环过程中，记录并筛选出从此区间内哪个点起跳，可以使得跳的最远，并最终以此点作为新的起跳点，更新step和下个区间右边界
            //     针对跳出的情况，i不会走到更新的右侧边界（跳出）
            //--------------------------------------------
            if (i == end) { //遍历到区间右边界
                // 区间遍历完成，从此区间内选择一个起跳点，使得能够跳的最远，即 maxPosition对应的起跳点
                end = maxPosition;
                steps++;           //##进入##下一次跳跃
            }
        }
        return steps;
    }

    //反向查找
    public int jump01(int[] nums) {
        int steps = 0;
        int position = nums.length - 1;
        while (position > 0) {
            for (int i = 0; i < position; i++) {
                if (i + nums[i] >= position) {   //正序找到第一个满足条件的，则其跳跃距离最大
                    position = i;
                    steps++;
                    break;
                }
            }
        }
        return steps;
    }


    /**
     * 424. 替换后的最长重复字符
     */
    public int characterReplacement(String str, int k) {
        int left = 0;
        int right = 0;
        int maxFreq = 0;
        int maxWindow = 0;
        int[] freq = new int[26];
        while (right < str.length()) {
            maxFreq = Math.max(maxFreq, ++freq[str.charAt(right) - 'A']);
            while (maxFreq + k < right - left + 1) {
                maxFreq = Math.max(maxFreq, --freq[str.charAt(left) - 'A']);
                left++;
            }
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }


    /**
     * 1705. 吃苹果的最大数目
     */
    public int eatenApples(int[] apples, int[] days) {
        int maxApples = 0;
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> (o1[0] - o2[0]));
        int currentDay = 0;
        while (currentDay < apples.length || !sortedQueue.isEmpty()) {
            //新增元素
            if (currentDay < apples.length && apples[currentDay] != 0) {
                sortedQueue.add(new int[]{currentDay + days[currentDay], apples[currentDay]});
            }
            //剔除过期元素
            while (!sortedQueue.isEmpty() && (sortedQueue.peek()[0] <= currentDay || sortedQueue.peek()[1] == 0)) {
                sortedQueue.poll();
            }
            //贪心：吃掉最近会过期的一个苹果
            if (!sortedQueue.isEmpty()) {
                sortedQueue.peek()[1]--;
                maxApples++;
            }
            currentDay++;
        }
        return maxApples;
    }


    /**
     * 2104. 子数组范围和
     */
    public long subArrayRanges(int[] nums) {
        long sum = 0;
        for (int i = 0; i < nums.length; i++) {
            int minValue = nums[i];
            int maxValue = nums[i];
            for (int j = i + 1; j < nums.length; j++) {
                minValue = Math.min(minValue, nums[j]);
                maxValue = Math.max(maxValue, nums[j]);
                sum += maxValue - minValue;
            }
        }
        return sum;
    }


    /**
     * 1518. 换酒问题
     */
    public int numWaterBottles(int numBottles, int numExchange) {
        //重点：不要直接基于除法进行计算，而是一个一个的处理，更新相关变量，并判断是否触发相关操作
        int ans = 0;
        int currentEmptyBottles = 0;
        while (numBottles > 0) {
            if (currentEmptyBottles == numExchange) { //兑换一瓶
                numBottles++;
                currentEmptyBottles = 0;
            }
            ans++;            //喝一瓶
            numBottles--;     //少一瓶
            currentEmptyBottles++;  //多一个空瓶子
        }
        if (currentEmptyBottles == numExchange)
            ans++;
        return ans;
    }

    /**
     * 594. 最长和谐子序列
     */
    public int findLHS(int[] nums) {   //子序列可考虑排序问题
        int left = 0;
        int right = 0;
        int maxWindow = 0;
        Arrays.sort(nums);
        while (right < nums.length) {
            while (left <= right && nums[right] - nums[left] > 1) {
                left++;
            }
            if (left <= right && nums[right] - nums[left] == 1)
                maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }


    /**
     * 495. 提莫攻击
     */
    public int findPoisonedDuration(int[] timeSeries, int duration) {
        int beginTime = timeSeries[0];
        int endTime = timeSeries[0] + duration - 1;
        int totalTime = 0;
        for (int i = 1; i < timeSeries.length; i++) {
            if (timeSeries[i] > endTime) {  //无法续上，累积并更新左边界
                totalTime += endTime - beginTime + 1;  //不需要 + 1，因为时间是间隔，而非个数
                beginTime = timeSeries[i];
            }
            endTime = Math.max(endTime, timeSeries[i] + duration - 1);
        }
        totalTime += endTime - beginTime + 1;
        return totalTime;
    }

    public int findPoisonedDuration01(int[] timeSeries, int duration) {
        int totalTime = 0;
        for (int i = 1; i < timeSeries.length; i++) {
            totalTime += Math.min(timeSeries[i] - timeSeries[i - 1], duration);
        }
        return totalTime + duration;
    }

    /**
     * 205. 同构字符串
     */
    public boolean isIsomorphic(String s, String t) {
        for (int i = 0; i < s.length(); i++) {
            //s与 t的前缀就是映射关系，如果后面的和前面的不一致，就报错
            if (s.indexOf(s.charAt(i)) != t.indexOf(t.charAt(i))) {  //结合 "badc"与 "baba"、"paper"与 "title"深入体会
                return false;
            }
        }
        return true;
    }


    private boolean isIsomorphic001(String s, String t) {
        HashMap<Character, Character> hTable = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            //1、一对多
            if (hTable.containsKey(s.charAt(i)) && hTable.get(s.charAt(i)) != t.charAt(i)) {
                return false;
            }
            //2、多对一
            if (!hTable.containsKey(s.charAt(i)) && hTable.containsValue(t.charAt(i))) {
                return false;
            }
            hTable.put(s.charAt(i), t.charAt(i));
        }
        return true;
    }

    /**
     * 1711. 大餐计数
     */
    public int countPairs(int[] deliciousness) {
        long ans = 0;
        int mod = (int) Math.pow(10, 9) + 7;
        int maxTarget = (1 << 21);
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int food : deliciousness) {
            for (int sum = maxTarget; sum >= 1; sum >>= 1) {    // 1 是 2 的零次方
                ans = (ans + hTable.getOrDefault(sum - food, 0)) % mod;
            }
            hTable.put(food, hTable.getOrDefault(food, 0) + 1);  //剔除本身，防止 1 + 1 = 2，且两个 1 对应同一个 1
        }
        return (int) ans;
    }

    /**
     * 1. 两数之和
     */
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (hTable.containsKey(target - nums[i])) {
                return new int[]{hTable.get(target - nums[i]), i};
            }
            hTable.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }

    public int[] twoSum01(int[] nums, int target) {  //错误写法
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            hTable.put(nums[i], i);     //[3,3] 在遍历过程中，会覆盖
        }
        for (int num : hTable.keySet()) {
            if (hTable.containsKey(target - num))
                return new int[]{hTable.get(num), hTable.get(target - num)};
        }
        return new int[]{-1, -1};
    }

    /**
     * 1218. 最长定差子序列
     */
    public static int longestSubsequence(int[] arr, int difference) {
        int ans = 1;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int num : arr) {
            hTable.put(num, hTable.getOrDefault(num - difference, 0) + 1);
            ans = Math.max(ans, hTable.get(num));
        }
        return ans;
    }

    /**
     * 674. 最长连续递增序列
     */
    public int findLengthOfLCIS(int[] nums) {   //也可以用滑动窗口来做，但这样的写法更直观容易理解
        int ans = 0;
        int n = nums.length;
        int left = 0;
        for (int right = 0; right < n; right++) {
            if (right > 0 && nums[right] <= nums[right - 1]) {
                left = right;  //重新开始计数
            }
            ans = Math.max(ans, right - left + 1);
        }
        return ans;
    }


    /**
     * 1624. 两个相同字符之间的最长子字符串
     */
    public int maxLengthBetweenEqualCharacters(String str) {
        int ans = -1;
        HashMap<Character, ArrayList<Integer>> hTable = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            char xx = str.charAt(i);
            if (!hTable.containsKey(xx)) {
                hTable.put(xx, new ArrayList<>());
            }
            hTable.get(xx).add(i);
        }
        for (char ch : hTable.keySet()) {
            if (hTable.get(ch).size() >= 2) {
                ArrayList<Integer> nums = hTable.get(ch);
                ans = Math.max(ans, nums.get(nums.size() - 1) - nums.get(0) - 1);
            }
        }
        return ans;
    }



    public int maxLengthBetweenEqualCharacters01(String str) {
        int ans = -1;
        int[] firstIndex = new int[26];
        Arrays.fill(firstIndex, -1);
        for (int i = 0; i < str.length(); i++) {
            int index = str.charAt(i) - 'a';
            if (firstIndex[index] == -1) {
                //----------------------------
                // 仅记录字符第一次出现的位置，第二次出现只计算，不更新此字符的位置，如果后面仍有相同的字符，计算仍基于第一个字符的位置
                //----------------------------
                firstIndex[index] = i;
            } else {
                ans = Math.max(ans, i - firstIndex[index] - 1);
            }
        }
        return ans;
    }

    /**
     * 1846. 减小和重新排列数组后的最大元素
     */
    public int maximumElementAfterDecrementingAndRearranging(int[] arr) {
        Arrays.sort(arr);
        arr[0] = 1;
        for (int i = 1; i < arr.length; i++) {
            arr[i] = Math.min(arr[i], arr[i - 1] + 1);
        }
        return arr[arr.length - 1];
    }

    /**
     * 1446. 连续字符
     */
    public int maxPower(String str) {
        int left = 0;
        int right = 0;
        int maxWindow = 1;
        while (right < str.length()) {
            if (right > 0 && str.charAt(right) != str.charAt(right - 1)) {
                left = right;
            }
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }


    /**
     * 524. 通过删除字母匹配到字典里最长单词
     */
    public String findLongestWord(String str, List<String> dictionary) {
        dictionary.sort((o1, o2) -> {
            if (o1.length() != o2.length()) {
                return o2.length() - o1.length();
            } else {
                return o1.compareTo(o2);
            }
        });
        for (String verifyWord : dictionary) {
            int currentIndex = 0;
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == verifyWord.charAt(currentIndex)) {
                    currentIndex++;
                }
                if (currentIndex==verifyWord.length()){
                    return verifyWord;
                }
            }
        }
        return "";
    }

    public String findLongestWord01(String s, List<String> dictionary) {
        String longesWord = "";
        for (String word : dictionary) {
            int pos = 0;
            int move = 0;
            while (pos < word.length() && move < s.length()) {
                if (word.charAt(pos) == s.charAt(move)) {
                    pos++;
                    move++;
                } else
                    move++;
            }
            if (pos == word.length()) {
                if (pos > longesWord.length()) {
                    longesWord = word;
                } else if (pos == longesWord.length() && word.compareTo(longesWord) < 0) {
                    longesWord = word;
                }
            }
        }
        return longesWord;
    }



    /**
     * 1221. 分割平衡字符串
     */
    public int balancedStringSplit(String s) {
        char[] target = s.toCharArray();
        int right = 0;
        int numL = 0;
        int numR = 0;
        int balanceNums = 0;
        while (right < target.length) {
            if (target[right] == 'R')
                numR++;
            else
                numL++;
            if (numL == numR) {
                balanceNums++;
                numL = 0;
                numR = 0;
            }
            right++;
        }
        return balanceNums;
    }


    /**
     * 1800. 最大升序子数组和
     */
    public int maxAscendingSum(int[] nums) {
        int left = 0;
        int right = 0;
        int maxAns = 0;
        int currentAns = 0;
        while (right < nums.length) {
            currentAns += nums[right];
            if (right > 0 && nums[right - 1] >= nums[right]) {
                currentAns = nums[right];
            }
            maxAns = Math.max(maxAns, currentAns);
            right++;
        }
        return maxAns;
    }

    /**
     * 976. 三角形的最大周长
     */
    public int largestPerimeter(int[] nums) {
        Arrays.sort(nums);
        for (int i = nums.length - 1; i >= 2; i--) {
            if (nums[i - 2] + nums[i - 1] > nums[i]) {
                return nums[i - 2] + nums[i - 1] + nums[i];
            }
        }
        return 0;
    }



}
