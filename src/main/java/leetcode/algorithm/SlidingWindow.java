package leetcode.algorithm;

import java.util.*;
import java.util.Arrays;

/**
 * 滑动窗口
 */
public class SlidingWindow {


    /**
     * 3. 无重复字符的最长子串
     */
    public int lengthOfLongestSubstring(String s) {
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

    public int lengthOfLongestSubstring10(String s) {
        //记录不重复字串中各个字符的位置,{字符:index}
        HashMap<Character, Integer> charIndex = new HashMap<>();
        int left = 0;
        int right = 0;
        int maxLength = 0;
        //循环结束条件：滑动窗口右侧right顶格/到头
        while (right < s.length()) {
            if (charIndex.containsKey(s.charAt(right))) {
                //如果滑动窗口右侧right遇到字串中已有字符，则将滑动窗口左侧移动至已有字符的右侧一位
                left = Math.max(left, charIndex.get(s.charAt(right)) + 1); //max的原因是left只能朝后跳
            }
            //无论将char写入哈希表，可覆盖/更新至最新位置***
            charIndex.put(s.charAt(right), right);
            maxLength = Math.max(maxLength, right - left + 1);
            right++;
        }
        return maxLength;
    }

    /**
     * 解法一和解法二三的本质区别在于：
     * 解法一：滑动窗口在一个循环中，左右边界均可移动
     * 解法二：滑动窗口在一个循环中，仅右边界可移动（至不重复的字符位置）
     */

    //解法二
    public int lengthOfLongestSubstring02(String s) {
        int maxLength = 0;
        for (int left = 0; left < s.length(); left++) {
            //记录出现过的char字符
            HashSet<Character> disChar = new HashSet<>();
            int right = left;
            while (right < s.length() && !disChar.contains(s.charAt(right))) {
                disChar.add(s.charAt(right));
                right++;
            }
            maxLength = Math.max(maxLength, right - left);    //注意对比上下两种的写法，为什么有的有 +1，此处没有
            //此处是在right不满足条件后，算的，其余两种是在满足条件时算故要+1
        }
        return maxLength;
    }

    //解法三
    public int lengthOfLongestSubstring03(String s) {
        int maxLength = 0;
        for (int left = 0; left < s.length(); left++) {
            //记录出现过的char字符
            HashSet<Character> disChar = new HashSet<>();
            for (int right = left; right < s.length(); right++) {
                if (disChar.contains(s.charAt(right))) {
                    break;
                }
                disChar.add(s.charAt(right));
                maxLength = Math.max(maxLength, right - left + 1);
            }
        }
        return maxLength;
    }


    public int lengthOfLongestSubstring000(String s) {
        int maxLength = 0;
        HashMap<Character, Integer> OnecharAndIndex = new HashMap<>();
        int left = 0;
//        if (chars.length == 0) return 0;
        for (int i = 0; i < s.length(); i++) {
            if (OnecharAndIndex.containsKey(s.charAt(i))) {
                left = Math.max(left, OnecharAndIndex.get(s.charAt(i)) + 1);
            }
            maxLength = Math.max(maxLength, i - left + 1);
            OnecharAndIndex.put(s.charAt(i), i);
        }
        return maxLength;
    }


    public int lengthOfLongestSubstring003(String s) {
        int maxLength = 0;
        for (int left = 0; left < s.length(); left++) {
            HashSet<Character> singleChars = new HashSet<>();
            for (int right = left; right < s.length(); right++) {
                if (singleChars.add(s.charAt(right))) {
                    maxLength = Math.max(maxLength, right - left + 1);
                    singleChars.add(s.charAt(right));
                } else {
                    break;
                }
            }
        }
        return maxLength;
    }


    /**
     * 643. 子数组最大平均数 I
     */
    public double findMaxAverage(int[] nums, int k) {
        double maxAverage = Integer.MIN_VALUE;
        int length = nums.length;
        int left = 0;
        int right = k;
        int sum = 0;
        for (int i = 0; i < k; i++) {
            sum += nums[i];
        }
        maxAverage = 1.0 * sum / k;
        while (right < length) {
            sum -= nums[left];
            sum += nums[right];
            maxAverage = Math.max(maxAverage, 1.0 * sum / k);
            right++;
            left++;
        }
        return maxAverage;
    }

    public double findMaxAverage10(int[] nums, int k) {
        double maxAverage = Integer.MIN_VALUE;
        int length = nums.length;
        int left = 0;
        int right = 0;
        int sum = 0;
        while (right < length) {
            sum += nums[right];
            while (right - left + 1 > k) {
                sum -= nums[left];
                left++;
            }
            if (right - left + 1 == k)
                maxAverage = Math.max(maxAverage, 1.0 * sum / k);
            right++;
        }
        return maxAverage;
    }


    public double findMaxAverage01(int[] nums, int k) {   //暴力解法，超时
        double maxAverage = Integer.MIN_VALUE;
        int length = nums.length;
        for (int left = 0; left < nums.length - k + 1; left++) {
            int sum = 0;
            int right = left;
            while (right < nums.length && right < left + k) {
                sum += nums[right];
                right++;
            }
            maxAverage = Math.max(maxAverage, 1.0 * sum / k);
        }
        return maxAverage;
    }

    public double findMaxAverage02(int[] nums, int k) {  //暴力解法，超时
        double maxAverage = Integer.MIN_VALUE;
        int length = nums.length;
        for (int left = 0; left < nums.length - k + 1; left++) {
            int sum = 0;
            for (int i = 0; i < k; i++) {
                sum += nums[left + i];
            }
            maxAverage = Math.max(maxAverage, 1.0 * sum / k);
        }
        return maxAverage;
    }

    public double findMaxAverage11(int[] nums, int k) {
        double maxAverage = Integer.MIN_VALUE;
        int sum = 0;
        if (nums.length < k) return -1;
        for (int left = 0; left < k; left++) {
            sum += nums[left];
        }
        maxAverage = 1.0 * sum / k;
        for (int right = k; right < nums.length; right++) {
            sum += nums[right];
            sum -= nums[right - k];
            maxAverage = Math.max(maxAverage, 1.0 * sum / k);
        }
        return maxAverage;
    }


    /**
     * 187. 重复的DNA序列
     */
    public List<String> findRepeatedDnaSequences(String s) {
        ArrayList<String> seqResult = new ArrayList<>();
        HashMap<String, Integer> seqAndNum = new HashMap<>();
        int windowLength = 10;
        int left = 0;
        int right = left + windowLength;
        while (right <= s.length()) {
            String seq = s.substring(left, right);
            seqAndNum.put(seq, seqAndNum.getOrDefault(seq, 0) + 1);
            left++;
            right++;
        }
        for (String seq : seqAndNum.keySet()) {
            if (seqAndNum.get(seq) > 1) {
                seqResult.add(seq);
            }
        }
        return seqResult;
    }


    public List<String> findRepeatedDnaSequences02(String s) {
        int windowLength = 10;
        ArrayList<String> result = new ArrayList<>();
        if (s.length() < 10) return result;
        HashMap<Character, Integer> charToNum = new HashMap<>();
        charToNum.put('A', 0);//00
        charToNum.put('C', 1);//01
        charToNum.put('G', 2);//10
        charToNum.put('T', 3);//11
        int left = 0;
        int right = 0;
        int windowsum = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        while (right < s.length()) {
            windowsum = (windowsum << 2) | charToNum.get(s.charAt(right));   //right向前探索未知区域
            while (right - left + 1 > windowLength) {
                //剪掉左侧left位置处的字符
                windowsum = windowsum & ((1 << windowLength * 2) - 1);
                left++;
            }
            if (right - left + 1 == windowLength) {
                hTable.put(windowsum, hTable.getOrDefault(windowsum, 0) + 1);
                if (hTable.get(windowsum) == 2)
                    result.add(s.substring(left, left + windowLength));
            }
            right++;
        }
        return result;
    }


    public List<String> findRepeatedDnaSequences01(String s) {
        ArrayList<String> seqResult = new ArrayList<>();
        HashMap<String, Integer> seqAndNum = new HashMap<>();
        int windowLength = 10;
        int left = 0;
        int right = left + windowLength;
        while (right <= s.length()) {
            String seq = s.substring(left, right);
            seqAndNum.put(seq, seqAndNum.getOrDefault(seq, 0) + 1);
            if (seqAndNum.get(seq) == 2) {
                seqResult.add(seq);
            }
            left++;
            right++;
        }
        return seqResult;
    }

    public List<String> findRepeatedDnaSequences03(String s) {
        ArrayList<String> repeatedSequences = new ArrayList<>();
        HashMap<String, Integer> seqAndNum = new HashMap<>();
        int left = 0;
        int right = 0;
        int window = 10;
        while (right < s.length()) {
            while (right - left + 1 > window)
                left++;
            if (right - left + 1 == window) {
                String seq = s.substring(left, left + window);
                seqAndNum.put(seq, seqAndNum.getOrDefault(seq, 0) + 1);
                if (seqAndNum.getOrDefault(seq, 0) == 2)
                    repeatedSequences.add(seq);
            }
            right++;
        }
        return repeatedSequences;
    }

    /**
     * 424. 替换后的最长重复字符
     */
    public int characterReplacement(String s, int k) {
        int left = 0, right = 0;
        int[] charNum = new int[26];
        int historyMaxCharNum = 0;
        while (right < s.length()) {
            charNum[s.charAt(right) - 'A']++;
            historyMaxCharNum = Math.max(historyMaxCharNum, charNum[s.charAt(right) - 'A']);
            if (right - left + 1 - historyMaxCharNum > k) { //k已经消耗完了，无法再支持区间扩张（right++,left不动）
                charNum[s.charAt(left) - 'A']--;
                left++;
            }
            //一轮结束，最终：
            //    确定了当前的最大区间（到底是扩展还是滑动（平移））
            //一轮的过程：
            //    上面的right其实在上一轮while中已经++过了，在上面先更新historyMaxCharNum
            //    最终目的是在“假设扩展区间”的情况下（仅right++,left不动）判断k是否足够消耗，如果足够就扩展，否则就滑动/平移
            //    判断过程：
            //        1.首先：
            //            如果新增的char和historyCharNum对应的char一致，则historyCharNum+1，且right已经+1，故if中左侧不变，不进if中（整个过程中不会消耗k）
            //            如果新增的char和historyCharNum对应的char不一致，则一定会消耗k：
            //        2.其次：
            //            判断k是否足够消耗:
            //                1.不够消耗（k已经消耗完了）
            //                   意味着：此时的区间长度已经是最大值
            //                   移动形式：一定是滑动/平移，则预定的扩展（仅增加right）将最终退化为平移（left也要+1）
            //                   left++
            //                2.仍可消耗
            //                   移动形式：扩展，不进入if，仅right++进入下一轮循环
            //        3.然后：
            //             right++，进入下一轮while，继续判断是扩展还是平移
            //注意：只有在k被消耗完的时候，才会达到最大的区间
            right++;
            //right-left+1维护的就是最大区间长度
            //导致区间增加的：left不动，right++；
            //导致left不动的原因：right - left + 1 - historyMaxCharNum <= k
            //导致left不动的原因：最大区间长度 - 历史区间内重复最多字符个数 <= k  导致k一直在消耗，left不动，区间长度再增加
        }
        return (right - 1) - left + 1;
        //在整个过程中，其实left和right之间的距离就是最大窗口
//        return maxLength;
    }
//    https://leetcode-cn.com/problems/longest-repeating-character-replacement/solution/tong-guo-ci-ti-liao-jie-yi-xia-shi-yao-shi-hua-don/


    /**
     * 424. 替换后的最长重复字符
     */
    public int characterReplacement01(String s, int k) {
        int left = 0, right = 0;
        char[] charsNums = new char[26];
        int historyMaxCharNum = 0;
        while (right < s.length()) {
            //计算一下新增右侧边界字母在在窗口内出现的次数
            charsNums[s.charAt(right) - 'A']++;
            //算一下右边界字母加入后，窗口内重复字符出现最多的次数
            historyMaxCharNum = Math.max(historyMaxCharNum, charsNums[s.charAt(right) - 'A']);   //不会减少，只会增加
            //原因：其之前满足过，在区间l[eft-right]中，此historymaxCharNum+k=区间长度
            //historyCharNum+k=区间长度的情况很多，但其中使得区间l[eft-right]最长的一定是maxhistoryCharNum，因为k固定
            //所以只有其增加的情况下，区间才会变长
            //这个最大字符个数，保持的是历史窗口内的最大值，而不是当前窗口内的最大值
            if (right - left + 1 - historyMaxCharNum > k) {   //不满足扩展的条件，则窗口滑动，left++
                charsNums[s.charAt(left) - 'A']--;  //一定是right遇到了和historymaxCharNum对应的char不同的字符char，k不够消耗
                //此时，针对此时窗口左侧的char对应的num--
                //1.此left的char是historyMaxCharNum对应的char
                // #####对historyMaxCharNum无影响，因为historyMaxCharNum不会减少
                // #####此char也应该维护，因为后期的窗口内可能此char出现的频次高于**现在**这个historyMaxCharNum
                //2.根本目的是维护新窗口内非historyMaxCharNum的字符个数
                //######用于分析当前窗口中频次最大的char
                left++;
            }
            right++;
        }
        return right - 1 - left + 1;
    }

    public int characterReplacement02(String s, int k) {
        int left = 0, right = 0;
        char[] charsNums = new char[26];
        int historyMaxCharNum = 0;
        while (right < s.length()) {
            charsNums[s.charAt(right) - 'A']++;
            historyMaxCharNum = Math.max(historyMaxCharNum, charsNums[s.charAt(right) - 'A']);   //不会减少，只会增加
            if (right - left + 1 - historyMaxCharNum > k) {   //不满足扩展的条件，则窗口滑动，left++
                charsNums[s.charAt(left) - 'A']--;  //一定是right遇到了和historymaxCharNum对应的char不同的字符char，k不够消耗
                left++;
            }
            right++;
        }
        return right - 1 - left + 1;
    }


    /**
     * 594. 最长和谐子序列
     * 子序列问题可考虑排序
     */
    public int findLHS(int[] nums) {
        Arrays.sort(nums);
        int left = 0;
        int right = 0;
        int maxLength = 0;
        while (right < nums.length) {
            while (left < right && nums[right] - nums[left] > 1) {
                left++;
            }
            if (nums[right] - nums[left] == 1) {
                maxLength = Math.max(maxLength, right - left + 1);
            }
            right++;
        }
        return maxLength;
    }

    public int findLHS01(int[] nums) {
        HashMap<Integer, Integer> hTable = new HashMap<>();
        int maxLength = 0;
        for (int num : nums) {
            hTable.put(num, hTable.getOrDefault(num, 0) + 1);
        }
        for (Integer digit : hTable.keySet()) {
            if (hTable.containsKey(digit + 1)) {
                maxLength = Math.max(maxLength, hTable.get(digit) + hTable.get(digit + 1));
            }
        }
        return maxLength;
    }


    /**
     * 438. 找到字符串中所有字母异位词
     */
    public List<Integer> findAnagrams(String s, String p) {
        ArrayList<Integer> targetList = new ArrayList<>();
        int length = s.length();
        int windowLength = p.length();
        if (length < windowLength)
            return new ArrayList<>();
        int[] targetArray = new int[26];
        int[] tempArray = new int[26];
        for (int i = 0; i < windowLength; i++) {
            targetArray[p.charAt(i) - 'a']++;
            tempArray[s.charAt(i) - 'a']++;
        }
        if (Arrays.equals(targetArray, tempArray)) targetList.add(0);
        int left = 0;   //滑动窗口移动后，离开的地方，从0开始--
        int right = windowLength; //滑动窗口移动后，到达的地方，从此处开始
        //String s = "cbaebabacd", p = "abc";
        while (right < length) {
            tempArray[s.charAt(right) - 'a']++;
            tempArray[s.charAt(left) - 'a']--;
            if (Arrays.equals(targetArray, tempArray))
                targetList.add(right - windowLength + 1); //滑动窗口达到的位置处right-window+1
            //targetList.add(left + 1); //或者滑动窗口离开的位置处+1
            right++;
            left++;
        }
        return targetList;
    }

    public List<Integer> findAnagrams01(String s, String p) {
        List<Integer> ans = new ArrayList<>();
        if (s.length() < p.length()) return ans;
        int[] window = new int[26];
        int[] target = new int[26];
        for (int i = 0; i < p.length(); i++) {
            target[p.charAt(i) - 'a']++;
        }
        int left = 0;
        int right = 0;
        while (right < s.length()) {
            window[s.charAt(right) - 'a']++;
            if (right - left + 1 > p.length()) {
                window[s.charAt(left) - 'a']--;
                left++;
            }
            if (right - left + 1 == p.length()) {
                if (Arrays.equals(window, target)) {
                    ans.add(left);
                }
            }
            right++;
        }
        return ans;
    }


    /**
     * 220. 存在重复元素 III
     * <p>
     * 滑动窗口 + 有序集合
     * <p>
     * 传统做法：
     * 如果使用队列维护滑动窗口内的元素，由于元素是无序的，我们只能对于每个元素都遍历一次队列来检查是否有元素符合条件。
     * 如果数组的长度为 n，则使用队列的时间复杂度为 O(nk)，会超出时间限制。
     * <p>
     * 因此我们希望能够找到一个数据结构维护滑动窗口内的元素，该数据结构需要满足以下操作：
     * 1.支持添加和删除指定元素的操作，否则我们无法维护滑动窗口；
     * 2.内部元素有序，支持二分查找的操作，
     * 这样我们可以快速判断滑动窗口中是否存在元素满足条件
     * 具体而言，对于元素 x，当我们希望判断滑动窗口中是否存在某个数 y 落在区间 [x - t, x + t]中，只需要判断滑动窗口中所有大于等于 x - t 的元素中的最小元素是否小于等于 x + t即可。
     * <p>
     * 我们可以使用有序集合来支持这些操作。
     * <p>
     * 给你一个整数数组 nums 和两个整数 k 和 t 。请你判断是否存在 两个不同下标 i 和 j
     * 使得 abs(nums[i] - nums[j]) <= t ，同时又满足 abs(i - j) <= k 。
     * 条件转换为：
     * 1.当前元素为i,搜索范围为[u-k,u+k],仅考虑左侧，转换为滑动窗口为k
     * 2.在滑动窗口中的元素，如果满足：
     * ######1.存在大于num[i]的元素，找到其中大于num[i]的最小元素rightValue，如果满足rightValue-num[i]<=t，则true
     * ######2.存在小于num[i]的元素，找到其中小于num[i]的最大元素leftValue，如果满足num[i]-leftValue<=t，则true
     * 其余false
     */
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        TreeSet<Long> windowSet = new TreeSet<>();
        for (int i = 0; i < nums.length; i++) {
            Long temp = (long) nums[i];
            // 从 windowSet 中找到小于等于 temp 的最大值（小于等于 u 的最接近 u 的数），没有则为null
            Long leftValue = windowSet.floor(temp);
            // 从 windowSet 中找到大于等于 temp 的最小值（大于等于 u 的最接近 u 的数），没有则为null
            Long rightValue = windowSet.ceiling(temp);
            if (rightValue != null && rightValue - temp <= t) return true;
            if (leftValue != null && temp - leftValue <= t) return true;
            // 将当前数加到 windowSet 中，并移除下标范围不在 [max(0, i - k), i) 的数（维持滑动窗口大小为 k）
            windowSet.add(temp); //滑动窗口的头
            if (i >= k)
                windowSet.remove((long) nums[i - k]);//当前滑动窗口左边界的左侧一位（滑动窗口离开的一位）
        }
        return false;
    }


