import java.util.*;

public class DoublePointer2 {


    /**
     * 11. 盛最多水的容器
     *
     * @param height
     * @return
     */
    public int maxArea(int[] height) {
        int maxArea = 0;
        for (int left = 0; left < height.length - 1; left++) {
            for (int right = left + 1; right < height.length; right++) {
                int hwater = Math.min(height[left], height[right]);
                maxArea = Math.max(maxArea, (right - left) * hwater);
            }
        }
        return maxArea;
    }

    //双指针的解法（非常类似二分法）
    public int maxArea01(int[] height) {
        int maxArea = 0;
        int left = 0;
        int right = height.length - 1;
        while (left < right) {
            int hwater = Math.min(height[left], height[right]);
            maxArea = Math.max(maxArea, (right - left) * hwater);
            if (height[left] < height[right])
                left++;
            else
                right--;
        }
        return maxArea;
    }


    /**
     * 15. 三数之和
     * <p>
     * 本题的核心是：两个数确定，第三个数用二分查找来做，会有较多的无效遍历，近似枚举的时间复杂度
     *
     * @param nums
     * @return
     */
    public List<List<Integer>> threeSum(int[] nums) {
        //定义一个结果集
        ArrayList<List<Integer>> resList = new ArrayList<>();
        Arrays.sort(nums);
        HashSet<HashMap<Integer, Integer>> htable = new HashSet<>();
        for (int first = 0; first < nums.length - 2; first++) {
            for (int second = first + 1; second < nums.length - 1; second++) {
                int left = second + 1;
                int right = nums.length - 1;
                while (left <= right) {
                    int mid = left + ((right - left) >> 1);
                    int tempsum = nums[first] + nums[second] + nums[mid];
                    if (tempsum < 0)
                        left = mid + 1;
                    else
                        right = mid - 1;
                }
                if (left == nums.length) continue;
                else if (nums[first] + nums[second] + nums[left] == 0) {
                    //当前满足条件的数组
                    HashMap<Integer, Integer> arr = new HashMap<>();
                    arr.put(nums[first], nums[second]);
                    //如果此数组已经记录，则跳过
                    if (htable.contains(arr))
                        continue;
                    //返回值
                    resList.add(Arrays.asList(nums[first], nums[second], nums[left]));
                    htable.add(arr);
                }
            }
        }
        return resList;
    }


    /**
     * 通过同一个题，上下两种解法的分析，可知二分法和双指针有着本质的区别：
     *
     * <p>
     * 二者相似点：
     * left和right的赋值为区间两个端点
     * <p>
     * 差异点：
     * 二分法，通过计算相关值，来使得left或right移动，最终返回一个坐标，使其等于或逼近题目中的条件
     * * 二分法：通过两个指针计算中间位置，去寻找一个满足条件的位置（一个点mid）
     * #########二分法的坐标移动方式是：left=mid+1或者right=mid-1，所以称为二分
     * 双指针，通过计算相关值，来使得left或right移动，最终不返回坐标，不过在移动的过程中，计算等于或逼近题目种的条件
     * * 双指针：通过两个指针，去寻找满足条件的位置（两个点left和right）
     * #########双指针的坐标移动方式是：left++或right--
     * <p>
     * 双指针是找到满足条件的两个元素，二分法是找到满足条件的一个元素
     *
     * @param nums
     * @return
     */

