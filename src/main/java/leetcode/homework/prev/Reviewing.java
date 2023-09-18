package leetcode.homework.prev;

import leetcode.algorithm.ArrayListNode;
import leetcode.algorithm.greedy;

import java.util.*;

public class Reviewing {


    public int maxProduct(String[] words) {
        int maxWindow = 0;
        int[] maskBit = new int[words.length];
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < words[i].length(); j++) {
                maskBit[i] |= (1 << (words[i].charAt(j) - 'a'));
            }
        }
        for (int i = 0; i < maskBit.length; i++) {
            for (int j = i + 1; j < maskBit.length; j++) {
                if ((maskBit[i] & maskBit[j]) == 0)
                    maxWindow = Math.max(maxWindow, words[i].length() * words[j].length());
            }
        }
        return maxWindow;
    }


    /**
     * 面试题 08.01. 三步问题
     */
    public int waysToStep(int n) {
        if (n <= 2) return n;
        int mod = (int) Math.pow(10, 9) + 7;
        long[] dp = new long[n + 1];
        dp[1] = 1;   //到第 1 个台阶，共有 1 种走法
        dp[2] = 2;   //到第 2 个台阶，共有 2 种走法
        dp[3] = 4;   //到第 3 个台阶，共有 4 种走法
        for (int i = 4; i <= n; i++) {
            dp[i] = (dp[i - 1] + dp[i - 2] + dp[i - 3]) % mod;
        }
        return (int) (dp[n] % mod);
    }

    public int waysToStep01(int n) {   //动态规划 + 滚动数组，优化时间复杂度
        if (n <= 2) return n;
        int mod = (int) Math.pow(10, 9) + 7;
        long dp1 = 1;
        long dp2 = 2;
        long dp3 = 4;
        for (int i = 4; i <= n; i++) {
            long current = (dp1 + dp2 + dp3) % mod;
            dp1 = dp2;
            dp2 = dp3;
            dp3 = current;
        }
        return (int) dp3;
    }


    /**
     * 70. 爬楼梯
     */
    public int climbStairs(int n) {
        if (n <= 2) return n;
        int[] dp = new int[n + 1];
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i <= n; i++) {
            //-------------------------------------------------------------------------------
            // 由于每次只能走 1 / 2步，因此走到 n层，只能从 n - 1层（走 1 步） 和 n - 2层（走 2 步）
            //-------------------------------------------------------------------------------
            dp[i] = dp[i - 2] + dp[i - 1];
        }
        return dp[n];
    }

    public int climbStairs01(int n) {  //动态数组，优化时间复杂度
        if (n <= 2) return n;
        int dp1 = 1;
        int dp2 = 2;
        for (int i = 3; i <= n; i++) {
            int current = dp1 + dp2;
            dp1 = dp2;
            dp2 = current;
        }
        return dp2;
    }


    /**
     * 746. 使用最小花费爬楼梯
     */
    public int minCostClimbingStairs(int[] cost) {
        //--------------------------------------------
        // 问题分析：
        //     假设数组 cost 的长度为 n,则 n 个阶梯对应的下标为 0 到 n - 1，楼层顶部对应下标 n
        // 问题等价：
        //     到达下标 n 的最小花费
        //--------------------------------------------
        int n = cost.length;
        int[] dp = new int[n + 1];
        //可以用下标 0 或 1 作为初始阶梯，开始攀登
        dp[0] = 0;
        dp[1] = 0;
        for (int i = 2; i < dp.length; i++) {
            //到达下标 i 楼梯的最小花费
            dp[i] = Math.min(dp[i - 1] + cost[i - 1], dp[i - 2] + cost[i - 2]);
        }
        return dp[n];
    }


    public int minCostClimbingStairs001(int[] cost) {  //滚动数组，优化空间
        //针对当前 current 台阶来讲
        int prevPrev = 0;   //达到坐标 0 的台阶，所需花费的最小费用
        int prev = 0;       //达到坐标 1 的台阶，所需花费的最小费用
        for (int i = 2; i <= cost.length; i++) {   //台阶坐标
            int current = Math.min(prev + cost[i - 1], prevPrev + cost[i - 2]);
            prevPrev = prev;
            prev = current;
        }
        return prev;
    }


    /**
     * 926. 将字符串翻转到单调递增
     */
    public int minFlipsMonoIncr(String str) {   //基于前缀和的思想
        //初始位点（-1）及其左侧 1 的个数，包含初始位点
        int left_1 = 0;
        //初始位点右侧 0 的个数，不包含初始位点
        int right_0 = (int) Arrays.stream(str.split("")).filter(o -> o.equals("0")).count();
        //最小翻转次数的默认值
        int ans = Math.min(right_0, str.length() - right_0);  //将 0 全部转换为 1，或将 1 全部转换为 0 所需要的最小次数

        //-----------------------------------------------------------------------------
        // 其余为了满足单调递增的要求，翻转策略均为 将当前点及其左侧全部翻转为 0，右侧（不包含）全部翻转为 1
        //-----------------------------------------------------------------------------
        for (int i = 0; i < str.length(); i++) {
            //----------------------------------------------------------------
            // 将当前点（要包含）左侧全部翻转为 0，因此需要统计当前点（包含）左侧中 1 的个数
            // 将当前点（不包含）右侧全部翻转为 1，因此需要统计当前点（不包含）右侧中 0 的个数
            //----------------------------------------------------------------

            //错误理解：无论当前位点为 0/1，均不涉及翻转，
            //正确理解：从左向右遍历，要将当前位以及当前位前所有位置的 1 转换为 0
            if (str.charAt(i) == '1')
                left_1++;  //如果为 1，则当前位点需要翻转，//必须这样写，而不能此处写为 left_1++，下面写为 right_0--，因此 left要有机会 ++
            if (str.charAt(i) == '0') right_0--; //如果为 0，则当前位点无需翻转

            //全局逐个位置比较最小翻转次数
            ans = Math.min(ans, left_1 + right_0);   //以当前位点 i 为分割点，左侧（含当前位点）全部转换为 0 ，右侧（不含当前位点）全部转换为 1
        }
        return ans;
    }

    public int minFlipsMonoIncr01(String str) {
        int n = str.length();
        int[][] dp = new int[n][2];
        //初始化状态
        dp[0][0] = str.charAt(0) == '0' ? 0 : 1;
        dp[0][1] = str.charAt(0) == '1' ? 0 : 1;
        for (int i = 1; i < str.length(); i++) {
            dp[i][0] = dp[i - 1][0] + (str.charAt(i) == '0' ? 0 : 1);
            dp[i][1] = Math.min(dp[i - 1][0], dp[i - 1][1]) + (str.charAt(i) == '1' ? 0 : 1);
        }
        return Math.min(dp[n - 1][0], dp[n - 1][1]);
    }


    /**
     * 1137. 第 N 个泰波那契数
     */
    public int tribonacci(int n) {
        if (n == 0) return 0;
        if (n <= 2) return 1;
        int dp0 = 0;
        int dp1 = 1;
        int dp2 = 1;
        for (int i = 3; i <= n; i++) {
            int current = dp0 + dp1 + dp2;
            dp0 = dp1;
            dp1 = dp2;
            dp2 = current;
        }
        return dp2;
    }


    /**
     * 509. 斐波那契数
     */
    public int fib(int n) {
        if (n <= 1) return n;
        int dp0 = 0;
        int dp1 = 1;
        for (int i = 2; i <= n; i++) {
            int current = dp0 + dp1;
            dp0 = dp1;
            dp1 = current;
        }
        return dp1;
    }


    /**
     * 1025. 除数博弈
     */
    public boolean divisorGame(int n) {
        return (n & 1) != 1;
    }


    /**
     * 剑指 Offer II 088. 爬楼梯的最少成本
     */
    public int minCostClimbingStairs100(int[] cost) {
        int[] dp = new int[cost.length + 1];
        dp[0] = 0;  //初始阶梯
        dp[1] = 0;  //初始阶梯
        for (int i = 2; i < dp.length; i++) {
            dp[i] = Math.min(dp[i - 2] + cost[i - 2], dp[i - 1] + cost[i - 1]);
        }
        return dp[dp.length - 1];
    }


    /**
     * 剑指 Offer 10- II. 青蛙跳台阶问题
     */
    public int numWays(int n) {
        if (n == 0) return 1;
        if (n == 1 || n == 2) return n;
        int mod = (int) Math.pow(10, 9) + 7;
        long[] dp = new long[n + 1];
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i < dp.length; i++) {
            dp[i] = (dp[i - 1] + dp[i - 2]) % mod;
        }
        return (int) dp[dp.length - 1];
    }


    /**
     * 698. 划分为 k个相等的子集
     */
    public boolean canPartitionKSubsets(int[] nums, int k) {
        int sum = Arrays.stream(nums).sum();
        if (sum % k != 0) return false;
        int target = sum / k;
        int[] buckets = new int[k];
        Arrays.sort(nums);
        for (int i = 0, j = nums.length - 1; i < j; i++, j--) {  //降序，增大剪枝的情况，优化时间复杂度
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
        return canPartitionKSubsetsDfs(nums, buckets, target, 0);
    }

    //只要存在即可
    private boolean canPartitionKSubsetsDfs(int[] nums, int[] buckets, int target, int currentIndex) {
        if (currentIndex == nums.length) {
            return true;
        }

        for (int i = 0; i < buckets.length; i++) {  //横线枚举遍历，尝试将 nums[currentIndex] 放入到不同的 bucket中
            //剪枝
            if (buckets[i] + nums[currentIndex] > target) {  //将 nums[currentIndex] 放入当前桶 bucket[i] 中不满足要求
                continue;
            }

            //剪枝
            if (i > 0 && buckets[i] == buckets[i - 1]) {      //遇到相同情况
                continue;
            }

            //剪枝
            if (currentIndex == 0 && i > 0) {                  //头一个元素放在那个桶中效果一致
                break;
            }


            //1、添加元素
            buckets[i] += nums[currentIndex];

            //2、纵向递归搜索
            if (canPartitionKSubsetsDfs(nums, buckets, target, currentIndex + 1)) {
                return true;
            }

            //3、移除元素
            buckets[i] -= nums[currentIndex];
        }
        return false;
    }


    /**
     * 575. 分糖果
     */
    public int distributeCandies(int[] candyType) {
        return Math.min(candyType.length / 2, (int) Arrays.stream(candyType).distinct().count());
    }

    /**
     * 561. 数组拆分
     */
    public int arrayPairSum(int[] nums) {
        int ans = 0;
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i += 2) {
            ans += nums[i];
        }
        return ans;
    }


    /**
     * 524. 通过删除字母匹配到字典里最长单词
     */
    public String findLongestWord(String str, List<String> dictionary) {
        dictionary.sort((o1, o2) -> {
            if (o1.length() != o2.length()) {
                return o2.length() - o1.length();
            }
            return o1.compareTo(o2);
        });
        for (String word : dictionary) {
            int currentIndex = 0;
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == word.charAt(currentIndex)) {
                    currentIndex++;
                }
                if (currentIndex == word.length()) {
                    return word;
                }
            }
        }
        return "";
    }


    /**
     * 781. 森林中的兔子
     */
    public int numRabbits(int[] answers) {
        int res = 0;
        int[] buckets = new int[1000];
        for (int ans : answers) {
            if (buckets[ans] == 0) {
                res += ans + 1;
                buckets[ans] = ans;   //还能容纳其余 i 个兔子
            } else {
                buckets[ans]--;
            }
        }
        return res;
    }

    public int numRabbits01(int[] answers) {
        int res = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int ans : answers) {
            hTable.put(ans, hTable.getOrDefault(ans, 0) + 1);
        }
        for (int ans : hTable.keySet()) {
            if (ans == 0) {   //单独成组
                res += hTable.get(ans);
            } else if (ans + 1 >= hTable.get(ans)) {  //一个组
                res += ans + 1;   //贪心
            } else {
                Integer nums = hTable.get(ans);
                res += (ans + 1) * (nums / (ans + 1) + (nums % (ans + 1) == 0 ? 0 : 1));  //贪心
            }
        }
        return res;
    }


    /**
     * 1221. 分割平衡字符串
     */
    public int balancedStringSplit(String s) {
        int ans = 0;
        int numL = 0;
        int numR = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'L') numL++;
            else numR++;
            if (numL == numR) ans++;
        }
        return ans;
    }


    /**
     * 807. 保持城市天际线
     */
    public int maxIncreaseKeepingSkyline(int[][] grid) {
        int ans = 0;
        int[] leftSkyline = new int[grid.length];
        int[] bottomSkyline = new int[grid.length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                leftSkyline[i] = Math.max(leftSkyline[i], grid[i][j]);
                bottomSkyline[j] = Math.max(bottomSkyline[j], grid[i][j]);
            }
        }
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                ans += Math.min(leftSkyline[i], bottomSkyline[j]) - grid[i][j];
            }
        }
        return ans;
    }


    /**
     * 1405. 最长快乐字符串
     */
    public String longestDiverseString(int a, int b, int c) {
        StringBuilder ans = new StringBuilder();
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o2[1] - o1[1]);  //按照数量降序排序
        if (a > 0) sortedQueue.add(new int[]{'a', a});
        if (b > 0) sortedQueue.add(new int[]{'b', b});
        if (c > 0) sortedQueue.add(new int[]{'c', c});
        int currentIndex = 0;     //待插入字符的位置索引
        while (!sortedQueue.isEmpty()) {
            int[] current = sortedQueue.poll();
            //当前数量最多的字符，不满足要求
            if (currentIndex >= 2 && ans.charAt(currentIndex - 1) == ans.charAt(currentIndex - 2) && ans.charAt(currentIndex - 2) == (char) current[0]) {
                //如果无可用字符
                if (sortedQueue.isEmpty()) {
                    return ans.toString();
                }
                int[] next = sortedQueue.poll();
                ans.append((char) next[0]);
                next[1]--;
                if (next[1] != 0) {
                    sortedQueue.add(next);
                }
                sortedQueue.add(current);
            } else {  //当前数量最多的字符，满足要求
                ans.append((char) current[0]);
                current[1]--;
                if (current[1] != 0) {
                    sortedQueue.add(current);
                }
            }
            currentIndex++;
        }
        return ans.toString();
    }


    /**
     * 1218. 最长定差子序列
     */
    public int longestSubsequence(int[] arr, int difference) {
        int maxWindow = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int num : arr) {
            hTable.put(num, hTable.getOrDefault(num - difference, 0) + 1);
            maxWindow = Math.max(maxWindow, hTable.get(num));
        }
        return maxWindow;
    }


    /**
     * 1005. K 次取反后最大化的数组和
     */
    public int largestSumAfterKNegations(int[] nums, int k) {
        int sum = 0;
        int[] buckets = new int[202];
        for (int num : nums) {
            sum += num;
            buckets[num + 100]++;
        }

        //依次对负数取反
        for (int i = 0; i < 100; i++) {
            int times = Math.min(buckets[i], k);
            sum += (-(i - 100)) * times * 2;
            buckets[100 - (i - 100)] += times;
            k -= times;
            if (k == 0)
                return sum;
        }

        //存在 0 或者 翻转次数为 偶数
        if (buckets[100] != 0 || ((k & 1) == 0)) {
            return sum;
        }

        //针对正数反转
        for (int i = 101; i < 202; i++) {
            if (buckets[i] != 0) {
                sum -= 2 * (i - 100);
                break;
            }
        }
        return sum;
    }


    /**
     * 1705. 吃苹果的最大数目
     */
    public int eatenApples(int[] apples, int[] days) {
        int maxApples = 0;
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[0] - o2[0]);  //按照腐烂时间，升序排序
        int currentDay = 0;
        while (currentDay < days.length || !sortedQueue.isEmpty()) {
            //添加当日苹果
            if (currentDay < days.length) {
                sortedQueue.add(new int[]{currentDay + days[currentDay], apples[currentDay]});
            }
            //移除过期苹果，移除消耗完的元素
            while (!sortedQueue.isEmpty() && (sortedQueue.peek()[0] == currentDay || sortedQueue.peek()[1] == 0)) {
                sortedQueue.poll();
            }

            //当日消耗苹果
            if (!sortedQueue.isEmpty()) {
                sortedQueue.peek()[1]--;
                maxApples++;
            }

            //进入下一天
            currentDay++;
        }
        return maxApples;
    }


    /**
     * 517. 超级洗衣机
     */
    public int findMinMoves(int[] machines) {
        int ans = 0;
        int sum = Arrays.stream(machines).sum();
        if (sum % machines.length != 0) return -1;
        int average = sum / machines.length;
        int prefixSum = 0;
        for (int num : machines) {
            int gap = num - average;
            prefixSum += gap;
//            ans = Math.max(ans, Math.max(Math.abs(prefixSum), Math.abs(gap)));
            ans = Math.max(ans, Math.max(Math.abs(prefixSum), gap));
            //------------------------------------------------
            // Math.abs(gap) 是不正确的，因为只考虑 gap 为正数的情况，因此这种情况下，举例
            //
            //------------------------------------------------
        }
        return ans;
    }


    int maxArea = 0;

    /**
     * 695. 岛屿的最大面积
     */
    public int maxAreaOfIsland(int[][] grid) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
        PriorityQueue<Integer> sortedQueue = new PriorityQueue<>((o1, o2) -> o2 - o1);  //降序，大根堆
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                maxArea = 0;
                if (grid[i][j] == 1) {
                    maxAreaOfIslandDfs(grid, directions, i, j);
                }
                sortedQueue.add(maxArea);
            }
        }
        return sortedQueue.isEmpty() ? 0 : sortedQueue.peek();
    }

    private void maxAreaOfIslandDfs(int[][] grid, int[][] directions, int currentRow, int currentCol) {
        if (currentRow < 0 || currentRow >= grid.length || currentCol < 0 || currentCol >= grid[0].length) {
            return;
        }
        if (grid[currentRow][currentCol] == 0) {
            return;
        }
        maxArea++;
        grid[currentRow][currentCol] = 0;
        for (int[] dir : directions) {
            int nextRow = currentRow + dir[0];
            int nextCol = currentCol + dir[1];
            maxAreaOfIslandDfs(grid, directions, nextRow, nextCol);
        }
    }


    /**
     * 417. 太平洋大西洋水流问题
     */
    public List<List<Integer>> pacificAtlantic(int[][] heights) {
        int m = heights.length;
        int n = heights[0].length;
        int[][] reachPacific = new int[m][n];
        int[][] reachAtlantic = new int[m][n];
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int i = 0; i < m; i++) {
            pacificAtlanticDfs(heights, directions, reachPacific, i, 0);       //左边
            pacificAtlanticDfs(heights, directions, reachAtlantic, i, n - 1);  //右遍
        }
        for (int j = 0; j < n; j++) {
            pacificAtlanticDfs(heights, directions, reachPacific, 0, j);       //上边
            pacificAtlanticDfs(heights, directions, reachAtlantic, m - 1, j);  //下边
        }
        List<List<Integer>> ans = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (reachAtlantic[i][j] == 1 && reachPacific[i][j] == 1) {
                    ans.add(Arrays.asList(i, j));
                }
            }
        }
        return ans;
    }

    private void pacificAtlanticDfs(int[][] heights, int[][] directions, int[][] reach, int currentRow, int currentCol) {
        if (reach[currentRow][currentCol] == 1) {
            return;
        }
        reach[currentRow][currentCol] = 1; //高位

        for (int[] dir : directions) {
            int nextRow = currentRow + dir[0];
            int nextCol = currentCol + dir[1];
            if (nextRow < 0 || nextRow >= heights.length || nextCol < 0 || nextCol >= heights[0].length) {
                continue;
            }
            if (heights[currentRow][currentCol] <= heights[nextRow][nextCol]) {
                pacificAtlanticDfs(heights, directions, reach, nextRow, nextCol);
            }
        }
    }


    int currentArea = 0;

    /**
     * 827. 最大人工岛
     */
    public int largestIsland(int[][] grid) {
        int maxLandArea = 0;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int isLandNums = 2;
        boolean isAllIsLand = true;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    currentArea = 0;
                    largestIslandDfs(grid, directions, isLandNums, i, j);  //感染编号、累加面积
                    hTable.put(isLandNums, currentArea);
                    isLandNums++;
                } else if (grid[i][j] == 0) {
                    isAllIsLand = false;
                }
            }
        }
        if (isAllIsLand) return grid.length * grid.length;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == 0) {
                    int isLandArea = 1;  //连通块自身
                    HashSet<Integer> uniqueIsLand = new HashSet<>();
                    for (int[] dir : directions) {
                        int nextRow = i + dir[0];
                        int nextCol = j + dir[1];
                        if (nextRow < 0 || nextRow >= grid.length || nextCol < 0 || nextCol >= grid[0].length) {
                            continue;
                        }
                        int isLandFlag = grid[nextRow][nextCol];
                        if (isLandFlag != 0 && !uniqueIsLand.contains(isLandFlag)) {
                            isLandArea += hTable.get(isLandFlag);
                            uniqueIsLand.add(isLandFlag);
                        }
                    }
                    maxLandArea = Math.max(maxLandArea, isLandArea);
                }
            }
        }
        return maxLandArea;
    }

    private void largestIslandDfs(int[][] grid, int[][] directions, int isLandNums, int currentRow, int currentCol) {
        if (currentRow < 0 || currentRow >= grid.length || currentCol < 0 || currentCol >= grid[0].length) {
            return;
        }
        if (grid[currentRow][currentCol] == 0) {
            return;
        }
        if (grid[currentRow][currentCol] == isLandNums) {
            return;
        }

        currentArea++;
        grid[currentRow][currentCol] = isLandNums;  //感染
        for (int[] dir : directions) {
            int nextRow = currentRow + dir[0];
            int nextCol = currentCol + dir[1];
            largestIslandDfs(grid, directions, isLandNums, nextRow, nextCol);
        }
    }


    /**
     * 79. 单词搜索
     */
    public boolean exist(char[][] board, String word) {
        int m = board.length;
        int n = board[0].length;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int[][] used = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == word.charAt(0)) {
                    if (existDfs(board, word, directions, used, i, j, 0)) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    private boolean existDfs(char[][] board, String word, int[][] directions, int[][] used, int currentRow, int currentCol, int currentDepth) {
        currentDepth++;
        if (currentDepth == word.length()) {
            return true;
        }

        used[currentRow][currentCol] = 1;
        for (int[] dir : directions) {
            int nextRow = currentRow + dir[0];
            int nextCol = currentCol + dir[1];
            if (nextRow < 0 || nextRow >= board.length || nextCol < 0 || nextCol >= board[0].length) {
                continue;
            }
            if (used[nextRow][nextCol] == 1) {
                continue;
            }
            if (board[nextRow][nextCol] == word.charAt(currentDepth)) {
                if (existDfs(board, word, directions, used, nextRow, nextCol, currentDepth)) {
                    return true;
                }
            }
        }
        used[currentRow][currentCol] = 0;

        return false;
    }


    /**
     * 1773. 统计匹配检索规则的物品数量
     */
    public int countMatches(List<List<String>> items, String ruleKey, String ruleValue) {
        if (ruleKey.equals("type")) {
            return countMatchesHelper(items, 0, ruleValue);
        }
        if (ruleKey.equals("color")) {
            return countMatchesHelper(items, 1, ruleValue);
        }
        if (ruleKey.equals("name")) {
            return countMatchesHelper(items, 2, ruleValue);
        }
        return 0;
    }

    private int countMatchesHelper(List<List<String>> items, int index, String ruleValue) {
        int ans = 0;
        for (List<String> item : items) {
            if (item.get(index).equals(ruleValue)) ans++;
        }
        return ans;
    }


    /**
     * 90. 子集 II
     */

    public List<List<Integer>> subsetsWithDup(int[] nums) {   //给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的子集（幂集）
        Arrays.sort(nums);
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        ans.add(path);
        subsetsWithDupDfs(nums, ans, path, 0);
        return ans;
    }

    private void subsetsWithDupDfs(int[] nums, List<List<Integer>> ans, LinkedList<Integer> path, int currentIndex) {
        //递归终止条件
        if (currentIndex >= nums.length) {
            return;
        }

        for (int i = currentIndex; i < nums.length; i++) {
            //横向剪枝
            if (i > currentIndex && nums[i] == nums[i - 1]) {
                continue;
            }

            //1、添加元素
            path.add(nums[i]);
            ans.add(new ArrayList<>(path));

            //2、纵向递归搜索
            subsetsWithDupDfs(nums, ans, path, i + 1);  //纵向不使用同一个位点

            //3、移除元素
            path.removeLast();
        }
    }


    public List<List<Integer>> permuteUnique(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        int[] used = new int[nums.length];
        permuteUniqueDfs(nums, ans, path, used, 0);
        return ans;
    }

    private void permuteUniqueDfs(int[] nums, List<List<Integer>> ans, LinkedList<Integer> path, int[] used, int currentIndex) {
        if (currentIndex == nums.length) {
            ans.add(new ArrayList<>(path));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (used[i] == 1) {
                continue;
            }
            if (i > 0 && nums[i] == nums[i - 1] && used[i - 1] == 1) {
                continue;
            }

            //1、增加元素
            used[i] = 1;
            path.add(nums[i]);

            permuteUniqueDfs(nums, ans, path, used, i + 1);

            //2、移除元素
            used[i] = 0;
            path.removeLast();
        }
    }


    /**
     * 207. 课程表
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        //入度
        int[] inDegree = new int[numCourses];
        //邻接表，叶节点指向叶子节点
        HashMap<Integer, ArrayList<Integer>> adjacent = new HashMap<>();
        //初始化入度和邻接表
        for (int[] relate : prerequisites) {
            int childNode = relate[0];
            int fatherNode = relate[1];
            if (!adjacent.containsKey(fatherNode)) {
                adjacent.put(fatherNode, new ArrayList<>());
            }
            adjacent.get(fatherNode).add(childNode);  //叶节点指向叶子节点
            inDegree[childNode]++;  //入度，记录当前节点依赖的节点数
        }
        //记录无前置依赖项的课程，开始学习
        ArrayDeque<Integer> learnedQueue = new ArrayDeque<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) learnedQueue.add(i);
        }
        //学习课程，并动态更新可以开始的课程，周而复始的学习
        while (!learnedQueue.isEmpty()) {
            Integer learnNode = learnedQueue.pollFirst();
            //如果当前课程不是其他课程的前置课程，则直接跳过
            if (!adjacent.containsKey(learnNode)) {
                continue;
            }
            //当前课程作为其他课程的前置课程，学习后，更新其子节点的入度
            ArrayList<Integer> affectNodes = adjacent.get(learnNode);
            for (int affect : affectNodes) {
                inDegree[affect]--;
                if (inDegree[affect] == 0) {  //此课程再无其他前置课程，将其添加到可学习的队列中
                    learnedQueue.addLast(affect);
                }
            }
        }
        //所有学习的课程学习完毕，查看是否有入度不为 0 的课程，如果有则构成闭环
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] != 0) return false;
        }
        //所有课程学习完毕
        return true;
    }


    /**
     * 802. 找到最终的安全状态
     */
    public List<Integer> eventualSafeNodes(int[][] graph) {   //基于反向图来解决
        List<Integer> ans = new ArrayList<>();
        int n = graph.length;
        //入度
        int[] inDegree = new int[n];
        //邻接表
        HashMap<Integer, ArrayList<Integer>> adjacent = new HashMap<>();
        //初始化邻接表
        for (int i = 0; i < n; i++) {  //反向图
            inDegree[i] = graph[i].length;
            for (int j = 0; j < graph[i].length; j++) {
                if (!adjacent.containsKey(graph[i][j])) {
                    adjacent.put(graph[i][j], new ArrayList<>());
                }
                adjacent.get(graph[i][j]).add(i);
            }
        }
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) arrayDeque.add(i);  //终端节点
        }
        while (!arrayDeque.isEmpty()) {
            Integer fatherNode = arrayDeque.poll();
            if (!adjacent.containsKey(fatherNode)) {
                continue;
            }
            ArrayList<Integer> affectNodes = adjacent.get(fatherNode);
            for (int aff : affectNodes) {
                inDegree[aff]--;
                if (inDegree[aff] == 0) arrayDeque.add(aff);
            }
        }
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) ans.add(i);
        }
        return ans;
    }


    /**
     * 802. 找到最终的安全状态
     */
    public List<Integer> eventualSafeNodes01(int[][] graph) {   //基于反向图来解决
        int n = graph.length;
        List<Integer> ans = new ArrayList<>();
        int[] visited = new int[n];
        for (int i = 0; i < n; i++) {
            if (eventualSafeNodesDfs(graph, visited, i)) {
                ans.add(i);
            }
        }
        return ans;
    }

    private boolean eventualSafeNodesDfs(int[][] graph, int[] visited, int currentNode) {  //目的为寻找闭环
        //迭代终止条件一：闭环
        if (visited[currentNode] == 1) {
            //--------------------------------
            // 1、此节点可能是从上个非安全节点遗留的，即非安全节点对应的闭环路径中的节点，均为非安全节点
            // 2、此节点可能是从单个节点搜索路径中，途径过同时仍处于搜索中的节点
            //--------------------------------
            return false;
        }

        visited[currentNode] = 1;  //正在搜索

        for (int nextNode : graph[currentNode]) {  //目的为寻找闭环
            //剪枝一：终端节点
            if (graph[nextNode].length == 0) {
                continue;
            }
            //剪枝二：安全节点
            if (visited[nextNode] == 2) {
                continue;
            }

            //寻找闭环
            if (!eventualSafeNodesDfs(graph, visited, nextNode)) {
                return false;
            }

        }

        visited[currentNode] = 2;  //当前节点搜索完毕，为安全节点
        return true;
    }


    /**
     * 851. 喧闹和富有
     */
    public int[] loudAndRich(int[][] richer, int[] quiet) {
        int n = quiet.length;
        //入度
        int[] inDegree = new int[n];
        //邻接表
        HashMap<Integer, ArrayList<Integer>> adjacent = new HashMap<>();
        //初始化入度和邻接表
        for (int[] relate : richer) {
            int fatherNode = relate[0];
            int childNode = relate[1];
            if (!adjacent.containsKey(fatherNode)) {
                adjacent.put(fatherNode, new ArrayList<>());
            }
            adjacent.get(fatherNode).add(childNode);
            inDegree[childNode]++;
        }
        int[] ans = new int[n];
        //搜索节点
        ArrayDeque<Integer> arrayQueue = new ArrayDeque<>();
        //初始化搜索节点和安静节点
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) arrayQueue.add(i);
            ans[i] = i;  //每个节点的安静节点为自身
        }
        while (!arrayQueue.isEmpty()) {
            Integer fatherNode = arrayQueue.pollFirst();
            if (!adjacent.containsKey(fatherNode)) {
                continue;
            }
            ArrayList<Integer> affectNodes = adjacent.get(fatherNode);
            for (int affect : affectNodes) {
                //1、子节点的安静节点一定是安静值最小的父节点
                if (quiet[ans[fatherNode]] < quiet[ans[affect]]) { //从当前节点的邻接父节点和非邻接父节点（父节点的父节点，体现全局邻接）中，搜索安静值最小的节点
                    ans[affect] = ans[fatherNode];
                }

                //2、当前节点的父节点数减少
                inDegree[affect]--;
                if (inDegree[affect] == 0) arrayQueue.addLast(affect);
            }
        }
        return ans;
    }


    /**
     * 1971. 寻找图中是否存在路径
     */
    public boolean validPath(int n, int[][] edges, int source, int destination) {
        int[] visited = new int[n];
        //邻接表
        List<List<Integer>> adjacent = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjacent.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            adjacent.get(node1).add(node2);
            adjacent.get(node2).add(node1);
        }
        return validPathDfs(edges, adjacent, visited, source, destination);
    }

    private boolean validPathDfs(int[][] edges, List<List<Integer>> adjacent, int[] visited, int currentNode, int targetNode) {
        //递归终止条件一；找到终点
        if (currentNode == targetNode) {
            return true;
        }

        visited[currentNode] = 1; //标记为正在搜索
        List<Integer> affectNodes = adjacent.get(currentNode);
        for (int nextNode : affectNodes) {   //横向枚举搜索
            //剪枝一：已搜索
            if (visited[nextNode] == 2) {    //已完成搜索的节点，但未找到终点，跳过
                continue;
            }
            //剪枝二：正在搜索
            if (visited[nextNode] == 1) {    //跳过
                continue;
            }

            if (validPathDfs(edges, adjacent, visited, nextNode, targetNode)) {
                return true;
            }
        }
        //搜索完成，未找到终点
        visited[currentNode] = 2;  //三色标记，记忆化搜索
        return false;
    }


    /**
     * 122. 买卖股票的最佳时机 II
     */
    public int maxProfit(int[] prices) {
        int n = prices.length;
        int maxProfit = 0;
        for (int i = 1; i < n; i++) {
            int profit = prices[i] - prices[i - 1];
            maxProfit += Math.max(profit, 0);
        }
        return maxProfit;
    }


    /**
     * 123. 买卖股票的最佳时机 III
     */
    public int maxProfitIII(int[] prices) {   //基于上面动态规划的思路，最多可以完成 两笔 交易
        int n = prices.length;
        int k = 2;
        int[][][] dp = new int[n][k + 1][2];
        for (int i = 0; i <= k; i++) {        //初始化 day = 0 的情况
            dp[0][i][0] = 0;
            dp[0][i][1] = -prices[0];
        }
        for (int day = 1; day < n; day++) {   //初始化 K = 0 的情况，即尚未完成一次买卖的情况
            dp[day][0][0] = 0;
            dp[day][0][1] = Math.max(dp[day - 1][0][1], dp[day - 1][0][0] - prices[day]);
        }
        for (int d = 1; d < n; d++) {
            for (int m = 1; m <= k; m++) {
                dp[d][m][0] = Math.max(dp[d - 1][m][0], dp[d - 1][m - 1][1] + prices[d]);
                dp[d][m][1] = Math.max(dp[d - 1][m][1], dp[d - 1][m][0] - prices[d]);
            }
        }
        return dp[n - 1][k][0];
    }


    public int maxProfitIII01(int[] prices) {   //基于上面动态规划的思路，最多可以完成 两笔 交易
        int n = prices.length;
        int buyDay1 = -prices[0];
        int sellDay1 = 0;
        int buyDay2 = -prices[0];
        int sellDay2 = 0;
        for (int i = 1; i < n; i++) {
            buyDay1 = Math.max(buyDay1, -prices[i]);
            sellDay1 = Math.max(sellDay1, buyDay1 + prices[i]);
            buyDay2 = Math.max(buyDay2, sellDay1 - prices[i]);
            sellDay2 = Math.max(sellDay2, buyDay2 + prices[i]);
        }
        return sellDay2;
    }


    public int maxProfitIII02(int[] prices) {   //基于上面动态规划的思路，最多可以完成 两笔 交易
        int n = prices.length;
        int[] before = new int[n];
        int minPrice = prices[0];
        for (int i = 1; i < n; i++) {
            if (prices[i] > minPrice) {
                before[i] = Math.max(before[i - 1], prices[i] - minPrice);
            } else {
                minPrice = prices[i];
                before[i] = before[i - 1];
            }
        }
        int[] after = new int[n];
        int maxPrice = prices[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            if (prices[i] < maxPrice) {
                after[i] = Math.max(after[i + 1], maxPrice - prices[i]);
            } else {
                after[i] = after[i + 1];
                maxPrice = prices[i];
            }
        }
        int maxProfit = 0;
        for (int i = 0; i < n; i++) {
            maxProfit = Math.max(maxProfit, before[i] + after[i]);
        }
        return maxProfit;
    }

}
