import com.alibaba.fastjson.JSONObject;
import leetcode.algorithm.*;
import leetcode.algorithm.Byte;
import leetcode.homework.Working;
import leetcode.homework.Working2;
import leetcode.homework.prev.Reviewing;
import leetcode.important.DynamicProgrammingSpecial;
import leetcode.important.ThinkingMore;
import leetcode.important.Thinking;
import leetcode.homework.prev.comeBackHome;
import leetcode.important.Weekly;
import utils.review.DoublePointer2;

import java.util.*;


public class leetcodeMain {

    public static void main(String[] args) throws InterruptedException {
        greedy greedy = new greedy();
        HashTable hashTable = new HashTable();
        BackTrack backTrack = new BackTrack();
        Working2 working2 = new Working2();
        Working working = new Working();
        BinSearch binSearch = new BinSearch();
        Simulation simulation = new Simulation();
        prefixSum prefixSum = new prefixSum();
        Bucket bucket = new Bucket();
        queue queue = new queue();
        comeBackHome comeBackHome = new comeBackHome();
        Byte myByte = new Byte();
        DFS dfs = new DFS();
        Reviewing reviewing = new Reviewing();
        Thinking thinking = new Thinking();
        DAG dag = new DAG();
        Matrix matrixing = new Matrix();
        Weekly weekly = new Weekly();
        DynamicProgramming000 dynamicProgramming000 = new DynamicProgramming000();
        ArrayListNode arrayListNode = new ArrayListNode();
        DoublePointer2 doublePointer2 = new DoublePointer2();
        DoublePointer doublePointer = new DoublePointer();
        ThinkingMore thinkingMore = new ThinkingMore();
        leetcode.important.DynamicProgramming dynamicProgramming = new leetcode.important.DynamicProgramming();
        DynamicProgrammingSpecial dynamicProgrammingSpecial = new DynamicProgrammingSpecial();
        minDistance minDistance = new minDistance();

        //-------------------------------------
        // 案例测试
        //-------------------------------------

        JSONObject result = new JSONObject();
        result.put("before", new HashMap<>());
        result.put("before111", "");

        HashMap<String, String> after = new HashMap<>();
        after.put("name", "任世坤");
        after.put("address", "北京");
        result.put("after", after);

        System.out.println(result);

        System.out.println("-----------------------------");
        System.out.println(result.getJSONObject("before"));
        System.out.println(result.getJSONObject("before111"));
        System.out.println(result.getJSONObject("after"));


        System.out.println("-----------------------------");
        // 创建一个 HashMap
        HashMap<String, Integer> prices = new HashMap<>();

        // 往HashMap中添加映射项
        prices.put("Shoes", 200);
        prices.put("Bag", 300);
        prices.put("Pant", 150);
        System.out.println("HashMap: " + prices);

        // 计算 Shirt 的值
        int shirtPrice = prices.computeIfAbsent("Shirt", key -> 280);
        int test = prices.computeIfAbsent("Pant", key -> 280);
        System.out.println("Price of Shirt: " + shirtPrice);
        System.out.println("Price of test: " + test);

        // 输出更新后的HashMap
        System.out.println("Updated HashMap: " + prices);


        System.out.println("-------------------------------------------------");
//        String[] arr = {"flower", "flow", "flight"};
//        String[] arr = {"dog", "racecar", "car"};
//        String[] arr = {"racecar", "racecar", "racecar"};
//        String[] arr = {"racecar"};

        String[] arr = {"", ""};
        System.out.println(simulation.longestCommonPrefix(arr));
//        System.out.println(working.longestCommonPrefix(arr));




    }


}

















