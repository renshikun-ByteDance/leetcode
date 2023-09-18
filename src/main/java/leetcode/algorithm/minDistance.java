package leetcode.algorithm;

import org.omg.PortableInterceptor.INACTIVE;

import java.util.*;

/**
 * 最短距离
 */
public class minDistance {

    /**
     * 743. 网络延迟时间
     */
    public int networkDelayTime(int[][] times, int n, int k) {  //基于 Dijkstra 算法，暴力
        int INF = Integer.MAX_VALUE / 2;
        //邻接矩阵，存储边的信息
        int[][] grid = new int[n][n];  //这是一个带权矩阵，表示任意 i 结点到 j 结点间边的距离，若两者间无边则初始为无穷大
        //邻接矩阵，初始化
        for (int i = 0; i < n; i++) {
            Arrays.fill(grid[i], INF);  //初始化各点间无边
        }
        for (int[] time : times) {
            int node1 = time[0] - 1;  //边界序号从 0 开始
            int node2 = time[1] - 1;  //边界序号从 0 开始
            grid[node1][node2] = time[2];   //单向
        }

        //记录起点到当前节点的最短距离，初始值全为无穷大
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[k - 1] = 0;   //由于从 K 开始，因此 K 为起点

        //记录已经找到最短距离的节点
        int[] visited = new int[n];
        int visitedNums = 0;
        while (visitedNums < n) {  //记录已经找到最短距离的节点数
            //-------------------------------------------------------------------
            // 每一轮循环：
            // 首先，从未找到最短距离的节点中，找出当前具有最短距离的节点
            // 其次，更新所有节点"途径"此节点的最短距离
            //-------------------------------------------------------------------
            int minDistanceNode = -1;
            //1、从未找到最短距离的节点中，找出当前具有最短距离的节点
            for (int i = 0; i < n; i++) {
                if (visited[i] == 0 && (minDistanceNode == -1 || dist[i] < dist[minDistanceNode])) {
                    minDistanceNode = i;
                }
            }

            visited[minDistanceNode] = 1; //贪心，认为此节点的最短距离就是当前的距离，并去影响其他节点的最短距离

            //2、更新所有节点"途径"此节点的最短距离
            for (int i = 0; i < n; i++) {
                dist[i] = Math.min(dist[i], dist[minDistanceNode] + grid[minDistanceNode][i]);  //关键：途径，即两个节点间有边，不途径，无边则为默认的较大值
            }
            visitedNums++;
        }

        //当前以及获取各个点从起点的最短距离，取其中的最大值即可
        int ans = Arrays.stream(dist).max().getAsInt();

        //如果存在不能接受信号的节点，则 ans 为默认值，此时返回 -1
        return ans == INF ? -1 : ans;
    }

