package leetcode.algorithm;

import java.util.*;


/**
 * 位运算专题
 */
public class Byte {


//------------------------------------------------------------------------------------------------------
// 难度：简单
//------------------------------------------------------------------------------------------------------


    /**
     * 231. 2 的幂
     * 难度：简单
     */
    public boolean isPowerOfTwo(int n) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            res += (n >> i) & 1;
        }
        if (((n >> 31) & 1) == 1) //2的次幂一定是正数，小于0的情况一定不满足条件
            return false;
        return res == 1;
    }

    public boolean isPowerOfTwo01(int n) {
        if (n > 0) {   //2的次幂一定是正数，小于0的情况一定不满足条件
            int res = 0;
            for (int i = 0; i < 32; i++) {
                res += (n >> i) & 1;
            }
            return res == 1;
        }
        return false;
    }

    public boolean isPowerOfTwo02(int n) {
        return n > 0 && ((n & (n - 1)) == 0);   //n & (n - 1) 该位运算技巧可以直接将 n 二进制表示的最低位 1 移除
    }

    public boolean isPowerOfTwo03(int n) {
        if (n <= 0)
            return false;
        return (n & (-n)) == n;   //n & (-n) n>0时，如果结果为 n,则 n 为 2 的幂
    }

    /**
     * 342. 4的幂
     */
    public boolean isPowerOfFour(int n) {
        if (n <= 0) return false;
        int res = 0;
        //---------------------------------------------------
        // 奇数和偶数位为1的含义：
        //     1、偶数位为 1，在二进制中此位代表2的偶数次方，即4的*次方
        //     2、奇数位为 1，在二进制中此位代表2的奇数次方，一定不是4的*次方
        //---------------------------------------------------
        for (int i = 0; i < 16; i++) {
            res += (n >> 2 * i) & 1;
            if (res == 2)            //理论4的幂对应的应该仅有一个偶数位为 1
                return false;
            if (((n >> (2 * i - 1)) & 1) == 1)    //奇数位为1，一定不满足条件
                return false;
        }
        return true;
    }

    public boolean isPowerOfFour01(int n) {
        int M0 = 0xaaaaaaaa;   //奇数为 1，所以要判断"与"后是否为 0
        //-----------------------------------
        // 条件一：大于零
        // 条件二：对应的二进制形式中只有一个 1
        // 条件三：对应的二进制形式中，1要出现在偶数位，所以 "与" 后要判断为 0
        //------------------------------------
        return n > 0 && (n & (n - 1)) == 0 && (n & M0) == 0;
    }

    public boolean isPowerOfFour02(int n) {
        int M = 0x55555555;      //01010101010101010101010101010101
        return n > 0 && (n & (n - 1)) == 0 && (n & M) != 0;
    }


    /**
     * 190. 颠倒二进制位
     * 0x代表十六进制，其中每个元素代表4位二进制位，5代表0101、3代表0011、f代表1111、a代表1010等等
     * 难度：简单
     */
    public int reverseBits(int n) {
        int M1 = 0x55555555;   //01010101010101010101010101010101
        int M2 = 0x33333333;   //00110011001100110011001100110011
        int M4 = 0x0f0f0f0f;   //00001111000011110000111100001111
        int M8 = 0x00ff00ff;   //00000000111111110000000011111111
        int M16 = 0x0000ffff;  //00000000000000001111111111111111
        //分治
        n = n >> 1 & M1 | (n & M1) << 1;   //一位为一个单元：偶数位右移一位|奇数位左移一位
        n = n >> 2 & M2 | (n & M2) << 2;   //两位为一个单元：偶数单元右移两位|奇数单元左移两位
        n = n >> 4 & M4 | (n & M4) << 4;   //四位为一个单元：偶数单元右侧四位|奇数单元左移四位
        n = n >> 8 & M8 | (n & M8) << 8;   //八位为一个单元：偶数单元右侧八位|奇数单元左移八位
        n = n >> 16 & M16 | (n & M16) << 16;   //十六位为一个单元：偶数单元右侧十六位|奇数单元左移十六位
        return n;
    }

    public int reverseBits00(int n) {
        int M1 = 0x55555555;  //01010101010101010101010101010101
        int M2 = 0x33333333;  //00110011001100110011001100110011
        int M4 = 0x0f0f0f0f;  //00001111000011110000111100001111
        int M8 = 0x00ff00ff;  //00000000111111110000000011111111
        int M16 = 0x0000ffff; //00000000000000001111111111111111
        n = (n & M16) << 16 | (n >> 16) & M16;
        n = (n & M8) << 8 | (n >> 8) & M8;
        n = (n & M4) << 4 | (n >> 4) & M4;
        n = (n & M2) << 2 | (n >> 2) & M2;
        n = (n & M1) << 1 | (n >> 1) & M1;
        return n;
    }

    //遍历一次的倒序，理解为水流，尾接头、头接尾的倒序形式
    public int reverseBits01(int n) {
        int res = 0;
        for (int i = 0; i < 32 && n != 0; i++) {
            res |= (n & 1) << (31 - i);   //理解为一个bucket，由右向左每次填充一个数据（从末尾取的）
            n >>>= 1;   //没有无符号整数类型，因此对 n 的右移操作应使用逻辑右移
        }
        return res;
    }

    public int reverseBits02(int n) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            res.append((n >> i) & 1);
        }
        int ans = 0;
        //res本质已经将二进制的形式反转了，但对应的数组如果从0开始遍历，就相当于仍是从n二进制形式的右侧开始遍历，等效于没反转
        //为了仍可从0开始遍历数组，故对res再进行反转即可，即最终res的形式和n的二进制表示形式相同，但反转后的结果从0开始遍历，相当于从n的二进制形式的左侧开始，等效于反转
        char[] target = res.reverse().toString().toCharArray();
        for (int i = 0; i < target.length; i++) {
            //由于数组为char类型，故意应该先将各个元素转换为int型，结果只有 0 或 1
            ans |= ((target[i] - '0') << i);  // +号等效
        }
        return ans;
    }


    public int reverseBits03(int n) {
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

    /**
     * 191. 位 1的个数
     * 难度：简单
     */
    public int hammingWeight(int n) {
        int res = 0;
        while (n != 0) {
            res += n & 1;  //右移二进制，每次二进制最低位 与 1 比较，并计数
            n >>>= 1;
        }
        return res;
    }


    public int hammingWeight01(int n) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            if ((n & (1 << i)) != 0)     //左移 1，与二进制的对应位比较，并计数
                res++;
        }
        return res;
    }

    public int hammingWeight00(int n) {
        int res = 0;
        while (n != 0) {
            n = n & (n - 1);  //寻找并消除最低位 1，计数
            res++;
        }
        return res;
    }

    /**
     * 461. 汉明距离
     */
    public int hammingDistance(int x, int y) {
        int res = 0;
        int bin = x ^ y;      //最聪明的方式，所有位置一次性的计算完，是都相等
        for (int i = 0; i < 32 && bin != 0; i++) {
            res += bin & 1;   //逐一判断，不相等位置的个数
            bin >>>= 1;       //无符号右移
        }
        return res;
    }

    public int hammingDistance03(int x, int y) {
        int res = 0;
        int t = x ^ y;
        while (t != 0) {  //因为x和y均大于零，故不需要考虑负数的情况
            t &= t - 1;
            res++;
        }
        return res;
    }


    public int hammingDistance01(int x, int y) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            int xx = (x >> i) & 1;  //略笨的方式，诸位进行移动和比较
            int yy = (y >> i) & 1;
            res += xx ^ yy;   //不相等的为 1，聪明的点在于：没有引入其他变量
        }
        return res;
    }

    public int hammingDistance02(int x, int y) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            if ((((x >> i) & 1) ^ ((y >> i) & 1)) != 0)   //最笨的方法，原因一：逐位进行移动和比较
                res++;    //原因二：引入其他变量
        }
        return res;
    }


    //---------------------------------------------
    // 1、上面的逻辑：两个数比较，常规做法，同时比较
    // 2、下面的逻辑：两个数比较，常规做法，两个数两个数比较，时间复杂度高；聪明做法，所有数按位同时比较
    //---------------------------------------------

    /**
     * 477. 汉明距离总和
     */
    public int totalHammingDistance(int[] nums) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            int xx = 0;
            int yy = 0;
            //针对所有元素的每一位并行处理，计算异同的个数
            for (int num : nums) {
                if (((num >> i) & 1) == 1)  //分治
                    xx++;
                else
                    yy++;
            }
            res += xx * yy;
        }
        return res;
    }

    public int totalHammingDistance01(int[] nums) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            int m = 0;
            for (int num : nums) {
                m += num >> i & 1;
            }
            res += m * (nums.length - m);   //相较于上的优化
        }
        return res;
    }

    public int totalHammingDistance02(int[] nums) {
        int res = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                res += binDistance(nums[i], nums[j]);   //暴力解法：超时
            }
        }
        return res;
    }

    private int binDistance(int a, int b) {
        int xx = a ^ b;
        int res = 0;
        while (xx != 0) {
            res += xx & 1;
            xx >>>= 1;
        }
        return res;
    }

    /**
     * 693. 交替位二进制数
     */
    public boolean hasAlternatingBits(int n) {
        int len = Integer.toBinaryString(n).length();
        int last = -1;
        for (int i = 0; i < len; i++) {   //逐位与上一位比较
            int now = (n >> i) & 1;
            if (now == last)
                return false;
            last = now;
        }
        return true;
    }

    public boolean hasAlternatingBits01(int n) {
        int last = -1;
        while (n != 0) {   // n > 0，不需要考虑负数的情况
            int now = n & 1;
            if (now == last)   //逐位与上一位比较
                return false;
            last = now;
            n >>= 1;
        }
        return true;
    }

    public boolean hasAlternatingBits02(int n) {
        int T = 0x55555555;  //01010101010101010101010101010101
        for (int i = 0; i < 32; i++) {
            if (T == n)
                return true;
            else
                T >>= 1;
        }
        return false;
    }

    public boolean hasAlternatingBits03(int n) {
        //异或运算符:  两个位相等则为 0，不相等则为 1
        int a = n ^ (n >> 1);  //如果 n 为交替二进制位，则 a 的结果类似于 0000...1111
        return (a & (a + 1)) == 0;
    }


    /**
     * 137. 只出现一次的数字 II
     * 难度：中等
     * 思路：重要
     */
    public int singleNumber(int[] nums) {
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int num : nums)
            hTable.put(num, hTable.getOrDefault(num, 0) + 1);
        for (int num : nums)
            if (hTable.get(num) == 1)
                return num;
        return -1;
    }

    public int singleNumber01(int[] nums) {
        //1、使用长度为 32 的数组res[] 记录下所有数值的每一位共出现了多少次 1
        //2、对 res[] 数组的每一位进行 mod3 操作，重新拼凑出只出现一次的数值
        int[] res = new int[32];
        int ans = 0;
        for (int i = 0; i < 32; i++) {   // 关键：所有 num 同时处理同一位
            for (int num : nums) {
                if (((num >> i) & 1) == 1)
                    res[i]++;
                //---------------------------
                // 或者写为 res[i] += ((num >> i) & 1);
                //---------------------------
            }
        }
        for (int i = 0; i < 32; i++) {
            if (res[i] % 3 == 1) //只会为0或1，0：出现三次，1：仅出现一次
                ans += (1 << i);
        }
        return ans;
    }

    public int singleNumber02(int[] nums) {
        int[] res = new int[32];
        for (int num : nums) {
            int i = 0;
            while (num != 0) {    // 一次处理完一个 num
                res[i] += (num & 1);
                num >>>= 1;   //必须是无符号右侧移动，否则针对负数的情况失败（循环无法终止导致数组越界），且每次移动一位
                i++;
            }
        }
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            if (res[i] % 3 == 1)
                ans |= (1 << i);
            //--------------------------
            // ans += (1 << i)，二者等效
            //--------------------------
        }
        return ans;
    }


    /**
     * 268. 丢失的数字
     */
    public int missingNumber(int[] nums) {
        int n = nums.length;
        int XOR = 0;
        for (int i = 0; i < n; i++) {
            XOR ^= i;
            XOR ^= nums[i];
        }
        XOR ^= n;
        return XOR;
    }

    /**
     * 136. 只出现一次的数字
     */
    public int singleNumber_I(int[] nums) {
        int res = 0;
        for (int num : nums)
            res = res ^ num;
        return res;
    }

    /**
     * 260. 只出现一次的数字 III
     * 技巧：异或
     */
    public int[] singleNumberIII(int[] nums) {
        int res = 0;
        for (int num : nums)
            res ^= num;
        //---------------------------------------------------------------
        // n & (-n)位运算的结果为：获取 n 从右至左第一个非 0 的位置，二进制的形式标识第一个非 0 的位置
        // 因此，如果 n 是正整数并且 n & (-n) = n，那么 n 就是 2 的幂
        //---------------------------------------------------------------
        //Integer.MIN_VALUE的二进制表示就是符号位是1，其余都为0，所以求他的最低位1代表的整数就是他自己
        int xor = res == Integer.MIN_VALUE ? res : res & (-res);  //注意：这个并不是具体第几位，而是对应了一个二进制
        int[] target = new int[2];
        for (int num : nums) {
            //-----------------------------------------------------------
            //采用分治的思想：通过获取最低有效位，将数组分为两部分或者两个队列
            //              每一队列的特点都是：仅有一个出现一次的的数字，其余数字出现两次
            //-----------------------------------------------------------
            if ((num & xor) == 0)   //重要
                target[0] ^= num;
            else
                target[1] ^= num;
        }
        return target;
    }

    public int[] singleNumberIII00(int[] nums) {
        int res = 0;
        for (int num : nums)
            res ^= num;
        int k = 0;   //记录需要移动的偏移量
        while ((res & 1) == 0) {   //记录最低位 1 的位置
            res >>= 1;
            ++k;
        }
        int[] target = new int[2];
        for (int num : nums) {
            if (((num >> k) & 1) == 0)  //同上区别：将num右移多少位，并于 1 做与操作，进行分流
                target[0] ^= num;
            else
                target[1] ^= num;
        }
        return target;
    }

    public int[] singleNumberIII01(int[] nums) {
        int[] res = new int[2];
        int ans = 0;
        for (int num : nums)
            ans ^= num;
        //n & (-n)位运算的结果为：获取 n 从右至左第一个非 0 的位置，二进制的形式标识第一个非 0 的位置
        int t = ans & (-ans);
        int n = 0;
        //计算第一个非0位置的偏移量
        while (t != 0) {
            n++;
            t >>>= 1;
        }
//        n = Integer.toBinaryString(t).length();  //效果同上
        n--;  //上面计算的是从右至左首个非0位置，其对应的偏移量应该减 1
        for (int num : nums) {
            if (((num >> n) & 1) == 0)
                res[0] ^= num;
            else
                res[1] ^= num;
        }
        return res;
    }


    /**
     * 338. 比特位计数
     */
    public int[] countBits(int n) {
        int[] res = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            res[i] = countOnes(i);
        }
        return res;
    }

    private int countOnes(int n) {
        int count = 0;
        while (n != 0) {
            n &= (n - 1);   //挺好的想法，剔除一个 1 记录一个 1 ，计算 1 的个数
            count++;
        }
        return count;
    }

    private int countOnes00(int n) {
        int count = 0;
        for (int i = 0; i < 32; i++) {
            count += (n >> i) & 1;
        }
        return count;
    }

    private int countOnes01(int n) {
        int count = 0;
        while (n != 0) {
            count += n & 1;
            n >>>= 1;
        }
        return count;
    }

    //-----------------------------
    // 动态规划：需要发现规律
    //-----------------------------
    public int[] countBits01(int n) {
        int[] res = new int[n + 1];
        int hight = 0;
        for (int i = 1; i <= n; i++) {
            if ((i & (i - 1)) == 0)  //每一轮迭代开始，出现新高位，高位指：二进制形式中仅有一个1，且位置提升
                hight = i;
            //注意：高位后面的位置的组合，前面已经出现，在此轮循环中只需要在之前的基础上，将高位添加进去就行
            res[i] = res[i - hight] + 1;
            //---------------------------
            // hight：递归的区间范围
            // i - hight：此位与递归的区间内匹配的位置
            // + 1：在匹配值的基础上，将高位加上
            //---------------------------
        }
        return res;
    }


    /**
     * 371. 两整数之和
     * 难度：简单
     * 技巧：异或
     */
    public int getSum(int a, int b) {
        //--------------------------------------
        // a 定义为无进位加法的结果
        // b 定义为进位的结果，仅在计算下一位时使用，因此，当 b 为 0 时，则说明无进位的可能，此时 无进位加法的结果 a 为目标值
        //--------------------------------------
        while (b != 0) { //不为 0，则说明二进制表达式中仍有 1，即需要进位
            //注意：
            // 1、这样的计算方式，并非从右至左，逐位计算进位
            // 2、而是全局统一进位，下一轮迭代是处理某位自身为 1，同时上一轮结果中此位要进 1 ，导致二者相加也要向前进 1 的情况，每轮迭代如此
            int up = (a & b) << 1; //无符号进位，用于下一位的使用，本位不使用
            a ^= b;  //无符号加法，其中的 b为上一轮进位值（0或1）
            b = up;
        }
        return a;
    }

    public int getSum01(int a, int b) {
        while (a != 0) {
            int newA = (a & b) << 1;
            int newB = a ^ b;
            a = newA;
            b = newB;
        }
        return a | b;
    }

    public int getSum02(int a, int b) {
        int res = 0;
        int t = 0;
        for (int i = 0; i < 32; i++) {
            int aBit = (a >> i) & 1;
            int bBit = (b >> i) & 1;
            //--------------------------------------------------
            //逐位比较两个数二进制形式的每一位，共有以下三种情况：
            //   1、两个当前位均为 1
            //        此时当前位是什么，仅取决于前一位的进位置情况
            //   2、两个当前位仅有一位是 1
            //        此时当前位是什么，取决于前一位的进位置情况和当前位的 1
            //   3、两个当前位均为 0
            //        此时当前位是什么，仅取决于前一位的进位置情况
            //   注意：
            //        或操作：两位中有一位是1则为1，否则为0
            //--------------------------------------------------
            if (aBit == 1 && bBit == 1) {
                res |= (t << i);  //此位数值：仅取决于t，因为 二者相加为 1已经贡献/进一位
                t = 1;            //贡献：进一位，即 t = 1;
            } else if (aBit == 1 || bBit == 1) {
                res |= ((t ^ 1) << i); //此位数值：取决于累加结果和 t
//                if (t == 1) {
//                    res |= (0 << i);
//                    t = 1;
//                } else {
//                    res |= (1 << i);
//                    t = 0;
//                }
            } else {
                res |= (t << i);
                t = 0;  //如果 t为1，则进位此处被消耗，故置 0
            }
        }
        return res;
    }


    /**
     * 476. 数字的补数
     */
    public int findComplement(int num) {
        //和长度相等的二进制表示全为 1的数，取异或即可
        int len = Integer.toBinaryString(num).length();
        //len类似于数组的nums.length，因此num非零的最高位应该位于 len -1 "坐标"
        return num ^ ((1 << len) - 1);  //先移动指高位的高一位，减 1 即可获得...
    }

    public int findComplement01(int num) {
        int highBit = num;
        //highBit最终是要获取和 (1 << len) - 1 一样的二进制表示形式，从num的非 0最高位至最低位全部置为 1
        highBit |= highBit >> 1;
        highBit |= highBit >> 2;
        highBit |= highBit >> 4;
        highBit |= highBit >> 8;
        highBit |= highBit >> 16;
        return highBit ^ num;
    }

    public int findComplement02(int num) {
        int res = 0;
        for (int i = 0; i < 32 & num != 0; i++) {
            //两个数做异或操作，要注意会对所有位进行比较并进行异或，看是否是想要的结果
            res |= (num & 1 ^ 1) << i;  //要先基于 & 将最低位的值取出来，才能于 1 做异或
            num >>= 1;
        }
        return res;
    }


    /**
     * 1711. 大餐计数
     */
    public int countPairs(int[] deliciousness) {  //思路非常好，针对重复的元素，只需要检验一次
        int MOD = 1000000007;
//        int MOD = (int) Math.pow(10, 9) + 7;  //等效于上
        HashMap<Integer, Integer> hTable = new HashMap<>();
        int sumTaget = 1 << 21;  //单个餐品的最大值为2的20次幂，故“大餐”最大值为2的21次幂（两个最大值的餐品的和）
        long res = 0;
        for (int del : deliciousness) {
            for (int sum = 1; sum <= sumTaget; sum <<= 1) {  //sum以2的次幂增大，初始值为1，不能为0
                Integer r = hTable.getOrDefault(sum - del, 0);  //找到在del之前的美食，是否与del能够构成“大餐”
                res = (res + r) % MOD;   //注意，只要涉及需要取模的，在每次计算后都要进行处理
            }
            hTable.put(del, hTable.getOrDefault(del, 0) + 1);
        }
        return (int) res % MOD;
    }

    //暴力解法：超时
    public int countPairs01(int[] deliciousness) {
        int MOD = 1000000007;
        long del = 0;
        for (int i = 0; i < deliciousness.length - 1; i++) {
            for (int j = i + 1; j < deliciousness.length; j++) {
                long delValue = deliciousness[i] + deliciousness[j];
                if (delValue > 0 && (delValue & (delValue - 1)) == 0)  //判断，两数之和是否为2的幂
                    del++;
            }
        }
        return (int) del % MOD;
    }


    /**
     * 405. 数字转换为十六进制数
     */
    public String toHex(int num) {
        if (num == 0)
            return "0";
        long numLong = num;
        if (num < 0) {
            //处理负数补码的技巧：针对负数，用长整型来表示，先转换成大正数，以无符号整形表示，该无符号整形与原始有符号负数的 2进制表示是一样的
            numLong = (long) (num + Math.pow(2, 32));
            //负数的二进制是用补码来表示，为了能与正数统一处理，故需要将 负数转换为二进制形式一样的正数
        }
        char[] target = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuilder res = new StringBuilder();
        while (numLong != 0) {
            long i = numLong % 16;  //针对负数，如果不进行上面的转换，这里直接为-1
            res.append(target[(int) i]);
            numLong /= 16;
        }
        return res.reverse().toString();
    }

    //-----------------------------------------------------------------------------
    // 无论何种处理方式，其关键在于对于负数的处理，因为循环终止的条件为 while (num != 0)，如果为负数，则循环一次就退出
    // 因为负数在计算机中是通过补码的形式来表示
    // 1、将负数转换为与其二进制形式相同的无符号长整型
    //       相当于用一个大整数来替代负数，因为二者在计算机中的二进制表示形式一致，即负数的"补码"于无符号长整型的"原码"一致
    // 2、直接将负数的除法运算基于位运算的"无符号右移动"（高位补 0）来实现，取余也基于位运算的"与"运算来实现
    //------------------------------------------------------------------------------

    public String toHex01(int num) {
        if (num == 0)
            return "0";
        char[] target = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuilder res = new StringBuilder();
        while (num != 0) {
            //计算机内无论十六进制还是十进制都是给人们看的，所以我们不用纠结num是否是十进制，他在计算机中储存的就是二进制！所以可以直接进行移位操作！
            int i = num & 0xf; //除以16，取余
            //注意，此处不能写为 int i = num % 16，因为 -1 % 16 = -1；
            res.append(target[i]);
            num >>>= 4;  //除以16，取整，无符号右移（非常重要，因为在计算中负数是用补码来表示），如果写为>>则异常
            //------------------------------------------
            //无符号右侧移：无论正负，高位补充 0
            //有符号右侧移：正数，高位补充 0，负数，高位补 1
            //------------------------------------------
        }
        return res.reverse().toString();
    }

    public String toHex02(int num) {
        if (num == 0)
            return "0";
        StringBuilder res = new StringBuilder();
        while (num != 0) {
            int i = num & 0xf;  //除以16，取余数
            //--------------------------------------------------------------------------------
            // 1、基于ASCII码，将余数转换为对应的16进制表示
            // 2、需要转换为 char类型
            //      因为十六进制的有部分需要用char来表示，故需要将每一位均转换为char类型
            //--------------------------------------------------------------------------------

            //整数范围 [0,9]的数字，十六进制表示与十进制表示相同，即在'0'的基准上做操作即可
            char append = (char) (i + '0');
            //整数范围 [10,15]的数字，十六进制，即在'a'的基准上做操作即可
            if (i >= 10)
                append = (char) (i - 10 + 'a');   //需要减去10，回退到基准，即相较于'a'后的第几位
            res.append(append);
            num >>>= 4;  //除以16
        }
        return res.reverse().toString();
    }

    public String toHex03(int num) {
        if (num == 0)
            return "0";
        StringBuilder res = new StringBuilder();
        while (num != 0) {
            int i = num & 0xf;  //除以16，取余数
            if (i < 10)         //小于10的直接添加，因为十进制和十六进制对应的二进制表示相同
                res.append(i);
            if (i >= 10)        //大于等于10的需要基于ASCII码转换为char后添加，因为此时十进制和十六进制对应的二进制表示不相同
                res.append((char) (i - 10 + 'a'));   //需要减去10，回退到基准，即相较于'a'后的第几位
            num >>>= 4;  //除以16
        }
        return res.reverse().toString();
    }


