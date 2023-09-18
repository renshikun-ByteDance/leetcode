package leetcode.homework;

import com.sun.jmx.remote.internal.ArrayQueue;
import org.omg.PortableInterceptor.INACTIVE;
import utils.pojo.ListNode;

import java.util.*;

public class Working2 {

    /**
     * 388. 文件的最长绝对路径
     */
    public int lengthLongestPath(String input) {
        int[] lens = new int[input.length() + 1];
        int res = 0;
        for (String line : input.split("\n")) {
            int depth = getDepth(line);
            //后面依次覆盖，减去 depth 的目的是 \t转换为 /，相当于 line中的 \t转为换为 /，长度变化
            lens[depth + 1] = lens[depth] + line.length() - depth;   // 防止数组越界，所以 depth + 1 代表本层，depth 代表上一层
            if (line.contains(".")) {
                res = Math.max(res, lens[depth + 1] + depth);
            }
        }
        return res;
    }

    private int getDepth(String line) {
        int depth = 0;
        for (char ch : line.toCharArray()) {
            if (ch == '\t') depth++;   //'\t'为一个字符，制表符，而非两个字符
        }
        return depth;
    }


    /**
     * 856. 括号的分数
     */
    public int scoreOfParentheses(String str) {
        int n = str.length();
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (str.charAt(i) == '(') {  //遇到左括号，添加 0
                queue.addLast(0);
            } else {                     //遇到右括号，分情况讨论
                Integer prev = queue.pollLast();  //取出堆顶值
                if (prev == 0) {                       //1、如果为 0 ，则其余当前右括号配对，值为 1
                    queue.addLast(1);
                } else {                               //2、如果非 0 ，其内部成对，2倍关系，注意要将当前右括号在队列中对应的左括号 0 移除
                    int sum = prev;
                    while (!queue.isEmpty() && queue.peekLast() > 0) {
                        sum += queue.pollLast();
                    }
                    queue.pollLast();  //将于其匹配的左括号抵消掉
                    queue.addLast(2 * sum);
                }
            }
        }
        int ans = 0;
        while (!queue.isEmpty()) {
            ans += queue.poll();
        }
        return ans;
    }

    public int scoreOfParentheses00(String str) {
        int n = str.length();
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (str.charAt(i) == '(') {  //遇到左括号，添加 0
                queue.addLast(0);
            } else {                     //遇到右括号，分情况讨论
                Integer curr = queue.pollLast();  //取出堆顶值
                if (curr == 0) {                       //1、如果为 0 ，则其余当前右括号配对，值为 1
                    queue.addLast(1);
                } else {                               //2、如果非 0 ，其内部成对，2倍关系，注意要将当前右括号在队列中对应的左括号 0 移除
                    queue.pollLast();  //将于其匹配的左括号抵消掉
                    queue.addLast(2 * curr);
                }
            }
        }
        int ans = 0;
        while (!queue.isEmpty()) {
            ans += queue.poll();
        }
        return ans;
    }


    public int scoreOfParentheses02(String str) {
        return scoreOfParenthesesDfs(str, 0, str.length() - 1);
    }

    private int scoreOfParenthesesDfs(String str, int left, int right) {
        int ans = 0;
        int cur = 0;
        for (int i = left; i <= right; i++) {
            cur += (str.charAt(i) == '(' ? 1 : -1);
            if (cur == 0) {
                if (i == left + 1) {   //递归终止条件
                    ans++;
                } else {
                    ans += 2 * scoreOfParenthesesDfs(str, left + 1, i - 1);  //向内收缩一层，因为外面已经乘以了 2
                }
                left = i + 1;
            }
        }
        return ans;
    }


    /**
     * 406. 根据身高重建队列
     */
    public int[][] reconstructQueue(int[][] people) {  //贪心思想
        int n = people.length;
        Arrays.sort(people, (o1, o2) -> {
            if (o1[0] != o2[0]) return o2[0] - o1[0];  //身高不同，按照身高降序排序
            return o1[1] - o2[1];  //身高相同，按照前面人数升序排序
        });
        ArrayList<int[]> queue = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            queue.add(people[i][1], people[i]);
        }
        return queue.toArray(new int[queue.size()][]);
    }


    /**
     * 621. 任务调度器
     */
    public int leastInterval(char[] tasks, int n) {
        int currTimes = 1;
        int[] buckets = new int[26];
        for (char task : tasks) {
            buckets[task - 'A']++;
        }
        //按照每个任务的剩余个数，降序排序
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o2[1] - o1[1]);
        for (int i = 0; i < 26; i++) {
            if (buckets[i] > 0) sortedQueue.add(new int[]{i, buckets[i]});
        }
        ArrayDeque<int[]> deleteQueue = new ArrayDeque<>();
        int[] prevTimes = new int[26];
        while (!sortedQueue.isEmpty()) {
            //1、剔除处于冷却期的任务
            while (!sortedQueue.isEmpty() && prevTimes[sortedQueue.peek()[0]] > 0 && currTimes - prevTimes[sortedQueue.peek()[0]] <= n) {
                deleteQueue.addLast(sortedQueue.poll());
            }
            //2、为当前时间分配任务
            if (!sortedQueue.isEmpty()) {
                int[] currTask = sortedQueue.poll();
                prevTimes[currTask[0]] = currTimes;
                currTask[1]--;
                if (currTask[1] > 0) sortedQueue.add(currTask);
            }
            //3、将当前处于冷却期的任务重新添加到优先队列中
            sortedQueue.addAll(deleteQueue);
            deleteQueue.clear();

            currTimes++;
        }
        return --currTimes;
    }


    public int leastInterval01(char[] tasks, int n) {  //基于桶的概念
        int[] buckets = new int[26];
        int maxFreq = 0;
        int maxNums = 0;
        for (char task : tasks) {
            maxFreq = Math.max(maxFreq, ++buckets[task - 'A']);
        }
        for (int i = 0; i < 26; i++) {
            if (buckets[i] == maxFreq) maxNums++;
        }
        return Math.max(tasks.length, (maxFreq - 1) * (n + 1) + maxNums);
    }


    /**
     * 1705. 吃苹果的最大数目
     */
    public int eatenApples(int[] apples, int[] days) {
        int currDay = 0;
        int maxApples = 0;
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[0] - o2[0]);
        while (currDay < apples.length || !sortedQueue.isEmpty()) {
            //1、添加苹果
            if (currDay < apples.length) {
                sortedQueue.add(new int[]{currDay + days[currDay], apples[currDay]});
            }
            //2、移除过期的苹果
            while (!sortedQueue.isEmpty() && sortedQueue.peek()[0] <= currDay) {
                sortedQueue.poll();
            }
            if (!sortedQueue.isEmpty()) {
                int[] tuple = sortedQueue.poll();
                tuple[1]--;
                maxApples++;
                if (tuple[1] > 0) sortedQueue.add(tuple);
            }
            currDay++;
        }

        return maxApples;
    }

    /**
     * 1996. 游戏中弱角色的数量
     */
    public int numberOfWeakCharacters(int[][] properties) {
        int ans = 0;
        Arrays.sort(properties, (o1, o2) -> {
            if (o1[0] != o2[0]) return o2[0] - o1[0];   //优先按照攻击值降序排序
            return o1[1] - o2[1];                       //其次按照防御值升序排序
        });
        int currDefense = -1;
        for (int[] property : properties) {
            if (property[1] < currDefense) {
                ans++;
            } else {
                currDefense = property[1];
            }
        }
        return ans;
    }

    /**
     * 870. 优势洗牌
     */
    public int[] advantageCount(int[] nums1, int[] nums2) {  //田忌赛马
        int n = nums1.length;
        int[] ans = new int[n];
        Arrays.sort(nums1);
        int[][] sortNums2 = new int[n][2];
        for (int i = 0; i < n; i++) {
            sortNums2[i] = new int[]{nums2[i], i};
        }
        Arrays.sort(sortNums2, (o1, o2) -> o1[0] - o2[0]);  //按照值升序排序
        int left = 0;
        int right = n - 1;
        for (int num : nums1) {
            if (num > sortNums2[left][0]) {
                ans[sortNums2[left][1]] = num;
                left++;
            } else {
                ans[sortNums2[right][1]] = num;
                right--;
            }
        }
        return ans;
    }

    /**
     * 1353. 最多可以参加的会议数目
     */
    public int maxEvents(int[][] events) {  //与吃苹果的非常类似
        int ans = 0;
        int currDay = 0;
        int currIndex = 0;
        int n = events.length;
        //按照会议开始时间升序排序
        Arrays.sort(events, (o1, o2) -> o1[0] - o2[0]);
        //可选会议按照结束时间升序排序（贪心），谁先结束我尽早参加谁
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[1] - o2[1]);
        while (currIndex < n || !sortedQueue.isEmpty()) {
            while (currIndex < n && currDay == events[currIndex][0]) {
                sortedQueue.add(events[currIndex]);
                currIndex++;
            }
            while (!sortedQueue.isEmpty() && sortedQueue.peek()[1] < currDay) {
                sortedQueue.poll();
            }
            if (!sortedQueue.isEmpty()) {
                ans++;
                sortedQueue.poll();
            }
            currDay++;
        }
        return ans;
    }


    /**
     * 1109. 航班预订统计
     */
    public int[] corpFlightBookings(int[][] books, int n) {
        int[] ans = new int[n];
        for (int[] book : books) {
            for (int i = book[0] - 1; i < book[1]; i++) {
                ans[i] += book[2];
            }
        }
        return ans;
    }

    public int[] corpFlightBookings01(int[][] books, int n) {  //动态上下公交车
        int[] ans = new int[n];
        for (int[] book : books) {
            ans[book[0] - 1] += book[2];  //上车
            if (book[1] < n) {
                ans[book[1]] -= book[2];  //下车
            }
        }
        for (int i = 1; i < n; i++) {
            ans[i] += ans[i - 1];
        }
        return ans;
    }

    /**
     * 1488. 避免洪水泛滥
     */
    public int[] avoidFlood(int[] rains) {
        int n = rains.length;
        int[] ans = new int[n];
        Arrays.fill(ans, 1);
        //下过雨但未抽干的湖水编号
        HashSet<Integer> set = new HashSet<>();
        //记录当前湖水满载（下过雨但未抽干）同时后续仍会下雨的湖水及其下次下雨的日期
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[1] - o2[1]);   //按照下次下雨日期升序排序
        //记录各个湖水下雨的日期（有序）
        HashMap<Integer, ArrayDeque<Integer>> prevAndNextRainDay = new HashMap<>();
        for (int i = 0; i < n; i++) {  //初始化
            if (rains[i] > 0) {
                int lakeId = rains[i];
                ArrayDeque<Integer> queue = prevAndNextRainDay.getOrDefault(lakeId, new ArrayDeque<>());
                queue.addLast(i);
                prevAndNextRainDay.put(lakeId, queue);
            }
        }
        for (int i = 0; i < n; i++) {
            int lakeId = rains[i];
            //1、下雨天
            if (lakeId > 0) {   //模拟下雨，查看是否会出现洪水
                if (set.contains(lakeId)) {  //如果此湖水未抽干，则会发生洪水
                    return new int[]{};
                }
                set.add(lakeId);   //维护满载的湖水编号
                ArrayDeque<Integer> queue = prevAndNextRainDay.get(lakeId);   //维护每个湖水下次下雨的日期队列
                queue.pollFirst();
                if (!queue.isEmpty()) {
                    sortedQueue.add(new int[]{lakeId, queue.peekLast()});   //维护下次选取的优先级
                    prevAndNextRainDay.put(lakeId, queue);
                }
                ans[i] = -1;
            }
            //2、不下雨
            if (lakeId == 0) {
                if (!sortedQueue.isEmpty()) {
                    int[] curr = sortedQueue.poll();
                    ans[i] = curr[0];  //第 i 天抽干 curr[0] 池塘（贪心），注意其实 curr[1] 一定是大于 i 的
                    set.remove(curr[0]);   //当前湖水被抽干，从满载的湖水中移除
                }
            }
        }
        return ans;
    }

    //-----------------------------------------------------------------
    // 上下两种写法基本一致，差异在于：
    //     上面的写法使用了 HashSet 记录当前下过雨且未抽干的湖水编号、使用 PriorityQueue 存储当前下过雨且未抽干且后续仍会下雨的湖水编号和下次下雨日期
    //     下面的写法劲使用 PriorityQueue 存储当前下过雨且未抽干且后续仍会下雨的湖水编号和下次下雨日期
    //         下面超时的原因在于 rainedQueue.removeIf(o -> o[0] == lakeId
    //-----------------------------------------------------------------

    public int[] avoidFlood01(int[] rains) {  //超时
        int n = rains.length;
        int[] ans = new int[n];
        Arrays.fill(ans, 1);
        //记录当前湖水满载（下过雨但未抽干）同时后续仍会下雨的湖水及其下次下雨的日期
        PriorityQueue<int[]> rainedQueue = new PriorityQueue<>((o1, o2) -> o1[1] - o2[1]);   //按照下次下雨日期升序排序
        //记录各个湖水下雨的日期（有序）
        HashMap<Integer, ArrayDeque<Integer>> nextRainDay = new HashMap<>();
        for (int i = 0; i < n; i++) {  //初始化
            if (rains[i] > 0) {
                int lakeId = rains[i];
                ArrayDeque<Integer> queue = nextRainDay.getOrDefault(lakeId, new ArrayDeque<>());
                queue.addLast(i);
                nextRainDay.put(lakeId, queue);
            }
        }
        for (int i = 0; i < n; i++) {
            int lakeId = rains[i];
            //1、下雨天
            if (lakeId > 0) {   //模拟下雨，查看是否会出现洪水
                if (rainedQueue.removeIf(o -> o[0] == lakeId)) {  //超时原因，如果此湖水未抽干，则会发生洪水
                    return new int[]{};
                }
                //今天下过雨了，更新此湖水下次下雨日期的队列
                ArrayDeque<Integer> currLakeQueue = nextRainDay.get(lakeId);   //维护每个湖水下次下雨的日期队列
                currLakeQueue.pollFirst();
                if (!currLakeQueue.isEmpty()) {  //后面仍会下雨的湖水，重新添加回队列
                    rainedQueue.add(new int[]{lakeId, currLakeQueue.peekLast()});   //维护下次选取的优先级
                    nextRainDay.put(lakeId, currLakeQueue);
                }
                ans[i] = -1;
            }
            //2、不下雨
            if (lakeId == 0) {
                if (!rainedQueue.isEmpty()) {
                    int[] curr = rainedQueue.poll();
                    ans[i] = curr[0];  //第 i 天抽干 curr[0] 池塘（贪心），注意其实 curr[1] 一定是大于 i 的
                }
            }
        }
        return ans;
    }


    /**
     * 630. 课程表 III
     */
    public int scheduleCourse(int[][] courses) {
        int currDay = 0;
        //优先按照结束时间降序排序
        Arrays.sort(courses, (o1, o2) -> o1[1] - o2[1]);
        //当前已选课程
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o2[0] - o1[0]);  //按照持续时长降序排序
        for (int[] course : courses) {
            if (currDay + course[0] <= course[1]) {  //新增
                sortedQueue.add(course);
                currDay += course[0];
            } else if (!sortedQueue.isEmpty() && course[0] < sortedQueue.peek()[0]) {  //置换
                int[] remove = sortedQueue.poll();
                currDay -= remove[0];
                sortedQueue.add(course);
                currDay += course[0];
            }
        }
        return sortedQueue.size();
    }


    /**
     * 1223. 掷骰子模拟
     */
    public int dieSimulator(int n, int[] rollMax) {
        int res = 0;
        int mod = 1000000007;
        int[][][] dp = new int[n + 1][6][16];   //dp[i][j][k]含义为完成 i 次摇筛子，摇到 j 的次数为 k
        for (int i = 0; i < 6; i++) {
            dp[1][i][1] = 1;
        }
        for (int i = 2; i <= n; i++) {
            for (int j = 0; j < 6; j++) {
                for (int k = 1; k <= rollMax[j]; k++) {
                    for (int p = 0; p < 6; p++) {
                        if (p != j) {
                            dp[i][p][1] += dp[i - 1][j][k];
                            dp[i][p][1] %= mod;
                        } else if (k + 1 <= rollMax[p]) {
                            dp[i][p][k + 1] += dp[i - 1][j][k];
                            dp[i][p][k + 1] %= mod;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 6; i++) {
            for (int k = 1; k <= rollMax[i]; k++) {
                res += dp[n][i][k];
                res %= mod;
            }
        }
        return res;
    }


    /**
     * 45. 跳跃游戏 II
     */
    public int jump(int[] nums) {
        if (nums.length == 0) return 0;
        int ans = 1;  //起跳点的个数
        int maxEnd = 0;
        int nextEnd = nums[0];
        for (int i = 1; i < nums.length - 1; i++) {
            maxEnd = Math.max(maxEnd, i + nums[i]);
            if (i == nextEnd) {
                ans++;
                nextEnd = maxEnd;
            }
        }
        return ans;
    }

    public int jump01(int[] nums) {
        int ans = 0;
        int targetIndex = nums.length - 1;
        while (targetIndex > 0) {
            for (int i = 0; i < targetIndex; i++) {  //不能等于 targetIndex，因为不能从这里起跳
                if (i + nums[i] >= targetIndex) {
                    ans++;
                    targetIndex = i;
                    break;
                }
            }
        }
        return ans;
    }


    /**
     * 1124. 表现良好的最长时间段
     */
    public int longestWPI(int[] hours) {
        int ans = 0;
        int n = hours.length;
        for (int i = 0; i < n; i++) {
            hours[i] = hours[i] > 8 ? 1 : -1;
        }
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + hours[i];
        }
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i <= n; i++) {
            if (queue.isEmpty() || prefix[queue.peekLast()] > prefix[i]) {
                queue.addLast(i);
            }
        }
        for (int i = n; i >= 0; i--) {
            while (!queue.isEmpty() && queue.peekLast() >= i) {
                queue.pollLast();
            }
            while (!queue.isEmpty() && prefix[queue.peekLast()] < prefix[i]) {
                ans = Math.max(ans, i - queue.pollLast());
            }
        }
        return ans;
    }


    /**
     * 962. 最大宽度坡
     */
    public int maxWidthRamp(int[] nums) {
        int ans = 0;
        int n = nums.length;
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (queue.isEmpty() || nums[queue.peekLast()] > nums[i]) {
                queue.addLast(i);
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            while (!queue.isEmpty() && queue.peekLast() >= i) {
                queue.pollLast();
            }
            while (!queue.isEmpty() && nums[queue.peekLast()] <= nums[i]) {
                ans = Math.max(ans, i - queue.pollLast());
            }
        }
        return ans;
    }


    /**
     * 862. 和至少为 K 的最短子数组
     */
    public int shortestSubarray(int[] nums, int k) {
        int n = nums.length;
        int ans = n + 10;
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i <= n; i++) {
            while (!queue.isEmpty() && prefix[queue.peekLast()] >= prefix[i]) {
                queue.pollLast();
            }
            while (!queue.isEmpty() && prefix[i] - prefix[queue.peekFirst()] >= k) {
                ans = Math.min(ans, i - queue.pollFirst());
            }
            queue.addLast(i);
        }
        return ans == n + 10 ? -1 : ans;
    }


    public int[] avoidFlood001(int[] rains) {  //以雨天为基准
        int n = rains.length;
        int[] ans = new int[n];
        Arrays.fill(ans, 1);
        HashMap<Integer, Integer> prevRainDay = new HashMap<>();
        TreeSet<Integer> unUsedSunnyDay = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            int currRainID = rains[i];
            //1、晴天，不下雨
            if (currRainID == 0) {
                unUsedSunnyDay.add(i);
                continue;
            }
            //2、雨天，下雨
            ans[i] = -1;
            if (prevRainDay.containsKey(currRainID)) {
                //尝试寻找上次下雨后的第一个可用的晴天
                Integer rainDay = unUsedSunnyDay.ceiling(prevRainDay.get(currRainID));
                if (rainDay == null) {
                    return new int[]{};
                }
                ans[rainDay] = currRainID;
                unUsedSunnyDay.remove(rainDay);
            }
            prevRainDay.put(currRainID, i);
        }
        return ans;
    }


    /**
     * 775. 全局倒置与局部倒置
     */
    public boolean isIdealPermutation(int[] nums) {
        int n = nums.length;
        int[] nextMin = new int[n];
        nextMin[n - 1] = nums[n - 1];   //从当前位（含）后的最小值
        for (int i = n - 2; i >= 0; i--) {
            nextMin[i] = Math.min(nextMin[i + 1], nums[i]);
        }
        for (int i = 0; i < n - 2; i++) {
            if (nums[i] > nextMin[i + 2]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 775. 全局倒置与局部倒置
     */
    public boolean isIdealPermutation01(int[] nums) {  //贪心
        int n = nums.length;
        int min = nums[n - 1];
        for (int i = n - 3; i >= 0; i--) {
            if (nums[i] > min) return false;
            min = Math.min(min, nums[i + 1]);
        }
        return true;
    }


    /**
     * 334. 递增的三元子序列
     */
    public boolean increasingTriplet(int[] nums) {
        int num1 = nums[0];             //最左侧最小值
        int num2 = Integer.MAX_VALUE;   //中间中间值
        for (int i = 1; i < nums.length; i++) {
            if (num2 < nums[i]) {
                return true;
            } else if (num1 < nums[i] && nums[i] < num2) {
                num2 = nums[i];
            } else if (nums[i] < num1) {
                num1 = nums[i];
            }
        }
        return false;
    }


    public boolean increasingTriplet003(int[] nums) {  //思路不清晰
        int top2 = nums[0];
        int top3 = Integer.MAX_VALUE;
        for (int i = 1; i < nums.length; i++) {
            if (top3 != Integer.MAX_VALUE && top2 < nums[i]) {
                return true;
            } else if (top3 < nums[i] && nums[i] <= top2) {
                top2 = nums[i];
            } else if (nums[i] <= top3) {
                top3 = nums[i];
            }
        }
        return false;
    }

    /**
     * 300. 最长递增子序列
     */
    public int lengthOfLIS(int[] nums) {
        int ans = 1;
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }


    /**
     * 1221. 分割平衡字符串
     */
    public int balancedStringSplit(String str) {
        int balanceNums = 0;
        int numsL = 0;
        int numsR = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == 'L') numsL++;
            else numsR++;
            if (numsL == numsR) {
                balanceNums++;
                numsL = 0;
                numsR = 0;
            }
        }
        return balanceNums;
    }


    /**
     * 486. 预测赢家
     */
    public boolean PredictTheWinner(int[] nums) {
        int n = nums.length;
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }
        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                dp[i][j] = Math.max(nums[i] - dp[i + 1][j], nums[j] - dp[i][j - 1]);
            }
        }
        return dp[0][n - 1] >= 0;
    }


    public boolean PredictTheWinner01(int[] nums) {
        return predictTheWinnerDfs(nums, 0, nums.length - 1, 0, 0, 0);
    }

    private boolean predictTheWinnerDfs(int[] nums, int left, int right, int sum1, int sum2, int times) {
        //递归终止条件
        if (right < left) {
            return sum1 >= sum2;
        }

        //广度优先搜索，枚举遍历
        if ((times & 1) == 0) {  //偶数次，play1选择
            if (nums[left] > nums[right]) {
                if (predictTheWinnerDfs(nums, left + 1, right, sum1 + nums[left], sum2, times + 1)) {
                    return true;
                }
            } else if (nums[left] < nums[right]) {
                if (predictTheWinnerDfs(nums, left, right - 1, sum1 + nums[right], sum2, times + 1)) {
                    return true;
                }
            } else {
                if (predictTheWinnerDfs(nums, left + 1, right, sum1 + nums[left], sum2, times + 1) || predictTheWinnerDfs(nums, left, right - 1, sum1 + nums[right], sum2, times + 1)) {
                    return true;
                }
            }
        } else {                 //奇数次，play2选择
            if (nums[left] > nums[right]) {
                if (predictTheWinnerDfs(nums, left + 1, right, sum1, sum2 + nums[left], times + 1)) {
                    return true;
                }
            } else if (nums[left] < nums[right]) {
                if (predictTheWinnerDfs(nums, left, right - 1, sum1, sum2 + nums[right], times + 1)) {
                    return true;
                }
            } else {
                if (predictTheWinnerDfs(nums, left + 1, right, sum1, sum2 + nums[left], times + 1) || predictTheWinnerDfs(nums, left, right - 1, sum1, sum2 + nums[right], times + 1)) {
                    return true;
                }
            }
        }
        return false;
    }


