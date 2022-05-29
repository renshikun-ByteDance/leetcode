package leetcode;

import java.util.*;

//数据结构：哈希表hashmap和hashset
//数组其实就是一个简单哈希表，而且这道题目中字符串只有小写字符，那么就可以定义一个数组，来记录字符串s里字符出现的次数。
public class HashTable {

    /**
     * 242. 有效的字母异位词
     * 利用数组和ASCII码或unicode码来记录字符及其出现次数
     */
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        int[] sCharNums = new int[26];
        int[] tCharNums = new int[26];
        for (int i = 0; i < t.length(); i++) {
            sCharNums[s.charAt(i) - 'a']++;
            tCharNums[t.charAt(i) - 'a']++;
        }
        if (Arrays.equals(sCharNums, tCharNums)) {
            return true;
        }
        return false;
    }


    /**
     * 1. 两数之和
     * 利用hashmap来存储 数据和索引
     */
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> hTable = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (hTable.containsKey(target - nums[i])) {
                return new int[]{i, hTable.get(target - nums[i])};
            }
            hTable.put(nums[i], i);
        }
        return new int[]{0};
    }


    /**
     * 500. 键盘行
     */
    public String[] findWords(String[] words) {
        ArrayList<String> validWords = new ArrayList<>();
        String row1 = "qwertyuiop";
        String row2 = "asdfghjkl";
        String row3 = "zxcvbnm";
        HashMap<Character, Integer> hTable = new HashMap<>();
        //记录各个字母的行号
        for (int i = 0; i < row1.length(); i++)
            hTable.put(row1.charAt(i), 1);
        for (int i = 0; i < row2.length(); i++)
            hTable.put(row2.charAt(i), 2);
        for (int i = 0; i < row3.length(); i++)
            hTable.put(row3.charAt(i), 3);
        //检查各个单词
        for (String word : words) {
            String lower = word.toLowerCase();
            Integer row = hTable.get(lower.charAt(0));
            boolean isOk = true;
            for (int i = 1; i < word.length(); i++) {
                if (!hTable.get(lower.charAt(i)).equals(row)) {
                    isOk = false;
                    break;
                }
            }
            if (isOk)
                validWords.add(word);
        }
//        return validWords.toArray(new String[validWords.size()]);
        return validWords.stream().toArray(String[]::new);
    }

    /**
     * 884. 两句话中的不常见单词
     */
    public String[] uncommonFromSentences(String s1, String s2) {
        ArrayList<String> ans = new ArrayList<>();
        String[] words1 = s1.split(" ");
        String[] words2 = s2.split(" ");
        HashMap<String, Integer> hTable = new HashMap<>();
        for (String word : words1)
            hTable.put(word, hTable.getOrDefault(word, 0) + 1);
        for (String word : words2)
            hTable.put(word, hTable.getOrDefault(word, 0) + 1);
        for (String word : hTable.keySet()) {
            if (hTable.get(word) == 1) {
                ans.add(word);
            }
        }
        return ans.toArray(new String[ans.size()]);
    }


    /**
     * 692. 前 K个高频单词
     */
    public List<String> topKFrequent(String[] words, int k) {
        ArrayList<String> ans = new ArrayList<>();
        HashMap<String, Integer> Fres = new HashMap<>();
        for (String word : words) {
            Fres.put(word, Fres.getOrDefault(word, 0) + 1);
        }
        ArrayList<Map.Entry<String, Integer>> sorted = new ArrayList<>(Fres.entrySet());
        //自定义排序
        sorted.sort((o1, o2) -> {
            if (!o1.getValue().equals(o2.getValue()))
                return o2.getValue() - o1.getValue();
            else
                return o1.getKey().compareTo(o2.getKey());
        });
        for (int i = 0; i < k; i++) {
            ans.add(sorted.get(i).getKey());
        }
        return ans;
    }

    public List<String> topKFrequent01(String[] words, int k) {
        ArrayList<String> ans = new ArrayList<>();
        HashMap<String, Integer> Fres = new HashMap<>();
        for (String word : words) {
            Fres.put(word, Fres.getOrDefault(word, 0) + 1);
        }
        //----------------------------------------------------
        // 小根优先队列：就是优先队列顶端元素是最小元素的优先队列
        //     注意：此处优先队列的元素为 Map.Entry<String, Integer>
        //----------------------------------------------------
        PriorityQueue<Map.Entry<String, Integer>> sortedQueue = new PriorityQueue<Map.Entry<String, Integer>>((o1, o2) -> {
            if (!o1.getValue().equals(o2.getValue()))
                return o1.getValue() - o2.getValue(); //按照频次升序排序（排序规则，与上面的方法相反）
            else
                return o2.getKey().compareTo(o1.getKey());  //按照字典序倒序排序（排序规则，与上面的方法相反）
        });

        for (Map.Entry<String, Integer> map : Fres.entrySet()) {
            sortedQueue.add(map);
            if (sortedQueue.size() > k) {
                sortedQueue.poll();
            }
        }
        while (!sortedQueue.isEmpty()) {
            ans.add(sortedQueue.poll().getKey());  //poll: 拿出无放回，peek: 不拿出直接取值
        }
        Collections.reverse(ans);
        return ans;
    }


}
