package leetcode.algorithm;

import java.util.*;

public class Bucket {


    /**
     * 451. 根据字符出现频率排序
     */
    public String frequencySort(String s) {
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


    public String frequencySort01(String s) {
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



}

