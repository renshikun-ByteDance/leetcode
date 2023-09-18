package leetcode.algorithm;

import java.util.*;


/**
 * 拓扑结构、有向（无向）的无环（有换）图
 */
public class DAG {

    /**
     * 207. 课程表
     * https://leetcode.cn/problems/course-schedule/solution/bao-mu-shi-ti-jie-shou-ba-shou-da-tong-tuo-bu-pai-/
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {   //广度优先搜索 BFS + 有向图 DAG

        //--------------------------------------------------
        // 将问题转换为 是否存在有向闭环图，基于 inDegree判断
        //--------------------------------------------------

        //入度
        int[] inDegree = new int[numCourses];
        //邻接表，叶节点指向多个子节点
        HashMap<Integer, ArrayList<Integer>> adjacent = new HashMap<>();  //记录某个课程是哪些课程的依赖，即学完此门课程后可学习哪些课程
        //初始化
        for (int[] relate : prerequisites) {
            int current = relate[0];
            int last = relate[1];
            //更新当前课程的入度
            inDegree[current]++;
            if (!adjacent.containsKey(last)) {
                adjacent.put(last, new ArrayList<>());
            }
            //更新依赖
            adjacent.get(last).add(current);  //关键，value记录 Key 作为哪些课程的依赖
        }
        //BFS，将入度为 0 的课程添加至队列，即队列中的课程的入度为 0，没有依赖课程或者依赖课程已经被完成
        ArrayDeque<Integer> learnQueue = new ArrayDeque<>();
        for (int i = 0; i < inDegree.length; i++) {
            if (inDegree[i] == 0) {
                learnQueue.addLast(i);
            }
        }

        //3、取出一个节点，学习此课程，并更新其影响性
        while (!learnQueue.isEmpty()) {
            //3.1、学习此课程
            Integer learned = learnQueue.poll();
            //3.2、更新此课程学习完的影响性，主要影响其他课程的入度
            //当前课程不会对任何课程有影响，即其不是任何课程的依赖
            if (!adjacent.containsKey(learned)) {
                continue;
            }
            //受影响的课程
            ArrayList<Integer> affected = adjacent.get(learned);
            //更新这些受影响课程的入度，因为当前课程已经学完，不再作为其余课程的依赖
            for (int aff : affected) {
                inDegree[aff]--;
                //3.3、寻找新的可学习的课程
                if (inDegree[aff] == 0) {   //如果更新入度后，其入度为 0 ，即其已经没有依赖的课程，当前可学习，故添加至队列
                    learnQueue.add(aff);
                }
            }
        }

        //4、校验各个课程的入度
        for (int i = 0; i < inDegree.length; i++) {
            if (inDegree[i] != 0) {   //如果不为 0 ，则说明当前课程一直没有被选择，即不满足条件
                return false;
            }
        }
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //对于图中的任意一个节点，它在搜索的过程中有三种状态：
    //   1、未搜索：我们还没有搜索到这个节点；
    //   2、搜索中：我们搜索过这个节点，但还没有回溯到该节点，即该节点还没有入栈，还有相邻的节点没有搜索完成；
    //   3、已完成：我们搜索过并且回溯过这个节点，即该节点已经入栈，并且所有该节点的相邻节点都出现在栈的更底部的位置，满足拓扑排序的要求。
    //-----------------------------------------------------------------------------------------------------------------------

    public boolean canFinish00(int numCourses, int[][] prerequisites) {
        int[] visited = new int[numCourses];
        List<List<Integer>> adjacent = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            adjacent.add(new ArrayList<>());
        }
        for (int[] relate : prerequisites) {
            int childNode = relate[0];
            int fatherNode = relate[1];
            adjacent.get(fatherNode).add(childNode);
        }
        for (int i = 0; i < numCourses; i++) {
            if (visited[i] == 0) {
                if (!canFinishDfs00(adjacent, visited, i)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean canFinishDfs00(List<List<Integer>> adjacent, int[] visited, int fatherNode) {
        //1、递归终止条件一：找到闭环
        if (visited[fatherNode] == 1) {
            return false;
        }

        visited[fatherNode] = 1;   //将当前节点标记为正在搜索中
        List<Integer> affectNode = adjacent.get(fatherNode);  //获取其影响的子节点
        //横向枚举搜索
        for (int affect : affectNode) {
            //剪枝
            if (visited[affect] == 2) {  //此节点已搜索完毕，无异常，但不能直接返回 true，因为我们找到是任意一个不满足情况的路径
                continue;
            }
            if (!canFinishDfs00(adjacent, visited, affect)) {
                return false;
            }
        }
        visited[fatherNode] = 2;  //完成搜索
        return true;
    }


    private boolean isValid = true;

    public boolean canFinish01(int numCourses, int[][] prerequisites) {   //深度优先搜索 DFS + 有向图 DAG
        int[] visited = new int[numCourses];
        //邻接表，叶节点指向子节点
        List<List<Integer>> adjacent = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            adjacent.add(new ArrayList<>());
        }
        for (int[] relate : prerequisites) {
            int child = relate[0];
            int father = relate[1];
            adjacent.get(father).add(child);
        }
        for (int i = 0; i < numCourses && isValid; i++) {
            if (visited[i] == 0) {   //没有被访问过
                canFinishDfs(adjacent, visited, i);
            }
        }
        return isValid;
    }

    private void canFinishDfs(List<List<Integer>> adjacent, int[] visited, int father) {
        visited[father] = 1;

        //1、横向枚举搜索
        for (int child : adjacent.get(father)) {  //获取其子节点
            //2、纵向递归搜索
            if (visited[child] == 0) {  //如果此节点未被访问过
                canFinishDfs(adjacent, visited, child);
                if (!isValid) {
                    return;
                }
            } else if (visited[child] == 1) {  //纵向递归终止条件：构成有向闭环，即此节点已经被访问过
                isValid = false;
                return;
            }
        }
        visited[father] = 2;   //没有子节点、或者子节点搜索结束，则标识 Father 节点搜索完成，即此课程不会构成依赖闭环，可有序学习
    }

    public boolean canFinish06(int numCourses, int[][] prerequisites) {
        int[] visited = new int[numCourses];
        //邻接表，叶节点指向子节点
        HashMap<Integer, ArrayList<Integer>> adjacent = new HashMap<>();
        for (int[] relate : prerequisites) {
            int child = relate[0];
            int father = relate[1];
            if (!adjacent.containsKey(father)) {
                adjacent.put(father, new ArrayList<>());
            }
            adjacent.get(father).add(child);
        }
        for (int i = 0; i < numCourses; i++) {
            if (visited[i] == 0) {
                if (!canFinishDfs06(adjacent, visited, i))   //只要有一个错误的（闭环），就错误
                    return false;
            }
        }
        return true;
    }

    private boolean canFinishDfs06(HashMap<Integer, ArrayList<Integer>> adjacent, int[] visited, int father) {
        //当前节点开始搜索
        visited[father] = 1;
        //1、如果有子节点，继续搜索
        if (adjacent.containsKey(father)) {
            //1、横向枚举搜索
            for (int child : adjacent.get(father)) {   //逐一搜索子节点
                if (visited[child] == 0) {
                    //2、深度优先搜索
                    if (!canFinishDfs06(adjacent, visited, child)) {
                        return false;
                    }
                } else if (visited[child] == 1) {    //关键：一旦构成闭环，则结束
                    return false;
                }
            }
        }
        //当前节点搜索完成
        visited[father] = 2;
        return true;  //无闭环，有闭环上面直接就返回了
    }

    private boolean canFinishDfs006(HashMap<Integer, ArrayList<Integer>> adjacent, int[] visited, int father) {
        //当前节点开始搜索
        visited[father] = 1;
        //1、如果有子节点，继续搜索
        if (adjacent.containsKey(father)) {
            //1、横向枚举搜索
            for (int child : adjacent.get(father)) {   //逐一搜索子节点
                if (visited[child] == 1) {    //关键：一旦构成闭环，则结束
                    return false;
                }

                //2、深度优先搜索
                if (!canFinishDfs06(adjacent, visited, child)) {
                    return false;
                }
            }
        }
        //当前节点搜索完成
        visited[father] = 2;
        return true;  //无闭环，有闭环上面直接就返回了
    }

    /**
     * 210. 课程表 II
     */
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        //学习课程的顺序
        ArrayList<Integer> scheduler = new ArrayList<>();
        //入度
        int[] inDegree = new int[numCourses];   //基于入度来衡量当前课程是否可被学习
        //邻接表
        HashMap<Integer, ArrayList<Integer>> fatherToChildren = new HashMap<>();
        for (int[] dependency : prerequisites) {
            int father = dependency[1];
            int child = dependency[0];
            if (!fatherToChildren.containsKey(father)) {
                fatherToChildren.put(father, new ArrayList<>());
            }
            fatherToChildren.get(father).add(child);   //父节点视角，维护父子关系
            inDegree[child]++;                         //子节点视角，仅影响 child 的入度
        }
        Deque<Integer> learnedQueue = new ArrayDeque<>();
        //基于入度，及其依赖的个数来确定哪些课程可以开始学习
        for (int i = 0; i < inDegree.length; i++) {
            if (inDegree[i] == 0) {
                learnedQueue.addLast(i);
                scheduler.add(i);
            }
        }
        //递归遍历
        while (!learnedQueue.isEmpty()) {  //学习对应可学习的课程，然后记录其影响性
            Integer father = learnedQueue.pollLast();
            //1、无子节点，不会对任何课程产生影响
            if (!fatherToChildren.containsKey(father))
                continue;
            //2、有子节点，逐一记录对子节点的影响性
            ArrayList<Integer> children = fatherToChildren.get(father);
            for (int child : children) {
                inDegree[child]--;
                if (inDegree[child] == 0) {
                    learnedQueue.addLast(child);
                    scheduler.add(child);
                }
            }
        }
        if (scheduler.size() == numCourses) {
            return scheduler.stream().mapToInt(Integer::intValue).toArray();
        }
        return new int[]{};
    }



    /**
     * 2050. 并行课程 III
     */
    public int minimumTime(int n, int[][] relations, int[] time) {
        ArrayList<ArrayList<Integer>> adjacent = new ArrayList<>();
        int[] dp = new int[n];
        int[] inDegree = new int[n];
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            adjacent.add(new ArrayList<>());
        }
        for (int[] edge : relations) {
            int node1 = edge[0] - 1;
            int node2 = edge[1] - 1;
            inDegree[node2]++;                //记录父节点的个数
            adjacent.get(node1).add(node2);   //记录子节点
        }
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.addLast(i);
                dp[i] = time[i];
            }
        }
        while (!queue.isEmpty()) {
            Integer currNode = queue.pollFirst();
            ArrayList<Integer> infects = adjacent.get(currNode);
            for (int nextNode : infects) {
                dp[nextNode] = Math.max(dp[nextNode], dp[currNode] + time[nextNode]);
                inDegree[nextNode]--;
                if (inDegree[nextNode] == 0) {
                    queue.addLast(nextNode);
                }
            }
        }
        int max = 0;
        for (int i = 0; i < n; i++) {
            max = Math.max(max, dp[i]);
        }
        return max;
    }



    /**
     * 802. 找到最终的安全状态
     */
    public List<Integer> eventualSafeNodes(int[][] graph) {   //基于反向图来解决
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

    public List<Integer> eventualSafeNodes01(int[][] graph) {  //正向
        ArrayList<Integer> ans = new ArrayList<>();
        int[] visited = new int[graph.length];
        for (int i = 0; i < graph.length; i++) {
            if (eventualSafeNodesDfs(graph, visited, i)) {
                ans.add(i);
            }
        }
        return ans;
    }


    private boolean eventualSafeNodesDfs(int[][] graph, int[] visited, int currentNode) {  //目的为寻找闭环
        //迭代终止条件一：闭环
        if (visited[currentNode] == 1) {
            //--------------------------------
            // 1、此节点可能是从上个非安全节点遗留的，即非安全节点对应的闭环路径中的节点，均为非安全节点
            // 2、此节点可能是从单个节点搜索路径中，途径过同时仍处于搜索中的节点
            //--------------------------------
            return false;
        }

        visited[currentNode] = 1;  //正在搜索

        for (int nextNode : graph[currentNode]) {  //目的为寻找闭环
            //剪枝一：终端节点
            if (graph[nextNode].length == 0) {
                continue;
            }
            //剪枝二：安全节点
            if (visited[nextNode] == 2) {
                continue;
            }

            //寻找闭环
            if (!eventualSafeNodesDfs(graph, visited, nextNode)) {
                return false;
            }

        }

        visited[currentNode] = 2;  //当前节点搜索完毕，为安全节点
        return true;
    }

    private boolean eventualSafeNodesDfs01(int[][] graph, int[] visited, int currentIndex) {
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
            if (!eventualSafeNodesDfs01(graph, visited, node)) {   //只要存在，即为不满足情况
                return false;
            }
        }

        //搜索完毕，当前节点为安全节点
        visited[currentIndex] = 2;  //如果再有其他节点搜索至此节点，无需再次搜索
        return true;  //不存在反例，当前节点的所有连通节点均通过"终端节点"
    }


    /**
     * 851. 喧闹和富有
     */
    public int[] loudAndRich(int[][] richer, int[] quiet) {  //基于 DFS
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

    private void loudAndRichDfs
            (HashMap<Integer, ArrayList<Integer>> adjacent, HashMap<Integer, HashSet<Integer>> adjacentDepth,
             int[] visited, int currentNode) {
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

    //---------------------------------------------------------
    // 上下两种写法均是基于全局邻接表来做的，差异在于 深度优先搜索的逻辑略有不同
    // 以及全局邻接表赋值的逻辑不同、以及全局邻接表中是否包含自身不同。判断自身是否是其安静节点的逻辑不同
    //---------------------------------------------------------

    public int[] loudAndRich10(int[][] richer, int[] quiet) {
        int n = quiet.length;
        int[] ans = new int[n];
        HashMap<Integer, ArrayList<Integer>> adjacent = new HashMap<>();     //叶子节点指向叶节点
        HashMap<Integer, HashSet<Integer>> adjacentDepth = new HashMap<>();
        for (int[] relate : richer) {
            int fatherNode = relate[0];
            int childNode = relate[1];
            if (!adjacent.containsKey(childNode)) {
                adjacent.put(childNode, new ArrayList<>());
            }
            adjacent.get(childNode).add(fatherNode);
        }

        int[] visited = new int[n];
        for (int i = 0; i < n; i++) {
            if (visited[i] == 0) {
                loudAndRichDfs(adjacent, adjacentDepth, visited, i);
            }
        }

        for (int i = 0; i < n; i++) {
            if (!adjacentDepth.containsKey(i)) {
                ans[i] = i;
                continue;
            }

            int targetNode = i;
            HashSet<Integer> affectNodes = adjacentDepth.get(i);
            for (int prevNode : affectNodes) {
                if (quiet[prevNode] < quiet[targetNode]) {
                    targetNode = prevNode;
                }
            }
            ans[i] = targetNode;
        }

        return ans;
    }

    private void loudAndRichDfs10(HashMap<Integer, ArrayList<Integer>> adjacent, HashMap<Integer, HashSet<Integer>> adjacentDepth, int[] visited, int currentNode) {
        //递归终止条件：已搜索
        if (visited[currentNode] == 1) {
            return;
        }
        //递归终止条件：越界
        if (!adjacent.containsKey(currentNode)) {
            return;
        }

        visited[currentNode] = 1;
        ArrayList<Integer> affectNodes = adjacent.get(currentNode);
        for (int prevNode : affectNodes) {

            loudAndRichDfs10(adjacent, adjacentDepth, visited, prevNode);

            if (!adjacentDepth.containsKey(currentNode)) {
                adjacentDepth.put(currentNode, new HashSet<>());
            }
            adjacentDepth.get(currentNode).add(prevNode);
            if (adjacentDepth.containsKey(prevNode)) {
                adjacentDepth.get(currentNode).addAll(adjacentDepth.get(prevNode));
            }
        }
    }


    public int[] loudAndRich01(int[][] richer, int[] quiet) {  //基于拓扑排序
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
                if (quiet[ans[fatherNode]] < quiet[ans[childNode]]) {  //从当前节点的邻接父节点和非邻接父节点（父节点的父节点，体现全局邻接）中，搜索安静值最小的节点
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


    //----------------------------------------------------------------------------
    // 本质此题并没有用到 拓扑排序/图论 的算法，而仅仅使用了深度优先搜索获取了 全局的依赖关系
    // 写法一：
    //     统一的深度优先搜索，来获取每个节点及其子节点列表
    // 写法二：
    //     统一的深度优先搜索，来获取每个节点及其父节点列表
    // 暴力超时写法：
    //     从待查询的节点向上纵向递归搜索，来判断当前两个节点是否存在父子关系
    //         时间复杂度高的原因：
    //             对于每一对节点，都要进行独立的深度优先搜索
    //----------------------------------------------------------------------------

    /**
     * 1462. 课程表 IV
     */
    public List<Boolean> checkIfPrerequisite(int numCourses, int[][] prerequisites, int[][] queries) {
        //基于一次统一的深度优先搜索，获取每个节点的"子节点"
        ArrayList<Boolean> ans = new ArrayList<>();
        //邻接表
        ArrayList<ArrayList<Integer>> adjacent = new ArrayList<>();  //父节点指向子节点，仅一层
        //深度邻接表，父节点指向子节点，所有层
        ArrayList<HashSet<Integer>> adjacentDepth = new ArrayList<>();    //核心： HashSet是关键，用 ArrayList 超出内存限制，是去重的目的吗？不理解
        for (int i = 0; i < numCourses; i++) {
            adjacent.add(new ArrayList<>());
            adjacentDepth.add(new HashSet<>());
        }
        //初始化邻接表
        for (int[] dependency : prerequisites) {
            adjacent.get(dependency[0]).add(dependency[1]);  //父节点指向子节点
        }
        int[] visited = new int[numCourses];
        //基于邻接表，通过 DFS初始化全局邻接表
        for (int i = 0; i < numCourses; i++) {
            //---------------------------------------------------
            // 关键：并不是从单独一个课程开始深度优先搜索，而是从多个不同的课程开始，因为有些课程间不存在依赖关系
            //---------------------------------------------------

            if (visited[i] == 1)  //已搜索
                continue;
            checkIfPrerequisiteDfs(adjacent, adjacentDepth, visited, i);
        }
        //基于全局深度邻接表，逐一判断待查询的节点对是否是父子关系
        for (int[] query : queries) {
            ans.add(adjacentDepth.get(query[0]).contains(query[1]));
        }
        return ans;
    }

    private void checkIfPrerequisiteDfs
            (ArrayList<ArrayList<Integer>> adjacent, ArrayList<HashSet<Integer>> adjacentDepth, int[] visited,
             int fatherIndex) {
        //将当前节点标识为已遍历，也防止有向环的出现
        visited[fatherIndex] = 1;
        ArrayList<Integer> childNodes = adjacent.get(fatherIndex);
        //基于邻接表，横向枚举遍历
        for (int childIndex : childNodes) {
            if (visited[childIndex] == 0) {   //未被搜索
                checkIfPrerequisiteDfs(adjacent, adjacentDepth, visited, childIndex);  //继续向下搜索
            }
            //-------------------------------------------------------------------------------
            // 无论此子节点是否已经被搜索，下面都要在当前的全局邻接表中添加此父节点和子节点深度邻接信息
            //-------------------------------------------------------------------------------
            //1、将邻接表的父子关系添加到深度邻接表中
            adjacentDepth.get(fatherIndex).add(childIndex);
            //2、将子节点的深度邻接表的关系添加到当前的子节点中
            adjacentDepth.get(fatherIndex).addAll(adjacentDepth.get(childIndex));
        }
    }

    //上下 DFS 逻辑略有不同
    private void checkIfPrerequisiteDfs11
    (ArrayList<ArrayList<Integer>> adjacent, ArrayList<HashSet<Integer>> adjacentDepth, int[] visited,
     int fatherNode) {
        //迭代终止条件
        if (visited[fatherNode] == 1) {
            return;
        }

        //置为已搜索
        visited[fatherNode] = 1;
        ArrayList<Integer> childNodes = adjacent.get(fatherNode);
        //将当前节点的邻接表添加至深度邻接表中
        adjacentDepth.get(fatherNode).addAll(childNodes);

        //横向枚举搜索
        for (int childIndex : childNodes) {
            checkIfPrerequisiteDfs(adjacent, adjacentDepth, visited, childIndex);

            //-------------------------------------------------------------
            // 递归至最深处，在逐层回溯的过程中，依次向上添加子节点的深度邻接表
            //-------------------------------------------------------------

            //将"子节点"的深度邻接表信息添加至"父节点"深度邻接表中
            adjacentDepth.get(fatherNode).addAll(adjacentDepth.get(childIndex));
        }
    }


    //---------------------------------------------------------------------
    // 写法二：基于一次统一的深度优先搜索，获取每个节点的"父节点"
    //---------------------------------------------------------------------
    public List<Boolean> checkIfPrerequisite01(int numCourses, int[][] prerequisites, int[][] queries) {
        List<Boolean> ans = new ArrayList<>();
        //邻接表，子节点指向父节点，仅记录相邻层
        ArrayList<ArrayList<Integer>> adjacent = new ArrayList<>();
        //深度邻接表，子节点指向父节点，记录全局的父子（依赖）关系
        ArrayList<HashSet<Integer>> adjacentDepth = new ArrayList<>();
        //初始化
        for (int i = 0; i < numCourses; i++) {
            adjacent.add(new ArrayList<>());
            adjacentDepth.add(new HashSet<>());
        }
        for (int[] dependency : prerequisites) {
            adjacent.get(dependency[1]).add(dependency[0]);  //子节点指向父节点
        }
        int[] visited = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            if (visited[i] == 1)
                continue;
            checkIfPrerequisiteDfs01(adjacent, adjacentDepth, visited, i);
        }
        for (int[] relate : queries) {
            ans.add(adjacentDepth.get(relate[1]).contains(relate[0]));
        }
        return ans;
    }

    private void checkIfPrerequisiteDfs01
            (ArrayList<ArrayList<Integer>> adjacent, ArrayList<HashSet<Integer>> adjacentDepth, int[] visited,
             int childIndex) {
        visited[childIndex] = 1;
        ArrayList<Integer> fatherNodes = adjacent.get(childIndex);
        //1、横向枚举搜索
        for (Integer fatherIndex : fatherNodes) {
            //---------------------------------------------------------
            // 访问过，意味着此节点已经被遍历过，此节点的父节点关系已经被获取，无需再次访问
            //---------------------------------------------------------
            if (visited[fatherIndex] == 0) {  //没有访问过
                //2、纵向递归搜索
                checkIfPrerequisiteDfs01(adjacent, adjacentDepth, visited, fatherIndex);
            }
            adjacentDepth.get(childIndex).add(fatherIndex);
            adjacentDepth.get(childIndex).addAll(adjacentDepth.get(fatherIndex));   // HashSet 去重
        }
    }


    //---------------------------------------------------------------------
    // 暴力超时写法：
    //     从待查询的节点向上纵向递归搜索，来判断当前两个节点是否存在父子关系
    //         时间复杂度高的原因：
    //             对于每一对节点，都要进行独立的深度优先搜索
    //---------------------------------------------------------------------
    private boolean isFather;

    public List<Boolean> checkIfPrerequisite00(int numCourses, int[][] prerequisites, int[][] queries) {   //DFS暴力搜索，超时
        List<Boolean> ans = new ArrayList<>();
        HashMap<Integer, ArrayList<Integer>> childToFather = new HashMap<>();
        for (int[] dependency : prerequisites) {
            int child = dependency[0];
            int father = dependency[1];
            if (!childToFather.containsKey(child)) {
                childToFather.put(child, new ArrayList<>());
            }
            childToFather.get(child).add(father);
        }
        for (int[] query : queries) {
            isFather = false;
            int start = query[0];
            int end = query[1];
            checkIfPrerequisiteDfs00(childToFather, start, end);
            ans.add(isFather);
        }
        return ans;
    }

    private void checkIfPrerequisiteDfs00(HashMap<Integer, ArrayList<Integer>> childToFather, int start,
                                          int end) {
        //终止条件一：找到目标节点
        if (isFather) {
            return;
        }
        //终止条件二：无可搜索的父节点
        if (!childToFather.containsKey(start)) {
            return;
        }
        ArrayList<Integer> fathers = childToFather.get(start);
        //终止条件三：找到对应的节点
        if (fathers.contains(end)) {
            isFather = true;
            return;
        }
        for (int i = 0; i < fathers.size(); i++) {
            checkIfPrerequisiteDfs00(childToFather, fathers.get(i), end);
            //剪枝
            if (isFather) break;
        }
    }

    /**
     * 1971. 寻找图中是否存在路径         基于 并查集的做法，参见 {@link UnionFindSets#validPath}
     */
    public boolean validPath(int n, int[][] edges, int source, int destination) {
        int[] visited = new int[n];
        //邻接表
        ArrayList<ArrayList<Integer>> adjacent = new ArrayList<>();
        //初始化
        for (int i = 0; i < n; i++) {
            adjacent.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            adjacent.get(node1).add(node2);
            adjacent.get(node2).add(node1);
        }
        return validPathDfs(adjacent, visited, source, destination);
    }

    private boolean validPathDfs(ArrayList<ArrayList<Integer>> adjacent, int[] visited, int currentNode, int targetNode) {
        if (currentNode == targetNode) {
            return true;
        }

        //标识为已搜索
        visited[currentNode] = 1;
        ArrayList<Integer> nextNodes = adjacent.get(currentNode);
        for (int nextNode : nextNodes) {  //横线枚举搜索
            //剪枝：已搜索
            if (visited[nextNode] == 1) {
                continue;
            }
            if (validPathDfs(adjacent, visited, nextNode, targetNode)) {  //纵向递归搜索
                return true;
            }
        }
        return false;  //运行到这里说明从此点无法走到 targetNode
    }

    //--------------------------------------------------------------------------
    // 下面的写法相较于上，在三色标记法的基础上，增加了记忆化搜索
    //--------------------------------------------------------------------------

    private boolean validPathDfs01(List<List<Integer>> adjacent, int[] visited, int currentNode, int targetNode) {
        //递归终止条件一；找到终点
        if (currentNode == targetNode) {
            return true;
        }

        visited[currentNode] = 1; //标记为正在搜索
        List<Integer> affectNodes = adjacent.get(currentNode);
        for (int nextNode : affectNodes) {   //横向枚举搜索
            //剪枝一：已搜索
            if (visited[nextNode] == 2) {    //已完成搜索的节点，但未找到终点，跳过
                continue;
            }
            //剪枝二：正在搜索
            if (visited[nextNode] == 1) {    //跳过
                continue;
            }

            if (validPathDfs01(adjacent, visited, nextNode, targetNode)) {
                return true;
            }
        }
        //搜索完成，未找到终点
        visited[currentNode] = 2;  //三色标记，记忆化搜索
        return false;
    }


    /**
     * 886. 可能的二分法
     */
    public boolean possibleBipartition(int n, int[][] dislikes) {
        List<List<Integer>> adjacent = new ArrayList<>();
        for (int i = 0; i <= n; i++) {   //多一个 0 ，但没关系
            adjacent.add(new ArrayList<>());
        }
        for (int[] relate : dislikes) {
            int a = relate[0];
            int b = relate[1];
            adjacent.get(a).add(b);
            adjacent.get(b).add(a);
        }
        //--------------------------------------------------------------
        // 染色法：将当前数字 A 染为红色，尝试并将其不喜欢的数字 B 均染色为黄色，如果在染的过程中，发现 B 已经被染色为红色，则矛盾，直接返回
        //--------------------------------------------------------------
        int[] visited = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            if (visited[i] == 0 && !possibleBipartitionDfs(adjacent, visited, 1, i)) {
                return false;
            }
        }
        return true;
    }

    private boolean possibleBipartitionDfs(List<List<Integer>> adjacent, int[] visited, int color, int currentIndex) {
        visited[currentIndex] = color;
        List<Integer> dislike = adjacent.get(currentIndex);
        for (int dis : dislike) {
            //1、不喜欢的对象已经被上色，且上色和当前的一致，则矛盾
            if (visited[dis] != 0 && visited[dis] == color) {
                return false;
            }
            //2、不喜欢的对象未被上色，将其上为其他颜色，判断是否矛盾
            if (visited[dis] == 0 && !possibleBipartitionDfs(adjacent, visited, 3 - color, dis)) {
                return false;
            }
        }
        return true;
    }


    public boolean possibleBipartition01(int n, int[][] dislikes) {   //超时
        List<List<Integer>> adjacent = new ArrayList<>();
        for (int i = 0; i <= n; i++) {   //多一个 0 ，但没关系
            adjacent.add(new ArrayList<>());
        }
        for (int[] relate : dislikes) {
            int a = relate[0];
            int b = relate[1];
            adjacent.get(a).add(b);
            adjacent.get(b).add(a);
        }
        HashSet[] buckets = new HashSet[2];
        buckets[0] = new HashSet();
        buckets[1] = new HashSet();

        return possibleBipartitionDfs(n, adjacent, buckets, 1);
    }

    private boolean possibleBipartitionDfs(int n, List<List<Integer>> adjacent, HashSet[] buckets, int currentIndex) {
        if (currentIndex == n + 1) {  //可分配 n + 1 时说明前面的均已经分配
            return true;
        }
        List<Integer> dislike = adjacent.get(currentIndex);
        for (int i = 0; i <= 1; i++) {
            //剪枝，第一个元素放在两个空桶中结果一致
            if (currentIndex == 1 && i > 0) continue;
            boolean push = true;
            for (int dis : dislike) {
                if (buckets[i].contains(dis)) {
                    push = false;
                    break;
                }
            }
            if (push) {
                buckets[i].add(currentIndex);
                if (possibleBipartitionDfs(n, adjacent, buckets, currentIndex + 1)) {
                    return true;
                }
                buckets[i].remove(currentIndex);
            }
        }
        return false;
    }


    /**
     * 1615. 最大网络秩
     */
    public int maximalNetworkRank(int n, int[][] roads) {
        int[] inDegree = new int[n];
        int[][] connect = new int[n][n];
        for (int[] edge : roads) {
            int node1 = edge[0];
            int node2 = edge[1];
            inDegree[node1]++;
            inDegree[node2]++;
            connect[node1][node2] = 1;
            connect[node2][node1] = 1;
        }
        int maxRank = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int currRank = inDegree[i] + inDegree[j] + (connect[i][j] == 1 ? -1 : 0);
                maxRank = Math.max(maxRank, currRank);
            }
        }
        return maxRank;
    }


    /**
     * 2192. 有向无环图中一个节点的所有祖先
     */
    public List<List<Integer>> getAncestors(int n, int[][] edges) {
        HashMap<Integer, HashSet<Integer>> map = new HashMap<>();
        int[] inDegree = new int[n];
        List<List<Integer>> adjacent = new ArrayList<>();
        List<List<Integer>> ans = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjacent.add(new ArrayList<>());
            ans.add(new ArrayList<>());
            map.put(i, new HashSet<>());
        }
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            adjacent.get(node1).add(node2);
            inDegree[node2]++;
        }
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) queue.addLast(i);
        }
        while (!queue.isEmpty()) {
            Integer currNode = queue.pollFirst();
            List<Integer> injects = adjacent.get(currNode);
            for (int nextNode : injects) {
                map.get(nextNode).add(currNode);
                map.get(nextNode).addAll(map.get(currNode));
                inDegree[nextNode]--;
                if (inDegree[nextNode] == 0) {
                    queue.addLast(nextNode);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            HashSet<Integer> set = map.get(i);
            for (Integer node : set) {
                ans.get(i).add(node);
            }
            Collections.sort(ans.get(i));
        }
        return ans;
    }


    /**
     * 2115. 从给定原材料中找到所有可以做出的菜
     */
    public List<String> findAllRecipes(String[] recipes, List<List<String>> ingredients, String[] supplies) {
        HashMap<String, HashSet<String>> adjacent = new HashMap<>();  //自底向上，记录当前原材料可"直接"做出的菜
        HashMap<String, Integer> inDegree = new HashMap<>(); //当前这道菜"直接"需要几个原材料
        for (int i = 0; i < recipes.length; i++) {
            for (String currNode : ingredients.get(i)) {
                //记录当前原材料可直接做出的菜
                HashSet<String> fatherNode = adjacent.getOrDefault(currNode, new HashSet<>());
                fatherNode.add(recipes[i]);
                adjacent.put(currNode, fatherNode);
                //入度，做出当前菜品"直接"需要几个原材料
                inDegree.put(recipes[i], inDegree.getOrDefault(recipes[i], 0) + 1);
            }
        }
        List<String> ans = new ArrayList<>();
        ArrayDeque<String> queue = new ArrayDeque<>(Arrays.asList(supplies));
        //自下而上搜索
        while (!queue.isEmpty()) {
            String currNode = queue.pollFirst();
            for (String fatherNode : adjacent.getOrDefault(currNode, new HashSet<>())) {
                Integer freq = inDegree.get(fatherNode);
                freq--;
                if (freq == 0) {
                    ans.add(fatherNode);
                    queue.add(fatherNode);
                }
                inDegree.put(fatherNode, freq);
            }
        }
        return ans;
    }



}
