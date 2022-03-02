
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
     *
     * @param height
     * @return
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
     * 核心：两个指针均从左侧开始移动，left满足指定条件才可移动，right作为遍历索引一直移动;
     * 因此只需要为left赋初值就好
     *
     * @param nums
     * @return
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

    /**
     * 27. 移除元素
     * 核心：同上
     *
     * @param nums
     * @param val
     * @return
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
     * 滑动窗口/双指针很多时候，通用
     *
     * @param s
     * @return
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

    /**
     * 滑动窗口有两种写法：
     * 1.循环时，左侧固定，右侧动（满足一定条件，停止）
     * 2.循环时，左右两侧都动：
     * <p>########1.滑动/平移
     * <p>########2.动态收缩滑动窗口两个边界
     *
     * @param s
     * @return
     */
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
     *
     * @param nums
     * @return
     */
    public List<List<Integer>> threeSum(int[] nums) {
        //定义一个结果集
        List<List<Integer>> listResult = new ArrayList<>();
        //数组的长度
        int len = nums.length;
        if (len < 3)
            return listResult;
        //数组排序
        Arrays.sort(nums);
        //共三个指针：left、mid、right
        for (int left = 0; left < len; left++) {
            //数组已排序，因此 nums[left] <= nums[mid] <= nums[right]，如果nums[left]>0，故后续遍历一定都sum > 0
            if (nums[left] > 0)
                return listResult;
            int mid = left + 1;
            int right = len - 1;
//            if (left < len - 1 && nums[left] == nums[left + 1])
//                continue;   //注意这里一定不能写为left++，因为left++是在for中控制的，如果这里为left++上下完全对不上
            //注意上下的写法对比，上面的写法，针对[-1,-1,2]，导致mid取不到-1（不合理），下面可以取到
            if (left > 0 && nums[left] == nums[left - 1]) {
                continue;   //注意这里一定不能写为left++，因为left++是在for中控制的，如果这里为left++上下完全对不上
            }
            while (mid < right) {
                int sum = nums[left] + nums[mid] + nums[right];
                if (sum == 0) {
                    //此时三个指针满足条件
                    listResult.add(Arrays.asList(nums[left], nums[mid], nums[right]));
                    //排序数组，相邻值可能会重复，因此要跳过
                    while (mid < right && nums[mid] == nums[mid + 1])      //和行进方向的前一位对比，要一直判断mid和right的关系
                        mid++;
                    while (mid < right && nums[right] == nums[right - 1])  //和行进方向的前一位对比，要一直判断mid和right的关系
                        right--;
                    //sum等于0,则midt右移、right左移
                    mid++;
                    right--;
                } else if (sum < 0)
                    mid++;
                else if (sum > 0)
                    right--;
            }
        }
        return listResult;
    }


    /**
     * 15. 三数之和
     *
     * @param nums
     * @return
     */
    public List<List<Integer>> threeSum09(int[] nums) {
        //定义一个结果集
        ArrayList<List<Integer>> resList = new ArrayList<>();
        int length = nums.length;
        Arrays.sort(nums);
        for (int pos = 0; pos < length; pos++) {
            int left = pos + 1;
            int right = length - 1;
            int temp = 0;
            if (pos > 0 && nums[pos] == nums[pos - 1])  //pos-1的时候已经把pos位置处的所有情况都包含了
                continue;
            while (left < right) {
                //计算本次大循环下，三数的初始值
                temp = nums[pos] + nums[left] + nums[right];
                if (temp == 0) {  //满足条件
                    resList.add(Arrays.asList(nums[pos], nums[left], nums[right]));
                    left++;
                    right--;
                    //剔除重复情况
                    while (left < right && nums[left] == nums[left - 1])   //和行进方向的后一位对比，要一直判断mid和right的关系
                        left++;
                    while (left < right && nums[right] == nums[right + 1]) //和行进方向的后一位对比，要一直判断mid和right的关系
                        right--;
                } else if (temp < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return resList;
    }


    /**
     * 18. 四数之和
     *
     * @param nums
     * @param target
     * @return
     */
    public List<List<Integer>> fourSum(int[] nums, int target) {
        int length = nums.length;
        ArrayList<List<Integer>> targetList = new ArrayList<>();
        int right1 = 0;
        int right2 = 0;
        int temp = 0;
        //对数组排序
        Arrays.sort(nums);
        for (int left1 = 0; left1 < length; left1++) {
            if (left1 > 0 && nums[left1] == nums[left1 - 1])
                continue;
            for (int left2 = left1 + 1; left2 < length; left2++) {
                right1 = left2 + 1;
                right2 = length - 1;
                if (left2 > left1 + 1 && nums[left2] == nums[left2 - 1])
                    //***left2 > left1 + 1保证left2仅仅与和自己已经走过的路径（前一位对比）
                    //其中不包括left2 = left1 + 1时，nums[left2 - 1]就是nums[left1]，如果把这种情况也剔除了那肯定是不合理的
                    //因为去重仅仅是针对left1走过路径中，每个index对应的值是不相等的（left2、right1、right2类同），而不是要保证nums[left1]和nums[left2]的值不同
//                if (nums[left2] == nums[left2 - 1]) //错误
                    continue;
                //计算当前四数和
                while (right1 < right2) {
                    temp = nums[left1] + nums[left2] + nums[right1] + nums[right2];
                    //满足条件
                    if (temp == target) {
                        targetList.add(Arrays.asList(nums[left1], nums[left2], nums[right1], nums[right2]));
                        right1++;
                        right2--;
                        while (right1 < right2 && nums[right1] == nums[right1 - 1])
                            right1++;
                        while (right1 < right2 && nums[right2] == nums[right2 + 1])
                            right2--;
                    } else if (temp < target) {
                        right1++;
                    } else {
                        right2--;
                    }
                }
            }
        }
        return targetList;
    }

    /**
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     * @return
     */
    public int[] merge(int[] nums1, int m, int[] nums2, int n) {
        for (int i = m, j = 0; i < m + n; i++, j++) {
            nums1[i] = nums2[j];
        }
        Arrays.sort(nums1);
        return nums1;
    }

    /**
     * 倒序
     * 88. 合并两个有序数组
     *
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     * @return
     */
    public int[] merge01(int[] nums1, int m, int[] nums2, int n) {
        int p1 = m - 1;
        int p2 = n - 1;
        int write = m + n - 1;
        while (write >= 0) {
            if (p1 < 0) {          //关键：某个数组使用完的情况
                nums1[write] = nums2[p2];
                p2--;
            } else if (p2 < 0) {   //关键：某个数组使用完的情况
                nums1[write] = nums1[p1];
                p1--;
            } else if (nums1[p1] > nums2[p2]) {
                nums1[write] = nums1[p1];
                p1--;
            } else {
                nums1[write] = nums2[p2];
                p2--;
            }
            write--;
        }
        return nums1;
    }

    /**
     * 正序
     *
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     * @return
     */

    public int[] merge02(int[] nums1, int m, int[] nums2, int n) {
        int p1 = 0;
        int p2 = 0;
        int write = 0;
        int[] target = new int[m + n];
        while (write < m + n) {
            if (p1 == m) {    //nums1消耗完
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
        for (int i = 0; i < m + n; i++) {
            nums1[i] = target[i];
        }
        return nums1;
    }

    /**
     * [1,1,0,1,1,1]
     *
     * @param nums
     * @return
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
     *
     * @param s
     * @return
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
     * 16. 最接近的三数之和
     *
     * @param nums
     * @param target
     * @return
     */
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int small = 10000000;  //******
//        int small = Integer.MIN_VALUE;  // 可能溢出，因为会存在 small-aa
//        int small = Integer.MAX_VALUE;  // 可能溢出，因为会存在 small+aa
        for (int index = 0; index < nums.length; index++) {
            int left = index + 1;
            int right = nums.length - 1;
            int sum = 0;
            int min = Integer.MAX_VALUE;
            while (left < right) {
                sum = nums[index] + nums[left] + nums[right];
                if (sum == target) {
                    return sum;
                } else if (sum < target) {
                    left++;
                } else {
                    right--;
                }
//                min = Math.min(Math.abs(sum - target), min);   //和其他index循环中最接近的值比较。
                min = Math.min(Math.abs(sum - target), Math.abs(small - target));   //和其他index循环中最接近的值比较。
                if (min == Math.abs(sum - target)) {
                    small = sum;
                }
            }
        }
        return small;
    }


    /**
     * 55. 跳跃游戏
     *
     * @param nums
     * @return
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


    /**
     * 45. 跳跃游戏 II
     *
     * @param nums
     * @return
     */
    public int jump(int[] nums) {
        int steps = 0;
        int maxPosition = 0; //当前跳跃区间内的num[i]+i可到达的最大位置index;
//        int end = nums[0]; //首次区间右侧是nums[0],本身没问题，但是影响第一次step++（跳到nums[0]时，step也要++）,对比上面
        int end = 0;   //跳跃区间的右边，初始为0，每次计算下次跳跃最大区间时++（++保证能到到当前区间内一个位置，使能到下次最大跳跃区间）
        for (int i = 0; i < nums.length - 1; i++) {     //必须-1；
            maxPosition = Math.max(maxPosition, i + nums[i]);
//            if (maxPosition >= nums.length) return steps;
            if (i == end) {           //当遍历当前跳跃区间后（i到达区间右边界），更新下一轮跳跃区间的右边界
                // i==0相当是，[-100,0]跳跃区间内的右边界
                end = maxPosition;    //更新下一轮跳跃区间的右边界(为此轮跳跃区间内能达到的最大Index)
                steps++;              //跳跃一步
            }
        }
        //遍历结束后，就知道跳跃了几次（触达几次右边界）
        return steps;
    }


    /**
     * 413. 等差数列划分
     *
     * @param nums
     * @return
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
     *
     * @param chars
     * @return
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

    public void reverse(char[] chars, int writeBegin, int writeEnd) {
        while (writeBegin < writeEnd) {
            char temp = chars[writeBegin];
            chars[writeBegin] = chars[writeEnd];
            chars[writeEnd] = temp;
            writeBegin++;
            writeEnd--;
        }
    }


}
