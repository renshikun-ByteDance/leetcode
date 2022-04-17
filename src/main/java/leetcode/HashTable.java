package leetcode;

import java.util.Arrays;
import java.util.HashMap;

//数据结构：哈希表hashmap和hashset
//数组其实就是一个简单哈希表，而且这道题目中字符串只有小写字符，那么就可以定义一个数组，来记录字符串s里字符出现的次数。
public class HashTable {

    /**
     * 242. 有效的字母异位词
     * 利用数组和ASCII码或unicode码来记录字符及其出现次数
     *
     * @param s
     * @param t
     * @return
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
     *
     * @param nums
     * @param target
     * @return
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


}
