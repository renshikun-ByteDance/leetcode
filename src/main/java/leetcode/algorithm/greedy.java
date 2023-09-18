package leetcode.algorithm;



import java.util.*;

/**
 * 贪心算法
 */
public class greedy {



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
     * 1736. 替换隐藏数字得到的最晚时间
     */
    public String maximumTime01(String time) {
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
        int[] buckets = new int[1000];
        for (int answer : answers) {
            //----------------------------------------------------------------------------------------------
            //报数相同的元素，不一定就是一种颜色，在此可理解为不一定属于一个组
            //    因此，应该将"报数相同的元素"逐一分组，做法：
            //        将报数相同的元素，归为一"堆"，逐一的分配组，连续的优先分配到同一个组，组内元素使用完，则新开一个组
            //    注意，每个组内可容纳的元素，为 报的数字 +1
            //---------------------------------------------------------------------------------------------
            if (buckets[answer] == 0) {     //针对报数相同的元素，单组内元素消耗完了，注意：单个组内的元素才是一个颜色
                res += answer + 1;        //新开辟一个组，以继续容纳"报数相同"元素
                buckets[answer] = answer;   //自身已经占据一个，此处为剩余仍有多少个可报此数的兔子（仍有多少个兔子可入组）
            } else {
                buckets[answer]--;          //消耗当前组内的元素
            }
        }
        return res;
    }


    public int numRabbits001(int[] answers) {
        int ans = 0;
        int maxFreq = 0;
        for (int answer : answers) {
            maxFreq = Math.max(maxFreq, answer);
        }
        int[] buckets = new int[maxFreq + 1];
        for (int answer : answers) {
            buckets[answer]++;
        }
        for (int i = 0; i <= maxFreq; i++) {
            if (buckets[i] == 0) continue;
            int x = buckets[i];
            int y = i + 1;
            ans += (((x + y - 1) / y) * y);    //向上取整
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
     * 916. 单词子集
     */
    public List<String> wordSubsets(String[] words1, String[] words2) {  //类似于保持城市的天际线
        List<String> ans = new ArrayList<>();
        int[] buckets = new int[26];
        for (String word : words2) {
            int[] temp = new int[26];
            for (int i = 0; i < word.length(); i++) {
                temp[word.charAt(i) - 'a']++;
            }
            for (int i = 0; i < 26; i++) {
                buckets[i] = Math.max(buckets[i], temp[i]);
            }
        }
        for (String word : words1) {
            int[] temp = new int[26];
            for (int i = 0; i < word.length(); i++) {
                temp[word.charAt(i) - 'a']++;
            }
            int currentIndex = 0;
            while (currentIndex <= 25) {
                if (buckets[currentIndex] > 0 && buckets[currentIndex] > temp[currentIndex]) {
                    break;
                }
                currentIndex++;
            }
            if (currentIndex == 26) ans.add(word);
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
                //    原因：在currentTime一定的情况下，课程 A首先被选择，课程 B其次被选择，说明 A[1] < B[1]，即课程 A的最晚完成时间在课程 B之前
                //          如果后期要替换掉课程 A，则说明 在 currentTime + A[0] + B[0] > B[1] &&  B[0] < A[0]
                //          如果课程 A被课程 B替换掉后，如果后面课程 A仍可正确被选择，则应该满足 currentTime + B[0] + A[0] < A[1]，这显然和 A[1] < B[1] 矛盾
                //     注意：不等式左侧，其实都是 currentTime + A[0] + B[0]
                //-----------------------------------------------------------------
            }
        }
        return ans;
    }

    public int scheduleCourse01(int[][] courses) {
        int ans = 0;
        int currentDay = 0;
        Arrays.sort(courses, (o1, o2) -> o1[1] - o2[1]);  //按照最晚开始时间升序排序
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o2[0] - o1[0]);  //大根堆
        for (int[] course : courses) {
            if (currentDay + course[0] <= course[1]) {
                sortedQueue.add(course);
                currentDay += course[0];
            } else if (!sortedQueue.isEmpty() && sortedQueue.peek()[0] >= course[0]
                    && (currentDay - sortedQueue.peek()[0]) <= course[1]) {
                int[] poll = sortedQueue.poll();
                currentDay -= poll[0];
                sortedQueue.add(course);
                currentDay += course[0];
            }
        }
        return sortedQueue.size();
    }


