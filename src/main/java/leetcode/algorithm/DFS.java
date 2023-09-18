package leetcode.algorithm;

import java.util.*;


//---------------------------------------------------------------------------------------------
// 什么时候使用 used 数组，什么时候使用 begin 变量
//   1、排列问题
//        讲究顺序（即 [2, 2, 3] 与 [2, 3, 2] 视为不同列表时），需要记录哪些数字已经使用过，此时用 used 数组；
//   2、组合问题
//        不讲究顺序（即 [2, 2, 3] 与 [2, 3, 2] 视为相同列表时），需要按照某种顺序搜索，此时使用 begin 变量。
//---------------------------------------------------------------------------------------------


public class DFS {

    /**
     * 797. 所有可能的路径
     */
    public List<List<Integer>> allPathsSourceTarget(int[][] graph) {
        List<List<Integer>> ans = new ArrayList<>();
        ArrayList<Integer> path = new ArrayList<>();
        path.add(0);
        dfs(graph, 0, path, ans);
        return ans;
    }

    private void dfs(int[][] graph, int currentIndex, List<Integer> path, List<List<Integer>> ans) {
        if (currentIndex == graph.length - 1) {   //遇到结尾
            ans.add(new ArrayList<>(path));   //添加
            return;   //返回
        }
        //否则，在当前 pos 上，逐一搜索对应的路径
        for (int i = 0; i < graph[currentIndex].length; i++) {
            path.add(graph[currentIndex][i]);
            dfs(graph, graph[currentIndex][i], path, ans);
            path.remove(path.size() - 1);
        }
    }


    /**
     * 78. 子集
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        subsetsDfs(nums, ans, path, 0);
        return ans;
    }

    private void subsetsDfs(int[] nums, List<List<Integer>> ans, LinkedList<Integer> path, int startIndex) {
        ans.add(new ArrayList<>(path));
        if (startIndex == nums.length) {  //其实不需要
            return;
        }

        //----------------------------------------------------------------------------------------------------------------
        // 每一层的横向搜索其实都是在枚举 path 中同一个位置（待插入位置）的元素，当前递归到第几层了，就是改在 path 中插入第几个元素了
        //----------------------------------------------------------------------------------------------------------------

        for (int i = startIndex; i < nums.length; i++) {
            path.add(nums[i]);
            subsetsDfs(nums, ans, path, i + 1);  //从当前枚举元素的下一位开始
            path.removeLast();
        }

    }

    //------------------------------------------------------------------------------------
    // 上面题目由于元素没有重复，故子集中不能存在以下情况：
    //     [1,2] [2,1]
    // 下面题目由于元素存在重复，故子集中在上面情况的前提下，仍不可存在情况：
    //     [1,2] [1,2]的情况
    //     因此下面题目中不可存在：
    //         [1,2] [2,1] [1,2] [2,1]
    //  这两类题目的关键在于：
    //     每次横向搜索均从 当前 currentIndex 向后横向枚举遍历，而不能用 used[i]
    //         因为 used[i] 搜索的结果会使得 [1,2] [2,1]同时存在
    //------------------------------------------------------------------------------------


    /**
     * 90. 子集 II
     */
    public List<List<Integer>> subsetsWithDup(int[] nums) {   //给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的子集（幂集）
        Arrays.sort(nums);
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        subsetsWithDupDfs(nums, ans, path, 0);
        return ans;
    }

    private void subsetsWithDupDfs(int[] nums, List<List<Integer>> ans, LinkedList<Integer> path, int startIndex) {
        ans.add(new ArrayList<>(path));
        if (startIndex == nums.length) {
            return;
        }
        //---------------------------------------------------------------------------------------------------
        // 每层深度优先搜索，对应 path 中一个索引位置，而每层深度优先搜索中的横向枚举，其实是枚举当前位置可用的元素
        //---------------------------------------------------------------------------------------------------
        for (int i = startIndex; i < nums.length; i++) {
            if (i > startIndex && nums[i] == nums[i - 1]) {
                continue;
            }
            path.add(nums[i]);
            subsetsWithDupDfs(nums, ans, path, i + 1);
            path.removeLast();
        }
    }

    public List<List<Integer>> subsetsWithDup000(int[] nums) {   //给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的子集（幂集）
        Arrays.sort(nums);  //排序
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        ans.add(new ArrayList<>(path));
        subsetsWithDupDfs000(nums, ans, path, 0);
        return ans;
    }

    //标准处理，没有使用 used[i]
    private void subsetsWithDupDfs000(int[] nums, List<List<Integer>> ans, LinkedList<Integer> path, int currentIndex) {
        if (currentIndex == nums.length) {   //递归终止
            return;
        }

        //---------------------------------------------------------------------------------------------------
        // 每层深度优先搜索，对应 path 中一个索引位置，而每层深度优先搜索中的横向枚举，其实是枚举当前位置可用的元素
        //---------------------------------------------------------------------------------------------------

        // 从 currentIndex，因为 [1,2] 与 [2,1]相同
        for (int i = currentIndex; i < nums.length; i++) {  //横向枚举搜索
            //剪枝，横向剔除
            if (i > currentIndex && nums[i] == nums[i - 1]) {  // used[i - 1] == 0 是关键，即在同一层中/同一位置
                //保证在同一个层中，同一位不会使用相同的数字；保证在不同层中，不同位可以使用相同的数字
                continue;
            }

            //1、添加元素
            path.add(nums[i]);
            ans.add(new ArrayList<>(path));
            //2、纵向递归
            subsetsWithDupDfs000(nums, ans, path, i + 1);  //纵向递归搜索，一定是 i + 1，一定不能为 currentIndex + 1

            //3、移除元素
            path.removeLast();
        }
    }


    //非标准处理，使用了 used[i]
    public List<List<Integer>> subsetsWithDup00(int[] nums) {   //给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的子集（幂集）
        Arrays.sort(nums);  //排序
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        int[] used = new int[nums.length];
        ans.add(new ArrayList<>(path));
        subsetsWithDupDfs00(nums, used, ans, path, 0);
        return ans;
    }

    private void subsetsWithDupDfs00(int[] nums, int[] used, List<List<Integer>> ans, LinkedList<Integer> path, int currentIndex) {
        if (currentIndex == nums.length) {   //递归终止
            return;
        }

        // 从 currentIndex，因为 [1,2] 与 [2,1]相同
        for (int i = currentIndex; i < nums.length; i++) {  //横向枚举搜索
            //--------------------------------------------------
            // 在 [1,2] 与 [2,1]均需要保存的情况下，i 每次都需要从 0 开始遍历，此时需要结合 used[]来判断是否使用过，即下面的逻辑
            //--------------------------------------------------
//            if (used[i] == 1) {  //如果当前数值已经使用，则跳过此数值，
//                continue;
//            }
            //剪枝，横向剔除
            if (i > 0 && nums[i] == nums[i - 1] && used[i - 1] == 0) {  // used[i - 1] == 0 是关键，即在同一层中/同一位置
                //保证在同一个层中，同一位不会使用相同的数字；保证在不同层中，不同位可以使用相同的数字
                continue;
            }

            //1、添加元素
            path.add(nums[i]);
            used[i] = 1;           //主要用于判断同一个位置重复使用同一个值的元素
            ans.add(new ArrayList<>(path));
            //2、纵向递归
            subsetsWithDupDfs00(nums, used, ans, path, i + 1);  //纵向递归搜索，一定是 i + 1，一定不能为 currentIndex + 1

            //3、移除元素
            path.removeLast();
            used[i] = 0;
        }
    }


    public List<List<Integer>> subsetsWithDup01(int[] nums) {
        Arrays.sort(nums);  //排序
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        int[] used = new int[nums.length];
        subsetsWithDupDfs01(nums, used, ans, path, 0);
        return ans;
    }

    private void subsetsWithDupDfs01(int[] nums, int[] used, List<List<Integer>> ans, LinkedList<Integer> path, int currentIndex) {
        //上下两种写法，在此处有差异
        ans.add(new ArrayList<>(path));
        if (currentIndex == nums.length) {   //递归终止
            return;
        }

        for (int i = currentIndex; i < nums.length; i++) {  //横向枚举搜索
            //剪枝，横向剔除
            if (i > 0 && nums[i] == nums[i - 1] && used[i - 1] == 0) {  // used[i - 1] == 0 是关键
                //保证在同一个层中，同一位不会使用相同的数字；保证在不同层中，不同位可以使用相同的数字
                continue;
            }

            //1、添加元素
            path.add(nums[i]);
            used[i] = 1;

            //2、纵向递归
            subsetsWithDupDfs01(nums, used, ans, path, i + 1);  //纵向递归搜索

            //3、移除元素
            path.removeLast();
            used[i] = 0;
        }
    }


    //------------------------------------------
    // 排列问题： [1,2],[2,1]视为不同的解
    //------------------------------------------


    /**
     * 46. 全排列
     */
    public List<List<Integer>> permute(int[] nums) {  //给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        int[] used = new int[nums.length];
        permuteDfs(nums, ans, path, used, 0);
        return ans;
    }


    private void permuteDfs(int[] nums, List<List<Integer>> ans, LinkedList<Integer> path, int[] used, int window) {
        if (window == nums.length) {
            ans.add(new ArrayList<>(path));
            return;
        }
        for (int i = 0; i < nums.length; i++) { //横向搜索
            if (used[i] == 1) {
                continue;
            }
            //--------------------------------
            // 针对循环到每个元素 A，当前组合为 M，都将执行以下三步操作：
            //    1、添加进组合 M->N
            //    2、基于新组合 N，进行纵向搜索
            //    3、从组合 N中剔除 N->M
            //--------------------------------

            //1、组合中新增元素，并将其标记为使用，防止纵向搜索的过程中，重复使用同一个元素
            path.add(nums[i]);
            used[i] = 1;

            //2、基于当前组合，进行纵向搜索
            permuteDfs(nums, ans, path, used, window + 1);  //纵向搜索

            //3、组合中剔除元素
            path.removeLast();  //将上面新增的元素剔除出组合，并将其标记为未使用，以备后续搜索过程中使用
            used[i] = 0;
        }
    }


    /**
     * 47. 全排列 II
     */
    public List<List<Integer>> permuteUniqueIIII(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        int[] visited = new int[nums.length];
        permuteUniqueDfsIIII(nums, visited, ans, path);
        return ans;
    }

    private void permuteUniqueDfsIIII(int[] nums, int[] visited, List<List<Integer>> ans, LinkedList<Integer> path) {
        if (path.size() == nums.length) {
            ans.add(new ArrayList<>(path));
            return;
        }

        //------------------------------------------------------------------------------------------
        // 当前层其实是在枚举 path中某个位置的所有可用值，这些可用值是除了此路径下已经使用的元素外的所有元素，当然也要剔除当前层重复的元素
        //------------------------------------------------------------------------------------------

        for (int i = 0; i < nums.length; i++) {
            if (visited[i] == 1) {
                continue;
            }
            if (i > 0 && nums[i] == nums[i - 1] && visited[i - 1] == 0) {
                continue;
            }

            path.add(nums[i]);
            visited[i] = 1;
            permuteUniqueDfsIIII(nums, visited, ans, path);
            path.removeLast();
            visited[i] = 0;
        }
    }


    public List<List<Integer>> permuteUnique(int[] nums) { //给定一个可包含重复数字的序列 nums ，按任意顺序 返回所有不重复的全排列。
        List<List<Integer>> ans = new ArrayList<>();
        int[] used = new int[nums.length];
        LinkedList<Integer> path = new LinkedList<>();
        permuteUniqueDfs(nums, used, ans, path, 0);
        return ans;
    }

    private void permuteUniqueDfs(int[] nums, int[] used, List<List<Integer>> ans, LinkedList<Integer> path, int deep) {
        if (deep == nums.length) {
            ans.add(new ArrayList<>(path));
            return;
        }
        //关键在于水平方向的剪枝和去重，即同一个位置不使用相同的元素
        int[] rowUsed = new int[21];  //在每一层中标识，不使用相同的元素
        for (int i = 0; i < nums.length; i++) {
            if (used[i] == 1) {                //1、纵向递归剪枝，如果路径中已经使用，则跳出
                continue;
            }
            if (rowUsed[nums[i] + 10] == 1) {  //2、横向枚举剪枝，同一层中已经被使用，则不再使用，避免重复
                continue;
            }
            //1、增加元素
            path.add(nums[i]);
            rowUsed[nums[i] + 10] = 1;   //横向标识为已经使用，标记此数值已经被使用
            used[i] = 1;                 //纵向标记为已经使用，标记此位置的数据已经被使用

            //2、基于当前组合，纵向递归搜索
            permuteUniqueDfs(nums, used, ans, path, deep + 1);

            //3、删除元素
            path.removeLast();
            used[i] = 0;
        }
    }


    //--------------------------------------------------------------------------------------------
    // 上下两种写法最大的区别在于：横向如何去重
    //     1、上面写法：横向去重（同一位点使用不同的数值）、纵向记录已使用的位点，二者使用两个参数分别处理
    //     2、下面写法：横向去重（同一位点使用不同的数值）、纵向记录已使用的位点，二者使用一个参数统一处理
    //--------------------------------------------------------------------------------------------

    // https://leetcode.cn/problems/permutations-ii/solution/hui-su-suan-fa-python-dai-ma-java-dai-ma-by-liwe-2/
    public List<List<Integer>> permuteUnique01(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        int[] used = new int[nums.length];
        Arrays.sort(nums);   //关键：排序，后去去重的关键
        LinkedList<Integer> path = new LinkedList<>();
        permuteUniqueDfs01(nums, used, ans, path, 0);
        return ans;
    }

    private void permuteUniqueDfs01(int[] nums, int[] used, List<List<Integer>> ans, LinkedList<Integer> path, int deep) {
        if (deep == nums.length) {
            ans.add(new ArrayList<>(path));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i] == 1) {                      //1、纵向递归剪枝，如果路径中已经使用，则跳出
                continue;
            }
            //好好理解一下 used[i - 1] == 0
            if (i > 0 && nums[i] == nums[i - 1] && used[i - 1] == 0) {  //2、横向枚举剪枝，同一层中已经被使用，则不再使用，避免重复
                continue;
            }
            //1、增加元素
            path.add(nums[i]);
            used[i] = 1;                 //纵向标记为已经使用，标记此位置的数据已经被使用

            //2、基于当前组合，纵向递归搜索
            permuteUniqueDfs01(nums, used, ans, path, deep + 1);

