package leetcode.algorithm;

import java.util.*;

//双指针
//针对问题：
public class DoublePointer {


    /**
     * 11. 盛最多水的容器
     * 核心：两个指针分别从两侧向中间移动
     * 也可考虑使用下面：三元运算符
     * res = height[i] < height[j] ?
     * Math.max(res, (j - i) * height[i++]):
     * Math.max(res, (j - i) * height[j--]);
     */
    public int maxArea(int[] height) {
        int maxArea = 0;
        int left = 0;
        int right = height.length - 1;
        while (left < right) {
            maxArea = Math.max(maxArea, Math.min(height[left], height[right]) * (right - left));
            if (height[left] <= height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxArea;
    }


    /**
     * 26. 删除有序数组中的重复项
     */
    public int removeDuplicates(int[] nums) {
        if (nums.length == 1) {
            return 1;
        }
        int left = 1;
        for (int right = 1; right < nums.length; right++) {
            if (nums[right] != nums[right - 1]) {
                nums[left] = nums[right];
                left++;
            }
        }
        return left;
    }

    public int removeDuplicates01(int[] nums) {
        if (nums.length == 0) return 0;
        if (nums.length == 1) return 1;
        int left = 1;
        int right = 1;
        while (right < nums.length) {
            if (nums[right] != nums[right - 1])
                nums[left++] = nums[right];       //写入当前判断的right位置的值
            right++;
        }
        return left;
    }

    /**
     * 27. 移除元素
     */
    public int removeElement(int[] nums, int val) {
        int left = 0; //为需要满足条件的指针赋初值
        for (int right = 0; right < nums.length; right++) {
            if (nums[right] != val) {
                nums[left] = nums[right];
                left++;
            }
        }
        return left;
    }


    /**
     * 3. 无重复字符的最长子串
     */
    public int lengthOfLongestSubstring(String s) {
        int left = 0;
        int right = 0;
        int maxLength = 0;
        HashMap<Character, Integer> hTable = new HashMap<>();
        while (right < s.length()) {
            if (hTable.get(s.charAt(right)) != null) {
                left = Math.max(left, hTable.get(s.charAt(right)) + 1);  //目的：获取到最右侧left
            }
            maxLength = Math.max(maxLength, right - left + 1);
            hTable.put(s.charAt(right), right);
            right++;
        }
        return maxLength;
    }

    public int lengthOfLongestSubstring00(String s) {
        int maxLength = 0;
        for (int left = 0; left < s.length(); left++) {
            HashSet<Character> singleChars = new HashSet<>();             //不重复，无序
            for (int right = left; right < s.length(); right++) {
                if (singleChars.add(s.charAt(right))) {                   //无字符，添加
                    maxLength = Math.max(maxLength, right - left + 1);
                    singleChars.add(s.charAt(right));
                } else {                                                  //有此字符，跳出循环，左侧移动一位，时间复杂度高
                    break;
                }
            }
        }
        return maxLength;
    }


    /**
     * 15. 三数之和
     */
    public List<List<Integer>> threeSum(int[] nums) {  //双指针
        List<List<Integer>> ans = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; i++) {  //枚举第一个数字的索引
            //提前终止循环，因为后面不会有满足情况的结
            if (nums[i] > 0) return ans;
            //1.剔除第一个数重复的情况
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int left = i + 1;              //初始值：第二个数字的索引
            int right = nums.length - 1;   //初始值：第三个数字的索引
            while (left < right) {   //从两端收缩
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    ans.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    left++;
                    right--;
                    //2.剔除第二个数重复的情况
                    while (left < right && nums[left] == nums[left - 1]) {
                        left++;
                    }
                    //3.剔除第三个数重复的情况
                    while (left < right && nums[right] == nums[right + 1]) {
                        right--;
                    }
                } else if (sum > 0) {
                    right--;
                } else {
                    left++;
                }
            }
        }
        return ans;
    }