    public int scheduleCourse02(int[][] courses) {
        Arrays.sort(courses, (a,b)->a[1]-b[1]);
        PriorityQueue<Integer> q = new PriorityQueue<>((a,b)->b-a);
        int sum = 0;
        for (int[] c : courses) {
            int d = c[0], e = c[1];
            sum += d;
            q.add(d);
            if (sum > e) sum -= q.poll();
        }
        return q.size();
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
            arr[i] = Math.min(arr[i], arr[i - 1] + 1);   //min的原因，题目中只可减少不可增加
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

    public int[] diStringMatch001(String str) {  //贪心占位
        int n = str.length();
        int max = n;
        int min = 0;
        int[] ans = new int[n + 1];
        for (int i = 0; i < n; i++) {
            if (str.charAt(i) == 'D') {
                ans[i] = max;
                max--;
            } else {
                ans[i] = min;
                min++;
            }
        }
        ans[n] = max;   //此时 min == max
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
        int left = 0;
        int right = height.length - 1;
        int maxArea = 0;
        while (left < right) {
            //当前可盛的水容量
            maxArea = Math.max(maxArea, (right - left) * Math.min(height[left], height[right]));
            //移动最矮边
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }

            //---------------------------------------------------
            // 分析：针对两边相等的情况下，是否会因为移动不同的边，导致结果不同，答案肯定是不会的
            //      关键在于：此时，移动任意一边后，另一边不移动的情况下，无论怎么组合，都不会比当前的结更优，仔细思考
            //---------------------------------------------------
        }
        return maxArea;
    }


