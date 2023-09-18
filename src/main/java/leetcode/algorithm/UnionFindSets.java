package leetcode.algorithm;


import java.util.*;

/**
 * 并查集
 */
public class UnionFindSets {

    /**
     * 1971. 寻找图中是否存在路径       基于 DAG的做法，参见 {@link DAG#validPath}
     */
    public boolean validPath(int n, int[][] edges, int source, int destination) {
        validPathUFS validPathUFS = new validPathUFS(n);
        for (int[] edge : edges) {
            validPathUFS.union(edge[0], edge[1]);
        }
        return validPathUFS.findSet(source) == validPathUFS.findSet(destination);
    }

    static class validPathUFS {
        int[] nodes;   //记录每个节点的根节点

        validPathUFS(int n) {  //MAKE-SET(x)
            this.nodes = new int[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = i;  //初始化时，每个结点都是以自己为单个集合（树），即每个节点的根节点初始化为自身
            }
        }

        /**
         * 将两个有关联的两个节点的集合合并为一个集合，相当于合并两个树
         */
        void union(int x, int y) {
            int xRoot = findSet(x);
            int yRoot = findSet(y);

            if (xRoot == yRoot) return;  //二者同根，故本就属于一个集合，无需合并
            nodes[xRoot] = yRoot;        //二者非同根，即二者不属于一个集合，需要合并
        }

        /**
         * 找到当前节点的根节点，同时进行路径压缩
         */
        int findSet(int tt) {   //注意，只有在该结点被查找的时候才会压缩
            //将当前节点递归路径中的所有节点，直接接到根节点下面, 拍平, 此时树深度为2
            if (nodes[tt] != tt) {
                nodes[tt] = findSet(nodes[tt]);   //递归和回溯
            }
            return nodes[tt];
        }
    }


