package leetcode.algorithm;


import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 最小生成树算法，前置需要了解并查集算法
 */
public class MinSpanningTree {


    /**
     * 1584. 连接所有点的最小费用
     */
    public int minCostConnectPoints(int[][] points) { //Kruskal
        //------------------------------------------------------------------------------------
        // 题中给出了一张包含 n 个节点的完全图，任意两点之间的距离均为它们的曼哈顿距离。
        // 现在我们需要在这个图中取得一个子图，恰满足子图的任意两点之间有且仅有一条简单路径，且这个子图的所有边的总权值之和尽可能小。
        // 最小生成树：
        //     能够满足任意两点之间有且仅有一条简单路径只有树，且这棵树包含 n 个节点。我们称这棵树为给定的图的生成树，其中总权值最小的生成树，我们称其为最小生成树。
        // 最小生成树有一个非常经典的解法：Kruskal。
        //     Kruskal需要对所有的边进行排序，然后从小到大，依次遍历每条边，同时判断每条边是否同源（连通性，可以使用并查集解决），如果同源，跳过；如果不同源，将两个连通分量合并，直到所有顶点属于同一个连通分量，算法结束
        // 使用并查集来判断是否会产生环：
        //     判断选取一条边是否会产生环的做法是，判断这条边连接的两个点是否属于同一个连通分量，如果属于同一个连通分量则选取这条边之后会产生环，如果不属于同一个连通分量则选取这条边之后不会产生环。
        //     选取一条边之后，需要将这条边连接的两个点合并到同一个连通分量。
        //------------------------------------------------------------------------------------
        int ans = 0;
        int n = points.length;
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);  //按照两边的权重（曼哈顿距离）升序排序
        //计算各个边与其他边的曼哈顿距离
        for (int i = 0; i < n; i++) {  //编号为 i 的边与其余边 j 的曼哈顿距离
            for (int j = i + 1; j < n; j++) {
                int distance = Math.abs(points[i][0] - points[j][0]) + Math.abs(points[i][1] - points[j][1]);
                sortedQueue.add(new int[]{i, j, distance});  //记录两个点及其二者间的曼哈顿距离
            }
        }
        connectPointsUFS ufs = new connectPointsUFS(n);
        while (ufs.count > 1 && !sortedQueue.isEmpty()) {
            //从小到大遍历各个边
            int[] edge = sortedQueue.poll();
            int node1 = edge[0];
            int node2 = edge[1];
            int distance = edge[2];
            //如果当前两个点已经从属相同的集合，则如果再次连接两点（累加权重），则会构成闭环
            if (ufs.findSet(node1) == ufs.findSet(node2)) continue;  //跳过
            //不会构成闭环，则相连两点
            ans += distance;
            ufs.union(node1, node2);
        }
        return ans;
    }

    static class connectPointsUFS {
        int[] nodes;
        int count;

        connectPointsUFS(int n) {
            this.count = n;
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
            count--;
        }

        private int getCount() {
            return count;
        }

    }




    /**
     * 778. 水位上升的泳池中游泳
     */
    public int swimInWater(int[][] grid) {  //基于最小生成树算法 Kruskal
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

    public int swimInWater00(int[][] grid) {  //并查集
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        //------------------------------------------------------
        // 本题可直接用并查集的关键在于 元素不重复、元素区间为 [0,n*n)
        //------------------------------------------------------
        int n = grid.length;
        //记录元素 [0,n*n) 的位置
        int[] index = new int[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                index[grid[i][j]] = i * n + j;   //由于元素不重复，因此以这种方式记录各个元素的位置
            }
        }
        swimUFS ufs = new swimUFS(n * n);
        for (int currHigh = 0; currHigh < index.length; currHigh++) {
            int row = index[currHigh] / n;
            int col = index[currHigh] % n;
            for (int[] dir : directions) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];
                if (nextRow >= 0 && nextRow < n && nextCol >= 0 && nextCol < n) {
                    if (grid[nextRow][nextCol] <= currHigh) {
                        ufs.union(row * n + col, nextRow * n + nextCol);
                    }
                }
            }

            //判断当前水位，首尾是否相连
            if (ufs.findSet(0) == ufs.findSet(n * n - 1)){
                return currHigh;
            }
        }
        return 0;
    }

    static class swimUFS {
        int[] nodes;

        private swimUFS(int n) {
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

    public int swimInWater01(int[][] grid) {  //广度优先搜索 + 小根堆
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

            ans = Math.max(ans, currHigh);
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
     * 1631. 最小体力消耗路径
     */
    public int minimumEffortPath(int[][] heights) {  //最小生成树算法
        int m = heights.length;
        int n = heights[0].length;
        int[][] directions = {{0, 1}, {1, 0}};
        //优先队列，存储边的信息，包括两个端点及其边的权重
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);
        //初始化优先队列，将所有边添加至队列中，并按照权重升序排序
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int currNode = i * n + j;
                for (int[] dir : directions) {
                    int nextRow = i + dir[0];
                    int nextCol = j + dir[1];
                    if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                        int nextNode = nextRow * n + nextCol;
                        int weight = Math.abs(heights[nextRow][nextCol] - heights[i][j]);
                        sortedQueue.add(new int[]{currNode, nextNode, weight});
                    }
                }
            }
        }
        //初始化并查集，每个节点单独一个集合
        minimumEffortPathUFS ufs = new minimumEffortPathUFS(m * n);
        //---------------------------------------------------------------------------------------
        // 最小生成树算法的关键：按照边的权重大小，依次将边添加到并查集中，即将边的两个端点对应的集合合并
        //---------------------------------------------------------------------------------------
        while (!sortedQueue.isEmpty()) {
            int[] currEdge = sortedQueue.poll();
            int node1 = currEdge[0];
            int node2 = currEdge[1];
            int weight = currEdge[2];
            ufs.union(node1, node2);
            if (ufs.findSet(0) == ufs.findSet(m * n - 1)) {
                return weight;
            }
        }
        return 0;
    }

    static class minimumEffortPathUFS {
        int[] nodes;

        minimumEffortPathUFS(int n) {
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


    public int minimumEffortPath01(int[][] heights) {  //单源最短路算法 Dijkstra 使用队列进行优化
        int INF = 0x3f3f3f3f;
        int m = heights.length;
        int n = heights[0].length;
        int[][] visited = new int[m][n];
        int[][] dist = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(dist[i], INF);
        }
        dist[0][0] = 0;
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        //优先队列，按照边的权重升序排序
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);
        sortedQueue.add(new int[]{0, 0, dist[0][0]});  //注意，一个边可能在队列中出现多次，但对应的最短距离不一样
        while (!sortedQueue.isEmpty()) {
            int[] currNode = sortedQueue.poll();
            int row = currNode[0];
            int col = currNode[1];
            int high = currNode[2];

            //---------------------------------------------------------------------------------------------
            // 如果同一个点的最短路被更新多次，因为先前更新时插入的元素不能被删除，也不能被修改，只能留在优先队列中，因此一条边可能在队列中出现多次，但会按照最短距离排序
            // 因此在当前边被认定为当前队列中的最短边时，会用此边及其最短距离更新其影响的边，而在队列中此边后续可能仍存在（重复），会借助 visited 剔除
            //---------------------------------------------------------------------------------------------
            if (visited[row][col] == 1) continue;

            //当前队列中的最短距离边是目标节点，因此直接返回其最短距离
            if (row == m - 1 && col == n - 1) {
                return dist[row][col];
            }

            visited[row][col] = 1;   //将当前点标识为已经找到最短距离

            for (int[] dir : directions) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    if (visited[nextRow][nextCol] == 0) {
                        int currHigh = Math.max(high, Math.abs(heights[row][col] - heights[nextRow][nextCol]));
                        if (currHigh < dist[nextRow][nextCol]) {
                            dist[nextRow][nextCol] = currHigh;
                            sortedQueue.add(new int[]{nextRow, nextCol, currHigh});
                        }
                    }
                }
            }
        }
        return dist[m - 1][n - 1];
    }



}