    public List<List<Integer>> threeSum01(int[] nums) {   //DFS搜索，超时
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        Arrays.sort(nums);
        for (int i = 0, j = nums.length - 1; i < j; i++, j--) {  //降序排序，增大剪枝的概率
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
        threeSumDfs(nums, ans, path, 0, 0);
        return ans;
    }

    private void threeSumDfs(int[] nums, List<List<Integer>> ans, LinkedList<Integer> path, int currentIndex, int sum) {
        //迭代终止条件：找到满足题意的解
        if (path.size() == 3 && sum == 0) {
            ans.add(new ArrayList<>(path));
            return;
        }

        //剪枝一：超长
        if (path.size() >= 3) {
            return;
        }

        for (int i = currentIndex; i < nums.length; i++) {  //横向枚举搜索，纵向处理，同一个元素不可多次使用
            //剪枝二：横向剔除重复元素，即同一位置不使用相同的元素
            if (i > currentIndex && nums[i] == nums[i - 1]) {
                continue;
            }

            //剪枝三：不可能满足条件的分支，直接返回
            if (path.size() == 2 && ((sum > 0 && nums[i] >= 0) || (sum < 0 && nums[i] <= 0))) {
                continue;
            }

            //1、增加元素
            path.add(nums[i]);
            sum += nums[i];

            //2、纵向递归搜索
            threeSumDfs(nums, ans, path, i + 1, sum);

            //3、删除元素
            path.removeLast();
            sum -= nums[i];
        }
    }


    //------------------------------------------------------------------------------------------------------------------------------------------------
    // 对比以下两种写法的差异：
    //   if ((long) (nums[i] + nums[nums.length - 3] + nums[nums.length - 2] + nums[nums.length - 1]) < target) {   ##错误写法，已经数组越界了才转 long
    //   if ((long) nums[i] + nums[nums.length - 3] + nums[nums.length - 2] + nums[nums.length - 1] < target) {     ##正确写法
    // 具体测试：
    //        System.out.println(1000000000 + 1000000000 + 1000000000 < 0);               ##越界
    //        System.out.println((long) (1000000000 + 1000000000 + 1000000000) < 0);      ##越界
    //        System.out.println((long) 1000000000 + 1000000000 + 1000000000 < 0);        ##正确结果
    // 下面题目与越界相关的测试案例：
    //        int[] nums = {0, 0, 0, 1000000000, 1000000000, 1000000000, 1000000000}; int target = 1000000000;
    //------------------------------------------------------------------------------------------------------------------------------------------------


    /**
     * 18. 四数之和
     */
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 3; i++) {
            //------------------------------------------
            // 不能加以下的提前结束的逻辑
            //
            //
            // int[] nums = {0, 0, 0, 0}; int target = 0;
            // int[] nums = {-5, -4, -3, -2, 1, 3, 3, 5}; int target = -11;
            //------------------------------------------
            //剪枝一：提前结束
//            if (nums[i] >= target) return ans;
            //-------------------------------------------
            // 忽略重复的第一个数字：
            // 这里剔除的主要重复组合的情况是 仅需要两个值相同的数字中的其中一个，如下面案例的 [2, 4, 5, 9]
            // 如果组合需要两个相同值的数字，则通过移动 i，也仅会保留一个组合，如下面案例的 [2, 2, 5, 11], [2, 2, 7, 9]
            // int[] nums = {2, 2, 4, 5, 7, 9, 11}; int target = 20;
            // 不加下面剔除重复的逻辑对应结果： [[2, 2, 5, 11], [2, 2, 7, 9], [2, 4, 5, 9], [2, 4, 5, 9]]
            //-------------------------------------------
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            //剪枝一：第一个数太小
            if ((long) nums[i] + nums[nums.length - 3] + nums[nums.length - 2] + nums[nums.length - 1] < target) {
                continue;
            }
            //剪枝一：第一个数太大
            if ((long) nums[i] + nums[i + 1] + nums[i + 2] + nums[i + 3] > target) {
                break;
            }
            for (int j = i + 1; j < nums.length - 2; j++) {
                //忽略重复的第二个数字
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }
                //剪枝二：第二个数太小
                if ((long) nums[i] + nums[j] + nums[nums.length - 2] + nums[nums.length - 1] < target) {
                    continue;
                }
                //剪枝二：第二个数太大
                if ((long) nums[i] + nums[j] + nums[j + 1] + nums[j + 2] > target) {
                    break;
                }
                int left = j + 1;
                int right = nums.length - 1;
                while (left < right) {
                    long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];
                    if (sum == target) {
                        ans.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));
                        left++;
                        right--;
                        //忽略重复的第三第四个数字
                        while (left < right && nums[left] == nums[left - 1]) left++;
                        while (left < right && nums[right] == nums[right + 1]) right--;
                    } else if (sum < target) {
                        left++;
                    } else {
                        right--;
                    }
                }
            }
        }
        return ans;
    }


    /**
     * 16. 最接近的三数之和
     */
    public int threeSumClosest(int[] nums, int target) {  //固定一个数，动态搜索两个数字
        Arrays.sort(nums);
        int sumTarget = nums[0] + nums[1] + nums[2];
        for (int i = 0; i < nums.length - 2; i++) {
            //剔除重复的第一个数
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int left = i + 1;
            int right = nums.length - 1;
            while (left < right) {
                int currentSum = nums[i] + nums[left] + nums[right];
                if (currentSum == target) {
                    return currentSum;
                } else if (currentSum < target) {
                    //动态记录
                    if (Math.abs(currentSum - target) < Math.abs(sumTarget - target)) {
                        sumTarget = currentSum;
                    }
                    left++;
                    //剔除重复的第二个元素
                    while (left < right && nums[left] == nums[left - 1]) {
                        left++;
                    }
                } else {
                    //动态记录
                    if (Math.abs(currentSum - target) < Math.abs(sumTarget - target)) {
                        sumTarget = currentSum;
                    }
                    right--;
                    //剔除重复的第三个数
                    while (left < right && nums[right] == nums[right + 1]) {
                        right--;
                    }
                }
            }
        }
        return sumTarget;
    }


    public int threeSumClosest01(int[] nums, int target) {  //固定两个数，二分搜索剩余一个数字
        Arrays.sort(nums);
        int sumTarget = nums[0] + nums[1] + nums[2];
        for (int i = 0; i < nums.length - 2; i++) {
            for (int j = i + 1; j < nums.length - 1; j++) {
                int left = j + 1;
                int right = nums.length - 1;
                while (left <= right) {
                    int mid = left + ((right - left) >> 1);
                    int currentSum = nums[i] + nums[j] + nums[mid];
                    if (currentSum == target) {
                        return currentSum;
                    } else if (currentSum < target) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }
                left = Math.min(left, nums.length - 1);  //右侧越界
                right = Math.max(j + 1, right);  //左侧越界
                int currentSum1 = nums[i] + nums[j] + nums[left];
                int currentSum2 = nums[i] + nums[j] + nums[right];
                if (Math.abs(currentSum1 - target) < Math.abs(sumTarget - target)) {
                    sumTarget = currentSum1;
                }
                if (Math.abs(currentSum2 - target) < Math.abs(sumTarget - target)) {
                    sumTarget = currentSum2;
                }
            }
        }
        return sumTarget;
    }


    /**
     * 88. 合并两个有序数组
     */
    public int[] merge(int[] nums1, int m, int[] nums2, int n) {
        for (int i = m, j = 0; i < m + n; i++, j++) {
            nums1[i] = nums2[j];
        }
        Arrays.sort(nums1);
        return nums1;
    }

    public void merge00(int[] nums1, int m, int[] nums2, int n) {   //正向写入
        int[] target = new int[m + n];
        int p1 = 0;
        int p2 = 0;
        int write = 0;
        while (write < m + n) {
            if (p1 == m) {
                target[write] = nums2[p2];
                p2++;
            } else if (p2 == n) {
                target[write] = nums1[p1];
                p1++;
            } else if (nums1[p1] < nums2[p2]) {
                target[write] = nums1[p1];
                p1++;
            } else {
                target[write] = nums2[p2];
                p2++;
            }
            write++;
        }
        System.arraycopy(target, 0, nums1, 0, target.length);
    }


    /**
     * 88. 合并两个有序数组
     */
    public void merge01(int[] nums1, int m, int[] nums2, int n) {   //反向写入
        int write = m + n - 1;
        int p1 = m - 1;
        int p2 = n - 1;
        while (write >= 0) {
            if (p1 < 0) {
                nums1[write] = nums2[p2];
                p2--;
            } else if (p2 < 0) {
                nums1[write] = nums1[p1];
                p1--;
            } else if (nums1[p1] < nums2[p2]) {
                nums1[write] = nums2[p2];
                p2--;
            } else {
                nums1[write] = nums2[p1];
                p1--;
            }
            write--;
        }
    }


    /**
     * 485. 最大连续 1 的个数
     */
    public int findMaxConsecutiveOnes(int[] nums) {
        int left = 0;
        int right = 0;
        int maxWindow = 0;
        while (right < nums.length) {
            if (nums[right] == 0)
                left = right + 1;
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }


    /**
     * 345. 反转字符串中的元音字母
     */
    public String reverseVowels(String s) {
        char[] chars = s.toCharArray();
        char[] targetC = {'a', 'e', 'i', 'o', 'u'};
        HashSet<Character> target = new HashSet<>();

        for (Character c : targetC) {
            target.add(c);
            target.add(Character.toUpperCase(c));
        }
        int left = 0;
        int right = s.length() - 1;
        while (left < right) {
            if (target.contains(chars[left]) && target.contains(chars[right])) {
                Character temp = chars[left];
                chars[left] = chars[right];
                chars[right] = temp;
                left++;
                right--;
            } else if (!target.contains(chars[left])) {
                left++;
            } else if (!target.contains(chars[right])) {
                right--;
            }
        }
        return new String(chars);
//        return Arrays.toString(chars);
    }

    //上下两种写法效率差异非常大（静态代码块？？）
    static char[] vowels = new char[]{'a', 'e', 'i', 'o', 'u'};
    static Set<Character> set = new HashSet<>();

    static {
        for (char c : vowels) {
            set.add(c);
            set.add(Character.toUpperCase(c));
        }
    }

    public String reverseVowels01(String s) {
        char[] cs = s.toCharArray();
        int n = s.length();
        int l = 0, r = n - 1;
        while (l < r) {
            if (set.contains(cs[l]) && set.contains(cs[r])) {
                swap(cs, l++, r--);
            } else {
                if (!set.contains(cs[l])) l++;
                if (!set.contains(cs[r])) r--;
            }
        }
        return String.valueOf(cs);
    }

    void swap(char[] cs, int l, int r) {
        char c = cs[l];
        cs[l] = cs[r];
        cs[r] = c;
    }




    /**
     * 45. 跳跃游戏 II
     * 本质是寻找最少的起跳点
     */
    public int jump(int[] nums) {  //反向查找
        int ans = 0;
        int end = nums.length - 1;
        while (end > 0) {   //不能有等号，否则死循环
            for (int i = 0; i < end; i++) {  //巧妙，正序查找，且不能从end起跳
                if (i + nums[i] >= end) {
                    end = i;   //更新下一轮循环的右侧边界
                    ans++;
                    break;
                }
            }
        }
        return ans;
    }

    public int jump01(int[] nums) {  //正向查找
        int ans = 0;
        int endIndex = 0;
        int maxIndex = 0;
        for (int i = 0; i < nums.length - 1; i++) {  //不能从目标点起跳，否则就是原地跳
            maxIndex = Math.max(maxIndex, i + nums[i]);      //当前区间中，能跳跃的最远距离
            if (i == endIndex) {  //移动至当前区间右边界
                endIndex = maxIndex;    //更新下一个区间的终点，下一个区间的终点为当前区间内，能跳跃达到的最远距离
                ans++; //从当前区间中的选择的起跳点起跳，起跳点数增加
            }
        }
        return ans;
    }

    public int jump02(int[] nums) {
        int step = 0;
        int begin = 0;
        int end = 1;   //不会计算右侧边界能到达的最远位置，用作挡板
        int maxPosition = 0;
        while (end < nums.length) {  //如果能跳的最远位置已经超出区间，则结束循环，且step是先加的，故直接返回即可
            //跳动
            step++;   //"先"跳，具体跳到那个位置，由下计算得出，而下次跳跃区间的开始点为上此跳跃区间的右侧一位
            //计算本次跳跃（在跳跃区间内[start,end)）能达到的最远距离
            for (int start = begin; start < end; start++) {
                maxPosition = Math.max(maxPosition, start + nums[start]);  //题目中明确能跳出去，故每个跳跃区间内至少有一个位置能保证跳出当前跳跃区间，即更新maxPosition
            }
            //计算下次跳跃区间[begin,end)
            begin = end;             //深入理解，下次跳跃的区间开始位置为上次跳跃能达到的最远位置 + 1
            end = maxPosition + 1;   //不会计算右侧编边界能到达的最远位置，用作挡板
        }
        return step;
    }


    /**
     * 55. 跳跃游戏
     */
    public boolean canJump(int[] nums) {
        int minStep = 1;  //从倒数第二位能跳动1步到最后一位即可
        for (int i = nums.length - 2; i >= 0; i--) {
            if (nums[i] < minStep) {   //如果此位置的**值**不能满足到达目的地的最低标准，则其前一位就要有能力跳过这一位
                minStep++;    //则倒前一位至少要有minStep++的值，才能跳过此位
            } else {    //如果此位置的值能满足到达目的地的最低标准，则其前一位只要保证能跳到此位置就好，则minStep = 1
                minStep = 1;
            }
            if (i == 0 && minStep != 1)
                return false;
        }
        return true;
    }


    public boolean canJump000(int[] nums) {
        if (nums.length == 1) return true;
        int minStep = 1;  //从倒数第二位能跳动1步到最后一位即可
        for (int i = nums.length - 2; i >= 0; i--) {
            if (nums[i] < minStep) {   //如果此位置的**值**不能满足到达目的地的最低标准，则其前一位就要有能力跳过这一位
                minStep++;    //则倒前一位至少要有minStep++的值，才能跳过此位
            } else {    //如果此位置的值能满足到达目的地的最低标准，则其前一位只要保证能跳到此位置就好，则minStep = 1
                minStep = 1;
            }
        }
        return nums[0] >= minStep;
    }




    /**
     * 413. 等差数列划分
     */
    public int numberOfArithmeticSlices(int[] nums) {
        int sum = 0;
        int cycleValue = 0;
        if (nums.length < 3) return 0;
        int left = 0;     //没用上
        int right = 2;                //从index=2开始遍历
        int diff = nums[1] - nums[0]; //初始等差序列的差值
        while (right < nums.length) {
            if (nums[right] - nums[right - 1] == diff)
                cycleValue++;
            else {
                diff = nums[right] - nums[right - 1];    //此时相当于 int diff = nums[1] - nums[0]; //初始等差序列的差值
                //注意：差值变化的地方，可能就是新一轮的等差数列了  如 [...1,2,3,5,7,9,11]  而不是类似[...1,2,3,5,8,11,14]  对比下面的写法
                cycleValue = 0;
//                left = right;
//                right++;
//                cycleValue = 0;
//                if (right < nums.length) {
//                    diff = nums[right] - nums[left];
//                }
            }
            sum += cycleValue;
            right++;
        }
        return sum;
    }


    /**
     * 443. 压缩字符串
     */
    public int compress(char[] chars) {
        int write = 0;  //记录写入的次数
        int tempLen = 0;
        int left = 0;
        int right = 0;
        while (right < chars.length) {
            if (right == chars.length - 1 || chars[right + 1] != chars[right]) {    //right到达数组末端或者不连续
                chars[write++] = chars[right];    //添加字符，写入点
                tempLen = right - left + 1;     //左右指针来表示区间的长度，用right表示待写入的点
                if (tempLen > 1) {                 //添加字符出现的次数
                    int writeBegin = write;
                    while (tempLen > 0) {
                        chars[write++] = (char) (tempLen % 10 + '0');    //难点：取余（最后一位）
                        tempLen /= 10;
                    }
                    int writeEnd = write - 1;
                    reverse(chars, writeBegin, writeEnd);  //从两端到中间交替，这之间仅仅有数字，不会有字符，因为对应的ab等在writeBegin的前一位
                }
                left = right + 1;   //越界时，right也越界了，就不会进入while
            }
            right++;
        }
        return write;
    }

    private void reverse(char[] chars, int writeBegin, int writeEnd) {
        while (writeBegin < writeEnd) {
            char temp = chars[writeBegin];
            chars[writeBegin] = chars[writeEnd];
            chars[writeEnd] = temp;
            writeBegin++;
            writeEnd--;
        }
    }

    public int compress01(char[] chars) {
        StringBuilder ans = new StringBuilder();
        int left = 0;
        int right = 0;
        while (right < chars.length - 1) {
            if (chars[right] != chars[right + 1]) {
                ans.append(chars[left]);
                int len = right - left + 1;
                if (len > 1) {
                    ans.append(len);
                }
                left = right + 1;
            }
            right++;
        }
        if (left == right && right == chars.length - 1) {
            ans.append(chars[right]);
        }
        if (left != right) {
            ans.append(chars[left]);
            int len = right - left + 1;
            if (len > 1) {
                ans.append(len);
            }
        }
        for (int i = 0; i < ans.length(); i++) {
            chars[i]=ans.charAt(i);
        }
        return ans.length();
    }

    /**
     * 1446. 连续字符
     */
    public int maxPower(String s) {
        int len = s.length();
        int window = 0;
        int left = 0;
        int right = 0;
        while (right < len) {
            while (s.charAt(left) != s.charAt(right))
                left++;
            window = Math.max(window, right - left + 1);
            right++;
        }
        return window;
    }



    /**
     * 611. 有效三角形的个数
     */
    public int triangleNumber(int[] nums) {
        int triangleNumber = 0;
        Arrays.sort(nums);
        for (int first = 0; first < nums.length - 2; first++) {//枚举最短边
            for (int left = first + 1; left < nums.length - 1; left++) {//次最短边
                if (nums[first] + nums[left] <= nums[left + 1])   //最短的第三边都大于两边和，则此轮循环不会有满足条件的三角形
                    continue;
                int right = nums.length - 1;//最长边
                while (nums[first] + nums[left] <= nums[right] && left < right) {//不满足三角形的条件
                    right--;
                }
                if (nums[first] + nums[left] > nums[right])  //满足条件
                    triangleNumber += right - (left + 1) + 1;
            }
        }
        return triangleNumber;
    }

    public int triangleNumber11(int[] nums) {
        int triangleNumber = 0;
        Arrays.sort(nums);
        for (int first = 0; first < nums.length - 2; first++) {//枚举最短边
            for (int second = first + 1; second < nums.length - 1; second++) {//次最短边
                int target = nums[first] + nums[second];
                int left = second + 1;
                int right = nums.length - 1;
                while (left <= right) {
                    int mid = left + ((right - left) >> 1);
                    if (nums[mid] < target)
                        left = mid + 1;
                    else
                        right = mid - 1;
                }
                if (left == nums.length) {
                    triangleNumber += (left - 1) - (second + 1) + 1;
                } else if (nums[left] == target && left - 1 != second) {  //即使在存在多个left的值等于target时，left逼近左侧target
                    //最右侧的target，这样写能通过，但是不严谨，因为会有多个left的值等于target，此处left-1的目的是跳过等于的情况，直接来到小于的情况，但-1在多个left的值等于target时，这样的写法就会有问题
                    triangleNumber += (left - 1) - (second + 1) + 1;
                } else if (nums[left] > target && left - 1 != second) {
                    triangleNumber += (left - 1) - (second + 1) + 1;
                }
            }
        }
        return triangleNumber;
    }

    public int triangleNumber12(int[] nums) {
        int triangleNumber = 0;
        Arrays.sort(nums);
        for (int first = 0; first < nums.length - 2; first++) {//枚举最短边
            for (int second = first + 1; second < nums.length - 1; second++) {//次最短边
                int target = nums[first] + nums[second];
                int left = second + 1;
                int right = nums.length - 1;
                int k = second;
                while (left <= right) {
                    int mid = left + ((right - left) >> 1);
                    if (nums[mid] < target) {
                        k = mid;
                        left = mid + 1;
                    } else if (target <= nums[mid])
                        right = mid - 1;
                }
                triangleNumber += (k) - (second + 1) + 1;
            }
        }
        return triangleNumber;
    }


    public int triangleNumber20(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        int ans = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                int left = j + 1, right = n - 1, k = j;
                while (left <= right) {
                    int mid = (left + right) / 2;
                    if (nums[mid] < nums[i] + nums[j]) {
                        k = mid;
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }
                ans += k - j;
            }
        }
        return ans;
    }


    public int triangleNumber000(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        int ans = 0;
        for (int i = 0; i < n - 2; ++i) {
            for (int j = i + 1; j < n - 1; ++j) {
                int k = j + 1;
                while (k < n && nums[k] < nums[i] + nums[j]) {
                    ++k;
                }
                ans += Math.max(k - 1 - (j + 1) + 1, 0);
            }
        }
        return ans;
    }

    public int triangleNumber01(int[] nums) {
        int tnum = 0;
        Arrays.sort(nums);
        for (int left = 0; left < nums.length - 2; left++) { //枚举最短边
            int mid = left + 1;                              //次最短边
            int right = mid + 1;                             //最长边
            while (right < nums.length) {  //right + 1 为最长边
                if (nums[left] + nums[mid] > nums[right])
                    tnum++;
                right++;
            }
        }
        return tnum;
    }


    /**
     * 1013. 将数组分成和相等的三个部分
     */
    public boolean canThreePartsEqualSum(int[] arr) {
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        if (sum % 3 != 0) return false;
        int target = sum / 3;
        int nums = 0;
        int current = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            current += arr[i];
            if (current == target) {
                nums++;
                if (nums == 2) return true;
                current = 0;  //重置
            }
        }
        return false;
    }

    public boolean canThreePartsEqualSum01(int[] arr) {    //双指针，从两头向中间搜索
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        if (sum % 3 != 0) return false;
        int target = sum / 3;
        int left = 0;
        int right = arr.length - 1;
        int nums = 0;
        int current = 0;
        while (left < right) {
            current += arr[left];
            if (current == target) break;          //1、左侧找到目标值
            left++;
        }
        current = 0;
        while (right > left + 1) {  //中间必须留有空位置，所以无需关注中间的目标值怎么构成
            current += arr[right];
            if (current == target) return true;   //2、右侧找到目标值
            right--;
        }
        return false;
    }


    /**
     * 986. 区间列表的交集
     */
    public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {  //双指针
        ArrayList<int[]> ans = new ArrayList<>();
        int i = 0;   //指针一，指向其中一个区间
        int j = 0;   //指针二，指向其中一个区间
        while (i < firstList.length && j < secondList.length) {
            //1、判断两个区间是否有交集
            int begin = Math.max(firstList[i][0], secondList[j][0]);
            int end = Math.min(firstList[i][1], secondList[j][1]);
            if (begin <= end) {   //两个区间存在交集
                ans.add(new int[]{begin, end});
            }
            //2、无论两个区间是否有交集，均需要移动指针，移动原则是保留整体靠后得区间，跳过靠前的区间
            if (firstList[i][1] < secondList[j][1]) {
                i++;
            } else {
                j++;
            }
        }
        return ans.toArray(new int[ans.size()][]);
    }


    /**
     * 1805. 字符串中不同整数的数目
     */
    public int numDifferentIntegers(String word) {
        int left = 0;
        int n = word.length();
        HashSet<String> ans = new HashSet<>();
        while (true) {
            //寻找数字串的左端点
            while (left < n && !Character.isDigit(word.charAt(left))) {
                left++;   //left最终停留在数字串的左端点
            }
            //后续不存在数字
            if (left == n) break;
            //后续存在数字
            int right = left;
            while (right < n && Character.isDigit(word.charAt(right))) {
                right++;  //right最终停留在数字串的右端点的下一位
            }
            //去除前导 0
            while (right - left > 1 && word.charAt(left) == '0') {  //只有在数字串长度大于 1 时才考虑前导 0，单独的 0 不会剔除
                left++;
            }
            ans.add(word.substring(left, right));
            //重置开始搜索的左端点
            left = right;  //关键
        }
        return ans.size();
    }



    /**
     * 1764. 通过连接另一个数组的子数组得到一个数组
     */
    public boolean canChoose(int[][] groups, int[] nums) {   //双指针
        int index1 = 0;
        int index2 = 0;
        while (index1 < groups.length && index2 < nums.length) {
            if (canChooseHelper(groups[index1], nums, index2)) {  //存在
                index2 += groups[index1].length;  //更新在 nums 可搜索的最左侧位置
                index1++;   //寻找下一个目标数组
            } else {
                index2++;   //尝试从 nums 的下一位位置开始匹配 groups[index1]
            }
        }
        return index1 == groups.length;
    }

    private boolean canChooseHelper(int[] target, int[] nums, int index2) { //校验当前目标值数组，是否在 nums 数组中的 index2 位置后存在
        if (index2 + target.length > nums.length) {
            return false;
        }
        for (int i = 0; i < target.length; i++) {
            if (target[i] != nums[index2 + i]) {   //不要更新 index2
                return false;
            }
        }
        return true;
    }


    public boolean canChoose01(int[][] groups, int[] nums) {
        HashMap<Integer, ArrayDeque<Integer>> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            ArrayDeque<Integer> queue = map.getOrDefault(nums[i], new ArrayDeque<>());
            queue.addLast(i);
            map.put(nums[i], queue);
        }
        //作用在 nums 上的指针
        int allIndex = 0;
        for (int m = 0; m < groups.length; m++) {
            //当前目标数组的首个元素值
            int currValue = groups[m][0];
            //获取此元素值在 nums 中的位置索引队列
            ArrayDeque<Integer> queue = map.getOrDefault(currValue, new ArrayDeque<>());
            //剔除相交的情况
            while (!queue.isEmpty() && queue.peekFirst() < allIndex) {
                queue.pollFirst();
            }
            //后续无可用元素
            if (queue.isEmpty()) return false;
            //迭代终止标识
            int pollNext = 1;
            while (!queue.isEmpty() && pollNext == 1) {
                Integer currIndex = queue.pollFirst();
                if (currIndex + groups[m].length > nums.length) {
                    return false;
                }
                for (int i = 0; i < groups[m].length; i++) {  //从 0 开始，虽然第一个元素是相等的，但考虑 group中仅有一个元素时，更新边界
                    if (groups[m][i] == nums[currIndex++]) {
                        if (i == groups[m].length - 1) {  //这一组完全匹配
                            //更新队列
                            if (!queue.isEmpty()) {
                                map.put(currValue, queue);
                            }
                            //更新指针
                            allIndex = currIndex;

                            //后续已经不满足情况
                            if (m < groups.length - 1 && allIndex >= nums.length) {
                                return false;
                            }

                            //更新标识
                            pollNext = 0;   // currValue 对应的 group 已经完成搜索，无需再从 currValue 对应的队列中提取元素
                        }
                    } else {
                        break;
                    }
                }
            }
            if (pollNext == 1) return false;
        }
        return true;
    }


    /**
     * 1813. 句子相似性 III
     */
    public boolean areSentencesSimilar(String sentence1, String sentence2) {  //双端队列
        //-----------------------------------------------------
        // 本题关键：以较短的句子为基准，如果在其左侧、中间、右侧添加一个句子，可以构成较长的句子，则满足条件
        // 因此，可以从两端比较，相同则向内收缩，如果某个队列变为空（即此句子为较短句子，且全部匹配了，故为 true）
        //-----------------------------------------------------
        String[] sen1 = sentence1.split(" ");
        String[] sen2 = sentence2.split(" ");
        ArrayDeque<String> sen1Deque = new ArrayDeque<>();
        ArrayDeque<String> sen2Deque = new ArrayDeque<>();
        for (int i = 0; i < sen1.length; i++) {
            sen1Deque.addLast(sen1[i]);
        }
        for (int i = 0; i < sen2.length; i++) {
            sen2Deque.addLast(sen2[i]);
        }
        //前缀
        while (!sen1Deque.isEmpty() && !sen2Deque.isEmpty() && sen1Deque.peekFirst().equals(sen2Deque.peekFirst())) {
            sen1Deque.pollFirst();
            sen2Deque.pollFirst();
        }
        //后缀
        while (!sen1Deque.isEmpty() && !sen2Deque.isEmpty() && sen1Deque.peekLast().equals(sen2Deque.peekLast())) {
            sen1Deque.pollLast();
            sen2Deque.pollLast();
        }
        return sen1Deque.isEmpty() || sen2Deque.isEmpty();
    }

    public boolean areSentencesSimilar01(String sentence1, String sentence2) {  //双指针
        String[] sen1 = sentence1.split(" ");
        String[] sen2 = sentence2.split(" ");
        int n1 = sen1.length;
        int n2 = sen2.length;
        int i = 0;
        int j = 0;
        //1、从左侧开始匹配收缩
        while (i < n1 && i < n2 && sen1[i].equals(sen2[i])) {
            i++;
        }
        //2、从右侧开始匹配收缩
        while (j < n1 - i && j < n2 - i && sen1[n1 - 1 - j].equals(sen2[n2 - 1 - j])) {
            j++;
        }
        return i + j == Math.min(n1, n2);
    }

    public boolean areSentencesSimilar02(String sentence1, String sentence2) {  //错误写法，关键在没有从两头向内收缩，而是单向搜索
        int index1 = 0;
        int index2 = 0;
        String[] sen1 = sentence1.split(" ");
        String[] sen2 = sentence2.split(" ");
        //1、前缀相等，齐头并进
        while (index1 < sen1.length && index2 < sen2.length && sen1[index1].equals(sen2[index2])) {
            index1++;
            index2++;
        }
        if (index1 == sen1.length || index2 == sen2.length) return true;

        //--------------------------------------------------------------------------
        // 错误关键，不能尝试从左向右跳过不同的元素，会存在特殊案例，案例："A" "a A b A"，而应该严格按照从两头收缩的方式
        //--------------------------------------------------------------------------

        //2、插入句子，重新对齐
        while (index1 < sen1.length && index2 < sen2.length && !sen1[index1].equals(sen2[index2])) {
            if (sen1.length < sen2.length) index2++;
            else index1++;
        }
        //3、后缀相等，齐头并进
        while (index1 < sen1.length && index2 < sen2.length && sen1[index1].equals(sen2[index2])) {
            index1++;
            index2++;
        }
        return index1 == sen1.length && index2 == sen2.length;
    }

}
