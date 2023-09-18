package utils.review;

import java.util.*;

public class reviewCode {

    public int search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target)
                return mid;
            else if (nums[left] == target)
                return left;
            else if (nums[right] == target)
                return right;
            else if (nums[mid] <= nums[right]) {  //右侧有序
                if (nums[mid] < target && target < nums[right])
                    left = mid + 1;
                else
                    right = mid - 1;
            } else if (nums[left] <= nums[mid]) {
                if (nums[left] < target && target < nums[mid])
                    right = mid - 1;
                else
                    left = mid + 1;
            }
        }
        return -1;
    }


    public int searchInsert(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (target <= nums[mid])
                right = mid - 1;
            else if (nums[mid] < target)
                left = mid + 1;
        }
        return left;
    }


    public int searchInsert01(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int K = nums.length;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target)
                left = mid + 1;
            else if (target <= nums[mid]) {
                K = mid;
                right = mid - 1;
            }
        }
        return K;
    }


    public int findPeakElement(int[] nums) {
        int index = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[i - 1])
                index = i;
        }
        return index;
    }

    public int findPeakElement01(int[] nums) {
        int[] convertNums = new int[nums.length + 2];
        for (int i = 0; i < convertNums.length; i++) {
            if (i == 0)
                convertNums[i] = Integer.MIN_VALUE;
            else if (i == convertNums.length - 1)
                convertNums[i] = Integer.MIN_VALUE;
            else
                convertNums[i] = nums[i - 1];
        }
        for (int i = 1; i < convertNums.length - 1; i++) {
            if (convertNums[i] > convertNums[i - 1] && convertNums[i] > convertNums[i + 1])
                return i - 1;
        }
        return 0;
    }

    public int findPeakElement02(int[] nums) {
        int[] convert = new int[nums.length + 2];
        convert[0] = Integer.MIN_VALUE;
        convert[convert.length - 1] = Integer.MIN_VALUE;
        System.arraycopy(nums, 0, convert, 1, nums.length);   //数组赋值
        for (int i = 1; i < convert.length - 1; i++) {
            if (convert[i - 1] < convert[i] && convert[i] > convert[i + 1])
                return i - 1;
        }
        return 0;
    }


    public boolean searchMatrix(int[][] matrix, int target) {
        int columns = matrix[0].length - 1;
        for (int row = 0; row < matrix.length; row++) {
            if (matrix[row][0] <= target && target <= matrix[row][columns]) {  //target在此区间
                int left = 0;
                int right = columns;
                while (left <= right) {
                    int mid = left + ((right - left) >> 1);
                    if (matrix[row][mid] == target)
                        return true;
                    else if (matrix[row][mid] < target)
                        left = mid + 1;
                    else if (target < matrix[row][mid])
                        right = mid - 1;
                }
            }
        }
        return false;
    }

    public int[] searchRange(int[] nums, int target) {
        int beginIndex;
        int endIndex;
        int left = 0;
        int right = nums.length - 1;
        //寻找左侧边界
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target)
                left = mid + 1;
            else if (target <= nums[mid])   //返回左侧 target
                right = mid - 1;
        }
        beginIndex = left;
        left = 0;
        right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] <= target)
                left = mid + 1;
            else if (target < nums[mid])
                right = mid - 1;
        }
        endIndex = right;
        if (beginIndex <= endIndex && nums[beginIndex] == target && nums[endIndex] == target)
            return new int[]{beginIndex, endIndex};
        return new int[]{-1, -1};
    }


    public int[] searchRange00(int[] nums, int target) {
        int beginIndex;
        int endIndex;
        int left = 0;
        int right = nums.length - 1;
        //寻找左侧边界
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target)
                right = mid - 1;            //target重复存在，逼近左侧target；target唯一存在，right一步到位，left压在target上
            if (nums[mid] < target)         //target不存在，left跳至大于target的位置上，即target应该插入的位置上
                left = mid + 1;
            else if (target < nums[mid])
                right = mid - 1;
        }
        beginIndex = left;
        left = 0;
        right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target)
                left = mid + 1;              //target重复存在，逼近右侧target;target唯一存在，left一步到位，right压在target上
            if (nums[mid] < target)
                left = mid + 1;
            else if (target < nums[mid])     //target不存在，right跳至小于target的位置
                right = mid - 1;
        }
        endIndex = right;
        if (beginIndex <= endIndex && nums[beginIndex] == target && nums[endIndex] == target)
            return new int[]{beginIndex, endIndex};
        return new int[]{-1, -1};
    }


    public int[] searchRange10(int[] nums, int target) {
        int beginIndex;
        int endIndex;

        int left = 0;
        int right = nums.length - 1;
        while (left < right) {   //区间内逼近，
            int mid = left + ((right - left + 1) >> 1);  //向右侧倾斜
            if (nums[mid] <= target)   //逼近右侧target
                left = mid;
            else if (target < nums[mid])
                right = mid - 1;
        }
        endIndex = left;
        left = 0;
        right = nums.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);  //向左倾斜
            if (nums[mid] < target)
                left = mid + 1;
            else if (target <= nums[mid])
                right = mid;
        }
        beginIndex = right;
        if (0 <= beginIndex && beginIndex <= endIndex && nums[beginIndex] == target && nums[endIndex] == target)
            return new int[]{beginIndex, endIndex};
        return new int[]{-1, -1};
    }


    public int[] searchRange11(int[] nums, int target) {
        int beginIndex;
        int endIndex;

        int left = 0;
        int right = nums.length - 1;
        while (left < right) {   //区间内逼近，
            int mid = left + ((right - left + 1) >> 1);  //向右侧倾斜
            if (nums[mid] == target)     //逼近右侧target
                left = mid;
            else if (nums[mid] < target)   //不建议这样写，在target存在的情况下可控，但是在target不存在的情况下，区间内会有左侧和右侧逼近，不确定
                left = mid + 1;
            else if (target < nums[mid])
                right = mid - 1;
        }
        endIndex = left;
        left = 0;
        right = nums.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);  //向左倾斜
            if (nums[mid] == target)
                right = mid;
            else if (nums[mid] < target)
                left = mid + 1;
            else if (target < nums[mid])
                right = mid - 1;
        }
        beginIndex = right;
        if (0 <= beginIndex && beginIndex <= endIndex && nums[beginIndex] == target && nums[endIndex] == target)
            return new int[]{beginIndex, endIndex};
        return new int[]{-1, -1};
    }


    public boolean isPerfectSquare(int num) {
        int left = 0;
        int right = num;
        int target = num;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            long temp = (long) mid * mid;
            if (temp == target)
                return true;
            else if (temp < target)
                left = mid + 1;
            else
                right = mid - 1;
        }
        return false;
    }


    public int findMin(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < nums[right]) {       //mid落在右侧侧区间
                right = mid;
            } else if (nums[mid] >= nums[right]) {   //mid落在左侧区间
                left = mid + 1;
            }
        }
        return nums[right];
    }


    public int findMin00(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] <= nums[nums.length - 1]) {   //mid落在右侧区间
                right = mid;
            } else if (nums[nums.length - 1] < nums[mid]) {   //mid在左侧区间
                left = mid + 1;
            }
        }
        return nums[right];
    }


    public int minAbsoluteSumDiff(int[] nums1, int[] nums2) {
        int mod = (int) 1e9 + 7;
        long sum = 0;
        long maxDiff = 0;
        int[] sorted = Arrays.copyOf(nums1, nums1.length);
        Arrays.sort(sorted);
        for (int i = 0; i < nums1.length; i++) {
            sum += Math.abs(nums1[i] - nums2[i]);
            //获取nums2中当前元素，在nums1中的相似值
            int leftValue = binSearchLeftValue(sorted, nums2[i]);
            int rightValue = binSearchRightValue(sorted, nums2[i]);
            //----------------------
            // 以下两种写法均正确
            //----------------------
//            int value = Math.min(Math.abs(leftValue - nums2[i]), Math.abs(rightValue - nums2[i]));
//            maxDiff = Math.max(maxDiff, Math.abs(Math.abs(nums1[i] - nums2[i]) - value));
            //判断谁距离nums2[i]近
            int value = Math.abs(leftValue - nums2[i]) > Math.abs(rightValue - nums2[i]) ? rightValue : leftValue;
            maxDiff = Math.max(maxDiff, Math.abs(Math.abs(nums1[i] - nums2[i]) - Math.abs(value - nums2[i])));
        }
        return (int) ((sum - maxDiff) % mod);
    }

    //区间内寻找目标值两侧的值
    public int binSearchLeftValue(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int K = right;
        while (left <= right) {               //左侧逼近
            int mid = left + ((right - left) >> 1);
            if (nums[mid] <= target) {
                K = mid;
                left = mid + 1;
            } else if (target < nums[mid]) {
                right = mid - 1;
            }
        }
        return nums[K];
    }

    public int binSearchRightValue(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int K = right;
        while (left <= right) {      //右侧逼近
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target) {
                left = mid + 1;
            } else if (target <= nums[mid]) {
                K = mid;
                right = mid - 1;
            }
        }
        return nums[K];
    }


    public int minAbsoluteSumDiff01(int[] nums1, int[] nums2) {
        int mod = (int) 1e9 + 7;
        TreeSet<Integer> treeSet = new TreeSet<>();
        long sum = 0;
        long maxDiff = 0;
        for (int num : nums1)
            treeSet.add(num);
        for (int i = 0; i < nums1.length; i++) {
            sum += Math.abs(nums1[i] - nums2[i]);
            Integer floor = treeSet.floor(nums2[i]);
            Integer ceiling = treeSet.ceiling(nums2[i]);

            int closeValue = 0;
            if (floor != null && ceiling != null) {
                if (Math.abs(floor - nums2[i]) > Math.abs(ceiling - nums2[i]))
                    closeValue = ceiling;
                else
                    closeValue = floor;
            } else if (floor != null)
                closeValue = floor;
            else if (ceiling != null)
                closeValue = ceiling;

            maxDiff = Math.max(maxDiff, Math.abs(Math.abs(nums1[i] - nums2[i]) - Math.abs(closeValue - nums2[i])));
        }
        return (int) ((sum - maxDiff) % mod);
    }


    public boolean increasingTriplet(int[] nums) {
        TreeSet<Integer> leftTree = new TreeSet<>();
        TreeSet<Integer> rightTree = new TreeSet<>();
        for (int num : nums)
            rightTree.add(num);

        leftTree.add(nums[0]);
        rightTree.remove(nums[0]);
        for (int i = 1; i < nums.length - 1; i++) {
            //移除当前元素
            rightTree.remove(nums[i]);
            leftTree.remove(nums[i]);
            //[1,1,-2,6]

            //判断相邻值
            Integer floor = leftTree.floor(nums[i]);
            Integer ceiling = rightTree.ceiling(nums[i]);
            if (ceiling != null && floor != null)
                return true;

            //增加当前元素
            leftTree.add(nums[i]);
        }
        return false;
    }


    public boolean increasingTriplet01(int[] nums) {
        int first = nums[0];
        int second = Integer.MAX_VALUE;
        for (int i = 1; i < nums.length; i++) {
            int temp = nums[i];
            if (second < temp)
                return true;
            else if (first < temp && temp < second)
                second = temp;
            else if (temp < first)   //有比最小数first的还小的数temp，此时是为了降低下限，为后续的second做铺垫，但当前的second更可满足temp<second
                first = temp;
        }
        return false;
    }


    /**
     * 373. 查找和最小的 K 对数字
     * 关键：和最小的 K 对数字，可能是 K + n 对，因为最后一对 "重复"；也可能是 不足 K
     */
    public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> ansList = new ArrayList<>();
        int pairValue = 0;
        int len1 = nums1.length - 1;
        int len2 = nums2.length - 1;

        //基于二分法，寻找第 K小的数对和pairValue
        int leftValue = nums1[0] + nums2[0];
        int rightValue = nums1[len1] + nums2[len2];
