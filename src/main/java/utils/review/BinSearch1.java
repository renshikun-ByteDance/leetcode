package utils.review;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * 算法类型：二分法
 * 二分法使用的前提条件：数组有序&&数组不重复
 * <p>
 * 二分查找也称折半查找（Binary Search），是一种在有序数组中查找某一特定元素的搜索算法。
 * 我们可以从定义可知，运用二分搜索的前提是数组必须是有序的
 * 这里需要注意的是，我们的输入不一定是数组，也可以是数组中某一区间的起始位置和终止位置
 * <p>
 * 根据搜索区间不同，主要分为两种写法：
 * 1.[left,right] 左闭右闭
 * 2.[left,right) 左闭右开
 * 具体详见：
 * https://leetcode-cn.com/problems/binary-search/submissions/
 *
 * <p>
 * mid的错误计算方法：mid=(left + right) / 2;
 * int类型表示范围有限：MIN_VALUE和MAX_VALUE
 * left <= MAX_VALUE和 right <= MAX_VALUE是肯定的，但是left+right <= MAX_INT 我们无法确定，所以可能会造成栈溢出。
 * <p>
 * mid的正确计算方法：mid=left + (right - left) / 2; 或者 mid=left + ((right - left) >>1) 通过位运算符 >>1等效于/2
 */
public class BinSearch1 {