    public List<List<Integer>> threeSum01(int[] nums) {
        //定义一个结果集
        ArrayList<List<Integer>> resList = new ArrayList<>();
        Arrays.sort(nums);
        for (int first = 0; first < nums.length - 2; first++) {
            //数组已排序，因此 nums[left] <= nums[mid] <= nums[right]，如果nums[left]>0，故后续遍历一定都sum > 0
            if (nums[first] > 0)
                return resList;
            int left = first + 1;
            int right = nums.length - 1;
            if (first > 0 && nums[first] == nums[first - 1])  //pos-1的时候已经把pos位置处的所有情况都包含了
                continue;
            while (left < right) {
                //两个指针移动
                int tempsum = nums[first] + nums[left] + nums[right];
                if (tempsum == 0) {
                    resList.add(Arrays.asList(nums[first], nums[left], nums[right]));
                    left++;
                    right--;
                    //left使用过的可跳过
                    while (left < right && nums[left] == nums[left - 1])
                        left++;
                    //right使用过的可跳过
                    while (left < right && nums[right] == nums[right + 1])
                        right--;
                } else if (tempsum < 0)
                    left++;
                else if (tempsum > 0)
                    right--;
            }
        }
        return resList;
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
        int threeSumClosest = nums[0] + nums[1] + nums[2];
        for (int first = 0; first < nums.length - 2; first++) {
            if (first > 0 && nums[first] == nums[first - 1])
                continue;
            for (int second = first + 1; second < nums.length - 1; second++) {
                int left = second + 1;
                int right = nums.length - 1;
                while (left <= right) {
                    int mid = left + ((right - left) >> 1);
                    int sum = nums[first] + nums[second] + nums[mid];
                    if (sum < target)
                        left = mid + 1;
                    else
                        right = mid - 1;
                }
                int k = 0;
                if (left == nums.length)              //结合此处的写法，理解二分写法中整合单侧和区间逼近的写法（默认值怎么给）
                    k = nums.length - 1;
                else
                    k = left;
                int rclose = nums[first] + nums[second] + nums[k];
                if (Math.abs(rclose - target) < Math.abs(threeSumClosest - target))
                    threeSumClosest = rclose;
                if (left > 0 && left - 1 != second) {     //必须这样写，否则会导致第三个数==second
                    int m = left - 1;
                    int lclose = nums[first] + nums[second] + nums[m];
                    if (Math.abs(lclose - target) < Math.abs(threeSumClosest - target))
                        threeSumClosest = lclose;
                }
            }
        }
        return threeSumClosest;
    }


    /**
     * 枚举+双指针的解法
     */
    public int threeSumClosest01(int[] nums, int target) {
        Arrays.sort(nums);
        int small = nums[0] + nums[1] + nums[2];
        //枚举
        for (int first = 0; first < nums.length; first++) {
            //跳过重复元素的循环
            if (first > 0 && nums[first] == nums[first - 1])
                continue;
            int second = first + 1;
            int third = nums.length - 1;
            while (second < third) {
                int sum = nums[first] + nums[second] + nums[third];
                //如果存在三数之和等于target则直接返回结果
                if (sum == target)
                    return target;
                if (sum < target) {  //类似二分法的判断方式
                    if (Math.abs(sum - target) < Math.abs(small - target))
                        small = sum;
                    second++;
                    //跳过重复元素
                    while (second < third && nums[second] == nums[second - 1])
                        second++;
                } else if (sum > target) {
                    if (Math.abs(sum - target) < Math.abs(small - target))
                        small = sum;
                    third--;
                    //跳过重复元素
                    while (second < third && nums[third] == nums[third + 1])
                        third--;
                }
            }
        }
        return small;
    }