    /**
     * 滑动窗口长度固定，平移的场景
     */
    public boolean containsNearbyAlmostDuplicate06(int[] nums, int k, int t) {
        TreeSet<Long> windowSet = new TreeSet<>();
        int left = 0;
        int right = 0;
        while (right < nums.length) {
            Long temp = (long) nums[right];
            if (right - left + 1 > k + 1) {
                windowSet.remove((long) nums[left]);
                left++;
            }
//          if (right - left + 1 == k + 1) {     //导致无法在窗口形成的过程中判断
            Long floor = windowSet.floor(temp);
            Long ceiling = windowSet.ceiling(temp);
            if (floor != null && temp - floor <= t) return true;
            if (ceiling != null && ceiling - temp <= t) return true;
            windowSet.add(temp);    //放后面，因为left没动，维持的仍然为一个标准window，如果提前导入，会导致floor和ceiling本身
//            }
            right++;
        }
        return false;
    }


    /**
     * 220. 存在重复元素 III
     * <p>
     * 滑动窗口 + 桶排序
     * <p>
     * 对比“滑动窗口 +有序集合”的方法，其要在有序集合中搜索是否有满足条件的元素,空间复杂度为非O(1)，而桶排序是O(1)
     * <p>
     * 此算法的复杂度：
     * <p>
     * 时间复杂度：O(n)，其中 n 是给定数组的长度。每个元素至多被插入哈希表和从哈希表中删除一次，每次操作的时间复杂度均为O(1)。
     * 空间复杂度：O(min(n,k))，其中 n 是给定数组的长度。哈希表中至多包含 min(n,k+1) 个元素。
     */
    public boolean containsNearbyAlmostDuplicate02(int[] nums, int k, int t) {
        //桶集合
        HashMap<Long, Long> bucketSet = new HashMap<>();
        //桶的大小，t+1为了保证桶内的maxValue-minValue<=t,如 1,2,3,4 t=3,则size应该为t+1;
        long size = t + 1;   //t
        for (int i = 0; i < nums.length; i++) {
            long bucketID = getBucketID(nums[i], size);
            if (bucketSet.containsKey(bucketID))
                return true;
            long leftBucketID = bucketID - 1;
            long rightBucketID = bucketID + 1;
            if (bucketSet.containsKey(leftBucketID) && nums[i] - bucketSet.get(leftBucketID) <= t)
                return true;
            if (bucketSet.containsKey(rightBucketID) && bucketSet.get(rightBucketID) - nums[i] <= t)
                return true;
            bucketSet.put(bucketID, (long) nums[i]);
            //维护滑动窗口的大小为 K，即有 K个桶，开始时（滑动窗口一直扩张），滑动窗口大小为 K时，窗口滑动（左缩，右扩）
            if (i >= k)
                bucketSet.remove(getBucketID((long) nums[i - k], size));
        }
        return false;
        /*
         * 桶的解法相当凝练，不过有一点可以啰嗦两句。不知道有没有人疑惑
         * 在比较id - 1和id + 1这两个相邻桶时，只比较了一个元素，这足够吗？哈希表的行为不是会用新元素覆盖旧元素，一个桶里有多个元素怎么办？
         *
         * 其实是覆盖根本不会发生...因为一旦要覆盖，就说明存在两个元素同属一个桶，直接返回true了。
         * 这就是题解说的“一个桶内至多只会有一个元素”——数组输入里当然可以有多个元素属于同一个桶，但是一旦出现一对，算法就结束了。
         */
    }