//------------------------------------------------------------------------------------------------------
// 难度：中等
//------------------------------------------------------------------------------------------------------


    /**
     * 318. 最大单词长度乘积
     * 基于位掩码，就把出现的字符转压缩成了一个整数，优点是可以通过与运算直接判断两个串是否有相同字符
     * https://www.cnblogs.com/hihtml5/p/6483783.html
     * 思路：重要
     */
    public int maxProduct(String[] words) {
        int len = words.length;
        int[] maskBit = new int[len];
        int maxLen = 0;
        for (int i = 0; i < len; i++) {
            String word = words[i];
            int wordLength = word.length();
            for (int j = 0; j < wordLength; j++) {
                //--------------------------------------------------------------------------
                //或运算：只要一个为 1则为1，否则为 0
                //每个 word表示的数值：
                //   是在对 word中字符去重后的结果，仅用于基于位运算比较两个word中是否存在相同的字符
                //--------------------------------------------------------------------------
                maskBit[i] |= (1 << word.charAt(j) - 'a');
            }
        }
        //嵌套循环，寻找无重复字符的两个单词，并求其长度乘积，更新maxLen
        for (int i = 0; i < len - 1; i++) {
            for (int j = i + 1; j < len; j++) {
                if ((maskBit[i] & maskBit[j]) == 0)  //两个单词中无重复的字符
                    maxLen = Math.max(maxLen, words[i].length() * words[j].length());
            }
        }
        return maxLen;
    }


    /**
     * 187. 重复的DNA序列
     */
    public List<String> findRepeatedDnaSequences(String s) {
        int windowLength = 10;
        ArrayList<String> seqRepeat = new ArrayList<>();
        if (s.length() < windowLength) {
            return seqRepeat;
        }
        HashMap<Character, Integer> chatTonum = new HashMap<>();
        chatTonum.put('A', 0);//00
        chatTonum.put('C', 1);//01
        chatTonum.put('G', 2);//10
        chatTonum.put('T', 3);//11
        int windowNum = 0;
        for (int i = 0; i < windowLength - 1; ++i) {
            windowNum = (windowNum << 2) | chatTonum.get(s.charAt(i));
        }
        HashMap<Integer, Integer> seqAndnum = new HashMap<>();
        for (int left = 0; left <= s.length() - windowLength; ++left) {
            windowNum = ((windowNum << 2) | chatTonum.get(s.charAt(left + windowLength - 1))) & ((1 << windowLength * 2) - 1);//位运算的优先级
            seqAndnum.put(windowNum, seqAndnum.getOrDefault(windowNum, 0) + 1);
            if (seqAndnum.get(windowNum) == 2)
                seqRepeat.add(s.substring(left, left + windowLength));
        }
        return seqRepeat;
    }

    public List<String> findRepeatedDnaSequences04(String str) {
        int windowLen = 10;
        List<String> ans = new ArrayList<>();
        if (str.length() < windowLen) return ans;
        HashMap<Character, Integer> map = new HashMap<>();
        map.put('A', 0);  //00
        map.put('C', 1);  //01
        map.put('G', 2);  //10
        map.put('T', 3);  //11
        int windowNum = 0;
        for (int i = 0; i < windowLen - 1; i++) {
            windowNum = windowNum << 2 | map.get(str.charAt(i));
        }
        int right = windowLen - 1;
        HashMap<Integer, Integer> seqAndNum = new HashMap<>();
        while (right < str.length()) {
            windowNum = ((windowNum << 2 | map.get(str.charAt(right))) & ((1 << windowLen * 2) - 1));
            seqAndNum.put(windowNum, seqAndNum.getOrDefault(windowNum, 0) + 1);
            if (seqAndNum.get(windowNum) == 2) {
                ans.add(str.substring(right - windowLen + 1, right + 1));
            }
            right++;
        }
        return ans;
    }

    public List<String> findRepeatedDnaSequences01(String s) {
        int windowLength = 10;
        ArrayList<String> seqRepeat = new ArrayList<>();
        if (s.length() < windowLength) {
            return seqRepeat;
        }
        HashMap<Character, Integer> chatTonum = new HashMap<>();
        chatTonum.put('A', 0);//00
        chatTonum.put('C', 1);//01
        chatTonum.put('G', 2);//10
        chatTonum.put('T', 3);//11
        int windowNum = 0;
        HashMap<Integer, Integer> seqAndnum = new HashMap<>();
        int left = 0;
        int right = 0;
        while (right < s.length()) {
            windowNum = (windowNum << 2) | chatTonum.get(s.charAt(right));
            while (right - left + 1 > windowLength) {
                windowNum = windowNum & ((1 << windowLength * 2) - 1);
                left++;
            }
            if (right - left + 1 == windowLength) {
                seqAndnum.put(windowNum, seqAndnum.getOrDefault(windowNum, 0) + 1);
                if (seqAndnum.get(windowNum) == 2)
                    seqRepeat.add(s.substring(left, left + windowLength));
            }
            right++;
        }
        return seqRepeat;
    }

    public List<String> findRepeatedDnaSequences02(String s) {
        int L = 10;
        List<String> ans = new ArrayList<String>();
        HashMap<String, Integer> seqAndNum = new HashMap<>();
        for (int i = 0; i <= s.length() - L; i++) {
            String temp = s.substring(i, i + L);   //substring不存在数组越界的问题
            seqAndNum.put(temp, seqAndNum.getOrDefault(temp, 0) + 1);
            if (seqAndNum.get(temp) == 2)
                ans.add(temp);
        }
        return ans;
    }

    public List<String> findRepeatedDnaSequences03(String s) {
        int windowLength = 10;
        ArrayList<String> seqRepeat = new ArrayList<>();
        if (s.length() <= 10)
            return seqRepeat;
        HashMap<Character, Integer> charTonum = new HashMap<>();
        charTonum.put('A', 1);//001
        charTonum.put('C', 2);//020
        charTonum.put('G', 3);//011
        charTonum.put('T', 4);//100
        Integer oneWindow = 0;
        HashMap<Integer, Integer> seqAndTimes = new HashMap<>();
//        for (int i = 0; i < windowLength - 1; i++) {
//            oneWindow = (oneWindow << 3) | charTonum.get(s.charAt(i));
//        }
//        for (int i = 0; i <= s.length() - windowLength; i++) {
//            oneWindow = ((oneWindow << 3) | charTonum.get(s.charAt(i + windowLength - 1))) & ((1 << windowLength * 3) - 1);
//            seqAndTimes.put(oneWindow, seqAndTimes.getOrDefault(oneWindow, 0) + 1);
//            if (seqAndTimes.get(oneWindow) == 2)
//                seqRepeat.add(s.substring(i, i + windowLength));  //substring不会有数组越界
//        }
        for (int i = 0; i < windowLength; i++) {
            oneWindow = (oneWindow << 3) | charTonum.get(s.charAt(i));
        }
        seqAndTimes.put(oneWindow, 1);
        for (int i = windowLength; i < s.length(); i++) {
            oneWindow = ((oneWindow << 3) | charTonum.get(s.charAt(i))) & ((1 << windowLength * 3) - 1);
            seqAndTimes.put(oneWindow, seqAndTimes.getOrDefault(oneWindow, 0) + 1);
            if (seqAndTimes.get(oneWindow) == 2)
                if (i == s.length() - 1)
                    seqRepeat.add(s.substring(i - windowLength + 1));
                else
                    seqRepeat.add(s.substring(i - windowLength + 1, i + 1));
        }
        return seqRepeat;
    }


    /**
     * 36. 有效的数独
     * <p>
     * 二维数组和一维数组+位运算区别在于：
     * 二维数组：
     * 索引##一维：坐标
     * 索引##二维：目标值
     * 索引##值：目标值出现的次数
     * <p>
     * 一维数组+位运算：
     * 索引##一维：坐标
     * 索引##值：int型整数，其对应的二进制的每一位0-9位（低位）：标识目标值[0-9],0-9位（低位）的值（0/1）表示出现过的次数（超过1不表示）
     */
    public boolean isValidSudoku(char[][] board) {
        int[] row = new int[10], col = new int[10], area = new int[10];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                char c = board[i][j];
                if (c == '.') continue;
                int charTonum = c - '0';
                int idx = i / 3 * 3 + j / 3;
                if (((row[i] >> charTonum) & 1) == 1 || ((col[j] >> charTonum) & 1) == 1 || ((area[idx] >> charTonum) & 1) == 1)
                    return false; //一定要有 &1,这个相当于是和..00000001做的与运算，只看这一位，否则会有其他位置的干扰
                row[i] |= (1 << charTonum);
                col[j] |= (1 << charTonum);
                area[idx] |= (1 << charTonum);
            }
        }
        return true;
    }

    public boolean isValidSudoku00(char[][] board) {
        int[][] rows = new int[9][9];
        int[][] columns = new int[9][9];
        int[][][] areas = new int[3][3][9];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != '.') {
                    int charTonum = board[i][j] - '0' - 1;
                    rows[i][charTonum]++;
                    columns[j][charTonum]++;
                    areas[i / 3][j / 3][charTonum]++;
                    if (rows[i][charTonum] > 1 || columns[j][charTonum] > 1 || areas[i / 3][j / 3][charTonum] > 1)
                        return false;
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
            xor ^= s.charAt(i);
        }
        for (int i = 0; i < t.length(); i++) {
            xor ^= t.charAt(i);
        }
        return (char) xor;
    }

    public char findTheDifference01(String s, String t) {
        int sum = 0;
        for (int i = 0; i < t.length(); i++) {
            sum += t.charAt(i);
        }
        for (int i = 0; i < s.length(); i++) {
            sum -= s.charAt(i);
        }
        return (char) sum;
    }

    public char findTheDifference02(String s, String t) {
        int[] ans = new int[26];
        for (int i = 0; i < s.length(); i++) {
            ans[s.charAt(i) - 'a']++;
        }
        for (int i = 0; i < t.length(); i++) {
            ans[t.charAt(i) - 'a']--;
        }
        for (int i = 0; i < 26; i++) {
            if (ans[i] != 0)
                return (char) (i + 'a');
        }
        return 'a';
    }


    /**
     * 6194. 最小 XOR
     */
    public int minimizeXor(int num1, int num2) {  //周赛，自己写的，通过
        int target = num1;
        int res1 = 0;
        int res2 = 0;
        for (int i = 0; i < 32; i++) {
            res1 += (num1 >> i) & 1;
            res2 += (num2 >> i) & 1;
        }
        if (res2 > res1) {
            int diff = res2 - res1;
            for (int i = 0; i < 32; i++) {
                if (((target >> i) & 1) == 0) {
                    target |= 1 << i;
                    diff--;
                    if (diff == 0) return target;
                }
            }
        }
        if (res1 > res2) {   //将res1低位的1消除
            int diff = res1 - res2;
            int targetIndex = 0;
            for (int i = 0; i < 32; i++) {
                if (((target >> i) & 1) == 1) {
                    diff--;
                    if (diff == 0) {
                        targetIndex = i;
                        break;
                    }
                }
            }
            return num1 & (~((1 << (targetIndex + 1)) - 1));
        }
        return num1;
    }

    /**
     * 421. 数组中两个数的最大异或值
     * https://leetcode-cn.com/problems/maximum-xor-of-two-numbers-in-an-array/solution/li-yong-yi-huo-yun-suan-de-xing-zhi-tan-xin-suan-f/
     */
    public int findMaximumXOR(int[] nums) {
        int res = 0;
        int mask = 0;
        // 先确定高位，再确定低位（有点贪心算法的意思），才能保证这道题的最大性质
        // 一位接着一位去确定这个数位的大小
        // 利用性质： a ^ b = c ，则 a ^ c = b，且 b ^ c = a
        for (int i = 30; i >= 0; i--) {
            //1、获取数组中每个元素的前缀（i位前）
            mask |= 1 << i;    //计算位置掩码，迭代获取
            HashSet<Integer> prefix = new HashSet<>();  //存储此轮各个数字的前缀
            for (int num : nums)
                prefix.add(mask & num);  //mask高位为 1，低位为 0，因此"与"运算后，保留num的前缀，后面的置为 0
            //2、假设理想的前缀
            int temp = res | (1 << i);  //假设当前位为 1，基于之前的迭代结果
            //3、验证前缀是否合理
            for (int pre : prefix) {
                //基于"异或运算"的性质 temp ^ pre = one 则有 one ^ pre = temp
                if (prefix.contains(temp ^ pre)) {  //存在对应的数，使得 假设的 前缀合理
                    res = temp;
                    break;
                }
            }
        }
        return res;
    }


    /**
     * 2433. 找出前缀异或的原始数组
     */
    public int[] findArray(int[] pref) {  //周赛 314 题目
        //基于性质 a ^ b = c ，则 a ^ c = b，且 b ^ c = a
        int[] ans = new int[pref.length];
        ans[0] = pref[0];
        for (int i = 1; i < pref.length; i++) {
            ans[i] = pref[i] ^ pref[i - 1];
        }
        return ans;
    }



    /**
     * 982. 按位与为零的三元组
     */
    public int countTriplets(int[] nums) {
        int ans = 0;
        int[] buckets = new int[1 << 16];
        for (int x : nums) {
            for (int y : nums) {
                buckets[x & y]++;
            }
        }
        for (int z : nums) {
            for (int prev = 0; prev < (1 << 16); prev++) {
                if ((z & prev) == 0) ans += buckets[prev];
            }
        }
        return ans;
    }




    /*
     * 397. 整数替换
     *
     * 如果本位是 1，下一位也是 1 ，处理过程类似多米诺骨牌，此时对 +1 和 -1 两种情况分析：
     *          1、+ 1
     *             针对本位，则需要进行两步操作才能削掉，分别为 +1 和 >>
     *             针对本位前的连续为 1 的高位区间(区间长度: L - 1) ，由于本位 +1，则由于进位的原因，使得全部置为 0 ，则这些仅需要 一步操作即可 >>
     *             截至连续区间，共需要：L + 1
     *          2、- 1
     *             针对本位，则需要进行两步操作才能削掉 -1 和 >>
     *             针对本位前的连续为 1 的高位区间 L ，由于本位 -1，不会对连续区间的移动有任何影响和优化
     *             截至连续区间，共需要：2L
     *          因此：针对连续 1 的区间，应该选择 +1
     *
     *      如果本位是 1，高一位是 0 ，此时对 +1 和 -1 两种情况分析：
     *           1、+ 1
     *             针对本位，需要进行两步操作才能消掉，分别为 +1 和 >>
     *             针对高一位，由于上一位+1导致进位，所以此位 也需要进行 相同的两步操作
     *             因此，需要进行4步操作才可以
     *                   相当于本来的0可以由一步操作来削掉，但却由于进一位导致需要两步才行
     *           1、- 1
     *             针对本位，需要进行两步操作才能消掉，分别为 -1 和 >>
     *             针对高一位，上一位-1对此无影响，所以此位 仅仅需要一步操作 >>
     *             因此，需要进行3步操作才可以
     *           因此，交替的情况，应该选择对 -1
     */

    /**
     * 二进制观察处理法
     * 实现思路:
     * 最快的移动就是遇到2的次幂(例如数字16  10000 -> 01000 -> 00100 -> 00010 -> 00001)
     * 将二进制一直左移 最右为0时可以直接移动(例如数字6  000110 -> 000011)
     * 最右位为1时需把1变成0, 再移动(例如数字9  001001 -> 001000)
     * 故最优解就是如何在迭代中减少出现末尾1(就是什么时候+1, 什么时候-1 来实现过程中最少出现01或11结尾)
     * 得出以下结论:
     * 若n的二进制为 xxxx10, 则下一次处理 n = n/2 次数+1
     * 若n的二进制为 xxxx01, 则下一次处理 n = n/2 次数+2(即需要先-1再除以2, 故这里是加2) n > 1
     * 若n的二进制为 xxxx11, 则下一次处理 n = n/2 +1 次数+2(即需要先+1再除以2, 故这里是加2) n > 3
     * 特殊情况: 数字3  000011, 000011 -> 000010 -> 000001(两次即可)
     * 边界条件: 000001 -> 答案为0
     */
    public int integerReplacement(int n) {
        int res = 0;
        while (n != 1) {
            //判断奇偶性
            if ((n & 1) == 0)  //偶数
                n >>>= 1;
                //-----------------------------
                //细节：虽然 n 是正数，但这里要用 无符号右移
                //     因为 n 为 2^31 -1 时，n + 1会导致int溢出，将 n 变为一个负数，如果用的是 >> ,则高位一直补 1 ，进入死循环
                //或者，先将 n 变为 long 型在做处理
                //-----------------------------
            else {             //奇数
                if (n != 3 && ((n >> 1) & 1) == 1)  //奇数，则当前最低位为 1
                {
                    //综上：无论是至少两个连续 1的区间，还是01的区间，都应该整体考虑，尽可能将区间内的数字置为0，以便利用 偶数可直接消除的优势
                    n++;
                } else
                    n--;
            }
            res++;
        }
        return res;
    }

    public int integerReplacement01(int n) {
        if (n == 1)
            return 0;
        if ((n & 1) == 0)  //偶数的情况：
            return 1 + integerReplacement(n / 2);  //1代表一步 n/2 即可
        //奇数的情况：2代表，先加 1 或 减 1，使其变为偶数，然后再有 "n"/2
        return 2 + Math.min(integerReplacement(n / 2), integerReplacement(n / 2 + 1));
        //----------------------------------------
        //处理过程的整体结构类似二叉树的"样子"
        //    虽然代码的写法像是从上向下（即从根节点到各个叶子节点），但整体的处理过程是可以理解为从下向上
        //       每个节点均取两个子节点中最小"替换次数"，自叶子节点向上汇总
        //----------------------------------------
    }

    Map<Long, Integer> map = new HashMap<>();

    public int integerReplacement02(int n) {
        return dfs(n * 1L);
    }

    int dfs(long n) {
        if (n == 1) return 0;
        if (map.containsKey(n)) return map.get(n);
        int ans = n % 2 == 0 ? dfs(n / 2) : Math.min(dfs(n + 1), dfs(n - 1));
        map.put(n, ++ans);
        return ans;
    }


    /**
     * 6431. 相邻值的按位异或
     */
    public boolean doesValidArrayExist(int[] derived) {
        int n = derived.length;
        int[] original0 = new int[n];
        int[] original1 = new int[n];
        return ValidHelper(derived, original0, 0) || ValidHelper(derived, original1, 1);
    }

    private boolean ValidHelper(int[] derived, int[] original, int start) {
        original[0] = start;
        int n = original.length;
        for (int i = 0; i < n - 1; i++) {
            original[i + 1] = (original[i] ^ derived[i]);
        }
        return (original[n - 1] ^ derived[n - 1]) == original[0];
    }






}