//    private boolean predictTheWinnerDfs(int[] nums, int left, int right, int sum1, int sum2, int times) {
//        //递归终止条件
//        if (right < left) {
//            return sum1 >= sum2;
//        }
//
//        //广度优先搜索，枚举遍历
//        if ((times & 1) == 0) {  //偶数次，play1选择
//            if (predictTheWinnerDfs(nums, left + 1, right, sum1 + nums[left], sum2, times + 1) || predictTheWinnerDfs(nums, left, right - 1, sum1 + nums[right], sum2, times + 1)) {
//                return true;
//            }
//        } else {                 //奇数次，play2选择
//            if (predictTheWinnerDfs(nums, left + 1, right, sum1, sum2 + nums[left], times + 1) || predictTheWinnerDfs(nums, left, right - 1, sum1, sum2 + nums[right], times + 1)) {
//                return true;
//            }
//        }
//        return false;
//    }


    /**
     * 1218. 最长定差子序列
     */
    public int longestSubsequence(int[] arr, int difference) {
        int ans = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : arr) {
            map.put(num, map.getOrDefault(num - difference, 0) + 1);
            ans = Math.max(ans, map.get(num));
        }
        return ans;
    }


    /**
     * 1005. K 次取反后最大化的数组和
     */
    public int largestSumAfterKNegations(int[] nums, int k) {
        int ans = 0;
        Arrays.sort(nums);
        int prev = Integer.MAX_VALUE;
        for (int num : nums) {
            if (num < 0) {
                if (k > 0) {
                    ans += (-num);
                    k--;
                } else {
                    ans += num;
                }
            } else if (num > 0) {
                if (k > 0 && k % 2 != 0) {  //一定要选一个作为负数
                    if (prev < num) {
                        ans -= 2 * prev;
                        ans += num;
                    } else {
                        ans -= num;
                    }
                    k = 0;
                } else {
                    ans += num;
                }
                prev = Math.min(prev, num);
            } else {
                k = 0;
                prev = 0;
            }
            if (num < 0) prev = -num;
        }
        if (k > 0 && k % 2 != 0) {
            ans -= 2 * prev;
        }
        return ans;
    }


    /**
     * 2341. 数组能形成多少数对
     */
    public int[] numberOfPairs(int[] nums) {
        int[] ans = new int[2];
        int[] buckets = new int[101];
        for (int num : nums) {
            buckets[num]++;
        }
        for (int i = 0; i <= 100; i++) {
            if (buckets[i] == 0) continue;
            ans[0] += buckets[i] / 2;
            ans[1] += buckets[i] % 2;
        }
        return ans;
    }


    /**
     * 452. 用最少数量的箭引爆气球
     */
    public int findMinArrowShots(int[][] points) {
        //按照左侧边界升序排序
        Arrays.sort(points, (o1, o2) -> {
            if (o1[0] > o2[0]) return 1;
            else if (o1[0] < o2[0]) return -1;
            return 0;
        });
        int ans = 1;
        int[] edge = points[0];
        for (int i = 1; i < points.length; i++) {
            if (edge[1] < points[i][0]) {
                ans++;
                edge = points[i];
            } else {
                edge[0] = points[i][0];
                edge[1] = Math.min(edge[1], points[i][1]);
            }
        }
        return ans;
    }


    /**
     * 1326. 灌溉花园的最少水龙头数目
     */
    public int minTaps(int n, int[] ranges) {   //区间重叠问题，贪心
        ArrayList<int[]> sortedQueue = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            sortedQueue.add(new int[]{i - ranges[i], i + ranges[i]});
        }
        sortedQueue.sort((o1, o2) -> o1[0] - o2[0]);  //按照区间左边界升序排序
        int ans = 0;
        int currIndex = 0;
        int currRight = 0;
        while (currRight < n) {
            int maxRight = -1;
            //针对每个位点，统计左边界在此位点左侧的区间中，最大的右边界
            while (currIndex <= n && sortedQueue.get(currIndex)[0] <= currRight) {
                maxRight = Math.max(maxRight, sortedQueue.get(currIndex)[1]);
                currIndex++;
            }
            if (maxRight == -1) return -1;
            ans++;
            currRight = maxRight; //更新右边界
        }
        return ans;
    }

    public int minTaps01(int n, int[] ranges) {
        int[] maxRight = new int[n];
        for (int i = 0; i <= n; i++) {
            int left = Math.max(i - ranges[i], 0);
            int right = Math.min(i + ranges[i], n);
            for (int j = left; j < right; j++) {
                maxRight[j] = Math.max(maxRight[j], right);
            }
        }
        int ans = 0;
        int currRight = 0;
        while (currRight < n) {
            if (maxRight[currRight] == 0) return -1;
            ans++;
            currRight = maxRight[currRight];
        }
        return ans;
    }


    /**
     * 45. 跳跃游戏 II
     */
    public int jump06(int[] nums) {
        int ans = 0;
        int maxIndex = 0;
        int endIndex = 0;
        for (int i = 0; i < nums.length; i++) {
            maxIndex = Math.max(maxIndex, i + nums[i]);
            if (i == endIndex) {
                ans++;    //在此区间内寻找一个起跳点，可跳跃至 maxIndex
                endIndex = maxIndex;
            }
        }
        return ans;
    }


    /**
     * 1024. 视频拼接
     */
    public int videoStitching(int[][] clips, int time) {
        int ans = 0;
        int n = clips.length;
        Arrays.sort(clips, (o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];   //优先按照开始时间升序排序
            else return o2[1] - o1[1];                  //其次按照结束时间降序排序
        });
        int endIndex = 0;
        int curIndex = 0;
        while (curIndex < n && endIndex < time) {
            int maxIndex = -1;
            while (curIndex < n && clips[curIndex][0] <= endIndex) {
                maxIndex = Math.max(maxIndex, clips[curIndex][1]);
                curIndex++;
            }
            if (maxIndex == -1) {
                return -1;
            }
            ans++;
            endIndex = maxIndex;
        }
        return endIndex < time ? -1 : ans;
    }

    public int videoStitching01(int[][] clips, int time) {   //动态规划
        int[] dp = new int[time + 1];
        Arrays.fill(dp, Integer.MAX_VALUE - 1);
        dp[0] = 0;
        for (int i = 1; i <= time; i++) {
            for (int[] range : clips) {
                if (range[0] < i && i <= range[1]) {
                    dp[i] = Math.min(dp[i], dp[range[0]] + 1);
                }
            }
        }
        return dp[time] == Integer.MAX_VALUE - 1 ? -1 : dp[time];
    }


    private int maxArea = 0;

    /**
     * 剑指 Offer II 105. 岛屿的最大面积
     */
    public int maxAreaOfIsland(int[][] grid) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};  //上下左右
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    int currArea = maxArea;
                    maxArea = 0;
                    maxAreaOfIslandDfs(grid, directions, i, j);
                    maxArea = Math.max(maxArea, currArea);
                }
            }
        }
        return maxArea;
    }

    private void maxAreaOfIslandDfs(int[][] grid, int[][] directions, int currRow, int currCol) {
        //递归终止条件一：越界
        if (currRow < 0 || currRow >= grid.length || currCol < 0 || currCol >= grid[0].length) {
            return;
        }
        //递归终止条件二：水域
        if (grid[currRow][currCol] == 0) {
            return;
        }

        grid[currRow][currCol] = 0;
        maxArea++;

        for (int[] dir : directions) {
            int nextRow = currRow + dir[0];
            int nextCol = currCol + dir[1];
            maxAreaOfIslandDfs(grid, directions, nextRow, nextCol);
        }
    }


    public int maxAreaOfIsland01(int[][] grid) {    //广度优先搜素
        int m = grid.length;
        int n = grid[0].length;
        int[][] dirs = {{-1, 0}, {0, -1}};
        maxAreaUFS ufs = new maxAreaUFS(grid);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    for (int[] dir : dirs) {
                        int nextRow = i + dir[0];
                        int nextCol = j + dir[1];
                        if (nextRow >= 0 && nextCol >= 0) {
                            if (grid[nextRow][nextCol] == 1) {
                                ufs.union(i * n + j, nextRow * n + nextCol);
                            }
                        }
                    }
                }
            }
        }
        return ufs.getMaxSize();
    }

    public static class maxAreaUFS {
        int[] nodes;
        int[] sizes;

        maxAreaUFS(int[][] grid) {
            int m = grid.length;
            int n = grid[0].length;
            nodes = new int[m * n];
            sizes = new int[m * n];
            for (int i = 0; i < m * n; i++) {
                int row = i / n;
                int col = i % n;
                if (grid[row][col] == 1) {
                    nodes[i] = i;
                    sizes[i] = 1;
                }
            }
        }

        public int findSet(int xx) {
            if (nodes[xx] != xx) {
                nodes[xx] = findSet(nodes[xx]);
            }
            return nodes[xx];
        }

        public void union(int xx, int yy) {
            int xRoot = findSet(xx);
            int yRoot = findSet(yy);
            if (xRoot == yRoot) return;
            nodes[xRoot] = yRoot;
            sizes[yRoot] += sizes[xRoot];
        }

        int getMaxSize() {
            int max = 0;
            for (int size : sizes) {
                max = Math.max(max, size);
            }
            return max;
        }
    }


    /**
     * 859. 亲密字符串
     */
    public boolean buddyStrings(String str, String goal) {   //错误解法
        if (str.length() != goal.length()) {
            return false;
        }
        int[] arr1 = new int[26];
        int[] arr2 = new int[26];
        int diff = 0;
        for (int i = 0; i < str.length(); i++) {
            char xx = str.charAt(i);
            char yy = goal.charAt(i);
            arr1[xx - 'a']++;
            arr2[yy - 'a']++;
            if (xx != yy) {
                diff++;
            }
        }
        return Arrays.equals(arr1, arr2) && (diff == 2 || diff == 0);
    }


    /**
     * 1160. 拼写单词
     */
    public int countCharacters(String[] words, String chars) {   //错误写法
        int ans = 0;
        int[] buckets = new int[26];
        for (int i = 0; i < chars.length(); i++) {
            buckets[chars.charAt(i) - 'a']++;
        }
        for (String word : words) {
            int[] temp = new int[26];
            for (int i = 0; i < word.length(); i++) {
                temp[word.charAt(i) - 'a']++;
            }
            for (int i = 0; i < 26; i++) {
                if (temp[i] > buckets[i]) {
                    break;
                }
                if (i == 25) {
                    ans += word.length();
                }
            }
        }
        return ans;
    }


    /**
     * 202. 快乐数
     */
    public boolean isHappy(int n) {    //快慢指针
        //基于哈希表判断是否进入循环
        HashSet<Integer> set = new HashSet<>();
        while (n > 1 && !set.contains(n)) {
            set.add(n);
            n = getNext(n);
        }
        return n == 1;
    }

    private int getNext(int n) {
        int sum = 0;
        while (n > 0) {
            int aa = n % 10;
            sum += (aa * aa);
            n /= 10;
        }
        return sum;
    }


    /**
     * 1365. 有多少小于当前数字的数字
     */
    public int[] smallerNumbersThanCurrent(int[] nums) {  //暴力
        int n = nums.length;
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            int freq = 0;
            for (int j = 0; j < n; j++) {
                if (nums[j] < nums[i]) freq++;
            }
            ans[i] = freq;
        }
        return ans;
    }

    public int[] smallerNumbersThanCurrent01(int[] nums) {  //桶排序 + 前缀和
        int n = nums.length;
        int[] ans = new int[n];
        int[] buckets = new int[101];
        //记录每个值的索引位置
        HashMap<Integer, ArrayDeque<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            buckets[nums[i]]++;
            ArrayDeque<Integer> list = map.getOrDefault(nums[i], new ArrayDeque<>());
            list.addLast(i);
            map.put(nums[i], list);
        }
        int prefix = 0;
        for (int i = 0; i < 101; i++) {
            if (buckets[i] > 0) {
                ArrayDeque<Integer> list = map.get(i);
                while (!list.isEmpty()) {
                    ans[list.poll()] = prefix;
                }
                prefix += buckets[i];
            }
        }
        return ans;
    }


    /**
     * 2365. 任务调度器 II
     */
    public long taskSchedulerII(int[] tasks, int space) {  //暴力逐天模拟
        long currDays = 0;
        HashMap<Integer, Long> map = new HashMap<>();
        int index = 0;
        while (index < tasks.length) {
            currDays++;
            long prevDay = map.getOrDefault(tasks[index], 0L);
            if (prevDay == 0 || currDays - prevDay > space) {
                map.put(tasks[index], currDays);
                index++;
            }
        }
        return currDays;
    }

    public long taskSchedulerII01(int[] tasks, int space) {  //暴力区间模拟
        long currDays = 0;
        HashMap<Integer, Long> map = new HashMap<>();
        for (int type : tasks) {
            if (!map.containsKey(type)) {
                currDays++;
            } else {
                currDays = Math.max(currDays + 1, map.get(type) + space + 1);  //注意，只能按顺序执行，不可插空执行，有点类似区间合并的问题
            }
            map.put(type, currDays);
        }
        return currDays;
    }


    /**
     * 2149. 按符号重排数组
     */
    public int[] rearrangeArray(int[] nums) {
        ArrayDeque<Integer> queue1 = new ArrayDeque<>();
        ArrayDeque<Integer> queue2 = new ArrayDeque<>();
        for (int num : nums) {
            if (num > 0) queue1.addLast(num);
            if (num < 0) queue2.addLast(num);
        }
        int currIndex = 0;
        while (!queue1.isEmpty() && !queue2.isEmpty()) {
            nums[currIndex++] = queue1.pollFirst();
            nums[currIndex++] = queue2.pollFirst();
        }
        return nums;
    }

    public int[] rearrangeArray01(int[] nums) {
        int n = nums.length;
        int[] ans = new int[n];
        int indexA = 0;
        int indexB = 1;
        for (int num : nums) {
            if (num > 0) {
                ans[indexA] = num;
                indexA += 2;
            }
            if (num < 0) {
                ans[indexB] = num;
                indexB += 2;
            }
        }
        return ans;
    }


    /**
     * LCP 03. 机器人大冒险
     */
    public boolean robot(String command, int[][] obstacles, int x, int y) {
        HashMap<Integer, HashSet<Integer>> map = new HashMap<>();
        for (int[] obs : obstacles) {
            HashSet<Integer> set = map.getOrDefault(obs[1], new HashSet<>());   //横纵坐标搞清楚
            set.add(obs[0]);
            map.put(obs[1], set);
        }
        int row = 0;
        int col = 0;
        while (true) {
            for (int i = 0; i < command.length(); i++) {
                //1、剪枝一：机器人遇到障碍物，机器人损毁
                if (map.containsKey(row) && map.get(row).contains(col)) {
                    return false;
                }
                //2、剪枝二：后续不可能到达目标点
                if (row > y || col > x) {
                    return false;
                }
                //3、目标点：到达目标点
                if (row == y && col == x) {
                    return true;
                }

                if (command.charAt(i) == 'U') row++;
                if (command.charAt(i) == 'R') col++;
            }
        }
    }


    /**
     * 929. 独特的电子邮件地址
     */
    public int numUniqueEmails(String[] emails) {
        HashSet<String> set = new HashSet<>();
        for (String email : emails) {
            String[] split = email.split("@");
            String str = split[0].replaceAll("\\.", "");
            if (str.contains("+")) {
                set.add(str.substring(0, str.indexOf("+")) + "@" + split[1]);
            } else {
                set.add(str + "@" + split[1]);
            }
        }
        return set.size();
    }

    /**
     * 941. 有效的山脉数组
     */
    public boolean validMountainArray(int[] arr) {
        int n = arr.length;
        if (n < 3) return false;
        int freq = 0;
        for (int i = 1; i < n - 1; i++) {
            if (arr[i - 1] == arr[i] || arr[i] == arr[i + 1]) return false;
            if (arr[i - 1] > arr[i] && arr[i] < arr[i + 1]) return false;
            if (arr[i - 1] < arr[i] && arr[i] > arr[i + 1]) {
                freq++;
            }
        }
        return freq == 1;
    }

    public boolean validMountainArray01(int[] arr) {   //单侧线性扫描
        int n = arr.length;
        if (n < 3) return false;
        int currIndex = 1;
        //1、递增扫描
        while (currIndex < n && arr[currIndex - 1] < arr[currIndex]) {
            currIndex++;
        }

        if (currIndex == 1 || currIndex == n) {
            return false;
        }

        //2、递减扫描
        while (currIndex < n && arr[currIndex - 1] > arr[currIndex]) {
            currIndex++;
        }
        return currIndex == n;
    }


    public boolean validMountainArray02(int[] arr) {  //双指针
        int n = arr.length;
        if (n < 3) return false;
        int left = 1;
        while (left < n && arr[left - 1] < arr[left]) {
            left++;
        }
        if (left == 1 || left == n) return false;
        int right = n - 1;
        while (right > 0 && arr[right - 1] > arr[right]) {
            right--;
        }
        return right + 1 == left;
    }


    /**
     * 914. 卡牌分组
     */
    public boolean hasGroupsSizeX(int[] deck) {  //错误写法，[1,1,2,2,2,2]
        int[] buckets = new int[10000];
        for (int num : deck) {
            buckets[num]++;
        }
        int gg = -1;
        for (int freq : buckets) {
            if (freq > 0) {
                if (gg == -1) gg = freq;
                else gg = gcd(gg, freq);
            }
        }
        return gg >= 2;
    }

    private int gcd(int xx, int yy) {  //最大公约数
        return xx == 0 ? yy : gcd(yy % xx, xx);
    }


    public boolean hasGroupsSizeX01(int[] deck) {  //错误写法，[1,1,2,2,2,2]
        int max = 0;
        for (int num : deck) {
            max = Math.max(max, num);
        }
        int[] buckets = new int[max + 1];
        for (int num : deck) {
            buckets[num]++;
        }
        HashSet<Integer> set = new HashSet<>();
        int maxValue = 0;
        for (int freq : buckets) {
            if (freq > 0) {
                set.add(freq);
                maxValue = Math.max(maxValue, freq);
            }
        }
        return set.size() == 1 && maxValue >= 2;
    }


    /**
     * 1334. 阈值距离内邻居最少的城市
     */
    public int findTheCity(int n, int[][] edges, int distanceThreshold) {  //Floyd
        int INF = 0x3f3f3f3f;
        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], INF);
            dist[i][i] = 0;
        }
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            dist[node1][node2] = edge[2];
            dist[node2][node1] = edge[2];
        }
        for (int m = 0; m < n; m++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    dist[i][j] = Math.min(dist[i][j], dist[i][m] + dist[m][j]);
                }
            }
        }
        int ans = -1;
        int minNums = n + 10;
        for (int i = n - 1; i >= 0; i--) {
            int currNums = 0;
            for (int j = 0; j < n; j++) {
                if (dist[i][j] <= distanceThreshold) currNums++;
            }
            if (currNums < minNums) {
                ans = i;
                minNums = currNums;
            }
        }
        return ans;
    }


    public int findTheCity01(int n, int[][] edges, int distanceThreshold) {  //Dijkstra
        int minNode = -1;
        int minNums = n + 10;
        for (int i = n - 1; i >= 0; i--) {
            int currNums = findTheCityBellmanFord(n, i, edges, distanceThreshold);
            if (currNums < minNums) {
                minNode = i;
                minNums = currNums;
            }
        }
        return minNode;
    }


    private int findTheCityBellmanFord(int n, int k, int[][] edges, int distanceThreshold) {
        int INF = 0x3f3f3f3f;
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[k] = 0;
        for (int i = 0; i < n; i++) {
            int[] prev = dist.clone();
            for (int[] edge : edges) {
                int node1 = edge[0];
                int node2 = edge[1];
                int distance = edge[2];
                dist[node1] = Math.min(dist[node1], prev[node2] + distance);
                dist[node2] = Math.min(dist[node2], prev[node1] + distance);
            }
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (dist[i] <= distanceThreshold) ans++;
        }
        return ans;
    }


    /**
     * 1089. 复写零
     */
    public void duplicateZeros(int[] arr) {
        int n = arr.length;
        int[] nums = arr.clone();
        int currIndex = 0;
        for (int i = 0; i < n && currIndex < n; i++) {
            arr[currIndex] = nums[i];
            currIndex++;
            if (nums[i] == 0 && currIndex < n) {
                arr[currIndex] = 0;
                currIndex++;
            }
        }
    }


    /**
     * 6319. 奇偶位数
     */
    public int[] evenOddBit(int n) {
        int num1 = 0;
        int num2 = 0;
        int time = 0;
        while (n > 0) {
            if ((n & 1) == 1) {
                if ((time & 1) == 1)
                    num2++;
                else
                    num1++;
            }
            time++;
            n /= 2;
        }
        return new int[]{num1, num2};
    }


    /**
     * 6322. 检查骑士巡视方案
     */
    public boolean checkValidGrid(int[][] grid) {
        int n = grid.length;
        int[][] dirs = {{-2, -1}, {-2, 1}, {2, -1}, {2, 1}, {-1, -2}, {1, -2}, {-1, 2}, {1, 2}};
        int nextNum = 1;
        int row = 0;
        int col = 0;
        while (nextNum < n * n) {
            int flag = 0;
            for (int[] dir : dirs) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];
                if (nextRow >= 0 && nextRow < n && nextCol >= 0 && nextCol < n) {
                    if (grid[nextRow][nextCol] == nextNum) {
                        row = nextRow;
                        col = nextCol;
                        nextNum++;
                        flag = 1;
                        break;
                    }
                }
            }
            if (flag == 0) return false;
        }
        return true;
    }


    /**
     * 6352. 美丽子集的数目
     */
    public int beautifulSubsets(int[] nums, int k) {


        return 0;
    }


    /**
     * 6321. 执行操作后的最大 MEX
     */
    public int findSmallestInteger01(int[] nums, int value) {   //超时
        int ans = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        while (true) {
            Set<Integer> list = map.keySet();
            int flag = 0;
            for (int digit : list) {
                if ((Math.abs(digit - ans) % value) == 0) {
                    Integer freq = map.get(digit);
                    freq--;
                    if (freq > 0) map.put(digit, freq);
                    else map.remove(digit);
                    ans++;
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) return ans;
        }
    }


    /**
     * 6321. 执行操作后的最大 MEX
     */
    public int findSmallestInteger02(int[] nums, int value) {   //超时
        int ans = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
            max = Math.max(max, num);
            min = Math.min(min, num);
        }
        while (true) {
            if (ans <= min) {
                int temp = ans;
                int flag = 0;
                while (temp <= max + value) {
                    if (map.containsKey(temp)) {
                        Integer freq = map.get(temp);
                        freq--;
                        if (freq > 0) map.put(temp, freq);
                        else map.remove(temp);
                        ans++;
                        flag = 1;
                        break;
                    }
                    temp += value;
                }
                if (flag == 0) return ans;
            } else if (ans >= max) {
                int temp = ans;
                int flag = 0;
                while (temp >= min - value) {
                    if (map.containsKey(temp)) {
                        Integer freq = map.get(temp);
                        freq--;
                        if (freq > 0) map.put(temp, freq);
                        else map.remove(temp);
                        ans++;
                        flag = 1;
                        break;
                    }
                    temp -= value;
                }
                if (flag == 0) return ans;
            } else {
                int temp = ans;
                int flag = 0;
                while (temp <= max + value) {
                    if (map.containsKey(temp)) {
                        Integer freq = map.get(temp);
                        freq--;
                        if (freq > 0) map.put(temp, freq);
                        else map.remove(temp);
                        ans++;
                        flag = 1;
                        break;
                    }
                    temp += value;
                }
                if (flag == 1) continue;
                temp = ans;
                while (temp >= min - value) {
                    if (map.containsKey(temp)) {
                        Integer freq = map.get(temp);
                        freq--;
                        if (freq > 0) map.put(temp, freq);
                        else map.remove(temp);
                        ans++;
                        flag = 1;
                        break;
                    }
                    temp -= value;
                }
                if (flag == 0) return ans;
            }
        }
    }


    /**
     * 6321. 执行操作后的最大 MEX
     */
    public int findSmallestInteger(int[] nums, int value) {
        int[] buckets = new int[value];
        for (int num : nums) {
//            buckets[(num + value) % value]++;  //错误写法
            buckets[(num % value + value) % value]++;
        }
        for (int i = 0; ; i++) {
            if (--buckets[i % value] < 0) return i;
        }
    }

    /**
     * 806. 写字符串需要的行数
     */
    public int[] numberOfLines(int[] widths, String str) {
        int ans = 0;
        int prefix = 0;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (prefix + widths[ch - 'a'] > 100) {
                ans++;
                prefix = 0;
            }
            prefix += widths[ch - 'a'];
        }
        return new int[]{ans, prefix};
    }


    /**
     * 1630. 等差子数组
     */
    public List<Boolean> checkArithmeticSubarrays(int[] nums, int[] index1, int[] index2) {
        ArrayList<Boolean> ans = new ArrayList<>();
        int n = index1.length;
        for (int i = 0; i < n; i++) {
            int m = index2[i] - index1[i] + 1;
            if (m <= 1) {
                ans.add(false);
                continue;
            }
            if (m == 2) {
                ans.add(true);
                continue;
            }
            int[] arr = new int[m];
            System.arraycopy(nums, index1[i], arr, 0, m);
            Arrays.sort(arr);
            int diff = arr[1] - arr[0];
            int flag = 0;
            for (int j = 2; j < m; j++) {
                if (arr[j] - arr[j - 1] != diff) {
                    ans.add(false);
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                ans.add(true);
            }
        }
        return ans;
    }


    /**
     * 1311. 获取你好友已观看的视频
     */
    public List<String> watchedVideosByFriends(List<List<String>> watchedVideos, int[][] friends, int id, int level) {
        int n = friends.length;
        int[] visited = new int[n];
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.addLast(id);
        visited[id] = 1;
        int currLevel = 0;
        while (!queue.isEmpty() && currLevel < level) {
            int size = queue.size();
            for (int i = 0; i < size && !queue.isEmpty(); i++) {
                Integer currNode = queue.pollFirst();
                for (int nextNode : friends[currNode]) {
                    if (visited[nextNode] == 1) continue;
                    queue.addLast(nextNode);
                    visited[nextNode] = 1;
                }
            }
            currLevel++;
        }
        HashMap<String, Integer> map = new HashMap<>();
        while (!queue.isEmpty()) {
            Integer currNode = queue.poll();
            List<String> lists = watchedVideos.get(currNode);
            for (String videoName : lists) {
                map.put(videoName, map.getOrDefault(videoName, 0) + 1);
            }
        }
        ArrayList<videos> sorted = new ArrayList<>();
        for (String videoName : map.keySet()) {
            sorted.add(new videos(videoName, map.get(videoName)));
        }
        sorted.sort(new Comparator<videos>() {
            @Override
            public int compare(videos o1, videos o2) {
                if (o1.freqs != o2.freqs) return o1.freqs - o2.freqs;
                return o1.name.compareTo(o2.name);
            }
        });
        ArrayList<String> ans = new ArrayList<>();
        for (int i = 0; i < sorted.size(); i++) {
            ans.add(sorted.get(i).name);
        }
        return ans;
    }

    static class videos {
        public String name;
        public int freqs;

        public videos(String name, int freqs) {
            this.name = name;
            this.freqs = freqs;
        }
    }


    /**
     * 面试题 17.16. 按摩师
     */
    public int massage(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        if (n == 1) return nums[0];
        int[] dp = new int[n];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);   //dp[i]表示到达位置 i 时的最优值
        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }
        return dp[n - 1];
    }

    public int massage01(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        if (n == 1) return nums[0];
        int[] dp = new int[n];
        dp[0] = nums[0];
        dp[1] = nums[1];    //错误写法
        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }
        return Math.max(dp[n - 1], dp[n - 2]);
    }

    //------------------------------------------
    // 将上面的题好好思考一下
    //------------------------------------------


    /**
     * 1638. 统计只差一个字符的子串数目
     */
    public int countSubstrings(String aa, String bb) {
        int ans = 0;
        int m = aa.length();
        int n = bb.length();
        for (int i = 0; i < m; i++) {       //枚举起点一
            for (int j = 0; j < n; j++) {   //枚举起点二
                int diff = 0;
                for (int k = 0; i + k < m && j + k < n; k++) {
                    diff += aa.charAt(i + k) == bb.charAt(j + k) ? 0 : 1;
                    if (diff == 1) ans++;
                    else if (diff > 1) break;
                }
            }
        }
        return ans;
    }


    /**
     * 77. 组合
     */
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = i + 1;
        }
        combineDfs(nums, ans, path, k, 0);
        return ans;
    }

    private void combineDfs(int[] nums, List<List<Integer>> ans, LinkedList<Integer> path, int k, int currIndex) {
        if (path.size() == k) {
            ans.add(new ArrayList<>(path));
            return;
        }

        //--------------------------------------------
        // 每层的循环都是在枚举某个位置的可用元素，其可用元素指从当前 currIndex 后的所有元素
        //--------------------------------------------
        for (int i = currIndex; i < nums.length; i++) {  //纵向剪枝，组合而非排序的关键
            path.addLast(nums[i]);
            combineDfs(nums, ans, path, k, i + 1);
            path.removeLast();
        }

    }


    /**
     * 40. 组合总和 II
     */
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        combinationSum2Dfs(candidates, ans, path, 0, target, 0);
        return ans;
    }

    private void combinationSum2Dfs(int[] candidates, List<List<Integer>> ans, LinkedList<Integer> path, int sum, int target, int currIndex) {
        if (sum > target) {
            return;
        }
        if (sum == target) {
            ans.add(new ArrayList<>(path));
            return;
        }

        for (int i = currIndex; i < candidates.length; i++) {
            if (i > currIndex && candidates[i] == candidates[i - 1]) {
                continue;
            }
            path.addLast(candidates[i]);
            combinationSum2Dfs(candidates, ans, path, sum + candidates[i], target, i + 1);
            path.removeLast();
        }

    }


    /**
     * 47. 全排列 II
     */
    public List<List<Integer>> permuteUnique(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        int[] visited = new int[nums.length];
        permuteUniqueDfs(nums, ans, path, visited);
        return ans;
    }

    private void permuteUniqueDfs(int[] nums, List<List<Integer>> ans, LinkedList<Integer> path, int[] visited) {
        if (path.size() == nums.length) {
            ans.add(new ArrayList<>(path));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (visited[i] == 1) {
                continue;
            }
            if (i > 0 && nums[i] == nums[i - 1] && visited[i - 1] == 0) {  // && visited[i - 1] == 0 也可通过，但注掉就无法通过
                continue;
            }
            path.addLast(nums[i]);
            visited[i] = 1;

            permuteUniqueDfs(nums, ans, path, visited);

            path.removeLast();
            visited[i] = 0;
        }

    }


    /**
     * 688. 骑士在棋盘上的概率
     */
    public double knightProbability(int n, int k, int row, int column) {
        double[][][] cached = new double[n][n][k + 1];
        int[][] dirs = {{-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}, {1, 2}, {2, 1}, {2, -1}, {1, -2}};
        return knightProbabilityDFS(n, k, row, column, cached, dirs);
    }

    private double knightProbabilityDFS(int n, int k, int row, int col, double[][][] cached, int[][] dirs) {
        //剪枝：越界
        if (row < 0 || col < 0 || row >= n || col >= n) {
            return 0;
        }
        //目标：跳跃次数使用完
        if (k == 0) {
            return 1;
        }
        //剪枝：记忆化搜索
        if (cached[row][col][k] > 0) {
            return cached[row][col][k];
        }
        double ans = 0;
        for (int[] dir : dirs) {
            int nextRow = row + dir[0];
            int nextCol = col + dir[1];
            ans += knightProbabilityDFS(n, k - 1, nextRow, nextCol, cached, dirs) / 8.0;
        }
        cached[row][col][k] = ans;
        return ans;
    }


    public double knightProbability01(int n, int k, int row, int column) {
        double[][][] dp = new double[n][n][k + 1];
        int[][] dirs = {{-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}, {1, 2}, {2, 1}, {2, -1}, {1, -2}};
        //走 0 步到达 {i , j} 的概率
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j][0] = 1;
            }
        }
        for (int m = 1; m <= k; m++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    for (int[] dir : dirs) {
                        int nextRow = i + dir[0];
                        int nextCol = j + dir[1];
                        if (nextRow >= 0 && nextRow < n && nextCol >= 0 && nextCol < n) {
                            dp[i][j][m] += dp[nextRow][nextCol][m - 1] / 8.0;
                        }
                    }
                }
            }
        }
        return dp[row][column][k];
    }


    /**
     * 1017. 负二进制转换
     */
    public String baseNeg2(int n) {

        return "";
    }


    /**
     * 402. 移掉 K 位数字
     */
    public String removeKdigits(String num, int k) {
        int n = num.length();
        ArrayDeque<Character> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!queue.isEmpty() && queue.peekLast() > num.charAt(i) && k > 0) {
                queue.pollLast();
                k--;
            }
            queue.addLast(num.charAt(i));
        }
        while (!queue.isEmpty() && queue.peekFirst() == '0') {
            queue.pollFirst();
        }
        while (!queue.isEmpty() && k > 0) {
            queue.pollLast();
            k--;
        }

        if (queue.isEmpty()) return "0";
        StringBuilder ans = new StringBuilder();
        while (!queue.isEmpty()) {
            ans.append(queue.pollFirst());
        }
        return ans.toString();
    }


    /**
     * 2399. 检查相同字母间的距离
     */
    public boolean checkDistances(String str, int[] distance) {
        HashSet<Character> visited = new HashSet<>();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (visited.contains(ch)) {
                continue;
            }
            int end = str.indexOf(ch, i + 1);
            if (distance[ch - 'a'] != end - i - 1) {
                return false;
            }
            visited.add(ch);
        }
        return true;
    }


    /**
     * 1029. 两地调度
     */
    public int twoCitySchedCost(int[][] costs) {
        int n = costs.length;
        Arrays.sort(costs, (o1, o2) -> Math.abs(o2[0] - o2[1]) - Math.abs(o1[0] - o1[1]));
        int sum1 = 0;
        int sum2 = 0;
        int nums1 = 0;
        int nums2 = 0;
        for (int i = 0; i < n; i++) {
            int[] tuple = costs[i];
            int costA = tuple[0];
            int costB = tuple[1];
            if (costA <= costB) {
                if (nums1 < n / 2) {
                    sum1 += costA;
                    nums1++;
                    continue;
                }
                sum2 += costB;
                nums2++;
            } else {
                if (nums2 < n / 2) {
                    sum2 += costB;
                    nums2++;
                    continue;
                }
                sum1 += costA;
                nums1++;
            }
        }
        return sum1 + sum2;
    }


    public int twoCitySchedCost01(int[][] costs) {
        int sum = 0;
        int n = costs.length;
        int[] diff = new int[n];
        for (int i = 0; i < n; i++) {
            sum += costs[i][0];   //暂定所有人都去城市 A
            diff[i] = costs[i][1] - costs[i][0];
        }
        Arrays.sort(diff);  //从小到大排序
        for (int i = 0; i < n / 2; i++) {   //从所有人中，选出去城市 B 代价最低的 n/2 个人
            sum += diff[i];  //存在负数和正数
        }
        return sum;
    }


    /**
     * 2404. 出现最频繁的偶数元素
     */
    public int mostFrequentEven(int[] nums) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            if ((num & 1) != 0) continue;
            Integer freq = map.getOrDefault(num, 0);
            map.put(num, freq + 1);
        }
        ArrayList<Map.Entry<Integer, Integer>> sorted = new ArrayList<>(map.entrySet());
        sorted.sort((o1, o2) -> {
            if (!o1.getValue().equals(o2.getValue())) {
                return o2.getValue() - o1.getValue();
            }
            return o1.getKey() - o2.getKey();
        });
        return sorted.size() == 0 ? -1 : sorted.get(0).getKey();
    }

    public int mostFrequentEven01(int[] nums) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            if ((num & 1) != 0) continue;
            Integer freq = map.getOrDefault(num, 0);
            map.put(num, freq + 1);
        }
        if (map.size() == 0) return -1;
        int maxFreq = -1;
        int ans = -1;
        Set<Integer> list = map.keySet();
        for (int num : list) {
            if (map.get(num) > maxFreq) {
                ans = num;
                maxFreq = map.get(num);
            } else if (map.get(num) == maxFreq && num < ans) {
                ans = num;
            }
        }
        return ans;
    }

    /**
     * 435. 无重叠区间
     */
    public int eraseOverlapIntervals(int[][] intervals) {   //当成预定会议的题目

        return 0;
    }

    /**
     * 452. 用最少数量的箭引爆气球
     */
    public int findMinArrowShots01(int[][] points) {
        int n = points.length;
        Arrays.sort(points, (o1, o2) -> {
            if (o1[0] > o2[0]) return 1;
            else if (o1[0] < o2[1]) return -1;
            return 0;
        });
        int ans = 1;
        int[] edge = points[0];
        for (int i = 1; i < n; i++) {
            if (edge[1] < points[i][0]) {
                ans++;
                edge = points[i];
            } else {
                edge[0] = points[i][0];
                edge[1] = Math.min(edge[1], points[i][1]);
            }
        }
        return ans;
    }


    /**
     * 45. 跳跃游戏 II
     */
    public int jump011(int[] nums) {
        int ans = 0;
        int n = nums.length;
        int maxIndex = 0;
        int endIndex = 0;
        for (int i = 0; i < n - 1; i++) {
            maxIndex = Math.max(maxIndex, i + nums[i]);
            if (i == endIndex) {
                ans++;
                endIndex = maxIndex;
            }
        }
        return ans;
    }


    public int[] rowAndMaximumOnes(int[][] mat) {
        int m = mat.length;
        int n = mat[0].length;
        int[] ans = new int[2];
        for (int i = 0; i < m; i++) {
            int freq = 0;
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 1) freq++;
            }
            if (freq > ans[1]) {
                ans[0] = i;
                ans[1] = freq;
            }
        }
        return ans;
    }


    /**
     * 1024. 视频拼接
     */
    public int videoStitching00(int[][] clips, int time) {   //错误写法
        int ans = 0;
        int n = clips.length;
        Arrays.sort(clips, (o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];  //首先，以开始时间升序排序
            return o2[1] - o1[1];                      //其次，以持续时长降序排序
        });
        int endIndex = 0;
        for (int i = 0; i < n && endIndex <= time; i++) {
            if (endIndex < clips[i][0]) {
                return -1;
            } else if (clips[i][1] <= endIndex) {
                continue;
            }
            int maxIndex = endIndex;
            while (i < n && clips[i][0] <= endIndex && endIndex < clips[i][1]) {
                maxIndex = Math.max(maxIndex, clips[i][1]);
                i++;
            }
            endIndex = maxIndex;
            ans++;
            i--;
        }
        return endIndex < time ? -1 : ans;
    }


    public int videoStitching02(int[][] clips, int time) {   //错误写法
        int ans = 0;
        int n = clips.length;
        Arrays.sort(clips, (o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];  //首先，以开始时间升序排序
            return o2[1] - o1[1];                      //其次，以持续时长降序排序
        });
        int endIndex = 0;
        for (int i = 0; i < n && endIndex <= time; i++) {
            if (endIndex < clips[i][0]) return -1;
            else if (clips[i][1] > endIndex) {
                ans++;
                endIndex = clips[i][1];
                System.out.println(Arrays.toString(clips[i]));
            }
        }
        return endIndex < time ? -1 : ans;
    }


    /**
     * 1024. 视频拼接
     */
    public int videoStitchingTTT(int[][] clips, int time) {
        int ans = 0;
        int n = clips.length;
        Arrays.sort(clips, (o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];   //优先按照开始时间升序排序
            else return o2[1] - o1[1];                  //其次按照结束时间降序排序
        });
        int endIndex = 0;
        int curIndex = 0;
        while (curIndex < n && endIndex < time) {
            int maxIndex = -1;
            while (curIndex < n && clips[curIndex][0] <= endIndex) {
                maxIndex = Math.max(maxIndex, clips[curIndex][1]);
                curIndex++;
            }
            if (maxIndex == -1) {
                return -1;
            }
            ans++;
            endIndex = maxIndex;
        }
        return endIndex < time ? -1 : ans;
    }

    public int videoStitching1111(int[][] clips, int time) {
        int ans = 0;
        int n = clips.length;
        Arrays.sort(clips, (o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];
            return o2[1] - o1[1];
        });
        int curIndex = 0;
        int endIndex = 0;
        while (curIndex < n && endIndex < time) {
            int maxIndex = -1;
            while (curIndex < n && clips[curIndex][0] <= endIndex) {
                maxIndex = Math.max(maxIndex, clips[curIndex][1]);
                curIndex++;
            }
            if (maxIndex == -1) return -1;
            ans++;
            endIndex = maxIndex;
        }
        return endIndex < time ? -1 : ans;
    }


    /**
     * 1419. 数青蛙
     */
    public int minNumberOfFrogs(String croakOfFrogs) {
        //---------------------------------------------------
        // 俄罗斯方块思想：
        //    一共 5 个位置的盘，遍历字符串，每个字符是一个方块，会落入到盘
        //    每个方块落盘时：
        //        1、高度必须小于前面一个方块的高度，才能保证 croak 字符的顺序性和完整性
        //        2、五个位置均有方块，则消掉一层
        //     整个过程中，需要同时鸣叫的青蛙数量，取决于每个方块落地时，各个方块的最大高度差
        //---------------------------------------------------
        int c = 0;
        int r = 0;
        int o = 0;
        int a = 0;
        int k = 0;
        int max = 0;
        for (int i = 0; i < croakOfFrogs.length(); i++) {
            if (croakOfFrogs.charAt(i) == 'c') c++;
            if (croakOfFrogs.charAt(i) == 'r') r++;
            if (croakOfFrogs.charAt(i) == 'o') o++;
            if (croakOfFrogs.charAt(i) == 'a') a++;
            if (croakOfFrogs.charAt(i) == 'k') k++;
            if (c >= r && r >= o && o >= a && a >= k) {
                max = Math.max(max, c - k);
            } else {
                return -1;
            }
        }
        if (c == r && r == o && o == a && a == k) return max;
        return -1;
    }


    public int minNumberOfFrogs01(String croakOfFrogs) {
        int rest = 0;
        int active = 0;
        int[] buckets = new int[5];
        HashMap<Character, Integer> map = new HashMap<>();
        map.put('c', 0);
        map.put('r', 1);
        map.put('o', 2);
        map.put('a', 3);
        map.put('k', 4);
        for (int i = 0; i < croakOfFrogs.length(); i++) {
            char ch = croakOfFrogs.charAt(i);
            Integer index = map.get(ch);
            buckets[index]++;
            if (ch == 'c') {
                active++;
                if (rest > 0) {
                    rest--;
                    //--------------------------------------------
                    // 1、如果当前无正在休息的青蛙，无法从其中借一个，后面再还，意味着新增了一个青蛙
                    // 2、如果当前有正在休息的青蛙，从其中借一个，后面再还
                    //--------------------------------------------
                }
            } else {
                if (buckets[index] > buckets[index - 1]) return -1;
                if (ch == 'k') {    //蛙鸣结束，将鸣叫的青蛙还到休息的青蛙
                    active--;
                    rest++;
                }
            }
        }
        for (int i = 1; i < 5; i++) {
            if (buckets[i] != buckets[i - 1]) return -1;
        }
        return rest;
    }


    /**
     * 1010. 总持续时间可被 60 整除的歌曲
     */
    public int numPairsDivisibleBy60(int[] time) {
        int ans = 0;
        int[] buckets = new int[60];
        for (int tt : time) {
            ans += buckets[(60 - tt % 60) % 60];
            buckets[tt % 60]++;
        }
        return ans;
    }


    public int numPairsDivisibleBy60BAK(int[] time) {
        int ans = 0;
        int[] buckets = new int[60];
        for (int tt : time) {
            tt %= 60;
            if (tt == 0) ans += buckets[0];   //自身就是 60 的倍数，可以和其他为 60 倍数的歌曲组合
            else ans += buckets[60 - tt];     //自身不是 60 的倍数，可以和其他歌曲组合为 60 的倍数
            buckets[tt]++;
        }
        return ans;
    }






}


