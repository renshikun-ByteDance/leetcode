package leetcode.algorithm;

import java.util.*;

public class prefixSum {


    /**
     * 930. 和相同的二元子数组
     */
    public int numSubarraysWithSum(int[] nums, int goal) {
        int preSum = 0;
        int totalNum = 0;
        //实时维护当前pos位置前一位的前缀和preSum及num个数
        HashMap<Integer, Integer> hTable = new HashMap<>();
        //第一项
        for (int pos = 0; pos < nums.length; pos++) {
            //第一项进来，添加 0->1，用于后面判断nums[0]自身的情况
            hTable.put(preSum, hTable.getOrDefault(preSum, 0) + 1);
            //前缀和（包含枚举值的前缀和，即为累加值）
            preSum += nums[pos];//第一项时，即为本身，后续每枚举一位，均为包含自身的累加值
            //假设原数组的前缀和数组为sum，且子数组 (i,pos] 的区间和为 goal，那么sum[pos] − sum[i] = goal。因此我们可以枚举 pos ，每次查询满足该等式的 i 的数量
            totalNum += hTable.getOrDefault(preSum - goal, 0);//sum[pos] − sum[i] = goal，等式变换为sum[pos] − goal = sum[i]，看hTable中已经存在了多少个sum[i]，因此区间不是以nums[0]开始的
        }
        return totalNum;
    }


    int sum[];

//    public NumArray(int[] nums) {
//        sum = new int[nums.length];
//        sum[0] = nums[0];
//        for (int i = 1; i < nums.length; i++) {
//            sum[i] = sum[i - 1] + nums[i];
//        }
//    }

    public int sumRange(int left, int right) {
        return left == 0 ? sum[right] : sum[right] - sum[left - 1];
    }


//    public NumArray01(int[] nums) {
//        sum = new int[nums.length + 1];
//        for (int i = 0; i < nums.length; i++) {
//            sum[i + 1] = sum[i] + nums[i];     //sum[i+1]记录[0,i]区间的值
//        }
//    }

    public int sumRange01(int left, int right) {
        return sum[right + 1] - sum[left];
    }


    int[][] preSum;

//    public NumMatrix(int[][] matrix) {
//        //创建二维前缀和数组
//        preSum = new int[matrix.length][matrix[0].length + 1];
//        //初始化前缀和数组
//        for (int i = 0; i < matrix.length; i++) {
//            for (int j = 0; j < matrix[0].length; j++) {   //j 不必< matrix[0].length + 1
//                //j=0时，preSum[i][0]不计算，因为此时的前缀和就是其本身
//                preSum[i][j + 1] = preSum[i][j] + matrix[i][j];
//            }
//        }
//    }

    public int sumRegion(int row1, int col1, int row2, int col2) {
        int sum = 0;
        while (row1 <= row2) {
            sum += preSum[row1][col2 + 1] - preSum[row1][col1];
            row1++;
        }
        return sum;
    }

