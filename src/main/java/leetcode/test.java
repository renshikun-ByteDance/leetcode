package leetcode;

import java.util.*;

public class test {


    public int lengthOfLongestSubstring(String s) {
        int left = 0;
        int right = 0;
        int len = s.length();
        int maxwindow = 0;
        HashMap<Character, Integer> hTable = new HashMap<>();
        while (right < len) {
            if (hTable.get(s.charAt(right)) != null) {
                left = Math.max(left, hTable.get(s.charAt(right)) + 1);
            }
            hTable.put(s.charAt(right), right);
            maxwindow = Math.max(maxwindow, right - left + 1);
            right++;
        }
        return maxwindow;
    }

    public int characterReplacement(String s, int k) {
        int left = 0;
        int right = 0;
        int[] charAndNums = new int[26];
        int historyWindowMaxCharNums = 0;
        while (right < s.length()) {
            charAndNums[s.charAt(right) - 'A']++;
            historyWindowMaxCharNums = Math.max(historyWindowMaxCharNums, charAndNums[s.charAt(right) - 'A']);
            if (right - left + 1 - historyWindowMaxCharNums > k) {   //k被消耗完
                charAndNums[s.charAt(left) - 'A']--;
                left++;
            }
            right++;
        }
        return right - 1 - left + 1;
    }


    public List<Integer> findAnagrams(String s, String p) {
        ArrayList<Integer> targetList = new ArrayList<>();
        int[] target = new int[26];
        int[] window = new int[26];
        if (s.length() < p.length())
            return targetList;
        for (int i = 0; i < p.length(); i++) {
            target[p.charAt(i) - 'a']++;
            window[s.charAt(i) - 'a']++;
        }
        if (Arrays.equals(window, target))
            targetList.add(0);
        for (int i = p.length(); i < s.length(); i++) {
            window[s.charAt(i) - 'a']++;
            window[s.charAt(i - p.length()) - 'a']--;
            if (Arrays.equals(window, target))
                targetList.add(i - p.length() + 1);
        }
        return targetList;
    }


    public int findLHS(int[] nums) {
        HashMap<Integer, Integer> hTable = new HashMap<>();
        int maxLength = 0;
        for (int num : nums)
            hTable.put(num, hTable.getOrDefault(num, 0) + 1);
        for (int num : hTable.keySet()) {
            if (hTable.containsKey(num + 1))
                maxLength = Math.max(maxLength, hTable.get(num) + hTable.get(num + 1));
        }
        return maxLength;
    }

    public int findLHS01(int[] nums) {
        Arrays.sort(nums);
        int left = 0;
        int right = 0;
        int maxLength = 0;
        while (right < nums.length) {
            while (left < right && nums[right] - nums[left] > 1)
                left++;
            if (nums[right] - nums[left] == 1)
                maxLength = Math.max(maxLength, right - left + 1);
            right++;
        }
        return maxLength;
    }

