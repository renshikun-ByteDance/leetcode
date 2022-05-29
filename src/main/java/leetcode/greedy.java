package leetcode;


import com.sun.org.apache.bcel.internal.generic.ARETURN;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.*;

/**
 * 贪心算法
 */
public class greedy {


    /**
     * 575. 分糖果
     */
    public int distributeCandies(int[] candyType) {
        int total = candyType.length / 2;
        HashSet<Integer> candyTypeDistinct = new HashSet<>();
        for (int candy : candyType)
            candyTypeDistinct.add(candy);
        return Math.min(candyTypeDistinct.size(), total);
    }


    /**
     * 561. 数组拆分 I
     */
    public int arrayPairSum(int[] nums) {
        int ans = 0;
        // 则尽量使大小相近的两个数挨在一起
        // 求最大：成对数组的元素，应该挨着取，这样后续就不会舍弃掉差距大的值
        // 求最小：成对数组的元素，应该是头一个尾巴一个
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i += 2) //取奇数位，即为两个数中的最小值
            ans += nums[i];
        return ans;
    }


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
     * 524. 通过删除字母匹配到字典里最长单词
     */
    public String findLongestWord(String s, List<String> dictionary) {  //偏暴力解法
        String ans = "";
        int maxLen = 0;
        for (String word : dictionary) {
            int rightW = 0;
            int rightS = 0;
            while (rightS < s.length() && rightW < word.length()) {
                if (s.charAt(rightS) == word.charAt(rightW)) {
                    rightW++;
                }
                rightS++;
            }
            if (rightW == word.length()) {
                if (maxLen < word.length()) {
                    ans = word;
                    maxLen = word.length();
                } else if (maxLen == word.length() && word.compareTo(ans) < 0) {
                    ans = word;
                    maxLen = word.length();
                }
            }

        }
        return ans;
    }

    //思想：先按照基础条件排序，再逐个判断是否满足真正的条件，第一个满足条件的即为 最终答案
    public String findLongestWord01(String s, List<String> dictionary) {
        dictionary.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                //优先取长度最长的字符串
                if (o1.length() != o2.length())
                    return o2.length() - o1.length();   //到底谁减谁
                //长度相同，取字母序最小的字符串
                return o1.compareTo(o2);                //到底谁比谁
            }
        });
