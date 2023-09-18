package leetcode.important;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MemorySearch {





    //------------------------------------------------------------
    // 路径问题，终点理解动态规划中的状态转移
    //------------------------------------------------------------

    /**
     * 62. 不同路径
     */
    public int uniquePaths(int m, int n) {  //类似杨辉三角
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || j == 0) {
                    dp[i][j] = 1;
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }
        return dp[m - 1][n - 1];
    }

    public int uniquePaths00(int m, int n) {  //类似杨辉三角
        int[][] dp = new int[m][n];
        //初始化，上边界和左边界
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for (int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }
        //动态规划
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];   //当前位点，只可从上方、左方到达
            }
        }
        return dp[m - 1][n - 1];
    }

    public int uniquePaths01(int m, int n) { //记忆化搜索，自顶向下的搜索
        int[][] direction = {{1, 0}, {0, 1}};
        int[][] cached = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(cached[i], -1);
        }
        return uniquePathsDfs(m, n, direction, cached, 0, 0);
    }

    private int uniquePathsDfs(int m, int n, int[][] direction, int[][] cached, int currentRow, int currentCol) { //自下向上的搜索
        //递归终止条件一：越界
        if (currentRow >= m || currentCol >= n) {
            return 0;
        }
        //递归终止条件二：找到一个目标路径
        if (currentRow == m - 1 && currentCol == n - 1) {
            return 1;
        }
        //递归终止条件三：记忆化搜索
        if (cached[currentRow][currentCol] != -1) {
            return cached[currentRow][currentCol];
        }

        int paths = 0;
        for (int[] dir : direction) { //横向枚举遍历
            int nextRow = currentRow + dir[0];
            int nextCol = currentCol + dir[1];
            paths += uniquePathsDfs(m, n, direction, cached, nextRow, nextCol);
        }
        cached[currentRow][currentCol] = paths; //当前已完成全部搜索，记忆化

        return paths;
    }

    //------------------------------------------------------------------------
    // 对比上下两种写法，一个是自顶而下的写法、一个是自低而上的写法
    //------------------------------------------------------------------------

    public int uniquePaths02(int m, int n) {  //类似杨辉三角
        int[][] cached = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(cached[i], -1);
        }
        int[][] directions = {{-1, 0}, {0, -1}};
        return uniquePathsDfs02(m, n, directions, cached, m - 1, n - 1);
    }

    private int uniquePathsDfs02(int m, int n, int[][] directions, int[][] cached, int currentRow, int currentCol) {
        //1、递归终止条件一：越界
        if (currentRow < 0 || currentCol < 0) {
            return 0;
        }
        //2、递归终止条件二：目标点
        if (currentRow == 0 && currentCol == 0) {
            return 1;
        }
        //3、递归终止条件三：已搜索
        if (cached[currentRow][currentCol] != -1) {
            return cached[currentRow][currentCol];
        }

        int sumPaths = 0;
        //横向枚举搜索
        for (int[] dir : directions) {
            int nextRow = currentRow + dir[0];
            int nextCol = currentCol + dir[1];
            sumPaths += uniquePathsDfs02(m, n, directions, cached, nextRow, nextCol);
        }
        //记忆化
        cached[currentRow][currentCol] = sumPaths;

        return sumPaths;
    }

    /**
     * 63. 不同路径 II
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {  //网格中有障碍物
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        int[][] dp = new int[m][n];
        //初始化，上边界和左边界
        for (int i = 0; i < m; i++) {
            if (obstacleGrid[i][0] == 1) {
                continue;
            }
            dp[i][0] = i == 0 ? 1 : dp[i - 1][0];
        }
        for (int j = 0; j < n; j++) {
            if (obstacleGrid[0][j] == 1) {
                continue;
            }
            dp[0][j] = j == 0 ? 1 : dp[0][j - 1];
        }
        //动态规划
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (obstacleGrid[i][j] == 1) {
                    continue;
                }
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }
        return dp[m - 1][n - 1];
    }

    public int uniquePathsWithObstacles00(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] dp = new int[m][n];
        if (grid[0][0] == 1) return 0;
        dp[0][0] = 1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j > 0 && grid[i][j] == 0) {
                    dp[i][j] = dp[i][j - 1];
                }
                if (j == 0 && i > 0 && grid[i][j] == 0) {
                    dp[i][j] = dp[i - 1][j];
                }
                if (i > 0 && j > 0 && grid[i][j] == 0) {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }
        return dp[m - 1][n - 1];
    }

    public int uniquePathsWithObstacles01(int[][] obstacleGrid) {  //网格中有障碍物
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        int[][] directions = {{1, 0}, {0, 1}};
        int[][] cached = new int[m][n];  //cached[i][j]状态定义：从点 [i,j] 移动至终点的路径数
        for (int i = 0; i < cached.length; i++) {
            Arrays.fill(cached[i], -1);
        }
        return uniquePathsWithObstaclesDfs(obstacleGrid, directions, cached, 0, 0);
    }

    private int uniquePathsWithObstaclesDfs(int[][] obstacleGrid, int[][] directions, int[][] cached, int currentRow, int currentCol) {
        //递归终止条件一：越界
        if (currentRow >= obstacleGrid.length || currentCol >= obstacleGrid[0].length) {
            return 0;
        }
        //递归终止条件二：遇到障碍物，此路径不通
        if (obstacleGrid[currentRow][currentCol] == 1) {
            return 0;
        }
        //递归终止条件三：找到一个目标路径
        if (currentRow == obstacleGrid.length - 1 && currentCol == obstacleGrid[0].length - 1) {
            return 1;
        }
        //递归终止条件四：记忆化搜索
        if (cached[currentRow][currentCol] != -1) {
            return cached[currentRow][currentCol];
        }

        int paths = 0;
        for (int[] dir : directions) { //横向枚举遍历
            int nextRow = currentRow + dir[0];
            int nextCol = currentCol + dir[1];
            paths += uniquePathsWithObstaclesDfs(obstacleGrid, directions, cached, nextRow, nextCol);
        }
        cached[currentRow][currentCol] = paths;   //当前位点搜索完成，记录其结果

        return paths;
    }

    //--------------------------------------------------------------
    // 对比上下两种写法，一个自顶向下写法，一个自下向上写法
    //--------------------------------------------------------------

    public int uniquePathsWithObstacles02(int[][] grid) {  //网格中有障碍物
        int m = grid.length;
        int n = grid[0].length;
        int[][] cached = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(cached[i], -1);
        }
        return uniquePathsWithObstaclesDfs02(grid, cached, m - 1, n - 1);
    }

    private int uniquePathsWithObstaclesDfs02(int[][] grid, int[][] cached, int currentRow, int currentCol) {
        //1、递归终止条件一：越界
        if (currentRow < 0 || currentCol < 0) {
            return 0;
        }
        //2、递归终止条件三：障碍物
        if (grid[currentRow][currentCol] == 1) {   //要放在上面
            return 0;
        }
        //3、递归终止条件二：目标位点
        if (currentRow == 0 && currentCol == 0) {
            return 1;
        }
        //4、递归终止条件三：已搜索
        if (cached[currentRow][currentCol] != -1) {
            return cached[currentRow][currentCol];
        }

        int sumPaths = 0;
        //横向枚举搜索
        sumPaths += uniquePathsWithObstaclesDfs02(grid, cached, currentRow - 1, currentCol);
        sumPaths += uniquePathsWithObstaclesDfs02(grid, cached, currentRow, currentCol - 1);

        //记忆化搜索
        cached[currentRow][currentCol] = sumPaths;

        return sumPaths;
    }

    /**
     * 931. 下降路径最小和
     */
    public int minFallingPathSum(int[][] matrix) {  //与上题类似，上题是三角形，此题为矩形，本题索引好控制一些
        int n = matrix.length;
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}};
        int[][] dp = new int[n][n];
        //初始化，首层
        dp[0] = matrix[0];
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n; j++) {
                //-------------------------------------------------------
                // 此处要转换一下想法：要从"从当前点可到达的两点"，转换为"当前节点可从哪些节点可到达"
                //-------------------------------------------------------
                int prevMin = Integer.MAX_VALUE;
                for (int[] dir : directions) {
                    int prevRow = i + dir[0];
                    int prevCol = j + dir[1];
                    if (prevCol < 0 || prevCol == n) {
                        continue;
                    }
                    prevMin = Math.min(prevMin, dp[prevRow][prevCol]);
                }
                dp[i][j] = prevMin + matrix[i][j];
            }
        }
        int ans = Integer.MAX_VALUE;
        for (int j = 0; j < n; j++) {
            ans = Math.min(ans, dp[n - 1][j]);
        }
        return ans;
    }

    public int minFallingPathSum01(int[][] matrix) {  //记忆化搜索
        int n = matrix.length;
        int[][] directions = {{1, -1}, {1, 0}, {1, 1}};
        int[][] cached = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(cached[i], -1);
        }
        int minFall = Integer.MAX_VALUE;
        for (int j = 0; j < n; j++) {
            minFall = Math.min(minFall, minFallingPathSumDfs(matrix, directions, cached, 0, j));
        }
        return minFall;
    }

    private int minFallingPathSumDfs(int[][] matrix, int[][] directions, int[][] cached, int currentRow, int currentCol) {
        //1、递归终止条件一：越界
        if (currentRow >= matrix.length || currentCol < 0 || currentCol >= matrix[0].length) {  //不会向上走，无需判断
            return 20001;  //给一个较大的数，用于剔除这种情况，路径和最大为 100*100*2
        }
        //2、递归终止条件二：记忆化搜索
        if (cached[currentRow][currentCol] != -1) {
            return cached[currentRow][currentCol];
        }
        //3、递归终止条件三：搜索到最底层
        if (currentRow == matrix.length - 1) {
            return matrix[currentRow][currentCol];
        }

        int minValue = Integer.MAX_VALUE;
        for (int[] dir : directions) {  //横向枚举遍历，对当前节点进行搜索
            int nextRow = currentRow + dir[0];
            int nextCol = currentCol + dir[1];
            minValue = Math.min(minValue, minFallingPathSumDfs(matrix, directions, cached, nextRow, nextCol) + matrix[currentRow][currentCol]);
        }
        cached[currentRow][currentCol] = minValue;

        return minValue;
    }

    //-----------------------------------------------------------------------------------------
    // 上下两个题目均为下降路径的最小和，都可以通过动态规划和记忆化搜索来实现，注意对比两种写法的差异：
    //    动态规划：自顶向下，体现在状态继承取决于上一层，核心思想为全局最优一定是处处局部最优，节点状态为从第一行任意节点到当前节点的最小路径和
    //    记忆搜索：自底向上，体现在递归到最底层后开始回溯，核心思想为大问题转换为小问题，节点状态为从当前节点到最后一行任意节点的最小路径和
    //-----------------------------------------------------------------------------------------

    /**
     * 1289. 下降路径最小和  II
     */
    public int minFallingPathSumII(int[][] grid) {  //类同上，状态继承的位点不同，此处为非同一列，上面是左上、正上、右上
        int n = grid.length;
        int[][] dp = new int[n][n];
        //初始化，首层
        dp[0] = grid[0];
        //动态规划
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int minVal = Integer.MAX_VALUE;
                for (int t = 0; t < n; t++) {
                    if (t != j) {
                        minVal = Math.min(minVal, dp[i - 1][t]);
                    }
                }
                dp[i][j] = grid[i][j] + minVal;
            }
        }
        int ans = Integer.MAX_VALUE;
        for (int j = 0; j < n; j++) {
            ans = Math.min(ans, dp[n - 1][j]);
        }
        return ans;
    }

    public int minFallingPathSumII00(int[][] grid) {  //记忆化搜索
        int n = grid.length;
        if (n == 1) return grid[0][0];  //案例特殊，一行一列，由于为正方形矩阵，故不会存在多行一列
        int[][] cached = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(cached[i], -1);
        }
        int ans = Integer.MAX_VALUE;
        for (int j = 0; j < n; j++) {
            ans = Math.min(ans, minFallingPathSumDfs(grid, cached, 0, j));
        }
        return ans;
    }

    private int minFallingPathSumDfs(int[][] grid, int[][] cached, int currentRow, int currentCol) {
        //1、递归终止条件一：向下越界
        if (currentRow == grid.length) {
            return 0;
        }
        //2、递归终止条件二：记忆化搜索
        if (cached[currentRow][currentCol] != -1) {
            return cached[currentRow][currentCol];
        }

        int minValue = Integer.MAX_VALUE;  //记录从点 [currentRow,currentCol] 到最后一行的最小路径和
        for (int i = 0; i < grid[0].length; i++) {
            if (i == currentCol) continue;
            minValue = Math.min(minValue, minFallingPathSumDfs(grid, cached, currentRow + 1, i) + grid[currentRow][currentCol]);
        }
        cached[currentRow][currentCol] = minValue;

        return minValue;
    }

    public int minFallingPathSumII01(int[][] grid) {  //优化时间复杂度，记录每一层中最小值和次最小值
        int n = grid.length;
        int[][] dp = new int[n][n];
        int t1 = -1;
        int t2 = -1;
        //初始化，首层及最大值和次最大值的索引
        for (int j = 0; j < n; j++) {
            dp[0][j] = grid[0][j];
            if (t1 == -1) {
                t1 = j;
            } else if (t2 == -1) {
                if (grid[0][j] < grid[0][t1]) {
                    t2 = t1;
                    t1 = j;
                } else {
                    t2 = j;
                }
            } else {
                if (grid[0][j] < grid[0][t1]) {
                    t2 = t1;
                    t1 = j;
                } else if (grid[0][j] < grid[0][t2]) {
                    t2 = j;
                }
            }
        }
        //动态规划
        for (int i = 1; i < n; i++) {
            int tt1 = -1;
            int tt2 = -1;
            for (int j = 0; j < n; j++) {
                dp[i][j] = grid[i][j] + (j != t1 ? dp[i - 1][t1] : dp[i - 1][t2]);
                if (tt1 == -1) {
                    tt1 = j;
                } else if (tt2 == -1) {
                    if (dp[i][j] < dp[i][tt1]) {
                        tt2 = tt1;
                        tt1 = j;
                    } else {
                        tt2 = j;
                    }
                } else {
                    if (dp[i][j] < dp[i][tt1]) {
                        tt2 = tt1;
                        tt1 = j;
                    } else if (dp[i][j] < dp[i][tt2]) {
                        tt2 = j;
                    }
                }
            }
            t1 = tt1;
            t2 = tt2;
        }
        return dp[n - 1][t1];
    }

    public int minFallingPathSumII0001(int[][] grid) {  //优化时间复杂度
        int n = grid.length;
        int[][] dp = new int[n][n];
        int t1 = -1;
        int t2 = -1;
        //初始化，首层及最大值和次最大值的索引
        for (int j = 0; j < n; j++) {
            dp[0][j] = grid[0][j];
            if (t1 == -1) {
                t1 = j;
            } else if (t2 == -1) {
                if (grid[0][j] > grid[0][t1]) {
                    t2 = t1;
                    t1 = j;
                } else {
                    t2 = j;
                }
            } else {
                if (grid[0][j] > grid[0][t1]) {
                    t2 = t1;
                    t1 = j;
                } else if (grid[0][j] > grid[0][t2]) {
                    t2 = j;
                }
            }
        }
        //动态规划
        for (int i = 1; i < n; i++) {
            int tt1 = -1;
            int tt2 = -1;
            for (int j = 0; j < n; j++) {
                dp[i][j] += (j != t1 ? dp[i - 1][t1] : dp[i - 1][t2]);
                if (tt1 == -1) {
                    tt1 = j;
                } else if (tt2 == -1) {
                    if (dp[i][j] > dp[i][tt1]) {
                        tt2 = tt1;
                        tt1 = j;
                    } else {
                        tt2 = j;
                    }
                } else {
                    if (dp[i][j] > dp[i][tt1]) {
                        tt2 = tt1;
                        tt1 = j;
                    } else if (dp[i][j] > dp[i][tt2]) {
                        tt2 = j;
                    }
                }
            }
            t1 = tt1;
            t2 = tt2;
        }
        return dp[n - 1][t1];
    }




    /**
     * 118. 杨辉三角
     */
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> ans = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j <= i; j++) {    // 至 i
                if (j == 0 || j == i) {
                    row.add(1);
                } else {
                    row.add(ans.get(i - 1).get(j - 1) + ans.get(i - 1).get(j));
                }
            }
            ans.add(row);
        }
        return ans;
    }

    /**
     * 119. 杨辉三角 II
     */
    public List<Integer> getRow(int rowIndex) {
        List<Integer> prev = new ArrayList<>();
        for (int i = 0; i <= rowIndex; i++) {
            List<Integer> current = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                if (j == 0 || j == i) {
                    current.add(1);
                } else {
                    current.add(prev.get(j - 1) + prev.get(j));
                }
            }
            prev = current;
        }
        return prev;
    }


    /**
     * 799. 香槟塔
     */
    public double champagneTower(int poured, int query_row, int query_glass) {
        double[][] dp = new double[query_row + 1][query_row + 1];
        dp[0][0] = poured;
        for (int i = 1; i <= query_row; i++) {
            for (int j = 0; j <= i; j++) {
                if (j == 0) {
                    if (dp[i - 1][0] > 1) {
                        dp[i][0] = (dp[i - 1][0] - 1) * 1.0 / 2;
                    }
                } else if (j == i) {
                    if (dp[i - 1][i - 1] > 1) {
                        dp[i][i] = (dp[i - 1][i - 1] - 1) * 1.0 / 2;
                    }
                } else {
                    dp[i][j] += dp[i - 1][j - 1] > 1 ? dp[i - 1][j - 1] - 1 : 0;
                    dp[i][j] += dp[i - 1][j] > 1 ? dp[i - 1][j] - 1 : 0;
                    dp[i][j] = dp[i][j] * 1.0 / 2;
                }
            }
        }
        return Math.min(1.0, dp[query_row][query_glass]);
    }


    /**
     * 120. 三角形最小路径和
     */
    public int minimumTotal(List<List<Integer>> triangle) {  //动态规划
        int n = triangle.size();
        int[][] dp = new int[n][n];
        dp[0][0] = triangle.get(0).get(0);
        //---------------------------------------------------------------------------
        // 此处要转换一下想法：要从"从当前点可到达的两点"，转换为"当前节点可从哪些节点可到达"
        //---------------------------------------------------------------------------
        for (int i = 1; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                if (j == 0) {
                    dp[i][j] = dp[i - 1][j] + triangle.get(i).get(j);  //左上方无法继承
                } else if (j == i) {
                    dp[i][j] = dp[i - 1][j - 1] + triangle.get(i).get(j);  //右上方无法继承
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1], dp[i - 1][j]) + triangle.get(i).get(j);  //左上方和右上方均可继承，取最小值
                }
            }
        }
        int minimumTotal = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            minimumTotal = Math.min(minimumTotal, dp[n - 1][i]);
        }
        return minimumTotal;
    }


    public int minimumTotal01(List<List<Integer>> triangle) {  //记忆化搜索
        int n = triangle.size();
        int[][] directions = {{1, 0}, {1, 1}};
        int[][] cached = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(cached[i], -1);
        }
        return minimumTotalDfs(triangle, directions, cached, 0, 0);  //深度优先搜索的方程就定义为从当前点到终点的最小路径和
    }

    private int minimumTotalDfs(List<List<Integer>> triangle, int[][] directions, int[][] cached, int currentRow, int currentCol) {
        //1、递归终止条件一：下方越界
        if (currentRow == triangle.size()) {
            return 0;
        }
        //2、递归终止条件二：右侧越界
        if (currentCol == triangle.size()) {
            return 200 * 10000 + 2;  //给出一个较大值，剔除这种情况，结合题中最大值给出，不要给 Integer.MAX_VALUE，累加后可能越界
        }
        //3、递归终止条件三：记忆化
        if (cached[currentRow][currentCol] != -1) {
            return cached[currentRow][currentCol];
        }

        int minimumTotal = Integer.MAX_VALUE;
        for (int[] dir : directions) {
            int nextRow = currentRow + dir[0];
            int nextCol = currentCol + dir[1];
            minimumTotal = Math.min(minimumTotal, minimumTotalDfs(triangle, directions, cached, nextRow, nextCol) + triangle.get(currentRow).get(currentCol));
        }
        cached[currentRow][currentCol] = minimumTotal;

        return minimumTotal;
    }





    //--------------------------------------------------------------------------
    // 爬楼梯的问题
    //--------------------------------------------------------------------------

    /**
     * 面试题 08.01. 三步问题
     */
    public int waysToStep(int n) {
        if (n <= 2) return n;
        long mod = (int) Math.pow(10, 9) + 7;
        long[] dp = new long[n + 1];    //long存储
        dp[0] = 1;   //到第 0 个台阶的走法，不走
        dp[1] = 1;   //到第 1 个台阶的走法
        dp[2] = 2;   //到第 2 个台阶的走法
        for (int i = 3; i <= n; i++) {
            //----------------------------------
            // 从 i - 1 处直接走到 i，唯一走法
            // 从 i - 2 处直接走到 i，唯一走法
            // 从 i - 3 处直接走到 i，唯一走法
            //    关注的是走法，而不是步数
            //----------------------------------
            dp[i] = (dp[i - 1] + dp[i - 2] + dp[i - 3]) % mod;
        }
        return (int) (dp[n] % mod);
    }

    //-------------------------------------------------
    // 上面写法的时间和空间复杂度分别为： O(n)、O(n)
    // 下面写法的时间和空间复杂度分别为： O(n)、O(1) 优化了空间复杂度
    //-------------------------------------------------

    public int waysToStep00(int n) {     //动态规划 + 滚动数组
        //滚动数组的主要作用是用有限个变量来维护一组状态，降低空间复杂度
        if (n < 3) return n;
        long mod = (int) Math.pow(10, 9) + 7;
        long dp1 = 1;   //到第 1 个台阶的走法
        long dp2 = 2;   //到第 2 个台阶的走法
        long dp3 = 4;   //到第 3 个台阶的走法
        for (int i = 4; i <= n; i++) {
            long temp = (dp1 + dp2 + dp3) % mod;
            dp1 = dp2;
            dp2 = dp3;
            dp3 = temp;
        }
        return (int) dp3;
    }

    public int waysToStep01(int n) {  //将问题泛化，多重线性背包
        if (n <= 2) return n;
        int mod = (int) Math.pow(10, 9) + 7;
        int[] nums = {1, 2, 3};
        long[] dp = new long[n + 1];
        dp[0] = 1;
        for (int j = 1; j <= n; j++) {   //1、遍历背包，排列问题
            for (int num : nums) {       //2、遍历物品
                if (j >= num) {
                    dp[j] += dp[j - num];
                    dp[j] %= mod;
                }
            }
        }
        return (int) dp[n];
    }

    //--------------------------------------------------------------------------------------------------
    // 能否将爬楼梯转换为 0-1 背包问题，答案是不能的，因为 0 - 1 背包主要考虑，当前物品取或不取从而导致的不同价值
    //--------------------------------------------------------------------------------------------------

    public int waysToStep20(int n) {  //当前是错误的解法，能否将爬楼梯问题转换为 0-1背包，排列的问题
        if (n <= 2) return n;
        int mod = (int) Math.pow(10, 9) + 7;
        int[] nums = {1, 2, 3};
        long[][] dp = new long[nums.length + 1][n + 1];
//        for (int i = 0; i < nums.length; i++) {
//            dp[i][0] = 1;
//        }

        dp[0][0] = 1;
        dp[0][1] = 1;
        dp[0][2] = 2;

        for (int j = 0; j <= n; j++) {                     //1、遍历背包，排列问题
            for (int i = 1; i <= nums.length; i++) {       //2、遍历物品
                if (j >= nums[i - 1])
                    dp[i][j] = (dp[i][j] + dp[i - 1][j - nums[i - 1]]) % mod;
            }
        }
        return (int) dp[nums.length][n];
    }

    /**
     * 70. 爬楼梯
     */
    public int climbStairs(int n) {   //题目和上面完全一致，此处仅仅用 泛化的解法来做
        if (n <= 2) return n;
        int[] nums = {1, 2};
        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i <= n; i++) {
            for (int num : nums) {
                if (i >= num) dp[i] += dp[i - num];
            }
        }
        return dp[n];
    }

    public int climbStairs01(int n) {
        if (n <= 2) return n;
        int[] nums = {1, 2};
        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];    //未泛化的写法，体会二者的差异，以及为什么要先遍历背包
        }
        return dp[n];
    }

    //---------------------------------------------------------------
    // 记忆化搜索：记录遍历过的节点及其结果，可通过树状图来清晰的表示，搜索过程，其中重复的位点一目了然
    // 1、自顶向下的递归
    // 2、自下向上的递归
    //---------------------------------------------------------------
    HashMap<Integer, Integer> hTable = new HashMap<>();

    public int climbStairs10(int n) {  //1、自顶向下的递归
        if (n == 1) return 1;  //递归终止条件一：递归至余额 n == 1，则只有 1 种情况
        if (n == 2) return 2;  //递归终止条件二：递归至余额 n == 2，则只有 2 种情况
        if (hTable.containsKey(n)) return hTable.get(n);  //递归终止条件三：递归至余额 n == xx，则只有 yy 种情况，剪枝

        int steps = climbStairs10(n - 1) + climbStairs10(n - 2);  //纵向递归，当当前位置 n ,可走 1 步或 2 步，然后分别递归二者对应位置的情况
        hTable.put(n, steps);

        return steps;
    }

    HashMap<Integer, Integer> cached = new HashMap<>();

    public int climbStairs12(int n) {  //2、自下向上的递归
        int[] nums = {1, 2};
        climbStairsDfs01(nums, n, 0);
        return cached.get(0);
    }

    private int climbStairsDfs01(int[] nums, int target, int currentIndex) {
        if (currentIndex > target) {
            return 0;
        }
        if (currentIndex == target) {
            return 1;
        }
        if (cached.containsKey(currentIndex)) {
            return cached.get(currentIndex);
        }
        int paths = 0;
        for (int num : nums) {
            paths += climbStairsDfs01(nums, target, currentIndex + num);
        }
        cached.put(currentIndex, paths);
        return paths;
    }

    int couns = 0;

    public int climbStairs11(int n) {  //未添加记忆化的自低向上的搜索，超时，案例 44
        int[] nums = {1, 2};
        climbStairsDfs(nums, n, 0);
        return couns;
    }

    private void climbStairsDfs(int[] nums, int sum, int currentValue) {
        if (currentValue > sum) {  //当前路径的组合不满足条件，跳出
            return;
        }
        if (currentValue == sum) { //当前路径的组合满足条件，记录并跳出
            couns++;
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            //1、添加元素
            currentValue += nums[i];
            //2、纵向递归搜索
            climbStairsDfs(nums, sum, currentValue);
            //3、删除元素
            currentValue -= nums[i];
        }
    }

    /**
     * 746. 使用最小花费爬楼梯
     */
    public int minCostClimbingStairs(int[] cost) {  //线性背包
        //--------------------------------------------
        // 问题分析：
        //     假设数组 cost 的长度为 n,则 n 个阶梯对应的下标为 0 到 n - 1，楼层顶部对应下标 n
        // 问题等价：
        //     到达下标 n 的最小花费
        //--------------------------------------------
        int n = cost.length;
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 0;
        for (int i = 2; i <= n; i++) {
            //到达下标 i 楼梯的最小花费
            dp[i] = Math.min(dp[i - 1] + cost[i - 1], dp[i - 2] + cost[i - 2]);
        }
        return dp[n];
    }

    public int minCostClimbingStairs00(int[] cost) {  //线性背包，问题泛化
        int n = cost.length;
        int[] nums = {1, 2};
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 0;
        for (int i = 2; i <= n; i++) {
            for (int num : nums) {
                if (num == 1) dp[i] = dp[i - 1] + cost[i - 1];
                else if (num <= i) dp[i] = Math.min(dp[i], dp[i - num] + cost[i - num]);
            }
        }
        return dp[n];
    }

    public int minCostClimbingStairs01(int[] cost) {  //线性背包转换为滚动数组
        int n = cost.length;
        //针对当前 current 台阶来讲
        int prevPrev = 0;  //初始化，达到坐标 0 的台阶，所需花费的最小费用
        int prev = 0;      //初始化，达到坐标 1 的台阶，所需花费的最小费用
        for (int i = 2; i <= n; i++) {   //台阶坐标
            int current = Math.min(prev + cost[i - 1], prevPrev + cost[i - 2]);
            prevPrev = prev;
            prev = current;
        }
        return prev;
    }

    HashMap<Integer, Integer> cachedNode = new HashMap<>();

    public int minCostClimbingStairs12(int[] cost) {  //1、记忆化搜索，自顶向下
        int n = cost.length;
        int[] nums = {1, 2};
        return minCostClimbingStairsDfs12(cost, nums, n);
    }

    private int minCostClimbingStairsDfs12(int[] cost, int[] nums, int currentIndex) {
        //递归终止条件一：越界
        if (currentIndex < 0) {
            return 0;
        }
        //递归终止条件二：递归终点
        if (currentIndex == 0 || currentIndex == 1) {  //可以从这两个点开始爬楼梯
            return 0;
        }
        //递归终止条件三：已搜索，记忆化
        if (cachedNode.containsKey(currentIndex)) {
            return cachedNode.get(currentIndex);
        }
        //横向枚举遍历
        int minPay = Integer.MAX_VALUE;
        for (int num : nums) {
            //1、更新元素
            int prevIndex = currentIndex - num;
            //2、纵向递归搜素
            minPay = Math.min(minPay, minCostClimbingStairsDfs12(cost, nums, prevIndex) + cost[prevIndex]);
        }
        cachedNode.put(currentIndex, minPay);
        return minPay;
    }

    public int minCostClimbingStairs21(int[] cost) {   //2、记忆化搜索，自低向上
        int[] nums = {1, 2};
        return Math.min(
                minCostClimbingStairsDfs21(cost, nums, 0),
                minCostClimbingStairsDfs21(cost, nums, 1));
    }

    private int minCostClimbingStairsDfs21(int[] cost, int[] nums, int currentIndex) {
        //递归终止条件一：递归终点
        if (currentIndex >= cost.length) {
            return 0;
        }
        //递归终止条件二：记忆化搜索
        if (cachedNode.containsKey(currentIndex)) {
            return cachedNode.get(currentIndex);
        }

        int minPay = Integer.MAX_VALUE;
        for (int num : nums) {
            int nextIndex = currentIndex + num;
            minPay = Math.min(minPay, minCostClimbingStairsDfs21(cost, nums, nextIndex) + cost[currentIndex]);  //注意这里的 currentIndex 和 nextIndex
        }
        cachedNode.put(currentIndex, minPay);
        return minPay;
    }

    /**
     * 1137. 第 N 个泰波那契数
     */
    public int tribonacci(int n) {   //线性背包，简化版本的爬楼梯
        if (n == 0) return 0;
        if (n == 1 || n == 2) return 1;
        int dp0 = 0;
        int dp1 = 1;
        int dp2 = 1;
        for (int i = 3; i <= n; i++) {
            int currentDp = dp0 + dp1 + dp2;
            dp0 = dp1;
            dp1 = dp2;
            dp2 = currentDp;
        }
        return dp2;
    }


    /**
     * 198. 打家劫舍
     */
    public int rob(int[] nums) {  //动态规划
        //类似与爬楼梯问题
        int n = nums.length;
        if (n == 0) return 0;
        if (n == 1) return nums[0];
        if (n == 2) return Math.max(nums[0], nums[1]);
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);
        for (int i = 2; i < nums.length; i++) {
            dp[i] = Math.max(
                    dp[i - 1],              //当前房屋不取，基于上一个房屋的状态即可
                    dp[i - 2] + nums[i]);   //当前房屋取，要隔一个房屋
        }
        return dp[nums.length - 1];
    }



    public int rob01(int[] nums) {  //记忆化搜索
        int n = nums.length;
        if (n == 1) return nums[0];
        if (n == 2) return Math.max(nums[0], nums[1]);
        int[] steps = {1, 2};
        int[]  cachedRob = new int[n];
        Arrays.fill(cachedRob, -1);
        //---------------------------------------------------------------
        // 自下而上的搜索，最先记录的索引靠后的位置的最值
        //---------------------------------------------------------------
        return Math.max(robDfs01(nums, cachedRob, steps, 0), robDfs01(nums, cachedRob, steps, 1));
    }

    private int robDfs01(int[] nums, int[] cached, int[] steps, int currentIndex) {
        //1、递归终止条件一：越界
        if (currentIndex >= nums.length) {
            return 0;
        }
        //2、递归终止条件二：记忆化
        if (cached[currentIndex] != -1) {
            return cached[currentIndex];
        }

        int maxRob = Integer.MIN_VALUE;
        for (int step : steps) {
            int nextIndex = currentIndex + step;
            maxRob = Math.max(maxRob, robDfs01(nums, cached, steps, nextIndex) + (step == 1 ? 0 : nums[currentIndex]));
        }
        cached[currentIndex] = maxRob;

        return maxRob;
    }

    //--------------------------------------------------------------------------------
    // 对比两种写法，一个自顶向下，一个自下而上
    //--------------------------------------------------------------------------------

    public int rob02(int[] nums) {
        int n = nums.length;
        int[] cached = new int[n];
        Arrays.fill(cached, -1);
        return Math.max(robDfs02(nums, cached, n - 1), robDfs02(nums, cached, n - 2));
    }

    private int robDfs02(int[] nums, int[] cached, int currentIndex) {
        //递归终止条件一：越界
        if (currentIndex < 0) {
            return 0;
        }
        //递归终止条件二：已搜索
        if (cached[currentIndex] != -1) {
            return cached[currentIndex];
        }

        int max = -1;

        //广度优先搜索，横向枚举
        max = Math.max(max, robDfs02(nums, cached, currentIndex - 1));
        max = Math.max(max, robDfs02(nums, cached, currentIndex - 2) + nums[currentIndex]);

        //记忆化
        cached[currentIndex] = max;

        return max;
    }

    /**
     * 213. 打家劫舍 II
     */
    public int robII(int[] nums) {
        //----------------------------------------------------------------------------------------
        // 分而治之：
        //     环状排列意味着第一个房子和最后一个房子中只能选择一个偷窃，因此可以把此环状排列房间问题约化为两个单排排列房间子问题
        // 算法题有两大核心简化复杂问题的思路：减而治之和分而治之，这题把环形分为单排就是分而治之，即把一个问题分成多个规模更小的子问题
        //----------------------------------------------------------------------------------------
        int n = nums.length;
        if (n == 1) return nums[0];
        if (n == 2) return Math.max(nums[0], nums[1]);
        return Math.max(
                robHelper(Arrays.copyOfRange(nums, 0, nums.length - 1)),
                robHelper(Arrays.copyOfRange(nums, 1, nums.length)));
    }

    private int robHelper(int[] nums) {  //同 198. 打家劫舍 解法，使用滚动数组进行优化，优化空间复杂度
        int n = nums.length;
        if (n == 1) return nums[0];
        if (n == 2) return Math.max(nums[0], nums[1]);
        int dp1 = nums[0];
        int dp2 = Math.max(nums[0], nums[1]);
        for (int i = 2; i < n; i++) {
            int dp3 = Math.max(dp1 + nums[i], dp2);
            dp1 = dp2;
            dp2 = dp3;
        }
        return dp2;
    }

    public int robII01(int[] nums) {  //记忆化搜索，直接基于 198. 打家劫舍
        int n = nums.length;
        if (n == 1) return nums[0];
        if (n == 2) return Math.max(nums[0], nums[1]);
        return Math.max(
                rob01(Arrays.copyOfRange(nums, 0, nums.length - 1)),
                rob01(Arrays.copyOfRange(nums, 1, nums.length)));
    }

     //---------------------------------------------------------------------
     // 以下为和字符串相关的动态规划和记忆化搜索
     //---------------------------------------------------------------------

    /**
     * 139. 单词拆分
     */
    public boolean wordBreak(String str, List<String> wordDict) {   //动态规划，路径问题，类似爬楼梯
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

    public boolean wordBreak01(String str, List<String> wordDict) {   //记忆化搜索，否则基本的 DFS 超时
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

    public boolean wordBreak02(String str, List<String> wordDict) {    //记忆化搜索，否则基本的 DFS 超时
        boolean[] visited = new boolean[str.length() + 1];
        wordDict.sort((o1, o2) -> o2.length() - o1.length());   //增加剪枝的概率
        return wordBreakDfs(str, wordDict, visited, 0);
    }

    private boolean wordBreakDfs(String str, List<String> wordDict, boolean[] visited, int currentWindow) {
        //迭代终止条件
        if (str.length() == 0) {
            return true;
        }

        for (String word : wordDict) {  //横向枚举遍历
            //剪枝一：非前缀
            if (!str.startsWith(word)) continue;

            //剪枝二：长度超过限制
            if (word.length() > str.length()) continue;

            //剪枝三：
            if (visited[currentWindow]) continue;


            //1、添加元素
            str = str.replaceFirst(word, "");
            currentWindow += word.length();

            //2、纵向递归搜索
            if (wordBreakDfs(str, wordDict, visited, currentWindow)) {
                return true;
            }
            visited[currentWindow] = true;

            //3、删除元素
            str = word + str;
            currentWindow -= word.length();
        }
        return false;
    }


    /**
     * 97. 交错字符串
     */
    public boolean isInterleave(String str1, String str2, String str3) {   //动态规划
        if (str1.length() + str2.length() != str3.length())
            return false;
        int len1 = str1.length();
        int len2 = str2.length();
        // dp[i][j] 表示 str1 的前 i 个字符和 str2 的前 j 个字符，能否构成 str3 的前 i + j 个字符，均含
        int[][] dp = new int[len1 + 1][len2 + 1];

        //---------------------------------------------------------
        // 针对坐标系进行分析：
        //     横坐标，表示 str1，其中 i - 1 对应 str1 中的索引 i
        //     纵坐标，表示 str2，其中 j - 1 对应 str2 中的索引 j
        //     联合坐标，表示 str3，其中 i + j - 1 对应 str3 中的索引 index
        //---------------------------------------------------------

        //初始化二维数据原点
        dp[0][0] = 1;
        //初始化二维数组第一列
        for (int i = 1; i <= len1; i++) {
            if (dp[i - 1][0] == 1 && str1.charAt(i - 1) == str3.charAt(i - 1)) {  //注意 dp 行的个数比 str1长度多一位
                dp[i][0] = 1;  //此路可通
            }
        }
        //初始化二维数组第一行
        for (int j = 1; j <= len2; j++) {
            if (dp[0][j - 1] == 1 && str2.charAt(j - 1) == str3.charAt(j - 1)) {  //注意 dp 列的个数比 str2长度多一位
                dp[0][j] = 1;  //此路可通
            }
        }
        //动态规划
        for (int i = 1; i <= len1; i++) {           //str1的指针、纵向（横坐标）
            for (int j = 1; j <= len2; j++) {       //str2的指针、横向（纵坐标）
                //-----------------------------------------------
                // 横向和纵向，只要有一个满足情况，此路可通
                //-----------------------------------------------

                //纵向
                if (dp[i - 1][j] == 1 && str1.charAt(i - 1) == str3.charAt(i + j - 1)) {
                    dp[i][j] = 1;   //注意：此处前 i 个字符，对应在 str1中的索引为 i - 1
                }
                //横向
                if (dp[i][j - 1] == 1 && str2.charAt(j - 1) == str3.charAt(i + j - 1)) {
                    dp[i][j] = 1;   //注意：此处前 j 个字符，对应在 str2中的索引为 j - 1
                }

                //-------------------------------------------------------------------------------------------------
                // 其实可以这样理解，我们关注的是 str3 的索引 currentIndex处的字符，是否可以在 str1 和 str2 中"相关"位置获取
                //     遍历的顺序也可以是按照 currentIndex++ 为导引的，给所有 i + j - 1 的点（分布在斜率为 1 的直线）打标，只要横纵方向对应的有此 currentIndex的字符，则为 true/1
                //     由于没有判断连续性，因此，为了保证可以构成连续的 str3，所以需要对二维表格进行深度优先搜索，移动方向仅为向下或向右，是否能够找到一条连续的路径，到达右下角
                //  上面这种写法，其实是以 i 和 j 的形式来遍历二维数组，简单直观，和上面一个意思，同时也保证了连续性
                //-------------------------------------------------------------------------------------------------
            }
        }
        return dp[len1][len2] == 1;
    }


    public boolean isInterleave01(String str1, String str2, String str3) {  //记忆化搜索
        if (str1.length() + str2.length() != str3.length()) {
            return false;
        }
        int[][] cached = new int[str1.length() + 1][str2.length() + 1];
        return isInterleaveDfs(str1, str2, str3, cached, 0, 0, 0);
    }

    private boolean isInterleaveDfs(String str1, String str2, String str3, int[][] cached, int index1, int index2, int index3) {
        //1、递归终止条件一：找到目标
        if (index1 == str1.length() && index2 == str2.length() && index3 == str3.length()) { //递归的最深处，开始回溯
            return true;
        }
        //2、递归终止条件二：记忆化搜索
        if (cached[index1][index2] == 1) {
            return false;
        }

        //横向枚举搜索，任意一种情况满足即可
        if (index1 < str1.length() && str1.charAt(index1) == str3.charAt(index3)) {
            if (isInterleaveDfs(str1, str2, str3, cached, index1 + 1, index2, index3 + 1)) {
                return true;
            }
        }
        if (index2 < str2.length() && str2.charAt(index2) == str3.charAt(index3)) {
            if (isInterleaveDfs(str1, str2, str3, cached, index1, index2 + 1, index3 + 1)) {
                return true;
            }
        }

        //当前已完成全部搜索，1、记忆化
        cached[index1][index2] = 1;
        //当前已完成全部搜索，2、回溯返回上一层（关键，巧妙）
        return false;
    }





    //---------------------------------------------------------------------------------------
    // 多维度的动态规划和记忆化搜索
    //---------------------------------------------------------------------------------------

    int mod = (int) Math.pow(10, 9) + 7;

    /**
     * 576. 出界的路径数
     */
    public int findPaths(int m, int n, int maxMove, int startRow, int startColumn) {  //动态规划
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        long[][][] dp = new long[m][n][maxMove + 1];  //dp[i][j][q]状态定义：表示在拥有 q 次移动机会的情况下，从点 [i,j] 出界的路径数

        for (int k = 1; k <= maxMove; k++) {
            //------------------------------------------------------------------------
            // 状态转移的过程：
            //     移动次数 2的都是从移动次数为 1 的扩展来的，同理，移动次数 3 的都是从移动次数为 2 的扩展来的
            //     所以要注意循环的顺序
            //------------------------------------------------------------------------
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    for (int[] dir : directions) {
                        int nextRow = i + dir[0];
                        int nextCol = j + dir[1];
                        if (nextRow < 0 || nextRow >= m || nextCol < 0 || nextCol >= n) {  //1、边界，直接一步出界
                            dp[i][j][k] += 1;
                        } else {                                                           //2、内部，走一步到下一个节点，状态继承下一个节点在对应可移动次数的情况下的出界数
                            dp[i][j][k] += dp[nextRow][nextCol][k - 1];
                            dp[i][j][k] %= mod;
                        }
                        //---------------------------------------------------------------------------------------------------------------
                        // 无论边界或非边界，均消耗了 1 步，边界消耗一步至界外，非边界消耗一步至其余位点（所以要基于这些位点 K - 1 的状态进行转移）
                        //---------------------------------------------------------------------------------------------------------------
                    }
                }
            }
        }
        return (int) dp[startRow][startColumn][maxMove];
    }

    //------------------------------------------------------------------------------------
    // 上面是动态规划，下面是记忆化搜索，其中效率最高的是记忆化搜索，因为其有针对性，从指定点出发，而动态规划记录了从所有点出发的情况
    //------------------------------------------------------------------------------------

    public int findPaths01(int m, int n, int maxMove, int startRow, int startColumn) {  //记忆化搜索
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        //关键：记忆化，记录移动至位点 [m,n] 同时剩余 K 次可移动的次数时，可到达的边界数
        int[][][] cached = new int[m][n][maxMove + 1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                Arrays.fill(cached[i][j], -1);
            }
        }
        return findPathsDfs(m, n, directions, cached, startRow, startColumn, maxMove);
    }

    private int findPathsDfs(int m, int n, int[][] directions, int[][][] cached, int row, int col, int k) {
        //递归终止条件一：越界
        if (row < 0 || row == m || col < 0 || col == n) {
            return 1;
        }
        //递归终止条件二：如果没有越界，但是 K 已经消耗完了
        if (k == 0) return 0;
        //递归终止条件三：已搜索的情况
        if (cached[row][col][k] != -1) return cached[row][col][k];  //记忆化搜索

        int paths = 0;  //从当前节点可达到的边界数
        for (int[] dir : directions) {  //横向枚举遍历
            int nextRow = row + dir[0];
            int nextCol = col + dir[1];
            paths += findPathsDfs(m, n, directions, cached, nextRow, nextCol, k - 1);
            paths %= mod;
        }
        //记忆化
        cached[row][col][k] = paths;

        //递归终止条件四：所有方向搜索完毕
        return paths;
    }

    //-------------------------------------------------------------------------------------
    // 上下两个题均为三维的动态规划，同时均可通过记忆化搜索来实现，注意比较
    //    上下两个三维动态规划，其循环的顺序有所不同，其取决于状态转移：
    //       1、上面，逐个平面来处理，即最外层循环为 K，并且下一层需要基于上一层进行状态转移
    //       2、下面，最小的一次循环，依次处理纵向多个平面上的同一个位置（点），即最内层循环为 K，并且下一个点（外层循环）需要基于上一点进行状态转移
    //-------------------------------------------------------------------------------------

    /**
     * 6203. 矩阵中和能被 K 整除的路径
     */
    public int numberOfPaths(int[][] grid, int k) {  //动态规划，周赛 314
        int mod = (int) Math.pow(10, 9) + 7;
        long[][][] dp = new long[grid.length][grid[0].length][k];
        dp[0][0][grid[0][0] % k] = 1;  //dp[i][j][m]状态定义：从原点 [0,0] 到 [i,j]的路径和对 K 取余，值为 m 的路径数
        for (int i = 0; i < grid.length; i++) {          //1、平面横坐标
            for (int j = 0; j < grid[0].length; j++) {   //2、平面纵坐标
                for (int tt = 0; tt < k; tt++) {         //3、立体法向坐标，垂直于屏幕，标识多个平面
                    //---------------------------------------------
                    // 遍历的顺序，是针对每个点 [i,j]，均会计算其各种取余的情况，即遍历平面上某个点时，会钻下去将第三个法向信息遍历完成，才会进行下一个点的平面遍历
                    //---------------------------------------------
                    if (i > 0)
                        dp[i][j][tt] = (dp[i][j][tt] + dp[i - 1][j][(tt - grid[i][j] % k + k) % k]) % mod;   //关键在于：(tt - grid[i][j] % k + k) % k 处理负数取余，如 (-3 + 7) % 7 = 4
                    if (j > 0)
                        dp[i][j][tt] = (dp[i][j][tt] + dp[i][j - 1][(tt - grid[i][j] % k + k) % k]) % mod;
                }
            }
        }
        return (int) dp[grid.length - 1][grid[0].length - 1][0];
    }

    public int numberOfPaths00(int[][] grid, int k) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[][] directions = {{-1, 0}, {0, -1}};  //上、左
        int[][][] dp = new int[rows][cols][k];
        dp[0][0][grid[0][0] % k] = 1;
        for (int i = 0; i < rows; i++) {            //1、平面横坐标
            for (int j = 0; j < cols; j++) {        //2、平面纵坐标
                for (int tt = 0; tt < k; tt++) {    //3、立体法向坐标，垂直于屏幕，标识多个平面
                    for (int[] dir : directions) {  //4、两个来源方向
                        int prevRow = i + dir[0];
                        int prevCol = j + dir[1];
                        //由于遍历的顺序决定了只可能左侧和上侧越界，此处判断这两个方向，如果越界，则不从此方向进行状态转移
                        if (prevRow >= 0 && prevCol >= 0) {
                            dp[i][j][tt] += dp[prevRow][prevCol][(tt - grid[i][j] % k + k) % k];
                            dp[i][j][tt] %= mod;
                        }
                    }
                }
            }
        }
        return dp[rows - 1][cols - 1][0];
    }

    public int numberOfPaths01(int[][] grid, int k) {  //取余转换为 以上侧和左侧为基准 tt，上面以当前点为基准
        int mod = (int) Math.pow(10, 9) + 7;
        long[][][] dp = new long[grid.length][grid[0].length][k];
        dp[0][0][grid[0][0] % k] = 1;  //dp[i][j][m]状态定义：从原点 [0,0] 到 [i,j]的路径和对 K 取余，值为 m 的路径数
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                for (int tt = 0; tt < k; tt++) {
                    if (i > 0) {
                        dp[i][j][(tt + grid[i][j]) % k] += dp[i - 1][j][tt];   //dp[i][j][t] 由于点 [i,j]可以从上侧和左侧转移过来，所以此处为 +=
                        dp[i][j][(tt + grid[i][j]) % k] %= mod;
                    }
                    if (j > 0) {
                        dp[i][j][(tt + grid[i][j]) % k] += dp[i][j - 1][tt];
                        dp[i][j][(tt + grid[i][j]) % k] %= mod;
                    }
                }
            }
        }
        return (int) dp[grid.length - 1][grid[0].length - 1][0];
    }

    public int numberOfPaths02(int[][] grid, int k) {  //相比上面，当前做法扩展了横纵坐标
        int time = 1;
        int rows = grid.length;
        int cols = grid[0].length;
        int mod = (int) Math.pow(10, 9) + 7;
        long[][][] dp = new long[rows + 1][cols + 1][k];
        dp[1][1][grid[0][0] % k] = 1;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                for (int tt = 0; tt < k; tt++) {
                    dp[i + 1][j + 1][(tt + grid[i][j]) % k] += dp[i + 1][j][tt] + dp[i][j + 1][tt];  //必须有 + 号，原因是原点的状态继承
                    dp[i + 1][j + 1][(tt + grid[i][j]) % k] %= mod;
                }
            }
        }
        return (int) dp[rows][cols][0];
    }

    //-----------------------------------------------
    // 上下两种写法的差异在于：[0,0]处的状态转换，导致的状态转移方程中是否有 + 号
    //    上面写法，初始值赋给了自身，因此需要 + 号
    //    下面写法，初始值赋给了依赖的元素，因此不需要 + 号
    //-----------------------------------------------

    public int numberOfPaths03(int[][] grid, int k) {
        int rows = grid.length;
        int cols = grid[0].length;
        int mod = (int) Math.pow(10, 9) + 7;
        long[][][] dp = new long[rows + 1][cols + 1][k];
//        dp[1][1][grid[0][0] % k] = 1;  //错误
//        dp[0][1][grid[0][0] % k] = 1;  //错误
        dp[0][1][0] = 1;  //正确
//        dp[1][0][0] = 1;  //正确
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                for (int tt = 0; tt < k; tt++) {
                    dp[i + 1][j + 1][(tt + grid[i][j]) % k] = (dp[i + 1][j][tt] + dp[i][j + 1][tt]) % mod;   //关键在于状态继承，[1,1]直接从[0,1]或[1，0]进行状态转换，无需基于自身[1,1]
                }
            }
        }
        return (int) dp[rows][cols][0];
    }

    public int numberOfPaths20(int[][] grid, int k) {   //记忆化搜索
        int m = grid.length;
        int n = grid[0].length;
        int mod = (int) Math.pow(10, 9) + 7;
        int[][] direction = {{1, 0}, {0, 1}};
        int[][][] cached = new int[m][n][k];  //dp[i][j][m]状态定义，从 [0,0] 到点 [i,j] 路径和对 K取余值为 m 的路径数
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                Arrays.fill(cached[i][j], -1);
            }
        }
        return numberOfPathsDfs(grid, cached, direction, 0, 0, 0, k);

    }

    private int numberOfPathsDfs(int[][] grid, int[][][] cached, int[][] direction, int row, int col, int sum, int k) {
        //递归终止条件一：越界
        if (row == grid.length || col == grid[0].length) {
            return 0;
        }
        //递归终止条件二：记忆化搜索
        if (cached[row][col][sum] != -1) {
            return cached[row][col][sum];
        }
        //递归终止条件二：终点
        int remainder = (sum + grid[row][col]) % k;  //更新 sum，其实当前的 sum 就是 remainder
        if (row == grid.length - 1 && col == grid[0].length - 1) {
            return remainder == 0 ? 1 : 0;
        }

        //向四个方向深度搜索
        int paths = 0;
        for (int[] dir : direction) {
            int nextRow = row + dir[0];
            int nextCol = col + dir[1];
            paths += numberOfPathsDfs(grid, cached, direction, nextRow, nextCol, remainder, k);
            paths %= mod;
        }

        //记忆化
        cached[row][col][sum] = paths;

        //递归终止条件四：当前节点首次搜索完成
        return paths;
    }


    /**
     * 2267. 检查是否有合法括号字符串路径
     */
    public boolean hasValidPath(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] directions = {{0, 1}, {1, 0}};
        //剪枝一：头尾不满足条件
        if (grid[0][0] == ')' || grid[m - 1][n - 1] == '(') return false;
        //剪枝一：路径长度为奇位，一定不满足条件
        if (((m + n) & 1) == 0) return false;

        //记忆化
        int[][][] cached = new int[m][n][200];  //cached[i][j][k] 在位点 [i,j] 其状态为 k 是否可以完成搜索
        return hasValidPathDfs(grid, directions, cached, 0, 0, 0);
    }

    private boolean hasValidPathDfs(char[][] grid, int[][] directions, int[][][] cached, int currRow, int currCol, int currNum) {
        //将括号匹配转换为分值判定问题
        currNum += grid[currRow][currCol] == '(' ? 1 : -1;
        //剪枝，对以下三种情况进行剪枝，右括号超标、左括号超标、已完成搜索
        if (currNum < 0 || currNum > grid.length + grid[0].length - currRow - currCol || cached[currRow][currCol][currNum] == 1)
            return false;
        if (currRow == grid.length - 1 && currCol == grid[0].length - 1) {
            if (currNum == 0) {
                return true;
            }
        }

        //-------------------------------------
        // 深度度优先搜索
        //-------------------------------------
        for (int[] dir : directions) {   //横向枚举
            int nextRow = currRow + dir[0];
            int nextCol = currCol + dir[1];
            if (nextRow >= 0 && nextRow < grid.length && nextCol >= 0 && nextCol < grid[0].length) {
                //任意一种情况满足条件，则返回结果
                if (hasValidPathDfs(grid, directions, cached, nextRow, nextCol, currNum)) {   //纵向递归
                    return true;
                }
            }
        }
        //能执行到到这里，则说明当前位点已经完成搜索，其当前节点的当前状态没有满足情况的解
        cached[currRow][currCol][currNum] = 1;

        return false;
    }


}
