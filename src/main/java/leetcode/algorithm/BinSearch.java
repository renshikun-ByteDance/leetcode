package leetcode.algorithm;

import java.util.*;

//-----------------
// 二分搜索
//-----------------
public class BinSearch {


    /**
     * 33. 搜索旋转排序数组
     */
    public int search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[left] == target) return left;
            if (nums[mid] == target) return mid;
            if (nums[right] == target) return right;
            if (nums[left] <= nums[right]) {              //1、整个区间内有序
                if (nums[mid] < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            } else if (nums[left] <= nums[mid]) {          //2、左侧有序，等号的目的在于 区间长度长度为 1
                if (nums[left] < target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else if (nums[mid] <= nums[right]) {        //3、右侧有序，等号的目的在于 区间长度长度为 1
                if (nums[mid] < target && target < nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return -1;
    }

    public int search000(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target)   //这种写法已经将 left==right的情况包含进去了，也包含巧合的情况  *** 要好好理解理解
                return mid;
            //左侧有序
            if (nums[left] <= nums[mid]) {
                if (nums[left] <= target && target <= nums[mid])
                    right = mid - 1;
                else
                    left = mid + 1;
            }
            //右侧有序
            if (nums[mid] <= nums[right]) { //注意：尽量不要这样写，虽然本题没错误，要写为if/else，二者只能执行一个，因为上面if执行后，会使得另外一个判断条件中的项的值发生改变
                //对比下面的题目 81. 搜索旋转排序数组 II也是，最好写为if/else
                if (nums[mid] <= target && target <= nums[right])
                    left = mid + 1;
                else
                    right = mid - 1;
            }
        }
        return -1;  //这个题，不是单纯的左侧逼近和右侧逼近，所以无法用return left/right，只能用最原始的办法，if (nums[mid] == target) return mid;
    }

    public int search01(int[] nums, int target) {
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
     */
    public boolean searchII(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target)
                return true;
            if (nums[left] == nums[mid] && nums[mid] == nums[right]) {
//            if (nums[left] == nums[right]) {   //这种情况下，不需要两侧移动，单侧移动就行
                left++;
                right--;
            } else if (nums[left] <= nums[mid]) {    //左侧有序
                if (nums[left] <= target && target <= nums[mid])     //target在左侧区间
                    right = mid - 1;
                else
                    left = mid + 1;
            } else if (nums[mid] <= nums[right]) {
                if (nums[mid] <= target && target <= nums[right])
                    left = mid + 1;
                else
                    right = mid - 1;
            }
        }
        return false;
    }


    /**
     * 34. 在排序数组中查找元素的第一个和最后一个位置
     */
    public int[] searchRange(int[] nums, int target) {
        int beginIndex = -1;
        int endIndex = -1;
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target)
                left = mid + 1;
            else if (target <= nums[mid])
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

        //通过左滑或右滑去寻找target时，不一定就一定能找到target，在target不存在的情况下，左滑和右滑指针会停留在target理论会出现的位置
        //所以应该判断一下最终的left或right指针是否停留在了target上*******************
        if (beginIndex <= endIndex && nums[beginIndex] == target && nums[endIndex] == target)
            return new int[]{beginIndex, endIndex};

        return new int[]{-1, -1};
    }


    public int[] searchRange01(int[] nums, int target) {
        int left = leftHelper(nums, target);
        int right = rightHelper(nums, target);
        return new int[]{left, right};
    }

    private int leftHelper(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        if (left == nums.length) return -1;
        else if (nums[left] == target) return left;  //存在，如果重复，则位于最左侧的 target上
        else if (nums[left] > target) return -1;
        return -1;  //其实只有上述三种情况，所以此处怎么写无所谓
    }

    private int rightHelper(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        if (right == -1) return -1;  //不存在，越界的情况
        else if (nums[right] == target) return right;   //存在，如果重复，则停留在最右侧 target上
        else if (nums[right] < target) return -1;
        return -1;
    }


    /**
     * 35. 搜索插入位置
     */
    public int searchInsert(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target)
                left = mid + 1;
            else if (target <= nums[mid])
                right = mid - 1;
        }

        //此题目，不需要一定找出target的位置，在target不存在时，返回其理论的位置，所以此处不需要判断一下最终的left指针是否停留在target上了
        //对比 34. 在排序数组中查找元素的第一个和最后一个位置

        return left;
    }

    public int searchInsert01(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] <= target)
                left = mid + 1;
            else if (target < nums[mid])
                right = mid - 1;
        }
        if (right == -1)
            return right + 1;
        if (nums[right] == target)
            return right;
        else if (nums[right] < target)
            return right + 1;
        return -1;
    }

    public int searchInsert02(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int k = nums.length;              //严格的右侧逼近，默认值右侧区间外一位
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target)
                left = mid + 1;
            else if (target <= nums[mid]) {
                k = mid;                 //严格的右侧逼近，K保证target <= nums[K]
                right = mid - 1;
            }
        }
        return k;
    }



    /**
     * 74. 搜索二维矩阵
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        int rows = matrix.length - 1;
        int columns = matrix[0].length - 1;
        int left = 0;
        int right = columns;
        int rowTemp = 0;
        while (rowTemp <= rows) {
            if (matrix[rowTemp][0] <= target && target <= matrix[rowTemp][columns]) {
                while (left <= right) {
                    int mid = left + ((right - left) >> 1);
                    if (matrix[rowTemp][mid] < target)
                        left = mid + 1;
                    else if (target <= matrix[rowTemp][mid])   //右侧逼近
                        right = mid - 1;
                }

                //通过左滑或右滑来寻找目标值target,其最终不一定就真的停在target上，故需要判断一下
                //如果通过另一种写法 if (==target) 则无需判断*****
                if (matrix[rowTemp][left] == target)
                    return true;
            }
            rowTemp++;
        }
        return false;
    }


    int[][] matrix;
    int mm;
    int nn;

    public boolean searchMatrix01(int[][] matrix, int target) {  //将二维矩阵视为一维有序数组
        this.matrix = matrix;
        this.mm = matrix.length;
        this.nn = matrix[0].length;
        int left = 0;
        int right = mm * nn - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (getNums(mid) < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        if (left == mm * nn) return false;
        return getNums(left) == target;
    }

    private int getNums(int index) {  //转换为一维
        int row = index / nn;
        int col = index % nn;
        return matrix[row][col];
    }

    public boolean searchMatrix10(int[][] matrix, int target) {  //基于广度优先搜索
        int m = matrix.length;
        int n = matrix[0].length;
        int row = 0;
        int col = n - 1;
        while (row < m && col >= 0) {
            if (matrix[row][col] < target) row++;
            else if (matrix[row][col] > target) col--;
            else return true;
        }
        return false;
    }


    /**
     * 240. 搜索二维矩阵 II
     */
    public boolean searchMatrixII(int[][] matrix, int target) {  //基于广度优先搜索，忽略基于二分的写法（没意思）
        int m = matrix.length;
        int n = matrix[0].length;
        //从第一行最后一个元素开始广度优先搜索，因为此位置对应的两个方向，一个大于一个小于
        int row = 0;           //只能增加
        int col = n - 1;       //只能减少
        while (row < m && col >= 0) {
            if (matrix[row][col] < target) row++;
            else if (matrix[row][col] > target) col--;
            else return true;
        }
        return false;
    }


    public boolean searchMatrixII0(int[][] matrix, int target) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        int tRow = 0;
        int tCol = columns - 1;
        while (tRow < rows && tCol >= 0) {
            if (matrix[tRow][tCol] == target)
                return true;
            else if (matrix[tRow][tCol] < target)
                tRow++;
            else if (matrix[tRow][tCol] > target)
                tCol--;
        }
        return false;
    }

    public boolean searchMatrixII00(int[][] matrix, int target) {
        HashSet<Integer> hTable = new HashSet<>();
        int rows = matrix.length;
        int columns = matrix[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                hTable.add(matrix[i][j]);            //最愚蠢的写法
            }
        }
        return hTable.contains(target);
    }

    public boolean searchMatrixII01(int[][] matrix, int target) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (matrix[i][j] == target) return true;     //稍微聪明一丁点的写法
            }
        }
        return false;
    }


    /**
     * 153. 寻找旋转排序数组中的最小值
     */
    public int findMin(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < nums[0]) {
                right = mid - 1;
            } else if (nums[0] <= nums[mid]) {
                left = mid + 1;
            }
        }
        if (left == nums.length) return nums[0];        //数组为升序
        if (right == -1) return nums[nums.length - 1];  //数组为降序
        return Math.min(nums[left], nums[right]);       //数组旋转，肯定有一个是最小值
    }

    public int findMin22(int[] nums) {   //“target”不变的情况
        int left = 0;
        int right = nums.length - 1;   //“target”为区间最右侧的值
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

    //这种写法必须判断是否是有序序列
    public int findMin01(int[] nums) {   //“target”不变的情况,target为区间最左侧值nums[0]
        int left = 0;
        int right = nums.length - 1;
        if (nums[left] < nums[right]) return nums[0];   //有序序列直接返回，下面没法处理有序序列的结果
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < nums[0])           //mid在最小值所在区间，mid是最小值右侧的元素
                right = mid;                   //这样写最终状态也是 [right,left](因为while条件决定了)，不过就是移动慢点
            else if (nums[0] <= nums[mid])     //等号一定在此，否在上面就是死循环
                left = mid + 1;
        }
        return nums[right];
    }


    public int findMin10(int[] nums) {     //关键和区别：“target”一直在变
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            //旋转点的右侧一位为最小值,right为最小值所在区间
            //本题中nums[right]一直在变，尝试找到最小值，一旦找到就占着（right = mid）
            if (nums[mid] < nums[right])           //mid在最小值所在区间，mid是最小值右侧的元素
                //如果right处相较于mid处不是较小值（mid落在右侧最小值所在区间），则right占据mid位置，即记录当前的较小值
                right = mid;                       //这样写最终状态也是 [right,left](因为while条件决定了)，不过就是移动慢点
                //本题必须这样写，防止在mid压在最小值（最终target）上时，right = mid - 1 导致跳过最小值变为最大值
            else if (nums[right] <= nums[mid])     //等号一定在此，否在上面就是死循环
                //如果right处相较于mid是较小值（mid落在左侧最大值所在区间），则移动left，来间接改变mid的位置
                left = mid + 1;
        }
        return nums[right];
    }


    public int findMin11(int[] nums) {     //关键和区别：“target”一直在变，写法同方法一
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] <= nums[right])
                right = mid;
            else if (nums[right] < nums[mid])
                left = mid + 1;
        }
        return nums[right];
    }


    //关键的问题在于right可能会压着最大值，但下次计算mid后，无论怎样最终都是left==mid
    //找最大值，最大值右侧就是最小值
    public int findMin004(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        if (nums[left] < nums[right] || nums.length == 1) return nums[0];
        while (left <= right) {            //这种写法，最终状态一定是 left==right
            int mid = left + (right - left) / 2;
            if (nums[mid] <= nums[left]) {   //mid落在右侧小值区间，移动right
                right = mid - 1;
//                if ((right - left == 1 || right - left == 2) && nums[left] < nums[right])  //此时right压着最大值****
                if (nums[left] < nums[right])  //此时right压着最大值****
                    return nums[right + 1];
            }
            //问题在于：前一个mid恰巧压着最大值，然后计算下一个mid，此时恰逢有效区间太短（2），计算的新mid时，导致新mid偏左移动，并压着left，迫使right非法移动，而left并未记录最大值
            else if (nums[left] < nums[mid])   //mid落在左侧大值区间，更新left
                left = mid;                    //left一直占据当前的最大值，循环结束就是区间的最大值
        }
        return nums[left];
    }


    //题目中nums[right]一直小于等于nums[nums.length - 1]，且right一直在最小值所在区间内移动


    //思路：通过寻找最大值，然后其右侧即为最小值
    //错误代码
    public int findMin000(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        if (nums[left] <= nums[right]) return nums[0];
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] > nums[left]) {
                left = mid;
            } else if (nums[left] >= nums[mid]) {
                right = mid - 1;
            }
        }
        return nums[left + 1];
    }


    /**
     * 162. 寻找峰值
     */
    public int findPeakElement(int[] nums) {   //找到最右侧峰值
        int index = 0;
        for (int i = 1; i < nums.length; i++) {
            //---------------------------------------------------------------------------------
            // 关键：相邻元素不相等，且从左至右遍历，最后一个比左侧值大的点一定是峰值，因为峰值一定存在
            //---------------------------------------------------------------------------------
            if (nums[i - 1] < nums[i])
                index = i;
        }
        return index;
    }

    public int findPeakElement00(int[] nums) {   //找到最左侧峰值
        int index = nums.length - 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            if (nums[i] > nums[i + 1]) {
                index = i;
            }
        }
        return index;
    }

    public int findPeakElement01(int[] nums) {   //二分，二段性的体现，找到任意一个峰值
        //-------------------------------------------------------------------------------------
        // 本题关键：
        //     对于 nums[xx] < nums[xx + 1]，则在 xx 右侧一定有峰值，而 xx 左侧不一定有峰值
        // 二段性的体现：
        //     指在以 mid 为分割点的数组上，根据 nums[mid] 与 nums[mid±1] 的大小关系
        //     可以确定其中一段满足「必然有解」，另外一段不满足「必然有解」（可能有解，可能无解）
        //-------------------------------------------------------------------------------------
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);    //偶数区间，中点逼近左侧，保证下面 nums[mid + 1] 不会越界
            if (nums[mid] <= nums[mid + 1]) {          //left爬山
                left = mid + 1;
            } else if (nums[mid] > nums[mid + 1]) {
                right = mid;
            }
        }
        return left;
    }

    public int findPeakElement02(int[] nums) {   //二分，二段性的体现
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + ((right - left + 1) >> 1);  //偶数区间，中点逼近右侧，保证下面 nums[mid + 1] 不会越界，同时不会进入死循环，如 left 的逻辑
            if (nums[mid - 1] < nums[mid]) {
                left = mid;
            } else if (nums[mid - 1] >= nums[mid]) {
                right = mid - 1;
            }
        }
        return left;
    }

    //错误写法
    public int findPeakElement03(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < nums[mid + 1])           //left左侧上山，但left能取到更大的值 mid+1
                left = mid;
            else if (nums[mid] >= nums[mid + 1])     //right右侧上山，如果nums[mid]正好就是目标值，就会导致跳过
                right = mid - 1;                                     //只知道nums[mid]>=nums[mid+1]，故right应该占据mid，而非mid-1;
        }
        return left;
    }