//        //基于lambda表达式
//        dictionary.sort((o1, o2) -> {
//            //优先取长度最长的字符串
//            if (o1.length() != o2.length())
//                return o2.length() - o1.length();
//            //长度相同，取字母序最小的字符串
//            return o1.compareTo(o2);
//        });

        for (String word : dictionary) {
            int rightW = 0;
            int rightS = 0;
            while (rightS < s.length() && rightW < word.length()) {
                if (s.charAt(rightS) == word.charAt(rightW)) {
                    rightW++;
                }
                rightS++;
            }
            if (rightW == word.length())  //由于已经排序，因此首个满足条件的，既是题目解
                return word;
        }
        return "";
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
     * 1736. 替换隐藏数字得到的最晚时间
     */
    public String maximumTime(String time) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < time.length(); i++) {
            if (time.charAt(i) == '?') {
                if (i == 0) {
                    if (time.charAt(1) == '?')
                        res.append('2');
                    else if (time.charAt(1) - '0' <= 3)
                        res.append('2');
                    else
                        res.append('1');
                } else if (i == 1) {
                    if (time.charAt(i - 1) == '0' || time.charAt(i - 1) == '1')
                        res.append('9');
                    else
                        res.append('3');
                } else if (i == 3)
                    res.append('5');
                else
                    res.append('9');
            } else
                res.append(time.charAt(i));
        }
        return res.toString();
    }


    /**
     * 781. 森林中的兔子
     */
    public int numRabbits(int[] answers) {
        int res = 0;
        HashMap<Integer, Integer> colorAndNums = new HashMap<>();
        //按照最小化原则，故认为回答的相同的兔子是同一个颜色的兔子，以此分组
        for (int answer : answers)
            colorAndNums.put(answer, colorAndNums.getOrDefault(answer, 0) + 1);
        //每个分组中的个数，都应该是 “回答” + 1
        for (int color : colorAndNums.keySet()) {
            if (color == 0)   //各不相同，独立成组
                res += colorAndNums.get(color);
            else if (color + 1 < colorAndNums.get(color))  //一个组不合理，需要拆分为多个组
                res += ((colorAndNums.get(color) / (color + 1)) + (colorAndNums.get(color) % (color + 1) != 0 ? 1 : 0)) * (color + 1);
                //组数*组中元素个数，其中组数又要判断一下这些元素至少需要多少组来容纳
            else
                res += color + 1;
        }
        return res;
    }

    //分组，消耗组内元素，一旦新分配一个组，无论组内元素是否占满，均应该记录整个组的长度，因为有些兔子没有报数
    public int numRabbits01(int[] answers) {
        int res = 0;
        int[] count = new int[1000];
        for (int answer : answers) {
            //----------------------------------------------------------------------------------------------
            //报数相同的元素，不一定就是一种颜色，在此可理解为不一定属于一个组
            //    因此，应该将"报数相同的元素"逐一分组，做法：
            //        将报数相同的元素，归为一"堆"，逐一的分配组，连续的优先分配到同一个组，组内元素使用完，则新开一个组
            //    注意，每个组内可容纳的元素，为 报的数字 +1
            //---------------------------------------------------------------------------------------------
            if (count[answer] == 0) {     //针对报数相同的元素，单组内元素消耗完了，注意：单个组内的元素才是一个颜色
                res += answer + 1;        //新开辟一个组，以继续容纳"报数相同"元素
                count[answer] = answer;
            } else {
                count[answer]--;          //消耗当前组内的元素
            }
        }
        return res;
    }


    /**
     * 1221. 分割平衡字符串
     */
    public int balancedStringSplit(String s) {
        int ans = 0;
        int numsL = 0;
        int numsR = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'L')
                numsL++;
            else
                numsR++;
            if (numsL == numsR) {
                ans++;
            }
        }
        return ans;
    }


    /**
     * 807. 保持城市天际线
     */
    public int maxIncreaseKeepingSkyline(int[][] grid) {
        int ans = 0;
        int[][] mergeGrid = new int[grid.length][grid.length];
        int[] westSkyline = new int[grid.length];
        int[] northSkyline = new int[grid.length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                //西向天际线
                westSkyline[i] = Math.max(westSkyline[i], grid[i][j]);   //通过二维矩阵中的横坐标表示
                //北向天际线
                northSkyline[j] = Math.max(northSkyline[j], grid[i][j]); //通过二维矩阵中的纵坐标表示
            }
        }
        //贪心，要满足两个方向的最值，故取交集
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                mergeGrid[i][j] = Math.min(westSkyline[i], northSkyline[j]);  //取交集
            }
        }
        //取最大差值
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                ans += mergeGrid[i][j] - grid[i][j];
            }
        }
        return ans;
    }


    /**
     * 1405. 最长快乐字符串
     */
    public String longestDiverseString(int a, int b, int c) {
        StringBuilder res = new StringBuilder();
        charAndNums[] sorted = {
                new charAndNums('a', a),
                new charAndNums('b', b),
                new charAndNums('c', c)
        };
        while (true) {
            //每次循环均需要重新排序
            Arrays.sort(sorted, (m, n) -> n.nums - m.nums);  //从大到小排序
            //注意：其实每次都是在"剩余最多"或"剩余次最多"的字符中选择一个进行消耗
            for (charAndNums tuple : sorted) {
                // 4、当遍历到需要消耗（插入）已经消耗完的"字符"时，说明当前已经是最快乐字符串
                //---------------------------------------------
                // 首先，每次消耗字符后，都会重新排序
                //     1、当第一个字符消耗完，则由于排序，会将其排在最后一位，且for循环每次都是在前两位中选取一位消耗，故当第一个字符消耗完，for循环遍历不到此此字符，也不会触发下面的情况
                //     2、当前仅当第二个字符也消耗完了（排在第三个的字符更是消耗完了），且第一个字符已经连续消耗两个后，才会触发此条件
                // 此时，所有字符要么已经消耗完（第二个、第三个），要么不能再消耗（第一个字符可能没消耗完，但由于不能超过三个连续字符，故已经不能在消耗）
                // 因此，当前的字符串即为最快乐字符串
                //---------------------------------------------
                if (tuple.nums == 0)       //两个字符消耗完同时第一个字符已经连续消耗两次，才会触发此条件
                    return res.toString();
                int rlen = res.length();
                // 1、优先消耗（插入）当前"剩余最多的字符"，但如果消耗（插入）此字符，将导致连续三个相同字符的情况，此时不满足条件
                // 2、因此，故应跳过此字符，尝试插入"剩余次最多的字符"
                if (rlen >= 2 && res.charAt(rlen - 1) == tuple.ch && res.charAt(rlen - 2) == tuple.ch)
                    continue;   //跳过此字符，下面不执行
                // 3、找到当前要消耗的字符
                res.append(tuple.ch);
                tuple.nums -= 1;
                break;  //跳出此轮遍历，出去重新排序，继续下个元素的消耗
            }
        }
    }

    public String longestDiverseString01(int a, int b, int c) {
        StringBuilder res = new StringBuilder();
        //优先队列
        PriorityQueue<charAndNums> sortedQueue = new PriorityQueue<>((o1, o2) -> (o2.nums - o1.nums));  //降序排列
        if (a > 0) sortedQueue.add(new charAndNums('a', a));
        if (b > 0) sortedQueue.add(new charAndNums('b', b));
        if (c > 0) sortedQueue.add(new charAndNums('c', c));
        while (true) {
            if (sortedQueue.isEmpty())  //上一轮恰好用完所有的元素
                return res.toString();
            charAndNums current = sortedQueue.poll();
            int len = res.length();
            //判断是否要取优先队列的首个元素
            if (len >= 2 && res.charAt(len - 1) == current.ch && res.charAt(len - 2) == current.ch) {
                //队列仅留一种元素（在上面取出后，队列就为空了），且这种元素已经消耗了两次
                if (sortedQueue.isEmpty())  //放这里的效果：上一轮消耗元素后（消耗非本元素），其余两种元素"恰"均已消耗完，在这里判读，故仍可继续消耗两个最多的元素
                    return res.toString();
                //满足上述条件，则不能取优先队列的首个元素，而应该取第二个元素，此时一定由第二个元素（否则第一步就退出了）
                charAndNums next = sortedQueue.poll();  //取当前队列首个，即为排序第二的元素，因为 current 还未加入队列
                //消耗元素
                res.append(next.ch);
                next.nums -= 1;
                //将出组的元素重新加入队伍
                if (next.nums > 0) sortedQueue.add(next);
                sortedQueue.add(current);
            } else {
                res.append(current.ch);
                current.nums -= 1;
                if (current.nums > 0) sortedQueue.add(current);
            }
        }
    }


    private class charAndNums {
        char ch;
        int nums;

        charAndNums(char ch, int nums) {
            this.ch = ch;
            this.nums = nums;
        }
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


    //时间复杂度高，超时
    public int longestSubsequence01(int[] arr, int difference) {
        int ans = 0;
        for (int i = 0; i < arr.length; i++) {
            int times = findNext(arr, arr[i], i, difference) + 1;
            ans = Math.max(ans, times);
        }
        return ans;
    }

    private int findNext(int[] arr, int currentValue, int currentPos, int difference) {
        for (int i = currentPos + 1; i < arr.length; i++) {
            if (arr[i] == currentValue + difference) {
                return findNext(arr, arr[i], i, difference) + 1;
            }
        }
        return 0;
    }


    private boolean findNext01(int[] arr, int currentValue, int currentPos, int difference) {
        for (int i = currentPos + 1; i < arr.length; i++) {
            if (arr[i] == currentValue + difference)
                return true;
        }
        return false;
    }


    /**
     * 1005. K 次取反后最大化的数组和
     */
    public int largestSumAfterKNegations(int[] nums, int k) {
        int sum = 0;
        while (k > 0) {
            //排序，每次反转最小值即可
            Arrays.sort(nums);    // K 次排序，时间复杂度高，但能通过
            nums[0] = -nums[0];
            k--;
        }
        for (int num : nums)
            sum += num;
        return sum;
    }

    //仅排一次序
    public int largestSumAfterKNegations01(int[] nums, int k) {
        //排序的目的：按照需要反转的迫切程度，对相关数据依次反转
        Arrays.sort(nums);
        int sum = 0;
        int minValue = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] < 0 && k > 0) {   //对于需要反转的，且当前仍有反转次数的情况，根据迫切程度依次反转
                nums[i] = -nums[i];
                k--;   // K先用于反转负数
            }
            sum += nums[i];
            minValue = Math.min(minValue, nums[i]); //记录最小值（如果K未被消耗完的花，最小值为正数），用于在后续消耗尽K
        }
        //如果 K被消耗完，则当前一定是最优解
        //如果 K未被消耗完，则仍需继续处理，即此时根据K的奇偶性，判断是否需要反转最小的正数
        if (k != 0) {
            if (k % 2 != 0)  //K为奇数
                sum -= 2 * minValue;
        }
        return sum;
    }


    //桶排序，归类统一计算
    public int largestSumAfterKNegations02(int[] nums, int k) {
        int sum = Arrays.stream(nums).sum();
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int num : nums)
            hTable.put(num, hTable.getOrDefault(num, 0) + 1);
        // 1、首先，先用 K 反转负数
        for (int num = -100; num < 0; num++) {
            if (hTable.containsKey(num)) {
                int times = Math.min(k, hTable.get(num));
                sum += (-num) * times * 2;  //加正数
                //更新消耗后对map的影响
                hTable.put(num, hTable.get(num) - times); //将这部分负数，转换为正数了，故消耗掉了一部分
                hTable.put(-num, hTable.getOrDefault(-num, 0) + times); //添加转换的结果
                k -= times; //K被消耗 times次
                if (k == 0)
                    return sum;
            }
        }
        // 2、其次，反转 0 ，如果有可以多次反转，故如果包含 0 ，直接返回当前结果即可
        if (hTable.containsKey(0) || k % 2 == 0)  //如果 没有 0 ，但剩余需要反转的次数为偶数，相当于无反转，直接返回结果即可
            return sum;

        // 3、最后，反转正数（迫不得已，为了消耗尽 K），且此时 K 为奇数，只需反转最小的一个正数即可
        for (int num = 1; num <= 100; num++) {
            if (hTable.containsKey(num))
                return sum - 2 * num;
        }
        return -1;
    }


    public int largestSumAfterKNegations03(int[] nums, int k) {
        int sum = 0;
        int minR = Integer.MAX_VALUE;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int num : nums) {
            sum += num;
            if (num >= 0)
                minR = Math.min(minR, num);
            hTable.put(num, hTable.getOrDefault(num, 0) + 1);
        }
        for (int i = -100; i < 0; i++) {
            if (hTable.containsKey(i)) {
                int times = Math.min(k, hTable.get(i));
                sum += 2 * (-i) * times;
                //重新维护 hTable 的状态（重要，但容易忽略）
                hTable.put(i, hTable.get(i) - times);
                hTable.put(-i, hTable.getOrDefault(i, 0) + times);
                //重新维护 minR 的值
                minR = Math.min(minR, -i);
                //重新维护 K 的值
                k -= times;
            }
            if (k == 0)
                return sum;
        }

        if (minR == 0 || (k & 1) == 0)  //最小的数为 0或 k为偶数
            return sum;
        else {     //不为零且奇数个
            sum -= 2 * minR;
        }
        return sum;
    }



    /**
     * 1705. 吃苹果的最大数目
     */
    public int eatenApples(int[] apples, int[] days) {
        int maxApples = 0;
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> (o1[0] - o2[0]));  //按照腐烂日期升序排序
        int maxVaildPeriod = 0;
        int d = 0;
