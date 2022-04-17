package leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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

}
