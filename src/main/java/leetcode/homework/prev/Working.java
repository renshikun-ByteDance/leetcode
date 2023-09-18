package leetcode.homework.prev;


import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Working {

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


    /**
     * 1052. 爱生气的书店老板
     */
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

    public int maxSatisfied02(int[] customers, int[] grumpy, int minutes) {
        int ans = 0;
        int maxIncrease = 0;
        for (int i = 0; i < customers.length; i++) {
            if (grumpy[i] == 0)
                ans += customers[i];
            if (grumpy[i] == 1)
                grumpy[i] = customers[i];
        }
        int left = 0;
        int right = 0;
        int currentTotal = 0;
        while (right < grumpy.length) {
            currentTotal += grumpy[right];
            if (right - left + 1 > minutes) {
                currentTotal -= grumpy[left];
                left++;
            }
            maxIncrease = Math.max(maxIncrease, currentTotal);
            right++;
        }
        return ans + maxIncrease;
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


    /**
     * 594. 最长和谐子序列
     */
    public int findLHS00(int[] nums) {
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

    public int findLHS03(int[] nums) {
        int maxLength = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int num : nums)
            hTable.put(num, hTable.getOrDefault(num, 0) + 1);
        for (int num : nums) {
            if (hTable.containsKey(num + 1))
                maxLength = Math.max(maxLength, hTable.get(num) + hTable.get(num + 1));
        }
        return maxLength;
    }


    /**
     * 524. 通过删除字母匹配到字典里最长单词
     */
    public String findLongestWord(String s, List<String> dictionary) {  //逐个遍历
        String ans = "";
        for (String word : dictionary) {
            int rightS = 0;
            int rightW = 0;
            while (rightS < s.length() && rightW < word.length()) {
                if (word.charAt(rightW) == s.charAt(rightS)) {
                    rightW++;
                }
                rightS++;
            }
            if (rightW == word.length()) {
                if (rightW > ans.length())
                    ans = word;
                else if (rightW == ans.length() && word.compareTo(ans) < 0)
                    ans = word;
            }
        }
        return ans;
    }


    public String findLongestWord2(String s, List<String> dictionary) {  //逐个遍历
        //直接对列表排序，第一个满足条件的即为结果
        dictionary.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.length() != o2.length())
                    return o2.length() - o1.length();
                else
                    return o1.compareTo(o2);
            }
        });
        for (String word : dictionary) {
            int rightS = 0;
            int rightW = 0;
            while (rightS < s.length() && rightW < word.length()) {
                if (word.charAt(rightW) == s.charAt(rightS)) {
                    rightW++;
                }
                rightS++;
            }
            if (rightW == word.length()) {
                return word;
            }
        }
        return "";
    }


    /**
     * 553. 最优除法
     */
    public String optimalDivision(int[] nums) {
        if (nums.length == 1)
            return String.valueOf(nums[0]);
        if (nums.length == 2)
            return String.valueOf(nums[0] + "/" + nums[1]);
        StringBuilder ans = new StringBuilder();
        ans.append(nums[0]).append("/(");
        for (int i = 1; i < nums.length - 1; i++) {
            ans.append(nums[i]).append("/");
        }
        ans.append(nums[nums.length - 1]).append(")");
        return ans.toString();
    }


    /**
     * 561. 数组拆分 I
     */
    public int arrayPairSum(int[] nums) {
        int ans = 0;
        Arrays.sort(nums);     //排序后，使得大小相近的尽可能挨在一起
        // 取奇数位
        for (int i = 0; i < nums.length; i = i + 2) {
            ans += nums[i];
        }
        return ans;
    }


    /**
     * 575. 分糖果
     */
    public int distributeCandies(int[] candyType) {
        int ans = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int value : candyType) {
            hTable.put(value, 1);
        }

        ans = Math.min(hTable.size(), candyType.length / 2);

        return ans;
    }


    /**
     * 630. 课程表 III
     */
    public int scheduleCourse(int[][] courses) {
        int ans = 0;
        int currentTime = 0;
        //对当前课程排序
        Arrays.sort(courses, (o1, o2) -> {
            if (o1[1] != o2[1])
                return o1[1] - o2[1];   //按照最晚结束时间，升序排序
            else
                return o1[0] - o2[0];   //按照持续时长，升序排序
        });
        //存储的均为满足或者暂时满足条件的课程
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<int[]>((o1, o2) -> {
            return o2[0] - o1[0];
        });
        for (int[] course : courses) {
            if (currentTime + course[0] <= course[1]) {  //满足条件
                sortedQueue.add(course);  //添加至大根堆的队列，按照最晚完成时间降序排序
                currentTime += course[0]; //时间推进
                ans++;
            } else if (!sortedQueue.isEmpty() && sortedQueue.peek()[0] > course[0]) {  //与当前锥顶课程比较，二选一？
                int[] poll = sortedQueue.poll();
                currentTime -= poll[0];
                sortedQueue.add(course);
                currentTime += course[0];
            }
        }
        return ans;
    }


    /**
     * 781. 森林中的兔子
     */
    public int numRabbits(int[] answers) {
        //关键点：报数相同的可能是同一个颜色的兔子
        int res = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int ans : answers) {  //报的数：报这个数的兔子数
            hTable.put(ans, hTable.getOrDefault(ans, 0) + 1);
        }
        //认为报数相同的兔子大可能性是同一个颜色
        for (int ans : hTable.keySet()) {
            if (ans == 0)
                res += hTable.get(ans);
            else if (ans + 1 >= hTable.get(ans))  //同一个颜色：同颜色的部分兔子，没报数
                res += ans + 1; // + 自己
            else {                       //不同颜色
//                int groups = (hTable.get(ans) + ans + 1 - 1) / (ans + 1);  //向上取整，其中每组的大小为 ans +1
                int groups = (hTable.get(ans) / (ans + 1)) + (hTable.get(ans) % (ans + 1) == 0 ? 0 : 1);   //最后一定有括号
                res += groups * (ans + 1);  //组数 * 每组中容纳的兔子数
            }
        }
        return res;
    }

    /**
     * 881. 救生艇
     */
    public int numRescueBoats(int[] people, int limit) {
        int ans = 0;
        Arrays.sort(people);
        int left = 0;
        int right = people.length - 1;
        while (left < right) {
            if (people[left] + people[right] <= limit) {
                left++;
                right--;
            } else if (people[right] <= limit) {
                right--;
            }
            ans++;
        }
        if (left == right && people[left] <= limit)
            ans++;
        return ans;
    }


    /**
     * 942. 增减字符串匹配
     */
    public int[] diStringMatch(String s) {
        int[] ans = new int[s.length() + 1];
        int low = 0;
        int high = s.length();
        for (int i = 0; i < s.length(); i++) {
            ans[i] = s.charAt(i) == 'I' ? low++ : high--;
        }
        ans[s.length()] = s.charAt(s.length() - 1) == 'I' ? high : low;
        return ans;
    }

    public int[] diStringMatch01(String s) {
        int[] ans = new int[s.length() + 1];
        TreeSet<Integer> sortedTree = new TreeSet<>();
        int numD = 0;
        for (int i = 0; i < ans.length; i++) {
            sortedTree.add(i);
            if (i < ans.length - 1 && s.charAt(i) == 'D')
                numD++;
        }
        //主要为了复初始值
        ans[0] = numD;
        sortedTree.remove(numD);

        for (int i = 0; i < ans.length - 1; i++) {
            if (s.charAt(i) == 'D') {
                ans[i + 1] = sortedTree.floor(ans[i]); //找一个比 ans[i] 略小的值
            } else {
                ans[i + 1] = sortedTree.ceiling(ans[i]);
            }
            sortedTree.remove(ans[i + 1]);
        }
        return ans;
    }


    /**
     * 1005. K 次取反后最大化的数组和
     */
    public int largestSumAfterKNegations(int[] nums, int k) {
        int ans = 0;
        PriorityQueue<Integer> sortedQueue = new PriorityQueue<Integer>((o1, o2) -> {
            return o1 - o2;
        });
        for (int num : nums) {
            sortedQueue.add(num);
        }
        while (k > 0 && !sortedQueue.isEmpty()) {
            sortedQueue.add(-sortedQueue.poll());
            k--;
        }
        while (!sortedQueue.isEmpty()) {
            ans += sortedQueue.poll();
        }
        return ans;
    }

    public int largestSumAfterKNegations01(int[] nums, int k) {
        Arrays.sort(nums);
        int ans = 0;
        int minValue = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] < 0 && k > 0) { //最多消耗K次
                nums[i] = -nums[i];
                k--;
            }
            minValue = Math.min(minValue, nums[i]);
            ans += nums[i];
        }
        if (k != 0) { // K 未被消耗完，则应该继续消耗
            if ((k & 1) == 1) {  //奇数
                ans -= 2 * minValue;
            }
        }
        return ans;
    }


    public int largestSumAfterKNegations02(int[] nums, int k) {
        int sum = 0;
        int minR = Integer.MAX_VALUE;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int num : nums) {
            sum += num;
            if (num >= 0)
                minR = Math.min(minR, num);
            hTable.put(num, hTable.getOrDefault(num, 0) + 1);
        }
        for (int i = -100; i < 0; i++) {
            if (hTable.containsKey(i)) {
                int times = Math.min(k, hTable.get(i));
                sum += 2 * (-i) * times;
                //重新维护 hTable 的状态（重要，但容易忽略）
                hTable.put(i, hTable.get(i) - times);
                hTable.put(-i, hTable.getOrDefault(i, 0) + times);
                //重新维护 minR 的值
                minR = Math.min(minR, -i);
                //重新维护 K 的值
                k -= times;
            }
            if (k == 0)
                return sum;
        }

        if (minR == 0 || (k & 1) == 0)  //最小的数为 0或 k为偶数
            return sum;
        else {     //不为零且奇数个
            sum -= 2 * minR;
        }
        return sum;
    }


    /**
     * 1221. 分割平衡字符串
     */
    public int balancedStringSplit(String s) {
        int ans = 0;
        int numL = 0;
        int numR = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'L')
                numL++;
            else
                numR++;

            if (numL == numR)
                ans++;
        }
        return ans;
    }

    public int balancedStringSplit01(String s) {
        int ans = 0;
        int sum = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'L')
                sum++;
            else
                sum--;
            if (sum == 0)
                ans++;
        }
        return ans;
    }


    /**
     * 1405. 最长快乐字符串
     */
    public String longestDiverseString00(int a, int b, int c) {
        StringBuilder ans = new StringBuilder();
        TreeSet<Map.Entry<Character, Integer>> sortedTree = new TreeSet<Map.Entry<Character, Integer>>((o1, o2) -> {
            if (!o2.getValue().equals(o1.getValue()))
                return o2.getValue() - o1.getValue();  //降序
            else
                return o2.getKey().compareTo(o1.getKey());
        });
        HashMap<Character, Integer> hTable = new HashMap<>();
        if (a > 0)
            hTable.put('a', a);
        if (b > 0)
            hTable.put('b', b);
        if (c > 0)
            hTable.put('c', c);
        sortedTree.addAll(hTable.entrySet());

        while (true) {
            //拿出最多的当前元素，增长字符串长度
            Map.Entry<Character, Integer> more = sortedTree.pollFirst();
            int k = Math.min(2, more.getValue());
            int t = k;
            while (t > 0) {
                ans.append(more.getKey());
                t--;
            }
            if (sortedTree.size() == 0)
                return ans.toString();
            else {
                Map.Entry<Character, Integer> second = sortedTree.pollFirst();
                if (second.getValue() != 0) {
                    ans.append(second.getKey());
                    if (second.getValue() - 1 != 0) {
                        HashMap<Character, Integer> smallTest = new HashMap<>();
                        smallTest.put(second.getKey(), second.getValue() - 1);
                        sortedTree.addAll(smallTest.entrySet());
                    }
                }
            }
            if (more.getValue() - k != 0) {
                HashMap<Character, Integer> test = new HashMap<>();
                test.put(more.getKey(), more.getValue() - k);
                sortedTree.addAll(test.entrySet());
            }

            if (sortedTree.size() == 0)
                return ans.toString();
        }

    }


    public String longestDiverseString(int a, int b, int c) {
        StringBuilder ans = new StringBuilder();
        //排序数组
        charAndNums[] sorted = {
                new charAndNums('a', a),
                new charAndNums('b', b),
                new charAndNums('c', c)
        };
        while (true) {
            //每次循环进来，都要重新排序
            Arrays.sort(sorted, (o1, o2) -> {
                return o2.nums - o1.nums;
            });
            //每次"逐个"消耗一个，逐个的顺序：按照字符的个数来消耗，尽可能消耗多的，一个交替另一个最好，同时保证不会连续三个
            for (charAndNums tuple : sorted) {  //每一轮循环的目的：找到一个可以添加的元素
                int slen = ans.length();
                if (tuple.nums == 0)  //如果没有可以消耗的元素，则直接返回
                    return ans.toString();
                //保证不会连续有三个相同的
                if (slen >= 2 && ans.charAt(slen - 1) == tuple.ch && ans.charAt(slen - 2) == tuple.ch) {
                    continue;
                }
                //当前 tuple 的元素满足条件（添加后不连续三个），继续执行逻辑（添加此元素）
                ans.append(tuple.ch);
                tuple.nums -= 1;
                break;  //直接退出此轮循环，已经找到一个可添加的元素，循环的目的达到，退出循环
            }
            // 后面重新排序，重新进行循环找到合理的元素
        }
    }


    public String longestDiverseString01(int a, int b, int c) {
        StringBuilder ans = new StringBuilder();
        PriorityQueue<charAndNums> sortedQueue = new PriorityQueue<>((o1, o2) -> o2.nums - o1.nums);   //降序排列
        if (a > 0)
            sortedQueue.add(new charAndNums('a', a));
        if (b > 0)
            sortedQueue.add(new charAndNums('b', b));
        if (c > 0)
            sortedQueue.add(new charAndNums('c', c));
        while (true) {
            if (sortedQueue.isEmpty())
                return ans.toString();
            charAndNums first = sortedQueue.poll();
            int alen = ans.length();
            if (alen >= 2 && ans.charAt(alen - 1) == first.ch && ans.charAt(alen - 2) == first.ch) {  //第一个元素虽然最多，但不能够满足消耗条件，则取第二个
                if (sortedQueue.isEmpty())  //如果为空，说明第二第三个元素均已经消耗完，且当前元素不可消耗，故返回结果
                    return ans.toString();
                charAndNums second = sortedQueue.poll();  //取出 第二个元素
                ans.append(second.ch);
                second.nums -= 1;
                if (second.nums > 0)
                    sortedQueue.add(second);
                sortedQueue.add(first);
            } else {
                ans.append(first.ch);
                first.nums -= 1;
                if (first.nums > 0)
                    sortedQueue.add(first);
            }
        }
    }


    private class charAndNums {
        char ch;
        int nums;

        public charAndNums(char ch, int nums) {
            this.ch = ch;
            this.nums = nums;
        }
    }


    static {
        ArrayList<Integer> fibLists = new ArrayList<>();
        int f1 = 1;
        int f2 = 1;
        fibLists.add(f1);
        fibLists.add(f2);
        int m = 2;
        int fib = 0;
        while (fib <= Math.pow(10, 9)) {
            fib = fibLists.get(m - 1) + fibLists.get(m - 2);
            fibLists.add(fib);
            m++;
        }
        int[] fibs = fibLists.stream().mapToInt(Integer::intValue).toArray();
    }


    private static final int[] FIBs = {
            1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, 17711,
            28657, 46368, 75025, 121393, 196418, 317811, 514229, 832040, 1346269, 2178309, 3524578, 5702887, 9227465,
            14930352, 24157817, 39088169, 63245986, 102334155, 165580141, 267914296, 433494437, 701408733, 1134903170
    };


    public int findMinFibonacciNumbers(int k) {
        int ans = 0;
        while (k > 0) {
            k -= binSearchLeft(FIBs, k);
            ans++;
        }
        return ans;
    }

    private int binSearchLeft(int[] FIBs, int target) {
        int left = 0;
        int right = FIBs.length - 1;
        int k = left;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (FIBs[mid] <= target) {
                k = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return FIBs[k];  //左侧逼近值
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


    public int eatenApples01(int[] apples, int[] days) {
        int maxApples = 0;
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> (o1[0] - o2[0]));  //按照腐烂日期升序排序
        int maxVaildPeriod = 0;
        int d = 0;
        while (!sortedQueue.isEmpty() || d < apples.length) {
            // while (d <= maxVaildPeriod || d < apples.length) {
            if (d < apples.length && apples[d] > 0) {
                sortedQueue.add(new int[]{d + days[d], apples[d]});  //允许重复，故无需于之前的合并
                maxVaildPeriod = Math.max(maxVaildPeriod, d + days[d]);
            }
            //剔除过期腐烂的苹果
            while (!sortedQueue.isEmpty() && (sortedQueue.peek()[0] <= d || sortedQueue.peek()[1] == 0))  //队首的苹果筐中的腐烂日期小于今天的日期，故过期了
                sortedQueue.poll(); //取出，不在放入队列，相当于剔除
            if (!sortedQueue.isEmpty()) {
                sortedQueue.peek()[1]--;
                maxApples++;
            }
            d++;
        }
        return maxApples;
    }


    /**
     * 1833. 雪糕的最大数量
     */
    public int maxIceCream(int[] costs, int coins) {
        int ans = 0;
        Arrays.sort(costs);
        int sum = 0;
        for (int cost : costs) {
            if (sum + cost <= coins) {
                sum += cost;
                ans++;
            } else {
                break;
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
        int maxValue = 1;
        for (int i = 0; i < arr.length - 1; i++) {
            if (Math.abs(arr[i] - arr[i + 1]) > 1) {
                arr[i + 1] = arr[i] + 1;
            }
            maxValue = arr[i + 1];
        }
        return maxValue;
    }


    public int maximumElementAfterDecrementingAndRearranging01(int[] arr) {
        Arrays.sort(arr);
        arr[0] = 1;
        for (int i = 1; i < arr.length; i++) {
            arr[i] = Math.min(arr[i], arr[i - 1] + 1);
        }
        return arr[arr.length - 1];
    }


    /**
     * 1877. 数组中最大数对和的最小值
     */
    public int minPairSum(int[] nums) {
        int ans = 0;
        Arrays.sort(nums);
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            ans = Math.max(ans, nums[left] + nums[right]);
            left++;
            right--;
        }
        return ans;
    }


    /**
     * 2170. 使数组变成交替数组的最少操作数
     */
    public int minimumOperations(int[] nums) {
        if (nums.length == 1)
            return 0;

        int maxTimes = 0;
        HashMap<Integer, Integer> oneTable = new HashMap<>();
        HashMap<Integer, Integer> twoTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if ((i & 1) == 1) {  //奇数位
                oneTable.put(nums[i], oneTable.getOrDefault(nums[i], 0) + 1);
            } else {
                twoTable.put(nums[i], twoTable.getOrDefault(nums[i], 0) + 1);
            }
        }

        //核心：保证每个map中至少有两个元素
        oneTable.put(-1, 0);
        twoTable.put(-1, 0);
        //奇数位元素的排序
        ArrayList<Map.Entry<Integer, Integer>> oneSorted = new ArrayList<>(oneTable.entrySet());
        oneSorted.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder())); //由大到小排序
        //偶数位元素的排序
        ArrayList<Map.Entry<Integer, Integer>> twoSorted = new ArrayList<>(twoTable.entrySet());
        twoSorted.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder())); //由大到小排序

        //获取排名第一的元素
        Map.Entry<Integer, Integer> oneFirst = oneSorted.get(0);
        Map.Entry<Integer, Integer> twoFirst = twoSorted.get(0);

        //获取排名第二的元素
        Map.Entry<Integer, Integer> oneSecond = oneSorted.get(1);
        Map.Entry<Integer, Integer> twoSecond = twoSorted.get(1);

        //计算，奇数位和偶数位不同元素的最大频次和
        if (!oneFirst.getKey().equals(twoFirst.getKey())) {
            maxTimes = oneFirst.getValue() + twoFirst.getValue();
        } else {
            maxTimes = Math.max(oneFirst.getValue() + twoSecond.getValue(), twoFirst.getValue() + oneSecond.getValue());
        }

        return nums.length - maxTimes;
    }


    public int minimumOperations01(int[] nums) {
        if (nums.length == 1)
            return 0;

        int maxTimes = 0;
        HashMap<Integer, Integer> AhTable = new HashMap<>();
        HashMap<Integer, Integer> BhTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if ((i & 1) == 1) {  //奇数位
                AhTable.put(nums[i], AhTable.getOrDefault(nums[i], 0) + 1);
            } else {
                BhTable.put(nums[i], BhTable.getOrDefault(nums[i], 0) + 1);
            }
        }

        //奇数位元素的排序
        ArrayList<Map.Entry<Integer, Integer>> ASorted = new ArrayList<>(AhTable.entrySet());
        ASorted.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder())); //由大到小排序
        //偶数位元素的排序
        ArrayList<Map.Entry<Integer, Integer>> BSorted = new ArrayList<>(BhTable.entrySet());
        BSorted.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder())); //由大到小排序

        //各集合元素，至少两位，同时获取排名第一的元素
        Map.Entry<Integer, Integer> AFirst = ASorted.get(0);
        Map.Entry<Integer, Integer> BFirst = BSorted.get(0);

        //计算，奇数位和偶数位不同元素的最大频次和
        if (!AFirst.getKey().equals(BFirst.getKey())) {  // Top1元素的 Key不相等
            maxTimes = AFirst.getValue() + BFirst.getValue();
        } else {  //Top1的 Key相等
            //----------------------------------------
            //注意：
            //  每种情况，算的均是 Math.max(AFirst.getValue() + BSecond.getValue() ， ASecond.getValue() + BFirst.getValue())
            //差异：
            //  A集合 和 B集合，分别都是基于主键合并的结果，所以 A集合和 B集合均有可能仅含有一种主键 K,故涉及到对应的 第二个元素的出现频次时，给 0
            //----------------------------------------

            if (ASorted.size() >= 2 && BSorted.size() >= 2) {  //两个排序集合中，基于主键去重计数后，各自分别有至少两个不同的元素
                Map.Entry<Integer, Integer> ASecond = ASorted.get(1);
                Map.Entry<Integer, Integer> BSecond = BSorted.get(1);
                maxTimes = Math.max(AFirst.getValue() + BSecond.getValue(), BFirst.getValue() + ASecond.getValue());
            } else if (ASorted.size() >= 2) {  //A集合有至少两个不同的元素，B集合中只有一个元素
                Map.Entry<Integer, Integer> ASecond = ASorted.get(1);
                maxTimes = Math.max(AFirst.getValue() + 0, ASecond.getValue() + BFirst.getValue());
            } else if (BSorted.size() >= 2) {  //B集合有至少两个不同的元素，A集合中只有一个元素
                Map.Entry<Integer, Integer> BSecond = BSorted.get(1);
                maxTimes = Math.max(BFirst.getValue() + 0, AFirst.getValue() + BSecond.getValue());
            } else {   //A、B集合中只有一个元素
                maxTimes = Math.max(AFirst.getValue() + 0, BFirst.getValue() + 0);
            }
        }
        return nums.length - maxTimes;
    }


    public int minimumOperations06(int[] nums) {
        int[] countA = new int[100001];
        int[] countB = new int[100001];
        for (int i = 0; i < nums.length; i++) {
            if ((i & 1) == 1) //奇数位
                countA[nums[i]]++;
            else              //偶数位
                countB[nums[i]]++;
        }
        //A/B集合中值最大的前两位的索引值，默认值
        int ATop1 = 0;
        int ATop2 = 0;
        int BTop1 = 0;
        int BTop2 = 0;
        for (int i = 1; i < 100001; i++) {
            //集合A中会存在 Top1和Top2索引不同，但频次相同的情况
            if (countA[i] > countA[ATop1]) {  //是否有等号，无所谓，其关系到 top1 和 top2 在频次相等时，在哪里处理，没有等号，在下面处理，有等号，在这里处理
                ATop2 = ATop1;
                ATop1 = i;
            } else if (countA[i] > countA[ATop2]) {  //类同上，但这里涉及top2 和 top3在频次相等的情况下，在哪里处理，这里无需处理，等不等没所谓
                ATop2 = i;
            }

            if (countB[i] >= countB[BTop1]) {
                BTop2 = BTop1;
                BTop1 = i;
            } else if (countB[i] >= countB[BTop2]) {
                BTop2 = i;
            }
        }
        int maxTimes = ATop1 == BTop1 ?
                Math.max(countA[ATop1] + countB[BTop2], countA[ATop2] + countB[BTop1]) : countA[ATop1] + countB[BTop1];

        return nums.length - maxTimes;
    }


    /**
     * 1996. 游戏中弱角色的数量
     */
    public int numberOfWeakCharacters(int[][] properties) {
        int ans = 0;
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<int[]>((o1, o2) -> {
            if (o1[0] != o2[0])
                return o2[0] - o1[0];   //攻击值，降序排序
            else
                return o1[1] - o2[1];   //攻击值相等，防御值升序排序
        });
        int currentDefense = -1;   //记录当前最大的防御值
        sortedQueue.addAll(Arrays.asList(properties));
        while (!sortedQueue.isEmpty()) {
            int[] next = sortedQueue.poll();
            if (currentDefense > next[1])  //获取到的顺序，就是按照攻击值降序的，所以这个条件满足
                ans++;
            else
                currentDefense = next[1];
        }
        return ans;
    }


    public int numberOfWeakCharacters01(int[][] properties) {
        int ans = 0;
        Arrays.sort(properties, (o1, o2) -> {
            if (o2[0] != o1[0])
                return o2[0] - o1[0];
            else
                return o1[1] - o2[1];
        });
        int maxDefense = properties[0][1];
        for (int i = 1; i < properties.length; i++) {
            if (maxDefense > properties[i][1])
                ans++;
            else
                maxDefense = Math.max(maxDefense, properties[i][1]);
        }
        return ans;
    }


    /**
     * 34. 在排序数组中查找元素的第一个和最后一个位置
     */
    public int[] searchRange(int[] nums, int target) {
        int[] ans = new int[]{-1, -1};
        if (nums.length == 0) return ans;
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target) {
                left = mid + 1;
            } else if (target <= nums[mid]) {  //逼近左侧 target
                right = mid - 1;
            }
        }
        if (left <= nums.length - 1 && nums[left] == target)  //一定注意边界条件
            ans[0] = left;
        left = 0;
        right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] <= target) {   //逼近右侧 target
                left = mid + 1;
            } else if (target < nums[mid]) {
                right = mid - 1;
            }
        }
        if (right >= 0 && nums[right] == target)   //一定注意边界条件
            ans[1] = right;
        return ans;
    }


    /**
     * 611. 有效三角形的个数
     */
    public int triangleNumber(int[] nums) {
        Arrays.sort(nums);
        int ans = 0;
        for (int i = 0; i < nums.length - 2; i++) {
            for (int j = i + 1; j < nums.length - 1; j++) {
                int target = nums[i] + nums[j];
                int left = j + 1;
                int right = nums.length - 1;
                while (left <= right) { //左侧逼近 target，两边之和 target大于第三边
                    int mid = left + ((right - left) >> 1);
                    if (nums[mid] < target) {
                        left = mid + 1;
                    } else if (target <= nums[mid]) {
                        right = mid - 1;
                    }
                }
                if (left == nums.length) { //区间内元素均满足
                    ans += left - 1 - (j + 1) + 1;  //left -1 回退到区间内最后一个元素的位置
                } else if (nums[left] == target) {  //即使target重复，这里逼近的也是左侧 target
                    ans += left - 1 - (j + 1) + 1;  //left -1 回退到非target位置
                } else {  //区间内不存在 target，此时 left 位于大于target的位置
                    ans += left - 1 - (j + 1) + 1;
                }
            }
        }
        return ans;
    }


    /**
     * 704. 二分查找
     */
    public int search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target)
                return mid;
            else if (nums[mid] < target)
                left = mid + 1;
            else if (target < nums[mid])
                right = mid - 1;
        }
        return -1;
    }


    public int search01(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target)
                left = mid + 1;
            else if (target <= nums[mid])
                right = mid - 1;
        }

        if (left == nums.length)
            return -1;
        else if (nums[left] == target)
            return left;
        else if (nums[left] > target)
            return -1;

        return -6;
    }


    public int search02(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] <= target)
                left = mid + 1;
            else if (target < nums[mid])
                right = mid - 1;
        }

        if (right == -1)
            return -1;
        else if (nums[right] == target)
            return right;
        else if (nums[right] < target)
            return -1;

        return -6;
    }


    /**
     * 162. 寻找峰值
     */
    public int findPeakElement(int[] nums) {
        if (nums.length == 1) return 0;
        int[] ans = new int[nums.length + 2];
        ans[0] = Integer.MIN_VALUE;
        System.arraycopy(nums, 0, ans, 1, nums.length);
        ans[ans.length - 1] = Integer.MIN_VALUE;
        for (int i = 1; i < ans.length - 1; i++) {
            if (ans[i - 1] < ans[i] && ans[i] > ans[i + 1])
                return i - 1;
        }
        return -1;  //无效
    }


    public int findPeakElement01(int[] nums) {
        int index = 0;  //默认值，即当前暂时认为 此位置为峰值，因为左侧为 负无穷
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[i - 1])   //关键：在 峰值处，由于 其严格大于两侧，所以从左向右遍历时，先在峰值处先更新 Index（左侧条件满足），后无法更新 Index（右侧条件满足）
                index = i;
        }
        return index;
    }


    public int findPeakElement02(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < nums[mid + 1]) {           //left左侧上山
                left = mid + 1;
            } else if (nums[mid + 1] <= nums[mid]) {   //right右侧上山
                right = mid;
            }
        }
        return left;
    }


    /**
     * 1818. 绝对差值和
     */
    public int minAbsoluteSumDiff01(int[] nums1, int[] nums2) {
        long mod = (int) Math.pow(10, 9) + 7;
        long sum = 0;       //一定用长整型
        long maxDiff = 0;   //一定用长整型
        int[] sorted = Arrays.copyOf(nums1, nums1.length);
        Arrays.sort(sorted);
        for (int i = 0; i < nums1.length; i++) {
            int target = nums2[i];  //和此值的距离
            int currentDiff = Math.abs(nums1[i] - target);
            sum += currentDiff;
            //逼近target右侧
            int pos = binSearch(sorted, target);
            //target右侧
            int Diff = Math.abs(sorted[pos] - target);
            //target左侧
            if (pos - 1 >= 0)
                Diff = Math.min(Diff, Math.abs(sorted[pos - 1] - target));  //单个位置，计算的目标是：取距离 target的最近距离值（从左侧或右侧）
            //取各位中，当前使用的距离 与 计算的最近距离 二者差值的最大值
            maxDiff = Math.max(maxDiff, currentDiff - Diff);   //不用加绝对值
        }
        return (int) ((sum - maxDiff) % (mod));
    }


    private int binSearch(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target)
                left = mid + 1;
            else if (target <= nums[mid])
                right = mid - 1;
        }
        if (left == nums.length)
            return nums.length - 1;
        else if (nums[left] == target)
            return left;
        else if (nums[left] > target)  //右侧逼近
            return left;

        return -6;
    }


    /**
     * 686. 重复叠加字符串匹配
     */
    public int repeatedStringMatch(String a, String b) {
//        if (b.length() < a.length()) return -1;  //  a="aa",b="a" 答案为 1，因此无需判断
        int Times = 0;
        StringBuilder build = new StringBuilder();
        while (build.length() < b.length()) {
            build.append(a);
            Times++;
        }
        if (build.toString().contains(b))
            return Times;
        //新增一遍
        build.append(a);
        Times++;
        if (build.toString().contains(b))
            return Times;
        return -1;
    }


    /**
     * 451. 根据字符出现频率排序
     */
    public String frequencySort(String s) {
        StringBuilder ans = new StringBuilder();
        HashMap<Character, Integer> charAndTimes = new HashMap<>();
        int maxFreq = 0;  //用于确定 bucket的个数
        for (int i = 0; i < s.length(); i++) {  //记录各个字符出现的字数
            Integer freq = charAndTimes.getOrDefault(s.charAt(i), 0);
            charAndTimes.put(s.charAt(i), freq + 1);
            maxFreq = Math.max(maxFreq, freq + 1);
        }
        StringBuilder[] buckets = new StringBuilder[maxFreq + 1];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new StringBuilder();
        }
        for (Map.Entry<Character, Integer> entries : charAndTimes.entrySet()) {
            buckets[entries.getValue()].append(entries.getKey());
        }
        for (int i = maxFreq; i >= 0; i--) {
            StringBuilder bucketElements = buckets[i];
            for (int j = 0; j < bucketElements.length(); j++) {
                char ca = bucketElements.charAt(j);
                int Times = i;
                while (Times > 0) {
                    ans.append(ca);
                    Times--;
                }
            }
        }
        return ans.toString();
    }


    public String frequencySort01(String s) {
        StringBuilder ans = new StringBuilder();
        int[][] sorted = new int[128][2];
        for (int i = 0; i < sorted.length; i++) {
            sorted[i][0] = i;  //第一列元素的值用"索引"赋值，用于标识"各个字符"
        }
        for (int i = 0; i < s.length(); i++) {
            //自动将字符a b等转换为对应的 ASCII码（int）
            sorted[s.charAt(i)][1]++;     //基于"字符的标识"，来累加各个字符出现的频次
        }
        Arrays.sort(sorted, (o1, o2) -> { //基于各个字符出现的频次Times对数组排序，期间字符和字符的频次的对应关系不会变化，其相当于一行
            if (o1[1] != o2[1])    //二维数组每一行均为一个 数组
                return o2[1] - o1[1];
            else
                return o1[0] - o2[0];  //直接基于 字符转换对应的 ASCII 码进行比较，A.compare(B)的本质

            //--------------------------------------------------
            // 两个字符串 compare ，本质是逐个字符进行比较
            // 两个字符   compare ，本质是字符对应的 ASCII码进行比较
            //--------------------------------------------------

        });
        //排序后，索引已经和 o[1] 的关系断裂
        for (int i = 0; i < 128; i++) {  //排序后，索引小的频次大，整体呈现下降趋势（因为每一位标识一个字符，可能存在多个字符的频次相等，故趋势会有平移的情况），到某个位置归为 0
            int times = sorted[i][1];
            if (times != 0) {
                while (times > 0) {
                    ans.append((char) sorted[i][0]);  // 将此 频次对应的字符 添加至结果
                    times--;
                }
            }
        }
        return ans.toString();
    }


    /**
     * 1005. K 次取反后最大化的数组和
     */
    public int largestSumAfterKNegations03(int[] nums, int k) {
        PriorityQueue<Integer> sortedQueue = new PriorityQueue<>();
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            sortedQueue.add(nums[i]);
        }
        for (int i = k; i > 0 && !sortedQueue.isEmpty(); i--) {
            if (sortedQueue.peek() < 0) {
                Integer poll = sortedQueue.poll();
                sortedQueue.add(-poll);
                sum -= 2 * poll;
            } else if (sortedQueue.peek() > 0 && (i & 1) == 1) {
                return sum - 2 * sortedQueue.poll();
            } else {
                return sum;
            }
        }
        return sum;
    }

    public int largestSumAfterKNegations04(int[] nums, int k) {
        int sum = 0;
        int[] buckets = new int[202];  //桶排序：将数据散列至桶内，无需执行排序操作
        for (int num : nums) {
            buckets[num + 100]++;      //负数映射至索引区间（正）
            sum += num;
        }
        //处理负数
        for (int i = 0; i < 100; i++) {
            if (buckets[i] != 0) {
                int freq = Math.min(buckets[i], k);  //可替换的次数
                sum -= 2 * freq * (i - 100);  //回归本源
                buckets[100 - i + 100] += freq;      // 100 - i为对应的正数，+ 100将此正数投射至区间 [100 , 200]
                k -= freq;                           //更新剩余可替换的次数
                if (k == 0)
                    return sum;
            }
        }
        //处理 0
        if (buckets[100] != 0 || (k & 1) == 0)  //存在 0 或剩余次数为 偶数，直接返回结果
            return sum;
        //处理正数
        for (int i = 101; i < buckets.length; i++) {  //且 剩余 的可替换体术为 奇数，即只需找到最小值替换即可
            if (buckets[i] != 0) {
                sum -= 2 * (i - 100);  //剩余奇数次，等效于针对正数的最小值执行一次
                return sum;
            }
        }
        return -1;
    }


    public int largestSumAfterKNegations05(int[] nums, int k) {
        int sum = 0;
        HashMap<Integer, Integer> buckets = new HashMap<>();
        for (int num : nums) {
            sum += num;
            buckets.put(num, buckets.getOrDefault(num, 0) + 1);
        }
        for (int i = -100; i < 0; i++) {
            if (buckets.containsKey(i)) {
                int freq = Math.min(k, buckets.get(i));
                sum -= 2 * freq * i;
                buckets.put(-i, buckets.getOrDefault(-i, 0) + freq);
                k -= freq;
            }
            if (k == 0)
                return sum;
        }

        StringBuilder[] stringBuilders = new StringBuilder[10];
        if (buckets.containsKey(0) || (k & 1) == 0)
            return sum;

        for (int i = 1; i < 101; i++) {
            if (buckets.containsKey(i)) {
                sum -= 2 * i;
                return sum;
            }
        }
        return -1;
    }


    /**
     * 20. 有效的括号
     */
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        HashMap<Character, Character> hTable = new HashMap<>();
        hTable.put('(', ')');
        hTable.put('[', ']');
        hTable.put('{', '}');
        char[] array = s.toCharArray();
        for (Character ca : array) {
            if (!stack.isEmpty() && hTable.containsKey(stack.peek()) && hTable.get(stack.peek()) == ca)
                stack.pop();
            else
                stack.add(ca);
        }
        return stack.isEmpty();
    }


    /**
     * 1190. 反转每对括号间的子串
     */
    public String reverseParentheses(String s) {
        Stack<Character> stack = new Stack<>();
        char[] array = s.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == ')') {
                StringBuilder builder = new StringBuilder();
                while (stack.peek() != '(') {
                    builder.append(stack.pop());   // LIFO -> FIFO
                }
                stack.pop();  //剔除 '('
                for (Character ca : builder.toString().toCharArray())
                    stack.push(ca);
            } else {
                stack.add(array[i]);
            }
        }

        StringBuilder ans = new StringBuilder();
        for (Character ca : stack) {
            ans.append(ca);
        }
        return ans.toString();
    }


    public String reverseParentheses01(String s) {
        char[] array = s.toCharArray();
        ArrayDeque<Character> dequeQueue = new ArrayDeque<>();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == ')') {
                ConcurrentLinkedQueue<Character> fifoQueue = new ConcurrentLinkedQueue<>();
                while (!dequeQueue.isEmpty() && dequeQueue.getLast() != '(') {
                    fifoQueue.add(dequeQueue.pollLast());
                }
                dequeQueue.pollLast();
                while (!fifoQueue.isEmpty()) {
                    dequeQueue.add(fifoQueue.poll());
                }
            } else {
                dequeQueue.add(array[i]);
            }
        }
        StringBuilder ans = new StringBuilder();
        while (!dequeQueue.isEmpty()) {
            ans.append(dequeQueue.pollFirst());
        }
        return ans.toString();
    }


    /**
     * 1047. 删除字符串中的所有相邻重复项
     */
    public String removeDuplicates(String s) {
        StringBuilder ans = new StringBuilder();
        char[] array = s.toCharArray();
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < array.length; i++) {
            if (!stack.isEmpty() && stack.peek() == array[i])
                stack.pop();
            else
                stack.add(array[i]);
        }
        for (Character ca : stack)
            ans.append(ca);
        return ans.toString();
    }


    /**
     * 32. 最长有效括号
     */
    public int longestValidParentheses(String s) {
        char[] array = s.toCharArray();
        int[] flags = new int[array.length];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == '(') {  //仅记录左括号，右括号忽略
                stack.add(i);
            } else {
                if (!stack.isEmpty() && array[stack.peek()] == '(') {
                    flags[i] = 1;   //右括号位置
                    flags[stack.pop()] = 1;  //右括号对应左括号的位置
                }
            }
        }
        int maxLong = 0;
        int Times = 0;
        for (int i = 0; i < flags.length; i++) {
            if (flags[i] == 1) {
                Times++;
                maxLong = Math.max(maxLong, Times);
            } else {
                Times = 0;
            }
        }
        return maxLong;
    }

    public int longestValidParentheses01(String s) {
        int maxLong = 0;
        char[] array = s.toCharArray();
        Stack<Integer> stack = new Stack<>();
        stack.add(-1);
        for (int i = 0; i < array.length; i++) {
            if (array[i] == '(') {
                stack.push(i);
            } else {
                Integer pop = stack.pop();
                if (stack.isEmpty()) {  //等效于取出的是"("下标，因为堆底为"最后一个没有被匹配的右括号的下标"
                    stack.push(i);
                } else {
//                    maxLong = Math.max(maxLong, i - pop + 1);      //不能计算连续区间
                    maxLong = Math.max(maxLong, i - stack.peek());   //才能计算连续区间 ")()())"
                }
            }
        }
        return maxLong;
    }


    /**
     * 524. 通过删除字母匹配到字典里最长单词
     */
    public String findLongestWord3(String sentence, List<String> dictionary) {
        dictionary.sort((o1, o2) -> {   //贪心
            if (o1.length() != o2.length())
                return o2.length() - o1.length();
            else
                return o1.compareTo(o2);
        });
        for (String word : dictionary) {
            int wpos = 0;
            int spos = 0;
            while (wpos < word.length() && spos < sentence.length()) {
                if (word.charAt(wpos) == sentence.charAt(spos)) {
                    wpos++;
                }
                spos++;
            }
            if (wpos == word.length())   //退出循环的条件为 word 遍历完，且第一个退出循环的即为目标值
                return word;
        }
        return "";
    }


    /**
     * 1705. 吃苹果的最大数目
     */
    public int eatenApples02(int[] apples, int[] days) {
        int maxApples = 0;
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[0] - o2[0]);
        int currentDay = 0;
        while (!sortedQueue.isEmpty() || currentDay < apples.length) {  //不能写为 currentDay == 0
            //添加元素
            if (currentDay < apples.length && apples[currentDay] != 0)
                sortedQueue.add(new int[]{currentDay + days[currentDay], apples[currentDay]});
            //剔除过期元素
            while (!sortedQueue.isEmpty() && ((sortedQueue.peek()[0] <= currentDay) || (sortedQueue.peek()[1] == 0)))
                sortedQueue.poll();
            //消耗元素
            if (!sortedQueue.isEmpty()) {
                sortedQueue.peek()[1]--;
                maxApples++;
            }
            currentDay++;
        }
        return maxApples;
    }


    /**
     * 1833. 雪糕的最大数量
     */
    public int maxIceCream01(int[] costs, int coins) {
        Arrays.sort(costs);
        int nums = 0;
        for (int i = 0; i < costs.length; i++) {
            if (coins - costs[i] >= 0) {
                coins -= costs[i];
                nums++;
            }
        }
        return nums;
    }


    /**
     * 1877. 数组中最大数对和的最小值
     */
    public int minPairSum01(int[] nums) {
        Arrays.sort(nums);
        int ans = Integer.MIN_VALUE;
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            ans = Math.max(ans, nums[right] + nums[left]);
            left++;
            right--;
        }
        return ans;
    }


    /**
     * 1846. 减小和重新排列数组后的最大元素
     */
    public int maximumElementAfterDecrementingAndRearranging02(int[] arr) {
        Arrays.sort(arr);
        arr[0] = 1;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] - arr[i - 1] > 1)
                arr[i] = arr[i - 1] + 1;
        }
        return arr[arr.length - 1];
    }

    public int maximumElementAfterDecrementingAndRearranging03(int[] arr) {
        Arrays.sort(arr);
        arr[0] = 1;
        for (int i = 1; i < arr.length; i++) {
            arr[i] = Math.min(arr[i], arr[i - 1] + 1);
        }
        return arr[arr.length - 1];
    }


    /**
     * 1996. 游戏中弱角色的数量
     */
    public int numberOfWeakCharacters02(int[][] properties) {
        int ans = 0;
        Arrays.sort(properties, (o1, o2) -> {
            if (o1[0] != o2[0])
                return o2[0] - o1[0];
            else
                return o1[1] - o2[1];
        });
        int currentMaxDefense = properties[0][1];
        for (int i = 1; i < properties.length; i++) {
            if (currentMaxDefense > properties[i][1])
                ans++;
            currentMaxDefense = Math.max(currentMaxDefense, properties[i][1]);
        }
        return ans;
    }


    /**
     * 2170. 使数组变成交替数组的最少操作数
     */
    public int minimumOperations02(int[] nums) {
        int maxTimes = 0;
        HashMap<Integer, Integer> aTimes = new HashMap<>();
        HashMap<Integer, Integer> bTimes = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if ((i & 1) == 1)
                aTimes.put(nums[i], aTimes.getOrDefault(nums[i], 0) + 1);
            else
                bTimes.put(nums[i], bTimes.getOrDefault(nums[i], 0) + 1);
        }
        //均给两位默认值，防止数组为 [1]
        aTimes.put(0, 0);
        aTimes.put(-1, 0);
        bTimes.put(0, 0);
        bTimes.put(-1, 0);
        ArrayList<Map.Entry<Integer, Integer>> sortedATimes = new ArrayList<>(aTimes.entrySet());
        ArrayList<Map.Entry<Integer, Integer>> sortedBTimes = new ArrayList<>(bTimes.entrySet());
        sortedATimes.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        sortedBTimes.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        Map.Entry<Integer, Integer> aTop1 = sortedATimes.get(0);
        Map.Entry<Integer, Integer> aTop2 = sortedATimes.get(1);
        Map.Entry<Integer, Integer> bTop1 = sortedBTimes.get(0);
        Map.Entry<Integer, Integer> bTop2 = sortedBTimes.get(1);
        if (!aTop1.getKey().equals(bTop1.getKey()))
            maxTimes = aTop1.getValue() + bTop1.getValue();
        else
            maxTimes = Math.max(aTop1.getValue() + bTop2.getValue(), aTop2.getValue() + bTop1.getValue());
        return nums.length - maxTimes;
    }


    public int minimumOperations03(int[] nums) {
        int maxTimes = 0;
        int[] countA = new int[100001];
        int[] countB = new int[100001];
        for (int i = 0; i < nums.length; i++) {
            if ((i & 1) == 1)
                countA[nums[i]]++;    //将 num 映射为 countA 的索引
            else
                countB[nums[i]]++;
        }
        int ATop1 = 0;  //索引 其实就是 num
        int ATop2 = 0;
        int BTop1 = 0;
        int BTop2 = 0;
        for (int i = 0; i < countA.length; i++) {
            if (countA[i] > countA[ATop1]) {     //水流，更新相关元素
                ATop2 = ATop1;  //容易忽略
                ATop1 = i;
            } else if (countA[i] > countA[ATop2]) {
                ATop2 = i;
            }
            if (countB[i] > countB[BTop1]) {
                BTop2 = BTop1;
                BTop1 = i;
            } else if (countB[i] > countB[BTop2]) {
                BTop2 = i;
            }
        }
        maxTimes = ATop1 != BTop1 ? countA[ATop1] + countB[BTop1]
                : Math.max(countA[ATop1] + countB[BTop2], countA[ATop2] + countB[BTop1]);
        return nums.length - maxTimes;
    }


    public int minimumOperations04(int[] nums) {   //仔细思考
        int maxTimes = 0;
        int[][] countA = new int[100001][2];
        int[][] countB = new int[100001][2];
        for (int i = 0; i < nums.length; i++) {
            countA[nums[i]][0] = i;     //构建原始索引 与 浮动索引的映射关系，浮动索引 0 其实就是 num，值 1 为 freq
            countB[nums[i]][0] = i;
        }

        for (int i = 0; i < nums.length; i++) {
            if ((i & 1) == 1)
                countA[nums[i]][1]++;   //奇数位，记录 countA
            else
                countB[nums[i]][1]++;   //偶数位，记录 countB
        }

        Arrays.sort(countA, (o1, o2) -> o2[1] - o1[1]);  //基于频次，降序排列
        Arrays.sort(countB, (o1, o2) -> o2[1] - o1[1]);  //基于频次，降序排列

        maxTimes = countA[0][0] != countB[0][0] ?  //判断奇数和偶数位置，频次最高的 num 是否一致
                countA[0][1] + countB[0][1] : Math.max(countA[0][1] + countB[1][1], countA[1][1] + countB[0][1]);

        return nums.length - maxTimes;
    }


    public int minimumOperations05(int[] nums) {   //仔细思考
        int maxTimes = 0;
        int[][] countA = new int[100001][2];
        int[][] countB = new int[100001][2];
        for (int i = 0; i < countA.length; i++) {
            countA[i][0] = i;     //构建原始索引 与 浮动索引的映射关系，浮动索引 0 其实就是 num，值 1 为 freq
            countB[i][0] = i;
        }

        for (int i = 0; i < nums.length; i++) {
            if ((i & 1) == 1)
                countA[nums[i]][1]++;   //奇数位，记录 countA
            else
                countB[nums[i]][1]++;   //偶数位，记录 countB
        }

        Arrays.sort(countA, (o1, o2) -> o2[1] - o1[1]);  //基于频次，降序排列
        Arrays.sort(countB, (o1, o2) -> o2[1] - o1[1]);  //基于频次，降序排列

        maxTimes = countA[0][0] != countB[0][0] ?  //判断奇数和偶数位置，频次最高的 num 是否一致
                countA[0][1] + countB[0][1] : Math.max(countA[0][1] + countB[1][1], countA[1][1] + countB[0][1]);

        return nums.length - maxTimes;
    }


    /**
     * 1405. 最长快乐字符串
     */
    public String longestDiverseString02(int a, int b, int c) {   //一次取两个元素的思路不对，应该一次取一个
        StringBuilder ans = new StringBuilder();
        HashMap<Integer, Character> map = new HashMap<>();
        map.put(0, 'a');
        map.put(1, 'b');
        map.put(2, 'c');
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o2[1] - o1[1]);  //大根堆，基于剩余频次排序
        if (a != 0) sortedQueue.add(new int[]{0, a});
        if (b != 0) sortedQueue.add(new int[]{1, b});
        if (c != 0) sortedQueue.add(new int[]{2, c});
        int lastAdd = -1;
        while (!sortedQueue.isEmpty()) {
            int Times = 0;
            if (sortedQueue.peek()[0] != lastAdd) {  //当前剩余频次最多的元素，不是上次添加的元素
                int[] poll = sortedQueue.poll();
                Times = Math.min(2, poll[1]);
                poll[1] -= Times;
                if (poll[1] != 0)
                    sortedQueue.add(poll);
                while (Times > 0) {
                    ans.append(map.get(poll[0]));
                    Times--;
                }
                lastAdd = poll[0];  //更新本次append元素
            } else {                                   //当前剩余频次最多的元素，仍是上次添加的元素
                int[] firstPoll = sortedQueue.poll();  //忽略当前元素
                if (!sortedQueue.isEmpty()) {
                    int[] poll = sortedQueue.poll();
                    Times = Math.min(2, poll[1]);
                    poll[1] -= Times;
                    if (poll[1] != 0)
                        sortedQueue.add(poll);
                    while (Times > 0) {
                        ans.append(map.get(poll[0]));
                        Times--;
                    }
                    lastAdd = poll[0];  //更新本次append元素
                } else {
                    return ans.toString();
                }
                sortedQueue.add(firstPoll);   //将忽略的元素添加回队列
            }
        }
        return ans.toString();
    }

    public String longestDiverseString03(int a, int b, int c) {   //一次取两个元素的思路不对，应该一次取一个
        StringBuilder ans = new StringBuilder();
        HashMap<Integer, Character> map = new HashMap<>();
        map.put(0, 'a');
        map.put(1, 'b');
        map.put(2, 'c');
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o2[1] - o1[1]);  //大根堆，基于剩余频次排序
        if (a != 0) sortedQueue.add(new int[]{0, a});
        if (b != 0) sortedQueue.add(new int[]{1, b});
        if (c != 0) sortedQueue.add(new int[]{2, c});
        while (!sortedQueue.isEmpty()) {
            int[] first = sortedQueue.poll();
            int alen = ans.length();
            if (alen >= 2 && ans.charAt(alen - 1) == map.get(first[0]) && ans.charAt(alen - 2) == map.get(first[0])) {
                if (sortedQueue.isEmpty())           //无剩余可用元素，贪心结束，返回结果
                    return ans.toString();
                int[] second = sortedQueue.poll();   //否则，使用第二个元素继续贪心
                ans.append(map.get(second[0]));
                second[1]--;
                if (second[1] != 0)
                    sortedQueue.add(second);
                sortedQueue.add(first);
            } else {  //直接基于第一个元素，进行贪心
                ans.append(map.get(first[0]));
                first[1]--;
                if (first[1] != 0)
                    sortedQueue.add(first);
            }
        }
        return ans.toString();
    }


    public int maxFrequency01(int[] nums, int k) {
        int maxWindow = 0;
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            int[] temp = Arrays.copyOf(nums, nums.length);
            int right = i;
            int m = k;
            while (m != 0 && temp[i] != temp[right]) {
                m--;
                maxWindow = Math.max(maxWindow, right - i + 1);
                right++;
            }


        }


        return maxWindow;
    }

    /**
     * 1838. 最高频元素的频数
     */
    public int maxFrequency02(int[] nums, int k) {
        Arrays.sort(nums);
        int left = 0;
        int right = 1;
        int maxWindow = 1;
        long sum = 0;
        while (right < nums.length) {
            sum += (long) (right - left) * (nums[right] - nums[right - 1]);   // right - left 代表除了 right 以外的区间长度
            while (sum > k) {
                sum -= nums[right] - nums[left];
                left++;
            }
            maxWindow = Math.max(maxWindow, right - left + 1);   // 包含 right 在内的区间长度
            right++;
        }
        return maxWindow;
    }


    /**
     * 1588. 所有奇数长度子数组的和
     */
    public int sumOddLengthSubarrays(int[] nums) {
        int ans = 0;
        int[] prefixSum = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        int maxWindow = (nums.length & 1) == 1 ? nums.length : nums.length - 1;
        for (int window = 1; window <= maxWindow; window += 2) {
            int left = 0;
            int right = window + left - 1;
            while (right < nums.length) {
                ans += prefixSum[right + 1] - prefixSum[left];
                right++;
                left++;
            }
        }
        return ans;
    }

    public int sumOddLengthSubarrays01(int[] nums) {
        int ans = 0;
        int maxWindow = (nums.length & 1) == 1 ? nums.length : nums.length - 1;
        for (int window = 1; window <= maxWindow; window += 2) {
            int left = 0;
            int right = 0;
            int prefix = 0;
            while (right < nums.length) {
                prefix += nums[right];
                while (right - left + 1 > window) {
                    prefix -= nums[left];
                    left++;
                }
                if (right - left + 1 == window)
                    ans += prefix;
                right++;
            }
        }
        return ans;
    }


    /**
     * 1221. 分割平衡字符串
     */
    public int balancedStringSplit02(String s) {
        int ans = 0;
        char[] array = s.toCharArray();
        int left = 0;
        int right = 0;
        int numL = 0;
        int numR = 0;
        while (right < array.length) {
            if (array[right] == 'L')
                numL++;
            else
                numR++;
            if (numL == numR) {
                ans++;
                numL = 0;
                numR = 0;
            }
            right++;
        }
        return ans;
    }

    public int balancedStringSplit03(String s) {
        int ans = 0;
        int sum = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'L')
                sum++;
            else
                sum--;
            if (sum == 0)
                ans++;
        }
        return ans;
    }


    /**
     * 1005. K 次取反后最大化的数组和
     */
    public int largestSumAfterKNegations06(int[] nums, int k) {
        int sum = 0;
        Arrays.sort(nums);
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            hTable.put(nums[i], hTable.getOrDefault(nums[i], 0) + 1);
        }
        // 负数
        for (int i = -100; i < 0; i++) {
            if (hTable.containsKey(i)) {
                Integer freq = hTable.get(i);
                int times = Math.min(freq, k);
                sum -= 2 * times * i;
                k -= times;
                hTable.put(-i, hTable.getOrDefault(-i, 0) + freq);
                if (k == 0)
                    return sum;
            }
        }

        // 零 和 偶数 K
        if (hTable.containsKey(0) || (k & 1) == 0)
            return sum;

        // 正数
        for (int i = 1; i <= 100; i++) {
            if (hTable.containsKey(i)) {  //首个正数，反转一次即可
                sum -= 2 * i;
                break;
            }
        }
        return sum;
    }


    /**
     * 881. 救生艇
     */
    public int numRescueBoats01(int[] people, int limit) {
        int ans = 0;
        Arrays.sort(people);
        int left = 0;
        int right = people.length - 1;
        while (left <= right) {
            if (people[left] + people[right] <= limit) {
                left++;
            }
            right--;
            ans++;
        }
        return ans;
    }


    /**
     * 169. 多数元素
     */
    public int majorityElement(int[] nums) {  //同归于尽，题目同面试题 17.10. 主要元素
        int win = nums[0];
        int remain = 1;
        //----------------------------------------------------------
        // 逐个士兵发起进攻占领高地，最终占领高地的士兵所属团队即为胜利的一方
        //----------------------------------------------------------
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == win) {        //1、冲锋的士兵和当前占领高地的士兵，属于同一个集团军
                remain++;       //当前控制高地的集团军士兵数增加一个
            } else if (remain > 0) {     //2、冲锋的士兵和当前占领高地的士兵，不属于同一个集团军，但当前高地中仍有士兵可抵挡当前冲锋的士兵
                remain--;       //高地中的一个士兵与当前冲锋的士兵同归于尽，高地所属方不变
            } else if (remain == 0) {    //3、冲锋的士兵和当前占领高地的士兵，不属于同一个集团军，但当前高地中已经没有士兵可抵挡当前冲锋的士兵
                win = nums[i];  //高地易主
                remain = 1;
            }
        }
        return win;
    }

    public int majorityElement01(int[] nums) {  //投票法，和上面类似
        int win = nums[0];
        int count = 1;      //票数
        for (int i = 1; i < nums.length; i++) {
            if (count == 0) {
                win = nums[i];
            }
            count += nums[i] == win ? 1 : -1;   //更新获胜者时，其应该对应 1 票，逻辑在这里处理
        }
        return win;
    }

    public int majorityElement02(int[] nums) {  //贪心
        Arrays.sort(nums);
        return nums[nums.length / 2];
    }


    /**
     * 228. 汇总区间
     */
    public List<String> summaryRanges(int[] nums) {  //自己写的逻辑
        List<String> ans = new ArrayList<>();
        if (nums.length == 0) return ans;
        int left = 0;
        int right = 1;
        while (right < nums.length) {
            if (nums[right] != nums[right - 1] + 1) {  //不连续
                if (right - 1 - left + 1 == 1) //区间长度为 1
                    ans.add(String.valueOf(nums[left]));
                else {
                    ans.add(nums[left] + "->" + nums[right - 1]);
                }
                left = right;
            }
            right++;
        }
        if (left != right) {
            if (left == right - 1)
                ans.add(String.valueOf(nums[left]));
            else
                ans.add(nums[left] + "->" + nums[right - 1]);
        }
        return ans;
    }

    public List<String> summaryRanges01(int[] nums) {
        List<String> ans = new ArrayList<>();
        int current = 0;
        while (current < nums.length) {
            int left = current;
            current++;
            while (current < nums.length && nums[current] == nums[current - 1] + 1) {  //连续区间
                current++;
            }
            int right = current - 1; //连续区间 [left,right]
            if (right == left) //1、区间长度为 1
                ans.add(String.valueOf(nums[left]));
            else //2、区间长度不为 1
                ans.add(nums[left] + "->" + nums[right]);
        }
        return ans;
    }


    /**
     * 349. 两个数组的交集
     */
    public int[] intersection(int[] nums1, int[] nums2) {
        int[] ans1 = new int[1001];
        int[] ans2 = new int[1001];
        ArrayList<Integer> ans = new ArrayList<>();
        for (int num : nums1) {
            ans1[num] = 1;
        }
        for (int num : nums2) {
            ans2[num] = 1;
        }
        for (int i = 0; i < 1001; i++) {
            if (ans1[i] == 1 && ans2[i] == 1)
                ans.add(i);
        }
        return ans.stream().mapToInt(Integer::intValue).toArray();
    }


    /**
     * 350. 两个数组的交集 II
     */
    public int[] intersect(int[] nums1, int[] nums2) {  // by myself
        int[] ans1 = new int[1001];
        int[] ans2 = new int[1001];
        ArrayList<Integer> ans = new ArrayList<>();
        for (int num : nums1) {
            ans1[num]++;
        }
        for (int num : nums2) {
            ans2[num]++;
        }
        for (int i = 0; i < 1001; i++) {
            int freq = Math.min(ans1[i], ans2[i]);
            while (freq > 0) {
                ans.add(i);
                freq--;
            }
        }
        return ans.stream().mapToInt(Integer::intValue).toArray();  //慢，耗时
    }


    public int[] intersect00(int[] nums1, int[] nums2) {  // by myself
        int[] ans1 = new int[1001];
        int[] ans2 = new int[1001];
        int[] ans = new int[Math.min(nums1.length, nums2.length)];
        int current = 0;
        for (int num : nums1) {
            ans1[num]++;
        }
        for (int num : nums2) {
            ans2[num]++;
        }
        for (int i = 0; i < 1001; i++) {
            int freq = Math.min(ans1[i], ans2[i]);
            while (freq > 0) {
                ans[current] = i;
                current++;
                freq--;
            }
        }
        return Arrays.copyOfRange(ans, 0, current);  //效率高
    }

    public int[] intersect01(int[] nums1, int[] nums2) {  //by offical
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        ArrayList<Integer> ans = new ArrayList<>();
        int index1 = 0;
        int index2 = 0;
        while (index1 < nums1.length && index2 < nums2.length) {
            if (nums1[index1] < nums2[index2])
                index1++;
            else if (nums1[index1] > nums2[index2])
                index2++;
            else {
                ans.add(nums1[index1]);
                index1++;
                index2++;
            }
        }
        return ans.stream().mapToInt(Integer::intValue).toArray();  //慢，耗时
    }


    public int[] intersect02(int[] nums1, int[] nums2) {  //by offical
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        int[] ans = new int[Math.min(nums1.length, nums2.length)];
        int index = 0;
        int index1 = 0;
        int index2 = 0;
        while (index1 < nums1.length && index2 < nums2.length) {
            if (nums1[index1] < nums2[index2])
                index1++;
            else if (nums1[index1] > nums2[index2])
                index2++;
            else {
                ans[index] = nums1[index1];
                index++;
                index1++;
                index2++;
            }
        }
        return Arrays.copyOfRange(ans, 0, index);   //效率高
    }


    /**
     * 448. 找到所有数组中消失的数字
     */
    public List<Integer> findDisappearedNumbers(int[] nums) {
        int len = nums.length;
        ArrayList<Integer> ans = new ArrayList<>();
        int[] buckets = new int[len + 1];  //保证可存储 len
        for (int num : nums) {
            buckets[num] = 1;
        }
        for (int i = 1; i < buckets.length; i++) {  //忽略 0
            if (buckets[i] == 0)
                ans.add(i);
        }
        return ans;
    }


    /**
     * 496. 下一个更大元素 I
     */
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        //------------------------------------
        // 下一个更大元素的解法：基于 单调栈，非常好
        // https://leetcode.cn/problems/next-greater-element-i/solution/dan-diao-zhan-jie-jue-next-greater-number-yi-lei-w/
        //------------------------------------

        int[] ans = new int[nums1.length];
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 0; i < nums2.length; i++) {
            hTable.put(nums2[i], i);   //有助于快速基于 value 获取索引 index
        }
        for (int i = 0; i < nums1.length; i++) {
            Integer index = hTable.get(nums1[i]);
            while (index < nums2.length) {
                if (nums2[index] > nums1[i]) {
                    ans[i] = nums2[index];
                    break;
                }
                index++;
                if (index == nums2.length) {
                    ans[i] = -1;
                    break;
                }
            }
        }
        return ans;
    }


    /**
     * 414. 第三大的数
     */
    public int thirdMax(int[] nums) {
        HashSet<Integer> hTable = new HashSet<>();
        for (int num : nums) {
            hTable.add(num);
        }
        ArrayList<Integer> sorted = new ArrayList<>(hTable);
        sorted.sort(Collections.reverseOrder());
        return sorted.size() < 3 ? sorted.get(0) : sorted.get(2);
    }


    public int thirdMax01(int[] nums) {
        HashSet<Integer> hTable = new HashSet<>();
        for (int num : nums) {
            hTable.add(num);
        }
        Integer[] sorted = new Integer[hTable.size()];
        int i = 0;
        for (Integer num : hTable) {
            sorted[i++] = num;
        }
        Arrays.sort(sorted, Comparator.reverseOrder());  //降序
        return sorted.length < 3 ? sorted[0] : sorted[2];
    }

    public int thirdMax02(int[] nums) {
        Arrays.sort(nums);
        int right = nums.length - 1;
        int times = 1;
        while (right >= 0) {
            right--;
            while (right >= 0 && nums[right] == nums[right + 1]) {
                right--;
            }
            times++;
            if (right >= 0 && times == 3) return nums[right];
        }
        return nums[nums.length - 1];
    }

    /**
     * 455. 分发饼干
     */
    public int findContentChildren(int[] g, int[] s) {
        int ans = 0;
        int indexS = 0;
        int indexG = 0;
        Arrays.sort(g);
        Arrays.sort(s);
        while (indexG < g.length && indexS < s.length) {
            if (g[indexG] <= s[indexS]) {
                indexG++;
                indexS++;
                ans++;
            } else {
                indexS++;
            }
        }
        return ans;
    }

    /**
     * 506. 相对名次
     */
    public String[] findRelativeRanks(int[] score) {
        String[] ans = new String[score.length];
        HashMap<Integer, String> hTable = new HashMap<>();
        int[] sorted = Arrays.copyOf(score, score.length);
        Arrays.sort(sorted);
        int i = sorted.length - 1;
        int times = 1;
        while (i >= 0) {
            if (i == sorted.length - 1)
                hTable.put(sorted[i], "Gold Medal");
            else if (i == sorted.length - 2)
                hTable.put(sorted[i], "Silver Medal");
            else if (i == sorted.length - 3)
                hTable.put(sorted[i], "Bronze Medal");
            else
                hTable.put(sorted[i], String.valueOf(times));
            i--;
            times++;
        }
        for (int m = 0; m < score.length; m++) {
            ans[m] = hTable.get(score[m]);
        }
        return ans;
    }


    /**
     * 566. 重塑矩阵
     */
    public int[][] matrixReshape(int[][] mat, int r, int c) {
        int rows = mat.length;
        int columns = mat[0].length;
        if (r * c != rows * columns)
            return mat;
        int[] res = new int[r * c];
        int current = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                res[current] = mat[i][j];
                current++;
            }
        }
        current = 0;
        int[][] ans = new int[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                ans[i][j] = res[current];
                current++;
            }
        }
        return ans;
    }


    /**
     * 13. 罗马数字转整数
     */
    public int romanToInt(String s) {
        //预处理，添加以下 6 中对照表
        s = s.replace("IV", "a");    //转换是比较好的思路
        s = s.replace("IX", "b");
        s = s.replace("XL", "c");
        s = s.replace("XC", "d");
        s = s.replace("CD", "e");
        s = s.replace("CM", "f");
        int sum = 0;
        for (int i = 0; i < s.length(); i++) {
            sum += convertValue(s.charAt(i));
        }
        return sum;
    }

    private int convertValue(char cha) {
        switch (cha) {
            case 'a':
                return 4;
            case 'b':
                return 9;
            case 'c':
                return 40;
            case 'd':
                return 90;
            case 'e':
                return 400;
            case 'f':
                return 900;
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                return 0;
        }
    }

    /**
     * 67. 二进制求和
     */
    public String addBinary(String a, String b) {   //模拟，自己做的
        StringBuilder ans = new StringBuilder();
        char append = '0';
        int i = 0;
        int leftA = a.length() - 1;
        int leftB = b.length() - 1;
        int minIndex = Math.min(a.length() - 1, b.length() - 1);
        while (i <= minIndex) {
            if ((a.charAt(leftA - i) == b.charAt(leftB - i) && b.charAt(leftB - i) == '1')) { //均为 1，故和为 0，进一位
                //本位相加后，要向下进一位，且本位为 0
                ans.append(append == '1' ? 1 : 0);  //故本位的值取决于上一位 append是否进位
                append = '1';    //此位决定向下一位进一
            } else if (a.charAt(leftA - i) == '1' || b.charAt(leftB - i) == '1') { //和为 1
                ans.append(append == '1' ? 0 : 1);
                append = (append == '1' ? '1' : '0');
            } else { //均为 0，故和为 0，无进位
                ans.append(append == '1' ? 1 : 0);
                append = '0';
            }
            i++;
        }
        while (i <= leftA) {
            if (a.charAt(leftA - i) == '1') {
                ans.append(append == '1' ? 0 : 1);
                append = (append == '1' ? '1' : '0');
            } else {
                ans.append(append == '1' ? 1 : 0);
                append = '0';
            }
            i++;
        }
        while (i <= leftB) {
            if (b.charAt(leftB - i) == '1') {
                ans.append(append == '1' ? 0 : 1);
                append = (append == '1' ? '1' : '0');
            } else {
                ans.append(append == '1' ? 1 : 0);
                append = '0';
            }
            i++;
        }
        if (append == '1') ans.append(1);
        return ans.reverse().toString();
    }

    public String addBinary01(String a, String b) {
        int aa = Integer.parseInt(a, 2);  //有缺席，过长的不能转换
        int bb = Integer.parseInt(b, 2);
        while (bb != 0) {
            int up = (aa & bb) << 1;
            aa ^= bb;
            bb = up;
        }
        return Integer.toBinaryString(aa);
    }


    /**
     * 125. 验证回文串
     */
    public boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;
        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }
            char aa = Character.isLetter(s.charAt(left)) ? Character.toLowerCase(s.charAt(left)) : s.charAt(left);
            char bb = Character.isLetter(s.charAt(right)) ? Character.toLowerCase(s.charAt(right)) : s.charAt(right);

            if (aa != bb)
                return false;
            left++;
            right--;
        }
        return true;
    }


    /**
     * 171. Excel 表列序号
     */
    public int titleToNumber(String columnTitle) {
        StringBuilder ans = new StringBuilder();
        StringBuilder reverse = ans.append(columnTitle).reverse();
        int sum = 0;
        int current = 0;
        while (current < reverse.length()) {
            sum += Math.pow(26, current) * (reverse.charAt(current) - 'A' + 1);
            current++;
        }
        return sum;
    }


    /**
     * 28. 实现 strStr()
     */
    public int strStr(String haystack, String needle) {
        if (needle.equals(""))
            return 0;
        if (!haystack.contains(needle))
            return -1;
        int left = 0;
        int right = needle.length();
        while (right <= haystack.length()) {
            if (haystack.substring(left, right).equals(needle))
                return left;
            left++;
            right++;
        }
        return -1;
    }

    public int strStr01(String haystack, String needle) {
        if (needle.equals(""))
            return 0;
        if (!haystack.contains(needle))
            return -1;
        return haystack.indexOf(needle);
    }


    /**
     * 205. 同构字符串
     */
    public boolean isIsomorphic(String s, String t) {  //相互的关系 唯一对应，不存在一对多的情况
        char[] aa = s.toCharArray();
        char[] bb = t.toCharArray();
        HashMap<Character, Character> hTable = new HashMap<>();
        for (int i = 0; i < aa.length; i++) {
            if (hTable.containsKey(aa[i])) {
                if (hTable.get(aa[i]) != bb[i])
                    return false;
            } else {
                if (hTable.containsValue(bb[i]))
                    return false;
                hTable.put(aa[i], bb[i]);
            }
        }
        return true;
    }

    public boolean isIsomorphic01(String s, String t) {  //思路同下
        int[] m1 = new int[256];
        int[] m2 = new int[256];
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (m1[s.charAt(i)] != m2[t.charAt(i)])
                return false;
            m1[s.charAt(i)] = i + 1;  //因为 int默认值为 0，为了避开默认值，其实给 i + 100也可行
            m2[t.charAt(i)] = i + 1;
        }
        return true;
    }

    public boolean isIsomorphic02(String s, String t) {
        for (int i = 0; i < s.length(); i++) {
            //s与 t的前缀就是映射关系，如果后面的和前面的不一致，就报错
            if (s.indexOf(s.charAt(i)) != t.indexOf(t.charAt(i))) {  //结合 "badc"与 "baba"、"paper"与 "title"深入体会
                return false;
            }
        }
        return true;
    }


    public boolean isIsomorphic001(String s, String t) {
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
     * 387. 字符串中的第一个唯一字符
     */
    public int firstUniqChar(String s) {
        int[] ans = new int[26];
        for (int i = 0; i < s.length(); i++) {
            ans[s.charAt(i) - 'a']++;
        }
        for (int i = 0; i < s.length(); i++) {
            if (ans[s.charAt(i) - 'a'] == 1)
                return i;
        }
        return -1;
    }


    /**
     * 389. 找不同
     */
    public char findTheDifference(String s, String t) {
        int xor = 0;
        for (int i = 0; i < s.length(); i++) {
            xor ^= s.charAt(i);
        }
        for (int i = 0; i < t.length(); i++) {
            xor ^= t.charAt(i);
        }
        return (char) xor;
    }


    /**
     * 371. 两整数之和
     */
    public int getSum(int a, int b) {
        while (b != 0) {
            int up = (a & b) << 1;   //进位结果
            a ^= b;   //无进位
            b = up;
        }
        return a;
    }


    public int getSum01(int a, int b) {
        int sum = 0;
        int append = 0;  //0/1
        for (int i = 0; i < 32; i++) {
            int aa = (a >> i) & 1;
            int bb = (b >> i) & 1;
            if (aa == 1 && bb == 1) {   //均为 1，进一位，本位取决于 上一位是否进位
                sum |= (append << i);
                append = 1;
            } else if (aa == 1 || bb == 1) {//和为 1，本位取决于上一位是否进位
                if (append == 1) {
                    sum |= (0 << i);
                    append = 1;
                } else {
                    sum |= (1 << i);
                    append = 0;
                }
            } else {
                sum |= (append << i);
                append = 0;
            }
        }
        return sum;
    }


    /**
     * 476. 数字的补数
     */
    public int findComplement(int num) {
        int ans = 0;
        for (int i = 0; i < 32 && (num >> i) != 0; i++) {   //num！=0是关键
            ans |= ((((num >> i) & 1) ^ 1)) << i;
        }
        return ans;
    }

    public int findComplement01(int num) {
        int length = Integer.toBinaryString(num).length();
        return num ^ ((1 << length) - 1);
    }


    /**
     * 78. 子集
     * 回溯问题
     * https://leetcode.cn/problems/subsets/solution/dai-ma-sui-xiang-lu-78-zi-ji-hui-su-sou-6yfk6/
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> dfs = new LinkedList<>();
        if (nums.length == 0) {
            ans.add(new ArrayList<>());
            return ans;
        }
        subsetsDfs(nums, ans, dfs, 0);
        return ans;
    }

    private void subsetsDfs(int[] nums, List<List<Integer>> ans, LinkedList<Integer> dfs, int currentIndex) {
        ans.add(new ArrayList<>(dfs));
        if (currentIndex >= nums.length)
            return;
        for (int i = currentIndex; i < nums.length; i++) {
            dfs.add(nums[i]);
            subsetsDfs(nums, ans, dfs, i + 1);
            dfs.removeLast();
        }
    }


    /**
     * 1405. 最长快乐字符串
     */
    public String longestDiverseString04(int a, int b, int c) {
        StringBuilder ans = new StringBuilder();
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o2[1] - o1[1]);
        if (a > 0)
            sortedQueue.add(new int[]{'a', a});
        if (b > 0)
            sortedQueue.add(new int[]{'b', b});
        if (c > 0)
            sortedQueue.add(new int[]{'c', c});

        while (!sortedQueue.isEmpty()) {
            int[] first = sortedQueue.poll();
            if (ans.length() < 2) {
                ans.append((char) first[0]);
                first[1]--;
                if (first[1] > 0)
                    sortedQueue.add(first);
            } else {
                if (ans.charAt(ans.length() - 1) == ans.charAt(ans.length() - 2) && ans.charAt(ans.length() - 1) == (char) first[0]) {
                    if (sortedQueue.isEmpty())
                        return ans.toString();
                    int[] second = sortedQueue.poll();
                    ans.append((char) second[0]);
                    second[1]--;
                    if (second[1] > 0)
                        sortedQueue.add(second);
                    sortedQueue.add(first);
                } else {
                    ans.append((char) first[0]);
                    first[1]--;
                    if (first[1] > 0)
                        sortedQueue.add(first);
                }
            }
        }
        return ans.toString();
    }


    /**
     * 1705. 吃苹果的最大数目
     */
    public int eatenApples10(int[] apples, int[] days) {
        int ans = 0;
        int currentDay = 0;
        //小根堆
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[0] - o2[0]); //过期日期：苹果数
        while (currentDay < apples.length || !sortedQueue.isEmpty()) {
            //1、移除过期的苹果
            while (!sortedQueue.isEmpty() && currentDay >= sortedQueue.peek()[0]) {
                sortedQueue.poll();
            }

            //2、添加当日新长出来的苹果
            if (currentDay < apples.length && days[currentDay] != 0) {
                sortedQueue.add(new int[]{currentDay + days[currentDay], apples[currentDay]});  //不用合并，不会覆盖
            }

            //3、消耗苹果
            if (!sortedQueue.isEmpty()) {
                int[] poll = sortedQueue.poll();
                poll[1]--;
                if (poll[1] > 0)
                    sortedQueue.add(poll);
                ans++;
            }
            currentDay++;
        }
        return ans;
    }


    /**
     * 1846. 减小和重新排列数组后的最大元素
     */
    public int maximumElementAfterDecrementingAndRearranging10(int[] arr) {
        Arrays.sort(arr);
        arr[0] = 1;
        for (int i = 1; i < arr.length; i++) {
            arr[i] = Math.min(arr[i - 1] + 1, arr[i]);
        }
        return arr[arr.length - 1];
    }


    /**
     * 162. 寻找峰值
     */
    public int findPeakElement10(int[] nums) {
        int[] convert = new int[nums.length + 2];
        convert[0] = Integer.MIN_VALUE;
        convert[nums.length + 1] = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            convert[i + 1] = nums[i];
            if (i > 0 && convert[i] > convert[i - 1] && convert[i] > convert[i + 1])
                return i - 1;
        }
        if (convert[nums.length] > convert[nums.length - 1] && convert[nums.length] > convert[nums.length + 1])
            return nums.length - 1;
        return 0;
    }

    public int findPeakElement11(int[] nums) {
        int index = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[i - 1])   //大于左侧，则记录
                index = i;
            //小于左侧，则不记录，从而 index 记录了最右侧的峰值
        }
        return index;
    }

    public int findPeakElement12(int[] nums) {
        int index = nums.length - 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            if (nums[i] > nums[i + 1])  //大于右侧，则记录
                index = i;
            //小于右侧，则不记录，从而 index 记录了最左侧的峰值
        }
        return index;
    }


    /**
     * 1475. 商品折扣后的最终价格
     */
    public int[] finalPrices(int[] prices) {
        for (int i = 0; i < prices.length; i++) {
            for (int j = i + 1; j < prices.length; j++) {
                if (prices[i] >= prices[j]) {
                    prices[i] -= prices[j];
                    break;
                }
            }
        }
        return prices;
    }


    /**
     * 9. 回文数
     */
    public boolean isPalindrome(int x) {
        if (x < 0) return false;
        String xx = String.valueOf(x);
        if (xx.length() == 1) return true;
        int left = 0;
        int right = xx.length() - 1;
        while (left < right) {
            if (xx.charAt(left) != xx.charAt(right))
                return false;
            left++;
            right--;
        }
        return true;
    }


    /**
     * 258. 各位相加
     */
    public int addDigits(int num) {
        if (num < 10) return num;
        while (true) {
            int total = 0;
            String xx = String.valueOf(num);
            for (int i = 0; i < xx.length(); i++) {
                total += (xx.charAt(i) - '0');
            }
            num = total;
            if (num < 10)
                return num;
        }
    }


    /**
     * 75. 颜色分类
     */
    public void sortColors(int[] nums) {
        int[] buckets = new int[3];
        for (int num : nums) {
            buckets[num]++;
        }
        int currentIndex = 0;
        for (int i = 0; i < buckets.length; i++) {
            while (buckets[i] > 0) {
                nums[currentIndex] = i;
                currentIndex++;
                buckets[i]--;
            }
        }
    }


    /**
     * 89. 格雷编码
     */
    public List<Integer> grayCode(int n) {
        List<Integer> ret = new ArrayList<Integer>();
        ret.add(0);
        for (int i = 1; i <= n; i++) {
            int m = ret.size();
            for (int j = m - 1; j >= 0; j--) {
                ret.add(ret.get(j) | (1 << (i - 1)));
            }
        }
        return ret;
    }

    public List<Integer> grayCode01(int n) {
        List<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < 1 << n; i++) {
            ret.add((i >> 1) ^ i);
        }
        return ret;
    }

    public List<Integer> grayCode02(int n) {
        List<Integer> ans = new ArrayList<>();
        ans.add(0);
        while (n-- > 0) {
            int m = ans.size();
            for (int i = m - 1; i >= 0; i--) {
                ans.set(i, ans.get(i) << 1);
                ans.add(ans.get(i) + 1);
            }
        }
        return ans;
    }


    /**
     * 290. 单词规律
     */
    public boolean wordPattern(String pattern, String s) {
        String[] words = s.trim().split(" ");
        if (words.length != pattern.length()) return false;
        HashMap<String, String> hTable = new HashMap<>();
        boolean more = words[0].length() > 1;   //主要解决 pattern = "abc",s = "b c a"; 为 true的情况
        for (int i = 0; i < pattern.length(); i++) {
            String xx = String.valueOf(pattern.charAt(i));
            if (hTable.containsKey(xx) && !hTable.get(xx).equals(words[i])) {
                return false;
            }
            if (more) {
                if (hTable.containsKey(words[i]) && !hTable.get(words[i]).equals(xx))
                    return false;
                hTable.put(words[i], xx);
            }
            hTable.put(xx, words[i]);
        }
        return true;
    }

    public boolean wordPattern01(String pattern, String str) {
        String[] words = str.split(" ");
        if (words.length != pattern.length()) {
            return false;
        }
        Map<Object, Integer> map = new HashMap<>();
        for (Integer i = 0; i < words.length; i++) {
            if (map.put(pattern.charAt(i), i) != map.put(words[i], i)) {   //非常好的思路，双向映射
                return false;
            }
        }
        return true;
    }


    /**
     * 605. 种花问题
     */
    public boolean canPlaceFlowers(int[] flowerbed, int n) {
        if (n == 0) return true;
        int[] flowering = new int[flowerbed.length + 2];
        System.arraycopy(flowerbed, 0, flowering, 1, flowerbed.length);
        for (int i = 1; i < flowering.length - 1; i++) {  //不遍历两头
            if (flowering[i] == 0 && flowering[i - 1] == 0 && flowering[i + 1] == 0) {  //满足条件
                flowering[i] = 1;
                n--;
                if (n == 0) {    //提前跳出，及时返回
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 680. 验证回文串 II
     */
    public boolean validPalindrome(String str) {  //错误
        StringBuilder builder = new StringBuilder();
        int[][] directions = {{1, 0}, {0, -1}};
        int left = 0;
        int right = str.length() - 1;
        return validPalindromeDfs(str, directions, left, right, 1);
    }

    private boolean validPalindromeDfs(String str, int[][] directions, int left, int right, int k) {
        while (left < right && str.charAt(left) == str.charAt(right)) {
            left++;
            right--;
        }
        //迭代终止条件
        if (left == right || left == right + 1 || (left + 1 == right && k == 1)) { // A A 奇数/偶数导致跳到同一点或跳过、 A B且此时还可剔除掉一个
            return true;
        }
        for (int[] direction : directions) {
            int leftNext = left + direction[0];
            int rightNext = right + direction[1];
            if (k == 1 && validPalindromeDfs(str, directions, leftNext, rightNext, k - 1))
                return true;
        }
        return false;
    }


    /**
     * 630. 课程表 III
     */
    public int scheduleCourse01(int[][] courses) {
        int currentDay = 0;
        Arrays.sort(courses, (o1, o2) -> o1[1] - o2[1]);  //按照结束时间升序排序
        //存储已选课程
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o2[0] - o1[0]);  //按照持续时间降序排序
        for (int[] course : courses) {
            if (currentDay + course[0] <= course[1]) {
                currentDay += course[0];
                sortedQueue.add(course);
            } else if (!sortedQueue.isEmpty() && course[0] < sortedQueue.peek()[0]) {
                //-------------------------------------------------
                // 此处其实无需判断以下两个条件：
                //     1、将 A 替换为 B 后，B 一定满足 currentDay < B[1]
                //          其中替换后，因为 A 放进队列是满足条件的，且 B[0] < A[0]，因此一定有
                //             currentDay < A[1] < B[1]
                //     2、将 A 替换掉后，不会有机会再放回去
                //          可以全局考虑，将 A 替换 B，后续 B 再添加回队列的场景，等效于 A B 均在队列中，并在队列中交换位置
                //              因为，上面的条件已经判断了， A B 同时放在队列中，时间不满足，如果均在队列中，会有
                //              A[1] < B[1] < currentDay，一定是超过 A 的结束时间的
                //-------------------------------------------------
                int[] remove = sortedQueue.poll();
                currentDay -= remove[0];
                sortedQueue.add(course);
                currentDay += course[0];
            }
        }
        return sortedQueue.size();
    }


    /**
     * 696. 计数二进制子串
     */
    public int countBinarySubstrings(String str) {
        ArrayList<Integer> counts = new ArrayList<>();
        int currentIndex = 0;
        int len = str.length();
        while (currentIndex < len) {
            int count = 0;
            char xx = str.charAt(currentIndex);
            while (currentIndex < len && str.charAt(currentIndex) == xx) {
                count++;        //计数
                currentIndex++; //移动
            }
            counts.add(count);
        }
        int ans = 0;
        for (int i = 1; i < counts.size(); i++) {
            //-----------------------------------------------
            // 核心逻辑：相邻两数的个数，取最小值，削平二者，然后..配对
            //-----------------------------------------------
            ans += Math.min(counts.get(i - 1), counts.get(i));
        }
        return ans;
    }


    /**
     * 674. 最长连续递增序列
     */
    public int findLengthOfLCIS(int[] nums) {
        int maxWindow = 1;
        int left = 0;
        int right = 1;
        while (right < nums.length) {
            int count = 1;
            while (right < nums.length && nums[right - 1] < nums[right]) {
                right++;
                count++;
            }
            if (right < nums.length && nums[right] <= nums[right - 1]) {     //跳过此位
                right++;
            }
            maxWindow = Math.max(maxWindow, count);
        }
        return maxWindow;
    }

    public int findLengthOfLCIS00(int[] nums) {
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
     * 657. 机器人能否返回原点
     */
    public boolean judgeCircle(String moves) {
        int up = 0;
        int down = 0;
        int left = 0;
        int right = 0;
        for (int i = 0; i < moves.length(); i++) {
            char xx = moves.charAt(i);
            up = xx == 'U' ? up + 1 : up;
            down = xx == 'D' ? down + 1 : down;
            left = xx == 'L' ? left + 1 : left;
            right = xx == 'R' ? right + 1 : right;
        }
        return up == down && left == right;
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
     * 409. 最长回文串
     */
    public int longestPalindrome(String str) {
        int[] buckets = new int[256];
        int base = 0;
        int remain = 0;
        for (int i = 0; i < str.length(); i++) {
            buckets[str.charAt(i)]++;
        }
        for (int i = 0; i < 256; i++) {
            if (buckets[i] == 0)
                continue;
            if ((buckets[i] & 1) == 0) {   //值为偶数，对称放置
                base += buckets[i];
            } else {                       //值为奇数，对称放置，前面减掉一
                base += buckets[i] - 1;
                remain++;
            }
        }
        return remain == 0 ? base : base + 1;
    }


    /**
     * 661. 图片平滑器
     */
    public int[][] imageSmoother(int[][] img) {
        int rows = img.length;
        int cols = img[0].length;
        int[][] ans = new int[rows][cols];
        int[][] directions = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 0}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int sum = 0;
                int count = 0;
                for (int[] dir : directions) {
                    int xx = i + dir[0];
                    int yy = j + dir[1];
                    if (xx < 0 || xx >= rows || yy < 0 || yy >= cols)
                        continue;
                    sum += img[xx][yy];
                    count++;
                }
                ans[i][j] = sum / count;
            }
        }
        return ans;
    }


    public int[][] imageSmoother01(int[][] img) {
        int rows = img.length;
        int cols = img[0].length;
        int[][] ans = new int[rows][cols];
        int[][] prefixSum = new int[rows + 1][cols + 1];
        for (int i = 1; i < prefixSum.length; i++) {
            for (int j = 1; j < prefixSum[0].length; j++) {
                prefixSum[i][j] = prefixSum[i - 1][j] + prefixSum[i][j - 1] + img[i - 1][j - 1] - prefixSum[i - 1][j - 1];
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int left = Math.max(j - 1, 0);
                int right = Math.min(j + 1, cols - 1);
                int top = Math.max(i - 1, 0);
                int bottom = Math.min(i + 1, rows - 1);
                int areas = (right - left + 1) * (bottom - top + 1);
                int sum = prefixSum[bottom + 1][right + 1]
                        - prefixSum[bottom + 1][left]
                        - prefixSum[top][right + 1]
                        + prefixSum[top][left];
                ans[i][j] = sum / areas;
            }
        }
        return ans;
    }


    /**
     * 1818. 绝对差值和
     */
    public int minAbsoluteSumDiff001(int[] nums1, int[] nums2) {
        int mod = (int) Math.pow(10, 9) + 7;
        long sum = 0;
        long maxDiff = 0;
        int[] sorted = new int[nums1.length];
        System.arraycopy(nums1, 0, sorted, 0, nums1.length);
        Arrays.sort(sorted);
        for (int i = 0; i < nums1.length; i++) {
            int currentDiff = Math.abs(nums1[i] - nums2[i]);
            sum += currentDiff;
            //------------------------------------------
            // 尝试寻找最先近的位置，并计算最近距离
            //------------------------------------------
            //1、右侧
            int index = searchTarget(sorted, nums2[i]);  //在 sorted 中比 nums2[i]大的最小数
            long minDiff = Math.abs(sorted[index] - nums2[i]);
            //2、左侧
            if (index > 0)
                minDiff = Math.min(minDiff, Math.abs(sorted[index - 1] - nums2[i]));

            //更新全局最大的"差距"
            maxDiff = Math.max(maxDiff, currentDiff - minDiff);
        }
        return (int) ((sum - maxDiff) % mod);
    }

    private int searchTarget(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target) {
                left = mid + 1;
            } else if (target <= nums[mid]) {
                right = mid - 1;
            }
        }
        //--------------------------------------------
        // 返回 left :
        //     1、target 不存在：
        //           1、界外
        //              1、target 大于数组中所有元素       ##需要判断
        //                 left == nums.length 越界
        //              2、target 小于数组中所有元素
        //                 left == 0           不越界
        //           2、界内
        //               left 位于 target的右侧           ##需要判断
        //     2、target 存在
        //           1、唯一
        //                 left 为 target 的索引
        //           2、多个
        //                 left 为最左侧 target 的索引
        //--------------------------------------------
        if (left == nums.length)
            return nums.length - 1;
        else if (nums[left] == target)
            return left;
        else if (nums[left] > target)
            return left;
        return -2;   //不会有这种情况
    }


    /**
     * 1818. 绝对差值和
     */
    public int minAbsoluteSumDiff002(int[] nums1, int[] nums2) {
        long sum = 0;
        long maxDiff = 0;
        int mod = (int) Math.pow(10, 9) + 7;
        int[] sorted = Arrays.copyOf(nums1, nums1.length);
        Arrays.sort(sorted);
        for (int i = 0; i < nums1.length; i++) {
            long currentDiff = Math.abs(nums1[i] - nums2[i]);
            sum += currentDiff;
            //-----------------------------------------------------
            // 针对当前组合，尝试在 nums1 中寻找距离 nums2[i] 最近的值
            //-----------------------------------------------------
            //1、左侧
            int index = searchTargetRight(sorted, nums2[i]);
            long minDiff = Math.abs(sorted[index] - nums2[i]);
            //2、右侧
            if (index + 1 != nums1.length)
                minDiff = Math.min(minDiff, Math.abs(sorted[index + 1] - nums2[i]));

            //更新全局
            maxDiff = Math.max(maxDiff, currentDiff - minDiff);
        }
        return (int) ((sum - maxDiff) % mod);
    }

    private int searchTargetRight(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] <= target) {
                left = mid + 1;
            } else if (target < nums[mid]) {
                right = mid - 1;
            }
        }
        //----------------------------------------
        //  返回 right ：
        //     1、target 不存在
        //          1、界外
        //               1、target 小于数组内的元素
        //                  right == -1  越界
        //               2、target 大于数组内的元素
        //                  right == nums.length - 1
        //          2、界内
        //               right 位于 target 左侧
        //     2、target 存在
        //          1、唯一
        //               right 为 target 的索引
        //          2、重复
        //               right 为 最右侧 target 的索引
        //----------------------------------------
        if (right == -1)
            return 0;
        else if (nums[right] == target)
            return right;
        else if (nums[right] < target)
            return right;
        return -2;
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
     * 1838. 最高频元素的频数
     */
    public int maxFrequency001(int[] nums, int k) {
        Arrays.sort(nums);
        int maxFreq = 0;
        int left = 0;
        int right = 0;
        int consume = 0;
        while (right < nums.length) {
            if (right > 0) {
                consume += (right - left) * (nums[right] - nums[right - 1]);  //将 right 前位置的高度增加至 nums[right]，肯定不包含 right 位置
            }
            while (consume > k) {
                consume -= nums[right] - nums[left];
                left++;
            }
            maxFreq = Math.max(maxFreq, right - left + 1);
            right++;
        }
        return maxFreq;
    }


    /**
     * 1221. 分割平衡字符串
     */
    public int balancedStringSplit04(String str) {
        int ans = 0;
        for (int i = 0; i < str.length(); i++) {
            int count = 0;
            for (int j = i; j < str.length(); j++) {
                count = str.charAt(j) == 'L' ? count + 1 : count - 1;
                ans = count == 0 ? ans + 1 : ans;
            }
        }
        return ans;
    }


    /**
     * 733. 图像渲染
     */
    public int[][] floodFill(int[][] image, int sr, int sc, int color) {
        int[][] ans = new int[image.length][image[0].length];
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};  //上、下、左、右
        floodFillDfs(image, directions, image[sr][sc], sr, sc);
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                ans[i][j] = image[i][j] == -1 ? color : image[i][j];
            }
        }
        return ans;
    }

    private void floodFillDfs(int[][] image, int[][] directions, int target, int xx, int yy) {  //感染病的解题模式
        //终止条件一：越界
        if (xx < 0 || xx >= image.length || yy < 0 || yy >= image[0].length) {
            return;
        }
        //终止条件二：已搜索
        if (image[xx][yy] == -1) {
            return;
        }
        //终止条件三：不满足搜索条件
        if (image[xx][yy] != target) {
            return;
        }

        image[xx][yy] = -1; //标识为已经搜索

        //从当前位置向四个方向开始搜索
        for (int[] dir : directions) {  //1、横向枚举搜索
            int nextRow = xx + dir[0];
            int nextCol = yy + dir[1];

            floodFillDfs(image, directions, target, nextRow, nextCol);
        }
    }


    /**
     * 744. 寻找比目标字母大的最小字母
     */
    public char nextGreatestLetter(char[] letters, char target) {
        if (target < letters[0]) return letters[0];
        for (int i = 0; i < letters.length; i++) {
            if (letters[i] > target)
                return letters[i];
        }
        return letters[0];
    }

    public char nextGreatestLetter00(char[] letters, char target) {  //有序，所以二分写法
        if (letters[letters.length - 1] <= target) return letters[0];
        int left = 0;
        int right = letters.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (letters[mid] <= target) {
                left = mid + 1;
            } else if (target < letters[mid]) {
                right = mid - 1;
            }
        }
        if (right == -1)
            return letters[0];
        else if (letters[right] < target || letters[right] == target) {
            return letters[right + 1];
        }
        return '0';  //不会走这里
    }


    /**
     * 1218. 最长定差子序列
     */
    public int longestSubsequence(int[] arr, int difference) {
        int maxWindow = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            hTable.put(arr[i], hTable.getOrDefault(arr[i] - difference, 0) + 1);
            maxWindow = Math.max(maxWindow, hTable.get(arr[i]));
        }
        return maxWindow;
    }

    /**
     * 926. 将字符串翻转到单调递增
     */
    public int minFlipsMonoIncr(String str) {   //状态转移，同时此题细节巨多
        int n = str.length();
        //----------------------------------------------------
        // dp[i][0] 表示前 i 个元素，最后一个元素为 0 的最小翻转次数
        // dp[i][1] 表示前 i 个元素，最后一个元素为 1 的最小翻转次数
        //----------------------------------------------------
        int[][] dp = new int[n][2];
        //初始化
        dp[0][0] = str.charAt(0) == '0' ? 0 : 1;
        dp[0][1] = str.charAt(0) == '1' ? 0 : 1;
        for (int i = 1; i < n; i++) {
            //当前位置为 0，则前一位必须为 0
            dp[i][0] = dp[i - 1][0] + (str.charAt(i) == '0' ? 0 : 1);
            //当前位置为 1 ，前一位可以为 0 也可以为 1，因此，贪心则取其最小值
            dp[i][1] = Math.min(dp[i - 1][0], dp[i - 1][1]) + (str.charAt(i) == '1' ? 0 : 1);
        }
        return Math.min(dp[n - 1][0], dp[n - 1][1]);
    }

    //#遍历字符串，找到一个分界点，使得该分界点之前1的个数和分界点之后0的个数之和最小，把分界点之前的1变成0，之后的0变成1
    public int minFlipsMonoIncr01(String str) {   //基于前缀和的思想
        int n = str.length();
        //------------------------------------------------------------------------------
        // left_1 代表当前位置及左侧位置中 1 的个数，right_0 代表当前位置及右侧位置中 0 的个数
        //------------------------------------------------------------------------------
        int left_1 = 0;
        int right_0 = (int) Arrays.stream(str.split("")).mapToInt(Integer::valueOf).filter(o -> o == 0).count();
        //默认值
        int ans = Math.min(right_0, n - right_0);  //将 0 全部翻转为 1 或者 将 1 全部翻转为 0两种情况中，最小的翻转次数
        for (int i = 0; i < n; i++) {
            if (str.charAt(i) == '0')
                right_0--;
            if (str.charAt(i) == '1')
                left_1++;

            //每个位置均计算，将此位置左侧的 1 全部翻转为 0 && 将右侧 0 全部翻转为 1 的次数，并各个位置比较其最小值
            ans = Math.min(ans, left_1 + right_0);
        }
        return ans;
    }


    /**
     * 781. 森林中的兔子
     */
    public int numRabbits02(int[] answers) {
        int result = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>(); //数字：报此数字的兔子
        for (int answer : answers) {
            hTable.put(answer, hTable.getOrDefault(answer, 0) + 1);
        }
        Set<Map.Entry<Integer, Integer>> entries = hTable.entrySet();
        for (Map.Entry<Integer, Integer> map : hTable.entrySet()) {
            Integer nums = map.getKey();
            Integer times = map.getValue();
            if (nums >= times) {
                result += nums + 1;
            } else {
                int colors = times / (nums + 1);
                colors = times % (nums + 1) == 0 ? colors : colors + 1;  //多少个颜色或者组
                result += colors * (nums + 1);
            }
        }
        return result;
    }

    public int numRabbits03(int[] answers) {   //桶
        int res = 0;
        int[] buckets = new int[1000];
        for (int answer : answers) {
            if (buckets[answer] == 0) {  //此数的桶内已被消耗完，需要为此数字新开辟一个大小相同的桶
                res += answer + 1;
                buckets[answer] = answer;
            } else {
                buckets[answer]--;       //消耗桶内元素
            }
        }
        return res;
    }


    /**
     * 45. 跳跃游戏 II
     */
    public int jump(int[] nums) {  //贪心，找到能跳到目标位置的最小坐标
        int ans = 0;
        int targetIndex = nums.length - 1;
        while (targetIndex > 0) {
            for (int i = 0; i < nums.length; i++) {
                if (i + nums[i] >= targetIndex) {
                    ans++;
                    targetIndex = i;
                    break;
                }
            }
        }
        return ans;
    }


    public int jump01(int[] nums) {  //贪心，在遍历过程中动态的调整最大目标
        int ans = 0;
        int targetIndex = 0;
        int maxIndex = 0;
        //寻找起跳点，而 0 一定是起跳点，nums.length - 1 一定不是起跳点
        for (int i = 0; i < nums.length - 1; i++) {
            maxIndex = Math.max(maxIndex, i + nums[i]);
            if (i == targetIndex) {
                targetIndex = maxIndex;
                ans++;
            }
        }
        return ans;
    }


    /**
     * 1518. 换酒问题
     */
    public int numWaterBottles(int numBottles, int numExchange) {
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
     * 424. 替换后的最长重复字符
     */
    public int characterReplacement01(String str, int k) {
        int left = 0;
        int right = 0;
        int maxFreq = 0;
        int maxWindow = 0;
        int[] charAndNums = new int[26];
        while (right < str.length()) {
            charAndNums[str.charAt(right) - 'A']++;  //更新此字母的频次
            maxFreq = Math.max(maxFreq, charAndNums[str.charAt(right) - 'A']);  //基于字母的频次，尝试更新最大频次
            while (right - left + 1 > maxFreq + k) {
                charAndNums[str.charAt(left) - 'A']--;
                maxFreq = Math.max(maxFreq, charAndNums[str.charAt(left) - 'A']);
                left++;
            }
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }


    /**
     * 594. 最长和谐子序列
     */
    public int findLHS02(int[] nums) {
        Arrays.sort(nums);
        int left = 0;
        int right = 0;
        int maxWindow = 0;
        while (right < nums.length) {
            while (nums[right] > nums[left] + 1) {
                left++;
            }
            if (nums[right] == nums[left] + 1) {
                maxWindow = Math.max(maxWindow, right - left + 1);
            }
            right++;
        }
        return maxWindow;
    }

    public int findLHS04(int[] nums) {
        int maxWindow = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int num : nums) {
            hTable.put(num, hTable.getOrDefault(num, 0) + 1);
        }
        for (int num : nums) {
            if (hTable.containsKey(num + 1)) {
                maxWindow = Math.max(maxWindow, hTable.get(num) + hTable.get(num + 1));
            }
        }
        return maxWindow;
    }


    /**
     * 1592. 重新排列单词间的空格
     */
    public String reorderSpaces(String text) {
        StringBuilder ans = new StringBuilder();
        String[] words = text.trim().split("\\s+");
        int spaces = (int) Arrays.stream(text.split("")).filter(o -> o.equals(" ")).count();
        if (words.length == 1) {
            ans.append(words[0]);
            int xx = text.length() - words[0].length();
            while (xx != 0) {
                ans.append(" ");
                xx--;
            }
            return ans.toString();
        }
        int averNums = spaces / (words.length - 1);
        int remainder = spaces % (words.length - 1);
        StringBuilder gap = new StringBuilder();
        while (averNums > 0) {
            gap.append(" ");
            averNums--;
        }
        for (int i = 0; i < words.length - 1; i++) {
            ans.append(words[i]);
            ans.append(gap);
        }
        ans.append(words[words.length - 1]);
        while (remainder != 0) {
            ans.append(" ");
            remainder--;
        }
        return ans.toString();
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

    public int[] twoSum01(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{-1, -1};
    }


    /**
     * 383. 赎金信
     */
    public boolean canConstruct(String ransomNote, String magazine) {
        int[] merge = new int[26];
        for (int i = 0; i < Math.max(ransomNote.length(), magazine.length()); i++) {
            if (i < ransomNote.length())
                merge[ransomNote.charAt(i) - 'a']--;
            if (i < magazine.length())
                merge[magazine.charAt(i) - 'a']++;
        }
        for (int i = 0; i < 26; i++) {
            if (merge[i] < 0 && ransomNote.contains(String.valueOf((char) (i + 'a'))))
                return false;
        }
        return true;
    }


    public boolean canConstruct01(String ransomNote, String magazine) {
        int[] ransomNums = new int[26];
        int[] magazineNums = new int[26];
        for (int i = 0; i < ransomNote.length(); i++) {
            ransomNums[ransomNote.charAt(i) - 'a']++;
        }
        for (int i = 0; i < magazine.length(); i++) {
            magazineNums[magazine.charAt(i) - 'a']++;
        }
        for (int i = 0; i < 26; i++) {
            if (ransomNums[i] > magazineNums[i])
                return false;
        }
        return true;
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
     * 477. 汉明距离总和
     */
    public int totalHammingDistance(int[] nums) {
        int total = 0;
        for (int i = 0; i < 32; i++) {
            int xx = 0;
            int yy = 0;
            for (int num : nums) {
                if (((num >> i) & 1) == 1)
                    xx++;
                else
                    yy++;
            }
            total += xx * yy;
        }
        return total;
    }


    public int totalHammingDistance00(int[] nums) {
        int total = 0;
        for (int i = 0; i < 32; i++) {
            int xx = 0;
            for (int num : nums) {
                xx += ((num >> i) & 1);
            }
            total += xx * (nums.length - xx);
        }
        return total;
    }

    /**
     * 13. 罗马数字转整数
     */
    public int romanToInt00(String str) {
        int sum = 0;
        String convert = str.replaceAll("IV", "a")
                .replaceAll("IX", "b")
                .replaceAll("IX", "b")
                .replaceAll("XL", "c")
                .replaceAll("XC", "d")
                .replaceAll("CD", "e")
                .replaceAll("CM", "f");
        for (int i = 0; i < convert.length(); i++) {
            sum += romanToIntConvert(convert.charAt(i));
        }
        return sum;
    }

    private int romanToIntConvert(Character xx) {
        switch (xx) {
            case 'a':
                return 4;
            case 'b':
                return 9;
            case 'c':
                return 40;
            case 'd':
                return 90;
            case 'e':
                return 400;
            case 'f':
                return 900;
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                return 0;
        }
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


    public int findLengthOfLCIS000(int[] nums) {
        int ans = 0;
        int n = nums.length;
        int left = 0;
        for (int right = 0; right < nums.length; right++) {
            if (right > 0 && nums[right] <= nums[right - 1]) {
                left = right;
            }
            ans = Math.max(ans, right - left + 1);
        }
        return ans;
    }


    /**
     * 1619. 删除某些元素后的数组均值
     */
    public double trimMean(int[] arr) {
        Arrays.sort(arr);
        int sum = 0;
        int len = arr.length;
        for (int i = len / 20; i < len - len / 20; i++) {
            sum += arr[i];
        }
        return sum / (0.9 * len);
    }


    boolean subIsland = true;

    /**
     * 1905. 统计子岛屿
     */
    public int countSubIslands(int[][] grid1, int[][] grid2) {
        int ans = 0;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};  //上、下、左、右
        for (int i = 0; i < grid2.length; i++) {
            for (int j = 0; j < grid2[0].length; j++) {
                if (grid2[i][j] == 1) {
                    subIsland = true;
                    countSubIslandsDfs(grid1, grid2, directions, i, j);
                    ans = subIsland ? ans + 1 : ans;
                }
            }
        }
        return ans;
    }

    private void countSubIslandsDfs(int[][] grid1, int[][] grid2, int[][] directions, int row, int col) {
        //递归终止条件一：边界
        if (row < 0 || row >= grid2.length || col < 0 || col >= grid2[0].length) {
            return;
        }
        //递归终止条件而：水域
        if (grid2[row][col] == 0) {
            return;
        }
        //子岛屿判断
        if (grid1[row][col] == 0) {
            subIsland = false;  //grid2中此岛屿并非grid1中的子岛屿
        }

        grid2[row][col] = 0;   //搜索直至，感染 grid2中的此岛屿的全域

        for (int[] dir : directions) {
            int nextRow = row + dir[0];
            int nextCol = col + dir[1];
            countSubIslandsDfs(grid1, grid2, directions, nextRow, nextCol);
        }
    }


    /**
     * 473. 火柴拼正方形
     */
    public boolean makesquare(int[] matchsticks) {   //官方
        int sum = Arrays.stream(matchsticks).sum();
        if (sum % 4 != 0) return false;
        int target = sum / 4;
        int[] edges = new int[4];
        Arrays.sort(matchsticks);
        for (int i = 0, j = matchsticks.length - 1; i < j; i++, j--) {
            int temp = matchsticks[i];
            matchsticks[i] = matchsticks[j];
            matchsticks[j] = temp;
        }
        return makesquareDfs(matchsticks, edges, target, 0);
    }

    private boolean makesquareDfs(int[] matchsticks, int[] edges, int target, int currentIndex) {
        if (currentIndex == matchsticks.length) {
            return true;
        }

        for (int i = 0; i < edges.length; i++) {   //横向枚举遍历四条边，尝试将 matchsticks[currentIndex] 添加进去
            //剪枝，不满足条件
            if (edges[i] + matchsticks[currentIndex] > target) {
                continue;
            }
            //剪枝，重复情况
            if (i > 0 && edges[i] == edges[i - 1]) {
                continue;
            }
            //剪枝
            if (currentIndex == 0 && i > 0) {
                break;
            }

            //1、枚举添加元素
            edges[i] += matchsticks[currentIndex];

            //2、递归纵向搜索
            if (makesquareDfs(matchsticks, edges, target, currentIndex + 1)) {
                return true;
            }

            //3、回溯剔除元素
            edges[i] -= matchsticks[currentIndex];
        }
        return false;
    }


    /**
     * 698. 划分为k个相等的子集
     */
    public boolean canPartitionKSubsets(int[] nums, int k) {
        int sum = Arrays.stream(nums).sum();
        if (sum % k != 0) return false;
        int target = sum / k;
        int[] buckets = new int[k];
        Arrays.sort(nums);
        for (int i = 0, j = nums.length - 1; i < j; i++, j--) {
            int xx = nums[i];
            nums[i] = nums[j];
            nums[j] = xx;
        }
        return canPartitionKSubsetsDfs(nums, buckets, target, 0);
    }

    private boolean canPartitionKSubsetsDfs(int[] nums, int[] buckets, int target, int currentIndex) {
        if (currentIndex == nums.length) {
            return true;
        }

        for (int i = 0; i < buckets.length; i++) {
            if (currentIndex == 0 && i > 0) {
                break;
            }
            if (buckets[i] + nums[currentIndex] > target) {
                continue;
            }
            if (i > 0 && buckets[i] == buckets[i - 1]) {
                continue;
            }

            buckets[i] += nums[currentIndex];

            if (canPartitionKSubsetsDfs(nums, buckets, target, currentIndex + 1)) {
                return true;
            }

            buckets[i] -= nums[currentIndex];
        }
        return false;
    }

    int globalFairValue = Integer.MAX_VALUE;

    /**
     * 2305. 公平分发饼干
     */
    public int distributeCookies(int[] cookies, int k) {
        int[] buckets = new int[k];
        Arrays.sort(cookies);
        for (int i = 0, j = cookies.length - 1; i < j; i++, j--) {
            int xx = cookies[i];
            cookies[i] = cookies[j];
            cookies[j] = xx;
        }
        distributeCookiesDfs(cookies, buckets, 0);
        return globalFairValue;
    }

    private void distributeCookiesDfs(int[] cookies, int[] buckets, int currentIndex) {
        //递归终止
        if (currentIndex == cookies.length) {
            int currentFairValue = Arrays.stream(buckets).max().getAsInt();
            globalFairValue = Math.min(globalFairValue, currentFairValue);
            return;
        }

        //剪枝，未收到零食的孩子数  大于 未分配的零食袋数
        int emptyBuckets = (int) Arrays.stream(buckets).filter(o -> o == 0).count();
        if (emptyBuckets > cookies.length - currentIndex) {
            return;
        }

        for (int i = 0; i < buckets.length; i++) {
            //剪枝，第一个零食包，放在任意一个桶里，效果一致
            if (i > 0 && currentIndex == 0) {
                break;
            }
            //剪枝，剔除重复情况
            if (i > 0 && buckets[i] == buckets[i - 1]) {
                continue;
            }
            //剪枝，当前已经有孩子收到的零食超过 globalFairValue
            if (Arrays.stream(buckets).max().getAsInt() > globalFairValue) {
                continue;
            }

            buckets[i] += cookies[currentIndex];

            distributeCookiesDfs(cookies, buckets, currentIndex + 1);

            buckets[i] -= cookies[currentIndex];
        }
    }


    /**
     * 306. 累加数
     */
    public boolean isAdditiveNumber(String num) {   //基于深度递归算法
        //题目关键：整个累加数列只由第一和第二个数决定
        return isAdditiveNumberDfs(num, 0, 0, 0, 0);
    }

    private boolean isAdditiveNumberDfs(String num, int count, int startIndex, long prevPrev, long prev) {
        if (startIndex == num.length()) {
            return count >= 3;   //count
        }

        //每一层，都代表一个数，通过横向搜索，来扩展其范围，从而校验其是否满足条件
        long currentNums = 0;
        //------------------------------------------------------------------
        // 每层搜索都需要使用，此处同时控制第一个数的范围，如果当前第一个数不满足条件，则横向扩展
        //------------------------------------------------------------------
        for (int currentIndex = startIndex; currentIndex < num.length(); currentIndex++) {   //横向枚举搜索，在此其实并不是单个枚举，而是代表每个数的扫描区间，currentIndex 包含开始和结束的端点
            //剪枝
            if (currentIndex > startIndex && num.charAt(startIndex) == '0') {
                break;
            }

            currentNums = currentNums * 10 + (num.charAt(currentIndex) - '0');

            //1、如果当前不够三个数
            if (count < 2) {  //则继续向下深度搜索，从而开辟一个新数

                //-----------------------------------------------------------
                // 针对当前第二个数是否能够满足条件，如果不满足，则横向扩展
                //-----------------------------------------------------------

                if (isAdditiveNumberDfs(num, count + 1, currentIndex + 1, prev, currentNums)) {
                    return true;
                }
            }

            //2、如果当前已够三个数
            if (count >= 2) {  //此处仅控制第三个数的范围

                //-------------------------------------------------------------
                // 构成了三个数，基于当前第一、第二个数，下面基于当前的第三个数，对其进行扩展，尝试满足条件
                //-------------------------------------------------------------

                //剪枝优化：第三个数已经无法满足条件
                if (currentNums > prevPrev + prev) {  //即基于当前第一、第二个数，无法找到满足条件的第三个数
                    break;   //回溯，尝试扩展第二个数
                }
                //横向扩展：第三个数尚不满足条件，扩展当前第三个数，尝试满足情况
                if (currentNums < prevPrev + prev) {
                    continue;
                }
                //深度递归：当前第三个数满足情况
                if (currentNums == prevPrev + prev) {
                    //交替：第二个数赋值给第一个数，第三个数赋值给第二个数，尝试寻找新的满足条件的第三个数
                    if (isAdditiveNumberDfs(num, count + 1, currentIndex + 1, prev, currentNums)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isAdditiveNumber01(String num) {   //基于枚举
        int n = num.length();
        for (int secondBegin = 1; secondBegin < n - 1; secondBegin++) {
            //剪枝，第一个数不满足情况
            if (num.charAt(0) == '0' && secondBegin > 1) {
                break;
            }
            for (int secondEnd = secondBegin; secondEnd < n - 1; secondEnd++) {
                //剪枝，第二个数不满足情况
                if (num.charAt(secondBegin) == '0' && secondEnd > secondBegin) {
                    break;
                }
                //逐一校验
                if (checkCombinationValid(num, secondBegin, secondEnd)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkCombinationValid(String num, int secondBegin, int secondEnd) {
        int firstBegin = 0;
        int firstEnd = secondBegin - 1;
        while (secondEnd < num.length()) {
            String currentThird = stringAdd(num, firstBegin, firstEnd, secondBegin, secondEnd);
            int thirdBegin = secondEnd + 1;
            int thirdEnd = secondEnd + currentThird.length();
            if (thirdEnd >= num.length() || !num.substring(thirdBegin, thirdEnd + 1).equals(currentThird)) {
                return false;
            }
            if (thirdEnd == num.length() - 1) {
                return true;
            }

            //--------------------------------
            // 交换两个累加数
            //--------------------------------

            firstBegin = secondBegin;
            firstEnd = secondEnd;
            secondBegin = thirdBegin;
            secondEnd = thirdEnd;

        }
        return false;
    }

    private String stringAdd(String num, int firstBegin, int firstEnd, int secondBegin, int secondEnd) {
        StringBuilder currentThird = new StringBuilder();
        int carry = 0;
        int current = 0;
        while (firstBegin <= firstEnd || secondBegin <= secondEnd || carry != 0) {
            current = carry;  //每轮循环，当前位的初始值为上一位进位的结果
            if (firstBegin <= firstEnd) {
                current += (num.charAt(firstEnd) - '0');
                firstEnd--;
            }

            if (secondBegin <= secondEnd) {
                current += (num.charAt(secondEnd) - '0');
                secondEnd--;
            }

            carry = current / 10;  //进位
            current %= 10;         //当前位

            currentThird.append((char) (current + '0'));
        }
        return currentThird.reverse().toString();
    }


    /**
     * 680. 验证回文串 II
     */
    public boolean validPalindrome01(String str) {
        int left = 0;
        int right = str.length() - 1;
        int[][] directions = {{1, 0}, {0, -1}};
        return validPalindromeDfs01(str, directions, left, right, 1);
    }

    private boolean validPalindromeDfs01(String str, int[][] directions, int left, int right, int t) {
        while (left < right && str.charAt(left) == str.charAt(right)) {
            left++;
            right--;
        }

        if (left == right || right + 1 == left) {
            return true;
        }

        for (int[] dir : directions) {
            int nextLeft = left + dir[0];
            int nextRight = right + dir[1];
            if (t == 1) {
                if (validPalindromeDfs01(str, directions, nextLeft, nextRight, t - 1)) {
                    return true;
                }
            }
        }
        return false;
    }


    public List<List<String>> partition(String s) {
        int len = s.length();
        List<List<String>> res = new ArrayList<>();
        if (len == 0) {
            return res;
        }

        char[] charArray = s.toCharArray();
        // 预处理
        // 状态：dp[i][j] 表示 s[i][j] 是否是回文
        boolean[][] dp = new boolean[len][len];
        // 状态转移方程：在 s[i] == s[j] 的时候，dp[i][j] 参考 dp[i + 1][j - 1]
        for (int right = 0; right < len; right++) {
            // 注意：left <= right 取等号表示 1 个字符的时候也需要判断
            for (int left = 0; left <= right; left++) {
                if (charArray[left] == charArray[right] && (right - left <= 2 || dp[left + 1][right - 1])) {
                    dp[left][right] = true;
                }
            }
        }

        Deque<String> stack = new ArrayDeque<>();
        dfs(s, 0, len, dp, stack, res);
        return res;
    }

    private void dfs(String s, int index, int len, boolean[][] dp, Deque<String> path, List<List<String>> res) {
        if (index == len) {
            res.add(new ArrayList<>(path));
            return;
        }

        for (int i = index; i < len; i++) {
            if (dp[index][i]) {
                path.addLast(s.substring(index, i + 1));
                dfs(s, i + 1, len, dp, path, res);
                path.removeLast();
            }
        }
    }


    public List<List<String>> partition00(String str) {
        List<List<String>> ans = new ArrayList<>();
        if (str.length() == 0) {
            return ans;
        }
        LinkedList<String> path = new LinkedList<>();
        partitionDfs00(str, ans, path, 0);
        return ans;
    }

    private void partitionDfs00(String str, List<List<String>> ans, LinkedList<String> path, int currentIndex) {
        if (currentIndex == str.length()) {
            ans.add(new ArrayList<>(path));
            return;
        }

        StringBuilder seq = new StringBuilder();
        for (int i = currentIndex; i < str.length(); i++) {
            seq.append(str.charAt(i));
            if (checkPalindrome(seq)) {
                path.add(new String(seq));
                partitionDfs00(str, ans, path, i + 1);
                path.removeLast();
            }
        }
    }

    private boolean checkPalindrome(StringBuilder word) {
        int left = 0;
        int right = word.length() - 1;
        while (left < right) {
            if (word.charAt(left) != word.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;

    }


    /**
     * 1640. 能否连接形成数组
     */
    public boolean canFormArray(int[] arr, int[][] pieces) {
        int len = 0;
        HashMap<Integer, int[]> hTable = new HashMap<>();
        for (int[] piece : pieces) {
            if (piece.length > 0)
                hTable.put(piece[0], piece);
            len += piece.length;
        }
        if (len != arr.length) return false;
        int currentIndex = 0;
        while (currentIndex < arr.length) {
            if (!hTable.containsKey(arr[currentIndex])) {
                return false;
            }
            int[] nums = hTable.get(arr[currentIndex]);
            for (int num : nums) {
                if (num != arr[currentIndex])
                    return false;
                currentIndex++;
            }
        }
        return true;
    }


    /**
     * 113. 课程顺序
     */
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        //满足顺序要求的课程表
        ArrayList<Integer> scheduler = new ArrayList<>();
        //入度
        int[] inDegree = new int[numCourses];  //各个节点依赖的课程数目
        //邻接表
        HashMap<Integer, ArrayList<Integer>> adjacency = new HashMap<>();
        //初始化
        for (int[] relation : prerequisites) {
            int father = relation[1];
            int child = relation[0];
            inDegree[child]++;  //子节点的入度受影响
            if (!adjacency.containsKey(father)) {
                adjacency.put(father, new ArrayList<>());
            }
            adjacency.get(father).add(child);  //将子节点添加依赖关系
        }
        //初始化学习的课程
        Deque<Integer> availableCourses = new ArrayDeque<>();
        for (int id = 0; id < inDegree.length; id++) {
            if (inDegree[id] == 0) {
                availableCourses.addLast(id);
            }
        }
        while (!availableCourses.isEmpty()) {
            //学习此课程
            Integer course = availableCourses.pollLast();
            scheduler.add(course);
            //----------------------------------------
            // 此课程的影响性
            //----------------------------------------

            //1、不作为其他课程的依赖项
            if (!adjacency.containsKey(course)) {
                continue;
            }

            //2、作为其他课程的依赖项，对其他课程的安排产生影响
            ArrayList<Integer> influences = adjacency.get(course);
            for (int child : influences) {
                inDegree[child]--;
                if (inDegree[child] == 0) {   //再无依赖项
                    availableCourses.addLast(child);
                }
            }
        }
        if (scheduler.size() == numCourses) {
            return scheduler.stream().mapToInt(Integer::intValue).toArray();
        }
        return new int[]{};
    }


    /**
     * 520. 检测大写字母
     */
    public boolean detectCapitalUse(String word) {
        int upper = 0;
        int lower = 0;
        for (int i = 0; i < word.length(); i++) {
            if (i > 0) {
                if (Character.isLowerCase(word.charAt(i)))
                    lower++;
                else
                    upper++;
            }
            if (Character.isLowerCase(word.charAt(0))) {  //小写字母
                if (upper == 1) return false;                     //则全部应该是小写
            } else {
                if (upper > 0 && lower > 0) return false;         //既有大写也有小写
            }
        }
        return true;
    }


    /**
     * 804. 唯一摩尔斯密码词
     */
    public int uniqueMorseRepresentations(String[] words) {
        List<String> morse = Arrays.asList(".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..");
        HashMap<Character, String> hTable = new HashMap<>();
        for (int i = 0; i < morse.size(); i++) {
            hTable.put((char) (i + 'a'), morse.get(i));
        }
        HashSet<String> distinct = new HashSet<>();
        for (String word : words) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < word.length(); i++) {
                builder.append(hTable.get(word.charAt(i)));
            }
            distinct.add(builder.toString());
        }
        return distinct.size();
    }

    /**
     * 630. 课程表 III
     */
    public int scheduleCourse10(int[][] courses) {
        int currentDay = 0;
        Arrays.sort(courses, (o1, o2) -> o1[1] - o2[1]);  //按照最晚结束时间，升序排序
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o2[0] - o1[0]);  //队列内部按照课程时长降序排序
        for (int[] course : courses) {
            if (currentDay + course[0] <= course[1]) {  //当前课程直接可选，不会与队列中课程进行贪心竞争
                currentDay += course[0];  //时间进度
                sortedQueue.add(course);
            } else if (!sortedQueue.isEmpty() && course[0] < sortedQueue.peek()[0]) {   //贪心竞争
                int[] poll = sortedQueue.poll();//剔除队首课程
                currentDay -= poll[0];  //时间回退
                currentDay += course[0];
                sortedQueue.add(course);
            }
        }
        return sortedQueue.size();
    }


    /**
     * 1846. 减小和重新排列数组后的最大元素
     */
    public int maximumElementAfterDecrementingAndRearranging00(int[] arr) {
        Arrays.sort(arr);
        arr[0] = 1;
        for (int i = 1; i < arr.length; i++) {
            arr[i] = Math.min(arr[i], arr[i - 1] + 1);
        }
        return arr[arr.length - 1];
    }


    /**
     * 1833. 雪糕的最大数量
     */
    public int maxIceCream010(int[] costs, int coins) {
        int ans = 0;
        Arrays.sort(costs);
        for (int cost : costs) {
            coins -= cost;
            if (coins < 0) return ans;
            ans++;
        }
        return ans;
    }


    /**
     * 942. 增减字符串匹配
     */
    public int[] diStringMatch00(String str) {
        int min = 0;
        int max = str.length();
        int[] ans = new int[str.length() + 1];
        for (int i = 0; i < str.length(); i++) {
            char xx = str.charAt(i);
            if (xx == 'I') {
                ans[i] = min;
                min++;
            }

            if (xx == 'D') {
                ans[i] = max;
                max--;
            }
        }
        ans[str.length()] = max;
        return ans;
    }

    public int[] diStringMatch02(String str) {
        int[] ans = new int[str.length() + 1];
        int numD = 0;
        TreeSet<Integer> sortedTree = new TreeSet<>();
        for (int i = 0; i < ans.length; i++) {
            sortedTree.add(i);
            if (i < str.length() && str.charAt(i) == 'D') {
                numD++;
            }
        }
        ans[0] = numD;
        sortedTree.remove(numD);
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == 'D') {
                ans[i + 1] = sortedTree.floor(ans[i]);
            } else {
                ans[i + 1] = sortedTree.ceiling(ans[i]);
            }
            sortedTree.remove(ans[i + 1]);
        }
        return ans;
    }


    //静态代码块
    static {
        ArrayList<Integer> FIBS = new ArrayList<>();
        int dp1 = 1;
        int dp2 = 1;
        FIBS.add(dp1);
        FIBS.add(dp2);
        while (true) {
            int current = dp1 + dp2;
            if (current > Math.pow(10, 9)) {
                break;
            }
            FIBS.add(current);
            dp1 = dp2;
            dp2 = current;
        }
        int[] nums = FIBS.stream().mapToInt(Integer::intValue).toArray();
    }

    private int[] nums = {1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597,
            2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418, 317811, 514229,
            832040, 1346269, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817, 39088169, 63245986,
            102334155, 165580141, 267914296, 433494437, 701408733};


    /**
     * 1414. 和为 K 的最少斐波那契数字数目
     */
    public int findMinFibonacciNumbers01(int k) {  //二分
        int ans = 0;
        while (k > 0) {
            k -= binSearchFib(nums, k);
            ans++;
        }
        return ans;
    }

    private int binSearchFib(int[] nums, int target) {  //如果有 target则返回，否则返回小于target的最大值
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] <= target) {
                left = mid + 1;
            } else if (target < nums[mid]) {
                right = mid - 1;
            }
        }
        return nums[right];
    }


    /**
     * 1337. 矩阵中战斗力最弱的 K 行
     */
    public int[] kWeakestRows(int[][] mat, int k) {
        int[] ans = new int[k];
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];   //1.按照军人数量，升序排序
            else return o1[1] - o2[1];                  //2.按照索引，升序排序

        });
        for (int i = 0; i < mat.length; i++) {
            int soldier = Arrays.stream(mat[i]).sum();
            sortedQueue.add(new int[]{soldier, i});
        }
        int currentIndex = 0;
        while (currentIndex < k) {
            ans[currentIndex] = Objects.requireNonNull(sortedQueue.poll())[1];
            currentIndex++;
        }
        return ans;
    }


    /**
     * 2170. 使数组变成交替数组的最少操作数
     */
    public int minimumOperations001(int[] nums) {   //时间复杂度高，但能通过
        int[][] countA = new int[100001][2];
        int[][] countB = new int[100001][2];
        for (int i = 0; i < 100001; i++) {
            countA[i][0] = i;
            countB[i][0] = i;
        }
        for (int i = 0; i < nums.length; i++) {
            if ((i & 1) == 1)  //1.奇数位
                countA[nums[i]][1]++;
            else
                countB[nums[i]][1]++;
        }
        //降序排序
        Arrays.sort(countA, (o1, o2) -> o2[1] - o1[1]);   //关键在于此，排序时间复杂度高 O(n*log(n))
        Arrays.sort(countB, (o1, o2) -> o2[1] - o1[1]);
        //奇数位的前两位
        int ATop1 = countA[0][0];
        int ATop1Freq = countA[0][1];
        int ATop2 = countA[1][0];
        int ATop2Freq = countA[1][1];
        //偶数位的前两位
        int BTop1 = countB[0][0];
        int BTop1Freq = countB[0][1];
        int BTop2 = countB[1][0];
        int BTop2Freq = countB[1][1];
        //奇数位和偶数位中不同两数的最高频次和
        int maxFreqSum = ATop1 != BTop1 ?
                ATop1Freq + BTop1Freq : Math.max(ATop1Freq + BTop2Freq, ATop2Freq + BTop1Freq);
        //最终结果
        return nums.length - maxFreqSum;
    }


    public int minimumOperations002(int[] nums) {   //时间复杂度低，桶排序
        int[] countA = new int[100001];
        int[] countB = new int[100001];
        //入桶
        for (int i = 0; i < nums.length; i++) {
            if ((i & 1) == 1)  //1.奇数位
                countA[nums[i]]++;
            else
                countB[nums[i]]++;
        }
        //奇数位的前两位的索引，其实就是 num
        int ATop1 = 0;
        int ATop2 = 0;
        //偶数位的前两位
        int BTop1 = 0;
        int BTop2 = 0;
        for (int i = 0; i < 100001; i++) {
            if (countA[i] > countA[ATop1]) {
                ATop2 = ATop1;
                ATop1 = i;
            } else if (countA[i] > countA[ATop2]) {
                ATop2 = i;
            }

            if (countB[i] > countB[BTop1]) {
                BTop2 = BTop1;
                BTop1 = i;
            } else if (countB[i] > countB[BTop2]) {
                BTop2 = i;
            }
        }
        //奇数位和偶数位中不同两数的最高频次和
        int maxFreqSum = ATop1 != BTop1 ?
                countA[ATop1] + countB[BTop1] : Math.max(countA[ATop1] + countB[BTop2], countA[ATop2] + countB[BTop1]);
        //最终结果
        return nums.length - maxFreqSum;
    }


    /**
     * 397. 整数替换
     */
    public int integerReplacement(int n) {
        //递归终止条件
        if (n == 1) return 0;

        //递归分支一：偶数
        if ((n & 1) == 0) return 1 + integerReplacement(n / 2);

        //递归分支二：奇数
        return 2 + Math.min(integerReplacement(n / 2), integerReplacement(n / 2 + 1));
    }

    public int integerReplacement01(int n) {
        int ans = 0;
        while (n != 1) {
            //特殊的奇数
            if (n == 3) return ans + 2;

            //奇偶数判断
            if ((n & 1) == 0) {       //1.偶数 X0的情况
                n >>>= 1;
            } else {                  //2.奇数 X1的情况
                if (((n >> 1) & 1) == 1)     //2.1 11的情况
                    n++;
                else                         //2.2 01的情况
                    n--;
            }
            ans++;
        }
        return ans;
    }


    /**
     * 1736. 替换隐藏数字得到的最晚时间
     */
    public String maximumTime(String time) {
        char[] arrayTimes = time.toCharArray();
        if (arrayTimes[4] == '?') arrayTimes[4] = '9';
        if (arrayTimes[3] == '?') arrayTimes[3] = '5';
        if (arrayTimes[1] == '?') {
            if (arrayTimes[0] == '0' || arrayTimes[0] == '1') arrayTimes[1] = '9';
            if (arrayTimes[0] == '2' || arrayTimes[0] == '?') arrayTimes[1] = '3';
        }
        if (arrayTimes[0] == '?') {
            if (arrayTimes[1] >= '4') arrayTimes[0] = '1';
            else arrayTimes[0] = '2';
        }
        return new String(arrayTimes);
    }


    /**
     * 704. 二分查找
     */
    public int search704(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else if (target < nums[mid]) {
                right = mid - 1;
            }
        }
        return -1;
    }

    public int search70400(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] <= target) {
                left = mid + 1;
            } else if (target < nums[mid]) {
                right = mid - 1;
            }
        }
        if (right == -1) return -1;
        else if (nums[right] < target) return -1;
        return right;
    }

    public int search70401(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target) {
                left = mid + 1;
            } else if (target <= nums[mid]) {
                right = mid - 1;
            }
        }
        if (left == nums.length) return -1;
        else if (nums[left] > target) return -1;
        return left;
    }

    /**
     * 26. 删除有序数组中的重复项
     */
    public int removeDuplicates(int[] nums) {
        int currentIndex = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1])
                continue;
            nums[currentIndex] = nums[i];
            currentIndex++;
        }
        return currentIndex;
    }

    /**
     * 27. 移除元素
     */
    public int removeElement(int[] nums, int val) {
        int currentIndex = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == val) continue;
            nums[currentIndex] = nums[i];
            currentIndex++;
        }
        return currentIndex;
    }


    /**
     * 485. 最大连续 1 的个数
     */
    public int findMaxConsecutiveOnes(int[] nums) {
        int maxWindow = 0;
        int left = 0;
        int right = 0;
        while (right < nums.length) {
            if (nums[right] == 0) {
                left = right + 1;
            }
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }


    //静态代码块
    static HashSet<Character> vowels = new HashSet<>();

    static {
        char[] arrayVowels = {'a', 'e', 'i', 'o', 'u'};
        for (char arrayVowel : arrayVowels) {
            vowels.add(arrayVowel);
            vowels.add(Character.toUpperCase(arrayVowel));
        }
    }

    /**
     * 345. 反转字符串中的元音字母
     */
    public String reverseVowels(String str) {
        int left = 0;
        int right = str.length() - 1;
        char[] array = str.toCharArray();
        while (left < right) {
            while (left < right && !vowels.contains(str.charAt(left))) {
                left++;
            }
            while (left < right && !vowels.contains(str.charAt(right))) {
                right--;
            }
            //交换
            if (left < right && vowels.contains(str.charAt(left)) && vowels.contains(str.charAt(right))) {
                array[left] = str.charAt(right);
                array[right] = str.charAt(left);
            }
            left++;
            right--;
        }
        return new String(array);
    }


    /**
     * 面试题 01.08. 零矩阵
     */
    public void setZeroes(int[][] matrix) {
        int[] rowsZero = new int[matrix.length];
        int[] colsZero = new int[matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rowsZero[i] = -1;
                    colsZero[j] = -1;
                }
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (rowsZero[i] == -1 || colsZero[j] == -1) {
                    matrix[i][j] = 0;
                }
            }
        }
    }


    /**
     * 475. 供暖器
     */
    public int findRadius(int[] houses, int[] heaters) {  //
        int radiusTarget = 0;
        TreeSet<Integer> sorted = new TreeSet<>();
        for (int heater : heaters) {
            sorted.add(heater);
        }
        for (int house : houses) {
            Integer ceiling = sorted.ceiling(house) == null ? Integer.MAX_VALUE : sorted.ceiling(house);
            Integer floor = sorted.floor(house) == null ? Integer.MAX_VALUE : sorted.floor(house);
            int currentTarget = Math.min(Math.abs(house - ceiling), Math.abs(house - floor));
            radiusTarget = Math.max(radiusTarget, currentTarget);
        }
        return radiusTarget;
    }

    public int findRadius01(int[] houses, int[] heaters) {  //基于二分搜索
        int radiusTarget = 0;
        Arrays.sort(heaters);   //一定要排序，排序前，索引和值，对应的暖器编号与暖器的位置，排序后，只关心哪些位置上是否有暖器
        for (int house : houses) {
            int index = searchHeater(heaters, house);  //可能会越界
            int rightIndex = Math.min(index, heaters.length - 1);
            int leftIndex = Math.max(index - 1, 0);
            int minRadius = Math.min(Math.abs(heaters[rightIndex] - house), Math.abs(heaters[leftIndex] - house));
            radiusTarget = Math.max(radiusTarget, minRadius);
        }
        return radiusTarget;
    }

    private int searchHeater(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;   //值比 target 大的最小索引位置
    }


    /**
     * 581. 最短无序连续子数组
     */
    public int findUnsortedSubarray(int[] nums) {
        int[] sorted = Arrays.copyOfRange(nums, 0, nums.length);
        Arrays.sort(sorted);
        if (Arrays.equals(nums, sorted)) return 0;
        int left = 0;
        int right = nums.length - 1;
        while (left < right && nums[left] == sorted[left]) {
            left++;
        }
        while (left < right && nums[right] == sorted[right]) {
            right--;
        }
        return right - left + 1;
    }


    /**
     * 1221. 分割平衡字符串
     */
    public int balancedStringSplit00(String str) {
        int balanceNums = 0;
        int numL = 0;
        int numR = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == 'R')
                numR++;
            else
                numL++;
            if (numL == numR) {
                balanceNums++;
                numL = 0;
                numR = 0;
            }
        }
        return balanceNums;
    }


    /**
     * 3. 无重复字符的最长子串
     */
    public int lengthOfLongestSubstring00(String str) {
        int left = 0;
        int right = 0;
        int maxWindow = 0;
        HashMap<Character, Integer> hTable = new HashMap<>();
        while (right < str.length()) {
            if (hTable.containsKey(str.charAt(right))) {
                left = Math.max(left, hTable.get(str.charAt(right)) + 1);   //跳过一位，保证无重复
            }
            maxWindow = Math.max(maxWindow, right - left + 1);
            hTable.put(str.charAt(right), right);
            right++;
        }
        return maxWindow;
    }


    /**
     * 134. 加油站
     */
    public int canCompleteCircuit(int[] gas, int[] cost) {   //超时
        for (int i = 0; i < gas.length; i++) {
            int currentGas = 0;
            int currentIndex = i;
            while (true) {  //正好耗完
                currentGas += gas[currentIndex];  //加油
                currentGas -= cost[currentIndex]; //消耗汽油
                if (currentGas < 0) break;
                //向前移动一步
                currentIndex++;
                //拐弯
                if (currentIndex == gas.length)
                    currentIndex = 0;
                if (currentIndex == i)  //回到原点，则直接返回
                    return i;
            }
        }
        return -1;
    }


    public int canCompleteCircuit01(int[] gas, int[] cost) {
        int len = gas.length;
        int spare = 0;
        int minSpare = Integer.MAX_VALUE;
        int minIndex = 0;

        for (int i = 0; i < len; i++) {
            spare += gas[i] - cost[i];
            if (spare < minSpare) {
                minSpare = spare;
                minIndex = i;
            }
        }
        return spare < 0 ? -1 : (minIndex + 1) % len;
    }

    /**
     * 643. 子数组最大平均数 I
     */
    public double findMaxAverage01(int[] nums, int k) {
        double maxAverage = Integer.MIN_VALUE;
        double currentSum = 0.0;
        int left = 0;
        int right = 0;
        while (right < nums.length) {
            currentSum += nums[right];
            if (right - left + 1 > k) {
                currentSum -= nums[left];
                left++;
            }
            if (right - left + 1 == k) {
                maxAverage = Math.max(maxAverage, currentSum * 1.0 / k);
            }
            right++;
        }
        return maxAverage;
    }


    /**
     * 187. 重复的DNA序列
     */
    public List<String> findRepeatedDnaSequences000(String str) {
        List<String> ans = new ArrayList<>();
        HashMap<String, Integer> hTable = new HashMap<>();
        int left = 0;
        int right = 10;
        while (right <= str.length()) {
            String sequence = str.substring(left, right);
            hTable.put(sequence, hTable.getOrDefault(sequence, 0) + 1);
            if (hTable.get(sequence) == 2) {
                ans.add(sequence);
            }
            left++;
            right++;
        }
        return ans;
    }

    public List<String> findRepeatedDnaSequences001(String str) {
        List<String> ans = new ArrayList<>();
        if (str.length() < 10) return ans;
        HashMap<Character, Integer> charToNum = new HashMap<>();
        charToNum.put('A', 0);
        charToNum.put('C', 1);
        charToNum.put('G', 2);
        charToNum.put('T', 3);
        int left = 0;
        int right = 0;
        int windowSum = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        while (right < str.length()) {
            windowSum = ((windowSum << 2) | charToNum.get(str.charAt(right)));
            if (right - left + 1 > 10) {
                windowSum &= ((1 << (10 * 2)) - 1);   //内层中 不是 (10 * 2 + 1)
                left++;
            }
            if (right - left + 1 == 10) {
                hTable.put(windowSum, hTable.getOrDefault(windowSum, 0) + 1);
                if (hTable.get(windowSum) == 2) {
                    ans.add(str.substring(left, right + 1));
                }
            }
            right++;
        }
        return ans;
    }


    /**
     * 424. 替换后的最长重复字符
     */
    public int characterReplacement0(String str, int k) {
        int left = 0;
        int right = 0;
        int maxFreq = 0;
        int maxWindow = 0;
        int[] freq = new int[26];
        while (right < str.length()) {
            maxFreq = Math.max(maxFreq, ++freq[str.charAt(right) - 'A']);
            while (right - left + 1 > maxFreq + k) {
                maxFreq = Math.max(maxFreq, --freq[str.charAt(left) - 'A']);
                left++;
            }
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }


    /**
     * 594. 最长和谐子序列
     */
    public int findLHS000(int[] nums) {
        int maxWindow = 0;
        HashMap<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        for (int num : freq.keySet()) {
            if (freq.containsKey(num + 1)) {
                maxWindow = Math.max(maxWindow, freq.get(num) + freq.get(num + 1));
            }
        }
        return maxWindow;
    }

    public int findLHS0001(int[] nums) {
        int left = 0;
        int right = 0;
        int maxWindow = 0;
        Arrays.sort(nums);
        while (right < nums.length) {
            while (left < right && nums[right] - nums[left] > 1) {
                left++;
            }
            if (nums[right] - nums[left] == 1) {
                maxWindow = Math.max(maxWindow, right - left + 1);
            }
            right++;
        }
        return maxWindow;
    }


    /**
     * 438. 找到字符串中所有字母异位词
     */
    public List<Integer> findAnagrams0(String s, String p) {
        List<Integer> ans = new ArrayList<>();
        if (s.length() < p.length()) return ans;
        int[] target = new int[26];
        int[] window = new int[26];
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
                if (Arrays.equals(target, window)) {
                    ans.add(left);
                }
            }
            right++;
        }
        return ans;
    }


    /**
     * 811. 子域名访问计数
     */
    public List<String> subdomainVisits(String[] cpdomains) {
        ArrayList<String> ans = new ArrayList<>();
        HashMap<String, Integer> hTable = new HashMap<>();
        for (String sentence : cpdomains) {
            String[] split = sentence.split("\\s+");
            int nums = Integer.parseInt(split[0]);
            hTable.put(split[1], hTable.getOrDefault(split[1], 0) + nums);
            String[] domains = split[1].split("\\.");
            int len = domains.length;
            hTable.put(domains[len - 1], hTable.getOrDefault(domains[len - 1], 0) + nums);
            if (len == 3) {
                String domainTwo = domains[1] + "." + domains[2];
                hTable.put(domainTwo, hTable.getOrDefault(domainTwo, 0) + nums);
            }
        }
        for (String domain : hTable.keySet()) {
            Integer nums = hTable.get(domain);
            ans.add(nums + " " + domain);
        }
        return ans;
    }


    /**
     * 474. 一和零
     */
    public int findMaxForm(String[] strs, int m, int n) {
        int[][][] dp = new int[strs.length][m + 1][n + 1];
        //初始化，记录各个字符串中 0 和 1 的个数
        int[][] digit = new int[strs.length][2];
        for (int i = 0; i < strs.length; i++) {
            String str = strs[i];
            int xx = 0;
            int yy = 0;
            for (int j = 0; j < str.length(); j++) {
                if (str.charAt(j) == '0') xx++;
                else yy++;
            }
            digit[i] = new int[]{xx, yy};
        }

        //1、只考虑第一件物品
        for (int i = 0; i <= m; i++) {  //共两层循环
            for (int j = 0; j <= n; j++) {
                dp[0][i][j] = (i >= digit[0][0] && j >= digit[0][1]) ? 1 : 0;
            }
        }

        //2、考虑其他物品
        for (int t = 1; t < strs.length; t++) {
            for (int i = 0; i <= m; i++) {
                for (int j = 0; j <= n; j++) {
                    //1.不选择物品 t
                    int aa = dp[t - 1][i][j];
                    //2.选择物品 t
                    int bb = (i >= digit[t][0] && j >= digit[t][1]) ? dp[t - 1][i - digit[t][0]][j - digit[t][1]] + 1 : 0;

                    //合并两种情况
                    dp[t][i][j] = Math.max(aa, bb);
                }
            }
        }
        return dp[strs.length - 1][m][n];
    }


    public int findMaxForm01(String[] strs, int m, int n) {  //降维
        int[][] dp = new int[m + 1][n + 1];
        for (String str : strs) {
            int a = 0;
            int b = 0;
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == '0') a++;
                else b++;
            }

            for (int i = m; i >= a; i--) {
                for (int j = n; j >= b; j--) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - a][j - b] + 1);
                }
            }
        }
        return dp[m][n];
    }


    /**
     * 1441. 用栈操作构建数组
     */
    public List<String> buildArray(int[] target, int n) {
        String push = "Push";
        String pop = "Pop";
        List<String> ans = new ArrayList<>();
        int num = 1;
        for (int i = 0; i < target.length; i++) {
            while (num != target[i]) {
                ans.add(push);
                ans.add(pop);
                num++;
            }
            if (num == target[i]) {
                ans.add(push);
                num++;
            }
        }
        return ans;
    }


    /**
     * 1277. 统计全为 1 的正方形子矩阵
     */
    public int countSquares(int[][] matrix) {
        int ans = 0;
        int m = matrix.length;
        int n = matrix[0].length;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || j == 0) {
                    dp[i][j] = matrix[i][j];
                } else if (matrix[i][j] == 0) {
                    dp[i][j] = 0;
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
                ans += dp[i][j];
            }
        }
        return ans;
    }


    /**
     * 1768. 交替合并字符串
     */
    public String mergeAlternately(String word1, String word2) {
        StringBuilder ans = new StringBuilder();
        int index1 = 0;
        int index2 = 0;
        while (index1 < word1.length() || index2 < word2.length()) {
            if (index1 < word1.length()) {
                ans.append(word1.charAt(index1));
                index1++;
            }
            if (index2 < word2.length()) {
                ans.append(word2.charAt(index2));
                index2++;
            }
        }
        return ans.toString();
    }


    int mod = (int) Math.pow(10, 9) + 7;

    /**
     * 6203. 矩阵中和能被 K 整除的路径
     */
    public int numberOfPaths(int[][] grid, int k) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[][] directions = {{1, 0}, {0, 1}};
        int[][][] cached = new int[rows][cols][k];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Arrays.fill(cached[i][j], -1);
            }
        }
        return numberOfPathsDfs(grid, directions, cached, k, 0, 0, 0);
    }

    private int numberOfPathsDfs(int[][] grid, int[][] directions, int[][][] cached, int k, int currentRow, int currentCol, int currentRemainder) {
        //1、递归终止条件一：越界，不满足情况
        if (currentRow >= grid.length || currentCol >= grid[0].length) {
            return 0;
        }
        //2、递归终止条件二：记忆化搜索，已搜索
        if (cached[currentRow][currentCol][currentRemainder] != -1) {
            return cached[currentRow][currentCol][currentRemainder];
        }
        //3、递归终止条件三：找到其中一条路径
        int nextRemainder = (currentRemainder + grid[currentRow][currentCol]) % k;
        if (nextRemainder == 0 && currentRow == grid.length - 1 && currentCol == grid[0].length - 1) {
            return 1;
        }

        int paths = 0;
        for (int[] dir : directions) { //横向枚举遍历
            int nextRow = currentRow + dir[0];
            int nextCol = currentCol + dir[1];
            paths += numberOfPathsDfs(grid, directions, cached, k, nextRow, nextCol, nextRemainder);
            paths %= mod;
        }
        cached[currentRow][currentCol][currentRemainder] = paths;

        return paths;
    }

    /**
     * 416. 分割等和子集
     */
    public boolean canPartition(int[] nums) {   //动态规划，最值，二维
        int sum = 0;
        int max = 0;
        for (int num : nums) {
            sum += num;
            max = Math.max(max, num);
        }
        int target = sum / 2;
        if (sum % 2 != 0) return false;
        int[][] dp = new int[nums.length + 1][target + 1];  // 物品为 i，背包容量为 j，价值为背包可装下的最大重量，最大就是背包容量
        for (int i = 1; i <= nums.length; i++) {
            for (int j = 0; j <= target; j++) {
                if (j < nums[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - nums[i - 1]] + nums[i - 1]);
                }
            }
        }
        return dp[nums.length][target] == target;
    }

    public boolean canPartition01(int[] nums) {   //动态规划，最值，一维
        int sum = 0;
        int max = 0;
        for (int num : nums) {
            sum += num;
            max = Math.max(max, num);
        }
        int target = sum / 2;
        if (sum % 2 != 0) return false;
        int[] dp = new int[target + 1];  // 物品为 i，背包容量为 j，价值为背包可装下的最大重量，最大就是背包容量
        for (int i = 1; i <= nums.length; i++) {
            for (int j = target; j >= nums[i - 1]; j--) {
                dp[j] = Math.max(dp[j], dp[j - nums[i - 1]] + nums[i - 1]);
            }
        }
        return dp[target] == target;
    }


    public boolean canPartition10(int[] nums) {   //动态规划，存在，二维
        int sum = 0;
        int max = 0;
        for (int num : nums) {
            sum += num;
            max = Math.max(max, num);
        }
        int target = sum / 2;
        if (sum % 2 != 0) return false;
        boolean[][] dp = new boolean[nums.length + 1][target + 1];  //前 i 个物品，能否恰好装满容量为 j 的背包
        for (int i = 0; i <= nums.length; i++) {
            dp[i][0] = true;
        }
        for (int i = 1; i <= nums.length; i++) {
            for (int j = 1; j <= target; j++) {
                if (j < nums[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i - 1]];
                }
            }
        }
        return dp[nums.length][target];
    }


    public boolean canPartition11(int[] nums) {   //动态规划，存在，一维
        int sum = 0;
        int max = 0;
        for (int num : nums) {
            sum += num;
            max = Math.max(max, num);
        }
        int target = sum / 2;
        if (sum % 2 != 0) return false;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        for (int i = 1; i <= nums.length; i++) {
            for (int j = target; j >= nums[i - 1]; j--) {
                dp[j] = dp[j] || dp[j - nums[i - 1]];
            }
        }
        return dp[target];
    }


    /**
     * 474. 一和零
     */
    public int findMaxForm10(String[] strs, int m, int n) {//动态规划，最值，二维
        int len = strs.length;
        int[][][] dp = new int[len + 1][m + 1][n + 1];  //dp[tt][i][j]前 tt 个物品，不超过 m 个 0，不超过 n 个 1的情况下，对应的最大价值（使用最多的物品）
        for (int tt = 1; tt <= len; tt++) {
            int aa = getZeroNums(strs[tt - 1]);
            int bb = strs[tt - 1].length() - aa;
            for (int i = 0; i <= m; i++) {
                for (int j = 0; j <= n; j++) {
                    if (i < aa || j < bb) {
                        dp[tt][i][j] = dp[tt - 1][i][j];
                    } else {
                        dp[tt][i][j] = Math.max(dp[tt - 1][i][j], dp[tt - 1][i - aa][j - bb] + 1);
                    }
                }
            }
        }
        return dp[len][m][n];
    }


    public int findMaxForm11(String[] strs, int m, int n) {//动态规划，最值，一维
        int len = strs.length;
        int[][] dp = new int[m + 1][n + 1];  //dp[tt][i][j]前 tt 个物品，不超过 m 个 0，不超过 n 个 1的情况下，对应的最大价值（使用最多的物品）
        for (int tt = 0; tt < len; tt++) {
            int aa = getZeroNums(strs[tt]);
            int bb = strs[tt].length() - aa;
            for (int i = m; i >= aa; i--) {
                for (int j = n; j >= bb; j--) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - aa][j - bb] + 1);
                }
            }
        }
        return dp[m][n];
    }

    private int getZeroNums(String str) {
        int zeroNums = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '0') zeroNums++;
        }
        return zeroNums;
    }


    /**
     * 879. 盈利计划
     */
    public int profitableSchemes(int n, int minProfit, int[] group, int[] profit) {
        int m = group.length;
        //dp[m][n][K] 前 m 个工作，"恰好"使用 n 个人，获得至少为 K 的盈利计划数
        int[][][] dp = new int[m + 1][n + 1][minProfit + 1];
        dp[0][0][0] = 1;
        for (int i = 1; i <= m; i++) {
            for (int j = 0; j <= n; j++) {              //背包一
                for (int k = 0; k <= minProfit; k++) {  //背包二
                    if (j < group[i - 1]) {  //第一个背包（人数）无法放下
                        dp[i][j][k] = dp[i - 1][j][k];
                    } else {                 //第一个背包（人数）可以放下
                        dp[i][j][k] = dp[i - 1][j][k] + dp[i - 1][j - group[i - 1]][Math.max(0, k - profit[i - 1])];
                        dp[i][j][k] %= mod;
                    }
                }
            }
        }
        int sumPlans = 0;
        for (int j = 0; j <= n; j++) {
            sumPlans += dp[m][j][minProfit];
            sumPlans %= mod;
        }
        return sumPlans;
    }


    /**
     * 322. 零钱兑换
     */
    public int coinChange(int[] coins, int target) {   //恰好等于
        int[][] dp = new int[coins.length + 1][target + 1];
        for (int i = 0; i <= coins.length; i++) {
            Arrays.fill(dp[i], target + 1);
            dp[i][0] = 0;
        }
        for (int i = 1; i <= coins.length; i++) {
            for (int j = 1; j <= target; j++) {
                if (j < coins[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - coins[i - 1]] + 1);
                }
            }
        }
        return dp[coins.length][target] != target + 1 ? dp[coins.length][target] : -1;
    }


    public int coinChange01(int[] coins, int target) {   //恰好等于
        int[] dp = new int[target + 1];
        Arrays.fill(dp, target + 1);
        dp[0] = 0;
        for (int i = 0; i < coins.length; i++) {
            for (int j = coins[i]; j <= target; j++) {
                dp[j] = Math.min(dp[j], dp[j - coins[i]] + 1);
            }
        }
        return dp[target] == target + 1 ? -1 : dp[target];
    }


    /**
     * 518. 零钱兑换 II
     */
    public int change(int amount, int[] coins) {  //可以凑成总金额的硬币组合数
        int[][] dp = new int[coins.length + 1][amount + 1];
        for (int i = 0; i <= coins.length; i++) {
            dp[i][0] = 1;
        }
        for (int i = 1; i <= coins.length; i++) {
            for (int j = 1; j <= amount; j++) {
                if (j < coins[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - coins[i - 1]];
                }
            }
        }
        return dp[coins.length][amount];
    }


    public int change01(int amount, int[] coins) {  //可以凑成总金额的硬币组合数
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        for (int i = 0; i < coins.length; i++) {
            for (int j = coins[i]; j <= amount; j++) {
                dp[j] = dp[j] + dp[j - coins[i]];
            }
        }
        return dp[amount];
    }


    /**
     * 1449. 数位成本和为目标值的最大数字
     */
    public String largestNumber(int[] cost, int target) {  //二维
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        String[][] dp = new String[nums.length + 1][target + 1];
        for (int i = 0; i <= nums.length; i++) {
            Arrays.fill(dp[i], "#");
            dp[i][0] = "";
        }
        for (int i = 1; i <= nums.length; i++) {
            for (int j = 1; j <= target; j++) {
                //只有这一种写法正确，其余错误
                dp[i][j] = dp[i - 1][j];
                if (j >= cost[i - 1] && !dp[i][j - cost[i - 1]].equals("#")) {
                    dp[i][j] = strMax(dp[i - 1][j], nums[i - 1] + dp[i][j - cost[i - 1]]);
                }

//                if (j < cost[i - 1]) {
//                    dp[i][j] = dp[i - 1][j];
//                } else if (!dp[i - 1][j].equals("#") && !dp[i][j - cost[i - 1]].equals("#")) {
//                    dp[i][j] = strMax(dp[i - 1][j], nums[i - 1] + dp[i][j - cost[i - 1]]);
//                }
//                dp[i][j] = dp[i - 1][j];
//                if (j >= cost[i - 1] && !dp[i - 1][j].equals("#") && !dp[i][j - cost[i - 1]].equals("#")) {
//                    dp[i][j] = strMax(dp[i - 1][j], nums[i - 1] + dp[i][j - cost[i - 1]]);
//                }

            }
        }
        return dp[nums.length][target].equals("#") ? "0" : dp[nums.length][target];
    }

    private String strMax(String str1, String str2) {
        if (str1.length() > str2.length()) return str1;
        if (str1.length() < str2.length()) return str2;
        return str1.compareTo(str2) > 0 ? str1 : str2;
    }


    /**
     * 1839. 所有元音按顺序排布的最长子字符串
     */
    public int longestBeautifulSubstring(String word) {
        int ans = 0;
        int nums = 1;
        int length = 1;
        int currentIndex = 1;
        while (currentIndex < word.length()) {
            //1、判断连续升序
            if (word.charAt(currentIndex) >= word.charAt(currentIndex - 1)) {
                length++;
                //记录升序区间内不同字符的个数
                if (word.charAt(currentIndex) > word.charAt(currentIndex - 1)) {
                    nums++;
                }
                if (nums == 5) {
                    ans = Math.max(ans, length);
                }
            } else {   //2、不满足升序，重新开始计数
                nums = 1;
                length = 1;
            }
            currentIndex++;
        }
        return ans;
    }


    /**
     * 1662. 检查两个字符串数组是否相等
     */
    public boolean arrayStringsAreEqual(String[] word1, String[] word2) {
        return String.join("", word1).equals(String.join("", word2));
    }


    /**
     * 10. 正则表达式匹配
     */
    public boolean isMatch(String str, String cur) {
        int m = str.length();
        int n = cur.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        for (int j = 1; j <= n; j++) {
            if (cur.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 2];
            }
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (cur.charAt(j - 1) == '.' || str.charAt(i - 1) == cur.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else if (cur.charAt(j - 1) == '*') {
                    if (str.charAt(i - 1) != cur.charAt(j - 2) && cur.charAt(j - 2) != '.') {
                        dp[i][j] = dp[i][j - 2];
                    } else {
                        dp[i][j] = dp[i][j - 2] || dp[i - 1][j];
                    }
                }
            }
        }
        return dp[m][n];
    }


    /**
     * 1678. 设计 Goal 解析器
     */
    public String interpret(String command) {
        while (command.contains("(")) {
            command = command.replaceAll("\\(\\)", "o");
            command = command.replaceAll("\\(al\\)", "al");
        }
        return command;
    }


    /**
     * 1094. 拼车
     */
    public boolean carPooling(int[][] trips, int capacity) {  //桶的概念
        int[] roads = new int[1001];  //记录最终状态
        for (int[] trip : trips) {
            for (int i = trip[1]; i < trip[2]; i++) {  //关键，不能取等号，因为最终会下车，否则就会有 [[2,1,5],[3,5,7]] 3 错误
                roads[i] += trip[0];
            }
        }
        for (int i = 0; i < 1001; i++) {   //关注各个位置是否存在容量超限的情况
            if (roads[i] > capacity) {
                return false;
            }
        }
        return true;
    }

    public boolean carPooling01(int[][] trips, int capacity) {  //差分数组
        int[] roads = new int[1001];  //记录最终状态
        for (int[] trip : trips) {
            roads[trip[1]] += trip[0];
            roads[trip[2]] -= trip[0];
        }
        int prefixSum = 0;
        for (int i = 0; i < 1001; i++) {   //关注各个位置是否存在容量超限的情况
            prefixSum += roads[i];
            if (prefixSum > capacity) {
                return false;
            }
        }
        return true;
    }





}
