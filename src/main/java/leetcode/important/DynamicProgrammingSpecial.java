package leetcode.important;

import java.util.*;

public class DynamicProgrammingSpecial {


    /**
     * 940. 不同的子序列 II
     * 计算 s 的 不同非空子序列 的个数
     */
    public int distinctSubseqII(String str) {
        int n = str.length();
        int mod = (int) Math.pow(10, 9) + 7;
        int[][] dp = new int[n + 1][26];
        for (int i = 1; i <= n; i++) {       //1、先遍历物品
            int currentEnd = str.charAt(i - 1) - 'a';
            for (int j = 0; j < 26; j++) {   //2、后遍历背包
                if (j != currentEnd) {
                    dp[i][j] = dp[i - 1][j];   //状态转移上一层
                } else {                       //状态更新，针对以当前字符结尾的情况
                    //--------------------------------------------------------------------------
                    // 无论当前字符之前是否出现过，increase == 1 都认为是当前字符都成一个子序列的情况；
                    //     如果当前字符第二次出现，则会在在拼接当前字符后构成新的字符串，通过遍历上一个字符的结果从而得到累加
                    //     然而，针对"当前字符单独构成一个子序列的情况"，有此字符首次出现，转移到 当前
                    //--------------------------------------------------------------------------
                    int increase = 1;
                    for (int k = 0; k < 26; k++) {
                        increase = (increase + dp[i - 1][k]) % mod;
                    }
                    dp[i][j] = increase;
                }
            }
        }
        int ans = 0;
        //-------------------------------------------------
        // 注意每一层仅仅计算"以当前字符结尾的子序列的个数"，但同时会对非当前层的字符的状态进行转移，转移至当前层
        //-------------------------------------------------
        for (int j = 0; j < 26; j++) {
            ans = (ans + dp[n][j]) % mod;
        }
        return ans;
    }

    public int distinctSubseqII01(String str) {
        int n = str.length();
        int mod = (int) Math.pow(10, 9) + 7;
        long[] dp = new long[26];
        for (int i = 0; i < str.length(); i++) {
            long sumPlans = 0;
            for (int k = 0; k < 26; k++) {
                sumPlans = (sumPlans + dp[k]) % mod;
            }
            //因为可以把26个字母结尾的子序列的末尾加上该字符，就能得到不同的序列了
            //但是这样每个子序列的长度都是大于1的，然后再加上这个字符本身
            dp[str.charAt(i) - 'a'] = sumPlans + 1;
        }
        long ans = 0;
        for (int i = 0; i < 26; i++) {
            ans = (ans + dp[i]) % mod;
        }
        return (int) ans;
    }


