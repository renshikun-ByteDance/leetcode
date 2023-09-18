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
        int hight = s.length();
        for (int i = 0; i < s.length(); i++) {
            ans[i] = s.charAt(i) == 'D' ? hight-- : low++;
            //反向思维：如果为 "D" ，则当前位直接用当前的最大值，则下一位的选择一定小于当前最大值
            //反向思维：如果为 "I" ，则当前位直接用当前的最小值，则下一位的选择一定大于当前最大值
        }
        //此时必有 height == low
        ans[s.length()] = hight;
        return ans;
    }

    //------------------------------------------------------------------
    // 上下两题很典型，贪心将当前最值占用，后续所有值都不会大于或小于当前的最值
    //------------------------------------------------------------------

    /**
     * 面试题 10.11. 峰与谷
     */
    public void wiggleSort(int[] nums) {
        int[] sorted = Arrays.copyOf(nums, nums.length);
        Arrays.sort(sorted);
        int minIndex = 0;
        int maxIndex = nums.length - 1;
        for (int i = 0; i < nums.length; i++) {
            //偶数位
            if ((i & 1) == 0) {
                nums[i] = sorted[maxIndex];
                maxIndex--;
            }
            //奇数位
            if ((i & 1) == 1) {
                nums[i] = sorted[minIndex];
                minIndex++;
            }
        }
        System.out.println(Arrays.toString(nums));
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

    // 依次取数，而不是对 HashMap 排序，也是桶排序的思想
    public int largestSumAfterKNegations01(int[] nums, int k) {
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

    public int maximumElementAfterDecrementingAndRearranging01(int[] arr) {
        Arrays.sort(arr);
        arr[0] = 1;
        for (int i = 1; i < arr.length; i++) {
            arr[i] = Math.min(arr[i], arr[i - 1] + 1);  //巧用最值，避免复杂的判断
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


    public int minimumOperations02(int[] nums) {
        int[][] one = new int[100001][2];
        int[][] two = new int[100001][2];
        //索引初始化
        for (int i = 0; i < 100001; i++) {
            one[i][0] = i;
            two[i][0] = i;
        }
        //赋值
        for (int i = 0; i < nums.length; i++) {
            if ((i & 1) == 0)  //偶数位
                two[nums[i]][1]++;
            else               //奇数位
                one[nums[i]][1]++;
        }
        //排序
        Arrays.sort(one, (o1, o2) -> o2[1] - o1[1]);  //降序
        Arrays.sort(two, (o1, o2) -> o2[1] - o1[1]);  //降序
        int[] oneTop1 = one[0];
        int[] oneTop2 = one[1];
        int[] twoTop1 = two[0];
        int[] twoTop2 = two[1];
        int maxFreq = 0;
        if (oneTop1[0] == twoTop1[0]) {
            maxFreq = Math.max(oneTop1[1] + twoTop2[1], twoTop1[1] + oneTop2[1]);
        } else {
            maxFreq = oneTop1[1] + twoTop1[1];
        }
        return nums.length - maxFreq;
    }


    //------------------------------------------------
    // 对比上下两种写法的差异
    //    首先，相同点在于 二者均采用给定默认值，来简化复杂逻辑判断的处理
    //    其次，差异点在于 上面要排序（每个元素都要两两比较），时间复杂度高，下面无需排序，只需要遍历一遍数组，记录最大值即可
    //          有点类似桶排序
    //------------------------------------------------


    public int minimumOperations01(int[] nums) {    //水流
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
     * 1996. 游戏中弱角色的数量
     */
    public int numberOfWeakCharacters02(int[][] properties) {
        int ans = 0;
        Arrays.sort(properties, (o1, o2) -> {
            if (o1[0] != o2[0])
                return o2[0] - o1[0];
            else
                return o1[1] - o2[1];
        });
        int currentMaxDefense = properties[0][1];
        for (int i = 1; i < properties.length; i++) {
            //-------------------------------------
            //由于主排序基于"攻击值"，为了保证"攻击值"相等的两个角色 A[100,100]/B[100,200] 可以同时作为"弱角色"，因此，此处仅逐一比较各个角色的"防御值"
            //   为了避免这样的逻辑导致 A成为B的弱角色，因此，在排序的时候，针对这种"攻击值"相等的情况，应该按照"防御值"升序排序，从而避免这种情况；
            //       更深入解释，在计算 currentMaxDefense 时，先处理的 A ，而排序规则是，A 的防御值小于 B 的防御值
            //           如果基于以下判断条件，认为 B为弱角色，那其一定不是 A 的弱角色
            //-------------------------------------
            if (currentMaxDefense > properties[i][1])
                ans++;
            currentMaxDefense = Math.max(currentMaxDefense, properties[i][1]);  //更新防御值，取最大值
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

    public int findPeakElement12(int[] nums) {
        int index = nums.length - 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            if (nums[i] > nums[i + 1])  //大于右侧，则记录
                index = i;
            //小于右侧，则不记录，从而 index 记录了最左侧的峰值
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


    /**
     * 661. 图片平滑器
     * 基于 矩阵前缀和 的思想
     */
    public int[][] imageSmoother(int[][] img) {  //前缀和 + 最值简化判断逻辑
        int rows = img.length;
        int columns = img[0].length;
        int[][] ans = new int[rows][columns];
        int[][] prefixSum = new int[rows + 1][columns + 1];  //列仅补齐左侧，左侧第一列为 0
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                prefixSum[i][j] = prefixSum[i - 1][j] + prefixSum[i][j - 1] - prefixSum[i - 1][j - 1] + img[i - 1][j - 1];
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                //四个点均为 img 矩阵中的坐标
                int left = Math.max(j - 1, 0);
                int right = Math.min(j + 1, columns - 1);
                int top = Math.max(i - 1, 0);
                int bottom = Math.min(i + 1, rows - 1);
                //矩阵的合理范围
                int window = (right - left + 1) * (bottom - top + 1);
                //矩阵的累加值
                int sum = prefixSum[bottom + 1][right + 1]
                        - prefixSum[bottom + 1][left]
                        - prefixSum[top][right + 1]
                        + prefixSum[top][left];
                //矩阵的平均值
                ans[i][j] = sum / window;
            }
        }
        return ans;
    }

    public int[][] imageSmoother01(int[][] img) {   //基于矩阵的方向
        int rows = img.length;
        int cols = img[0].length;
        int[][] ans = new int[rows][cols];
        int[][] directions = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 0}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int sum = 0;
                int count = 0;
                for (int[] dir : directions) {
                    int xx = i + dir[0];
                    int yy = j + dir[1];
                    if (xx < 0 || xx >= rows || yy < 0 || yy >= cols)
                        continue;
                    sum += img[xx][yy];
                    count++;
                }
                ans[i][j] = sum / count;
            }
        }
        return ans;
    }


    /**
     * 2104. 子数组范围和
     */
    public long subArrayRanges(int[] nums) {
        long ans = 0;
        for (int i = 0; i < nums.length; i++) { //枚举数组的开始元素
            int maxValue = nums[i];
            int minValue = nums[i];
            for (int j = i + 1; j < nums.length; j++) {  //枚举所有连续子数组的组合
                maxValue = Math.max(maxValue, nums[j]);
                minValue = Math.min(minValue, nums[j]);
                ans += maxValue - minValue;      //计算
            }
        }
        return ans;
    }


    /**
     * 1838. 最高频元素的频数
     */
    public int maxFrequency(int[] nums, int k) {   //水位线上升（横向 前缀和）、滑动窗口
        Arrays.sort(nums);
        int left = 0;
        int right = 1;
        int maxWindow = 1;
        long sum = 0;
        while (right < nums.length) {
            //水平面上升
            sum += (long) (right - left) * (nums[right] - nums[right - 1]);   // right - left 代表除了 right 以外的区间长度
            while (sum > k) {
                //纵向剔除前缀和
                sum -= nums[right] - nums[left];
                left++;
            }
            maxWindow = Math.max(maxWindow, right - left + 1);   // 包含 right 在内的区间长度
            right++;
        }
        return maxWindow;
    }


    /**
     * 1221. 分割平衡字符串
     */
    public int balancedStringSplit(String s) {
        int ans = 0;
        int sum = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'L')
                sum++;
            else
                sum--;
            if (sum == 0)
                ans++;
        }
        return ans;
    }

    public int balancedStringSplit04(String str) {   //针对此题目是错误写法，但题目自身有歧义
        int ans = 0;
        for (int i = 0; i < str.length(); i++) {
            int count = 0;
            for (int j = i; j < str.length(); j++) {
                count = str.charAt(j) == 'L' ? count + 1 : count - 1;
                ans = count == 0 ? ans + 1 : ans;
            }
        }
        return ans;
    }


    /**
     * 1218. 最长定差子序列
     */
    public static int longestSubsequence(int[] arr, int difference) {
        int ans = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            hTable.put(arr[i], hTable.getOrDefault(arr[i] - difference, 0) + 1); //继承 / 续上
            ans = Math.max(ans, hTable.get(arr[i]));
        }
        return ans;
    }


    /**
     * 926. 将字符串翻转到单调递增
     */
    public int minFlipsMonoIncr(String s) {   //状态转移，同时此题细节巨多
        int n = s.length();
        int[][] dp = new int[n][2];  //每一位
        dp[0][0] = s.charAt(0) == '0' ? 0 : 1;  //为 0 无需翻转
        dp[0][1] = s.charAt(0) == '1' ? 0 : 1;  //为 1 无需翻转
        //状态转移，记录所有的可能
        for (int i = 1; i < n; i++) {
            dp[i][0] = dp[i - 1][0] + (s.charAt(i) == '0' ? 0 : 1);
            dp[i][1] = Math.min(dp[i - 1][0], dp[i - 1][1]) + (s.charAt(i) == '1' ? 0 : 1);
        }
        return Math.min(dp[n - 1][0], dp[n - 1][1]);
    }

    //#遍历字符串，找到一个分界点，使得该分界点之前1的个数和分界点之后0的个数之和最小，把分界点之前的1变成0，之后的0变成1
    public int minFlipsMonoIncr01(String str) {   //基于前缀和的思想
        int n = str.length();
        //------------------------------------------------------------------------------
        // left_1 代表当前位置及左侧位置中 1 的个数，right_0 代表当前位置及右侧位置中 0 的个数
        //------------------------------------------------------------------------------
        int left_1 = 0;
        int right_0 = (int) Arrays.stream(str.split("")).mapToInt(Integer::valueOf).filter(o -> o == 0).count();
        //默认值
        int ans = Math.min(right_0, n - right_0);  //将 0 全部翻转为 1 或者 将 1 全部翻转为 0两种情况中，最小的翻转次数
        for (int i = 0; i < n; i++) {
            if (str.charAt(i) == '0')
                right_0--;
            if (str.charAt(i) == '1')
                left_1++;

            //每个位置均计算，将此位置左侧的 1 全部翻转为 0 && 将右侧 0 全部翻转为 1 的次数，并各个位置比较其最小值
            ans = Math.min(ans, left_1 + right_0);
        }
        return ans;
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
     * 45. 跳跃游戏 II
     */
    public int jump(int[] nums) {
        int steps = 0;
        int end = 0;
        int maxPosition = 0;
        //如果访问最后一个元素，在边界正好为最后一个位置的情况下，我们会增加一次「不必要的跳跃次数」，因此我们不必访问最后一个元素
        for (int i = 0; i < nums.length - 1; i++) {  //起跳点不能是 nums.lenth - 1，否则就相当于原地跳
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
     * 1052. 爱生气的书店老板
     */
    public int maxSatisfied(int[] customers, int[] grumpy, int minutes) {
        int basePeople = 0;
        for (int i = 0; i < customers.length; i++) {
            if (grumpy[i] == 0) {
                basePeople += customers[i];
                customers[i] = 0;   //精髓，将每一时刻不生气的人数累加到默认值后，将其置为 0
                // 后续仅用滑动窗口计算滑动窗口内生气的人数，并将最大值（变为满意）累加到默认值即可，不需要复杂的判断了；
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


    /**
     * 1518. 换酒问题
     */
    public int numWaterBottles(int numBottles, int numExchange) {
        int ans = 0;
        int currentEmptyBottles = 0;
        while (numBottles > 0) {
            if (currentEmptyBottles == numExchange) { //兑换一瓶
                numBottles++;
                currentEmptyBottles = 0;
            }
            ans++;            //喝一瓶
            numBottles--;     //少一瓶
            currentEmptyBottles++;  //多一个空瓶子
        }
        if (currentEmptyBottles == numExchange)
            ans++;
        return ans;
    }

    /**
     * 1518. 换酒问题
     */
    public int numWaterBottles02(int numBottles, int numExchange) {  //兑换的问题，尽量不要用除法，尽量一个一个来
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
    public int findLHS(int[] nums) {   //子序列可考虑排序问题
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
     * 1. 两数之和
     */
    public int[] twoSum(int[] nums, int target) {
        int[] ans = new int[2];
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (hTable.containsKey(target - nums[i]))
                return new int[]{hTable.get(target - nums[i]), i};  //数组中存在相同的两个 num，如示例 [3,3] 6
            hTable.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }


    public int[] twoSum01(int[] nums, int target) {  //错误写法
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            hTable.put(nums[i], i);     //[3,3] 在遍历过程中，会覆盖
        }
        for (int num : hTable.keySet()) {
            if (hTable.containsKey(target - num))
                return new int[]{hTable.get(num), hTable.get(target - num)};
        }
        return new int[]{-1, -1};
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

    public int findPoisonedDuration00(int[] timeSeries, int duration) {
        int beginTime = timeSeries[0];
        int endTime = timeSeries[0] + duration - 1;
        int totalTime = 0;
        for (int i = 1; i < timeSeries.length; i++) {
            if (timeSeries[i] > endTime) {  //无法续上，累积并更新左边界
                totalTime += endTime - beginTime + 1;  //不需要 + 1，因为时间是间隔，而非个数
                beginTime = timeSeries[i];
            }
            endTime = Math.max(endTime, timeSeries[i] + duration - 1);
        }
        totalTime += endTime - beginTime + 1;
        return totalTime;
    }

    public int findPoisonedDuration01(int[] timeSeries, int duration) {
        int sumTimes = 0;
        for (int i = 1; i < timeSeries.length; i++) {
            sumTimes += Math.min(timeSeries[i] - timeSeries[i - 1], duration);  //非常好的思路
        }
        return sumTimes + duration;
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


    /**
     * 13. 罗马数字转整数
     */
    public int romanToInt(String s) {
        //预处理，添加以下 6 中对照表
        s = s.replace("IV", "a");    //转换是比较好的思路
        s = s.replace("IX", "b");
        s = s.replace("XL", "c");
        s = s.replace("XC", "d");
        s = s.replace("CD", "e");
        s = s.replace("CM", "f");
        int sum = 0;
        for (int i = 0; i < s.length(); i++) {
            sum += convertValue(s.charAt(i));
        }
        return sum;
    }

    private int convertValue(char cha) {
        switch (cha) {
            case 'a':
                return 4;
            case 'b':
                return 9;
            case 'c':
                return 40;
            case 'd':
                return 90;
            case 'e':
                return 400;
            case 'f':
                return 900;
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                return 0;
        }
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

    //-------------------------------------------------------------
    // 可基于位掩码快速识别
    // 本质是通过一个int/long的数字来表示 一组字符(a-z)或一组数字，更确切的说是int/long对应的二进制表示，每一位代表一个字符或数字，同时这里也包含去重的功能
    //-------------------------------------------------------------

    /**
     * 318. 最大单词长度乘积
     * 基于位掩码，就把出现的字符转压缩成了一个整数，优点是可以通过与运算直接判断两个串是否有相同字符
     * https://www.cnblogs.com/hihtml5/p/6483783.html
     * 思路：重要
     */
    public int maxProduct(String[] words) {
        int len = words.length;
        int[] maskBit = new int[len];
        int maxLen = 0;
        for (int i = 0; i < len; i++) {
            String word = words[i];
            int wordLength = word.length();
            for (int j = 0; j < wordLength; j++) {
                //--------------------------------------------------------------------------
                //或运算：只要一个为 1则为1，否则为 0
                //每个 word表示的数值：
                //   是在对 word中字符去重后的结果，仅用于基于位运算比较两个word中是否存在相同的字符
                //--------------------------------------------------------------------------
                maskBit[i] |= (1 << word.charAt(j) - 'a');
            }
        }
        //嵌套循环，寻找无重复字符的两个单词，并求其长度乘积，更新maxLen
        for (int i = 0; i < len - 1; i++) {
            for (int j = i + 1; j < len; j++) {
                if ((maskBit[i] & maskBit[j]) == 0)  //两个单词中无重复的字符
                    maxLen = Math.max(maxLen, words[i].length() * words[j].length());
            }
        }
        return maxLen;
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
     * 137. 只出现一次的数字 II
     */
    public int singleNumberII(int[] nums) {
        int[] res = new int[32];
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            for (int num : nums) {
                if (((num >> i) & 1) == 1)
                    res[i]++;
            }
            if (res[i] % 3 == 1)
                ans += (1 << i);
        }
        return ans;
    }

    public int singleNumberII01(int[] nums) {
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            int times = 0;
            for (int num : nums) {
                times += ((num >> i) & 1);
            }
            if (times % 3 == 1)    //注意：这里不能写为 上面的判断条件，因为不同的数会在相同的二进制位 均为 1
                ans += (1 << i);
        }
        return ans;
    }

    /**
     * 260. 只出现一次的数字 III
     * 技巧：异或
     */
    public int[] singleNumberIII(int[] nums) {
        int res = 0;
        for (int num : nums)
            res ^= num;
        //---------------------------------------------------------------
        // n & (-n)位运算的结果为：获取 n 从右至左第一个非 0 的位置，二进制的形式标识第一个非 0 的位置
        // 因此，如果 n 是正整数并且 n & (-n) = n，那么 n 就是 2 的幂
        //---------------------------------------------------------------
        //Integer.MIN_VALUE的二进制表示就是符号位是1，其余都为0，所以求他的最低位1代表的整数就是他自己
        int xor = res == Integer.MIN_VALUE ? res : res & (-res);  //注意：这个并不是具体第几位，而是对应了一个二进制
        int[] target = new int[2];
        for (int num : nums) {
            //-----------------------------------------------------------
            //采用分治的思想：通过获取最低有效位，将数组分为两部分或者两个队列
            //              每一队列的特点都是：仅有一个出现一次的的数字，其余数字出现两次
            //-----------------------------------------------------------
            if ((num & xor) == 0)   //重要
                target[0] ^= num;
            else
                target[1] ^= num;
        }
        return target;
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
        String[] words = str.trim().split(" ");
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
     * 696. 计数二进制子串
     */
    public int countBinarySubstrings(String str) {
        ArrayList<Integer> counts = new ArrayList<>();
        int currentIndex = 0;
        int len = str.length();
        while (currentIndex < len) {
            int count = 0;
            char xx = str.charAt(currentIndex);
            while (currentIndex < len && str.charAt(currentIndex) == xx) {
                count++;        //计数
                currentIndex++; //移动
            }
            counts.add(count);
        }
        int ans = 0;
        for (int i = 1; i < counts.size(); i++) {
            //-----------------------------------------------
            // 核心逻辑：相邻两数的个数，取最小值，削平二者，然后..配对
            //-----------------------------------------------
            ans += Math.min(counts.get(i - 1), counts.get(i));
        }
        return ans;
    }


    /**
     * 674. 最长连续递增序列
     */
    public int findLengthOfLCIS(int[] nums) {   //也可以用滑动窗口来做，但这样的写法更直观容易理解
        int ans = 0;
        int n = nums.length;
        int left = 0;
        for (int right = 0; right < n; right++) {
            if (right > 0 && nums[right] <= nums[right - 1]) {
                left = right;  //重新开始计数
            }
            ans = Math.max(ans, right - left + 1);
        }
        return ans;
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
        t = ' ' + t;   //考虑到对第一个字符的处理
        //dp[1][0] = 5，代表在索引位置 1后，字符 a 的下一个位置的索引为 5
        int[][] dp = new int[t.length()][26];  //二维坐标：i为位置索引，j为 a-z，值代表下一个字符 j 出现的位置
        //预处理
        for (char ch = 'a'; ch <= 'z'; ch++) {
            int nextIndex = -1; // -1 表示不存在
            for (int i = t.length() - 1; i >= 0; i--) {
                dp[i][ch - 'a'] = nextIndex;
                if (t.charAt(i) == ch) {  //更新，下一个字符使用时会使用，当前字符不会使用
                    nextIndex = i;
                }
            }
        }
        //开始匹配，判断是否是子序列
        int currentIndex = 0;
        for (char ch : s.toCharArray()) {
            currentIndex = dp[currentIndex][ch - 'a'];  //更新跳跃
            if (currentIndex == -1)
                return false;
        }
        return true;
    }


    int left = 0;
    int right = 0;

    /**
     * 5. 最长回文子串
     */
    public String longestPalindrome(String str) {  //中心扩展法
        String ans = "";
        for (int i = 0; i < str.length(); i++) {  //依次点为正中心，或左中心
            //----------------------------------------------
            // 以当前点为回文串的正中心，寻找奇数位的最长回文串
            //----------------------------------------------
            left = i;
            right = i;
            while (left >= 0 && right < str.length() && str.charAt(left) == str.charAt(right)) {
                left--;
                right++;
            }
            if (right - left - 1 > ans.length()) {
                ans = str.substring(left + 1, right - 1 + 1);
            }

            //-------------------------------------------------------------------------------------------
            // 以当前点为回文串的左中心，寻找偶数位的最长回文串，即 i 和 i + 1 分别为偶数位回文串的左中心和右中心
            //-------------------------------------------------------------------------------------------
            left = i;
            right = i + 1;
            while (left >= 0 && right < str.length() && str.charAt(left) == str.charAt(right)) {
                left--;
                right++;
            }
            if (right - left - 1 > ans.length()) {
                ans = str.substring(left + 1, right - 1 + 1);
            }
        }
        return ans;
    }


    public String longestPalindrome00(String str) {  //思路更清晰
        String ans = "";
        for (int i = 0; i < str.length(); i++) {
            //扫描，当前点两侧的具有与当前点相同值的位点
            int left = i - 1;
            int right = i + 1;
            while (left >= 0 && str.charAt(left) == str.charAt(i)) {
                left--;
            }
            while (right < str.length() && str.charAt(right) == str.charAt(i)) {
                right++;
            }
            //搜索回文串
            while (left >= 0 && right < str.length() && str.charAt(left) == str.charAt(right)) {
                left--;
                right++;
            }

            //最长回文串
            if (right - 1 - (left + 1) + 1 > ans.length()) {
                ans = str.substring(left + 1, right - 1 + 1);
            }
        }
        return ans;
    }


    /**
     * 1936. 新增的最少台阶数
     */
    public int addRungs(int[] rungs, int dist) {   //超时
        int ans = 0;
        int currentIndex = 0;
        for (int i = 0; i < rungs.length; i++) {
            //无需额外添加阶梯
            if (currentIndex + dist >= rungs[i]) {
                currentIndex = rungs[i];
                continue;
            }
            //需要额外添加阶梯
            while (currentIndex + dist < rungs[i]) {
                ans++;
                currentIndex += dist;
                if (currentIndex + dist >= rungs[i]) {  //更新
                    currentIndex = rungs[i];
                }
            }
        }
        return ans;
    }

    public int addRungs01(int[] rungs, int dist) {   //优化时间复杂度
        int[] res = new int[rungs.length + 1];
        System.arraycopy(rungs, 0, res, 1, rungs.length);
        int ans = 0;
        int currentIndex = 0;
        for (int i = 1; i < res.length; i++) {
            //无需额外添加阶梯
            if (res[i] - res[i - 1] > dist) {
                ans += (res[i] - res[i - 1] - 1) / dist;   // -1是为向下取整，比如 20，40，dist为 10，其实只需要一步即可
            }
        }
        return ans;
    }


    /**
     * 128. 最长连续序列
     */
    public int longestConsecutive(int[] nums) {
        int maxWindow = 0;
        int[] distinct = Arrays.stream(nums).distinct().toArray();
        Arrays.sort(distinct);
        int currentWindow = 1;
        for (int i = 0; i < distinct.length; i++) {
            if (i > 0 && distinct[i] == distinct[i - 1] + 1) {
                currentWindow++;
            } else {
                currentWindow = 1;
            }
            maxWindow = Math.max(maxWindow, currentWindow);
        }
        return maxWindow;
    }

    public int longestConsecutive01(int[] nums) {  //会比上面的快一点
        int maxWindow = 0;
        HashSet<Integer> distinct = new HashSet<>();
        for (int num : nums) {
            distinct.add(num);
        }
        for (int num : distinct) {
            int currentWindow = 1;
            if (distinct.contains(num - 1))       // 只考虑一侧的情况
                continue;
            //------------------------------------------------
            // 以当前元素为连续序列的最小值来处理
            //------------------------------------------------
            while (distinct.contains(num + 1)) {  // 每次时间复杂度为 O(1)
                num++;
                currentWindow++;
            }
            maxWindow = Math.max(maxWindow, currentWindow);
        }
        return maxWindow;
    }

    public int longestConsecutive02(int[] nums) {  //滑动窗口
        if (nums.length == 0) return 0;
        int maxWindow = 1;
        int[] distinct = Arrays.stream(nums).distinct().toArray();
        Arrays.sort(distinct);
        int left = 0;
        int right = 0;
        while (right < distinct.length) {
            while (right > 0 && right < distinct.length && distinct[right] == distinct[right - 1] + 1) {
                maxWindow = Math.max(maxWindow, right - left + 1);
                right++;
            }
            if (right == distinct.length) return maxWindow;
            if (right > 0 && distinct[right] != distinct[right - 1] + 1) {
                left = right;
            }
            right++;
        }
        return maxWindow;
    }


    //桶排序，桶太多了
    public int longestConsecutive06(int[] nums) {  //超出内存限制 java.lang.OutOfMemoryError: Java heap space
        int maxWindow = 0;
        int base = (int) Math.pow(10, 9);
        int[] buckets = new int[2 * base + 2];
        for (int index : nums) {
            buckets[index + base]++;
        }
        int currentWindow = 1;  //初始值
        for (int i = 0; i < buckets.length; i++) {
            if (i > 0 && buckets[i] != 0 && buckets[i - 1] != 0) {
                currentWindow++;
            } else {
                currentWindow = 1;  //代表当前值
            }
            maxWindow = Math.max(maxWindow, currentWindow);
        }
        return maxWindow;
    }

    //并查集的解法
    //https://leetcode.cn/problems/longest-consecutive-sequence/solution/by-lfool-jdy4/


    /**
     * 1462. 课程表 IV
     */
    public List<Boolean> checkIfPrerequisite(int numCourses, int[][] prerequisites, int[][] queries) {
        //基于一次统一的深度优先搜索，获取每个节点的"子节点"
        ArrayList<Boolean> ans = new ArrayList<>();
        //单层邻接表
        ArrayList<ArrayList<Integer>> adjacent = new ArrayList<>();  //父节点指向子节点
        //深度邻接表
        ArrayList<HashSet<Integer>> adjacentDepth = new ArrayList<>();    //父节点指向子节点
        for (int i = 0; i < numCourses; i++) {
            adjacent.add(new ArrayList<>());
            adjacentDepth.add(new HashSet<>());
        }
        //初始化单层邻接表
        for (int[] dependency : prerequisites) {
            adjacent.get(dependency[0]).add(dependency[1]);   //父节点指向子节点
        }

        //基于邻接表，通过统一的 DFS初始化全局邻接表，获取每个节点的"子节点"
        int[] visited = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            //---------------------------------------------------
            // 关键：并不是从单独一个课程开始深度优先搜索，而是从多个不同的课程开始，因为有些课程间不存在依赖关系
            //---------------------------------------------------

            //针对已经搜索过的位点，下次忽略搜索
            if (visited[i] == 1)
                continue;
            checkIfPrerequisiteDfs(adjacent, adjacentDepth, visited, i);
        }

        //逐一查询结果
        for (int[] query : queries) {
            ans.add(adjacentDepth.get(query[0]).contains(query[1]));
        }
        return ans;
    }

    private void checkIfPrerequisiteDfs(ArrayList<ArrayList<Integer>> adjacent, ArrayList<HashSet<Integer>> adjacentDepth, int[] visited, int fatherNode) {  //超出内存限制
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

    /**
     * 796. 旋转字符串
     */
    public boolean rotateString(String s, String goal) {
        return s.length() == goal.length() && (s + s).contains(goal);
    }


}
