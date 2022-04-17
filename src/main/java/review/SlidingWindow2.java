package review;

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
