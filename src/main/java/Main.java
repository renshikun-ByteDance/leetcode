

import javax.crypto.spec.PSource;
import java.lang.reflect.Array;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        leetcode leetcode = new leetcode();
//        SlidingWindow slidingWindow = new SlidingWindow();
//
//
//        int[][] grid = new int[][]{{1, 3, 1}, {1, 5, 1}, {4, 2, 1}};
//        int[][] grid1 = new int[][]{{1, 2, 3}, {4, 5, 6}};
//        int min = leetcode.minPathSum(grid1);
//        System.out.println(min);
//
//        int sumPointPaths = leetcode.uniquePaths(7, 3);
//        System.out.println(sumPointPaths);
//
//        int[][] obstacleGrid = new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}};
//        int sumUniquePointPaths = leetcode.uniquePathsWithObstacles(obstacleGrid);
//        System.out.println(sumUniquePointPaths);
//
//        int[] nums = {3, 2, 1, 0, 4};
//
//        StringBuilder builder = new StringBuilder();
//        System.out.println(builder + "空");
//        String aaaa = "clear:烟酒店|18-超市类$|便利商店||:4";
//        String[] split = aaaa.split("[:|$]", 2);
//        System.out.println(Arrays.toString(split));
//
//
//        String[] strings = {"a", "ab", "abc"};
//        Arrays.stream(strings)
//                .forEach(System.out::println);
//
//        StringBuilder allTypeWeith = new StringBuilder();
//        allTypeWeith.append("~出行类:0.86")
//                .append("~餐饮类:0.911")
//                .append("~其他类:0.901")
//                .append("~娱乐类:0.112");
//        String[] typeWeithList = allTypeWeith.toString().split("~");
//        System.out.println(allTypeWeith.toString());
//        HashMap<String, Float> typeAndWeight = new HashMap<>();
//        for (String str : typeWeithList) {
//            if (str.length() > 0) {
//                String[] split1 = str.split(":");
//                typeAndWeight.put(split1[0], Float.parseFloat(split1[1]));
//            }
//        }
//        System.out.println("原始HashMap的排序:");
//        System.out.println(typeAndWeight);
//        String collect = leetcode.mapSorted(typeAndWeight);
//
//
//        System.out.println(collect);
//
//        List<String> strList = Arrays.asList("a", "ba", "bb", "abc", "cbb", "bba", "cab");
//        Map<Integer, String> strMap = new HashMap<Integer, String>();
//
//        strMap = strList.stream()
//                .collect(Collectors.toMap(str -> strList.indexOf(str), str -> str));
//        System.out.println("\n\n");
//
//        int[] ints = new int[]{9, 9, 9};
//        int[] ints1 = leetcode.plusOne(ints);
//        System.out.println(Arrays.toString(ints1));
//
//        int[] array = {0, 1, 0, 3, 12};
//        leetcode.moveZeroes(array);
//        System.out.println(Arrays.toString(array));
//
//        int[][] twoArray = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
//        List<Integer> integers = leetcode.spiralOrder(twoArray);
//        System.out.println(integers);
//
//        int[] fruits = {1, 2, 1};
//        int trees = leetcode.totalFruit(fruits);
//        System.out.println(trees);
//
//        HashMap<Integer, Integer> test = new HashMap<>();
//        Integer integer = test.get(1);
//        System.out.println(integer);
//
//        String s = "anagram";
//        String t = "anagarm";
//        boolean anagram = leetcode.isAnagram3(s, t);
//        System.out.println(anagram);
//
//        int[] ints2 = {3, 1, 1};
//        int target = 6;
//        int[] ints3 = leetcode.twoSum2(ints2, target);
//        System.out.println(Arrays.toString(ints3));
//
//        boolean b = leetcode.containsDuplicate3(ints2);
//        System.out.println(b);
//
//        s = "abba";
//        int maxLeng = leetcode.lengthOfLongestSubstring(s);
//        System.out.println(maxLeng);
//
//        System.out.println("\n\n");
//        String s1 = "ab";
//        String s2 = "eidbaooo";
//        boolean b1 = leetcode.checkInclusion(s1, s2);
//        System.out.println(b1);
//
//
//        String qqq = "abcd";
//        int iio = qqq.charAt(0) - 'a';
//        System.out.println(iio);
//
//        String sinChar = "bb";
//        int maxlengthOfLongestSubstring = slidingWindow.lengthOfLongestSubstring02(sinChar);
//        System.out.println(maxlengthOfLongestSubstring);
//        int left = 0;
//        int right = 9;
//        int mid = left + ((right - left) >> 1);
//        System.out.println(mid);
//
//        System.out.println("\n\n");
        Dichotomy dichotomy = new Dichotomy();