//        while (leftValue < rightValue) {
//            int midValue = leftValue + ((rightValue - leftValue) >> 1);
//            //计算当前“中值”可容纳的数组对数的个数和
//            int tempK = checkPairValue(nums1, nums2, midValue);
//            if (k <= tempK)    //这种二分法写法，具体算出了多少个K（无需，只需要判断是否够就就行），时间复杂度较高，故采用下面的写法
//                rightValue = midValue;
//            else
//                leftValue = midValue + 1;
//        }

        while (leftValue < rightValue) {
            int midValue = leftValue + ((rightValue - leftValue) >> 1);
            //计算当前“中值”可容纳的数组对数的个数和
            if (check(nums1, nums2, midValue, k))
                rightValue = midValue;
            else
                leftValue = midValue + 1;
        }

        //满足至少K对数对和的值
        pairValue = rightValue;

        //1、先寻找数对和小于pairValue的数对
        for (int i = 0; i < nums1.length; i++) {
            for (int j = 0; j < nums2.length; j++) {
                if (nums1[i] + nums2[j] < pairValue) {   //数对和小于pairValue的情况
                    ArrayList<Integer> temp = new ArrayList<>();
                    temp.add(nums1[i]);
                    temp.add(nums2[j]);
                    ansList.add(temp);
                } else
                    break;
            }
        }

        //2、处理数对和恰等于pairValue的数对情况
        for (int i = 0; i < nums1.length && ansList.size() < k; i++) {  //每次迭代，枚举nums1中的元素，进而确定nums2中的目标target元素，考虑nums2中target会有重复，故要
            //---------------------------------------------------------
            // 考虑target重复的情况，故需要分别逼近左侧 target和右侧 target
            //---------------------------------------------------------
            int repeatBeginIndex = 0;
            int repeatEndIndex = 0;

            int target = pairValue - nums1[i];
            //1、逼近左侧 target
            int left = 0;
            int right = nums2.length - 1;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                if (nums2[mid] < target) {
                    left = mid + 1;
                } else if (target <= nums2[mid]) {  //如果target存在，则逼近左侧target
                    right = mid - 1;
                }
            }
            if (left == nums2.length)
                continue;
            else if (nums2[left] == target)   //target存在
                repeatBeginIndex = left;
            else if (nums2[left] > target)    //target不存在
                continue;

            //2、逼近右侧 target
            left = 0;
            right = nums2.length - 1;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);
                if (nums2[mid] <= target) {     //如果target存在，逼近右侧target
                    left = mid + 1;
                } else if (target < nums2[mid]) {
                    right = mid - 1;
                }
            }
            if (right == -1)
                continue;
            else if (nums2[right] == target)
                repeatEndIndex = right;
            else if (nums2[right] < target)
                continue;
            //------------------------------------------------------------------------------------------
            // 考虑target重复的情况，左侧 target和右侧 target的索引分别为 repeatBeginIndex 和 repeatEndIndex
            //------------------------------------------------------------------------------------------
            for (int j = repeatBeginIndex; j <= repeatEndIndex && ansList.size() < k; j++) {
                ArrayList<Integer> temp = new ArrayList<>();
                temp.add(nums1[i]);
                temp.add(nums2[j]);

                ansList.add(temp);
            }
        }
        return ansList;
    }

    private int checkPairValue(int[] nums1, int[] nums2, int midValue) {
        int k = 0;
        for (int i = 0; i < nums1.length; i++) {
            for (int j = 0; j < nums2.length; j++) {
                if (nums1[i] + nums2[j] <= midValue)
                    k++;
                else
                    break;
            }
        }
        return k;
    }


    public List<List<Integer>> kSmallestPairs123(int[] nums1, int[] nums2, int k) {
        int n = nums1.length;
        int m = nums2.length;
        List<List<Integer>> ans = new ArrayList<>();
        int l = nums1[0] + nums2[0], r = nums1[n - 1] + nums2[m - 1];
        while (l < r) {
            int mid = (int) (0L + l + r >> 1);
            if (check(nums1, nums2, mid, k)) r = mid;
            else l = mid + 1;
        }
        int x = r;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (nums1[i] + nums2[j] < x) {
                    List<Integer> temp = new ArrayList<>();
                    temp.add(nums1[i]);
                    temp.add(nums2[j]);
                    ans.add(temp);
                } else break;
            }
        }
        for (int i = 0; i < n && ans.size() < k; i++) {
            int a = nums1[i], b = x - a;
            int c = -1, d = -1;
            l = 0;
            r = m - 1;
            while (l < r) {
                int mid = (int) (0L + l + r >> 1);
                if (nums2[mid] >= b) r = mid;
                else l = mid + 1;
            }
            if (nums2[r] != b) continue;
            c = r;
            l = 0;
            r = m - 1;
            while (l < r) {
                int mid = (int) (0L + l + r + 1) >> 1;
                if (nums2[mid] <= b) l = mid;
                else r = mid - 1;
            }
            d = r;
            for (int p = c; p <= d && ans.size() < k; p++) {
                List<Integer> temp = new ArrayList<>();
                temp.add(a);
                temp.add(b);
                ans.add(temp);
            }
        }
        return ans;
    }

    boolean check(int[] nums1, int[] nums2, int x, int k) {
        int ans = 0;
        for (int i = 0; i < nums1.length && ans < k; i++) {
            for (int j = 0; j < nums2.length && ans < k; j++) {
                if (nums1[i] + nums2[j] <= x) ans++;
                else break;
            }
        }
        return ans >= k;
    }

    public int pivotIndex02(int[] nums) {
        int preSum = 0;
        int sum = Arrays.stream(nums).sum();
        for (int i = 0; i < nums.length; i++) {
            if (2 * preSum + nums[i] == sum)
                return i;
            preSum += nums[i];
        }
        return -1;
    }

    public int sumOddLengthSubarrays00(int[] nums) {
        int sum = 0;
        int maxWindowLen = (nums.length & 1) == 1 ? nums.length : nums.length - 1;
        for (int window = 1; window <= maxWindowLen; window += 2) {
            int left = 0;
            int right = 0;
            int temp = 0;
            while (right < nums.length) {
                temp += nums[right];
                if (right - left + 1 == window) {
                    sum += temp;
                    temp -= nums[left];
                    left++;
                }
                right++;
            }
        }
        return sum;
    }

    /**
     * 231. 2 的幂
     */
    public boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    public boolean isPowerOfTwo01(int n) {
        if (n <= 0) return false;
        int count = 0;
        for (int i = 0; i < 32; i++) {
            count += (n >> i) & 1;
        }
        return count == 1;
    }


    /**
     * 342. 4的幂
     */
    public boolean isPowerOfFour(int n) {
        String aaa = Integer.toBinaryString(n);
        if (n <= 0) return false;
        int count = 0;
        for (int i = 0; i < 32; i++) {
            if ((i & 1) == 1) {  //奇数位
                if (((n >> i) & 1) == 1)
                    return false;
            } else {
                count += (n >> i) & 1;
                if (count == 2)
                    return false;
            }
        }
        return count == 1;
    }

    public boolean isPowerOfFour01(int n) {
        // 大于 0 && 只有一个 1 && 唯一的 1 在偶数位
        return n > 0 && (n & (n - 1)) == 0 && (n & 0xaaaaaaaa) == 0;  // 1010
    }

    public boolean isPowerOfFour02(int n) {
        // 大于 0 && 只有一个 1 && 唯一的 1 在偶数位
        return n > 0 && (n & (n - 1)) == 0 && (n & 0x55555555) != 0;  // 0101
    }


    /**
     * 191. 位 1的个数
     */
    public int hammingWeight(int n) {
        int count = 0;
        for (int i = 0; i < 32; i++) {  // n 右侧移动，低位比较
            count += (n >> i) & 1;
        }
        return count;
    }

    public int hammingWeight01(int n) {
        int count = 0;
        for (int i = 0; i < 32; i++) {  // 1 左侧移动，逐位比较
            if ((n & (1 << i)) != 0)
                count++;
        }
        return count;
    }

    public int hammingWeight02(int n) {
        int count = 0;
        long numLong = n >= 0 ? n : (long) (n + Math.pow(2, 32));
        while (numLong != 0) {     // 基于公式
            numLong = numLong & (numLong - 1);
            count++;
        }
        return count;
    }

    public int hammingWeight03(int n) {
        int count = 0;
        while (n != 0) {     // 基于公式
            count += n & 1;
            n >>>= 1;
        }
        return count;
    }


    /**
     * 268. 丢失的数字
     */
    public int missingNumber(int[] nums) {
        //-----------------------------
        // 异或：相同的为 0，不同的为 1
        //-----------------------------
        int xor = 0;
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            //------------------------------
            // n ^ n = 0
            // n ^ 0 = n  可用异或判断数字出现的奇偶性
            //------------------------------
            xor ^= i;
            xor ^= nums[i];
        }
        xor ^= n;
        return xor;
    }


    /**
     * 136. 只出现一次的数字
     */
    public int singleNumber(int[] nums) {
        int ans = 0;
        //----------------------------------
        // 异或：两数相同则为 0，不同为 1
        //      n ^ n = 0
        //      n ^ 0 = n
        //----------------------------------
        for (int num : nums) {
            ans ^= num;
        }
        return ans;
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
//            if (times == 4 || times == 1)
            if (times % 3 == 1)    //注意：这里不能写为 上面的判断条件，因为不同的数会在相同的二进制位 均为 1
                ans += (1 << i);
        }
        return ans;
    }


    /**
     * 260. 只出现一次的数字 III
     */
    public int[] singleNumberIII(int[] nums) {
        int res = 0;
        for (int num : nums)
            res ^= num;
        int xor = res == Integer.MIN_VALUE ? res : res & (-res);
        int xx = 0;
        int yy = 0;
        for (int num : nums) {
            if ((num & xor) == 0)
                xx ^= num;
            else
                yy ^= num;
        }
        return new int[]{xx, yy};
    }


    /**
     * 338. 比特位计数
     */
    public int[] countBits(int n) {
        int[] ans = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            int m = i;
            int count = 0;
            while (m != 0) {
                count += m & 1;
                m >>= 1;
            }
            ans[i] = count;
        }
        return ans;
    }

    public int[] countBits01(int n) {
        int[] ans = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            int count = 0;
            for (int mm = 0; mm < 32; mm++) {
                count += (i >> mm) & 1;
            }
            ans[i] = count;
        }
        return ans;
    }

    public int[] countBits02(int n) {
        int[] ans = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            int m = i;
            int count = 0;
            while (m != 0) {
                m = m & (m - 1);
                count++;
            }
            ans[i] = count;
        }
        return ans;
    }


    /**
     * 371. 两整数之和
     */
    public int getSum(int a, int b) {
        while (b != 0) {
            int up = (a & b) << 1;  //进位
            a ^= b;  //本位相加，异或，相同的为 0 ，不同的 为 1
            b = up;
        }
        return a;
    }


    public int getSum01(int a, int b) {
        int sum = 0;
        int add = 0;
        for (int i = 0; i < 32; i++) {
            int aBit = (a >> i) & 1;
            int bBit = (b >> i) & 1;
            if (aBit == 1 && bBit == 1) {
                sum |= (add << i);
                add = 1;
            } else if (aBit == 1 || bBit == 1) {
                if (add == 0) {
                    sum |= (1 << i);
                } else {
                    add = 1;
                }
            } else {
                sum |= (add << i);
                add = 0;
            }
        }
        return sum;
    }


    /**
     * 476. 数字的补数
     */
    public int findComplement(int num) {
        int reverse = 0;
        for (int i = 0; i < 32 && num != 0; i++) {
            reverse |= ((((num) & 1) == 1 ? 0 : 1) << i);
            num >>= 1;
        }
        return reverse;
    }

    public int findComplement01(int num) {
        int reverse = 0;
        for (int i = 0; i < 32 && num != 0; i++) {
            reverse |= ((((num) & 1) == 1 ? 0 : 1) << i);
            num >>= 1;
        }
        return reverse;
    }

    public int findComplement02(int num) {
        int length = Integer.toBinaryString(num).length();
        return num ^ ((1 << length) - 1);
    }


    /**
     * 318. 最大单词长度乘积
     */
    public int maxProduct(String[] words) {
        int maxValue = 0;
        int[] bits = new int[words.length];
        for (int i = 0; i < words.length; i++) {
            int bit = 0;
            for (int j = 0; j < words[i].length(); j++) {
                bit |= 1 << (words[i].charAt(j) - 'a');
            }
            bits[i] = bit;
        }
        for (int i = 0; i < words.length; i++) {
            for (int j = i + 1; j < words.length; j++) {
                if ((bits[i] & bits[j]) == 0) {
                    maxValue = Math.max(maxValue, words[i].length() * words[j].length());
                }
            }
        }
        return maxValue;
    }


    /**
     * 36. 有效的数独
     */
    public boolean isValidSudoku(char[][] board) {
        int[][] rows = new int[9][9];
        int[][] cols = new int[9][9];
        int[][][] areas = new int[3][3][9];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != '.') {
                    int charToNum = board[i][j] - '0' - 1;
                    rows[i][charToNum]++;
                    cols[j][charToNum]++;
                    areas[i / 3][j / 3][charToNum]++;
                    if (rows[i][charToNum] > 1 || cols[j][charToNum] > 1 || areas[i / 3][j / 3][charToNum] > 1)
                        return false;
                }
            }
        }
        return true;
    }

    public boolean isValidSudoku01(char[][] board) {
        int[] rows = new int[9];
        int[] cols = new int[9];
        int[] area = new int[9];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != '.') {
                    int charToNum = board[i][j] - '0' - 1;
                    int index = i / 3 * 3 + j / 3;
                    if (((rows[i] >> charToNum) & 1) == 1 || ((cols[j] >> charToNum) & 1) == 1 || ((area[index] >> charToNum) & 1) == 1)
                        return false;
                    rows[i] |= 1 << charToNum;
                    cols[j] |= 1 << charToNum;
                    area[index] |= 1 << charToNum;
                }
            }
        }
        return true;
    }


    /**
     * 389. 找不同
     */
    public char findTheDifference(String s, String t) {
        int xor = 0;
        for (int i = 0; i < s.length(); i++) {
            xor ^= s.charAt(i);  //异或 ^ : n ^ n = 0，n ^ 0 = n
        }
        for (int i = 0; i < t.length(); i++) {
            xor ^= t.charAt(i);
        }
        return (char) xor;
    }


    /**
     * 1711. 大餐计数
     */
    public int countPairs(int[] deliciousness) {
        int mod = (int) Math.pow(10, 9) + 7;
        int ans = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        int sumTarget = 1 << 21;
        for (int value : deliciousness) {
            for (int sum = 1; sum <= sumTarget; sum <<= 1) {
                ans += hTable.getOrDefault(sum - value, 0);
            }
            hTable.put(value, hTable.getOrDefault(value, 0) + 1);
            ans %= mod;
        }
        return ans;
    }


    /**
     * 190. 颠倒二进制位
     */
    public int reverseBits(int n) {
        int ans = 0;
        ArrayDeque<Integer> dequeQueue = new ArrayDeque<>();
        for (int i = 0; i < 32; i++) {
            dequeQueue.addLast((n >> i) & 1);
        }
        int m = 0;
        while (!dequeQueue.isEmpty()) {
            Integer current = dequeQueue.pollLast();
            ans |= 1 << m;
            m++;
        }
        return ans;
    }

    public int reverseBits01(int n) {
        int ans = 0;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            builder.append((n >> i) & 1);
        }
        StringBuilder reverse = builder.reverse();
        for (int i = 0; i < 32; i++) {
            ans |= (reverse.charAt(i) - '0') << i;
        }
        return ans;
    }


    /**
     * 383. 赎金信
     */
    public boolean canConstruct(String ransomNote, String magazine) {
        int[] ran = new int[26];
        int[] mag = new int[26];
        for (int i = 0; i < ransomNote.length(); i++) {
            ran[ransomNote.charAt(i) - 'a']++;
        }
        for (int i = 0; i < magazine.length(); i++) {
            mag[magazine.charAt(i) - 'a']++;
        }
        for (int i = 0; i < 26; i++) {
            if (ran[i] > mag[i])
                return false;
        }
        return true;
    }

    public boolean canConstruct01(String ransomNote, String magazine) {
        int[] compare = new int[26];
        for (int i = 0; i < magazine.length(); i++) {
            compare[magazine.charAt(i) - 'a']++;
        }
        for (int i = 0; i < ransomNote.length(); i++) {
            if (--compare[ransomNote.charAt(i) - 'a'] < 0)
                return false;
        }
        return true;
    }


    /**
     * 495. 提莫攻击
     */
    public int findPoisonedDuration(int[] timeSeries, int duration) {
        int ans = 0;
        int lastEndTime = 0;
        for (int i = 0; i < timeSeries.length; i++) {
            if (timeSeries[i] >= lastEndTime)
                ans += duration;
            else
                ans += duration - (lastEndTime - timeSeries[i]);
            lastEndTime = timeSeries[i] + duration;
        }
        return ans;
    }


    public int findPoisonedDuration01(int[] timeSeries, int duration) {
        int ans = 0;
        for (int i = 1; i < timeSeries.length; i++) {
            ans += Math.min(timeSeries[i] - timeSeries[i - 1], duration);
        }
        return ans + duration;
    }

    public int findPoisonedDuration02(int[] timeSeries, int duration) {  //错误
        int ans = 0;
        int endTime = 0;
        for (int timePoint : timeSeries) {
            ans += duration - Math.max(endTime, timePoint);
            endTime = timePoint + duration;
        }
        return ans;
    }


    /**
     * 46. 全排列
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        int[] used = new int[nums.length];
        LinkedList<Integer> path = new LinkedList<>();
        permuteDfs(nums, used, ans, path, 0);
        return ans;
    }

    private void permuteDfs(int[] nums, int[] used, List<List<Integer>> ans, LinkedList<Integer> path, int deep) {
        if (deep == nums.length) {  //迭代终止
            ans.add(new ArrayList<>(path));
            return;
        }
        for (int i = 0; i < nums.length; i++) {  //横向枚举搜索
            if (used[i] == 0) { //未被使用
                // 1、增加元素
                path.add(nums[i]);
                used[i] = 1;    //已经使用
                // 2、基于当前路径，纵向搜索
                permuteDfs(nums, used, ans, path, deep + 1);  //纵向递归搜索
                // 3、删除元素，为横向搜索铺垫
                path.removeLast();
                used[i] = 0;
            }
        }
    }


    /**
     * 1005. K 次取反后最大化的数组和
     */
    public int largestSumAfterKNegations(int[] nums, int k) {
        int sum = 0;
        int add = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int num : nums) {
            sum += num;
            hTable.put(num, hTable.getOrDefault(num, 0) + 1);
        }
        for (int i = -100; i < 0; i++) {
            if (hTable.containsKey(i)) {
                int times = Math.min(k, hTable.get(i));
                add += 2 * (-i) * times;
                hTable.put(-i, hTable.getOrDefault(-i, 0) + times);
                k -= times;
                if (k == 0)
                    break;
            }
        }
        if (hTable.containsKey(0) || (k & 1) == 0)
            return sum + add;

        for (int i = 1; i < 101; i++) {
            if (hTable.containsKey(i)) {
                add -= 2 * i;
                break;
            }
        }
        return sum + add;
    }


    public int largestSumAfterKNegations01(int[] nums, int k) {
        int sum = 0;
        int[] buckets = new int[202];
        for (int num : nums) {
            sum += num;
            buckets[num + 100]++;  //桶排序：分桶要素为元素值，桶内为元素的频次
        }
        //1、[-100,0) 的处理 => [0,100)的处理
        for (int i = 0; i < 100; i++) {
            if (buckets[i] == 0)
                continue;
            int times = Math.min(buckets[i], k);
            sum += 2 * (-(i - 100)) * times;
            buckets[-(i - 100) + 100] += times;
            k -= times;
            if (k == 0) return sum;
        }

        //2、0 => 100
        if (buckets[100] != 0 || (k & 1) == 0)
            return sum;

        //3、[101,200]
        for (int i = 101; i < buckets.length; i++) {
            if (buckets[i] != 0) {
                sum += 2 * (-(i - 100));
                return sum;
            }
        }
        return sum;
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
     * 1996. 游戏中弱角色的数量
     */
    public int numberOfWeakCharacters(int[][] properties) {
        int ans = 0;
        Arrays.sort(properties, (o1, o2) -> {
            if (o1[0] != o2[0]) //攻击值不同的情况
                return o2[0] - o1[0];  //按照攻击值降序排序
            else                //攻击值相同的情况
                return o1[1] - o2[1];  //按照防御值升序排序
            //-------------------------------------
            // A B攻击值相同，防御值升序排序的好处在于 A/C 更容易成为弱角色
            //-------------------------------------
        });
        int currentDefense = properties[0][1];
        for (int i = 1; i < properties.length; i++) {
            if (currentDefense > properties[i][1])
                ans++;
            currentDefense = Math.max(currentDefense, properties[i][1]);
        }
        return ans;
    }

    public int numberOfWeakCharacters01(int[][] properties) {
        Arrays.sort(properties, (o1, o2) -> {
            return o1[0] != o2[0] ? (o1[0] - o2[0]) : (o2[1] - o1[1]);   //攻击值升序，攻击值相同，按照防御值降序
        });
        int ans = 0;
        Deque<Integer> st = new ArrayDeque<Integer>();
        //------------------------------------
        // 栈中的元素，应该是无序的
        //    如果 A/B攻击值不同， A[200,100]  B[100,200/50]，那么按照遍历的顺序将防御值写入栈时，应该不是有序的
        //    如果 A/B攻击值相同， A[200,100]  B[200,50]，按照防御值降序，那么按照遍历的顺序将防御值写入栈时，锥顶小，锥底大，但也仅限于这种情况
        //        全局来看栈，应该是无序的，不清楚官方的含义
        //------------------------------------
        for (int[] p : properties) {
            while (!st.isEmpty() && st.peek() < p[1]) {
                st.pop();  //pop的元素为 弱角色
                ans++;
            }
            st.push(p[1]);   //添加防御值，新添加的都是相交前一个更小的，因此从锥顶到锥底，为升序？？
        }
        return ans;
    }


}