//        while (!sortedQueue.isEmpty() || d < apples.length) {   //保证队列中的元素均被处理到，消耗或剔除
        while (d <= maxVaildPeriod || d < apples.length) {   //maxVaildPeriod保证所有的均能遍历到
            if (d < apples.length && apples[d] > 0) {
                sortedQueue.add(new int[]{d + days[d], apples[d]});  //允许重复，故无需于之前的合并
                maxVaildPeriod = Math.max(maxVaildPeriod, d + days[d]);
            }
            //剔除过期腐烂的苹果
            while (!sortedQueue.isEmpty() && (sortedQueue.peek()[0] <= d || sortedQueue.peek()[1] == 0))  //队首的苹果筐中的腐烂日期小于今天的日期，故过期了
                sortedQueue.poll(); //取出，不在放入队列，相当于剔除
            if (!sortedQueue.isEmpty()) {
                sortedQueue.peek()[1]--;
                maxApples++;
            }
            d++;
        }
        return maxApples;
    }


    /**
     * 517. 超级洗衣机
     */
    public int findMinMoves(int[] machines) {
        int sum = Arrays.stream(machines).sum();
        if (sum % machines.length != 0)
            return -1;
        int average = sum / machines.length;
        int ans = 0;
        int prefixSum = 0;
        for (int num : machines) {
            //重新定义基准（平均值），即重新定义相较于 0 的情况，此时有正数有负数，正数代表：相对平均值多的，负数代表：相对平均值少的
            num -= average;
            prefixSum += num;
            //----------------------------------------------------
            //注意：前缀和区间内的"中间"元素可承担桥梁的作用，每次一进一出
            //前缀和的目的：将洗衣机分为两组，前半部分为一组，后半部分为一组
            //    1、前缀和区间内，如果元素均大于 0 或均小于 0
            //           代表如果想要将这部分区间填平，则仅"单纯"的需要 "移出"或"移入"，因为，前缀和区间内所有元素均大于 0 或 小于 0，所以整个区间内所有元素的移动方向一致
            //               1、前缀和 m > 0
            //                  移出，操作的步骤数为 prefixSum
            //               2、前缀和 m < 0
            //                  移入，操作的步骤数为 -prefixSum
            //           因此，针对前缀和区间内元素 正负 一致的情况，最终需要 Math.abs(prefixSum) 次操作才可将前缀和区间所有元素填平（置为 0）
            //    2、前缀和区间内，如果元素有大于 0 也有均小于 0
            //           代表如果想要将这部分区间填平，不仅需要区间整体对外 "移出"或"移入"，同时，也需要内部平衡，因为，前缀和区间内所有元素 正负不一致，所以内部平衡和外部平衡所需要移动元素的方向会有不一致的情况
            //               1、前缀和 m > 0 ，
            //                  因为，前缀和区间内元素有正有负
            //                      1、对外
            //                        "最终"要对外"移出"，向右侧移动
            //                      2、对内
            //                        需要内部平衡，有一部分需要向左侧移动
            //                   但由于每个洗衣机每次只能移动一个衣服（肯定是只能朝一个方向移动），因此，在对外输出的方向和对内平衡的方向不一致时，一次移动不可同时兼顾两种情况
            //             因此，针对前缀和区间内元素 正负 不一致的情况，最终需要前缀和区间内 max(num) 次移动，即将"最"值处理完了，其余的情况也一定处理完了
            //                2、前缀和 m < 0
            //                    同上，略
            //----------------------------------------------------
            ans = Math.max(ans, Math.max(Math.abs(prefixSum), num));
        }
        return ans;
    }


    /**
     * 630. 课程表 III
     */
    public int scheduleCourse(int[][] courses) {
        //基于课程的最晚完成时间排序（时间顺序）
        Arrays.sort(courses, (o1, o2) -> (o1[1] - o2[1]));
        //选择的课程，对已经选中的课程在最优队列中按照课程消耗的时长降序排序，用于回退
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> (o2[0] - o1[0]));
        int ans = 0;
        int currentTime = 0;
        for (int[] course : courses) {  //按最晚完成时间依次遍历
            //-------------------------------------------------------------------
            // 满足以下两条条件的将作为所选课程
            //    1）当前时间 + 该课程持续时间 <= 该课程截止时间，此时，直接选修该课程；
            //    2）当前时间 + 该课程持续时间 > 该课程截止时间 && 该课程持续时间 < 堆顶课程的持续时间，此时选择该课程，淘汰堆顶课程，用最小的时间上最多的课；
            //       注意：
            //           1、堆顶课程是在已添加课表中持续时间最长的课程，即最耗时的课程
            //           2、由于 for循环仅一次遍历，因此针对先添加后淘汰的课程，后续不会尝试再次添加，那么有个疑问：在淘汰后的课程是否有可能或有条件重新添加到已选课程中
            //                 答案：肯定是不行的
            //                 原因：...见下
            //-------------------------------------------------------------------
            if (currentTime + course[0] <= course[1]) {
                sortedQueue.add(course);
                currentTime += course[0];  //时间推进
                ans++;   //课程数加一
            } else if (!sortedQueue.isEmpty() && sortedQueue.peek()[0] > course[0]) { //待添加课程的不满足最晚完成时间，同时其时长 小于 已选课程的最长时长
                //则从已选课程中剔除时长最长的课程，添加当前课程
                int[] poll = sortedQueue.poll();
                currentTime -= poll[0];    //时间回退
                sortedQueue.add(course);
                currentTime += course[0];  //基于新选的课程，重新推进时间
                //课程数不变，剔除顶堆课程，添加当前课程

                //-----------------------------------------------------------------
                // 会不会存在：被剔除的元素，仍有可能在重新被选，并满足条件，答案是不会的
                //    原因：在currentTime一定的情况下，课程A首先被选择，课程B其次被选择，说明 A[1] < B[1]，即课程A的最晚完成时间在课程B之前
                //          如果后期要替换掉课程 A，则说明 在 currentTime + A[0] + B[0] > B[1] &&  B[0] < A[0]
                //          如果课程A被课程B替换掉后，如果后面课程A仍可正确被选择，则应该满足 currentTime + B[0] + A[0] < A[1]，这显然和 A[1] < B[1] 矛盾
                //     注意：不等式左侧，其实都是 currentTime + A[0] + B[0]
                //-----------------------------------------------------------------
            }
        }
        return ans;
    }


    /**
     * 1846. 减小和重新排列数组后的最大元素
     */
    public int maximumElementAfterDecrementingAndRearranging(int[] arr) {
        //如果一个数组是满足要求的，那么将它的元素按照升序排序后得到的数组也是满足要求的
        Arrays.sort(arr);
        //由于数组必须以1开头，同时两数之差的最大为 1（在升序的条件下）
        arr[0] = 1;
        for (int i = 1; i < arr.length; i++) {
            //-----------------------------------------
            // 1、如果arr[i]满足条件，就用自身arr[i]，此时：
            //       arr[i] == arr[i - 1] 或 arr[i] == arr[i - 1] + 1
            // 2、如果arr[i]不满足条件，此时用当前位置上的最大值（贪心），以让数组末尾尽可能大
            //       arr[i] == arr[i - 1] + 1
            //-----------------------------------------
            arr[i] = Math.min(arr[i], arr[i - 1] + 1);
        }
        return arr[arr.length - 1];
    }

    public int maximumElementAfterDecrementingAndRearranging01(int[] arr) {
        Arrays.sort(arr);
        arr[0] = 1;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] - arr[i - 1] > 1)
                arr[i] = arr[i - 1] + 1;
        }
        return arr[arr.length - 1];
    }

    /**
     * 1833. 雪糕的最大数量
     */
    public int maxIceCream(int[] costs, int coins) {
        int ans = 0;
        Arrays.sort(costs);
        int total = 0;
        for (int cost : costs) {
            total += cost;
            if (total <= coins)
                ans++;
            else
                break;
        }
        return ans;
    }


    /**
     * 553. 最优除法
     */
    public String optimalDivision(int[] nums) {
        if (nums.length == 1)
            return String.valueOf(nums[0]);
        if (nums.length == 2)   //必须有，因为 [3,2] 如果不这样处理，执行以下逻辑，其返回值为 3/(2)，即会有多余的括号
            return String.valueOf(nums[0]) + "/" + String.valueOf(nums[1]);
        StringBuilder ans = new StringBuilder();
        ans.append(nums[0]).append("/").append("(");
        for (int i = 1; i < nums.length; i++) {
            ans.append(nums[i]);
            if (i != nums.length - 1)
                ans.append("/");
        }
        ans.append(")");
        return ans.toString();
    }


    /**
     * 942. 增减字符串匹配
     */
    public int[] diStringMatch(String s) {   //比较当前位字符，给下一位赋值，关键在于初始化
        int[] ans = new int[s.length() + 1];
        TreeSet<Integer> sortedTree = new TreeSet<>();
        int DTimes = 0;
        for (int i = 0; i < ans.length; i++) {
            sortedTree.add(i);
            if (i < ans.length - 1 && s.charAt(i) == 'D')
                DTimes++;
        }
        ans[0] = DTimes;
        sortedTree.remove(DTimes);
        for (int i = 0; i < ans.length - 1; i++) {
            if (s.charAt(i) == 'I')
                ans[i + 1] = sortedTree.ceiling(ans[i]);  //较大的
            else
                ans[i + 1] = sortedTree.floor(ans[i]);    //较小的
            sortedTree.remove(ans[i + 1]);
        }
        return ans;
    }

    public int[] diStringMatch01(String s) {    //比较当前位字符，给当前位赋值，关键在于思想
        int[] ans = new int[s.length() + 1];
        int low = 0;
        int high = s.length();
        for (int i = 0; i < s.length(); i++) {
            //如果是下降，则给当前位为当前的最大值，那么无论后面怎么取，都是小于此最大值的
            ans[i] = s.charAt(i) == 'D' ? high-- : low++;   //当前位的值：由当前位的 char 决定
        }
        ans[s.length()] = s.charAt(s.length() - 1) == 'D' ? low : high;  //当前为的值，由前一位的 char决定
        return ans;
    }


    /**
     * 1996. 游戏中弱角色的数量
     */
    public int numberOfWeakCharacters(int[][] properties) {
        int ans = 0;
        Arrays.sort(properties, (o1, o2) -> {
            //首先按照攻击值降序排序，当攻击值相等其次按照防御值升序排序
            return o1[0] != o2[0] ? o2[0] - o1[0] : o1[1] - o2[1];  //防御值，一定在攻击值相等时，按照升序排序
        });
        int currentDefense = -1;
        for (int[] p : properties) {    //已排序，因此遍历的顺序首先满足第一个条件
            //----------------------------------------
            //针对攻击值相等的角色，按照防御值升序排列的作用：
            //   1、对外
            //      针对防御值相等的角色，最多只有一个"弱角色"，而这个"弱角色"最有可能的就是防御值最小的哪个角色，排在防御值相等角色的最前面，用于同前一个角色比较
            //   2、对内
            //      不会触发第二个条件："弱角色"的防御值降序的条件，因为攻击值已经是降序，但针对攻击值相等的情况，如果防御值也按照降序排列，按照以下判断方式，会导致"攻击值相等的多个角色中，会有多个"弱角色""
            //注意：
            //   1、排序
            //      攻击值按照降序排序，会存在相等的情况
            //      防御值按照升序排列，是在攻击值相等的情况下，因此，整体防御值是乱序的，只是在攻击值相等的情况下，防御值按照降序排列
            //   2、更新防御值
            //      1、攻击值不相等的相邻两个角色 A B
            //           1、如果角色 B为"弱角色"，此时不必更新最大的防御值，贪心，当前的防御值就是最大
            //           2、如果角色 B不是"弱角色"（防御值较 A大），此时要更新最大的防御值，贪心，拓展防御值的空间，以容纳更多"弱角色"
            //      2、攻击值相等的相邻两个角色 A B
            //           因为攻击值相等的角色，按照防御值升序排序，故要依次更新防御值，原因同上
            //       关键：更新防御值尽可能使其变大，以给后面的角色留有余地
            //贪心：
            //    根据遍历的顺序：
            //       弱角色的，力求自保，不更新防御值
            //       非弱角色的，舍己为人，更新防御值（因为防御值肯定是大于前一个角色防御值），拓展空间，以容纳更多的"弱角色"
            //----------------------------------------
            if (currentDefense > p[1])  //在满足第一个条件的基础上，单独校验是否满足第二个条件
                ans++;
            else                        //如果不满足第二个条件，则应该用此角色的防御值更新下一轮比较的防御值，而不能用当前已经遍历过的最大的防御值
                currentDefense = p[1];
            //关键：else 中的情况，包含了 攻击值相等的情况，在这种情况下，要更新防御值，故尽量提高防御值，所以排序是，在攻击值相等的情况下，按照防御值升序排序
        }
        return ans;
    }

    public int numberOfWeakCharacters02(int[][] properties) {
        int ans = 0;
        Arrays.sort(properties, (o1, o2) ->
                o2[0] - o1[0]   //错误写法，可验证 int[][] properties = {{7, 9}, {10, 7}, {6, 9}, {10, 4}, {7, 5}, {7, 10}};
        );
        int maxDefense = properties[0][1];
        for (int i = 1; i < properties.length; i++) {
            if (properties[i - 1][0] > properties[i][0] && maxDefense > properties[i][1])  //关键在于 两组攻击值相等的元素，对比时，有问题
                ans++;
            else
                maxDefense = Math.max(maxDefense, properties[i][1]);
        }
        return ans;
    }


    //优先队列，相对于上面的解法，此方法时间复杂度高
    public int numberOfWeakCharacters01(int[][] properties) {
        int ans = 0;
        //优先队列，以各个角色的attack攻击值降序排序
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> {
            return o1[0] != o2[0] ? o2[0] - o1[0] : o1[1] - o2[1];
        });
        sortedQueue.addAll(Arrays.asList(properties));
        int currentDefense = -1;
        while (!sortedQueue.isEmpty()) {
            int[] p = sortedQueue.poll();
            if (currentDefense > p[1])
                ans++;
            else
                currentDefense = p[1];
        }
        return ans;
    }


    /**
     * 11. 盛最多水的容器
     */
    public int maxArea(int[] height) {
        int maxAreaWater = 0;
        int left = 0;
        int right = height.length - 1;
        while (left < right) {
            maxAreaWater = Math.max(maxAreaWater, Math.min(height[left], height[right]) * (right - left));
            if (height[left] > height[right])
                right--;
            else
                left++;
        }
        return maxAreaWater;
    }


    /**
     * 45. 跳跃游戏 II
     */
    public int jump(int[] nums) {
        int steps = 0;
        int end = 0;
        int maxPosition = 0;
        //如果访问最后一个元素，在边界正好为最后一个位置的情况下，我们会增加一次「不必要的跳跃次数」，因此我们不必访问最后一个元素
        for (int i = 0; i < nums.length - 1; i++) {
            maxPosition = Math.max(maxPosition, i + nums[i]);
            //大循环将一个连续的区间分割为了多个区间段
            if (i == end) {        // 0 == 0 已经累加了跳跃数，如果数组仅有 1 个元素，则不会执行到此，在for循环就被拒掉了
                end = maxPosition;
                steps++;           //##进入##下一次跳跃
            }
        }
        return steps;
    }

    //反向查找
    public int jump01(int[] nums) {
        int steps = 0;
        int position = nums.length - 1;
        while (position > 0) {
            for (int i = 0; i < position; i++) {
                if (i + nums[i] >= position) {   //正序找到第一个满足条件的，则其跳跃距离最大
                    position = i;
                    steps++;
                    break;
                }
            }
        }
        return steps;
    }

    //自己写的逻辑，好复杂
    public int jump02(int[] nums) {
        if (nums.length == 1) return 0;
        int ans = 0;
        int pos = 0;
        int maxPosition = 0;
        //如果访问最后一个元素，在边界正好为最后一个位置的情况下，我们会增加一次「不必要的跳跃次数」，因此我们不必访问最后一个元素
        while (true) {
            ans++;    //本次跳动
            int start = pos + 1;        //区间开始，这里写的pos为起跳位置
            int end = pos + nums[pos];  //区间结束，由于区间长度>=2时，由于一定有解，所以nums[0]不会为0，至少唯一，所以有 end>=start
            if (end >= nums.length - 1)  //由于本次跳动导致的越界，直接返回
                return ans;
            for (int i = start; i <= end; i++) {
                if (i + nums[i] > maxPosition) { //不断更新可以跳到的最远距离，从而获取对应的起跳位置
                    pos = i;        //记录起跳位置（可跳最远）
                    maxPosition = i + nums[i];  //最远距离
                }
            }
            //以下加不加均可，不加得话，在下一次循环也能处理
//            if (maxPosition >= nums.length - 1)    //由于新选择的位置，导致的越界，增加新选择的位置后，返回
//                return ++ans;
        }
    }


    /**
     * 1414. 和为 K 的最少斐波那契数字数目
     */
    static {
        ArrayList<Integer> lists = new ArrayList<>();
        lists.add(1);
        lists.add(1);
        int a = 1;
        int b = 1;
        int Fib = -1;
        while (Fib <= Math.pow(10, 9)) {
            Fib = a + b;
            lists.add(Fib);
            a = b;    //a移动至 b的大小，记录 b
            b = Fib;  //b记录Fib
        }
        int[] nums = lists.stream().mapToInt(Integer::intValue).toArray();
    }

    //预计算
    private static final int[] FIB = {
            1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597,
            2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418, 317811,
            514229, 832040, 1346269, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817,
            39088169, 63245986, 102334155, 165580141, 267914296, 433494437, 701408733, 1134903170
    };

    public int findMinFibonacciNumbers(int k) {
        int ans = 0;
        while (k > 0) {
            //二分，尝试在左侧逼近 target
            int floor = binSearchNextFib(FIB, k);  //从左侧找到最接近的值，则无限逼近，贪心
            k -= floor;
            ans++;     //在 K 的加法运算中添加 floor 一项（非零项）
        }
        return ans;
    }

    //二分法写法一：从左侧逼近target
    private int binSearchNextFib(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {  //最终状态 left = right
            int mid = left + ((right - left) >> 1);  //mid必须向右侧倾斜，防止进入死循环
            if (nums[mid] <= target)
                left = mid + 1;
            else if (target < nums[mid])
                right = mid - 1;
        }
        //虽然这样的写法，会有数组越界的风险，但数组已知且target已知，经评估不会越界，无需判断
        return nums[right]; //target存在返回 target、target不存在 right位于target的左侧，即左侧逼近
    }

    //二分法写法二：从左侧逼近target
    private int binSearchNextFib01(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {  //最终状态 left = right
            int mid = left + ((right - left + 1) >> 1);  //mid必须向右侧倾斜，防止进入死循环
            if (nums[mid] <= target)
                left = mid;
            else if (target < nums[mid])
                right = mid - 1;
        }
        return nums[left];
    }

    //二分法写法三：从左侧逼近target
    private int binSearchLeft(int[] FIBs, int target) {
        int left = 0;
        int right = FIBs.length - 1;
        int k = left;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (FIBs[mid] <= target) {
                k = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return FIBs[k];  //左侧逼近值
    }




    /**
     * 1877. 数组中最大数对和的最小值
     */
    public int minPairSum(int[] nums) {
        int ans = 0;
        //排序
        Arrays.sort(nums);
        int left = 0;
        int right = nums.length - 1;
        //排序后，从两头分别取数，用一个较大的数和一个较小的数组成数对，以防止两个较大的数组成数对，贪心，从而使得数组中最大数对和最小
        while (left < right) {
            ans = Math.max(ans, nums[left] + nums[right]);
            left++;
            right--;
        }
        return ans;
    }


    /**
     * 2170. 使数组变成交替数组的最少操作数
     */
    public int minimumOperations(int[] nums) {  //时间复杂度高
        if (nums.length == 1) return 0;
        HashMap<Integer, Integer> AhTable = new HashMap<>();
        HashMap<Integer, Integer> BhTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if ((i & 1) == 1)   //奇数位
                AhTable.put(nums[i], AhTable.getOrDefault(nums[i], 0) + 1);
            else                //偶数位
                BhTable.put(nums[i], BhTable.getOrDefault(nums[i], 0) + 1);
        }
        ArrayList<Map.Entry<Integer, Integer>> Asorted = new ArrayList<>(AhTable.entrySet());
        ArrayList<Map.Entry<Integer, Integer>> Bsorted = new ArrayList<>(BhTable.entrySet());
        //降序排序
        Asorted.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        Bsorted.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        Map.Entry<Integer, Integer> ATop1 = Asorted.get(0);
        Map.Entry<Integer, Integer> BTop1 = Bsorted.get(0);
        int fre = 0;
        if (!ATop1.getKey().equals(BTop1.getKey()))     //奇偶位上Top1频次的元素不重复
            fre = ATop1.getValue() + BTop1.getValue();
        else {                                          //奇偶位上Top1频次的元素重复，尝试取奇偶位Top2频次的元素
            if (Asorted.size() > 1 && Bsorted.size() > 1)
                fre = Math.max(BTop1.getValue() + Asorted.get(1).getValue(), ATop1.getValue() + Bsorted.get(1).getValue());
            else if (Asorted.size() > 1)
                fre = Math.max(BTop1.getValue() + Asorted.get(1).getValue(), ATop1.getValue());
            else if (Bsorted.size() > 1)
                fre = Math.max(BTop1.getValue(), ATop1.getValue() + Bsorted.get(1).getValue());
            else   //奇偶位均分别只有一个元素，且元素重复，故此数组所有位置上的数一样，但奇数和偶数位数不一定相同，因为数组长度不一定是偶数
                fre = Math.max(ATop1.getValue(), BTop1.getValue());  //注意：这里不是 nums.length / 2
        }
        return nums.length - fre;
    }


    //类同于上面的算法，但是专门处理了一下，保证奇数和偶数位对应的 map中至少含有两个元素
    public int minimumOperations01(int[] nums) {  //时间复杂度高
        if (nums.length == 1) return 0;
        HashMap<Integer, Integer> AhTable = new HashMap<>();
        HashMap<Integer, Integer> BhTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if ((i & 1) == 1)   //奇数位
                AhTable.put(nums[i], AhTable.getOrDefault(nums[i], 0) + 1);
            else                //偶数位
                BhTable.put(nums[i], BhTable.getOrDefault(nums[i], 0) + 1);
        }
        //奇数和偶数位置最终的map中各自分别至少有两个元素
        AhTable.put(-1, 0);  //等价于空元素
        BhTable.put(-1, 0);  //等价于空元素
        ArrayList<Map.Entry<Integer, Integer>> Asorted = new ArrayList<>(AhTable.entrySet());
        ArrayList<Map.Entry<Integer, Integer>> Bsorted = new ArrayList<>(BhTable.entrySet());
        //降序排序
        Asorted.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        Bsorted.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        Map.Entry<Integer, Integer> ATop1 = Asorted.get(0);
        Map.Entry<Integer, Integer> BTop1 = Bsorted.get(0);
        int fre = 0;
        if (!ATop1.getKey().equals(BTop1.getKey()))     //奇偶位上Top1频次的元素不重复
            fre = ATop1.getValue() + BTop1.getValue();
        else
            fre = Math.max(ATop1.getValue() + Bsorted.get(1).getValue(), BTop1.getValue() + Asorted.get(1).getValue());
        return nums.length - fre;
    }

    public int minimumOperations06(int[] nums) {
        int[] countA = new int[100001];
        int[] countB = new int[100001];
        for (int i = 0; i < nums.length; i++) {
            if ((i & 1) == 1) //奇数位
                countA[nums[i]]++;
            else              //偶数位
                countB[nums[i]]++;
        }
        //A/B集合中值最大的前两位的索引值，默认值
        int ATop1 = 0;
        int ATop2 = 0;
        int BTop1 = 0;
        int BTop2 = 0;
        for (int i = 1; i < 100001; i++) {
            //集合A中会存在 Top1和Top2索引不同，但频次相同的情况
            if (countA[i] > countA[ATop1]) {  //是否有等号，无所谓，其关系到 top1 和 top2 在频次相等时，在哪里处理，没有等号，在下面处理，有等号，在这里处理
                ATop2 = ATop1;
                ATop1 = i;
            } else if (countA[i] > countA[ATop2]) {  //类同上，但这里涉及top2 和 top3在频次相等的情况下，在哪里处理，这里无需处理，等不等没所谓
                ATop2 = i;
            }

            if (countB[i] >= countB[BTop1]) {
                BTop2 = BTop1;
                BTop1 = i;
            } else if (countB[i] >= countB[BTop2]) {
                BTop2 = i;
            }
        }
        int maxTimes = ATop1 == BTop1 ?
                Math.max(countA[ATop1] + countB[BTop2], countA[ATop2] + countB[BTop1]) : countA[ATop1] + countB[BTop1];

        return nums.length - maxTimes;
    }



    /**
     * 881. 救生艇
     */
    public int numRescueBoats(int[] people, int limit) {
        int ans = 0;
        Arrays.sort(people);
        int left = 0;
        int right = people.length - 1;
        while (left < right) {
            if (people[right] + people[left] <= limit) {
                left++;
                right--;
            } else
                right--;
            ans++;
        }
        if (left == right)
            ans++;
        return ans;
    }


    /**
     * 1337. 矩阵中战斗力最弱的 K 行
     */
    public int[] kWeakestRows(int[][] mat, int k) {  //优先队列的方式
        int[] ans = new int[k];
        PriorityQueue<Map.Entry<Integer, Integer>> sortedQueue = new PriorityQueue<Map.Entry<Integer, Integer>>(
                (o1, o2) -> {
                    if (!o1.getValue().equals(o2.getValue()))
                        return o1.getValue() - o2.getValue();  //升序
                    else
                        return o1.getKey() - o2.getKey();      //升序
                }
        );
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 0; i < mat.length; i++) {
            if (mat[i][0] == 0) hTable.put(i, 0);
            for (int j = 0; j < mat[0].length && mat[i][j] != 0; j++) {
                hTable.put(i, hTable.getOrDefault(i, 0) + 1);
            }
        }
        sortedQueue.addAll(hTable.entrySet());
        for (int i = 0; i < k && !sortedQueue.isEmpty(); i++) {
            Map.Entry<Integer, Integer> poll = sortedQueue.poll();
            ans[i] = poll.getKey();
        }
        return ans;
    }


    public int[] kWeakestRows01(int[][] mat, int k) {  //优先队列的方式
        int[] ans = new int[k];
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> {
            if (o1[1] != o2[1])
                return o2[1] - o1[1];   //降序、大根堆
            else
                return o2[0] - o1[0];   //降序、大根堆

        });
        for (int i = 0; i < mat.length; i++) {
            int m = binSearch(mat[i]);
            int[] tuple = new int[2];
            tuple[0] = i;
            if (m >= 0) {
                tuple[1] = m + 1;  // 1 的个数
            }
            if (sortedQueue.size() >= k) {
                int[] peek = sortedQueue.peek();
                if (tuple[1] < peek[1] || (tuple[1] == peek[1] && tuple[0] < peek[0])) {
                    sortedQueue.poll();
                    sortedQueue.add(tuple);
                }
            } else {
                sortedQueue.add(tuple);
            }
        }

        while (k > 0 && !sortedQueue.isEmpty()) {
            ans[k - 1] = sortedQueue.poll()[0];
            k--;
        }

        return ans;
    }

    private int binSearch(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        int k = -1;  // K 位于 1 最右侧的1上
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] != 0) {     //也是一种比较巧妙的二分方法，虽然不是有序的数据
                k = mid;
                left = mid + 1;
            } else
                right = mid - 1;
        }
        return k;
    }


}


