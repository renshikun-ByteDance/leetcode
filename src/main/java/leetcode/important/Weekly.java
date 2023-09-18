package leetcode.important;

import java.util.*;

public class Weekly {


    /**
     * 2367. 算术三元组的数目
     */
    public int arithmeticTriplets(int[] nums, int diff) {
        //----------------------------------------
        // 注意：此题目不清晰，其实是每个位点只能用一次
        //      nums = [0,1,4,6,7,10], diff = 3
        //         1,4,7 和 4,7,10 均为三元组
        //         但 1,7,10不是三元组，懵逼中.......
        // 明白了，其实题目单调递增序列，就好理解了
        //----------------------------------------
        int ans = 0;
        HashSet<Integer> hTables = new HashSet<>();
        for (int num : nums) {
            hTables.add(num);
        }
        //枚举不同的初始值
        for (int i = 0; i < nums.length; i++) {
            int count = 0;
            int num = nums[i];
            while (hTables.contains(num)) {
                count++;
                if (count >= 3) {
                    ans++;
                    break;
                }
                num += diff;
            }
        }
        return ans;
    }

    public int arithmeticTriplets01(int[] nums, int diff) {
        int ans = 0;
        HashSet<Integer> hTables = new HashSet<>();
        for (int num : nums) {
            hTables.add(num);
        }
        for (int num : nums) {
            if (hTables.contains(num + diff) && hTables.contains(num + 2 * diff))
                ans++;
        }
        return ans;
    }


    /**
     * 6180. 最小偶倍数
     */
    public int smallestEvenMultiple(int n) {
        if ((n & 1) == 0)
            return n;
        return n * 2;
    }


    /**
     * 6181. 最长的字母序连续子字符串的长度
     */
    public int longestContinuousSubstring(String str) {
        int ans = 0;
        int left = 0;
        int right = 0;
        while (right < str.length()) {
            if (right > 0 && str.charAt(right) != str.charAt(right - 1) + 1) {
                left = right;
            }
            ans = Math.max(ans, right - left + 1);
            right++;
        }
        return ans;
    }


    /**
     * 6183. 字符串的前缀分数和
     */
    public int[] sumPrefixScores(String[] words) {  //错误解法
        int[] ans = new int[words.length];
        HashMap<String, Integer> hTables = new HashMap<>();
        ArrayList<wordAndIndex> sorted = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            sorted.add(new wordAndIndex(words[i], i));
        }
        sorted.sort((o1, o2) -> {
            return o1.word.length() == o2.word.length() ? o1.word.compareTo(o2.word) : o1.word.length() - o2.word.length();
        });