    /**
     * 二维基于面积的前缀和数组的横纵坐标的前缀点均为左上角matrix[0][0]
     */
    public int NumMatrix01(int[][] matrix) {
        //创建二维前缀和数组
        preSum = new int[matrix.length + 1][matrix[0].length + 1];
        //初始化前缀和数组（基于面积计算）
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                //求取[i+1][j+1]处的前缀和，要基于以及计算过的前缀和（两个减一个）加上当前坐标[i][j]的值
                //0行和0列的不用计算，均为默认值0，因为0列和0行无法构成二维的前缀和面积，仅利用0行和0列的值来计算其余位点的前缀和
                preSum[i + 1][j + 1] = preSum[i][j + 1] + preSum[i + 1][j] - preSum[i][j] + matrix[i][j];
            }
        }
        return 0;
    }

    /**
     * 对于"左上角": [row1][col1]其前缀和在preSum中对应的坐标为[row1 + 1][col1 + 1]
     * 对于"右下角": [row2][col2]其前缀和在preSum中对应的坐标为[row2 + 1][col2 + 1]
     * <p>
     * 因此，在随机求取对角为 [row1][col1]和[row2][col2] 的二维区间和时
     * ## 1.基础二维区间子矩阵面积：对应[0][0]和[row1][col1]的二维区间矩阵面积 -> preSum[row2 + 1][col2 + 1]
     * ## 2.减掉"上侧"二维区间子矩阵面积：对应[0][0]和[row1 - 1][col2]的二维区间矩阵面积 ->  preSum[row1][col2 + 1]
     * ## 2.减掉"左侧"二维区间子矩阵面积：对应[0][0]和[row2][col1 - 1]的二维区间矩阵面积 ->  preSum[row2 + 1][col1]
     * ## 3.重复减掉了重复的"左上角"二维区间子矩阵的面积，故应加上：对应[0][0]和[row1 - 1][col1 - 1]的二维区间和子矩阵 -> preSum[row1][col1]
     */
    public int sumRegion01(int row1, int col1, int row2, int col2) {
        //仔细领悟一下，注释与最终结果的差异
        return preSum[row2 + 1][col2 + 1] - preSum[row1][col2 + 1] - preSum[row2 + 1][col1] + preSum[row1][col1];
//        return preSum[row2 + 1][col2 + 1] - preSum[row1 + 1][col2 + 1] - preSum[row2 + 1][col1 + 1] + preSum[row1 + 1][col1 + 1];
    }


    /**
     * 523. 连续的子数组和（基于前缀和的暴力解法，超时）
     */
    public boolean checkSubarraySum(int[] nums, int k) {
        //创建前缀和数组，proSum中的每一位元素记录nums中截至前一位元素的累加值，preSum[0]弃用，无意义即默认为0
        int[] preSum = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            preSum[i + 1] = preSum[i] + nums[i];  //每一位的前缀和不包含自身
        }
        //暴力解法，对前缀和数组进行多次循环和遍历，
        for (int i = 0; i < preSum.length; i++) { //逐个枚举前缀和，确定连续子数组的左侧边界
            for (int j = i + 2; j < preSum.length; j++) { //逐个枚举前缀和，确定连续子数组的右侧边界，且二者间相差2
                int sum = preSum[j] - preSum[i];  //preSum[0]就相当于，sum就想当于从头开始算
                if (sum % k == 0)
                    return true;
            }
        }
        return false;
    }


    /**
     * 523. 连续的子数组和（基于前缀和的简约解法（基于HashSet），超时）
     */
    public boolean checkSubarraySum01(int[] nums, int k) {
        //创建前缀和数组，proSum中的每一位元素记录nums中截至前一位元素的累加值，preSum[0]弃用，无意义即默认为0
        int[] preSum = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            preSum[i + 1] = preSum[i] + nums[i];  //每一位的前缀和不包含自身
        }
        HashSet<Integer> hTable = new HashSet<>();
        for (int i = 2; i < preSum.length; i++) {
            //添加i-2位置处的取余结果
            hTable.add(preSum[i - 2] % k);
            //因为取余结果是根据遍历顺序依次添加的，所以在pos=i处，只会看到preSum的pos在[0,i-2]区间内的取余结果，而且是HashSet的形式存在，无需多次遍历，只需get即可
            if (hTable.contains(preSum[i] % k))
                return true;
        }
        return false;
    }


    /**
     * 523. 连续的子数组和（基于前缀和的简约解法（基于HashMap），超时）
     */
    public boolean checkSubarraySum02(int[] nums, int k) {
        //创建前缀和数组，proSum中的每一位元素记录nums中截至前一位元素的累加值，preSum[0]弃用，无意义即默认为0
        int[] preSum = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            preSum[i + 1] = preSum[i] + nums[i];  //每一位的前缀和不包含自身
        }
        System.out.println(Arrays.toString(preSum));
        HashMap<Integer, Integer> hTable = new HashMap<>();
        hTable.put(0, 0);  //必须有，否则记录不上从pos等于0开始的...如前缀和数组为[0, 23, 25, 29, 35, 41] k=7的情况下，无法记录从0开始前缀和自身就是k的倍数的情况
        for (int i = 1; i < preSum.length; i++) {
            int remind = preSum[i] % k;
            if (hTable.containsKey(remind)) {
                int leftPos = hTable.get(remind);
                if (i - leftPos >= 2)    //如果相邻的preSum的余数相等，则不更新，仍记录首个插入的结果
                    return true;
            } else {
                hTable.put(remind, i);
            }
        }
        return false;
    }


    /**
     * 525. 连续数组
     * 暴力解法，超时
     */
    public int findMaxLength(int[] nums) {
        int maxLength = 0;
        int left = 0;
        while (left < nums.length) {   //枚举左边界，暴力解法，多次遍历数组，时间复杂度高
            int right = left;
            HashMap<Integer, Integer> hTable = new HashMap<>();
            while (right < nums.length) {
                hTable.put(nums[right], hTable.getOrDefault(nums[right], 0) + 1);
                if (hTable.getOrDefault(0, 0).equals(hTable.getOrDefault(1, 0)))
                    maxLength = Math.max(maxLength, right - left + 1);
                right++;
            }
            left++;
        }
        return maxLength;
    }

    /**
     * 525. 连续数组
     * 前缀和 + Hash表
     * 前缀的思想在于，利用不同段的计数作差来得出中间连续子数组的存在
     */
    public int findMaxLength01(int[] nums) {
        int maxLength = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        hTable.put(0, -1);  //和下面计算maxLength相关，前缀和区间右侧端点 + 1才应该是目标区间的左侧端点
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 1) sum++;   //前缀和，记录当前0和1个数的差值;
            else sum--;                //出现0，记为 -1;
            if (hTable.containsKey(sum))  //此时两个前缀和区间内的情况一致，故两个前缀和区间的差值，一定为0.即0和1出现的次数相同
                maxLength = Math.max(maxLength, i - hTable.get(sum));  //不是i - hTable.get(sum) + 1，因为 数组中满足条件的位置应该是[hTable.get(sum) + 1, i]
            else                     //不记录（覆盖）已包含的情况，因为要求最大区间长度
                hTable.put(sum, i);

        }
        return maxLength;
    }


    /**
     * 525. 连续数组
     * 前缀和 + Hash表
     * 前缀的思想在于，利用不同段的计数作差来得出中间连续子数组的存在
     * 先算前缀和，再计算
     * <p>
     * <p>
     * 【同余定理】 【哈希表】【简化前缀和】
     * <p>
     * 同余定理：如果两个整数m、n满足n-m能被k整除，那么n和m对k同余
     * <p>
     * 即 ( pre(j) - pre (i) ) % k == 0 则 pre(j) % k == pre(i) % k
     * <p>
     * 推导 => pre (i) % k = (a0 + a1 + ... + ai) % k = (a0 % k + a1 % k + ... ai % k ) % k （该推导在简化前缀和的时候有用，说明当前前缀和 % k 不会影响后面的前缀和 % k ）
     */
    public int findMaxLength02(int[] nums) {
        int maxLength = 0;
        int[] preSum = new int[nums.length + 1];
        //数组中每个元素i均记录着区间[0, i - 1]的前缀和
        for (int i = 0; i < nums.length; i++)
            preSum[i + 1] = preSum[i] + (nums[i] == 1 ? 1 : -1);
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 2; i < preSum.length; i++) {
            if (!hTable.containsKey(preSum[i - 2]))   //区间长度至少为2，所以记录当前位点前2位的前缀和
                hTable.put(preSum[i - 2], i - 2);
            if (hTable.containsKey(preSum[i]))
                maxLength = Math.max(maxLength, i - hTable.get(preSum[i]));
        }
        return maxLength;
    }


    /**
     * 724. 寻找数组的中心下标
     */
    public int pivotIndex(int[] nums) {
        int total = Arrays.stream(nums).sum();
        int[] preSum = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            preSum[i + 1] = preSum[i] + nums[i];
        }
        for (int i = 1; i < preSum.length; i++) {
            if (preSum[i - 1] == total - preSum[i])   //较为复杂，没有下面的解法直观
                return i - 1;
        }
        return -1;
    }

    public int pivotIndex01(int[] nums) {
        int total = Arrays.stream(nums).sum();
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            if (2 * sum + nums[i] == total)
                return i;
            sum += nums[i];
        }
        return -1;
    }


    /**
     * 1154. 一年中的第几天
     */
    public int dayOfYear(String date) {
        int dayofyear = 0;
        String[] dates = date.split("-", -1);
        int year = Integer.parseInt(dates[0]);
        int month = Integer.parseInt(dates[1]);
        int day = Integer.parseInt(dates[2]);
        int[] monthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0))
            monthDays[1]++;
        for (int i = 0; i < month - 1; i++) {
            dayofyear += monthDays[i];
        }
        dayofyear += day;
        return dayofyear;
    }


    /**
     * 1588. 所有奇数长度子数组的和
     */
    public int sumOddLengthSubarrays(int[] arr) {
        int sum = 0;
        int maxWindowLen = (arr.length & 1) == 1 ? arr.length : arr.length - 1;
        for (int window = 1; window <= maxWindowLen; window += 2) {
            int left = 0;
            int right = 0;
            int presum = 0;
            while (right < arr.length) {
                presum += arr[right];      //窗口右侧元素
                if (right - left + 1 >= window) {
                    sum += presum;         //累加
                    presum -= arr[left];   //剔除下一轮窗口左侧元素值
                    left++;                //维护下一轮窗口左侧位置
                }
                right++;
            }
        }
        return sum;
    }

    public int sumOddLengthSubarrays01(int[] arr) {
        int sum = 0;
        int maxWindowLen = (arr.length & 1) == 1 ? arr.length : arr.length - 1;
        int[] prefixSums = new int[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            prefixSums[i + 1] = prefixSums[i] + arr[i];
        }
        for (int len = 1; len <= maxWindowLen; len += 2) {
            for (int startPos = 0; startPos + len < prefixSums.length; startPos++) {
                int endPos = len + startPos - 1;
                sum += prefixSums[endPos + 1] - prefixSums[startPos];
            }
        }
        return sum;
    }

    public int sumOddLengthSubarrays02(int[] nums) {
        int sum = 0;
        int maxWindow = (nums.length & 1) == 1 ? nums.length : nums.length - 1;
        int[] prefixSum = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        for (int i = 0; i < nums.length; i++) {    //依次固定窗口左侧边界
            for (int window = 1; window <= maxWindow; window += 2) {  //在左侧边界固定的情况下，依次计算奇数长度窗口内的累加和
                int right = window + i - 1;
                if (right < nums.length)
                    sum += prefixSum[right + 1] - prefixSum[i];
            }
        }
        return sum;
    }

    public int sumOddLengthSubarrays03(int[] nums) {
        int ans = 0;
        int[] prefixSum = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        int maxWindow = (nums.length & 1) == 1 ? nums.length : nums.length - 1;
        for (int window = 1; window <= maxWindow; window += 2) {
            int left = 0;
            int right = window + left - 1;
            while (right < nums.length) {
                ans += prefixSum[right + 1] - prefixSum[left];
                right++;
                left++;
            }
        }
        return ans;
    }

    public int sumOddLengthSubarrays04(int[] nums) {
        int ans = 0;
        int maxWindow = (nums.length & 1) == 1 ? nums.length : nums.length - 1;
        for (int window = 1; window <= maxWindow; window += 2) {
            int left = 0;
            int right = 0;
            int prefix = 0;
            while (right < nums.length) {
                prefix += nums[right];
                while (right - left + 1 > window) {
                    prefix -= nums[left];
                    left++;
                }
                if (right - left + 1 == window)
                    ans += prefix;
                right++;
            }
        }
        return ans;
    }

    /**
     * 209. 长度最小的子数组
     */
    public int minSubArrayLen(int target, int[] nums) {
        int ans = Integer.MAX_VALUE;
        if (Arrays.stream(nums).sum() < target) return 0;
        int[] prefixSums = new int[nums.length + 1];
        for (int i = 1; i < prefixSums.length; i++) {
            prefixSums[i] = prefixSums[i - 1] + nums[i - 1];
        }
        for (int i = 0; i < prefixSums.length; i++) {
            for (int j = i + 1; j < prefixSums.length; j++) {
                if (prefixSums[j] - prefixSums[i] >= target)
                    ans = Math.min(ans, j - i);
            }
        }
        return ans;
    }

    public int minSubArrayLen01(int target, int[] nums) {
        if (Arrays.stream(nums).sum() < target) return 0;
        int left = 0;
        int right = 0;
        int sum = 0;
        int minWindow = Integer.MAX_VALUE;
        while (right < nums.length) {
            sum += nums[right]; //滑动窗口右侧，一直向前移动，探索未知区域
            while (left <= right && sum >= target) {  //滑动窗口左侧边界，基于条件移动
                minWindow = Math.min(minWindow, right - left + 1);
                sum -= nums[left];
                left++;
            }
            right++;
        }
        return minWindow;
    }


    public int minSubArrayLen02(int target, int[] nums) {
        int n = nums.length;
        int[] prefixSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {    //区间左边界
            int left = i + 1;
            int right = n;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                if (prefixSum[mid] - prefixSum[i] < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            if (left == n + 1) continue;  //忽略这种情况，即不存在一个以 i 开始的连续子数组的和大于等于 target
            ans = Math.min(ans, left - i);     //区间长度无需 + 1
        }
        return ans == Integer.MAX_VALUE ? 0 : ans;
    }


    /**
     * 560. 和为 K 的子数组
     */
    public int subarraySum(int[] nums, int k) {
        int ans = 0;
        for (int left = 0; left < nums.length; left++) {
            int sum = 0;
            for (int right = left; right < nums.length; right++) {
                sum += nums[right];
                if (sum == k)
                    ans++;
            }
        }
        return ans;
    }


    public int subarraySum01(int[] nums, int k) {
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
     * 661. 图片平滑器
     * 基于 矩阵 的思想
     */
    public int[][] imageSmoother(int[][] img) {
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


    public int[][] imageSmoother01(int[][] img) {  //模拟
        int rows = img.length;
        int columns = img[0].length;
        int[][] ans = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int sum = 0;
                int count = 0;
                for (int x = i - 1; x <= i + 1; x++) {
                    for (int y = j - 1; y <= j + 1; y++) {
                        if (x >= 0 && x < rows && y >= 0 && y < columns) {
                            sum += img[x][y];
                            count++;
                        }
                    }
                }
                ans[i][j] = sum / count;
            }
        }
        return ans;
    }


    public int[][] imageSmoother02(int[][] img) {  //错误
        int rows = img.length;
        int columns = img[0].length;
        int[][] ans = new int[rows][columns];
        int[][] prefixSum = new int[rows + 2][columns + 1];  //列仅补齐左侧，左侧第一列为 0
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                // 列基于前缀和计算
                prefixSum[i + 1][j + 1] += prefixSum[i + 1][j] + img[i][j];  //行号整体偏移一行（不参与 前缀和，仅是坐标）
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int sum = -1;
                int rowWindow = 3;
                int colWindow = 3;
                //行的有效长度
                if (i == 0)
                    rowWindow--;
                if (i == img.length - 1)
                    rowWindow--;
                //列的有效长度
                if (j == 0)
                    colWindow--;
                if (j == img[0].length - 1)
                    colWindow--;

                //计算三行的前缀和
                if (j == 0) {
                    sum = prefixSum[i][j + 2]
                            + prefixSum[i + 1][j + 2]
                            + prefixSum[i + 2][j + 2];
                } else if (j == img[0].length - 1) {
                    sum = prefixSum[i][j + 1] - prefixSum[i][j - 1]
                            + prefixSum[i + 1][j + 1] - prefixSum[i + 1][j - 1]
                            + prefixSum[i + 2][j + 1] - prefixSum[i + 2][j - 1];
                } else {
                    sum = prefixSum[i][j + 2] - prefixSum[i][j - 1]
                            + prefixSum[i + 1][j + 2] - prefixSum[i + 1][j - 1]
                            + prefixSum[i + 2][j + 2] - prefixSum[i + 2][j - 1];
                }
                ans[i][j] = sum / (colWindow * rowWindow);
            }
        }
        return ans;
    }


    /**
     * 1480. 一维数组的动态和
     */
    public int[] runningSum(int[] nums) {
        int[] prefixSum = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        return Arrays.copyOfRange(prefixSum, 1, prefixSum.length); //包含开头，不含结尾索引
    }

    public int[] runningSum01(int[] nums) {  //原地修改
        for (int i = 1; i < nums.length; i++) {
            nums[i] += nums[i - 1];
        }
        return nums;
    }


    /**
     * 238. 除自身以外数组的乘积
     */
    public int[] productExceptSelf(int[] nums) {
        int len = nums.length;
        int[] ans = new int[len];  //长度同原始数组
        int[] prefix = new int[len];  //长度同原始数组
        prefix[0] = 1;
        int[] suffix = new int[len];
        suffix[len - 1] = 1;
        for (int i = 0; i < len - 1; i++) {     //前缀积，不算最后一位，没用，因为前缀积最多用到最后一位之前的乘积，因为当算最后一位的两侧乘积时....
            prefix[i + 1] = prefix[i] * nums[i];//不含索引位置的前缀积
        }
        for (int i = len - 1; i > 0; i--) {     //后缀积，不算第一位，没用，因为
            suffix[i - 1] = suffix[i] * nums[i];//不含索引位置的后缀积
        }
        for (int i = 0; i < len; i++) {
            ans[i] = prefix[i] * suffix[i];  //不含索引位置的前缀指 * 不含索引值的后缀值
        }
        return ans;
    }


    /**
     * 2055. 蜡烛之间的盘子
     */
    public int[] platesBetweenCandles(String s, int[][] queries) {
        int[] ans = new int[queries.length];
        TreeSet<Integer> candles = new TreeSet<>();
        int[] prefix = new int[s.length() + 1];
        //预处理：前缀和 + 蜡烛的索引位置
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '|')
                candles.add(i);
            prefix[i + 1] = prefix[i] + (s.charAt(i) == '|' ? 0 : 1);  //必须括号
        }
        if (candles.size() == 0) return ans;
        //针对每个子字符串处理
        for (int i = 0; i < queries.length; i++) {
            //字符串的边界
            int left = queries[i][0];
            int right = queries[i][1];
            //基于二分法找到字符串有效的边界
            if (candles.ceiling(left) != null && candles.floor(right) != null && candles.ceiling(left) <= candles.floor(right)) {
                ans[i] = prefix[candles.floor(right) + 1] - prefix[candles.ceiling(left)];
            }
        }
        return ans;
    }


    /**
     * 2100. 适合打劫银行的日子
     */
    public List<Integer> goodDaysToRobBank(int[] security, int time) {
        List<Integer> ans = new ArrayList<>();
        int[] left = new int[security.length];
        int[] right = new int[security.length];
        for (int i = 1; i < security.length; i++) {   //计算当前元素，左侧连续"下降"的元素数
            if (security[i] <= security[i - 1])
                left[i] = left[i - 1] + 1;    //前缀和，状态转移
            else
                left[i] = 0;
        }
        for (int i = security.length - 2; i >= 0; i--) {
            if (security[i] <= security[i + 1])
                right[i] = right[i + 1] + 1;  //后缀和，状态转移
            else
                right[i] = 0;
        }
        for (int i = 0; i < security.length; i++) {
            if (left[i] >= time && right[i] >= time)
                ans.add(i);
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
     * 1732. 找到最高海拔
     */
    public int largestAltitude(int[] gain) {
        int max = Math.max(0, gain[0]);
        for (int i = 1; i < gain.length; i++) {
            gain[i] += gain[i - 1];
            max = Math.max(max, gain[i]);
        }
        return max;
    }

    public int largestAltitude01(int[] gain) {   //本质是求前缀和的最大值
        int max = 0;
        int prefixSum = 0;
        for (int height : gain) {
            prefixSum += height;
            max = Math.max(max, prefixSum);
        }
        return max;
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


}
