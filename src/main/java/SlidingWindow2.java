import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SlidingWindow2 {

    public int lengthOfLongestSubstring(String s) {
        int len = s.length();
        HashMap<Character, Integer> hTable = new HashMap<>();
        int left = 0;
        int right = 0;
        int maxWindow = 0;
        while (right < len) {
            if (hTable.containsKey(s.charAt(right)))
                left = Math.max(left, hTable.get(s.charAt(right)) + 1);
            hTable.put(s.charAt(right), right);
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }


    public List<String> findRepeatedDnaSequences(String s) {
        int windowLength = 10;
        ArrayList<String> hList = new ArrayList<>();
        HashMap<String, Integer> hTable = new HashMap<>();
        if (s.length() < windowLength) return hList;
        for (int i = 0; i <= s.length() - windowLength; i++) {
            String temp = s.substring(i, i + windowLength);
            hTable.put(temp, hTable.getOrDefault(temp, 0) + 1);
            if (hTable.get(temp) == 2)
                hList.add(temp);
        }
        return hList;
    }

    public List<String> findRepeatedDnaSequences01(String s) {
        int windowLength = 10;
        ArrayList<String> hList = new ArrayList<>();
        ArrayList<String> hTable = new ArrayList<>();
        if (s.length() < windowLength) return hList;
        for (int i = 0; i <= s.length() - windowLength; i++) {
            String temp = s.substring(i, i + windowLength);
            if (hTable.contains(temp) && !hList.contains(temp))
                hList.add(temp);
            else hTable.add(temp);
        }
        return hList;
    }

    public List<String> findRepeatedDnaSequences02(String s) {
        int windowLength = 10;
        ArrayList<String> result = new ArrayList<>();
        if (s.length() < 10) return result;
        HashMap<Character, Integer> charToNum = new HashMap<>();
        charToNum.put('A', 0);//00
        charToNum.put('C', 1);//01
        charToNum.put('G', 2);//10
        charToNum.put('T', 3);//11
        int left = 0;
        int right = 0;
        int windowsum = 0;
        HashMap<Integer, Integer> hTable = new HashMap<>();
        while (right < s.length()) {
            windowsum = (windowsum << 2) | charToNum.get(s.charAt(right));   //right向前探索未知区域
            while (right - left + 1 > windowLength) {
                //剪掉左侧left位置处的字符
                windowsum = windowsum & ((1 << windowLength * 2) - 1);
                left++;
            }
            if (right - left + 1 == windowLength) {
                hTable.put(windowsum, hTable.getOrDefault(windowsum, 0) + 1);
                if (hTable.get(windowsum) == 2)
                    result.add(s.substring(left, left + windowLength));
            }
            right++;
        }
        return result;
    }


    public static boolean checkInclusion(String s1, String s2) {
        int windowLength = s1.length();
        int[] target = new int[26];
        int[] window = new int[26];
        int left = 0;
        int right = 0;
        for (int i = 0; i < windowLength; i++)
            target[s1.charAt(i) - 'a']++;
        if (s1.length() > s2.length()) return false;
        while (right < s2.length()) {
            window[s2.charAt(right) - 'a']++;
            while (right - left + 1 > windowLength) {
                window[s2.charAt(left) - 'a']--;
                left++;
            }
            if (right - left + 1 == windowLength) {
                if (Arrays.equals(target, window))
                    return true;
            }
            right++;
        }
        return false;
    }


    public List<Integer> findAnagrams(String s, String p) {
        ArrayList<Integer> targetList = new ArrayList<>();
        if (p.length() > s.length())
            return targetList;
        int windowLen = p.length();
        int[] target = new int[26];
        int[] window = new int[26];
        for (int i = 0; i < p.length(); i++)
            target[p.charAt(i) - 'a']++;
        int left = 0;
        int right = 0;
        while (right < s.length()) {
            window[s.charAt(right) - 'a']++;
            while (right - left + 1 > windowLen) {
                window[s.charAt(left) - 'a']--;
                left++;
            }
            if (right - left + 1 == windowLen) {
                if (Arrays.equals(target, window))
                    targetList.add(left);
            }
            right++;
        }
        return targetList;
    }

}