    /**
     * 18. 四数之和
     *
     * @param nums
     * @param target
     * @return
     */
    public List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums);
        int len = nums.length - 1;
        ArrayList<List<Integer>> targetList = new ArrayList<>();
        for (int first = 0; first <= len - 3; first++) {
            //跳过枚举第一个元素重复的情况
            if (first > 0 && nums[first] == nums[first - 1])
                continue;
            //如果当前枚举的first与数组中最大的三个数之和仍小于target，则证明此first太小，跳过此轮循环，first直接进入下一轮的循环；
//            if ((long) (nums[first] + nums[len - 2] + nums[len - 1] + +nums[len]) < target)   //区别上下两种写法的差异
            if ((long) nums[first] + nums[len - 2] + nums[len - 1] + +nums[len] < target)
                continue;
            //如果当前枚举的first与数组中最小的三个数之和都大于target，则证明此first和之后的数太大，直接跳出循环；
            if ((long) nums[first] + nums[first + 1] + nums[first + 2] + nums[first + 3] > target)
                break;
            for (int second = first + 1; second <= len - 2; second++) {
                //剔除重复枚举第二个元素情况
                if (second > first + 1 && nums[second] == nums[second - 1])
                    continue;
                //此first和second组合的值太小，跳过此轮循环，second直接进入下一轮循环；
                if ((long) nums[first] + nums[second] + nums[len - 1] + nums[len] < target)
                    continue;
                //此first和second组合的值太大，跳出此轮循环
                if ((long) nums[first] + nums[second] + nums[second + 1] + nums[second + 2] > target)
                    break;
                //双指针，分别指向第三个元素和最后一个元素
                int left = second + 1;
                int right = len;
                //通过条件判断来移动双指针，尝试寻找满足条件的两个元素
                while (left < right) {
                    int sum = nums[first] + nums[second] + nums[left] + nums[right];
                    if (sum == target) {
                        targetList.add(Arrays.asList(nums[first], nums[second], nums[left], nums[right]));
                        left++;
                        right--;
                        while (left < right && nums[left] == nums[left - 1])
                            left++;
                        while (left < right && nums[right] == nums[right + 1])
                            right--;
                    } else if (sum < target)
                        left++;
                    else if (sum > target)
                        right--;
                }
            }
        }
        return targetList;
    }


    /**
     * 26. 删除有序数组中的重复项
     *
     * @param nums
     * @return
     */
    public int removeDuplicates(int[] nums) {
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
     *
     * @param nums
     * @param val
     * @return
     */
    public int removeElement(int[] nums, int val) {
        int left = 0;
        int right = 0;
        while (right < nums.length) {
            if (nums[right] != val)
                nums[left++] = nums[right];
            right++;
        }
        return left;
    }


    /**
     * 45. 跳跃游戏 II
     *
     * @param nums
     * @return
     */
    public int jump(int[] nums) {
        int step = 0;
        int begin = 0;
        int end = 1;   //不会计算右侧编边界能到达的最远位置，用作挡板
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

    public int jump01(int[] nums) {
        int steps = 0;
        int maxPosition = 0; //当前跳跃区间内的num[i]+i可到达的最大位置index;
        int end = 0;   //跳跃区间的右边，初始为0，每次计算下次跳跃最大区间时++（++保证能到到当前区间内一个位置，使能到下次最大跳跃区间）
        for (int i = 0; i < nums.length - 1; i++) {     //必须-1，此处的i既是“搜索点”也是“右边界”
            maxPosition = Math.max(maxPosition, i + nums[i]);
            if (i == end) {           //当遍历到当前可跳跃区间右边界时
                //意味着遍历完了当前可跳跃的区间，根据记录的maxPosition可以获得从当前区间的某个位置跳跃，最远可达maxPosition
                end = maxPosition;    //更新下一轮跳跃区间的右边界
//                if (maxPosition >= nums.length) break;
                //下一轮跳跃的左边界不用手动更新，其实自动更新，即当前已经遍历完的区间的右侧一位i++
                steps++;              //跳跃一步，下一轮循环计算跳动的这一步
            }
        }
        //遍历结束后，就知道跳跃了几次（触达几次右边界）
        return steps;
    }

    /**
     * 88. 合并两个有序数组
     *
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int p1 = 0;
        int p2 = 0;
        int pos = 0;
        int[] target = new int[m + n];
        while (pos < m + n) {
            if (p1 == m)
                target[pos++] = nums2[p2++];
            else if (p2 == n)
                target[pos++] = nums1[p1++];
            else if (nums1[p1] <= nums2[p2])
                target[pos++] = nums1[p1++];
            else
                target[pos++] = nums2[p2++];
        }
        System.arraycopy(target, 0, nums1, 0, m + n);
    }


    /**
     * 345. 反转字符串中的元音字母
     *
     * @param s
     * @return
     */
    public String reverseVowels(String s) {
        char[] chars = s.toCharArray();
        char[] vowels = new char[]{'a', 'e', 'i', 'o', 'u'};
        HashSet<Character> hTable = new HashSet<>();
        for (Character c : vowels) {
            hTable.add(c);
            hTable.add(Character.toUpperCase(c));
        }
        //双指针
        int left = 0;
        int right = s.length() - 1;
        while (left < right) {
            if (hTable.contains(chars[left]) && hTable.contains(chars[right])) {
                char temp = chars[left];
                chars[left] = chars[right];
                chars[right] = temp;
                left++;
                right--;
            } else if (!hTable.contains(chars[left]))
                left++;
            else
                right--;
        }
        return new String(chars);
//        return chars.toString();
    }


    /**
     * 395. 至少有 K 个重复字符的最长子串
     *
     * @param s
     * @param k
     * @return
     */
    public int longestSubstring01(String s, int k) {
        char[] chars = s.toCharArray();
        //分割符
        HashSet<Character> set = new HashSet<>();
        //计数map
        HashMap<Character, Integer> charAndNums = new HashMap<>();
        for (Character c : chars) {
            charAndNums.put(c, charAndNums.getOrDefault(c, 0) + 1);
        }
        for (Character c : chars) {
            if (charAndNums.get(c) < k && !set.contains(c))
                set.add(c);
        }
        int maxWindow = 0;
        int left = 0;
        int right = 0;
        while (right < s.length()) {
            HashMap<Character, Integer> moveCharAndNums = new HashMap<>();
            moveCharAndNums.put(chars[right], moveCharAndNums.getOrDefault(chars[right], 0) + 1);
            if (moveCharAndNums.get(chars[right]) >= k)
                maxWindow = Math.max(maxWindow, right - left + 1);

            if (set.contains(chars[right])) {
                //移动到下一个元素
                right++;
                left = right;  //对其归位
            }


        }


        return 0;
    }


    public int longestSubstring00(String s, int k) {
        return dfs(s, k);
    }

    private int dfs(String s, int k) {
        //统计每个字符出现的次数
        int[] charCount = new int[26];
        for (int i = 0; i < s.length(); i++) {
            charCount[s.charAt(i) - 'a']++;
        }

        //统计<k的字符
        String split = "";
        for (int i = 0; i < charCount.length; i++) {
            int count = charCount[i];
            if (count > 0 && count < k) {
                split = String.valueOf((char) (i + 'a'));
                break;
            }
        }

        if (split.equals("")) {
            //全部都>k
            return s.length();
        }

        final String[] split1 = s.split(split);
        int max = 0;
        for (final String s1 : split1) {
            final int dfs = dfs(s1, k);
            max = Math.max(max, dfs);
        }

        return max;
    }


    /**
     * 395. 至少有 K 个重复字符的最长子串
     *
     * @param s
     * @param k
     * @return
     */
    public int longestSubstring(String s, int k) {
        //如果s的长度小于K，则一定不会有满足条件的字串
        if (s.length() < k) return 0;
        //计算当前字符串中各个字符出现的频次
        HashMap<Character, Integer> charsAndNums = new HashMap<>();
        char[] temchars = s.toCharArray();
        for (char c : temchars)
            charsAndNums.put(c, charsAndNums.getOrDefault(c, 0) + 1);
        //遍历频次数组，寻找当前字符串中是否存在频次<K的字符，如有，则递归调用此函数继续切分
        for (char split : charsAndNums.keySet()) {
            //当前字符串s中，字符split的频次 < K
            if (charsAndNums.get(split) < k) {
                //由于字符串s中包含频次小于K的字符，故字符串s不满足题意，因此对其继续递归切分，寻找满足条件的字串
                int max = 0;
                for (String ss : s.split(String.valueOf(split))) {
                    //其中的每一个ss的角色都相当于此轮循环中的s
                    max = Math.max(max, longestSubstring(ss, k));   //递归寻找
                }
                return max;    //******疑问，为什么在这return，
                //这里仅仅是s中的由第一个split分割的字符串的max，后面的都还没算呢
            }
            //其余字符的频次 > K，否则都在上面递归了
        }
        //如果走到这里，说明字符串ssss中所有字符出现的频次都大于等于K，否则都在上面递归，此处return都是上面递归的根节点处（最先满足条件的字串，当前字串在此处长度较长）
        return s.length();
    }


//
//    public int longestSubstring(String s, int k) {
//        if (s.length() < k) return 0;
//        HashMap<Character, Integer> counter = new HashMap();
//        for (int i = 0; i < s.length(); i++) {
//            counter.put(s.charAt(i), counter.getOrDefault(s.charAt(i), 0) + 1);
//        }
//        for (char c : counter.keySet()) {
//            if (counter.get(c) < k) {
//                int res = 0;
//                for (String t : s.split(String.valueOf(c))) {
//                    res = Math.max(res, longestSubstring(t, k));
//                }
//                return res;
//            }
//        }
//        return s.length();
//    }


    /**
     * 413. 等差数列划分
     *
     * @param nums
     * @return
     */
    public int numberOfArithmeticSlices(int[] nums) {
        int sum = 0;
        if (nums.length < 3) return 0;
        int right = 2;
        //初始差值
        int diff = nums[1] - nums[0];
        int valueTime = 0;
        while (right < nums.length) {
            //当前数与前一个数构成等差数列
            if (nums[right] - nums[right - 1] == diff) {
                valueTime++;
                sum += valueTime;
            } else {
                //新等差数列的差值
                diff = nums[right] - nums[right - 1];
                valueTime = 0;
            }
            right++;
        }
        return sum;
    }


    //枚举+双指针求解
    public int numberOfArithmeticSlices01(int[] nums) {
        int sum = 0;
        //一定不满足等差数列
        if (nums.length < 3) return 0;
        //枚举等差数列第一个数
        for (int first = 0; first < nums.length - 2; first++) {
            //第二个数
            int second = first + 1;
            //等差值
            int diff = nums[second] - nums[first];
            //等差数列右边界，初始值为第三个数
            int right = second + 1;
            //寻找等差数列的右侧边界
            while (right < nums.length && nums[right] - nums[right - 1] == diff) {
                right++;
            }
            //循环结束right只有两种情况，一越界，二不满足等差序列的条件，无论如何right--为

//            if (right - 1 - first + 1 >= 3 && (nums[right] - nums[right - 1] != diff || right == nums.length)) {   //right - 1 为等差数列的右侧边界
            if (right - 1 - first + 1 >= 3) {   //right - 1 为等差数列的右侧边界
                //计算当前区间内的子等差序列个数，(1+n)*n/2   n =right - 1 - (first + 2) + 1 = right - first -2
                sum += (1 + right - first - 2) * (right - first - 2) / 2;
            }
            //重置枚举项first，重置为此轮等差数列的右侧
//            first = right - 1;  //错误
            first = right - 2;  //正确
        }
        return sum;
    }


    /**
     * 424. 替换后的最长重复字符
     * 滑动窗口
     *
     * @param s
     * @param k
     * @return
     */
    public int characterReplacement(String s, int k) {
        int maxWindow = 0;
        int left = 0;
        int right = 0;
        int replaces = 0;
        char[] chars = s.toCharArray();
        int[] charAndTimes = new int[26];
        int maxTimes = 0;
        char maxChar = chars[0];
        while (right < chars.length) {
            int posTimes = ++charAndTimes[chars[right] - 'A'];
            if (posTimes > maxTimes) {
                maxTimes = posTimes;
                maxChar = chars[right];
            }
            //减去最高频的单词，剩余均替换
            replaces = right - left + 1 - maxTimes;
            //如果需要替换次数replaces超过可替换次数K，则需要移动左侧left寻找满足条件的区间
            while (replaces > k && left < right) {
                charAndTimes[chars[left] - 'A']--;
                //如果left端的字符 不是 当前区间内的频次最高的字符，才会消耗K
                if (maxChar != chars[left])
                    replaces--;
                //继续移动左侧端点，尝试寻找满足条件的区间
                left++;
            }
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }


    /**
     * 438. 找到字符串中所有字母异位词
     *
     * @param s
     * @param p
     * @return
     */
    public List<Integer> findAnagrams(String s, String p) {
        ArrayList<Integer> resList = new ArrayList<>();
        int len = p.length();
        if (s.length() < len)
            return resList;
        int[] target = new int[26];
        int[] window = new int[26];
        //形成滑动窗口：构建目标数据和首个window数组（记录窗口内各个字符出现的次数）
        for (int i = 0; i < len; i++) {
            target[p.charAt(i) - 'a']++;
            window[s.charAt(i) - 'a']++;
        }
        if (Arrays.equals(target, window))
            resList.add(0);
        int left = 0;
        int right = len;
        //滑动窗口长度固定，向前平移滑动
        while (right < s.length()) {
            window[s.charAt(right) - 'a']++;
            window[s.charAt(left) - 'a']--;
            if (Arrays.equals(target, window))
                resList.add(left + 1);//right带动窗口右移后，left则停留在window左侧一位，此时区间满足条件，区间左端点为left+1
            left++;
            right++;
        }
        return resList;
    }


    /**
     * 443. 压缩字符串
     *
     * @param chars
     * @return
     */
    public int compress(char[] chars) {
        int left = 0;
        int right = 1;
        int pos = 0;
        while (right < chars.length) {
            //right一直向前移动，探索其他不同数值的区间
            if (chars[right] != chars[right - 1] || right == chars.length - 1) {
                //left移送到和right-1同一个区间内
                while (left <= right - 1 && chars[left] != chars[right - 1]) {
                    left++;
                }
                chars[pos++] = chars[left];
                int window = right - left;    //right - 1 - left + 1
                if (window > 1) {
                    String times = String.valueOf(window);
                    char[] chartimes = times.toCharArray();
                    for (char c : chartimes)
                        chars[pos++] = c;
                }
            }
            right++;
        }
        return pos;
    }


    public int compress00(char[] chars) {
        int left = 0;
        int right = 0;
        int pos = 0;
        while (right < chars.length) {
            //right一直向前移动，探索其他不同数值的区间
            if (right == chars.length - 1 || chars[right] != chars[right + 1]) {
//                if (chars[right] != chars[right + 1] || right == chars.length - 1) {  //对比上述区别，此处报错数组越界
                //left移送到和right同一个区间内（有问题，应该是和right+1一个区间）
//                while (left <= right && chars[left] != chars[right]) {
//                    left++;
//                }
                chars[pos++] = chars[right];
                //压缩区间长度
                int window = right - left + 1;    //right - 1 - left + 1
                if (window > 1) {
                    String times = String.valueOf(window);
                    char[] chartimes = times.toCharArray();
                    for (char c : chartimes)
                        chars[pos++] = c;
                }
                //left站在下一个压缩区间的起始位置
                left = right + 1;
            }
            right++;
        }
        return pos;
    }


    public int compress01(char[] chars) {
        int left = 0;
        int right = 0;
        int pos = 0;
        while (right < chars.length) {
            //right一直向前移动，探索其他不同数值的区间
            if (right == chars.length - 1 || chars[right] != chars[right + 1]) {
//                if (chars[right] != chars[right + 1] || right == chars.length - 1) {  //对比上述区别，此处报错数组越界
                //left移送到和right同一个区间内（有问题，应该是和right+1一个区间）
                while (left <= right && chars[left] != chars[right]) {
                    left++;
                }
                chars[pos++] = chars[right];
                //压缩区间长度
                int window = right - left;    //right - 1 - left + 1
                if (window > 1) {
                    String times = String.valueOf(window);
                    char[] chartimes = times.toCharArray();
                    for (char c : chartimes)
                        chars[pos++] = c;
                }
                //left站在下一个压缩区间的起始位置
//                left = right + 1;
            }
            right++;
        }
        return pos;
    }


    /**
     * 475. 供暖器
     *
     * @param houses
     * @param heaters
     * @return
     */
    public int findRadius(int[] houses, int[] heaters) {
        int Radius = 0;
        Arrays.sort(houses);
        Arrays.sort(heaters);
        //枚举房子，每轮循环房子固定，寻找最近的heater，然后计算最近距离
        for (int pos = 0; pos < houses.length; pos++) {
            int closePos = binSearch(heaters, houses[pos]);
            int minRadius = Integer.MAX_VALUE;
            if (closePos == 0)
                minRadius = Math.min(minRadius, Math.abs(houses[pos] - heaters[closePos]));
            else if (closePos == heaters.length)
                minRadius = Math.min(minRadius, Math.abs(houses[pos] - heaters[heaters.length - 1]));
            else
                minRadius = Math.min(minRadius, Math.min(Math.abs(houses[pos] - heaters[closePos]), Math.abs(houses[pos] - heaters[closePos - 1])));

            Radius = Math.max(Radius, minRadius);
        }
        return Radius;
    }

    public int binSearch(int[] heaters, int target) {
        int left = 0;
        int right = heaters.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (heaters[mid] < target)
                left = mid + 1;
            else
                right = mid - 1;
        }
        return left;
    }


    /**
     * 485. 最大连续 1 的个数
     *
     * @param nums
     * @return
     */
    public int findMaxConsecutiveOnes(int[] nums) {
        int left = 0;
        int right = 0;
        int maxWindow = 0;
        while (right < nums.length) {
            if (nums[right] == 0) {
                maxWindow = Math.max(maxWindow, right - left);
                left = right + 1;
            } else if (right == nums.length - 1) {
                maxWindow = Math.max(maxWindow, right - left + 1);
            }
            right++;
        }
        return maxWindow;
    }


    public int findMaxConsecutiveOnes01(int[] nums) {
        int right = 0;
        int maxWindow = 0;
        int window = 0;
        while (right < nums.length) {
            if (nums[right] == 1) {
                window++;
            } else {
                maxWindow = Math.max(maxWindow, window);
                window = 0;
            }
            maxWindow = Math.max(maxWindow, window);//考虑最后right==nums.length-1的情况
            right++;
        }
        return maxWindow;
    }


    /**
     * 524. 通过删除字母匹配到字典里最长单词
     *
     * @param s
     * @param dictionary
     * @return
     */
    public String findLongestWord(String s, List<String> dictionary) {
        String longesWord = "";
        for (String word : dictionary) {
            int pos = 0;
            int move = 0;
            while (pos < word.length() && move < s.length()) {
                if (word.charAt(pos) == s.charAt(move)) {
                    pos++;
                    move++;
                } else
                    move++;
            }
            if (pos == word.length()) {
                if (pos > longesWord.length()) {
                    longesWord = word;
                } else if (pos == longesWord.length() && word.compareTo(longesWord) < 0) {
                    longesWord = word;
                }
            }
        }
        return longesWord;
    }


    /**
     * 581. 最短无序连续子数组
     *
     * @param nums
     * @return
     */
    public int findUnsortedSubarray(int[] nums) {
        int[] sorted = Arrays.copyOf(nums, nums.length);
        Arrays.sort(sorted);
        int left = 0;
        int right = nums.length - 1;
        int window = 0;
        while (nums[left] == sorted[left] && left < right)
            left++;
        while (nums[right] == sorted[right] && right > left)
            right--;
        if (left < right)
            window = right - left + 1;

        return window;
    }


    /**
     * 594. 最长和谐子序列
     * <p>
     * 滑动窗口
     *
     * @param nums
     * @return
     */
    public int findLHS(int[] nums) {
        int maxLength = 0;
        Arrays.sort(nums);
        int left = 0;
        int right = 0;
        while (right < nums.length) {
            //left移动，满足题目条件
            while (nums[right] - nums[left] > 1 && left < right) {  //非常巧妙
                // 在nums[right] - nums[left] == 0 的时候不移动
                // 在>1时，能把left移动到满足nums[right] - nums[left] == 1条件的最小/最左侧的left
                // 包含两层含义：
                // ### 1.如果nums[right]-nums[right-1]==1时，将left移动到满足nums[left]==nums[right-1]条件的最左侧left
                // ### 2.如果nums[right]-nums[right-1]>1时，将left移动到left==right处
                left++;
            }
            if (nums[right] - nums[left] == 1) {
                maxLength = Math.max(maxLength, right - left + 1);
            }
            //探索未知区域
            right++;
        }
        return maxLength;
    }


    public int findLHS01(int[] nums) {
        int maxLength = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int digit : nums)
            hTable.put(digit, hTable.getOrDefault(digit, 0) + 1);
        for (int digit : hTable.keySet()) {
            if (hTable.containsKey(digit + 1))
                maxLength = Math.max(maxLength, hTable.get(digit) + hTable.get(digit + 1));
        }
        return maxLength;
    }


    /**
     * 611. 有效三角形的个数
     *
     * @param nums
     * @return
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
     * 825. 适龄的朋友
     * <p>
     * 暴力求解：逐个分析两个用户的情况，逻辑正确，但时间复杂度过高，超时
     *
     * @param ages
     * @return
     */
    public int numFriendRequests(int[] ages) {
        int sumRequests = 0;
        for (int left = 0; left < ages.length - 1; left++) {  //边界条件
            for (int right = left + 1; right < ages.length; right++) {
                if (ages[right] <= 0.5 * ages[left] + 7 || ages[right] > ages[left] || ages[right] > 100 && ages[left] < 100)
                    sumRequests += 0;
                else
                    sumRequests++;
                if (ages[left] <= 0.5 * ages[right] + 7 || ages[left] > ages[right] || ages[left] > 100 && ages[right] < 100)
                    sumRequests += 0;
                else
                    sumRequests++;
            }
        }
        return sumRequests;
    }
//    20 的区间范围为 [17.0, 20]
//    30 的区间范围为 [22.0, 30]
//    100 的区间范围为 [57.0, 100]
//    110 的区间范围为 [62.0, 110]
//    120 的区间范围为 [67.0, 120]


    /**
     * 逻辑正确，但时间复杂度高
     * 问题在于：
     * ####虽然是双指针，但是每次都要从两头向中间寻找目标区间，循环次数较多
     */
    public int numFriendRequests00(int[] ages) {
        int sumRequests = 0;
        Arrays.sort(ages);
        for (int age : ages) {
            int left = 0;
            int right = ages.length - 1;
            if (age <= 14) {
                continue;
            }
            //针对当前的agePos的用户，其能发送请求的年龄区间的左侧边界如下
            while (ages[left] <= 0.5 * age + 7)          //不满足条件，能移动left以寻找满足条件的左边界
                left++;
            while (ages[right] > age)    //不满足条件，能移动right以寻找满足条件的右边界，右侧可等于
                right--;
            if (left <= right)
                sumRequests += right - left;
        }
        return sumRequests;
    }


    /**
     * 由于left和right具有延续性，相邻两轮的迭代，left和right均递增，所以left和right在各轮循环中持续增长，不重新归零
     */
    public int numFriendRequests01(int[] ages) {
        Arrays.sort(ages);
        int left = 0;
        int right = 0;
        int sumRequests = 0;
        for (int age : ages) {
            if (age <= 14) {
                continue;
            }
            //针对当前的agePos的用户，其能发送请求的年龄区间的左侧边界如下
            while (ages[left] <= 0.5 * age + 7)         //不满足条件，能移动left以寻找满足条件的左边界
                left++;
            while (right + 1 < ages.length && ages[right + 1] <= age) {
                //必须是<=age，必须有等号，特别是遇到多个age相同的情况
                right++;
                //1.在即将越界时，会压在边界上，而不会越界，即此时 right==ages.length
                //2.在age存在多个相同的情况下，right压在右侧age上
                //这种写法，无论何种情况，其自身一定是在区间内（多个age相同时）或位于区间右边界，都应将自身剔除
            }
            sumRequests += right - left + 1 - 1;  //-1剔除自身
        }
        return sumRequests;
    }

    //相对上面的方法，区别在于：第二个while种的right+1改为right的
    public int numFriendRequests02(int[] ages) {
        Arrays.sort(ages);
        int left = 0;
        int right = 0;
        int sumRequests = 0;
        for (int age : ages) {
            if (age <= 14) {
                continue;
            }
            //针对当前的agePos的用户，其能发送请求的年龄区间的左侧边界如下
            while (ages[left] <= 0.5 * age + 7)         //不满足条件，能移动left以寻找满足条件的左边界
                left++;
            while (right < ages.length && ages[right] <= age) {
                //必须是<=age，必须有等号，特别是遇到多个age相同的情况
                right++;
                //1.right会越界，即此时 right==ages.length
                //2.在age存在多个相同的情况下，right压在age右侧"外一位"上，即ages[right] > age
                //####1.多个age相同的情况下（重点考虑，容易忽略的点）
                //#########right最终状态为ages[right] > age，right位于age右侧外一位
                //####2.单个age的情况下
                //#########right最终状态为ages[right] > age，right位于age右侧外一位
                //无论何种情况，right使得枚举的元素（自身）一定在区间内，其次right也在使得ages[right] = age的右侧一位
            }
            sumRequests += right - 1 - left + 1 - 1;  //第一个"-1"是收缩区间，第二个"-1"是剔除枚举元素自身
        }
        return sumRequests;
    }


    public int numFriendRequests10(int[] ages) {
        int[] cnts = new int[120], presum = new int[121];
        for (int age : ages)
            cnts[age - 1]++;
        for (int i = 1; i < 121; i++)
            presum[i] = presum[i - 1] + cnts[i - 1];
        int ans = 0;
        for (int age : ages)
            ans += Math.max(0, presum[age] - presum[age / 2 + 7] - 1);
        return ans;
    }


    public int numFriendRequests11(int[] ages) {
        int numFriendRequests = 0;
        int[] ageNum = new int[120];
        int[] preSum = new int[121];
        //因为年龄区间为0-120，故计算ages数组中，各个年龄的人数
        for (int age : ages) {
            ageNum[age - 1]++;
        }
        //计算各个年龄的前缀和（含自身年龄）
        for (int i = 1; i < 121; i++) {  //本位，记录ageNum上一位（含）之前的累计和
            preSum[i] = preSum[i - 1] + ageNum[i - 1];
        }
        for (int age : ages) {
            numFriendRequests += Math.max(0, preSum[age] - preSum[age / 2 + 7] - 1);//此处取最大值，是为了剔除年龄小于15岁的请求
        }
        return numFriendRequests;
    }


}