        for (int i = 0; i < sorted.size(); i++) {
            int prefixScore = 0;
            int currentIndex = 1;
            String word = sorted.get(i).word;
            int index = sorted.get(i).index;
            while (currentIndex <= word.length()) {
                String prefix = word.substring(0, currentIndex);
                hTables.put(prefix, hTables.getOrDefault(prefix, 0) + 1);
                prefixScore += hTables.get(prefix);
                currentIndex++;
            }
            ans[index] = prefixScore;
        }
        return ans;
    }

    class wordAndIndex {
        String word;
        int index;

        public wordAndIndex(String word, int index) {
            this.word = word;
            this.index = index;
        }
    }

    public int[] sumPrefixScores01(String[] words) {   //超时
        int[] ans = new int[words.length];
        HashMap<String, Integer> hTables = new HashMap<>();
        //预处理
        for (String word : words) {
            int currentIndex = 1;
            while (currentIndex <= word.length()) {
                String prefix = word.substring(0, currentIndex);
                hTables.put(prefix, hTables.getOrDefault(prefix, 0) + 1);
                currentIndex++;
            }
        }
        for (int i = 0; i < words.length; i++) {
            int currentIndex = 1;
            int prefixScore = 0;
            while (currentIndex <= words[i].length()) {
                String prefix = words[i].substring(0, currentIndex);
                prefixScore += hTables.get(prefix);
                currentIndex++;
            }
            ans[i] = prefixScore;
        }
        return ans;
    }


    /**
     * 6188. 按身高排序
     */
    public String[] sortPeople(String[] names, int[] heights) {
        String[] ans = new String[names.length];
        PriorityQueue<people> sortedQueue = new PriorityQueue<>((o1, o2) -> o2.height - o1.height);
        for (int i = 0; i < names.length; i++) {
            sortedQueue.add(new people(names[i], heights[i]));
        }
        int currentIndex = 0;
        while (!sortedQueue.isEmpty()) {
            people poll = sortedQueue.poll();
            ans[currentIndex] = poll.name;
            currentIndex++;
        }
        return ans;
    }


    class people {
        String name;
        int height;

        public people(String name, int height) {
            this.name = name;
            this.height = height;
        }
    }

    public String[] sortPeople01(String[] names, int[] heights) {   //错误写法，因为竟然有同名的情况
        String[] ans = new String[names.length];
        HashMap<String, Integer> hTable = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            hTable.put(names[i], heights[i]);
        }
        ArrayList<Map.Entry<String, Integer>> sorted = new ArrayList<>(hTable.entrySet());
        sorted.sort((o1, o2) -> o2.getValue() - o1.getValue());
        Iterator<Map.Entry<String, Integer>> iterator = sorted.iterator();
        int currentIndex = 0;
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> next = iterator.next();
            ans[currentIndex] = next.getKey();
            currentIndex++;
        }
        return ans;
    }


    /**
     * 6189. 按位与最大的最长子数组
     */
    public int longestSubarray(int[] nums) {
        int k = Arrays.stream(nums).max().getAsInt();
        int maxWindow = 1;
        int currentWindow = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1] && nums[i] == k) {  //考虑连续
                currentWindow++;
            } else {
                currentWindow = 1;
            }
            maxWindow = Math.max(maxWindow, currentWindow);
        }
        return maxWindow;
    }


    /**
     * 6191. 好路径的数目
     */
    public int numberOfGoodPaths(int[] vals, int[][] edges) {   //有案例，错误
        //初始值
        int ans = 0;  //各个节点均为好路径
        HashSet<String> pathTarget = new HashSet<>();  //去重
        //邻接表
        HashMap<Integer, ArrayList<Integer>> adjacent = new HashMap<>();
        //初始化
        for (int i = 0; i < vals.length; i++) {
            adjacent.put(i, new ArrayList<>());
        }
        for (int[] edge : edges) {  //无向的邻接表
            int node1 = edge[0];
            int node2 = edge[1];
            adjacent.get(node1).add(node2);
            adjacent.get(node2).add(node1);
        }
        int[] visited = new int[vals.length];
        for (int i = 0; i < vals.length; i++) {
            numberOfGoodPathsDfs(vals, adjacent, pathTarget, visited, vals[i], new StringBuilder().append(i), i);
        }

        HashSet<String> distinct = new HashSet<>();

        for (String path : pathTarget) {
            String reversePath = new StringBuilder(path).reverse().toString();
            if (distinct.contains(reversePath)) {
                continue;
            }
            distinct.add(path);
        }
        return distinct.size();
    }

    private void numberOfGoodPathsDfs(int[] vals, HashMap<Integer, ArrayList<Integer>> adjacent, HashSet<String> pathTarget, int[] visited, int target, StringBuilder path, int currentNode) {
        //迭代终止
        if (visited[currentNode] == 1) {
            return;
        }

        if (vals[currentNode] == target) {  //迭代不终止
            pathTarget.add(new String(path.toString()));
        }

        visited[currentNode] = 1;

        ArrayList<Integer> nextNodes = adjacent.get(currentNode);
        for (int nextNode : nextNodes) {
            //剪枝
            if (vals[nextNode] > target)  //剔除不满足条件的路径
                continue;

            //添加元素
            path.append(nextNode);

            //纵向搜索
            numberOfGoodPathsDfs(vals, adjacent, pathTarget, visited, target, path, nextNode);

            //移除元素
            path.deleteCharAt(path.length() - 1);
        }
        visited[currentNode] = 0;
    }


    /**
     * 6192. 公因子的数目
     */
    public int commonFactors(int a, int b) {
        int ans = 0;
        int end = Math.min(a, b);
        for (int i = 1; i <= end; i++) {
            if (a % i == 0 && b % i == 0)
                ans++;
        }
        return ans;
    }


    /**
     * 6193. 沙漏的最大总和
     */
    public int maxSum(int[][] grid) {
        long ans = 0;
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {1, -1}, {1, 0}, {1, 1}};
        int rows = grid.length;
        int cols = grid[0].length;
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                long currentTotal = grid[i][j];
                for (int[] dir : directions) {
                    int currRow = i + dir[0];
                    int currCol = j + dir[1];
                    currentTotal += grid[currRow][currCol];
                }
                ans = Math.max(ans, currentTotal);
            }
        }
        return (int) ans;
    }


    /**
     * 6194. 最小 XOR
     */
    public int minimizeXor(int num1, int num2) {  //自己写的，通过
        int target = num1;
        int res1 = 0;
        int res2 = 0;
        for (int i = 0; i < 32; i++) {
            res1 += (num1 >> i) & 1;
            res2 += (num2 >> i) & 1;
        }
        if (res2 > res1) {
            int diff = res2 - res1;
            for (int i = 0; i < 32; i++) {
                if (((target >> i) & 1) == 0) {
                    target |= 1 << i;
                    diff--;
                    if (diff == 0) return target;
                }
            }
        }
        if (res1 > res2) {   //将res1低位的1消除
            int diff = res1 - res2;
            int targetIndex = 0;
            for (int i = 0; i < 32; i++) {
                if (((target >> i) & 1) == 1) {
                    diff--;
                    if (diff == 0) {
                        targetIndex = i;
                        break;
                    }
                }
            }
            return num1 & (~((1 << (targetIndex + 1)) - 1));
        }
        return num1;
    }


    /**
     * 6195. 对字母串可执行的最大删除数
     */
    public int deleteString(String str) {   //没时间了
        int ans = 0;
        int currentIndex = 0;


        return ans;
    }


    /**
     * 6200. 处理用时最长的那个任务的员工
     */
    public int hardestWorker(int n, int[][] logs) {
        int[][] sorted = new int[logs.length][2];
        sorted[0] = new int[]{logs[0][0], logs[0][1]};
        for (int i = 1; i < logs.length; i++) {
            sorted[i] = new int[]{logs[i][0], logs[i][1] - logs[i - 1][1]};
        }
        Arrays.sort(sorted, (o1, o2) -> {
                    if (o2[1] != o1[1])
                        return o2[1] - o1[1];
                    else
                        return o1[0] - o2[0];
                }
        );
        return sorted[0][0];
    }


    int pathNums = 0;

    public int numberOfPaths01(int[][] grid, int k) {  //超时
        int mod = (int) Math.pow(10, 9) + 7;
        int[][] direction = {{1, 0}, {0, 1}};
        numberOfPathsDfs(grid, direction, 0, 0, 0, k);
        return pathNums % mod;
    }

    private void numberOfPathsDfs(int[][] grid, int[][] direction, int row, int col, long sum, int target) {
        int mod = (int) Math.pow(10, 9) + 7;
        if (row == grid.length || col == grid[0].length) {
            return;
        }

        sum += grid[row][col];
        if (row == grid.length - 1 && col == grid[0].length - 1) {
            if (sum % target == 0) {
                pathNums = (++pathNums) % mod;
            }
            return;
        }

        for (int[] dir : direction) {
            int nextRow = row + dir[0];
            int nextCol = col + dir[1];
            numberOfPathsDfs(grid, direction, nextRow, nextCol, sum, target);
        }

        sum -= grid[row][col];
    }


    /**
     * 6204. 与对应负数同时存在的最大正整数
     */
    public int findMaxK(int[] nums) {
        HashSet<Integer> distinct = new HashSet<>();
        for (int num : nums) {
            distinct.add(num);
        }
        for (int i = 1000; i > 0; i--) {
            if (distinct.contains(i) && distinct.contains(-i))
                return i;
        }
        return -1;
    }


    /**
     * 6205. 反转之后不同整数的数目
     */
    public int countDistinctIntegers(int[] nums) {
        HashSet<Integer> source = new HashSet<>();
        for (int num : nums) {
            source.add(num);
        }
        HashSet<Integer> distinct = new HashSet<>();
        for (int num : source) {
            distinct.add(num);
            distinct.add(reverseNum(num));
        }
        return distinct.size();
    }

    /**
     * 6219. 反转之后的数字和
     */
    public boolean sumOfNumberAndReverse(int num) {
        for (int i = 0; i <= num; i++) {
            if (i + reverseNum(i) == num) return true;
        }
        return false;
    }

    private int reverseNum(int num) {
        StringBuilder builder = new StringBuilder();
        builder.append(num);
        String reverse = builder.reverse().toString();
        return Integer.parseInt(reverse);
    }


    /**
     * 6207. 统计定界子数组的数目
     */
    public long countSubarrays(int[] nums, int minK, int maxK) {   //最终自己写的，通过，但没来得及提交
        long ans = 0;
        int left = 0;
        int right = 0;
        int[] both = {-1, -1};
        int end = 0;
        while (right < nums.length) {
            if (nums[right] == maxK) {
                both[0] = right;
            }
            if (nums[right] == minK) {  //和上面分开写，保证最大和最小值一致的情况
                both[1] = right;
            }
            if (nums[right] > maxK || nums[right] < minK) {
                both[0] = -1;
                both[1] = -1;
                left = right + 1;
            }
            if (both[0] != -1 && both[1] != -1) {
                end = Math.max(end, Math.min(both[0], both[1]));  //关键
                long value = end - left + 1;
                ans += value;
            }
            right++;
        }
        return ans;
    }

    public long countSubarrays01(int[] nums, int minK, int maxK) {  //错误，本来像转换为 连续为1的区间内的数组个数，但题目其实是要包含最大和最小值的
        long ans = 0;
        int n = nums.length;
        int[] target = new int[n];
        Arrays.fill(target, 1);
        for (int i = 0; i < n; i++) {
            if (nums[i] < minK || nums[i] > maxK) target[i] = -1;
        }
        int right = 0;
        long currentValue = 1;
        while (right < n) {
            if (target[right] == 1) {
                ans += currentValue;
                currentValue++;
            } else {
                currentValue = 1;
            }
            right++;
        }
        return ans;
    }


    /**
     * 6214. 判断两个事件是否存在冲突
     */
    public boolean haveConflict(String[] event1, String[] event2) {
        String[][] arr = new String[][]{event1, event2};
        Arrays.sort(arr, (o1, o2) -> o1[0].compareTo(o2[0]));  //按照开始时间升序排序
        return arr[1][0].compareTo(arr[0][1]) <= 0;
    }

    /**
     * 6224. 最大公因数等于 K 的子数组数目
     */
    public int subarrayGCD(int[] nums, int k) {
        int ans = 0;
        int left = 0;
        int right = 0;
        while (right < nums.length) {


            right++;
        }


        return ans;
    }


    /**
     * 6216. 使数组相等的最小开销
     */
    public long minCost(int[] nums, int[] cost) {
        int n = nums.length;
        int[][] arr = new int[n][2];
        for (int i = 0; i < n; i++) {
            arr[i] = new int[]{nums[i], cost[i]};
        }
        Arrays.sort(arr, (o1, o2) -> o1[0] - o2[0]);  //按照数字升序排序
        long currentPay = 0;  //记录不同情况下的开销
        long sum = 0;  //记录各个位点单位开销和
        //初始化
        for (int i = 0; i < n; i++) {  //初始化为将所有数字转换为 nums[0] 所需要的开销
            currentPay += (long) (arr[i][0] - arr[0][0]) * arr[i][1];  //##必须有强制转换##
            sum += arr[i][1];  //各个位点的单位开销和，用于与前缀和一并使用，方便快速计算后缀和
        }
        long prefixSum = arr[0][1]; //开销的前缀和，初始化
        long ans = currentPay;
        //枚举，以不同点作为目标，其对应的开销和，并于以其他位点为目标的开销和，求最小值
        for (int i = 1; i < n; i++) {
            //将目标值转换为当前值，当前位置相较于前一位（前一个目标值）的变化量，由于数组有序，故 diff > 0
            long diff = arr[i][0] - arr[i - 1][0];
            currentPay += diff * prefixSum;               //1、将目标值转换为当前值，其前缀对总开销的影响
            currentPay -= diff * (sum - prefixSum);       //2、将目标值转换为当前值，其后缀对总开销的影响
            prefixSum += arr[i][1];   //更新花销的前缀和
            ans = Math.min(ans, currentPay);  //currentPay在变换目标值的过程中，需要进行状态转换
        }
        return ans;
    }


    /**
     * 6217. 使数组相似的最少操作次数
     */
    public long makeSimilar(int[] nums, int[] target) {  //分治
        int n = nums.length;
        Arrays.sort(nums);
        Arrays.sort(target);
        ArrayList<Integer> nums1 = new ArrayList<>();  //记录奇数
        ArrayList<Integer> nums2 = new ArrayList<>();  //记录偶数
        ArrayList<Integer> target1 = new ArrayList<>();  //记录奇数
        ArrayList<Integer> target2 = new ArrayList<>();  //记录偶数
        for (int i = 0; i < n; i++) {
            if ((nums[i] & 1) == 1) nums1.add(nums[i]);
            else nums2.add(nums[i]);
            if ((target[i] & 1) == 1) target1.add(target[i]);
            else target2.add(target[i]);
        }
        long ans = 0;
        for (int i = 0; i < nums1.size(); i++) {
            ans += Math.abs(nums1.get(i) - target1.get(i)) / 2;  //计算所需要的交换次数
        }
        for (int i = 0; i < nums2.size(); i++) {
            ans += Math.abs(nums2.get(i) - target2.get(i)) / 2;  //计算所需要的交换次数
        }
        return ans / 2;// 为了去重复
    }

    public long makeSimilar01(int[] nums, int[] target) {  //错误写法
        int n = nums.length;
        Arrays.sort(nums);
        Arrays.sort(target);
        ArrayList<Integer> nums1 = new ArrayList<>();  //记录奇数
        ArrayList<Integer> nums2 = new ArrayList<>();  //记录奇数
        long sum = 0;
        long xx = 0;  //记录奇数的累计和
        long yy = 0;  //记录奇数的累计和
        long diff = 0;
        for (int i = 0; i < n; i++) {
            sum += nums[i];
            if ((nums[i] & 1) == 1) {
                xx += nums[i];
                nums1.add(nums[i]);
            }
            if ((target[i] & 1) == 1) {
                yy += target[i];
                nums2.add(target[i]);
            }
            if (nums[i] > target[i]) {
                diff += nums[i] - target[i];
            }
        }

        if (sum == xx || xx == 0) {  //全部为奇数或者全部为偶数
            return diff / 2;
        }

        //奇数偶数均存在
        int m = nums1.size();
        long currentDiff1 = 0;
        long currentDiff2 = 0;
        for (int i = 0; i < m; i++) {
            int dis = Math.abs(nums1.get(i) - nums2.get(i));
            if (dis > 0) currentDiff1 += dis;
            else currentDiff2 += dis;
        }
        return Math.max(Math.abs(currentDiff1), Math.abs(currentDiff2)) / 2;
    }


    /**
     * 6229. 对数组执行操作
     */
    public int[] applyOperations(int[] nums) {
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] == nums[i + 1]) {
                nums[i] = 2 * nums[i];
                nums[i + 1] = 0;
            }
        }
        int[] ans = new int[nums.length];
        int currentIndex = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                ans[currentIndex] = nums[i];
                currentIndex++;
            }
        }
        return ans;
    }

    /**
     * 6230. 长度为 K 子数组中的最大和
     */
    public long maximumSubarraySum(int[] nums, int k) {
        HashMap<Integer, Integer> hTable = new HashMap<>();
        int left = 0;
        int right = 0;
        long max = 0;
        long cur = 0;
        while (right < nums.length) {
            cur += nums[right];
            hTable.put(nums[right], hTable.getOrDefault(nums[right], 0) + 1);
            if (right - left + 1 > k) {  //维护固定的滑动窗口
                Integer count = hTable.get(nums[left]);
                count--;
                if (count > 0) {
                    hTable.put(nums[left], count);  //覆盖
                } else {
                    hTable.remove(nums[left]);      //移除
                }

                cur -= nums[left];
                left++;
            }
            if (right - left + 1 == k) {
                if (hTable.size() == k) {
                    max = Math.max(max, cur);
                }
            }
            right++;
        }
        return max;
    }


    /**
     * 6231. 雇佣 K 位工人的总代价
     */
    public long totalCost(int[] costs, int k, int candidates) {
        int n = costs.length;
        long ans = 0;
        int left = 0;
        int right = n - 1;
        PriorityQueue<Integer> sortedQueue1 = new PriorityQueue<>((o1, o2) -> o1 - o2);  //升序
        PriorityQueue<Integer> sortedQueue2 = new PriorityQueue<>((o1, o2) -> o1 - o2);  //升序
        //初始化左端队列
        while (left < Math.min(n, candidates)) {
            sortedQueue1.add(costs[left]);
            left++;  //待写入位点
        }
        //初始化右端队列
        while (right >= left && n - right <= candidates) {
            sortedQueue2.add(costs[right]);
            right--;  //待写入位点
        }
        while (k > 0) {
            if (!sortedQueue1.isEmpty() && !sortedQueue2.isEmpty()) {
                Integer aa = sortedQueue1.peek();
                Integer bb = sortedQueue2.peek();
                if (aa <= bb) {
                    ans += aa;
                    sortedQueue1.poll();
                    if (left <= right) {
                        sortedQueue1.add(costs[left]);
                        left++;
                    }
                } else {
                    ans += bb;
                    sortedQueue2.poll();
                    if (left <= right) {
                        sortedQueue2.add(costs[right]);
                        right--;
                    }
                }
            } else if (!sortedQueue1.isEmpty()) {   //如果有一个已经是空，则说明二者已经交叉了，无需再添加元素了
                Integer aa = sortedQueue1.poll();
                ans += aa;
            } else {
                Integer bb = sortedQueue2.poll();
                ans += bb;
            }
            k--;
        }
        return ans;
    }


    /**
     * 6232. 最小移动总距离
     */
    public long minimumTotalDistance(List<Integer> robot, int[][] factory) {
        long ans = 0;
        int n = robot.size();
        int[] visited = new int[n];
        HashMap<Integer, Integer> hTable = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> repeated = new HashMap<>();  //一个机器人距离两个位置相等
        ArrayList<HashSet<Integer>> assign = new ArrayList<>();
        for (int i = 0; i < factory.length; i++) {
            assign.add(new HashSet<>());
        }
        for (int[] fac : factory) {
            int address = fac[0];
            int needRobots = fac[1];
            //找到距离最近的 needRobots 个机器人的位置
//            PriorityQueue<Integer> sortedQueue = new PriorityQueue<>((o1, o2) -> Math.abs(o1 - address) - Math.abs(o2 - address));
            PriorityQueue<Integer> sortedQueue = new PriorityQueue<>();
            sortedQueue.addAll(robot);
            while (!sortedQueue.isEmpty() && address > 0) {
                Integer robotID = sortedQueue.poll();
                if (hTable.containsKey(robotID)) {
                    Integer factoryID = hTable.get(robotID);
                    if (Math.abs(factory[factoryID][0] - robot.get(robotID)) == Math.abs(factory[factoryID][0] - robot.get(robotID))) {

                    }
                }
                address--;
            }
        }
        return ans;
    }


    public double[] convertTemperature(double celsius) {
        double[] ans = new double[2];
        ans[0] = celsius + 273.15;
        ans[1] = celsius * 1.80 + 32.00;
        return ans;
    }


    public int subarrayLCM(int[] nums, int k) {
        int res = 0;
        int ans = 0;
        int left = 0;
        int right = 0;
        while (right < nums.length) {
            if (nums[right] == k) res++;


        }


        return ans + res;
    }


    public int unequalTriplets(int[] nums) {
        int ans = 0;
        for (int i = 0; i < nums.length - 2; i++) {
            for (int j = i + 1; j < nums.length - 1; j++) {
                for (int k = j + 1; k < nums.length; k++) {
                    if (nums[i] != nums[j] && nums[j] != nums[k] && nums[i] != nums[k]) {
                        ans++;
                    }
                }
            }
        }
        return ans;
    }


}
