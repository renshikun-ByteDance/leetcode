package leetcode.algorithm;

import java.util.*;

/**
 * 回溯算法
 */
public class BackTrack {


    /**
     * 17. 电话号码的字母组合
     */
    public List<String> letterCombinations(String digits) {
        ArrayList<String> combinations = new ArrayList<>();
        if (digits.length() == 0)
            return combinations;
        HashMap<Character, String> hTable = new HashMap<>();
        hTable.put('2', "abc");
        hTable.put('3', "def");
        hTable.put('4', "ghi");
        hTable.put('5', "jkl");
        hTable.put('6', "mno");
        hTable.put('7', "pqrs");
        hTable.put('8', "tuv");
        hTable.put('9', "wxyz");
        //回溯（递归）
        backtrack(digits, hTable, 0, new StringBuilder(), combinations);

        return combinations;
    }

    private void backtrack(String digits, HashMap<Character, String> hTable, int index, StringBuilder builder, ArrayList<String> combinations) {
        //递归到叶子节点，则将拼接的结果写入到集合中
        if (index == digits.length()) {   //index为树的高度，即 digits的长度
            combinations.add(builder.toString());
        } else {
            //获取当前位置的数字
            char digit = digits.charAt(index);
            //获取数据对应的字符组合
            String digitToMap = hTable.get(digit);
            for (int i = 0; i < digitToMap.length(); i++) {  //遍历此 digit 对应的所有 字母
                builder.append(digitToMap.charAt(i));        //第index位的 digit 对应的 字母（其中一种情况）
                backtrack(digits, hTable, index + 1, builder, combinations); //递归处理 在这种组合（叶节点）的 下一位
                builder.deleteCharAt(index);  //build的每一位都是树结构的一层，某个层有多个并行节点，均应该占据build的同一位置
                // 可以想象为数状结构，从上而下对build的复用程度逐步发散，对应同一层的节点，先添加某个节点，再删除替换为另外一个节点
            }
        }
    }


    int ans = 0;
    int[][] grid;

    /**
     * 1219. 黄金矿工
     */
    public int getMaximumGold(int[][] grid) {
        this.grid = grid;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] > 0)
                    dfs(i, j, 0);
            }
        }
        return ans;
    }

    private void dfs(int i, int j, int gold) {
        //检查是否触达边界（递归过程中判断），或者不可前进
        if (i == -1 || j == -1 || i == grid.length || j == grid[0].length || grid[i][j] == 0) return;
        //1、针对当前位点的处理
        int currentPosGold = grid[i][j];
        gold += currentPosGold;
        grid[i][j] = 0;  //当前递归，不能再途径此位点

        //----------------------------------------------------------------------
        // 理论上，每次移动都会使得 gold变大，但 max的作用就是统一各个递归路径的累加值
        //----------------------------------------------------------------------

        ans = Math.max(ans, gold);  //核心：

        //2、针对下一步位点的处理
        dfs(i + 1, j, gold);   //四个方向均尝试移动，但是否可以挖掘，在内部判断
        dfs(i - 1, j, gold);
        dfs(i, j + 1, gold);
        dfs(i, j - 1, gold);

        grid[i][j] = currentPosGold;
    }


    /**
     * 22. 括号生成
     */
    public List<String> generateParenthesis(int n) {
        ArrayList<String> ans = new ArrayList<>();
        if (n <= 0)
            return ans;
        dfs("", 0, 0, n, ans);  //根
        return ans;
    }

    private void dfs(String paths, int left, int right, int n, List<String> ans) {
        if (right > left)   //剪枝
            return;
        if (left == n && right == n) {  //满足条件
            ans.add(paths);
            return;
        }

        if (left < n)
            dfs(paths + "(", left + 1, right, n, ans);
        if (right < n)
            dfs(paths + ")", left, right + 1, n, ans);
    }


    /**
     * 46. 全排列
     */
    public List<List<Integer>> permute(int[] nums) {
        int len = nums.length;
        // 使用一个动态数组记录所有可能的排列
        List<List<Integer>> res = new ArrayList<>();
        if (len == 0)
            return res;

        // 通过一个 boolean数组来记录各个位点数字的使用情况
        boolean[] used = new boolean[len];
        ArrayDeque<Integer> path = new ArrayDeque<>();

        // 深度优先搜索
        dfs(nums, len, 0, path, used, res);

        return res;
    }

    private void dfs(int[] nums, int len, int depth, ArrayDeque<Integer> path, boolean[] used, List<List<Integer>> res) {

        if (depth == len) {
            res.add(new ArrayList<>(path));
            return;
        }

        for (int i = 0; i < len; i++) {
            if (!used[i]) {
                path.addLast(nums[i]);  //向队列尾部添加元素
                used[i] = true;         //标识此位置的元素已经使用

                dfs(nums, len, depth + 1, path, used, res);

                path.removeLast();     //移除最后一个元素
                used[i] = false;       //标识此位置的元素没有使用
            }
        }
    }


    /**
     * 39. 组合总和
     */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        if (candidates.length == 0)
            return ans;

        Deque<Integer> path = new ArrayDeque<>();
        dfs(candidates, 0, target, path, ans);

        return ans;
    }

    private void dfs(int[] candidates, int begin, int target, Deque<Integer> path, List<List<Integer>> ans) {
        if (target < 0)     //剪枝
            return;
        if (target == 0) {
            ans.add(new ArrayList<>(path));
            return;
        }

        for (int i = begin; i < candidates.length; i++) {  //从begin开始的含义：避免不同分支间无重复
            path.addLast(candidates[i]);
            //注意：每一个元素可以重复，所以下一次搜索的起点仍为此元素
            dfs(candidates, i, target - candidates[i], path, ans);
            //状态重置
            path.removeLast();
        }
    }

    //---------------------------------------------------------------------------------------------
    // 什么时候使用 used 数组，什么时候使用 begin 变量
    //   1、排列问题
    //        讲究顺序（即 [2, 2, 3] 与 [2, 3, 2] 视为不同列表时），需要记录哪些数字已经使用过，此时用 used 数组；
    //   2、组合问题
    //        不讲究顺序（即 [2, 2, 3] 与 [2, 3, 2] 视为相同列表时），需要按照某种顺序搜索，此时使用 begin 变量。
    //---------------------------------------------------------------------------------------------




}