    /**
     * 45. 跳跃游戏 II
     */
    public int jump(int[] nums) {  //寻找最少的起跳点
        int steps = 0;
        int end = 0;
        int maxPosition = 0;
        //如果访问最后一个元素，在边界正好为最后一个位置的情况下，我们会增加一次「不必要的跳跃次数」，因此我们不必访问最后一个元素
        for (int i = 0; i < nums.length - 1; i++) {  //起跳点不能是 nums.length - 1，否则就相当于原地跳
            maxPosition = Math.max(maxPosition, i + nums[i]);
            //--------------------------------------------
            // 大循环通过 end 将一个连续的区间分割为了多个区间段
            //     在循环过程中，其实就是在遍历每个区间段，截至条件为 i == end
            //     在循环过程中，记录并筛选出从此区间内哪个点起跳，可以使得跳的最远，并最终以此点作为新的起跳点，更新step和下个区间右边界
            //     针对跳出的情况，i不会走到更新的右侧边界（跳出）
            //--------------------------------------------
            if (i == end) { //遍历到区间右边界
                // 区间遍历完成，从此区间内选择一个起跳点，使得能够跳的最远，即 maxPosition对应的起跳点
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
                countA[nums[i]]++;  //关键，投影
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

    public int maxEvents01(int[][] events) {  //错误写法
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> {
            if (o1[0] != o2[0]) return o1[0] - o2[0];  //先按照开始时间升序排序
            return o1[1] - o2[1];                      //再按照结束时间升序排序
        });
        sortedQueue.addAll(Arrays.asList(events));
        int ans = 0;
        int currentDay = 1;
        while (!sortedQueue.isEmpty()) {
            int[] subject = sortedQueue.poll();
            if (subject[0] <= currentDay && currentDay <= subject[1]) ans++;
            currentDay++;
        }
        return ans;
    }

    /**
     * 1710. 卡车上的最大单元数
     */
    public int maximumUnits(int[][] boxTypes, int truckSize) {
        int ans = 0;
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o2[1] - o1[1]);  //直接按照每个箱子可装载的单元数排序
        sortedQueue.addAll(Arrays.asList(boxTypes));
        while (truckSize > 0 && !sortedQueue.isEmpty()) {
            int[] current = sortedQueue.poll();
            ans += current[1];
            current[0]--;
            if (current[0] > 0) {
                sortedQueue.add(current);
            }
            truckSize--;
        }
        return ans;
    }

    public int maximumUnits01(int[][] boxTypes, int truckSize) {  //会快一些
        int ans = 0;
        Arrays.sort(boxTypes, (o1, o2) -> o2[1] - o1[1]);  //按照每个箱子的单元数排序
        for (int[] unit : boxTypes) {
            int nums = Math.min(unit[0], truckSize);
            ans += nums * unit[1];
            truckSize -= nums;
            if (truckSize == 0) break;
        }
        return ans;
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
     * 649. Dota2 参议院
     */
    public String predictPartyVictory(String senate) {
        ArrayDeque<Character> queue = new ArrayDeque<>();   //记录仍可投票的人员
        int numsR = 0;
        int numsD = 0;
        for (char ch : senate.toCharArray()) {
            queue.addLast(ch);
            numsD += ch == 'D' ? 1 : 0;
            numsR += ch == 'R' ? 1 : 0;
        }
        if (numsD == 0) return "Radiant";
        if (numsR == 0) return "Dire";
        int needR = 0;   //失效 R 的个数
        int needD = 0;   //失效 D 的个数
        while (numsD > 0 && numsR > 0 && !queue.isEmpty()) {
            Character current = queue.pollFirst();
            if (current == 'R') {
                if (needR > 0) needR--;   //当前元素被前方元素置为失效，不可参与投票，故不会再加入队列
                else if (needR == 0) {    //未失效，具有投票的权力
                    numsD--;
                    if (numsD == 0) return "Radiant";
                    needD++;
                    queue.addLast(current);
                }
            }
            if (current == 'D') {
                if (needD > 0) needD--;
                else if (needD == 0) {
                    numsR--;
                    if (numsR == 0) return "Dire";
                    needR++;
                    queue.addLast(current);
                }
            }
        }
        return "";
    }


    /**
     * 334. 递增的三元子序列
     */
    public boolean increasingTriplet(int[] nums) {  //参考  300. 最长递增子序列
        int top1 = nums[0];                      //左侧最小值
        int top2 = Integer.MAX_VALUE;            //中间中间值
        for (int i = 1; i < nums.length; i++) {  //寻找最大值
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


    //---------------------------------------------------
    // 下个的解法可以通过，但本质是由漏洞的，比如 {1, 6, 3, 6} 在 remove(6)时会把最后的也移除，从而错失了正确答案，建议通过 treeMap 来解决
    //---------------------------------------------------

    public boolean increasingTriplet01(int[] nums) {    //类似双指针的算法
        int len = nums.length;
        TreeSet<Integer> rightTree = new TreeSet<>();
        TreeSet<Integer> leftTree = new TreeSet<>();
        for (int i : nums)
            rightTree.add(i);
        rightTree.remove(nums[0]);
        leftTree.add(nums[0]);
        for (int i = 1; i < len - 1; i++) {
            int midValue = nums[i];
            //右侧树-剔除当前midValue（右侧树只需要剔除）
            rightTree.remove(nums[i]);
            leftTree.remove(nums[i]);   //目的是剔除重复元素
            //左侧
            Integer floor = leftTree.floor(nums[i]);
            //右侧
            Integer ceiling = rightTree.ceiling(nums[i]);
            //左侧树-添加当前元素（左侧树只需要添加）
            leftTree.add(nums[i]);
            if (floor != null && ceiling != null)
                return true;
        }
        return false;
    }

    public boolean increasingTriplet001(int[] nums) {  //单调栈，错误写法
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int num : nums) {
            while (!queue.isEmpty() && queue.peekLast() >= num) {  //升序排序
                queue.pollLast();
            }
            queue.addLast(num);
            if (queue.size() >= 3) return true;
        }

        return false;
    }


    /**
     * 826. 安排工作以达到最大收益
     */
    public int maxProfitAssignment(int[] difficulty, int[] profit, int[] worker) {
        int ans = 0;
        int n = difficulty.length;
        int[][] grid = new int[n][2];
        for (int i = 0; i < n; i++) {
            grid[i] = new int[]{difficulty[i], profit[i]};
        }
        Arrays.sort(grid, (o1, o2) -> o1[0] - o2[0]);  //按照工作的难度升序排序
        for (int i = 0; i < worker.length; i++) {
            int maxProfit = 0;
            for (int m = 0; m < n; m++) {
                if (grid[m][0] > worker[i]) break;
                maxProfit = Math.max(maxProfit, grid[m][1]);
            }
            ans += maxProfit;
        }
        return ans;
    }


    /**
     * 1801. 积压订单中的订单总数
     */
    public int getNumberOfBacklogOrders(int[][] orders) {
        int mod = (int) 1e9 + 7;
        //采购订单的信息：订单金额、订单数量
        PriorityQueue<int[]> buy = new PriorityQueue<>((o1, o2) -> o2[0] - o1[0]);  //按照订单金额降序排序
        //销售订单的信息：订单金额、订单数量
        PriorityQueue<int[]> sel = new PriorityQueue<>((o1, o2) -> o1[0] - o2[0]);  //按照订单金额升序排序
        for (int[] order : orders) {
            int price = order[0];
            int nums = order[1];
            int type = order[2];
            //采购订单
            if (type == 0) {
                while (nums > 0 && !sel.isEmpty() && sel.peek()[0] <= price) {
                    int[] curr = sel.poll();
                    if (curr[1] > nums) {
                        curr[1] -= nums;
                        sel.add(curr);
                        nums = 0;
                    } else {
                        nums -= curr[1];
                    }
                }
                if (nums != 0) buy.add(new int[]{price, nums});
            }
            //销售订单
            if (type == 1) {
                while (nums > 0 && !buy.isEmpty() && buy.peek()[0] >= price) {
                    int[] curr = buy.poll();
                    if (curr[1] > nums) {
                        curr[1] -= nums;
                        buy.add(curr);
                        nums = 0;
                    } else {
                        nums -= curr[1];
                    }
                }
                if (nums != 0) sel.add(new int[]{price, nums});
            }
        }
        int ans = 0;
        while (!buy.isEmpty()) {
            ans += buy.poll()[1];
            ans %= mod;
        }
        while (!sel.isEmpty()) {
            ans += sel.poll()[1];
            ans %= mod;
        }
        return ans;
    }


    public int getNumberOfBacklogOrders01(int[][] orders) {
        int mod = 1000000007;
        PriorityQueue<int[]> buy = new PriorityQueue<>((o1, o2) -> o2[0] - o1[0]);  //按照价格降序排序
        PriorityQueue<int[]> sel = new PriorityQueue<>((o1, o2) -> o1[0] - o2[0]);  //按照价格升序排序
        for (int[] order : orders) {
            int price = order[0];
            int amount = order[1];
            int type = order[2];
            //如果是采购订单
            if (type == 0) {
                while (!sel.isEmpty() && sel.peek()[0] <= price && amount > 0) {
                    int[] minTuple = sel.poll();
                    int minNums = Math.min(minTuple[1], amount);
                    amount -= minNums;
                    minTuple[1] -= minNums;
                    if (minTuple[1] > 0) {
                        sel.add(minTuple);
                    }
                }
                if (amount > 0) {
                    buy.add(new int[]{price, amount});
                }
            }
            //如果是销售订单
            if (type == 1) {
                while (!buy.isEmpty() && buy.peek()[0] >= price && amount > 0) {
                    int[] maxTuple = buy.poll();
                    int minNums = Math.min(maxTuple[1], amount);
                    amount -= minNums;
                    maxTuple[1] -= minNums;
                    if (maxTuple[1] > 0) {
                        buy.add(maxTuple);
                    }
                }
                if (amount > 0) {
                    sel.add(new int[]{price, amount});
                }
            }
        }
        int ans = 0;
        while (!buy.isEmpty()) {
            ans += buy.poll()[1];
            ans %= mod;
        }
        while (!sel.isEmpty()) {
            ans += sel.poll()[1];
            ans %= mod;
        }
        return ans;
    }


    /**
     * 1488. 避免洪水泛滥
     */
    public int[] avoidFlood(int[] rains) {
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
        TreeSet<Integer> unUsedSunnyDays = new TreeSet<>();  //基于 treeSet 实现二分搜索
        for (int i = 0; i < n; i++) {
            int curPool = rains[i];
            //1、当天为晴天，记录晴天日期
            if (curPool == 0) {
                unUsedSunnyDays.add(i);
                continue;
            }
            //2、当天为雨天，判断当前是否会导致洪水
            ans[i] = -1;  //当天下雨不能操作
            if (prevRains.containsKey(curPool)) {  //当前池塘之前下过雨
                Integer nextSunnyDay = unUsedSunnyDays.ceiling(prevRains.get(curPool));
                //如果上次下过雨后，没有可用的晴天，则会知道当前池塘发生洪水
                if (nextSunnyDay == null) {
                    return new int[]{};
                }
                //有可用的晴天，则此晴天需要抽干此池塘中上次下雨的水
                ans[nextSunnyDay] = curPool;
                unUsedSunnyDays.remove(nextSunnyDay);  //此晴天已被使用
            }
            prevRains.put(curPool, i);  //无论此池塘之前是否下过雨，能运行至此处说明都需要更新当前池塘下雨的日期
        }
        return ans;
    }


    /**
     * 1663. 具有给定数值的最小字符串
     */
    public String getSmallestString(int n, int k) {
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

    //-------------------------------------------------------------------------------------------
    // 上下两种写法均是按照右边界排序，区别在于上面 ans 记录不重叠区间的个数，下面 ans 记录重叠区间的个数
    //-------------------------------------------------------------------------------------------

    public int eraseOverlapIntervals000(int[][] intervals) {   //按照右边界升序排序
        int ans = 0;  //记录重叠区间的个数
        int n = intervals.length;
        Arrays.sort(intervals, (o1, o2) -> o1[1] - o2[1]);  //按照结束时间升序排序
        int currEndTime = intervals[0][1];
        for (int i = 1; i < n; i++) {
            if (intervals[i][0] < currEndTime) {
                ans++;
            } else {
                currEndTime = intervals[i][1];
            }
        }
        return ans;
    }

    //-------------------------------------------------------------------------
    // 上下两种写法，一种按照会议开始时间排序，一种按照会议结束时间排序
    //    1、按照会议结束时间排序，则最初筛选出来的基准会议，就直接是最终的会议
    //    2、按照会议开始时间排序，则最初筛选出来的基准会议，不一定是最终的会议，需要基于此找出与其时间冲突的会议，在这些会议中挑选那个结束时间最早的会议
    //-------------------------------------------------------------------------

    public int eraseOverlapIntervals01(int[][] intervals) {   //当成预定会议的题目，按照左边界升序排序
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

    public int eraseOverlapIntervals02(int[][] intervals) { //类似于 300. 最长递增子序列，但本题超时
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
     * 2335. 装满杯子需要的最短总时长
     */
    public int fillCups(int[] amount) {
        int ans = 0;
        PriorityQueue<Integer> sortedQueue = new PriorityQueue<>((o1, o2) -> o2 - o1);  //按照剩余数量降序排序
        for (int amt : amount) {
            if (amt > 0) sortedQueue.add(amt);
        }
        while (!sortedQueue.isEmpty() && sortedQueue.size() > 1) {
            Integer top1 = sortedQueue.poll();
            Integer top2 = sortedQueue.poll();
            top1--;
            top2--;
            if (top1 > 0) sortedQueue.add(top1);
            if (top2 > 0) sortedQueue.add(top2);
            ans++;
        }
        if (!sortedQueue.isEmpty()) {
            ans += sortedQueue.poll();
        }
        return ans;
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
     * 2418. 按身高排序
     */
    public String[] sortPeople(String[] names, int[] heights) {
        int n = names.length;
        Integer[] nums = new Integer[n];
        for (int i = 0; i < n; i++) {
            nums[i] = i;
        }
        Arrays.sort(nums, (o1, o2) -> heights[o2] - heights[o1]);
        String[] ans = new String[n];
        for (int i = 0; i < n; i++) {
            ans[i] = names[nums[i]];
        }
        return ans;
    }


    public String[] sortPeople01(String[] names, int[] heights) {
        int n = names.length;
        ArrayList<PeopleInfo> sorted = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            sorted.add(new PeopleInfo(names[i], heights[i]));
        }
        sorted.sort((o1, o2) -> o2.height - o1.height);
        String[] ans = new String[n];
        for (int i = 0; i < n; i++) {
            ans[i] = sorted.get(i).name;
        }
        return ans;
    }

    static class PeopleInfo {
        String name;
        int height;

        PeopleInfo(String name, int height) {
            this.name = name;
            this.height = height;
        }
    }

    /**
     * 1503. 所有蚂蚁掉下来前的最后一刻
     */
    public int getLastMoment(int n, int[] left, int[] right) {
        Arrays.sort(left);
        Arrays.sort(right);
        if (left.length == 0 && right.length == 0) return 0;
        else if (left.length == 0) return n - right[0];
        else if (right.length == 0) return left[left.length - 1];
        return Math.max(left[left.length - 1], n - right[0]);
    }



    /**
     * 1054. 距离相等的条形码
     */
    public int[] rearrangeBarcodes(int[] barcodes) {
        int[] buckets = new int[10001];
        for (int num : barcodes) {
            buckets[num]++;
        }
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o2[1] - o1[1]);  //按照出现的频次降序排序
        for (int i = 0; i < 10001; i++) {
            if (buckets[i] > 0) sortedQueue.add(new int[]{i, buckets[i]});
        }
        int currIndex = 0;
        int[] ans = new int[barcodes.length];
        while (currIndex < barcodes.length && !sortedQueue.isEmpty()) {
            int[] freqTop1 = sortedQueue.poll();
            if (currIndex > 0 && ans[currIndex - 1] == freqTop1[0] && !sortedQueue.isEmpty()) {
                int[] freqTop2 = sortedQueue.poll();
                ans[currIndex] = freqTop2[0];
                freqTop2[1]--;
                if (freqTop2[1] > 0) sortedQueue.add(freqTop2);
            } else {
                ans[currIndex] = freqTop1[0];
                freqTop1[1]--;
            }
            if (freqTop1[1] > 0) sortedQueue.add(freqTop1);
            currIndex++;
        }
        return ans;
    }


    /**
     * 1072. 按列翻转得到最大值等行数
     */
    public int maxEqualRowsAfterFlips(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        HashMap<String, Integer> map = new HashMap<>();
        //-----------------------------------------------------
        // 本题题意：经过多次翻转后，返回行内元素相同的最大行数，这里包括了两种情况：
        //    1、在翻转前，两行各位置的元素，就是相同的，那么翻转特定列后，可以保证行内元素相同，同时，两行各位元素也一致
        //    2、在翻转前，两行各位置的元素，均是取反的，那么翻转特定列后，可以保证行内元素相同，同事，两行元素不同
        // 本题关键：为了合并考虑上述两种情况，故可以将每行都按高位取 ^ 亦或，合并两种情况考虑
        //-----------------------------------------------------
        for (int i = 0; i < m; i++) {
            StringBuilder cur = new StringBuilder();
            cur.append(0);
            for (int j = 1; j < n; j++) {
                int ch = matrix[i][0] ^ matrix[i][j];
                cur.append(ch);
            }
            String ans = new String(cur);
            map.put(ans, map.getOrDefault(ans, 0) + 1);
        }
        int maxNums = 0;
        for (String cur : map.keySet()) {
            maxNums = Math.max(maxNums, map.get(cur));
        }
        return maxNums;
    }


    /**
     * 2208. 将数组和减半的最少操作次数
     */
    public int halveArray(int[] nums) {
        int freq = 0;
        long sums = 0;
        PriorityQueue<Double> sortedQueue = new PriorityQueue<Double>((o1, o2) -> o2.compareTo(o1));
        for (int num : nums) {
            sums += num;
            sortedQueue.add(num * 1.0);
        }
        double target = sums * 1.0 / 2;
        double curr = 0;
        while (curr < target && !sortedQueue.isEmpty()) {
            Double max = sortedQueue.poll();
            double next = max / 2;
            curr += next;
            sortedQueue.add(next);
            freq++;
        }
        return freq;
    }



}