//        int[] ints4 = {1, 3, 5, 6};
//        int targetttt = 7;
//        System.out.println(dichotomy.searchInsert(ints4, targetttt));
//        System.out.println(dichotomy.searchInsert01(ints4, targetttt));
//        System.out.println(dichotomy.searchInsert02(ints4, targetttt));
//
//        System.out.println("\n\n");
//        System.out.println(dichotomy.mySqrt(2147395599));
//        System.out.println(dichotomy.mySqrt01(2147395599));

        System.out.println("\n\n");
//        int[] int1111 = {1, 0, 1, 1, 1};
////        int[] int11112 = {1};
//        int target1111 = 0;
//        boolean b = dichotomy.searchII(int1111, target1111);
//        System.out.println(b);
//        System.out.println("\n\n");
//
//        char aaa = '9';
//        int imb = aaa - '0';
//        System.out.println(imb);
//
////        System.out.println("计算：(n &(1<<3)>>3) = " + (n &(1<<3)>>3));
//        System.out.println("计算：(n &(1<<3)>>3) = " + (n &(1<<3)>>3));

        SlidingWindow slidingWindow = new SlidingWindow();

        int[] nums = {5};
        double maxAverage = slidingWindow.findMaxAverage(nums, 1);
        double maxAverage01 = slidingWindow.findMaxAverage01(nums, 1);
        double maxAverage02 = slidingWindow.findMaxAverage02(nums, 1);
        System.out.println(maxAverage);
        System.out.println(maxAverage01);
        System.out.println(maxAverage02);

        String aaa = "AAAAAAAAAAAAA";
        List<String> repeatedDnaSequences = slidingWindow.findRepeatedDnaSequences(aaa);
        System.out.println(repeatedDnaSequences);


        System.out.println("位运算示例:");
//        System.out.println(((2>>1)&1)==1);
        System.out.println((1 << 20) - 1);
        System.out.println(Integer.toBinaryString((1 << 20)));
        System.out.println(Integer.toBinaryString((1 << 20) - 1));
//        System.out.println(((5 >> 1)));
//        System.out.println(((5 >> 5) & 1));


        for (int i = 0; i < 3; ++i) {
            System.out.println("开始:" + i);
            System.out.println("处理:" + i);
            System.out.println("结束:" + i);
        }

//        Integer.toBinaryString
//        x & ((1 << 20) - 1)

//        System.out.println(((5 >> 1)));
//        System.out.println(((5 >> 5) & 1));
//        BitOperation bitOperation = new BitOperation();
//        String ssss = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT";
//        System.out.println(bitOperation.findRepeatedDnaSequences00(ssss));
//        System.out.println(bitOperation.findRepeatedDnaSequences(ssss));

        String s = "cbaebabacd", p = "abc";
        List<Integer> anagrams = slidingWindow.findAnagrams(s, p);
        System.out.println(anagrams);

        System.out.println("\n\n");


//        int[] customers = {1,0,1,2,1,1,7,5};
//        int[] grumpy = {0,1,0,1,0,1,0,1};
//        int X = 3;

        int[] numssss = {9930, 9923, 9983, 9997, 9934, 9952, 9945, 9914, 9985, 9982, 9970, 9932, 9985, 9902, 9975, 9990, 9922, 9990, 9994, 9937, 9996, 9964, 9943, 9963, 9911, 9925, 9935, 9945, 9933, 9916, 9930, 9938, 10000, 9916, 9911, 9959, 9957, 9907, 9913, 9916, 9993, 9930, 9975, 9924, 9988, 9923, 9910, 9925, 9977, 9981, 9927, 9930, 9927, 9925, 9923, 9904, 9928, 9928, 9986, 9903, 9985, 9954, 9938, 9911, 9952, 9974, 9926, 9920, 9972, 9983, 9973, 9917, 9995, 9973, 9977, 9947, 9936, 9975, 9954, 9932, 9964, 9972, 9935, 9946, 9966};
        int k = 3056;
        int maxSatisfied = slidingWindow.maxFrequency(numssss, k);
        int maxSatisfied01 = slidingWindow.maxFrequency01(numssss, k);
        System.out.println(maxSatisfied);
        System.out.println(maxSatisfied01);

        HashSet<Character> singleChars = new HashSet<>();
        System.out.println(singleChars.add('a'));
        System.out.println(singleChars.add('a'));


        System.out.println("\n\n");
//        int target00 = 3;
//        int[][] matrix = new int[][]{{1, 3, 5, 7}, {10, 11, 16, 20}, {23, 30, 34, 60}};
        int target00 = 0;
        int[][] matrix = new int[][]{{1}};
        Binsearch doublePointerTest = new Binsearch();

        //        int[] tests = new int[]{4, 5, 1, 2, 3};
