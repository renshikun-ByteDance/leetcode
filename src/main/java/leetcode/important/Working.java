package leetcode.important;

import java.util.*;

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


}