    /**
     * 115. 不同的子序列
     */
    public int numDistinct(String s, String t) {
        int m = s.length();
        int n = t.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            dp[i][0] = 1;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = dp[i - 1][j];
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[i][j] += dp[i - 1][j - 1];
                }
            }
        }
        return dp[m][n];
    }

    public int numDistinct01(String s, String t) {
        int m = s.length();
        int n = t.length();
        s = " " + s;
        t = " " + t;
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            dp[i][0] = 1;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = dp[i - 1][j];
                if (s.charAt(i) == t.charAt(j)) {
                    dp[i][j] += dp[i - 1][j - 1];
                }
            }
        }
        return dp[m][n];
    }

    //------------------------------------------------------------
    // 多维背包
    //------------------------------------------------------------

    /**
     * 1995. 统计特殊四元组
     */
    public int countQuadruplets(int[] nums) {
        //--------------------------------------------------------------
        // 限制组合个数的维度有两个，均为「恰好」限制，转换为「二维费用背包问题求方案数」问题。
        // 状态定义：
        //     dp[i][j][k] 表示在前 i 个数字中，恰好使用 j 个数字，其和为数字 K（nums[i]） 的方案数， 不过 K 具有连续性
        // 最终结果：
        //     dp[i][3][nums[i]] 的累加和,其中  3 <= i <= nums.length - 1
        // 初始化：
        //     dp[0][0][0] = 1 代表不考虑任何物品时，凑成数值为 0 的方案数为 1
        // 状态转移：
        //     根据 nums[i - 1] 是否参与组合，可分为两种情况：
        //        1、不参与组合 dp[i - 1][j][k]
        //        2、 参与组合 dp[i - 1][j - nums[i - 1]][k]
        //--------------------------------------------------------------

        int n = nums.length;
        int max = 0;   //背包最大容量
        for (int num : nums) {
            max = Math.max(max, num);
        }
        int[][][] dp = new int[n + 1][4][max + 1];
        dp[0][0][0] = 1;
        for (int i = 1; i <= nums.length; i++) {
            for (int j = 0; j <= 3; j++) {
                for (int k = 0; k <= max; k++) {   //遍历背包
                    dp[i][j][k] = dp[i - 1][j][k];
                    if (j >= 1 && k >= nums[i - 1]) dp[i][j][k] += dp[i - 1][j - 1][k - nums[i - 1]];
                }
            }
        }
        int ans = 0;
        for (int i = 3; i < nums.length; i++) {
            ans += dp[i][3][nums[i]];
        }
        return ans;
    }

    public int countQuadruplets01(int[] nums) {
        int ans = 0;
        for (int i = 0; i < nums.length - 3; i++) {
            for (int j = i + 1; j < nums.length - 2; j++) {
                for (int m = j + 1; m < nums.length - 1; m++) {
                    for (int n = m + 1; n < nums.length; n++) {
                        if (nums[i] + nums[j] + nums[m] == nums[n]) ans++;
                    }
                }
            }
        }
        return ans;
    }


    /**
     * 377. 组合总和 Ⅳ
     */
    public int combinationSum4(int[] nums, int target) {  //类似爬楼梯的问题，是一种排列问题
        int[] dp = new int[target + 1];
        dp[0] = 1;
        for (int i = 1; i <= target; i++) {   //枚举背包
            for (int num : nums) {  //枚举物品
                if (i >= num) dp[i] += dp[i - num];
            }
        }
        return dp[target];
    }


    public int combinationSum4IV(int[] nums, int target) {
        int len = target;
        int[][] f = new int[len + 1][target + 1];
        f[0][0] = 1;
        int ans = 0;
        for (int i = 1; i <= len; i++) {
            for (int j = 0; j <= target; j++) {
                for (int u : nums) {
                    if (j >= u) f[i][j] += f[i - 1][j - u];
                }
            }
            ans += f[i][target];
        }
        return ans;
    }


    public int combinationSum401(int[] nums, int target) {  //错误写法，想将爬楼梯的问题，转换为二维写法，但下面为错误写法
        int[][] dp = new int[nums.length + 1][target + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= nums.length; i++) {
            dp[i][0] = 1;
        }
        for (int j = 1; j <= target; j++) {
            for (int i = 1; i <= nums.length; i++) {
                if (j < nums[i - 1])
                    dp[i][j] = dp[i - 1][j];
                else
                    dp[i][j] = dp[i - 1][j] + dp[i][j - nums[i - 1]];
            }
        }
        return dp[nums.length][target];
    }



    //------------------------------------------------------------------------------
    // 以下三个题目类似于爬楼梯的问题，差异在于 爬楼梯是基于相邻的位置进行状态转移，而下面是基于 特定条件下的状态转移
    //------------------------------------------------------------------------------


    /**
     * 1235. 规划兼职工作
     */
    public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {  //动态规划 + 二分搜索
        int n = startTime.length;
        int[] dp = new int[n];    //dp[i] 表示在不冲突的情况下，前 i 个（含）工作可获得最大利润
        int[][] jobs = new int[n][3];
        for (int i = 0; i < n; i++) {
            jobs[i] = new int[]{startTime[i], endTime[i], profit[i]};
        }
        Arrays.sort(jobs, (o1, o2) -> o1[1] - o2[1]);  //以结束时间升序排序，用于二分查找
        dp[0] = jobs[0][2];
        for (int i = 1; i < n; i++) {
            int prevIndex = jobSearch(jobs, jobs[i][0]);  //基于当前会议的开始时间，进行二分查找，找到上一个不冲突的工作
            dp[i] = Math.max(dp[i - 1], (prevIndex == -1 ? 0 : dp[prevIndex]) + jobs[i][2]);  //如果没有上一个可继承的工作，则前置状态为 0
        }
        return dp[n - 1];
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------
    // 二分搜索：以当前的 startTime 作为目标值，在所有的工作中找到上一个不冲突的工作的#最大索引#，因为可能多个工作的结束时间一致，由于状态的转移，因此返回最大的索引
    //------------------------------------------------------------------------------------------------------------------------------------------------
    private int jobSearch(int[][] jobs, int startTime) {  //找到 endTime <= startTime 的最大索引
        int left = 0;
        int right = jobs.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (jobs[mid][1] <= startTime) {
                left = mid + 1;     //最终落在 target 右侧
            } else if (startTime < jobs[mid][1]) {
                right = mid - 1;    //最终落在 target 左侧或者 target 重复的情况下落在索引最大的位置
            }
        }
        return right;  //可能为 -1
    }

    //-------------------------------------------------------------------
    // 上下两个题最大的差异在于会议重叠的定义不同
    //    1、上面会议 A结束时间 == 会议 B开始时间，意味着二者均可选，会议 B 的状态可基于会议 A来进行状态转移
    //    2、下面会议 A结束时间 == 会议 B开始时间，意味着会议重叠二者不可都选，会议 B 的状态不可基于 会议 A 来进行状态转移
    //-------------------------------------------------------------------


    /**
     * 1751. 最多可以参加的会议数目 II
     */
    public int maxValue(int[][] events, int k) {
        int n = events.length;
        Arrays.sort(events, (o1, o2) -> o1[1] - o2[1]);
        int[][] dp = new int[n][k + 1];    //dp[i][j]状态定义：前 i 个会议中，最多选取 j 个不冲突的会议，所获取的最大价值
        dp[0][0] = 0;
        for (int j = 1; j <= k; j++) {
            dp[0][j] = events[0][2];  //只能选择一个会议，即"索引"为 0 的会议
        }
        for (int i = 1; i < n; i++) {
            //1、如果选择当前会议，则需要贪心的找到此会议的前置会议
            int prevIndex = maxValueSearch(events, events[i][0]);  //距离最近且不冲突

            //2、当前会议选择与否的影响
            for (int j = 1; j <= k; j++) {
                dp[i][j] = Math.max(dp[i - 1][j], (prevIndex == -1 ? 0 : dp[prevIndex][j - 1]) + events[i][2]);
            }
        }
        return dp[n - 1][k];
    }

    private int maxValueSearch(int[][] events, int startTime) {
        int left = 0;
        int right = events.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (events[mid][1] < startTime) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        //寻找 endTime < startTime 的最大索引
        //在 target不存在的情况下，right处于小于 target 的最大索引位置
        //在 target存在或重复的情况下，位于最左侧 target的左侧一位，因此满足 right 处于小于 target 的最大索引位置
        return right;
    }

    /**
     * 2008. 出租车的最大盈利
     */
    public long maxTaxiEarnings(int n, int[][] rides) {
        //计算纯利润
        for (int[] arr : rides) {
            arr[2] += (arr[1] - arr[0]);
        }
        //基于订单终点升序排序
        Arrays.sort(rides, (o1, o2) -> o1[1] - o2[1]);
        int m = rides.length;
        long[] dp = new long[m];
        dp[0] = rides[0][2];
        for (int i = 1; i < m; i++) {
            //1、前置行程
            int prevIndex = maxTaxiEarningsSearch(rides, rides[i][0]);

            //2、当前行程选择与否
            dp[i] = Math.max(dp[i - 1], (prevIndex == -1 ? 0 : dp[prevIndex]) + rides[i][2]);
        }
        return dp[m - 1];
    }

    private int maxTaxiEarningsSearch(int[][] rides, int startAddress) {  //找到 endAddress <= startAddress 的最大索引
        int left = 0;
        int right = rides.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (rides[mid][1] <= startAddress) {
                left = mid + 1;
            } else {
                right = mid - 1;  //target不存在时，位于 target左侧、target存在且重复时，位于最右侧 target处
            }
        }
        return right;
    }

    //---------------------------------------------------------------------------
    // 字符串相关的动态规划
    //---------------------------------------------------------------------------


    /**
     * 926. 将字符串翻转到单调递增
     */
    public int minFlipsMonoIncr(String s) {
        int n = s.length();
        // dp[i][0]表示前 i个元素，最后一个元素为 0的最小翻转次数；
        // dp[i][1]表示前 i个元素，最后一个元素为 1的最小翻转次数
        int[][] dp = new int[n][2];
        //状态初始化
        dp[0][0] = s.charAt(0) == '0' ? 0 : 1;  //注意：本位是 0 就不需要反转，故 0
        dp[0][1] = s.charAt(0) == '1' ? 0 : 1;
        for (int i = 1; i < s.length(); i++) {
            //-----------------------------------------------------------------------------------
            // 状态转移方程
            //    1、如果前 i 个元素最后以 0 结尾且满足单调递增，那么前 i 个元素必须均为 0，因此 dp[i][0]的状态转移方程为：
            //       dp[i][0] = dp[i - 1][0] + s.charAt(i) == 0 ? 0 : 1
            //    2、如果前 i 个元素最后以 1 结尾且满足单调递增，那么前 i 个元素可以为 0或 1，因此 dp[i][1]的状态转移方程为：
            //       dp[i][0] = Math.min(dp[i - 1][0] , dp[i - 1][1]) + s.charAt(i) == 1 ? 0 : 1
            //-----------------------------------------------------------------------------------
            dp[i][0] = dp[i - 1][0] + (s.charAt(i) == '0' ? 0 : 1);   // 0 : 1 是 0 就无需翻转，不是 0 就需要反转，故增加一次翻转次数
            dp[i][1] = Math.min(dp[i - 1][0], (dp[i - 1][1])) + (s.charAt(i) == '1' ? 0 : 1);  //一定有括号
        }
        return Math.min(dp[n - 1][0], dp[n - 1][1]);
    }

    public int minFlipsMonoIncr01(String str) {
        int n = str.length();
        str = " " + str;
        int[][] dp = new int[n + 1][2];
        for (int i = 1; i <= n; i++) {
            char xx = str.charAt(i);
            dp[i][0] = dp[i - 1][0] + (xx == '0' ? 0 : 1);
            dp[i][1] = Math.min(dp[i - 1][0], dp[i - 1][1]) + (xx == '0' ? 1 : 0);
        }
        return Math.min(dp[n][0], dp[n][1]);
    }

    public int minFlipsMonoIncr00(String str) {  //错误写法
        int n = str.length();
        str = " " + str;
        int[][] dp = new int[n + 1][2];
        for (int i = 1; i <= n; i++) {
            char xx = str.charAt(i);
            //错误原因：每一位都要计算当前位置是 0 或 1 的情况，而非仅计算当前位是自身的情况
            //因为后面计算的时候，应当考虑的现在这一位是否要反转，如果仅计算自身，那肯定就不用反转
            if (xx == '0') dp[i][0] = dp[i - 1][0];
            else dp[i][1] = Math.min(dp[i - 1][0], dp[i - 1][1]);
        }
        return Math.min(dp[n][0], dp[n][1]);
    }

    //#遍历字符串，找到一个分界点，使得该分界点之前1的个数和分界点之后0的个数之和最小，把分界点之前的1变成0，之后的0变成1
    public int minFlipsMonoIncr20(String str) {   //基于前缀和的思想
        int n = str.length();
        //------------------------------------------------------------------------------
        // left_1 代表当前位置（包含，因为是加法）及左侧位置中 1 的个数，right_0 代表当前位置（不含，因为是减法）右侧位置中 0 的个数
        //------------------------------------------------------------------------------
        int left = 0;
        int right = (int) Arrays.stream(str.split("")).mapToInt(Integer::valueOf).filter(o -> o == 0).count();
        //默认值
        int ans = Math.min(right, n - right);  //将 0 全部翻转为 1 或者 将 1 全部翻转为 0两种情况中，最小的翻转次数
        for (int i = 0; i < n; i++) {
            if (str.charAt(i) == '0')
                right--;
            if (str.charAt(i) == '1')
                left++;

            //每个位置均计算，将此位置左侧的 1 全部翻转为 0 && 将右侧 0 全部翻转为 1 的次数，并各个位置比较其最小值
            ans = Math.min(ans, left + right);
        }
        return ans;
    }

    public int minFlipsMonoIncr21(String str) {
        int left = 0;
        int right = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '0') right++;
        }
        int ans = left + right;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '1')
                left++;
            else
                right--;
            ans = Math.min(ans, left + right);
        }
        return ans;
    }


    /**
     * 5. 最长回文子串
     */
    public String longestPalindrome(String str) {   //动态规划
        int len = str.length();
        if (len <= 1) return str;
        String ans = String.valueOf(str.charAt(0));  //关键：初始长度为 1，且单个字符一定是回文串
        // dp[i][j] 表示 s[i,j] 是否为回文串
        boolean[][] dp = new boolean[len][len];
        //初始化：所有长度为 1 的字串都是回文串
        for (int i = 0; i < len; i++) {    // 1、长度为 1 的字串
            dp[i][i] = true;
        }
        //开始递推，依次枚举字串长度
        for (int window = 2; window <= str.length(); window++) {  //依次枚举字串长度，进而判断其是否为回文串，注意有等号
            for (int i = 0; i < str.length(); i++) {  //枚举字串的左边界
                int j = window + i - 1;  //对应右侧边界
                if (j >= str.length()) { //保证右侧边界在str中
                    break;
                }

                if (str.charAt(i) != str.charAt(j)) {
                    dp[i][j] = false;
                } else {
                    if (j - i + 1 == 2) { // 2、长度为 2 的字串，两边字符还相等
                        dp[i][j] = true;
                    } else {              // 3、长度为 3 的字串
                        dp[i][j] = dp[i + 1][j - 1];  //动态规划，优化时间复杂度的关键
                        //向内取，因为会依次判断长度为 1、2、3..字串是否为回文串，减少重复遍历的次数，优化时间复杂度
                    }
                }

                //获取最长回文串
                if (dp[i][j] && j - i + 1 > ans.length()) {
                    ans = str.substring(i, j + 1);
                }
            }
        }
        return ans;
    }

    public String longestPalindrome00(String str) {   //暴力解法，枚举所有字串，然后判断是否是回文串
        int n = str.length();
        if (n == 1) return str;
        char[] arr = str.toCharArray();
        String ans = String.valueOf(arr[0]);  //初始化
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (j - i + 1 > ans.length() && isPalindrome(arr, i, j)) {
                    ans = str.substring(i, j + 1);
                }
            }
        }
        return ans;
    }

    private boolean isPalindrome(char[] arr, int left, int right) {
        while (left < right) {
            if (arr[left] != arr[right]) return false;
            left++;
            right--;
        }
        return true;
    }

    public String longestPalindrome01(String str) {  //中心扩展法，思路更清晰，时间复杂度比动态规划更低
        String ans = "";
        for (int i = 0; i < str.length(); i++) {
            //扫描，当前点两侧的具有与当前点相同值的位点
            int left = i - 1;
            int right = i + 1;
            while (left >= 0 && str.charAt(left) == str.charAt(i)) {
                left--;
            }
            while (right < str.length() && str.charAt(right) == str.charAt(i)) {
                right++;
            }
            //搜索回文串
            while (left >= 0 && right < str.length() && str.charAt(left) == str.charAt(right)) {
                left--;
                right++;
            }

            //最长回文串
            if (right - 1 - (left + 1) + 1 > ans.length()) {
                ans = str.substring(left + 1, right - 1 + 1);
            }
        }
        return ans;
    }

    /**
     * 221. 最大正方形
     */
    public int maximalSquare(char[][] matrix) {
        int maxEdge = 0;
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] dp = new int[rows][cols];   //dp[i][j]代表以点（i,j）为右下角的正方形的最大边长
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == 0 || j == 0) {
                    dp[i][j] = matrix[i][j] == '1' ? 1 : 0;
                } else if (matrix[i][j] == '0') {
                    dp[i][j] = 0;
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
                maxEdge = Math.max(maxEdge, dp[i][j]);
            }
        }
        return maxEdge * maxEdge;
    }

    /**
     * 1277. 统计全为 1 的正方形子矩阵
     */
    public int countSquares(int[][] matrix) {
        int ans = 0;
        //dp[i][j]记录以(i,j)为右下角的正方形的边长
        int[][] dp = new int[matrix.length][matrix[0].length];  //动态规划，记忆搜索
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (i == 0 || j == 0) {           //1、左边界、上边界
                    dp[i][j] = matrix[i][j];              //只需考虑自身，因为上边界和左边界上的点，不会与左侧、上侧、左上侧构成正方形
                } else if (matrix[i][j] == 0) {   //2、位于内部的点，自身值为 0
                    dp[i][j] = 0;                         //不会有以此点为右下角的正方形
                } else {                          //3、位于内部的点，自身值为 1
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;  //必须有左上方的dp值
                    //动态规划：取左上方相邻三个点对应的 dp值的最小值（因为要构成正方形），+ 1 是自身
                    //注意：
                    //dp[i][j]可以理解为以(i,j)为右下角的正方形的边长，其取决与左、左上、上，三个点上所能构成的正方形边长的最小值
                    //由于大正方形是由小正方形构成的，因此，斜对角上的小正方形数和边长上的正方形的个数是一致的
                }
                ans += dp[i][j];  //以 [i,j] 为右下角的正方形的个数，其实等于边长
            }
        }
        return ans;
    }



    /**
     * 53. 最大子数组和
     */
    public int maxSubArray(int[] nums) {  //动态规划
        int n = nums.length;
        int[] dp = new int[n];    //状态定义：以 nums[i] 结尾的连续子序列的最大和
        dp[0] = nums[0];
        for (int i = 1; i < n; i++) {
            if (dp[i - 1] > 0) {
                dp[i] = dp[i - 1] + nums[i];
            } else {
                dp[i] = nums[i];
            }
        }
        int ans = dp[0];
        for (int i = 1; i < n; i++) {
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }

    public int maxSubArray10(int[] nums) {  //动态规划
        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];
        int ans = nums[0];
        for (int i = 1; i < n; i++) {
            dp[i] = Math.max(nums[i], dp[i - 1] + nums[i]);
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }

    public int maxSubArray01(int[] nums) {  //官方动态规划
        int pre = 0;
        int res = nums[0];
        for (int num : nums) {
            pre = Math.max(pre + num, num);
            res = Math.max(res, pre);
        }
        return res;
    }

    public int maxSubArray02(int[] nums) {  //前缀和，超时
        int[] prefixSum = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        int res = Integer.MIN_VALUE;
        for (int i = 1; i < prefixSum.length; i++) {
            for (int j = i + 1; j < prefixSum.length; j++) {
                res = Math.max(res, prefixSum[j] - prefixSum[i]);
            }
        }
        return res;
    }

    public int maxSubArray20(int[] nums) {  //暴力超时
        int sum = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            int temp = 0;
            for (int j = i; j < nums.length; j++) {
                temp += nums[j];
                sum = Math.max(sum, temp);
            }
        }
        return sum;
    }

    public int maxSubArray21(int[] nums) {
        int res = Integer.MIN_VALUE;
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            res = Math.max(res, sum);
            sum = Math.max(sum, 0);  //sum为 0 则此前缀不可取，重新开始字串
        }
        return res;
    }


    /**
     * 918. 环形子数组的最大和
     */
    public int maxSubarraySumCircular(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];
        int max = nums[0];
        int sum = nums[0];
        for (int i = 1; i < n; i++) {
            sum += nums[i];
            dp[i] = Math.max(nums[i], dp[i - 1] + nums[i]);
            max = Math.max(max, dp[i]);
        }
        //-----------------------------------------------------------------------
        // 注意：最小连续子区间的右边界，不能触及区间的右边界，左边界可以触及区间左边界
        //-----------------------------------------------------------------------