    /**
     * 704. 二分查找
     */
    //左闭右闭的写法
    public int search_simple_01(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;           //**重点
        while (left <= right) {                //**重点: 区间[left,right]，右闭
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target) {
                return mid;
            } else if (target < nums[mid]) {
                //下一轮搜索区间: [left,mid-1]
                right = mid - 1;   //由于此时target一定不等于num[mid]，即目标索引一定不是mid，故右侧区间收缩至mid-1;
            } else if (nums[mid] < target) {
                //下一轮搜索区间: [mid+1,right]
                left = mid + 1;    //由于此时target一定不等于num[mid]，即目标索引一定不是mid，故左侧区间收缩至mid+1;
            }
        }
        return -1;
    }

    //左闭右开的写法
    public int search_simple_02(int[] nums, int target) {
        int left = 0;
        int right = nums.length;            //**重点
        while (left < right) {              //**重点: 区间[left,right)，右开区:每次循环时，均应为right赋值一个不可达的Index
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target)
                return mid;
            else if (target < nums[mid])
                //下一轮搜索区间: [left,mid]
                right = mid;   //由于此时target一定不等于num[mid]，即目标索引一定不是mid，故右侧区间收缩至mid（右侧不可达的Index）;
                //**如果赋值为mid-1,就像相当于右侧区间可达的为mid-2(因为循环条件为 while (left < right))
            else if (nums[mid] < target)
                //下一轮搜索区间: [mid+1,right]
                left = mid + 1;    //由于此时target一定不等于num[mid]，即目标索引一定不是mid，故左侧区间收缩至mid+1;
        }
        return -1;
    }


    /**
     * 二分法主要分为两类：
     * 1.快速查找数组中是否存在某一个数（精确查找）
     * 不再赘述
     * 2.快速找到“最”满足某个条件的数
     * 此类在处理过程中需要记录这个数的索引index，可分为：
     * @左侧逼近：
     * ## 1.result默认值-1
     * ## 2.temp < target时，记录此result
     * @右侧逼近：
     * ## 1.result默认值“区间长度”(nums.length)
     * ## 2.target < temp 时，记录此result
     */


    /**
     * 69. Sqrt(x) 算数平方根
     * 关键： 不是精确找到某一个数，而是找到“最”满足某个条件的数，因此在处理过程中需要记录这个数的Index
     *
     * @param x
     * @return
     */
    public int mySqrt(int x) {
        //构建有限&&不重复的数组[0,x]，则其中某个元素m的平方一定大于等于x
        int left = 0;
        int right = x;  // 右侧最大就是x
        int result = -1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if ((long) mid * mid == x) {
                return mid;
            } else if ((long) mid * mid < x) {
                left = mid + 1;
                result = mid;    //从左侧逼近
            } else if (x < (long) mid * mid) {
                right = mid - 1;
            }
        }
        return result;
    }


    public int mySqrt01(int x) {
        //构建有限&&不重复的数组[0,x]，则其中某个元素m的平方一定大于等于x
        int left = 0;
        int right = x;  // 右侧最大就是x
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if ((long) mid * mid == x) {
                return mid;
            } else if ((long) mid * mid < x) {
                left = mid + 1;
            } else if (x < (long) mid * mid) {
                right = mid - 1;
            }
        }
        return right;
    }


    /**
     * 35. 搜索插入位置
     * 记录可插入的位置
     *
     */
    public int searchInsert(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;  //左闭右闭
        int Insert = nums.length;    //由于数组自增，因此初始插入点应该在初始区间右侧
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target)
                return mid;
            else if (target < nums[mid]) {
                right = mid - 1;
                Insert = mid;    //由于数组自增，因此应该插入点应该一直逼近右侧区间
            } else if (nums[mid] < target) {
                left = mid + 1;
            }
        }
        return Insert;
    }


    /**
     * 不需要记录可插入位置的解法：
     * 此类题最终直接返回left/right就可以了
     * <p>
     * 1.根据if的判断条件
     * 只要left移动，则left左边的值一直保持小于target；
     * 只要right移动，则right右边的值一直保持大于target；
     * 2.最终循环结束条件
     * ###1.fun(mid)==target,return;
     * ###2.由于不满足循环条件while(left<=right),则此时left一定等于right+1
     * 因此，循环结束后，在left和right之间画一条竖线，恰好可以把数组分为两部分：left左边的部分和right右边的部分
     * left左边（不含left）的部分全部小于target，并以right结尾；
     * right右边（不含right）的部分全部大于target，并以left为首；
     * 同时，本题要找的是插入点（自增），所以最终答案一定在left的位置
     * <p>
     * <p>
     * <p>
     * <p>
     * 你仔细看代码，每次循环，至少会把区间[left,right]的长度减少1。当l==r时，mid==l==r，如果target>nums[l]，那么l会向右移动一位(l=mid+1)，到达指定位置，即l==right+1；如果target<=nums[l]，那么r就向左移动一位(r=mid-1)，还是到达指定位置，即l==right+1。
     * 要知道while的判断条件是l<=r，如果l最后不大于r，一定会死循环，而且l-r不会大于1，因为mid在每次循环中始终在[l,r]之间，所以mid-1最小为l-1，mid+1最大为r+1。
     *
     * @param nums
     * @param target
     * @return
     */
    public int searchInsert01(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;  //左闭右闭
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target)
                return mid;
            else if (target < nums[mid]) {
                right = mid - 1;
            } else if (nums[mid] < target) {
                left = mid + 1;
            }
        }
        return left;
    }


    /**
     * while结束时：
     * 第一点：right一定在left的左面（紧邻），这是由while条件决定的，否则是跳不出来的。
     * 第二点：right右侧数必然是>=target的，left左侧数必然是<target的，这是由于if条件判断加上left，right的移动引起的。
     * 最后，因为我们要找的是插入位置，就是某位置a左面都小于target，右面（包括a）都大于等于target，那么a就是我们要找的位置。不管你是存在还是不存在，都是这个位置。
     *
     * @param nums
     * @param target
     * @return
     */
    public int searchInsert02(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        // 在区间 nums[left..right] 里查找第 1 个大于等于 target 的元素的下标
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < target) {                 //由于返回left，所以此处不能写=，因为此处如果相等，那会对left做操作，变化
                // 下一轮搜索的区间是 [mid + 1,right]
                left = mid + 1;
            } else {                                 //由于返回left，且循环结束的条件是left>right，因此此处写等号，
                // 下一轮搜索的区间是 [left,mid - 1]
                right = mid - 1;
                //在nums[mid] = target，即此时的mid为所求结果，所以将right快速/直接定位至此mid-1处，此位置保证nums[right]<target
                // 则后续的while循环
                // 由于right的位置（nums[right]<target），将会导致后续计算的num[mid]一直<target
                // 故只一直会触发left移动，一直移动至right处(left==right)，且nums[right]<target，则left+1至刚刚的mid处
                // 此时，right<left，while循环跳出
                // 此时，left记录的就是target位置或者target应该插入的位置
            }
        }
        return left;
    }

    public int searchInsert03(int[] nums, int target) {
        int len = nums.length;
        int left = 0;
        int right = len;
        // 在区间 nums[left..right] 里查找第 1 个大于等于 target 的元素的下标
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < target) {
                // 下一轮搜索的区间是 [mid + 1..right]
                left = mid + 1;
            } else {
                // 下一轮搜索的区间是 [left..mid]
                right = mid;
            }
        }
        return left;
    }

    /**
     * @param nums
     * @param target
     * @return
     */
    public int[] searchRange(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        //搜索左边界：beginIndex
        while (left <= right) {
            int mid = left + ((right - left) >> 2);
            if (nums[mid] < target)
                left = mid + 1;
            else if (target <= nums[mid])      //target=nums[mid]时，right移动/左移，下一轮循环的区间和mid，向左侧收缩/滑动，逼近左边界
                right = mid - 1;
        }
        //跳出循环的状态:
        // 1、right,left => left = right + 1
        // 2、num[right] 小于 target
        // 3、num[left] 大于等于 target
        int beginIndex = left;

        left = 0;
        right = nums.length - 1;
        //搜索右边界：endIndex
        while (left <= right) {
            int mid = left + ((right - left) >> 2);
            if (nums[mid] <= target)                 //target=nums[mid]时，left移动/右移，下一轮循环的区间和mid，向右侧收缩/滑动，逼近右边界
                left = mid + 1;
            else if (target < nums[mid])
                right = mid - 1;
        }
        //跳出循环的状态:
        // 1、right,left => left = right + 1
        // 2、num[right] 小于等于 target        *****差异点
        // 3、num[left] 大于 target            *****差异点
        int endIndex = right;
//        int endIndex = left - 1;  //效果同上

        if (beginIndex <= endIndex && endIndex < nums.length && nums[beginIndex] == target && nums[endIndex] == target) {
            return new int[]{beginIndex, endIndex};
        }
        return new int[]{-1, -1};
    }


    public int[] searchRange01(int[] nums, int target) {
        int begin = -1;
        int end = -1;
        int len = 0;
        for (int i = 0; i < nums.length; i++) {
            while (nums[i] == target) {
                end = i;
                len++;
                i++;
                if (i == nums.length)
                    break;
            }
        }
        begin = len == 0 ? begin : end - len + 1;
        return new int[]{begin, end};
    }


    /**
     * 33. 搜索旋转排序数组
     *
     * @param nums
     * @param target
     * @return
     */
    public int search01(int[] nums, int target) {
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            hTable.put(nums[i], i);
        }
        if (hTable.containsKey(target)) {
            return hTable.get(target);
        }
        return -1;
    }

    public int search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            //基于有序区间判断： target是否在此区间内
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[left] <= nums[mid]) { //左侧有序   要有等于号
                if (nums[left] <= target && target < nums[mid]) {   //target在左侧有序区间内
                    right = mid - 1;
                } else { //target不在左侧有序区间内
                    left = mid + 1;
                }
//            } else {  等价于下
            } else if (nums[mid] <= nums[right]) {  //右侧有序  nums[left] > nums[mid]
                if (nums[mid] < target && target <= nums[right]) {  //target在右侧有序区间内
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return -1;
    }

    public int search02(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (target == nums[mid])
                return mid;
            if (nums[left] == nums[mid])
                left = mid + 1;
            else if (nums[right] == nums[mid])
                right = mid - 1;
                //左区间有序
            else if (nums[left] < nums[mid]) {
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            //右侧区间有序
            else {
                if (nums[mid] < target && target <= nums[right])
                    left = mid + 1;
                else
                    right = mid - 1;
            }
        }
        return -1;
    }


    /**
     * 81. 搜索旋转排序数组 II
     * <p>关键
     * 对于数组中有重复元素的情况，二分查找时可能会有 a[l]=a[\textit{mid}]=a[r]a[l]=a[mid]=a[r]
     * 此时无法判断区间 [l,\textit{mid}][l,mid] 和区间 [\textit{mid}+1,r][mid+1,r] 哪个是有序的。
     * 例如 \textit{nums}=[3,1,2,3,3,3,3]nums=[3,1,2,3,3,3,3]，\textit{target}=2target=2，首次二分时无法判断区间 [0,3][0,3] 和区间 [4,6][4,6] 哪个是有序的。
     * <p>
     * 对于这种情况，我们只能将当前二分区间的左边界加一，右边界减一，然后在新区间上继续二分查找
     */
    public boolean searchII(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            System.out.printf(
                    "left = %d , right = %d , nums[left] = %d , nums[right] = %d, mid = %d , nums[mid] = %d    \n",
                    left, right, nums[left], nums[right], mid, nums[mid]
            );
            if (nums[mid] == target)
                return true;
            if (nums[left] == nums[mid] && nums[mid] == nums[right]) {
                ++left;
                --right;
            } else if (nums[left] <= nums[mid]) {  //左侧有序，此时也可能出现 nums[left]=0 ,nums[mid]==1 ,nums[right]==1的情况
                // 即同时满足两个else if的情况，这种情况下，按照if的优先级执行第一个；  案例：int[] nums = {1,0,1,1,1};int target = 0;
                if (nums[left] <= target && target < nums[mid]) {  //target是否在区间内
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else if (nums[mid] <= nums[right]) {  //右侧有序
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            System.out.printf(
                    "left = %d , right = %d , nums[left] = %d , nums[right] = %d, mid = %d , nums[mid] = %d    \n",
                    left, right, nums[left], nums[right], mid, nums[mid]
            );
        }
        return false;
    }


    /**
     * 475. 供暖器
     * <p> 排序+二分
     * 对于每个house找到距离其**最近**的供暖器
     * 由于供暖器的使用范围一致，故其应该满足所有的**最近**距离，即为最大的**最近***距离
     *
     */
    public int findRadius(int[] houses, int[] heaters) {
        int radius = -1;
        //使用二分法的前提条件
        Arrays.sort(heaters);
        for (int house : houses) {
            int indexClose = binarySearch(heaters, house);  //左侧接近
            //因为是二分法，且是左侧逼近，但不能保证具体左侧还是右侧距离target更近，所以此处需要判断；
            int closeReadiu = indexClose == 0 || indexClose >= heaters.length ?
                    Math.abs(house - heaters[indexClose = indexClose >= heaters.length ? heaters.length - 1 : indexClose]) : Math.min(Math.abs(house - heaters[indexClose - 1]), Math.abs(house - heaters[indexClose]));
            radius = Math.max(radius, closeReadiu);
        }
        return radius;
    }

    //二分搜索：尝试在找到距离target最近的供热器（找到或一侧最近）
    public int binarySearch(int[] heaters, int target) {
        int left = 0;
        int right = heaters.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (heaters[mid] < target)
                left = mid + 1;
            else                       //等于的情况在此，故始终向左收缩区间，而最终区间状态为[right,left]故left记录的最接近目标的index
                right = mid - 1;
        }
        return left;
    }

    //暴力解法
    public int findRadius01(int[] houses, int[] heaters) {
        int radius = -1;
        //使用二分法的前提条件
        Arrays.sort(heaters);
        for (int h = 0; h < houses.length; h++) {
            int tempMin = Integer.MAX_VALUE;
            for (int he = 0; he < heaters.length; he++) {
                tempMin = Math.min(tempMin, Math.abs(houses[h] - heaters[he]));
            }
            radius = Math.max(radius, tempMin);
        }
        return radius;
    }

    /**
     * 475. 供暖器
     * <p> 排序+双指针
     * 精髓：
     * ****两个数组均为有序数组，通过一个指向heaters数组的指针he
     * *****************来寻找每次大循环（对应一个house数组中元素ho）中的元素ho在he中的两侧相邻值（边界时也可能仅为单侧）
     * *****************he每次大循环不置0，因为两个数据均为有序数组
     */
    public int findRadius03(int[] houses, int[] heaters) {
        int radius = 0;
        int tempMin = Integer.MAX_VALUE;
        Arrays.sort(houses);
        Arrays.sort(heaters);
        //整体可以理解为，在 heaters中找到**每个ho**的两侧位置，当前也会存在仅有一侧的情况（两侧）
        int he = 0;  //heaters数组的指针 ********************精髓，每次循环不归0
        for (int ho = 0; ho < houses.length; ho++) {
            while (he < heaters.length && houses[ho] >= heaters[he]) {
                he++;    //一直找到一个he使得houses[ho]<=heaters[he]
                //由于houses和heaters都是有序的，且为自增，那么houses[ho+1]比houses[ho+1]大，所以起码要与heaters[he+1]才有可能存在houses[ho+1]<=houses[ho+1]
                //因此，每次he不归0;
            }
            if (he == 0) {   //单侧的情况
                //代表：houses[ho]<=heaters[0]，即在ho左侧不存在heaters[he]<houses[ho]
                tempMin = heaters[0] - houses[ho];
            } else if (he == heaters.length) {    //单侧的情况
                tempMin = houses[ho] - heaters[he - 1];
            } else {  //两侧的情况
                tempMin = Math.min(houses[ho] - heaters[he - 1], heaters[he] - houses[ho]);
            }
            radius = Math.max(radius, tempMin);
        }
        return radius;
    }

    public int findRadius02(int[] houses, int[] heaters) {
        int ans = 0;
        int d = (int) 2e9;
        Arrays.sort(houses);
        Arrays.sort(heaters);
        int j = 0;
        for (int i = 0; i < houses.length; i++) {
            while (j < heaters.length && heaters[j] < houses[i]) {
                j++;
            }
            if (j == 0) {
                d = heaters[0] - houses[i];
            } else if (j == heaters.length) {
                d = houses[i] - heaters[j - 1];
            } else {
                d = Math.min(heaters[j] - houses[i], houses[i] - heaters[j - 1]);
            }
            ans = Math.max(ans, d);
        }
        return ans;
    }


    public String findLongestWord(String s, List<String> dictionary) {
        String result = "";
        for (String temp : dictionary) {
            int te = 0;
            int ta = 0;
            while (ta < s.length() && te < temp.length()) {
                if (temp.charAt(te) == s.charAt(ta))
                    te++;     //此元素匹配，继续在s中匹配temp中的next元素
                ta++;         //s的指针每次都移动
            }
            if (te == temp.length())
                if (te > result.length() || (te == result.length() && temp.compareTo(result) < 0))
                    result = temp;
        }
        return result;
    }


}