            //3、删除元素
            path.removeLast();
            used[i] = 0;
        }
    }


    /**
     * 77. 组合
     */
    public List<List<Integer>> combine(int n, int k) {
        int[] nums = new int[n];
        for (int i = 0; i < nums.length; i++)
            nums[i] = i + 1;
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        combineDfs(nums, k, ans, path, 0);
        return ans;
    }

    private void combineDfs(int[] nums, int k, List<List<Integer>> ans, LinkedList<Integer> path, int currentIndex) {
        if (path.size() > k)      //纵向剪枝
            return;
        if (path.size() == k) {
            ans.add(new ArrayList<>(path));
            return;
        }


        //--------------------------------
        // 针对循环到每个元素 A，当前组合为 M，都将执行以下三步操作：
        //    1、添加进组合 M->N
        //    2、基于新组合 N，进行纵向搜索
        //    3、从组合 N中剔除 N->M
        //--------------------------------
        for (int i = currentIndex; i < nums.length; i++) {  //横向搜索
            //---------------------------------------------
            // 纵向递归搜索，横向枚举搜索
            //    终止条件：
            //        1、纵向递归触底
            //        2、横向枚举结束
            //---------------------------------------------

            //1、组合中新增元素 A
            path.add(nums[i]);

            //2、基于当前组合，纵向搜索
            combineDfs(nums, k, ans, path, i + 1);

            //3、组合中剔除元素 A
            path.removeLast();
        }
    }


    /**
     * 39. 组合总和
     * https://leetcode.cn/problems/combination-sum/solution/hui-su-suan-fa-jian-zhi-python-dai-ma-java-dai-m-2/
     */
    public List<List<Integer>> combinationSum(int[] candidates, int target) { //给你一个 无重复元素 的整数数组 candidates 和一个目标整数 target ，找出 candidates 中可以使数字和为目标数 target 的 所有 不同组合
        Arrays.sort(candidates);   //排序，用于横向剪枝
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        combinationDfs(candidates, ans, path, 0, target, 0);
        return ans;
    }

    private void combinationDfs(int[] candidates, List<List<Integer>> ans, LinkedList<Integer> path, int sum, int target, int currentIndex) {
        if (sum > target)      //1、纵向剪枝：仅针对当前元素，不满足条件，进行剪枝，即终止纵向搜索
            return;
        if (sum == target)
            ans.add(new ArrayList<>(path));
        for (int i = currentIndex; i < candidates.length; i++) {  //横向搜索，逐个遍历
            //2、横向剪枝：在待遍历的数组是有序的情况下，如果当前元素不满足条件，则后续待遍历元素也不满足条件，故不再遍历，进行横向剪枝，即终止横向搜索
            if (sum + candidates[i] > target)
                break;
            path.add(candidates[i]);
            combinationDfs(candidates, ans, path, sum + candidates[i], target, i);  //仍从currentIndex开始，保证能取到自身
            path.removeLast();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // 第 39  题：candidates 是无重复元素的数组，数组中每个数字可以无限制重复被选取（一个 1，可以被使用无数次 ）
    // 与以下两题做对比：
    //     第 40  题：candidates 是有重复元素的数组，数组中每个数字在每个组合中只能使用一次（两个 1，最多可使用两次，而并不是数字 1 只能用 1 次）
    //     第 216 题：candidates 是无重复元素的数组，数组中每个数字在每个组合中只能使用一次（一个 1，最多可使用一次）
    //------------------------------------------------------------------------------------------------------------------

    /**
     * 40. 组合总和 II
     * https://leetcode.cn/problems/combination-sum-ii/solution/hui-su-suan-fa-jian-zhi-python-dai-ma-java-dai-m-3/
     */
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        combinationSum2Dfs(candidates, target, ans, path, 0, 0);
        return ans;
    }

    private void combinationSum2Dfs(int[] candidates, int target, List<List<Integer>> ans, LinkedList<Integer> path, int sum, int currentIndex) {
        if (sum == target) {
            ans.add(new ArrayList<>(path));
            return;
        }
        for (int i = currentIndex; i < candidates.length; i++) {  //横向枚举遍历
            if (sum + candidates[i] > target)  //横向剪枝
                break;

            //横向过滤：避免同一侧层中出现相同的元素，因为其对应的结果一致，同时保证纵向可获取值相同的另一个元素
            if (i > currentIndex && candidates[i] == candidates[i - 1]) //关键：i > currentIndex保证纵向可重复，但横向不可重复
                continue;

            //1、增加元素
            path.add(candidates[i]);

            //2、纵向递归遍历
            combinationSum2Dfs(candidates, target, ans, path, sum + candidates[i], i + 1);  //同一个元素不可重复使用，所以 + 1

            //3、剔除元素
            path.removeLast();
        }
    }

    private void combinationSum2Dfs(int[] candidates, List<List<Integer>> ans, LinkedList<Integer> path, int target, int currentIndex, int sum) {
        //递归终止条件一：越界
        if (currentIndex >= candidates.length) {
            return;
        }

        for (int i = currentIndex; i < candidates.length; i++) {
            //横向剔除重复元素
            if (i > currentIndex && candidates[i] == candidates[i - 1]) {
                continue;
            }

            if (sum + candidates[i] > target) {
                break;
            }
            //1、增加元素
            sum += candidates[i];
            path.add(candidates[i]);

            //剪枝
            if (sum == target) {
//                sum -= candidates[i];
                ans.add(new ArrayList<>(path));   //一直没搞清楚，sum和path为什么一个可以一个不可以
                path.removeLast();
                return;
            }

            //2、纵向深度搜索
            combinationSum2Dfs(candidates, ans, path, target, i + 1, sum);  //纵向不可使用同一个元素

            //3、剔除元素
            sum -= candidates[i];
            path.removeLast();
        }
    }

    //-----------------------------------------------------------------------------------
    // 对比上下两种写法的差异，避免将 return 放在 for 循环内部，会导致部分递归和回溯不匹配不完整
    //-----------------------------------------------------------------------------------

    private void combinationSum2Dfs02(int[] candidates, List<List<Integer>> ans, LinkedList<Integer> path, int target, int currentIndex, int sum) {
        //递归终止条件一：越界
        if (currentIndex >= candidates.length) {
            return;
        }

        //剪枝
        if (sum == target) {
            ans.add(new ArrayList<>(path));
//            path.removeLast();
            return;
        }

        for (int i = currentIndex; i < candidates.length; i++) {
            //横向剔除重复元素
            if (i > currentIndex && candidates[i] == candidates[i - 1]) {
                continue;
            }

            if (sum + candidates[i] > target) {
                break;
            }
            //1、增加元素
            sum += candidates[i];
            path.add(candidates[i]);

            //2、纵向深度搜索
            combinationSum2Dfs02(candidates, ans, path, target, i + 1, sum);  //纵向不可使用同一个元素

            //3、剔除元素
            sum -= candidates[i];
            path.removeLast();
        }
    }


    /**
     * 216. 组合总和 III
     */
    public List<List<Integer>> combinationSum3(int count, int target) {  //只使用数字 1到 9，每个数字 最多使用一次
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        combinationSum3Dfs(target, count, ans, path, 0, 1);
        return ans;
    }

    private void combinationSum3Dfs(int target, int count, List<List<Integer>> ans, LinkedList<Integer> path, int sum, int current) {
        if (sum == target && path.size() == count) {  //纵向终止条件，纵向剪枝
            ans.add(new ArrayList<>(path));
            return;
        }
        for (int num = current; num < 10; num++) {  //横向枚举遍历
            //横向剪枝
            if (sum > target)  //不加的话，效率低
                break;

            //1、添加元素
            path.add(num);

            //2、纵向递归遍历
            combinationSum3Dfs(target, count, ans, path, sum + num, num + 1);

            //3、剔除元素
            path.removeLast();
        }
    }


    /**
     * 17. 电话号码的字母组合
     */
    public List<String> letterCombinations(String digits) {
        ArrayList<String> ans = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        if (digits.length() == 0)   //剔除空字符串
            return ans;
        HashMap<Character, String> hTable = new HashMap<>();
        hTable.put('2', "abc");
        hTable.put('3', "def");
        hTable.put('4', "ghi");
        hTable.put('5', "jkl");
        hTable.put('6', "mno");
        hTable.put('7', "pqrs");
        hTable.put('8', "tuv");
        hTable.put('9', "wxyz");
        letterCombinationsDfs(digits, hTable, ans, builder, 0);
        return ans;
    }

    private void letterCombinationsDfs(String digits, HashMap<Character, String> hTable, ArrayList<String> ans, StringBuilder builder, int currentIndex) {
        if (builder.length() == digits.length()) {
            ans.add(builder.toString());
            return;
        }
        //--------------------------------------------------------------------
        //类似"全排列"，不过差异在于：
        //   1、"全排列"是每一位基于一个相同的 int[] nums
        //   2、本题每一位 currentIndex使用的是不同 digitToWord
        //--------------------------------------------------------------------
        for (int i = 0; i < hTable.get(digits.charAt(currentIndex)).length(); i++) {  //横向枚举遍历，类似全排列，每次均从 0 开始遍历
            //1、添加元素
            builder.append(hTable.get(digits.charAt(currentIndex)).charAt(i));

            //2、纵向递归遍历
            letterCombinationsDfs(digits, hTable, ans, builder, currentIndex + 1);

            //3、添加元素
            builder.deleteCharAt(currentIndex);  //关键，而非 i，因为这一位为 digit.charAt(currentIndex)对应 digit的 word中所有枚举的情况
        }
    }


    /**
     * 417. 太平洋大西洋水流问题
     */
    public List<List<Integer>> pacificAtlantic(int[][] heights) {
        List<List<Integer>> ans = new ArrayList<>();
        int rows = heights.length;
        int columns = heights[0].length;
        boolean[][] reachPacific = new boolean[rows][columns];
        boolean[][] reachAtlantic = new boolean[rows][columns];
        //从左边界和右边界 开始 深度搜索
        for (int i = 0; i < rows; i++) {
            dfs(heights, reachPacific, i, 0);   // Pacific 从左边界开始深度搜搜
            dfs(heights, reachAtlantic, i, columns - 1);  // Atlantic 从右边界开始深度搜搜
        }
        //从上边界和下边界 开始 深度搜索
        for (int j = 0; j < columns; j++) {
            dfs(heights, reachPacific, 0, j);   // Pacific 从上边界开始深度搜索
            dfs(heights, reachAtlantic, rows - 1, j);  // Atlantic 从下边界开始深度搜索
        }
        //寻找重叠点：既可以流向 Pacific 也可以流向 Atlantic
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (reachAtlantic[i][j] && reachPacific[i][j])
                    ans.add(Arrays.asList(i, j));
            }
        }
        return ans;
    }

    private void dfs(int[][] heights, boolean[][] reach, int i, int j) {
        if (reach[i][j]) return;  //重叠交叉，不重复搜索，也是迭代终止条件
        reach[i][j] = true;

        //-------------------------------------------------------------
        // 在 矩阵内部（矩阵四个边已经搜索过，不再搜索） 向四个方向搜索
        //-------------------------------------------------------------

        //向上搜索
        if (i - 1 >= 0 && heights[i - 1][j] >= heights[i][j])   //不搜索上边界 && 下降趋势的位置
            dfs(heights, reach, i - 1, j);
        //向下搜索
        if (i + 1 < heights.length && heights[i + 1][j] >= heights[i][j])  //不搜索下边界（无等号） && 下降趋势的位置
            dfs(heights, reach, i + 1, j);
        //向左搜索
        if (j - 1 >= 0 && heights[i][j - 1] >= heights[i][j])   //不搜索左边界 && 下降趋势的位置
            dfs(heights, reach, i, j - 1);
        //向右搜索
        if (j + 1 < heights[0].length && heights[i][j + 1] >= heights[i][j])   //不搜索右边界（无等号） && 下降趋势的位置
            dfs(heights, reach, i, j + 1);
    }


    public List<List<Integer>> pacificAtlantic01(int[][] heights) {
        List<List<Integer>> ans = new ArrayList<>();
        int rows = heights.length;
        int columns = heights[0].length;
        boolean[][] reachPacific = new boolean[rows][columns];
        boolean[][] reachAtlantic = new boolean[rows][columns];
        for (int i = 0; i < rows; i++) {  //从左、右两边开始搜索
            pacificAtlanticDfs(heights, reachPacific, i, 0);
            pacificAtlanticDfs(heights, reachAtlantic, i, columns - 1);
        }
        for (int j = 0; j < columns; j++) {  //从上、下两边开始搜索
            pacificAtlanticDfs(heights, reachPacific, 0, j);
            pacificAtlanticDfs(heights, reachAtlantic, rows - 1, j);
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (reachAtlantic[i][j] && reachPacific[i][j])
                    ans.add(Arrays.asList(i, j));
            }
        }
        return ans;
    }


    private void pacificAtlanticDfs(int[][] heights, boolean[][] reach, int i, int j) {  //与上差异
        if (reach[i][j]) return;
        reach[i][j] = true;  //能到这里，逻辑在下面封装

        //-----------------------------
        // 四方搜索：上、下、左、右
        //    注意：搜索过程中，保证前一位能触达边界，然后对重复的可通过上面 if剪枝
        //    原因：笼统来讲，起点是四条边界
        //          但本质为 Pacific从左边界和上边界开始，其并没有从右边界和下边界开始搜索，因此要保证从这两个边界开始的搜索能够触达右边界和下边界
        //          但本质为 Atlantic从右边界和下边界开始，其并没有从左边界和上边界开始搜索，因此要保证从这两个边界开始的搜索能够触达左边界和上边界
        //-----------------------------

        //下面 4 个判断来实现向 4 个方向搜索的过程，其本质类似于通过一个 for循环来实现的向 4 个方向搜索的过程，这里简化了

        //向上搜索
        if (i - 1 >= 0 && heights[i - 1][j] >= heights[i][j]) {
            pacificAtlanticDfs(heights, reach, i - 1, j);
        }

        //向下搜索
        if (i + 1 <= heights.length - 1 && heights[i + 1][j] >= heights[i][j]) {
            pacificAtlanticDfs(heights, reach, i + 1, j);
        }

        //向左搜索
        if (j - 1 >= 0 && heights[i][j - 1] >= heights[i][j]) {
            pacificAtlanticDfs(heights, reach, i, j - 1);
        }

        //向右搜索
        if (j + 1 <= heights[0].length - 1 && heights[i][j + 1] >= heights[i][j]) {
            pacificAtlanticDfs(heights, reach, i, j + 1);
        }
    }


    public List<List<Integer>> pacificAtlantic02(int[][] heights) {
        List<List<Integer>> ans = new ArrayList<>();
        int rows = heights.length;
        int columns = heights[0].length;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};   //上、下、左、右
        boolean[][] reachPacific = new boolean[rows][columns];
        boolean[][] reachAtlantic = new boolean[rows][columns];
        for (int i = 0; i < rows; i++) {  //从左、右两边开始搜索
            pacificAtlanticDfs(heights, directions, reachPacific, i, 0);
            pacificAtlanticDfs(heights, directions, reachAtlantic, i, columns - 1);
        }
        for (int j = 0; j < columns; j++) {  //从上、下两边开始搜索
            pacificAtlanticDfs(heights, directions, reachPacific, 0, j);
            pacificAtlanticDfs(heights, directions, reachAtlantic, rows - 1, j);
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (reachAtlantic[i][j] && reachPacific[i][j])
                    ans.add(Arrays.asList(i, j));
            }
        }
        return ans;
    }

    private void pacificAtlanticDfs(int[][] heights, int[][] directions, boolean[][] reach, int i, int j) {
        if (reach[i][j]) {
            return;
        }
        reach[i][j] = true;
        for (int[] direction : directions) {   //横向枚举搜索，4个方向
            int nextRow = i + direction[0];
            int nextCol = j + direction[1];
            if (nextRow >= 0 && nextRow < heights.length && nextCol >= 0 && nextCol < heights[0].length
                    && heights[nextRow][nextCol] >= heights[i][j]) {
                pacificAtlanticDfs(heights, directions, reach, nextRow, nextCol);  //纵向递归搜索
            }
        }
    }

    /**
     * 856. 括号的分数
     */
    public int scoreOfParentheses(String str) {
        return scoreOfParenthesesDfs(str, 0, str.length() - 1);
    }

    private int scoreOfParenthesesDfs(String str, int left, int right) {
        int ans = 0;        //当前层的分数，即以当前右边界结尾的括号对的分数
        int balance = 0;    //当前层的动态平衡指标，用于衡量是否对当前层的括号再进行匹配

        for (int i = left; i <= right; i++) {   //left代表的为区间的左边界，一定是 '('，i在尝试搜索区间右边界，范围是小于 right
            char xx = str.charAt(i);
            balance += xx == '(' ? 1 : -1;

            //-------------------------------------------------------------------
            // 虽然遍历是 i 向右在搜索，但每次满足条件的是区间，其特点是 从两端到中间
            //-------------------------------------------------------------------

            if (balance == 0) {        //一定是遇到右边界了
                if (i == left + 1)          //1.最小单元的括号对 ()
                    ans++;                                                      //积分 1
                else                        //2.多个括号对组合
                    ans += 2 * scoreOfParenthesesDfs(str, left + 1, i - 1);    //递归回溯结果积分

                //在内部跳动，如 (()()()) 时移动的方式，如果不存在这种情况，在此计算业务影响，因为left时层级别的，非全局的
                left = i + 1;   //关键
            }
        }
        return ans;
    }

    /**
     * 1020. 飞地的数量
     */
    public int numEnclaves(int[][] grid) {
        int ans = 0;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        //从上下左右四个边界开始感染陆地
        for (int i = 0; i < grid.length; i++) {
            numEnclavesDfs(grid, directions, i, 0);
            numEnclavesDfs(grid, directions, i, grid[0].length - 1);
        }
        for (int j = 0; j < grid[0].length; j++) {
            numEnclavesDfs(grid, directions, 0, j);
            numEnclavesDfs(grid, directions, grid.length - 1, j);
        }
        //查找内陆块的面积
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) ans++;
            }
        }
        return ans;
    }

    private void numEnclavesDfs(int[][] grid, int[][] directions, int row, int col) {
        //递归终止条件一：边界
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
            return;
        }
        //递归终止条件二：水域
        if (grid[row][col] == 0) {
            return;
        }

        grid[row][col] = 0;  //感染

        for (int[] dir : directions) {  //横向枚举遍历
            int nextRow = row + dir[0];
            int nextCol = col + dir[1];
            numEnclavesDfs(grid, directions, nextRow, nextCol);  //纵向递归遍历
        }
    }


    /**
     * 130. 被围绕的区域
     */
    public void solve(char[][] board) {   //巧妙思路：从边开始感染
        int rows = board.length - 1;
        int cols = board[0].length - 1;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};  //上、下、左、右
        //从第一列和最后一列开始搜索
        for (int i = 0; i <= rows; i++) {
            if (board[i][0] == 'O') {
                solveDfs(board, directions, i, 0);
            }
            if (board[i][cols] == 'O') {
                solveDfs(board, directions, i, cols);
            }
        }
        //从第一行和最后一行开始搜索
        for (int j = 0; j <= cols; j++) {
            if (board[0][j] == 'O') {
                solveDfs(board, directions, 0, j);
            }
            if (board[rows][j] == 'O') {
                solveDfs(board, directions, rows, j);
            }
        }

        //重新赋值
        for (int i = 0; i <= rows; i++) {
            for (int j = 0; j <= cols; j++) {
                if (board[i][j] == 'X')
                    continue;
                if (board[i][j] == 'O') board[i][j] = 'X';
                if (board[i][j] == 'Y') board[i][j] = 'O';
            }
        }
        System.out.println(Arrays.deepToString(board));
    }

    private void solveDfs(char[][] board, int[][] directions, int row, int col) {
        //终止条件一：越界
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return;
        }
        //终止条件二：陆地
        if (board[row][col] == 'X') {
            return;
        }

        //终止条件三：已搜索
        if (board[row][col] == 'Y') {
            return;
        }

        //感染
        board[row][col] = 'Y';

        for (int[] dir : directions) {
            int nextRow = row + dir[0];
            int nextCol = col + dir[1];
            solveDfs(board, directions, nextRow, nextCol);
        }
    }


    /**
     * 130. 被围绕的区域
     */
    public void solve01(char[][] board) {  //基于广度优先搜索的算法
        int m = board.length;
        int n = board[0].length;
        int[][] directions = {{1, 0}, {-1, 0}, {0, -1}, {0, 1}};
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {
                    if (board[i][j] == 'O') {
                        queue.addLast(new int[]{i, j});
                        board[i][j] = 'Y';
                    }
                }
            }
        }
        while (!queue.isEmpty()) {
            int[] address = queue.pollFirst();
            for (int[] dir : directions) {
                int nextRow = address[0] + dir[0];
                int nextCol = address[1] + dir[1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    if (board[nextRow][nextCol] == 'O') {
                        queue.addLast(new int[]{nextRow, nextCol});
                        board[nextRow][nextCol] = 'Y';
                    }
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'Y') board[i][j] = 'O';
                else board[i][j] = 'X';
            }
        }
    }

    /**
     * 1254. 统计封闭岛屿的数目
     */
    public int closedIsland(int[][] grid) {
        int ans = 0;
        int row = grid.length;
        int col = grid[0].length;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};  //上下左右
        //--------------------------------------------------------------------------------
        // 由于与边界接壤的陆地不会成为封闭岛，因此先将这部分陆地置为水域，不对其进行封闭岛的搜索
        //--------------------------------------------------------------------------------

        //1.1 从左右两边开始感染
        for (int i = 0; i < row; i++) {
            closedIslandDfs(grid, directions, i, 0);
            closedIslandDfs(grid, directions, i, col - 1);
        }
        //1.2 从上下两边开始感染
        for (int j = 0; j < col; j++) {
            closedIslandDfs(grid, directions, 0, j);
            closedIslandDfs(grid, directions, row - 1, j);
        }
        //----------------------------------------
        // 封闭岛屿的判断
        //----------------------------------------
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == 0) {  //封闭岛，基于感染的思想感染全岛，防止重复搜索
                    ans++;
                    closedIslandDfs(grid, directions, i, j);  //感染
                }
            }
        }
        return ans;
    }

    private void closedIslandDfs(int[][] grid, int[][] directions, int row, int col) {
        //递归终止条件一：边界
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
            return;
        }
        //递归终止条件二：水域
        if (grid[row][col] == 1) {
            return;
        }

        //将其变为水域，因为其不能构成封闭岛
        grid[row][col] = 1;

        for (int[] dir : directions) {   //横向枚举遍历
            int nextRow = row + dir[0];
            int nextCol = col + dir[1];
            closedIslandDfs(grid, directions, nextRow, nextCol);  //纵向递归搜索
        }
    }


    int sum = 0;

    /**
     * 1219. 黄金矿工
     */
    public int getMaximumGold(int[][] grid) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};  //上、下、左、右
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                getMaximumGoldDfs(grid, directions, 0, i, j);
            }
        }
        return sum;
    }


    private void getMaximumGoldDfs(int[][] grid, int[][] directions, int pathTotal, int i, int j) {
        if (grid[i][j] == 0)
            return;

        int currentGold = grid[i][j];
        //1、添加元素
        pathTotal += currentGold;
        grid[i][j] = 0;  //不可途径此位点

        sum = Math.max(sum, pathTotal);

        for (int[] direction : directions) {   //横向枚举搜索，4 个方向
            int nextRow = i + direction[0];
            int nextCol = j + direction[1];
            if (nextRow >= 0 && nextRow < grid.length && nextCol >= 0 && nextCol < grid[0].length) {
                //2、纵向递归搜索
                getMaximumGoldDfs(grid, directions, pathTotal, nextRow, nextCol);
            }
        }
        grid[i][j] = currentGold;  // 4 个方向搜索完成，则释放此位点
    }


    public int getMaximumGold01(int[][] grid) {   //错误写法：因为矿工可以从任意一点开始挖掘，而不一定要从四个边开始挖掘
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};  //上、下、左、右
        for (int i = 0; i < grid.length; i++) {
            //左边界
            getMaximumGoldDfs(grid, directions, 0, i, 0);
            //右边界
            getMaximumGoldDfs(grid, directions, 0, i, grid[0].length - 1);
        }
        for (int j = 0; j < grid[0].length; j++) {
            //上边界
            getMaximumGoldDfs(grid, directions, 0, 0, j);
            //下边界
            getMaximumGoldDfs(grid, directions, 0, grid.length - 1, j);
        }
        return sum;
    }


    /**
     * 661. 图片平滑器
     */
    public int[][] imageSmoother(int[][] img) {   //基于矩阵的方向
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


    /**
     * 79. 单词搜索
     */
    public boolean exist(char[][] board, String word) {
        //因为搜索是顺序迭代的，而不是并发执行的，因此此参数无需作为全局变量，但如果想要提高效率，应该尝试怎么共享
        int[][] existUsed = new int[board.length][board[0].length];
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};  //上、下、左、右
        if (board.length == 1 && board[0].length == 1 && word.length() == 1) {
            return board[0][0] == word.charAt(0);
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == word.charAt(0)) {
                    if (existDfs(board, directions, existUsed, i, j, word, 1))
                        return true;
                }
            }
        }
        return false;
    }

    private boolean existDfs(char[][] board, int[][] directions, int[][] existUsed, int row, int col, String word, int depth) {
        if (depth == word.length()) {
            return true;
        }

        //基于线路规划的想法，在for前更新变量，for后恢复变量
        existUsed[row][col] = 1;

        for (int i = 0; i < directions.length; i++) {   //同一个层，横向枚举搜索
            int nextRow = row + directions[i][0];
            int nextCol = col + directions[i][1];
            //搜索到边界
            if (nextRow == -1 || nextRow == board.length || nextCol == -1 || nextCol == board[0].length) {  //剪枝，此层不搜索这个方向
                continue;
            }
            //搜索到使用过的元素
            if (existUsed[nextRow][nextCol] == 1) {   //剪枝，此层不搜索这个方向
                continue;
            }
            //搜索位点不满足条件
            if (board[nextRow][nextCol] != word.charAt(depth)) {
                continue;
            }
            //纵向递归
            if (existDfs(board, directions, existUsed, nextRow, nextCol, word, depth + 1)) {
                return true;
            }
        }
        existUsed[row][col] = 0;
        //搜索完成，没有发现 true的情况，则均不满足，故返回 false
        return false;
    }


    /**
     * 200. 岛屿数量
     */
    public int numIslands(char[][] grid) {  //基于接触传染的思想
        int ans = 0;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};  //上下左右四个方向
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    ans++;
                    numIslandsDfs(grid, directions, i, j);
                }
            }
        }
        return ans;
    }

    private void numIslandsDfs(char[][] grid, int[][] directions, int i, int j) {
        if (grid[i][j] == '0') {  //递归终止条件
            return;
        }
        grid[i][j] = '0';  //被传染
        for (int[] direction : directions) {  //横向枚举遍历，4 个方向
            int nextRow = i + direction[0];
            int nextCol = j + direction[1];
            if (nextRow >= 0 && nextRow < grid.length && nextCol >= 0 && nextCol < grid[0].length) {  //纵向递归遍历，寻找新的节点
                numIslandsDfs(grid, directions, nextRow, nextCol);
            }
        }
    }


    public int numIslands01(char[][] grid) {  //广度优先搜索
        if (grid == null || grid.length == 0) {
            return 0;
        }

        int nr = grid.length;
        int nc = grid[0].length;
        int num_islands = 0;

        for (int r = 0; r < nr; ++r) {
            for (int c = 0; c < nc; ++c) {
                if (grid[r][c] == '1') {
                    ++num_islands;
                    grid[r][c] = '0';
                    Queue<Integer> neighbors = new LinkedList<>();
                    neighbors.add(r * nc + c);
                    while (!neighbors.isEmpty()) {
                        int id = neighbors.remove();
                        int row = id / nc;
                        int col = id % nc;
                        if (row - 1 >= 0 && grid[row - 1][col] == '1') {
                            neighbors.add((row - 1) * nc + col);
                            grid[row - 1][col] = '0';
                        }
                        if (row + 1 < nr && grid[row + 1][col] == '1') {
                            neighbors.add((row + 1) * nc + col);
                            grid[row + 1][col] = '0';
                        }
                        if (col - 1 >= 0 && grid[row][col - 1] == '1') {
                            neighbors.add(row * nc + col - 1);
                            grid[row][col - 1] = '0';
                        }
                        if (col + 1 < nc && grid[row][col + 1] == '1') {
                            neighbors.add(row * nc + col + 1);
                            grid[row][col + 1] = '0';
                        }
                    }
                }
            }
        }

        return num_islands;
    }


    int ans = 0;

    /**
     * 463. 岛屿的周长
     */
    public int islandPerimeter(int[][] grid) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};    //上、下、左、右
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    islandPerimeterDfs(grid, directions, i, j);
                    return ans;
                }
            }
        }
        return -1;
    }

    private void islandPerimeterDfs(int[][] grid, int[][] directions, int i, int j) {
        //------------------------------------------
        // 岛屿的边界为水，因此岛屿边界的判断：1、网格界外 2、网格内，但从某个方向触及到 水域
        //------------------------------------------

        //1、界外：从某个方向出界的情况，认为触达水域 '0'
        if (!(i >= 0 && i <= grid.length - 1 && j >= 0 && j <= grid[0].length - 1)) {
            ans++;  //此边为周长的一部分
            return;
        }
        //2、界内：从某个方向触达到水域 '0'
        if (grid[i][j] == 0) {
            ans++;  //此边为周长的一部分
            return;
        }
        //3、跳过已遍历的网点
        if (grid[i][j] == 2)
            return;

        grid[i][j] = 2;  //标识当前节点为已遍历过的节点，防止后续递归重复遍历

        for (int[] direction : directions) {  //横向枚举遍历，从 4 个方向
            int nextRow = i + direction[0];
            int nextCol = j + direction[1];
            islandPerimeterDfs(grid, directions, nextRow, nextCol); //纵向递归遍历，寻找新的节点
        }
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

    //---------------------------------------------------------------------------
    // 上下两种写法的差别在于：上面基于 grid2 自身原地的感染，而下面是基于 visited 感染
    //---------------------------------------------------------------------------

    public int countSubIslands01(int[][] grid1, int[][] grid2) {
        int ans = 0;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};  //上、下、左、右
        int[][] visited = new int[grid2.length][grid2[0].length];
        for (int i = 0; i < grid2.length; i++) {
            for (int j = 0; j < grid2[0].length; j++) {
                if (visited[i][j] == 0 && grid2[i][j] == 1) {
                    subIsland = true;
                    countSubIslandsDfs(grid1, grid2, directions, visited, i, j);
                    ans = subIsland ? ans + 1 : ans;
                }
            }
        }
        return ans;
    }

    private void countSubIslandsDfs(int[][] grid1, int[][] grid2, int[][] directions, int[][] visited, int row, int col) {
        //终止条件一：越界
        if (row < 0 || row >= grid2.length || col < 0 || col >= grid2[0].length) {
            return;
        }
        //终止条件二：已搜索
        if (visited[row][col] == 1) {
            return;
        }
        //终止条件三：子岛屿边界
        if (grid2[row][col] == 0) {   //此时在 grid2 中此位置就不是岛屿了，故不关心 grid1 中同一个位置是否为岛屿
            return;
        }

        //终止条件四：触碰到 grid1 界内的水域
        if (grid1[row][col] == 0) {   //grid2 中此位置为岛屿，但 grid1 中此位置不为岛屿
            subIsland = false;
        }

        //----------------------------------------------------------------------------------
        // 无论二号岛屿是否满足条件，均会将其全部搜索，感染全岛屿，即将当前二号子岛屿全部置为已经搜索
        //----------------------------------------------------------------------------------
        visited[row][col] = 1;    //已搜索

        //横向枚举搜索相邻位置
        for (int[] direction : directions) {
            int nextRow = row + direction[0];
            int nextCol = col + direction[1];
            //纵向递归搜索
            countSubIslandsDfs(grid1, grid2, directions, visited, nextRow, nextCol);
        }
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


    public int[][] floodFill01(int[][] image, int sr, int sc, int color) {   //广度优先搜索
        int m = image.length;
        int n = image[0].length;
        int[][] visited = new int[m][n];
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.addLast(new int[]{sr, sc});
        int prev = image[sr][sc];
        visited[sr][sc] = 1;
        image[sr][sc] = color;
        while (!queue.isEmpty()) {
            int[] tuple = queue.pollFirst();
            int row = tuple[0];
            int col = tuple[1];
            for (int[] dir : directions) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    if (visited[nextRow][nextCol] == 0) {
                        if (image[nextRow][nextCol] == prev) {
                            image[nextRow][nextCol] = color;
                            queue.addLast(new int[]{nextRow, nextCol});
                        }
                        visited[nextRow][nextCol] = 1;
                    }
                }
            }
        }
        return image;
    }

    int maxArea = 0;

    /**
     * 695. 岛屿的最大面积
     */
    public int maxAreaOfIsland(int[][] grid) {
        PriorityQueue<Integer> sortedQueue = new PriorityQueue<>((o1, o2) -> o2 - o1);  //降序，大根堆
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {  //从此网格开始感染计数，一切归零，重新开始
                    maxArea = 0;
                    maxAreaOfIslandDfs(grid, directions, i, j);
                    sortedQueue.add(maxArea);
                }
            }
        }
        return sortedQueue.isEmpty() ? 0 : sortedQueue.peek();
    }

    private void maxAreaOfIslandDfs(int[][] grid, int[][] directions, int i, int j) {
        //终止条件一：出界
        if (!(i >= 0 && i < grid.length && j >= 0 && j < grid[0].length)) {
            return;
        }
        //终止条件二：触水
        if (grid[i][j] == 0) {
            return;
        }
        maxArea++; //累加
        grid[i][j] = 0;  //不再遍历
        for (int[] direction : directions) {
            int nextRow = i + direction[0];
            int nextCol = j + direction[1];
            maxAreaOfIslandDfs(grid, directions, nextRow, nextCol);
        }
    }


    int currentArea = 0;

    /**
     * 827. 最大人工岛
     */
    public int largestIsland(int[][] grid) {   //困难
        int maxLandArea = 0;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};   //上、下、左、右
        //记录各个岛屿的编号和面积
        HashMap<Integer, Integer> hTable = new HashMap<>();
        boolean isAllGrid = true;
        int serialNumber = 2;
        //先一次深度优先遍历，获取各个岛屿的面积
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                //校验是否所有其余均为陆地
                if (grid[i][j] == 0) isAllGrid = false;
                //搜索陆地边界
                if (grid[i][j] == 1) {
                    //初始值
                    currentArea = 0;
                    //基于"感染"的想法，星火燎原
                    largestIslandDFS(grid, directions, i, j, serialNumber);
                    hTable.put(serialNumber, currentArea);
                    serialNumber++;
                }
            }
        }
        //1、如果所有区域均为陆地，则直接返回陆地面积
        if (isAllGrid) return grid.length * grid[0].length;
        //2、如果存在非陆地的区域，基于连通块的想法，连节点连通两个岛屿，计算最大相邻岛屿的面积
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                int unionArea = 1; //连通总面积
                HashSet<Integer> uniqueIsland = new HashSet<>();   //记录当前连通点，连接的岛屿编号，防止重复添加
                if (grid[i][j] == 0) { //连通点，本质其实是广度优先搜索
                    for (int[] direction : directions) {  //四个方向搜索
                        int nearRow = i + direction[0];
                        int nearCol = j + direction[1];
                        //越界
                        if (nearRow < 0 || nearRow >= grid.length || nearCol < 0 || nearCol >= grid[0].length) {
                            continue;
                        }
                        //水域
                        if (grid[nearRow][nearCol] == 0) {
                            continue;
                        }
                        //已经累加此岛屿的面积
                        if (uniqueIsland.contains(grid[nearRow][nearCol])) {
                            continue;
                        }
                        unionArea += hTable.get(grid[nearRow][nearCol]);
                        uniqueIsland.add(grid[nearRow][nearCol]);
                    }
                }
                //获取多个连通点对应的连通面积的最大值
                maxLandArea = Math.max(maxLandArea, unionArea);
            }
        }
        return maxLandArea;
    }

    private void largestIslandDFS(int[][] grid, int[][] directions, int row, int col, int serialNumber) {
        //终止条件：越界
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
            return;
        }
        //终止条件：搜索至水域、已搜索此节点
        if (grid[row][col] != 1) {
            return;
        }

        //标识为已搜索，并累积面积
        currentArea++;
        grid[row][col] = serialNumber;

        //横向枚举搜索
        for (int[] direction : directions) {
            int nextRow = row + direction[0];
            int nextCol = col + direction[1];
            //纵向递归搜索
            largestIslandDFS(grid, directions, nextRow, nextCol, serialNumber);
        }
    }


    /**
     * 934. 最短的桥
     */
    public int shortestBridge(int[][] grid) {
        int n = grid.length;
        ArrayDeque<int[]> dequeQueue = new ArrayDeque<>();
        int isLandNums = 0;
        int[][] directions = {{1, 0}, {-1, 0}, {0, -1}, {0, 1}};  //上下左右
        //1、深度优先搜索，感染首个岛屿，并记录其边界水域的位置
        for (int i = 0; i < n && isLandNums == 0; i++) {
            for (int j = 0; j < n && isLandNums == 0; j++) {
                if (grid[i][j] == 1) {
                    shortestBridgeDfs(grid, directions, dequeQueue, i, j);
                    isLandNums++;
                }
            }
        }
        int result = 1;
        //2、广度优先搜索，逐层向外延伸
        while (!dequeQueue.isEmpty()) {
            //--------------------------------------------------------------------
            // 关键：由于 dequeQueue 存储的是边缘水域格子，即沿着岛屿边界向外延伸的一层，而且是逐层存储，且各层间共享队列，为了逐层获取，可通过以下方式来实现
            //--------------------------------------------------------------------
            int nums = dequeQueue.size();
            for (int i = 0; i < nums; i++) {   //多层共用一个队列，通过这种形式来统一批量的处理单层中的节点
                int[] edgeNode = dequeQueue.pollFirst();  //从头取，保证一层和一层之间有顺序
                for (int[] dir : directions) {
                    int nextRow = edgeNode[0] + dir[0];
                    int nextCol = edgeNode[1] + dir[1];
                    if (nextRow >= 0 && nextRow < grid.length && nextCol >= 0 && nextCol < grid[0].length) {
                        if (grid[nextRow][nextCol] == 0) {
                            grid[nextRow][nextCol] = -1;  //感染，标记为已搜索
                            dequeQueue.addLast(new int[]{nextRow, nextCol});  //添加到下层
                        }
                        if (grid[nextRow][nextCol] == 1) {  //找到第二个岛屿
                            return result;
                        }
                    }
                }
            }
            result++;
        }
        return result;
    }

    private void shortestBridgeDfs(int[][] grid, int[][] directions, ArrayDeque<int[]> dequeQueue, int currentRow, int currentCol) {
        //1、递归终止条件一：越界
        if (currentRow < 0 || currentRow >= grid.length || currentCol < 0 || currentCol >= grid[0].length) {
            return;
        }
        //2、递归终止条件二：已搜索
        if (grid[currentRow][currentCol] == -1) {
            return;
        }
        //3、递归终止条件三：边界水域
        if (grid[currentRow][currentCol] == 0) {
            grid[currentRow][currentCol] = -1;  //相邻边界水域感染
            dequeQueue.addLast(new int[]{currentRow, currentCol});
            return;
        }

        //运行到这里说明是岛屿内部
        grid[currentRow][currentCol] = -1;  //岛屿感染
        for (int[] dir : directions) {  //横向枚举遍历
            int nextRow = currentRow + dir[0];
            int nextCol = currentCol + dir[1];
            shortestBridgeDfs(grid, directions, dequeQueue, nextRow, nextCol);
        }
    }

    //---------------------------------------------------------------------------------------
    // 上下两种写法的差异关键在于，两个岛屿连接的方式是通过广度优先搜索（上面）或深度优先搜索（下面）
    //---------------------------------------------------------------------------------------


    public int shortestBridge01(int[][] grid) {  //有点问题
        int n = grid.length;
        int colorNum = 2;
        int[][] directions = {{1, 0}, {-1, 0}, {0, -1}, {0, 1}};  //上下左右
        for (int i = 0; i < n; i++) {  //基于深度优先搜索，对岛屿进行感染标识
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    shortestBridgeDfs(grid, directions, i, j, colorNum);
                    colorNum++;
                }
            }
        }
        //记忆化搜索，搜索所有路径，记录各个位点到最近的岛屿 3 的距离
        //思路是好的，但会陷入进退两难无法搜索的困境，案例如下
        //int[][] grid = {{0, 1, 0, 0, 0, 0}, {0, 1, 1, 1, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {1, 1, 0, 0, 0, 0}};
        int[][] cached = new int[n][n];  //记忆化，结果
        int[][] visited = new int[n][n]; //记忆化，不走回头路
        for (int i = 0; i < n; i++) {
            Arrays.fill(cached[i], -1);
        }
        int minPaths = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 2) {
                    minPaths = Math.min(minPaths, shortestBridgeDfs(grid, directions, visited, cached, i, j));
                }
            }
        }
        return minPaths - 2;
    }

    private void shortestBridgeDfs(int[][] grid, int[][] directions, int currentRow, int currentCol, int colorNum) {
        //1、递归终止条件一：越界
        if (currentRow < 0 || currentRow >= grid.length || currentCol < 0 || currentCol >= grid.length) {
            return;
        }
        //2、递归终止条件二：水域
        if (grid[currentRow][currentCol] == 0) {
            return;
        }
        //3、递归终止条件三：已搜索
        if (grid[currentRow][currentCol] == colorNum) {
            return;
        }

        grid[currentRow][currentCol] = colorNum; //感染
        for (int[] dir : directions) {
            int nextRow = currentRow + dir[0];
            int nextCol = currentCol + dir[1];
            shortestBridgeDfs(grid, directions, nextRow, nextCol, colorNum);
        }
        //当前位点感染完毕
    }


    private int shortestBridgeDfs(int[][] grid, int[][] directions, int[][] visited, int[][] cached, int currentRow, int currentCol) { //记忆化搜索
        //1、递归终止条件一：已经搜索
        if (cached[currentRow][currentCol] != -1) {  //非岛屿内部的点，已经搜索完成，直接返回搜索结果
            return cached[currentRow][currentCol];
        }
        //2、递归终止条件二：到达 3号岛屿
        if (grid[currentRow][currentCol] == 3) {
            return 1;  //此位点暂且作为桥长度的一部分，后面会剪掉
        }

        visited[currentRow][currentCol] = 1;  //标记为搜索中
        int paths = 101;   //初始值，给一个本题的最大值，尽量不要给 Integer.MAX_VALUE ,因为一旦有 累加 的，直接越界
        for (int[] dir : directions) {
            int nextRow = currentRow + dir[0];
            int nextCol = currentCol + dir[1];
            //1、剪枝一：越界
            if (nextRow < 0 || nextRow >= grid.length || nextCol < 0 || nextCol >= grid.length) {
                continue;
            }
            //2、剪枝二：岛屿内部
            if (grid[nextRow][nextCol] == 2) {
                continue;
            }
            //3、剪枝三：不走回头路
            if (visited[nextRow][nextCol] == 1) {
                continue;
            }
            paths = Math.min(paths, shortestBridgeDfs(grid, directions, visited, cached, nextRow, nextCol) + 1);
        }
        cached[currentRow][currentCol] = paths;
        visited[currentRow][currentCol] = 0;  //恢复状态

        return paths;
    }


    // n 个相等集合的问题，类似于 那类问题 ？？


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


    public boolean makesquare01(int[] matchsticks) {   //官方
        int sum = Arrays.stream(matchsticks).sum();
        int average = sum / 4;
        if (sum % 4 != 0) return false;
        Arrays.sort(matchsticks);  //默认升序排序
//        for (int i = 0, j = matchsticks.length - 1; i < matchsticks.length && j >= 0; i++, j--) {  //错误写法
        for (int i = 0, j = matchsticks.length - 1; i < j; i++, j--) {  //改为降序排序
            int temp = matchsticks[i];
            matchsticks[i] = matchsticks[j];
            matchsticks[j] = temp;
        }
        int[] edges = new int[4];
        return makesquareDfs01(matchsticks, edges, 0, average);
    }

    // currentIndex 当前待处理元素
    private boolean makesquareDfs01(int[] matchsticks, int[] edges, int currentIndex, int target) {
        if (currentIndex == matchsticks.length) {  //所有元素处理完毕
            for (int edge : edges) {  //验证各个边界是否满足条件，其实处理到这里就已经说明没问题了
                if (edge != target) return false;
            }
            return true;
        }
        for (int i = 0; i < edges.length; i++) { //横向枚举遍历
            if (edges[i] + matchsticks[currentIndex] > target) {  //剪枝，跳过不满足的桶
                continue;
            }
            //1、添加元素
            edges[i] += matchsticks[currentIndex];

            //2、纵向递归搜索
            if (makesquareDfs01(matchsticks, edges, currentIndex + 1, target)) {
                return true;   //直接返回了，不会再朝下执行了，因此，此元素分配的合理
            }

            //3、减少元素
            edges[i] -= matchsticks[currentIndex];
        }
        // 一个元素，添加到 4 个边都不满足条件
        return false;
    }


    private boolean makesquareDfs02(int[] matchsticks, int[] edges, int currentIndex, int target) {
        if (currentIndex == matchsticks.length) {
            return true;
        }
        for (int i = 0; i < edges.length; i++) { //横向枚举遍历，将当前元素添加至 四个 边中
            //1、添加元素
            edges[i] += matchsticks[currentIndex];

            //2、纵向递归搜索
            if (edges[i] <= target && makesquareDfs(matchsticks, edges, currentIndex + 1, target)) {
                return true;
            }

            //3、减少元素
            edges[i] -= matchsticks[currentIndex];
        }
        // 一个元素，添加到 4 个边都不满足条件
        return false;
    }

    /**
     * 698. 划分为k个相等的子集
     */
    public boolean canPartitionKSubsets(int[] nums, int k) {
        int sum = Arrays.stream(nums).sum();
        if (sum % k != 0) return false;
        int target = sum / k;
        int[] edges = new int[k];
        Arrays.sort(nums);
        //递减排序，先让值大的元素选择桶，这样可以增加剪枝的命中率，从而降低回溯的概率
        for (int i = 0, j = nums.length - 1; i < j; i++, j--) {
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
        return canPartitionKSubsetsDfs(nums, edges, 0, target);
    }


    // currentIndex 为待分配元素的索引
    private boolean canPartitionKSubsetsDfs(int[] nums, int[] edges, int currentIndex, int target) {
        if (currentIndex == nums.length) {
            return true;   //递归结束的最终条件一，然后逐层返回 true，在 if 中.....
        }

        for (int i = 0; i < edges.length; i++) {   //横向枚举搜索，看放入那个边合适
            //剪枝，跳过不满足条件情况
            if (edges[i] + nums[currentIndex] > target) {  //配和上面 降序排序使用效果更好
                continue;
            }

            //剪枝，跳过相同的情况
            if (i > 0 && edges[i] == edges[i - 1]) {  //如果两个边界的"元素和"相等，那么 nums[index] 选择上一个桶和选择当前桶可以得到的结果是一致的
                continue;
            }

            //剪枝，特殊情况
            if (i > 0 && currentIndex == 0) {  //对于第一个球，任意放到某个桶中的效果都是一样的，所以我们规定放到第一个桶中
                break;
            }

            //--------------------------------------------
            // 执行到这里说明，当前 currentIndex 可以被分配
            //--------------------------------------------

            //1、添加元素
            edges[i] += nums[currentIndex];

            //2、递归下一个元素
            if (canPartitionKSubsetsDfs(nums, edges, currentIndex + 1, target)) {  //纵向递归遍历
                return true;
                //-------------------------------------
                // 执行到这里，直接返回，不会执行下面的逻辑
                //-------------------------------------
            }

            //3、删除元素
            edges[i] -= nums[currentIndex];
            //------------------------------------------------------------------------
            // 在当前元素 nums[currentIndex] 放在此 edges[i]时，其余元素的组合不能满足条件
            //     因为，上面的 if 递归结果一定是 false，即 存在某个元素，放入任何一个 edges[m] 都不能满足条件
            //------------------------------------------------------------------------

        }
        return false;  //递归结束的最终条件二，然后逐层返回 false，在 if 中.....
    }


    private int globalFairValue = Integer.MAX_VALUE;

    /**
     * 2305. 公平分发饼干
     */
    public int distributeCookies(int[] cookies, int k) {
        Arrays.sort(cookies);
        int[] buckets = new int[k];  //记录每个小朋友，当前手中的饼干数

        for (int i = 0, j = cookies.length - 1; i < j; i++, j--) {
            int temp = cookies[i];
            cookies[i] = cookies[j];
            cookies[j] = temp;
        }

        distributeCookiesDfs(cookies, buckets, 0);
        return globalFairValue;
    }

    private void distributeCookiesDfs(int[] cookies, int[] buckets, int currentIndex) {   // currentIndex 为下一个要分发的零食包的索引
        //递归结束条件
        if (currentIndex == cookies.length) {
            //处理当前路径的递归结果
            int currentFairValue = 0;
            for (int total : buckets) {
                currentFairValue = Math.max(currentFairValue, total);
            }
            globalFairValue = Math.min(globalFairValue, currentFairValue);

            return; //递归结束
        }


        //剪枝，如果剩余的零食包，不够空手小朋友每人至少一个，则直接返回
        int emptyBuckets = (int) Arrays.stream(buckets).filter(o -> o == 0).count();
        if (cookies.length - (currentIndex - 1) < emptyBuckets) {   //当前剩余的零食包数 < 空桶的数量，不能保证每个孩子都有零食
            return;
        }

        //剪枝，如果某个小朋友手里的饼干数已经大于当前最优解，则此递归路径的结果不会称为最优解，直接终止当前递归
        for (int bucket : buckets) {
            if (bucket > globalFairValue)
                return;
        }

        for (int i = 0; i < buckets.length; i++) {  //横向枚举遍历，即将 cookies[currentIndex] 放在不同的桶中，进行纵向递归
            //剪枝，无论第一个零食给哪个小朋友，其对应的回溯路径和结果一致，因此，第一个零食分给第一个小朋友，其余的剪枝
            if (i > 0 && currentIndex == 0) {
                return;
            }

            //1、增加元素
            buckets[i] += cookies[currentIndex];

            //2、基于当前元素，进行纵向搜索
            distributeCookiesDfs(cookies, buckets, currentIndex + 1);

            //3、移除元素
            buckets[i] -= cookies[currentIndex];

        }
    }


    public int distributeCookies01(int[] cookies, int k) {
        int[] buckets = new int[k];
        Arrays.sort(cookies);
        for (int i = 0, j = cookies.length - 1; i < j; i++, j--) {
            int xx = cookies[i];
            cookies[i] = cookies[j];
            cookies[j] = xx;
        }
        distributeCookiesDfs01(cookies, buckets, 0);
        return globalFairValue;
    }

    private void distributeCookiesDfs01(int[] cookies, int[] buckets, int currentIndex) {
        //递归终止
        if (currentIndex == cookies.length) {
            int currentFairValue = Arrays.stream(buckets).max().getAsInt();
            globalFairValue = Math.min(globalFairValue, currentFairValue);
            return;
        }

        //剪枝，未收到零食的孩子数  大于 未分配的零食袋数
        int emptyBuckets = (int) Arrays.stream(buckets).filter(o -> o == 0).count();
        if (emptyBuckets > cookies.length - currentIndex) {    //与上有差异，少一个 1
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

            distributeCookiesDfs01(cookies, buckets, currentIndex + 1);

            buckets[i] -= cookies[currentIndex];
        }
    }


    /**
     * 306. 累加数
     */
    public boolean isAdditiveNumber(String num) {   //基于递归算法
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


    private boolean isAdditiveNumberDfs00(String num, int count, int startIndex, long prevPrev, long prev) {
        if (startIndex == num.length()) {
            return count >= 3;
        }
        long current = 0;
        for (int i = startIndex; i < num.length(); i++) {
            //剪枝_1，开头为 0 的分支，全部剪掉
            if (num.charAt(startIndex) == '0' && i > startIndex) {
                return false;
            }

            //横向扩展，进行累加
            current = current * 10 + (num.charAt(i) - '0');

            if (count >= 2) {
                //1、已不能满足情况的分支
                if (current > prevPrev + prev) {   //剪枝_2
                    return false;
                }

                //2、尚未满足情况的分支
                if (current < prevPrev + prev) {    //横向扩展，继续累加
                    continue;
                }
            }

            //--------------------------------------------------------------------
            // 这样递归可以实现，尝试使用 前一位作为第一个数，前两位作为第一个数、前三位作为第一个数、前 n 位作为第一个数
            //--------------------------------------------------------------------

            //3、当前满足条件的分支、或者不到 2 个数的情况
            if (isAdditiveNumberDfs(num, count + 1, i + 1, prev, current)) { //纵向递归搜索，从新开始
                return true;
            }
        }
        return false;
    }


    public boolean isAdditiveNumber01(String num) {  //基于枚举
        //整个累加数列只由第一和第二个数决定，因此枚举所有第一个和第二个数的组合，并验证是否能满足条件
        int n = num.length();
        //-----------------------------------------------------------
        // secondBegin/secondEnd的取值保证，整个字符串至少被切分为三个数
        //-----------------------------------------------------------
        for (int secondBegin = 1; secondBegin < n - 1; secondBegin++) {
            //剪枝，第一个数不满足条件的情况
            if (num.charAt(0) == '0' && secondBegin != 1) {  //num以 0 开头，则第一个数一定为 0，第二个数的开始一定从 1 开始
                break;
            }
            for (int secondEnd = secondBegin; secondEnd < n - 1; secondEnd++) {
                //剪枝，第二个数不满足条件的情况
                if (num.charAt(secondBegin) == '0' && secondEnd != secondBegin) {
                    break;
                }
                //逐一进行校验
                if (checkCombinationValid(num, secondBegin, secondEnd)) {   //如果由满足条件的组合，则返回 true
                    return true;
                }
            }
        }
        return false; //所有组合遍历结束，无满足条件的组合，则返回 false
    }

    private boolean checkCombinationValid(String num, int secondBegin, int secondEnd) {
        int firstBegin = 0;
        int firstEnd = secondBegin - 1;
        while (secondEnd < num.length()) {
            String third = stringAdd(num, firstBegin, firstEnd, secondBegin, secondEnd);
            int thirdBegin = secondEnd + 1;
            int thirdEnd = secondEnd + third.length();
            //校验当前两数之和在字符串中的合理性
            if (thirdEnd >= num.length() || !num.substring(thirdBegin, thirdEnd + 1).equals(third)) {
                return false;
            }
            if (thirdEnd == num.length() - 1) {
                return true;
            }
            //递归元素
            firstBegin = secondBegin;
            firstEnd = secondEnd;
            secondBegin = thirdBegin;
            secondEnd = thirdEnd;
        }
        return false;
    }

    //----------------------------------------------------------------------------------------------------
    // 当输入规模较小时，可以直接使用整形或者长整型的数字的相加，为了防止处理溢出的过大的整数输入，因此使用字符串加法
    //----------------------------------------------------------------------------------------------------
    private String stringAdd(String num, int firstBegin, int firstEnd, int secondBegin, int secondEnd) {  //字符串加法，避免整数溢出
        StringBuilder third = new StringBuilder();
        int carry = 0;   //进位值
        int current = 0; //当前位值
        while (firstEnd >= firstBegin || secondEnd >= secondBegin || carry != 0) {
            //当前位的累加值的基准为 上一位的进位值
            current = carry;
            //第一个数，低位累加
            if (firstEnd >= firstBegin) {
                current += num.charAt(firstEnd) - '0';
                firstEnd--;
            }
            //第二个数，低位累加
            if (secondEnd >= secondBegin) {
                current += num.charAt(secondEnd) - '0';
                secondEnd--;
            }
            carry = current / 10;
            current %= 10;
            third.append((char) (current + '0'));
        }
        return third.reverse().toString();
    }


    /**
     * 680. 验证回文串 II
     */
    public boolean validPalindrome(String str) {
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
        if (left == right || left == right + 1) { // A A 奇数/偶数导致跳到同一点或跳过、 A B且此时还可剔除掉一个
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


    public boolean validPalindrome01(String str) {  //错误
        StringBuilder builder = new StringBuilder();
        int left = 0;
        int right = str.length() - 1;
        return validPalindromeDfs(str, left, right, 1);
    }

    private boolean validPalindromeDfs(String str, int left, int right, int k) {
        while (left < right && str.charAt(left) == str.charAt(right)) {
            left++;
            right--;
        }
        // A A 奇数/偶数导致跳到同一点或跳过、 A B且此时还可剔除掉一个
        if (left == right || left == right + 1 || (left + 1 == right && k == 1)) {
            return true;
        }

        //---------------------------------------------------------------------------------
        // 以下这种写法，第一个 迭代 会影响 left 和 right 的值，导致第一个和第二个迭代的起始点不同
        //---------------------------------------------------------------------------------
        if (k == 1 && str.charAt(left + 1) == str.charAt(right)) {
            if (validPalindromeDfs(str, left + 1, right, k - 1))
                return true;
        }
        if (k == 1 && str.charAt(left) == str.charAt(right - 1)) {
            if (validPalindromeDfs(str, left, right - 1, k - 1))
                return true;
        }

        return false;
    }

    public boolean validPalindrome02(String str) {  //错误
        StringBuilder builder = new StringBuilder();
        int left = 0;
        int right = str.length() - 1;
        int skips = 1;
        while (left < right) {
            while (left < right && str.charAt(left) == str.charAt(right)) {
                builder.append(str.charAt(left));
                left++;
                right--;
            }
            if (left >= right) return true;       //跳到同一位上，或者 已经相互跳出，所以为 true
            if (left + 1 == right) return true;   //剩余两个，则剔除一个，其余的对称，所以为 true

            //这种写法无法处理，这种混沌的情况 String str = "gmlcuppuculmg";
            if (skips == 1 && str.charAt(left + 1) == str.charAt(right)) {
                left++;
                skips--;
            } else if (skips == 1 && str.charAt(left) == str.charAt(right - 1)) {
                right--;
                skips--;
            } else {
                System.out.println(builder.toString());
                return false;
            }
        }
        return true;
    }


    /**
     * 131. 分割回文串
     */
    public List<List<String>> partition(String str) {
        List<List<String>> ans = new ArrayList<>();
        if (str.length() == 0) {
            return ans;
        }
        LinkedList<String> path = new LinkedList<>();
        partitionDfs(str, ans, path, 0);
        return ans;
    }

    private void partitionDfs(String str, List<List<String>> ans, LinkedList<String> path, int currentIndex) {
        if (currentIndex == str.length()) {
            ans.add(new ArrayList<>(path));
            return;
        }

        //横向搜索和纵向递归使用的 currentIndex 是承前启后，接力使用的，而不同的路径，其实是接力的位点不同

        for (int right = currentIndex; right < str.length(); right++) {
            if (checkPalindrome(str, currentIndex, right)) {
                //1、添加元素
                path.add(str.substring(currentIndex, right + 1));
                //2、纵向递归
                partitionDfs(str, ans, path, right + 1);
                //3、回溯状态
                path.removeLast();
            }
        }

    }

    private boolean checkPalindrome(String str, int left, int right) {
        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
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

        StringBuilder seq = new StringBuilder();   //关键
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
     * 93. 复原 IP 地址
     */
    public List<String> restoreIpAddresses(String str) {
        List<String> ans = new ArrayList<>();
        if (str.length() < 4) {
            return ans;
        }
        LinkedList<String> path = new LinkedList<>();
        restoreIpAddressesDfs(str, ans, path, 0);
        return ans;
    }

    private void restoreIpAddressesDfs(String str, List<String> ans, LinkedList<String> path, int currentIndex) {
        if (currentIndex == str.length() && path.size() == 4) {
            //一步到位的处理
            ans.add(String.join(".", path));
//            StringBuilder ipAddress = new StringBuilder();
//            for (String bit : path) {
//                ipAddress.append(bit);
//                ipAddress.append(".");
//            }
//            ans.add(ipAddress.substring(0, ipAddress.length() - 1));
            return;
        }

        //剪枝
        if (path.size() >= 4) {
            return;
        }

        StringBuilder seq = new StringBuilder();
        for (int i = currentIndex; i < str.length(); i++) {
            //剪枝
            if (i > currentIndex && str.charAt(currentIndex) == '0') {  //前导零
                break;
            }

            //1、新增元素
            seq.append(str.charAt(i));
            //---------------------------------
            // 剪枝
            //---------------------------------
            if (seq.length() > 3) {  //每一段最大长度为 3
                break;
            }
            if (seq.length() == 3 && Integer.parseInt(new String(seq)) > 255) {   //每一段最大值
                break;
            }

            path.add(seq.toString());

            //2、纵向递归
            restoreIpAddressesDfs(str, ans, path, i + 1);

            //3、移除元素
            path.removeLast();
        }
    }


    int reachNodes = 0;

    /**
     * 2368. 受限条件下可到达节点的数目
     */
    public int reachableNodes(int n, int[][] edges, int[] restricted) {
        //邻接表
        HashMap<Integer, ArrayList<Integer>> adjacent = new HashMap<>();  //自顶向下
        int[] used = new int[n];
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            if (!adjacent.containsKey(node1)) {
                adjacent.put(node1, new ArrayList<>());
            }
            if (!adjacent.containsKey(node2)) {
                adjacent.put(node2, new ArrayList<>());
            }
            adjacent.get(node1).add(node2);
            adjacent.get(node2).add(node1);
        }
        HashSet<Integer> restrictNode = new HashSet<>();
        for (int num : restricted) {
            restrictNode.add(num);
        }
        reachableNodesDfs(adjacent, restrictNode, used, 0);
        return reachNodes;
    }

    private void reachableNodesDfs(HashMap<Integer, ArrayList<Integer>> adjacent, HashSet<Integer> restrictNode, int[] used, int currentIndex) {
        reachNodes++;
        ArrayList<Integer> nodes = adjacent.get(currentIndex);

        used[currentIndex] = 1;   //类似感染的感觉

        for (int node : nodes) {  //横向枚举遍历
            //剪枝，不可搜索
            if (restrictNode.contains(node)) {
                continue;
            }
            if (used[node] == 1) {
                continue;
            }
            reachableNodesDfs(adjacent, restrictNode, used, node);
        }
    }


    /**
     * 22. 括号生成
     */
    public List<String> generateParenthesis0000(int n) {   //非常推荐这种写法
        List<String> ans = new ArrayList<>();
        if (n == 0) return ans;
        generateParenthesisDfs0000(ans, "", 0, 0, n);
        return ans;
    }

    private void generateParenthesisDfs0000(List<String> ans, String path, int leftNums, int rightNums, int n) {
        if (rightNums > leftNums)
            return;
        if (leftNums == n && rightNums == n) {
            ans.add(path);
            return;
        }

        //-----------------------------------------------------------------------------
        // 以下关键，先执行第一段逻辑，当纵向递归结束，特别是碰到 rightNums > leftNums，返回时，才会执行下面逻辑
        //     如果碰到触及 n ,则也会执行下面逻辑，但是会通过判断而跳过
        // 其特点是没有明确的增加元素和剔除元素的过程，都是在递归的时候在现有的基础上 增加，回溯至此，其初始状态一致，故无需剔除
        //-----------------------------------------------------------------------------

        if (leftNums < n)
            generateParenthesisDfs0000(ans, path + "(", leftNums + 1, rightNums, n);
        if (rightNums < n)
            generateParenthesisDfs0000(ans, path + ")", leftNums, rightNums + 1, n);

    }

    public List<String> generateParenthesis(int n) {
        ArrayList<String> ans = new ArrayList<>();
        String[] element = {"(", ")"};
        generateParenthesisDfs(n, element, ans, 0, new StringBuilder());
        return ans;
    }

    private void generateParenthesisDfs(int n, String[] element, ArrayList<String> ans, int sum, StringBuilder path) {
        //迭代终止条件
        if (path.length() == 2 * n && sum == 0) {
            ans.add(new String(path));
            return;
        }

        //剪枝：无法构成有效括号
        if (sum < 0 || path.length() > 2 * n) {
            return;
        }

        for (String parenthesis : element) {  //横向枚举遍历
            sum = parenthesis.equals("(") ? sum + 1 : sum - 1;

            //1、添加元素
            path.append(parenthesis);
            //2、纵向递归搜索
            generateParenthesisDfs(n, element, ans, sum, path);
            //3、移除元素
            path.deleteCharAt(path.length() - 1);
            sum = parenthesis.equals("(") ? sum - 1 : sum + 1;
        }
    }

    public List<String> generateParenthesis06(int n) {
        int sum = 0;
        String[] element = {"(", ")"};
        List<String> ans = new ArrayList<>();
        LinkedList<String> path = new LinkedList<>();
        generateParenthesisDfs(n, ans, element, sum, path);
        return ans;
    }

    private void generateParenthesisDfs(int n, List<String> ans, String[] element, int sum, LinkedList<String> path) {
        //递归终止条件
        if (path.size() == 2 * n && sum == 0) {
            ans.add(String.join("", path));
            return;
        }

        //剪枝
        if (sum < 0 || sum > 2 * n - path.size() || path.size() > 2 * n) {  //右括号多、左括号多、长度超限
            return;
        }

        for (String curr : element) {
            //1、新增元素
            path.addLast(curr);

            //2、纵向递归
            generateParenthesisDfs(n, ans, element, sum + (curr.equals("(") ? 1 : -1), path);  //极其推荐这种写法

            //3、移除元素
            path.removeLast();
        }
    }


    //---------------------------------------------------------
    // 对比上写两个 DFS 的写法，一个将 if (sum < 0) return 写在 for 循环内，一个在 for 循环外，差异在于 sum的增加和减少是否匹配
    // 下面的写法，在这里没问题，但非常不推荐，尽量将 递归终止条件写在 for 循环外
    //---------------------------------------------------------

    private void generateParenthesisDfsError(int n, String[] element, ArrayList<String> ans, int sum, StringBuilder path) {
        //迭代终止条件
        if (path.length() == 2 * n && sum == 0) {
            ans.add(new String(path));
            return;
        }

        //剪枝：无法构成有效括号
        if (path.length() > 2 * n) {
            return;
        }

        for (String parenthesis : element) {  //横向枚举遍历
            sum = parenthesis.equals("(") ? sum + 1 : sum - 1;
            //剪枝一：当前的组合无法构成有效括号
            if (sum < 0) return;

            //1、添加元素
            path.append(parenthesis);
            //2、纵向递归搜索
            generateParenthesisDfsError(n, element, ans, sum, path);
            //3、移除元素
            path.deleteCharAt(path.length() - 1);
            sum = parenthesis.equals("(") ? sum - 1 : sum + 1;
        }
    }


    public List<String> generateParenthesis00(int n) {
        List<String> ans = new ArrayList<>();
        StringBuilder path = new StringBuilder();
        path.append('(');
        int[] parenthesis = new int[2];
        parenthesis[0] = '(';
        parenthesis[1] = ')';
        generateParenthesisDfs(parenthesis, ans, path, (n - 1), (n - 1));
        return ans;
    }

    private void generateParenthesisDfs(int[] parenthesis, List<String> ans, StringBuilder path, int leftNums, int rightNums) {
        if (leftNums == 0 && rightNums == 0) {
            path.append(")");
            ans.add(path.toString());
            return;
        }
        if (leftNums > rightNums)
            return;

        for (int i = 0; i < parenthesis.length; i++) {
            //1、添加元素
            if (i == 0) {
                leftNums--;
                if (leftNums < 0)
                    return;
            }
            if (i == 1) {
                rightNums--;
                if (rightNums < 0)
                    return;
            }

            path.append((char) parenthesis[i]);

            //2、纵向递归
            generateParenthesisDfs(parenthesis, ans, path, leftNums, rightNums);

            //3、移除元素
            path.deleteCharAt(path.length() - 1);
            if (i == 0)
                leftNums++;
            if (i == 1)
                rightNums++;
        }

    }

    // 做减法
    public List<String> generateParenthesis02(int n) {
        List<String> ans = new ArrayList<>();
        StringBuilder path = new StringBuilder();
        generateParenthesisDfs(ans, path, 0, 0, n);
        return ans;
    }

    private void generateParenthesisDfs(List<String> ans, StringBuilder path, int leftNums, int rightNums, int n) {
        if (rightNums > leftNums) {
            return;
        }
        if (leftNums == n && rightNums == n) {
            StringBuilder builder = new StringBuilder();
            ans.add(builder.append(path).toString());
            return;
        }
        if (leftNums < n)
            generateParenthesisDfs(ans, path.append("("), leftNums + 1, rightNums, n);


        if (rightNums < n)
            generateParenthesisDfs(ans, path.append(")"), leftNums, rightNums + 1, n);
    }


    /**
     * 802. 找到最终的安全状态
     */
    public List<Integer> eventualSafeNodes(int[][] graph) {  //正向
        ArrayList<Integer> ans = new ArrayList<>();
        int[] visited = new int[graph.length];
        for (int i = 0; i < graph.length; i++) {
            if (eventualSafeNodesDfs(graph, visited, i)) {
                ans.add(i);
            }
        }
        return ans;
    }

    private boolean eventualSafeNodesDfs(int[][] graph, int[] visited, int currentIndex) {  //时间复杂度高
        //递归迭代终止条件
        if (graph[currentIndex].length == 0 || visited[currentIndex] == 2) {
            return true;
        }

        //正在搜索
        visited[currentIndex] = 1;
        int[] nodes = graph[currentIndex];
        for (int node : nodes) {  //横向枚举遍历
            //构成闭环
            if (visited[node] == 1) {
                return false;
            }
            //搜索反例
            if (!eventualSafeNodesDfs(graph, visited, node)) {   //只要存在，即为不满足情况
                return false;
            }
        }

        //搜索完毕，当前节点为安全节点
        visited[currentIndex] = 2;  //如果再有其他节点搜索至此节点，无需再次搜索
        return true;  //不存在反例，当前节点的所有连通节点均通过"终端节点"
    }


    public List<Integer> eventualSafeNodes01(int[][] graph) {   //基于反向图来解决
        ArrayList<Integer> ans = new ArrayList<>();
        //邻接表
        HashMap<Integer, ArrayList<Integer>> adjacent = new HashMap<>();  //其实graph就是邻接表，但我们现在要反转邻接表
        //入度
        int[] inDegree = new int[graph.length];
        //初始化
        for (int i = 0; i < graph.length; i++) {
            inDegree[i] = graph[i].length;
            for (int j = 0; j < graph[i].length; j++) {
                if (!adjacent.containsKey(graph[i][j])) {
                    adjacent.put(graph[i][j], new ArrayList<>());
                }
                adjacent.get(graph[i][j]).add(i);
            }
        }
        //从当前的终端节点出发，搜索所有可到达的节点，更新节点的入度
        ArrayDeque<Integer> currQueue = new ArrayDeque<>();
        for (int i = 0; i < inDegree.length; i++) {
            if (inDegree[i] == 0) {
                currQueue.add(i);
            }
        }
        while (!currQueue.isEmpty()) {
            //当前节点
            Integer currentNode = currQueue.poll();
            //1、当前节点没有相邻节点，不会影响其他节点的入度
            if (!adjacent.containsKey(currentNode)) {
                continue;
            }
            //2、可到达的相邻节点
            ArrayList<Integer> reachNodes = adjacent.get(currentNode);

            //依次影响相邻节点的入度
            for (int node : reachNodes) {
                inDegree[node]--;
                if (inDegree[node] == 0)
                    currQueue.add(node);
            }
        }

        //获取安全节点
        for (int i = 0; i < inDegree.length; i++) {
            //--------------------------------------
            // 反转图后，安全节点一定是仅能从终端节点达到的节点，因此从终端节点出发，取影响各个节点的入度：
            //     如果入度为 0 ，则说明此节点除了能从终端节点到达，没有从其他节点到达此节点的路径
            //     如果入度为 n ，则说明此节点除了能从终端节点到达，还能从其他 n 个节点到达此节点
            //--------------------------------------
            if (inDegree[i] == 0) {
                ans.add(i);
            }
        }
        return ans;
    }


    /**
     * 851. 喧闹和富有
     */
    public int[] loudAndRich(int[][] richer, int[] quiet) {
        int[] ans = new int[quiet.length];
        //邻接表
        HashMap<Integer, ArrayList<Integer>> adjacent = new HashMap<>();
        //全局依赖表
        HashMap<Integer, HashSet<Integer>> adjacentDepth = new HashMap<>();

        //初始化邻接表
        for (int[] relate : richer) {   //子节点指向父节点
            int fatherNode = relate[0];
            int childNode = relate[1];
            if (!adjacent.containsKey(childNode)) {
                adjacent.put(childNode, new ArrayList<>());
            }
            adjacent.get(childNode).add(fatherNode);
        }

        //初始化全局依赖表
        for (int i = 0; i < quiet.length; i++) {
            adjacentDepth.put(i, new HashSet<>());
            adjacentDepth.get(i).add(i);    //自己也是自己的依赖，晕
        }

        //基于一次全局深度搜索，初始化全局依赖表
        int[] visited = new int[quiet.length];
        for (int i = 0; i < quiet.length; i++) {
            if (visited[i] == 0) {
                loudAndRichDfs(adjacent, adjacentDepth, visited, i);
            }
        }
        //获取各个节点的安静节点
        for (int i = 0; i < quiet.length; i++) {
            int targetNode = -1;
            int quietMin = Integer.MAX_VALUE;
            HashSet<Integer> fatherNode = adjacentDepth.get(i);
            for (int node : fatherNode) {
                if (quiet[node] < quietMin) {
                    //更新最小值
                    quietMin = quiet[node];
                    //更新目标值
                    targetNode = node;
                }
            }
            ans[i] = targetNode;
        }
        return ans;
    }

    private void loudAndRichDfs(HashMap<Integer, ArrayList<Integer>> adjacent, HashMap<Integer, HashSet<Integer>> adjacentDepth, int[] visited, int currentNode) {
        //迭代终止条件：叶子节点
        if (!adjacent.containsKey(currentNode)) {
            return;
        }

        visited[currentNode] = 1;
        ArrayList<Integer> fatherNodes = adjacent.get(currentNode);
        for (int father : fatherNodes) { //横向枚举搜索
            //纵向递归搜索
            if (visited[father] == 0) {  //未被搜索
                loudAndRichDfs(adjacent, adjacentDepth, visited, father);
            }

            //将邻接表添加至全局依赖中
            adjacentDepth.get(currentNode).add(father);
            //将子节点的全局依赖，添加至其父节点的全局依赖中
            if (adjacentDepth.containsKey(father)) {
                adjacentDepth.get(currentNode).addAll(adjacentDepth.get(father));
            }
        }
    }


    public int[] loudAndRich01(int[][] richer, int[] quiet) {
        int[] ans = new int[quiet.length];
        //邻接表
        HashMap<Integer, ArrayList<Integer>> adjacent = new HashMap<>();
        //入度
        int[] inDegree = new int[quiet.length];
        //初始化
        for (int[] relate : richer) {
            int fatherNode = relate[0];
            int childNode = relate[1];
            if (!adjacent.containsKey(fatherNode)) {
                adjacent.put(fatherNode, new ArrayList<>());
            }
            adjacent.get(fatherNode).add(childNode);  //父节点指向子节点
            inDegree[childNode]++;  //影响子节点入度
        }
        //可被搜索的节点
        ArrayDeque<Integer> arrayQueue = new ArrayDeque<>();   //入度为 0 的节点
        for (int i = 0; i < inDegree.length; i++) {
            if (inDegree[i] == 0) arrayQueue.add(i);
            //初始化各个节点的安静节点为自身
            ans[i] = i;  // 最关键的是哪些最富有的人，其安静节点一定为自身，也为下面队列中的初始节点（最富有的人）
        }
        //------------------------------------------------------
        // 逐层搜索和影响对应节点的入度
        //     基于影响后的入度，判断其是否还有其余比起大的值
        //     如无，则入度为 0，此此点此时的 ans[currentIndex]固定，同时可以入队列，去影响其他元素
        //------------------------------------------------------
        while (!arrayQueue.isEmpty()) {
            Integer fatherNode = arrayQueue.pollFirst();
            //当前节点不会影响其余节点
            if (!adjacent.containsKey(fatherNode)) {
                continue;
            }
            ArrayList<Integer> childNodes = adjacent.get(fatherNode);

            //------------------------------------------------
            // 对此节点进行影响性分析，即父节点对其子节点的影响
            //------------------------------------------------

            for (int childNode : childNodes) {
                //1、影响一：此节点的安静节点，一定是其所有父节点的安静节点中最安静的那个
                if (quiet[ans[fatherNode]] < quiet[ans[childNode]]) {
                    ans[childNode] = ans[fatherNode];
                }

                //2、影响二：此节点的入度，标识此节点的父节点中，还有多少个未被搜索
                inDegree[childNode]--;
                if (inDegree[childNode] == 0) {
                    arrayQueue.add(childNode);
                }
            }
        }
        return ans;
    }


    /**
     * 139. 单词拆分
     */
    public boolean wordBreak(String str, List<String> wordDict) {   //记忆化搜索，否则基本的 DFS 超时
        wordDict.sort((o1, o2) -> o2.length() - o1.length());    //按照长度排序
        int[] visited = new int[str.length() + 1];
        return wordBreakDfs(str, wordDict, visited, 0);
    }


    private boolean wordBreakDfs(String target, List<String> wordDict, int[] visited, int currentIndex) {
        //递归终止条件
        if (currentIndex == target.length()) {
            return true;
        }

        for (String word : wordDict) {   //横向枚举遍历
            //剪枝一：不满足条件，超长
            if (currentIndex + word.length() > target.length()) {
                continue;
            }

            //剪枝二：不满足条件，非前缀
            if (!target.startsWith(word, currentIndex)) {
                continue;
            }

            //剪枝三：重复搜索
            if (visited[currentIndex] == 1) {
                continue;
            }

            //-----------------------------------------------
            // 运行到这里说明当前单词是 target 的前缀
            //-----------------------------------------------

            //1、增加元素
            currentIndex += word.length();  //下一个待拼接的首个字符对应的索引

            //2、纵向递归搜索
            if (wordBreakDfs(target, wordDict, visited, currentIndex)) {
                return true;
            }

            //----------------------------------------------------------------------
            // 右上可知，当前拼接出的字符串一定是 target 的前缀，且前缀为 currentIndex
            // 其次，程序运行到这里说明，以此字符串为前缀基础，再尝试拼接其他字符，无法拼接处 target
            // 所以，假设当前的前缀为 abc，无论当此前缀是由 (abc)、(a bc)、(ab c)任意那种情况拼接而成，后续也不会成功拼接 target，所以直接忽略
            //----------------------------------------------------------------------
            visited[currentIndex] = 1;

            //3、减少元素
            currentIndex -= word.length();
        }
        return false;
    }

    public boolean wordBreak01(String str, List<String> wordDict) {   //动态规划，路径问题，类似爬楼梯
        int n = str.length();
        int[] dp = new int[n + 1];
        dp[0] = 1;
        for (int i = 0; i <= str.length(); i++) {
            if (dp[i] == 0) continue;  //无法基于当前位置跳跃
            for (String word : wordDict) {
                if (i + word.length() <= str.length() && str.startsWith(word, i)) {
                    dp[i + word.length()] = 1;
                    // 要将这层的全部搜索才行，这个不是 接力式的
//                    break;   //不能加，案例："cars"， ["car","ca","rs"]
                }
            }
        }
        return dp[n] == 1;
    }


    /**
     * 面试题 17.15. 最长单词
     */
    public String longestWord(String[] words) {   //DFS + 记忆化搜索，不加记忆化搜索，效率已经很高
        Arrays.sort(words, (o1, o2) -> {
            if (o1.length() != o2.length())
                return o2.length() - o1.length();   //1.按照长度排序，提高剪枝二的概率
            else
                return o1.compareTo(o2);            //2.按照字典序排序
        });
        for (String targetWord : words) {
            int[] visited = new int[targetWord.length() + 1];
            if (longestWordDfs(words, targetWord, visited, 0)) {
                return targetWord;
            }
        }
        return "";
    }

    private boolean longestWordDfs(String[] words, String targetWord, int[] visited, int currentIndex) {
        if (currentIndex == targetWord.length()) {
            return true;
        }

        for (String word : words) {   //横向枚举遍历
            //剪枝一：忽略自身
            if (targetWord.equals(word)) continue;

            //剪枝二：不满足要求，超长
            if (currentIndex + word.length() > targetWord.length()) continue;

            //剪枝三：不满足要求，非前缀
            if (!targetWord.startsWith(word, currentIndex)) continue;

            //剪枝四：不满足要求，记忆化搜索：当前前缀无论由哪些单词组成，后续均不会成功拼接 target
            if (visited[currentIndex] == 1) continue;   //此处 return false 也可以

            //--------------------------------------------
            // 运行到下面说明，当前单词是 targetWord 的前缀
            //--------------------------------------------

            //1、添加元素
            currentIndex += word.length();

            //2、深度递归搜索
            if (longestWordDfs(words, targetWord, visited, currentIndex)) {
                return true;
            }
            visited[currentIndex] = 1;

            //3、移除元素
            currentIndex -= word.length();
        }
        return false;
    }


    public String longestWord00(String[] words) {   //错误解法，原因在于 startWith 会有多种情况，如果遇到第一个就出来，可能不会把所有情况处理完
        Arrays.sort(words, (o1, o2) -> {
            if (o1.length() != o2.length())
                return o2.length() - o1.length();   //1.按照长度排序
            else
                return o1.compareTo(o2);            //2.按照字典序排序
        });
        HashSet<String> disWord = new HashSet<>(Arrays.asList(words));
        for (String word : words) {
            disWord.remove(word);
            String currentWord = word;
            int prevLength = currentWord.length();
            while (currentWord.length() > 0) {
                for (String xx : disWord) {
                    if (currentWord.startsWith(xx)) {
                        currentWord = currentWord.replaceFirst(xx, "");
                        break;
                    }
                }
                if (currentWord.length() == prevLength) {
                    break;
                }
                prevLength = currentWord.length();
            }
            if (currentWord.length() == 0) return word;
            disWord.add(word);
        }
        return "";
    }


    /**
     * 15. 三数之和
     */
    public List<List<Integer>> threeSum(int[] nums) {  //双指针
        List<List<Integer>> ans = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; i++) {  //枚举第一个数字的索引
            //提前终止循环，因为后面不会有满足情况的结
            if (nums[i] > 0) return ans;
            //1.剔除第一个数重复的情况
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int left = i + 1;              //初始值：第二个数字的索引
            int right = nums.length - 1;   //初始值：第三个数字的索引
            while (left < right) {   //从两端收缩
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    ans.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    left++;
                    right--;
                    //2.剔除第二个数重复的情况
                    while (left < right && nums[left] == nums[left - 1]) {
                        left++;
                    }
                    //3.剔除第三个数重复的情况
                    while (left < right && nums[right] == nums[right + 1]) {
                        right--;
                    }
                } else if (sum > 0) {
                    right--;
                } else {
                    left++;
                }
            }
        }
        return ans;
    }

    public List<List<Integer>> threeSum01(int[] nums) {   //DFS搜索，超时
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        Arrays.sort(nums);
        for (int i = 0, j = nums.length - 1; i < j; i++, j--) {  //降序排序，增大剪枝的概率
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
        threeSumDfs(nums, ans, path, 0, 0);
        return ans;
    }

    private void threeSumDfs(int[] nums, List<List<Integer>> ans, LinkedList<Integer> path, int currentIndex, int sum) {
        //迭代终止条件：找到满足题意的解
        if (path.size() == 3 && sum == 0) {
            ans.add(new ArrayList<>(path));
            return;
        }

        //剪枝一：超长
        if (path.size() >= 3) {
            return;
        }

        for (int i = currentIndex; i < nums.length; i++) {  //横向枚举搜索，纵向处理，同一个元素不可多次使用
            //剪枝二：横向剔除重复元素，即同一位置不使用相同的元素
            if (i > currentIndex && nums[i] == nums[i - 1]) {
                continue;
            }

            //剪枝三：不可能满足条件的分支，直接返回
            if (path.size() == 2 && ((sum > 0 && nums[i] >= 0) || (sum < 0 && nums[i] <= 0))) {
                continue;
            }

            //1、增加元素
            path.add(nums[i]);
            sum += nums[i];

            //2、纵向递归搜索
            threeSumDfs(nums, ans, path, i + 1, sum);

            //3、删除元素
            path.removeLast();
            sum -= nums[i];
        }
    }

    /**
     * 784. 字母大小写全排列
     */
    public List<String> letterCasePermutation(String str) {
        List<String> ans = new ArrayList<>();
        String path = "";
        letterCasePermutationDfs(str, ans, path, 0);
        return ans;
    }

    private void letterCasePermutationDfs(String str, List<String> ans, String path, int currentIndex) {
        //1、递归终止条件一：目标
        if (currentIndex == str.length()) {
            ans.add(path);
            return;
        }

        char currentCh = str.charAt(currentIndex);
        //无论是什么，都要进行自身的深度搜搜
        letterCasePermutationDfs(str, ans, path + currentCh, currentIndex + 1);

        if (Character.isLowerCase(currentCh)) {   //1、如果是小写，则要替换为大写再进行一次递归
            letterCasePermutationDfs(str, ans, path + Character.toUpperCase(currentCh), currentIndex + 1);
        }

        if (Character.isUpperCase(currentCh)) {   //2、如果是大写，则要替换为小写再进行一次递归
            letterCasePermutationDfs(str, ans, path + Character.toLowerCase(currentCh), currentIndex + 1);
        }
    }

    private void letterCasePermutationDfs01(String str, List<String> ans, String path, int currentIndex) {
        //1、递归终止条件一：目标
        if (currentIndex == str.length()) {
            ans.add(path);
            return;
        }

        char currentCh = str.charAt(currentIndex);
        //无论是什么，都要进行自身的深度搜搜
        letterCasePermutationDfs01(str, ans, path + currentCh, currentIndex + 1);

        if (Character.isLetter(currentCh)) {  //基于大小写转换的统一写法
            letterCasePermutationDfs01(str, ans, path + (char) (currentCh ^ ' '), currentIndex + 1);
        }
    }


    /**
     * 547. 省份数量
     */
    public int findCircleNum(int[][] grid) {   //深度优先搜索算法
        int provinces = 0;
        int n = grid.length;
        int[] visited = new int[n];   //关键，记录是否已经被搜索，避免重复搜素
        for (int i = 0; i < grid.length; i++) {
            if (visited[i] == 0) {    //从当前城市出发，进行深度搜索，感染
                provinces++;
                findCircleNumDfs(grid, visited, i);   //将于此城市直接或间接相邻的城市标记为 visited[city] = 1，感染
            }
        }
        return provinces;
    }

    private void findCircleNumDfs(int[][] grid, int[] visited, int city1) {
        //标记为已搜索
        visited[city1] = 1;
        //横向搜索，当前 city1 与哪些城市相邻
        for (int city2 = 0; city2 < grid.length; city2++) {
            //两个城市相邻，且相邻的城市未被搜索
            if (grid[city1][city2] == 1 && visited[city2] == 0) {
                //-------------------------------------------------------------------------------------
                // visited 的作用是记录当前城市是否被搜索，避免重复搜索和死循环
                // 值得注意的是，两个城市相邻或者说属于同一个省份时（grid[city1][city2] == 1），在遍历 city1 时才有机会判断于其相邻 city2 是否已经被搜索了，从而避免重复搜索和死循环
                //     如果两个城市不相邻，其实根本没机会判断 visited
                //-------------------------------------------------------------------------------------
                findCircleNumDfs(grid, visited, city2);      //纵向搜索，当前与 city1 直接相邻的 city2 与那些节点相邻，其均属于一个省份
            }
        }
    }

    public int findCircleNum01(int[][] grid) {   //广度优先搜索算法
        int provinces = 0;
        int[] visited = new int[grid.length];
        LinkedList<Integer> queue = new LinkedList<>();
        for (int i = 0; i < grid.length; i++) {
            if (visited[i] == 0) {
                provinces++;
                queue.addLast(i);  //可认为是当前集合（省份）的根节点
                //----------------------------------------------
                // 从根节点出发，依次找到与其相邻的节点，再从相邻的节点找到与根节点间接相邻的节点，依次类推，直至找到与当前根节点的整棵树
                // 为了简化逻辑，捅死不同树的层次、相邻层次对应的搜索逻辑一致
                // 因此直接用 !queue.isEmpty() 作为循环迭代的条件，而无需在每层开始搜索的时候，记录当前层次涉及的节点数 queue.size()，并以此作为此轮循环的终止条件
                //----------------------------------------------
                while (!queue.isEmpty()) {
                    Integer city1 = queue.pollFirst();  //搜索的顺序性，每层之间的搜索也是顺序平滑过渡的
                    visited[city1] = 1;
                    for (int city2 = 0; city2 < grid.length; city2++) {
                        if (visited[city2] == 0 && grid[city1][city2] == 1) {
                            queue.addLast(city2);
                        }
                    }
                }
            }
        }
        return provinces;
    }


    public int findCircleNum03(int[][] grid) {   //深度优先搜索算法
        int cityNums = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    findCircleNumDfs(grid, i, j);
                    cityNums++;
                }
            }
        }
        return cityNums;
    }

    private void findCircleNumDfs(int[][] grid, int city1, int city2) {
        //感染
        grid[city1][city2] = 0;  //不会再次遍历，故其实无需置零
        grid[city2][city1] = 0;

        //--------------------------------------------------
        // 当前的逻辑为自己写的，虽然可以通过，但不是严谨
        // 因为其只对 city2 进行深度搜索了，并未对 city1 进行深度搜索，而如果仅靠 上面的 for 循环对 city1 进行搜索，其实只是对 city1 相邻的第一个 city 进行了深度搜索
        // 可能存在与 city1 相邻的城市，被划分为其他省份（因为上面每一次 for 循环均需要进行 cityNums++ ）
        //--------------------------------------------------


        //横向枚举，与 city2 直接相连的城市
        for (int city3 = 0; city3 < grid[0].length; city3++) {
            if (grid[city2][city3] == 1) {
                findCircleNumDfs(grid, city2, city3);       //递归搜索 city2 与哪些城市相连，直接置为 0，因为其与 city1 间接相邻
            }
        }
    }


    /**
     * 1559. 二维网格图中探测环
     */
    public boolean containsCycle(char[][] grid) {  //存在闭环，故推荐使用深度优先搜索
        //--------------------------------------------------------
        // 本题关键在于在如何不走回头路、不走来时的路
        // 同时又不能更改二维矩阵中的元素值，也不能借助 visited 数组，因为 visited[i][j] == 1 代表此节点已经搜索，但其包含来时的节点和构成闭环的节点两种情况，单独基于 visited 无法区分两种情况
        //--------------------------------------------------------
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int m = grid.length;
        int n = grid[0].length;
        int[][] visited = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (visited[i][j] == 0) {
                    if (containsCycleDfs(grid, directions, visited, new int[]{0, 1}, grid[i][j], i, j)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean containsCycleDfs(char[][] grid, int[][] directions, int[][] visited, int[] currentDir, char ch, int currentRow, int currentCol) {
        if (visited[currentRow][currentCol] == 1 && grid[currentRow][currentCol] == ch) {
            return true;
        }

        visited[currentRow][currentCol] = 1;
        int[] reverse = new int[]{-currentDir[0], -currentDir[1]};   //来时方向的反方向

        for (int[] dir : directions) {
            if (Arrays.equals(dir, reverse)) {
                continue;   //关键，不走回头路，本题不能通过将走过的路径节点置为 -1 等元素来不走回头路
            }
            int nextRow = currentRow + dir[0];
            int nextCol = currentCol + dir[1];
            if (nextRow >= 0 && nextRow < grid.length && nextCol >= 0 && nextCol < grid[0].length) {
                //剪枝
                if (grid[nextRow][nextCol] != ch) {
                    continue;
                }
                if (containsCycleDfs(grid, directions, visited, dir, ch, nextRow, nextCol)) {  //当前方向，下一个元素
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 1559. 二维网格图中探测环
     */
    public boolean containsCycle01(char[][] grid) {  //存在闭环，故推荐使用并查集
        //------------------------------------------------------
        // 由于构成闭环的最后一个点一定是闭环右下角的点，其最后一个被遍历到
        // 因此当前点可以与上方或左侧的点判断字符是否匹配，如果匹配，则当前点和某个点连成一条边，合并在一个集合中
        //------------------------------------------------------
        int[][] directions = {{-1, 0}, {0, -1}};
        int m = grid.length;
        int n = grid[0].length;
        containsCycleUFS ufs = new containsCycleUFS(m * n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                //--------------------------------------------------
                // 顺序遍历各个点，而不是广度或深度的搜索，在遍历的过程中如果当前点的字符同上方或左侧点的字符一致，则构成了一条边，尝试合并
                //--------------------------------------------------
                for (int[] dir : directions) {
                    int prevRow = i + dir[0];
                    int prevCol = j + dir[1];
                    if (prevRow >= 0 && prevRow < m && prevCol >= 0 && prevCol < n) {
                        if (grid[i][j] == grid[prevRow][prevCol]) {
                            if (ufs.findSet(i * n + j) == ufs.findSet(prevRow * n + prevCol)) {
                                //----------------------------------------------------
                                // 注意，如果当前点作为闭环的最后一个点，则其首先与上方的点合并到一个集合，然后在与其左侧的点合并前，发现二者已经是一个集合了，此时返回 true
                                // 因此，当前点能否构成闭环，是分别于上方点和左侧点合并，而不是直接判断上方的点和左侧的点是否是一个集合（其实这种逻辑也可以，但没实现）
                                //----------------------------------------------------
                                return true;
                            }
                            ufs.union(i * n + j, prevRow * n + prevCol);
                        }
                    }
                }
            }
        }
        return false;
    }

    static class containsCycleUFS {
        int[] nodes;

        containsCycleUFS(int n) {
            this.nodes = new int[n];
            for (int i = 0; i < nodes.length; i++) {
                nodes[i] = i;
            }
        }

        private int findSet(int xx) {
            if (nodes[xx] != xx) {
                nodes[xx] = findSet(nodes[xx]);
            }
            return nodes[xx];
        }

        private void union(int xx, int yy) {
            int xRoot = findSet(xx);
            int yRoot = findSet(yy);
            if (xRoot == yRoot) return;
            nodes[xRoot] = yRoot;
        }
    }


    /**
     * 1222. 可以攻击国王的皇后
     */
    public List<List<Integer>> queensAttacktheKing(int[][] queens, int[] king) {
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        List<List<Integer>> ans = new ArrayList<>();
        int[][] grid = new int[8][8];
        for (int[] queen : queens) {
            grid[queen[0]][queen[1]] = 1;
        }
        for (int[] dir : directions) {  //逐一沿着 8 个方向搜索
            queensAttackDfs(grid, dir, king[0], king[1], ans);
        }
        return ans;
    }

    private void queensAttackDfs(int[][] grid, int[] dir, int row, int col, List<List<Integer>> ans) {
        //剪枝一：越界
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            return;
        }
        //找到目标
        if (grid[row][col] == 1) {
            ans.add(Arrays.asList(row, col));
            return;
        }
        queensAttackDfs(grid, dir, row + dir[0], col + dir[1], ans);
    }


    /**
     * 749. 隔离病毒
     */
    public int containVirus(int[][] isInfected) {  //firewall  around
        int ans = 0;
        int m = isInfected.length;
        int n = isInfected[0].length;
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        while (true) {
            //1、记录各个疫区周边相邻的未感染区域
            HashMap<Integer, HashSet<Integer>> around = new HashMap<>();  //通过此来判断当前要隔离那个疫区
            //2、记录各个疫区所需要的防火墙的数量
            HashMap<Integer, Integer> firewall = new HashMap<>();  //不关心各个疫区所需要的防火墙的数量
            int currIndex = 10;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    //每找到一个疫区，就进行一次广度优先搜索
                    if (isInfected[i][j] == 1) {
                        //若限制当前疫区疫情的蔓延，所需要的防火墙的数量
                        int currFirewall = 0;
                        //当前疫区周围的位点
                        HashSet<Integer> currAround = new HashSet<>();
                        //双端队列
                        ArrayDeque<int[]> queue = new ArrayDeque<>();
                        queue.addLast(new int[]{i, j});
                        //当前疫区编号
                        isInfected[i][j] = currIndex;
                        //广度优先搜索
                        while (!queue.isEmpty()) {
                            int[] tuple = queue.pollFirst();
                            int row = tuple[0];
                            int col = tuple[1];
                            for (int[] dir : directions) {
                                int nextRow = row + dir[0];
                                int nextCol = col + dir[1];
                                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                                    if (isInfected[nextRow][nextCol] == 1) {        //1、当前位点为疫区
                                        isInfected[nextRow][nextCol] = currIndex;
                                        queue.addLast(new int[]{nextRow, nextCol});
                                    } else if (isInfected[nextRow][nextCol] == 0) { //2、当前位点非疫区
                                        //当前疫区所需要的防火墙
                                        currFirewall++;
                                        //记录当前疫区周围临近的未感染的区域
                                        currAround.add(nextRow * n + nextCol);
                                    }
                                }
                            }
                        }
                        //记录当前疫区所需要的防火墙以及相邻区域的坐标
                        around.put(currIndex, currAround);
                        firewall.put(currIndex, currFirewall);
                        //自增疫区编号
                        currIndex++;
                    }
                }
            }

            //所有的感染结束
            if (around.isEmpty()) {
                //--------------------------------------------
                // 感染结束的标识是：当前已经没有可以感染的区域了，其包括两种情况：
                //     1、疫情已经得到控制；
                //     2、疫情已经蔓延至所有区域
                //--------------------------------------------
                break;
            }

            //1、找到影响范围最大的疫区
            int targetIndex = 10;
            for (int index : around.keySet()) {
                if (around.get(index).size() > around.get(targetIndex).size()) {
                    targetIndex = index;
                }
            }
            ans += firewall.get(targetIndex);  //记录需要的防火墙的数量
            //2、恢复非管控区域的默认值
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (isInfected[i][j] > 0) {  //疫区
                        if (isInfected[i][j] == targetIndex) {  //此轮要管控的疫区
                            isInfected[i][j] = -1;  //管控后的疫区，不会再此感染
                        } else {
                            isInfected[i][j] = 1;   //将疫区编号恢复为疫区标识
                        }
                    }
                }
            }
            //3、无管控的疫区向外传染一圈
            for (int index : around.keySet()) {
                if (index == targetIndex) {
                    continue;
                }
                HashSet<Integer> aroundArea = around.get(index);
                for (int num : aroundArea) {
                    int xx = num / n;
                    int yy = num % n;
                    isInfected[xx][yy] = 1;
                }
            }
        }
        return ans;
    }


    /**
     * 301. 删除无效的括号
     */
    public List<String> removeInvalidParentheses(String str) {
        List<String> ans = new ArrayList<>();
        int leftNums = 0;
        int rightNums = 0;
        //首先计算左括号和右括号被删除的最小数量
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') leftNums++;
            if (str.charAt(i) == ')') {
                if (leftNums > 0) leftNums--;
                else rightNums++;
            }
        }
        removeDfs(str, ans, leftNums, rightNums, 0);
        return ans;
    }

    private void removeDfs(String str, List<String> ans, int leftNums, int rightNums, int currIndex) {
        //递归终止条件
        if (leftNums == 0 && rightNums == 0) {
            if (isValid(str)) {
                ans.add(str);
            }
            return;
        }

        for (int i = currIndex; i < str.length(); i++) {
            //剪枝：重复情况
            if (i > currIndex && str.charAt(i) == str.charAt(i - 1)) {
                continue;
            }
            //剪枝：异常情况
            if (leftNums + rightNums > str.length() - i) {
                return;
            }

            //左括号，尝试移除
            if (str.charAt(i) == '(' && leftNums > 0) {
                removeDfs(str.substring(0, i) + str.substring(i + 1), ans, leftNums - 1, rightNums, i);
            }
            //右括号，尝试移除
            if (str.charAt(i) == ')' && rightNums > 0) {
                removeDfs(str.substring(0, i) + str.substring(i + 1), ans, leftNums, rightNums - 1, i);
            }
        }

    }

    private boolean isValid(String str) {
        int ans = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') ans++;
            if (str.charAt(i) == ')') ans--;
            if (ans < 0) return false;
        }
        return ans == 0;
    }

    /**
     * 529. 扫雷游戏
     */
    public char[][] updateBoard(char[][] board, int[] click) {
        int m = board.length;
        int n = board[0].length;
        int[][] visited = new int[m][n];   //关键，需要去重，队列中已经有的元素，不再添加至队列
        visited[click[0]][click[1]] = 1;
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.addLast(click);
        int[][] dirs = {{-1, 0}, {-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}};
        while (!queue.isEmpty()) {
            int[] tuple = queue.poll();
            int row = tuple[0];
            int col = tuple[1];
            if (board[row][col] == 'M') {
                board[row][col] = 'X';
                break;
            }
            //记录当前位点周围八个位点的地雷个数
            int num = 0;
            for (int[] dir : dirs) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    if (board[nextRow][nextCol] == 'M') {
                        num++;
                    }
                }
            }
            //1、周围不存在地雷，标识当前位点后，将周围的八个位点添加至队列中，用于后续搜索
            if (num == 0) {
                board[row][col] = 'B';
                //两次遍历数组
                for (int[] dir : dirs) {
                    int nextRow = row + dir[0];
                    int nextCol = col + dir[1];
                    if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                        if (visited[nextRow][nextCol] == 0) {  //同一个位点不重复进入队列
                            visited[nextRow][nextCol] = 1;
                            queue.addLast(new int[]{nextRow, nextCol});
                        }
                    }
                }
            } else {   //2、周围存在地雷，周围的位点不再搜索
                board[row][col] = (char) (num + '0');
            }
        }
        return board;
    }

    public char[][] updateBoard01(char[][] board, int[] click) {
        int[][] dirs = {{-1, 0}, {-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}};
        if (board[click[0]][click[1]] == 'M') {
            board[click[0]][click[1]] = 'X';
        } else {
            updateBoardDfs(board, dirs, click[0], click[1]);
        }
        return board;
    }

    //------------------------------------------------------
    // 深度优先搜索的逻辑：当前位点周围八个方向
    //     1、如果存在地雷，标识当前位点后，停止搜索
    //     2、如果没有地雷，标识当前位点后，继续周围八个位点搜索
    //------------------------------------------------------
    private void updateBoardDfs(char[][] board, int[][] dirs, int row, int col) {
        //记录当前位点周围的情况
        int nums = 0;
        for (int[] dir : dirs) {
            int nextRow = row + dir[0];
            int nextCol = col + dir[1];
            if (nextRow >= 0 && nextRow < board.length && nextCol >= 0 && nextCol < board[0].length) {
                if (board[nextRow][nextCol] == 'M') {
                    nums++;
                }
            }
        }
        //1、周围存在地雷
        if (nums > 0) {
            board[row][col] = (char) (nums + '0');
            return;
        }
        //2、周围不存在地雷
        board[row][col] = 'B';
        for (int[] dir : dirs) {
            int nextRow = row + dir[0];
            int nextCol = col + dir[1];
            if (nextRow >= 0 && nextRow < board.length && nextCol >= 0 && nextCol < board[0].length) {
                if (board[nextRow][nextCol] == 'E') {   //关键，仅搜索未被搜索的位点
                    updateBoardDfs(board, dirs, nextRow, nextCol);
                }
            }
        }
    }


    /**
     * 419. 甲板上的战舰
     */
    public int countBattleships(char[][] board) {   //DFS
        int ans = 0;
        int m = board.length;
        int n = board[0].length;
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'X') {
                    countBattleshipsDfs(board, dirs, i, j);
                    ans++;
                }
            }
        }
        return ans;
    }

    private void countBattleshipsDfs(char[][] board, int[][] dirs, int row, int col) {
        if (row < 0 || col < 0 || row >= board.length || col >= board[0].length) {
            return;
        }
        if (board[row][col] == '.') {
            return;
        }

        board[row][col] = '.';
        for (int[] dir : dirs) {
            int nextRow = row + dir[0];
            int nextCol = col + dir[1];
            countBattleshipsDfs(board, dirs, nextRow, nextCol);
        }

    }

    public int countBattleships01(char[][] board) {   //扫描遍历
        int ans = 0;
        int m = board.length;
        int n = board[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'X') {
                    ans++;
                    //将此战舰其他位点置为空
                    int row = i + 1;
                    while (row < m && board[row][j] == 'X') {
                        board[row][j] = '.';
                        row++;
                    }
                    int col = j + 1;
                    while (col < n && board[i][col] == 'X') {
                        board[i][col] = '.';
                        col++;
                    }
                }
            }
        }
        return ans;
    }

    public int countBattleships02(char[][] board) {   //UFS
        int m = board.length;
        int n = board[0].length;
        int[][] dirs = {{-1, 0}, {0, -1}};
        countBattleshipsUFS ufs = new countBattleshipsUFS(board);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'X') {
                    for (int[] dir : dirs) {
                        int nextRow = i + dir[0];
                        int nextCol = j + dir[1];
                        if (nextRow >= 0 && nextCol >= 0) {
                            if (board[nextRow][nextCol] == 'X') {
                                ufs.union(i * n + j, nextRow * n + nextCol);
                            }
                        }
                    }
                }
            }
        }
        return ufs.getNums();
    }

    static class countBattleshipsUFS {
        int[] nodes;

        countBattleshipsUFS(char[][] board) {
            int m = board.length;
            int n = board[0].length;
            this.nodes = new int[m * n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    int id = i * n + j;
                    if (board[i][j] == 'X') {
                        nodes[id] = id;
                    } else {
                        nodes[id] = -1;
                    }
                }
            }
        }

        private int findSet(int xx) {
            if (nodes[xx] != xx) {
                nodes[xx] = findSet(nodes[xx]);
            }
            return nodes[xx];
        }

        private void union(int xx, int yy) {
            int xRoot = findSet(xx);
            int yRoot = findSet(yy);
            if (xRoot == yRoot) return;
            nodes[xRoot] = yRoot;
        }

        private int getNums() {
            int ans = 0;
            for (int i = 0; i < nodes.length; i++) {
                if (nodes[i] == i) {
                    ans++;
                }
            }
            return ans;
        }

    }


    /**
     * 1034. 边界着色
     */
    public int[][] colorBorder(int[][] grid, int row, int col, int color) {  //DFS
        int m = grid.length;
        int n = grid[0].length;
        int[][] visited = new int[m][n];
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        List<int[]> ans = new ArrayList<>();
        colorBorderDfs(grid, dirs, visited, ans, row, col, grid[row][col]);
        for (int[] tuple : ans) {
            int xx = tuple[0];
            int yy = tuple[1];
            grid[xx][yy] = color;
        }
        return grid;
    }

    private void colorBorderDfs(int[][] grid, int[][] dirs, int[][] visited, List<int[]> ans, int row, int col, int color) {
        int isEdge = 0;
        for (int[] dir : dirs) {
            int nextRow = row + dir[0];
            int nextCol = col + dir[1];
            if (nextRow >= 0 && nextRow < grid.length && nextCol >= 0 && nextCol < grid[0].length) {
                if (grid[nextRow][nextCol] != color) {
                    isEdge = 1;
                } else if (visited[nextRow][nextCol] == 0) {   //容易判断错地方
                    visited[nextRow][nextCol] = 1;
                    colorBorderDfs(grid, dirs, visited, ans, nextRow, nextCol, color);
                }
            } else {
                isEdge = 1;
            }
        }
        if (isEdge == 1) {
            ans.add(new int[]{row, col});
        }
    }

    public int[][] colorBorder01(int[][] grid, int xx, int yy, int color) {  //BFS
        int m = grid.length;
        int n = grid[0].length;
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int[][] isVisited = new int[m][n];
        ArrayList<int[]> ans = new ArrayList<>();
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.addLast(new int[]{xx, yy});
        while (!queue.isEmpty()) {
            int[] tuple = queue.poll();
            int row = tuple[0];
            int col = tuple[1];
            int isEdge = 0;
            for (int[] dir : dirs) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    if (grid[nextRow][nextCol] != grid[xx][yy]) {
                        isEdge = 1;
                    } else if (isVisited[nextRow][nextCol] == 0) {
                        isVisited[nextRow][nextCol] = 1;
                        queue.addLast(new int[]{nextRow, nextCol});
                    }
                } else {
                    isEdge = 1;
                }
            }
            if (isEdge == 1) {
                ans.add(new int[]{row, col});
            }
        }
        for (int[] tuple : ans) {
            grid[tuple[0]][tuple[1]] = color;
        }
        return grid;
    }


    public int[][] colorBorder02(int[][] grid, int xx, int yy, int color) {  //BFS，错误写法
        int m = grid.length;
        int n = grid[0].length;
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int[][] isVisited = new int[m][n];
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.addLast(new int[]{xx, yy});
        while (!queue.isEmpty()) {
            int[] tuple = queue.poll();
            int row = tuple[0];
            int col = tuple[1];
            int isEdge = 0;
            for (int[] dir : dirs) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    if (grid[nextRow][nextCol] != grid[xx][yy]) {
                        grid[row][col] = color;    //动态更新，会有问题
                    } else if (isVisited[nextRow][nextCol] == 0) {
                        isVisited[nextRow][nextCol] = 1;
                        queue.addLast(new int[]{nextRow, nextCol});
                    }
                } else {
                    grid[row][col] = color;
                }
            }
        }
        return grid;
    }



    private int vowelNums = 0;

    /**
     * 1641. 统计字典序元音字符串的数目
     */
    public int countVowelStrings(int n) {
        char[] list = {'a', 'e', 'i', 'o', 'u'};
        LinkedList<Character> str = new LinkedList<>();
        countVowelDfs(list, str, n, 0);
        return vowelNums;
    }

    private void countVowelDfs(char[] list, LinkedList<Character> str, int n, int currIndex) {
        if (str.size() == n) {
            vowelNums++;
            return;
        }

        for (int i = currIndex; i < 5; i++) {
            str.addLast(list[i]);
            countVowelDfs(list, str, n, i);
            str.removeLast();
        }

    }



    class Employee {
        public int id;
        public int importance;
        public List<Integer> subordinates;
    }

    /**
     * 690. 员工的重要性
     */
    public int getImportance(List<Employee> employees, int id) {
        int ans = 0;
        HashMap<Integer, Employee> adjacent = new HashMap<>();
        for (Employee people : employees) {
            adjacent.put(people.id, people);
        }
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.add(id);
        while (!queue.isEmpty()) {
            Integer currID = queue.pollFirst();
            ans += adjacent.get(currID).importance;
            if (adjacent.get(currID).subordinates.size() > 0) {
                queue.addAll(adjacent.get(currID).subordinates);
            }
        }
        return ans;
    }

    public int getImportance01(List<Employee> employees, int id) {
        HashMap<Integer, Employee> adjacent = new HashMap<>();
        for (Employee people : employees) {
            adjacent.put(people.id, people);
        }
        return getImportanceDFS(id, adjacent);
    }

    private int getImportanceDFS(int currID, HashMap<Integer, Employee> adjacent) {
        if (adjacent.get(currID).subordinates.size() == 0) {
            return adjacent.get(currID).importance;
        }

        int sum = adjacent.get(currID).importance;
        for (int nextID : adjacent.get(currID).subordinates) {
            sum += getImportanceDFS(nextID, adjacent);
        }

        return sum;
    }

    /**
     * 1376. 通知所有员工所需的时间
     */
    public int numOfMinutes(int n, int headID, int[] manager, int[] informTime) {  //广度优先搜索
        int ans = 0;
        HashMap<Integer, ArrayList<Integer>> adjacent = new HashMap<>();
        for (int i = 0; i < manager.length; i++) {
            adjacent.putIfAbsent(manager[i], new ArrayList<>());
            if (manager[i] == -1) continue;
            adjacent.get(manager[i]).add(i);
        }
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{headID, 0});
        while (!queue.isEmpty()) {
            int[] tuple = queue.pollFirst();
            int currID = tuple[0];
            int currTime = tuple[1];
            if (adjacent.containsKey(currID)) {
                ArrayList<Integer> nextNodes = adjacent.get(currID);
                for (int nextNode : nextNodes) {
                    queue.add(new int[]{nextNode, currTime + informTime[currID]});
                }
            }
            ans = Math.max(ans, currTime);
        }
        return ans;
    }

    public int numOfMinutes01(int n, int headID, int[] manager, int[] informTime) {   //深度优先搜索
        HashMap<Integer, ArrayList<Integer>> adjacent = new HashMap<>();
        for (int i = 0; i < manager.length; i++) {
            adjacent.putIfAbsent(manager[i], new ArrayList<>());
            if (manager[i] == -1) continue;
            adjacent.get(manager[i]).add(i);
        }
        return numOfMinutesDFS(headID, 0, informTime, adjacent);
    }

    private int numOfMinutesDFS(int currNode, int currTime, int[] informTime, HashMap<Integer, ArrayList<Integer>> adjacent) {
        if (!adjacent.containsKey(currNode)) {
            return currTime;
        }

        int maxTimes = 0;
        ArrayList<Integer> nextNodes = adjacent.get(currNode);
        for (int nextNode : nextNodes) {
            maxTimes = Math.max(maxTimes, numOfMinutesDFS(nextNode, currTime + informTime[currNode], informTime, adjacent));
        }
        return maxTimes;
    }





}
