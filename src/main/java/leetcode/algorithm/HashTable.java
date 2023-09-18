package leetcode.algorithm;

import org.omg.PortableInterceptor.INACTIVE;

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
                sortedQueue.poll();    //小根队，方便剔除堆顶元素
            }
        }
        while (!sortedQueue.isEmpty()) {
            ans.add(sortedQueue.poll().getKey());  //poll: 拿出无放回，peek: 不拿出直接取值
        }
        Collections.reverse(ans);
        return ans;
    }


    /**
     * 599. 两个列表的最小索引总和
     */
    public String[] findRestaurant(String[] list1, String[] list2) {
        ArrayList<String> ans = new ArrayList<>();
        HashMap<String, Integer> hTable = new HashMap<>();
        for (int i = 0; i < list1.length; i++) {
            hTable.put(list1[i], i);
        }
        HashMap<String, Integer> hTableNext = new HashMap<>();
        for (int i = 0; i < list2.length; i++) {
            hTableNext.put(list2[i], i);
        }
        HashMap<String, Integer> hTableBoth = new HashMap<>();
        for (Map.Entry<String, Integer> next : hTable.entrySet()) {
            if (hTableNext.containsKey(next.getKey())) {
                hTableBoth.put(next.getKey(), next.getValue() + hTableNext.get(next.getKey()));
            }
        }
        ArrayList<Map.Entry<String, Integer>> sorted = new ArrayList<>(hTableBoth.entrySet());
        sorted.sort(Comparator.comparing(Map.Entry::getValue));
        Iterator<Map.Entry<String, Integer>> iterator = sorted.iterator();
        int minSum = Integer.MAX_VALUE;
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> next = iterator.next();
            Integer sum = next.getValue();
            if (sum <= minSum) {
                ans.add(next.getKey());
                minSum = sum;
            } else {
                break;
            }
        }
        return ans.toArray(new String[]{});
    }


    public String[] findRestaurant01(String[] list1, String[] list2) {
        ArrayList<String> ans = new ArrayList<>();
        HashMap<String, Integer> hTable = new HashMap<>();
        for (int i = 0; i < list1.length; i++) {
            hTable.put(list1[i], i);
        }
        int minSum = Integer.MAX_VALUE;
        for (int i = 0; i < list2.length; i++) {
            if (hTable.containsKey(list2[i])) {
                if (i + hTable.get(list2[i]) < minSum) {
                    ans.clear();
                    ans.add(list2[i]);
                    minSum = i + hTable.get(list2[i]);
                } else if (i + hTable.get(list2[i]) == minSum) {
                    ans.add(list2[i]);
                }
            }
        }
        return ans.toArray(new String[]{});
    }


    /**
     * 451. 根据字符出现频率排序
     */
    public String frequencySort(String s) {
        StringBuilder ans = new StringBuilder();
        HashMap<Object, Integer> hTable = new HashMap<>();
        HashMap<Object, StringBuilder> hTableAndBuild = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            hTable.put(s.charAt(i), hTable.getOrDefault(s.charAt(i), 0) + 1);
            if (hTableAndBuild.containsKey(s.charAt(i)))
                hTableAndBuild.put(s.charAt(i), hTableAndBuild.get(s.charAt(i)).append(s.charAt(i)));
            else
                hTableAndBuild.put(s.charAt(i), new StringBuilder().append(s.charAt(i)));
        }
        ArrayList<Map.Entry<Object, Integer>> sorted = new ArrayList<>(hTable.entrySet());
        sorted.sort(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
        Iterator<Map.Entry<Object, Integer>> iterator = sorted.iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next().getKey();
            ans.append(hTableAndBuild.get(key));
        }
        return ans.toString();
    }

    //优先队列


    /**
     * 890. 查找和替换模式
     */
    public List<String> findAndReplacePattern(String[] words, String pattern) {
        ArrayList<String> ans = new ArrayList<>();
        for (String word : words) {
            if (verifyMatch(word, pattern))
                ans.add(word);
        }
        return ans;
    }

    private boolean verifyMatch(String word, String pattern) {
        //记录字符的唯一映射关系
        HashMap<Character, Character> hTable = new HashMap<>();   //记录 word 中每个字符 与 pattern中每个字符的映射关系
        HashMap<Character, Character> reverse = new HashMap<>();  //记录 pattern中每个字符 与 word中每个字符的映射关系
        //用两个 HashMap其实是为了快速判断是否存在 value
        for (int i = 0; i < word.length(); i++) {
            char wc = word.charAt(i);
            char pc = pattern.charAt(i);
            if (!hTable.containsKey(wc) && !reverse.containsKey(pc)) {
                hTable.put(wc, pc);
                reverse.put(pc, wc);
            } else if ((hTable.containsKey(wc) && hTable.get(wc) != pc) || (reverse.containsKey(pc) && reverse.get(pc) != wc)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 1436. 旅行终点站
     */
    public String destCity(List<List<String>> paths) {
        HashMap<String, Integer> cityAndTimes = new HashMap<>();
        HashSet<String> targetCity = new HashSet<>();
        for (List<String> route : paths) {
            int k = 0;
            for (String city : route) {
                k++;
                cityAndTimes.put(city, cityAndTimes.getOrDefault(city, 0) + 1);
                if (k == 2)
                    targetCity.add(city);
            }
        }
        for (Map.Entry<String, Integer> cityAndTime : cityAndTimes.entrySet()) {
            if ((cityAndTime.getValue() & 1) == 1 && targetCity.contains(cityAndTime.getKey()))
                return cityAndTime.getKey();
        }
        return "";
    }

    public String destCity01(List<List<String>> paths) {
        HashSet<String> cityA = new HashSet<>();
        for (List<String> path : paths) {
            cityA.add(path.get(0));    //记录出发地
        }
        for (List<String> path : paths) {
            if (!cityA.contains(path.get(1)))  //检查出发地是否有对应的目的地
                return path.get(1);
        }
        return "";
    }

    public String destCity02(List<List<String>> paths) {
        HashSet<String> cityB = new HashSet<>();
        HashSet<String> cityA = new HashSet<>();
        for (List<String> path : paths) {
            cityA.add(path.get(0));
            cityB.add(path.get(1));       //记录目的地
        }
        Iterator<String> iterator = cityB.iterator();
        while (iterator.hasNext()) {
            String cityTarget = iterator.next();
            if (!cityA.contains(cityTarget))
                return cityTarget;
        }
        return "";
    }


    /**
     * 692. 前K个高频单词
     */
    public List<String> topKFrequent09(String[] words, int k) {
        HashMap<String, Integer> hTable = new HashMap<>();
        for (String word : words) {
            hTable.put(word, hTable.getOrDefault(word, 0) + 1);
        }
        ArrayList<Map.Entry<String, Integer>> sorted = new ArrayList<>(hTable.entrySet());
        sorted.sort((o1, o2) -> {
            if (!o1.getValue().equals(o2.getValue()))
                return o2.getValue() - o1.getValue();
            else
                return o1.getKey().compareTo(o2.getKey());    //字典序由小到大
        });
        List<String> target = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            target.add(sorted.get(i).getKey());
        }
        return target;
    }

    public List<String> topKFrequent10(String[] words, int k) {
        ArrayList<String> ans = new ArrayList<>();
        HashMap<String, Integer> hTable = new HashMap<>();
        for (String word : words) {
            hTable.put(word, hTable.getOrDefault(word, 0) + 1);
        }
        PriorityQueue<Map.Entry<String, Integer>> sortedQueue = new PriorityQueue<>((o1, o2) -> {
            if (!o1.getValue().equals(o2.getValue()))
                return o1.getValue() - o2.getValue();   //升序，小根队，方便剔除堆顶元素
            else
                return o2.getKey().compareTo(o1.getKey());
        });
        for (Map.Entry<String, Integer> map : hTable.entrySet()) {
            sortedQueue.add(map);
            if (sortedQueue.size() > k)
                sortedQueue.poll();   //小根队，方便剔除堆顶元素
        }
        while (!sortedQueue.isEmpty()) {
            ans.add(sortedQueue.poll().getKey());
        }

        Collections.reverse(ans);

        return ans;
    }


    /**
     * 1418. 点菜展示表
     */
    public List<List<String>> displayTable(List<List<String>> orders) {
        List<List<String>> ans = new ArrayList<>();
        ArrayList<String> menuLists = new ArrayList<>();

        HashSet<String> distinct = new HashSet<>();
        for (List<String> order : orders) {
            for (int i = 2; i < order.size(); i++) {
                if (!distinct.contains(order.get(i))) {
                    menuLists.add(order.get(i));    //获取AscII序得菜单名称
                    distinct.add(order.get(i));
                }
            }
        }
        List<String> Items = new ArrayList<>();
        Items.add("Table");
        Collections.sort(menuLists);
        Items.addAll(menuLists);
        ans.add(Items); //第一行为 按照 ASCII排序的菜单

        //基于桌号 统计其对应的 菜单
        HashMap<Integer, HashMap<String, Integer>> tableAndMenus = new HashMap<>();
        for (List<String> order : orders) {
            //桌号
            int tableID = Integer.parseInt(order.get(1));
            //桌号对应的菜单列表
            HashMap<String, Integer> menus = tableAndMenus.getOrDefault(tableID, new HashMap<String, Integer>());
            String item = order.get(2);  //菜品
            menus.put(item, menus.getOrDefault(item, 0) + 1);
            tableAndMenus.put(tableID, menus);
        }
        ArrayList<Map.Entry<Integer, HashMap<String, Integer>>> sorted = new ArrayList<>(tableAndMenus.entrySet());
        sorted.sort((o1, o2) -> o1.getKey() - o2.getKey());
        Iterator<Map.Entry<Integer, HashMap<String, Integer>>> iterator = sorted.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, HashMap<String, Integer>> next = iterator.next();
            Integer tableID = next.getKey();
            HashMap<String, Integer> menus = next.getValue();
            List<String> sortedmenus = new ArrayList<>();
            sortedmenus.add(String.valueOf(tableID));
            for (String me : menuLists) {
                sortedmenus.add(String.valueOf(menus.getOrDefault(me, 0)));
            }
            ans.add(sortedmenus);
        }
        return ans;
    }


    /**
     * 1995. 统计特殊四元组
     */
    public int countQuadruplets(int[] nums) {
        int ans = 0;
        for (int i = 0; i < nums.length - 3; i++) {
            for (int j = i + 1; j < nums.length - 2; j++) {
                for (int x = j + 1; x < nums.length - 1; x++) {
                    for (int y = x + 1; y < nums.length; y++) {
                        if (nums[i] + nums[j] + nums[x] == nums[y]) ans++;
                    }
                }
            }
        }
        return ans;
    }

    public int countQuadruplets01(int[] nums) {
        int ans = 0;
        int len = nums.length;
        HashMap<Integer, Integer> numAndTimes = new HashMap<>();
        for (int c = len - 2; c >= 2; c--) {
            //基于 HashMap记录 d 可以取那些值，以及其出现的频次
            numAndTimes.put(nums[c + 1], numAndTimes.getOrDefault(nums[c + 1], 0) + 1);
            for (int a = 0; a < len; a++) {
                for (int b = a + 1; b < c; b++) {
                    ans += numAndTimes.getOrDefault(nums[a] + nums[b] + nums[c], 0);
                }
            }
        }
        return ans;
    }


    /**
     * 888. 公平的糖果交换
     */
    public int[] fairCandySwap(int[] aliceSizes, int[] bobSizes) {
        int[] ans = new int[2];
        int sumAll = 0;
        int asum = 0;
        int bsum = 0;
        int alen = aliceSizes.length;
        int blen = bobSizes.length;
        HashMap<Integer, Integer> ahTable = new HashMap<>();
        HashMap<Integer, Integer> bhTable = new HashMap<>();
        for (int i = 0; i < alen; i++) {
            sumAll += aliceSizes[i];
            asum += aliceSizes[i];
            ahTable.put(aliceSizes[i], ahTable.getOrDefault(aliceSizes[i], 0) + 1);
        }
        for (int i = 0; i < blen; i++) {
            sumAll += bobSizes[i];
            bsum += bobSizes[i];
            bhTable.put(bobSizes[i], bhTable.getOrDefault(bobSizes[i], 0) + 1);
        }
        int average = sumAll / 2;
        for (int aKey : ahTable.keySet()) {
            if (bhTable.containsKey(average - (asum - aKey))) {
                return new int[]{aKey, average - (asum - aKey)};
            }
        }
        return new int[]{-1, -1};
    }


    public int[] fairCandySwap01(int[] aliceSizes, int[] bobSizes) {
        int[] ans = new int[2];
        int asum = Arrays.stream(aliceSizes).sum();
        int bsum = Arrays.stream(bobSizes).sum();
        int average = (asum + bsum) / 2;
        HashSet<Integer> hTable = new HashSet<>();
        for (int i = 0; i < aliceSizes.length; i++) {
            hTable.add(aliceSizes[i]);
        }
        for (int i = 0; i < bobSizes.length; i++) {
            if (hTable.contains(average - (bsum - bobSizes[i]))) {
                ans[0] = average - (bsum - bobSizes[i]);
                ans[1] = bobSizes[i];
                break;
            }
        }
        return ans;
    }


}