//    /**
//     * 278. 第一个错误的版本
//     */
//    public int firstBadVersion(int n) {
//        int left = 0;
//        int right = n;
//        while (left <= right) {
//            int mid = left + ((right - left) >> 1);
//            if (isBadVersion(mid)) {
//                right = mid - 1;
//            } else
//                left = mid + 1;
//        }
//        return left;
//    }

//    public int firstBadVersion00(int n) {
//        int left = 0;
//        int right = n;
//        while (left < right) {
//            int mid = left + ((right - left) >> 1);
//            if (isBadVersion(mid))     //true是错误版本
//                right = mid;
//            else                       //false是正确版本
//                left = mid + 1;
//        }
//        return left;
//    }


    /**
     * 目标值：
     * 总共有 h 篇论文分别被引用了至少 h 次。且其余的 n - h 篇论文每篇被引用次数 不超过 h 次
     */
    public int hIndex(int[] citations) {
        Arrays.sort(citations);
        int h = 0, i = citations.length - 1;
        while (i >= 0 && citations[i] > h) {    //h正好可以表示，比当前文章出现次数多的文章数
            h++;
            i--;
        }
        return h;
    }

    public int hIndex00(int[] cs) {
        int n = cs.length;
        int l = 0, r = n;
        while (l < r) {
            int mid = l + r + 1 >> 1;
            if (check(cs, mid)) l = mid;        //不明白为什么不先排序？？
            else r = mid - 1;
        }
        return r;
    }

    boolean check(int[] cs, int mid) {
        int ans = 0;
        for (int i : cs) if (i >= mid) ans++;
        return ans >= mid;
    }


    //错误示例
    public int hIndex01(int[] citations) {
        int h = -1;
        for (int i = 0; i < citations.length; i++) {
            int meetcits = 0;
            for (int cit : citations) {
                if (cit >= citations[i]) meetcits++;  //满足条件的文章数
            }
            //共有meetcits篇文章，至少被引用citations[i]次
            if (meetcits == citations[i])
                h = Math.max(h, meetcits);
        }
        return h;
    }


    /**
     * 错误示例
     */
    public int hIndex02(int[] citations) {
        Arrays.sort(citations);
        int left = 0;
        int right = citations.length - 1;
        int h = 0;
        while (left < right) {
            int len = right - left + 1;         //满足条件的文章数
            if (citations[left] == h)         //感觉有问题，但不知道那里有问题
                h = Math.max(h, len);
            left++;
        }
        return h;
    }


    /**
     * 704. 二分查找
     */
    public int search704(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] == target)
                return mid;
            else if (nums[mid] < target)
                left = mid + 1;
            else if (nums[mid] > target)
                right = mid - 1;
        }
        return -1;
    }

    //上下写法没啥区别，right 一个是 nums.length 一个是 nums.length - 1，对应<和<=
    public int search7(int[] nums, int target) {
        int left = 0, right = nums.length;
        while (left < right) {
            int mid = (right + left) / 2;
            if (nums[mid] == target)
                return mid;
            else if (nums[mid] < target)
                left = mid + 1;
            else if (nums[mid] > target)
                right = mid;
        }
        return -1;
    }

    //这种是错误写法，因为这种写法也是去寻找目标值所在位置，如果目标值不存在则返回目标值应该存在的位置
    //写法类似于 左滑和右滑，但和其的差异在于，最终状态为left==right
    public int search4(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = (right + left) / 2;
            if (nums[mid] <= target)
                left = mid + 1;
            else if (nums[mid] > target)
                right = mid;
        }
        return left;
    }


    /**
     * 1818. 绝对差值和
     * 二分查找，作为算法的一部分来使用
     */
    private int mod = (int) 1e9 + 7;

    public int minAbsoluteSumDiff(int[] nums1, int[] nums2) {
        long sum = 0;
        long max = 0;             //各个元素“填平”前后的最大差值的绝对值
        int len = nums1.length;
        int[] sorted = Arrays.copyOf(nums1, len);
        Arrays.sort(sorted);    //用于二分查找
        for (int i = 0; i < len; i++) {
            sum += Math.abs(nums1[i] - nums2[i]);
            //在nums1[i] 找到距离 nums2[i]最近的元素
            int index = binSearch(sorted, nums2[i]);
            int temp = Math.abs(sorted[index] - nums2[i]);
            if (index + 1 < len)
                temp = Math.min(temp, Math.abs(sorted[index + 1] - nums2[i]));
            if (index - 1 >= 0)
                temp = Math.min(temp, Math.abs(sorted[index - 1] - nums2[i]));
            max = Math.max(max, Math.abs(Math.abs(nums2[i] - nums1[i]) - temp));
        }
        return (int) ((sum - max) % mod);
    }

    private int binSearch(int[] sorted, int target) {
        int left = 0;
        int right = sorted.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (sorted[mid] < target)
                left = mid + 1;
            else if (target <= sorted[mid])
                right = mid - 1;
        }
        if (left == sorted.length)               //序列单调的情况，会导致left越界
            return sorted.length - 1;
        return left;    //target存在的话，在left位置，不存在的话应该在left位置
    }


    public int minAbsoluteSumDiff6(int[] nums1, int[] nums2) {
        long sum = 0;                       //一定是长整型，否则结果错误
        long maxdiff = 0;                   //一定是长整型，否则结果错误
        //复制数组
        int len = nums1.length;
        int[] sorted = Arrays.copyOf(nums1, len);
        //排序，用于二分搜索
        Arrays.sort(sorted);
        for (int i = 0; i < nums1.length; i++) {
            //在不替换的情况下，计算绝对差值和
            sum += Math.abs(nums1[i] - nums2[i]);
            int Close = Math.abs(nums1[i] - nums2[i]);
            int tempIndex = binSearchClose(sorted, nums2[i]);
            int twoCloseMin = Math.abs(sorted[tempIndex] - nums2[i]);
            if (tempIndex + 1 < nums1.length)
                twoCloseMin = Math.min(twoCloseMin, Math.abs(sorted[tempIndex + 1] - nums2[i]));
            if (tempIndex - 1 >= 0)
                twoCloseMin = Math.min(twoCloseMin, Math.abs(sorted[tempIndex - 1] - nums2[i]));
            maxdiff = Math.max(maxdiff, Math.abs(Close - twoCloseMin));
        }
        return (int) ((sum - maxdiff) % mod);
    }


    //二分搜索的写法不同
    private int binSearchClose(int[] sorted, int target) {
        int left = 0;
        int right = sorted.length - 1;
        while (left < right) {
            int mid = left + ((right - left + 1) >> 1);
            if (sorted[mid] <= target) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }


    public int minAbsoluteSumDiff02(int[] nums1, int[] nums2) {
        TreeSet<Integer> treeSet = new TreeSet<>();
        long sum = 0;
        //各个元素“填平”前后的最大差值的绝对值
        long max = 0;
        int len = nums1.length;
        //将nums1中的元素存储咋treeSet中
        for (int temp : nums1) {
            treeSet.add(temp);
        }
        for (int i = 0; i < len; i++) {
            //算不替换的情况下，二者各项元素的差值和
            sum += Math.abs(nums1[i] - nums2[i]);
            //下面计算，在nums1中找到一个元素替换nums1[i]，使得nums1[*]距离nums2[i]最近
            Integer floor = treeSet.floor(nums2[i]);
            Integer ceiling = treeSet.ceiling(nums2[i]);
            int temp = 0;
            if (floor != null && ceiling != null) {
                if (Math.abs(floor - nums2[i]) - Math.abs(ceiling - nums2[i]) > 0)   //ceiling距离nums2[i]更近
                    temp = ceiling;
                else
                    temp = floor;
            } else if (floor != null) {
                temp = floor;
            } else if (ceiling != null) {
                temp = ceiling;
            }
            max = Math.max(max, Math.abs(nums2[i] - nums1[i]) - Math.abs(temp - nums2[i]));
        }
        return (int) ((sum - max) % mod);
    }


    public int minAbsoluteSumDiff11(int[] nums1, int[] nums2) {
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
    private int binSearchLeftValue(int[] nums, int target) {
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

    private int binSearchRightValue(int[] nums, int target) {
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


    /**
     * 852. 山脉数组的峰顶索引
     */
    public int peakIndexInMountainArray(int[] arr) {
        int left = 0;
        int right = arr.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (arr[mid] > arr[mid + 1])
                right = mid;                 //right右侧上山
            else if ((arr[mid] <= arr[mid + 1]))
                left = mid + 1;              //left左侧上山
        }
        return right;
    }

    //专门用一个变量记录相关值，整体的算法可以写为 既有 +1也有 -1 不用左右的指针记录目标值，用单独的变量记录，因此不会错误相关的目标
    public int peakIndexInMountainArray000(int[] arr) {
        int n = arr.length;
        int left = 1, right = n - 2, ans = 0;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (arr[mid] > arr[mid + 1]) {
                ans = mid;             //******
                right = mid - 1;       //******
            } else {
                left = mid + 1;        //******
            }
        }
        return ans;
    }


    /**
     * 475. 供暖器
     */
    public int findRadius01(int[] houses, int[] heaters) {  //暴力解法
        int radius = -1;
        for (int i = 0; i < houses.length; i++) {
            int minRadius = Integer.MAX_VALUE;
            for (int he = 0; he < heaters.length; he++) {
                minRadius = Math.min(minRadius, Math.abs(houses[i] - heaters[he]));
            }
            radius = Math.max(radius, minRadius);
        }
        return radius;
    }

    //在暴力解法的基础上，优化算法
    public int findRadius(int[] houses, int[] heaters) {
        int radius = -1;
        Arrays.sort(heaters);
        for (int i = 0; i < houses.length; i++) {
            int minRadius = Integer.MAX_VALUE;
            //找到距离house[i]最近的供暖器
            int he = binSearchRadius(heaters, houses[i]);
            minRadius = Math.min(minRadius, Math.abs(houses[i] - heaters[he]));
            if (he - 1 >= 0)
                minRadius = Math.min(minRadius, Math.abs(houses[i] - heaters[he - 1]));
            if (he + 1 < heaters.length)
                minRadius = Math.min(minRadius, Math.abs(houses[i] - heaters[he + 1]));
            radius = Math.max(radius, minRadius);
        }
        return radius;
    }

    private int binSearchRadius(int[] heaters, int house) {
        int left = 0;
        int right = heaters.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (heaters[mid] < house)
                left = mid + 1;
            else if (house <= heaters[mid])
                right = mid - 1;
        }
        if (left >= heaters.length)           //二分查找中，left可能会出现压在数组边界外的情况（即在数组有序，且target>数组的最后一个元素）
            return heaters.length - 1;        //务必注意总结
        return left;
    }

    /**
     * 367. 有效的完全平方数
     */
    public boolean isPerfectSquare01(int num) {
        int sqrt = (int) Math.sqrt(num);
        return sqrt * sqrt == num;
    }

    public boolean isPerfectSquare(int num) {
        int left = 0;
        int right = num;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            long square = (long) mid * mid;         //mid*mid的形式会超出时间限制，必须写为这种形式，不知道为什么
            if (square == num)                      //明确找到目标值的写法
                return true;
            else if (square < num)
                left = mid + 1;
            else if (num < square)
                right = mid - 1;
        }
        return false;
    }

    private int[] nums1, nums2;
    private int n, m;

    /**
     * 373. 查找和最小的 K 对数字
     */
    public List<List<Integer>> kSmallestPairs(int[] n1, int[] n2, int k) {
        nums1 = n1;
        nums2 = n2;
        n = nums1.length;
        m = nums2.length;
        List<List<Integer>> ans = new ArrayList<>();
        int l = nums1[0] + nums2[0], r = nums1[n - 1] + nums2[m - 1];
        while (l < r) {
            int mid = l + r >> 1;
            if (check(mid, k)) r = mid;
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
                int mid = l + r >> 1;
                if (nums2[mid] >= b) r = mid;
                else l = mid + 1;
            }
            if (nums2[r] != b) continue;
            c = r;
            l = 0;
            r = m - 1;
            while (l < r) {
                int mid = l + r + 1 >> 1;
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

    boolean check(int mid, int k) {    //mid为"中间值"
        int ans = 0;    //满足条件的"对数"
        for (int i = 0; i < n && ans < k; i++) {
            for (int j = 0; j < m && ans < k; j++) {    //退出循环的条件一：如果ans>=K，则不满足条件，退出循环
                if (nums1[i] + nums2[j] <= mid)                                       //不满足k对的条件（根本条件）
                    ans++;
                else if (nums1[i] + nums2[i] > mid)    //退出循环的条件二：则当前和之后"对"不满足条件，退出循环
                    break;                                                            //不满足构成"数对"的条件
            }
        }
        return ans >= k;     //以当前值（mid）来分割，"对数"是否满足条件
        //推测此处的目的是，判断退出循环的条件是二者中的哪一个，更严格的写这块应该是  == 而不会有 ans>k的情况
    }


    public List<List<Integer>> kSmallestPairs11(int[] n1, int[] n2, int k) {
        nums1 = n1;
        nums2 = n2;
        n = nums1.length;
        m = nums2.length;
        List<List<Integer>> ans = new ArrayList<>();
        int l = nums1[0] + nums2[0], r = nums1[n - 1] + nums2[m - 1];
        while (l < r) {
            int mid = (int) (0L + l + r >> 1);
            if (check00(mid, k)) r = mid;
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

    boolean check00(int x, int k) {
        int ans = 0;
        for (int i = 0; i < n && ans < k; i++) {
            for (int j = 0; j < m && ans < k; j++) {
                if (nums1[i] + nums2[j] <= x) ans++;
                else break;
            }
        }
        return ans >= k;
    }


    public List<List<Integer>> kSmallestPairs00(int[] nums1, int[] nums2, int k) {
        int m = nums1.length;
        int n = nums2.length;

        /*二分查找第 k 小的数对和的大小*/
        int left = nums1[0] + nums2[0];
        int right = nums1[m - 1] + nums2[n - 1];
        int pairSum = right;

        //基于二分查找算法，找到一个目标值pairSum，此目标值pairSum满足：恰能容纳前k个最小数对和，即pairSum为第k个最小数对和
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            long cnt = 0;
            int start = 0;
            int end = n - 1;
            //基于滑动窗口的思想，计算当前mid值能"容纳（满足条件）"的数组对数
            while (start < m && end >= 0) {
                if (nums1[start] + nums2[end] > mid) {
                    end--;
                } else if (nums1[start] + nums2[end] <= mid) {
                    cnt += end + 1;     //nums1[start]固定，其与nums2中的前end+1个元素构成"数对"，满足"数对和" 小于等于mid的条件
                    start++;            //nums1中固定的元素向前移动一位，进入下一轮循环
                }
            }

            //针对当前的mid，判断其能"容纳（满足条件）"的数组对数 与 题目要求的k对满足条件的数组对 间的关系
            if (cnt < k) {
                left = mid + 1;
            } else if (cnt >= k) {   //右侧移动
                pairSum = mid;
                right = mid - 1;
            }
        }

        //二分结束，此时获得的pairSum即为满足条件的"数对和"，此"数对和"恰好使得两个数组中共有k个数组对小于等于"此数对和"pairSum
        //下面是基于pairSum这个衡量标准，来找到最小的前k个数组对，并记录
        List<List<Integer>> ans = new ArrayList<>();
        int pos = n - 1;
        /*找到小于目标值 pairSum 的数对*/
        //关键在于每次内循环中固定nums1[i]
        for (int i = 0; i < m; i++) {       //此处的i为nums1中的索引
            while (pos >= 0 && nums1[i] + nums2[pos] >= pairSum) {   //pos为nums2中的索引，通过滑动窗口获取nums2中索引在pos前的元素
                //****************************************************且这些元素满足其与nums1[i]构成的数组对的和 小于 pairSum
                //循环结束条件： 针对当前nums1[i]，从右至左，遍历nums[pos]，当两数之和 小于 pairSum，跳出循环
                //关键在于获取pos，此时nums1[i]与 nums[pos]构成的数对满足条件
                //即nums2中的j<=pos的数与nums1[i]构成的数对都满足条件
                pos--;
            }
            //记录nums1[i]与nums2[*]构成的数组对
            for (int j = 0; j <= pos && k > 0; j++, k--) {
                List<Integer> list = new ArrayList<>();
                list.add(nums1[i]);
                list.add(nums2[j]);
                ans.add(list);
            }
        }

        /*找到等于目标值 pairSum 的数对*/
        //关键在于每次内循环中固定nums2[pos]
        pos = n - 1;
        for (int i = 0; i < m && k > 0; i++) {
            while (pos >= 0 && nums1[i] + nums2[pos] > pairSum) {
                //循环结束条件： 针对当前nums1[i]，从右至左，遍历nums[pos]，当两数之和 小于或""等于"" pairSum，跳出循环
                pos--;
            }
            //因此在循环跳出时，nums2以pos为基点，与nums1[i]构成的数组对，小于等于pairSum
            //在nums1中以i（temp）为起点，索引向左移动，去找到nums1[temp]使得其与nums2[pos]构成的数组对 等于 pairSum

            //个人认为，要不就是nums1[i]直接满足 等于 的条件，或者以小于索引i（如ttt）的元素中也有满足条件的（这些nums1[ttt]一定也是和nums1[i]相同）
            //其插入List后也会覆盖，所以不知道为什么要这样写
            for (int temp = i; k > 0 && temp >= 0 && nums1[temp] + nums2[pos] == pairSum; temp--, k--) {    //上下的两个K是续上的
                //没太理解为什么从i开始，也没太理解怎么就直接找到等于的了

                //j为nums1的索引
                //pos为nums2的索引
                List<Integer> list = new ArrayList<>();
                list.add(nums1[temp]);
                list.add(nums2[pos]);
                ans.add(list);
            }
        }
        return ans;
    }


    /**
     * 378. 有序矩阵中第 K 小的元素
     * 暴力解法
     */
    public int kthSmallest(int[][] matrix, int k) {
        int rowNums = matrix.length;
        int colNums = matrix[0].length;
        int[] sorted = new int[rowNums * colNums];
        int m = 0;
        for (int i = 0; i < rowNums; i++) {
            for (int j = 0; j < colNums; j++) {
                sorted[m++] = matrix[i][j];
            }
        }
        Arrays.sort(sorted);
        return sorted[k - 1];
    }

    //错误写法
    public int kthSmallest00(int[][] matrix, int k) {
        ArrayList<Integer> hlist = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                hlist.add(matrix[i][j]);
            }
        }
        hlist.sort(Comparator.naturalOrder());
        return hlist.get(k);
    }


    /**
     * 378. 有序矩阵中第 K 小的元素
     * 基于二分查找，满足二段性
     */
    public int kthSmallest01(int[][] matrix, int k) {
        int rowNums = matrix.length;
        int colNums = matrix[0].length;
        int left = matrix[0][0];
        int right = matrix[rowNums - 1][colNums - 1];
        //让左或右指针压着第k小的数
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            int num = searchNum(matrix, mid, rowNums, colNums);
            if (num < k)
                left = mid + 1;
            else if (k <= num)
                right = mid - 1;
        }
        //可以直接返回left的原因为：最终left为排序序列的第k个元素，其左侧（左侧也包含本身）共有k个数小于等于left
        return left;
    }

    //从左下角开始搜索小于等于mid的数，并记录个数
    public int searchNum(int[][] matrix, int mid, int rowNums, int colNums) {
        int num = 0;
        int row = rowNums - 1;
        int col = 0;
        while (row >= 0 && col <= colNums - 1) {
            if (matrix[row][col] <= mid) {
                //matrix[row][col]元素满足条件，则此元素所"列"的上方元素（matrix[0][col] -> matrix[row][col]）均满足此条件
                num += row + 1;     //必须竖着记录，否则会有部分元素不被记录，和搜索路径相关
                //区别：记录的方向必须和matrix[row][col] > mid时，坐标移动的方向平行，即row变化，故竖着记录。
                //向值大的方向移动
                //由于从左下角开始移动，因此移动的方向只有col++和row--，那么能使得元素值变大的方向只有col++
                col++;   //尝试突破此条件，即找到边界
            } else if (matrix[row][col] > mid) {
                row--;
            }
        }
        return num;
    }

    //从右上角开始搜索小于等于mid的数，并记录个数
    public int searchNum01(int[][] matrix, int mid, int rowNums, int colNums) {
        int num = 0;
        int row = 0;
        int col = colNums - 1;
        while (row <= rowNums - 1 && col >= 0) {
            if (matrix[row][col] <= mid) {
                //matrix[row][col]元素满足条件，则此元素所"列"的上方元素（matrix[0][col] -> matrix[row][col]）均满足此条件
                num += col + 1;   //必须横向记录，和搜索方式相关
                //向值大的方向移动
                //由于从左下角开始移动，因此移动的方向只有col++和row--，那么能使得元素值变大的方向只有col++
                row++;   //尝试突破此条件，即找到边界
            } else if (matrix[row][col] > mid) {
                col--;
            }
        }
        return num;
    }


    /**
     * 441. 排列硬币
     * 暴力解法，此逻辑正确，但最终一个案例超时
     */
    public int arrangeCoins02(int n) {
        int rowNeedCoins = 1;
        int sum = 0;
        if (n == 0) return 0;
        while (rowNeedCoins <= n) {
            //计算第i行满足完整阶梯型所需的累积硬币数
            sum += rowNeedCoins;    //第i行需要i个硬币
            if (sum > n) break;     //与现有硬币数比较，如果现有硬币数不够，则跳出循环，不满足条件
            rowNeedCoins++;         //进入第i行
        }
        return rowNeedCoins - 1;    //当前行不满足，上一行满足条件
    }


    /**
     * 441. 排列硬币
     * <p>
     * 二分搜索-写法一
     *
     * @param n
     * @return
     */
    public int arrangeCoins(int n) {
        int left = 1;   //1 <= n <= 2的31次方 - 1
        int right = n;
        //left和right代表最小和最大行数，通过二分来找到满足条件的行数
        while (left < right) {
            //折半搜索：mid行
            //当区间只剩下两个元素的时候，left = mid 和 right = mid - 1
            //这种划分方式，如果 mid 使用默认下取整的方式，在数值上 left = mid，而它对应的其中一个区间是 [mid..right]，在这种情况下，下一轮搜索区间还是 [left..right]，搜索区间没有减少，会进入死循环。
            int mid = left + ((right - left + 1) >> 1);
            //前mid行满足完整阶梯型所需的Coins数量
            long needCoins = (long) (1 + mid) * mid / 2;   //必须为长整型****否则答案错误
            if (needCoins <= n)
                //则现有的coins数可能可以完成更多行的完整阶梯，故最大的完整阶梯数在mid上或者其右侧，此处不能写为 mid + 1这样可能直接跳过目标值
                left = mid;
            else if (needCoins > n)
                right = mid - 1;
        }
        return left;
    }

    /**
     * 441. 排列硬币
     */
    public int arrangeCoins01(int n) {
        int left = 1;
        int right = n;
        int target = 0;
        while (left <= right) {
            int mid = left + ((right - left + 1) >> 1);
            long needCoins = (long) (1 + mid) * mid / 2;   //必须为长整型****否则答案错误
            //这种写法可能会left直接跳过最终的目标值，因此在下面应该判断一下
            if (needCoins < n)
                left = mid + 1;
            else if (needCoins >= n)
                right = mid - 1;
        }
        target = (long) left * (left + 1) / 2 > (long) n ? left - 1 : left;
        return target;
    }


    /**
     * 611. 有效三角形的个数
     * <p>
     * 思路：在数组排序后，通过两层循环分别固定左侧最短边left和右侧最长边right，同时在最短和最长边间通过二分法搜索能满足条件的""最短的""第二条边tmid
     * 因此，在此当前left和right固定的情况下，数组内能满足第二条边的元素个数为，tmid和right间的元素（具体要分析tmid和两边的大小，以及剔除边界的情况）
     *
     * @param nums
     * @return
     */
    public int triangleNumber(int[] nums) {
        Arrays.sort(nums);
        int sumtriangle = 0;
        //首先，left和right分别作为三角形的最短边和最长边
        //然后，通过二分法，找到满足条件的第二条边的"最短"长度
        for (int left = 0; left < nums.length; left++) {                           //第一条边（最短边）固定值，此轮循环：固定
            for (int right = nums.length - 1; right - left + 1 >= 3; right--) {    //第三条边（最长边）固定值，此轮循环：固定
                int tleft = left + 1;      //第二条边左侧最小值，此轮循环：浮动
                int tright = right - 1;    //第二条边右侧最大值，此轮循环：浮动
                //在最短边和最长边固定的前提下，通过二分搜索到能满足条件的""最短""的第二条边，则这条边与最长边间的元素，即为此轮循环能满足条件的三角形个数
                while (tleft <= tright) {
                    int mid = tleft + ((tright - tleft) >> 1);
                    //判断：两个短边的长度之和 是否 大于 长边长度
                    if (nums[left] + nums[mid] <= nums[right]) {   //必须有等号，因为等于也是不满足条件的，让tleft一直位于满足条件的状态
                        tleft = mid + 1;
                    } else
                        tright = mid - 1;
                }
                //由于tleft在区间内部[left + 1,right - 1]移动，因此不会tleft==nums.length即数组越界的情况
                //但此时tleft也会存在"数组越界"，即跳出了活动区间，最终状态压在右侧边界+1处，即right上，因此需要判断
                if (tleft == right)                                          //1.tleft越界情况的处理
                    sumtriangle += right - (tleft - 1) - 1;   // + 1 - 1 （tleft - 1）满足条件，最后 -1将right剔除掉
//                    sumtriangle += right - (tleft - 1);     //错误写法
                else if (nums[left] + nums[tleft] == nums[right])            //2.tleft不越界情况的处理
                    // 目标值存在，tleft正好压在target上
                    sumtriangle += right - tleft - 1;   //-1是将tleft自身剔除掉
                else if (nums[left] + nums[tleft] > nums[right]) {
                    // 目标值不存在，tleft正好压在target理论的位置上
                    sumtriangle += right - tleft;   // + 1 - 1 抵消 -1将right剔除掉
                }
            }
        }
        return sumtriangle;
    }


    //错误写法
    public int triangleNumber00(int[] nums) {
        Arrays.sort(nums);
        int sumtriangle = 0;
        //首先，left和right分别作为三角形的最短边和最长边
        //然后，通过二分法，找到满足条件的最大边长度，则小于此长度的元素均能构成合理的三角形
        for (int left = 0; left < nums.length; left++) {
            for (int right = nums.length - 1; right >= 0 && right - left + 1 >= 3; right--) {
                int tleft = left;
                int tright = right;
                //左侧和右侧至少间距1个数
                while (tleft < tright - 2) {    //最终状态 tleft==tright - 2
                    int mid = tleft + ((tright - tleft) >> 1);
                    //判断：两个短边的长度之和 是否 大于 长边长度
                    if (nums[left] + nums[mid] > nums[right])
                        tleft = mid;
                    else
                        tright = mid - 1;
                }
                if (nums[left] + nums[tleft] > nums[right])
                    sumtriangle += right - tleft;   // + 1 - 1 抵消
            }
        }
        return sumtriangle;
    }

    /**
     * 基于左右边界（如返回left）返回目标值的二分搜索写法，需要考虑以下两种情况：
     * <p>
     * 1.left可能存在越界的情况
     * ####寻找的目标值不存在&&目标值不在区间范围内，left会停留在目标值理论存在的位置，即在left可移动的区间外，越界
     * ##########越界也分为：
     * #############1.数组越界
     * left可活动的区间为整个数组，此时的越界就意味着数组越界
     * #############2.区间越界
     * left可活动的区间为数组中的某个区间[m,n]，此时的越界不是数组越界，而是区间越界，left会停留在n+1处
     * <p>
     * 2.left不越界的情况下
     * 要考虑以下两种情况：
     * ####寻找的目标值不存在（目标值在区间范围内（如果存在）），left会停留在目标值理论存在的位置，不越界
     * ####寻找的目标值存在
     * 不同的情况，处理方式也不同，请注意
     * <p>
     * 上面的方式较为复杂，可通过一个变量来记录"准"目标值，最终返回即可，不会存在越界的情况，也不许考虑目标值是否存在
     */

    //可通过一个变量来记录"准"目标值，最终返回即可，不会存在越界的情况，也不许考虑目标值是否存在
    public int triangleNumber01(int[] nums) {
        Arrays.sort(nums);
        int sumtriangle = 0;
        //首先，left和right分别作为三角形的最短边和最长边
        //然后，通过二分法，找到满足条件的第二条边的"最短"长度
        for (int left = 0; left < nums.length; left++) {                           //第一条边（最短边）固定值，此轮循环：固定
            for (int right = nums.length - 1; right - left + 1 >= 3; right--) {    //第三条边（最长边）固定值，此轮循环：固定
                int tleft = left + 1;      //第二条边左侧最小值，此轮循环：浮动
                int tright = right - 1;    //第二条边右侧最大值，此轮循环：浮动
                int k = right;             //****
                //在最短边和最长边固定的前提下，通过二分搜索到能满足条件的""最短""的第二条边，则这条边与最长边间的元素，即为此轮循环能满足条件的三角形个数
                while (tleft <= tright) {
                    int mid = tleft + ((tright - tleft) >> 1);
                    //判断：两个短边的长度之和 是否 大于 长边长度
                    if (nums[left] + nums[mid] <= nums[right]) {
                        tleft = mid + 1;
                    } else {
                        k = mid;                //本题不与等号一起存在，因为等号不满足构成三角形的条件，其他题目要具体分析******
                        tright = mid - 1;
                    }
                }
                sumtriangle += right - k;   // + 1 - 1 抵消 -1将right剔除掉
            }
        }
        return sumtriangle;
    }


    /**
     * 611. 有效三角形的个数
     * <p>
     * 官方题解
     * 思路：通过两层循环固定两个最短边，在第二条边至nums.length -1元素间，寻找满足条件的第三条边（最长边）
     * 同时，二分的时候，通过记录的方式来获取最终解，而不用二分的left和right来作为最终解（要进行复杂的判断，详见下下代码）
     */
    public int triangleNumber10(int[] nums) {
        Arrays.sort(nums);
        int sumtriangle = 0;
        //首先，i和j分别作为三角形的最短边和次最短边
        //然后，通过二分法，找到满足条件的最大边长度，则小于此长度的元素均能构成合理的三角形
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length - 1; j++) {
                int target = nums[i] + nums[j];
                int left = j + 1;
                int right = nums.length - 1;
                int k = j;
                while (left <= right) {
                    int mid = left + ((right - left) >> 1);
                    if (nums[mid] < target) {   //满足条件
                        k = mid;                //K记录着满足条件的最大值
                        left = mid + 1;
                    } else
                        right = mid - 1;
                }
                sumtriangle += k - j;  // +1 -1 抵消，+1是算区间长度的，-1是将左边界j减掉
            }
        }
        return sumtriangle;
    }


    //修改二分法中记录最终解的方式（基于二分的left或right，要进行复杂的判断）
    public int triangleNumber11(int[] nums) {
        Arrays.sort(nums);
        int sumtriangle = 0;
        //首先，left和right分别作为三角形的最短边和最长边
        //然后，通过二分法，找到满足条件的最大边长度，则小于此长度的元素均能构成合理的三角形
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length - 1; j++) {
                int target = nums[i] + nums[j];
                int left = j + 1;
                int right = nums.length - 1;
                while (left <= right) {
                    int mid = left + ((right - left) >> 1);
                    if (nums[mid] < target) {   //满足条件
                        left = mid + 1;
                    } else
                        right = mid - 1;
                }
                //以滑动的方式来进行二分，最终要判断一下最终的两个状态
                //1.left是否溢出（target不存在且理论位置位于数组之外）
                if (left == nums.length)
                    sumtriangle += left - j - 1;
//                    left = nums.length - 1;
                    //2.在target存在的情况下，left压在target上
                    //2.在target不存在的情况下，left压在target理论存在的位置
                else if (nums[left] == target)
                    sumtriangle += left - j - 1;   // +1 -1 抵消，+1是算区间长度的，-1是将左边界j减掉
                else if (nums[left] > target)      //此时的left占据着nums[left]大于target的值，因为理论存在，但不存在故被后面大的数占据着
                    //此时left是不满足条件的，因为最长边nums[left] > 两个短边和
                    //此时left-1是满足条件的，故通过left-1来计算满足条件的第三边（存在于j和left-1间）
                    sumtriangle += left - 1 - j;   // +1 -1 抵消，+1是算区间长度的，-1是将左边界j减掉
            }
        }
        return sumtriangle;
    }


    /**
     * 911. 在线选举
     */
    public class TopVotedCandidate {
        //动态记录候选人person和votes的状态
        HashMap<Integer, Integer> voteCounts;
        //记录每次投票后，当前的票王是谁，即<time,person>
        HashMap<Integer, Integer> timeTop;
        //选举时间
        int[] times;

        public TopVotedCandidate(int[] persons, int[] times) {
            //每次选举后，每个person的得票votes状态
            voteCounts = new HashMap<>();
            //每个时刻的票王
            timeTop = new HashMap<>();
            int topVotes = 0;
            for (int i = 0; i < persons.length; i++) {
                //times[i]时刻为persion[i]投一票
                voteCounts.put(persons[i], voteCounts.getOrDefault(persons[i], 0) + 1);
                Integer tempVotes = voteCounts.get(persons[i]);
                //投票后，如果persion[i]的累积票数大于等于当前的最大票数，则记录times[i]时刻的票王为persion[i]
                if (tempVotes >= topVotes) {
                    timeTop.put(times[i], persons[i]);
                    topVotes = tempVotes;
                } else
                    //否则，times[i]时刻的票王仍未上一个时刻tiems[i-1]的票王
                    timeTop.put(times[i], timeTop.get(times[i - 1]));    //不会有数组越界的情况，因为第一个时刻不会走此
            }
            this.times = times;
            System.out.println("voteCounts:" + voteCounts);
            System.out.println("timeTop:" + timeTop);
        }

        //基于二分搜索，找到小于等于查询时刻t的最大投票时间v
        //即找到满足 times[v] <= t 的最大的 l
        //二分方式一：通过变量记录搜索值
        public int q(int t) {
            int left = 0;
            int right = times.length - 1;
            int k = 0;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);   //向左倾斜
                if (times[mid] <= t) {
                    k = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            System.out.println("k: " + k);
            System.out.println("left: " + left);
            return timeTop.get(times[k]);
        }

        //二分方式二：通过left或right记录搜索值，但需要进行复杂的判断
        public int q01(int t) {
            int left = 0;
            int right = times.length - 1;
            int k = 0;
            while (left <= right) {
                int mid = left + ((right - left) >> 1);   //向左倾斜
                if (times[mid] < t) {                    //本题中，k和等号同时使用
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
//        System.out.println("k: " + k);
//        System.out.println("left: " + left);
            int mm = -1;
            if (left == times.length)
                mm = times[left - 1];
            else if (times[left] == t)
                mm = times[left];
            else if (times[left] > t)
                mm = times[left - 1];
            return timeTop.get(mm);
        }

        //二分方式三：通过left和right记录搜索值，最终状态left==right
        public int q02(int t) {
            int left = 0;
            int right = times.length - 1;
            int k = 0;
            while (left < right) {       //一定不能写等号，因为下面写法的最终状态 left==right，如果此处有等号，会进入死循环   ****待深入理解
                int mid = left + ((right - left + 1) >> 1);   //向右倾斜
                if (times[mid] <= t) {                        //本题中，k和等号应该同时使用，因此计算mid的时候，应该+1使其向右倾斜，防止在区间剩余两个元素时进入死循环
                    //在搜索区间变为[m,m+1]时，按照常规的(right + left)/2方式计算mid时;此时left=mid进入死循环
                    //因此，在if中的等号和left/right=mid 同时使用时，计算mid的方式应该发生改变 +1
                    left = mid;
                } else {
                    right = mid - 1;
                }
            }
            return timeTop.get(times[left]);
        }
    }


    /**
     * 875. 爱吃香蕉的珂珂
     */
    public int minEatingSpeed(int[] piles, int targetHour) {
        int left_K = 1;
        int right_K = Arrays.stream(piles).max().getAsInt();
        while (left_K <= right_K) {
            int mid_K = left_K + ((right_K - left_K) >> 1);
            if (minEatingSpeedBinSearch(piles, mid_K) <= targetHour) {
                right_K = mid_K - 1;
            } else {
                left_K = mid_K + 1;
            }
        }
        //----------------------------------------
        // 首先，left_K 右侧不会越界
        // 其次，内部不存在 targetHour，left_K 停留在右侧（函数递减，故右侧为小于 targetHour的最大 K 值）
        // 最后，内部存在多个 targetHour（多个 K 均对应 targetHour），left_K 停留在最左侧 targetHour的索引处，由于（函数递减，故左侧的 K 最小）
        //----------------------------------------
        return left_K;
    }

    private long minEatingSpeedBinSearch(int[] piles, int mid_K) {  //返回当前 K 对应的时间
        long currentHours = 0;  //一定是长整型，否则会溢出
        for (int pile : piles) {
            currentHours += (pile / mid_K);
            currentHours += (pile % mid_K == 0 ? 0 : 1);
        }
        return currentHours;
    }

    //----------------------------------------------------------------------------
    // 注意：上下两题的二分写法，本质都是两个变量间的函数，而且是递减函数
    //----------------------------------------------------------------------------

    /**
     * 1011. 在 D 天内送达包裹的能力
     */
    public int shipWithinDays(int[] weights, int days) {
        int left = Arrays.stream(weights).max().getAsInt();   //最低运载能力
        int right = Arrays.stream(weights).sum();             //最大运载能力
        int weightAns = 1;
        //通过二分法，找到一个合理的最低运载能力，使得货物可在days天内运输完成
        while (left <= right) {
            int mid = left + ((right - left + 1) >> 1);
            //计算当船的运载能力为mid时，至少需要多少天运输完成货物
            int tempDays = 0;
            int tempWeight = 0;
            for (int i = 0; i < weights.length; i++) {
                tempWeight += weights[i];
                if (tempWeight >= mid || i == weights.length - 1) {
                    tempDays++;
                    if (tempWeight > mid)
                        i--;
                    tempWeight = 0;
                }
            }
            if (tempDays <= days) {       //当前船的承载量mid使得最少的运载天数要小于要求的天数，因此船的承载量较大，可降低其承载量，故区间右侧缩小
                weightAns = mid;
                right = mid - 1;
            } else if (tempDays > days) {
                left = mid + 1;
            }
        }
        return weightAns;
    }

    //另一种二分写法
    public int shipWithinDays01(int[] weights, int days) {
        int left = Arrays.stream(weights).max().getAsInt();   //最低运载能力
        int right = Arrays.stream(weights).sum();             //最大运载能力
        int needDays = 1;
        //通过二分法，找到一个合理的最低运载能力，使得货物可在days天内运输完成
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            //计算当船的运载能力为mid时，至少需要多少天运输完成货物
            int tempDays = 0;
            int tempWeight = 0;
            for (int i = 0; i < weights.length; i++) {
                tempWeight += weights[i];
                if (tempWeight >= mid || i == weights.length - 1) {
                    tempDays++;
                    if (tempWeight > mid)
                        i--;
                    tempWeight = 0;
                }
            }
            if (tempDays <= days) {       //当前船的承载量mid使得最少的运载天数要小于要求的天数，因此船的承载量较大，可降低其承载量，故区间右侧缩小
                right = mid;
                //此处right=mid且和if中等号一起使用，因此应该避免死循环，此时上面mid计算应该采用(right - left)/2向左倾斜
                //注意区别left=mid且和if中等号一起使用时mid计算的区别，此时向左倾斜会导致死循环，因此，mid计算应该采用(right - left + 1)/2右侧倾斜，压在右侧边界
            } else if (tempDays > days) {
                left = mid + 1;
            }
        }
        return right;
    }


    /**
     * 1894. 找到需要补充粉笔的学生编号
     */
    public int chalkReplacer(int[] chalk, int k) {  //暴力解法：这种写法在K很大时，会遍历多次chalk，时间复杂度高
        int i = 0;
        while (i < chalk.length) {
            k -= chalk[i];
            if (k < 0) {   //不够用
                return i;
            }
            if (i == chalk.length - 1)  //K仍然够用，但学生轮循完一次，进入下一次轮循
                i = 0;
            else
                i++;
        }
        return -1;
    }

    //优化暴力解法：算sum并取余，只关心最后一轮迭代，最多对数组进行两次迭代
    public int chalkReplacer01(int[] chalk, int k) {
//        int sum = Arrays.stream(chalk).sum();    //不能这样写，会忽略K不能满足一轮消耗的情况
        int sum = 0;
        for (int i = 0; i < chalk.length; i++) {
            sum += chalk[i];
            if (sum > k)
                return i;
        }
        //取余，只关心最后一轮迭代
        int target = k % sum;
        int temp = 0;
        for (int i = 0; i < chalk.length; i++) {
            temp += chalk[i];
            if (temp > target)
                return i;
        }
        return -1;
    }


    //前缀和 + 二分搜索
    public int chalkReplacer02(int[] chalk, int k) {
        int sum = chalk[0];
        if (chalk[0] > k) return 0;     //这种写法要考虑此种情况
        //计算前缀和，即到达此位置所需要的最少粉笔数
        for (int i = 1; i < chalk.length; i++) {
            sum += chalk[i];
            chalk[i] += chalk[i - 1];
            if (chalk[i] > k)
                return i;
        }
        //取余，只关心最后一轮迭代
        int target = k % sum;
        int left = 0;
        int right = chalk.length - 1;
        //二分搜索的目的是找到，满足chalk[i]>target的最小值
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (chalk[mid] <= target)
                left = mid + 1;
            else if (chalk[mid] > target) {
                right = mid;
            }
        }
        return left;
    }


    //前缀和 + 二分搜索（方式区别于上）
    public int chalkReplacer03(int[] chalk, int k) {
        int sum = chalk[0];
        if (chalk[0] > k) return 0;     //这种写法要考虑此种情况
        //计算前缀和，即到达此位置所需要的最少粉笔数
        for (int i = 1; i < chalk.length; i++) {
            sum += chalk[i];
            chalk[i] += chalk[i - 1];
            if (chalk[i] > k)
                return i;
        }
        //取余，只关心最后一轮迭代
        int target = k % sum;
        int left = 0;
        int right = chalk.length - 1;
        //二分搜索的目的是找到，满足chalk[i]>target的最小值
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (chalk[mid] < target)
                left = mid + 1;
            else if (chalk[mid] >= target) {
                right = mid - 1;
            }
        }

        int tt = 0;
        if (left == chalk.length)
            tt = -2;//这种情况不存在
        else if (chalk[left] == target)
            tt = left + 1;
        else if (chalk[left] > target)    //target不存在，故target的位置上，是右侧补上来的数据，此数据大于target
            tt = left;
        return tt;
    }