    private long getBucketID(long num, long size) {
        return num >= 0 ? num / size : ((num + 1) / size) - 1;
        //num>=0时,0,1,2,3 t=3 size=4;可分到一个桶内；
        //#####t+1为了保证桶内的maxValue-minValue<=t,如 1,2,3,4 t=3,则size应该为t+1;
        //num<0时,-4,-3,-2,-1（没有0，从-1开始） t=3 size=4;此时[-4,-1]被分到两个桶中，不合理
        //#####负数整体右移，[-4,-1]=>[-3,0],均被分配到一个桶（合理），但这个桶*0*已经被占用，故负数桶的序号-1
    }


    /**
     * 给你一个整数数组 nums 和两个整数 k 和 t 。请你判断是否存在 两个不同下标 i 和 j
     * 使得 abs(nums[i] - nums[j]) <= t ，同时又满足 abs(i - j) <= k 。
     */
    public boolean containsNearbyAlmostDuplicate00(int[] nums, int k, int t) {
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (Math.abs((long) nums[j] - (long) nums[i]) <= t && Math.abs(j - i) <= k) {
                    return true;
                }
            }
        }
        return false;
    }  //超时


    /**
     * 1423. 可获得的最大点数
     */
    public int maxScore(int[] cardPoints, int k) {
        int ans = 0;
        int sum = 0;
        int left = 0;
        int right = 0;
        int n = cardPoints.length;
        int winLength = n - k;
        int windowSum = 0;
        int min = Integer.MAX_VALUE;
        while (right < n) {
            sum += cardPoints[right];
            windowSum += cardPoints[right];
            while (right - left + 1 > winLength) {
                windowSum -= cardPoints[left];
                left++;
            }
            if (right - left + 1 == winLength) {
                min = Math.min(min, windowSum);
            }
            right++;
        }
        return sum - min;
    }


    /**
     * 1658. 将 x 减到 0 的最小操作数
     */
    public int minOperations(int[] nums, int x) {   //滑动窗口
        int n = nums.length;
        int ans = 0;
        int sum = 0;
        int min = Integer.MAX_VALUE;
        for (int num : nums) {
            sum += num;
            min = Math.min(min, num);
        }
        //剪枝：目标值太小（最小值大于目标值）、目标值太大（累加和小于目标值）
        if (min > x || sum < x) return -1;
        if (sum == x) return n;
        int left = 0;
        int right = 0;
        while (right < n) {
            sum -= nums[right];
            while (sum < x) {
                sum += nums[left];
                left++;
            }
            if (sum == x) {
                ans = Math.max(ans, right - left + 1);  //sum中不含 nums[left] 和 nums[right] 的值
            }
            right++;
        }
        return ans == 0 ? -1 : n - ans;
    }

    public int minOperations01(int[] nums, int x) { //前缀和 + 滑动窗口
        int n = nums.length;
        int sum = 0;
        int min = Integer.MAX_VALUE;
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum += nums[i];
            prefix[i + 1] = prefix[i] + nums[i];
            min = Math.min(min, nums[i]);
        }
        if (min > x || sum < x) return -1;
        if (sum == x) return n;
        int target = sum - x;
        int ans = 0;
        int left = 0;
        int right = 0;
        while (right <= n) {   //要有等号
            while (prefix[right] - prefix[left] > target) {
                left++;
            }
            if (prefix[right] - prefix[left] == target) {
                ans = Math.max(ans, right - left);
            }
            if (right == 15) {
                System.out.println("...");
            }
            right++;
        }
        return ans == 0 ? -1 : n - ans;
    }

    public int minOperations02(int[] nums, int x) {  //前缀和 + 哈希表
        int n = nums.length;
        int sum = 0;
        int min = Integer.MAX_VALUE;
        //前缀和
        int[] prefix = new int[n + 1];
        HashMap<Integer, Integer> prefixMap = new HashMap<>();
        prefixMap.put(0, 0);
        for (int i = 0; i < n; i++) {
            sum += nums[i];
            min = Math.min(min, nums[i]);
            prefix[i + 1] = prefix[i] + nums[i];
            prefixMap.put(prefix[i + 1], i + 1);
        }
        if (sum < x || min > x) return -1;
        if (sum == x) return n;
        int target = sum - x;
        int ans = 0;
        for (int i = 1; i <= n; i++) {
            int prev = prefix[i] - target;
            if (prefixMap.containsKey(prev)) {
                ans = Math.max(ans, i - prefixMap.get(prev));
            }
        }
        return ans == 0 ? -1 : n - ans;
    }


    /**
     * 567. 字符串的排列
     * <p>
     * 滑动窗口：
     * 1.形成滑动窗口
     * 2.平滑移动/扩展（收缩）移动
     * <p>
     * s2.charAt(i - windowlength)       ：指的是滑动窗口right=i时，滑动窗口离开的位置
     * s2.charAt(i - windowlength + 1)   ：指的是滑动窗口right=i时，滑动窗口的最后一位
     */
    public boolean checkInclusion(String s1, String s2) {
        int windowlength = s1.length();
        int[] window = new int[26];
        int[] target = new int[26];
        if (s1.length() > s2.length()) return false;
        //形成滑动窗口
        for (int i = 0; i < windowlength; i++) {
            //滑动窗口的目标数组
            target[s1.charAt(i) - 'a']++;
            //滑动窗口的数组
            window[s2.charAt(i) - 'a']++;
        }
        //滑动窗口形成后，先判断一下，滑动窗口的初始状态和目标状态是否一致（否则下面不会判断）
        if (Arrays.equals(target, window)) return true;
        for (int i = windowlength; i < s2.length(); i++) {
            window[s2.charAt(i) - 'a']++;
            window[s2.charAt(i - windowlength) - 'a']--;
            if (Arrays.equals(target, window)) return true;
        }
        return false;
    }


    public static boolean checkInclusion01(String s1, String s2) {
        int windowLength = s1.length();
        int[] target = new int[26];
        int[] window = new int[26];
        int left = 0;
        int right = 0;
        for (int i = 0; i < windowLength; i++)
            target[s1.charAt(i) - 'a']++;
        if (s1.length() > s2.length()) return false;
        while (right < s2.length()) {
            window[s2.charAt(right) - 'a']++;
            while (right - left + 1 > windowLength) {
                window[s2.charAt(left) - 'a']--;
                left++;
            }
            if (right - left + 1 == windowLength) {
                if (Arrays.equals(target, window))
                    return true;
            }
            right++;
        }
        return false;
    }


    /**
     * 1004. 最大连续 1的个数 III
     */
    public int longestOnes(int[] nums, int k) {
        int left = 0;
        int right = 0;
        int maxWindow = 0;
        int zeros = 0;
        while (right < nums.length) {
            //滑动窗口右侧向前移动一步
            if (nums[right] == 0) {
                zeros++;
            }
            //滑动窗口左侧是否要移动，移动的条件：k被消耗完了，如果没消耗完，则跳过此步骤（继续进行滑动窗口右侧移动），否则移动滑动窗口得左侧
            while (left <= right && zeros > k)//k替换次数被消耗完了
            {
                if (nums[left] == 0)
                    zeros--;
                left++;
            }
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }


    public int longestOnes01(int[] nums, int k) {
        int left = 0;
        int right = 0;
        int maxWindow = 0;
        while (right < nums.length) {
            if (nums[right] == 0) {
                k--;  //消耗一次
            }
            while (k < 0) {  //只要有透支，就通过移动 left 回收，直至满足条件
                if (nums[left] == 0) {  //回收一次
                    k++;
                }
                left++;  //移动，开始回收
            }
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }

    /**
     * 1052. 爱生气的书店老板
     * <p>
     * customers = [1,0,1,2,1,1,7,5], grumpy = [0,1,0,1,0,1,0,1], X = 3
     *
     * @param customers
     * @param grumpy
     * @param minutes
     * @return
     */
    public int maxSatisfied(int[] customers, int[] grumpy, int minutes) {
        int length = customers.length;
        int satisfiedCustomers = 0;
        int maxSatisfied = 0;
        //计算一下原始满意的客户数
        for (int i = 0; i < length; i++) {
            if (grumpy[i] == 0)   //一定是0
                satisfiedCustomers += customers[i];
        }
        int temp = 0;   //用一个变量表示，滑动窗口内的状态
        //形成初始滑动窗口
        for (int i = 0; i < minutes; i++) {
            //计算初始滑动窗口内，老板生气状态下，有让多少客户不满意
            //并在滑动窗口内，让老板不生气，使客户最终满意
            if (grumpy[i] == 1) //如果是0，则已经累加过了
                temp += customers[i];
        }
        maxSatisfied = satisfiedCustomers + temp;
        int left = 0;         //离开
        int right = minutes;  //到达
        //滑动窗口移动中（大小不变）,让滑动窗口内的客户都满意
        while (right < length) {
            //滑动窗口到达的位置，如果客户之前不满意，则在滑动窗口范围内让其满意
            if (grumpy[right] == 1)
                temp += customers[right];
            //滑动窗口离开的位置，如果客户之前不满意，则在离开滑动窗口范围内后，无法让其满意
            if (grumpy[left] == 1)
                //这部分客户无法让其满意了
                temp -= customers[left];
            maxSatisfied = Math.max(maxSatisfied, satisfiedCustomers + temp);
            left++;
            right++;
        }
        return maxSatisfied;
    }


    /**
     * 注意：滑动窗口一般有两类写法：
     * 1.滑动窗口大小固定
     * <p>注意：滑动窗口一般有两种写法：</>
     * <p>1.滑动窗口形成的过程和移动的过程分开写（两个for循环）上题
     * <p>2.滑动窗口形成的过程和移动的过程写一起（一个for循环）本题
     * 2.滑动窗口大小变化
     *
     * @param customers
     * @param grumpy
     * @param minutes
     * @return
     */
    public int maxSatisfied01(int[] customers, int[] grumpy, int minutes) {
        int sum = 0;
        for (int i = 0; i < customers.length; i++) {
            if (grumpy[i] == 0) {
                sum += customers[i];
                customers[i] = 0;   //精髓，将每一时刻不生气的人数累加到默认值后，将其置为0
                // 后续仅用滑动窗口计算滑动窗口内生气的人数，并将最大值（变为满意）累加到默认值即可，不需要复杂的判断了；
            }
        }
        int temp = 0;
        int maxtemp = 0;
        for (int left = 0, right = 0; right < customers.length; right++) {
            temp += customers[right];
            if (right - left + 1 > minutes) {
                temp -= customers[left];
                left++;
            }
            if (right - left + 1 == minutes)
                maxtemp = Math.max(maxtemp, temp);
        }
        return sum + maxtemp;
    }

    /**
     * 错误示例
     *
     * @param customers
     * @param grumpy
     * @param minutes
     * @return
     */
    public int maxSatisfied00(int[] customers, int[] grumpy, int minutes) {
        int length = customers.length;
        int satisfiedCustomers = 0;
        int maxSatisfied = 0;
        //计算一下原始满意的客户数
        for (int i = 0; i < length; i++) {
            if (grumpy[i] == 0)   //一定是0
                satisfiedCustomers += customers[i];
        }
        maxSatisfied = satisfiedCustomers;
        //形成初始滑动窗口
        for (int i = 0; i < minutes; i++) {
            //计算初始滑动窗口内，老板生气状态下，有让多少客户不满意
            //并在滑动窗口内，让老板不生气，使客户最终满意
            if (grumpy[i] == 1) //如果是0，则已经累加过了
                maxSatisfied += customers[i];
        }
        int left = 0;         //离开
        int right = minutes;  //到达
        //滑动窗口移动中（大小不变）,让滑动窗口内的客户都满意
        while (right < length) {
            int cus = 0;
            //滑动窗口到达的位置，如果客户之前不满意，则在滑动窗口范围内让其满意
            if (grumpy[right] == 1)
                cus += customers[right];
            //滑动窗口离开的位置，如果客户之前不满意，则在离开滑动窗口范围内后，无法让其满意
            if (grumpy[left] == 1)
                //这部分客户无法让其满意了
                cus -= customers[left];
            maxSatisfied = Math.max(maxSatisfied, maxSatisfied + cus);
            left++;
            right++;
        }
        return maxSatisfied;
    }

    /**
     * 1838. 最高频元素的频数
     * <p>
     * 错误示例
     *
     * @param nums
     * @param k
     * @return
     */
    public int maxFrequency(int[] nums, int k) {
        Arrays.sort(nums);
        int len = nums.length;
        int[] indexpp = new int[len];
        int window = 1;
        int left = 0, right = 0;
        while (right < len - 1 && left <= right) {
            while (nums[right] < nums[right + 1] && k > 0) {   //错误
                nums[right]++;     //尝试追上nums[right+1]
                indexpp[right]++;  //记录此位置共消耗了多少1
                k--;               //消耗1
            }
            if (nums[right] == nums[right + 1]) {
                right++;
            } else if (k == 0) {
//                nums[left] -= indexpp[left];
                k += indexpp[left];
                left++;
            }
            window = Math.max(window, right - left + 1);
        }
        return window;
    }


    public int maxFrequency01(int[] nums, int k) {
        Arrays.sort(nums);
        int len = nums.length;
        int sum = 0;
        int left = 0;
        int right = 1;
        int window = 1;
        while (right < len) {
            //滑动窗口右侧right移动一格时，为了保证滑动窗口区间内的值相等（均等于nums[right]）
            //因此应该区间内每个元素的值（此时均等于nums[right-1]）均增加nums[right] - nums[right - 1],水平面上升
            sum += (long) (right - left) * (nums[right] - nums[right - 1]);
            //滑动窗口区间长度为right - left + 1，但nums[right]不需要填平（滑动窗口内其他元素填至nums[right]）,故区间长度为right-left
            while (sum > k) { //如果消耗超过允许值，则滑动窗口缩小（左侧移动）
                sum -= nums[right] - nums[left];  //此时已经把nums[left]填平至nums[right]，要把滑动窗口最左侧位置消耗的k释放
                left++;//滑动窗口左侧移动一位
            }
            window = Math.max(window, right - left + 1); //此时滑动窗口满足条件
            right++;
        }
        return window;
    }


    public double findMaxAverage0000(int[] nums, int k) {
        double maxAverage = Integer.MIN_VALUE;
        int len = nums.length - 1;
        int left = 0;
        int right = 0;
        int sum = 0;
        while (right <= len) {
            sum += nums[right];
            while (right - left + 1 > k) {
                sum -= nums[left];
                left++;
            }
            if (right - left + 1 == k)
                maxAverage = Math.max(maxAverage, 1.0 * sum / k);
            right++;
        }
        return maxAverage;
    }


    /**
     * 219. 存在重复元素 II
     */
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        int left = 0;
        int right = 0;
        HashSet<Integer> hTable = new HashSet<>();
        while (right < nums.length) {
            if (right - left > k) {
                hTable.remove(nums[left]);
                left++;
            }
            if (hTable.contains(nums[right]))
                return true;
            else
                hTable.add(nums[right]);
            right++;
        }
        return false;
    }

    //这种写法，在窗口形成后，只能判断两端的情况，不能判断内部的情况
    public boolean containsNearbyDuplicate00(int[] nums, int k) {
        int left = 0;
        int right = 0;
        while (right < nums.length) {
            while (right - left > k) {    //只能保证right - left = k，但还有内部的情况
                left++;
            }
            if (right - left <= k && nums[right] == nums[left])
                return true;
            right++;
        }
        return false;
    }


    /**
     * 904. 水果成篮
     */
    public int totalFruit(int[] fruits) {
        int ans = 0;
        int left = 0;
        int right = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        while (right < fruits.length) {
            hTable.put(fruits[right], hTable.getOrDefault(fruits[right], 0) + 1);
            while (hTable.size() > 2) {
                Integer nums = hTable.get(fruits[left]);
                nums--;
                if (nums == 0) {
                    hTable.remove(fruits[left]);
                } else {
                    hTable.put(fruits[left], nums);
                }
                left++;
            }
            ans = Math.max(ans, right - left + 1);
            right++;
        }
        return ans;
    }


    /**
     * 713. 乘积小于 K 的子数组
     */
    public int numSubarrayProductLessThanK(int[] nums, int k) {
        int ans = 0;
        for (int i = 0; i < nums.length; i++) {
            int current = 1;
            for (int j = i; j < nums.length; j++) {
                current *= nums[j];
                if (current < k) ans++;
                else break;
            }
        }
        return ans;
    }

    public int numSubarrayProductLessThanK01(int[] nums, int k) {
        int ans = 0;
        int left = 0;
        int right = 0;
        int current = 1;
        while (right < nums.length) {   //滑动窗口，寻找以 right 结尾的有效区间的个数
            current *= nums[right];
            while (left < right && current >= k) {
                current /= nums[left];
                left++;
            }
            if (current < k) {
                ans += right - left + 1;   //区间长度
            }
            right++;
        }
        return ans;
    }


    /**
     * 2516. 每种字符至少取 K 个
     */
    public int takeCharacters(String str, int k) {
        int[] buckets = new int[3];
        for (int i = 0; i < str.length(); i++) {
            buckets[str.charAt(i) - 'a']++;
        }
        if (buckets[0] < k || buckets[1] < k || buckets[2] < k) {
            return -1;
        }
        int x = buckets[0] - k;    //中间最多含有 a 的个数
        int y = buckets[1] - k;    //中间最多含有 b 的个数
        int z = buckets[2] - k;    //中间最多含有 c 的个数
        int[] curBuckets = new int[3];
        int ans = 0;
        int left = 0;
        int right = 0;
        while (right < str.length()) {
            curBuckets[str.charAt(right) - 'a']++;
            while (curBuckets[0] > x || curBuckets[1] > y || curBuckets[2] > z) {
                curBuckets[str.charAt(left) - 'a']--;
                left++;
            }
            ans = Math.max(ans, right - left + 1);
            right++;
        }
        return str.length() - ans;
    }


    /**
     * 1234. 替换子串得到平衡字符串
     */
    public int balancedString(String str) {
        int n = str.length();
        int limits = n / 4;
        int[] buckets = new int[26];
        for (int i = 0; i < n; i++) {
            buckets[str.charAt(i) - 'A']++;
        }
        if (checkBalance(buckets, limits)) return 0;
        int ans = n;
        int left = 0;
        int right = 0;
        while (right < n) {
            buckets[str.charAt(right) - 'A']--;
            while (checkBalance(buckets, limits) && left <= right) {
                ans = Math.min(ans, right - left + 1);
                buckets[str.charAt(left) - 'A']++;
                left++;
            }
            right++;
        }
        return ans;
    }

    private boolean checkBalance(int[] buckets, int limits) {
        if (buckets['Q' - 'A'] > limits || buckets['W' - 'A'] > limits || buckets['E' - 'A'] > limits || buckets['R' - 'A'] > limits) {
            return false;
        }
        return true;
    }


    /**
     * 228. 汇总区间
     */
    public List<String> summaryRanges(int[] nums) {
        int n = nums.length;
        List<String> ans = new ArrayList<>();
        int currIndex = 0;
        while (currIndex < n) {
            int prevIndex = currIndex;
            currIndex++;
            while (currIndex < n && nums[currIndex] == nums[currIndex - 1] + 1) {
                currIndex++;
            }
            int nextIndex = currIndex - 1;
            if (prevIndex == nextIndex) {
                ans.add(String.valueOf(nums[prevIndex]));
            } else {
                ans.add(nums[prevIndex] + "->" + nums[nextIndex]);
            }
        }
        return ans;
    }



}
