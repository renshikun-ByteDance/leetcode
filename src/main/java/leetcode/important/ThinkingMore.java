package leetcode.important;

import java.util.*;

public class ThinkingMore {


    /**
     * 421. 数组中两个数的最大异或值
     * https://leetcode-cn.com/problems/maximum-xor-of-two-numbers-in-an-array/solution/li-yong-yi-huo-yun-suan-de-xing-zhi-tan-xin-suan-f/
     */
    public int findMaximumXOR(int[] nums) {
        int res = 0;
        int mask = 0;
        // 先确定高位，再确定低位（有点贪心算法的意思），才能保证这道题的最大性质
        // 一位接着一位去确定这个数位的大小
        // 利用性质： a ^ b = c ，则 a ^ c = b，且 b ^ c = a
        for (int i = 30; i >= 0; i--) {
            //1、获取数组中每个元素的前缀（i位前）
            mask |= 1 << i;    //计算位置掩码，迭代获取
            HashSet<Integer> prefix = new HashSet<>();  //存储此轮各个数字的前缀
            for (int num : nums)
                prefix.add(mask & num);  //mask高位为 1，低位为 0，因此"与"运算后，保留num的前缀，后面的置为 0
            //2、假设理想的前缀
            int temp = res | (1 << i);  //假设当前位为 1，基于之前的迭代结果
            //3、验证前缀是否合理
            for (int pre : prefix) {
                //基于"异或运算"的性质 temp ^ pre = one 则有 one ^ pre = temp
                if (prefix.contains(temp ^ pre)) {  //存在对应的数，使得 假设的 前缀合理
                    res = temp;
                    break;
                }
            }
        }
        return res;
    }