    /**
     * 200. 岛屿数量
     */
    public int numIslands(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        numIslandsUFS ufs = new numIslandsUFS(grid);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    grid[i][j] = '0';  //感染
                    for (int[] dir : directions) {
                        int nextRow = i + dir[0];
                        int nextCol = j + dir[1];
                        if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                            if (grid[nextRow][nextCol] == '1') {
                                ufs.unionNode(i * n + j, nextRow * n + nextCol);
                            }
                        }
                    }

                }
            }
        }
        return ufs.getCount();
    }

    public static class numIslandsUFS {
        int[] nodes;     //记录各个节点的根节点，将各个节点二维坐标转换至一维坐标
        int count;       //记录不相交的集合数量

        //------------------------------------------------------------------------
        // 对所有元素进行初始化，每个元素的根节点初始化为自身，暂且认为每个节点都不相交
        //------------------------------------------------------------------------
        numIslandsUFS(char[][] grid) {
            int m = grid.length;
            int n = grid[0].length;
            this.nodes = new int[m * n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (grid[i][j] == '1') {
                        int index = i * n + j;
                        nodes[index] = index;      //每个节点的根节点初始化为自身
                        count++;                   //每个节点不相交
                    }
                }
            }
        }

        //-------------------------------------------------------------
        // 找到当前节点的根节点，并进行路径压缩，注意只会对搜索途径的节点进行路径压缩
        //-------------------------------------------------------------
        int findSet(int xx) {
            if (nodes[xx] != xx) {
                nodes[xx] = findSet(nodes[xx]);   //递归和回溯
            }
            return nodes[xx];
        }

        //---------------------------------------------------------
        // 合并两个节点对应集合，相当于合并两个树，因此总集合数减少
        //---------------------------------------------------------
        void unionNode(int xx, int yy) {
            int xRoot = findSet(xx);
            int yRoot = findSet(yy);

            if (xRoot == yRoot) return;  //两个集合同根，无需合并
            nodes[xRoot] = yRoot;  //合并两个集合
            count--;               //集合数减少
        }

        //----------------------------------
        // 获取当前不相交的集合个数
        //----------------------------------
        int getCount() {
            return count;
        }
    }


    /**
     * 1319. 连通网络的操作次数
     */
    public int makeConnected(int n, int[][] connections) {
        //当前的线数小于最少需要的线数，无法通过插拔来完成所有网络的连通
        if (connections.length < n - 1) {
            return -1;
        }
        makeConnectedUFS ufs = new makeConnectedUFS(n);
        for (int[] edge : connections) {
            ufs.union(edge[0], edge[1]);
        }
        //总集合数减去一个目标的连通域，剩下的集合均需要通过插拔来与目标连通域互联互通
        return ufs.getSetNums() - 1;  //剩下的集合内部可能只有一个元素，也可能有多个元素
    }

    static class makeConnectedUFS {
        int[] nodes;
        int setNums;

        makeConnectedUFS(int n) {
            this.setNums = n;
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
            setNums--;        //合并后，总集合数减少
        }

        private int getSetNums() {
            return setNums;
        }

    }


    /**
     * 547. 省份数量
     */
    public int findCircleNum(int[][] grid) {   //深度优先搜索算法
        int n = grid.length;
        findCircleNumUFS ufs = new findCircleNumUFS(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {   //只处理右上三角
                if (grid[i][j] == 1) {
                    ufs.union(i, j);
                }
            }
        }
        return ufs.getSetNums();
    }

    static class findCircleNumUFS {
        private int[] nodes;
        private int setNums;

        findCircleNumUFS(int n) {
            this.nodes = new int[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = i;
            }
            setNums = n;
        }

        private int findSet(int xx) {
            if (nodes[xx] != xx) {   //路径压缩
                nodes[xx] = findSet(nodes[xx]);
            }
            return nodes[xx];
        }

        private void union(int xx, int yy) {
            int xRoot = findSet(xx);
            int yRoot = findSet(yy);
            if (xRoot == yRoot) return;
            nodes[xRoot] = yRoot;
            setNums--;
        }

        private int getSetNums() {
            return setNums;
        }
    }


    /**
     * 128. 最长连续序列
     */
    public int longestConsecutive(int[] nums) {
        int n = nums.length;
        //------------------------------------------------
        // 索引作为节点标识，索引值作为节点间的关联关系
        //------------------------------------------------
        HashMap<Integer, Integer> hTable = new HashMap<>();
        longestConsecutiveUFS ufs = new longestConsecutiveUFS(n);
        for (int i = 0; i < n; i++) {
            if (hTable.containsKey(nums[i])) {
                continue;  //去重
            }
            if (hTable.containsKey(nums[i] - 1)) {
                ufs.union(hTable.get(nums[i] - 1), i);
            }
            if (hTable.containsKey(nums[i] + 1)) {
                ufs.union(hTable.get(nums[i] + 1), i);
            }
            hTable.put(nums[i], i);
        }
        return ufs.getMaxSize();
    }

    private static class longestConsecutiveUFS {
        int[] nodes;   //记录各个节点的根节点
        int[] sizes;   //记录各个根节点对应集合的元素个数

        longestConsecutiveUFS(int n) {
            this.nodes = new int[n];
            this.sizes = new int[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = i;
                sizes[i] = 1;
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
            //-----------------------------------------------------
            // 此处记录的是各个集合中节点的个数，而非各个集合的最大深度
            //-----------------------------------------------------
            sizes[yRoot] += sizes[xRoot];
        }

        private int getMaxSize() {
            int max = 0;
            for (int i = 0; i < nodes.length; i++) {
                if (i == nodes[i]) {  //根节点
                    max = Math.max(max, sizes[i]);
                }
            }
            return max;
        }
    }


    /**
     * 130. 被围绕的区域
     */
    public void solve(char[][] board) {  //并查集
        int m = board.length;
        int n = board[0].length;
        int dummyNode = m * n;  //虚拟节点，用于归拢目标节点
        solveUFS ufs = new solveUFS(m * n + 1);
        int[][] directions = {{1, 0}, {-1, 0}, {0, -1}, {0, 1}};
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                //只处理 'O' 节点
                if (board[i][j] == 'O') {
                    if (i == 0 || j == 0 || i == m - 1 || j == n - 1) {
                        ufs.union(i * n + j, dummyNode);  //将边界上的 'O' 节点"直接"归并到虚拟节点
                    } else {
                        for (int[] dir : directions) {
                            int nextRow = i + dir[0];
                            int nextCol = j + dir[1];
                            if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                                if (board[nextRow][nextCol] == 'O') {
                                    ufs.union(i * n + j, nextRow * n + nextCol);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'O' && ufs.findSet(i * n + j) != ufs.findSet(dummyNode)) {
                    board[i][j] = 'X';
                }
            }
        }
    }

    static class solveUFS {
        int[] nodes;

        solveUFS(int n) {
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
     * 1020. 飞地的数量
     */
    public int numEnclaves(int[][] grid) {
        int ans = 0;
        int m = grid.length;
        int n = grid[0].length;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int dummyNode = m * n;  //归并起来的可能是不相邻的两部分集合
        numEnclavesUFS ufs = new numEnclavesUFS(m * n + 1);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {
                        ufs.union(i * n + j, dummyNode);
                    } else {
                        for (int[] dir : directions) {
                            int nextRow = i + dir[0];
                            int nextCol = j + dir[1];
                            if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                                if (grid[nextRow][nextCol] == 1) {
                                    ufs.union(i * n + j, nextRow * n + nextCol);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1 && ufs.findSet(i * n + j) != ufs.findSet(dummyNode)) {
                    ans++;
                }
            }
        }
        return ans;
    }

    static class numEnclavesUFS {
        int[] nodes;

        numEnclavesUFS(int n) {
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
     * 695. 岛屿的最大面积
     */
    public int maxAreaOfIsland(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] directions = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
        maxAreaOfIslandUFS ufs = new maxAreaOfIslandUFS(grid, m * n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    for (int[] dir : directions) {
                        int nextRow = i + dir[0];
                        int nextCol = j + dir[1];
                        if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                            if (grid[nextRow][nextCol] == 1) {
                                ufs.union(i * n + j, nextRow * n + nextCol);
                            }
                        }
                    }
                }
            }
        }
        return ufs.getMaxSize();
    }

    static class maxAreaOfIslandUFS {
        int[] nodes;
        int[] sizes;

        maxAreaOfIslandUFS(int[][] grid, int n) {
            this.nodes = new int[n];
            this.sizes = new int[n];
            for (int i = 0; i < n; i++) {
                int row = i / grid[0].length;
                int col = i % grid[0].length;
                if (grid[row][col] == 1) {
                    nodes[i] = i;
                    sizes[i] = 1;
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
            sizes[yRoot] += sizes[xRoot];
        }

        private int getMaxSize() {
            int max = 0;
            for (int i = 0; i < sizes.length; i++) {
                if (nodes[i] == i) {
                    max = Math.max(max, sizes[i]);
                }
            }
            return max;
        }
    }


    /**
     * 2316. 统计无向图中无法互相到达点对数
     */
    public long countPairs(int n, int[][] edges) {
        countPairsUFS ufs = new countPairsUFS(n);
        for (int[] edge : edges) {
            ufs.union(edge[0], edge[1]);
        }
        return ufs.getAns();
    }

    static class countPairsUFS {
        int[] nodes;
        long[] sizes;

        countPairsUFS(int n) {
            this.nodes = new int[n];
            this.sizes = new long[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = i;
                sizes[i] = 1;
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
            sizes[yRoot] += sizes[xRoot];
        }

        private long getAns() {
            long ans = 0;
            long sum = 0;
            for (int i = 0; i < sizes.length; i++) { //前缀和，不能基于两个 for 循环来做，会超时，如案例 100000 []
                if (nodes[i] != i) continue;
                ans += sum * sizes[i];
                sum += sizes[i];
            }
            return ans;
        }
    }


    /**
     * 721. 账户合并
     */
    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        //---------------------------------------------------------------------------------------
        // 初始化时认为各个账户为独立的账户，每个账户有独立账户编号，后期基于账户中的邮箱通过并查集来合并账户
        //---------------------------------------------------------------------------------------
        int n = accounts.size();
        accountsMergeUFS ufs = new accountsMergeUFS(n);
        //记录各个邮箱对应的账户编号
        HashMap<String, Integer> emailAndID = new HashMap<>();   //通过账户编号可以基于并查集合并账户，也可获取账户的名称
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < accounts.get(i).size(); j++) {
                String emailName = accounts.get(i).get(j);
                if (emailAndID.containsKey(emailName)) {     //通过相同的邮箱，合并两个账户
                    ufs.union(emailAndID.get(emailName), i);
                } else {
                    emailAndID.put(emailName, i);            //记录邮箱名和账户编号
                }
            }
        }
        //--------------------------------------------------------------------------------------------------------------
        // 执行至此，已经完成了基于邮箱将一个人的多个账户实现了合并的操作，每个用户构成了一个连通块
        // 值得注意的是，当前连通块的信息已经存储并查集中了，一个连通块中各个节点的账户编号可能是不一致，当前尚未统一，因此需要通过连通域中的根节点进行统一
        //--------------------------------------------------------------------------------------------------------------

        //合并账户后，记录一个账户下的所有邮箱，注意此处的账户编号为连通块集合中根节点的标号
        HashMap<Integer, List<String>> accountAndEmails = new HashMap<>();  //虽然一个集合中各个节点的账户编号不一致，但这些账户编号对应的账户名称一定一致
        for (Map.Entry<String, Integer> entry : emailAndID.entrySet()) {
            int rootAccountID = ufs.findSet(entry.getValue());   //此账户编号对应的根节点，其也是一个账户编号，其标识了整个集合和连通块
            List<String> accountEmails = accountAndEmails.getOrDefault(rootAccountID, new ArrayList<>());
            accountEmails.add(entry.getKey());
            accountAndEmails.put(rootAccountID, accountEmails);
        }
        //整理返回值
        List<List<String>> ans = new ArrayList<>();
        for (Map.Entry<Integer, List<String>> entry : accountAndEmails.entrySet()) {
            //账户名
            String accountName = accounts.get(entry.getKey()).get(0);
            //邮箱
            List<String> emails = entry.getValue();
            Collections.sort(emails);
            //拼接返回值
            ArrayList<String> accountInfo = new ArrayList<>();
            accountInfo.add(accountName);
            accountInfo.addAll(emails);

            ans.add(accountInfo);
        }
        return ans;
    }

    static class accountsMergeUFS {
        int[] nodes;

        accountsMergeUFS(int n) {
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
     * 684. 冗余连接
     */
    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length + 1;
        connectionUFS ufs = new connectionUFS(n);
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            //-----------------------------------------------
            // 关键在于判断当前两个节点是否所属一个集合
            //-----------------------------------------------
            if (ufs.connect(node1, node2)) {  //两个节点已经所属一个集合，构成当前边会导致闭环，直接返回
                return edge;
            } else {
                ufs.union(node1, node2);      //两个节点为不同集合，合并到一个集合
            }
        }
        return edges[0];
    }

    static class connectionUFS {
        int[] nodes;

        connectionUFS(int n) {
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

        private boolean connect(int xx, int yy) {
            return findSet(xx) == findSet(yy);
        }

    }


    /**
     * 685. 冗余连接 II
     */
    public int[] findRedundantDirectedConnection(int[][] edges) {
        int n = edges.length;
        int[] errorEdge = {};
        int[] inDegree = new int[n + 1];
        for (int[] edge : edges) {
            inDegree[edge[1]]++;
            if (inDegree[edge[1]] == 2) errorEdge = edge;
        }
        //--------------------------------------------------------------------------------------
        // 1、不存在入度为 2 的节点，则图中一定存在闭环，因此按照无向图来判断当前构成闭环的最后一条边
        //--------------------------------------------------------------------------------------
        if (errorEdge.length == 0) {
            findRedundantDirectedConnectionUFS ufs = new findRedundantDirectedConnectionUFS(n + 1);
            for (int[] edge : edges) {
                if (ufs.connect(edge[0], edge[1])) { //闭环
                    return edge;
                }
                ufs.union(edge[0], edge[1]);
            }
        }

        //---------------------------------------------------------------------------------------
        // 2、存在入度为 2 的节点，则连接到这个点上的两个边中，一定有一条会构成有向的闭环
        //---------------------------------------------------------------------------------------
        findRedundantDirectedConnectionUFS ufs = new findRedundantDirectedConnectionUFS(n + 1);
        for (int[] edge : edges) {
            if (Arrays.equals(edge, errorEdge)) {  //1、首先忽略导致节点入度为 2 的其中一条边
                continue;
            }
            if (ufs.connect(edge[0], edge[1])) {   //不考虑入度为 2 的其中一条边后，仍闭环，则说明当前不考虑的边为正常的
                for (int[] ans : edges) {
                    if (ans[1] == errorEdge[1]) {
                        return ans;
                    }
                }
            }
            ufs.union(edge[0], edge[1]);
        }

        //如果不考虑当前这个有疑问的边后，不会构成闭环，则说明当前有疑问的边是构成闭环的边，应该剔除
        return errorEdge;
    }

    static class findRedundantDirectedConnectionUFS {
        int[] nodes;

        findRedundantDirectedConnectionUFS(int n) {
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

        private boolean connect(int xx, int yy) {
            return findSet(xx) == findSet(yy);
        }

    }


    /**
     * 1559. 二维网格图中探测环
     */
    public boolean containsCycle(char[][] grid) {  //存在闭环，故推荐使用并查集
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
     * 1559. 二维网格图中探测环
     */
    public boolean containsCycle01(char[][] grid) {  //存在闭环，故推荐使用深度优先搜索
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
     * 1267. 统计参与通信的服务器
     */
    public int countServers(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        HashMap<Integer, Integer> rows = new HashMap<>();
        HashMap<Integer, Integer> cols = new HashMap<>();
        //--------------------------------------------------
        // 巧妙，按照行和列进行编号，作为并查集的节点
        //--------------------------------------------------
        countServersUFS ufs = new countServersUFS(grid);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {   //遇到任意一个服务器，则合并同行同列对应的集合
                    if (rows.containsKey(i)) {
                        ufs.union(i * n + j, rows.get(i));
                    }
                    if (cols.containsKey(j)) {
                        ufs.union(i * n + j, cols.get(j));
                    }
                    rows.put(i, i * n + j);
                    cols.put(j, i * n + j);
                }
            }
        }
        return ufs.getNums();
    }

    static class countServersUFS {
        int[] nodes;
        int[] sizes;

        countServersUFS(int[][] grid) {
            int m = grid.length;
            int n = grid[0].length;
            this.nodes = new int[m * n];
            this.sizes = new int[m * n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (grid[i][j] == 1) {   //服务器节点进行赋值
                        int nodeIndex = i * n + j;
                        nodes[nodeIndex] = nodeIndex;
                        sizes[nodeIndex] = 1;
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
            sizes[yRoot] += sizes[xRoot];
        }

        private int getNums() {
            int ans = 0;
            for (int i = 0; i < sizes.length; i++) {
                //汇总集合大小为 1 的则说明只有一台服务器，不可互联互通
                if (nodes[i] == i && sizes[i] >= 2) {  //汇总集合大小大于 1 的集合中的所有元素的个数
                    ans += sizes[i];  //注意，一定判断根节点
                }
            }
            return ans;
        }

    }

    public int countServers01(int[][] grid) {  //模拟
        int m = grid.length;
        int n = grid[0].length;
        int[] count_rows = new int[m];
        int[] count_cols = new int[n];
        //---------------------------
        // 统计各行各列中服务器的个数
        //---------------------------
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    count_rows[i]++;
                    count_cols[j]++;
                }
            }
        }
        int ans = 0;
        //--------------------------------------------------------------------------------
        // 顺序遍历，如果当前节点的为服务器，同时其所在的列或所在的行中有其他服务器，则其满足条件
        //--------------------------------------------------------------------------------
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1 && (count_rows[i] > 1 || count_cols[j] > 1)) {
                    ans++;
                }
            }
        }
        return ans;
    }


    /**
     * 947. 移除最多的同行或同列石头
     */
    public int removeStones(int[][] stones) {
        int n = stones.length;
        removeStonesUFS ufs = new removeStonesUFS(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (stones[i][0] == stones[j][0] || stones[i][1] == stones[j][1]) {  //两个石头同行或同列
                    ufs.union(i, j);  //合并至一个集合
                }
            }
        }
        return n - ufs.count;  //n 个石头，构成了 m 个集合，需要移除的石头个数为 n - m
    }

    static class removeStonesUFS {
        int[] nodes;
        int count;

        removeStonesUFS(int n) {
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
     * 1697. 检查边长度限制的路径是否存在
     */
    public boolean[] distanceLimitedPathsExist(int n, int[][] edges, int[][] queries) {  //并查集 + 离线查询
        int m = queries.length;
        boolean[] ans = new boolean[m];
        Integer[] sortedIndex = new Integer[m];  //整数对象，才可排序
        for (int i = 0; i < m; i++) {
            sortedIndex[i] = i;
        }
        //1、对查询顺序进行排序，按照每个查询所限制的 limit 升序排序
        Arrays.sort(sortedIndex, (o1, o2) -> queries[o1][2] - queries[o2][2]);
        //2、对各个边进行排序，按照边长升序排序
        Arrays.sort(edges, (o1, o2) -> o1[2] - o2[2]);

        limitedPathsExistUFS ufs = new limitedPathsExistUFS(n);
        int edgeIndex = 0;
        for (int queryIndex : sortedIndex) {   //查询的顺序是按照每个查询限制的边长升序排序的
            //-------------------------------------------------------------------------------------------------------------
            // 将所有边长小于当前查询限制大小的边，添加至并查集中，本质是将此边的两个点在并查集中的集合进行合并，构建两个集合的连通性
            //-------------------------------------------------------------------------------------------------------------
            while (edgeIndex < edges.length && edges[edgeIndex][2] < queries[queryIndex][2]) {
                int node1 = edges[edgeIndex][0];
                int node2 = edges[edgeIndex][1];
                ufs.union(node1, node2);          //合并
                edgeIndex++;                      //指针右移，尝试添加下一个边，直至不满足当前查询的限制条件
            }

            //------------------------------------------------------------------------------------------------------------
            // 当前已经将所有小于当前查询限制大小的边，添加至并查集中，并构建了集合的连通性，进而判断当前查询的两个点是否具有连通性
            //------------------------------------------------------------------------------------------------------------
            int node1 = queries[queryIndex][0];
            int node2 = queries[queryIndex][1];
            ans[queryIndex] = ufs.findSet(node1) == ufs.findSet(node2);
        }
        return ans;
    }

    static class limitedPathsExistUFS {
        int[] nodes;

        limitedPathsExistUFS(int n) {
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
     * 2503. 矩阵查询可获得的最大分数
     */
    public int[] maxPoints(int[][] grid, int[] queries) {
        int m = grid.length;
        int n = grid[0].length;
        int k = queries.length;
        int[] ans = new int[k];
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        //-------------------------------------------------------------------------------------------------------------
        // 预处理二维矩阵中的元素值，按照元素值的大小升序排序，同时为了后续对此元素的四个方向进行搜索，故须记录当前元素的横纵坐标
        //-------------------------------------------------------------------------------------------------------------
        int[][] sortedGraph = new int[m * n][3];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                sortedGraph[i * n + j] = new int[]{grid[i][j], i, j};
            }
        }
        //1、按照各个位点值的大小，升序排序
        Arrays.sort(sortedGraph, (o1, o2) -> o1[0] - o2[0]);

        //--------------------------------------------------
        // 预处理查询顺序，按照各个查询限制的元素值进行升序排序
        //--------------------------------------------------
        Integer[] sortedIndex = new Integer[k];   //整型对象才可排序
        for (int i = 0; i < k; i++) {
            sortedIndex[i] = i;
        }
        //2、对查询顺序进行排序，按照每个查询的限制单元值大小升序排序
        Arrays.sort(sortedIndex, (o1, o2) -> queries[o1] - queries[o2]);

        int graphIndex = 0;
        maxPointsUFS ufs = new maxPointsUFS(m * n);
        for (int queryIndex : sortedIndex) {
            int currMax = queries[queryIndex];
            while (graphIndex < m * n && sortedGraph[graphIndex][0] < currMax) {
                int row = sortedGraph[graphIndex][1];
                int col = sortedGraph[graphIndex][2];
                for (int[] dir : directions) {
                    int nextRow = row + dir[0];
                    int nextCol = col + dir[1];
                    if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                        if (grid[nextRow][nextCol] < currMax) {
                            ufs.union(row * n + col, nextRow * n + nextCol);
                        }
                    }
                }
                graphIndex++;
            }
            if (grid[0][0] < currMax) {
                ans[queryIndex] = ufs.getNums(0);  //含有 [0,0] 位点的连通块大小
            }
        }
        return ans;
    }

    static class maxPointsUFS {
        int[] nodes;
        int[] sizes;

        maxPointsUFS(int n) {
            this.nodes = new int[n];
            this.sizes = new int[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = i;
                sizes[i] = 1;
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
            sizes[yRoot] += sizes[xRoot];
        }

        private int getNums(int xx) {
            return sizes[findSet(xx)];
        }

    }

    public int[] maxPoints01(int[][] grid, int[] queries) {
        //-----------------------------------------------------------------------------------
        // 基于离线查询 和 广度优先搜索，将问题转换为 海平面上升问题，其中广度优先搜索使用的是小根堆
        //-----------------------------------------------------------------------------------
        int m = grid.length;
        int n = grid[0].length;
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        int k = queries.length;
        int[] ans = new int[k];
        //-------------------------------------------------------
        // 离线查询，将查询顺序按照各个查询限制的水位高度，升序排序
        //-------------------------------------------------------
        Integer[] sortedIndex = new Integer[k];
        for (int i = 0; i < k; i++) {
            sortedIndex[i] = i;
        }
        Arrays.sort(sortedIndex, (o1, o2) -> queries[o1] - queries[o2]);

        //优先队列，存储当前可搜索的位点，并按照各个位点的水位高度升序排序
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[0] - o2[0]);
        sortedQueue.add(new int[]{grid[0][0], 0, 0});
        grid[0][0] = -1;  //将此位点标识为已经添加至队列

        //----------------------------------------------------------------------------------------------------------------------------------------
        // 此处使用广度优先搜索算法，但其特殊性在于其使用的队列为 小根堆，而非普通的先进先出队列
        // 使用小根堆的目的在于，配合有序的离线查询，"完整"的统计"当前可搜索到且满足当前查询限制条件"的位点个数
        //----------------------------------------------------------------------------------------------------------------------------------------
        int nums = 0;
        for (int queryIndex : sortedIndex) {
            int currWaterMark = queries[queryIndex];
            while (!sortedQueue.isEmpty() && sortedQueue.peek()[0] < currWaterMark) {
                nums++;      //在此位点海平面可上升
                int[] poll = sortedQueue.poll();
                int row = poll[1];
                int col = poll[2];
                for (int[] dir : directions) {
                    int nextRow = row + dir[0];
                    int nextCol = col + dir[1];
                    if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                        if (grid[nextRow][nextCol] != -1) {  //此位点未被搜索
                            //------------------------------------------------------------------------------------------------------------
                            // 无需判断此位点是否小于当前查询限制的水位线高度，其在优先队列中排序，并在 while 中判断是否满足当前查询限制的水位线高度
                            //------------------------------------------------------------------------------------------------------------
                            sortedQueue.add(new int[]{grid[nextRow][nextCol], nextRow, nextCol});
                            grid[nextRow][nextCol] = -1;
                        }
                    }
                }
            }
            ans[queryIndex] = nums;
        }
        return ans;
    }


    /**
     * 990. 等式方程的可满足性
     */
    public boolean equationsPossible(String[] equations) {
        equationsUFS ufs = new equationsUFS(26);
        //1、合并
        for (String eq : equations) {
            if (eq.charAt(1) == '=') {
                int node1 = eq.charAt(0) - 'a';
                int node2 = eq.charAt(3) - 'a';
                ufs.union(node1, node2);
            }
        }
        //2、校验闭环、冲突
        for (String eq : equations) {
            if (eq.charAt(1) == '!') {
                int node1 = eq.charAt(0) - 'a';
                int node2 = eq.charAt(3) - 'a';
                if (ufs.findSet(node1) == ufs.findSet(node2)) {
                    return false;
                }
            }
        }
        return true;
    }

    static class equationsUFS {
        int[] nodes;

        private equationsUFS(int n) {
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
            if (xRoot == yRoot) {
                return;
            }
            nodes[xRoot] = yRoot;   //不能写为 nodes[xx]
        }
    }


    /**
     * 959. 由斜杠划分区域
     */
    public int regionsBySlashes(String[] grid) {   //并查集
        int n = grid.length + 1;
        //构建虚拟网格，为每个节点进行编号，从而基于并查集来判断是否构成闭环
        regionsBySlashesUFS ufs = new regionsBySlashesUFS(n * n);
        //将网格四条边界合并为一个集合
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || j == 0 || i == n - 1 || j == n - 1) {
                    ufs.union(0, i * n + j);  //编号
                }
            }
        }
        int ans = 1;  //当前大网格
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length(); j++) {
                char ch = grid[i].charAt(j);
                //获取两个端点的编号
                int node1 = -1;
                int node2 = -1;
                if (ch == ' ') {
                    continue;
                }
                if (ch == '/') {
                    node1 = i * n + j + 1;
                    node2 = (i + 1) * n + j;
                }
                if (ch == '\\') {
                    node1 = i * n + j;
                    node2 = (i + 1) * n + j + 1;
                }
                //判断能否构成闭环
                if (ufs.findSet(node1) == ufs.findSet(node2)) {
                    ans++;
                } else {
                    ufs.union(node1, node2);
                }
            }
        }
        return ans;
    }

    static class regionsBySlashesUFS {
        int[] nodes;

        private regionsBySlashesUFS(int n) {
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

    public int regionsBySlashes01(String[] grid) {   //基于 3 * 3 矩阵转换为岛屿数量问题，使用深度优先搜索来做
        //------------------------------------------------------------------
        // 将 1 * 1 的方块转换为 3 * 3 的矩阵，基于对角线的位置，填充默认值
        // 注意至少使用 3 * 3 的矩阵来转换，也可使用 4 * 4 的矩阵，但不能使用 2 * 2 的矩阵，因为 其会丢失连通性（主要是斜向的连通性）
        //------------------------------------------------------------------
        int n = grid.length;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int[][] graph = new int[n * 3][n * 3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i].charAt(j) == '/') {
                    graph[3 * i][3 * j + 2] = 1;
                    graph[3 * i + 1][3 * j + 1] = 1;
                    graph[3 * i + 2][3 * j] = 1;
                } else if (grid[i].charAt(j) == '\\') {
                    graph[3 * i][3 * j] = 1;
                    graph[3 * i + 1][3 * j + 1] = 1;
                    graph[3 * i + 2][3 * j + 2] = 1;
                }
            }
        }
        int ans = 0;
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[0].length; j++) {
                if (graph[i][j] == 0) {
                    regionsBySlashesDFS(graph, directions, i, j);
                    ans++;
                }
            }
        }
        return ans;
    }

    private void regionsBySlashesDFS(int[][] graph, int[][] directions, int currRow, int currCol) {
        //递归终止条件一：越界
        if (currRow < 0 || currRow >= graph.length || currCol < 0 || currCol >= graph[0].length) {
            return;
        }
        //递归终止条件二：已搜索或障碍
        if (graph[currRow][currCol] == 1) {
            return;
        }

        graph[currRow][currCol] = 1;

        for (int[] dir : directions) {
            int nextRow = currRow + dir[0];
            int nextCol = currCol + dir[1];
            regionsBySlashesDFS(graph, directions, nextRow, nextCol);
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
     * 6432. 统计完全连通分量的数量
     */
    public int countCompleteComponents(int n, int[][] edges) {
        int[] inDegree = new int[n];
        ComponentsUFS ufs = new ComponentsUFS(n);
        for (int[] edge : edges) {
            inDegree[edge[0]]++;
            inDegree[edge[1]]++;
            ufs.union(edge[0], edge[1]);
        }
        HashSet<Integer> ans = new HashSet<>();
        for (int i = 0; i < nodes.length; i++) {
            if (i == nodes[i]) {  //根节点
                ans.add(i);
            }
        }
        for (int i = 0; i < n; i++) {
            int fatherNode = nodes[i];
            if (sizes[fatherNode] != inDegree[i] + 1) {
                ans.remove(fatherNode);
            }
        }
        return ans.size();
    }

    private int[] nodes;   //记录各个节点的根节点
    private int[] sizes;   //记录各个根节点对应集合的元素个数

    private class ComponentsUFS {
        ComponentsUFS(int n) {
            nodes = new int[n];
            sizes = new int[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = i;
                sizes[i] = 1;
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
            //-----------------------------------------------------
            // 此处记录的是各个集合中节点的个数，而非各个集合的最大深度
            //-----------------------------------------------------
            sizes[yRoot] += sizes[xRoot];
        }

    }


}