    public int networkDelayTime00(int[][] times, int n, int k) {  //基于 Dijkstra 算法，使用队列优化
        int INF = 0x3f3f3f3f;
        //建图，邻接矩阵
        int[][] grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(grid[i], INF);
        }
        //初始化图
        for (int[] edge : times) {
            int node1 = edge[0] - 1;
            int node2 = edge[1] - 1;
            int distance = edge[2];
            grid[node1][node2] = distance;
        }
        //-----------------------------------------------------
        // dist[p]最短距离的定义：从 0 点到 p 点的最短距离
        //-----------------------------------------------------
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[k - 1] = 0;
        int[] visited = new int[n];
        //优先队列，存储点的信息：点的编号、点的距离，注意一个点可能通过多个路径抵达，因此一个点可能在队列中存在多个距离信息
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[1] - o2[1]);
        sortedQueue.add(new int[]{k - 1, 0});
        while (!sortedQueue.isEmpty()) {
            int[] minDistanceNode = sortedQueue.poll();
            int currNode = minDistanceNode[0];
            int distance = minDistanceNode[1];
            //剔除队列中冗余元素
            if (visited[currNode] == 1) continue;
            visited[currNode] = 1;

            for (int i = 0; i < n; i++) {
                int currDistance = dist[currNode] + grid[currNode][i];
                if (currDistance < dist[i]) {
                    dist[i] = currDistance;
                    sortedQueue.add(new int[]{i, dist[i]});
                }
            }
        }

        int ans = Arrays.stream(dist).max().getAsInt();

        return ans == INF ? -1 : ans;
    }

    public int networkDelayTime01(int[][] times, int n, int k) {  //基于 Bellman Ford 算法
        int INF = 0x3f3f3f3f;
        //本题其实无需建图，本质 times 就是具有关联关系的图
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[k - 1] = 0;
        //-------------------------
        // Bellman Ford 的核心逻辑
        //-------------------------
        for (int i = 1; i <= n; i++) {  //根据题意，需要执行 n 次松弛操作
            int[] prev = dist.clone();
            for (int[] edge : times) {
                int node1 = edge[0] - 1;
                int node2 = edge[1] - 1;
                int distance = edge[2];
                dist[node2] = Math.min(dist[node2], prev[node1] + distance);
            }
        }
        int ans = Arrays.stream(dist).max().getAsInt();
        return ans == INF ? -1 : ans;
    }

    public int networkDelayTime02(int[][] times, int n, int k) {  //使用 SPFA 对 Bellman Ford 算法
        int INF = 0x3f3f3f3f;
        HashMap<Integer, ArrayList<int[]>> adjacent = new HashMap<>();
        for (int[] edge : times) {
            int node1 = edge[0] - 1;
            int node2 = edge[1] - 1;
            int distance = edge[2];
            ArrayList<int[]> arriveNodes = adjacent.getOrDefault(node1, new ArrayList<>());
            arriveNodes.add(new int[]{node2, distance});
            adjacent.put(node1, arriveNodes);
        }
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[k - 1] = 0;
        int[] inQueue = new int[n];
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.addLast(k - 1);
        inQueue[k - 1] = 1;
        //---------------------------------------------------
        // SPFA 的核心逻辑，使用队列有针对性的对边进行松弛操作
        //---------------------------------------------------
        while (!queue.isEmpty()) {
            Integer currNode = queue.pollFirst();
            inQueue[currNode] = 0;
            ArrayList<int[]> arriveNodes = adjacent.getOrDefault(currNode, new ArrayList<>());
            for (int[] info : arriveNodes) {
                int nextNode = info[0];
                int needDist = info[1];
                if (dist[currNode] + needDist < dist[nextNode]) {
                    dist[nextNode] = dist[currNode] + needDist;
                    if (inQueue[nextNode] == 0) {
                        queue.addLast(nextNode);
                        inQueue[nextNode] = 1;
                    }
                }
            }
        }
        int ans = Arrays.stream(dist).max().getAsInt();
        return ans == INF ? -1 : ans;
    }


    public int networkDelayTime03(int[][] times, int n, int k) {  //使用 Floyd（邻接矩阵）
        int INF = 0x3f3f3f3f;
        int[][] graph = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(graph[i], INF);
            graph[i][i] = 0;
        }
        for (int[] edge : times) {
            int node1 = edge[0] - 1;
            int node2 = edge[1] - 1;
            int price = edge[2];
            graph[node1][node2] = price;
        }

        //------------------------------------------------------------------------
        // Floyd 算法核心，基于三层循环，依次枚举 中转点、起点、终点，并最终执行松弛操作
        //------------------------------------------------------------------------
        for (int m = 0; m < n; m++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    graph[i][j] = Math.min(graph[i][j], graph[i][m] + graph[m][j]);  //松弛操作
                }
            }
        }

        int ans = 0;
        for (int i = 0; i < n; i++) {
            ans = Math.max(ans, graph[k - 1][i]);
        }
        return ans == INF ? -1 : ans;
    }


    public int networkDelayTime04(int[][] times, int n, int k) {  //使用 广度优先搜索
        int INF = 0x3f3f3f3f;
        int[][] graph = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(graph[i], INF);
            graph[i][i] = 0;
        }
        for (int[] edge : times) {
            int node1 = edge[0] - 1;
            int node2 = edge[1] - 1;
            int price = edge[2];
            graph[node1][node2] = price;
        }

        //------------------------------------------------------------------------
        // 广度优先搜索算法
        //------------------------------------------------------------------------
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[k - 1] = 0;
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.addLast(k - 1);
        while (!queue.isEmpty()) {
            Integer currNode = queue.pollFirst();
            for (int i = 0; i < n; i++) {
                if (dist[currNode] + graph[currNode][i] < dist[i]) {
                    dist[i] = dist[currNode] + graph[currNode][i];
                    queue.addLast(i);
                }
            }
        }
        int ans = Arrays.stream(dist).max().getAsInt();
        return ans == INF ? -1 : ans;
    }


    /**
     * 1976. 到达目的地的方案数
     */
    public int countPaths(int n, int[][] roads) {   //最短路径 + 拓扑排序 + 动态规划
        int mod = (int) 1e9 + 7;
        //邻接矩阵，存储边的信息
        int[][] graph = new int[n][n];
        //初始化邻接矩阵
        for (int[] road : roads) {
            int node1 = road[0];
            int node2 = road[1];
            int distance = road[2];
            //无向边
            graph[node1][node2] = distance;
            graph[node2][node1] = distance;
        }

        //入度
        int[] inDegree = new int[n];
        //基于朴素的 Dijkstra 获取从起点到各个节点最短路径
        long[] minDistance = countPathsDijkstra(graph);
        //基于最短路重新建图，并统计入度
        for (int[] road : roads) {   //新图中的路径是有向的
            int node1 = road[0];
            int node2 = road[1];
            int distance = road[2];

            //将图置为原始状态，后面仅记录最短的路径
            graph[node1][node2] = 0;
            graph[node2][node1] = 0;

            //------------------------------------------------------------
            // graph[i][j] 最初记录二者间的距离，用于计算端到端的最短路径
            // graph[i][j] 当前标识二者是否是最短路径的一条边，用 1/0 标识
            //------------------------------------------------------------

            //判断两点是否为最短路径的边，只有最短路径的边才会被建图
            if (minDistance[node1] + distance == minDistance[node2]) {  //方向一
                graph[node1][node2] = 1;   //仅表示二者是最短路径的一条边
                inDegree[node2]++;   //node2增加一个前置项，入度增加
            }
            if (minDistance[node2] + distance == minDistance[node1]) {  //方向二
                graph[node2][node1] = 1;   //仅表示二者是最短路径的一条边
                inDegree[node1]++;   //node1增加一个前置项，入度增加
            }
        }
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) arrayDeque.addLast(i);   //注意，非最短路径上的节点以及起点的入度均为 0
        }
        int[] dp = new int[n];
        dp[0] = 1; //动态规划，类似爬楼梯，起点到起点的路径数为 1
        while (!arrayDeque.isEmpty()) {
            Integer fatherNode = arrayDeque.pollFirst();
            for (int i = 0; i < n; i++) {
                if (graph[fatherNode][i] == 0) continue;
                //累加到达节点 i 的路径数
                dp[i] += dp[fatherNode];
                dp[i] %= mod;
                //更新节点 i 的入度
                inDegree[i]--;
                if (inDegree[i] == 0) {
                    arrayDeque.addLast(i);
                }
            }
        }
        return dp[n - 1];
    }

    private long[] countPathsDijkstra(int[][] graph) {
        //节点数
        int n = graph.length;
        //记录已经找到最短距离的节点
        int[] visited = new int[n];
        //从起点到各个节点最短路径的距离
        long[] minDistance = new long[n];

        //无穷大
        long INF = (long) 1e12;
        //初始化，原点到达各节点的最短距离
        Arrays.fill(minDistance, INF);
        minDistance[0] = 0;

        int visitedNums = 0;
        while (visitedNums < n) {
            int currentNode = -1;
            //1、从未找到最短距离距离的节点中，找出当前具有最短距离的节点
            for (int i = 0; i < n; i++) {
                if (visited[i] == 0 && (currentNode == -1 || minDistance[i] < minDistance[currentNode])) {
                    currentNode = i;
                }
            }

            visited[currentNode] = 1;  //贪心，认为此节点的最短距离为当前距离

            //2、更新所有节点从原点"途径"此节点的最短距离
            for (int i = 0; i < n; i++) {
                if (graph[currentNode][i] == 0) { //二者间无道路
                    continue;
                }
                minDistance[i] = Math.min(minDistance[i], minDistance[currentNode] + graph[currentNode][i]);
            }

            visitedNums++;
        }

        return minDistance;
    }


    /**
     * 882. 细分图中的可到达节点
     */
    public int reachableNodes(int[][] edges, int maxMoves, int n) {
        int INF = 0x3f3f3f3f;
        //建图
        int[][] graph = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(graph[i], INF);
        }
        //初始化图，各个边的细分节点数作为各个边的权重
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            int distance = edge[2] + 1;  //权重为边长，节点数 + 1
            graph[node1][node2] = distance;
            graph[node2][node1] = distance;
        }
        //计算各个节点距离原点的最短距离
        int[] dist = reachableNodesDijkstra(graph);
        int ans = 0;
        //-------------------------------------------------------------------------------------
        // 统计从 0 点可以到达的所有节点数，统计方式根据节点类型的不同，可分为：
        //    1、原始点个数：直接判断其最短距离是否小于 maxMoves，从而判断此点是否满足条件
        //    2、细分点个数：如果两个原始点之间有边，且可细分，则这两个原始点间满足条件的细分点个数为：
        //          基于两端的原始点，向内搜索，取并集，且其最大为其中的细分点个数
        //-------------------------------------------------------------------------------------
        for (int i = 0; i < n; i++) {  //1、原始点个数
            if (dist[i] <= maxMoves) ans++;
        }
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            int nodeNums = edge[2];
            int nums1 = Math.max(0, maxMoves - dist[node1]);  //从 node1 向细分节点搜索
            int nums2 = Math.max(0, maxMoves - dist[node2]);  //从 node2 向细分节点搜索

            ans += Math.min(nodeNums, nums1 + nums2);  //取交集，并去重
        }
        return ans;
    }

    private int[] reachableNodesDijkstra(int[][] graph) {
        int n = graph.length;
        int INF = 0x3f3f3f3f;
        int[] visited = new int[n];  //当前点是否已经确认最短距离
        int[] dist = new int[n];     //记录各个节点距离原点的最短距离
        Arrays.fill(dist, INF);
        dist[0] = 0;

        int ackNums = 0;  //已经确认最短距离的节点数
        while (ackNums < n) {
            //1、首先，确认最短距离的节点
            int currNode = -1;
            for (int i = 0; i < n; i++) {
                //------------------------------------------------------
                // 从当前未确认最短距离的所有节点中，贪心的认为其中拥有最短距离的节点，就是当前可选最短距离的节点
                //------------------------------------------------------
                if (visited[i] == 0 && (currNode == -1 || dist[i] < dist[currNode])) {
                    currNode = i;
                }
            }

            ackNums++;
            visited[currNode] = 1;  //将节点状态标记为已找到最短距离

            //2、新确认最短距离的节点，对其他节点施加影响
            for (int i = 0; i < n; i++) {
                //--------------------------------------------------------
                // 只要途径 currNode 的节点，都会受到影响，更新对应节点途径 currNode 节点后，其最短距离
                //--------------------------------------------------------
                dist[i] = Math.min(dist[i], dist[currNode] + graph[currNode][i]);  //如果不途径此节点，其最短距离仍为 INF
            }

        }

        return dist;
    }


    /**
     * 787. K 站中转内最便宜的航班
     */
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {   //运用 Bellman Ford 求解有限制的最短路问题
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

    public int findCheapestPrice00(int n, int[][] flights, int src, int dst, int k) {  //相较于上，无需构建邻接矩阵
        int INF = 0x3f3f3f3f;
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;
        for (int i = 0; i < k + 1; i++) {
            int[] prev = dist.clone();
            for (int[] flight : flights) {
                int node1 = flight[0];
                int node2 = flight[1];
                int price = flight[2];
                dist[node2] = Math.min(dist[node2], prev[node1] + price);
            }
        }
        return dist[dst] == INF ? -1 : dist[dst];
    }

    //-------------------------------------------------------------------------
    // 对比上面 Bellman Ford 算法 和下面 动态规划算法，本质是一致的
    // 均为当前轮的搜索基于上一轮的 dist 状态，上面是通过 clone 来获取上一轮 dist状态，下面存储每一轮的状态，通过 i - 1 来获取上一轮的状态
    //-------------------------------------------------------------------------

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


    public int findCheapestPrice01(int n, int[][] flights, int src, int dst, int k) {  //基于广度优先搜索 + 邻接表来解决最短路问题
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

    public int findCheapestPrice010(int n, int[][] flights, int src, int dst, int k) {  //广度优先搜索 + 邻接矩阵
        int INF = 0x3f3f3f3f;
        int[][] grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(grid[i], INF);
        }
        for (int[] edge : flights) {
            int node1 = edge[0];
            int node2 = edge[1];
            int price = edge[2];
            grid[node1][node2] = price;
        }
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.addLast(src);
        int nums = 0;
        while (nums < k + 1 && !queue.isEmpty()) {
            int size = queue.size();
            int[] prevDist = dist.clone();
            for (int i = 0; i < size; i++) {
                Integer currNode = queue.pollFirst();
                for (int j = 0; j < n; j++) {
                    int currPrice = prevDist[currNode] + grid[currNode][j];
                    if (currPrice < dist[j]) {
                        dist[j] = currPrice;
                        queue.addLast(j);
                    }
                }
            }
            nums++;
        }
        return dist[dst] == INF ? -1 : dist[dst];
    }


    public int findCheapestPrice011(int n, int[][] flights, int src, int dst, int k) {
        //---------------------------------------
        // SPFA 是在 Bellman Ford 基础上，使用"双端队列"（非优先队列）进行优化，有针对性的进行松弛操作
        //---------------------------------------
        int INF = 0x3f3f3f3f;
        int[][] grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(grid[i], INF);
            grid[i][i] = 0;
        }
        for (int[] edge : flights) {
            int node1 = edge[0];
            int node2 = edge[1];
            int price = edge[2];
            grid[node1][node2] = price;
        }
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.addLast(src);
        int[] inQueue = new int[n];
        inQueue[src] = 1;
        int nums = 0;
        while (nums < k + 1 && !queue.isEmpty()) {
            int size = queue.size();
            int[] prev = dist.clone();
            for (int i = 0; i < size; i++) {
                Integer currNode = queue.pollFirst();
                inQueue[currNode] = 0;
                for (int m = 0; m < n; m++) {
                    int currPrice = prev[currNode] + grid[currNode][m];
                    if (currPrice < dist[m]) {
                        dist[m] = currPrice;
                        queue.addLast(m);
                        //------------------------------------------------
                        // 其实所有的 SPFA 都不能这样写，因为每一轮循环使用的都是 prev 的状态
                        // 如果在此轮循环中 dist[m] 变的更小了，但由于队列中已经存在 m（此轮循环后续会遍历到）
                        // 所以以下面的逻辑，则不会再将 m 添加至队列，则后续无法使用此轮循环更新后更小的 dist[m]，则会有问题
                        //------------------------------------------------
//                        if (inQueue[m] == 0) {
//                            queue.addLast(m);
//                            inQueue[m] = 1;
//                        }
                    }
                }
            }
            nums++;
        }
        return dist[dst] == INF ? -1 : dist[dst];
    }


    public int findCheapestPrice04(int n, int[][] flights, int src, int dst, int k) {   //基于 Dijkstra 算法 + 优先队列
        int INF = 0x3f3f3f3f;
        int[][] graph = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(graph[i], INF);
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

        int[] cached = new int[n];
        Arrays.fill(cached, INF);
        cached[src] = 0;

        //优先队列，存储信息：节点编号、途径节点个数、距离
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);  //按照距离升序排序
        sortedQueue.add(new int[]{src, 0, 0});
        while (!sortedQueue.isEmpty()) {
            int[] tuple = sortedQueue.poll();
            int currNode = tuple[0];
            int currNums = tuple[1];
            int currDist = tuple[2];
            if (currNode == dst) {
                return currDist;
            }

            for (int i = 0; i < n; i++) {
                if (graph[currNode][i] == INF) {
                    continue;
                }
                int nextNums = currNums + 1;
                int nextDist = currDist + graph[currNode][i];
                if (nextNums > k + 1) {
                    continue;
                }
                //--------------------------------------------------------------------
                // 以下逻辑为 基于 Dijkstra 算法 + 优先队列 解决有限制的最短路问题的核心
                // 其相对于无限制的最短路问题的逻辑差异在于 多了 else if（贪心的保留使用限制条件更少的路径）
                //--------------------------------------------------------------------
                if (nextDist < dist[i]) {           //距离更短
                    cached[i] = nextNums;  //同步
                    dist[i] = nextDist;    //同步
                    sortedQueue.add(new int[]{i, nextNums, nextDist});
                } else if (nextNums < cached[i]) { //距离较长，但途径节点数增加，贪心的保留这种情况
//                    cached[i] = nextNums;  //不能加，否则结果错误，将条件加的太紧了，即使不是最短距离，也加紧，过度剪枝
//                    案例：int n = 9; int src = 0; int dst = 8; int k = 3;
//                          int[][] flights = {{0, 1, 1}, {1, 2, 1}, {2, 3, 1}, {3, 7, 1}, {0, 4, 3}, {4, 5, 3}, {5, 7, 3}, {0, 6, 5}, {6, 7, 100}, {7, 8, 1}};
                    sortedQueue.add(new int[]{i, nextNums, nextDist});
                }
            }
        }
        return -1;
    }

    public int findCheapestPrice041(int n, int[][] flights, int src, int dst, int k) {   //运用 Bellman Ford 求解有限制的最短路问题
        int INF = 0x3f3f3f3f;
        int[][] graph = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(graph[i], INF);
        }
        for (int[] edge : flights) {
            int node1 = edge[0];
            int node2 = edge[1];
            int price = edge[2];
            graph[node1][node2] = price;
        }
        int[] cached = new int[n];
        Arrays.fill(cached, INF);
        cached[src] = 0;

        //优先队列，存储信息：节点编号、途径节点个数、距离
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);  //按照距离升序排序
        sortedQueue.add(new int[]{src, 0, 0});
        while (!sortedQueue.isEmpty()) {
            int[] tuple = sortedQueue.poll();
            int currNode = tuple[0];
            int currNums = tuple[1];
            int currDist = tuple[2];
            if (currNode == dst) {
                return currDist;
            }

            for (int i = 0; i < n; i++) {
                if (graph[currNode][i] == INF) {
                    continue;
                }
                int nextNums = currNums + 1;
                int nextDist = currDist + graph[currNode][i];
                if (nextNums > k + 1) {
                    continue;
                }
                //------------------------------------------------
                // 以下逻辑为错误写法：其没有充分利用 K 的条件，只追求 K 更小（其实，最终只要不超就可以），而不追求 dist 更小
                // 错误示例：
                //     n = 3, edges = [[0,1,100],[1,2,100],[0,2,500]], src = 0, dst = 2, k = 1
                // 示例分析
                //     K = 2，最先到达 i（直接到达），其使用的 K == 1，更新 cache[i] == 1，下次如果遇到 K == 2 同时 dist 更小的路径，就会剔除掉，这是不合理的
                //------------------------------------------------
                if (nextNums >= cached[i]) {
                    continue;
                }
                cached[i] = nextNums;
                sortedQueue.add(new int[]{i, nextNums, nextDist});
            }
        }
        return -1;
    }


    /**
     * 778. 水位上升的泳池中游泳
     */
    public int swimInWater(int[][] grid) {  //单源最短路算法 Dijkstra，基于优先队列进行优化
        int m = grid.length;
        int n = grid[0].length;
        if (m == 1 && n == 1) return 0;
        int INF = 0x3f3f3f3f;
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        int[][] visited = new int[m][n];
        //--------------------------------------------------------------------------------
        // 每个位点最短距离的定义：
        //     dist[i][j]表示从源点 [0,0] 到点 [i,j] 的最短距离，由于可以从多条路径抵达点 [i,j]
        //     dist[i][j]其元素值定义为：
        //        多条路径最大值中的最小值，注意，每条路径最大值为其途径节点的最大元素值
        //--------------------------------------------------------------------------------
        int[][] dist = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(dist[i], INF);
        }
        dist[0][0] = grid[0][0];
        //优先队列，存储"点"的信息：row、col、weight
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);  //一个点会在queue中出现多次
        //-------------------------------------------------------------------------------------------------
        // 注意一个点可从多个路径抵达，同时每个路径均会生成到达当前点的"距离"
        // 由于搜索是有序的，因此一个点通过多个不同路径到达，其每个路径对应的距离，是依次有序获得的，是否将当前距离加入队列的依据是：
        //     只有当前路径对应的距离 < 之前路径到达的"最小距离"（在 dist 数组中记录），才会将此点及其更小的距离加入到队列中，并更新 dist 数组
        // 因此，一个点可能多次出现在 queue 中，在队列中会按照距离进行排序，一旦此点首次作为小根堆堆顶元素，则此点及其附带距离就是此点的最短距离，并将此点标记为已经找到最短距离，后续会依次作为依据忽略 queue中当前点的信息
        //-------------------------------------------------------------------------------------------------
        sortedQueue.add(new int[]{0, 0, grid[0][0]});
        while (!sortedQueue.isEmpty()) {
            int[] currNode = sortedQueue.poll();
            int row = currNode[0];
            int col = currNode[1];
            int high = currNode[2];
            //1、在 queue 中剔除已经找到最短距离的点
            if (visited[row][col] == 1) continue;
            //2、如果当前具有最短距离的点是终点，则直接返回结果，因为再也没有任何点可以更新终点的最短距离了
            if (row == m - 1 && col == n - 1) {
                return dist[m - 1][n - 1];
            }

            visited[row][col] = 1;  //将当前点标识为已经找到了最短距离，因为没有其他任何点可以更新此点的最短距离了

            //--------------------------------------------------------------------------------------------------------------
            // 使用当前拥有最短距离的点 A ，去尝试更新其相邻的点 B 的最短距离，即针对相邻点 B来讲，当前拥有最短距离点 A 代表抵达相邻点 B 的一条路径
            //--------------------------------------------------------------------------------------------------------------
            for (int[] dir : directions) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    if (visited[nextRow][nextCol] == 0) {
                        int currHigh = Math.max(dist[row][col], grid[nextRow][nextCol]);   //获取此路径的距离
                        if (currHigh < dist[nextRow][nextCol]) {  //此路径到达相邻点的距离，小于其他路径达到相邻点的距离
                            dist[nextRow][nextCol] = currHigh;                             //更新相邻点的最短距离
                            sortedQueue.add(new int[]{nextRow, nextCol, currHigh});        //将其添加至队列
                        }
                    }
                }
            }
        }
        return dist[m - 1][n - 1];
    }

    public int swimInWater00(int[][] grid) {  //单源最短路算法 Dijkstra
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

            //------------------------------------
            // 同上的差异在于一下逻辑，没有上个代码逻辑清晰和规范
            //------------------------------------

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

    public int swimInWater02(int[][] grid) {  //广度优先搜索 + 小根堆
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

    public int swimInWater000(int[][] grid) {  //并查集
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
            if (ufs.findSet(0) == ufs.findSet(n * n - 1)) {
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


    /**
     * 1631. 最小体力消耗路径
     */
    public int minimumEffortPath(int[][] heights) {  //单源最短路算法 Dijkstra 使用队列进行优化
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


    public int minimumEffortPath01(int[][] heights) {  //最小生成树算法
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


    /**
     * 1334. 阈值距离内邻居最少的城市
     */
    public int findTheCity(int n, int[][] edges, int distanceThreshold) {  //Dijkstra
        int INF = 0x3f3f3f3f;
        int[][] grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(grid[i], INF);
        }
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            int distance = edge[2];
            grid[node1][node2] = distance;
            grid[node2][node1] = distance;
        }
        //优先队列，每个元素：源点、满足条件的目标节点数
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> {
            if (o1[1] != o2[1]) {
                return o1[1] - o2[1];   //升序排序，优先取可到达节点数少的节点
            }
            return o2[0] - o1[0];       //降序排序，其次在可到达节点数一致的情况下，取节点编号大的节点
        });
        //--------------------------------------------------------------------
        // 每个节点以此所为源点，使用 Dijkstra 算法获取在规定距离内其可到达的节点数
        //--------------------------------------------------------------------
        for (int i = 0; i < n; i++) {
            int cityNums = findTheCityDijkstra(n, i, grid, distanceThreshold);
            sortedQueue.add(new int[]{i, cityNums});
        }
        return sortedQueue.peek()[0];
    }

    private int findTheCityDijkstra(int n, int src, int[][] grid, int distanceThreshold) {  //Dijkstra + 优先队列
        int INF = 0x3f3f3f3f;
        //记录各个节点的最短距离
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        //记录哪些节点已经确认了最短距离
        int[] visited = new int[n];
        //优先队列，存储点的信息：点的编号、点的距离
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[1] - o2[1]);  //按照权重升序排序
        sortedQueue.add(new int[]{src, dist[src]});

        //----------------------------------------------------------------------------------------------------------------------
        // Dijkstra + 优先队列，快速找到当前可以确认最短距离的节点，并影响此节点的相邻节点，如果相邻节点获取的更短的距离，添加至优先队列中
        //----------------------------------------------------------------------------------------------------------------------
        while (!sortedQueue.isEmpty()) {
            int[] mainInfo = sortedQueue.poll();
            int currNode = mainInfo[0];
            //1、剔除冗余元素
            if (visited[currNode] == 1) {
                continue;
            }
            //2、此节点确认了最短距离，给予标识
            visited[currNode] = 1;

            //3、使用此节点影响其相邻节点的最短距离
            for (int i = 0; i < n; i++) {
                if (visited[i] == 0) {
                    if (dist[currNode] + grid[currNode][i] < dist[i]) {
                        dist[i] = dist[currNode] + grid[currNode][i];
                        sortedQueue.add(new int[]{i, dist[i]});
                    }
                }
            }
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (dist[i] <= distanceThreshold) {
                ans++;   //每个节点到达自身的距离为 0，统计进来，因为所有的节点都会统计自身
            }
        }
        return ans;
    }


    private int findTheCityDijkstra01(int n, int src, int[][] grid, int distanceThreshold) {  //朴素的Dijkstra
        int INF = 0x3f3f3f3f;
        //记录各个节点的最短距离
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        //记录哪些节点已经确认了最短距离
        int[] visited = new int[n];

        //-----------------------------------------------------------------
        // Dijkstra，暴力找到当前可以确认最短距离的节点，并影响此节点的相邻节点
        //-----------------------------------------------------------------
        for (int m = 0; m < n; m++) {   //每轮会确认一个最短距离的节点，因此需要 n 轮
            int minNode = -1;
            //1、从当前未确认最短距离的节点中，选择一个当前拥有最短距离的节点，并认为其就是此节点的最短距离
            for (int i = 0; i < n; i++) {
                if (visited[i] == 0 && (minNode == -1 || dist[i] < dist[minNode])) {
                    minNode = i;
                }
            }

            //2、标识其找到了最短距离
            visited[minNode] = 1;

            //3、基于松弛操作影响其相邻节点
            for (int i = 0; i < n; i++) {
                dist[i] = Math.min(dist[i], dist[minNode] + grid[minNode][i]);  //松弛操作
            }
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (dist[i] <= distanceThreshold) {
                ans++;   //每个节点到达自身的距离为 0，统计进来，因为所有的节点都会统计自身
            }
        }
        return ans;
    }


    public int findTheCity01(int n, int[][] edges, int distanceThreshold) {  //Dijkstra
        int INF = 0x3f3f3f3f;
        int[][] grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(grid[i], INF);
        }
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            int distance = edge[2];
            grid[node1][node2] = distance;
            grid[node2][node1] = distance;
        }
        //优先队列，每个元素：源点、满足条件的目标节点数
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> {
            if (o1[1] != o2[1]) {
                return o1[1] - o2[1];   //升序排序，优先取可到达节点数少的节点
            }
            return o2[0] - o1[0];       //降序排序，其次在可到达节点数一致的情况下，取节点编号大的节点
        });
        //--------------------------------------------------------------------
        // 每个节点以此所为源点，使用 Dijkstra 算法获取在规定距离内其可到达的节点数
        //--------------------------------------------------------------------
        for (int i = 0; i < n; i++) {
            int cityNums = findTheCityDijkstra(n, i, grid, distanceThreshold);
            sortedQueue.add(new int[]{i, cityNums});
        }
        return sortedQueue.peek()[0];
    }

    public int findTheCity02(int n, int[][] edges, int distanceThreshold) {  //Floyd
        int INF = 0x3f3f3f3f;
        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], INF);
            dist[i][i] = 0;  //重要
        }
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            int distance = edge[2];
            dist[node1][node2] = distance;
            dist[node2][node1] = distance;
        }
        //---------------------------------------------------------------------------------------------------------------------
        // Floyd 算法主要解决多元最短路问题，即任意两点间的最短距离，其算法核心是通过三层循环，依次枚举 中转点、起点、终点，并执行松弛操作
        //---------------------------------------------------------------------------------------------------------------------
        for (int m = 0; m < n; m++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    dist[i][j] = Math.min(dist[i][j], dist[i][m] + dist[m][j]);   //松弛操作
                }
            }
        }
        //求最终结果不使用上面解法中的优先队列的方式，换一种方式
        int ans = -1;
        int minNums = n + 10;
        for (int i = n - 1; i >= 0; i--) {
            int currNums = 0;
            for (int j = 0; j < n; j++) {
                if (dist[i][j] <= distanceThreshold) {
                    currNums++;
                }
            }
            if (currNums < minNums) {
                ans = i;
                minNums = currNums;
            }
        }
        return ans;
    }

    public int findTheCity03(int n, int[][] edges, int distanceThreshold) {  //Bellman Ford
        int INF = 0x3f3f3f3f;
        int ans = -1;
        int minNums = n + 10;
        for (int i = n - 1; i >= 0; i--) {
            //-------------------------------------------------------------------------
            // Bellman Ford 单源最短路问题，不内置限制条件，限制条件在最终 dist 数组中施加
            //-------------------------------------------------------------------------
            int currNums = findTheCityBellmanFord(n, i, edges, distanceThreshold);
            if (currNums < minNums) {
                ans = i;
                minNums = currNums;
            }
        }
        return ans;
    }

    private int findTheCityBellmanFord(int n, int src, int[][] edges, int distanceThreshold) {
        int INF = 0x3f3f3f3f;
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;
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
            if (dist[i] <= distanceThreshold) {
                ans++;
            }
        }
        return ans;
    }


    public int findTheCity04(int n, int[][] edges, int distanceThreshold) {  //Bellman Ford + 双端队列
        int INF = 0x3f3f3f3f;
        int[][] grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(grid[i], INF);
        }
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            int distance = edge[2];
            grid[node1][node2] = distance;
            grid[node2][node1] = distance;
        }
        int ans = -1;
        int minNums = n + 10;
        for (int i = n - 1; i >= 0; i--) {
            //-------------------------------------------------------------------------
            // Bellman Ford 单源最短路问题，不内置限制条件，限制条件在最终 dist 数组中施加
            //-------------------------------------------------------------------------
            int currNums = findTheCityBellmanFord04(n, i, grid, distanceThreshold);
            if (currNums < minNums) {
                ans = i;
                minNums = currNums;
            }
        }
        return ans;
    }


    private int findTheCityBellmanFord04(int n, int src, int[][] grid, int distanceThreshold) {
        int INF = 0x3f3f3f3f;
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        int[] inQueue = new int[n];
        queue.addLast(src);
        inQueue[src] = 1;

        while (!queue.isEmpty()) {
            int currNode = queue.pollFirst();
            inQueue[currNode] = 0;
            for (int i = 0; i < n; i++) {
                if (dist[currNode] + grid[currNode][i] < dist[i]) {
                    dist[i] = dist[currNode] + grid[currNode][i];
                    if (inQueue[i] == 0) {
                        queue.addLast(i);
                        inQueue[i] = 1;
                    }
                }
            }
        }

        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (dist[i] <= distanceThreshold) {
                ans++;
            }
        }
        return ans;
    }


    /**
     * 1514. 概率最大的路径
     */
    public double maxProbability(int n, int[][] edges, double[] succProb, int start, int end) {  //基于 Dijkstra 算法，优先队列
        //邻接表
        HashMap<Integer, ArrayList<Node>> adjacent = new HashMap<>();  //稀疏图，建议使用 邻接表
        for (int i = 0; i < edges.length; i++) {
            int node1 = edges[i][0];
            int node2 = edges[i][1];
            double prob = succProb[i];
            ArrayList<Node> list1 = adjacent.getOrDefault(node1, new ArrayList<>());
            list1.add(new Node(node2, prob));
            adjacent.put(node1, list1);
            ArrayList<Node> list2 = adjacent.getOrDefault(node2, new ArrayList<>());
            list2.add(new Node(node1, prob));
            adjacent.put(node2, list2);
        }
        double[] dist = new double[n];
        dist[start] = 1.0;
        int[] visited = new int[n];
        PriorityQueue<Node> sortedQueue = new PriorityQueue<>((o1, o2) -> {
            if (o1.prob == o2.prob) return 0;
            return o1.prob > o2.prob ? -1 : 1;  //按照概率降序排序
        });  //按照概率降序排序
        sortedQueue.add(new Node(start, 1.0));
        while (!sortedQueue.isEmpty()) {
            Node node = sortedQueue.poll();
            int currNode = node.id;
            double currProb = node.prob;
            if (visited[currNode] == 1) continue;
            visited[currNode] = 1;
            if (currNode == end) return dist[currNode];

            ArrayList<Node> affactNodes = adjacent.getOrDefault(currNode, new ArrayList<>());
            for (Node nextNode : affactNodes) {
                if (visited[nextNode.id] == 1) continue;
                double nextProb = dist[currNode] * nextNode.prob;
                if (nextProb > dist[nextNode.id]) {
                    dist[nextNode.id] = nextProb;
                    sortedQueue.add(new Node(nextNode.id, nextProb));
                }
            }
        }
        return dist[end];
    }

    //------------------------------------------------------
    // 以下两种写法，没使用邻接表或没使用队列优化，均会超时
    //------------------------------------------------------

    public double maxProbability01(int n, int[][] edges, double[] succProb, int start, int end) {  //基于 Dijkstra 算法，暴力，超时
        double[][] grid = new double[n][n];
        for (int i = 0; i < n; i++) {
            grid[i][i] = 1.0;
        }
        for (int i = 0; i < edges.length; i++) {
            int node1 = edges[i][0];
            int node2 = edges[i][1];
            double prob = succProb[i];
            grid[node1][node2] = prob;
            grid[node2][node1] = prob;
        }
        double[] dist = new double[n];
        dist[start] = 1.0;
        int[] visited = new int[n];
        int nums = 0;
        while (nums < n) {
            int currNode = -1;
            for (int i = 0; i < n; i++) {
                if (visited[i] == 0 && (currNode == -1 || dist[i] > dist[currNode])) {
                    currNode = i;
                }
            }

            if (currNode == end) return dist[currNode];
            visited[currNode] = 1;

            for (int i = 0; i < n; i++) {
                if (grid[currNode][i] != 0.0) {
                    dist[i] = Math.max(dist[i], dist[currNode] * grid[currNode][i]);
                }
            }

            nums++;
        }

        return dist[end];
    }


    public double maxProbability02(int n, int[][] edges, double[] succProb, int start, int end) {  //基于 Dijkstra 算法，优先队列
        double[][] grid = new double[n][n];
        for (int i = 0; i < n; i++) {
            grid[i][i] = 1.0;
        }
        for (int i = 0; i < edges.length; i++) {
            int node1 = edges[i][0];
            int node2 = edges[i][1];
            double prob = succProb[i];
            grid[node1][node2] = prob;
            grid[node2][node1] = prob;
        }
        double[] dist = new double[n];
        dist[start] = 1.0;
        int[] visited = new int[n];
        PriorityQueue<Node> sortedQueue = new PriorityQueue<>((o1, o2) -> {
            if (o1.prob == o2.prob) return 0;
            return o1.prob > o2.prob ? -1 : 1;  //按照概率降序排序
        });  //按照概率降序排序
        sortedQueue.add(new Node(start, 1.0));
        while (!sortedQueue.isEmpty()) {
            Node node = sortedQueue.poll();
            int currNode = node.id;
            double currProb = node.prob;
            if (visited[currNode] == 1) continue;
            visited[currNode] = 1;
            if (currNode == end) return dist[currNode];

            for (int i = 0; i < n; i++) {
                if (grid[currNode][i] != 0.0) {
                    double nextProb = dist[currNode] * grid[currNode][i];
                    if (nextProb > dist[i]) {
                        dist[i] = nextProb;
                        sortedQueue.add(new Node(i, nextProb));
                    }
                }
            }
        }

        return dist[end];
    }

    static class Node {
        int id;
        double prob;

        Node(int id, double prob) {
            this.id = id;
            this.prob = prob;
        }
    }


    /**
     * 1368. 使网格图至少有一条有效路径的最小代价
     */
    public int minCost(int[][] grid) {      //基于 Dijkstra 算法，优先队列
        int m = grid.length;
        int n = grid[0].length;
        int INF = 0x3f3f3f3f;
        int[][] directions = {{0, 0}, {0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        //将二维坐标转换为一维
        int[] visited = new int[m * n];
        int[] dist = new int[m * n];      //最小花费，最小翻转次数
        Arrays.fill(dist, INF);
        dist[0] = 0;
        //优先队列，存储点的信息
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);  //按照权重升序排序
        sortedQueue.add(new int[]{0, 0, 0});
        while (!sortedQueue.isEmpty()) {
            int[] tuple = sortedQueue.poll();
            int row = tuple[0];
            int col = tuple[1];
            int weight = tuple[2];
            int currIndex = row * n + col;
            if (visited[currIndex] == 1) continue;
            visited[currIndex] = 1;

            for (int i = 1; i <= 4; i++) {
                int nextRow = row + directions[i][0];
                int nextCol = col + directions[i][1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    int nextIndex = nextRow * n + nextCol;
                    int nextWeight = dist[currIndex] + (grid[row][col] == i ? 0 : 1);   //权重的关键
                    if (nextWeight < dist[nextIndex]) {
                        dist[nextIndex] = nextWeight;
                        sortedQueue.add(new int[]{nextRow, nextCol, nextWeight});
                    }
                }
            }
        }
        return dist[m * n - 1];
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


    public int shortestPath010(int[][] grid, int k) {   //基于 Dijkstra 算法 + 优先队列
        int INF = 0x3f3f3f3f;
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
        cached[0][0] = grid[0][0];  //直接写为 0 可以，因为题目明确 grid[0][0] == 0

        int[][] dist = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(dist[i], INF);
        }
        dist[0][0] = 1;

        //优先队列
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);  //按照步数升序排序
        sortedQueue.add(new int[]{0, 0, 0, 0});  //横坐标、纵坐标、步数、替换次数
        while (!sortedQueue.isEmpty()) {
            int[] tuple = sortedQueue.poll();
            int row = tuple[0];
            int col = tuple[1];
            int currDist = tuple[2];  //步数
            int currNums = tuple[3];  //替换次数
            if (row == m - 1 && col == n - 1) {
                return currDist;
            }

            for (int[] dir : directions) {
                int nextRow = row + dir[0];
                int nextCol = col + dir[1];
                if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                    int nextNums = currNums + grid[nextRow][nextCol];
                    int nextDist = currDist + 1;
                    if (nextNums > k) {
                        continue;
                    }
                    if (nextDist < dist[nextRow][nextCol]) {
                        dist[nextRow][nextCol] = nextDist;
                        cached[nextRow][nextCol] = nextNums;
                        sortedQueue.add(new int[]{nextRow, nextCol, nextDist, nextNums});
                    } else if (nextNums < cached[nextRow][nextCol]) {
                        cached[nextRow][nextCol] = nextNums;   //不加，则超时，加上其实逻辑不严谨，可能会剔除正确路径，但如果案例不完善，则测不到
                        sortedQueue.add(new int[]{nextRow, nextCol, nextDist, nextNums});
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 1928. 规定时间内到达终点的最小花费
     */
    public int minCost(int maxTime, int[][] edges, int[] passingFees) {
        int INF = 0x3f3f3f3f;
        int n = passingFees.length;
        int[][] dp = new int[maxTime + 1][n];  //dp[i][j] 状态定义为 恰好使用 i 分钟，到达城市 j 所需要的最小花费
        for (int i = 0; i < maxTime + 1; i++) {
            Arrays.fill(dp[i], INF);
        }
        dp[0][0] = passingFees[0]; //花费 0 分钟到达城市 0 需要的通行费之和，注意："通行费之和"包括 起点和终点城市的通行费
        //-------------------------------------------------
        // 动态规划，按照"恰好"的写法来进行状态转移
        //-------------------------------------------------
        for (int i = 1; i <= maxTime; i++) {
            for (int[] edge : edges) {
                int node1 = edge[0];
                int node2 = edge[1];
                int needTime = edge[2];
                if (i >= needTime) {  //不超过 指定时间 的关键
                    dp[i][node1] = Math.min(dp[i][node1], dp[i - needTime][node2] + passingFees[node1]);
                    dp[i][node2] = Math.min(dp[i][node2], dp[i - needTime][node1] + passingFees[node2]);
                }

            }
        }
        int ans = INF;
        for (int i = 0; i <= maxTime; i++) {
            ans = Math.min(ans, dp[i][n - 1]);
        }
        return ans == INF ? -1 : ans;
    }


    public int minCost01(int maxTime, int[][] edges, int[] passingFees) {  //基于 Dijkstra 算法 + 优先队列
        int INF = 0x3f3f3f3f;
        int n = passingFees.length;
        //邻接矩阵
        int[][] grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(grid[i], INF);
        }
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            int times = edge[2];
            grid[node1][node2] = Math.min(grid[node1][node2], times);
            grid[node2][node1] = Math.min(grid[node2][node1], times);
        }
        //优先队列，存储信息：节点编号、花费时长、花费金额，并按照题目待求解的要素（金额）升序排序
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);  //升序排序
        sortedQueue.add(new int[]{0, 0, passingFees[0]});
        //记录最短时间，同时用于识别哪些节点已经更新过最短时间了
        int[] visited = new int[n];
        Arrays.fill(visited, INF);
        while (!sortedQueue.isEmpty()) {
            int[] tuple = sortedQueue.poll();
            int currNode = tuple[0];
            int currTime = tuple[1];
            int currDist = tuple[2];
            //当前节点是新被确认的最短距离的节点，如果此节点为目标节点，直接返回
            if (currNode == n - 1) {
                return currDist;
            }
            //剪枝一：优先队列按照路径的花费升序排序，因此先经过的花费小，后经过的花费大
            if (currTime >= visited[currNode]) {  //后经过，时间又长，那么肯定不是花费最小的路径
                continue;
            }
            //---------------------------------------------------------------
            // 执行到此处则说明当前节点对应的距离一定不是最短距离，此时贪心的认为：
            //     1、不关心前面的最短距离是否能够最终在满足时间要求的条件下，构建到达 n - 1 的路径
            //     2、只关心当前的次短距离拥有更短的时间消耗，而最短距离对应的时间比此长，但其可能最终因为路径超时而不满足条件，
            //           因此，此最短距离可能就是最符合时间条件的，构建到达 n - 1 距离最短的路径
            // 所以，此时间消耗更少的次最短距离，不能舍弃（下个题解的 visited[i] == 1 就是舍弃了，因此错误），而应施加影响并最终添加到队列中
            //---------------------------------------------------------------
            visited[currNode] = currTime;  //放在这里和放在下面，区别不大，因为下面仅仅是满足其影响的 nextTime 当前不超时，没法决定其直到 n - 1 都不超时

            //对其它节点施加影响，尝试进行松弛操作
            for (int i = 0; i < n; i++) {
                int nextDist = currDist + passingFees[i];
                int nextTime = currTime + grid[currNode][i];
                //剪枝二：不满足题目时间要求的路径耗时
                if (nextTime > maxTime) {
                    continue;
                }
//                visited[currNode] = currTime;
                //无需判断其是否会成功松弛 nextDist
                sortedQueue.add(new int[]{i, nextTime, nextDist});
            }
        }
        return -1;
    }


    public int minCost011(int maxTime, int[][] edges, int[] passingFees) {  //基于 Dijkstra 算法 + 优先队列
        int INF = 0x3f3f3f3f;
        int n = passingFees.length;
        //邻接矩阵
        int[][] grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(grid[i], INF);
        }
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            int times = edge[2];
            grid[node1][node2] = Math.min(grid[node1][node2], times);
            grid[node2][node1] = Math.min(grid[node2][node1], times);
        }
        //优先队列，存储信息：节点编号、花费时长、花费金额，并按照题目待求解的要素（金额）升序排序
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);  //升序排序
        sortedQueue.add(new int[]{0, 0, passingFees[0]});
        //记录最短时间，同时用于识别哪些节点已经更新过最短时间了
        int[] visited = new int[n];
        Arrays.fill(visited, INF);
        visited[0] = 0;
        while (!sortedQueue.isEmpty()) {
            int[] tuple = sortedQueue.poll();
            int currNode = tuple[0];
            int currTime = tuple[1];
            int currDist = tuple[2];
            //当前节点是新被确认的最短距离的节点，如果此节点为目标节点，直接返回
            if (currNode == n - 1) {
                return currDist;
            }
            //对其它节点施加影响，尝试进行松弛操作
            for (int i = 0; i < n; i++) {
                int nextDist = currDist + passingFees[i];
                int nextTime = currTime + grid[currNode][i];
                //剪枝一：不满足题目时间要求的路径耗时
                if (nextTime > maxTime) {
                    continue;
                }
                //剪枝二：优先队列按照路径的花费升序排序，因此先经过的花费小，后经过的花费大
                if (nextTime >= visited[i]) {  //后经过，时间又长，那么肯定不是花费最小的路径
                    continue;
                }
                //---------------------------------------------------------------
                // 关键：判断入队列的条件：耗时更短，而非传统的距离更短
                // 执行到此处则说明当前节点之前已经有一个距离入队列了，所以 visited中才会有对应的耗时，此时贪心的认为：
                //     1、不关心前面的较短距离是否能够最终在满足时间要求的条件下，构建到达 n - 1 的最短路径
                //     2、只关心当前的较短距离拥有更短的时间消耗，而上个较短距离对应的时间比此长，但其可能最终因为路径超时而不满足条件，
                //           因此，当前拥有更少耗时的较短距离可能就是最符合时间条件的，构建到达 n - 1 的最短路径
                // 所以，此时间消耗更少的次最短距离，不能舍弃（下个题解的 visited[i] == 1 就是舍弃了，因此错误），而应施加影响并最终添加到队列中
                //---------------------------------------------------------------
                visited[i] = nextTime;
                sortedQueue.add(new int[]{i, nextTime, nextDist});
            }
        }
        return -1;
    }

    public int minCost02(int maxTime, int[][] edges, int[] passingFees) {  //基于 Dijkstra 算法 + 优先队列 + 邻接表，效率更高
        int INF = 0x3f3f3f3f;
        int n = passingFees.length;
        //邻接矩阵
        HashMap<Integer, ArrayList<int[]>> adjacent = new HashMap<>();
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            int times = edge[2];
            ArrayList<int[]> list1 = adjacent.getOrDefault(node1, new ArrayList<>());
            list1.add(new int[]{node2, times});
            adjacent.put(node1, list1);
            ArrayList<int[]> list2 = adjacent.getOrDefault(node2, new ArrayList<>());
            list2.add(new int[]{node1, times});
            adjacent.put(node2, list2);
        }
        //优先队列，存储信息：节点编号、花费时长、花费金额，并按照题目待求解的要素（金额）升序排序
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);  //升序排序
        sortedQueue.add(new int[]{0, 0, passingFees[0]});
        //记录最短时间，同时用于识别哪些节点已经更新过最短时间了
        int[] visited = new int[n];
        Arrays.fill(visited, INF);
        visited[0] = 0;
        while (!sortedQueue.isEmpty()) {
            int[] tuple = sortedQueue.poll();
            int currNode = tuple[0];
            int currTime = tuple[1];
            int currDist = tuple[2];
            //当前节点是新被确认的最短距离的节点，如果此节点为目标节点，直接返回
            if (currNode == n - 1) {
                return currDist;
            }
            //对其它节点施加影响，尝试进行松弛操作
            ArrayList<int[]> affectNodes = adjacent.getOrDefault(currNode, new ArrayList<>());
            for (int[] affect : affectNodes) {
                int nextNode = affect[0];
                int needTime = affect[1];
                //计算距离和耗时
                int nextDist = currDist + passingFees[nextNode];
                int nextTime = currTime + needTime;
                //剪枝一：不满足题目时间要求的路径耗时
                if (nextTime > maxTime) {
                    continue;
                }
                //剪枝二：优先队列按照路径的花费升序排序，因此先经过的花费小，后经过的花费大
                if (nextTime >= visited[nextNode]) {  //后经过，时间又长，那么肯定不是花费最小的路径
                    continue;
                }
                //---------------------------------------------------------------
                // 关键：判断入队列的条件：耗时更短，而非传统的距离更短
                // 执行到此处则说明当前节点之前已经有一个距离入队列了，所以 visited中才会有对应的耗时，此时贪心的认为：
                //     1、不关心前面的较短距离是否能够最终在满足时间要求的条件下，构建到达 n - 1 的最短路径
                //     2、只关心当前的较短距离拥有更短的时间消耗，而上个较短距离对应的时间比此长，但其可能最终因为路径超时而不满足条件，
                //           因此，当前拥有更少耗时的较短距离可能就是最符合时间条件的，构建到达 n - 1 的最短路径
                // 所以，此时间消耗更少的次最短距离，不能舍弃（下个题解的 visited[i] == 1 就是舍弃了，因此错误），而应施加影响并最终添加到队列中
                //---------------------------------------------------------------
                visited[nextNode] = nextTime;
                sortedQueue.add(new int[]{nextNode, nextTime, nextDist});
            }
        }
        return -1;
    }


    public int minCost021(int maxTime, int[][] edges, int[] passingFees) {  //基于 Dijkstra 算法 + 优先队列 + 邻接表，效率更高
        int INF = 0x3f3f3f3f;
        int n = passingFees.length;
        //邻接矩阵
        HashMap<Integer, ArrayList<int[]>> adjacent = new HashMap<>();
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            int times = edge[2];
            ArrayList<int[]> list1 = adjacent.getOrDefault(node1, new ArrayList<>());
            list1.add(new int[]{node2, times});
            adjacent.put(node1, list1);
            ArrayList<int[]> list2 = adjacent.getOrDefault(node2, new ArrayList<>());
            list2.add(new int[]{node1, times});
            adjacent.put(node2, list2);
        }
        //优先队列，存储信息：节点编号、花费时长、花费金额，并按照题目待求解的要素（金额）升序排序
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);  //升序排序
        sortedQueue.add(new int[]{0, 0, passingFees[0]});

        //记录最短距离
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[0] = passingFees[0];

        //记录最短时间，同时用于识别哪些节点已经更新过最短时间了
        int[] time = new int[n];
        Arrays.fill(time, INF);
        time[0] = 0;
        while (!sortedQueue.isEmpty()) {
            int[] tuple = sortedQueue.poll();
            int currNode = tuple[0];
            int currTime = tuple[1];
            int currDist = tuple[2];
            //当前节点是新被确认的最短距离的节点，如果此节点为目标节点，直接返回
            if (currNode == n - 1) {
                return currDist;
            }
            //对其它节点施加影响，尝试进行松弛操作
            ArrayList<int[]> affectNodes = adjacent.getOrDefault(currNode, new ArrayList<>());
            for (int[] affect : affectNodes) {
                int nextNode = affect[0];
                int needTime = affect[1];
                //计算距离和耗时
                int nextDist = currDist + passingFees[nextNode];
                int nextTime = currTime + needTime;
                //剪枝一：不满足题目时间要求的路径耗时
                if (nextTime > maxTime) {
                    continue;
                }
                if (nextDist < dist[nextNode]) {       //距离更短
                    dist[nextNode] = nextDist;
                    time[nextNode] = nextTime;
                    sortedQueue.add(new int[]{nextNode, nextTime, nextDist});
                } else if (nextTime < time[nextNode]) { //距离较长，但路径耗时较短，贪心将其添加至队列中
                    //-----------------------------------
                    // 理论上，不能加 time[nextNode] = nextTime，会过度剪枝，剔除掉正确路径，因此逻辑不合理，本题案例不全，没有覆盖不合理的分支
                    // 加上后，过度剪枝，使得效率更高，不会超时，否则超时
                    //-----------------------------------
                    time[nextNode] = nextTime;   //矛盾点
                    sortedQueue.add(new int[]{nextNode, nextTime, nextDist});
                }
            }
        }
        return -1;
    }

    public int minCost03(int maxTime, int[][] edges, int[] passingFees) {  //错误写法，其错误的剔除了所谓的冗余数据
        int INF = 0x3f3f3f3f;
        int n = passingFees.length;
        //邻接矩阵
        int[][] grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(grid[i], INF);
        }
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            int times = edge[2];
            grid[node1][node2] = Math.min(grid[node1][node2], times);
            grid[node2][node1] = Math.min(grid[node2][node1], times);
        }
        //优先队列，存储信息：节点编号、花费时长、花费金额，并按照题目待求解的要素（金额）升序排序
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[2] - o2[2]);  //升序排序
        sortedQueue.add(new int[]{0, 0, passingFees[0]});
        //记录哪些节点确认了最短距离
        int[] visited = new int[n];
        while (!sortedQueue.isEmpty()) {
            int[] tuple = sortedQueue.poll();
            int currNode = tuple[0];
            int currTime = tuple[1];
            int currDist = tuple[2];
            //剪枝一：此节点已经确认了最短距离，剔除冗余数据
            if (visited[currNode] == 1) {  //不能加这个，去掉后，超时，少一个重要的剪枝，见上面的写法
                //--------------------------------------
                // 不能加的原因是，这个题目本质并不是要求在不超过时间限制的情况下，求从 0 到各个节点的最短距离（花费最小）
                // 其本质是不超过时间限制的情况下，求从 0 到 n - 1 的最短距离（花费最小），即其不要求 从 0 到各个节点均拥有最短距离
                // 而是期望从 0 到 n - 1 途径的节点，能有合适的较小距离和较短时间，而不要求是最短距离，因为最短距离对应的时间可能较长，不能满足后续节点的耗时
                //--------------------------------------
                continue;
            }
            //剪枝二：不满足题目时间要求的路径耗时
            if (currTime > maxTime) {
                continue;
            }
            //当前节点是新被确认的最短距离的节点，如果此节点为目标节点，直接返回
            if (currNode == n - 1) {
                return currDist;
            }
            visited[currNode] = 1; //将其标识为已找到最短距离

            //对其它节点施加影响，尝试进行松弛操作
            for (int i = 0; i < n; i++) {
                int nextDist = currDist + passingFees[i];
                int nextTime = currTime + grid[currNode][i];
                if (nextTime <= maxTime) {
                    sortedQueue.add(new int[]{i, nextTime, nextDist});
                }
//                if (nextDist < dist[i] && nextTime <= maxTime) {
//                    dist[i] = nextDist;
//                    sortedQueue.add(new int[]{i, nextTime, nextDist});
//                }
            }
        }
        return -1;
    }


    /**
     * 399. 除法求值
     */
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {  //Floyd
        int currNums = 0;
        //将字符串转换为节点编号
        HashMap<String, Integer> hTable = new HashMap<>();
        for (List<String> eq : equations) {
            String node1 = eq.get(0);
            String node2 = eq.get(1);
            if (!hTable.containsKey(node1)) {
                hTable.put(node1, currNums++);
            }
            if (!hTable.containsKey(node2)) {
                hTable.put(node2, currNums++);
            }
        }

        //建图
        double[][] graph = new double[currNums][currNums];
        //初始化图
        for (int i = 0; i < currNums; i++) {
            Arrays.fill(graph[i], -1.0);
        }
        for (int i = 0; i < values.length; i++) {
            Integer node1 = hTable.get(equations.get(i).get(0));
            Integer node2 = hTable.get(equations.get(i).get(1));
            graph[node1][node2] = values[i];
            graph[node2][node1] = 1.0 / values[i];
        }

        //对任意节点进行松弛操作，获取各个节点的除法值
        for (int k = 0; k < currNums; k++) {
            for (int i = 0; i < currNums; i++) {
                for (int j = 0; j < currNums; j++) {
                    if (graph[i][k] != -1.0 && graph[k][j] != -1.0) {
                        graph[i][j] = graph[i][k] * graph[k][j];
                    }
                }
            }
        }

        //查询操作
        int qn = queries.size();
        double[] ans = new double[qn];
        Arrays.fill(ans, -1.0);
        for (int i = 0; i < qn; i++) {
            List<String> query = queries.get(i);
            if (hTable.containsKey(query.get(0)) && hTable.containsKey(query.get(1))) {
                Integer node1 = hTable.get(query.get(0));
                Integer node2 = hTable.get(query.get(1));
                ans[i] = graph[node1][node2];
            }
        }
        return ans;
    }


    public double[] calcEquation01(List<List<String>> equations, double[] values, List<List<String>> queries) {  //广度优先搜索
        int currNums = 0;
        HashMap<String, Integer> hTable = new HashMap<>();
        for (List<String> eq : equations) {
            String node1 = eq.get(0);
            String node2 = eq.get(1);
            if (!hTable.containsKey(node1)) {
                hTable.put(node1, currNums++);
            }
            if (!hTable.containsKey(node2)) {
                hTable.put(node2, currNums++);
            }
        }
        double[][] graph = new double[currNums][currNums];
        for (int i = 0; i < currNums; i++) {
            Arrays.fill(graph[i], -1.0);
        }
        for (int i = 0; i < values.length; i++) {
            Integer node1 = hTable.get(equations.get(i).get(0));
            Integer node2 = hTable.get(equations.get(i).get(1));
            graph[node1][node2] = values[i];
            graph[node2][node1] = 1.0 / values[i];
        }

        int qn = queries.size();
        double[] ans = new double[qn];
        Arrays.fill(ans, -1.0);
        for (int i = 0; i < qn; i++) {
            if (hTable.containsKey(queries.get(i).get(0)) && hTable.containsKey(queries.get(i).get(1))) {
                Integer node1 = hTable.get(queries.get(i).get(0));
                Integer node2 = hTable.get(queries.get(i).get(1));
                if (node1.equals(node2)) {
                    ans[i] = 1.0;
                } else {
                    ArrayDeque<Integer> queue = new ArrayDeque<>();
                    queue.addLast(node1);
                    double[] ratio = new double[currNums];
                    Arrays.fill(ratio, -1.0);
                    ratio[node1] = 1.0;

                    while (!queue.isEmpty() && ratio[node2] < 0) {
                        Integer currNode = queue.pollFirst();
                        for (int j = 0; j < currNums; j++) {
                            if (graph[currNode][j] > 0 && ratio[j] < 0) {  //二者有直接关系
                                ratio[j] = ratio[currNode] * graph[currNode][j];
                                queue.addLast(j);
                            }
                        }
                    }

                    ans[i] = ratio[node2];
                }
            }
        }

        return ans;
    }

    //为上题补充以下两种解法：并查集 和 深度优先搜索算法



    /**
     * 1824. 最少侧跳次数
     */
    public int minSideJumps(int[] obstacles) {
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


    /**
     * 1129. 颜色交替的最短路径
     */
    public int[] shortestAlternatingPaths(int n, int[][] redEdges, int[][] blueEdges) {  //最短路径，广度优先搜索
        int INF = 0x3f3f3f3f;
        //邻接表
        int[][][] adjacent = new int[n][2][n];
        //初始化红色边界
        for (int[] edge : redEdges) {
            int node1 = edge[0];
            int node2 = edge[1];
            adjacent[node1][0][node2] = 1;
        }
        //初始化蓝色边界
        for (int[] edge : blueEdges) {
            int node1 = edge[0];
            int node2 = edge[1];
            adjacent[node1][1][node2] = 1;
        }
        //最短距离
        int[][] dist = new int[2][n];  //分别记录最终通过蓝色或红色到达某个节点的最短距离
        for (int i = 0; i < 2; i++) {
            Arrays.fill(dist[i], INF);
        }
        dist[0][0] = 0;
        dist[1][0] = 0;
        ArrayDeque<int[]> queue = new ArrayDeque<>();  //记录从什么颜色到达此节点，一旦到达就确定了最短距离，记录至此队列
        queue.addLast(new int[]{0, 0});     //通过红色到达原点
        queue.addLast(new int[]{1, 0});     //通过蓝色到达原点
        while (!queue.isEmpty()) {
            int[] poll = queue.poll();   //队列中都是已经确认最短距离的节点
            int colorsPath = poll[0];
            int arriveNode = poll[1];
            for (int i = 0; i < n; i++) {
                //剪枝，通过另一个颜色无法到达相邻节点
                if (adjacent[arriveNode][1 - colorsPath][i] == 0) {  //从当前节点通过另一个颜色无法到达节点 i，不做考虑
                    continue;
                }
                //剪枝，通过另一个颜色到达相邻节点已经找最短距离
                if (dist[1 - colorsPath][i] != INF) {
                    continue;
                }

                //通过某个颜色首次到达相邻节点，因此通过相关颜色到达此节点的最短距离确定
                dist[1 - colorsPath][i] = dist[colorsPath][arriveNode] + 1;
                queue.addLast(new int[]{1 - colorsPath, i});
            }
        }
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = Math.min(dist[0][i], dist[1][i]);
            if (ans[i] == INF) {
                ans[i] = -1;
            }
        }
        return ans;
    }

}
