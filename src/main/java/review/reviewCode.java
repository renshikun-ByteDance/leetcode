package review;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

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



}
