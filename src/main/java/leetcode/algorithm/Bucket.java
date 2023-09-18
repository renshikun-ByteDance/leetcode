package leetcode.algorithm;

import java.util.*;
import java.util.regex.Pattern;

public class Bucket {


    /**
     * 451. 根据字符出现频率排序
     */
    public String frequencySort(String s) {

        //---------------------------------------------------------------------------
        // 桶排序，由于题目要求，需要对元素频次进行排序，故分桶要素是元素频次，桶内元素为具有相同频次的多个不同元素
        //---------------------------------------------------------------------------

        StringBuilder ans = new StringBuilder();
        HashMap<Character, Integer> charAndTimes = new HashMap<>();
        int maxFreq = 0;
        for (int i = 0; i < s.length(); i++) {
            int freq = charAndTimes.getOrDefault(s.charAt(i), 0) + 1;
            charAndTimes.put(s.charAt(i), freq);
            maxFreq = Math.max(maxFreq, freq);  //记录最大频次，用于确定 bucket 的个数
        }
        StringBuilder[] buckets = new StringBuilder[maxFreq + 1];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new StringBuilder();
        }
        //桶排序的核心是，无需排序，仅需将"排序字段值"与"桶编号"对应即可，之后按照 "桶编号" 取数据即为按照排序字段取数据，即排序的结果
        for (Map.Entry<Character, Integer> charTime : charAndTimes.entrySet()) {  //桶排序开始
            buckets[charTime.getValue()].append(charTime.getKey());   //一个桶为一个 StringBuild，记录频次为桶编号的对应字符
        }
        //基于桶编号依次取对应的值
        for (int i = maxFreq; i > 0; i--) {
            StringBuilder bucket = buckets[i]; //bucket中记录的元素均出现 i 次
            for (int j = 0; j < bucket.length(); j++) {
                int times = i;
                while (times > 0) {
                    ans.append(bucket.charAt(j));
                    times--;
                }
            }
        }
        return ans.toString();
    }


    public String frequencySort01(String s) {   //严格意义上来讲，不是桶排序，而是计数排序算法
        StringBuilder ans = new StringBuilder();
        int[][] sorted = new int[128][2];
        for (int i = 0; i < sorted.length; i++) {
            sorted[i][0] = i;  //第一列元素的值用"索引"赋值，用于标识"各个字符"
        }
        for (int i = 0; i < s.length(); i++) {
            //自动将字符a b等转换为对应的 ASCII码（int）
            sorted[s.charAt(i)][1]++;     //基于"字符的标识"，来累加各个字符出现的频次
        }
        Arrays.sort(sorted, (o1, o2) -> { //基于各个字符出现的频次Times对数组排序，期间字符和字符的频次的对应关系不会变化，其相当于一行
            if (o1[1] != o2[1])    //二维数组每一行均为一个 数组
                return o2[1] - o1[1];
            else
                return o1[0] - o2[0];  //直接基于 字符转换对应的 ASCII 码进行比较，A.compare(B)的本质
        });
        //排序后，索引已经和 o[1] 的关系断裂
        for (int i = 0; i < 128; i++) {  //排序后，索引小的频次大，整体呈现下降趋势（因为每一位标识一个字符，可能存在多个字符的频次相等，故趋势会有平移的情况），到某个位置归为 0
            int times = sorted[i][1];
            if (times != 0) {
                while (times > 0) {
                    ans.append((char) sorted[i][0]);  // 将此 频次对应的字符 添加至结果
                    times--;
                }
            }
        }
        return ans.toString();
    }

    /**
     * 1005. K 次取反后最大化的数组和
     */
    public int largestSumAfterKNegations(int[] nums, int k) {
        int sum = 0;
        int minPlus = Integer.MAX_VALUE;
        int[] buckets = new int[202];
        for (int i = 0; i < nums.length; i++) {
            buckets[nums[i] + 100]++;
            if (nums[i] > 0)
                minPlus = Math.min(minPlus, nums[i]);
            sum += nums[i];
        }
        // 1、针对负数进行处理
        for (int i = 0; i < 100; i++) {
            if (buckets[i] != 0) {
                int freq = Math.min(k, buckets[i]);  //巧妙
                sum -= 2 * (i - 100) * freq;
                k -= freq;
                buckets[i] -= freq;
                // 100 - i为对应的正数，+ 100将此正数投射至区间 [100 , 200]
                buckets[100 - i + 100] += freq;             //容易考虑不到的点，注意最后的 +100，是反转的目的，及将其映射到100-200坐标内
                minPlus = Math.min(minPlus, 100 - i);       //容易考虑不到的点，注意这里不需要 +100，这里要的就是原始值
                //记录最小正数的目的，就是最后 k 为奇数时，反转一次，如果记录比较复杂，可直接用 处理负数的方式进行处理，用区间遍历的方式
            }
            if (k == 0)
                return sum;
        }

        // 2、针对 0 进行处理
        if (buckets[100] != 0 || (k & 1) == 0)  //或剩余偶数次
            return sum;

        // 3、针对正数进行处理
        return sum - 2 * (minPlus);
    }

    //---------------------------------------------------------------------------
    // 桶排序，由于题目要求，需要对元素进行排序，故分桶要素是元素值，桶内元素为元素频次
    //      上下解题均为桶排序，应为桶排序的核心在于，无需排序，只需按照一定原则记录数据，同时按照顺序获取数据就能到达获取的为有序数据
    //---------------------------------------------------------------------------

    public int largestSumAfterKNegations01(int[] nums, int k) {
        int sum = 0;
        int[] buckets = new int[202];
        for (int i = 0; i < nums.length; i++) {
            buckets[nums[i] + 100]++;
            sum += nums[i];
        }
        // 1、针对负数进行处理
        for (int i = 0; i < 100; i++) {
            if (buckets[i] != 0) {
                int freq = Math.min(k, buckets[i]);  //巧妙
                sum -= 2 * (i - 100) * freq;
                k -= freq;
                buckets[i] -= freq;
                buckets[100 - i + 100] += freq;             //容易考虑不到的点，注意最后的 +100，是反转的目的，及将其映射到100-200坐标内
            }
            if (k == 0)
                return sum;
        }

        // 2、针对 0 进行处理
        if (buckets[100] != 0 || (k & 1) == 0)  //或剩余偶数次
            return sum;

        // 3、针对正数进行处理
        for (int i = 101; i < 202; i++) {
            if (buckets[i] != 0) {
                sum -= 2 * (i - 100);
                break;   //重要
            }
        }
        return sum;
    }


    public int largestSumAfterKNegations02(int[] nums, int k) {
        int sum = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int num : nums) {
            sum += num;
            hTable.put(num, hTable.getOrDefault(num, 0) + 1);
        }
        // 1、负数的情况
        for (int i = -100; i < 0; i++) {
            if (hTable.containsKey(i)) {
                int freq = Math.min(k, hTable.get(i));
                sum -= 2 * freq * i;
                hTable.put(i, hTable.get(i) - freq);
                hTable.put(-i, hTable.getOrDefault(-i, 0) + freq);
                k -= freq;
            }
            if (k == 0)
                return sum;
        }
        // 2、0 的情况
        if (hTable.containsKey(0) || (k & 1) == 0)
            return sum;

        // 3、正数的情况
        for (int i = 1; i < 101; i++) {
            if (hTable.containsKey(i)) {
                sum -= 2 * i;
                break;
            }
        }
        return sum;
    }


    /**
     * 220. 存在重复元素 III
     */
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        TreeSet<Long> windowTree = new TreeSet<>();
        int left = 0;
        for (int right = 0; right < nums.length; right++) {
            if (right - left > k) {
                windowTree.remove((long) nums[left]);
                left++;
            }
            if (windowTree.size() > 0) {
                Long floor = windowTree.floor((long) nums[right]);    //距离当前元素最近的两个元素
                Long ceiling = windowTree.ceiling((long) nums[right]);
                if ((floor != null && Math.abs(floor - nums[right]) <= t) || (ceiling != null && Math.abs(ceiling - nums[right]) <= t))
                    return true;
            }
            windowTree.add((long) nums[right]);
        }
        return false;
    }

    // 桶排序
    public boolean containsNearbyAlmostDuplicate01(int[] nums, int k, int t) {
        int n = nums.length;
        HashMap<Long, Long> buckets = new HashMap<>();  //Key桶编号，Value桶内元素值，注意桶内最多只有一个元素，否则就直接返回 true了
        int bucketSize = t + 1;  //基于"桶的大小"对数轴进行切分，划分为不同的桶
        // + 1的本质是因为差值为 t 两个数在数轴上相隔距离为 t + 1，它们需要被落到同一个桶中
        for (int i = 0; i < nums.length; i++) {
            long bucketID = getBucketID(nums[i], bucketSize);
            if (buckets.containsKey(bucketID))
                return true;
            long leftBucket = bucketID - 1;
            long rightBucket = bucketID + 1;
            if (buckets.containsKey(leftBucket) && Math.abs(nums[i] - buckets.get(leftBucket)) <= t)
                return true;
            if (buckets.containsKey(rightBucket) && Math.abs(nums[i] - buckets.get(rightBucket)) <= t)
                return true;
            buckets.put(bucketID, (long) nums[i]);
            if (i >= k)    //等于的时候，就要剔除，下一轮直接插入
                buckets.remove(getBucketID(nums[i - k], bucketSize));
        }
        return false;
    }

    private long getBucketID(int num, int size) {
        return num >= 0 ? num / size : ((num + 1) / size) - 1;
    }


    /**
     * 448. 找到所有数组中消失的数字
     */
    public List<Integer> findDisappearedNumbers(int[] nums) {
        int len = nums.length;
        ArrayList<Integer> ans = new ArrayList<>();
        int[] buckets = new int[len + 1];  //保证可存储 len
        for (int num : nums) {
            buckets[num] = 1;
        }
        for (int i = 1; i < buckets.length; i++) {  //忽略 0
            if (buckets[i] == 0)
                ans.add(i);
        }
        return ans;
    }


    /**
     * 2170. 使数组变成交替数组的最少操作数
     */
    public int minimumOperations(int[] nums) {
        int[] countA = new int[100001];
        int[] countB = new int[100001];
        //赋值
        for (int i = 0; i < nums.length; i++) {
            if ((i & 1) == 0)  //偶数位
                countB[nums[i]]++;
            else               //奇数位
                countA[nums[i]]++;
        }
        //分别记录奇数位和偶数位，频次前两位的两个数
        int ATop1 = 0;
        int ATop2 = 0;
        int BTop1 = 0;
        int BTop2 = 0;
        for (int i = 0; i < 100001; i++) {  //桶排序的思想
            if (countA[i] > countA[ATop1]) {
                ATop2 = ATop1;
                ATop1 = i;
            } else if (countA[i] > countA[ATop2]) {
                ATop2 = i;
            }

            if (countB[i] > countB[BTop1]) {
                BTop2 = BTop1;
                BTop1 = i;
            } else if (countB[i] > countB[BTop2]) {
                BTop2 = i;
            }
        }
        //记录最大频次和
        int maxFreq = ATop1 == BTop1 ?
                Math.max(countA[ATop1] + countB[BTop2], countA[ATop2] + countB[BTop1]) : countA[ATop1] + countB[BTop1];

        return nums.length - maxFreq;
    }

    /**
     * 75. 颜色分类
     */
    public void sortColors(int[] nums) {  //桶排序
        int[] buckets = new int[3];
        for (int num : nums) {
            buckets[num]++;
        }
        int currentIndex = 0;
        for (int i = 0; i < buckets.length; i++) {
            while (buckets[i] > 0) {
                nums[currentIndex] = i;
                currentIndex++;
                buckets[i]--;
            }
        }
    }


    /**
     * 781. 森林中的兔子
     */
    public int numRabbits(int[] answers) {
        //分组，消耗组内元素，一旦新分配一个组，无论组内元素是否占满，均应该记录整个组的长度，因为有些兔子没有报数
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

    public int numRabbits01(int[] answers) {
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

    /**
     * 1636. 按照频率将数组升序排序
     */
    public int[] frequencySort(int[] nums) {
        int[][] buckets = new int[202][2];
        for (int i = 0; i < 202; i++) {
            buckets[i][0] = i;
        }
        for (int num : nums) {
            buckets[num + 100][1]++;
        }
        Arrays.sort(buckets, (o1, o2) -> {
            //先按频次升序，后按值降序
            return o1[1] != o2[1] ? o1[1] - o2[1] : o2[0] - o1[0];
        });
        int currentIndex = 0;
        for (int[] bucket : buckets) {
            int freq = bucket[1];
            int num = bucket[0] - 100;
            while (freq != 0) {
                nums[currentIndex] = num;
                currentIndex++;
                freq--;
            }
        }
        return nums;
    }


    /**
     * 819. 最常见的单词
     */
    public String mostCommonWord(String paragraph, String[] banned) {   //细节很多
        String regEx = "[!?',;.]";
        String[] words = Pattern.compile(regEx).matcher(paragraph).replaceAll(" ").split(" ");//会存在  a,b 所以应该将标点符号替换为 " "而不是 ""，会导致两个单词串在一起
        List<String> ban = Arrays.asList(banned);
        HashMap<String, Integer> hTable = new HashMap<>();
        for (String word : words) {
            String lower = word.toLowerCase();
            if (word.equals("") || ban.contains(lower))   //过滤掉禁用词
                continue;
            hTable.put(lower, hTable.getOrDefault(lower, 0) + 1);
        }
        ArrayList<Map.Entry<String, Integer>> sorted = new ArrayList<>(hTable.entrySet());
        //降序排序
        sorted.sort((o1, o2) -> o2.getValue() - o1.getValue());
        Iterator<Map.Entry<String, Integer>> iterator = sorted.iterator();
        Map.Entry<String, Integer> next = iterator.next();
        return next.getKey();
    }


    public String mostCommonWord01(String paragraph, String[] banned) {  //很好的桶排序思路
        String regEx = "[!?',;.]";
        String[] words = Pattern.compile(regEx).matcher(paragraph).replaceAll(" ").split(" ");
        List<String> ban = Arrays.asList(banned);
        HashMap<String, Integer> hTable = new HashMap<>();
        int maxFreq = 0;
        for (String word : words) {
            String lower = word.toLowerCase();
            if (word.equals("") || ban.contains(lower))   //过滤掉禁用词
                continue;
            hTable.put(lower, hTable.getOrDefault(lower, 0) + 1);
            maxFreq = Math.max(maxFreq, hTable.get(lower));
        }
        //桶排序
        String[] buckets = new String[maxFreq + 1];
        for (String word : hTable.keySet()) {
            Integer freq = hTable.get(word);
            buckets[freq] = word;
        }
        //数据有序入桶，取数有序取数
        for (int i = maxFreq; i > 0; i--) {
            if (!ban.contains(buckets[i])) {
                return buckets[i];
            }
        }
        return "";
    }


    /**
     * 面试题 01.02. 判定是否互为字符重排
     */
    public boolean CheckPermutation(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        int[] xx = new int[256];
        int[] yy = new int[256];
        for (int i = 0; i < s1.length(); i++) {
            xx[s1.charAt(i)]++;
            yy[s2.charAt(i)]++;
        }
        return Arrays.equals(xx, yy);
    }


    /**
     * 347. 前 K 个高频元素
     */
    public int[] topKFrequent(int[] nums, int k) {   //桶排序，频次排序
        int maxFreq = 0;
        int[] ans = new int[k];
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int num : nums) {
            hTable.put(num, hTable.getOrDefault(num, 0) + 1);
            maxFreq = Math.max(maxFreq, hTable.get(num));
        }
        ArrayList<Integer>[] buckets = new ArrayList[maxFreq + 1];
        for (int num : hTable.keySet()) {
            Integer freq = hTable.get(num);
            if (buckets[freq] == null) {
                buckets[freq] = new ArrayList<>();
            }
            buckets[freq].add(num);
        }
        int currentIndex = 0;
        for (int i = maxFreq; i >= 0; i--) {
            if (buckets[i] == null || buckets[i].size() == 0) continue;
            ArrayList<Integer> curr = buckets[i];
            for (int num : curr) {
                ans[currentIndex] = num;
                currentIndex++;
                if (currentIndex == k) return ans;
            }
        }
        return ans;
    }


    /**
     * 347. 前 K 个高频元素
     */
    public int[] topKFrequent01(int[] nums, int k) {
        int[] ans = new int[k];
        PriorityQueue<int[]> sortedQueue = new PriorityQueue<>((o1, o2) -> o1[1] - o2[1]);
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int num : nums) {
            hTable.put(num, hTable.getOrDefault(num, 0) + 1);
        }
        for (int num : hTable.keySet()) {
            Integer freq = hTable.get(num);
            if (!sortedQueue.isEmpty() && sortedQueue.size() >= k) {
                if (freq > sortedQueue.peek()[1]) {
                    sortedQueue.poll();
                    sortedQueue.add(new int[]{num, freq});
                }
            } else {
                sortedQueue.add(new int[]{num, freq});
            }
        }
        for (int i = k - 1; i >= 0 && !sortedQueue.isEmpty(); i--) {
            ans[i] = sortedQueue.poll()[0];
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
     * 1742. 盒子中小球的最大数量
     */
    public int countBalls(int lowLimit, int highLimit) {  //基于桶的想法
        int[] buckets = new int[60];
        while (lowLimit <= highLimit) {
            int sum = 0;
            int nums1 = lowLimit;
            while (nums1 != 0) {
                sum += nums1 % 10;
                nums1 /= 10;
            }
            buckets[sum]++;
            lowLimit++;
        }
        int max = 0;
        for (int i = 0; i < 60; i++) {
            max = Math.max(max, buckets[i]);
        }
        return max;
    }

    public int countBalls01(int lowLimit, int highLimit) {  //基于map的想法，效率略低
        int max = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        while (lowLimit <= highLimit) {
            int sum = 0;
            int nums = lowLimit;
            while (nums != 0) {
                sum += nums % 10;
                nums /= 10;
            }
            map.put(sum, map.getOrDefault(sum, 0) + 1);
            max = Math.max(max, map.get(sum));
            lowLimit++;
        }
        return max;
    }


    /**
     * 1122. 数组的相对排序
     */
    public int[] relativeSortArray(int[] arr1, int[] arr2) {
        int[] buckets = new int[1001];
        ArrayList<Integer> ans = new ArrayList<>();
        //记录频次
        for (int nums : arr1) {
            buckets[nums]++;
        }
        //排序，针对 arr2 中的元素，按照 arr2 中顺序排序
        for (int nums : arr2) {
            while (buckets[nums] > 0) {
                ans.add(nums);
                buckets[nums]--;
            }
        }
        //排序，针对非 arr2 中的元素，按照 升序排序
        for (int i = 0; i < 1001; i++) {
            while (buckets[i] > 0) {
                ans.add(i);
                buckets[i]--;
            }
        }
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = ans.get(i);
        }
        return arr1;
    }

    public int[] relativeSortArray01(int[] arr1, int[] arr2) {   //自定义排序
        ArrayList<Integer> list = new ArrayList<>();
        for (int num : arr1) {
            list.add(num);
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < arr2.length; i++) {
            map.put(arr2[i], i);
        }
        list.sort((o1, o2) -> {
            //1、针对 arr2 中存在的元素，优先按照 arr2 中的顺序升序排序
            if (map.containsKey(o1) || map.containsKey(o2)) {
                return map.getOrDefault(o1, 1001) - map.getOrDefault(o2, 1001);
            }
            //2、针对非 arr2 中的元素，按照升序排序
            return o1 - o2;
        });
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = list.get(i);
        }
        return arr1;
    }


    public int[] relativeSortArray02(int[] arr1, int[] arr2) {   //哈希表
        ArrayList<Integer> ans = new ArrayList<>();
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int nums : arr1) {
            Integer freq = map.getOrDefault(nums, 0);
            map.put(nums, freq + 1);
        }
        for (int nums : arr2) {
            Integer freq = map.get(nums);
            while (freq > 0) {
                ans.add(nums);
                freq--;
            }
            map.remove(nums);
        }
        ArrayList<Map.Entry<Integer, Integer>> entries = new ArrayList<>(map.entrySet());
        entries.sort(Comparator.comparing(Map.Entry::getKey));
        for (Map.Entry<Integer, Integer> tuple : entries) {
            Integer nums = tuple.getKey();
            Integer freqs = tuple.getValue();
            while (freqs > 0) {
                ans.add(nums);
                freqs--;
            }
        }
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = ans.get(i);
        }
        return arr1;
    }


    /**
     * 645. 错误的集合
     */
    public int[] findErrorNums(int[] nums) {
        int[] ans = new int[2];
        int n = nums.length;
        int[] buckets = new int[n + 1];
        for (int num : nums) {
            buckets[num]++;
        }
        for (int i = 1; i <= n; i++) {
            if (buckets[i] == 0) ans[1] = i;
            if (buckets[i] == 2) ans[0] = i;
        }
        return ans;
    }


    /**
     * 1941. 检查是否所有字符出现次数相同
     */
    public boolean areOccurrencesEqual(String str) {
        int n = str.length();
        int[] buckets = new int[26];
        for (int i = 0; i < n; i++) {
            buckets[str.charAt(i) - 'a']++;
        }
        HashSet<Integer> set = new HashSet<>();
        for (int num : buckets) {
            if (num > 0) set.add(num);
        }
        return set.size() == 1;
    }


    /**
     * 2068. 检查两个字符串是否几乎相等
     */
    public boolean checkAlmostEquivalent(String word1, String word2) {
        int[] bucket1 = new int[26];
        int[] bucket2 = new int[26];
        for (int i = 0; i < word1.length(); i++) {
            bucket1[word1.charAt(i) - 'a']++;
        }
        for (int i = 0; i < word2.length(); i++) {
            bucket2[word2.charAt(i) - 'a']++;
        }
        for (int i = 0; i < 26; i++) {
            if (Math.abs(bucket1[i] - bucket2[i]) > 3) return false;
        }
        return true;
    }




}