    public int longestOnes(int[] nums, int k) {
        int left = 0;
        int right = 0;
        int maxWindow = 0;
        while (right < nums.length) {
            if (nums[right] == 0)
                k--;
            while (k < 0) {
                if (nums[left] == 0)
                    k++;
                left++;
            }
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }


    public int maxSatisfied(int[] customers, int[] grumpy, int minutes) {
        int sum = 0;
        int len = customers.length;
        int windowMaxSum = 0;
        int windowsum = 0;
        for (int i = 0; i < len; i++) {
            if (grumpy[i] == 0) {
                sum += customers[i];
                customers[i] = 0;
            }
        }
        for (int i = 0; i < minutes; i++) {
            windowsum += customers[i];
        }
        windowMaxSum = windowsum;
        for (int i = minutes; i < len; i++) {
            windowsum += customers[i];
            windowsum -= customers[i - minutes];
            windowMaxSum = Math.max(windowMaxSum, windowsum);
        }
        return sum + windowMaxSum;
    }


    public int maxSatisfied01(int[] customers, int[] grumpy, int minutes) {
        int sum = 0;
        int len = customers.length;
        int windowMaxSum = 0;
        int windowsum = 0;
        int right = 0, left = 0;
        for (int i = 0; i < len; i++) {
            if (grumpy[i] == 0) {
                sum += customers[i];
                customers[i] = 0;
            }
        }
        while (right < len) {
            windowsum += customers[right];
            while (right - left + 1 > minutes) {
                windowsum -= customers[left];
                left++;
            }
            windowMaxSum = Math.max(windowMaxSum, windowsum);
            right++;
        }
        return sum + windowMaxSum;
    }


    /**
     * [100000]
     * [000001]
     * 100000
     */
    public int maxFrequency(int[] nums, int k) {
        Arrays.sort(nums);
        int left = 0;
        int right = 1;
        int window = 1;
        long sum = 0;
        while (right < nums.length) {
            sum += (long) (right - left) * (nums[right] - nums[right - 1]);
            while (sum > k) {
                sum -= nums[right] - nums[left];
                left++;
            }
            window = Math.max(window, right - left + 1);
            right++;
        }
        return window;
    }


    /**
     * 1818. 绝对差值和
     */
    public int minAbsoluteSumDiff(int[] nums1, int[] nums2) {
        int mod = (int) 1e9 + 7;
        long sum = 0;
        int maxdiff = 0; //最大距离
        //排序，用于后续的二分
        int[] sorted = Arrays.copyOf(nums1, nums1.length);
        Arrays.sort(sorted);
        for (int i = 0; i < nums1.length; i++) {
            sum += Math.abs(nums1[i] - nums2[i]);
            //1、在nums1中找到和nums2[i]的左侧逼近和右侧逼近值
            int leftValue = leftValue(sorted, nums2[i]);
            int rightValue = rightValue(sorted, nums2[i]);
            //2、获取最接近的值
            int closeValue = Math.abs(leftValue - nums2[i]) > Math.abs(rightValue - nums2[i]) ? rightValue : leftValue;

            //3、最大差值
            maxdiff = Math.max(maxdiff, Math.abs(Math.abs(nums1[i] - nums2[i]) - Math.abs(closeValue - nums2[i])));
        }
        return (int) ((sum - maxdiff) % mod);
    }

    //左侧区间内逼近
    public int leftValue(int[] sorted, int target) {
        int left = 0;
        int right = sorted.length - 1;
        int K = left;  //默认值左侧端点
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (sorted[mid] <= target) {
                K = mid;
                left = mid + 1;
            } else if (target < sorted[mid]) {
                right = mid - 1;
            }
        }
        return sorted[K];
    }

