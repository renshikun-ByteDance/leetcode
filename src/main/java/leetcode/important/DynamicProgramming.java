package leetcode.important;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class DynamicProgramming {

    int mod = (int) Math.pow(10, 9) + 7;

    //爬楼梯其实是动态规划-路径问题、路径数、能否到达、到达的最小价值


    //------------------------------------------------------------------------------------
    // 背包问题系列：0 - 1 背包问题
    //   二维：第一个维度表示物品，第二个维度表示背包容量
    //   一维：表示背包容量，省略的第一个物品的维度，各层间进行状态转移
    //------------------------------------------------------------------------------------

    /**
     * 416. 分割等和子集
     * 将背包恰好装满下的"最大价值"
     */
    public boolean canPartition(int[] nums) {   //动态规划，二维
        int sum = 0;
        int max = 0;
        for (int num : nums) {
            sum += num;
            max = Math.max(max, num);
        }
        int target = sum / 2;
        if (sum % 2 == 1) return false;
        if (max > target) return false;
        // dp[i][j] 前 i 个物品，容量为 j 的背包中最大能装下的物品容量
        int[][] dp = new int[nums.length + 1][target + 1];  //各个维度表示：物品、背包容量
        for (int i = 1; i <= nums.length; i++) {  //外层循环，遍历物品
            //--------------------------------------------------------------------------------------------------
            // 针对当前物品，循环背包的过程是从小包到大包，其状态变化为：
            //    1、j < nums[i]
            //      表示当前此物品比当前的背包还大，放不下此物品，只能继承 dp[i-1][j]
            //    2、j >= nums[i]
            //      表示当前背包比当前物品大，可以放下此物品，但为了考虑背包尽可能"装满"，所以需要考虑两种情况：
            //          1、不装此物品
            //                继承 dp[i-1][j]，因为此时，可能通过前 i-1 个物品（可能选择了两个物品）就已经将容量为 j 的背包装满了
            //          2、装下此物品
            //                如果装下当前物品，在转移或继承前置依赖项时，依赖于容量为 j-nums[i] 背包的状态：
            //                   1、装满了，则当前背包 j 也可装满（对应的 true）
            //                   2、未装满，当但钱背包 j 不可装满（对应的 false）
            //--------------------------------------------------------------------------------------------------
            for (int j = 1; j <= target; j++) {   //内层循环，遍历背包
                if (j < nums[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - nums[i - 1]] + nums[i - 1]);
                }
            }
        }
        return dp[nums.length][target] == target;
    }

    public boolean canPartition01(int[] nums) {   //动态规划，一维
        int sum = 0;
        int max = 0;
        for (int num : nums) {
            sum += num;
            max = Math.max(max, num);
        }
        int target = sum / 2;
        if (sum % 2 == 1) return false;
        if (max > target) return false;
        int[] dp = new int[target + 1];
        for (int i = 1; i <= nums.length; i++) {
            for (int j = target; j >= nums[i - 1]; j--) {  //反向遍历，防止未使用的状态被覆盖
                dp[j] = Math.max(dp[j], dp[j - nums[i - 1]] + nums[i - 1]);
            }
        }
        return dp[target] == target;
    }

    //------------------------------------------------------------------------
    // 上下两种写法的差异：
    //    上面是容量为 j 的背包最大能装下物品的容量和
    //        状态转移完毕后，判断 容量为 target 的背包，其最大能装下的物品容量是否为 target，来判断其是否装满
    //    下面是容量为 j 的背包是否能恰好装满
    //        状态转移完毕后，判断 容量为 target 的背包是否为 true 是否被装满
    //------------------------------------------------------------------------


    //基于能否恰好装满背包（容量 j）来处理
    public boolean canPartition10(int[] nums) {   //动态规划，二维
        int sum = 0;
        int max = 0;
        for (int num : nums) {
            sum += num;
            max = Math.max(max, num);
        }
        int target = sum / 2;
        if (sum % 2 != 0) return false;
        if (max > target) return false;
        boolean[][] dp = new boolean[nums.length + 1][target + 1];
        //初始化
        for (int i = 0; i <= nums.length; i++) {    //第一列，即针对任意物品，如果不选的，都能装满背包（容量为 0）
            dp[i][0] = true;
        }
        for (int i = 1; i <= nums.length; i++) {    //1、从 1 开始
            for (int j = 1; j <= target; j++) {     //2、从 1 开始
                if (j < nums[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i - 1]];   //均是从上一行转换而来
                }
            }
        }
        return dp[nums.length][target];
    }

    public boolean canPartition11(int[] nums) {   //动态规划，一维
        int sum = 0;
        int max = 0;
        for (int num : nums) {
            sum += num;
            max = Math.max(max, num);
        }
        int target = sum / 2;
        if (sum % 2 != 0) return false;
        if (max > target) return false;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        for (int i = 0; i < nums.length; i++) {
            for (int j = target; j >= nums[i]; j--) {  //关键：倒序
                dp[j] = dp[j] || dp[j - nums[i]];      //均是从上一行转换而来
            }
        }
        return dp[target];
    }


    public boolean canPartition100(int[] nums) {   //与上面的差异在于，dp 的行数不同
        int sum = 0;
        int max = 0;
        for (int num : nums) {
            sum += num;
            max = Math.max(max, num);
        }
        int target = sum / 2;
        if (sum % 2 != 0) return false;
        if (max > target) return false;
        boolean[][] dp = new boolean[nums.length][target + 1];   //行数与上不同
        //初始化
        for (int i = 0; i < nums.length; i++) {    //第一列，即针对任意物品，如果不选的，都能装满背包（容量为 0）
            dp[i][0] = true;
        }
        dp[0][nums[0]] = true;  //关键
        for (int i = 1; i < nums.length; i++) {
            for (int j = 1; j <= target; j++) {
                if (j < nums[i]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i]];   //均是从上一行转换而来
                }
            }
        }
        return dp[nums.length - 1][target];
    }


    //--------------------------------------------------------
    // 上下为二维动态规划和二维的记忆化搜索
    //--------------------------------------------------------

    public boolean canPartition20(int[] nums) {  //记忆搜索，参见 面试题 17.15. 最长单词
        int sum = Arrays.stream(nums).sum();
        if (sum % 2 != 0) return false;
        int target = sum / 2;
        Arrays.sort(nums);
        int[][] cached = new int[nums.length + 1][target + 1];  //一定是二维的，不能是一维的
        return canPartitionDfs(nums, target, cached, 0);
    }

    private boolean canPartitionDfs(int[] nums, int target, int[][] cached, int currentIndex) {
        //递归终止条件一：全部搜索完成，未找到目标值
        if (target == 0) {
            return true;
        }
        //目标结果
        if (target < 0 || currentIndex >= nums.length) {
            return false;
        }
        //递归终止条件二：已搜索
        if (cached[currentIndex][target] == 1) {
            return false;
        }

        if (canPartitionDfs(nums, target, cached, currentIndex + 1) || canPartitionDfs(nums, target - nums[currentIndex], cached, currentIndex + 1)) {
            return true;
        }

        cached[currentIndex][target] = 1;

        return false;
    }

    //基于一维记忆化搜索
    private boolean canPartitionDfs(int[] nums, int target, int[] cached, int currentIndex) {  //错误写法
        //递归终止条件一：目标结果
        if (target == 0) {
            return true;
        }
        //递归终止条件二：未找到目标结果，或者当前分支不可用
        if (target < 0 || currentIndex >= nums.length) {
            return false;
        }
        //递归终止条件二：已搜索
        if (cached[target] == 1) {
            return false;
        }

        if (canPartitionDfs(nums, target, cached, currentIndex + 1) || canPartitionDfs(nums, target - nums[currentIndex], cached, currentIndex + 1)) {
            return true;
        }

        cached[target] = 1;

        return false;
    }

    /**
     * 474. 一和零
     */
    public int findMaxForm(String[] strs, int m, int n) {  //动态规划，二维
        int[][][] dp = new int[strs.length + 1][m + 1][n + 1];  //各个维度分别表示：物品、背包 A容量、背包 B容量
        for (int tt = 1; tt <= strs.length; tt++) {  //外层循环，遍历物品
            int aa = getZeroNums(strs[tt - 1]);
            int bb = strs[tt - 1].length() - aa;
            for (int i = 0; i <= m; i++) {           //内层循环，遍历背包，为什么要从 0 开始 而不是 从 1
                for (int j = 0; j <= n; j++) {
                    if (i < aa || j < bb) {   //1、当前物品 tt 不可选
                        dp[tt][i][j] = dp[tt - 1][i][j];
                    } else {                  //2、当前物品 tt 可选
                        //-----------------------------------------------
                        // 可选，但不一定非要选，贪心取二者最大值
                        //    2.1 不选，基于上侧状态
                        //    2.2 选择，基于上层状态 + 1
                        //-----------------------------------------------
                        dp[tt][i][j] = Math.max(dp[tt - 1][i][j], dp[tt - 1][i - aa][j - bb] + 1);  //两个背包不一定装满
                    }
                }
            }
        }
//        return dp[strs.length][m][n];  //官方直接返回

        //个人理解应该是以下的方式获取最值吧
        int ans = 0;
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                ans = Math.max(ans, dp[strs.length][i][j]);
            }
        }
        return ans;
    }

    public int findMaxForm01(String[] strs, int m, int n) {  //动态规划，一维
        int[][] dp = new int[m + 1][n + 1];  //各个维度分别表示：物品、背包 A容量、背包 B容量
        for (int tt = 1; tt <= strs.length; tt++) {  //外层循环，遍历物品
            int aa = getZeroNums(strs[tt - 1]);
            int bb = strs[tt - 1].length() - aa;
            for (int i = m; i >= aa; i--) {
                for (int j = n; j >= bb; j--) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - aa][j - bb] + 1);
                }
            }
        }
        return dp[m][n];
    }

    private int getZeroNums(String str) {
        int ans = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '0') ans++;
        }
        return ans;
    }


    /**
     * 494. 目标和
     */
    public int findTargetSumWays(int[] nums, int target) {  //动态规划，二维度
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        int diff = sum - target;
        if (diff < 0 || diff % 2 == 1) {
            return 0;
        }
        int neg = diff / 2;
        //各个维度分别表示：物品、背包容量
        //dp[i][j]:从nums[0...i]中取任意个数，装满容量为j的背包，有dp[i][j]种取法
        int[][] dp = new int[nums.length + 1][neg + 1];
        //第一列初始化
        dp[0][0] = 1;   //因为当前无任何元素可选，故只有一种情况满足背包容量为 0 ，即不选任何元素
        //动态规划
        for (int i = 1; i <= nums.length; i++) {   //外层循环，遍历物品
            for (int j = 0; j <= neg; j++) {       //内层循环，遍历背包容量，要从 0 开始，因为 nums[i] 会有等于 0 的情况
                if (j < nums[i - 1]) {        //1、当前的背包容量装不下，此物品不可选
                    dp[i][j] = dp[i - 1][j];
                } else {                      //2、当前的背包容量可装下，此物品可选
                    //--------------------------------------
                    // 当前物品可选，但不是必须选，当前容量下的路径数，为以下两种情况下的路径和：
                    //     1、选择当前物品
                    //     2、不选择当前物品
                    //--------------------------------------
                    dp[i][j] = dp[i - 1][j] + dp[i - 1][j - nums[i - 1]];
                }

                //上下两种写法均可，要注掉其中一个
                dp[i][j] = dp[i - 1][j];
                if (j >= nums[i - 1]) {
                    dp[i][j] = dp[i][j] + dp[i - 1][j - nums[i - 1]];
                }
            }
        }
        return dp[nums.length][neg];
    }
    //-------------------------------------------------------
    //  第一列，不能认为 背包大小为 0 时，各个物品都不选，则对应一个路径，故全部初始化为 1
    //        for (int i = 0; i <= nums.length; i++) {
    //            dp[i][0] = 1;
    //        }
    //       各个物品都不选，则对应的路径数为 1 ，如果 各个物品自身的的重量就是 0 ，则直接取本身也可以，所以这也是一种路径，则和为 2
    //       同时，背包大小要从 0 开始
    //  案例：int[] nums = {1, 0}，int target = 1;
    //  https://leetcode.cn/problems/target-sum/solution/0-1-by-friendly-sutherlandi7d-k6ms/
    //-------------------------------------------------------

    public int findTargetSumWays00(int[] nums, int target) {  //动态规划，二维度
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        int diff = sum - target;
        if (diff < 0 || diff % 2 == 1) {
            return 0;
        }
        int neg = diff / 2;
        int[][] dp = new int[nums.length][neg + 1];
        //初始化第一行元素
        dp[0][0] = 1;  //基本的不选第一个元素，就满足背包容量为 0
        if ((nums[0] <= neg)) dp[0][nums[0]]++;  //基于此元素 nums[0]，影响 dp 第一行，当 nums[0] == 0 时，其也影响 dp[0][0]

        //------------------------------------------------------------------------------
        //  当 i 属于区间 [0,nums.length)，初始化，此处可分为两种情况：
        //    1、dp[i][0] 最少为 1，原因是因为，针对容量为 0 的背包，每个物品不选的情况下，均满足容量为 0，所以...
        //    2、基于 nums[0] 对其初始化第一行 dp
        //         如果 nums[0] == 0，则影响 dp[0][0]，而 dp[0][0] 在不选择第一个物品的时候，就是 0，所以 dp[0][0] = 1 + 1
        //         如果 nums[0] != 0 && nums[0] <= neg，则影响 dp[0][nums[0]] == 1
        //------------------------------------------------------------------------------

        //动态规划，处理其余行元素
        for (int i = 1; i < nums.length; i++) {  //注意区别上下的差异
            for (int j = 0; j <= neg; j++) {
                if (j < nums[i]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i - 1][j - nums[i]];
                }
            }
        }
        return dp[nums.length - 1][neg];
    }


    public int findTargetSumWays01(int[] nums, int target) {  //动态规划，一维
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        int diff = sum - target;
        if (diff < 0 || diff % 2 == 1) {
            return 0;
        }
        int neg = diff / 2;
        int[] dp = new int[neg + 1];
        dp[0] = 1;
        for (int i = 0; i < nums.length; i++) {
            for (int j = neg; j >= nums[i]; j--) {
                dp[j] = dp[j] + dp[j - nums[i]];
            }
        }
        return dp[neg];
    }

    private int sumWays = 0;

    public int findTargetSumWays10(int[] nums, int target) {  //深度优先搜索，时间复杂度高，但未超时
        findTargetSumWaysDfs(nums, 0, 0, target);
        return sumWays;
    }

    private void findTargetSumWaysDfs(int[] nums, int currentIndex, int sum, int target) {
        if (currentIndex == nums.length) {
            if (sum == target) sumWays++;
            return;
        }
        findTargetSumWaysDfs(nums, currentIndex + 1, sum + nums[currentIndex], target);
        findTargetSumWaysDfs(nums, currentIndex + 1, sum - nums[currentIndex], target);
    }


    public int findTargetSumWays20(int[] nums, int target) {  //记忆化搜索，比上面暴力 DFS搜索快一些
//        int sum = Arrays.stream(nums).sum();
//        int[][] cached = new int[nums.length + 1][sum + 1];  //会有负数的情况，所以使用 HashMap 存储
        HashMap<String, Integer> cached = new HashMap<>();
        return findTargetSumWaysDfs00(nums, cached, 0, 0, target);
    }

    private int findTargetSumWaysDfs00(int[] nums, HashMap<String, Integer> cached, int currentIndex, int sum, int target) {
        if (currentIndex == nums.length) {
            if (sum == target) return 1;
            else return 0;
        }
        String currentKey = currentIndex + "_" + sum;
        if (cached.containsKey(currentKey)) {
            return cached.get(currentKey);
        }

        int paths = 0;
        paths += findTargetSumWaysDfs00(nums, cached, currentIndex + 1, sum + nums[currentIndex], target);
        paths += findTargetSumWaysDfs00(nums, cached, currentIndex + 1, sum - nums[currentIndex], target);

        cached.put(currentKey, paths);

        return paths;
    }


    /**
     * 1049. 最后一块石头的重量 II
     */
    public int lastStoneWeightII(int[] stones) {   //动态规划，二维，0-1背包问题，最值写法
        int sum = 0;
        for (int num : stones) {
            sum += num;
        }
        int target = sum / 2;
        int[][] dp = new int[stones.length + 1][target + 1];
        for (int i = 1; i <= stones.length; i++) {
            for (int j = 1; j <= target; j++) {   //从 0 和 1 开始结果一致，因为 stones[i] 均大于 0，所以 j = 0 时，本质取的是 dp[0][0] == 0
                if (j < stones[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - stones[i - 1]] + stones[i - 1]);
                }
            }
        }
        return sum - 2 * dp[stones.length][target];
    }

    public int lastStoneWeightII01(int[] stones) {   //动态规划，一维，0-1背包问题，最值写法
        int sum = 0;
        for (int num : stones) {
            sum += num;
        }
        int target = sum / 2;
        int[] dp = new int[target + 1];
        for (int i = 1; i <= stones.length; i++) {
            for (int j = target; j >= stones[i - 1]; j--) {
                dp[j] = Math.max(dp[j], dp[j - stones[i - 1]] + stones[i - 1]);
            }
        }
        return sum - 2 * dp[target];
    }


    public int lastStoneWeightII20(int[] stones) {   //动态规划，二维，0-1背包，恰好装满写法
        int sum = 0;
        for (int num : stones) {
            sum += num;
        }
        int target = sum / 2;
        boolean[][] dp = new boolean[stones.length + 1][target + 1];
        for (int i = 0; i <= stones.length; i++) {
            dp[i][0] = true;
        }
        for (int i = 1; i <= stones.length; i++) {
            for (int j = 1; j <= target; j++) {
                if (j < stones[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j] || dp[i - 1][j - stones[i - 1]];
                }
            }
        }
        for (int j = target; j >= 0; j--) {
            if (dp[stones.length][j]) {
                return sum - 2 * j;
            }
        }
        return 0;
    }

    public int lastStoneWeightII21(int[] stones) {   //动态规划，一维，0-1背包，恰好装满写法
        int sum = 0;
        for (int num : stones) {
            sum += num;
        }
        int target = sum / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        for (int i = 0; i < stones.length; i++) {
            for (int j = target; j >= stones[i]; j--) {
                dp[j] = dp[j] || dp[j - stones[i]];
            }
        }
        for (int j = target; j >= 0; j--) {
            if (dp[j]) {
                return sum - 2 * j;
            }
        }
        return 0;
    }

    //--------------------------------------------------
    // 对比上下两个系列的差异，关键在于"首行"的初始化：
    //     上面的"首行"为无效行，无需初始化，真正有意义的首行，可整体计算
    //     下面的"首行"指 第一个物品 stones[0] 对应的行，需要初始化，目的在于 dp[i] 和 stones[i] 协调一致，而非错一位
    //--------------------------------------------------
    public int lastStoneWeightII31(int[] stones) {   //动态规划，二维，0-1背包问题
        int sum = 0;
        for (int num : stones) {
            sum += num;
        }
        int target = sum / 2;
        int[][] dp = new int[stones.length][target + 1];
        //初始化第一行
        for (int j = 0; j <= target; j++) {
            if (j >= stones[0]) dp[0][j] = stones[0];
        }
        //动态规划，其余行
        for (int i = 1; i < stones.length; i++) {
            for (int j = 1; j <= target; j++) {   //从 0 和 1 开始结果一致，因为 stones[i] 均大于 0，所以 j = 0 时，本质取的是 dp[0][0] == 0
                if (j < stones[i]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - stones[i]] + stones[i]);
                }
            }
        }
        return sum - 2 * dp[stones.length - 1][target];
    }

    public int lastStoneWeightII32(int[] stones) {   //对比与上的差异
        int sum = 0;
        for (int num : stones) {
            sum += num;
        }
        int target = sum / 2;
        int[] dp = new int[target + 1];
        //初始化第一行
        for (int j = 0; j <= target; j++) {
            if (j >= stones[0]) dp[j] = stones[0];
        }
        for (int i = 1; i < stones.length; i++) {
            for (int j = target; j >= stones[i]; j--) {
                dp[j] = Math.max(dp[j], dp[j - stones[i]] + stones[i]);
            }
        }
        return sum - 2 * dp[target];
    }


    /**
     * 879. 盈利计划
     */
    public int profitableSchemes(int n, int minProfit, int[] group, int[] profit) {
        int m = group.length;
        int[][][] dp = new int[m + 1][n + 1][minProfit + 1];
        dp[0][0][0] = 1;  //dp[i][j][k] 表示在前 i 个工作中"恰好"选择了 j 个员工，并且满足工作利润至少为 k 的情况下的盈利计划的总数目
        for (int i = 1; i <= m; i++) {       //1、遍历物品
            for (int j = 0; j <= n; j++) {              //2、遍历背包一
                for (int k = 0; k <= minProfit; k++) {  //3、遍历背包二
                    if (j < group[i - 1]) {     //背包一容量不够
                        dp[i][j][k] = dp[i - 1][j][k];
                    } else {                    //背包一容量足够
                        //-------------------------------------------------------------------------------------------------------------------------------
                        // 背包三的处理：
                        // 如果在状态转移过程中出现需要基于一个 负盈利金额，则可直接转换为 基于 0 盈利金额，从而真实的金额就比预期大，故盈利金额可是实现至少为 minProfit 而非 恰好为 minProfit
                        //     Math.max(0, k - profit[i - 1])
                        //-------------------------------------------------------------------------------------------------------------------------------
                        dp[i][j][k] = (dp[i - 1][j][k] + dp[i - 1][j - group[i - 1]][Math.max(0, k - profit[i - 1])]) % mod;
                    }
                }
            }
        }
        int sumPlans = 0;
        for (int j = 0; j <= n; j++) {  //由于前面动态规划定义中考虑的是恰好使用 j 个员工，本题要求不超过 n 个员工，故应进行以下处理
            sumPlans = (sumPlans + dp[m][j][minProfit]) % mod;
        }
        return sumPlans;
    }

    //------------------------------------------------------------------------------
    // 对比上下两种写法的差别：上面恰好 j 个人，下面使用不超过 j 个人（1 到 j 个人均可）
    //------------------------------------------------------------------------------

    public int profitableSchemes01(int n, int minProfit, int[] group, int[] profit) {
        int m = group.length;
        int[][][] dp = new int[m + 1][n + 1][minProfit + 1];  //dp[i][j][k] 表示在前 i 个工作中选择了 j 个员工，并且满足工作利润至少为 k 的情况下的盈利计划的总数目
        for (int j = 0; j <= n; j++) {
            dp[0][j][0] = 1;   //状态为 不多于 j 个员工
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                for (int k = 0; k <= minProfit; k++) {
                    if (j < group[i - 1]) {
                        dp[i][j][k] = dp[i - 1][j][k];
                    } else {
                        dp[i][j][k] = (dp[i - 1][j][k] + dp[i - 1][j - group[i - 1]][Math.max(0, k - profit[i - 1])]) % mod;
                    }
                    //等价写法
//                    dp[i][j][k] = dp[i - 1][j][k];
//                    if (j >= group[i - 1]) {
//                        dp[i][j][k] = (dp[i - 1][j - group[i - 1]][Math.max(0, k - profit[i - 1])]) % mod;
//                    }
                }
            }
        }
        return dp[m][n][minProfit];
    }

    //------------------------------------------------------------------------------------
    // 背包问题系列：完全背包问题
    //     "完全背包"、"0-1背包"的状态转移略有不同：
    //      二维："0-1背包"完全基于上层的状态、"完全背包"则会部分基于同层的状态，因为完全背包中单个元素可重复使用
    //      一维："0-1背包"，为了保证每次均使用上层的状态，因此背包容量的循环采用反向循环
    //            "完全背包"，为了基于同层的状态，因此背包容量的循环采用正向循环
    //------------------------------------------------------------------------------------

    /**
     * 322. 零钱兑换
     */
    public int coinChange(int[] coins, int target) {    //凑成总金额所需的 最少的硬币个数
        int[][] dp = new int[coins.length + 1][target + 1];
        for (int[] arr : dp) {
            Arrays.fill(arr, target + 1);   //最多需要 target 个 1 构成 target，为了判断是否存在，需要再 + 1 以非合理的最大值初始化
        }
        dp[0][0] = 0;
        for (int i = 1; i <= coins.length; i++) {  //外层循环，遍历物品
            for (int j = 0; j <= target; j++) {    //内层循环，遍历背包
                if (j < coins[i - 1]) {
                    dp[i][j] = dp[i - 1][j];   //由于硬币值大于等于 1，因此背包 j = 0 此列，最终均使用 dp[0][0]
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - coins[i - 1]] + 1);  //注意此处第二个状态转移为什么要基于当前的 i 层，而不是从上一层 i - 1 开始
//                    dp[i][j] = Math.min(dp[i - 1][j], dp[i - 1][j - coins[i - 1]] + 1);
                }
            }
        }
        int ans = dp[coins.length][target];
        return ans == target + 1 ? -1 : ans;
    }


    public int coinChange01(int[] coins, int target) {   //一维
        //--------------------------------------------------------
        // 完全背包之凑满背包的最小物品数：
        //     背包容量：target、物品重量：coins[i]、物品价值：1，即求恰好装满背包的物品的最小价值
        // 1.状态定义：dp[j]为凑满容量为 j 的背包，所需的最少物品数
        // 2.状态转移：
        //      考虑背包容量 j 和物品重量 coins[i]的关系
        //       2.1 当 j <  coins[i] 时，当前背包装不下此物品，继承上一层 dp[j] 的值
        //       2.2 当 j >= coins[i] 时，当前背包可装下此物品，可以选择"装"和"不装"两种情况下的最小价值（最少物品数）进行状态转移
        // 3.初始化：
        //       Arrays.fill(dp, target + 1)、dp[0] = 0
        // 4.循环过程：
        //      4.1 内外循环：求最少物品数，因此组合和排列写法均可
        //      4.2 循环顺序：仅指的是背包循环的顺序，由于是"完全背包"，因此必须正序循环
        // 5.返回形式：
        //      dp[target]有状态转移则返回，无状态转移则返回 -1
        //--------------------------------------------------------
        int[] dp = new int[target + 1];
        Arrays.fill(dp, target + 1);
        dp[0] = 0;
        for (int i = 1; i <= coins.length; i++) {
            for (int j = coins[i - 1]; j <= target; j++) {
                dp[j] = Math.min(dp[j], dp[j - coins[i - 1]] + 1);
            }
        }
        int ans = dp[target];
        return ans == target + 1 ? -1 : ans;
    }

    int minAns = Integer.MAX_VALUE;

    //深度优先搜索，超时
    public int coinChange21(int[] coins, int target) {   //超时
        ArrayList<ArrayList<Integer>> val = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        Arrays.sort(coins);
        for (int i = 0, j = coins.length - 1; i < j; i++, j--) {
            int temp = coins[i];
            coins[i] = coins[j];
            coins[j] = temp;
        }
        coinChangeDfs(coins, target, val, path, 0, 0);
        System.out.println(val);
        return minAns == Integer.MAX_VALUE ? -1 : minAns;
    }

    private void coinChangeDfs(int[] coins, int target, ArrayList<ArrayList<Integer>> val, LinkedList<Integer> path, int currentIndex, int sum) {
        //递归终止条件一：找到目标组合
        if (sum == target) {
            val.add(new ArrayList<>(path));
            minAns = Math.min(minAns, path.size());
            return;
        }

        //递归终止条件二：越界
        if (currentIndex == coins.length) {
            return;
        }

        for (int i = currentIndex; i < coins.length; i++) {
            //剪枝
            if (sum + coins[i] > target) {
                continue;
            }

            //1、增加元素
            path.add(coins[i]);
            sum += coins[i];

            //2、纵向递归搜索
            coinChangeDfs(coins, target, val, path, i, sum);

            //3、删除元素
            path.removeLast();
            sum -= coins[i];
        }
    }

    public int coinChange22(int[] coins, int target) {   //记忆化搜索，大致写法，没验证，可能不对，后面再看吧
        int[][] cache = new int[coins.length + 1][target + 1];

        return coinChangeDfs(coins, cache, target, 0, 0);
    }

    private int coinChangeDfs(int[] coins, int[][] cache, int target, int currentIndex, int sum) {
        if (currentIndex >= cache.length) {
            return Integer.MAX_VALUE;
        }
        if (sum == target) {
            return 1;
        }
        if (cache[currentIndex][sum] != 0) {
            return cache[currentIndex][sum];
        }

        int minNums = Integer.MAX_VALUE;
        for (int i = currentIndex; i < coins.length; i++) {
            minNums = Math.min(minNums, coinChangeDfs(coins, cache, target, i, sum + coins[i]));
        }
        cache[currentIndex][sum] = minNums;

        return minNums;
    }


    /**
     * 518. 零钱兑换 II
     */
    public int change(int amount, int[] coins) {  //可以凑成总金额的硬币组合数
        //--------------------------------------------------------
        // 完全背包之凑满背包的最小物品数：
        //     背包容量：amount、物品重量：coins[i]、物品价值：？？，即求恰好装满背包的物品的组合数
        // 1.状态定义：dp[i][j]为前 i - 1 个物品，凑满容量为 j 的背包，对应的组合数
        // 2.状态转移：
        //      考虑背包容量 j 和物品重量 coins[i]的关系
        //       2.1 当 j <  coins[i] 时，当前背包装不下此物品，继承上一层 dp[i-1][j] 的值
        //       2.2 当 j >= coins[i] 时，当前背包可装下此物品，可以选择"装"和"不装"两种情况下的组合数之和进行状态转移
        // 3.初始化：
        //       dp[0][0] = 1
        // 4.循环过程：
        //      4.1 内外循环：求组合数，外循环遍历物品，内循环遍历背包
        //      4.2 循环顺序：仅指的是背包循环的顺序，由于是"完全背包"，因此必须正序循环
        // 5.返回形式：
        //      dp[target]有状态转移则返回，无状态转移则返回 -1
        //--------------------------------------------------------
        int[][] dp = new int[coins.length + 1][amount + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= coins.length; i++) {
            for (int j = 0; j <= amount; j++) {
                if (j < coins[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - coins[i - 1]];  //从本层进行状态转移
                }
            }
        }
        return dp[coins.length][amount];
    }

    public int change01(int amount, int[] coins) {  //可以凑成总金额的硬币组合数
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        for (int coin : coins) {
            for (int j = coin; j <= amount; j++) {
                dp[j] += dp[j - coin];
            }
        }
        return dp[amount];
    }


    static ArrayList<Integer> squares = new ArrayList<>();

    static {
        for (int i = 1; i < 10000; i++) {
            int xx = i * i;
            if (xx > 10000) break;
            squares.add(xx);
        }
    }

    int[] nums = {1, 4, 9, 16, 25, 36, 49, 64, 81, 100, 121, 144, 169, 196, 225, 256, 289, 324, 361, 400, 441, 484, 529, 576, 625, 676, 729, 784, 841, 900, 961, 1024, 1089, 1156, 1225, 1296, 1369, 1444, 1521, 1600, 1681, 1764, 1849, 1936, 2025, 2116, 2209, 2304, 2401, 2500, 2601, 2704, 2809, 2916, 3025, 3136, 3249, 3364, 3481, 3600, 3721, 3844, 3969, 4096, 4225, 4356, 4489, 4624, 4761, 4900, 5041, 5184, 5329, 5476, 5625, 5776, 5929, 6084, 6241, 6400, 6561, 6724, 6889, 7056, 7225, 7396, 7569, 7744, 7921, 8100, 8281, 8464, 8649, 8836, 9025, 9216, 9409, 9604, 9801, 10000};

    /**
     * 279. 完全平方数
     */
    public int numSquares(int n) { //二维，思路和同 322. 零钱兑换 基本一致
        int[][] dp = new int[nums.length + 1][n + 1];
        for (int[] arr : dp) {
            Arrays.fill(arr, n + 2);
        }
        dp[0][0] = 0;
        for (int i = 1; i <= nums.length; i++) {
            for (int j = 0; j <= n; j++) {
                if (j < nums[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - nums[i - 1]] + 1);
                }
            }
        }
        return dp[nums.length][n];
    }

    public int numSquares01(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, n + 2);
        dp[0] = 0;
        for (int num : nums) {
            for (int j = num; j <= n; j++) {
                dp[j] = Math.min(dp[j], dp[j - num] + 1);
            }
        }
        return dp[n];
    }


    /**
     * 1449. 数位成本和为目标值的最大数字
     */
    public String largestNumber(int[] cost, int target) {  //二维
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        String[][] dp = new String[nums.length + 1][target + 1];
        for (String[] arr : dp) {
            //--------------------------------------------------------------------------------------------------
            // 默认值，与 279. 完全平方数 类似，均使得当前值在比较的时候，不可用（失效），但差异在于：
            //    279. 完全平方数  不可用失效是自动实现的，即无需增加判断条件，在 Math.min 自动被失效，无需手工识别和判断
            //    当前题目         不可用失效是需要判断的，等于 "#" 无法进行状态转移，因为其前置状态不存在，背包无法背满
            //--------------------------------------------------------------------------------------------------
            Arrays.fill(arr, "#");
        }
        dp[0][0] = "";
        for (int i = 1; i <= nums.length; i++) {
            for (int j = 0; j <= target; j++) {   //完全背包：正序遍历背包
                dp[i][j] = dp[i - 1][j];
                if (j >= cost[i - 1] && !dp[i][j - cost[i - 1]].equals("#")) {  //恰好装满
                    dp[i][j] = strMax(dp[i - 1][j], nums[i - 1] + dp[i][j - cost[i - 1]]);
                }
            }
        }
        return dp[nums.length][target].equals("#") ? "0" : dp[nums.length][target];
    }

    public String largestNumber01(int[] cost, int target) {  //一维
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        String[] dp = new String[target + 1];
        Arrays.fill(dp, "#");
        dp[0] = "";
        for (int i = 1; i <= nums.length; i++) {
            for (int j = cost[i - 1]; j <= target; j++) {
                if (dp[j - cost[i - 1]].equals("#")) continue;
                dp[j] = strMax(dp[j], nums[i - 1] + dp[j - cost[i - 1]]);
            }
        }
        return dp[target].equals("#") ? "0" : dp[target];
    }

    private String strMax(String str1, String str2) {  //比较字符串中数字的大小
        if (str1.length() > str2.length()) return str1;
        if (str1.length() < str2.length()) return str2;
        return str1.compareTo(str2) > 0 ? str1 : str2;
    }


    /**
     * 1155. 掷骰子的N种方法
     */
    public int numRollsToTarget(int n, int k, int target) {
        int[] nums = new int[k];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = i + 1;
        }
        int[][] dp = new int[n + 1][target + 1];
        dp[0][0] = 1;
        int t = 1;
        while (t <= n) {  //仅用于计数，第几次掷骰子
            //-----------------------------
            // 里面套了一个 爬楼梯
            //-----------------------------
            for (int j = 1; j <= target; j++) {   //排列问题，先遍历背包容量
                for (int num : nums) {                       //在遍历物品价值
                    if (j >= num) {
                        dp[t][j] += dp[t - 1][j - num];
                        dp[t][j] %= mod;
                    }
                }
            }
            t++;
        }
        return dp[n][target];
    }


    public int numRollsToTarget010(int n, int k, int target) {  //一维
        int[] nums = new int[k];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = i + 1;
        }
        int[] dp = new int[target + 1];
        dp[0] = 1;
        int t = 1;
        while (t <= n) {
            for (int j = target; j >= 0; j--) {   //排列问题，先遍历背包容量，##反向##
                dp[j] = 0;   //要基于上层状态计算
                for (int num : nums) {
                    if (j >= num) {
                        dp[j] += dp[j - num];
                        dp[j] %= mod;
                    }
                }
            }
            t++;
        }
        return dp[target];
    }




    /**
     * 剑指 Offer 47. 礼物的最大价值
     */
    public int maxValue(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i > 0) dp[i][j] = Math.max(dp[i][j], dp[i - 1][j]);
                if (j > 0) dp[i][j] = Math.max(dp[i][j], dp[i][j - 1]);
                dp[i][j] += grid[i][j];
            }
        }
        return dp[m - 1][n - 1];
    }

    public int maxValue01(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[] dp = new int[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (j == 0) {
                    dp[j] += grid[i][j];
                    continue;
                }
                dp[j] = Math.max(dp[j], dp[j - 1]) + grid[i][j];
            }
        }
        return dp[n - 1];
    }



}