//        if (right == -1)
//            return 0;
//        else if (chalk[right] == target)
//            return right + 1;
//        else if (chalk[right] < target)
//            return right;

    //注意：滑动的时候，左侧逼近和右侧逼近，要仔细再思考一下
    //无论哪侧逼近，如果target不存在，其左侧逼近时left停留的位置和右侧逼近时right停留的位置一样，此位置上的值>target(数组有序，target不存在，右侧的数补充上来)，但差异在于两种写法，使用的返回值不同（一个用left一个用right）


    public int search00(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else if (target < nums[mid]) {
                right = mid - 1;
            }
        }
        return -1;
    }


    public int binSearch123(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + ((right - left + 1) >> 1);
            if (nums[mid] <= target) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        System.out.println("left:" + left);
        System.out.println("right:" + right);
        return left;
    }

    public int binSearch12(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        System.out.println("left:" + left);
        System.out.println("right:" + right);
        return right;
    }


    public int binSearch321(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int k = right;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] < target) {
                left = mid + 1;
            } else if (target <= nums[mid]) {
                k = mid;
                right = mid - 1;
            }
        }
        System.out.println("left:" + left);
        System.out.println("right:" + right);
        return k;
    }

    public int binSearch32(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int k = left;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] <= target) {
                k = mid;
                left = mid + 1;
            } else if (target < nums[mid]) {
                right = mid - 1;
            }
        }
        System.out.println("left:" + left);
        System.out.println("right:" + right);
        return k;
    }


    /**
     * ？？？？
     */
    public int minimumDifference(int[] nums, int k) {
        Arrays.sort(nums);
        int left = 0;
        int right = k - 1;
        int ans = Integer.MAX_VALUE;
        while (right < nums.length) {
            ans = Math.min(ans, nums[right] - nums[left]);
            right++;
            left++;
        }
        return ans;
    }


    static {
        ArrayList<Integer> lists = new ArrayList<>();
        lists.add(1);
        lists.add(1);
        int a = 1;
        int b = 1;
        int Fib = -1;
        while (Fib <= Math.pow(10, 9)) {
            Fib = a + b;
            lists.add(Fib);
            a = b;    //a移动至 b的大小，记录 b
            b = Fib;  //b记录Fib
        }
        int[] nums = lists.stream().mapToInt(Integer::intValue).toArray();
    }

    //预计算
    private static final int[] FIB = {
            1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597,
            2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418, 317811,
            514229, 832040, 1346269, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817,
            39088169, 63245986, 102334155, 165580141, 267914296, 433494437, 701408733, 1134903170
    };


    //二分法写法一：从左侧逼近target
    private int binSearchNextFib(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {  //最终状态 left = right
            int mid = left + ((right - left) >> 1);  //mid必须向右侧倾斜，防止进入死循环
            if (nums[mid] <= target)
                left = mid + 1;
            else if (target < nums[mid])
                right = mid - 1;
        }
        //虽然这样的写法，会有数组越界的风险，但数组已知且target已知，经评估不会越界，无需判断
        return nums[right]; //target存在返回 target、target不存在 right位于target的左侧，即左侧逼近
    }

    //二分法写法二：从左侧逼近target
    private int binSearchNextFib01(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {  //最终状态 left = right
            int mid = left + ((right - left + 1) >> 1);  //mid必须向右侧倾斜，防止进入死循环
            if (nums[mid] <= target)
                left = mid;
            else if (target < nums[mid])
                right = mid - 1;
        }
        return nums[left];
    }

    //二分法写法三：从左侧逼近target
    private int binSearchLeft(int[] FIBs, int target) {
        int left = 0;
        int right = FIBs.length - 1;
        int k = left;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (FIBs[mid] <= target) {
                k = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return FIBs[k];  //左侧逼近值
    }





    /**
     * 1760. 袋子里最少数目的球
     */
    public int minimumSize(int[] nums, int maxOperations) {   //非直观的二分搜索，需要借助函数图像来表示
        //-----------------------------------------------------
        // 将题目转换为判定性问题，本题二分的值是袋子中球的个数，根据题意获得二分起始的左右边界（最小值、最大值）
        // 由于两个因素有因果关系，构建二者之间的函数，即"最终单个袋子中拥有的最多的球数"（二分获取）和"needOperationsNums"（计算）
        // 将每次二分获取的球数，经过计算得到 needOperationsNums，让其与 maxOperations比较，从而决定下一次二分的方向
        //
        // 本题二分的思路就是二分单个袋子里面的球的个数的最大值，也就是袋子容量，然后判断此次的操作数有没有大于最大操作数
        // 如果大于最大操作数，违法了。那么我们就减少操作数即可，减少操作数对应就是增加单个袋子容量
        // 如果小于等于最大操作数，合法。因此我们可以增大操作数，或者保持不变，那么对应就是减少单个袋子容量
        //-----------------------------------------------------
        int left = 1;                                        //操作后袋子中最小的球个数
        int right = Arrays.stream(nums).max().getAsInt();    //操作后袋子中最大的球个数
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (maxOperations < needOperationsNums(nums, mid)) {
                left = mid + 1;
            } else {
                right = mid - 1;   //相等的时候，将期望目标的最大值变小，使得最终结果/"代销"最小
            }
        }
        return left;
    }

    private int needOperationsNums(int[] nums, int mid) {
        int ans = 0;
        for (int num : nums) {
            ans += (num - 1) / mid;  //针对每一个袋子中的数量，除以当前期望的每个袋子最终存放的球的数量，向下取整
        }
        return ans;
    }


    /**
     * 1552. 两球之间的磁力
     */
    public int maxDistance(int[] position, int target) {
        int n = position.length;
        Arrays.sort(position);
        int left = 1;           //任意两球间磁力的最小值
        int right = position[n - 1] - position[0];   //任意两球间磁力的最大值
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            //枚举的磁力，计算为了保持任意两球间磁力大于等于当前枚举的磁力，这些篮子里所能容下的球数
            if (getValue(position, mid) < target) {   //1、球数小于目标球数，减少磁力
                right = mid - 1;
            } else {                                  //2、球数大于目标球数，增大磁力
                left = mid + 1;
            }
            //注意：针对当前磁力使得可容纳的球数和目标球数相等，此时应该继续尝试增大磁力
        }
        return right;
    }

    private int getValue(int[] position, int diff) {
        int ans = 1;  //第一个球放在 position[0] 位置上
        int cur = position[0];
        for (int i = 1; i < position.length; i++) {
            if (position[i] - cur >= diff) {
                cur = position[i];
                ans++;
            }
        }
        return ans;
    }



    /**
     * 1802. 有界数组中指定下标处的最大值
     */
    public int maxValue(int n, int index, int maxSum) {  //二分搜索 + 数学求和
        int left = 1;
        int right = maxSum;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            long currSum = mid + getSum(mid - 1, index) + getSum(mid - 1, n - index - 1);
            if (currSum <= maxSum) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return right;
    }

    private long getSum(int max, int len) {
        //---------------------------------------
        // 贪心计算单侧和，贪心逻辑：类比山峰，在地面高度和下山步数固定时
        //    想要使得山峰高度最高，那么每走一步都应该是下降的趋势，进而上山是每一步都是上山
        // 补充：地面高度最低为 1、下山步数为单侧到达边界的长度
        //---------------------------------------
        if (max > len) {
            int small = max - len + 1;
            return (long) (max + small) * len / 2;   //（首项 + 末项）*项数 / 2
        } else {
            int oneSum = len - max;  //冗余 1 的个数，平路的长度
            int small = 1;
            return (long) (max + small) * max / 2 + oneSum;
        }
    }

    private long getSum01(int n, int index, int maxSum, int res) {  //暴力求和
        long sum = res;
        for (int i = index - 1; i >= 0 && sum <= maxSum; i--) sum += Math.max(res - (index - i), 1);
        for (int i = index + 1; i < n && sum <= maxSum; i++) sum += Math.max(res - (i - index), 1);
        return sum;
    }


    public int maxValue01(int n, int index, int maxSum) {
        for (int res = maxSum; res >= 1; res--) {  //暴力枚举
            long sum = res;
            //暴力求和
            for (int i = index - 1; i >= 0 && sum <= maxSum; i--) sum += Math.max(res - (index - i), 1);
            for (int i = index + 1; i < n && sum <= maxSum; i++) sum += Math.max(res - (i - index), 1);
            //判断是否满足条件
            if (sum <= maxSum) return res;
        }
        return -1;
    }



    /**
     * 2389. 和有限的最长子序列
     */
    public int[] answerQueries(int[] nums, int[] queries) {
        int n = nums.length;
        int m = queries.length;
        Arrays.sort(nums);
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        int[] ans = new int[m];
        for (int i = 0; i < m; i++) {
            ans[i] = answerHelp(prefix, queries[i]);
        }
        return ans;
    }

    private int answerHelp(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (nums[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return right;
    }


}