//        int[] tests = new int[]{5, 1, 2, 3, 4};
        int[] tests = new int[]{6, 7, 8, 1, 2, 3, 4, 5};
        System.out.println(doublePointerTest.findMin(tests));
        System.out.println(doublePointerTest.findMin004(tests));

        int[] tes = new int[]{-1, 0, 3, 5, 9, 12};
        int target = 2;
        System.out.println(doublePointerTest.search4(tes, target));

        HashMap<String, Integer> htable = new HashMap<>();
        htable.put("a", 1);
        htable.put("c", 3);
        htable.put("b", 2);

        ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>(htable.entrySet());
//        entries.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        entries.sort(Comparator.comparing(Map.Entry::getValue));

        System.out.println(entries);

        System.out.println("\n\n");
        Binsearch binsearch = new Binsearch();
        int[] ints = {24, 3, 82, 22, 35, 84, 19};
//        int[] ints = {2, 2, 3, 4};
//        int[] ints = {4, 2, 3, 4};
//        int[] ints = {7, 0, 0, 0};
//        int[] ints = {1, 1, 3, 4};
//        int[] ints = {0, 1, 1, 1};
        System.out.println(binsearch.triangleNumber(ints));
        System.out.println(binsearch.triangleNumber01(ints));
        System.out.println(binsearch.triangleNumber10(ints));
        System.out.println(binsearch.triangleNumber11(ints));


        System.out.println("\n\n");
//        [3], [12], [25], [15], [24], [8]
        int[] persons = new int[]{0, 1, 1, 0, 0, 1, 0};
        int[] times = new int[]{0, 5, 10, 15, 20, 25, 30};


//        //[45],[49],[59],[68],[42],[37],[99],[26],[78],[43]
//        int[] persons = new int[]{0, 0, 0, 0, 1};
//        int[] times = new int[]{0, 6, 39, 52, 75};
        TopVotedCandidate topVotedCandidate = new TopVotedCandidate(persons, times);
        System.out.println(topVotedCandidate.q(25));
        System.out.println(topVotedCandidate.q01(25));
        System.out.println(topVotedCandidate.q02(25));


        System.out.println("\n\n");
//        int[] weights = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
//        int days = 5;
//        int[] weights = new int[]{3, 2, 2, 4, 1, 4};
//        int days = 3;
        int[] weights = new int[]{1, 2, 3, 1, 1};
        int days = 4;
        System.out.println(binsearch.shipWithinDays(weights, days));
        System.out.println(binsearch.shipWithinDays01(weights, days));

        System.out.println("\n\n");

//        int[] chalk = new int[]{3, 4, 1, 2};
//        int ck = 25;

        int[] chalk = new int[]{5, 1, 5};
        int ck = 22;


//        int[] chalk = new int[]{2, 2, 3};
//        int ck = 1;
        System.out.println(binsearch.chalkReplacer(chalk, ck));
        System.out.println(binsearch.chalkReplacer01(chalk, ck));
//        System.out.println(binsearch.chalkReplacer02(chalk, ck));
        System.out.println(binsearch.chalkReplacer03(chalk, ck));

        System.out.println("\n\n");

        int[] nums1 = {1, 7, 5};
        int[] nums2 = {2, 3, 5};
        System.out.println(binsearch.minAbsoluteSumDiff(nums1, nums2));
        System.out.println(binsearch.minAbsoluteSumDiff6(nums1, nums2));

        System.out.println("\n\n");
//        int[] numslll = {1, 3, 5, 6};
        int[] numslll = {1, 6};
        int mm = 5;
        System.out.println(binsearch.binSearch123(numslll, mm));
        System.out.println(binsearch.binSearch12(numslll, mm));
        System.out.println(binsearch.binSearch321(numslll, mm));
        System.out.println(binsearch.binSearch32(numslll, mm));


//        System.out.println(binsearch.searchInsert(numslll, mm));
//        System.out.println(binsearch.searchInsert01(numslll, mm));

        SlidingWindow2 slidingWindow2 = new SlidingWindow2();
        String s1 = "ab";
        String s2 = "eidbaooo";
        System.out.println(SlidingWindow2.checkInclusion(s1, s2));


        int[] int00 = new int[10];
        Arrays.fill(int00, -1);
        System.out.println(Arrays.toString(int00));

        DoublePointer2 doublePointer2 = new DoublePointer2();


        System.out.println("\n\n");
//        int[] ages = {16, 16};
//        int[] ages = {16, 17, 18};
        int[] ages = {20, 30, 100, 110, 120};
        System.out.println(doublePointer2.numFriendRequests(ages));
        System.out.println(doublePointer2.numFriendRequests00(ages));
        System.out.println(doublePointer2.numFriendRequests01(ages));
        System.out.println(doublePointer2.numFriendRequests02(ages));
        System.out.println(doublePointer2.numFriendRequests10(ages));





    }
}
