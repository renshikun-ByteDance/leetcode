package leetcode.important;


import java.util.*;


/**
 * 比较好的思路，但不一定仅有这一种解法
 */
public class Thinking {


    /**
     * 942. 增减字符串匹配
     */
    public int[] diStringMatch(String s) {
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
     * 1005. K 次取反后最大化的数组和
     * 桶排序，归类统一计算
     */
    public int largestSumAfterKNegations(int[] nums, int k) {
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


    /**
     * 1405. 最长快乐字符串
     * 思想：一种排序取数的方法，每次选择其中一个满足条件的数据，直至取不到数据，就终止
     */
    public String longestDiverseString(int a, int b, int c) {
        StringBuilder res = new StringBuilder();
        charAndNums[] sorted = {
                new charAndNums('a', a),
                new charAndNums('b', b),
                new charAndNums('c', c)
        };
        //每次"逐个"消耗一个，逐个的顺序：按照字符的个数来消耗，尽可能消耗多的，一个交替另一个最好，同时保证不会连续三个
        while (true) {
            //每次循环均需要重新排序
            Arrays.sort(sorted, (m, n) -> n.nums - m.nums);  //从大到小排序
            //注意：其实每次都是在"剩余最多"或"剩余次最多"的字符中选择一个进行消耗
            for (charAndNums tuple : sorted) {    //每一轮循环的目的：找到一个可以添加的元素
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


    private class charAndNums {
        char ch;
        int nums;

        public charAndNums(char ch, int nums) {
            this.ch = ch;
            this.nums = nums;
        }
    }


    /**
     * 1705. 吃苹果的最大数目
     * 思想：随着时间进度，逐天添加相关元素，而非一开始全部添加
     */
    public int eatenApples(int[] apples, int[] days) {
        int maxApples = 0;
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[0] - o2[0]);
        int currentDay = 0;
        while (currentDay < apples.length || !sortedQueue.isEmpty()) {
            //1、按照时间推进的进度，诸天添加 元素
            if (currentDay < apples.length && apples[currentDay] != 0) {
                sortedQueue.add(new int[]{currentDay + days[currentDay], apples[currentDay]});
            }
            //2、剔除过期元素
            while (!sortedQueue.isEmpty() && (sortedQueue.peek()[0] <= currentDay || sortedQueue.peek()[1] == 0)) {
                sortedQueue.poll();
            }
            //3、贪心吃苹果
            if (!sortedQueue.isEmpty()) {
                sortedQueue.peek()[1]--;
                maxApples++;
            }
            currentDay++;
        }
        return maxApples;
    }


    /**
     * 1846. 减小和重新排列数组后的最大元素
     * 思路：元素值有上限，超过上线，取上限值，未超过上线，去本身
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
            arr[i] = Math.min(arr[i], arr[i - 1] + 1);  //关键：限定了上线，当 arr[i]超过上线...当 arr[i]未超过上线...
        }
        return arr[arr.length - 1];
    }


    /**
     * 2170. 使数组变成交替数组的最少操作数
     * 思想：附加默认值，保证位数恒定
     */
    public int minimumOperations(int[] nums) {
        if (nums.length == 1)
            return 0;

        int maxTimes = 0;
        HashMap<Integer, Integer> oneTable = new HashMap<>();
        HashMap<Integer, Integer> twoTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if ((i & 1) == 1) {  //奇数位
                oneTable.put(nums[i], oneTable.getOrDefault(nums[i], 0) + 1);
            } else {
                twoTable.put(nums[i], twoTable.getOrDefault(nums[i], 0) + 1);
            }
        }

        //核心：保证每个map中至少有两个元素
        oneTable.put(-1, 0);
        twoTable.put(-1, 0);
        //奇数位元素的排序
        ArrayList<Map.Entry<Integer, Integer>> oneSorted = new ArrayList<>(oneTable.entrySet());
        oneSorted.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder())); //由大到小排序
        //偶数位元素的排序
        ArrayList<Map.Entry<Integer, Integer>> twoSorted = new ArrayList<>(twoTable.entrySet());
        twoSorted.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder())); //由大到小排序