    /**
     * 1218. 最长定差子序列
     */
    public int longestSubsequence(int[] arr, int difference) {
        int ans = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int value : arr) {
            //动态规划：转移转移方程未 dp[i] = dp[i - difference] + 1;
            hTable.put(value, hTable.getOrDefault(value - difference, 0) + 1);   //// Key：arr[i]，value：以arr[i]结尾的等差子序列的长度
            ans = Math.max(ans, hTable.get(value));
        }
        return ans;
    }



    /*
     * 397. 整数替换
     *
     * 如果本位是 1，下一位也是 1 ，处理过程类似多米诺骨牌，此时对 +1 和 -1 两种情况分析：
     *          1、+ 1
     *             针对本位，则需要进行两步操作才能削掉，分别为 +1 和 >>
     *             针对本位前的连续为 1 的高位区间(区间长度: L - 1) ，由于本位 +1，则由于进位的原因，使得全部置为 0 ，则这些仅需要 一步操作即可 >>
     *             截至连续区间，共需要：L + 1
     *          2、- 1
     *             针对本位，则需要进行两步操作才能削掉 -1 和 >>
     *             针对本位前的连续为 1 的高位区间 L ，由于本位 -1，不会对连续区间的移动有任何影响和优化
     *             截至连续区间，共需要：2L
     *          因此：针对连续 1 的区间，应该选择 +1
     *
     *      如果本位是 1，高一位是 0 ，此时对 +1 和 -1 两种情况分析：
     *           1、+ 1
     *             针对本位，需要进行两步操作才能消掉，分别为 +1 和 >>
     *             针对高一位，由于上一位+1导致进位，所以此位 也需要进行 相同的两步操作
     *             因此，需要进行4步操作才可以
     *                   相当于本来的0可以由一步操作来削掉，但却由于进一位导致需要两步才行
     *           1、- 1
     *             针对本位，需要进行两步操作才能消掉，分别为 -1 和 >>
     *             针对高一位，上一位-1对此无影响，所以此位 仅仅需要一步操作 >>
     *             因此，需要进行3步操作才可以
     *           因此，交替的情况，应该选择对 -1
     */

    /**
     * 二进制观察处理法
     * 实现思路:
     * 最快的移动就是遇到2的次幂(例如数字16  10000 -> 01000 -> 00100 -> 00010 -> 00001)
     * 将二进制一直左移 最右为0时可以直接移动(例如数字6  000110 -> 000011)
     * 最右位为1时需把1变成0, 再移动(例如数字9  001001 -> 001000)
     * 故最优解就是如何在迭代中减少出现末尾1(就是什么时候+1, 什么时候-1 来实现过程中最少出现01或11结尾)
     * 得出以下结论:
     * 若n的二进制为 xxxx10, 则下一次处理 n = n/2 次数+1
     * 若n的二进制为 xxxx01, 则下一次处理 n = n/2 次数+2(即需要先-1再除以2, 故这里是加2) n > 1
     * 若n的二进制为 xxxx11, 则下一次处理 n = n/2 +1 次数+2(即需要先+1再除以2, 故这里是加2) n > 3
     * 特殊情况: 数字3  000011, 000011 -> 000010 -> 000001(两次即可)
     * 边界条件: 000001 -> 答案为0
     */
    public int integerReplacement(int n) {
        int res = 0;
        while (n != 1) {
            //判断奇偶性
            if ((n & 1) == 0)  //偶数
                n >>>= 1;
                //-----------------------------
                //细节：虽然 n 是正数，但这里要用 无符号右移
                //     因为 n 为 2^31 -1 时，n + 1会导致int溢出，将 n 变为一个负数，如果用的是 >> ,则高位一直补 1 ，进入死循环
                //或者，先将 n 变为 long 型在做处理
                //-----------------------------
            else {             //奇数
                if (n != 3 && ((n >> 1) & 1) == 1)  //奇数，则当前最低位为 1
                {
                    //综上：无论是至少两个连续 1的区间，还是01的区间，都应该整体考虑，尽可能将区间内的数字置为0，以便利用 偶数可直接消除的优势
                    n++;
                } else
                    n--;
            }
            res++;
        }
        return res;
    }


    public int integerReplacement001(int n) {
        int ans = 0;
        while (n != 1) {
            //特殊的奇数
            if (n == 3) return ans + 2;

            //奇偶数判断
            if ((n & 1) == 0) {       //1.偶数 X0的情况
                n >>>= 1;
            } else {                  //2.奇数 X1的情况
                if (((n >> 1) & 1) == 1)     //2.1 11的情况
                    n++;
                else                         //2.2 01的情况
                    n--;
            }
            ans++;
        }
        return ans;
    }


    public int integerReplacement01(int n) {
        if (n == 1)
            return 0;
        if ((n & 1) == 0)  //偶数的情况：
            return 1 + integerReplacement(n / 2);  //1代表一步 n/2 即可
        //奇数的情况：2代表，先加 1 或 减 1，使其变为偶数，然后再有 "n"/2
        return 2 + Math.min(integerReplacement(n / 2), integerReplacement(n / 2 + 1));
        //----------------------------------------
        //处理过程的整体结构类似二叉树的"样子"
        //    虽然代码的写法像是从上向下（即从根节点到各个叶子节点），但整体的处理过程是可以理解为从下向上
        //       每个节点均取两个子节点中最小"替换次数"，自叶子节点向上汇总
        //----------------------------------------
    }

    Map<Long, Integer> map = new HashMap<>();

    public int integerReplacement02(int n) {
        return dfs(n * 1L);
    }

    int dfs(long n) {
        if (n == 1) return 0;
        if (map.containsKey(n)) return map.get(n);
        int ans = n % 2 == 0 ? dfs(n / 2) : Math.min(dfs(n + 1), dfs(n - 1));
        map.put(n, ++ans);
        return ans;
    }


    /**
     * 517. 超级洗衣机
     */
    public int findMinMoves(int[] machines) {
        int ans = 0;
        int sum = Arrays.stream(machines).sum();
        if (sum % machines.length != 0) return -1;
        int average = sum / machines.length;
        int prefixSum = 0;
        for (int num : machines) {
            int gap = num - average;
            prefixSum += gap;
            ans = Math.max(ans, Math.max(Math.abs(prefixSum), Math.abs(gap)));
        }
        return ans;
    }

    /**
     * 942. 增减字符串匹配
     */
    public int[] diStringMatch(String s) {
        int[] ans = new int[s.length() + 1];
        int low = 0;
        int high = s.length();
        for (int i = 0; i < s.length(); i++) {
            ans[i] = s.charAt(i) == 'I' ? low++ : high--;
        }
        ans[s.length()] = s.charAt(s.length() - 1) == 'I' ? high : low;
        return ans;
    }


    /**
     * 1996. 游戏中弱角色的数量
     */
    public int numberOfWeakCharacters(int[][] properties) {
        int ans = 0;
        Arrays.sort(properties, (o1, o2) -> {
            if (o1[0] != o2[0]) {
                return o2[0] - o1[0];    //1.先按照攻击值降序排序
            } else {
                return o1[1] - o2[1];    //2.再按照防御值升序排序
            }
        });
        int maxDefense = properties[0][1];
        for (int i = 1; i < properties.length; i++) {
            if (properties[i][1] < maxDefense) {
                ans++;
            }
            maxDefense = Math.max(maxDefense, properties[i][1]);
        }
        return ans;
    }


    /**
     * 1736. 替换隐藏数字得到的最晚时间
     */
    public String maximumTime(String time) {
        char[] arrayTimes = time.toCharArray();
        if (arrayTimes[4] == '?') arrayTimes[4] = '9';
        if (arrayTimes[3] == '?') arrayTimes[3] = '5';
        if (arrayTimes[1] == '?') {
            if (arrayTimes[0] == '0' || arrayTimes[0] == '1') arrayTimes[1] = '9';
            if (arrayTimes[0] == '2' || arrayTimes[0] == '?') arrayTimes[1] = '3';
        }
        if (arrayTimes[0] == '?') {
            if (arrayTimes[1] >= '4') arrayTimes[0] = '1';
            else arrayTimes[0] = '2';
        }
        return new String(arrayTimes);
    }


    /**
     * 3. 无重复字符的最长子串
     */
    public int lengthOfLongestSubstring(String str) {
        if (str.length() == 0) return 0;
        int left = 0;
        int maxWindow = 1;
        int[] buckets = new int[256];
        for (int i = 0; i < str.length(); i++) {
            if (buckets[str.charAt(i)] != 0) {
                left = Math.max(left, Math.max(buckets[str.charAt(i)], 0) + 1);  //上一个相同字符的位点
            }
            maxWindow = Math.max(maxWindow, i - left + 1);  // i 与 left 都代表 位点
            buckets[str.charAt(i)] = i == 0 ? -1 : i;   //注意：由于 int[] 的默认值本身就是 0，而 索引位置为 0 的字符要特殊处理一下
        }
        return maxWindow;
    }


    /**
     * 3. 无重复字符的最长子串
     */
    public int lengthOfLongestSubstring01(String str) {
        int left = 0;
        int right = 0;
        int maxWindow = 0;
        HashMap<Character, Integer> hTable = new HashMap<>();
        while (right < str.length()) {
            char xx = str.charAt(right);
            if (hTable.containsKey(xx)) {
                left = Math.max(left, hTable.get(xx) + 1);
            }
            maxWindow = Math.max(maxWindow, right - left + 1);
            hTable.put(xx, right);
            right++;
        }
        return maxWindow;
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


    //------------------------------------------------------------------------------------------------------------------------------------------------
    // 对比以下两种写法的差异：
    //   if ((long) (nums[i] + nums[nums.length - 3] + nums[nums.length - 2] + nums[nums.length - 1]) < target) {   ##错误写法，已经数组越界了才转 long
    //   if ((long) nums[i] + nums[nums.length - 3] + nums[nums.length - 2] + nums[nums.length - 1] < target) {     ##正确写法
    // 具体测试：
    //        System.out.println(1000000000 + 1000000000 + 1000000000 < 0);               ##越界
    //        System.out.println((long) (1000000000 + 1000000000 + 1000000000) < 0);      ##越界
    //        System.out.println((long) 1000000000 + 1000000000 + 1000000000 < 0);        ##正确结果
    // 下面题目与越界相关的测试案例：
    //        int[] nums = {0, 0, 0, 1000000000, 1000000000, 1000000000, 1000000000}; int target = 1000000000;
    //------------------------------------------------------------------------------------------------------------------------------------------------


    /**
     * 18. 四数之和
     */
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 3; i++) {
            //------------------------------------------
            // 不能加以下的提前结束的逻辑
            //
            //
            // int[] nums = {0, 0, 0, 0}; int target = 0;
            // int[] nums = {-5, -4, -3, -2, 1, 3, 3, 5}; int target = -11;
            //------------------------------------------
            //剪枝一：提前结束
//            if (nums[i] >= target) return ans;
            //-------------------------------------------
            // 忽略重复的第一个数字：
            // 这里剔除的主要重复组合的情况是 仅需要两个值相同的数字中的其中一个，如下面案例的 [2, 4, 5, 9]
            // 如果组合需要两个相同值的数字，则通过移动 i，也仅会保留一个组合，如下面案例的 [2, 2, 5, 11], [2, 2, 7, 9]
            // int[] nums = {2, 2, 4, 5, 7, 9, 11}; int target = 20;
            // 不加下面剔除重复的逻辑对应结果： [[2, 2, 5, 11], [2, 2, 7, 9], [2, 4, 5, 9], [2, 4, 5, 9]]
            //-------------------------------------------
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            //剪枝一：第一个数太小
            if ((long) nums[i] + nums[nums.length - 3] + nums[nums.length - 2] + nums[nums.length - 1] < target) {
                continue;
            }
            //剪枝一：第一个数太大
            if ((long) nums[i] + nums[i + 1] + nums[i + 2] + nums[i + 3] > target) {
                break;
            }
            for (int j = i + 1; j < nums.length - 2; j++) {
                //忽略重复的第二个数字
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }
                //剪枝二：第二个数太小
                if ((long) nums[i] + nums[j] + nums[nums.length - 2] + nums[nums.length - 1] < target) {
                    continue;
                }
                //剪枝二：第二个数太大
                if ((long) nums[i] + nums[j] + nums[j + 1] + nums[j + 2] > target) {
                    break;
                }
                int left = j + 1;
                int right = nums.length - 1;
                while (left < right) {
                    long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];
                    if (sum == target) {
                        ans.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));
                        left++;
                        right--;
                        //忽略重复的第三第四个数字
                        while (left < right && nums[left] == nums[left - 1]) left++;
                        while (left < right && nums[right] == nums[right + 1]) right--;
                    } else if (sum < target) {
                        left++;
                    } else {
                        right--;
                    }
                }
            }
        }
        return ans;
    }


    /**
     * 16. 最接近的三数之和
     */
    public int threeSumClosest(int[] nums, int target) {  //固定一个数，动态搜索两个数字
        Arrays.sort(nums);
        int sumTarget = nums[0] + nums[1] + nums[2];
        for (int i = 0; i < nums.length - 2; i++) {
            //剔除重复的第一个数
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int left = i + 1;
            int right = nums.length - 1;
            while (left < right) {
                int currentSum = nums[i] + nums[left] + nums[right];
                if (currentSum == target) {
                    return currentSum;
                } else if (currentSum < target) {
                    //动态记录
                    if (Math.abs(currentSum - target) < Math.abs(sumTarget - target)) {
                        sumTarget = currentSum;
                    }
                    left++;
                    //剔除重复的第二个元素
                    while (left < right && nums[left] == nums[left - 1]) {
                        left++;
                    }
                } else {
                    //动态记录
                    if (Math.abs(currentSum - target) < Math.abs(sumTarget - target)) {
                        sumTarget = currentSum;
                    }
                    right--;
                    //剔除重复的第三个数
                    while (left < right && nums[right] == nums[right + 1]) {
                        right--;
                    }
                }
            }
        }
        return sumTarget;
    }

    public int threeSumClosest01(int[] nums, int target) {  //固定两个数，二分搜索剩余一个数字
        Arrays.sort(nums);
        int sumTarget = nums[0] + nums[1] + nums[2];
        for (int i = 0; i < nums.length - 2; i++) {
            for (int j = i + 1; j < nums.length - 1; j++) {
                int left = j + 1;
                int right = nums.length - 1;
                while (left <= right) {
                    int mid = left + ((right - left) >> 1);
                    int currentSum = nums[i] + nums[j] + nums[mid];
                    if (currentSum == target) {
                        return currentSum;
                    } else if (currentSum < target) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }
                left = Math.min(left, nums.length - 1);  //右侧越界
                right = Math.max(j + 1, right);  //左侧越界
                int currentSum1 = nums[i] + nums[j] + nums[left];
                int currentSum2 = nums[i] + nums[j] + nums[right];
                if (Math.abs(currentSum1 - target) < Math.abs(sumTarget - target)) {
                    sumTarget = currentSum1;
                }
                if (Math.abs(currentSum2 - target) < Math.abs(sumTarget - target)) {
                    sumTarget = currentSum2;
                }
            }
        }
        return sumTarget;
    }


    /**
     * 611. 有效三角形的个数
     */
    public int triangleNumber01(int[] nums) {  //偏暴力的解法
        int triangleNumber = 0;
        Arrays.sort(nums);
        for (int first = 0; first < nums.length - 2; first++) {   //枚举最短边
            for (int second = first + 1; second < nums.length - 1; second++) {   //枚举次最短边
                //剪枝
                if (nums[first] + nums[second] <= nums[second + 1]) { //最长边的最小值也大于等于两个短边的和
                    continue;
                }
                int third = second + 1;
                int increase = 0;
                while (third < nums.length) {
                    if (nums[first] + nums[second] > nums[third]) {
                        increase++;
                    } else {
                        break;
                    }
                    third++;
                }
                triangleNumber += increase;
            }
        }
        return triangleNumber;
    }


    public int triangleNumber02(int[] nums) {  //第三条边使用二分查找
        int triangleNumber = 0;
        Arrays.sort(nums);
        for (int first = 0; first < nums.length - 2; first++) {   //枚举最短边
            for (int second = first + 1; second < nums.length - 1; second++) { //枚举次最短边
                //剪枝
                if (nums[first] + nums[second] <= nums[second + 1]) { //最长边的最小值也大于等于两个短边的和
                    continue;
                }
                //基于二分搜索，获取满足条件的最长的第三边
                int left = second + 1;
                int right = nums.length - 1;
                int target = nums[first] + nums[second];
                while (left <= right) {
                    int mid = left + ((right - left) >> 1);
                    if (nums[mid] < target) {
                        left = mid + 1;
                    } else if (target <= nums[mid]) {
                        right = mid - 1;
                    }
                }
                if (left == nums.length) {         //1.target在区间外，在区间右侧
                    left = nums.length - 1;
                } else if (nums[left] == target) { //2.target在区间内，存在，left所在位置为最左侧 target的索引位置
                    left -= 1;
                } else if (nums[left] > target) {  //3.target在区间内，不存在，left为大于target的最小索引位置
                    left -= 1;
                }
                if (left >= second + 1) {  // left 为最长的第三边对应的索引，其一定要大于当前枚举的第二边
                    triangleNumber += left - (second + 1) + 1;
                }
            }
        }
        return triangleNumber;
    }


    public int triangleNumber03(int[] nums) {  //双指针
        int triangleNumber = 0;
        Arrays.sort(nums);
        for (int third = nums.length - 1; third >= 2; third--) {   //枚举最长边
            int left = 0;
            int right = third - 1;
            while (left < right) {
                //-------------------------------------------------------------------------
                // left向右侧移动，nums[left]呈增长趋势，right向左侧移动，nums[right]呈下降趋势
                //-------------------------------------------------------------------------
                if (nums[left] + nums[right] > nums[third]) {
                    triangleNumber += right - left;  //说明区间 [left,right - 1] 为满足条件的最短边区间
                    right--;  //贪心，较少次最长边，进入下一轮搜索
                } else {
                    left++;   //如果想要尝试满足条件，只能尝试增加左边的值，即 left++
                }
            }
        }
        return triangleNumber;
    }


    public int triangleNumber04(int[] nums) {  //思路同上，但第三条边是从左侧移动的
        int n = nums.length;
        Arrays.sort(nums);
        int ans = 0;
        for (int third = 2; third < nums.length; third++) {
            for (int first = 0, second = third - 1; first < second; second--) {
                while (first < second && nums[first] + nums[second] <= nums[third]) first++;
                ans += second - first;
            }
        }
        return ans;
    }


    /**
     * 88. 合并两个有序数组
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {   //正向写入
        int[] target = new int[m + n];
        int p1 = 0;
        int p2 = 0;
        int write = 0;
        while (write < m + n) {
            if (p1 == m) {
                target[write] = nums2[p2];
                p2++;
            } else if (p2 == n) {
                target[write] = nums1[p1];
                p1++;
            } else if (nums1[p1] < nums2[p2]) {
                target[write] = nums1[p1];
                p1++;
            } else {
                target[write] = nums2[p2];
                p2++;
            }
            write++;
        }
        System.arraycopy(target, 0, nums1, 0, target.length);
    }


    /**
     * 88. 合并两个有序数组
     */
    public void merge01(int[] nums1, int m, int[] nums2, int n) {   //反向写入
        int write = m + n - 1;
        int p1 = m - 1;
        int p2 = n - 1;
        while (write >= 0) {
            if (p1 < 0) {
                nums1[write] = nums2[p2];
                p2--;
            } else if (p2 < 0) {
                nums1[write] = nums1[p1];
                p1--;
            } else if (nums1[p1] < nums2[p2]) {
                nums1[write] = nums2[p2];
                p2--;
            } else {
                nums1[write] = nums2[p1];
                p1--;
            }
            write--;
        }
    }


    /**
     * 45. 跳跃游戏 II
     * 本质是寻找最少的起跳点
     */
    public int jump(int[] nums) {  //反向查找
        int ans = 0;
        int end = nums.length - 1;
        while (end > 0) {   //不能有等号，否则死循环
            for (int i = 0; i < end; i++) {  //巧妙，正序查找，且不能从end起跳
                if (i + nums[i] >= end) {
                    end = i;   //更新下一轮循环的右侧边界
                    ans++;
                    break;
                }
            }
        }
        return ans;
    }

    public int jump01(int[] nums) {  //正向查找
        int ans = 0;
        int targetIndex = 0;
        int maxIndex = 0;
        for (int i = 0; i < nums.length - 1; i++) {  //不能从目标点起跳，否则就是原地跳
            maxIndex = Math.max(maxIndex, i + nums[i]);      //当前区间中，能跳跃的最远距离
            if (i == targetIndex) {  //移动至当前区间右边界
                targetIndex = maxIndex;    //更新下一个区间的终点，下一个区间的终点为当前区间内，能跳跃达到的最远距离
                ans++; //从当前区间中的选择的起跳点起跳，起跳点数增加
            }
        }
        return ans;
    }

    //--------------------------------------------------------------------------------
    // 上面的本质：在指定范围内寻找起跳点，使其能跳的最远，返回起跳点的个数（一定是能跳到数组最后一位）
    // 下面的本质：在指定范围内寻找起跳点，使其能跳的最远，判断最终是否能跳到最后一位，因为一个区间内的位点能跳到的位点不会比之前能到的更远，比如 [4,3,2,1,0,5]
    //
    // 对比上下两题的正序遍历解法，其中核心思路是一致的，都是更新最大可达的位置，差异在于：
    //     差异一：是否确定跳到最后一个位置，二者不一致
    //        下面的题目，不一定能跳到最后一个位置，即中间有部分区域无法跨越，此时通过 i > maxIndex 来判断即可，因为如果 i == maxIndex + 1 了，则是"越界"了，即超过了其可到达的位置
    //        上面的题目，明确给出一定能跳到最后一个位置，所以无需判断 i > maxIndex
    //     差异二：是否记录对应的最小跳跃步数
    //        上面的题目，本质是在寻找起跳点，每次触达栅栏时，将栅栏左侧能跳的最远的点作为起跳点，其中有三个跳跃场景：
    //            1、从 0 起跳：触发条件，记录步数，更新最远距离（其自身就是起跳点）
    //            2、区间中部碰到栅栏：触发条件，记录步数，更新最远距离（栅栏位置不一定是起跳点，可能是其左侧的某个索引位置）
    //            3、如果最后一个栅栏，在数组界外：注意，仔细思考区间内通过栅栏选择的最后一个起跳点一定能保证跳到最后一个位置，因为如果不能跳到，则说明这个栅栏和起跳点并非最后一个（题目保证一定能跳出）
    //--------------------------------------------------------------------------------


    /**
     * 55. 跳跃游戏
     */
    public boolean canJump(int[] nums) {  //正向更新目标位置
        int maxIndex = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > maxIndex) {   //当前搜索的位点已经超过可以到达的最远距离，即通过跳跃无法到达此处
                return false;
            }
            maxIndex = Math.max(maxIndex, i + nums[i]);   //动态更新当前可以到达的最远索引位置
        }
        return true;
    }

    public boolean canJump01(int[] nums) {  //反向
        int n = nums.length;
        //---------------------------------------------------------------------------------------
        // 核心思想是寻找当前目标点的起跳点，初始目标点为数组最后一位，最终判断起跳点是否为索引 0 的位置
        //---------------------------------------------------------------------------------------
        int startIndex = n - 1;
        for (int i = n - 2; i >= 0; i--) {
            if (i + nums[i] >= startIndex) {    //只有在当前位点可以到达目标索引处时，才会更新新的目标索引位置为当前点
                startIndex = i;
            }
        }
        return startIndex == 0;  //走到最后一位，能否从位点 0 开始起跳
    }


    public boolean canJump000(int[] nums) {  //比上复杂
        int endIndex = 0;
        int maxIndex = 0;
        for (int i = 0; i < nums.length; i++) {
            maxIndex = Math.max(maxIndex, i + nums[i]);
            if (i == endIndex) {
                if (maxIndex >= nums.length - 1) {
                    return true;
                }
                endIndex = maxIndex;
            }
        }
        return false;
    }

    public boolean canJump02(int[] nums) {   //基于所需要的步数
        int stepsNeed = 1;
        if (nums.length == 1) return true;
        for (int i = nums.length - 2; i >= 0; i--) {
            if (nums[i] < stepsNeed) {   //1.此位点无法跳到下一个目标位点
                stepsNeed++;                      //不更新目标位点：此时如果想要满足条件，则下一个位置就要有能力跨过当前位点，故需要多一步的跳跃数
            } else {                     //2.此位点可以跳到下一个目标位点
                stepsNeed = 1;                    //更新目标位点：此时下一个位置的目标位点就是"当前位点"，下一个位置只需要能够跳一步即可满足条件
            }
        }
        return nums[0] >= stepsNeed;
    }

    /**
     * 413. 等差数列划分
     */
    public int numberOfArithmeticSlices(int[] nums) {  //模拟，关键：以当前位点开头的等差序列的个数
        if (nums.length <= 2) return 0;
        int ans = 0;
        for (int i = 0; i < nums.length - 2; i++) {
            int diff = nums[i + 1] - nums[i];
            for (int j = i + 1; j < nums.length - 1; j++) {
                if (nums[j + 1] - nums[j] == diff) {
                    ans++;
                } else {
                    //---------------------------------------------------------------------------------------------------------------------
                    // 即使后面仍有等差为 diff 的序列，此处也不再计算，因此此处仅仅计算以 i 开头的差值为 diff 连续序列对应的个数，不计算以其他开头的
                    //---------------------------------------------------------------------------------------------------------------------
                    break;
                }
            }
        }
        return ans;
    }

    public int numberOfArithmeticSlices01(int[] nums) {   //动态规划，关键：以当前位点结束的等差序列的个数
        if (nums.length <= 2) return 0;
        int[] dp = new int[nums.length];  //dp[i] 代表以 i 结尾的等差序列的个数
        dp[0] = 0;
        dp[1] = 0;
        int diff = nums[1] - nums[0];
        for (int i = 2; i < nums.length; i++) {
            if (nums[i] - nums[i - 1] == diff) {
                dp[i] = dp[i - 1] + 1;   //关键
            } else {
                dp[i] = 0;
                diff = nums[i] - nums[i - 1];
            }
        }
        int sum = 0;
        for (int value : dp) {
            sum += value;
        }
        return sum;
//        return Arrays.stream(dp).sum();   //时间复杂度高
    }

    public int numberOfArithmeticSlices02(int[] nums) {
        if (nums.length <= 2) return 0;
        int sum = 0;
        int base = 0;
        int increase = 0;
        int diff = nums[1] - nums[0];
        int right = 2;
        while (right < nums.length) {
            if (nums[right] - nums[right - 1] == diff) {  //长度为这么长时，等差数列的个数
                increase++;         //构建增量
                base += increase;   //构建新的基准基于已有的 + 增量
            } else {
                sum += base;
                base = 0;
                increase = 0;
                diff = nums[right] - nums[right - 1];
            }
            right++;
        }
        return sum + base;
    }


    /**
     * 443. 压缩字符串
     */
    public int compress(char[] chars) {
        StringBuilder ans = new StringBuilder();
        int left = 0;
        int right = 0;
        while (right < chars.length - 1) {
            if (chars[right] != chars[right + 1]) {
                ans.append(chars[left]);
                int len = right - left + 1;
                if (len > 1) {
                    ans.append(len);
                }
                left = right + 1;
            }
            right++;
        }
        if (left == right && right == chars.length - 1) {
            ans.append(chars[right]);
        }
        if (left != right) {
            ans.append(chars[left]);
            int len = right - left + 1;
            if (len > 1) {
                ans.append(len);
            }
        }
        for (int i = 0; i < ans.length(); i++) {
            chars[i] = ans.charAt(i);
        }
        return ans.length();
    }


    /**
     * 395. 至少有 K 个重复字符的最长子串
     */
    public int longestSubstring(String str, int k) {
        return longestSubstringHelper(str, 0, str.length() - 1, k);
    }

    private int longestSubstringHelper(String str, int left, int right, int k) {
        //递归终止条件：整体长度不满足条件
        if (right - left + 1 < k) {
            return 0;
        }
        //记录当前字串中各个字符的频次，以此为基准进行切分，分治
        int[] freq = new int[26];
        for (int i = left; i <= right; i++) {
            freq[str.charAt(i) - 'a']++;
        }
        //跳过两端不满足情况的字符
        while (left < right && freq[str.charAt(left) - 'a'] < k) {
            left++;
        }
        while (left < right && freq[str.charAt(right) - 'a'] < k) {
            right--;
        }
        //横向搜索，寻找新的分割点
        for (int i = left; i <= right; i++) {
            if (freq[str.charAt(i) - 'a'] < k) {  //新的分割点
                return Math.max(longestSubstringHelper(str, left, i - 1, k), longestSubstringHelper(str, i + 1, right, k));
            }
        }
        return right - left + 1;  //运行到这里说明，当前字符串满足条件（上面没有分割点），直接返回此字符串的长度
    }

    public int longestSubstring01(String str, int k) {  //分割字符串
        if (str.length() < k) {
            return 0;
        }
        //每一个递归层，都要统计当前字符串中各个字符出现的频次，从而寻找分割位点，进行分治递归
        int[] freq = new int[26];
        for (int i = 0; i < str.length(); i++) {
            freq[str.charAt(i) - 'a']++;
        }
        for (int i = 0; i < freq.length; i++) {
            if (freq[i] != 0 && freq[i] < k) {  //分割位点
                int maxWindow = 0;
                for (String xx : str.split(String.valueOf((char) (i + 'a')))) {
                    maxWindow = Math.max(maxWindow, longestSubstring01(xx, k));
                }
                return maxWindow;
            }
        }
        //在上面没有进入递归，说明当前字符串中没有可分割的位点，同时长度也满足条件
        return str.length();
    }


    /**
     * 424. 替换后的最长重复字符
     */
    public int characterReplacement(String str, int k) {
        int left = 0;
        int right = 0;
        int maxFreq = 0;
        int maxWindow = 0;
        int[] freq = new int[26];
        while (right < str.length()) {
            maxFreq = Math.max(maxFreq, ++freq[str.charAt(right) - 'A']);
            while (maxFreq + k < right - left + 1) {
                maxFreq = Math.max(maxFreq, --freq[str.charAt(left) - 'A']);
                left++;
            }
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }

    /**
     * 面试题 01.09. 字符串轮转
     */
    public boolean isFlipedString(String s1, String s2) {
        return s1.length() == s2.length() && (s1 + s1).contains(s2);
    }


    /**
     * 594. 最长和谐子序列
     */
    public int findLHS(int[] nums) {
        int maxWindow = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int num : nums) {
            hTable.put(num, hTable.getOrDefault(num, 0) + 1);
        }
        for (int num : hTable.keySet()) {
            if (hTable.containsKey(num + 1)) {
                maxWindow = Math.max(maxWindow, hTable.get(num) + hTable.get(num + 1));
            }
        }
        return maxWindow;
    }

    public int findLHS001(int[] nums) {
        int left = 0;
        int right = 0;
        int maxWindow = 0;
        Arrays.sort(nums);
        while (right < nums.length) {
            while (left < right && nums[right] - nums[left] > 1) {  //不能写为 != 1，因为要考虑相等的情况
                left++;
            }
            if (nums[right] - nums[left] == 1) {   //必须判断
                maxWindow = Math.max(maxWindow, right - left + 1);
            }
            right++;
        }
        return maxWindow;
    }


    /**
     * 1103. 分糖果 II
     */
    public int[] distributeCandies(int candies, int num_people) {
        int[] ans = new int[num_people];
        int candyNeed = 1;
        int currentIndex = 0;
        while (candies > 0) {
            while (currentIndex < ans.length) {
                //可提供、需要的糖果数
                int candyNums = Math.min(candyNeed, candies);   //巧妙
                ans[currentIndex] += candyNums;
                candies -= candyNums;
                if (candies == 0) {
                    return ans;
                }
                candyNeed++;
                currentIndex++;
            }
            currentIndex = 0;  //新一轮的分发
        }
        return ans;
    }


    /**
     * 838. 推多米诺
     */
    public String pushDominoes(String dominoes) {   //2022-10-01 自己写了好久，终于对了
        dominoes = "L" + dominoes + "R";
        int left = 0;
        int right = 1;
        StringBuilder ans = new StringBuilder();

        //-------------------------------------------------------------
        // 针对区间 "m...n" 的情况，left 位于 m 点上，right 位于 n 点上
        //-------------------------------------------------------------

        while (right < dominoes.length()) {
            //记录非"."的牌
            while (right < dominoes.length() && dominoes.charAt(right) != '.') { //right 从 1 开始，不记录添加的左边界 "L"
                ans.append(dominoes.charAt(right));
                right++;
            }

            //记录区间左边界 left
            left = right - 1;  //"LR...LR"，循环终止条件为 right 在首个"."上，则 left 位于第一个 R 上

            //跳过"."
            while (right < dominoes.length() && dominoes.charAt(right) == '.') {
                right++;      //"LR...LR"，循环终止条件为 right 停留在第二个 L 上
            }

            //-------------------------------------------------------------
            // 当前状态 left 和 right 均位于区间"..."的两端上首个非 "." 上
            //-------------------------------------------------------------
            int points = right - left + 1 - 2;  //区间"."的长度，不考虑 left/right 点
            if (right < dominoes.length() && dominoes.charAt(left) == dominoes.charAt(right)) {  //"L..L"、"R..R"
                while (points > 0) {
                    ans.append(dominoes.charAt(right));  //左右一致
                    points--;
                }
            } else if (right < dominoes.length() && dominoes.charAt(left) == 'R' && dominoes.charAt(right) == 'L') {  //"R..L"
                for (int i = 0; i < points / 2; i++) {
                    ans.append("R");
                }
                if ((points & 1) == 1) {  //奇数
                    ans.append(".");
                }
                for (int i = 0; i < points / 2; i++) {
                    ans.append("L");
                }
            } else if (right < dominoes.length() && dominoes.charAt(left) == 'L' && dominoes.charAt(right) == 'R') {  //"L..R"
                while (points > 0) {
                    ans.append(".");
                    points--;
                }
            }
        }
        return ans.deleteCharAt(ans.lastIndexOf("R")).toString();  //剔除手工添加的右边界 "R"
    }

    /**
     * 777. 在LR字符串中交换相邻字符
     */
    public boolean canTransform(String start, String end) {
        //-----------------------------------------------------------------------------------------
        // 相当于在 start 中，以 X 作为坐标轴，L 可以向左侧移动、R 可以向右侧移动，从而与 end 各位进行匹配
        //-----------------------------------------------------------------------------------------
        int pos1 = 0;
        int pos2 = 0;
        while (pos1 < start.length() || pos2 < end.length()) {
            //在 start 和 end 中找到首个非 'X' 的字符
            while (pos1 < start.length() && start.charAt(pos1) == 'X') {
                pos1++;
            }

            while (pos2 < end.length() && end.charAt(pos2) == 'X') {
                pos2++;
            }

            if (pos1 == start.length() || pos2 == end.length()) {
                return pos1 == pos2;
            }

            if (start.charAt(pos1) != end.charAt(pos2)) {
                return false;  //"LRX" 无法移动获得 "RLX"，"LXR" 无法移动获得 "RXL"
            }

            //--------------------------------------------------------
            // 运行到这里说明，pos1 和 pos2 在 start 和 end对应的字符相等
            //--------------------------------------------------------

            if (start.charAt(pos1) == 'L' && pos1 < pos2) {
                return false;  //针对 start、end 中序号相等的 "L"，在 start中向左侧移动 "L" 无法获取 end 中的结果
            }

            if (start.charAt(pos1) == 'R' && pos1 > pos2) {
                return false;  //针对 start、end 中序号相等的 "R"，在 start中向右侧移动 "R" 无法获取 end 中的结果
            }
            pos1++;
            pos2++;
        }
        return pos1 == pos2;
    }

    /**
     * 539. 最小时间差
     */
    public int findMinDifference(List<String> timePoints) {  //自己做的，一把过，关键在于首尾时间差
        int ans = Integer.MAX_VALUE;
        int size = timePoints.size();
        int[] minuteNums = new int[size * 2];
        for (int i = 0; i < size; i++) {
            String[] split = timePoints.get(i).split(":");
            minuteNums[i] = Integer.parseInt(split[0]) * 60 + Integer.parseInt(split[1]);
            minuteNums[i + size] = minuteNums[i] + 24 * 60;
        }
        Arrays.sort(minuteNums);
        for (int i = 1; i < minuteNums.length; i++) {
            ans = Math.min(ans, minuteNums[i] - minuteNums[i - 1]);
        }
        return ans;
    }


    /**
     * 151. 反转字符串中的单词
     */
    public String reverseWords(String str) {
        str = str.trim();   //消除两端的空字符
        List<String> words = Arrays.asList(str.split("\\s+"));  //关键，多个空会一并剔除
        Collections.reverse(words);
        return String.join(" ", words);
    }


    /**
     * 220. 存在重复元素 III
     */
    public boolean containsNearbyAlmostDuplicate(int[] nums, int indexDiff, int valueDiff) {
        int left = 0;
        int right = 0;
        TreeSet<Long> treeSet = new TreeSet<>();
        while (right < nums.length) {
            if (right - left > indexDiff) {
                //---------------------------------------
                // 因为存在重复的元素，考虑会不会删除掉正好满足条件的元素，其实不会的，因为只要满足条件就直接返回 true 不会再朝后执行了
                //---------------------------------------
                treeSet.remove((long) nums[left]);
                left++;
            }
            Long target = (long) nums[right];
            Long floor = treeSet.floor(target);
            Long ceiling = treeSet.ceiling(target);
            if (floor != null && Math.abs(floor - target) <= valueDiff)
                return true;
            if (ceiling != null && Math.abs(ceiling - target) <= valueDiff)
                return true;
            treeSet.add((long) nums[right]);
            right++;
        }
        return false;
    }

    public boolean containsNearbyAlmostDuplicate01(int[] nums, int indexDiff, int valueDiff) {
        //桶排序
        HashMap<Long, Long> bucketsMap = new HashMap<>();
        long size = valueDiff + 1;
        for (int i = 0; i < nums.length; i++) {
            long bucketID = getBucketID((long) nums[i], size);
            //1.1 当前桶
            if (bucketsMap.containsKey(bucketID)) {
                return true;
            }
            //1.2 相邻桶（左侧）
            if (bucketsMap.containsKey(bucketID - 1) && Math.abs(bucketsMap.get(bucketID - 1) - nums[i]) <= valueDiff) {
                return true;
            }
            //1.3 相邻桶（右侧）
            if (bucketsMap.containsKey(bucketID + 1) && Math.abs(bucketsMap.get(bucketID + 1) - nums[i]) <= valueDiff) {
                return true;
            }

            //2 维护窗口内的桶
            bucketsMap.put(bucketID, (long) nums[i]);
            if (i >= indexDiff) {  //要有等号，下一轮循环不再需要这个桶了
                bucketsMap.remove(getBucketID((long) nums[i - indexDiff], size));
            }
        }
        return false;
    }

    private long getBucketID(long nums, long size) {
        return nums >= 0 ? nums / size : ((nums + 1) / size) - 1;
    }


    /**
     * 921. 使括号有效的最少添加
     */
    public int minAddToMakeValid(String str) {
        int ans = 0;
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < str.length(); i++) {
            if (!stack.isEmpty() && stack.peek() == '(' && str.charAt(i) == ')') {  //可以抵消
                stack.pop();  //移除对应的括号
                continue;
            }
            stack.add(str.charAt(i));
        }
        return stack.size();
    }

    public int minAddToMakeValid01(String str) {
        int ans = 0;
        int score = 0;
        //-----------------------------------------------------
        // 将「有效括号问题」转化为「分值有效性」的数学判定
        //     score 动态记录处理过程中的得分，将 '(' 记为 +1，将 ')' 记为 -1
        //     一个有效的括号应当在整个过程中不出现负数，因此一旦 score 出现负数，我们需要马上增加 ( 来确保合法性；当整个 s 处理完后，还需要添加 socre 等同的 ) 来确保合法性
        //-----------------------------------------------------
        for (int i = 0; i < str.length(); i++) {
            score += str.charAt(i) == '(' ? 1 : -1;
            if (score < 0) {  //缺少左括号
                score = 0;
                ans += 1;
            }
        }
        return ans + score;  //缺少右括号
    }


    public int minAddToMakeValid02(String str) {  //错误写法，案例 "()))(("
        int left = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') left++;
        }
        int right = str.length() - left;
        return Math.abs(right - left);
    }


    /**
     * 875. 爱吃香蕉的珂珂
     */
    public int minEatingSpeed(int[] piles, int targetHour) {
        int left_K = 1;
        int right_K = Arrays.stream(piles).max().getAsInt();
        while (left_K <= right_K) {
            int mid_K = left_K + ((right_K - left_K) >> 1);
            if (minEatingSpeedBinSearch(piles, mid_K) <= targetHour) {
                right_K = mid_K - 1;
            } else {
                left_K = mid_K + 1;
            }
        }
        //----------------------------------------
        // 首先，left_K 右侧不会越界
        // 其次，内部不存在 targetHour，left_K 停留在右侧（函数递减，故右侧为小于 targetHour的最大 K 值）
        // 最后，内部存在多个 targetHour（多个 K 均对应 targetHour），left_K 停留在最左侧 targetHour的索引处，由于（函数递减，故左侧的 K 最小）
        //----------------------------------------
        return left_K;
    }

    private long minEatingSpeedBinSearch(int[] piles, int mid_K) {  //返回当前 K 对应的时间
        long currentHours = 0;  //一定是长整型，否则会溢出
        for (int pile : piles) {
            currentHours += (pile / mid_K);
            currentHours += (pile % mid_K == 0 ? 0 : 1);
        }
        return currentHours;
    }

    //----------------------------------------------------------------------------
    // 注意：上下两题的二分写法，本质都是两个变量间的函数，而且是递减函数
    //----------------------------------------------------------------------------

    /**
     * 1011. 在 D 天内送达包裹的能力
     */
    public int shipWithinDays(int[] weights, int days) {
        //--------------------------------------------
        // left、right、mid 均代表 船的运载能力
        //--------------------------------------------
        int left = 0;
        int right = 0;
        for (int weight : weights) {
            left = Math.max(left, weight);   //船的最小运载能力，要能装下单个最重的货物
            right += weight;                 //船的最大运载能力，可以一次运送所有的获取
        }
        //---------------------------------------------------------------
        // 场景理解：
        //     left 和 right 为搜索区间中的索引
        //     currentNeedDays(weights, mid) 类似于 nums[i] 为索引对应的值，但与常规不同的是，此处 nums[i] 其实是降序排列而非升序排列
        // 此题关键：
        //     如果多种船的运载能力都满足条件，则二分搜索取最小的运载能力，所以关键在于 逻辑判断时等号的位置 以及 哪个作为返回值
        //---------------------------------------------------------------
        while (left <= right) {
            int mid = left + ((right - left) >> 1);  //当前校验的运载能力
            if (currentNeedDays(weights, mid) <= days) {
                right = mid - 1;  //降低船的运载能力，从而增加运送天数
            } else {
                left = mid + 1;
            }
        }
        //-------------------------------------------------
        // 分析二分搜索的三种情况：
        //     1、越界
        //          left左侧不会越界：
        //          left右侧越界：
        //              本题中，右侧不会越界，根据题意由于 days >= 1 ，同时 right 为所有货物的总重量，所以 起码保证 left 的移动区间内能使得
        //     2、界内
        //          元素存在且唯一：直接返回
        //          元素存在但重复：返回最左侧满足条件的索引，即运载能力
        //          元素不存在：    此时 left 正好停留在 能使得 currentDay < days 的最小运载量（原因：区间内元素值降序），画图更直观
        //-------------------------------------------------
        return left;
    }


    private int currentNeedDays(int[] weights, int weight) {
        int currentWeight = 0;
        int currentNeedDays = 0;
        for (int i = 0; i < weights.length; i++) {
            currentWeight += weights[i];
            if (currentWeight > weight) {
                currentNeedDays++;
                currentWeight = 0;
                i -= 1;  //回退一位
            } else if (currentWeight == weight || i == weights.length - 1) {
                currentNeedDays++;
                currentWeight = 0;   //重置
            }
        }
        return currentNeedDays;
    }


    private int currentNeedDays00(int[] weights, int weight) {
        int currentWeight = 0;
        int currentNeedDays = 0;
        for (int i = 0; i < weights.length; i++) {
            currentWeight += weights[i];
            if (currentWeight == weight || i == weights.length - 1) {  //错误写法，即当 i == weights.length - 1 和 currentWeight > weight 同时满足条件时
                currentNeedDays++;
                currentWeight = 0;   //重置
            } else if (currentWeight > weight) {
                currentNeedDays++;
                currentWeight = 0;
                i -= 1;  //回退一位
            }
        }
        return currentNeedDays;
    }

    public int shipWithinDays00(int[] weights, int days) {   //错误写法，存在很多问题
        //--------------------------------------------
        // left、right、mid 均代表 船的运载能力
        //--------------------------------------------
        int left = 0;
        int right = 0;
        for (int weight : weights) {
            left = Math.max(left, weight);   //船的最小运载能力，要能装下单个最重的货物
            right += weight;                 //船的最大运载能力，可以一次运送所有的获取
        }
        int ans = left;
        while (left <= right) {
            if (currentNeedDays(weights, left) == days) {   //可能没有任何一种运载能力使得所需的运送天数等于要求的
                return left;
            } else if (currentNeedDays(weights, left) > days) {
                return left - 1;
            }
            left++;
        }
        return ans;
    }

    /**
     * 1894. 找到需要补充粉笔的学生编号
     */
    public int chalkReplacer(int[] chalk, int k) {  //时间复杂度高，但可以通过
        while (true) {
            for (int i = 0; i < chalk.length; i++) {
                k -= chalk[i];
                if (k < 0) return i;
            }
        }
    }

    public int chalkReplacer01(int[] chalk, int k) {  //优化，只关注最后一次遍历
        int sum = 0;
        for (int i = 0; i < chalk.length; i++) {
            sum += chalk[i];
            if (sum > k) return i;
        }
        int currentK = k % sum;
        for (int i = 0; i < chalk.length; i++) {
            currentK -= chalk[i];
            if (currentK < 0) return i;
        }
        return -1;
    }

    public int chalkReplacer02(int[] chalk, int k) {  //进一步优化，只关注最后一次遍历，且最后一次遍历不顺序遍历，而是基于二分搜索
        int sum = 0;
        int[] prefix = new int[chalk.length];
        for (int i = 0; i < chalk.length; i++) {
            sum += chalk[i];
            prefix[i] = sum;  //前缀和
            if (sum > k) return i;
        }
        int target = k % sum;
        int left = 0;
        int right = prefix.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (prefix[mid] < target) {
                left = mid + 1;
            } else if (target <= prefix[mid]) {
                right = mid - 1;
            }
        }
        if (prefix[left] == target) {
            return left + 1;   //右侧不会越界，越界说明 left == nums.length - 1，如果这样，则整个应该是一轮整体的循环，在上面取余的时候会被抹掉
        }
        return left;   //区间内大与 target 的位置
    }


    /**
     * 33. 搜索旋转排序数组
     */
    public int search0(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target)
                return mid;
            if (nums[left] == target)
                return left;
            if (nums[right] == target)
                return right;
            if (nums[left] <= nums[mid]) {    //左侧有序，等号的目的主要是针对左侧区间长度为 1 的情况，原因是 计算 mid 时的左侧偏移
                if (nums[left] < target && target < nums[mid]) {   //区间内
                    right = mid - 1;
                } else {                                           //区间外
                    left = mid + 1;
                }
            }
            if (nums[mid] < nums[right]) {   //右侧有序，不用加等号，因为右侧区间不会有 1 的情况，如果想要有等号，可以将 mid 计算的时候右侧偏移
                if (nums[mid] < target && target < nums[right]) {  //区间内
                    left = mid + 1;
                } else {                                           //区间外
                    right = mid - 1;
                }
            }
        }
        return -1;
    }


    /**
     * 1541. 平衡括号字符串的最少插入次数
     */
    public int minInsertions(String str) {
        int left = 0;
        int right = 0;
        Stack<Character> stack = new Stack<>();  //只记录左括号
        for (int i = 0; i < str.length(); i++) {
            char xx = str.charAt(i);
            if (xx == '(') {
                stack.add(xx);
            } else {          //当前为右侧括号
                //1. 专注于判断后一位是否为右侧括号
                if (i < str.length() - 1) {
                    if (str.charAt(i + 1) == ')') {  //1.1 自身存在两个连续的右括号，抵消一个左括号
                        i++;          //跳过下一位
                    } else {                         //1.2 自身不存在两个连续的右括号，添加一个，进入下一个配对循环
                        right++;      //缺少一个右侧括号
                    }
                } else {
                    right++;          //缺少一个右侧括号
                }

                //2.专注于判断前一位是否存在左括号
                if (!stack.isEmpty()) {
                    stack.pop();  //配对
                } else {
                    left++;       //添加一个左括号
                }
            }
        }
        return left + right + 2 * stack.size();
    }


    public int minInsertions00(String str) {
        int leftNums = 0;
        int leftNeed = 0;
        int rightNeed = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                leftNums++;
            } else {
                //1.专注于左侧括号
                if (leftNums > 0) {
                    leftNums--;
                } else {
                    leftNeed++;
                }

                //2.专注于右侧括号
                if (i < str.length() - 1) {
                    if (str.charAt(i + 1) == ')') {
                        i++;          //下一位是右括号，则用下一位与之配对，故跳过下一位
                    } else {
                        rightNeed++;  //下一位不是右括号，则插入一个右括号
                    }
                } else {
                    rightNeed++;
                }
            }
        }
        return leftNeed + rightNeed + 2 * leftNums;
    }


    public int minInsertions01(String s) {   //没太理解
        int res = 0, need = 0;//res 记录人为添加的左右括号维护括号平衡；need记录需要的右括号
        for (char c : s.toCharArray()) {
            if (c == '(') {
                need += 2;
                //因为右括号数量必须是偶数，所以当遇到左括号时判断need，如果是奇数则添加一个右括号以平衡，添加之后，need--
                if (need % 2 == 1) {
                    res++;
                    need--;
                }
            } else {//碰到右括号
                //右括号需求-1
                need--;
                //如果需求剪到负，说明需要添加一个左括号保持满足规则，又因为一个左括号匹配两个右，于是need=1
                if (need == -1) {
                    need = 1;//右括号需求由 -1--->+1
                    res++;//添加一个左以满足规则
                }
            }
        }
        return res + need;
    }


    /**
     * 1037. 有效的回旋镖
     */
    public boolean isBoomerang(int[][] points) {
        int[] xy1 = points[0];
        int[] xy2 = points[1];
        int[] xy3 = points[2];
//        return (xy2[1] - xy1[1]) / (xy2[0] - xy1[0]) != (xy3[1] - xy1[1]) / (xy3[0] - xy1[0]);
        return (xy2[1] - xy1[1]) * (xy3[0] - xy1[0]) != (xy3[1] - xy1[1]) * (xy2[0] - xy1[0]);
    }


    /**
     * 392. 判断子序列
     */
    public boolean isSubsequence(String s, String t) { //进阶问题的解决
        t = " " + t;
        int n = t.length();
        int[][] dp = new int[n][26];
        for (char ch = 'a'; ch <= 'z'; ch++) {
            int nextIndex = -1;   //表示不存在
            for (int i = n - 1; i >= 0; i--) {   //dp[i][j]表示字符串 t 的第 i 位（不含自身）后，字符 j 出现的最近索引
                dp[i][ch - 'a'] = nextIndex;
                if (t.charAt(i) == ch) {         //当前字符如果是目标字符，更新nextIndex，下一个字符使用时会使用，当前字符不会使用
                    nextIndex = i;
                }
            }
        }
        int currentIndex = 0; //初始化为 0，也与 t = " " + t 相互映，表示 0 索引后首个字符 ch 出现的位置索引，更新 currentIndex
        for (int i = 0; i < s.length(); i++) {
            currentIndex = dp[currentIndex][s.charAt(i) - 'a'];
            if (currentIndex == -1) return false;
        }
        return true;
    }


    /**
     * 792. 匹配子序列的单词数
     */
    public int numMatchingSubseq(String str, String[] words) {  //基于桶的想法
        //----------------------------------------------------------
        // 基于桶的想法，使用 26个桶分别记录以此桶开头的字符串
        // 逐个遍历 str 的各个字符，依次更新每个字符对应桶中的字符串，将其截取开头一个字符后，仍按照开头字符存入对应的桶中
        // 直至字符串 word 的长度为 1，则代表可以作为 str 的子序列
        //----------------------------------------------------------
        int ans = 0;
        ArrayDeque<String>[] bucket = new ArrayDeque[26];  //记录以 'a'-'z' 开头的字符串
        for (int i = 0; i < 26; i++) {
            bucket[i] = new ArrayDeque<>();
        }
        for (String word : words) {
            bucket[word.charAt(0) - 'a'].addLast(word);
        }
        for (char ch : str.toCharArray()) {
            ArrayDeque<String> queue = bucket[ch - 'a'];
            int size = queue.size();
            //类似广度优先搜索，因为每次仅处理当前队列中的元素，由于本轮循环倒置此队列中新增的元素，不做处理
            for (int i = 0; i < size; i++) {   //不能写为 i < queue.size() 原因在于 queue 会一直在变化
                String current = queue.pollFirst();
                //当前字符 ch 被匹配
                if (current.length() == 1) {  //长度为 1，则当前原始字符串为一个子序列
                    ans++;
                } else {
                    bucket[current.charAt(1) - 'a'].addLast(current.substring(1));  //匹配一位，从第二位将其放入对应的桶中
                }
            }
        }
        return ans;
    }


    public int numMatchingSubseq01(String str, String[] words) {  //基于特殊的后缀数组
        int ans = 0;
        str = " " + str;
        int n = str.length();
        int[][] dp = new int[n][26];
        for (char ch = 'a'; ch <= 'z'; ch++) {
            int nextIndex = -1;
            for (int i = n - 1; i >= 0; i--) {
                dp[i][ch - 'a'] = nextIndex;
                if (str.charAt(i) == ch) nextIndex = i;
            }
        }
        for (String word : words) {
            int nextIndex = 0;
            for (char ch : word.toCharArray()) {
                nextIndex = dp[nextIndex][ch - 'a'];
                if (nextIndex == -1) break;
            }
            if (nextIndex != -1) ans++;
        }
        return ans;
    }

    public int numMatchingSubseq02(String str, String[] words) {  //基于二分
        int ans = words.length;
        ArrayList<ArrayList<Integer>> bucket = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            bucket.add(new ArrayList<>());
        }
        for (int i = 0; i < str.length(); i++) {
            bucket.get(str.charAt(i) - 'a').add(i);
        }
        for (String word : words) {
            if (word.length() > str.length()) {
                ans--;
                continue;
            }
            int currentIndex = -1;
            for (int i = 0; i < word.length(); i++) {
                char ch = word.charAt(i);
//                if (bucket.get(ch - 'a').isEmpty() || bucket.get(ch - 'a').get(bucket.get(ch - 'a').size() - 1) <= currentIndex) {
                if (bucket.get(ch - 'a').isEmpty()) {
                    ans--;
                    break;
                }
                currentIndex = subSeqHelper(bucket.get(ch - 'a'), currentIndex);
                if (currentIndex == -1) {
                    ans--;
                    break;
                }
            }
        }
        return ans;
    }

    private int subSeqHelper(ArrayList<Integer> nums, int target) {
        int left = 0;
        int right = nums.size() - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums.get(mid) < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        if (left == nums.size()) return -1;
        if (nums.get(left) == target) { //相等则向后取一位
            return left + 1 < nums.size() ? nums.get(left + 1) : -1;
        }
        return nums.get(left);
    }

    //----------------------------------------------------------------------------------
    // 上下两个题包含了相似的预处理，分别为：
    //    1、记录当前位点后，各个字符下次出现的索引位置
    //    2、记录当前位点后，最小的字符
    //----------------------------------------------------------------------------------

    /**
     * 6202. 使用机器人打印字典序最小的字符串
     */
    public String robotWithString(String str) {
        int n = str.length();
        StringBuilder ans = new StringBuilder();
        int[] dp = new int[n];
        dp[n - 1] = 'z';
        for (int i = n - 2; i >= 0; i--) {
            dp[i] = Math.min(str.charAt(i + 1), dp[i + 1]);   //dp[i]表示索引位置 i 后最小的字符
        }
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            stack.add(str.charAt(i));   //1、无论怎样，先将其放入栈中
            while (!stack.isEmpty() && stack.peek() <= dp[i]) {   //2、以当前位点右侧为挡板，判断栈中元素与当前为点右侧数组中字符的大小
                ans.append((stack.pop()));
            }
        }
        while (!stack.isEmpty()) {
            ans.append((stack.pop()));
        }
        return ans.toString();
    }

    /**
     * 915. 分割数组
     */
    public int partitionDisjoint(int[] nums) {
        int n = nums.length;
        int[] after = new int[n]; //dp[i] 在 i 位后最小的值
        after[n - 1] = Integer.MAX_VALUE;
        for (int i = n - 2; i >= 0; i--) {
            after[i] = Math.min(after[i + 1], nums[i + 1]);
        }
        int max = Integer.MIN_VALUE;
        int left = 0;
        while (left < n) {
            max = Math.max(max, nums[left]);
            if (max <= after[left]) {
                return left + 1;
            }
            left++;
        }
        return -1;
    }

    /**
     * 870. 优势洗牌
     */
    public int[] advantageCount(int[] nums1, int[] nums2) {  //田忌赛马
        int[] ans = new int[nums1.length];
        Integer[] index = new Integer[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            index[i] = i;
        }
        Arrays.sort(nums1);                                     //nums1 升序排序
        Arrays.sort(index, (o1, o2) -> nums2[o1] - nums2[o2]);  //nums2 升序排序的索引
        int left = 0;                  //记录 nums2 中最小值的索引
        int right = index.length - 1;  //记录 nums2 中最大值的索引
        for (int num : nums1) {   //田忌赛马，逐一对比
            //-----------------------------------------------
            // nums1 中从下等马 到 上等马，依次遍历，选取其与 nums2 中马的配对方案，配对规则：
            //   每次都是 num1 中的下等马与 num2 中的下等马进行比较：
            //       1、干得过，配对成功，两个下等马匹配
            //       2、干不过，配对失败，用 nums1 当前的下等马，去和 nums2 中的最上等的马（贪心）
            //    注意：nums2中各个元素不动，要通过将 nums1 中当前的下等马，放置在对应配对元素的索引中
            //-----------------------------------------------
            int i = num > nums2[index[left]] ? index[left++] : index[right--];
            ans[i] = num;
        }
        return ans;
    }

    /**
     * 769. 最多能完成排序的块
     */
    public int maxChunksToSorted(int[] arr) {
        int ans = 0;
        int rightEdge = 0;
        for (int i = 0; i < arr.length; i++) {
            rightEdge = Math.max(rightEdge, arr[i]);
            if (rightEdge == i) ans++;
        }
        return ans;
    }


    int ans = 0;
    int currentIndex = 0;

    /**
     * 902. 最大为 N 的数字组合
     */
    public int atMostNGivenDigitSet(String[] digits, int n) {
        String str = String.valueOf(n);
        int nums = digits.length;
        //1、计算长度小于目标的数字个数
        for (int i = 1; i < str.length(); i++) {
            ans += (int) Math.pow(nums, str.length() - i);  //累加长度为 str.length() - i 的数字个数，每一位可以为 digits 中的任意一个元素
        }
        //2、计算长度等于目标的满足条件（小于等于 n）的数字的个数
        atMostNGivenDigitSetDfs(digits, str, nums);  //递归处理和累加
        return ans;
    }

    //针对长度相等的组合数字
    private void atMostNGivenDigitSetDfs(String[] digits, String str, int nums) {
        //递归终止条件
        if (currentIndex == str.length()) {
            ans++;    //一摸一样
            return;
        }
        char xx = str.charAt(currentIndex);
        for (String digit : digits) {  //非递减顺序排列
            //----------------------------------------------
            // 按顺序处理：
            //   1、针对当前位置小于目标中对应位置的情况，直接累加计算
            //   2、针对当前位置大于目标中对应位置的情况，直接结束
            //   3、针对当前位置等于目标中对应位置的情况，递归类似的处理，比较下一位与目标中对应位置的情况
            //----------------------------------------------
            if (digit.charAt(0) < xx) {
                ans += (int) Math.pow(nums, str.length() - 1 - (currentIndex + 1) + 1);  //-1是不包含当前位置的情况，上面的是包含当前位置的情况
            } else if (xx == digit.charAt(0)) {
                ++currentIndex;
                atMostNGivenDigitSetDfs(digits, str, nums);  //比较下一位的情况
            }
        }
    }


    /**
     * 1700. 无法吃午餐的学生数量
     */
    public int countStudents(int[] students, int[] sandwiches) {
        int currentIndex = 0;
        ArrayDeque<Integer> dequeQueue = new ArrayDeque<>();
        for (int digit : students) {
            dequeQueue.addLast(digit);
        }
        while (currentIndex < sandwiches.length) {
            while (!dequeQueue.isEmpty() && dequeQueue.peekFirst() == sandwiches[currentIndex]) {
                dequeQueue.pollFirst();  //移除
                currentIndex++;
            }
            if (!dequeQueue.isEmpty() && dequeQueue.peekFirst() != sandwiches[currentIndex]) {  //耗时
                dequeQueue.addLast(dequeQueue.pollFirst());  //队首移动至队尾
                HashSet<Integer> distinct = new HashSet<>(dequeQueue);  //查看当前队列中是否所有人都喜欢同一个三明治类型
                if (distinct.size() == 1) return dequeQueue.size();
            }
        }
        return dequeQueue.size();
    }

    public int countStudents01(int[] students, int[] sandwiches) {
        int[] buckets = new int[2];
        for (int digit : students) {  //所有学生都有机会尝试一次当前的三明治，所以将其存储起来
            buckets[digit]++;
        }
        for (int currentType : sandwiches) {   //顺序处理待分配的三明治
            if (buckets[currentType] > 0) buckets[currentType]--; //学生领取当前三明治，并移除队列，即需要此类型三明治的学生减少一位
            else break;  //否则，当前待分配的三明治，没有一个学生喜欢，每人会认领，则直接退出循环
        }
        return buckets[0] + buckets[1]; //至少有一个是 0
    }


    /**
     * 121. 买卖股票的最佳时机
     */
    public int maxProfitI(int[] prices) {    //以当前位点为最小值来处理，需要预计算当前位点右侧的最大值
        int n = prices.length;
        int[] rightMax = new int[n];
        rightMax[n - 1] = prices[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], prices[i + 1]);
        }
        int ans = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            ans = Math.max(ans, rightMax[i] - prices[i]);
        }
        return Math.max(ans, 0);
    }

    public int maxProfitI01(int[] prices) {   //以当前位点为最大值来处理，记录左侧遍历过程中的最小值
        int maxProfit = 0;
        int minPrice = prices[0];
        for (int price : prices) {
            if (price > minPrice) {
                maxProfit = Math.max(maxProfit, price - minPrice);
            } else {
                minPrice = price;
            }
        }
        return maxProfit;
    }

    public int maxProfitI02(int[] prices) {  //简化上个写法
        int maxProfit = 0;
        int min = prices[0];
        for (int price : prices) {
            min = Math.min(min, price);
            maxProfit = Math.max(maxProfit, price - min);
        }
        return maxProfit;
    }

    public int maxProfitII02(int[] prices) {  //基于优先队列来做
        int maxProfit = 0;
        PriorityQueue<Integer> sortedQueue = new PriorityQueue<>((o1, o2) -> o1 - o2);  //小根堆
        for (int price : prices) {
            if (!sortedQueue.isEmpty()) {
                maxProfit = Math.max(maxProfit, price - sortedQueue.peek());
            }
            sortedQueue.add(price);
        }
        return maxProfit;
    }


    /**
     * 122. 买卖股票的最佳时机 II
     */
    public int maxProfitII(int[] prices) {  //爬山，只记录上山的价值
        int maxProfit = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i - 1] < prices[i]) {
                maxProfit += prices[i] - prices[i - 1];
            }
        }
        return maxProfit;
    }

    public int maxProfitII01(int[] prices) {  //爬山，只记录上山的价值
        int maxProfit = 0;
        for (int i = 1; i < prices.length; i++) {
            int diff = prices[i] - prices[i - 1]; //分段累加也可以
            maxProfit += Math.max(diff, 0);
        }
        return maxProfit;
    }


    /**
     * 123. 买卖股票的最佳时机 III
     */
    public int maxProfitIII01(int[] prices) {   //最多可以完成 两笔 交易
        //-------------------------------------------------
        // 分别以每天为分割点，计算之前和之后可获得的最大利润，二者之和即为最多买卖两次股票可获得最大收益
        //-------------------------------------------------
        int n = prices.length;
        int[] before = new int[n];
        int[] after = new int[n];
        int minPrice = prices[0];  //最低点
        //从左至右，计算截至当前可获得的最大利润
        for (int i = 1; i < n; i++) {
            if (prices[i] > minPrice) {
                before[i] = Math.max(before[i - 1], prices[i] - minPrice);  //利润为：当前价格 - 之前最低价
            } else {  //等号写那里无所谓
                minPrice = prices[i];
                before[i] = before[i - 1];
            }
        }
        int maxPrice = prices[n - 1];  //最高点
        //从右至左，计算从当天至最终可获得的最大利润
        for (int i = n - 2; i >= 0; i--) {
            if (prices[i] < maxPrice) {
                after[i] = Math.max(after[i + 1], maxPrice - prices[i]);    //利润为：后面的最高价格 - 当前价格 （因为是反向遍历的，但时间其实是正向流逝的）
            } else {
                maxPrice = prices[i];
                after[i] = after[i + 1];
            }
        }
        int ans = 0;
        //逐天计算当天前可获得的最大利润以及当天后可获得的最大利润
        for (int i = 0; i < n; i++) {
            ans = Math.max(ans, before[i] + after[i]);
        }
        return ans;
    }

    //--------------------------------------------------------------------------------
    // 下面介绍几种动态规划的写法
    //--------------------------------------------------------------------------------

    public int maxProfit30(int[] prices) {  //动态规划
        int buyDay1 = Integer.MIN_VALUE;
        int selDay1 = 0;
        int buyDay2 = Integer.MIN_VALUE;
        int selDay2 = 0;
        for (int day = 0; day < prices.length; day++) {  //均从营收视角看待买卖
            buyDay1 = Math.max(buyDay1, 0 - prices[day]);
            selDay1 = Math.max(selDay1, buyDay1 + prices[day]);
            buyDay2 = Math.max(buyDay2, selDay1 - prices[day]);
            selDay2 = Math.max(selDay2, buyDay2 + prices[day]);
        }
        return selDay2;
    }

    //--------------------------------------------------------------------------------
    // 上下两种写法关键在于 把支出当成正数处理还是负数处理，即是否站在"纯营收的视角"
    //--------------------------------------------------------------------------------

    public int maxProfit000(int[] prices) {
        int buyDay1 = Integer.MAX_VALUE;
        int selDay1 = 0;
        int buyDay2 = Integer.MAX_VALUE;
        int selDay2 = 0;
        for (int i = 0; i < prices.length; i++) {
            buyDay1 = Math.min(buyDay1, prices[i]);
            selDay1 = Math.max(selDay1, prices[i] - buyDay1);
            buyDay2 = Math.min(buyDay2, prices[i] - selDay1);
            selDay2 = Math.max(selDay2, prices[i] - buyDay2);
        }
        return selDay2;
    }

    public int maxProfit00(int[] prices) {  //动态规划
        int buyDay1 = prices[0];
        int selDay1 = 0;
        int buyDay2 = prices[0];
        int selDay2 = 0;
        for (int i = 1; i < prices.length; i++) {
            buyDay1 = Math.min(buyDay1, prices[i]);             //买入 1 次后的最小花费（支出）  完全是透支
            selDay1 = Math.max(selDay1, prices[i] - buyDay1);   //买卖 1 次后的最大利润（营收）
            buyDay2 = Math.min(buyDay2, prices[i] - selDay1);   //买入 2 次后的最小花费（净支出）手里有钱（之前的一次买卖的营收）的情况下，当前股票的抵消后的价格
            selDay2 = Math.max(selDay2, prices[i] - buyDay2);   //买卖 2 次后的最大利润（净营收）
        }
        return selDay2;
    }

    public int maxProfit01(int[] prices) {  //动态规划
        if (prices.length == 1) return 0;
        if (prices.length == 2 && prices[1] - prices[0] >= 0) return prices[1] - prices[0];
        int[][] dp = new int[prices.length][5];
        //-----------------------------------------------------------------------------
        // 定义 5 种状态： 0:无操作、1:第一次买入、2：第一次卖出、3:第二次买入、4:第二次卖出
        //-----------------------------------------------------------------------------
        dp[0][1] = -prices[0];
        dp[0][3] = -prices[0];
        for (int i = 1; i < prices.length; i++) {
            //----------------------------------------------------------
            // 买入为负数、卖出为正数，将其买卖均理解为 利润，故均使用最大值，想要使得利润最大
            //----------------------------------------------------------
            dp[i][1] = Math.max(dp[i - 1][1], dp[i][0] - prices[i]);   //支出（一定是一个负数，将其理解为利润，首次买入/支出为负数，取最大值，其实是求最小支出，即最低的价格）
            dp[i][2] = Math.max(dp[i - 1][2], dp[i][1] + prices[i]);   //净利润
            //第二次买入要减去第一次赚的利润,相当于降低第二次买入的成本了,所有的利润都叠加到第二次交易了
            dp[i][3] = Math.max(dp[i - 1][3], dp[i][2] - prices[i]);   //二次支出（净利润，正负不一定）
            dp[i][4] = Math.max(dp[i - 1][4], dp[i][3] + prices[i]);   //净利润
        }
        return dp[prices.length - 1][4];
    }

    public int maxProfit02(int[] prices) {  //自己写的，有点问题 案例 int[] prices = {1, 2, 4, 2, 5, 7, 2, 4, 9, 0};
        if (prices.length == 1) return 0;
        if (prices.length == 2 && prices[1] - prices[0] >= 0) return prices[1] - prices[0];
        int maxProfit = 0;
        //不能企图让两个端点为山峰、山谷、平地，均会有问题
        //存储连续上升空间
        ArrayList<Integer> sortedQueue = new ArrayList<>();
        int left = 0;
        int right = 0;
        while (right < prices.length - 1) {
            if (right > 0 && prices[right - 1] < prices[right] && prices[right] > prices[right + 1]) { //1、判断刚开始下山，山顶为 right
                int currentProfit = prices[right] - prices[left];
                sortedQueue.add(currentProfit);
            } else if (right > 0 && prices[right - 1] >= prices[right] && prices[right] <= prices[right + 1]) {  //2、判断刚刚上山，谷底为 right
                left = right;  //动态记录谷底
            }
            right++;
        }
        //最后一个端点的判断
        if (prices[prices.length - 1] > prices[prices.length - 2]) {  //如果为山峰的化，需要单独处理
            int currentProfit = prices[right] - prices[left];
            sortedQueue.add(currentProfit);
        }
        sortedQueue.sort((o1, o2) -> o2 - o1);
        if (sortedQueue.size() == 0) return 0;
        if (sortedQueue.size() == 1) return sortedQueue.get(0);
        return sortedQueue.get(0) + sortedQueue.get(1);
    }


    /**
     * 188. 买卖股票的最佳时机 IV
     */
    public int maxProfit(int K, int[] prices) {   //最多可以完成 K 笔交易
        int n = prices.length;
        //-------------------------------------------------
        // dp[d][k][z]状态定义：
        //    1、dp[d][k][0] 前 d 天完成 k 次买卖，并且当前手中不持有股票时的最大收益
        //    2、dp[d][k][1] 前 d 天完成 k 次买卖，并且当前手中持有股票时的最大收益
        //-------------------------------------------------
        int[][][] dp = new int[n][K + 1][2];   //关键： K + 1 的原因是 0 -> 1 是完成了一次买卖 1 -> 2 是完成了第二次买卖
        for (int k = 0; k <= K; k++) {
            dp[0][k][0] = 0;
            dp[0][k][1] = -prices[0];
        }
        for (int day = 1; day < n; day++) {
            dp[day][0][0] = 0;
            dp[day][0][1] = Math.max(dp[day - 1][0][1], dp[day - 1][0][0] - prices[day]);  //其实就是比较每天的股票价格
        }
        //买入 - 卖出 +
        for (int day = 1; day < n; day++) {
            for (int k = 1; k <= K; k++) {
                dp[day][k][0] = Math.max(dp[day - 1][k][0], dp[day - 1][k - 1][1] + prices[day]);  //卖出，完成一次买卖，因此要之前的 K - 1 开始状态转移
                dp[day][k][1] = Math.max(dp[day - 1][k][1], dp[day - 1][k][0] - prices[day]);   //买入，未完成一次买卖
            }
        }
        return dp[n - 1][K][0];
    }


    /**
     * 123. 买卖股票的最佳时机 III
     */
    public int maxProfitIII(int[] prices) {   //基于上面动态规划的思路，最多可以完成 两笔 交易
        int n = prices.length;
        int K = 2;
        int[][][] dp = new int[n][K + 1][2];
        //初始化三维表格
        for (int k = 0; k <= K; k++) {         //初始化 day = 0 的情况
            dp[0][k][0] = 0;
            dp[0][k][1] = -prices[0];
        }
        for (int day = 1; day < n; day++) {    //初始化 K = 0 的情况，即尚未完成一次买卖的情况
            dp[day][0][0] = 0;
            dp[day][0][1] = Math.max(dp[day - 1][0][1], dp[day - 1][0][0] - prices[day]);
        }
        //初始化其余表格
        for (int day = 1; day < n; day++) {
            for (int k = 1; k <= K; k++) {
                dp[day][k][0] = Math.max(dp[day - 1][k][0], dp[day - 1][k - 1][1] + prices[day]);
                dp[day][k][1] = Math.max(dp[day - 1][k][1], dp[day - 1][k][0] - prices[day]);
            }
        }
        return dp[n - 1][K][0];
    }


    /**
     * 122. 买卖股票的最佳时机 II
     */
    public int maxProfitII60(int[] prices) {   //基于上面动态规划的思路，不限制次数
        int n = prices.length;
        int[][] dp = new int[n][2];
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        for (int day = 1; day < n; day++) {
            dp[day][0] = Math.max(dp[day - 1][0], dp[day - 1][1] + prices[day]);
            dp[day][1] = Math.max(dp[day - 1][1], dp[day - 1][0] - prices[day]);
        }
        return dp[n - 1][0];
    }


    /**
     * 309. 最佳买卖股票时机含冷冻期
     */
    public int maxProfit(int[] prices) {   //思路同上，多了一个冻结期，影响##买入##的状态装换，不影响##卖出##的状态转换
        int n = prices.length;
        int[][] dp = new int[n][2];
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        if (prices.length == 1) return 0;
        if (prices.length == 2) return Math.max(prices[1] - prices[0], 0);
        dp[1][0] = Math.max(dp[0][0], dp[0][1] + prices[1]);
        dp[1][1] = Math.max(dp[0][1], -prices[1]);  //买入没有前置状态，因为有在冻结期的影响，不能从前一天的状态0进行状态转移
        for (int day = 2; day < n; day++) {
            //今日不做交易、今日卖出 二者的最值
            dp[day][0] = Math.max(dp[day - 1][0], dp[day - 1][1] + prices[day]);
            //今日不做交易、今日买入（有冻结期的影响，故从前天进行状态转移） 二者最值
            dp[day][1] = Math.max(dp[day - 1][1], dp[day - 2][0] - prices[day]);
        }
        return dp[n - 1][0];
    }


    /**
     * 714. 买卖股票的最佳时机含手续费
     */
    public int maxProfit(int[] prices, int fee) {   //思路同上，多了手续费
        int n = prices.length;
        int[][] dp = new int[n][2];
        dp[0][0] = 0;
        dp[0][1] = -prices[0];
        for (int day = 1; day < n; day++) {
            dp[day][0] = Math.max(dp[day - 1][0], dp[day - 1][1] + prices[day] - fee);  //每次卖出即完成一次买卖，收取一次手续费
            dp[day][1] = Math.max(dp[day - 1][1], dp[day - 1][0] - prices[day]);
        }
        return dp[n - 1][0];
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
     * 1822. 数组元素积的符号
     */
    public int arraySign(int[] nums) {
        int negNums = 0;
        for (int num : nums) {
            if (num == 0) return 0;
            else if (num < 0) negNums++;
        }
        return (negNums & 1) == 1 ? -1 : 1;
    }

    public int arraySign01(int[] nums) {
        long xx = 1;  //int long均会越界
        for (int num : nums) {
            if (num == 0) return 0;
            xx *= num;
        }
        return xx > 0 ? 1 : -1;
    }

    /**
     * 1839. 所有元音按顺序排布的最长子字符串
     */
    public int longestBeautifulSubstring(String word) {
        int ans = 0;
        int nums = 1;
        int length = 1;
        int currentIndex = 1;
        while (currentIndex < word.length()) {
            //1、判断连续升序
            if (word.charAt(currentIndex) >= word.charAt(currentIndex - 1)) {
                length++;
                //记录升序区间内不同字符的个数
                if (word.charAt(currentIndex) > word.charAt(currentIndex - 1)) {
                    nums++;
                }
                if (nums == 5) {
                    ans = Math.max(ans, length);
                }
            } else {   //2、不满足升序，重新开始计数
                nums = 1;
                length = 1;
            }
            currentIndex++;
        }
        return ans;
    }


    /**
     * 481. 神奇字符串
     */
    public int magicalString(int n) {
        int left = 0;
        int right = 0;      //待写入的位点
        int oneNums = 0;
        ArrayList<Integer> ans = new ArrayList<>();
        while (right < n) {
            if ((left & 1) == 1) {     //1、逻辑位为奇数，则对应的数字为 2
                Integer nums = (left == 1 ? 2 : ans.get(left));
                while (right < n && nums > 0) {
                    nums--;
                    right++;
                    ans.add(2);
                }
                if (right == n) return oneNums;
            } else {                   //2、逻辑位为偶数，则对应的数字为 1
                Integer nums = (left == 0 ? 1 : ans.get(left));
                while (right < n && nums > 0) {
                    nums--;
                    right++;
                    oneNums++;
                    ans.add(1);
                }
                if (right == n) return oneNums;
            }
            left++;
        }
        return oneNums;
    }


    /**
     * 316. 去除重复字母
     */
    public String removeDuplicateLetters(String str) {
        int n = str.length();
        Stack<Character> stack = new Stack<>();
        int[] visited = new int[26];  //记录当前队列中是否存在对应的字符
        int[] buckets = new int[26];  //记录待遍历的字符串中，此字符剩余的个数
        for (int i = 0; i < n; i++) {
            buckets[str.charAt(i) - 'a']++;
        }
        for (int i = 0; i < n; i++) {
            int ch = str.charAt(i) - 'a';
            //1、更新当前位置后此字符的个数
            buckets[ch]--;
            //2、去重
            if (visited[ch] == 1) {  //当前字符已经使用
                continue;
            }
            //3、当前字符可添加到队列中，找到当前字符合理的插入位置
            while (!stack.isEmpty() && stack.peek() > str.charAt(i)) { //判断当前字符与堆顶元素的大小，进而判断是否更新堆中已有元素
                //1、如果之后不存在当前字符了，则此字符不可剔除
                if (buckets[stack.peek() - 'a'] == 0) {
                    break;
                }
                //2、之后仍存在当前字符，将此字符从队列中剔除
                char deleteCh = stack.pop();
                visited[deleteCh - 'a'] = 0;  //记录队列中不存在此字符，用于将后续此字符添加到队列中
            }
            //4、在堆中插入当前字符
            stack.add(str.charAt(i));
            visited[ch] = 1;
        }
        StringBuilder ans = new StringBuilder();
        while (!stack.isEmpty()) {
            ans.append(stack.pop());
        }
        return ans.reverse().toString();
    }


    /**
     * 456. 132 模式
     */
    public boolean find132pattern(int[] nums) {  //132 => ijk
        int n = nums.length;
        ArrayDeque<Integer> arrayQueue = new ArrayDeque<>();   //维护 j
        int k = Integer.MIN_VALUE; //维护 k
        for (int i = n - 1; i >= 0; i--) { //倒叙遍历 i
            if (nums[i] < k) return true;    //满足 1K2
            while (!arrayQueue.isEmpty() && arrayQueue.peekLast() < nums[i]) {
                //----------------------------------------------------------------------
                // 队列中存储多个元素，每次从右侧加入队列，从右侧出队列，从左到右依次降序存储
                // 由于 K 是从队列右侧取处的元素，因此一定有 K 位于 j 右侧且是小于 j 的最大值（贪心）
                //----------------------------------------------------------------------
                k = arrayQueue.pollLast();   //K从队列中取值，一定小于队列中的值，且倒叙遍历，故满足 32
            }
            arrayQueue.add(nums[i]);
        }
        return false;
    }


    /**
     * 1668. 最大重复子字符串
     */
    public int maxRepeating(String sequence, String word) {
        if (!sequence.contains(word)) {
            return 0;
        }
        int nums = 0;
        String temp = word;
        while (sequence.contains(word)) {
            word += temp;
            nums++;
        }
        return nums;
    }

    public int maxRepeating01(String sequence, String word) {  //错误写法，题目要求连续
        if (!sequence.contains(word)) {
            return 0;
        }
        int nums = 0;
        while (sequence.contains(word)) {
            sequence = sequence.replaceFirst(word, "#");   //替换为特殊字符，防止前后合并
            nums++;
        }
        return nums;
    }


    /**
     * 42. 接雨水
     */
    public int trap(int[] height) {
        int sum = 0;
        Stack<Integer> stack = new Stack<>();  //记录索引
        for (int i = 0; i < height.length; i++) {
            while (!stack.isEmpty() && height[stack.peek()] < height[i]) {  //保证桶内降序排序
                //------------------------------------------------------------------------
                // 当前 height[i] 作为桶的右挡板，然后需要在栈中依次找到 桶底索引和桶左挡板索引
                //------------------------------------------------------------------------

                //1、首先，在栈中找到桶底的索引
                Integer bottomIndex = stack.pop();
                //2、然后，在栈找到桶左挡板的索引
                if (stack.isEmpty()) {  //无更多元素，无法构成一个完整的桶，直接跳出
                    break;
                }
                Integer leftIndex = stack.peek();   //桶左挡板索引，不可取出，便于后续以当前的桶右边界，以当前左挡板为桶底，继续寻找新桶的左边界

                //计算当前桶内积水量
                int distance = i - leftIndex - 1;
                int high = Math.min(height[leftIndex], height[i]) - height[bottomIndex];  //水位高度取决于桶两侧的最短板
                sum += distance * high;
            }
            stack.add(i);
        }
        return sum;
    }


    public int trap00(int[] height) {   //逐列累加
        int n = height.length;
        if (n == 1) return 0;
        int[] leftMax = new int[n];
        int[] rightMax = new int[n];
        //1、各个位点及其左侧的最大高度
        leftMax[0] = height[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i - 1], height[i]);
        }
        //2、各个位点及其右侧的最大高度
        rightMax[n - 1] = height[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], height[i]);
        }
        int ans = 0;
        //累加各个位点可以接到雨水的量
        for (int i = 0; i < n; i++) {
            ans += Math.min(leftMax[i], rightMax[i]) - height[i];
        }
        return ans;
    }


    public int trap01(int[] height) {   //逐行累加，但会超时
        int ans = 0;
        int maxHeight = Arrays.stream(height).max().getAsInt();
        for (int h = 1; h <= maxHeight; h++) {   //每层高度
            int start = 0;
            int rowSum = 0;
            for (int i = 0; i < height.length; i++) {
                if (start == 1 && height[i] < h) {
                    rowSum++;
                }
                //--------------------------------------------------------------------------------
                // 水位线为 h 时，则高于此水位线的台阶相当于栅栏，从而存储雨水
                // 每个栅栏是前一个区间的后挡板，也是下一个区间的前挡板，所以既要累加前一个区间的雨水量，也要从重新开始计算下一个区间的雨水量
                //--------------------------------------------------------------------------------
                if (height[i] >= h) {
                    ans += rowSum;  //作为区间的后挡板，累加前一个区间的积水量
                    start = 1;      //作为区间的前挡板，可以从下一个位置开始积累雨水
                    rowSum = 0;     //重置当前积累的雨水量
                }
            }
        }
        return ans;
    }


    /**
     * 754. 到达终点数字
     */
    public int reachNumber(int target) {
        target = Math.abs(target);
        int currentIndex = 0;
        int needNums = 0;
        //恰好等于 target 或 超过 target 且首个 currentIndex - target 为偶数的位置
        while (currentIndex < target || ((currentIndex - target) & 1) == 1) {
            ++needNums;
            currentIndex += needNums;
        }
        return needNums;
    }


    /**
     * 53. 最大子数组和
     */
    public int maxSubArray(int[] nums) {  //动态规划
        int n = nums.length;
        int[] dp = new int[n];    //状态定义：以 nums[i] 结尾的连续子序列的最大和
        dp[0] = nums[0];
        for (int i = 1; i < n; i++) {
            if (dp[i - 1] > 0) {
                dp[i] = dp[i - 1] + nums[i];
            } else {
                dp[i] = nums[i];
            }
        }
        int ans = dp[0];
        for (int i = 1; i < n; i++) {
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }

    public int maxSubArray10(int[] nums) {  //动态规划
        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];
        int ans = nums[0];
        for (int i = 1; i < n; i++) {
            dp[i] = Math.max(nums[i], dp[i - 1] + nums[i]);
            ans = Math.max(ans, dp[i]);
        }
        return ans;
    }

    public int maxSubArray01(int[] nums) {  //官方动态规划
        int pre = 0;
        int res = nums[0];
        for (int num : nums) {
            pre = Math.max(pre + num, num);
            res = Math.max(res, pre);
        }
        return res;
    }

    public int maxSubArray02(int[] nums) {  //前缀和，超时
        int[] prefixSum = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        int res = Integer.MIN_VALUE;
        for (int i = 1; i < prefixSum.length; i++) {
            for (int j = i + 1; j < prefixSum.length; j++) {
                res = Math.max(res, prefixSum[j] - prefixSum[i]);
            }
        }
        return res;
    }

    public int maxSubArray20(int[] nums) {  //暴力超时
        int sum = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            int temp = 0;
            for (int j = i; j < nums.length; j++) {
                temp += nums[j];
                sum = Math.max(sum, temp);
            }
        }
        return sum;
    }

    public int maxSubArray21(int[] nums) {
        int res = Integer.MIN_VALUE;
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            res = Math.max(res, sum);
            sum = Math.max(sum, 0);  //sum为 0 则此前缀不可取，重新开始字串
        }
        return res;
    }


    /**
     * 918. 环形子数组的最大和
     */
    public int maxSubarraySumCircular(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];
        int max = nums[0];
        int sum = nums[0];
        for (int i = 1; i < n; i++) {
            sum += nums[i];
            dp[i] = Math.max(nums[i], dp[i - 1] + nums[i]);
            max = Math.max(max, dp[i]);
        }
        //-----------------------------------------------------------------------
        // 注意：最小连续子区间的右边界，不能触及区间的右边界，左边界可以触及区间左边界
        //-----------------------------------------------------------------------
