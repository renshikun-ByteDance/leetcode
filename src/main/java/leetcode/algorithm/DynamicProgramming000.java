package leetcode.algorithm;

import java.util.*;


/**
 * 经典的动态规划 dp问题
 */
public class DynamicProgramming000 {






    /**
     * 518. 零钱兑换 II
     */
    public int change(int amount, int[] coins) {    //动态规划、背包问题，二维
        int[][] dp = new int[coins.length + 1][amount + 1];
        dp[0][0] = 1;
        //-----------------------------------------------------------------
        // 其实针对二维方格中，第一列和第一个行其实都是无效的
        //     1、第一列有效
        //        对应 i 属于区间 [0,coins.length - 1]、j = 0
        //     2、第一行无效
        //        因为 i = 0 代表使用前 i 个硬币（不含自身），分别构成和为 [0,amount] 的组合数，不会存在的，所以第一行除了 i = 0 处值为 1，其余的值均为 0
        //     注意：
        //        此处的 i 主要指的是 dp 中的 i，与 coins 中对应的索引相差一，dp[i]  = ..coins[i-1]..
        //     原因：
        //        由于 dp 存在上下的继承关系，即 i 位 要继承 i - 1 的情况，为了保证不越界，因此 dp 循环的时候，i 要从 1 开始（也是第一行无效的原因），但 coins 要从 0 开始，而二者共用的 i 因此存在 i - 1 的问题
        //     因此：
        //        dp[i][j] 代表使用前 i - 1 个 coin（包含 i - 1） 组合累加值为 j 的组合 数
        //     关键：
        //        dp新增了一行，所以 coins 中的索引与 dp中的索引相差一位，dp 中的索引 1 其实对应 coins 中的索引 0
        //-----------------------------------------------------------------

        // dp[i][j] 表示使用前 i - 1 个硬币（含自身），总金额为 j 的组合数
        for (int i = 1; i <= coins.length; i++) {
            for (int j = 0; j <= amount; j++) {
                if (j < coins[i - 1]) {         //只考虑二维方格中，上下的关系，不考虑左右的关系（越界）
                    dp[i][j] = dp[i - 1][j];
                } else {
                    //----------------------------------------------------
                    // 1、使用当前元素
                    //    前 i - 1 个硬币，通过相关的组合，就已经能够就可以构成总金额 j ，其对应的组合数为 dp[i - 1][j]
                    // 2、不使用当前元素
                    //    前 i - 1 个硬币，通过相关的组合，也可以构成总金额为 dp[i][j - coins[i - 1]]，即其再加上当前元素 coins[i - 1]
                    //----------------------------------------------------
                    dp[i][j] = dp[i - 1][j] + dp[i][j - coins[i - 1]];   //当前元素为什么是 coins[i - 1]，原因是每次 dp 都要取 dp[i - 1][j]，
                }
            }
        }
        return dp[coins.length][amount];
    }

    public int change000(int amount, int[] coins) {  //相较于上，交换内外循环，正确写法
        int[][] dp = new int[coins.length + 1][amount + 1];
        dp[0][0] = 1;
        for (int j = 0; j <= amount; j++) {
            for (int i = 1; i <= coins.length; i++) {
                if (j < coins[i - 1])
                    dp[i][j] = dp[i - 1][j];
                else
                    dp[i][j] = dp[i - 1][j] + dp[i][j - coins[i - 1]];
            }
        }
        return dp[coins.length][amount];
    }


    public int change00(int amount, int[] coins) {    //相较于上，错误写法
        int[][] dp = new int[coins.length + 1][amount + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= coins.length; i++) {
            for (int j = coins[i - 1]; j <= amount; j++) {  //没有判断，目的是想忽略 j 所属区间 [0,coins[i - 1])中 dp[i][j]与同层左侧的关系，但同时又忽略了与上层的关系
                dp[i][j] = dp[i - 1][j] + dp[i][j - coins[i - 1]];   //当前元素为什么是 coins[i - 1]，原因是每次 dp 都要取 dp[i - 1][j]，
            }
        }
        return dp[coins.length][amount];
    }

    public int change01(int amount, int[] coins) {    //相较于上，解耦了 dp中 i 和 coins 中 i 的关系
        int[][] dp = new int[coins.length + 1][amount + 1];
        int[] money = new int[coins.length + 1];
        System.arraycopy(coins, 0, money, 1, coins.length);
        dp[0][0] = 1;

        // dp[i][j] 表示使用前 i 个硬币，总金额为 j 的组合数
        for (int i = 1; i < money.length; i++) {
            for (int j = 0; j <= amount; j++) {
                if (j < coins[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - money[i]];
                }
            }
        }
        return dp[coins.length][amount];
    }

    //----------------------------------------------
    // 将 二维 dp 转换为 一维 dp
    //----------------------------------------------

    public int change02(int amount, int[] coins) {    //动态规划、背包问题，一维
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        // dp[i] 表示构成总金额为 j 的组合数
        for (int coin : coins) {   //遍历的顺序决定了组合枚举的顺序
            for (int j = coin; j <= amount; j++) {
                dp[j] += dp[j - coin];
            }
        }
        return dp[amount];
    }

    public int change022(int amount, int[] coins) {   //错误写法，相较于上，内外循环互换位置
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        for (int j = 1; j <= amount; j++) {
            for (int coin : coins) {
                if (j < coin) continue;
                dp[j] += dp[j - coin];
            }
        }
        return dp[amount];
    }

    public int change06(int amount, int[] coins) {    //超时
        List<ArrayList<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        Arrays.sort(coins);  //增大剪枝的概率
        changeDfs(coins, amount, ans, path, 0, 0);
        System.out.println(ans);
        return ans.size();
    }

    private void changeDfs(int[] coins, int target, List<ArrayList<Integer>> ans, LinkedList<Integer> path, int currentIndex, long sum) {
        //递归循环终止条件一：越界
        if (currentIndex == coins.length) {
            return;
        }
        ///递归循环终止条件二：超过目标值
        if (sum > target) {
            return;
        }
        //递归循环终止条件二：循环找到目标组合
        if (sum == target) {
            ans.add(new ArrayList<>(path));
            return;
        }

        for (int i = currentIndex; i < coins.length; i++) {
            //1、增加元素
            sum += coins[i];
            path.add(coins[i]);

            //2、纵向深度搜索
            changeDfs(coins, target, ans, path, i, sum);  //每次都从 i 开始，而非 i + 1开始，保证同一个元素可重复使用

            //3、移除元素
            sum -= coins[i];
            path.removeLast();
        }
    }


}


