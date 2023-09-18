package leetcode.homework.prev;


import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class comeBackHome {


    /**
     * 1005. K 次取反后最大化的数组和
     */
    public int largestSumAfterKNegations(int[] nums, int k) {
        HashMap<Integer, Integer> hTable = new HashMap<>();
        int sum = 0;
        for (int num : nums) {
            sum += num;
            hTable.put(num, hTable.getOrDefault(num, 0) + 1);  //记录频次
        }
        //针对负数
        for (int i = -100; i < 0; i++) {   //依次取数，而不是对 HashMap 排序，也是桶排序的思想
            if (hTable.containsKey(i)) {
                int times = Math.min(hTable.get(i), k);
                sum -= 2 * i * times;
                hTable.put(-i, hTable.getOrDefault(-i, 0) + times);
                k -= times;  //更新反转次数
                if (k == 0) return sum;
            }
        }
        //针对零
        if (hTable.containsKey(0) || (k & 1) == 0)  //偶数次
            return sum;
        //针对正数
        for (int i = 1; i <= 100; i++) {
            if (hTable.containsKey(i)) {
                sum -= 2 * i;
                break;
            }
        }
        return sum;
    }

    //---------------------------------------------------------------------------
    // 桶排序，由于题目要求，需要对元素进行排序，故分桶要素是元素值，桶内元素为元素频次
    //---------------------------------------------------------------------------

    public int largestSumAfterKNegations01(int[] nums, int k) {
        int sum = 0;
        int[] buckets = new int[202];
        for (int num : nums) {
            sum += num;
            buckets[num + 100]++;  //将所有数字 +100，映射到正数区间
        }
        for (int i = 0; i < 100; i++) {
            if (buckets[i] != 0) {   // i 是 +100后的数，buckets[i]为其频次
                int times = Math.min(buckets[i], k);
                sum -= 2 * (i - 100) * times;
                k -= times;
                buckets[-(i - 100) + 100] += times;
                if (k == 0) return sum;
            }
        }
        if (buckets[100] != 0 || (k & 1) == 0)
            return sum;
        for (int i = 101; i < 201; i++) {
            if (buckets[i] != 0) {
                sum -= 2 * (i - 100);
                break;
            }
        }
        return sum;
    }


    /**
     * 451. 根据字符出现频率排序
     */
    public String frequencySort(String s) {
        StringBuilder ans = new StringBuilder();
        HashMap<Character, Integer> hTable = new HashMap<>();
        char[] array = s.toCharArray();
        for (Character ca : array)
            hTable.put(ca, hTable.getOrDefault(ca, 0) + 1);
        ArrayList<Map.Entry<Character, Integer>> sorted = new ArrayList<>(hTable.entrySet());
        sorted.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        for (Map.Entry<Character, Integer> next : sorted) {
            Character key = next.getKey();
            Integer times = next.getValue();
            while (times != 0) {
                ans.append(key);
                times--;
            }
        }
        return ans.toString();
    }

    public String frequencySort01(String s) {  //方法类似上，此处给了128个桶，通过"元素"来分桶，桶内记录元素的频次 ，均需要排序一次
        StringBuilder ans = new StringBuilder();
        int[][] buckets = new int[128][2];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i][0] = i;
        }
        char[] array = s.toCharArray();
        for (Character ca : array) {
            buckets[ca][1]++;   //不需要 -'0'只用于将字符 '0'-'9'转换为 int 0-9
        }
        Arrays.sort(buckets, (o1, o2) -> {   //依旧排序，但仅排序一次，桶排序尝试基于分桶元素为 "频次"
            if (o1[1] != o2[1])
                return o2[1] - o1[1];
            else
                return o1[0] - o2[0];
        });
        for (int i = 0; i < 128; i++) {
            int times = buckets[i][1];
            if (times != 0) {
                while (times != 0) {
                    ans.append((char) buckets[i][0]);
                    times--;
                }
            }
        }
        return ans.toString();
    }

    //---------------------------------------------------------------------------
    // 桶排序，由于题目要求，需要对元素频次进行排序，故分桶要素是元素频次，桶内元素为具有相同频次的多个不同元素
    //---------------------------------------------------------------------------

    public String frequencySort02(String s) {  //通过"频次"来分桶，桶内记录频次相同的所有元素，无需排序
        StringBuilder ans = new StringBuilder();
        HashMap<Character, Integer> hTable = new HashMap<>();
        int maxFreq = 0;
        char[] array = s.toCharArray();
        for (Character ca : array) {
            int times = hTable.getOrDefault(ca, 0) + 1;
            hTable.put(ca, times);
            maxFreq = Math.max(maxFreq, times);  //记录最大频次，用于桶的个数
        }
        StringBuilder[] buckets = new StringBuilder[maxFreq + 1];
        for (int i = 1; i <= maxFreq; i++) {
            buckets[i] = new StringBuilder();
        }
        for (Map.Entry<Character, Integer> next : hTable.entrySet()) {
            buckets[next.getValue()].append(next.getKey());
        }
        for (int i = maxFreq; i > 0; i--) { //不排序，但基于一定数据取数据，类似排序效果
            while (buckets[i].length() > 0) {
                int times = i;
                char cc = buckets[i].charAt(0);
                while (times > 0) {
                    ans.append(cc);
                    times--;
                }
                buckets[i].deleteCharAt(0);
            }
        }
        return ans.toString();
    }


    /**
     * 1047. 删除字符串中的所有相邻重复项
     */
    public String removeDuplicates(String s) {
        StringBuilder ans = new StringBuilder();
        ArrayDeque<Character> dequeQueue = new ArrayDeque<>();
        char[] array = s.toCharArray();
        for (Character ca : array) {
            if (!dequeQueue.isEmpty()) {
                if (!dequeQueue.peekLast().equals(ca)) {
                    dequeQueue.addLast(ca);
                } else {
                    dequeQueue.removeLast();
                }
            } else {
                dequeQueue.addLast(ca);
            }
        }
        while (!dequeQueue.isEmpty()) {
            ans.append(dequeQueue.pollFirst());
        }
        return ans.toString();
    }

    public String removeDuplicates01(String s) {
        StringBuilder ans = new StringBuilder();
        char[] array = s.toCharArray();
        for (Character ca : array) {
            if (ans.length() > 0) {
                if (ans.charAt(ans.length() - 1) == ca) {
                    ans.deleteCharAt(ans.lastIndexOf(ca.toString()));
                } else {
                    ans.append(ca);
                }
            } else {
                ans.append(ca);
            }
        }
        return ans.toString();
    }


    /**
     * 20. 有效的括号
     */
    public boolean isValid(String s) {
        ArrayDeque<Character> dequeQueue = new ArrayDeque<>();
        char[] array = s.toCharArray();
        HashMap<Character, Character> hTable = new HashMap<>();
        hTable.put('{', '}');
        hTable.put('(', ')');
        hTable.put('[', ']');
        for (Character ca : array) {
            if (!dequeQueue.isEmpty()) {
                if (hTable.containsKey(dequeQueue.peekLast()) && hTable.get(dequeQueue.peekLast()) == ca) {
                    dequeQueue.removeLast();
                } else {
                    dequeQueue.addLast(ca);
                }
            } else {
                dequeQueue.addLast(ca);
            }
        }
        return dequeQueue.isEmpty();
    }

    public boolean isValid01(String s) {
        StringBuilder builder = new StringBuilder();
        char[] array = s.toCharArray();
        HashMap<Character, Character> hTable = new HashMap<>();
        hTable.put('{', '}');
        hTable.put('(', ')');
        hTable.put('[', ']');
        for (Character ca : array) {
            if (builder.length() > 0) {
                char last = builder.charAt(builder.length() - 1);
                if (hTable.containsKey(last) && hTable.get(last) == ca) {
                    builder.deleteCharAt(builder.lastIndexOf(String.valueOf(last)));
                } else {
                    builder.append(ca);
                }
            } else {
                builder.append(ca);
            }
        }
        return builder.length() == 0;
    }


    /**
     * 1190. 反转每对括号间的子串
     */
    public String reverseParentheses(String s) {
        StringBuilder ans = new StringBuilder();
        ArrayDeque<Character> dequeQueue = new ArrayDeque<>();
        char[] array = s.toCharArray();
        for (Character ca : array) {
            if (ca == ')') {
                StringBuilder builder = new StringBuilder();
                while (!dequeQueue.isEmpty() && dequeQueue.peekLast() != '(') {  //反转
                    builder.append(dequeQueue.pollLast());
                }
                dequeQueue.removeLast();  //移除 '('
                char[] reverseArray = builder.toString().toCharArray();
                for (Character re : reverseArray) {
                    dequeQueue.addLast(re);
                }
            } else {
                dequeQueue.addLast(ca);
            }
        }
        while (!dequeQueue.isEmpty()) {
            ans.append(dequeQueue.pollFirst());
        }
        return ans.toString();
    }

    public String reverseParentheses01(String s) {
        StringBuilder ans = new StringBuilder();
        ArrayDeque<Character> dequeQueue = new ArrayDeque<>();
        char[] array = s.toCharArray();
        for (Character ca : array) {
            if (ca == ')') {
                ConcurrentLinkedQueue<Character> FIFO = new ConcurrentLinkedQueue<>();
                while (!dequeQueue.isEmpty() && dequeQueue.peekLast() != '(') {  //反转
                    FIFO.add(dequeQueue.pollLast());
                }
                dequeQueue.removeLast();  //移除 '('
                while (!FIFO.isEmpty()) {
                    dequeQueue.add(FIFO.poll());
                }
            } else {
                dequeQueue.addLast(ca);
            }
        }
        while (!dequeQueue.isEmpty()) {
            ans.append(dequeQueue.pollFirst());
        }
        return ans.toString();
    }


    /**
     * 32. 最长有效括号
     */
    public int longestValidParentheses(String s) {
        int ans = 0;
        Stack<Integer> stack = new Stack<>();
        stack.push(-1);
        char[] array = s.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == '(') {
                stack.push(i);
            } else {
                stack.pop();
                if (stack.isEmpty()) {
                    stack.push(i);
                } else {
                    ans = Math.max(ans, i - stack.peek());
                }
            }
        }
        return ans;
    }

    public int longestValidParentheses01(String s) {
        int ans = 0;
        Stack<Integer> stack = new Stack<>();
        char[] array = s.toCharArray();
        int[] flags = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] == '(') {
                stack.push(i);
            } else {
                if (!stack.isEmpty() && array[stack.peek()] == '(') {
                    flags[i] = 1;
                    flags[stack.pop()] = 1;
                }
            }
        }
        int left = 0;
        int right = 0;
        while (right < flags.length) {
            if (flags[right] == 0) {
                ans = Math.max(ans, right - left);
                left = right + 1;
            } else if (right == flags.length - 1) {
                ans = Math.max(ans, right - left + 1);
            }
            right++;
        }
        return ans;
    }


    public int longestValidParentheses02(String s) {
        int ans = 0;
        Stack<Integer> stack = new Stack<>();
        char[] array = s.toCharArray();
        int[] flags = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] == '(') {
                stack.push(i);
            } else {
                if (!stack.isEmpty() && array[stack.peek()] == '(') {
                    flags[i] = 1;
                    flags[stack.pop()] = 1;
                }
            }
        }
        int times = 0;
        for (int i = 0; i < flags.length; i++) {
            if (flags[i] == 1) {
                times++;
                ans = Math.max(ans, times);
            } else {
                times = 0;
            }
        }
        return ans;
    }


    public int longestValidParentheses03(String s) {
        int ans = 0;
        Stack<Integer> stack = new Stack<>();
        char[] array = s.toCharArray();
        int[] flags = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] == '(') {
                stack.push(i);
            } else {
                if (!stack.isEmpty() && array[stack.peek()] == '(') {
                    flags[i] = 1;
                    flags[stack.pop()] = 1;
                }
            }
        }
        int left = -1;  //关键，保证 left 一直位于 0 上
        int right = 0;
        while (right < flags.length) {
            if (flags[right] == 1) {
                ans = Math.max(ans, right - left);  // left一直位于 0 上
            } else {
                left = right;
            }
            right++;
        }
        return ans;
    }


    /**
     * 575. 分糖果
     */
    public int distributeCandies(int[] candyType) {
        HashSet<Integer> distinct = new HashSet<>();
        for (int value : candyType) {
            distinct.add(value);
        }
        return Math.min(candyType.length / 2, distinct.size());
    }


    /**
     * 561. 数组拆分
     */
    public int arrayPairSum(int[] nums) {
        int ans = 0;
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i += 2) {
            ans += nums[i];
        }
        return ans;
    }


    /**
     * 524. 通过删除字母匹配到字典里最长单词
     */
    public String findLongestWord(String s, List<String> dictionary) {
        PriorityQueue<String> sortedQueue = new PriorityQueue<>((o1, o2) -> {
            if (o1.length() != o2.length())
                return o2.length() - o1.length();
            else
                return o1.compareTo(o2);
        });
        for (String dict : dictionary) {
            int right = 0;
            for (int i = 0; i < s.length() && right < dict.length(); i++) {
                if (s.charAt(i) == dict.charAt(right))
                    right++;
                if (right == dict.length())
                    sortedQueue.add(dict);
            }
        }
        return sortedQueue.size() == 0 ? "" : sortedQueue.peek();
    }


    /**
     * 781. 森林中的兔子
     */
    public int numRabbits(int[] answers) {
        int res = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();  //报的数：报此数的兔子数
        //解题关键: 颜色相同的兔子，报数一定一样，但报数一样的颜色不一定相同
        for (int ans : answers) {
            hTable.put(ans, hTable.getOrDefault(ans, 0) + 1);
        }
        for (int answer : hTable.keySet()) {
            if (answer == 0) {    //仅有一个此颜色的兔子
                res += hTable.get(answer);  //有几个兔子就是几个兔子，各个兔子独立成组（颜色独立），每个组内一个元素，不涉及贪心
            } else if (answer + 1 <= hTable.get(answer)) {  //分组，10：20兔，一个组代表一个颜色，报相同数的人多，将其至少分为几个组，贪心
                //报数 answer，则组内至少 answer + 1个兔子，如果报此数的兔子个数大于 answer + 1，则一个组容纳不下这些兔子，需要分组
                res += (hTable.get(answer) / (answer + 1) + (hTable.get(answer) % (answer + 1) == 0 ? 0 : 1)) * (answer + 1);
            } else {  //单个组，10：5兔，贪心认为 5个兔子均属于这个组
                //单个组能容纳，报的数 answer + 1 个兔子
                res += answer + 1;
            }
        }
        return res;
    }


    public int numRabbits01(int[] answers) {
        int res = 0;
        int[] counts = new int[1000];
        for (int answer : answers) {
            if (counts[answer] == 0) {  //此组无成员
                res += answer + 1;  //新开辟一个组，组内兔子数为 answer + 1
                counts[answer] = answer; //除本身外，仍能容纳的兔子数
            } else {   //报相同的数对应的组已经存在，消耗
                counts[answer]--;
            }
        }
        return res;
    }


    /**
     * 1221. 分割平衡字符串
     */
    public int balancedStringSplit(String s) {
        int ans = 0;
        int counts = 0;
        char[] array = s.toCharArray();
        for (Character ca : array) {
            if (ca == 'L')
                counts++;
            if (ca == 'R')
                counts--;
            if (counts == 0)
                ans++;
        }
        return ans;
    }


    /**
     * 807. 保持城市天际线
     */
    public int maxIncreaseKeepingSkyline(int[][] grid) {
        int ans = 0;
        int[] left = new int[grid.length];
        int[] bottom = new int[grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                left[i] = Math.max(left[i], grid[i][j]);
                bottom[j] = Math.max(bottom[j], grid[i][j]);
            }
        }
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                ans += Math.min(left[i], bottom[j]) - grid[i][j];
            }
        }
        return ans;
    }

    public int maxIncreaseKeepingSkyline01(int[][] grid) {
        int ans = 0;
        int[] westSkyline = new int[grid.length];
        int[] northSkyline = new int[grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                westSkyline[i] = Math.max(westSkyline[i], grid[i][j]);
                northSkyline[j] = Math.max(northSkyline[j], grid[i][j]);
            }
        }
        int[][] mergeSkyline = new int[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                mergeSkyline[i][j] = Math.min(westSkyline[i], northSkyline[j]); //计算各个方向均保持最高天际线的最高高度
            }
        }
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                ans += mergeSkyline[i][j] - grid[i][j];
            }
        }
        return ans;
    }


    /**
     * 1405. 最长快乐字符串
     */
    public String longestDiverseString(int a, int b, int c) {
        PriorityQueue<charAndNums> sortedQueue = new PriorityQueue<>((o1, o2) -> {
            if (o2.num != o1.num) return o2.num - o1.num;
            return o1.ca - o2.ca;
        });
        if (a > 0) sortedQueue.add(new charAndNums('a', a));
        if (b > 0) sortedQueue.add(new charAndNums('b', b));
        if (c > 0) sortedQueue.add(new charAndNums('c', c));
        StringBuilder ans = new StringBuilder();
        while (true) {
            if (sortedQueue.isEmpty())
                return ans.toString();
            charAndNums current = sortedQueue.poll();
            int len = ans.length();
            if (len > 1 && (ans.charAt(len - 2) == ans.charAt(len - 1)) && ans.charAt(len - 1) == current.ca) {
                if (sortedQueue.isEmpty()) {
                    return ans.toString();
                }
                charAndNums second = sortedQueue.poll();
                ans.append(second.ca);
                second.num--;
                if (second.num != 0)
                    sortedQueue.add(second);
                sortedQueue.add(current);
            } else {
                ans.append(current.ca);
                current.num--;
                if (current.num != 0)
                    sortedQueue.add(current);
            }
        }
    }

    public static class charAndNums {
        char ca;
        int num;

        charAndNums(char ca, int num) {
            this.ca = ca;
            this.num = num;
        }
    }


    /**
     * 1705. 吃苹果的最大数目
     */
    public int eatenApples(int[] apples, int[] days) {
        int maxApples = 0;
        int currentDay = 0;
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[0] - o2[0]);  //第几天：当天过期的苹果数
        while (!sortedQueue.isEmpty() || currentDay < apples.length) {
            // 1、按照时间进度添加元素，即当日新长出的苹果
            if (currentDay < apples.length && apples[currentDay] > 0) {
                sortedQueue.add(new int[]{currentDay + days[currentDay], apples[currentDay]});  //可以重复
            }
            // 2、删除当日过期的苹果 和 为零的情况
            while (!sortedQueue.isEmpty() && (sortedQueue.peek()[0] == currentDay || sortedQueue.peek()[1] == 0))
                sortedQueue.poll(); //剔除
            // 3、消耗当前最迫切要消耗的苹果
            if (!sortedQueue.isEmpty()) {
                sortedQueue.peek()[1]--;
                maxApples++;
            }
            currentDay++;
        }
        return maxApples;
    }


    /**
     * 1705. 吃苹果的最大数目
     */
    public int eatenApples01(int[] apples, int[] days) {
        int maxApples = 0;
        int currentDay = 0;
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[0] - o2[0]);   //第几天：当天腐烂的苹果数
        while (currentDay < days.length || !sortedQueue.isEmpty()) {
            // 1、添加元素
            if (currentDay < days.length && apples[currentDay] != 0) {
                sortedQueue.add(new int[]{currentDay + days[currentDay], apples[currentDay]}); //存在重复
            }

            // 2、剔除元素
            while (!sortedQueue.isEmpty() && (sortedQueue.peek()[1] == 0 || sortedQueue.peek()[0] == currentDay)) {
                sortedQueue.poll();
            }

            // 3、消耗元素
            if (!sortedQueue.isEmpty()) {
                sortedQueue.peek()[1]--;
                maxApples++;
            }

            currentDay++;
        }
        return maxApples;
    }


    /**
     * 630. 课程表 III
     */
    public int scheduleCourse(int[][] courses) {
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


    /**
     * 1846. 减小和重新排列数组后的最大元素
     */
    public int maximumElementAfterDecrementingAndRearranging(int[] arr) {
        Arrays.sort(arr);
        arr[0] = 1;
        for (int i = 1; i < arr.length; i++) {
            arr[i] = Math.min(arr[i], arr[i - 1] + 1);
        }
        return arr[arr.length - 1];
    }


    /**
     * 1833. 雪糕的最大数量
     */
    public int maxIceCream(int[] costs, int coins) {
        int ans = 0;
        int sum = 0;
        Arrays.sort(costs);
        for (int cost : costs) {
            sum += cost;
            if (sum > coins)
                return ans;
            ans++;
        }
        return ans;
    }


    /**
     * 1518. 换酒问题
     */
    public int numWaterBottles(int numBottles, int numExchange) {
        int ans = numBottles;  //首次喝完
        int remainEmptyBottle = numBottles;  //全部空瓶
        while (remainEmptyBottle >= numExchange) {  //统一判断条件为：当前空瓶个数是否大于可兑换的个数
            int get = remainEmptyBottle / numExchange;
            ans += get;  //喝完兑换的 get 瓶
            remainEmptyBottle = get + remainEmptyBottle % numExchange;
        }
        return ans;
    }

    public int numWaterBottles01(int numBottles, int numExchange) {
        int ans = 0;
        int remainEmptyBottle = 0;
        int get = numBottles;
        while (true) {
            ans += get; //全部喝完
            remainEmptyBottle = get + remainEmptyBottle;  //当前空瓶子的个数
            get = remainEmptyBottle / numExchange; //当前所有的空瓶，可兑换的酒水
            remainEmptyBottle = remainEmptyBottle % numExchange;  //未兑换酒水的空瓶子数

            //------------------------------
            // 错误写法：最终没有一个归一的可用于判断的条件
            //------------------------------
            if (remainEmptyBottle < numExchange)  //get还有呢，不统一
                return ans + get;
        }
    }


    /**
     * 942. 增减字符串匹配
     */
    public int[] diStringMatch(String s) {
        int[] ans = new int[s.length() + 1];
        int low = 0;
        int high = s.length();
        for (int i = 0; i < s.length(); i++) {
            ans[i] = s.charAt(i) == 'D' ? high-- : low++;
            //反向思维：如果为 "D" ，则当前位直接用当前的最大值，则下一位的选择一定小于当前最大值
            //反向思维：如果为 "I" ，则当前位直接用当前的最小值，则下一位的选择一定大于当前最大值
        }
        //此时必有 high == low
        ans[s.length()] = high;
        return ans;
    }

    public int[] diStringMatch01(String s) {
        int[] ans = new int[s.length() + 1];
        TreeSet<Integer> sortedTree = new TreeSet<>();
        int low = 0;
        int high = s.length();
        int DTimes = 0;
        for (int i = low; i <= high; i++) {
            sortedTree.add(i);
            if (i < high && s.charAt(i) == 'D') DTimes++;
        }
        ans[0] = DTimes;
        sortedTree.remove(DTimes);
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'D') {
                ans[i + 1] = sortedTree.floor(ans[i]); //比其小的最大数
            }
            if (s.charAt(i) == 'I') {
                ans[i + 1] = sortedTree.ceiling(ans[i]); //比其大的最小数
            }
            sortedTree.remove(ans[i + 1]);
        }
        return ans;
    }


    /**
     * 11. 盛最多水的容器
     */
    public int maxArea(int[] height) {
        int ans = 0;
        int left = 0;
        int right = height.length - 1;
        while (left < right) {
            ans = Math.max(ans, Math.min(height[left], height[right]) * (right - left));
            if (height[left] > height[right])
                right--;
            else
                left++;
        }
        return ans;
    }


    /**
     * 45. 跳跃游戏 II
     */
    public int jump(int[] nums) {
        if (nums.length == 1) return 0;
        int Times = 0;
        int current = 0;
        int next = -1;  //下一个位点
        int maxRight = 0;
        while (true) {
            Times++; //新跳一步，选择next
            int start = current + 1;
            int end = current + nums[current];
            if (end >= nums.length - 1)  //next点直接再区间外，即新跳的这一步直接跳出
                return Times;
            for (int i = start; i <= end; i++) {  //从 current 位置开始，跳动区间为 [start , end]
                if (i + nums[i] > maxRight) {  //记录从区间内某个next点起跳，其能跳跃的最大位置
                    next = i;
                    maxRight = i + nums[i];
                }
            }
            if (maxRight >= nums.length - 1)  //next在区间内，从next直接跳出（ +1）
                return ++Times;
            current = next;
        }
    }

    public int jump00(int[] nums) {
        int steps = 0;
        int right = nums.length - 1;
        while (right > 0) {
            for (int i = 0; i < right; i++) {
                if (i + nums[i] >= right) {
                    right = i;
                    steps++;
                    break;
                }
            }
        }
        return steps;
    }

    public int jump02(int[] nums) {
        int steps = 0;
        int maxPosition = 0;
        int end = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            maxPosition = Math.max(maxPosition, i + nums[i]);
            if (i == end) {
                end = maxPosition;
                steps++;
            }
        }
        return steps;
    }


    public int jump01(int[] nums) {  //错误写法，很乱
        int Times = 0;
        int current = 0;
        int maxRight = 0;
        if (nums.length == 1) return 0;
        while (true) {
            if (maxRight >= nums.length - 1)  //上一步选择的新的起跳点next，可直接跳出
                return Times;  //从 next 跳出

            Times++; //新跳一步
            int next = 0;  //下一个位点
            for (int i = 1; i <= nums[current]; i++) {  //从 current 位置开始，依次跳动 1 - nums[i] 步，并记录其能跳跃的最大位置
                //1、从current点就可直接跳出，跳动一次
                if (current + i >= nums.length)
                    return Times;  //这一步就跳出去了
                //2、从current点不可直接跳出，记录可跳跃的最大位移
                int tempRight = current + i + nums[current + i];  //跳动至 临时点 current + i
                if (tempRight > maxRight) { //记录从current点能跳至的区间内 [current + 1，current + nums[current]]的点可跳跃的最大位移及起跳点，作为current要跳动的点
                    next = current + i;
                    maxRight = tempRight;
                }
            }
            current = next;
        }
    }


    /**
     * 1877. 数组中最大数对和的最小值
     */
    public int minPairSum(int[] nums) {
        int ans = 0;
        int left = 0;
        int right = nums.length - 1;
        Arrays.sort(nums);
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
    public int minimumOperations(int[] nums) {
        if (nums.length == 1) return 0;
        HashMap<Integer, Integer> oneHTable = new HashMap<>();
        HashMap<Integer, Integer> twoHTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if ((i & 1) == 1) //奇数位置
                oneHTable.put(nums[i], oneHTable.getOrDefault(nums[i], 0) + 1);
            else
                twoHTable.put(nums[i], twoHTable.getOrDefault(nums[i], 0) + 1);
        }
        oneHTable.put(-1, 0);
        twoHTable.put(-1, 0);
        //奇数组排序
        ArrayList<Map.Entry<Integer, Integer>> oneSorted = new ArrayList<>(oneHTable.entrySet());
        oneSorted.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        //偶数组排序
        ArrayList<Map.Entry<Integer, Integer>> twoSorted = new ArrayList<>(twoHTable.entrySet());
        twoSorted.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        //Top
        Map.Entry<Integer, Integer> Atop1 = oneSorted.get(0);
        Map.Entry<Integer, Integer> Atop2 = oneSorted.get(1);
        Map.Entry<Integer, Integer> Btop1 = twoSorted.get(0);
        Map.Entry<Integer, Integer> Btop2 = twoSorted.get(1);
        int fre = 0;
        if (!Atop1.getKey().equals(Btop1.getKey())) {
            fre = Atop1.getValue() + Btop1.getValue();
        } else {
            fre = Math.max(Atop1.getValue() + Btop2.getValue(), Atop2.getValue() + Btop1.getValue());
        }
        return nums.length - fre;
    }


    public int minimumOperations01(int[] nums) {
        int[] countA = new int[100001];
        int[] countB = new int[100001];
        for (int i = 0; i < nums.length; i++) {
            if ((i & 1) == 1)
                countA[nums[i]]++;  //投影，基于 count记录各个数出现的频次，索引为 num,值为 Freq
            else
                countB[nums[i]]++;
        }
        int Atop1 = 0;
        int Atop2 = 0;
        int Btop1 = 0;
        int Btop2 = 0;
        //记录奇数、偶数位的最大值和次最大值
        for (int i = 0; i < countA.length; i++) {
            if (countA[i] > countA[Atop1]) {  //记录频次最大的 num
                Atop2 = Atop1;
                Atop1 = i;
            } else if (countA[i] > countA[Atop2]) { //记录频次次最大的 num
                Atop2 = i;
            }

            if (countB[i] > countB[Btop1]) {
                Btop2 = Btop1;
                Btop1 = i;
            } else if (countB[i] > countB[Btop2]) {
                Btop2 = i;
            }
        }
        int maxFreq = Atop1 != Btop1 ? countA[Atop1] + countB[Btop1] :
                Math.max(countA[Atop1] + countB[Btop2], countA[Atop2] + countB[Btop1]);
        return nums.length - maxFreq;
    }


    /**
     * 881. 救生艇
     */
    public int numRescueBoats(int[] people, int limit) {
        int ans = 0;
        Arrays.sort(people);
        int left = 0;
        int right = people.length - 1;
        while (left <= right) {
            if (people[left] + people[right] > limit) {
                right--;
            } else {
                left++;
                right--;
            }
            ans++;
        }
        return ans;
    }


    /**
     * 1337. 矩阵中战斗力最弱的 K 行
     */
    public int[] kWeakestRows(int[][] mat, int k) {
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> {  //索引：军人数量
            if (o1[1] != o2[1])
                return o1[1] - o2[1];  //数量升序
            else
                return o1[0] - o2[0];  //索引升序
        });
        for (int i = 0; i < mat.length; i++) {
            int counts = 0;
            for (int j = 0; j < mat[0].length; j++) {
                if (mat[i][j] == 0)
                    break;
                counts++;
            }
            sortedQueue.add(new int[]{i, counts});
        }
        int[] ans = new int[k];
        int pos = 0;
        while (!sortedQueue.isEmpty() && pos < k) {
            ans[pos] = sortedQueue.poll()[0];
            pos++;
        }
        return ans;
    }


    /**
     * 220. 存在重复元素 III
     */
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        int left = 0;
        int right = 0;
        TreeSet<Long> windowSet = new TreeSet<>();
        while (right < nums.length) {
            long temp = nums[right];
            if (right - left > k) {
                windowSet.remove((long) nums[left]);
                left++;
            }
            Long floor = windowSet.floor(temp);
            Long ceiling = windowSet.ceiling(temp);
            if (floor != null && temp - floor <= t) return true;
            if (ceiling != null && ceiling - temp <= t) return true;
            windowSet.add(temp);
            right++;
        }
        return false;
    }

    public boolean containsNearbyAlmostDuplicate01(int[] nums, int k, int t) {
        int left = 0;
        int right = 0;
        TreeSet<Long> windowSet = new TreeSet<>();
        while (right < nums.length) {
            long temp = nums[right];
            if (right - left > k) {
                windowSet.remove((long) nums[left]);
                left++;
            }
            Long floor = windowSet.floor(temp);
            Long ceiling = windowSet.ceiling(temp);
            if (floor != null && temp - floor <= t) return true;
            if (ceiling != null && ceiling - temp <= t) return true;
            windowSet.add(temp);
            right++;
        }
        return false;
    }


    /**
     * 1423. 可获得的最大点数
     */
    public int maxScore(int[] cardPoints, int k) {
        int left = 0;
        int right = 0;
        int window = cardPoints.length - k;
        int windowValue = 0;
        int minWindowValue = Integer.MAX_VALUE;
        while (right < cardPoints.length) {
            windowValue += cardPoints[right];
            if (right - left + 1 > window) {
                windowValue -= cardPoints[left];
                left++;
            }
            if (right - left + 1 == window)
                minWindowValue = Math.min(minWindowValue, windowValue);
            right++;
        }
        return Arrays.stream(cardPoints).sum() - minWindowValue;
    }


    public int maxScore01(int[] cardPoints, int k) {  //直观，但错误的写法
        int ans = 0;
        int left = 0;
        int right = cardPoints.length - 1;
        while (k > 0) {
            ans = ans + Math.max(cardPoints[left], cardPoints[right]);
            if (cardPoints[left] > cardPoints[right]) {
                left++;
            } else {
                right--;
            }
            k--;
        }
        return ans;
    }


    /**
     * 1408. 数组中的字符串匹配
     */
    public List<String> stringMatching(String[] words) {
        Arrays.sort(words, (o1, o2) -> o2.length() - o1.length());
        HashSet<String> disRes = new HashSet<>();
        for (int i = 0; i < words.length; i++) {
            for (int j = i + 1; j < words.length; j++) {
                if (words[i].contains(words[j]))
                    disRes.add(words[j]);
            }
        }
        return new ArrayList<>(disRes);
    }


    /**
     * 567. 字符串的排列
     */
    public boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) return false;
        int[] one = new int[26];
        int[] two = new int[26];
        int windowLen = s1.length();
        int left = 0;
        int right = 0;
        while (right < s2.length()) {
            if (right < s1.length())
                one[s1.charAt(right) - 'a']++;

            two[s2.charAt(right) - 'a']++;
            if (right - left + 1 > windowLen) {
                two[s2.charAt(left) - 'a']--;
                left++;
            }
            if (right - left + 1 == windowLen && Arrays.equals(one, two))
                return true;
            right++;
        }
        return false;
    }


    /**
     * 2190. 数组中紧跟 key 之后出现最频繁的数字
     */
    public int mostFrequent(int[] nums, int key) {
        int[][] counts = new int[1001][2];
        for (int i = 0; i < 1001; i++) {
            counts[i][0] = i;
        }
        for (int i = 1; i < nums.length; i++) {
            if (nums[i - 1] == key)
                counts[nums[i]][1]++;
        }
        Arrays.sort(counts, (o1, o2) -> o2[1] - o1[1]);  //基于频次排序
        return counts[0][0];
    }

    public int mostFrequent01(int[] nums, int key) {
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 1; i < nums.length; i++) {
            if (nums[i - 1] == key)
                hTable.put(nums[i], hTable.getOrDefault(nums[i], 0) + 1);
        }
        ArrayList<Map.Entry<Integer, Integer>> sorted = new ArrayList<>(hTable.entrySet());
        sorted.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        return sorted.get(0).getKey();
    }


    /**
     * 1004. 最大连续 1的个数 III
     */
    public int longestOnes(int[] nums, int k) {
        int left = 0;
        int right = 0;
        int maxWindow = 0;
        while (right < nums.length) {
            if (nums[right] == 0) {
                k--;  //消耗一次
            }
            while (k < 0) {  //只要有透支，就通过移动 left 回收，直至满足条件
                if (nums[left] == 0) {  //回收一次
                    k++;
                }
                left++;  //移动，开始回收
            }
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }

    public int longestOnes01(int[] nums, int k) {
        int left = 0;
        int right = 0;
        int maxWindow = 0;
        while (right < nums.length) {
            if (nums[right] == 0) {
                k--;  //消耗一次
            }
            if (k < 0) {  //透支一次
                if (nums[left] == 0) {  //回收一次
                    k++;
                }

                //-------------------------------
                // 共有两层含义：
                //    1、nums[left] == 0
                //         回收了一次可消耗次数，即 left位置变为原本的 0，此时需要 left右侧移动一位，因为其值部不为 1
                //    2、nums[left] == 1
                //         相当于衡量上一位时，窗口的长度
                //         因为 原本 nums[right] == 0，但透支消耗次数使其为 1，但左侧原本 nums[left]==1，无法抵消 k
                //         此时 left++的目的在于最终保证 window的长度是正确的，其代表上一个 window位置的长度
                //-------------------------------

                left++;
            }
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }


    /**
     * 1052. 爱生气的书店老板
     */
    public int maxSatisfied(int[] customers, int[] grumpy, int minutes) {
        int basePeople = 0;
        for (int i = 0; i < customers.length; i++) {
            if (grumpy[i] == 0) {
                basePeople += customers[i];
                customers[i] = 0;
            }
        }
        int maxWindowPeople = 0;
        int windowPeople = 0;
        int left = 0;
        int right = 0;
        while (right < customers.length) {
            windowPeople += customers[right];
            if (right - left + 1 > minutes) {
                windowPeople -= customers[left];
                left++;
            }
            if (right - left + 1 == minutes) {
                maxWindowPeople = Math.max(maxWindowPeople, windowPeople);
            }
            right++;
        }
        return basePeople + maxWindowPeople;
    }


    public int maxSatisfied01(int[] customers, int[] grumpy, int minutes) {  //错误示例
        int ans = 0;
        int left = 0;
        int right = 0;
        int maxPeople = 0;
        while (right < customers.length) {
            if (grumpy[right] == 0)
                ans += customers[right];
            while (right - left + 1 > minutes) {
                if (grumpy[left] == 0)
                    ans -= customers[left];
                left++;
            }
            if (right - left + 1 == minutes) {
                maxPeople = Math.max(maxPeople, ans);
            }
            right++;
        }
        return maxPeople;
    }

    /**
     * 1518. 换酒问题
     */
    public int numWaterBottles02(int numBottles, int numExchange) {
        int ans = 0;
        int bottles = 0;
        while (numBottles + bottles >= numExchange) {
            //每次喝一瓶酒，对各项指标的影响
            numBottles--;  //瓶装酒的个数
            bottles++;     //空瓶子的个数
            ans++;
            if (bottles / numExchange > 0) {  //满足空瓶换酒的条件
                numBottles += bottles / numExchange;  //更新当前酒水的个数
                bottles = bottles % numExchange;  //更新当前空瓶个数
            }
        }
        if (numBottles != 0) ans += numBottles;
        return ans;
    }


    /**
     * 1838. 最高频元素的频数
     */
    public int maxFrequency(int[] nums, int k) {
        int sum = 0;
        int left = 0;
        int right = 1;
        int maxWindow = 1;
        Arrays.sort(nums);  //升序
        while (right < nums.length) {
            int area = (nums[right] - nums[right - 1]) * (right - left + 1 - 1);
            sum += area;
            while (sum > k) {
                sum -= nums[right] - nums[left]; // left处填充 1 的次数
                left++;
            }
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }


    public int maxFrequency01(int[] nums, int k) {  //错误写法，太乱思路不清晰，应该有个 sum
        int left = 0;
        int right = 1;
        int maxWindow = 1;
        Arrays.sort(nums);  //升序
        while (right < nums.length) {
            int window = right - left + 1;
            int diff = nums[right] - nums[right - 1];
            int area = diff * (window - 1);
            while (area > k) {
                area = area - (nums[right] - nums[left]); // left处填充 1 的次数
                k += nums[right] - nums[left];
                left++;
            }
            k -= area;
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }


    /**
     * 3. 无重复字符的最长子串
     */
    public int lengthOfLongestSubstring(String s) {
        int left = 0;
        int right = 0;
        int maxWindow = 0;
        HashMap<Character, Integer> hTable = new HashMap<>();
        while (right < s.length()) {
            char current = s.charAt(right);
            if (hTable.containsKey(current)) {
                left = Math.max(left, hTable.get(current) + 1);
            }
            hTable.put(current, right);
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }

    /**
     * 643. 子数组最大平均数 I
     */
    public double findMaxAverage(int[] nums, int k) {
        int left = 0;
        int right = 0;
        double sum = 0;
        double maxAverage = Integer.MIN_VALUE;
        while (right < nums.length) {
            sum += nums[right];
            if (right - left + 1 > k) {
                sum -= nums[left];
                left++;
            }
            if (right - left + 1 == k)
                maxAverage = Math.max(maxAverage, sum / k);
            right++;
        }
        return maxAverage;
    }


    /**
     * 187. 重复的 DNA序列
     */
    public List<String> findRepeatedDnaSequences(String s) {
        int right = 0;
        HashMap<String, Integer> hTable = new HashMap<>();
        List<String> ans = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        while (right < s.length()) {
            builder.append(s.charAt(right));
            if (builder.length() > 10) {
                builder.deleteCharAt(0);
            }
            if (builder.length() == 10) {
                if (hTable.containsKey(builder.toString()) && hTable.get(builder.toString()) == 1) {
                    ans.add(builder.toString());
                }
                hTable.put(builder.toString(), hTable.getOrDefault(builder.toString(), 0) + 1);
            }
            right++;
        }
        return ans;
    }

    public List<String> findRepeatedDnaSequences01(String s) {
        int left = 0;
        int right = 0;
        int windowSum = 0;
        int windowLength = 10;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        HashMap<Character, Integer> charToNum = new HashMap<>();
        ArrayList<String> ans = new ArrayList<>();
        charToNum.put('A', 0); //00
        charToNum.put('C', 1); //01
        charToNum.put('G', 2); //10
        charToNum.put('T', 3); //11
        while (right < s.length()) {
            windowSum = (windowSum << 2) | charToNum.get(s.charAt(right));
            if (right - left + 1 > windowLength) {
                windowSum = windowSum & ((1 << (windowLength * 2)) - 1);  //位运算第一位索引为 0
                left++;
            }
            if (right - left + 1 == windowLength) {
                hTable.put(windowSum, hTable.getOrDefault(windowSum, 0) + 1);
                if (hTable.get(windowSum) == 2) {
                    ans.add(s.substring(left, left + windowLength));  //含头不含尾
                }
            }
            right++;
        }
        return ans;
    }


    /**
     * 424. 替换后的最长重复字符
     */
    public int characterReplacement(String s, int k) {
        int left = 0;
        int right = 0;
        int maxFreq = 0;
        int maxWindow = 0;
        char[] charAndNums = new char[26];
        for (int i = 0; i < s.length(); i++) {
            charAndNums[s.charAt(right) - 'A']++;
            maxFreq = Math.max(maxFreq, charAndNums[s.charAt(right) - 'A']);      //不用每次都排序，因为只需要知道频次即可，无需知道其对应的字符
            while (right - left + 1 > maxFreq + k) {
                charAndNums[s.charAt(left) - 'A']--;
                maxFreq = Math.max(maxFreq, charAndNums[s.charAt(left) - 'A']);
                left++;
            }
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }


    /**
     * 594. 最长和谐子序列
     */
    public int findLHS(int[] nums) {
        int left = 0;
        int right = 0;
        int maxWindow = 0;
        Arrays.sort(nums);
        while (right < nums.length) {
            while (left <= right && nums[right] - nums[left] > 1) {
                left++;
            }
            if (left <= right && nums[right] - nums[left] == 1)
                maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }

    public int findLHS01(int[] nums) {
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

    /**
     * 438. 找到字符串中所有字母异位词
     */
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> ans = new ArrayList<>();
        if (s.length() < p.length()) return ans;
        int[] target = new int[26];
        int[] temp = new int[26];
        for (int i = 0; i < p.length(); i++) {
            target[p.charAt(i) - 'a']++;
        }
        int left = 0;
        int right = 0;
        int window = p.length();
        while (right < s.length()) {
            temp[s.charAt(right) - 'a']++;
            if (right - left + 1 > window) {
                temp[s.charAt(left) - 'a']--;
                left++;
            }
            if (right - left + 1 == window) {
                if (Arrays.equals(temp, target))
                    ans.add(left);
            }
            right++;
        }
        return ans;
    }


    /**
     * 219. 存在重复元素 II
     */
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        int left = 0;
        int right = 0;
        HashSet<Integer> hSet = new HashSet<>();
        while (right < nums.length) {
            if (right - left > k) {
                hSet.remove(nums[left]);
                left++;
            }
            if (hSet.contains(nums[right]))
                return true;
            else
                hSet.add(nums[right]);
            right++;
        }
        return false;
    }

    /**
     * 1. 两数之和
     */
    public int[] twoSum(int[] nums, int target) {
        int[] ans = new int[2];
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (hTable.containsKey(target - nums[i]))
                return new int[]{hTable.get(target - nums[i]), i};
            hTable.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }


    public int[] twoSum01(int[] nums, int target) {  //错误写法
        int[] ans = new int[2];
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            hTable.put(nums[i], i);
        }
        for (int num : hTable.keySet()) {
            if (hTable.containsKey(target - num))
                return new int[]{hTable.get(num), hTable.get(target - num)};
        }
        return new int[]{-1, -1};
    }


    /**
     * 58. 最后一个单词的长度
     */
    public int lengthOfLastWord(String s) {
        String[] splitArray = s.split(" ");
        return splitArray[splitArray.length - 1].length();
    }

    /**
     * 剑指 Offer 58 - I. 翻转单词顺序
     */
    public String reverseWords(String s) {
        String[] splitArray = Arrays.stream(s.split(" "))
                .filter(o -> !o.equals("")).toArray(String[]::new);
        StringBuilder reverse = new StringBuilder();
        for (int i = splitArray.length - 1; i >= 0; i--) {
            reverse.append(splitArray[i]).append(" ");
        }
        return reverse.toString().trim();
    }


    public String reverseWords01(String s) {
        String trim = s.trim();
        List<String> splits = Arrays.asList(trim.split("\\s+"));
        Collections.reverse(splits);
        return String.join(" ", splits);
    }


    /**
     * 剑指 Offer 58 - II. 左旋转字符串
     */
    public String reverseLeftWords(String s, int n) {
        ArrayDeque<Character> deque = new ArrayDeque<>();
        char[] array = s.toCharArray();
        for (Character ca : array) {
            deque.addLast(ca);
        }
        while (n > 0) {
            if (!deque.isEmpty())
                deque.addLast(deque.pollFirst());
            n--;
        }
        StringBuilder builder = new StringBuilder();
        while (!deque.isEmpty()) {
            builder.append(deque.pollFirst());
        }
        return builder.toString();
    }


    /**
     * 14. 最长公共前缀
     */
    public String longestCommonPrefix(String[] strs) {
        Arrays.sort(strs, (o1, o2) -> o1.length() - o2.length());
        StringBuilder shortWord = new StringBuilder();
        shortWord.append(strs[0]);
        while (shortWord.length() > 0) {
            for (int i = 0; i < strs.length; i++) {  // 0
                if (!strs[i].startsWith(String.valueOf(shortWord))) {
                    shortWord.deleteCharAt(shortWord.length() - 1);
                    break;
                }
                if (i == strs.length - 1)
                    return shortWord.toString();
            }
        }
        return "";
    }


    public String longestCommonPrefix01(String[] strs) {
        String commonPrefix = strs[0];
        for (int i = 1; i < strs.length; i++) {
            commonPrefix = longestCommonPrefix01(commonPrefix, strs[i]);
            if (commonPrefix.equals(""))
                return "";
        }
        return commonPrefix;
    }

    private String longestCommonPrefix01(String str1, String str2) {
        StringBuilder commonBuilder = new StringBuilder();
        for (int i = 0; i < Math.min(str1.length(), str2.length()); i++) {
            if (str1.charAt(i) == str2.charAt(i))
                commonBuilder.append(str1.charAt(i));
            else
                break;
        }
        return commonBuilder.toString();
    }


    /**
     * 38. 外观数列
     */
    public String countAndSay(int n) {
        String current = "1";
        for (int i = 2; i <= n; i++) {
            current = countAndSayByBefore(current);
        }
        return current;
    }

    private String countAndSayByBefore(String current) {
        if (current.length() == 1) return "1" + current;
        StringBuilder builder = new StringBuilder();
        char cuca = current.charAt(0);
        int freq = 1;
        for (int i = 1; i < current.length(); i++) {
            if (current.charAt(i) == cuca) {
                freq++;
            } else {
                builder.append(freq).append(cuca);  //添加元素
                cuca = current.charAt(i);
                freq = 1;
            }
        }
        builder.append(freq).append(cuca);
        return builder.toString();
    }


    /**
     * 66. 加一
     */
    public int[] plusOne(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
            if (digits[i] == 9) {
                digits[i] = 0;
            } else {
                digits[i]++;
                return digits;
            }
        }
        int[] target = new int[digits.length + 1];
        target[0]++;
        return target;
    }


    /**
     * 268. 丢失的数字
     */
    public int missingNumber(int[] nums) {
        Arrays.sort(nums);
        int current = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i)
                return i;
        }
        return nums.length;
    }


    /**
     * 383. 赎金信
     */
    public boolean canConstruct(String ransomNote, String magazine) {  //判断 ransomNote 能不能由 magazine 里面的字符构成
        int[] ransomArray = new int[26];
        int[] magazineArray = new int[26];
        for (int i = 0; i < ransomNote.length(); i++) {
            ransomArray[ransomNote.charAt(i) - 'a']++;
        }
        for (int i = 0; i < magazine.length(); i++) {
            magazineArray[magazine.charAt(i) - 'a']++;
        }
        for (int i = 0; i < 26; i++) {
            if (ransomArray[i] > magazineArray[i])  //目标少，审查多
                return false;
        }
        return true;
    }

    public boolean canConstruct01(String ransomNote, String magazine) {  //判断 ransomNote 能不能由 magazine 里面的字符构成
        int[] counts = new int[26];
        for (int i = 0; i < magazine.length(); i++) {
            counts[magazine.charAt(i) - 'a']++;
        }
        for (int i = 0; i < ransomNote.length(); i++) {   //共用一个 数组，进行累加，与 0 对比
            counts[ransomNote.charAt(i) - 'a']--;
            if (counts[ransomNote.charAt(i) - 'a'] < 0)
                return false;
        }
        return true;
    }


    public boolean canConstruct02(String ransomNote, String magazine) { //思路就是错的，不能排序
        //判断 ransomNote 能不能由 magazine 里面的字符构成
        char[] ransomArray = ransomNote.toCharArray();
        char[] magazineArray = magazine.toCharArray();
        Arrays.sort(ransomArray);
        Arrays.sort(magazineArray);
        return String.valueOf(magazineArray).contains(String.valueOf(ransomArray));
    }


    /**
     * 6. Z 字形变换
     */
    public String convert(String s, int numRows) {
        if (numRows == 1) return s;
        Character[][] matrix = new Character[numRows][s.length()];
        int rows = 0;
        int columns = 0;
        int pos = 0;
        while (pos < s.length()) {
            //自上而下
            while (rows < numRows) {
                char c = s.charAt(pos);
                matrix[rows][columns] = s.charAt(pos);
                pos++;
                if (pos == s.length())
                    break;
                rows++;
            }
            if (pos == s.length())
                break;
            rows -= 2;   //反向跳一步
            columns++;

            //左下到右下
            while (rows >= 0) {
                matrix[rows][columns] = s.charAt(pos);
                pos++;
                if (pos == s.length())
                    break;
                rows--;
                columns++;
            }
            if (pos == s.length())
                break;
            rows += 2;
            columns--;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == null)
                    continue;
                builder.append(matrix[i][j]);
            }
        }
        return builder.toString();
    }


    public String convert01(String s, int numRows) {
        if (numRows == 1) return s;
        Character[][] matrix = new Character[numRows][s.length()];
        int rows = 0;
        int columns = 0;
        int pos = 0;
        while (pos < s.length()) {
            //自上而下
            while (rows < numRows && pos < s.length()) {
                char c = s.charAt(pos);
                matrix[rows][columns] = s.charAt(pos);
                pos++;
                rows++;
            }
            rows -= 2;   //反向跳一步
            columns++;

            //左下到右下
            while (rows >= 0 && pos < s.length()) {
                matrix[rows][columns] = s.charAt(pos);
                pos++;
                rows--;
                columns++;
            }
            rows += 2;
            columns--;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == null)
                    continue;
                builder.append(matrix[i][j]);
            }
        }
        return builder.toString();
    }


    /**
     * 73. 矩阵置零
     */
    public void setZeroes(int[][] matrix) {
        ArrayList<Integer> rows = new ArrayList<>();
        ArrayList<Integer> columns = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rows.add(i);
                    columns.add(j);
                }
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (rows.contains(i) || columns.contains(j))
                    matrix[i][j] = 0;
            }
        }
    }


    /**
     * 434. 字符串中的单词数
     */
    public int countSegments(String s) {
        return Arrays.stream(s.split(" ")).filter(o -> !o.equals("")).toArray(String[]::new).length;
    }


    /**
     * 168. Excel表列名称
     */
    public String convertToTitle(int columnNumber) {
        StringBuilder builder = new StringBuilder();
        while (columnNumber > 0) {
            columnNumber--;
            builder.append((char) (columnNumber % 26 + 'A'));
            columnNumber = columnNumber / 26;
        }
        return builder.reverse().toString();
    }


    /**
     * 165. 比较版本号
     */
    public int compareVersion(String version1, String version2) {
        int[] convert1 = Arrays.stream(version1.split("\\.")).mapToInt(Integer::valueOf).toArray();
        int[] convert2 = Arrays.stream(version2.split("\\.")).mapToInt(Integer::valueOf).toArray();
        int maxLength = Math.max(convert1.length, convert2.length);
        int[] current1 = new int[maxLength];
        int[] current2 = new int[maxLength];
        System.arraycopy(convert1, 0, current1, 0, convert1.length);
        System.arraycopy(convert2, 0, current2, 0, convert2.length);
        for (int i = 0; i < maxLength; i++) {
            if (current1[i] > current2[i])
                return 1;
            if (current1[i] < current2[i])
                return -1;
        }
        return 0;
    }


    public int compareVersion01(String version1, String version2) {
        int[] convert1 = Arrays.stream(version1.split("\\.")).mapToInt(Integer::valueOf).toArray();
        int[] convert2 = Arrays.stream(version2.split("\\.")).mapToInt(Integer::valueOf).toArray();
        int maxLength = Math.max(convert1.length, convert2.length);
        for (int i = 0; i < maxLength; i++) {
            int var1 = 0;
            int var2 = 0;
            if (i < convert1.length)
                var1 = convert1[i];
            if (i < convert2.length)
                var2 = convert2[i];

            if (var1 > var2)
                return 1;
            if (var1 < var2)
                return -1;
        }
        return 0;
    }


    /**
     * 495. 提莫攻击
     */
    public int findPoisonedDuration(int[] timeSeries, int duration) {
        int left = 0;
        int right = -1;  //关键在于 timeSeries[0] 会有等于 0的情况，初始为 0 的点应该算上，所以应该置为 -1
        int sumTimes = 0;
        for (int i = 0; i < timeSeries.length; i++) {
            //更新新一轮中毒区间的左边界，基于当前中毒的开始时间，以及上次中毒的结束时间
            if (timeSeries[i] <= right)  //计算sumTimes的话，会算重复
                sumTimes--;
            left = Math.max(timeSeries[i], right);
            right = timeSeries[i] + duration - 1;
            sumTimes += right - left + 1;
        }
        return sumTimes;
    }

    public int findPoisonedDuration03(int[] timeSeries, int duration) {
        int sumTimes = 0;
        for (int i = 1; i < timeSeries.length; i++) {
            sumTimes += Math.min(timeSeries[i] - timeSeries[i - 1], duration);
        }
        return sumTimes + duration;
    }

    public int findPoisonedDuration01(int[] timeSeries, int duration) {  //暴力超时
        int lastTime = timeSeries[timeSeries.length - 1];
        int[] attack = new int[lastTime + duration];
        for (int time : timeSeries) {
            int current = time;
            while (current <= time + duration - 1) {
                attack[current] = 1;
                current++;
            }
        }
        return (int) Arrays.stream(attack).filter(o -> o != 0).count();
    }

    /**
     * 299. 猜数字游戏
     */
    public String getHint(String secret, String guess) {
        int x = 0;
        int y = 0;
        int[] secretArray = new int[10];
        int[] guessArray = new int[10];
        for (int i = 0; i < secret.length(); i++) {
            if (secret.charAt(i) == guess.charAt(i)) {
                x++;
            } else {
                secretArray[secret.charAt(i) - '0']++;
                guessArray[guess.charAt(i) - '0']++;
            }
        }
        for (int i = 0; i < 10; i++) {
            y += Math.min(secretArray[i], guessArray[i]);
        }
        return x + "A" + y + "B";
    }


    /**
     * 54. 螺旋矩阵
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        ArrayList<Integer> ans = new ArrayList<>();
        //坐标点
        int rows = 0;
        int cols = 0;
        //四个边界
        int left = 0;                      //列：左边界
        int right = matrix[0].length - 1;  //列：右边界
        int top = 0;                       //行：上边界
        int bottom = matrix.length - 1;    //行：下边界
        int times = matrix[0].length * matrix.length;  //元素总个数
        int current = 1; //当前元素个数
        while (current <= times) {
            //从左往右
            while (cols <= right && current <= times) {
                ans.add(matrix[rows][cols]);
                cols++;
                current++;
            }
            cols--; //列：左移（回退）
            rows++; //行：下移
            top++;  //上边界：行下移

            //从上往下
            while (rows <= bottom && current <= times) {
                ans.add(matrix[rows][cols]);
                rows++;
                current++;
            }
            rows--;  //行：上移
            cols--;  //列：左移
            right--; //右边界：列左移

            //从右往左
            while (cols >= left && current <= times) {
                ans.add(matrix[rows][cols]);
                cols--;
                current++;
            }
            cols++;   //列：右移
            rows--;   //行：上移
            bottom--; //下边界：行上移

            //从下往上
            while (rows >= top && current <= times) {
                ans.add(matrix[rows][cols]);
                rows--;
                current++;
            }
            rows++;   //行：下移
            cols++;   //列：右移
            left++;   //左边界：列右移
        }
        return ans;
    }


    /**
     * 492. 构造矩形
     */
    public int[] constructRectangle(int area) {
        int sqrt = (int) Math.sqrt(area);
        for (int i = sqrt; i <= area; i++) {
            if (area % i == 0) { //可整除
                int m = area / i;
                return m > i ? new int[]{m, i} : new int[]{i, m};
            }
        }
        return new int[]{-1, -1};
    }


    /**
     * 31. 下一个排列
     */
    public void nextPermutation(int[] nums) {

    }


    /**
     * 747. 至少是其他数字两倍的最大数
     */
    public int dominantIndex(int[] nums) {
        if (nums.length == 1) return 0;
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o2[0] - o1[0]);
        for (int i = 0; i < nums.length; i++) {
            sortedQueue.add(new int[]{nums[i], i});
        }
        int[] max = sortedQueue.poll();
        int[] next = sortedQueue.poll();
        return max[0] >= next[0] * 2 ? max[1] : -1;
    }


    /**
     * 541. 反转字符串 II
     */
    public String reverseStr(String s, int k) {
        StringBuilder ans = new StringBuilder();
        int current = 0;
        while (current < s.length()) {
            int t = k;
            StringBuilder oneBuilder = new StringBuilder();
            while (t > 0 && current < s.length()) {
                oneBuilder.append(s.charAt(current));
                current++;
                t--;
            }
            ans.append(oneBuilder.reverse());
            if (current == s.length())
                break;
            int m = k;
            StringBuilder twoBuilder = new StringBuilder();
            while (m > 0 && current < s.length()) {
                twoBuilder.append(s.charAt(current));
                current++;
                m--;
            }
            ans.append(twoBuilder);
        }
        return ans.toString();
    }

    public String reverseStr01(String s, int k) {    //善于使用 Math.min()
        int pos = 0;
        char[] array = s.toCharArray();
        while (pos < s.length()) {
            //奇数位
            reverseStr(array, pos, Math.min(pos + k - 1, s.length() - 1));
            //偶数位
            pos += 2 * k;  //包括了奇数位的位置，直接跳过
        }
        return String.valueOf(array);
    }

    private void reverseStr(char[] array, int left, int right) {
        while (left < right) {
            char temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
    }


    /**
     * 551. 学生出勤记录 I
     */
    public boolean checkRecord(String s) {
        if (s.contains("LLL")) return false;
        int countA = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'A')
                countA++;
            if (countA == 2)
                return false;
        }
        return true;
    }


    /**
     * 2259. 移除指定数字得到的最大结果
     */
    public String removeDigit(String number, char digit) {
        int right = 0;
        while (right < number.length() - 1) {
            if (number.charAt(right) == digit && number.charAt(right) < number.charAt(right + 1)) {
                return number.substring(0, right) + number.substring(right + 1);
            }
            right++;
        }
        int last = number.lastIndexOf(digit);
        return number.substring(0, last) + number.substring(last + 1);
    }

    public String removeDigit01(String number, char digit) {  //错误写法
        ArrayList<Long> ans = new ArrayList<>();
        for (int i = 0; i < number.length(); i++) {
            if (number.charAt(i) == digit) {
                ans.add(Long.parseLong(number.substring(0, i) + number.substring(i + 1)));
            }
        }
        ans.sort((o1, o2) -> (int) (o2 - o1));
        return String.valueOf(ans.get(0));
    }

    /**
     * 1583. 统计不开心的朋友
     */
    public int unhappyFriends(int n, int[][] preferences, int[][] pairs) {
        int ans = 0;
        int[][] close_level = new int[n][n];
        int[] match = new int[n];
        //----------------------------------------------------------------------------------
        // 将 preferences二级索引与 value互换，从而通过 close_level[x][y] 直接获取其亲密程度的值
        //----------------------------------------------------------------------------------
        for (int i = 0; i < preferences.length; i++) {
            for (int j = 0; j < preferences[0].length; j++) {
                // 值越小，二者越亲密
                close_level[i][preferences[i][j]] = j;  //互换 二级索引 与 value
            }
        }
        //-------------------------------------------------------------------------------------------------
        // 基于 pairs获取 match，从而快速获取搭档关系，因为 pairs一级索引无意义、二级索引与 value也仅是单向，不方便
        //-------------------------------------------------------------------------------------------------
        for (int i = 0; i < pairs.length; i++) {
            for (int j = 0; j < pairs[0].length; j++) {
                match[pairs[i][0]] = pairs[i][1];
                match[pairs[i][1]] = pairs[i][0];
            }
        }
        //---------------------------------------------------------------------------
        // 上面仅仅是数据预处理，方便获取对应的数据，同时可以基于 match来逐一验证搭配的情况
        //---------------------------------------------------------------------------
        for (int x = 0; x < match.length; x++) {  //逐一验证每个人"x"是否是不开心的
            int y = match[x];  //获取当前 x 的搭档 y
            int maxClose = close_level[x][y];  //获取 x 对 y 的亲密程度
            for (int close = 0; close < maxClose; close++) { //基于递减的亲密程度，逐个获取相对于 y来讲，x更亲密的朋友 u
                //------------------------------------------------
                // 对于 x 来讲，u 比 y更受欢迎，剩下校验 u 的搭配情况
                //------------------------------------------------
                int u = preferences[x][close]; //
                int v = match[u];
                if (close_level[u][x] < close_level[u][v]) {
                    ans++;
                    break;
                }
            }
        }
        return ans;
    }


    /**
     * 231. 2 的幂
     */
    public boolean isPowerOfTwo(int n) {
        return n > 0 && ((n & (-n)) == n);
    }

    public boolean isPowerOfTwo01(int n) {
        return n > 0 && ((n & (n - 1)) == 0);
    }

    public boolean isPowerOfTwo02(int n) {
        int res = 0;
        if (n > 0) {
            for (int i = 0; i < 32; i++) {
                if (((n >> i) & 1) == 1)
                    res++;
                if (res == 2)
                    return false;
            }
        }
        return res == 1;
    }


    /**
     * 342. 4的幂
     */
    public boolean isPowerOfFour(int n) {
        int ans = 0; //偶数位 1 的个数
        if (n > 0) {
            for (int i = 0; i < 32; i++) {
                if ((i & 1) == 1) {  //奇数
                    if (((n >> i) & 1) == 1)
                        return false;
                } else {
                    if (((n >> i) & 1) == 1)
                        ans++;
                    if (ans == 2)
                        return false;
                }
            }
        }
        return ans == 1;
    }


    /**
     * 191. 位1的个数
     */
    public int hammingWeight(int n) {
        int ans = 0;
        while (n != 0) {
            ans += n & 1;  //二进制右移，最低位与 1 比较
            n >>>= 1;
        }
        return ans;
    }

    public int hammingWeight01(int n) {  // 1 左侧移动，与二进制对应位比较
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            if ((n & (1 << i)) != 0)
                ans++;
        }
        return ans;
    }


    public int hammingWeight02(int n) {
        int ans = 0;
        while (n != 0) {
            n = n & (n - 1);  //消除最低位的 1
            ans++;
        }
        return ans;
    }


    /**
     * 461. 汉明距离
     */
    public int hammingDistance(int x, int y) {
        int ans = 0;
        int m = x ^ y;
        for (int i = 0; i < 32; i++) {
            if (((1 << i) & m) != 0)  //左移 1，与二进制的对应位比较，并计数
                ans++;
        }
        return ans;
    }

    public int hammingDistance01(int x, int y) {
        int ans = 0;
        int m = x ^ y;
        for (int i = 0; i < 32; i++) {
            ans += m & 1;  //右移二进制，每次二进制最低位 与 1 比较，并计数
            m >>>= 1;
        }
        return ans;
    }

    public int hammingDistance02(int x, int y) {
        int ans = 0;
        int m = x ^ y;
        while (m != 0) {
            m = m & (m - 1); //寻找并消除最低位 1，计数
            ans++;
        }
        return ans;
    }

    public int hammingDistance03(int x, int y) {
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            int xx = (x >>> i) & 1;
            int yy = (y >>> i) & 1;
            ans += xx ^ yy;
        }
        return ans;
    }


    /**
     * 477. 汉明距离总和
     */
    public int totalHammingDistance(int[] nums) {
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            int xx = 0;
            int yy = 0;
            for (int num : nums) {  //逐个对比所有数字的同一位的值
                if (((num >> i) & 1) == 1)
                    xx++;
                else
                    yy++;
            }
            ans += xx * yy;
        }
        return ans;
    }

    public int totalHammingDistance01(int[] nums) {
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            int m = 0;
            for (int num : nums) {  //逐个对比所有数字的同一位的值
                m += (num >> i) & 1;
            }
            ans += m * (nums.length - m);
        }
        return ans;
    }


    public int totalHammingDistance02(int[] nums) {
        int ans = 0;
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                ans += hammingDistance(nums[i], nums[j]);
            }
        }
        return ans;
    }


    /**
     * 137. 只出现一次的数字 II
     */
    public int singleNumber(int[] nums) {
        int[] res = new int[32];
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            for (int num : nums) {
                res[i] += (num >>> i) & 1;
            }
        }
        for (int i = 0; i < 32; i++) {
            if (res[i] % 3 == 1)
                ans |= 1 << i;
        }
        return ans;
    }

    public int singleNumber01(int[] nums) {
        int[] res = new int[32];
        int ans = 0;
        for (int num : nums) {
            int t = 0;
            while (num != 0) {
                res[t] += num & 1;
                num >>>= 1;
                t++;
            }
        }
        for (int i = 0; i < 32; i++) {
            if ((res[i] % 3) == 1)
                ans |= 1 << i;
        }
        return ans;
    }


    /**
     * 268. 丢失的数字
     */
    public int missingNumber01(int[] nums) {
        int n = nums.length;
        int XOR = 0;
        for (int i = 0; i < n; i++) {
            XOR ^= i;
            XOR ^= nums[i];
        }
        XOR ^= n;
        return XOR;
    }


    /**
     * 136. 只出现一次的数字
     */
    public int singleNumberI(int[] nums) {  // 对比 题目137
        int XOR = 0;
        for (int num : nums)
            XOR ^= num;
        return XOR;
    }


    /**
     * 260. 只出现一次的数字 III
     */
    public int[] singleNumberIII(int[] nums) {
        int[] target = new int[2];
        int res = 0;
        for (int num : nums)
            res ^= num;
        int XOR = res == Integer.MIN_VALUE ? res : res & (-res);  //获取最低位 1 的二进制表示
        //基于 XOR 分流分治
        for (int num : nums) {
            if ((num & XOR) == 0)
                target[0] ^= num;
            else
                target[1] ^= num;
        }
        return target;
    }

    public int[] singleNumberIII01(int[] nums) {
        int[] target = new int[2];
        int res = 0;
        for (int num : nums)
            res ^= num;
        int k = 0;
        while ((res & 1) == 0) {
            res >>>= 1;
            k++;
        }
        for (int num : nums) {
            if ((num & (1 << k)) == 0) {
                target[0] ^= num;
            } else {
                target[1] ^= num;
            }
        }
        return target;
    }


    /**
     * 49. 字母异位词分组
     */
    public List<List<String>> groupAnagrams(String[] strs) { //错误写法
        List<List<String>> ans = new ArrayList<>();
        HashMap<String, ArrayList<String>> hTable = new HashMap<>();
        for (String word : strs) {
            char[] array = word.toCharArray();
            Arrays.sort(array);
            String key = String.valueOf(array);
            ArrayList<String> value = hTable.getOrDefault(key, new ArrayList<>());
            value.add(word);
            hTable.put(key, value);
        }
        return new ArrayList<>(hTable.values());
    }


    public List<List<String>> groupAnagrams01(String[] strs) { //错误写法
        List<List<String>> ans = new ArrayList<>();
        HashMap<int[], ArrayList<String>> hTable = new HashMap<>();
        for (String word : strs) {
            char[] wordArray = word.toCharArray();
            int[] temp = new int[26];
            for (char ca : wordArray) {
                temp[ca - 'a']++;
            }
            if (hTable.containsKey(temp)) {   //无法捕获已有数组
                ArrayList<String> strings = hTable.get(temp);
                strings.add(word);
                hTable.put(temp, strings);
            } else {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(word);
                hTable.put(temp, arrayList);
            }
        }
        for (Map.Entry<int[], ArrayList<String>> tuple : hTable.entrySet()) {
            ans.add(tuple.getValue());
        }
        return ans;
    }


    public List<List<String>> groupAnagrams02(String[] strs) { //错误写法的改进
        List<List<String>> ans = new ArrayList<>();
        HashMap<String, ArrayList<String>> hTable = new HashMap<>();
        for (String word : strs) {
            char[] wordArray = word.toCharArray();
            int[] temp = new int[26];
            for (char ca : wordArray) {
                temp[ca - 'a']++;
            }
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 26; i++) {
                if (temp[i] != 0)
                    builder.append((char) (i + 'a')).append(temp[i]);  //a2的形式
            }
            String key = builder.toString();
            ArrayList<String> value = hTable.getOrDefault(key, new ArrayList<>());
            value.add(word);
            hTable.put(key, value);
        }
        return new ArrayList<>(hTable.values());
    }


    /**
     * 56. 合并区间
     */
    public int[][] merge(int[][] intervals) {
        if (intervals.length == 1)
            return intervals;
        ArrayList<int[]> ans = new ArrayList<>();
        Arrays.sort(intervals, (o1, o2) -> {
            if (o1[0] != o2[0]) {
                return o1[0] - o2[0];
            }
            return o1[1] - o2[1];
        });
        for (int i = 0; i < intervals.length - 1; i++) {
            if (intervals[i][1] < intervals[i + 1][0]) {
                ans.add(intervals[i]);    //无重叠
                if (i == intervals.length - 2)
                    ans.add(intervals[i + 1]);
            } else {  //有重叠
                int[] overlapping = {intervals[i][0], Math.max(intervals[i][1], intervals[i + 1][1])};
                intervals[i] = new int[]{-1, -1};
                intervals[i + 1] = overlapping; //更新
            }
            //忽略 [2,6] 和 [3,5] 双边重叠的情况
        }
        int count = (int) Arrays.stream(intervals).filter(o -> o[0] != -1).count();
        int[][] res = new int[count][2];
        int curr = 0;
        for (int[] interval : intervals) {
            if (interval[0] != -1) {
                res[curr] = interval;
                curr++;
            }
        }
        return res;
    }

    public int[][] merge01(int[][] intervals) {
        if (intervals.length == 1)
            return intervals;
        ArrayList<int[]> ans = new ArrayList<>();
        Arrays.sort(intervals, (o1, o2) -> o1[0] - o2[0]);
        for (int[] interval : intervals) {
            if (ans.size() == 0 || ans.get(ans.size() - 1)[1] < interval[0]) {  //无重叠
                ans.add(interval);
            } else { //有重叠
                int[] last = ans.get(ans.size() - 1);
                last[1] = Math.max(last[1], interval[1]);
                ans.remove(ans.size() - 1);  //更新
                ans.add(last);
            }
        }
        return ans.toArray(new int[ans.size()][]);
    }

    /**
     * 57. 插入区间
     */
    public int[][] insert(int[][] intervals, int[] newInterval) {
        ArrayList<int[]> ans = new ArrayList<>();
        int len = intervals.length;
        int i = 0;
        //不重叠的情况
        while (i < len && intervals[i][1] < newInterval[0]) { //newInterval区间 均处于 intervals[i] 的右侧
            ans.add(intervals[i]);
            i++;
        }
        //重叠的情况
        while (i < len && intervals[i][0] <= newInterval[1]) { //横跨多个，newInterval逐渐扩展（更新）区间
            newInterval[0] = Math.min(intervals[i][0], newInterval[0]);
            newInterval[1] = Math.max(intervals[i][1], newInterval[1]);
            i++;
        }
        //添加最终的重叠区间
        ans.add(newInterval);
        //后续逐一添加，不会有重叠
        for (int m = i; m < len; m++) {
            ans.add(intervals[m]);
        }
        return ans.toArray(new int[ans.size()][]);
    }


    public int[][] insert01(int[][] intervals, int[] newInterval) {
        ArrayList<int[]> ans = new ArrayList<>();
        if (intervals.length == 0) return new int[][]{newInterval};
        boolean neverUsed = true;
        for (int i = 0; i < intervals.length; i++) {
            if (neverUsed) {
                if (intervals[i][1] < newInterval[0])  //1、interval[i] 完全在 newInterval左侧
                    ans.add(intervals[i]);
                else {
                    if (intervals[i][0] <= newInterval[1]) {  //2、interval[i] 与 newInterval有交集
                        int left = Math.min(intervals[i][0], newInterval[0]);
                        int right = Math.max(intervals[i][1], newInterval[1]); //合并
                        ans.add(new int[]{left, right});
                    } else {                               //3、interval[i] 完全在 newInterval 右侧
                        ans.add(newInterval); //均添加
                        ans.add(intervals[i]);
                    }
                    neverUsed = false;  //newInterval已经合并使用，但合并结果可能会与后续的重叠，以后均执行以下分支
                }
            } else {
                if (ans.get(ans.size() - 1)[1] < intervals[i][0]) { //ans中最后的数组区间，与 intervals[i] 无重叠
                    ans.add(intervals[i]);
                } else { //有重叠
                    int[] last = ans.get(ans.size() - 1);
                    last[1] = Math.max(last[1], intervals[i][1]);
                    ans.remove(ans.size() - 1);
                    ans.add(last);
                }
            }
        }
        if (neverUsed) ans.add(newInterval);  //newInterval在区间右侧，从未被使用，此时应该更新
        return ans.toArray(new int[ans.size()][]);
    }


    public int[][] insert02(int[][] intervals, int[] newInterval) {
        ArrayList<int[]> ans = new ArrayList<>();
        if (intervals.length == 0) return new int[][]{newInterval};
        for (int i = 0; i < intervals.length; i++) {
            if (newInterval[0] != -2) {
                if (intervals[i][1] < newInterval[0])  // 判断是否进行 interval[i] 与 newInterval合并
                    ans.add(intervals[i]);
                else {
                    if (intervals[i][0] <= newInterval[1]) {  // interval[i] 与 newInterval有交集
                        int left = Math.min(intervals[i][0], newInterval[0]);
                        int right = Math.max(intervals[i][1], newInterval[1]);
                        ans.add(new int[]{left, right});
                    } else {
                        ans.add(newInterval);
                        ans.add(intervals[i]);
                    }
                    newInterval[0] = -2;   //.......上面已经插入，但此处更新后，ans中的也会更新，注意.......
                }
            } else {
                if (ans.get(ans.size() - 1)[1] < intervals[i][0]) { //无重叠
                    ans.add(intervals[i]);
                } else { //有重叠
                    int[] last = ans.get(ans.size() - 1);
                    last[1] = Math.max(last[1], intervals[i][1]);
                    ans.remove(ans.size() - 1);
                    ans.add(last);
                }
            }
        }
        return ans.toArray(new int[ans.size()][]);
    }


    /**
     * 1711. 大餐计数
     */
    public int countPairs(int[] deliciousness) {
        long ans = 0;
        int mod = (int) Math.pow(10, 9) + 7;  //不能写为 (1 << 9) + 7，这个是 2 的 9次幂，而非 10的 9次幂
        int sumTarget = 1 << 21;   //大餐和的最大值，2的21次幂
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int food : deliciousness) {
            for (int sum = 1; sum <= sumTarget; sum <<= 1) {
                Integer add = hTable.getOrDefault(sum - food, 0);
                ans = (ans + add) % mod;
            }
            hTable.put(food, hTable.getOrDefault(food, 0) + 1);
        }
        return (int) ans;
    }


    /**
     * 405. 数字转换为十六进制数
     */
    public String toHex(int num) {
        if (num == 0)
            return "0";
        StringBuilder ans = new StringBuilder();
        while (num != 0) {
            int ii = num & 0xf;  //除以 16取余，针对正负数均可处理
            if (ii < 10)
                ans.append(ii);
            else
                ans.append((char) (ii - 10 + 'a'));
            num >>>= 4;  //无符号右移动
        }
        return ans.reverse().toString();
    }

    public String toHex01(int num) {
        if (num == 0)
            return "0";
        StringBuilder ans = new StringBuilder();
        long numLong = num < 0 ? (long) (num + Math.pow(2, 32)) : num;  //正负数统一处理
        char[] change = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        while (numLong != 0) {
            ans.append(change[(int) (numLong % 16)]);
            numLong = numLong / 16;
        }
        return ans.reverse().toString();
    }


    /**
     * 191. 位 1的个数
     */
    public int hammingWeight10(int n) {
        int count = 0;
        for (int i = 0; i < 32; i++) {
            count += (n >> i) & 1;  //右移
        }
        return count;
    }

    public int hammingWeight11(int n) {
        int count = 0;
        while (n != 0) {
            count += n & 1;
            n >>>= 1;    //右移
        }
        return count;
    }

    public int hammingWeight12(int n) {
        int count = 0;
        for (int i = 0; i < 32; i++) {
            if (((n & (1 << i)) != 0))  //左移
                count++;
        }
        return count;
    }

    public int hammingWeight13(int n) {
        int count = 0;
        while (n != 0) {
            n = n & (n - 1);
            count++;
        }
        return count;
    }


    /**
     * 318. 最大单词长度乘积
     */
    public int maxProduct(String[] words) {
        int ans = 0;
        int[] maskBits = new int[words.length];
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            for (int j = 0; j < word.length(); j++) {
                maskBits[i] |= (1 << (word.charAt(j) - 'a'));
            }
        }
        for (int i = 0; i < words.length; i++) {
            for (int j = i + 1; j < words.length; j++) {
                if ((maskBits[i] & maskBits[j]) == 0)
                    ans = Math.max(ans, words[i].length() * words[j].length());
            }
        }
        return ans;
    }


    /**
     * 187. 重复的 DNA序列
     */
    public List<String> findRepeatedDnaSequences02(String s) {
        List<String> ans = new ArrayList<>();
        HashMap<String, Integer> hTable = new HashMap<>();
        int left = 0;
        int right = 0;
        int windowLen = 10;
        while (right < s.length()) {
            while (right - left + 1 > windowLen) {
                left++;
            }
            if (right - left + 1 == windowLen) {
                String window = s.substring(left, right + 1);
                hTable.put(window, hTable.getOrDefault(window, 0) + 1);
                if (hTable.get(window) == 2)
                    ans.add(window);
            }
            right++;
        }
        return ans;
    }


    public List<String> findRepeatedDnaSequences03(String s) {
        List<String> ans = new ArrayList<>();
        HashMap<Character, Integer> hTable = new HashMap<>();
        hTable.put('A', 1); //001
        hTable.put('C', 2); //010
        hTable.put('G', 3); //011
        hTable.put('T', 4); //100
        int left = 0;
        int right = 0;
        int windowLen = 10;
        int sum = 0;
        HashMap<Integer, Integer> sumAndTimes = new HashMap<>();
        while (right < s.length()) {
            sum = (sum << 3) | hTable.get(s.charAt(right));
            if (right - left + 1 > windowLen) {
                sum = sum & ((1 << (windowLen * 3)) - 1);
                left++;
            }
            if (right - left + 1 == windowLen) {
                sumAndTimes.put(sum, sumAndTimes.getOrDefault(sum, 0) + 1);
                if (sumAndTimes.get(sum) == 2)
                    ans.add(s.substring(left, right + 1));
            }
            right++;
        }
        return ans;
    }


    /**
     * 36. 有效的数独
     */
    public boolean isValidSudoku(char[][] board) {
        int[][] row = new int[9][10];
        int[][] col = new int[9][10];
        int[][][] area = new int[3][3][10];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != '.') {
                    if (row[i][board[i][j] - '0'] == 1)
                        return false;
                    if (col[j][board[i][j] - '0'] == 1)
                        return false;
                    if (area[i / 3][j / 3][board[i][j] - '0'] == 1)
                        return false;
                    row[i][board[i][j] - '0'] = 1;
                    col[j][board[i][j] - '0'] = 1;
                    area[i / 3][j / 3][board[i][j] - '0'] = 1;
                }
            }
        }
        return true;
    }


    /**
     * 36. 有效的数独
     */
    public boolean isValidSudoku01(char[][] board) {
        int[] row = new int[9];
        int[] col = new int[9];
        int[] area = new int[9];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != '.') {
                    int num = board[i][j] - '0';
                    int index = i / 3 * 3 + j / 3;
                    if (((row[i] >> num) & 1) == 1)
                        return false;
                    if (((col[j] >> num) & 1) == 1)
                        return false;
                    if (((area[index] >> num) & 1) == 1)
                        return false;
                    row[i] |= 1 << num;  //占位
                    col[j] |= 1 << num;
                    area[index] |= 1 << num;
                }
            }
        }
        return true;
    }


    /**
     * 797. 所有可能的路径
     */
    public List<List<Integer>> allPathsSourceTarget(int[][] graph) {
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        path.add(0);
        allPathDfs(graph, ans, path, 0);
        return ans;
    }

    private void allPathDfs(int[][] graph, List<List<Integer>> ans, LinkedList<Integer> path, int currentIndex) {
        if (currentIndex == graph.length - 1) {  //并非记录所有节点的拼接情况，而是仅记录到达终点时的拼接情况
            ans.add(new ArrayList<>(path));
            return;
        }
        for (int i = 0; i < graph[currentIndex].length; i++) {  //横向搜索
            path.add(graph[currentIndex][i]);
            allPathDfs(graph, ans, path, graph[currentIndex][i]);  //纵向搜索
            path.removeLast();
        }
    }


    /**
     * 417. 太平洋大西洋水流问题
     */
    public List<List<Integer>> pacificAtlantic(int[][] heights) {
        List<List<Integer>> ans = new ArrayList<>();
        int rows = heights.length;
        int columns = heights[0].length;
        boolean[][] reachPacific = new boolean[rows][columns];
        boolean[][] reachAtlantic = new boolean[rows][columns];
        for (int i = 0; i < rows; i++) {  //从左、右两边开始搜索
            pacificAtlanticDfs(heights, reachPacific, i, 0);
            pacificAtlanticDfs(heights, reachAtlantic, i, columns - 1);
        }
        for (int j = 0; j < columns; j++) {  //从上、下两边开始搜索
            pacificAtlanticDfs(heights, reachPacific, 0, j);
            pacificAtlanticDfs(heights, reachAtlantic, rows - 1, j);
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (reachAtlantic[i][j] && reachPacific[i][j])
                    ans.add(Arrays.asList(i, j));
            }
        }
        return ans;
    }


    private void pacificAtlanticDfs(int[][] heights, boolean[][] reach, int i, int j) {
        if (reach[i][j]) return;
        reach[i][j] = true;  //能到这里，逻辑在下面封装

        //-----------------------------
        // 四方搜索：上、下、左、右
        //    注意：搜索过程中，保证前一位能触达边界，然后对重复的可通过上面 if剪枝
        //    原因：笼统来讲，起点是四条边界
        //          但本质为 Pacific从左边界和上边界开始，其并没有从右边界和下边界开始搜索，因此要保证从这两个边界开始的搜索能够触达右边界和下边界
        //          但本质为 Atlantic从右边界和下边界开始，其并没有从左边界和上边界开始搜索，因此要保证从这两个边界开始的搜索能够触达左边界和上边界
        //-----------------------------

        //下面 4 个判断来实现向 4 个方向搜索的过程，其本质类似于通过一个 for循环来实现的向 4 个方向搜索的过程，这里简化了
        if (i - 1 >= 0 && heights[i - 1][j] >= heights[i][j]) {   //向上搜索
            pacificAtlanticDfs(heights, reach, i - 1, j);
        }

        if (i + 1 <= heights.length - 1 && heights[i + 1][j] >= heights[i][j]) {  //向下搜索
            pacificAtlanticDfs(heights, reach, i + 1, j);
        }

        if (j - 1 >= 0 && heights[i][j - 1] >= heights[i][j]) {    //向左搜索
            pacificAtlanticDfs(heights, reach, i, j - 1);
        }

        if (j + 1 <= heights[0].length - 1 && heights[i][j + 1] >= heights[i][j]) {   //向右搜索
            pacificAtlanticDfs(heights, reach, i, j + 1);
        }
    }


    public List<List<Integer>> pacificAtlantic01(int[][] heights) {
        List<List<Integer>> ans = new ArrayList<>();
        int rows = heights.length;
        int columns = heights[0].length;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};   //上、下、左、右
        boolean[][] reachPacific = new boolean[rows][columns];
        boolean[][] reachAtlantic = new boolean[rows][columns];
        for (int i = 0; i < rows; i++) {  //从左、右两边开始搜索
            pacificAtlanticDfs(heights, directions, reachPacific, i, 0);
            pacificAtlanticDfs(heights, directions, reachAtlantic, i, columns - 1);
        }
        for (int j = 0; j < columns; j++) {  //从上、下两边开始搜索
            pacificAtlanticDfs(heights, directions, reachPacific, 0, j);
            pacificAtlanticDfs(heights, directions, reachAtlantic, rows - 1, j);
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (reachAtlantic[i][j] && reachPacific[i][j])
                    ans.add(Arrays.asList(i, j));
            }
        }
        return ans;
    }

    private void pacificAtlanticDfs(int[][] heights, int[][] directions, boolean[][] reach, int i, int j) {
        if (reach[i][j]) {
            return;
        }
        reach[i][j] = true;
        for (int[] direction : directions) {   //横向枚举搜索，4个方向
            int nextRow = i + direction[0];
            int nextCol = j + direction[1];
            if (nextRow >= 0 && nextRow < heights.length && nextCol >= 0 && nextCol < heights[0].length
                    && heights[nextRow][nextCol] >= heights[i][j]) {
                pacificAtlanticDfs(heights, directions, reach, nextRow, nextCol);  //纵向递归搜索
            }
        }
    }


    /**
     * 942. 增减字符串匹配
     */
    public int[] diStringMatch02(String s) {
        int[] ans = new int[s.length() + 1];
        int low = 0;
        int high = s.length();
        for (int i = 0; i < s.length(); i++) {
            ans[i] = s.charAt(i) == 'I' ? low++ : high--;
        }
        ans[s.length()] = low;
        return ans;
    }

    public int[] diStringMatch03(String s) {
        int[] ans = new int[s.length() + 1];
        TreeSet<Integer> sortedSet = new TreeSet<>();
        int DTimes = 0;
        for (int i = 0; i < s.length(); i++) {
            sortedSet.add(i);
            if (s.charAt(i) == 'D') DTimes++;
        }
        sortedSet.add(s.length());
        ans[0] = DTimes;
        sortedSet.remove(DTimes);
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'D') {
                ans[i + 1] = sortedSet.floor(ans[i]);
            }
            if (s.charAt(i) == 'I') {
                ans[i + 1] = sortedSet.ceiling(ans[i]);
            }
            sortedSet.remove(ans[i + 1]);
        }
        return ans;
    }










}