//        int min = 0;  //不能给 nums[0]，因为数组长度为 1 的情况下，nums[0] 其实就是右侧边界，给 0 的目的其实是不考虑 sum - min 的情况
        int min = (n == 1 ? 0 : nums[0]);  //给 0 或 此处，以及下面遇到 n == 1 直接返回的情况都可以
        for (int i = 1; i < n - 1; i++) {   //不能触及右边界
            dp[i] = Math.min(nums[i], dp[i - 1] + nums[i]);
            min = Math.min(min, dp[i]);
        }
        return Math.max(max, sum - min);
    }

    public int maxSubarraySumCircular01(int[] nums) {
        int n = nums.length;
        //---------------------------------------------
        // 关键：保证了 min = nums[0] 赋值的情况下，min一定不会触及右边界，因为这种写法导致 min 触及右边界的原因只能是 n == 1 此处直接返回，防止下面出现这种情况
        //---------------------------------------------
        if (n == 1) return nums[0];
        int[] dp = new int[n];
        dp[0] = nums[0];
        int max = nums[0];
        int sum = nums[0];
        for (int i = 1; i < n; i++) {
            sum += nums[i];
            dp[i] = Math.max(nums[i], dp[i - 1] + nums[i]);
            max = Math.max(max, dp[i]);
        }
        //-----------------------------------------------------------------------
        // 注意：最小连续子区间的右边界，不能触及区间的右边界，左边界可以触及区间左边界
        //-----------------------------------------------------------------------
        int min = nums[0];
        for (int i = 1; i < n - 1; i++) {   //不能触及右边界
            dp[i] = Math.min(nums[i], dp[i - 1] + nums[i]);
            min = Math.min(min, dp[i]);
        }
        return Math.max(max, sum - min);
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
     * 816. 模糊坐标
     */
    public List<String> ambiguousCoordinates(String str) {
        List<String> ans = new ArrayList<>();
        str = str.replaceAll("\\(", "").replaceAll("\\)", "");
        for (int i = 0; i < str.length() - 1; i++) {  //枚举插入","的位置
            List<String> arrLeft = ambiguousCoordinatesHelper(str, 0, i);
            List<String> arrRight = ambiguousCoordinatesHelper(str, i + 1, str.length() - 1);
            for (String left : arrLeft) {  //组合拼接
                for (String right : arrRight) {
                    ans.add("(" + left + ", " + right + ")");
                }
            }
        }
        return ans;
    }

    private List<String> ambiguousCoordinatesHelper(String str, int start, int end) {  //枚举"."添加的位置
        List<String> ans = new ArrayList<>();
        //1、待处理区间长度为 1，无需添加"."
        if (start == end) {
            ans.add(String.valueOf(str.charAt(start)));
            return ans;
        }
        //2、待处理区间长度大于 1，合理的添加"."
        if (str.charAt(start) != '0') {      //首先，添加"."到最左侧，等效于不添加"."，保持原样
            ans.add(str.substring(start, end + 1));
        }
        for (int i = start; i < end; i++) {  //其次，依次在各位置后面添加"."，虽然 end 可以取到，但此处不能有等号，因为我们枚举的在 i 位后添加"."，肯定不能再最后添加"."
            String left = str.substring(start, i + 1);
            String right = str.substring(i + 1, end + 1);
            //剪枝一：剔除整数位不合理的情况
            if (left.startsWith("0") && left.length() > 1) {  //整数位以 0 开头，但长度大于 1
                continue;
            }
            if (right.endsWith("0")) {  //小数位以 0 结尾，无论长度是否大于 1，均不合理
                continue;
            }

            //将合理的情况添加到结果集中
            ans.add(left + "." + right);
        }
        return ans;
    }


    /**
     * 621. 任务调度器
     */
    public int leastInterval(char[] tasks, int n) {
        int currentTime = 0;
        int[] buckets = new int[26];
        int[] visited = new int[26];
        Arrays.fill(visited, -1);
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o2[1] - o1[1]);  //按照频次降序排序，贪心
        for (int i = 0; i < tasks.length; i++) {
            buckets[tasks[i] - 'A']++;
        }
        for (int i = 0; i < 26; i++) {
            if (buckets[i] > 0) {
                sortedQueue.add(new int[]{i, buckets[i]});
            }
        }
        ArrayDeque<int[]> arrayQueue = new ArrayDeque<>();  //缓存当前不可执行的任务
        while (!sortedQueue.isEmpty()) {
            //剔除当前不可以执行的任务，移动至缓存队列中
            while (!sortedQueue.isEmpty() && visited[sortedQueue.peek()[0]] != -1 && currentTime - visited[sortedQueue.peek()[0]] <= n) {
                arrayQueue.addLast(sortedQueue.poll());
            }
            //如果当前有可以执行的任务，就执行任务，并消减任务数，无可执行的任务，就闲置
            if (!sortedQueue.isEmpty()) {
                int[] currentTask = sortedQueue.poll();
                visited[currentTask[0]] = currentTime;
                currentTask[1]--;
                if (currentTask[1] > 0) {
                    sortedQueue.add(currentTask);      //当前此类任务已经全部执行完毕
                }
            }
            //将今日不可执行的任务，重新添加到任务队列中
            while (!arrayQueue.isEmpty()) {
                sortedQueue.add(arrayQueue.poll());
            }
            currentTime++;
        }
        return currentTime;
    }

    public int leastInterval01(char[] tasks, int n) {  //基于桶的概念
        int[] buckets = new int[26];
        int maxFreq = 0;
        int maxNums = 0;
        for (int i = 0; i < tasks.length; i++) {
            maxFreq = Math.max(maxFreq, ++buckets[tasks[i] - 'A']);
        }
        for (int freq : buckets) {
            if (freq == maxFreq) {
                maxNums++;
            }
        }
        //---------------------------------------------------------------------------------------------------------------------------
        // 桶的大小：
        //   每个桶的容量为冻结时长 + 1，因为第一个任务入桶后，再冻结时长 n 的时间内均不能再执行此任务，即此桶内不可再执行此任务，故桶大小为 n + 1
        // 桶的个数：
        //   取决于单个任务出现的最大频次 maxFreq
        //       其除了最后一个桶外，前 maxFreq 个桶内无论任务是否安排满，为了满足冻结时长，均要消耗，故为 (maxFreq - 1) * (n + 1)
        //       最后一个桶，是否执行完，取决于桶内元素个数，即最大频次 maxFreq 任务的个数
        //   上面是无需将全部桶装满的情况，其实还有一个情况是任务非常多，桶的容量装不下，其实就是横向扩展桶的容量，等价于计算全部任务的个数
        //---------------------------------------------------------------------------------------------------------------------------
        return Math.max(tasks.length, (maxFreq - 1) * (n + 1) + maxNums);
    }


    /**
     * 767. 重构字符串
     */
    public String reorganizeString(String str) {   //基于优先队列
        int[] buckets = new int[26];
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o2[1] - o1[1]);  //按照频次降序排序
        for (int i = 0; i < str.length(); i++) {
            buckets[str.charAt(i) - 'a']++;
        }
        for (int i = 0; i < 26; i++) {
            if (buckets[i] > 0) {
                sortedQueue.add(new int[]{i, buckets[i]});
            }
        }
        ArrayDeque<Integer> builder = new ArrayDeque<>();
        while (!sortedQueue.isEmpty()) {
            int[] max = sortedQueue.poll();
            if (!builder.isEmpty() && builder.peekLast() == max[0]) {  //当前频次最高得字符同上一个字符相等
                if (sortedQueue.isEmpty()) {
                    return "";
                }
                int[] next = sortedQueue.poll();
                builder.addLast(next[0]);
                next[1]--;
                if (next[1] > 0) {  //未消耗完，添加回队列
                    sortedQueue.add(next);
                }
                sortedQueue.add(max);
            } else {
                builder.addLast(max[0]);
                max[1]--;
                if (max[1] > 0) {
                    sortedQueue.add(max);
                }
            }
        }
        StringBuilder ans = new StringBuilder();
        while (!builder.isEmpty()) {
            ans.append((char) (builder.pollFirst() + 'a'));
        }
        return ans.toString();
    }

    public String reorganizeString01(String str) {   //基于桶
        int[][] charAndFreq = new int[26][2];
        for (int i = 0; i < 26; i++) {
            charAndFreq[i][0] = i;
        }
        for (int i = 0; i < str.length(); i++) {
            charAndFreq[str.charAt(i) - 'a'][1]++;
        }
        Arrays.sort(charAndFreq, (o1, o2) -> o2[1] - o1[1]);  //按照字符的频次降序排序
        int maxFreq = charAndFreq[0][1];
        //导致重复的情况
        if (maxFreq > ((str.length() + 1) / 2)) {  //单个字符出现的频次超过半数，向上取整
            return "";
        }
        //不会重复的情况，则组合各个字符
        StringBuilder[] buckets = new StringBuilder[maxFreq];
        for (int i = 0; i < maxFreq; i++) {
            buckets[i] = new StringBuilder();
        }
        int bucketId = 0;  //桶编号，各个字符交替使用
        for (int i = 0; i < 26; i++) {  //将各个字母依次放入桶内
            char ch = (char) (charAndFreq[i][0] + 'a');
            int freq = charAndFreq[i][1];
            while (freq > 0) {
                buckets[bucketId].append(ch);
                freq--;
                bucketId++;   //下一个桶
                bucketId %= maxFreq; //一轮循环结束，从第一个桶开始
            }
        }
        StringBuilder ans = new StringBuilder();
        Arrays.stream(buckets).forEach(ans::append);
        return ans.toString();
    }


    /**
     * 764. 最大加号标志
     */
    public int orderOfLargestPlusSign(int n, int[][] mines) {  //四个方向的前缀和
        //生成二维矩阵
        int[][] grid = new int[n + 2][n + 2];
        for (int i = 1; i <= n; i++) {
            Arrays.fill(grid[i], 1);
        }
        for (int[] arr : mines) {
            grid[arr[0] + 1][arr[1] + 1] = 0;
        }
        for (int i = 0; i <= n + 1; i++) {
            for (int j = 0; j <= n + 1; j++) {
                if (i == 0 || i == n + 1 || j == 0 || j == n + 1) {
                    grid[i][j] = 0;
                }
            }
        }
        //定义四个方向的前缀和
        int[][] up = new int[n + 2][n + 2];
        int[][] down = new int[n + 2][n + 2];
        int[][] left = new int[n + 2][n + 2];
        int[][] right = new int[n + 2][n + 2];
        //正向
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (grid[i][j] == 1) {  //关键：如果当前点值为 0，则不计算前缀和，即为默认值 0
                    down[i][j] = down[i - 1][j] + 1;     //从上向下
                    right[i][j] = right[i][j - 1] + 1;   //从左向右
                }
            }
        }
        //反向
        for (int i = n; i >= 1; i--) {
            for (int j = n; j >= 1; j--) {
                if (grid[i][j] == 1) {  //关键：如果当前点值为 0，则不计算前缀和，即为默认值 0
                    up[i][j] = up[i + 1][j] + 1;         //从下向上
                    left[i][j] = left[i][j + 1] + 1;     //从右向左
                }
            }
        }
        int ans = 0;
        //遍历各个点，计算当前点在四个方向上的前缀和的最小值，即为当前点可构成的最大十字架臂长
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                ans = Math.max(ans, Math.min(Math.min(down[i][j], up[i][j]), Math.min(left[i][j], right[i][j])));
            }
        }
        return ans;
    }


    /**
     * 1353. 最多可以参加的会议数目
     */
    public int maxEvents(int[][] events) {  //与吃苹果的非常类似
        int ans = 0;
        int currDay = 0;
        int currIndex = 0;
        int n = events.length;
        //按照会议开始时间升序排序
        Arrays.sort(events, (o1, o2) -> o1[0] - o2[0]);
        //可选会议的队列，按照结束时间升序排序
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[1] - o2[1]);
        while (currIndex < n || !sortedQueue.isEmpty()) {
            //1、添加会议
            while (currIndex < n && events[currIndex][0] <= currDay) {
                sortedQueue.add(events[currIndex]);
                currIndex++;
            }
            //2、移除过期会议
            while (!sortedQueue.isEmpty() && sortedQueue.peek()[1] < currDay) {
                sortedQueue.poll();
            }
            //3、贪心选择会议
            if (!sortedQueue.isEmpty()) {
                sortedQueue.poll();
                ans++;
            }
            currDay++;
        }
        return ans;
    }

    /**
     * 1823. 找出游戏的获胜者
     */
    public int findTheWinner(int n, int k) {
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 1; i <= n; i++) {
            queue.addLast(i);
        }
        while (queue.size() > 1) {
            for (int i = 1; i < k && queue.size() > 1; i++) {
                queue.addLast(queue.pollFirst());
            }
            queue.pollFirst();  //移除第 K 个人
        }
        return queue.peekLast();
    }

    public int findTheWinner01(int n, int k) {
        int m = 0;
        int ans = 0;
        int[] nums = new int[n];
        Arrays.fill(nums, 1);
        int currentIndex = -1;
        while (ans < n - 1) {
            while (m < k) {
                m++;
                currentIndex++;
                currentIndex %= n;
                while (nums[currentIndex] == 0) {
                    currentIndex++;
                    currentIndex %= n;
                }
            }
            nums[currentIndex] = 0;
            m = 0;
            ans++;  //已经选择了多少个人
//            if (ans == n - 1) break;
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] == 1) return i + 1;  // + 1 的原因是，索引号和孩子的编号差 1
        }
        return -1;
    }


    /**
     * 791. 自定义字符串排序
     */
    public String customSortString(String order, String str) {
        int[] bucket1 = new int[26];
        int[] bucket2 = new int[26];
        for (int i = 0; i < order.length(); i++) {
            bucket1[order.charAt(i) - 'a']++;
        }
        for (int i = 0; i < str.length(); i++) {
            if (bucket1[str.charAt(i) - 'a'] != 0) {  //如果二者共有，位置需要具有相对性
                bucket2[str.charAt(i) - 'a']++;
            } else {                //独有，顺序任意
                bucket2[str.charAt(i) - 'a']--;
            }
        }
        ArrayList<Character> sorted = new ArrayList<>();
        for (int i = 0; i < order.length(); i++) {
            if (bucket2[order.charAt(i) - 'a'] > 0) {
                sorted.add(order.charAt(i));
            }
        }
        StringBuilder ans = new StringBuilder();
        for (Character current : sorted) {
            while (bucket2[current - 'a'] > 0) {
                ans.append(current);
                bucket2[current - 'a']--;
            }
        }
        for (int i = 0; i < 26; i++) {
            char current = (char) (i + 'a');
            while (bucket2[i] < 0) {
                ans.append(current);
                bucket2[i]++;
            }
        }
        return ans.toString();
    }

    public String customSortString01(String order, String str) {  //自定义排序，比上面的慢一些
        int[] priority = new int[26];
        for (int i = 0; i < order.length(); i++) {  //按照 order 中的顺序，进行优先级标识
            priority[order.charAt(i) - 'a'] = i + 1;
        }
        Character[] strArray = new Character[str.length()];  //将 str 中字符的顺序按照 order中的优先级排序
        for (int i = 0; i < str.length(); i++) {
            strArray[i] = str.charAt(i);
        }
        Arrays.sort(strArray, (o1, o2) -> priority[o1 - 'a'] - priority[o2 - 'a']);  //按照字符的优先级，升序排序
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < strArray.length; i++) {
            ans.append(strArray[i]);
        }
        return ans.toString();
    }


    /**
     * 1023. 驼峰式匹配
     */
    public List<Boolean> camelMatch(String[] queries, String pattern) {
        List<Boolean> ans = new ArrayList<>();
        for (String query : queries) {
            ans.add(camelMatchHelper(query, pattern));
        }
        return ans;
    }

    private boolean camelMatchHelper(String query, String pattern) {
        if (query.length() < pattern.length()) {
            return false;
        }
        int parIndex = 0;
        int queIndex = 0;
        while (queIndex < query.length()) {
            if (query.charAt(queIndex) == pattern.charAt(parIndex)) {      //相等，两个指针均向后移动一位
                queIndex++;
                parIndex++;
            } else if (Character.isLowerCase(query.charAt(queIndex))) {
                //不相等，此时 pattern 为大写、query 为小写，通过向 pattern 中填充小写字母，来满足条件
                queIndex++;  //需要可以于 pattern 当前匹配的大写字母
            } else if (Character.isUpperCase(query.charAt(queIndex))) {
                return false;
            }
            if (parIndex == pattern.length()) {  // pattern 匹配完了，如果 query 后续有小写，仍可向 pattern 中添加，以使得满足条件
                while (queIndex < query.length()) {
                    if (Character.isUpperCase(query.charAt(queIndex))) {  //但如果 query 后续有大写，则无法完成匹配
                        return false;
                    }
                    queIndex++;
                }
            }

        }
        return parIndex == pattern.length();
    }


    /**
     * 1023. 驼峰式匹配
     */
    public List<Boolean> camelMatch02(String[] queries, String pattern) {
        List<Boolean> ans = new ArrayList<>();
        for (String query : queries) {
            ans.add(camelMatchHelper02(query, pattern));
        }
        return ans;
    }

    private Boolean camelMatchHelper02(String query, String pattern) {
        if (query.length() < pattern.length()) {
            return false;
        }
        int currIndex = 0;
        for (int i = 0; i < query.length(); i++) {
            if (currIndex < pattern.length() && query.charAt(i) == pattern.charAt(currIndex)) {
                currIndex++;
            } else if (Character.isUpperCase(query.charAt(i))) {
                return false;
            }
        }
        return currIndex == pattern.length();
    }


    public List<Boolean> camelMatch01(String[] queries, String pattern) {   //错误写法，不仅考虑数量，还要考虑顺序
        List<Boolean> ans = new ArrayList<>();
        int[] max = new int[26];
        int[] min = new int[26];
        for (int i = 0; i < pattern.length(); i++) {
            if (Character.isUpperCase(pattern.charAt(i)))
                max[pattern.charAt(i) - 'A']++;
            else
                min[pattern.charAt(i) - 'a']++;
        }
        for (String query : queries) {
            if (query.length() < pattern.length()) {
                ans.add(false);
                continue;
            }
            ans.add(camelMatchHelper01(query, max, min));
        }
        return ans;
    }

    private boolean camelMatchHelper01(String query, int[] max, int[] min) {
        int[] currMax = new int[26];
        int[] currMin = new int[26];
        for (int i = 0; i < query.length(); i++) {
            if (Character.isUpperCase(query.charAt(i))) {
                currMax[query.charAt(i) - 'A']++;
                if (currMax[query.charAt(i) - 'A'] > max[query.charAt(i) - 'A'])
                    return false;
            } else {
                currMin[query.charAt(i) - 'a']++;
            }
        }
        for (int i = 0; i < 26; i++) {
            if (currMin[i] < min[i]) return false;
        }
        return Arrays.equals(currMax, max);
    }


    /**
     * 775. 全局倒置与局部倒置
     */
    public boolean isIdealPermutation(int[] nums) {  //贪心，维护后缀最小值
        int n = nums.length;
        int[] min = new int[n];    //记录当前位置后的最小值
        min[n - 1] = n;
        for (int i = n - 2; i >= 0; i--) {
            min[i] = Math.min(nums[i + 1], min[i + 1]);
        }
        for (int i = 0; i < n - 1; i++) {  //贪心，满足局部倒置的一定满足全局倒置，即局部倒置是相邻两个元素的全局倒置
            if (nums[i] > min[i + 1]) {     //因此，只需要判断不相邻的两个元素是否存在全局倒置
                return false;
            }
        }
        return true;
    }

    public boolean isIdealPermutation010(int[] nums) {  //贪心，维护后缀最小值，效率非常高
        int n = nums.length;
        int min = nums[n - 1];
        for (int i = n - 3; i >= 0; i--) {
            if (nums[i] > min) return false;
            min = Math.min(min, nums[i + 1]);  //忽略后一位
        }
        return true;
    }


    public boolean isIdealPermutation01(int[] nums) {  //有序集合，超时
        int n = nums.length;
        int ans = 0;
        int res = 0;
        TreeSet<Integer> treeSet = new TreeSet<>();
        for (int num : nums) {
            treeSet.add(num);
        }
        for (int i = 0; i < n; i++) {
            treeSet.remove(nums[i]);
            ans += treeSet.headSet(nums[i]).size();
            if (i < n - 1) {
                res += nums[i] > nums[i + 1] ? 1 : 0;
            }
        }
        return ans == res;
    }

    public boolean isIdealPermutation02(int[] nums) {  //错误解法，当前这个仅考虑了是否存在，而不是个数
        int n = nums.length;
        int[] min = new int[n];  //记录当前位置之后的最小值
        min[n - 1] = nums[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            min[i] = Math.min(nums[i + 1], min[i + 1]);
        }
        int ans = 0;
        int res = 0;
        for (int i = 0; i < n; i++) {
            ans += nums[i] > min[i] ? 1 : 0;  //后面存在比当前数小的值，则满足全局倒置
            if (i < n - 1) {
                res += nums[i] > nums[i + 1] ? 1 : 0;  //局部倒置
            }
        }
        return ans == res;
    }


    /**
     * 162. 寻找峰值
     */
    public int findPeakElement(int[] nums) {   //找到最右侧峰值
        int index = 0;
        for (int i = 1; i < nums.length; i++) {
            //---------------------------------------------------------------------------------
            // 关键：相邻元素不相等，且从左至右遍历，最后一个比左侧值大的点一定是峰值，因为峰值一定存在
            //---------------------------------------------------------------------------------
            if (nums[i - 1] < nums[i])
                index = i;
        }
        return index;
    }

    public int findPeakElement00(int[] nums) {   //找到最左侧峰值
        int index = nums.length - 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            if (nums[i] > nums[i + 1]) {
                index = i;
            }
        }
        return index;
    }

    public int findPeakElement01(int[] nums) {   //二分，二段性的体现，找到任意一个峰值
        //-------------------------------------------------------------------------------------
        // 本题关键：
        //     对于 nums[xx] < nums[xx + 1]，则在 xx 右侧一定有峰值，而 xx 左侧不一定有峰值
        // 二段性的体现：
        //     指在以 mid 为分割点的数组上，根据 nums[mid] 与 nums[mid±1] 的大小关系
        //     可以确定其中一段满足「必然有解」，另外一段不满足「必然有解」（可能有解，可能无解）
        //-------------------------------------------------------------------------------------
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);    //偶数区间，中点逼近左侧，保证下面 nums[mid + 1] 不会越界
            if (nums[mid] <= nums[mid + 1]) {          //left爬山
                left = mid + 1;
            } else if (nums[mid] > nums[mid + 1]) {
                right = mid;
            }
        }
        return left;
    }

    public int findPeakElement02(int[] nums) {   //二分，二段性的体现
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + ((right - left + 1) >> 1);  //偶数区间，中点逼近右侧，保证下面 nums[mid + 1] 不会越界，同时不会进入死循环，如 left 的逻辑
            if (nums[mid - 1] < nums[mid]) {
                left = mid;
            } else if (nums[mid - 1] >= nums[mid]) {
                right = mid - 1;
            }
        }
        return left;
    }


    /**
     * 1013. 将数组分成和相等的三个部分
     */
    public boolean canThreePartsEqualSum(int[] arr) {
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        if (sum % 3 != 0) return false;
        int target = sum / 3;
        int nums = 0;
        int current = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            current += arr[i];
            if (current == target) {
                nums++;
                if (nums == 2) return true;
                current = 0;  //重置
            }
        }
        return false;
    }

    public boolean canThreePartsEqualSum01(int[] arr) {    //双指针，从两头向中间搜索
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        if (sum % 3 != 0) return false;
        int target = sum / 3;
        int left = 0;
        int right = arr.length - 1;
        int nums = 0;
        int current = 0;
        while (left < right) {
            current += arr[left];
            if (current == target) break;          //1、左侧找到目标值
            left++;
        }
        current = 0;
        while (right > left + 1) {  //中间必须留有空位置，所以无需关注中间的目标值怎么构成
            current += arr[right];
            if (current == target) return true;   //2、右侧找到目标值
            right--;
        }
        return false;
    }


    /**
     * 891. 子序列宽度之和
     */
    public int sumSubseqWidths(int[] nums) {
        Arrays.sort(nums); // 排序
        long res = 0;
        int mod = (int) Math.pow(10, 9) + 7;
        int n = nums.length;
        //2的快速幂
        long[] pow = new long[n];
        pow[0] = 1;
        for (int i = 1; i < n; i++) {
            pow[i] = (pow[i - 1] << 1) % mod;
        }
        for (int i = 0; i < n; i++) {
            //-------------------------------------------------------------
            // 计算每个数 nums[i] 对最终结果的贡献
            //    1、nums[i] 作为子序列的最大值，计算子序列的宽度
            //      需要用 nums[i] 减去子序列的最小值，且其可以作为 2^i 个子序列的最大值，所以会被加 2^i 次
            //    2、nums[i] 作为子序列的最小值，计算子序列的宽度
            //      需要用子序列的最大值减去 nums[i] ，且其可以作为 2^(n-1-i) 个子序列的最小值，所以会被减 2^(n-1-i) 次
            //-------------------------------------------------------------
            res = (res + (pow[i] - pow[n - i - 1]) * nums[i] % mod) % mod;
        }
        return (int) res;
    }

    public int sumSubseqWidths01(int[] nums) { //理论是超时，但结果错误
        //[5,69,89,92,31,16,25,45,63,40,16,56,24,40,75,82,40,12,50,62,92,44,67,38,92,22,91,24,26,21,100,42,23,56,64,43,95,76,84,79,89,4,16,94,16,77,92,9,30,13]
        Arrays.sort(nums);
        long result = 0;
        int mod = (int) 1e9 + 7;
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                result += (((nums[j] - nums[i]) * Math.pow(2, j - i - 1) % mod) % mod);
            }
        }
        return (int) result;
    }


    /**
     * 986. 区间列表的交集
     */
    public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {  //双指针
        ArrayList<int[]> ans = new ArrayList<>();
        int i = 0;   //指针一，指向其中一个区间
        int j = 0;   //指针二，指向其中一个区间
        while (i < firstList.length && j < secondList.length) {
            //1、判断两个区间是否有交集
            int begin = Math.max(firstList[i][0], secondList[j][0]);
            int end = Math.min(firstList[i][1], secondList[j][1]);
            if (begin <= end) {   //两个区间存在交集
                ans.add(new int[]{begin, end});
            }
            //2、无论两个区间是否有交集，均需要移动指针，移动原则是保留整体靠后得区间，跳过靠前的区间
            if (firstList[i][1] < secondList[j][1]) {
                i++;
            } else {
                j++;
            }
        }
        return ans.toArray(new int[ans.size()][]);
    }


    /**
     * 169. 多数元素
     */
    public int majorityElement(int[] nums) {  //同归于尽
        int win = nums[0];
        int remain = 1;
        //----------------------------------------------------------
        // 逐个士兵发起进攻占领高地，最终占领高地的士兵所属团队即为胜利的一方
        //----------------------------------------------------------
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == win) {        //1、冲锋的士兵和当前占领高地的士兵，属于同一个集团军
                remain++;       //当前控制高地的集团军士兵数增加一个
            } else if (remain > 0) {     //2、冲锋的士兵和当前占领高地的士兵，不属于同一个集团军，但当前高地中仍有士兵可抵挡当前冲锋的士兵
                remain--;       //高地中的一个士兵与当前冲锋的士兵同归于尽，高地所属方不变
            } else if (remain == 0) {    //3、冲锋的士兵和当前占领高地的士兵，不属于同一个集团军，但当前高地中已经没有士兵可抵挡当前冲锋的士兵
                win = nums[i];  //高地易主
                remain = 1;
            }
        }
        return win;
    }

    public int majorityElement01(int[] nums) {  //投票法，和上面类似
        int win = nums[0];
        int count = 1;      //票数
        for (int i = 1; i < nums.length; i++) {
            if (count == 0) {
                win = nums[i];
            }
            count += nums[i] == win ? 1 : -1;   //更新获胜者时，其应该对应 1 票，逻辑在这里处理
        }
        return win;
    }

    public int majorityElement02(int[] nums) {  //贪心
        Arrays.sort(nums);
        return nums[nums.length / 2];
    }

    /**
     * 240. 搜索二维矩阵 II
     */
    public boolean searchMatrixII(int[][] matrix, int target) {  //基于广度优先搜索，忽略基于二分的写法（没意思）
        int m = matrix.length;
        int n = matrix[0].length;
        //从第一行最后一个元素开始广度优先搜索，因为此位置对应的两个方向，一个大于一个小于
        int row = 0;           //只能增加
        int col = n - 1;       //只能减少
        while (row < m && col >= 0) {
            if (matrix[row][col] < target) row++;
            else if (matrix[row][col] > target) col--;
            else return true;
        }
        return false;
    }


    /**
     * 334. 递增的三元子序列
     */
    public boolean increasingTriplet(int[] nums) {  //参考  300. 最长递增子序列
        int top1 = nums[0];
        int top2 = Integer.MAX_VALUE;
        for (int i = 1; i < nums.length; i++) {
            //--------------------------------------------------------------
            // 整体遍历的过程可以理解为一个水流流经三个滤网，最后的滤网保存最小值、中间滤网保存次最小值、最后滤网拦截较大值
            // 深入理解，第二个滤网维护的值仅代表前面的元素一定存在某个数小于其自身，但此数并不一定是第一个滤网种的值
            // 注意整个遍历的过程中，水流是顺序的，更新各个滤网的情况，详细见举例 [5,10,2,4,6]
            //     在处理第三个元素 2 时，会更新首个滤网的值为 2，此时前两个滤网维护的值分别为 2 和 10
            //         有人会有疑问，2 和 10 并非顺序的啊，此时最小值为 2 中值为 10，不合理啊，那么再来个 11，构成的 2 10 11 也不是按照索引顺序来的啊
            //         其实第二个滤网 10 只代表前面有比 10 小的数，其实对应的 5，而再来个 11 满足条件，其实满足条件的序列是 5 10 11
            //         因此更新首个滤网的值为 2 仅仅是拉低后续元素的门槛，使其更容易满足条件
            //     更新第一第二个滤网仅仅是为了拉低后续元素的门槛，贪心，同时每个元素代表前面一定存在某个数小于其自身
            //--------------------------------------------------------------
            if (top2 < nums[i]) {
                //此条件成立的前提是 top2 一定存在，top2存在说明其一定大于之前的某个值
                return true;
            } else if (top1 < nums[i] && nums[i] < top2) {
                //用于拉低下线（但仍保证 top2 大于 top1），为后续 top3 满足条件降低门槛
                top2 = nums[i];
            } else if (nums[i] < top1) {
                //用于拉低下线，从而为后续元素更新 top2 降低门槛
                top1 = nums[i];
            }
        }
        return false;
    }


    /**
     * 560. 和为 K 的子数组
     */
    public int subarraySum(int[] nums, int k) {
        int ans = 0;
        int[] prefixSum = new int[nums.length + 1];
        for (int i = 1; i < prefixSum.length; i++) {
            prefixSum[i] = prefixSum[i - 1] + nums[i - 1];
        }
        for (int i = 0; i < prefixSum.length; i++) {   //从零开始
            for (int j = i + 1; j < prefixSum.length; j++) {  // +1
                if (prefixSum[j] - prefixSum[i] == k)
                    ans++;
            }
        }
        return ans;
    }

    public int subarraySum02(int[] nums, int k) {
        int ans = 0;
        int prefixSum = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        hTable.put(0, 1);  //目的是保证区间 [0,i] 的累加和为 K 的情况，即从索引 0 开始的累加和等于 K 的情况
        for (int num : nums) {
            prefixSum += num;
            //--------------------------------------------------------
            // 可以简单理解为前缀和由两个区间构成，分别是 K 和 前置区间
            // 注意在循环中 K 是以 num 结尾的连续区间、前置区间为从索引位置为 0 到 num的区间，注意针对每个 K 对应的前置区间可能有多个，因为从索引 0 到 m 或到 n 其前缀和可能相等
            //--------------------------------------------------------
            if (hTable.containsKey(prefixSum - k)) {
                ans += hTable.get(prefixSum - k);
            }
            //将前缀和添加到 map中
            hTable.put(prefixSum, hTable.getOrDefault(prefixSum, 0) + 1);  //前缀和从索引位置 0 开始
        }
        return ans;
    }

    public int subarraySum03(int[] nums, int k) {  // [-1,-1,1] 0
        //---------------------------------------------------
        //为什么这题不可以用双指针或滑动窗口：
        //    因为 nums[i]可以小于0，也就是说右指针i向后移1位不能保证区间会增大，左指针j向后移1位也不能保证区间和会减小。给定j，i的位置没有二段性
        //---------------------------------------------------
        int ans = 0;
        int sum = 0;
        int left = 0;
        int right = 0;
        while (right < nums.length) {
            sum += nums[right];
            while (left < right && sum > k) {
                sum -= nums[left];
                left++;
            }
            if (sum == k) {
                ans++;
            }
            right++;
        }
        return ans;
    }


    /**
     * 409. 最长回文串
     */
    public int longestPalindrome(String str) {
        int[] buckets = new int[256];
        int ans = 0;
        int remain = 0;
        for (int i = 0; i < str.length(); i++) {
            buckets[str.charAt(i)]++;
        }
        for (int i = 0; i < 256; i++) {
            if (buckets[i] == 0)
                continue;
            if ((buckets[i] & 1) == 0) {   //值为偶数，对称放置
                ans += buckets[i];
            } else {                       //值为奇数，对称放置，前面减掉一
                ans += buckets[i] - 1;
                remain++;
            }
        }
        return remain == 0 ? ans : ans + 1; //中间插一位
    }


    /**
     * 5. 最长回文子串
     */
    public String longestPalindrome55(String str) {
        String ans = "";
        int n = str.length();
        for (int i = 0; i < n; i++) {
            String palindrome = palindrome(str, i);
            if (palindrome.length() > ans.length()) {
                ans = palindrome;
            }
        }
        return ans;
    }

    private String palindrome(String str, int mid) {  //中心对称法
        int left = mid;
        int right = mid;
        while (left >= 0 && str.charAt(left) == str.charAt(mid)) {      //左侧与中心相同的
            left--;
        }
        while (right < str.length() && str.charAt(right) == str.charAt(mid)) {  //右侧与中心相同的
            right++;
        }
        while (left >= 0 && right < str.length() && str.charAt(left) == str.charAt(right)) {
            left--;
            right++;
        }
        return str.substring(left + 1, right);
    }

    /**
     * 205. 同构字符串
     */
    public boolean isIsomorphic(String s, String t) {
        //------------------------------------------------------------------------
        // 题目关键：单向一对一
        //     1、不同字符不能映射到同一个字符上
        //     2、相同字符只能映射到同一个字符上
        //     因此，e -> l，r -> e 是正确的，并不要求一定 e -> l 就一定是 l -> e
        //------------------------------------------------------------------------
        for (int i = 0; i < s.length(); i++) {
            //s与 t的前缀就是映射关系，如果后面的和前面的不一致，就报错
            if (s.indexOf(s.charAt(i)) != t.indexOf(t.charAt(i))) {  //结合 "badc"与 "baba"、"paper"与 "title"深入体会
                //在两个字符串中，比较当前位置 i 处的字符在各自字符串中首次出现的索引

                //-----------------------------------------------------------
                // 第一组："badc"与 "baba"  多对一的异常场景
                //    索引位置 2处：
                //        s.indexOf(s.charAt(2)) = 2 说明之前 没出现过
                //        t.indexOf(t.charAt(2)) = 0 说明之前 已出现过，和 s 中其他的字符已经否成了 映射对，多对一的场景
                // 第二组："babc"与 "bata"  一对多的异常场景
                //    索引位置 2处：
                //        s.indexOf(s.charAt(2)) = 0 说明之前 已出现过，和 t 中其他的字符已经否成了 映射对，一对多的场景
                //        t.indexOf(t.charAt(2)) = 2 说明之前 没出现过
                // 第三组："paper"与 "title"
                //    索引位置 4、5处：
                //        仅单向
                //-----------------------------------------------------------
                return false;
            }
        }
        return true;
    }

    private boolean isIsomorphic01(String s, String t) {
        HashMap<Character, Character> hTable = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            //1、一对多
            if (hTable.containsKey(s.charAt(i)) && hTable.get(s.charAt(i)) != t.charAt(i)) {
                return false;
            }
            //2、多对一
            if (!hTable.containsKey(s.charAt(i)) && hTable.containsValue(t.charAt(i))) {
                return false;
            }
            hTable.put(s.charAt(i), t.charAt(i));  //但没要求双向映射
        }
        return true;
    }

    private boolean isIsomorphic06(String s, String t) {   //本题错误写法，此写法是双向映射，a -> b 则 b -> a
        HashMap<Character, Character> hTable = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            if (hTable.containsKey(s.charAt(i)) && hTable.get(s.charAt(i)) != t.charAt(i)) {
                return false;
            }
            if (hTable.containsKey(t.charAt(i)) && hTable.get(t.charAt(i)) != s.charAt(i)) {
                return false;
            }
            hTable.put(s.charAt(i), t.charAt(i));
            hTable.put(t.charAt(i), s.charAt(i));
        }
        return true;
    }

    public boolean isIsomorphic02(String s, String t) {
        int[] m1 = new int[256];
        int[] m2 = new int[256];
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (m1[s.charAt(i)] != m2[t.charAt(i)])
                return false;
            m1[s.charAt(i)] = i + 1;
            m2[t.charAt(i)] = i + 1;
        }
        return true;
    }

    //----------------------------------------------------------------------------------------------------
    // 上下两题，上面一维，下面是二维，都是要保证 ##单向一对一##映射，而不要求双向映射，即 a -> b 则必有 b -> a
    //----------------------------------------------------------------------------------------------------

    /**
     * 290. 单词规律
     */
    public boolean wordPattern(String pattern, String str) {
        String[] arr2 = str.split("\\s+");
        if (pattern.length() != arr2.length) return false;
        //记录 str 中各个字符串首次出现的索引，等效于判断字符串中单个字符首次出现的索引位置 indexof
        HashMap<String, Integer> hTable = new HashMap<>();
        for (int i = 0; i < arr2.length; i++) {
            if (!hTable.containsKey(arr2[i])) {
                hTable.put(arr2[i], i);
            }
        }
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.indexOf(pattern.charAt(i)) != hTable.get(arr2[i])) {
                return false;
            }
        }
        return true;
    }

    public boolean wordPattern01(String pattern, String str) {
        String[] words = str.split("\\s+");
        if (words.length != pattern.length()) {
            return false;
        }
        Map<Object, Integer> map = new HashMap<>();
        for (Integer i = 0; i < words.length; i++) {
            //-------------------------------------------------------
            // HashMap在插入元素的时候，无论相同的 Key 是否存在，均执行插入，同时会有返回值（用的少）
            //    1、如果 Key不存在，返回 null
            //    2、如果 Key存在，返回覆盖前的 Value
            // 根据本题要求，二者应该都是首次出现，或者之前均出现，且对应索引位置相同，如果仅某一个存在，则不满足双向映射的要求
            //-------------------------------------------------------
            if (!Objects.equals(map.put(pattern.charAt(i), i), map.put(words[i], i))) {
                return false;
            }
        }
        return true;
    }

    public boolean wordPattern00(String pattern, String str) {
        String[] words = str.trim().split("\\s+");
        if (pattern.length() != words.length) return false;
        HashMap<Character, String> hTable = new HashMap<>();
        //-------------------------------------------------------
        // 单向映射，分别基于 Key 和 Value 来判断是否满足单向映射
        //-------------------------------------------------------
        for (int i = 0; i < pattern.length(); i++) {
            //1、一对多，pattern 中一个元素对应 str 中多个字符串
            if (hTable.containsKey(pattern.charAt(i)) && !hTable.get(pattern.charAt(i)).equals(words[i])) {  //基于 Key 来判断单向映射
                return false;
            }
            //2、多对一，pattern 中多个元素对应 str 中一个字符串
            if (!hTable.containsKey(pattern.charAt(i)) && hTable.containsValue(words[i])) {  //基于 Value 来判断单向映射
                return false;
            }
            hTable.put(pattern.charAt(i), words[i]);
        }
        return true;
    }


    /**
     * 763. 划分字母区间
     */
    public List<Integer> partitionLabels(String str) {
        int n = str.length();
        int[] last = new int[26];
        Arrays.fill(last, -1);  //默认不存在
        HashSet<Character> set = new HashSet<>();
        for (int i = n - 1; i >= 0; i--) {
            if (!set.contains(str.charAt(i))) {
                last[str.charAt(i) - 'a'] = i;
            }
            set.add(str.charAt(i));
        }
        int prevIndex = 0;
        int nextIndex = -1;
        ArrayList<Integer> ans = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nextIndex = Math.max(nextIndex, last[str.charAt(i) - 'a']);
            if (nextIndex == i) {
                ans.add(nextIndex - prevIndex + 1);
                prevIndex = nextIndex + 1;
            }
        }
        return ans;
    }


    /**
     * 49. 字母异位词分组
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        for (String str : strs) {
            char[] arr = str.toCharArray();
            Arrays.sort(arr);
            String sorted = String.valueOf(arr);
            ArrayList<String> list = map.getOrDefault(sorted, new ArrayList<>());
            list.add(str);
            map.put(sorted, list);
        }
        return new ArrayList<>(map.values());
    }

    public List<List<String>> groupAnagrams01(String[] strs) {  //错误写法，如果单个字符串中有重复字符，则会错误
        HashMap<Integer, ArrayList<String>> map = new HashMap<>();
        for (String str : strs) {
            int digit = 0;
            for (int i = 0; i < str.length(); i++) {
                int nums = str.charAt(i) - 'a';
                digit |= (1 << nums);
            }
            ArrayList<String> arr = map.getOrDefault(digit, new ArrayList<>());
            arr.add(str);
            map.put(digit, arr);
        }
        List<List<String>> ans = new ArrayList<>();
        for (int digit : map.keySet()) {
            ans.add(map.get(digit));
        }
        return ans;
    }


    /**
     * 43. 字符串相乘
     */
    public String multiply(String num1, String num2) {   //模拟乘法过程
        if (num1.equals("0") || num2.equals("0")) return "0";   //特殊情况
        int n1 = num1.length();
        int n2 = num2.length();
        int[] result = new int[n1 + n2];
        //-----------------------------------------------
        // 通过一个数组来记录两个数字逐位相乘的中间结果
        //-----------------------------------------------
        for (int i = n1 - 1; i >= 0; i--) {
            int a = num1.charAt(i) - '0';
            for (int j = n2 - 1; j >= 0; j--) {   // 当前位为 i + j + 1
                int b = num2.charAt(j) - '0';
                int res = a * b + result[i + j + 1];   //计算当前位的值，在当前为的状态上增加两位相乘的结果
                result[i + j + 1] = res % 10;          //更新当前位状态，更新为累加后的状态
                result[i + j] += res / 10;             //更新前一位状态，在原有状态基础上，增加当前位的进位结果
            }
        }
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            if (ans.length() == 0 && result[i] == 0) continue;
            ans.append(result[i]);
        }
        return ans.toString();
    }


    /**
     * 415. 字符串相加
     */
    public String addStrings(String num1, String num2) {
        StringBuilder ans = new StringBuilder();
        int index1 = num1.length() - 1;
        int index2 = num2.length() - 1;
        int add = 0;
        while (index1 >= 0 || index2 >= 0 || add > 0) {
            int xx = index1 >= 0 ? num1.charAt(index1) - '0' : 0;   //此处为减字符 '0'
            int yy = index2 >= 0 ? num2.charAt(index2) - '0' : 0;   //此处为减字符 '0'
            int sum = xx + yy + add;
            ans.append(sum % 10);
            add = sum / 10;
            index1--;
            index2--;
        }
        return ans.reverse().toString();
    }


    /**
     * 795. 区间子数组个数
     */
    public int numSubarrayBoundedMax(int[] nums, int left, int right) {
        int ans = 0;
        //-------------------------------------------------------------
        // 一定写在外面，用于保留前一位的状态值
        // 针对 nums[i] < left 时使用，此时 i 位能构成的数组个数和前一位一致，相当于在前一位所有数组的基础上增加一个 nums[i]元素
        //-------------------------------------------------------------
        int res = 0;
        int prevIndex = -1;   //最后一个不满足条件索引位置，类似前挡板
        for (int i = 0; i < nums.length; i++) {
            //------------------------------------------------------------
            // 只有在数组中的最大值超过允许的最大值的情况下，才会更新前挡板的索引
            //------------------------------------------------------------
            if (nums[i] > right) {
                prevIndex = i;
            }
            //------------------------------------------------------------
            // 包含两种情况：
            //     1、nums[i] 小于等于 right，即在合理的范围内，正常计算数组个数
            //     2、nums[i] 大于     right，即不在合理范围内，则正好和上面新更新 prevIndex 抵消为 0，对最终结果无影响
            //------------------------------------------------------------
            if (nums[i] >= left) {
                res = i - prevIndex;
            }
            //------------------------------------------------------------
            // 三种情况在此汇总，前两种情况已经在上面说明过了，针对第三种情况，进行说明
            //     3、针对 nums[i] < left 时使用，此时 i 位能构成的数组个数和前一位一致，相当于在前一位所有数组的基础上增加一个 nums[i]元素
            //------------------------------------------------------------
            ans += res;
        }
        return ans;
    }

    public int numSubarrayBoundedMax01(int[] nums, int left, int right) {
        int ans = 0;
        int prev = -1;       //记录上一个范围在 [left,right]中的数字索引
        int prevPrev = -1;   //记录上一个超过 right 的数字索引
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > right) prevPrev = i;
//            if (nums[i] <= right && nums[i] >= left) prev = i;  //理论这样写，但其不能保证 prev >= prevPrev
            if (nums[i] >= left) prev = i;
            ans += prev - prevPrev;
        }
        return ans;
    }

    public int numSubarrayBoundedMax02(int[] nums, int left, int right) {
        int ans = 0;
        int prev = -1;       //记录上一个范围在 [left,right]中的数字索引
        int prevPrev = -1;   //记录上一个超过 right 的数字索引
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > right) { //一旦 nums[i] 大于最大值的可容忍的上限，则其相当于一个屏障或分水岭，则数组的起点和重点
                prevPrev = i;
                prev = i;          //保证 prev 在 prevPrev后面，即  prev >= prevPrev
            }
            if (nums[i] <= right && nums[i] >= left) prev = i;
            ans += prev - prevPrev;
        }
        return ans;
    }


    /**
     * 2444. 统计定界子数组的数目
     */
    public long countSubarrays(int[] nums, int minK, int maxK) {
        long ans = 0;
        int minIndex = -1;
        int maxIndex = -1;
        int outIndex = -1;
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            if (nums[i] < minK || nums[i] > maxK) outIndex = i;
            if (nums[i] == maxK) maxIndex = i;
            if (nums[i] == minK) minIndex = i;
            ans += Math.max(Math.min(maxIndex, minIndex) - outIndex, 0);
        }
        return ans;
    }

    public long countSubarrays00(int[] nums, int minK, int maxK) {   //最终自己写的，通过，但没来得及提交
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
     * 809. 情感丰富的文字
     */
    public int expressiveWords(String str, String[] words) {
        int ans = 0;
        for (String word : words) {
            if (verifyExpand(str, word)) {
                ans++;
            }
        }
        return ans;
    }

    private boolean verifyExpand(String target, String word) {
        int m = target.length();
        int n = word.length();
        if (m < n) return false;
        int index1 = 0;
        int index2 = 0;
        while (index1 < m && index2 < n) {
            if (target.charAt(index1) != word.charAt(index2)) {
                return false;
            }
            char ch = target.charAt(index1);
            int nums1 = 0;
            while (index1 < m && target.charAt(index1) == ch) {
                nums1++;     //相同字符的个数
                index1++;    //下一个索引位置
            }
            int nums2 = 0;
            while (index2 < n && word.charAt(index2) == ch) {
                nums2++;
                index2++;
            }

            if (nums1 < nums2) return false;
            if (nums1 > nums2 && nums1 < 3) return false;

        }

        return index1 == m && index2 == n;
    }

    public int expressiveWords01(String str, String[] words) {  //自己写的，虽然通过，但逻辑没有上面的清晰
        int ans = 0;
        int m = str.length();
        for (String word : words) {
            int n = word.length();
            if (m < n) continue;
            int index1 = 0;
            int index2 = 0;
            while (index1 < m && index2 < n) {
                //判断当前位的字符是否相等，不相等直接退出
                if (str.charAt(index1) != word.charAt(index2)) {
                    break;
                }
                //分别计算当前位置字符的个数
                int[] tuple1 = expressiveHelper(str, index1);
                int nums1 = tuple1[0];
                index1 = tuple1[1];

                int[] tuple2 = expressiveHelper(word, index2);
                int nums2 = tuple2[0];
                index2 = tuple2[1];

                if (nums1 > nums2 && nums1 < 3) {
                    break;
                }
                if (nums1 < nums2) {
                    break;
                }
                if (index1 == m && index2 == n) {   //放这里，不能放外面，放外面会误判 "lee" 和 "le" 满足条件
                    ans++;
                }
            }
        }
        return ans;
    }

    private int[] expressiveHelper(String str, int startIndex) {
        if (startIndex == str.length()) return new int[]{0, 0};
        int nums = 1;
        startIndex++;  //朝后窜一位
        while (startIndex < str.length()) {
            if (str.charAt(startIndex) != str.charAt(startIndex - 1)) {
                break;
            }
            nums++;
            startIndex++;
        }
        return new int[]{nums, startIndex};
    }


    /**
     * 1752. 检查数组是否经排序和轮转得到
     */
    public boolean check(int[] nums) {
        int peakNums = 0;  //拐点个数
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > nums[(i + 1) % nums.length]) {   //当成一个闭环
                peakNums++;
            }
        }
        return peakNums <= 1;  // [1,1,1] 元素值全部相等时，peakNums=0，其余情况，无论数组是否与原数组相等，其均应有一个拐点
    }

    public boolean check01(int[] nums) {
        int count = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i - 1] > nums[i]) count++;
            if (count > 1) return false;
        }
        return count == 0 || (count == 1 && nums[0] >= nums[nums.length - 1]);
    }


    /**
     * 1781. 所有子字符串美丽值之和
     */
    public int beautySum(String str) {
        int ans = 0;
        int n = str.length();
        for (int i = 0; i < n; i++) {      //枚举区间左端点
            int[] buckets = new int[26];
            for (int j = i; j < n; j++) {  //枚举区间右端点
                buckets[str.charAt(j) - 'a']++;
                //初始化区间内最大频次数和最小频次数
                int maxFreq = 0;
                int minFreq = n;
                for (int m = 0; m < 26; m++) {  //计算最大和最小频次
                    maxFreq = Math.max(maxFreq, buckets[m]);
                    minFreq = buckets[m] == 0 ? minFreq : Math.min(minFreq, buckets[m]);
                }
                ans += maxFreq - minFreq;
            }
        }
        return ans;
    }


    /**
     * 1796. 字符串中第二大的数字
     */
    public int secondHighest(String str) {
        int[] buckets = new int[10];
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                buckets[str.charAt(i) - '0']++;
            }
        }
        int nums = 0;
        for (int i = 9; i >= 0; i--) {
            if (buckets[i] > 0) {
                nums++;
                if (nums == 2) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int secondHighest01(String str) {  //基于水流滤网的思想，特别好的想法，和有一个题很类似
        int first = -1;
        int second = -1;
        int n = str.length();
        for (int i = 0; i < n; i++) {
            if (Character.isDigit(str.charAt(i))) {
                int currNums = str.charAt(i) - '0';
                if (first < currNums) {
                    second = first;
                    first = currNums;
                } else if (second < currNums && currNums < first) {
                    second = currNums;
                }
            }
        }
        return second;
    }


    /**
     * 1805. 字符串中不同整数的数目
     */
    public int numDifferentIntegers(String word) {
        int left = 0;
        int n = word.length();
        HashSet<String> ans = new HashSet<>();
        while (true) {
            //寻找数字串的左端点
            while (left < n && !Character.isDigit(word.charAt(left))) {
                left++;   //left最终停留在数字串的左端点
            }
            //后续不存在数字
            if (left == n) break;
            //后续存在数字
            int right = left;
            while (right < n && Character.isDigit(word.charAt(right))) {
                right++;  //right最终停留在数字串的右端点的下一位
            }
            //去除前导 0
            while (right - left > 1 && word.charAt(left) == '0') {  //只有在数字串长度大于 1 时才考虑前导 0，单独的 0 不会剔除
                left++;
            }
            ans.add(word.substring(left, right));
            //重置开始搜索的左端点
            left = right;  //关键
        }
        return ans.size();
    }

    /**
     * 1812. 判断国际象棋棋盘中一个格子的颜色
     */
    public boolean squareIsWhite(String coordinates) {
        int xx = coordinates.charAt(0) - 'a';
        int yy = coordinates.charAt(1) - '1';
        return ((xx & 1) == 1 && (yy & 1) == 0) || ((xx & 1) == 0 && (yy & 1) == 1);  //两种情况
    }


    /**
     * 1827. 最少操作使数组递增
     */
    public int minOperations(int[] nums) {
        int ans = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i - 1] >= nums[i]) {
                ans += nums[i - 1] - nums[i] + 1;  //累加需要执行的操作数
                nums[i] = nums[i - 1] + 1;         //更新当前位置的值
            }
        }
        return ans;
    }

    public int minOperations01(int[] nums) {
        int ans = 0;
        for (int i = 1; i < nums.length; i++) {
            while (nums[i - 1] >= nums[i]) {
                ans++;
                nums[i]++;
            }
        }
        return ans;
    }


    /**
     * 1785. 构成特定和需要添加的最少元素
     */
    public int minElements(int[] nums, int limit, int goal) {
        long sum = 0;
        for (int num : nums) {
            sum += num;
        }
        //--------------------------------------------------------
        // 错误写法，是在各个 num 累加后（已经溢出了）再转换为 long，这对于防止溢出是无效的
        // long sum = Arrays.stream(nums).sum();
        //--------------------------------------------------------
        long diff = Math.abs(sum - goal);
        return (int) ((diff + limit - 1) / limit);  // diff / limit 向上取整
    }


    /**
     * 1764. 通过连接另一个数组的子数组得到一个数组
     */
    public boolean canChoose(int[][] groups, int[] nums) {   //双指针
        int index1 = 0;
        int index2 = 0;
        while (index1 < groups.length && index2 < nums.length) {
            if (canChooseHelper(groups[index1], nums, index2)) {  //存在
                index2 += groups[index1].length;  //更新在 nums 可搜索的最左侧位置
                index1++;   //寻找下一个目标数组
            } else {
                index2++;   //尝试从 nums 的下一位位置开始匹配 groups[index1]
            }
        }
        return index1 == groups.length;
    }

    private boolean canChooseHelper(int[] target, int[] nums, int index2) { //校验当前目标值数组，是否在 nums 数组中的 index2 位置后存在
        if (index2 + target.length > nums.length) {
            return false;
        }
        for (int i = 0; i < target.length; i++) {
            if (target[i] != nums[index2 + i]) {   //不要更新 index2
                return false;
            }
        }
        return true;
    }


    public boolean canChoose01(int[][] groups, int[] nums) {
        HashMap<Integer, ArrayDeque<Integer>> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            ArrayDeque<Integer> queue = map.getOrDefault(nums[i], new ArrayDeque<>());
            queue.addLast(i);
            map.put(nums[i], queue);
        }
        //作用在 nums 上的指针
        int allIndex = 0;
        for (int m = 0; m < groups.length; m++) {
            //当前目标数组的首个元素值
            int currValue = groups[m][0];
            //获取此元素值在 nums 中的位置索引队列
            ArrayDeque<Integer> queue = map.getOrDefault(currValue, new ArrayDeque<>());
            //剔除相交的情况
            while (!queue.isEmpty() && queue.peekFirst() < allIndex) {
                queue.pollFirst();
            }
            //后续无可用元素
            if (queue.isEmpty()) return false;
            //迭代终止标识
            int pollNext = 1;
            while (!queue.isEmpty() && pollNext == 1) {
                Integer currIndex = queue.pollFirst();
                if (currIndex + groups[m].length > nums.length) {
                    return false;
                }
                for (int i = 0; i < groups[m].length; i++) {  //从 0 开始，虽然第一个元素是相等的，但考虑 group中仅有一个元素时，更新边界
                    if (groups[m][i] == nums[currIndex++]) {
                        if (i == groups[m].length - 1) {  //这一组完全匹配
                            //更新队列
                            if (!queue.isEmpty()) {
                                map.put(currValue, queue);
                            }
                            //更新指针
                            allIndex = currIndex;

                            //后续已经不满足情况
                            if (m < groups.length - 1 && allIndex >= nums.length) {
                                return false;
                            }

                            //更新标识
                            pollNext = 0;   // currValue 对应的 group 已经完成搜索，无需再从 currValue 对应的队列中提取元素
                        }
                    } else {
                        break;
                    }
                }
            }
            if (pollNext == 1) return false;
        }
        return true;
    }


    /**
     * 1753. 移除石子的最大得分
     */
    public int maximumScore(int a, int b, int c) {
        int ans = 0;
        PriorityQueue<Integer> sortedQueue = new PriorityQueue<>((o1, o2) -> o2 - o1);  //降序排序
        sortedQueue.add(a);
        sortedQueue.add(b);
        sortedQueue.add(c);
        while (sortedQueue.size() >= 2) {
            ans++;
            Integer xx = sortedQueue.poll();
            --xx;
            Integer yy = sortedQueue.poll();
            --yy;
            if (xx > 0) sortedQueue.add(xx);
            if (yy > 0) sortedQueue.add(yy);
        }
        return ans;
    }

    public int maximumScore01(int a, int b, int c) {
        int sum = a + b + c;
        int max = Math.max(Math.max(a, b), c);
        int res = sum - max;
        return max > res ? res : sum / 2;  //如果max不大于other，此时sum为偶数时必可以拿完，sum为奇数时必然剩一个，得分sum/2
    }


    /**
     * 1754. 构造字典序最大的合并字符串
     */
    public String largestMerge(String word1, String word2) {
        StringBuilder ans = new StringBuilder();
        while (word1.length() + word2.length() > 0) {
            //--------------------------------------------------------------------------------
            // 本题关键：当首字符相等时，选择那个字符串中的首字符
            // 为了使得最总结果尽可能地大，因此应该贪心的选择 当前字典序最大的字符串的首字母，这样使得后面字典序更大的字符向前移动，会更快的被选择
            //--------------------------------------------------------------------------------
            if (word1.compareTo(word2) > 0) {   //比较字典序
                ans.append(word1.charAt(0));
                word1 = word1.substring(1);
            } else {
                ans.append(word2.charAt(0));
                word2 = word2.substring(1);
            }
        }
        return ans.toString();
    }

    public String largestMerge01(String word1, String word2) {  //错误写法，在首字符相等时，随机选取一个
        int m = word1.length();
        int n = word2.length();
        int index1 = 0;
        int index2 = 0;
        StringBuilder ans = new StringBuilder();
        while (index1 < m || index2 < n) {
            if (index1 < m && index2 < n) {
                char xx = word1.charAt(index1);
                char yy = word2.charAt(index2);
                if (xx >= yy) {
                    ans.append(xx);
                    index1++;
                } else {
                    ans.append(yy);
                    index2++;
                }
            } else if (index1 < m) {
                ans.append(word1.charAt(index1));
                index1++;
            } else {
                ans.append(word2.charAt(index2));
                index2++;
            }
        }
        return ans.toString();
    }


    /**
     * 697. 数组的度
     */
    public int findShortestSubArray(int[] nums) {
        HashMap<Integer, int[]> hTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (hTable.containsKey(nums[i])) {
                hTable.get(nums[i])[1] = i;   //更新其最后出现位置的索引
                hTable.get(nums[i])[2]++;
            } else {
                hTable.put(nums[i], new int[]{i, i, 1});
            }
        }
        int maxFreq = -1;
        int ans = 0;
        for (int num : hTable.keySet()) {
            int[] tuple = hTable.get(num);
            int freq = tuple[2];
            if (maxFreq < freq) {
                ans = tuple[1] - tuple[0] + 1;
                maxFreq = freq;
            } else if (maxFreq == freq) {
                ans = Math.min(ans, tuple[1] - tuple[0] + 1);
            }
        }
        return ans;
    }


    /**
     * 821. 字符的最短距离
     */
    public int[] shortestToChar(String str, char ch) {
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

    public int[] shortestToChar02(String str, char ch) {   //基于广度优先搜索
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


    /**
     * 999. 可以被一步捕获的棋子数
     */
    public int numRookCaptures(char[][] board) {
        int ans = 0;
        int row = -1;
        int col = -1;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        //寻找出发点
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 'R') {
                    row = i;
                    col = j;
                    break;
                }
            }
            if (row != -1) break;
        }
        //从起点向四个方向搜索
        for (int[] dir : directions) {
            int steps = 1;
            while (steps <= 8) {
                int nextRow = row + steps * dir[0];
                int nextCol = col + steps * dir[1];
                //剪枝一：此方向越界
                if (nextRow < 0 || nextRow >= 8 || nextCol < 0 || nextCol >= 8) {
                    break;
                }
                //剪枝二：此方向阻碍
                if (board[nextRow][nextCol] == 'B') {
                    break;
                }
                //目标点
                if (board[nextRow][nextCol] == 'p') {
                    ans++;
                    break;
                }
                steps++;
            }
        }
        return ans;
    }

    /**
     * 1260. 二维网格迁移
     */
    public List<List<Integer>> shiftGrid(int[][] grid, int k) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] graph = new int[m][n];
        List<List<Integer>> ans = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int index = i * n + j + k;
                int row = (index / n) % m;
                int col = index % n;
                graph[row][col] = grid[i][j];
            }
        }
        for (int i = 0; i < m; i++) {
            ArrayList<Integer> currRow = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                currRow.add(graph[i][j]);
            }
            ans.add(currRow);
        }
        return ans;
    }

    //855. 考场就座

    /**
     * 2037. 使每位学生都有座位的最少移动次数
     */
    public int minMovesToSeat(int[] seats, int[] students) {
        int ans = 0;
        Arrays.sort(seats);
        Arrays.sort(students);
        for (int i = 0; i < seats.length; i++) {
            ans += Math.abs(seats[i] - students[i]);
        }
        return ans;
    }


    /**
     * 1802. 有界数组中指定下标处的最大值
     */
    public int maxValue(int n, int index, int maxSum) {  //二分搜索 + 数学求和
        int left = 1;
        int right = maxSum;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            long currSum = mid + getSum(mid - 1, index) + getSum(mid - 1, n - index - 1);
            if (currSum <= maxSum) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return right;
    }

    private long getSum(int max, int len) {
        //---------------------------------------
        // 贪心计算单侧和，贪心逻辑：类比山峰，在地面高度和下山步数固定时
        //    想要使得山峰高度最高，那么每走一步都应该是下降的趋势，进而上山是每一步都是上山
        // 补充：地面高度最低为 1、下山步数为单侧到达边界的长度
        //---------------------------------------
        if (max > len) {
            int small = max - len + 1;
            return (long) (max + small) * len / 2;   //（首项 + 末项）*项数 / 2
        } else {
            int oneSum = len - max;  //冗余 1 的个数，平路的长度
            int small = 1;
            return (long) (max + small) * max / 2 + oneSum;
        }
    }

    private long getSum01(int n, int index, int maxSum, int res) {  //暴力求和
        long sum = res;
        for (int i = index - 1; i >= 0 && sum <= maxSum; i--) sum += Math.max(res - (index - i), 1);
        for (int i = index + 1; i < n && sum <= maxSum; i++) sum += Math.max(res - (i - index), 1);
        return sum;
    }


    public int maxValue01(int n, int index, int maxSum) {
        for (int res = maxSum; res >= 1; res--) {  //暴力枚举
            long sum = res;
            //暴力求和
            for (int i = index - 1; i >= 0 && sum <= maxSum; i--) sum += Math.max(res - (index - i), 1);
            for (int i = index + 1; i < n && sum <= maxSum; i++) sum += Math.max(res - (i - index), 1);
            //判断是否满足条件
            if (sum <= maxSum) return res;
        }
        return -1;
    }


    /**
     * 1488. 避免洪水泛滥
     */
    public int[] avoidFlood(int[] rains) {    //贪心，超时
        int n = rains.length;
        //记录各天抽干的池塘编号
        int[] ans = new int[n];  //雨天，记录 -1；晴天，记录抽干的池塘编号，注意：如果无池塘可抽，也不能赋值为 -1
        Arrays.fill(ans, 1);
        //记录当前满池的湖水编号
        HashSet<Integer> prevFillLake = new HashSet<>();
        //记录各个湖水，未来下雨的日期
        int[] nextRainDay = new int[n];  // nextRainDay[i] 表示 池塘 rains[i] 下次下雨的日期
        HashMap<Integer, Integer> currMap = new HashMap<>();  //辅助记录
        //初始化各个湖水下雨的日期
        for (int i = n - 1; i >= 0; i--) {
            int lakeId = rains[i];
            if (lakeId > 0) {
                nextRainDay[i] = currMap.getOrDefault(lakeId, n);  //记录当前下雨的池塘，下次下雨的日期
                currMap.put(lakeId, i);    //更新当前池塘"最早"的下雨日期
            }
        }
        //优先队列，如果当前下雨池塘，后续仍会下雨，则记录其池塘编号、下雨日期
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[1] - o2[1]);  //按照下雨日期升序排序
        //-------------------------------------------------------
        // 按照时间顺序，模拟整个过程，在晴天时贪心的寻找要抽干的池塘
        //-------------------------------------------------------
        for (int i = 0; i < n; i++) {
            int lakeId = rains[i];
            //1、当前为雨天，更新相关信息
            if (lakeId > 0) {
                ans[i] = -1;  //今天不会抽干任意一个池塘
                if (prevFillLake.contains(lakeId)) {  //如果此池塘下过雨，但未抽干，发生洪水，直接返回
                    return new int[]{};
                }
                prevFillLake.add(lakeId);  //维护满载的池塘编号
                if (nextRainDay[i] < n) {  //关键，此池塘后续仍会下雨，记录其下雨的日期
                    sortedQueue.add(new int[]{lakeId, nextRainDay[i]});
                }
            }
            //2、当前为晴天，贪心的寻找要抽干的池塘
            if (lakeId == 0) {
                //---------------------------------------------------------------
                // 贪心逻辑：被选择的池塘满足：已经下过雨、后续还会下雨、下雨日期最早
                //---------------------------------------------------------------
                if (!sortedQueue.isEmpty()) {
                    int[] tuple = sortedQueue.poll();
                    int curLake = tuple[0];
                    ans[i] = curLake;  //今天抽取此湖水
                    prevFillLake.remove(curLake);  //湖水被抽干
                }
            }
        }
        return ans;
    }

    public int[] avoidFlood00(int[] rains) {    //贪心，超时
        int n = rains.length;
        //记录各天抽干的池塘编号
        int[] ans = new int[n];  //雨天，记录 -1；晴天，记录抽干的池塘编号，注意：如果无池塘可抽，也不能赋值为 -1
        Arrays.fill(ans, 1);
        //记录当前满池的湖水编号
        HashSet<Integer> prevFillPool = new HashSet<>();
        //记录各个湖水，未来下雨的日期（顺序记录）
        HashMap<Integer, LinkedList<Integer>> allPool_NextRainDays = new HashMap<>();
        //初始化各个湖水下雨的日期
        for (int i = 0; i < n; i++) {
            if (rains[i] != 0) {  //下雨
                LinkedList<Integer> days = allPool_NextRainDays.getOrDefault(rains[i], new LinkedList<>());
                days.addLast(i);  //第几天
                allPool_NextRainDays.put(rains[i], days);  //湖水编号：下雨日期
            }
        }
        //---------------------------------------------
        // 按照时间顺序，模拟每天下雨的情况
        //---------------------------------------------
        for (int i = 0; i < n; i++) {  //第几天
            //池塘编号
            int currPool = rains[i];
            //1、雨天，会在某个池塘处下雨
            if (currPool != 0) {
                //1.1、如果当前池塘，已经下过雨，则不满足条件，直接返回
                if (prevFillPool.contains(currPool)) {
                    return new int[]{};
                } else { //1.2、当前池塘未下过雨，则记录相关信息
                    //下雨天，无池塘可抽取
                    ans[i] = -1;
                    //当前池塘下雨
                    prevFillPool.add(currPool);
                    //更新当前池塘未来下雨的日期
                    LinkedList<Integer> currPool_NextRainDays = allPool_NextRainDays.get(currPool);
                    currPool_NextRainDays.pollFirst();  //此池塘，今天下雨，故被遗弃（更新）
                    if (currPool_NextRainDays.size() == 0) {  //后续无下雨的日期，不再添加
                        allPool_NextRainDays.remove(currPool);
                    }
                }
            }
            //2、晴天，可以抽干一个池塘的水
            if (currPool == 0) {
                int currRemovePool = -1;
                //----------------------------------------------------------------------------------
                // 贪心的选择想要抽干的湖水，贪心逻辑：从当前已满的湖水中选出，从当前日期后，第一个下雨的湖水
                //----------------------------------------------------------------------------------
                for (int prevRainPool : prevFillPool) {
                    if (currRemovePool == -1) {   //初始化
                        currRemovePool = prevRainPool;
                    } else if (allPool_NextRainDays.containsKey(prevRainPool)) {  //1、当前满湖，同时日后仍会下雨的湖水
                        //1、当前初始化的待抽取的池塘，在后续没有下雨天，则贪心的选择后续有下雨天的的池塘
                        if (!allPool_NextRainDays.containsKey(currRemovePool)) {
                            currRemovePool = prevRainPool;
                        } else if (allPool_NextRainDays.get(prevRainPool).peekFirst() < allPool_NextRainDays.get(currRemovePool).peekFirst()) {
                            //2、在两个之前下过雨的池塘中，贪心的选择后续下雨最早的池塘
                            currRemovePool = prevRainPool;
                        }
                    }
                }
                //---------------------------------------------------------
                // 待抽干的湖水编号已经确认，更新相关元数据信息
                //---------------------------------------------------------
                if (currRemovePool != -1) {  //如果当前有可以抽取的湖水
                    //记录抽取湖水编号
                    ans[i] = currRemovePool;
                    //更新当前已满的湖水编号列表
                    prevFillPool.remove(currRemovePool);  //移除
                }
            }
        }
        return ans;
    }


    //---------------------------------------------------------
    // 上下两种贪心写法的差异：
    //    上面以晴天为基准，尝试从之前下过雨的池塘中选择出一个进行抽干，贪心逻辑：下过雨的池塘中，在当前日期后，后续下雨最早的池塘
    //    下面以雨天为基准，贪心逻辑同上，但判断失败的节点不同
    //---------------------------------------------------------

    public int[] avoidFlood01(int[] rains) {  //通过
        int n = rains.length;
        //记录晴天抽干的湖水编号
        int[] ans = new int[n];   //如果是阴天，赋值为 -1，晴天赋值不能为 -1（即使晴天没有可抽干的池塘）
        Arrays.fill(ans, 1);
        //记录之前下雨且当前未被抽干的池塘信息：池塘编号，下雨日期
        HashMap<Integer, Integer> prevRains = new HashMap<>();
        TreeSet<Integer> sunnyDays = new TreeSet<>();  //基于 treeSet 实现二分搜索
        for (int i = 0; i < n; i++) {
            int curPool = rains[i];
            //1、当天为晴天，记录晴天日期
            if (curPool == 0) {
                sunnyDays.add(i);
                continue;
            }
            //2、当天为雨天，判断当前是否会导致洪水
            ans[i] = -1;  //当天下雨不能操作
            if (prevRains.containsKey(curPool)) {  //当前池塘之前下过雨
                Integer nextSunnyDay = sunnyDays.ceiling(prevRains.get(curPool));
                //如果上次下过雨后，没有可用的晴天，则会知道当前池塘发生洪水
                if (nextSunnyDay == null) {
                    return new int[]{};
                }
                //有可用的晴天，则此晴天需要抽干此池塘中上次下雨的水
                ans[nextSunnyDay] = curPool;
                sunnyDays.remove(nextSunnyDay);  //此晴天已被使用
            }
            prevRains.put(curPool, i);  //无论此池塘之前是否下过雨，能运行至此处说明都需要更新当前池塘下雨的日期
        }
        return ans;
    }

    /**
     * 640. 求解方程
     */
    public String solveEquation(String equation) {  //模拟，移项 + 合并同类项
        String[] arr = equation.split("=");
        String[] arr1 = arr[0].replace("-", "+-").split("\\+");
        String[] arr2 = arr[1].replace("-", "+-").split("\\+");
        int prefix = 0;  //记录 x 的系数
        int sum = 0;     //记录累加和
        //1、等号左侧
        int[] tuple1 = solveEquationHelper(arr1);
        prefix += tuple1[0];
        sum -= tuple1[1];

        //2、等号右侧
        int[] tuple2 = solveEquationHelper(arr2);
        prefix -= tuple2[0];
        sum += tuple2[1];

        if (prefix == 0) {
            if (sum == 0) return "Infinite solutions";
            else return "No solution";
        }
        if (sum % prefix != 0) return "No solution";
        return "x=" + sum / prefix;
    }

    private int[] solveEquationHelper(String[] arr) {
        int sum = 0;
        int prefix = 0;
        for (String cur : arr) {
            if (cur.equals("")) {   //不能忽略
                continue;
            }
            if (cur.equals("x")) {
                prefix++;
            } else if (cur.equals("-x")) {
                prefix--;
            } else if (cur.contains("x")) {
                if (cur.startsWith("-")) {
                    cur = cur.replace("-", "").replace("x", "");
                    prefix -= Integer.parseInt(cur);
                } else {
                    cur = cur.replace("x", "");
                    prefix += Integer.parseInt(cur);
                }
            } else {
                if (cur.startsWith("-")) {
                    cur = cur.replace("-", "");
                    sum -= Integer.parseInt(cur);
                } else {
                    sum += Integer.parseInt(cur);
                }
            }
        }
        return new int[]{prefix, sum};
    }


    /**
     * 1706. 球会落何处
     */
    public int[] findBall(int[][] grid) {  //模拟
        int m = grid.length;
        int n = grid[0].length;
        int[] curr = new int[n];
        for (int i = 0; i < n; i++) {
            curr[i] = i + 10;
        }
        for (int i = 0; i < m; i++) {
            int[] next = new int[n];
            for (int j = 0; j < n; j++) {
                if (curr[j] != 0) {
                    if (grid[i][j] == 1) {
                        if (j < n - 1 && grid[i][j + 1] == 1) {
                            next[j + 1] = curr[j];
                        }
                    } else {
                        if (j > 0 && grid[i][j - 1] == -1) {
                            next[j - 1] = curr[j];
                        }
                    }
                }
            }
            curr = next.clone();
        }
        int[] ans = new int[n];
        Arrays.fill(ans, -1);
        for (int i = 0; i < n; i++) {
            if (curr[i] > 0) {
                ans[curr[i] - 10] = i;
            }
        }
        return ans;
    }


    /**
     * 874. 模拟行走机器人
     */
    public int robotSim(int[] commands, int[][] obstacles) {  //模拟，其中更改方向是关键
        int ans = 0;
        //------------------------------------------------
        // 本题最核心得在于向左或向右更改方向，按照顺时针依次记录方向
        //------------------------------------------------
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};  //按照顺时针方向，北、东、南、西
        //记录每行中哪些点为障碍物
        HashMap<Integer, HashSet<Integer>> obs = new HashMap<>();
        //初始化
        for (int[] obstacle : obstacles) {
            int row = obstacle[0];
            int col = obstacle[1];
            HashSet<Integer> set = obs.getOrDefault(row, new HashSet<>());
            set.add(col);
            obs.put(row, set);
        }
        int currDir = 0; //初始方向为向北
        int currRow = 0;
        int currCol = 0;
        for (int i = 0; i < commands.length; i++) {
            int steps = commands[i];
            if (steps == -1) {           //向右转向
                currDir = (currDir + 1) % 4;
            } else if (steps == -2) {    //向左转向
                currDir = (currDir + 3) % 4;
            } else {                     //沿着当前方向行走
                for (int m = 1; m <= steps; m++) {  //仅计数
                    int nextRow = currRow + directions[currDir][0];
                    int nextCol = currCol + directions[currDir][1];
                    if (obs.containsKey(nextRow) && obs.get(nextRow).contains(nextCol)) {
                        break;   //遇到障碍物，直接跳出
                    }
                    currRow = nextRow;
                    currCol = nextCol;
                    ans = Math.max(ans, currRow * currRow + currCol * currCol);
                }
            }
        }
        return ans;
    }

    /**
     * 2287. 重排字符形成目标字符串
     */
    public int rearrangeCharacters(String str, String target) {
        int[] buckets = new int[26];
        for (int i = 0; i < str.length(); i++) {
            buckets[str.charAt(i) - 'a']++;
        }
        int ans = 0;
        while (true) {  //每轮都要处理一遍字符
            for (int i = 0; i < target.length(); i++) {   //逐一，target 字符有重复
                char ch = target.charAt(i);
                if (--buckets[ch - 'a'] < 0) {
                    return ans;
                }
            }
            ans++;
        }
    }

    public int rearrangeCharacters01(String str, String target) {  //错误写法
        int[] buckets = new int[26];
        for (int i = 0; i < str.length(); i++) {
            buckets[str.charAt(i) - 'a']++;
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < target.length(); i++) {
            min = Math.min(min, buckets[target.charAt(i) - 'a']);
        }
        return min;
    }


    /**
     * 885. 螺旋矩阵 III
     */
    public int[][] spiralMatrixIII(int m, int n, int currRow, int currCol) {
        int nums = m * n;
        int[][] ans = new int[nums][2];
        ans[0] = new int[]{currRow, currCol};
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};  //顺时针方向，东、南、西、北
        int steps = 1;     //当前在每个方向可行走的步数
        int currDir = 0;   //当前的方向
        int currNums = 1;  //当前满足条件位点数
        while (currNums < nums) {
            //1、沿着当前方向行走 steps 步
            for (int i = 0; i < steps; i++) {
                //1.1、沿着当前方向，继续向前移动
                currRow += dirs[currDir][0];
                currCol += dirs[currDir][1];
                //1.2、如果是有效位点
                if (currRow < m && currRow >= 0 && currCol < n && currCol >= 0) {
                    ans[currNums] = new int[]{currRow, currCol};
                    currNums++;
                }
            }
            //2、沿着顺时针，更改方向
            currDir++;
            //3、判断步长是否需要增加
            currDir %= 4;  //必须 %4，因为一共 4 个方向，如果 %2，则只是使用了两个方向
            if (currDir == 0 || currDir == 2) steps++;   //每改变两次方向，每个边的步长增加 1
            //以下为错误写法
//            currDir %= 2;
//            if (currDir == 0) steps++;   //每改变两次方向，每个边的步长增加 1
        }
        return ans;
    }


    /**
     * 2516. 每种字符至少取 K 个
     */
    public int takeCharacters(String str, int k) {
        int[] buckets = new int[3];
        for (int i = 0; i < str.length(); i++) {
            buckets[str.charAt(i) - 'a']++;
        }
        if (buckets[0] < k || buckets[1] < k || buckets[2] < k) {
            return -1;
        }
        int x = buckets[0] - k;    //中间最多含有 a 的个数
        int y = buckets[1] - k;    //中间最多含有 b 的个数
        int z = buckets[2] - k;    //中间最多含有 c 的个数
        int[] curBuckets = new int[3];
        int ans = 0;
        int left = 0;
        int right = 0;
        while (right < str.length()) {
            curBuckets[str.charAt(right) - 'a']++;
            while (curBuckets[0] > x || curBuckets[1] > y || curBuckets[2] > z) {
                curBuckets[str.charAt(left) - 'a']--;
                left++;
            }
            ans = Math.max(ans, right - left + 1);
            right++;
        }
        return str.length() - ans;
    }

    /**
     * 1663. 具有给定数值的最小字符串
     */
    public String getSmallestString(int n, int k) {  //贪心，反向添加
        //--------------------------------------------------------------------------------------------
        // 贪心：初始化字符串中各个字符全部为 'a'，从后往前每一位尽可能添加大字符，从而使得前面的字符可以最小
        //--------------------------------------------------------------------------------------------
        char[] ans = new char[n];
        Arrays.fill(ans, 'a');
        k -= n;  // n 个字符 'a'，每个字符 'a' 值为 1
        while (k > 0) {
            int curValue = Math.min(25, k); // 'z' 值为 26，相对于 'a' 多 25，此处仅考虑相较于 'a' 的增量
            ans[--n] += curValue;
            k -= curValue;
        }
        return String.valueOf(ans);
    }

    public String getSmallestString01(int n, int k) {  //贪心，正向添加
        StringBuilder ans = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            int curValue = k - (n - i) * 26;  //贪心认为从 i 位后所有字符均为 'z'，从而获取当前字符值
            if (curValue < 1) {  //1、如果从 i 位后所有字符均为 'z'，则没有满足条件的当前位
                //所以在当前 i 位存在的情况下，从 i 位后不可能所有字符均为 'z'，但当前位还必须存在，故贪心将 i 位视为 'a'
                ans.append('a');
                k -= 1;
            } else {             //2、如果从 i 位后所有字符均为 'z'，则存在满足条件的当前位
                ans.append((char) ('a' + curValue - 1));
                k -= curValue;
                break;  //当前位确定后，后续所有的位置均为 'z'，故可直接跳出
            }
        }
        //后续位置均为 'z'
        while (k > 0) {
            ans.append('z');
            k -= 26;
        }
        return ans.toString();
    }

    public String getSmallestString001(int n, int k) {  //贪心，正向添加（上述写法的变化形式）
        StringBuilder ans = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            int curValue = Math.max(1, k - (n - i) * 26);   //注意：curValue 一定是小于等于 26 的
            ans.append((char) ('a' + curValue - 1));
            k -= curValue;
        }
        return ans.toString();
    }


    public String getSmallestString02(int n, int k) {  //鸡兔同笼，详细见评论
        //------------------------------------------------------
        // 关键：最终字符串中仅会存在 <= 1 个非 'a' 和 'z' 的字符
        //      设字符 'a' 的个数为 x、字符 'z' 的个数为 y、另外一个字符为 bit（如果存在），则有以下方程
        //      x + 26y + bit = K 和 x + y + 1 = n
        //------------------------------------------------------
        int bit = 1;  //初始唯一字符为 'a'
        int y = 0;
        while (bit <= 26) { //逐一尝试各个字符是否满足条件
            double temp = getNums(k, n, bit);
            if (temp == (int) temp) {  //整除，合理
                y = (int) temp;
                break;
            }
            bit++;
        }
        StringBuilder ans = new StringBuilder();
        //1、如果所有字符均为 'z'
        if (y == n) {
            while (y > 0) {
                ans.append('z');
                y--;
            }
            return ans.toString();
        }
        //2、存在不为 'z' 的字符
        int x = n - y - 1;
        int nums = 1;
        while (nums <= x) {
            ans.append('a');
            nums++;
        }
        ans.append((char) ('a' + bit - 1));  //要有减 1
        nums++;
        while (nums <= n) {
            ans.append('z');
            nums++;
        }
        return ans.toString();
    }

    private double getNums(int k, int n, int z) {
        return (k - z - n + 1) * 1.0 / 25;  // y 的表达式
    }


    /**
     * 720. 词典中最长的单词
     */
    public String longestWord(String[] words) {
        String ans = "";
        Arrays.sort(words);
        HashSet<String> set = new HashSet<>();
        for (String word : words) {
            if (word.length() == 1 || set.contains(word.substring(0, word.length() - 1))) {
                set.add(word);
                //只有在长度更长时更新 ans，长度相等时，不更新 ans，因为当前 ans的字典序更小
                ans = word.length() > ans.length() ? word : ans;
            }
        }
        return ans;
    }

    /**
     * 2293. 极大极小游戏
     */
    public int minMaxGame(int[] nums) {  //递归
        int n = nums.length;
        //递归终止条件
        if (n == 1) {
            return nums[0];
        }
        int[] newNums = new int[n / 2];
        for (int i = 0; i < newNums.length; i++) {
            if ((i & 1) == 0) newNums[i] = Math.min(nums[2 * i], nums[2 * i + 1]);
            if ((i & 1) != 0) newNums[i] = Math.max(nums[2 * i], nums[2 * i + 1]);
        }
        return minMaxGame(newNums);  //进入下一轮递归
    }


    /**
     * 1813. 句子相似性 III
     */
    public boolean areSentencesSimilar(String sentence1, String sentence2) {  //双端队列
        //-----------------------------------------------------
        // 本题关键：以较短的句子为基准，如果在其左侧、中间、右侧添加一个句子，可以构成较长的句子，则满足条件
        // 因此，可以从两端比较，相同则向内收缩，如果某个队列变为空（即此句子为较短句子，且全部匹配了，故为 true）
        //-----------------------------------------------------
        String[] sen1 = sentence1.split(" ");
        String[] sen2 = sentence2.split(" ");
        ArrayDeque<String> sen1Deque = new ArrayDeque<>();
        ArrayDeque<String> sen2Deque = new ArrayDeque<>();
        for (int i = 0; i < sen1.length; i++) {
            sen1Deque.addLast(sen1[i]);
        }
        for (int i = 0; i < sen2.length; i++) {
            sen2Deque.addLast(sen2[i]);
        }
        //前缀
        while (!sen1Deque.isEmpty() && !sen2Deque.isEmpty() && sen1Deque.peekFirst().equals(sen2Deque.peekFirst())) {
            sen1Deque.pollFirst();
            sen2Deque.pollFirst();
        }
        //后缀
        while (!sen1Deque.isEmpty() && !sen2Deque.isEmpty() && sen1Deque.peekLast().equals(sen2Deque.peekLast())) {
            sen1Deque.pollLast();
            sen2Deque.pollLast();
        }
        return sen1Deque.isEmpty() || sen2Deque.isEmpty();
    }

    public boolean areSentencesSimilar01(String sentence1, String sentence2) {  //双指针
        String[] sen1 = sentence1.split(" ");
        String[] sen2 = sentence2.split(" ");
        int n1 = sen1.length;
        int n2 = sen2.length;
        int i = 0;
        int j = 0;
        //1、从左侧开始匹配收缩
        while (i < n1 && i < n2 && sen1[i].equals(sen2[i])) {
            i++;
        }
        //2、从右侧开始匹配收缩
        while (j < n1 - i && j < n2 - i && sen1[n1 - 1 - j].equals(sen2[n2 - 1 - j])) {
            j++;
        }
        return i + j == Math.min(n1, n2);
    }

    public boolean areSentencesSimilar02(String sentence1, String sentence2) {  //错误写法，关键在没有从两头向内收缩，而是单向搜索
        int index1 = 0;
        int index2 = 0;
        String[] sen1 = sentence1.split(" ");
        String[] sen2 = sentence2.split(" ");
        //1、前缀相等，齐头并进
        while (index1 < sen1.length && index2 < sen2.length && sen1[index1].equals(sen2[index2])) {
            index1++;
            index2++;
        }
        if (index1 == sen1.length || index2 == sen2.length) return true;

        //--------------------------------------------------------------------------
        // 错误关键，不能尝试从左向右跳过不同的元素，会存在特殊案例，案例："A" "a A b A"，而应该严格按照从两头收缩的方式
        //--------------------------------------------------------------------------

        //2、插入句子，重新对齐
        while (index1 < sen1.length && index2 < sen2.length && !sen1[index1].equals(sen2[index2])) {
            if (sen1.length < sen2.length) index2++;
            else index1++;
        }
        //3、后缀相等，齐头并进
        while (index1 < sen1.length && index2 < sen2.length && sen1[index1].equals(sen2[index2])) {
            index1++;
            index2++;
        }
        return index1 == sen1.length && index2 == sen2.length;
    }

    /**
     * 452. 用最少数量的箭引爆气球
     */
    public int findMinArrowShots(int[][] points) {  //贪心，按照区间右边界升序排序
        int ans = 0;
        int n = points.length;
        Arrays.sort(points, (o1, o2) -> {
            //本题特殊，有极端值 [[-2147483646,-2147483645],[2147483646,2147483647]] 直接向减溢出
            if (o1[1] < o2[1]) return -1;
            else if (o1[1] > o2[1]) return 1;
            return 0;
        });
        //------------------------------------------------
        // 由于按照区间右边界升序排序，因此后面区间的右边界一定大于当前区间的右边界
        // 所以可以将发射点设置在某个区间 A 的右边界，则后续区间 B 是否与此区间 A 有重叠取决于后续区间 B 的左边界是否小于等于此区间 A 的右边界
        //     如果有重叠，则在区间 A 右边界设置的发射点就可以将区间 B 射穿，无需消耗新的箭
        //     如果无重叠，则需要重新分配一个箭，用于射穿当前区间 A'，同时此箭是否能否一并将后续区间射穿，取决于...循环
        //------------------------------------------------
        int currEnd = points[0][1];
        ans++;
        for (int i = 1; i < points.length; i++) {
            if (currEnd < points[i][0]) {    //无交集，需要一支新箭
                ans++;
                currEnd = points[i][1];      //更新右边界，即此箭头发射的位置
            }
        }
        return ans;
    }

    public int findMinArrowShots01(int[][] points) {  //贪心，按照左边界升序排序
        int ans = 0;  //无重叠区间的个数
        int n = points.length;
        Arrays.sort(points, (o1, o2) -> {  //按照左边界升序排序
            if (o1[0] < o2[0]) return -1;
            else if (o1[0] > o2[0]) return 1;
            return 0;
        });
        ans++;
        int[] overArea = {points[0][0], points[0][1]};
        //------------------------------------------------------------------------------
        // 合并区间：合并重叠的区间，求交集，使用这种方法也可求并集，具体详细见 56. 合并区间
        //------------------------------------------------------------------------------
        for (int i = 1; i < n; i++) {
            int[] currArea = points[i];
            if (currArea[0] <= overArea[1]) {  //两个区间有重叠
                overArea[0] = Math.max(overArea[0], currArea[0]);
                overArea[1] = Math.min(overArea[1], currArea[1]);
            } else {   //无重叠，需要新增一支箭
                ans++;
                overArea[0] = currArea[0];
                overArea[1] = currArea[1];
            }
        }
        return ans;
    }


    //-------------------------------------------------------------------
    // 上下两题极其类似，上面求"公共交集"的个数，下面求"最大不相交集合"的个数
    //-------------------------------------------------------------------


    /**
     * 435. 无重叠区间
     */
    public int eraseOverlapIntervals(int[][] intervals) {   //当成预定会议的题目，按照右边界升序排序
        //-------------------------------------------------------
        // 按照"会议结束时间"升序排序，从而能够尽可能容纳更多的会议
        //-------------------------------------------------------
        Arrays.sort(intervals, (o1, o2) -> o1[1] - o2[1]);
        int n = intervals.length;
        //首个可被安排的会议的结束时间
        int currentEndTime = intervals[0][1];  //会议室被占用的结束时间
        //当前可被安排的会议数
        int ans = 1;
        for (int i = 1; i < n; i++) {
            //--------------------------------------------
            // 逐一判断接下来的会议是否与当前已经被选择的会议冲突
            //--------------------------------------------
            if (currentEndTime <= intervals[i][0]) {  //无交集，会议时间不冲突
                ans++;                                     //当前会议可被安排
                currentEndTime = intervals[i][1];          //更新会议室被占用的结束时间
            }
        }
        return n - ans;  //总会议数减去"最多"被安排的会议数，即为需要移除的最小会议数量
    }

    //-------------------------------------------------------------------------
    // 上下两种写法，一种按照会议开始时间排序，一种按照会议结束时间排序
    //    1、按照会议结束时间排序，则最初筛选出来的基准会议，就直接是最终的会议
    //    2、按照会议开始时间排序，则最初筛选出来的基准会议，不一定是最终的会议，需要基于此找出与其时间冲突的会议，在这些会议中挑选那个结束时间最早的会议
    //-------------------------------------------------------------------------

    public int eraseOverlapIntervals00(int[][] intervals) {   //当成预定会议的题目，按照左边界升序排序
        //------------------------------------------------------
        // 按照"会议开始时间"升序排序，我们一定能够找到一个最先开始的会议"，但是最先开始的会议，不一定最先结束
        // 因此，要在存在时间冲突的会议中，选择会议最早结束的会议（贪心），从而后续可容纳更多的会议
        //------------------------------------------------------
        Arrays.sort(intervals, (o1, o2) -> o1[0] - o2[0]);
        //会议时间存在冲突的最小会议数
        int ans = 0;
        int currentEndTime = intervals[0][1];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < currentEndTime) {  //时间冲突
                //会议冲突，需要删除的会议增加
                ans++;
                //保留会议结束时间早的会议，移除会议结束时间晚的会议
                currentEndTime = Math.min(currentEndTime, intervals[i][1]);
            } else {                                 //时间不冲突
                currentEndTime = intervals[i][1];
            }
        }
        return ans;
    }

    //尝试通过对左边界排序，写一下可以被安排的会议，最终的返回值应该是 return n - ans;

    public int eraseOverlapIntervals01(int[][] intervals) { //类似于 300. 最长递增子序列，但本题超时
        Arrays.sort(intervals, (o1, o2) -> o1[0] - o2[0]);  //按照起点位置升序排序
        int n = intervals.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (intervals[j][1] <= intervals[i][0]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }
        return n - Arrays.stream(dp).max().getAsInt();  //移除的最小个数
    }


    /**
     * 56. 合并区间
     */
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];  //优先按照起点升序排序
            return o2[1] - o1[1];                      //其次按照终点降序排序
        });
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        for (int[] interval : intervals) {
            if (queue.isEmpty() || queue.peekLast()[1] < interval[0]) {
                queue.addLast(interval);
            } else if (queue.peekLast()[1] < interval[1]) {
                queue.addLast(new int[]{queue.pollLast()[0], interval[1]});
            }
        }
        ArrayList<int[]> ans = new ArrayList<>();
        while (!queue.isEmpty()) {
            ans.add(queue.pollFirst());
        }
        return ans.toArray(new int[ans.size()][]);
    }

    public int[][] merge01(int[][] intervals) {
        ArrayList<int[]> ans = new ArrayList<>();
        Arrays.sort(intervals, (o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];  //优先按照起点升序排序
            return o2[1] - o1[1];                      //其次按照终点降序排序
        });
        int currEndTime = intervals[0][1];
        int[] edge = {intervals[0][0], intervals[0][1]};
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= currEndTime) {  //重叠
                edge[0] = Math.min(edge[0], intervals[i][0]);  //多余
                edge[1] = Math.max(edge[1], intervals[i][1]);
            } else {
                ans.add(edge.clone());
                edge[0] = intervals[i][0];
                edge[1] = intervals[i][1];
            }
            currEndTime = Math.max(currEndTime, intervals[i][1]);  //一定取最大值
        }
        ans.add(edge);
        return ans.toArray(new int[ans.size()][]);
    }


    public int[][] merge02(int[][] intervals) {    //错误的写法，示例：[[2,3],[4,5],[6,7],[8,9],[1,10]]
        ArrayList<int[]> ans = new ArrayList<>();
        Arrays.sort(intervals, (o1, o2) -> {
            if (o1[1] != o2[1]) return o1[1] - o2[1];   //首先按照区间终点升序排序
            else return o1[0] - o2[0];
        });
        int currEndTime = intervals[0][1];
        int[] edge = {intervals[0][0], intervals[0][1]};
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= currEndTime) {  //重叠
                edge[0] = Math.min(edge[0], intervals[i][0]);
                edge[1] = Math.max(edge[1], intervals[i][1]);
            } else {
                ans.add(edge.clone());
                edge[0] = intervals[i][0];
                edge[1] = intervals[i][1];
            }
            currEndTime = intervals[i][1];
        }
        ans.add(edge);
        return ans.toArray(new int[ans.size()][]);
    }


    /**
     * 1798. 你能构造出连续值的最大数目
     */
    public int getMaximumConsecutive(int[] coins) {  //贪心、排序
        int sum = 0;
        Arrays.sort(coins);
        int n = coins.length;
        for (int coin : coins) {
            if (coin > sum + 1) {  //意味着 0 + coin > sum + 1 无法与当前的 sum 连续，因为 coin 是未选择硬币中的最小值
                break;
            }
            sum += coin;
        }
        return sum + 1;
    }


    /**
     * 1109. 航班预订统计
     */
    public int[] corpFlightBookings(int[][] books, int n) {
        int[] ans = new int[n];
        for (int[] book : books) {
            for (int i = book[0] - 1; i < book[1]; i++) {
                ans[i] += book[2];
            }
        }
        return ans;
    }

    public int[] corpFlightBookings01(int[][] books, int n) {  //动态上下公交车
        int[] ans = new int[n];
        for (int[] book : books) {
            ans[book[0] - 1] += book[2];  //上车
            if (book[1] < n) {
                ans[book[1]] -= book[2];  //下车
            }
        }
        for (int i = 1; i < n; i++) {
            ans[i] += ans[i - 1];
        }
        return ans;
    }


    /**
     * 1234. 替换子串得到平衡字符串
     */
    public int balancedString(String str) {
        int n = str.length();
        int limits = n / 4;
        int[] buckets = new int[26];
        for (int i = 0; i < n; i++) {
            buckets[str.charAt(i) - 'A']++;
        }
        if (checkBalance(buckets, limits)) return 0;
        int ans = n;
        int left = 0;
        int right = 0;
        while (right < n) {
            buckets[str.charAt(right) - 'A']--;
            while (checkBalance(buckets, limits) && left <= right) {
                ans = Math.min(ans, right - left + 1);
                buckets[str.charAt(left) - 'A']++;
                left++;
            }
            right++;
        }
        return ans;
    }

    private boolean checkBalance(int[] buckets, int limits) {
        if (buckets['Q' - 'A'] > limits || buckets['W' - 'A'] > limits || buckets['E' - 'A'] > limits || buckets['R' - 'A'] > limits) {
            return false;
        }
        return true;
    }


    /**
     * 1138. 字母板上的路径
     */
    public String alphabetBoardPath(String target) {
        StringBuilder ans = new StringBuilder();
        int currRow = 0;
        int currCol = 0;
        int n = target.length();
        for (int i = 0; i < n; i++) {
            int ch = target.charAt(i) - 'a';
            int nextRow = ch / 5;
            int nextCol = ch % 5;
            //-------------------------------------------------------------------------
            // 由于 "Z" 再最左侧只能从上方向下移动达到，同时从 "Z" 向上出发移动指其余节点
            // 因此优先考虑左移（到达"Z"）和上移（移出"Z"）
            //-------------------------------------------------------------------------
            while (currCol > nextCol) {   //左移
                ans.append("L");
                currCol--;
            }
            while (currRow > nextRow) {   //上移
                ans.append("U");
                currRow--;
            }
            while (currCol < nextCol) {   //右移
                ans.append("R");
                currCol++;
            }
            while (currRow < nextRow) {   //下移
                ans.append("D");
                currRow++;
            }
            ans.append("!");
        }
        return ans.toString();
    }


    /**
     * 1437. 是否所有 1 都至少相隔 k 个元素
     */
    public boolean kLengthApart(int[] nums, int k) {
        int n = nums.length;
        int prev = -1;
        for (int i = 0; i < n; i++) {
            if (nums[i] == 1) {
                if (prev != -1 && i - prev - 1 < k) {
                    return false;
                }
                prev = i;
            }
        }
        return true;
    }


    public boolean kLengthApart01(int[] nums, int k) {  //错误写法
        int window = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                window++;
            } else {
                if (i > 0 && window < k) {
                    return false;
                }
                window = 0;
            }
        }
        return true;
    }


    /**
     * 202. 快乐数
     */
    public boolean isHappy(int n) {    //快慢指针
        //基于哈希表判断是否进入循环
        HashSet<Integer> set = new HashSet<>();
        while (n > 1 && !set.contains(n)) {
            set.add(n);
            n = getNext(n);
        }
        return n == 1;
    }

    private int getNext(int n) {
        int sum = 0;
        while (n > 0) {
            int aa = n % 10;
            sum += (aa * aa);
            n /= 10;
        }
        return sum;
    }


    /**
     * 1417. 重新格式化字符串
     */
    public String reformat(String str) {   //模拟
        int nums1 = 0;
        int nums2 = 0;
        int[] buckets1 = new int[26];
        int[] buckets2 = new int[10];
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isDigit(ch)) {
                nums2++;
                buckets2[ch - '0']++;
            } else if (Character.isLowerCase(ch)) {
                nums1++;
                buckets1[ch - 'a']++;
            }
        }
        if (Math.abs(nums1 - nums2) > 1) return "";
        if (nums1 >= nums2) return reformatHelper(buckets1, buckets2);
        return reformatHelper(buckets2, buckets1);
    }

    private String reformatHelper(int[] buckets1, int[] buckets2) {
        int n1 = buckets1.length;
        int n2 = buckets2.length;
        StringBuilder ans = new StringBuilder();
        int index1 = 0;
        int index2 = 0;
        while (index1 < n1 || index2 < n2) {
            //1、优先处理字符含量多的类型
            while (index1 < n1 && buckets1[index1] == 0) {
                index1++;
            }
            if (index1 < n1) {
                buckets1[index1]--;
                if (n1 == 10) ans.append(index1);
                else ans.append((char) (index1 + 'a'));
            }

            //2、其次处理字符含量少的类型
            while (index2 < n2 && buckets2[index2] == 0) {
                index2++;
            }
            if (index2 < n2) {
                buckets2[index2]--;
                if (n2 == 10) ans.append(index2);
                else ans.append((char) (index2 + 'a'));
            }
        }
        return ans.toString();
    }


    public String reformat01(String str) {   //模拟
        StringBuilder ans = new StringBuilder();
        char[] arr = str.toCharArray();
        int n = arr.length;
        Arrays.sort(arr);
        for (int i = 0, j = n - 1; i < j; i++, j--) {
            //前半部分出现字母，后半部分出现数字
            if (Character.isLowerCase(arr[i]) || Character.isDigit(arr[j])) {
                return "";
            }
            ans.append(arr[i]);   //数字
            ans.append(arr[j]);   //字母
        }
        //1、偶数位
        if (n % 2 == 0) return ans.toString();
        //2、奇数位
        char ch = arr[n / 2];
        if (Character.isLowerCase(ch)) ans.insert(0, ch);  //若是字符，插入最前面
        else ans.append(ch);  //若是数字，插入最后
        return ans.toString();
    }

    /**
     * 面试题 05.02. 二进制数转字符串
     */
    public String printBin(double num) {
        StringBuilder ans = new StringBuilder("0.");
        while (ans.length() <= 32 && num != 0) {
            num *= 2;
            int digit = (int) num;
            ans.append(digit);
            num -= digit;
        }
        return num == 0 ? ans.toString() : "ERROR";
    }


    /**
     * 2109. 向字符串添加空格
     */
    public String addSpaces(String str, int[] spaces) {
        StringBuilder ans = new StringBuilder(str);
        for (int i = 0; i < spaces.length; i++) {
            ans.insert(i + spaces[i], " ");   //注意：ans的长度是在动态变化的，所以要加 i
        }
        return ans.toString();
    }

    public String addSpaces01(String str, int[] spaces) {
        StringBuilder ans = new StringBuilder();
        int left = 0;
        for (int right : spaces) {
            ans.append(str.substring(left, right));
            ans.append(" ");
            left = right;
        }
        ans.append(str.substring(left));
        return ans.toString();
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
     * 1487. 保证文件名唯一
     */
    public String[] getFolderNames(String[] names) {
        int n = names.length;
        String[] ans = new String[n];
        HashMap<String, Integer> hTable = new HashMap<>();   //以 key 为前缀的文件夹名，当前可分配的后缀编号为 value
        for (int i = 0; i < n; i++) {
            if (hTable.containsKey(names[i])) {
                Integer k = hTable.get(names[i]);
                //过滤掉已经存在的文件夹名称
                while (hTable.containsKey(addSuffix(names[i], k))) {   //关键
                    k++;
                }
                String nextName = addSuffix(names[i], k);
                ans[i] = nextName;
                hTable.put(nextName, 1);   //维护新增的文件名，及以其作为前缀的下一个可用的后缀编号
                hTable.put(names[i], k + 1);   //维护当前的文件夹名，及以其作为前缀的下一个可用的后缀编号
            } else {
                ans[i] = names[i];
                hTable.put(names[i], 1);
            }
        }
        return ans;
    }

    private String addSuffix(String name, int k) {
        return name + "(" + k + ")";
    }


    public String[] getFolderNames01(String[] names) {  //错误写法
        int n = names.length;
        String[] ans = new String[n];
        HashMap<String, Integer> hTable = new HashMap<>();   //以 key 为前缀的文件夹名，当前可分配的后缀编号为 value
        for (int i = 0; i < n; i++) {
            if (hTable.containsKey(names[i])) {
                String nextName = names[i] + "(" + hTable.get(names[i]) + ")";
                ans[i] = nextName;
                hTable.put(nextName, 1);   //维护新增的文件名，及以其作为前缀的下一个可用的后缀编号
                hTable.put(names[i], hTable.get(names[i]) + 1);   //维护当前的文件夹名，及以其作为前缀的下一个可用的后缀编号
            } else {
                ans[i] = names[i];
                hTable.put(names[i], 1);
            }
        }
        return ans;
    }


    /**
     * 1535. 找出数组游戏的赢家
     */
    public int getWinner(int[] arr, int k) {
        int winner = arr[0];
        int freq = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < winner) {
                ++freq;
            } else {
                winner = arr[i];
                freq = 1;
            }
            if (freq == k) return winner;  //一定放外面，因为 if/else 均可能满足条件
        }
        return winner;  //数组遍历结束，仍没有返回结果，则说明当前记录的最大值，就是 winner，其将大于头部的数字
    }

    public int getWinner01(int[] arr, int k) {    //暴力，超时
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int num : arr) {
            queue.addLast(num);
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        while (true) {
            Integer xx = queue.pollFirst();
            Integer yy = queue.pollFirst();
            if (xx > yy) {
                //为胜利者添加胜利次数
                Integer freq = map.getOrDefault(xx, 0);
                freq++;
                if (freq == k) return xx;
                map.put(xx, freq);
                map.put(yy, 0);
                //有序放回队列
                queue.addFirst(xx);
                queue.addLast(yy);
            }
            if (yy > xx) {
                //为胜利者添加胜利次数
                Integer freq = map.getOrDefault(yy, 0);
                freq++;
                if (freq == k) return yy;
                map.put(yy, freq);
                map.put(xx, 0);
                //有序放回队列
                queue.addFirst(yy);
                queue.addLast(xx);
            }
        }
    }


    /**
     * 1914. 循环轮转矩阵
     */
    public int[][] rotateGrid(int[][] grid, int k) {
        int m = grid.length;
        int n = grid[0].length;
        int layers = Math.min(m / 2, n / 2);
        for (int layer = 0; layer < layers; layer++) {
            ArrayDeque<Integer> queue = new ArrayDeque<>();
            //1、从左向右，逐个添加元素
            for (int j = layer; j < n - layer; j++) {
                queue.addLast(grid[layer][j]);
            }
            //2、从上向下，逐个添加元素
            for (int i = layer + 1; i < m - layer; i++) {
                queue.addLast(grid[i][n - layer - 1]);
            }
            //3、从右向左，逐个添加元素
            for (int j = n - layer - 2; j >= layer; j--) {
                queue.addLast(grid[m - layer - 1][j]);
            }
            //4、从下向上，逐个添加元素
            for (int i = m - layer - 2; i > layer; i--) {
                queue.addLast(grid[i][layer]);
            }

            //---------------------------------------------
            // 逆时针移动 K 个位置
            //---------------------------------------------
            int kk = k % queue.size();   //否则超时
            for (int i = 0; i < kk && !queue.isEmpty(); i++) {
                queue.addLast(queue.pollFirst());
            }

            //1、从左向右，逐个添加元素
            for (int j = layer; j < n - layer; j++) {
                grid[layer][j] = queue.pollFirst();
            }
            //2、从上向下，逐个添加元素
            for (int i = layer + 1; i < m - layer; i++) {
                grid[i][n - layer - 1] = queue.pollFirst();
            }
            //3、从右向左，逐个添加元素
            for (int j = n - layer - 2; j >= layer; j--) {  //等于号
                grid[m - layer - 1][j] = queue.pollFirst();
            }
            //4、从下向上，逐个添加元素
            for (int i = m - layer - 2; i > layer; i--) {
                grid[i][layer] = queue.pollFirst();
            }
        }
        return grid;
    }


    /**
     * 892. 三维形体的表面积
     */
    public int surfaceArea(int[][] grid) {
        int sumArea = 0;
        int m = grid.length;
        int n = grid[0].length;
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] > 0) {
                    //1、四周无覆盖的面积
                    int currArea = grid[i][j] * 4 + 2;
                    //2、移除四周覆盖的面积
                    for (int[] dir : dirs) {
                        int nextRow = i + dir[0];
                        int nextCol = j + dir[1];
                        if (nextRow >= 0 && nextRow < m && nextCol >= 0 && nextCol < n) {
                            currArea -= Math.min(grid[i][j], grid[nextRow][nextCol]);
                        }
                    }
                    sumArea += currArea;
                }
            }
        }
        return sumArea;
    }

    public int surfaceArea03(int[][] grid) {   //错误写法
        int nums = 0;
        int sumArea = 0;
        int innerArea = 0;
        int m = grid.length;
        int n = grid[0].length;
        int[] buckets1 = new int[m];
        int[] buckets2 = new int[n];
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        //内部凹陷的面积
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                //记录投影面积（侧面）
                buckets1[i] = Math.max(buckets1[i], grid[i][j]);
                buckets2[j] = Math.max(buckets2[j], grid[i][j]);
                //记录投影面积（底面）
                if (grid[i][j] > 0) sumArea++;
                //记录内部凹陷导致未投影的面积（只能考虑到相邻的，远距离的考虑不到，错误写法点）
                if (i > 0 && i < m - 1 && j > 0 && j < n - 1) {
                    int minHeight = Integer.MAX_VALUE;
                    for (int[] dir : dirs) {
                        int nextRow = i + dir[0];
                        int nextCol = j + dir[1];
                        minHeight = Math.min(minHeight, grid[nextRow][nextCol]);
                    }
                    if (grid[i][j] < minHeight) {
                        nums += minHeight - grid[i][j];
                    }
                }

                //记录边界凹陷导致未投影的面积（只能考虑到相邻的，远距离考虑不到，同时四个顶点重复计算，错误写法点）
                if ((i == 0 || i == m - 1) && grid[i][j] == 0) {
                    if (j - 1 >= 0) innerArea += grid[i][j - 1];
                    if (j + 1 < n) innerArea += grid[i][j + 1];
                }
                if ((j == 0 || j == n - 1) && grid[i][j] == 0) {
                    if (i - 1 >= 0) innerArea += grid[i - 1][j];
                    if (i + 1 < m) innerArea += grid[i + 1][j];
                }
            }
        }
        for (int i = 0; i < m; i++) {
            sumArea += buckets1[i];
        }
        for (int j = 0; j < n; j++) {
            sumArea += buckets2[j];
        }
        return sumArea * 2 + nums * 4 + innerArea;
    }


    /**
     * 1380. 矩阵中的幸运数
     */
    public List<Integer> luckyNumbers(int[][] matrix) {
        List<Integer> ans = new ArrayList<>();
        int m = matrix.length;
        int n = matrix[0].length;
        int[] min = new int[m];
        Arrays.fill(min, Integer.MAX_VALUE);
        int[] max = new int[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                min[i] = Math.min(min[i], matrix[i][j]);
                max[j] = Math.max(max[j], matrix[i][j]);
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == min[i] && matrix[i][j] == max[j]) {
                    ans.add(matrix[i][j]);
                }
            }
        }
        return ans;
    }


    /**
     * 922. 按奇偶排序数组 II
     */
    public int[] sortArrayByParityII(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        ArrayList<Integer> one = new ArrayList<>();
        ArrayList<Integer> two = new ArrayList<>();
        for (int num : nums) {
            if ((num & 1) == 0) two.add(num);
            else one.add(num);
        }
        int index = 0;
        for (int i = 0; i < n; i += 2) {
            nums[i] = two.get(index);
            nums[i + 1] = one.get(index);
            index++;
        }
        return nums;
    }

    public int[] sortArrayByParityII01(int[] nums) {
        int n = nums.length;
        int[] ans = new int[n];
        int index = 0;
        for (int num : nums) {
            if (num % 2 == 0) {
                ans[index] = num;
                index += 2;
            }
        }
        index = 1;
        for (int num : nums) {
            if (num % 2 != 0) {
                ans[index] = num;
                index += 2;
            }
        }
        return ans;
    }


    /**
     * 2383. 赢得比赛需要的最少训练时长
     */
    public int minNumberOfHours(int initialEnergy, int initialExperience, int[] energy, int[] experience) {
        int ans = 0;
        int energyTotal = 0;
        for (int num : energy) {
            energyTotal += num;
        }
        ans += energyTotal < initialEnergy ? 0 : energyTotal - initialEnergy + 1;
        for (int exp : experience) {
            if (initialExperience <= exp) {
                ans += (exp - initialExperience + 1);
                initialExperience = exp + 1;
            }
            initialExperience += exp;
        }
        return ans;
    }


    /**
     * 面试题 17.05.  字母与数字
     */
    public String[] findLongestSubarray(String[] array) {   //前缀和 + 哈希表
        HashMap<Integer, Integer> map = new HashMap<>();          //记录前缀和和区间右端点索引
        map.put(0, -1);
        int maxLength = 0;
        int startIndex = -1;
        int prefix = 0;
        for (int i = 0; i < array.length; i++) {
            prefix += Character.isDigit(array[i].charAt(0)) ? 1 : -1;
            if (map.containsKey(prefix)) {
                Integer prevIndex = map.get(prefix);
                if (i - prevIndex > maxLength) {
                    maxLength = i - prevIndex;
                    startIndex = prevIndex + 1;
                }
                //不更新 map中 prefix 值对应的结束索引
            } else {
                map.put(prefix, i);
            }
        }
        if (maxLength == 0) return new String[]{};
        String[] ans = new String[maxLength];
        System.arraycopy(array, startIndex, ans, 0, maxLength);
        return ans;
    }


    /**
     * 2087. 网格图中机器人回家的最小代价
     */
    public int minCost(int[] startPos, int[] homePos, int[] rowCosts, int[] colCosts) {  //贪心模拟
        //-----------------------------------------
        // 本题没有用最短路算法的原因在于“行与行”、“列与列”间的代价一致，而不是每两个单元格间的代价不同
        //-----------------------------------------
        int ans = 0;
        int minX = Math.min(startPos[0], homePos[0]);
        int maxX = Math.max(startPos[0], homePos[0]);
        int minY = Math.min(startPos[1], homePos[1]);
        int maxY = Math.max(startPos[1], homePos[1]);
        for (int i = minX; i <= maxX; i++) {
            ans += rowCosts[i];
        }
        for (int j = minY; j <= maxY; j++) {
            ans += colCosts[j];
        }
        ans -= rowCosts[startPos[0]];
        ans -= colCosts[startPos[1]];
        return ans;
    }


    /**
     * 1021. 删除最外层的括号
     */
    public String removeOuterParentheses(String str) {  //将括号问题转换为分值判定的问题
        int sum = 0;
        int n = str.length();
        HashSet<Integer> remove = new HashSet<>();
        for (int i = 0; i < n; i++) {
            if (sum == 0) remove.add(i);  //左端点
            sum += str.charAt(i) == '(' ? 1 : -1;
            if (sum == 0) remove.add(i);  //右端点
        }
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (remove.contains(i)) continue;
            ans.append(str.charAt(i));
        }
        return ans.toString();
    }


    /**
     * 1616. 分割两个字符串得到回文串
     */
    public boolean checkPalindromeFormation(String xx, String yy) {
        return checkFromEdge(xx, yy) || checkFromEdge(yy, xx);
    }

    private boolean checkFromEdge(String xx, String yy) {
        int left = 0;
        int right = xx.length() - 1;
        while (left < right && xx.charAt(left) == yy.charAt(right)) {
            left++;
            right--;
        }
        //1、查看是否可以通过 "xx的前缀 + yy后缀" 来构成回文串
        if (left >= right) {  //考虑到长度的奇偶性
            return true;
        }
        //2、是否可通过 "xx的前缀 + xx/yy的中间部分 + yy后缀" 构成回文串
        return checkFromInside(xx, left, right) || checkFromInside(yy, left, right);
    }

    private boolean checkFromInside(String mm, int left, int right) {
        while (left < right && mm.charAt(left) == mm.charAt(right)) {
            left++;
            right--;
        }
        return left >= right;
    }


    /**
     * 647. 回文子串
     */
    public int countSubstrings(String str) {
        int ans = 0;
        int n = str.length();
        for (int i = 0; i < n; i++) {
            ans += getNums(str, i, i);              //奇数长度
            if (i == n - 1) break;
            ans += getNums(str, i, i + 1);    //偶数长度
        }
        return ans;
    }

    private int getNums(String str, int left, int right) {
        int ans = 0;
        while (left >= 0 && right < str.length() && str.charAt(left) == str.charAt(right)) {
            ans++;
            left--;
            right++;
        }
        return ans;
    }

    public String longestPalindrome66(String str) {
        String ans = "";
        int n = str.length();
        for (int i = 0; i < n; i++) {
            String palindrome = getPalindrome(str, i);
            if (palindrome.length() > ans.length()) {
                ans = palindrome;
            }
        }
        return ans;
    }

    private String getPalindrome(String str, int mid) {
        int n = str.length();
        int left = mid;
        int right = mid;
        //1、左侧平滑过渡
        while (left >= 0 && str.charAt(left) == str.charAt(mid)) {
            left--;
        }
        //2、右侧平滑过渡
        while (right < n && str.charAt(mid) == str.charAt(right)) {
            right++;
        }
        //3、回文检测
        while (left >= 0 && right < n && str.charAt(left) == str.charAt(right)) {
            left--;
            right++;
        }
        return str.substring(left + 1, right);
    }


    /**
     * 剑指 Offer 57 - II. 和为s的连续正数序列
     */
    public int[][] findContinuousSequence(int target) {
        int sum = 0;
        int left = 1;
        int right = 1;
        ArrayList<int[]> list = new ArrayList<>();
        while (right < target) {
            sum += right;
            while (sum > target) {
                sum -= left;
                left++;
            }
            if (sum == target) {
                int[] nums = new int[right - left + 1];
                for (int i = 0; i < nums.length; i++) {
                    nums[i] = left + i;
                }
                list.add(nums);
            }
            right++;
        }
        int[][] ans = new int[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            ans[i] = list.get(i);
        }
        return ans;
    }


    /**
     * 2389. 和有限的最长子序列
     */
    public int[] answerQueries(int[] nums, int[] queries) {
        int n = nums.length;
        int m = queries.length;
        Arrays.sort(nums);
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        int[] ans = new int[m];
        for (int i = 0; i < m; i++) {
            ans[i] = answerHelp(prefix, queries[i]);
        }
        return ans;
    }

    private int answerHelp(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return right;
    }


    /**
     * 1625. 执行操作后字典序最小的字符串
     */
    public String findLexSmallestString(String str, int a, int b) {   //BFS
        TreeSet<String> ans = new TreeSet<>();          //不超时
        ArrayDeque<String> queue = new ArrayDeque<>();
        ans.add(str);
        queue.addLast(str);
        while (!queue.isEmpty()) {
            String curr = queue.poll();
            //1、第一种变形
            char[] arr = curr.toCharArray();
            for (int i = 1; i < arr.length; i += 2) {
                arr[i] = (char) ((arr[i] - '0' + a) % 10 + '0');
            }
            String next1 = new String(arr);
            if (!ans.contains(next1)) {
                ans.add(next1);
                queue.addLast(next1);
            }
            //2、第二种变形
            String xx = curr.substring(0, b);
            String yy = curr.substring(b);
            String next2 = yy + xx;
            if (!ans.contains(next2)) {
                ans.add(next2);
                queue.addLast(next2);
            }
        }
        return ans.first();
    }

    public String findLexSmallestString01(String str, int a, int b) {   //BFS
        ArrayList<String> ans = new ArrayList<>();
        ArrayDeque<String> queue = new ArrayDeque<>();
        ans.add(str);
        queue.addLast(str);
        while (!queue.isEmpty()) {
            String curr = queue.poll();
            //1、第一种变形
            char[] arr = curr.toCharArray();
            for (int i = 1; i < arr.length; i += 2) {
                arr[i] = (char) ((arr[i] - '0' + a) % 10 + '0');
            }
            String next1 = new String(arr);
            if (!ans.contains(next1)) {
                ans.add(next1);
                queue.addLast(next1);
            }
            //2、第二种变形
            String xx = curr.substring(0, b);
            String yy = curr.substring(b);
            String next2 = yy + xx;
            if (!ans.contains(next2)) {
                ans.add(next2);
                queue.addLast(next2);
            }
        }
        ans.sort(String::compareTo);   //一次排序，也超时
        return ans.get(0);
    }


    public String findLexSmallestString02(String str, int a, int b) {   //BFS
        PriorityQueue<String> ans = new PriorityQueue<>(String::compareTo);   //每次新增元素，均排序，超时
        ArrayDeque<String> queue = new ArrayDeque<>();
        ans.add(str);
        queue.addLast(str);
        while (!queue.isEmpty()) {
            String curr = queue.poll();
            //1、第一种变形
            char[] arr = curr.toCharArray();
            for (int i = 1; i < arr.length; i += 2) {
                arr[i] = (char) ((arr[i] - '0' + a) % 10 + '0');
            }
            String next1 = new String(arr);
            if (!ans.contains(next1)) {
                ans.add(next1);
                queue.addLast(next1);
            }
            //2、第二种变形
            String xx = curr.substring(0, b);
            String yy = curr.substring(b);
            String next2 = yy + xx;
            if (!ans.contains(next2)) {
                ans.add(next2);
                queue.addLast(next2);
            }
        }
        return ans.poll();
    }


    /**
     * 2367. 算术三元组的数目
     */
    public int arithmeticTriplets(int[] nums, int diff) {
        int ans = 0;
        HashSet<Integer> hTables = new HashSet<>();
        for (int num : nums) {
            hTables.add(num);
        }
        for (int num : nums) {
            if (hTables.contains(num - diff) && hTables.contains(num + diff))
                ans++;
        }
        return ans;
    }


    /**
     * 1053. 交换一次的先前排列
     */
    public int[] prevPermOpt1(int[] arr) {   //基于单调队列
        //---------------------------------------------------------------
        // 贪心：从后往前找到小于 arr[i] 的最大的数 arr[j]，如果存在多个数满足条件，选择最靠左的进行替换
        //---------------------------------------------------------------
        int n = arr.length;
        ArrayDeque<Integer> queue = new ArrayDeque<>();  //队列元素单调递减（沿着遍历方向看，其实也是队尾向队首）
        for (int i = n - 1; i >= 0; i--) {
            //1、元素相同，剔除此元素较大的下标
            while (!queue.isEmpty() && arr[queue.peekLast()] == arr[i]) {
                queue.pollLast();
            }
            //2、维护队列的单调性
            while (!queue.isEmpty() && arr[i] > arr[queue.peekLast()]) {
                Integer index = queue.pollLast();
                if (queue.isEmpty() || arr[i] <= arr[queue.peekLast()]) {  //关键：从小于 arr[i] 的最大值
                    int temp = arr[i];
                    arr[i] = arr[index];
                    arr[index] = temp;
                    return arr;
                }
            }
            queue.addLast(i);
        }
        return arr;
    }

    public int[] prevPermOpt100(int[] arr) {   //基于贪心思想（自己写的）
        int n = arr.length;
        int min = arr[n - 1];
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.addLast(n - 1);
        for (int i = n - 2; i >= 0; i--) {  //[3,1,1,3]
            if (arr[i] > min) {
                Integer index = queue.peekFirst();
                //从后向前，剔除大于当前值的元素，同时，针对小于当前的值元素，如果存在重复，取最左侧的元素
                while ((!queue.isEmpty() && arr[index] >= arr[i]) || arr[index] == arr[index - 1]) {
                    index = queue.pollFirst();
                }
                int temp = arr[index];
                arr[index] = arr[i];
                arr[i] = temp;
                break;
            }
            queue.addLast(i);
            min = Math.min(min, arr[i]);
        }
        return arr;
    }

    public int[] prevPermOpt101(int[] arr) {   //基于贪心思想
        //---------------------------------------------------------------
        // 贪心：从后往前找到小于 arr[i] 的最大的数 arr[j]，如果存在多个数满足条件，选择最靠左的进行替换
        //---------------------------------------------------------------
        int n = arr.length;
        for (int i = n - 2; i >= 0; i--) {
            if (arr[i] > arr[i + 1]) {  //从后往前看，非递增的数组，找到首个拐点
                int index = n - 1;
                while (arr[i] <= arr[index] || arr[index] == arr[index - 1]) {
                    index--;
                }
                int temp = arr[i];
                arr[i] = arr[index];
                arr[index] = temp;
                return arr;
            }
        }
        return arr;
    }


    public int[] prevPermOpt102(int[] arr) {    //错误写法，没办法过滤掉相同值的情况，案例 int[] nums1 = {3, 1, 1, 3};
        int n = arr.length;
        int min = arr[n - 1];
        int[] max = new int[n];  //记录当前位置后的最大值
        max[n - 1] = arr[n - 1];
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(arr[n - 1], n - 1);
        for (int i = n - 2; i >= 0; i--) {
            max[i] = Math.max(max[i + 1], arr[i + 1]);
            map.put(arr[i], i);
        }
        for (int i = n - 2; i >= 0; i--) {
            if (arr[i] > max[i]) {
                Integer index = map.get(max[i]);
                arr[index] = arr[i];
                arr[i] = max[i];
                return arr;
            }
        }
        return arr;
    }


    /**
     * 2017. 网格游戏
     */
    public long gridGame(int[][] grid) {
        int n = grid[0].length;
        long min = Long.MAX_VALUE;
        long[] prefix1 = new long[n + 1];
        long[] prefix2 = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix1[i + 1] = prefix1[i] + grid[0][i];
            prefix2[i + 1] = prefix2[i] + grid[1][i];
        }
        for (int i = 1; i <= n; i++) {
            min = Math.min(min, Math.max(prefix2[i - 1], prefix1[n] - prefix1[i]));
        }
        return min;
    }

    public long gridGame01(int[][] grid) {
        int n = grid[0].length;
        long max = 0;
        long[] prefix1 = new long[n + 1];
        long[] prefix2 = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix1[i + 1] = prefix1[i] + grid[0][i];
            prefix2[i + 1] = prefix2[i] + grid[1][i];
        }
        for (int i = 1; i <= n; i++) {
            max = Math.max(max, Math.max(prefix2[i - 1], prefix1[n] - prefix1[i]));   //仔细思考错误的原因
        }
        return max;
    }

    /**
     * 1041. 困于环中的机器人
     */
    public boolean isRobotBounded(String instructions) {
        int[][] dirs = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        int row = 0;
        int col = 0;
        int dir = 0;
        for (int i = 0; i < instructions.length(); i++) {
            char ch = instructions.charAt(i);
            if (ch == 'G') {
                row += dirs[dir][0];
                col += dirs[dir][1];
            } else if (ch == 'L') {
                dir += 3;
                dir %= 4;
            } else {
                dir += 1;
                dir %= 4;
            }
        }
        return (row == 0 && col == 0) || dir != 0;
    }


    /**
     * 1042. 不邻接植花
     */
    public int[] gardenNoAdj(int n, int[][] paths) {
        HashMap<Integer, ArrayList<Integer>> adjacent = new HashMap<>();
        for (int[] edge : paths) {
            int node1 = edge[0] - 1;
            int node2 = edge[1] - 1;
            ArrayList<Integer> list1 = adjacent.getOrDefault(node1, new ArrayList<>());
            ArrayList<Integer> list2 = adjacent.getOrDefault(node2, new ArrayList<>());
            list1.add(node2);
            adjacent.put(node1, list1);
            list2.add(node1);
            adjacent.put(node2, list2);
        }
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            int[] visited = new int[5];
            //获取当前点的相邻点当前被占用的颜色
            for (int next : adjacent.getOrDefault(i, new ArrayList<>())) {
                visited[ans[next]] = 1;   //关键，没有被上色的，均将其颜色置为 0，即导致 visited[0] = 1
            }
            for (int m = 1; m <= 4; m++) {
                if (visited[m] == 0) {
                    ans[i] = m;
                    break;
                }
            }
        }
        return ans;
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
     * 630. 课程表 III
     */
    public int scheduleCourse(int[][] courses) {
        int n = courses.length;
        Arrays.sort(courses, (o1, o2) -> o1[1] - o2[1]);  //按照结束时间升序排序
        PriorityQueue<int[]> sortQueue = new PriorityQueue<>((o1, o2) -> o2[0] - o1[0]);  //按照持续时长降序排序
        int currDay = 0;
        for (int i = 0; i < n; i++) {
            if (currDay + courses[i][0] <= courses[i][1]) {
                sortQueue.add(courses[i]);
                currDay += courses[i][0];
            } else if (!sortQueue.isEmpty() && sortQueue.peek()[0] > courses[i][0]) {
                int[] remove = sortQueue.poll();
                currDay -= remove[0];
                if (currDay + courses[i][0] <= courses[i][1]) {
                    sortQueue.add(courses[i]);
                    currDay += courses[i][0];
                }
            }
        }
        return sortQueue.size();
    }

    public int scheduleCourse01(int[][] courses) {
        int currDay = 0;
        int n = courses.length;
        Arrays.sort(courses, (o1, o2) -> o1[1] - o2[1]);
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o2[0] - o1[0]);
        for (int[] course : courses) {
            currDay += course[0];
            sortedQueue.add(course);
            if (currDay > course[1] && !sortedQueue.isEmpty()) {
                int[] remove = sortedQueue.poll();
                currDay -= remove[0];
            }
        }
        return sortedQueue.size();
    }


    /**
     * 6416. 找出不同元素数目差数组
     */
    public int[] distinctDifferenceArray(int[] nums) {
        int n = nums.length;
        int[] diff = new int[n];
        HashSet<Integer> prefix = new HashSet<>();
        HashMap<Integer, Integer> suffix = new HashMap<>();
        for (int num : nums) {
            suffix.put(num, suffix.getOrDefault(num, 0) + 1);
        }
        for (int i = 0; i < n; i++) {
            prefix.add(nums[i]);
            Integer freq = suffix.get(nums[i]);
            freq--;
            if (freq == 0) {
                suffix.remove(nums[i]);
            } else {
                suffix.put(nums[i], freq);
            }
            diff[i] = prefix.size() - suffix.size();
        }
        return diff;
    }


    public int[] distinctDifferenceArray01(int[] nums) {  //错误写法
        int n = nums.length;
        int[] diff = new int[n];
        int[] prefix = new int[51];
        int[] suffix = new int[51];
        for (int i = 0; i < n; i++) {
            suffix[nums[i]]++;
        }
        for (int i = 0; i < n; i++) {
            suffix[nums[i]]--;
            int xx = i + 1 - prefix[nums[i]];
            int yy = n - i - 1 - suffix[nums[i]];
            diff[i] = xx - yy;
            prefix[nums[i]]++;
        }
        return diff;
    }


    /**
     * 6418. 有相同颜色的相邻元素数目
     */
    public int[] colorTheArray(int n, int[][] queries) {  //通过
        int m = queries.length;
        int[] ans = new int[m];
        int[] nums = new int[n];
        int res = 0;
        for (int i = 0; i < m; i++) {
            int index = queries[i][0];
            int prev = nums[index];
            int curr = queries[i][1];
            nums[index] = curr;
            if (prev == 0) {
                if (index > 0 && nums[index - 1] == nums[index]) {
                    res++;
                }
                if (index < n - 1 && nums[index + 1] == nums[index]) {
                    res++;
                }
            } else {
                if (prev != curr) {
                    if (index > 0 && nums[index - 1] == prev) {
                        res--;
                    }
                    if (index < n - 1 && nums[index + 1] == prev) {
                        res--;
                    }

                    if (index > 0 && nums[index - 1] == curr) {
                        res++;
                    }
                    if (index < n - 1 && nums[index + 1] == curr) {
                        res++;
                    }
                }
            }
            ans[i] = res;
        }
        return ans;
    }


    public int[] colorTheArray01(int n, int[][] queries) {   //超时
        int m = queries.length;
        int[] ans = new int[m];
        int[] nums = new int[n + 1];
        nums[n] = 0;
        for (int i = 0; i < m; i++) {
            nums[queries[i][0]] = queries[i][1];
            ans[i] = colorHelper(nums);
        }
        return ans;
    }

    private int colorHelper(int[] nums) {
        int ans = 0;
        int freq = nums[0] == 0 ? 0 : 1;
        int n = nums.length;
        for (int i = 1; i < n; i++) {
            if (nums[i] == 0) {
                if (freq > 0) ans += freq - 1;
                freq = 0;
            } else {
                if (nums[i] == nums[i - 1]) freq++;
                else {
                    if (freq > 0) ans += freq - 1;
                    freq = 1;  //自身
                }
            }
        }
        return ans;
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
     * 949. 给定数字能组成的最大时间
     */
    public String largestTimeFromDigits(int[] arr) {
        int maxTime = -1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == j) continue;
                for (int m = 0; m < 4; m++) {
                    if (m == i || m == j) continue;
                    int n = 6 - i - j - m;
                    int hour = 10 * arr[i] + arr[j];
                    int minute = 10 * arr[m] + arr[n];
                    if (hour < 24 && minute < 60) {
                        maxTime = Math.max(maxTime, 60 * hour + minute);
                    }
                }
            }
        }
        return maxTime == -1 ? "" : String.format("%02d:%02d", maxTime / 60, maxTime % 60);
    }


    /**
     * 1093. 大样本统计
     */
    public double[] sampleStats(int[] count) {
        int min = -1;
        int max = -1;
        long sum = 0;
        int num = 0;
        int maxFreqNum = 0;
        int freq = 0;
        for (int i = 0; i <= 255; i++) {
            if (count[i] == 0) continue;
            max = i;
            if (min == -1) min = i;
            num += count[i];
            sum += (long) count[i] * i;
            if (count[i] > freq) {
                freq = count[i];
                maxFreqNum = i;
            }
        }
        double avg = sum * 1.0 / num;
        //------------------------------------------------------
        // 本题的关键在于求解中位数，而且如果一共偶数个数字，还要求平均数，不能用传统的排序方式，会超时
        //------------------------------------------------------
        int xx = 0;
        int yy = 255;
        for (int step = (num - 1) >> 1; step >= count[xx]; step -= count[xx++]) ;
        for (int step = (num - 1) >> 1; step >= count[yy]; step -= count[yy--]) ;
        double mid = (xx + yy) / 2.0;
        return new double[]{min, max, avg, mid, maxFreqNum};
    }


    /**
     * 2511. 最多可以摧毁的敌人城堡数目
     */
    public int captureForts(int[] forts) {
        //-------------------------------
        // 题意的本质为求解 -1 和 1 之间最多的 0 的个数
        //-------------------------------
        int maxNums = 0;
        int prevIndex = -1;
        int n = forts.length;
        for (int i = 0; i < n; i++) {
            if (forts[i] == 1 || forts[i] == -1) {
                if (prevIndex >= 0 && forts[i] * forts[prevIndex] == -1) {
                    maxNums = Math.max(maxNums, i - prevIndex - 1);
                }
                prevIndex = i;
            }
        }
        return maxNums;
    }


    /**
     * 消灭怪物的最大数量
     */
    public int eliminateMaximum(int[] dist, int[] speed) {
        int n = dist.length;
        int[] arriveTimes = new int[n];
        for (int i = 0; i < n; i++) {
            arriveTimes[i] = (dist[i] + speed[i] - 1) / speed[i];
        }
        Arrays.sort(arriveTimes);
        for (int i = 0; i < n; i++) {
            if (arriveTimes[i] <= i) return i;
        }
        return n;
    }





}
