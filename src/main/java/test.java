import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class test {


    public int lengthOfLongestSubstring(String s) {
        int left = 0;
        int right = 0;
        int len = s.length();
        int maxwindow = 0;
        HashMap<Character, Integer> hTable = new HashMap<>();
        while (right < len) {
            if (hTable.get(s.charAt(right)) != null) {
                left = Math.max(left, hTable.get(s.charAt(right)) + 1);
            }
            hTable.put(s.charAt(right), right);
            maxwindow = Math.max(maxwindow, right - left + 1);
            right++;
        }
        return maxwindow;
    }

    public int characterReplacement(String s, int k) {
        int left = 0;
        int right = 0;
        int[] charAndNums = new int[26];
        int historyWindowMaxCharNums = 0;
        while (right < s.length()) {
            charAndNums[s.charAt(right) - 'A']++;
            historyWindowMaxCharNums = Math.max(historyWindowMaxCharNums, charAndNums[s.charAt(right) - 'A']);
            if (right - left + 1 - historyWindowMaxCharNums > k) {   //k被消耗完
                charAndNums[s.charAt(left) - 'A']--;
                left++;
            }
            right++;
        }
        return right - 1 - left + 1;
    }


    public List<Integer> findAnagrams(String s, String p) {
        ArrayList<Integer> targetList = new ArrayList<>();
        int[] target = new int[26];
        int[] window = new int[26];
        if (s.length() < p.length())
            return targetList;
        for (int i = 0; i < p.length(); i++) {
            target[p.charAt(i) - 'a']++;
            window[s.charAt(i) - 'a']++;
        }
        if (Arrays.equals(window, target))
            targetList.add(0);
        for (int i = p.length(); i < s.length(); i++) {
            window[s.charAt(i) - 'a']++;
            window[s.charAt(i - p.length()) - 'a']--;
            if (Arrays.equals(window, target))
                targetList.add(i - p.length() + 1);
        }
        return targetList;
    }


    public int findLHS(int[] nums) {
        HashMap<Integer, Integer> hTable = new HashMap<>();
        int maxLength = 0;
        for (int num : nums)
            hTable.put(num, hTable.getOrDefault(num, 0) + 1);
        for (int num : hTable.keySet()) {
            if (hTable.containsKey(num + 1))
                maxLength = Math.max(maxLength, hTable.get(num) + hTable.get(num + 1));
        }
        return maxLength;
    }

    public int findLHS01(int[] nums) {
        Arrays.sort(nums);
        int left = 0;
        int right = 0;
        int maxLength = 0;
        while (right < nums.length) {
            while (left < right && nums[right] - nums[left] > 1)
                left++;
            if (nums[right] - nums[left] == 1)
                maxLength = Math.max(maxLength, right - left + 1);
            right++;
        }
        return maxLength;
    }

    public int longestOnes(int[] nums, int k) {
        int left = 0;
        int right = 0;
        int maxWindow = 0;
        while (right < nums.length) {
            if (nums[right] == 0)
                k--;
            while (k < 0) {
                if (nums[left] == 0)
                    k++;
                left++;
            }
            maxWindow = Math.max(maxWindow, right - left + 1);
            right++;
        }
        return maxWindow;
    }


    public int maxSatisfied(int[] customers, int[] grumpy, int minutes) {
        int sum = 0;
        int len = customers.length;
        int windowMaxSum = 0;
        int windowsum = 0;
        for (int i = 0; i < len; i++) {
            if (grumpy[i] == 0) {
                sum += customers[i];
                customers[i] = 0;
            }
        }
        for (int i = 0; i < minutes; i++) {
            windowsum += customers[i];
        }
        windowMaxSum = windowsum;
        for (int i = minutes; i < len; i++) {
            windowsum += customers[i];
            windowsum -= customers[i - minutes];
            windowMaxSum = Math.max(windowMaxSum, windowsum);
        }
        return sum + windowMaxSum;
    }


    public int maxSatisfied01(int[] customers, int[] grumpy, int minutes) {
        int sum = 0;
        int len = customers.length;
        int windowMaxSum = 0;
        int windowsum = 0;
        int right = 0, left = 0;
        for (int i = 0; i < len; i++) {
            if (grumpy[i] == 0) {
                sum += customers[i];
                customers[i] = 0;
            }
        }
        while (right < len) {
            windowsum += customers[right];
            while (right - left + 1 > minutes) {
                windowsum -= customers[left];
                left++;
            }
            windowMaxSum = Math.max(windowMaxSum, windowsum);
            right++;
        }
        return sum + windowMaxSum;
    }


    /**
     * [100000]
     * [000001]
     * 100000
     * @param nums
     * @param k
     * @return
     */
    public int maxFrequency(int[] nums, int k) {
        Arrays.sort(nums);
        int left = 0;
        int right = 1;
        int window = 1;
        long sum = 0;
        while (right < nums.length) {
            sum += (long) (right - left) * (nums[right] - nums[right - 1]);
            while (sum > k) {
                sum -= nums[right] - nums[left];
                left++;
            }
            window = Math.max(window, right - left + 1);
            right++;
        }
        return window;
    }


}
