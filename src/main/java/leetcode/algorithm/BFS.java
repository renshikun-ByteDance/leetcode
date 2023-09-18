package leetcode.algorithm;

import java.util.*;

/**
 * 广度优先搜索
 */
public class BFS {


    /**
     * 1091. 二进制矩阵中的最短路径
     */
    public int shortestPathBinaryMatrix(int[][] grid) {  //经典的广度优先搜索
        int n = grid.length;
        if (grid[0][0] == 1 || grid[n - 1][n - 1] == 1) return -1;
        if (n == 1) return 1;  //容易忽略
        //-----------------------------------------------------------------------
        // 由于我们寻找的是最短路径，故在搜索过程中可以记录已经搜索的点，从而实现剪枝
        // 在搜索过程中，如果发现当前点已经被搜索了，说明从原点到当前点的最短路径已经找到，因此当前点没必要再搜索，即使搜索最终结果也不会是更短路径
        //-----------------------------------------------------------------------
        int[][] visited = new int[n][n];
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.addLast(new int[]{0, 0});
        visited[0][0] = 1;
        int minPath = 1;
        while (!queue.isEmpty()) {
            int nums = queue.size();
            //--------------------------------------------------------------------------------------------------
            // 基于广度优先搜索，通过双端队列完成分层搜索，在某个层内触及目标，则循环结束，最短路径为从原点到目标点的层数
            //--------------------------------------------------------------------------------------------------
            while (nums > 0) {
                int[] address = queue.pollFirst();
                for (int[] dir : directions) {
                    int nextRow = address[0] + dir[0];
                    int nextCol = address[1] + dir[1];
                    if (nextRow >= 0 && nextRow < n && nextCol >= 0 && nextCol < n) {
                        if (visited[nextRow][nextCol] == 0 && grid[nextRow][nextCol] == 0) {
                            if (nextRow == n - 1 && nextCol == n - 1) {  //找到目标
                                return ++minPath;
                            }
                            queue.addLast(new int[]{nextRow, nextCol});
                            visited[nextRow][nextCol] = 1;
                        }
                    }
                }
                nums--;
            }
            minPath++; //此轮循环结束，即此圈和层循环结束，层数 + 1
        }
        return -1;
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


    /**
     * 994. 腐烂的橘子
     */
    public int orangesRotting(int[][] grid) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int goodNums = 0;
        int m = grid.length;
        int n = grid[0].length;
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) goodNums++;
                if (grid[i][j] == 2) queue.addLast(new int[]{i, j});
            }
        }
        if (goodNums == 0) return 0;  //容易忽略
        int needDay = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size > 0) {
                int[] poll = queue.pollFirst();
                for (int[] dir : directions) {
                    int nextRow = poll[0] + dir[0];
                    int nextCol = poll[1] + dir[1];
                    if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                        if (grid[nextRow][nextCol] == 1) {
                            goodNums--;
                            if (goodNums == 0) {
                                return ++needDay;
                            }
                            grid[nextRow][nextCol] = 2;  //腐烂
                            queue.addLast(new int[]{nextRow, nextCol});
                        }
                    }
                }
                size--;
            }
            needDay++;
        }
        return -1;
    }


    /**
     * 200. 岛屿数量
     */
    public int numIslands(char[][] grid) {
        int islandNums = 0;
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    islandNums++;
                    grid[i][j] = '0';
                    ArrayDeque<int[]> queue = new ArrayDeque<>();
                    queue.addLast(new int[]{i, j});
                    while (!queue.isEmpty()) {
                        int[] address = queue.pollFirst();
                        for (int[] dir : directions) {
                            int nextRow = address[0] + dir[0];
                            int nextCol = address[1] + dir[1];
                            if (nextRow >= 0 && nextRow < grid.length && nextCol >= 0 && nextCol < grid[0].length) {
                                if (grid[nextRow][nextCol] == '1') {
                                    grid[nextRow][nextCol] = '0';
                                    queue.addLast(new int[]{nextRow, nextCol});
                                }
                            }
                        }
                    }
                }
            }
        }
        return islandNums;
    }


    /**
     * 130. 被围绕的区域
     */
    public void solve(char[][] board) {  //基于广度优先搜索的算法
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
     * 1020. 飞地的数量
     */
    public int numEnclaves(int[][] grid) {
        int ans = 0;
        int m = grid.length;
        int n = grid[0].length;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {
                    if (grid[i][j] == 1) {
                        queue.addLast(new int[]{i, j});
                        grid[i][j] = 0;
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
                    if (grid[nextRow][nextCol] == 1) {
                        queue.addLast(new int[]{nextRow, nextCol});
                        grid[nextRow][nextCol] = 0;
                    }
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) ans++;
            }
        }
        return ans;
    }


    /**
     * 695. 岛屿的最大面积
     */
    public int maxAreaOfIsland(int[][] grid) {
        int maxArea = 0;
        int m = grid.length;
        int n = grid[0].length;
        int[][] directions = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    int area = 1;
                    grid[i][j] = 0;
                    queue.addLast(new int[]{i, j});
                    while (!queue.isEmpty()) {
                        int[] address = queue.pollFirst();
                        for (int[] dir : directions) {
                            int nextRow = address[0] + dir[0];
                            int nextCol = address[1] + dir[1];
                            if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                                if (grid[nextRow][nextCol] == 1) {
                                    area++;
                                    grid[nextRow][nextCol] = 0;
                                    queue.addLast(new int[]{nextRow, nextCol});
                                }
                            }
                        }
                    }
                    maxArea = Math.max(maxArea, area);
                }
            }
        }
        return maxArea;
    }

    /**
     * 407. 接雨水 II
     */
    public int trapRainWater(int[][] height) {
        //------------------------------------------------
        // 将最低的边界向内传播，判断和边界邻接的位置能否蓄水
        //------------------------------------------------
        int ans = 0;
        int m = height.length;
        int n = height[0].length;
        int[][] visited = new int[m][n];
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);  //小顶堆，按照高度升序排序
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};  //上下左右
        //初始化
        for (int i = 0; i < m; i++) {  //将最外围的围墙添加到堆中
            for (int j = 0; j < n; j++) {
                if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {
                    sortedQueue.add(new int[]{i, j, height[i][j]});
                    visited[i][j] = 1;
                }
            }
        }
        //向内搜索
        while (!sortedQueue.isEmpty()) {  //外围围墙向内收拢
            int[] currentPoint = sortedQueue.poll();   //每次均从墙的最低位置开始搜索
            for (int[] dir : directions) {
                int nextRow = currentPoint[0] + dir[0];
                int nextCol = currentPoint[1] + dir[1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    if (visited[nextRow][nextCol] == 0) {
                        if (height[nextRow][nextCol] < currentPoint[2]) {  //最低点相邻的位置，比最低点更低，可积雨水，更新最低点位置，高度同最低点高度
                            ans += currentPoint[2] - height[nextRow][nextCol];
                        }
                        //-----------------------------------------------------------------------------------------------------------------------
                        // 无论是否能接到雨水，均更新当前最低点的位置，因为已经搜索，向内更新墙的位置，墙的高度为当前最低点高度和相邻的正在搜索位置的最大高度
                        //------------------------------------------------------------------------------------------------------------------------
                        sortedQueue.add(new int[]{nextRow, nextCol, Math.max(height[nextRow][nextCol], currentPoint[2])});
                        visited[nextRow][nextCol] = 1;
                    }
                }
            }
        }
        return ans;
    }


    public int trapRainWater01(int[][] height) { //广度优先搜索
        //--------------------------------------------------------------------
        // 接水后的高度为：
        //    water[i][j] = max(height[i][j],min(water[i−1][j],water[i+1][j],water[i][j−1],water[i][j+1]))
        // 实际接水容量为：
        //    temp = water[i][j] - height[i][j]
        //--------------------------------------------------------------------
        int ans = 0;
        int m = height.length;
        int n = height[0].length;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int maxHeight = 0;
        //获取最大高度
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                maxHeight = Math.max(maxHeight, height[i][j]);
            }
        }
        //各个位点的初始水位线
        int[][] waterMark = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(waterMark[i], maxHeight);
        }
        //记录更新水位线的位点
        ArrayDeque<int[]> arrayQueue = new ArrayDeque<>();  //这些位点也将会影响周围位点的水位线
        //用边界来初始化需要更新水位线的位点
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {
                    if (height[i][j] < waterMark[i][j]) {  //本质将边界的水位线还原为原始高度
                        waterMark[i][j] = height[i][j];    //接水后的高度已经确定
                        arrayQueue.addLast(new int[]{i, j});
                    }
                }
            }
        }
        //已经更新水位线的位点，对四周位点的水位线施加影响
        while (!arrayQueue.isEmpty()) {            //广度优先搜索
            int[] currentPoint = arrayQueue.pollFirst();
            int row = currentPoint[0];             //每个位点会先后被相邻的四个位点更新
            int col = currentPoint[1];
            for (int[] dir : directions) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    if (waterMark[nextRow][nextCol] > waterMark[row][col]) {  //下个位点的水位线受当前位点的水位线影响，可能需要调整
                        if (waterMark[nextRow][nextCol] > height[nextRow][nextCol]) {   //还有调整空间
                            //-----------------------------------------------------
                            // 当前位点的水位线高度取决于相邻位点的水位线高度以及当前位点的高度，两个高度决定了能否积累雨水，如果可以积累雨水，决定了积累的量
                            //-----------------------------------------------------
                            waterMark[nextRow][nextCol] = Math.max(height[nextRow][nextCol], waterMark[row][col]);
                            arrayQueue.addLast(new int[]{nextRow, nextCol});
                            //-----------------------------------------------------------------
                            // 一个位点接雨水后的高度，不仅仅取决于相邻的四个位点，而可能间接的取决于相邻位点的相邻位点，所以本题中，只要更新了当前位点的高度值，就要添加到队列中，从而对其相邻的位点进行影响
                            // 每次添加到队列中的位点，其节水后的高度并非最终确认值，需要多轮迭代，直至将队列中元素处理完毕，才是最终状态
                            // 我们不断重复的进行调整，直到所有的方块的接水高度不再有调整时即为满足要求
                            //-----------------------------------------------------------------
                        }
                    }
                }
            }
        }
        //累加各个位点积累的雨水
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                ans += waterMark[i][j] - height[i][j];
            }
        }
        return ans;
    }


    /**
     * 787. K 站中转内最便宜的航班
     */
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {  //基于广度优先搜索来解决最短路问题
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

    public int findCheapestPrice01(int n, int[][] flights, int src, int dst, int k) {   //运用 Bellman Ford 求解有限制的最短路问题
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


    public int findCheapestPrice02(int n, int[][] flights, int src, int dst, int k) {  //动态规划
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



    /**
     * 778. 水位上升的泳池中游泳
     */
    public int swimInWater(int[][] grid) {  //广度优先搜索 + 小根堆
        //------------------------------------------------------------------------------------------------------------------------------------------------------
        // 特殊的广度优先搜索的题目，使用优先队列（小根堆）来实现按照类似海平面上升的方式进行搜索，而非普通的基于双端队列的广度优先搜索采用一圈一圈的搜索模式
        //------------------------------------------------------------------------------------------------------------------------------------------------------
        int m = grid.length;
        int n = grid[0].length;
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        //优先队列，按照元素值升序排序
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[0] - o2[0]);
        sortedQueue.add(new int[]{grid[0][0], 0, 0});
        grid[0][0] = -1;
        int ans = 0;  //整个贪心搜索过程中遇到的最高台阶
        //------------------------------------------------------------------------
        // 搜索过程非常类似于海平面上升的过程
        // 特别是在朝一个方向搜索时，周围全是很高的台阶，下次搜索可能会跳跃很远（与当前点不相邻）从另外一个点进行搜索
        // 而这个点是在队列中存储的可搜索的位点中高度最小的位点，其类似于水位上升的模式
        //------------------------------------------------------------------------
        while (!sortedQueue.isEmpty()) {
            int[] currNode = sortedQueue.poll();
            int currHigh = currNode[0];
            int row = currNode[1];
            int col = currNode[2];

            ans = Math.max(ans, currHigh);   //关键：目标点可能在一个坑中，可以在脑海中想想水位在水槽中上升的状态
            if (row == m - 1 && col == n - 1) {
                return ans;
            }

            for (int[] dir : directions) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    if (grid[nextRow][nextCol] != -1) {  //未搜索
                        sortedQueue.add(new int[]{grid[nextRow][nextCol], nextRow, nextCol});
                        grid[nextRow][nextCol] = -1;     //标识为已搜索
                    }
                }
            }
        }
        return ans;
    }

    public int swimInWater01(int[][] grid) {  //基于最小生成树算法 Kruskal
        int m = grid.length;
        int n = grid[0].length;
        int[][] directions = {{0, 1}, {1, 0}};   //本题需要构建无向图，因此构建边只需向下和向右搜索
        swimInWaterUFS ufs = new swimInWaterUFS(m * n);
        //优先队列存储三元组，每个三元组记录一条边的两个端点及其权重
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);
        //初始化优先队列
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int currNode = i * n + j;  //转换为一维索引，标识其节点编号
                for (int[] dir : directions) {
                    int nextRow = i + dir[0];
                    int nextCol = j + dir[1];
                    if (nextRow < m && nextCol < n) {
                        int nextNode = nextRow * n + nextCol;  //转换为一维索引，标识其节点编号
                        int weight = Math.max(grid[i][j], grid[nextRow][nextCol]);  //相邻两点构成边，边的权重为两点元素的最大值
                        sortedQueue.add(new int[]{currNode, nextNode, weight});
                    }
                }
            }
        }
        //-----------------------------------------------------
        // 最小生成树的关键：从小到大遍历各个边
        //-----------------------------------------------------
        while (!sortedQueue.isEmpty()) {
            int[] currEdge = sortedQueue.poll();
            int node1 = currEdge[0];
            int node2 = currEdge[1];
            int weight = currEdge[2];
            ufs.union(node1, node2);
            //-------------------------------------------------------------------------------------------------------------
            // 判断当前左上角和右下角两点是否连通，如果连通则说明当前添加的边构成的连通块，由于边按照权重升序排序，故结果为 此边的权重
            //-------------------------------------------------------------------------------------------------------------
            if (ufs.findSet(0) == ufs.findSet(m * n - 1)) {
                return weight;
            }
        }

        return 0;   //必须为 0 ，为了处理 [[0]] 的情况，案例不会出现 [[1]]、[[2]]、[[3]] 的情况
    }

    static class swimInWaterUFS {
        int[] nodes;

        swimInWaterUFS(int n) {
            this.nodes = new int[n];
            for (int i = 0; i < n; i++) {
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

    public int swimInWater02(int[][] grid) {  //单源最短路算法 Dijkstra
        int m = grid.length;
        int n = grid[0].length;
        int INF = 0x3f3f3f3f;
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        int[] dist = new int[m * n];
        int[] visited = new int[m * n];
        Arrays.fill(dist, INF);
        dist[0] = grid[0][0];
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);
        sortedQueue.add(new int[]{0, 0, grid[0][0]});
        while (!sortedQueue.isEmpty()) {
            int[] currNode = sortedQueue.poll();
            int row = currNode[0];
            int col = currNode[1];
            int high = currNode[2];
            int currIdx = row * n + col;

            visited[row * n + col] = 1;

            for (int[] dir : directions) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    int nextIdx = nextRow * n + nextCol;
                    if (visited[nextIdx] == 0) {
                        int currHigh = Math.max(dist[currIdx], grid[nextRow][nextCol]);
                        if (currHigh < dist[nextIdx]) {
                            dist[nextIdx] = currHigh;
                            if (nextRow == m - 1 && nextCol == n - 1) {
                                return dist[nextIdx];
                            }
                            sortedQueue.add(new int[]{nextRow, nextCol, grid[nextRow][nextCol]});
                        }
                    }
                }
            }
        }
        return 0;
    }




    /**
     * 821. 字符的最短距离
     */
    public int[] shortestToChar(String str, char ch) {   //基于广度优先搜索
        int n = str.length();
        int[] ans = new int[n];
        Arrays.fill(ans, -1);
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        //初始化队列
        for (int i = 0; i < n; i++) {   //首轮搜索均以 ch 位置为起点
            if (str.charAt(i) == ch) {
                queue.addLast(i);
                ans[i] = 0;
            }
        }
        int[] dirs = {-1, 1};   //左右两个方向
        while (!queue.isEmpty()) {
            Integer curr = queue.pollFirst();
            for (int dir : dirs) {
                int next = curr + dir;
                if (next >= 0 && next < n) {
                    //当前节点首次达到，即此距离就是此节点的最短距离，再次到达则一定不是最短距离，直接跳过
                    if (ans[next] == -1) {
                        ans[next] = ans[curr] + 1;
                        queue.addLast(next);
                    }
                }
            }
        }
        return ans;
    }

    public int[] shortestToChar02(String str, char ch) {
        int n = str.length();
        int[] ans = new int[n];
        TreeSet<Integer> dist = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            if (str.charAt(i) == ch) {
                dist.add(i);
            }
        }
        for (int i = 0; i < n; i++) {
            if (str.charAt(i) == ch) continue;
            Integer prev = dist.floor(i);    //前一个位置
            Integer next = dist.ceiling(i);  //后一个位置
            if (prev != null && next != null) {
                ans[i] = Math.min(i - prev, next - i);
            } else if (prev != null) {
                ans[i] = i - prev;
            } else if (next != null) {
                ans[i] = next - i;
            }
        }
        return ans;
    }

    public int[] shortestToChar01(String str, char ch) {   //基于两次遍历，分别获取两侧相邻 ch 的位置
        int n = str.length();
        int[] ans = new int[n];
        Arrays.fill(ans, n + 1);
        //从左向右遍历
        int prev = -1;
        for (int i = 0; i < n; i++) {
            if (str.charAt(i) == ch) prev = i;
            if (prev != -1) ans[i] = i - prev;
        }
        //从右向左遍历
        int next = n + 1;
        for (int i = n - 1; i >= 0; i--) {
            if (str.charAt(i) == ch) next = i;
            if (next != n + 1) ans[i] = Math.min(ans[i], next - i);
        }
        return ans;
    }


    /**
     * 1293. 网格中的最短路径
     */
    public int shortestPath(int[][] grid, int k) {   //广度优先搜索 + 双端队列
        //---------------------------------------------------------------------------------------
        // 本题不能用动态规划，因为每次需要走四个方向，如果只考虑两个方向不可行，我们可以往上走的原因是我们只能破解有限的砖块，往其它地方走可以避免破解砖块!
        //---------------------------------------------------------------------------------------
        int m = grid.length;
        int n = grid[0].length;
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        //如果可消除的障碍物的个数大于最短路径，则直接返回结果
        if (k > m + n - 2) {  //剪枝
            return m + n - 2;
        }
        int[][][] visited = new int[m][n][k + 1];  //记录当前状态是否遇到过，用于提前剪枝，避免重复遍历一个状态
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        visited[0][0][k] = 1;
        queue.addLast(new int[]{0, 0, k});
        int ans = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size && !queue.isEmpty(); i++) {
                int[] currNode = queue.pollFirst();
                int row = currNode[0];
                int col = currNode[1];
                int restK = currNode[2];
                for (int[] dir : directions) {
                    int nextRow = row + dir[0];
                    int nextCol = col + dir[1];
                    //剪枝一：越界
                    if (nextRow < 0 || nextRow >= m || nextCol < 0 || nextCol >= n) continue;

                    if (grid[nextRow][nextCol] == 0 && visited[nextRow][nextCol][restK] == 0) {
                        if (nextRow == m - 1 && nextCol == n - 1) {
                            return ++ans;
                        } else {
                            visited[nextRow][nextCol][restK] = 1;
                            queue.addLast(new int[]{nextRow, nextCol, restK});
                        }
                    } else if (grid[nextRow][nextCol] == 1 && restK >= 1 && visited[nextRow][nextCol][restK - 1] == 0) {
                        visited[nextRow][nextCol][restK - 1] = 1;
                        queue.addLast(new int[]{nextRow, nextCol, restK - 1});
                    }
                }
            }
            ans++;
        }
        return -1;
    }


    public int shortestPath01(int[][] grid, int k) {   //基于 Dijkstra 算法 + 优先队列
        //-----------------------------------------------------
        // 同样的逻辑，使用 C++ 写不超时，Java 写则超时，C++已提交，见提交记录
        //-----------------------------------------------------
        int m = grid.length;
        int n = grid[0].length;
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        //如果可消除的障碍物的个数大于最短路径，则直接返回结果
        if (k > m + n - 2) {  //剪枝
            return m + n - 2;
        }
        int[][] cached = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(cached[i], k + 1);
        }
        //优先队列
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);  //按照步数升序排序
        sortedQueue.add(new int[]{0, 0, 0, 0});  //横坐标、纵坐标、步数、替换次数
        while (!sortedQueue.isEmpty()) {
            int[] tuple = sortedQueue.poll();
            int row = tuple[0];
            int col = tuple[1];
            int currStep = tuple[2];  //步数
            int currNums = tuple[3];  //替换次数
            if (row == m - 1 && col == n - 1) {
                return currStep;
            }

            for (int[] dir : directions) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    int nextNums = currNums + grid[nextRow][nextCol];
                    if (nextNums <= k && cached[nextRow][nextCol] > nextNums) {  //关键
                        sortedQueue.add(new int[]{nextRow, nextCol, currStep + 1, nextNums});
                    }
                }
            }
        }
        return -1;
    }



    /**
     * 1210. 穿过迷宫的最少移动次数
     */
    public int minimumMoves(int[][] grid) {   //最短路-广度优先搜索 BFS
        int n = grid.length;
        int[][][] dist = new int[n][n][2];   //关注蛇的头部位点
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Arrays.fill(dist[i][j], -1);
            }
        }
        dist[0][1][0] = 0;   //初始状态
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.addLast(new int[]{0, 1, 0});
        while (!queue.isEmpty()) {    //不分层
            int[] tuple = queue.pollFirst();
            int row = tuple[0];
            int col = tuple[1];
            int status = tuple[2];
            if (status == 0) {    //当前状态为水平
                //1.1 水平向右移动
                if (col + 1 < n && dist[row][col + 1][status] == -1 && grid[row][col + 1] == 0) {
                    dist[row][col + 1][status] = dist[row][col][status] + 1;
                    queue.addLast(new int[]{row, col + 1, status});
                }
                //1.2 竖直向下移动（两个点都移动，都判断）
                if (row + 1 < n && dist[row + 1][col][status] == -1 && grid[row + 1][col] == 0 && grid[row + 1][col - 1] == 0) {
                    dist[row + 1][col][status] = dist[row][col][status] + 1;
                    queue.addLast(new int[]{row + 1, col, status});
                }
                //1.3 顺时针旋转90度（两个点都判断，包含途径点）
                if (row + 1 < n && dist[row + 1][col - 1][1 - status] == -1 && grid[row + 1][col - 1] == 0 && grid[row + 1][col] == 0) {
                    dist[row + 1][col - 1][1 - status] = dist[row][col][status] + 1;
                    queue.addLast(new int[]{row + 1, col - 1, 1 - status});   //姿态变了
                }
            }
            if (status == 1) {  //当前状态为竖直
                //2.1 水平向右移动（两个点都移动）
                if (col + 1 < n && dist[row][col + 1][status] == -1 && grid[row][col + 1] == 0 && grid[row - 1][col + 1] == 0) {
                    dist[row][col + 1][status] = dist[row][col][status] + 1;
                    queue.addLast(new int[]{row, col + 1, status});
                }
                //2.2 竖直向下移动
                if (row + 1 < n && dist[row + 1][col][status] == -1 && grid[row + 1][col] == 0) {
                    dist[row + 1][col][status] = dist[row][col][status] + 1;
                    queue.addLast(new int[]{row + 1, col, status});
                }
                //2.3 逆时针旋转90度（两个点都判断，包含途径点）
                if (col + 1 < n && dist[row - 1][col + 1][1 - status] == -1 && grid[row - 1][col + 1] == 0 && grid[row][col + 1] == 0) {
                    dist[row - 1][col + 1][1 - status] = dist[row][col][status] + 1;
                    queue.addLast(new int[]{row - 1, col + 1, 1 - status});
                }
            }
        }
        return dist[n - 1][n - 1][0];
    }

    public int minimumMoves01(int[][] grid) {   //最短路-广度优先搜索 BFS
        int n = grid.length;
        //尾部移动的三种状态：向下侧移动、向右侧移动、顺逆旋转
        int[][] dirs = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        int[][][] visited = new int[n][n][2];
        visited[0][0][0] = 1;
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.addLast(new int[]{0, 0, 0});
        int step = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size && !queue.isEmpty(); i++) {
                int[] tuple = queue.pollFirst();
                int row = tuple[0];
                int col = tuple[1];
                int status = tuple[2];
                for (int[] dir : dirs) {
                    int nextRow = row + dir[0];
                    int nextCol = col + dir[1];
                    int nextStatus = status ^ dir[2];
                    int nextHeadRow = nextRow + nextStatus;
                    int nextHeadCol = nextCol + (nextStatus ^ 1);
                    if (nextHeadRow < n && nextHeadCol < n && visited[nextRow][nextCol][nextStatus] == 0
                            && grid[nextRow][nextCol] == 0 && grid[nextHeadRow][nextHeadCol] == 0
                            && (dir[2] == 0 || grid[nextRow + 1][nextCol + 1] == 0)) {
                        visited[nextRow][nextCol][nextStatus] = 1;
                        queue.addLast(new int[]{nextRow, nextCol, nextStatus});
                        if (nextRow == n - 1 && nextCol == n - 2) {
                            return ++step;
                        }
                    }
                }
            }
            step++;
        }
        return -1;
    }



    /**
     * 2257. 统计网格图中没有被保卫的格子数
     */
    public int countUnguarded(int m, int n, int[][] guards, int[][] walls) {
        int ans = 0;
        int[][] grid = new int[m][n];
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        for (int[] guard : guards) {
            grid[guard[0]][guard[1]] = 1;
            for (int i = 0; i < 4; i++) {
                queue.addLast(new int[]{guard[0], guard[1], i});   //警卫点向四个方向走
            }
        }
        for (int[] wall : walls) {
            grid[wall[0]][wall[1]] = 2;
        }
        while (!queue.isEmpty()) {
            int[] tuple = queue.pollLast();
            int row = tuple[0];
            int col = tuple[1];
            int dir = tuple[2];
            int nextRow = row + dirs[dir][0];
            int nextCol = col + dirs[dir][1];
            if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                if (grid[nextRow][nextCol] == 0 || grid[nextRow][nextCol] == -1) {
                    grid[nextRow][nextCol] = -1;
                    queue.addLast(new int[]{nextRow, nextCol, dir});
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0) ans++;
            }
        }
        return ans;
    }


    /**
     * 542. 01 矩阵
     */
    public int[][] updateMatrix(int[][] mat) {   //BFS解决多源最短路问题
        int m = mat.length;
        int n = mat[0].length;
        int[][] ans = new int[m][n];
        int[][] visited = new int[m][n];
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 0) {
                    queue.addLast(new int[]{i, j});
                    visited[i][j] = 1;
                }
            }
        }
        int dist = 1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size && !queue.isEmpty(); i++) {
                int[] tuple = queue.pollFirst();
                int row = tuple[0];
                int col = tuple[1];
                for (int[] dir : dirs) {
                    int nextRow = row + dir[0];
                    int nextCol = col + dir[1];
                    if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                        if (visited[nextRow][nextCol] == 0) {
                            visited[nextRow][nextCol] = 1;
                            ans[nextRow][nextCol] = dist;
                            queue.addLast(new int[]{nextRow, nextCol});
                        }
                    }
                }
            }
            dist++;
        }
        return ans;
    }

    public int[][] updateMatrix00(int[][] mat) {   //BFS解决多源最短路问题
        int m = mat.length;
        int n = mat[0].length;
        int[][] dist = new int[m][n];
        int[][] visited = new int[m][n];
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 0) {
                    queue.addLast(new int[]{i, j});
                    visited[i][j] = 1;
                }
            }
        }
        while (!queue.isEmpty()) {
            int[] tuple = queue.pollFirst();
            int row = tuple[0];
            int col = tuple[1];
            for (int[] dir : dirs) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    if (visited[nextRow][nextCol] == 0) {
                        visited[nextRow][nextCol] = 1;
                        dist[nextRow][nextCol] = dist[row][col] + 1;
                        queue.addLast(new int[]{nextRow, nextCol});
                    }
                }
            }
        }
        return dist;
    }


    public int[][] updateMatrix01(int[][] mat) {   //动态规划
        int m = mat.length;
        int n = mat[0].length;
        int INF = 0x3f3f3f3f;
        int[][] ans = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(ans[i], INF);
        }
        //1、左上到右下
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 0) {
                    ans[i][j] = 0;
                    continue;
                }
                if (i > 0) ans[i][j] = Math.min(ans[i][j], ans[i - 1][j] + 1);
                if (j > 0) ans[i][j] = Math.min(ans[i][j], ans[i][j - 1] + 1);
            }
        }
        //2、右下到左上
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (mat[i][j] == 0) {
                    ans[i][j] = 0;
                    continue;
                }
                if (i < m - 1) ans[i][j] = Math.min(ans[i][j], ans[i + 1][j] + 1);
                if (j < n - 1) ans[i][j] = Math.min(ans[i][j], ans[i][j + 1] + 1);
            }
        }
        return ans;
    }


    /**
     * 1162. 地图分析
     */
    public int maxDistance(int[][] grid) {
        int max = 0;
        int m = grid.length;
        int n = grid[0].length;
        int[][] dist = new int[m][n];
        int[][] visited = new int[m][n];
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {   //多源最短路，源头从 1 出发
                    visited[i][j] = 1;
                    queue.addLast(new int[]{i, j});
                }
            }
        }
        int distance = 1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size && !queue.isEmpty(); i++) {
                int[] tuple = queue.poll();
                int row = tuple[0];
                int col = tuple[1];
                for (int[] dir : dirs) {
                    int nextRow = row + dir[0];
                    int nextCol = col + dir[1];
                    if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                        if (visited[nextRow][nextCol] == 0) {
                            visited[nextRow][nextCol] = 1;
                            dist[nextRow][nextCol] = distance;
                            queue.addLast(new int[]{nextRow, nextCol});
                        }
                    }
                }
            }
            distance++;
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                max = Math.max(max, dist[i][j]);
            }
        }
        return max == 0 ? -1 : max;
    }

    public int maxDistance01(int[][] grid) {
        int max = 0;
        int m = grid.length;
        int n = grid[0].length;
        int[][] dist = new int[m][n];
        int[][] visited = new int[m][n];
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {   //多源最短路，源头从 1 出发
                    visited[i][j] = 1;
                    queue.addLast(new int[]{i, j});
                }
            }
        }
        while (!queue.isEmpty()) {
            int[] tuple = queue.poll();
            int row = tuple[0];
            int col = tuple[1];
            for (int[] dir : dirs) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    if (visited[nextRow][nextCol] == 0) {
                        visited[nextRow][nextCol] = 1;
                        dist[nextRow][nextCol] = dist[row][col] + 1;   //核心：从上一个点到当前点需要 1 个单位的距离
                        max = Math.max(max, dist[nextRow][nextCol]);
                        queue.addLast(new int[]{nextRow, nextCol});
                    }
                }
            }
        }
        return max == 0 ? -1 : max;
    }

    public int maxDistance02(int[][] grid) {   //动态规划
        int max = 0;
        int m = grid.length;
        int n = grid[0].length;
        int INF = 0x3f3f3f3f;
        int[][] dist = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(dist[i], INF);
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    dist[i][j] = 0;
                    continue;
                }
                if (i > 0) dist[i][j] = Math.min(dist[i][j], dist[i - 1][j] + 1);
                if (j > 0) dist[i][j] = Math.min(dist[i][j], dist[i][j - 1] + 1);
            }
        }
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (grid[i][j] == 1) {
                    dist[i][j] = 0;
                    continue;
                }
                if (i < m - 1) dist[i][j] = Math.min(dist[i][j], dist[i + 1][j] + 1);
                if (j < n - 1) dist[i][j] = Math.min(dist[i][j], dist[i][j + 1] + 1);
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                max = Math.max(max, dist[i][j]);
            }
        }
        return max == 0 || max >= INF ? -1 : max;
    }


    /**
     * 1765. 地图中的最高点
     */
    public int[][] highestPeak(int[][] grids) {
        int m = grids.length;
        int n = grids[0].length;
        int[][] dist = new int[m][n];
        int[][] visited = new int[m][n];
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grids[i][j] == 1) {
                    visited[i][j] = 1;
                    queue.addLast(new int[]{i, j});
                }
            }
        }
        while (!queue.isEmpty()) {
            int[] tuple = queue.poll();
            int row = tuple[0];
            int col = tuple[1];
            for (int[] dir : dirs) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    if (visited[nextRow][nextCol] == 0) {
                        visited[nextRow][nextCol] = 1;
                        dist[nextRow][nextCol] = dist[row][col] + 1;
                        queue.addLast(new int[]{nextRow, nextCol});
                    }
                }
            }
        }
        return dist;
    }

    public int[][] highestPeak01(int[][] grids) {
        int m = grids.length;
        int n = grids[0].length;
        int INF = 0x3f3f3f3f;
        int[][] dist = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(dist[i], INF);
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grids[i][j] == 1) {
                    dist[i][j] = 0;
                    continue;
                }
                if (i > 0) dist[i][j] = Math.min(dist[i][j], dist[i - 1][j] + 1);
                if (j > 0) dist[i][j] = Math.min(dist[i][j], dist[i][j - 1] + 1);
            }
        }
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (grids[i][j] == 1) {
                    dist[i][j] = 0;
                    continue;
                }
                if (i < m - 1) dist[i][j] = Math.min(dist[i][j], dist[i + 1][j] + 1);
                if (j < n - 1) dist[i][j] = Math.min(dist[i][j], dist[i][j + 1] + 1);
            }
        }
        return dist;
    }



    /**
     * 1926. 迷宫中离入口最近的出口
     */
    public int nearestExit(char[][] maze, int[] entrance) {   //从出口出发，搜索原始出发点
        int m = maze.length;
        int n = maze[0].length;
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {
                    if (maze[i][j] == '.') {
                        if (i != entrance[0] || j != entrance[1]) {
                            maze[i][j] = '+';
                            queue.addLast(new int[]{i, j});
                        }
                    }
                }
            }
        }
        int level = 1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size && !queue.isEmpty(); i++) {
                int[] tuple = queue.poll();
                int row = tuple[0];
                int col = tuple[1];
                for (int[] dir : dirs) {
                    int nextRow = row + dir[0];
                    int nextCol = col + dir[1];
                    if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                        //1、剪枝：重复搜索 或 墙壁
                        if (maze[nextRow][nextCol] == '+') continue;
                        if (nextRow == entrance[0] && nextCol == entrance[1]) {
                            return level;
                        }
                        maze[nextRow][nextCol] = '+';
                        queue.addLast(new int[]{nextRow, nextCol});
                    }
                }
            }
            level++;
        }
        return -1;
    }

    public int nearestExit01(char[][] maze, int[] entrance) {   //从出发点出发，搜索出口
        int m = maze.length;
        int n = maze[0].length;
        int[][] visited = new int[m][n];
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.addLast(new int[]{entrance[0], entrance[1]});
        visited[entrance[0]][entrance[1]] = 1;
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int level = 1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size && !queue.isEmpty(); i++) {
                int[] tuple = queue.poll();
                int row = tuple[0];
                int col = tuple[1];
                for (int[] dir : dirs) {
                    int nextRow = row + dir[0];
                    int nextCol = col + dir[1];
                    if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                        //1、剪枝：此路不通、重复搜索
                        if (maze[nextRow][nextCol] == '+' || visited[nextRow][nextCol] == 1) continue;
                        //2、校验出口
                        if (nextRow == 0 || nextRow == m - 1 || nextCol == 0 || nextCol == n - 1) {
                            return level;
                        }
                        queue.addLast(new int[]{nextRow, nextCol});
                        visited[nextRow][nextCol] = 1;
                    }
                }
            }
            level++;
        }
        return -1;
    }



    /**
     * 841. 钥匙和房间
     */
    public boolean canVisitAllRooms(List<List<Integer>> rooms) {
        int n = rooms.size();
        int[] visited = new int[n];
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.addLast(0);
        while (!queue.isEmpty()) {
            Integer currNode = queue.pollFirst();
            if (visited[currNode] == 1) continue;
            visited[currNode] = 1;
            for (int nextNode : rooms.get(currNode)) {
                queue.addLast(nextNode);
            }
        }
        for (int i = 0; i < n; i++) {
            if (visited[i] == 0) return false;
        }
        return true;
    }



    /**
     * 1263. 推箱子
     */
    public int minPushBox(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] dist = new int[m * n][m * n];
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int sx = -1;
        int sy = -1;
        int bx = -1;
        int by = -1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 'S') {
                    sx = i;
                    sy = j;
                }
                if (grid[i][j] == 'B') {
                    bx = i;
                    by = j;
                }
            }
        }
        for (int i = 0; i < m * n; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }
        dist[sx * n + sy][bx * n + by] = 0;
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{sx * n + sy, bx * n + by});
        while (!queue.isEmpty()) {
            ArrayDeque<int[]> queueNext = new ArrayDeque<>();
            while (!queue.isEmpty()) {
                int[] pos = queue.pollFirst();
                //人员的位置
                int s1 = pos[0];
                int sx1 = s1 / n;
                int sy1 = s1 % n;
                //箱子的位置
                int b1 = pos[1];
                int bx1 = b1 / n;
                int by1 = b1 % n;
                if (grid[bx1][by1] == 'T') { // 箱子已被推到目标处
                    return dist[s1][b1];
                }
                for (int[] dir : dirs) {
                    int sx2 = sx1 + dir[0];
                    int sy2 = sy1 + dir[1];
                    int s2 = sx2 * n + sy2;
                    //----------------------
                    // 人员的移动是主动的
                    //----------------------
                    //剪枝一：越界、障碍物
                    if (sx2 < 0 || sx2 >= m || sy2 < 0 || sy2 >= n || grid[sx2][sy2] == '#') {
                        continue;
                    }
                    //----------------------------------------------------------
                    // 运行至此，说明此方向人员可以移动，下面判断人员移动是否会使得箱子得到有效的移动
                    // 箱子被动移动，关键在于人员的移动是否会导致箱子的有效移动
                    //----------------------------------------------------------
                    //此方向，在移动前，人与箱子相邻
                    if (sx2 == bx1 && sy2 == by1) {
                        //计算在箱子被动移动后的位置
                        int bx2 = bx1 + dir[0];
                        int by2 = by1 + dir[1];
                        int b2 = bx2 * n + by2;
                        //剪枝一：越界、障碍物
                        if (bx2 < 0 || bx2 >= m || by2 < 0 || by2 >= n || grid[bx2][by2] == '#') {  //箱子移动不合理，故推不动，所以人员的移动也无效
                            continue;
                        }
                        //剪枝二：已遍历
                        if (dist[s2][b2] <= dist[s1][b1] + 1) {
                            continue;
                        }
                        dist[s2][b2] = dist[s1][b1] + 1;
                        //最终目标
                        if (grid[bx2][by2] == 'T') {
                            return dist[s2][b2];
                        }
                        queueNext.addLast(new int[]{s2, b2});
                    } else {  //此方向，在移动前，人与箱子不相邻
                        if (dist[s2][b1] <= dist[s1][b1]) {   //新位置，之前已经遍历过，已经有最短距离，无需在添加至队列
                            continue;
                        }
                        dist[s2][b1] = dist[s1][b1];          //箱子没有移动，所以无需 + 1
                        queue.addLast(new int[]{s2, b1});     //关键中的关键：向当前队列中插入，而非向下一个队列中插入
                    }
                }
            }
            queue = queueNext;
        }
        return -1;
    }


    public int minPushBox01(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int sx = -1;
        int sy = -1;
        int bx = -1;
        int by = -1;
        int tx = -1;
        int ty = -1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 'S') {
                    sx = i;
                    sy = j;
                }
                if (grid[i][j] == 'B') {
                    bx = i;
                    by = j;
                }
                if (grid[i][j] == 'T') {
                    tx = i;
                    ty = j;
                }
            }
        }
        int[][] dist = new int[m * n][m * n];
        for (int i = 0; i < m * n; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }
        dist[sx * n + sy][bx * n + by] = 0;
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        queue.addLast(new int[]{sx * n + sy, bx * n + by});
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        while (!queue.isEmpty()) {
            ArrayDeque<int[]> nextQueue = new ArrayDeque<>();
            while (!queue.isEmpty()) {
                int[] tuple = queue.pollFirst();
                int s1 = tuple[0];
                int b1 = tuple[1];
                int sx1 = s1 / n;
                int sy1 = s1 % n;
                int bx1 = b1 / n;
                int by1 = b1 % n;
                for (int[] dir : dirs) {
                    int sx2 = sx1 + dir[0];
                    int sy2 = sy1 + dir[1];
                    int s2 = sx2 * n + sy2;
                    //首先，要校验人员移动后的位置是否合法
                    if (sx2 < 0 || sx2 >= m || sy2 < 0 || sy2 >= n || grid[sx2][sy2] == '#') {
                        continue;
                    }
                    //人员合理的移动后，会与箱子的位置重叠，即会尝试同向推动箱子
                    if (sx2 == bx1 && sy2 == by1) {
                        int bx2 = bx1 + dir[0];
                        int by2 = by1 + dir[1];
                        int b2 = bx2 * n + by2;
                        if (bx2 < 0 || bx2 >= m || by2 < 0 || by2 >= n || grid[bx2][by2] == '#') {
                            continue;
                        }
                        if (dist[s2][b2] < Integer.MAX_VALUE) {  //首次遍历到即为最小值了，再次遍历，一定不是最小值，因此无需下面的判单
                            continue;
                        }
//                        if (dist[s2][b2] <= dist[s1][b1] + 1) {
//                            continue;
//                        }
                        dist[s2][b2] = dist[s1][b1] + 1;
                        nextQueue.addLast(new int[]{s2, b2});
                        if (bx2 == tx && by2 == ty) {
                            return dist[s2][b2];
                        }
                    } else {
                        //其次，校验人员移动后的位置是否已经搜索过
                        if (dist[s2][b1] < Integer.MAX_VALUE) {  //同上
                            continue;
                        }
//                        if (dist[s2][b1] <= dist[s1][b1]) {
//                            continue;
//                        }
                        dist[s2][b1] = dist[s1][b1];
                        queue.addLast(new int[]{s2, b1});
                    }
                }
            }
            queue = nextQueue;
        }
        return -1;
    }



    /**
     * 6433. 矩阵中移动的最大次数
     */
    public int maxMoves(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] dist = new int[m][n];
        int[][] dirs = {{-1, 1}, {0, 1}, {1, 1}};
        int maxTimes = 0;
        for (int i = 0; i < m; i++) {
            ArrayDeque<int[]> queue = new ArrayDeque<>();
            queue.addLast(new int[]{i, 0});
            int currTimes = 0;
            while (!queue.isEmpty()) {
                int kk = queue.size();
                for (int j = 0; j < kk && !queue.isEmpty(); j++) {
                    int[] tuple = queue.pollFirst();
                    int row = tuple[0];
                    int col = tuple[1];
                    for (int[] dir : dirs) {
                        int nextRow = row + dir[0];
                        int nextCol = col + dir[1];
                        if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                            if (grid[nextRow][nextCol] > grid[row][col]) {
                                if (dist[nextRow][nextCol] < currTimes + 1) {   //之前的情况小于当前情况，剔除无效的搜索
                                    dist[nextRow][nextCol] = currTimes + 1;  //走到这里的步数
                                    queue.addLast(new int[]{nextRow, nextCol});
                                }
                            }
                        }
                    }
                }
                if (queue.size() > 0) currTimes++;
            }
            maxTimes = Math.max(maxTimes, currTimes);
        }
        return maxTimes;
    }


}