    //右侧区间内逼近
    public int rightValue(int[] sorted, int target) {
        int left = 0;
        int right = sorted.length - 1;
        int K = right;   //默认值右侧端点
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (sorted[mid] < target) {
                left = mid + 1;
            } else if (target <= sorted[mid]) {
                K = mid;
                right = mid - 1;
            }
        }
        return sorted[K];
    }


    /**
     * 3. 无重复字符的最长子串
     */
    public int lengthOfLongestSubstring0(String s) {
        int left = 0;
        int right = 0;
        int maxwindow = 0;
        int len = s.length();
        HashMap<Character, Integer> hTable = new HashMap<>();
        while (right < len) {
            if (hTable.get(s.charAt(right)) != null) {
                left = Math.max(left, hTable.get(s.charAt(right)) + 1);
            }
            hTable.put(s.charAt(right), right);
            maxwindow = Math.max(maxwindow, right - left + 1);
            right++;
        }
        return maxwindow;
    }


    /**
     * 220. 存在重复元素 III
     */
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        int left = 0;
        int right = 0;
        TreeSet<Long> treeSet = new TreeSet<>();
        while (right < nums.length) {
            //当前窗口[left,right)，窗口长度 right - left，不包含right位置
            if (right - left > k) {  //如果超过窗口长度，则剔除左边界元素，左边界移动一位
                treeSet.remove((long) nums[left]);  //移除区间外元素
                left++;
            }
            //窗口右边界为探索未知区域，计算满足条件的范围区间边界值
            Long target = (long) nums[right] - (long) t;
            //区间内寻找边界值的近似值
            Long floor = treeSet.floor(target);
            Long ceiling = treeSet.ceiling(target);
            //因此，窗口内（set维护的元素）如果存在某个元素在范围 nums[right] - t 和 nums[right] + t 范围内
            if (floor != null && floor >= (long) nums[right] - (long) t)
                return true;
            if (ceiling != null && ceiling <= (long) nums[right] + (long) t)
                return true;

            treeSet.add((long) nums[right]);

            right++;
        }
        return false;
    }


    public boolean containsNearbyAlmostDuplicate00(int[] nums, int k, int t) {
        int left = 0;
        int right = 0;
        TreeSet<Long> treeSet = new TreeSet<>();
        while (right < nums.length) {
            //此轮循环，先剔除越界元素
            if (right - left > k) {  //如果超过窗口长度，则剔除左边界元素，左边界移动一位
                treeSet.remove((long) nums[left]);  //移除区间外元素
                left++;
            }
            //但不添加 nums[right]元素，因为会干扰 floor和 ceiling

            //计算nums[right]的临近值
            Long target = (long) nums[right];
            Long floor = treeSet.floor(target);
            Long ceiling = treeSet.ceiling(target);
            //检查临近值，是否在区间范围内
            if (floor != null && target - floor <= t)
                return true;
            if (ceiling != null && ceiling - target <= t)
                return true;
            //添加，保证下一轮循环
            treeSet.add((long) nums[right]); //right一直是区间内的元素，最后才加上的原因防止影响floor和ceiling，即不考虑right自身
            right++;
        }
        return false;
    }


    public boolean containsNearbyAlmostDuplicate02(int[] nums, int k, int t) {
        HashMap<Long, Long> bucketMap = new HashMap<>();
        long size = t + 1L; //t
        int left = 0;
        for (int right = 0; right < nums.length; right++) {
            if (right - left > k) {
                bucketMap.remove(getBucketID((long) nums[left], size));
                left++;
            }
            long bucketID = getBucketID((long) nums[right], size);
            //1.在桶内判断
            if (bucketMap.containsKey(bucketID))
                return true;
            //2.在相邻桶内判断，跨越桶的情况
            long leftBucketID = bucketID - 1;
            long rightBucketID = bucketID + 1;
            if (bucketMap.containsKey(leftBucketID) && (long) nums[right] - bucketMap.get(leftBucketID) <= t)
                return true;
            if (bucketMap.containsKey(rightBucketID) && bucketMap.get(rightBucketID) - (long) nums[right] <= t)
                return true;
            //增加新桶，而不会覆盖旧桶，因为覆盖旧桶（无论是左侧、当前、右侧桶）就意味有两个数距离<=t，就返回true了
            bucketMap.put(bucketID, (long) nums[right]);

        }
        return false;
    }

    private long getBucketID(long num, long size) {
        return num >= 0 ? num / size : ((num + 1) / size) - 1;
    }


    public double findMaxAverage(int[] nums, int k) {
        int sum = 0;
        int left = 0;
        int right = 0;
        double maxAverage = Integer.MIN_VALUE;
        while (right < nums.length) {
            sum += nums[right];
            while (right - left + 1 > k) {
                sum -= nums[left];
                left++;
            }
            if (right - left + 1 == k) {
                maxAverage = Math.max(maxAverage, sum * 1.0 / k);
            }
            right++;
        }
        return maxAverage;
    }


    public List<String> findRepeatedDnaSequences(String s) {
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


    public List<String> findRepeatedDnaSequences00(String s) {
        ArrayList<String> repeatedSequences = new ArrayList<>();
        HashMap<Character, Integer> charToNum = new HashMap<>();
        charToNum.put('A', 0); //00
        charToNum.put('C', 1); //01
        charToNum.put('G', 2); //10
        charToNum.put('T', 3); //11
        int windowLength = 10;
        int left = 0;
        int right = 0;
        int windowSum = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        while (right < s.length()) {
            windowSum = (windowSum << 2) | charToNum.get(s.charAt(right));
            while (right - left + 1 > windowLength) {
                //截取高位
                windowSum = windowSum & ((1 << windowLength * 2) - 1);
                left++;
            }
            if (right - left + 1 == windowLength) {
                hTable.put(windowSum, hTable.getOrDefault(windowSum, 0) + 1);
                if (hTable.getOrDefault(windowSum, 0) == 2) {
                    String seq = s.substring(left, left + windowLength);
                    repeatedSequences.add(seq);
                }
            }
            right++;
        }
        return repeatedSequences;
    }


    public boolean checkInclusion(String s1, String s2) {
        int[] target = new int[26];
        int[] window = new int[26];
        if (s1.length() > s2.length())
            return false;
        for (int i = 0; i < s1.length(); i++) {
            target[s1.charAt(i) - 'a']++;
            window[s2.charAt(i) - 'a']++;
        }
        if (Arrays.equals(target, window))
            return true;
        for (int i = s1.length(); i < s2.length(); i++) {
            window[s2.charAt(i) - 'a']++;
            window[s2.charAt(i - s1.length()) - 'a']--;
            if (Arrays.equals(target, window))
                return true;
        }
        return false;
    }


    public boolean checkInclusion00(String s1, String s2) {
        int[] target = new int[26];
        int[] window = new int[26];
        int windowLength = s1.length();
        if (s1.length() > s2.length())
            return false;
        for (int i = 0; i < s1.length(); i++) {
            target[s1.charAt(i) - 'a']++;
        }
        int left = 0;
        int right = 0;
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

}