//        int min = 0;  //不能给 nums[0]，因为数组长度为 1 的情况下，nums[0] 其实就是右侧边界，给 0 的目的其实是不考虑 sum - min 的情况
        int min = (n == 1 ? 0 : nums[0]);  //给 0 或 此处，以及下面遇到 n == 1 直接返回的情况都可以
        for (int i = 1; i < n - 1; i++) {   //不能触及右边界
            dp[i] = Math.min(nums[i], dp[i - 1] + nums[i]);
            min = Math.min(min, dp[i]);
        }
        return Math.max(max, sum - min);
    }

    public int maxSubarraySumCircular01(int[] nums) {
        int n = nums.length;
        //---------------------------------------------
        // 关键：保证了 min = nums[0] 赋值的情况下，min一定不会触及右边界，因为这种写法导致 min 触及右边界的原因只能是 n == 1 此处直接返回，防止下面出现这种情况
        //---------------------------------------------
        if (n == 1) return nums[0];
        int[] dp = new int[n];
        dp[0] = nums[0];
        int max = nums[0];
        int sum = nums[0];
        for (int i = 1; i < n; i++) {
            sum += nums[i];
            dp[i] = Math.max(nums[i], dp[i - 1] + nums[i]);
            max = Math.max(max, dp[i]);
        }
        //-----------------------------------------------------------------------
        // 注意：最小连续子区间的右边界，不能触及区间的右边界，左边界可以触及区间左边界
        //-----------------------------------------------------------------------
        int min = nums[0];
        for (int i = 1; i < n - 1; i++) {   //不能触及右边界
            dp[i] = Math.min(nums[i], dp[i - 1] + nums[i]);
            min = Math.min(min, dp[i]);
        }
        return Math.max(max, sum - min);
    }

    /**
     * 300. 最长递增子序列
     */
    public int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];  //dp[i]状态定义为以 nums[i] 结尾的最长递增子序列
        Arrays.fill(dp, 1); //每一位的最短递增子序列为自身
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {  //遍历当前基准前的各个位点的值
                if (nums[j] < nums[i]) {   //满足递增
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }
        return Arrays.stream(dp).max().getAsInt();
    }


    /**
     * 334. 递增的三元子序列
     */
    public boolean increasingTriplet(int[] nums) {  //参考  300. 最长递增子序列
        int top1 = nums[0];
        int top2 = Integer.MAX_VALUE;
        for (int i = 1; i < nums.length; i++) {
            //--------------------------------------------------------------
            // 整体遍历的过程可以理解为一个水流流经三个滤网，最后的滤网保存最小值、中间滤网保存次最小值、最后滤网拦截较大值
            // 深入理解，第二个滤网维护的值仅代表前面的元素一定存在某个数小于其自身，但此数并不一定是第一个滤网种的值
            // 注意整个遍历的过程中，水流是顺序的，更新各个滤网的情况，详细见举例 [5,10,2,4,6]
            //     在处理第三个元素 2 时，会更新首个滤网的值为 2，此时前两个滤网维护的值分别为 2 和 10
            //         有人会有疑问，2 和 10 并非顺序的啊，此时最小值为 2 中值为 10，不合理啊，那么再来个 11，构成的 2 10 11 也不是按照索引顺序来的啊
            //         其实第二个滤网 10 只代表前面有比 10 小的数，其实对应的 5，而再来个 11 满足条件，其实满足条件的序列是 5 10 11
            //         因此更新首个滤网的值为 2 仅仅是拉低后续元素的门槛，使其更容易满足条件
            //     更新第一第二个滤网仅仅是为了拉低后续元素的门槛，贪心，同时每个元素代表前面一定存在某个数小于其自身
            //--------------------------------------------------------------
            if (top2 < nums[i]) {
                //此条件成立的前提是 top2 一定存在，top2存在说明其一定大于之前的某个值
                return true;
            } else if (top1 < nums[i] && nums[i] < top2) {
                //用于拉低下线（但仍保证 top2 大于 top1），为后续 top3 满足条件降低门槛
                top2 = nums[i];
            } else if (nums[i] < top1) {
                //用于拉低下线，从而为后续元素更新 top2 降低门槛
                top1 = nums[i];
            }
        }
        return false;
    }


    /**
     * 790. 多米诺和托米诺平铺
     */
    public int numTilings(int n) {   //状态机
        int mod = (int) 1e9 + 7;
        int[][] dp = new int[n + 1][4];  //状态机
        dp[1][0] = 1;  //不填
        dp[1][1] = 1;  //填充一列
        for (int i = 2; i <= n; i++) {
            //1、状态 0 的转移
            dp[i][0] = dp[i - 1][1];  //直接状态转移
            //-----------------------------------------
            // 注意：对比 i 位状态为 0 和状态为 {2,3} 状态转移的差异
            //   i 位置的状态 0 不能从 i - 1 位的状态 0 通过添加一个列状的积木完成转移，因此这样的方式已经对 i - 1 位的状态 1 产生了影响
            //   所以不可写为：dp[i][0] = (dp[i - 1][1] + dp[i - 1][0]) % mod;
            //-----------------------------------------

            //2、状态 1 的转移
            int prev = 0;
            for (int j = 0; j < 4; j++) {
                prev += dp[i - 1][j];
                prev %= mod;
            }
            dp[i][1] = prev;

            //3、状态 2 的转移
            dp[i][2] = (dp[i - 1][0] + dp[i - 1][3]) % mod;
            //4、状态 3 的转移
            dp[i][3] = (dp[i - 1][0] + dp[i - 1][2]) % mod;
        }
        return dp[n][1];
    }


    /**
     * 1696. 跳跃游戏 VI
     */
    public int maxResult(int[] nums, int k) {
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MIN_VALUE);
        dp[0] = nums[0];
        //关键在于从前向后施加影响
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n && j <= i + k; j++) {  //当前位置 i 对后续位置 j 的影响
                dp[j] = Math.max(dp[j], dp[i] + nums[j]);
                //剪枝
                if (dp[j] >= dp[i]) {
                    //--------------------------------------------------------------------------------------------------
                    // 当 dp[j] > dp[i]时，dp[j+1] 到 dp[i+k] 必然已经不会受 dp[i]的影响了，因为 dp[j]比dp[i]是更优的跳跃选择
                    //--------------------------------------------------------------------------------------------------
                    break;
                }
            }
        }
        return dp[n - 1];
    }

    //--------------------------------------------------------
    // 上下两种写法的差异：
    //    上面是对当前元素对后 K 个元素的影响，并及时剪枝跳出
    //    下面是对当前元素对前 K 个元素的影响（超时）
    //    下面是对当前元素对前 K 个元素的影响，基于单调队列进行时间优化
    //--------------------------------------------------------

    public int maxResult01(int[] nums, int k) {  //超时
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MIN_VALUE);
        dp[0] = nums[0];
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= k; j++) {   //超时的原因，K 范围太大，重复遍历同一个元素多次
                if (i - j >= 0) {
                    dp[i] = Math.max(dp[i], dp[i - j] + nums[i]);
                }
            }
        }
        return dp[n - 1];
    }

    //基于单调队列的方式，进行优化
    public int maxResult02(int[] nums, int k) {  //单调队列 + 滑动窗口
        int n = nums.length;
        int[] dp = new int[n];
        ArrayDeque<Integer> queue = new ArrayDeque<>();      //记录索引，降序
        dp[0] = nums[0];
        queue.addLast(0);
        //关键在于从前向后施加影响
        for (int i = 1; i < n; i++) {
            //1、维持窗口大小
            while (!queue.isEmpty() && i - queue.peekFirst() > k) {
                queue.pollFirst();
            }
            //2、贪心计算当前位置最大值
            dp[i] = dp[queue.peekFirst()] + nums[i];  //满足影响范围
            //3、维护队列的单调性
            while (!queue.isEmpty() && dp[queue.peekLast()] <= dp[i]) {   //降序排序
                queue.pollLast();
            }
            queue.addLast(i);
        }
        return dp[n - 1];
    }



    /**
     * 813. 最大平均值和的分组
     */
    public double largestSumOfAverages(int[] nums, int k) {  //后续需要补充记忆化搜索的方法
        int n = nums.length;
        double[][] dp = new double[n + 1][k + 1];  //dp[i][j]定义为前 i 个数，被切分为 j 个连续区间，最大的平均值
        double[] prefix = new double[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= Math.min(i, k); j++) {
                if (j == 1) {
                    dp[i][j] = prefix[i] / i;
                } else {
                    //-----------------------------------------------------------------
                    // 关键问题在于第 j 个区间的开始节点是哪个，以下 For 循环就是在枚举左端点
                    //-----------------------------------------------------------------
                    for (int m = j - 1; m < i; m++) {
                        dp[i][j] = Math.max(dp[i][j], dp[m][j - 1] + (prefix[i] - prefix[m]) / (i - m));
                    }
                }
            }
        }
        return dp[n][k];
    }

    /**
     * 787. K 站中转内最便宜的航班
     */
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {  //动态规划
        int INF = 0x3f3f3f3f;
        //--------------------------------------------------------
        // 题目限制条件：最多不超过 K 次中转，即可以乘坐 K + 1 次航班
        //--------------------------------------------------------
        int[][] dp = new int[k + 2][n];  //dp[i][j] 状态定义为 恰好乘坐 i 次航班到达城市 j 的最小费用
        for (int i = 0; i < k + 2; i++) {
            Arrays.fill(dp[i], INF);
        }
        dp[0][src] = 0;  //不乘坐任何航班到达起点所需的费用为 0
        for (int i = 1; i <= k + 1; i++) {
            for (int[] flight : flights) {
                int node1 = flight[0];
                int node2 = flight[1];
                int price = flight[2];
                dp[i][node2] = Math.min(dp[i][node2], dp[i - 1][node1] + price);
            }
        }
        int ans = INF;
        for (int i = 0; i <= k + 1; i++) {  //题目限制条件为 最多经过 K 次中转
            ans = Math.min(ans, dp[i][dst]);
        }
        return ans == INF ? -1 : ans;
    }

    public int findCheapestPrice02(int n, int[][] flights, int src, int dst, int k) {   //运用 Bellman Ford 求解有限制的最短路问题
        //---------------------------------------------------------
        // Bellman-Ford 算法是一种基于松弛（relax）操作的最短路算法
        // Bellman-Ford 算法就是不断尝试对图上每一条边进行松弛，即在每一轮循环中，就对图上所有的边都尝试进行一次松弛操作，当一次循环中没有成功的松弛操作时，算法停止
        //---------------------------------------------------------
        int INF = 0x3f3f3f3f;
        int[][] graph = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                graph[i][j] = i == j ? 0 : INF;
//                graph[i][j] = INF;  //效果同上，结果一致
            }
        }
        for (int[] edge : flights) {
            int node1 = edge[0];
            int node2 = edge[1];
            int price = edge[2];
            graph[node1][node2] = price;
        }
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;
        int m = 0;
        //-------------------------------------------------------------
        // 限制最多经过不超过 K 个点等价于 限制最多不超过 K + 1 条边
        //-------------------------------------------------------------
        while (m < k + 1) {  //对每个节点进行 K + 1 次松弛操作
            //注意，在遍历所有的"边"进行松弛操作前，需要先对 dist 进行备份，否则会出现本次松弛操作所使用到的边，是在同一次迭代所更新的，从而不满足边数限制的要求
            int[] prev = dist.clone();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    //贪心的认为当前边 (src,i) 已经是从原点到点 i 的最短距离，因此尝试使用边 (src,i) 对边 (src,j) 进行松弛操作
                    dist[j] = Math.min(dist[j], prev[i] + graph[i][j]);  //松弛操作
                }
            }
            m++;
        }
        return dist[dst] == INF ? -1 : dist[dst];
    }

    public int findCheapestPrice01(int n, int[][] flights, int src, int dst, int k) {  //基于广度优先搜索来解决最短路问题
        //建图，构建邻接表
        HashMap<Integer, ArrayList<int[]>> adjacent = new HashMap<>();  //记录当前点可"直达"的点及其对应的花费
        for (int[] flight : flights) {
            int node1 = flight[0];
            int node2 = flight[1];
            int price = flight[2];
            ArrayList<int[]> arriveNodes = adjacent.getOrDefault(node1, new ArrayList<>());
            arriveNodes.add(new int[]{node2, price});
            adjacent.put(node1, arriveNodes);  //node1 可直达的地区列表及其对应的花费
        }
        int INF = 0x3f3f3f3f;
        int[] minDist = new int[n];
        Arrays.fill(minDist, INF);
        minDist[src] = 0;

        //广度优先搜索的核心，记录每轮搜索的位点
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.addLast(src);
        int count = 0;
        //--------------------------------------------------------
        //  限制最多经过不超过 K 个点 等价于 最多进行 K + 1 轮广度搜索
        //--------------------------------------------------------
        while (count < k + 1 && !queue.isEmpty()) {
            int size = queue.size();
            //关键：记录此轮搜索前各个位点的最短距离，用于避免在同一轮搜索过程中，后面位点使用前面位点更新的结果，导致不满足点数的要求
            int[] prevDist = minDist.clone();
            //完成此轮搜索需要从队列中获取位点的个数
            for (int i = 0; i < size; i++) {
                Integer currentNode = queue.pollFirst();
                ArrayList<int[]> arriveNodes = adjacent.getOrDefault(currentNode, new ArrayList<>());
                for (int[] nodeInfo : arriveNodes) {
                    int nextNode = nodeInfo[0];
                    int price = nodeInfo[1];
                    if (prevDist[currentNode] + price < minDist[nextNode]) {  //前面使用此轮搜索前的状态，后面使用实时的状态
                        minDist[nextNode] = prevDist[currentNode] + price;
                        if (nextNode != dst) {
                            queue.addLast(nextNode);
                        }
                    }
                }
            }
            count++;
        }
        return minDist[dst] == INF ? -1 : minDist[dst];
    }






    /**
     * 1824. 最少侧跳次数
     */
    public int minSideJumps(int[] obstacles) {  //动态规划
        int INF = 0x3f3f3f3f;
        int n = obstacles.length;
        int[][] dp = new int[n][3];
        dp[0][2] = 1;
        dp[0][1] = 0;
        dp[0][0] = 1;
        for (int i = 1; i < n; i++) {
            //1、针对当前位点，各个赛道均尝试从上个位点平移过来，不涉及侧跳次数
            for (int j = 0; j < 3; j++) {
                if (j == obstacles[i] - 1) {  //1.1 如果当前位点的此赛道中存在障碍物，无需转移，不会到达此位点
                    dp[i][j] = INF;
                } else {                      //1.2 如果当前位点的此赛道中没有障碍物，直接转移（无论此赛道的上一个位点是否为障碍物）
                    dp[i][j] = dp[i - 1][j];
                }
            }
            //2、针对当前位点，由于前一个位点在某个赛道上存在障碍物，因此尝试通过侧跳来优化对应赛道的结果
            int minNums = Math.min(Math.min(dp[i][0], dp[i][1]), dp[i][2]);  //获取到达此位点所需的最小侧跳次数
            for (int j = 0; j < 3; j++) {
                if (j == obstacles[i] - 1) {  //无法优化，此位点的此赛道无法达到
                    continue;
                }
                dp[i][j] = Math.min(dp[i][j], minNums + 1);
            }
        }
        return Math.min(Math.min(dp[n - 1][0], dp[n - 1][1]), dp[n - 1][2]);
    }

    public int minSideJumps01(int[] obstacles) {  //一维表示
        int INF = 0x3f3f3f3f;
        int n = obstacles.length;
        int[] dp = new int[3];
        dp[2] = 1;
        dp[0] = 1;
        for (int i = 1; i < n; i++) {
            int minNums = INF;
            for (int j = 0; j < 3; j++) {
                if (j == obstacles[i] - 1) {
                    dp[j] = INF;
                }
                minNums = Math.min(minNums, dp[j]);
            }
            for (int j = 0; j < 3; j++) {
                if (j == obstacles[i] - 1) {
                    continue;
                }
                dp[j] = Math.min(dp[j], minNums + 1);
            }
        }
        return Math.min(Math.min(dp[0], dp[1]), dp[2]);
    }

    public int minSideJumps02(int[] obstacles) {  //基于最短路算法 Dijkstra
        int INF = 0x3f3f3f3f;
        int n = obstacles.length;
        int[][] directions = {{1, 0, 1}, {2, 0, 1}, {0, 1, 0}};  //横向移动不消耗跳跃数，纵向移动消耗跳跃数
        int[][] dist = new int[3][n];
        for (int i = 0; i < 3; i++) {
            Arrays.fill(dist[i], INF);
        }
        dist[1][0] = 0;  //出发点
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);
        sortedQueue.add(new int[]{1, 0, 0});
        while (!sortedQueue.isEmpty()) {
            //1、获取最短距离的点
            int[] minDistanceNode = sortedQueue.poll();
            int row = minDistanceNode[0];
            int col = minDistanceNode[1];
            int dis = minDistanceNode[2];

            //剪枝
            if (dist[row][col] < dis) continue;
            //最短距离为目标列，直接返回结果
            if (col == n - 1) return dis;
            dist[row][col] = dis;

            //2、影响其余相邻节点的最短距离
            for (int[] dir : directions) {
                int nextRow = (row + dir[0]) % 3;
                int nextCol = col + dir[1];
                //剪枝，下一个位点为障碍物
                if (obstacles[nextCol] - 1 == nextRow) {
                    continue;
                }
                if (dist[row][col] + dir[2] < dist[nextRow][nextCol]) {
                    dist[nextRow][nextCol] = dist[row][col] + dir[2];
                    sortedQueue.add(new int[]{nextRow, nextCol, dist[nextRow][nextCol]});
                }
            }
        }
        return -1;
    }

}