        //获取排名第一的元素
        Map.Entry<Integer, Integer> oneFirst = oneSorted.get(0);
        Map.Entry<Integer, Integer> twoFirst = twoSorted.get(0);

        //获取排名第二的元素
        Map.Entry<Integer, Integer> oneSecond = oneSorted.get(1);
        Map.Entry<Integer, Integer> twoSecond = twoSorted.get(1);

        //计算，奇数位和偶数位不同元素的最大频次和
        if (!oneFirst.getKey().equals(twoFirst.getKey())) {
            maxTimes = oneFirst.getValue() + twoFirst.getValue();
        } else {
            maxTimes = Math.max(oneFirst.getValue() + twoSecond.getValue(), twoFirst.getValue() + oneSecond.getValue());
        }

        return nums.length - maxTimes;
    }

    //------------------------------------------------
    // 对比上下两种写法的差异
    //    首先，相同点在于 二者均采用给定默认值，来简化复杂逻辑判断的处理
    //    其次，差异点在于 上面要排序（每个元素都要两两比较），下面无需排序，只需要遍历一遍数组，记录最大值即可
    //------------------------------------------------


    public int minimumOperations01(int[] nums) {
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
     * 1996. 游戏中弱角色的数量
     * 思想：两个条件同时控制，可尝试先定位一个条件，然后第二个条件通过变量记录最值来逐一判断各个元素
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


    /**
     * 162. 寻找峰值
     */
    public int findPeakElement(int[] nums) {
        int index = 0;  //默认值，即当前暂时认为 此位置为峰值，因为左侧为 负无穷
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[i - 1])   //关键：在 峰值处，由于 其严格大于两侧，所以从左向右遍历时，先在峰值处先更新 Index（左侧条件满足），后无法更新 Index（右侧条件满足）
                index = i;
        }
        return index;
    }


    public int findPeakElement03(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < nums[mid + 1]) {           //left左侧上山
                left = mid + 1;
            } else if (nums[mid] >= nums[mid + 1]) {   //right右侧上山
                right = mid;
            }
        }
        return left;
    }

    public int findPeakElement04(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] <= nums[mid + 1]) {           //left左侧上山
                left = mid + 1;
            } else if (nums[mid] > nums[mid + 1]) {   //right右侧上山
                right = mid;
            }
        }
        return left;
    }





    /**
     * 1818. 绝对差值和
     * 思路：长整型的重要性
     */
    public int minAbsoluteSumDiff(int[] nums1, int[] nums2) {
        long mod = (int) Math.pow(10, 9) + 7;
        long sum = 0;       //一定用长整型
        long maxDiff = 0;   //一定用长整型
        int[] sorted = Arrays.copyOf(nums1, nums1.length);
        Arrays.sort(sorted);
        for (int i = 0; i < nums1.length; i++) {
            int target = nums2[i];  //和此值的距离
            int currentDiff = Math.abs(nums1[i] - target);
            sum += currentDiff;
            //逼近target右侧
            int pos = binSearch(sorted, target);
            //target右侧
            int Diff = Math.abs(sorted[pos] - target);
            //target左侧
            if (pos - 1 >= 0)
                Diff = Math.min(Diff, Math.abs(sorted[pos - 1] - target));  //单个位置，计算的目标是：取距离 target的最近距离值（从左侧或右侧）
            //取各位中，当前使用的距离 与 计算的最近距离 二者差值的最大值
            maxDiff = Math.max(maxDiff, currentDiff - Diff);   //不用加绝对值
        }
        return (int) ((sum - maxDiff) % (mod));
    }


    private int binSearch(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target)
                left = mid + 1;
            else if (target <= nums[mid])
                right = mid - 1;
        }
        if (left == nums.length)
            return nums.length - 1;
        else if (nums[left] == target)
            return left;
        else if (nums[left] > target)  //右侧逼近
            return left;

        return -6;
    }


}
