package leetcode.homework;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.Callable;

public class Working {

    /**
     * 838. 推多米诺
     */
    public String pushDominoes(String dominoes) {
        String prev = "";
        while (!dominoes.equals(prev)) {
            prev = dominoes;
            dominoes = dominoes.replaceAll("R\\.L", "T");
            dominoes = dominoes.replaceAll("\\.L", "LL");
            dominoes = dominoes.replaceAll("R\\.", "RR");
            dominoes = dominoes.replaceAll("T", "R\\.L");
        }
        return dominoes;
    }


    /**
     * 1488. 避免洪水泛滥
     */
    public int[] avoidFlood(int[] rains) {
        int n = rains.length;
        return new int[]{};
    }


    public String removeTrailingZeros(String num) {
        while (num.endsWith("0")) {
            num = num.substring(0, num.length() - 1);
        }
        return num;
    }


    public int[][] differenceOfDistinctValues(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] ans = new int[m][n];
        int[][] dirs = {{-1, -1}, {1, 1}};
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int row = i;
                int col = j;
                HashSet<Integer> set = new HashSet<>();
                while (true) {
                    int nextRow = row + 1;
                    int nextCol = col + 1;
                    if (nextRow < 0 || nextRow >= m || nextCol < 0 || nextCol >= n) {
                        break;
                    }
                    set.add(grid[nextRow][nextCol]);
                    row = nextRow;
                    col = nextCol;
                }
                int num1 = set.size();
                set.clear();

                row = i;
                col = j;
                while (true) {
                    int nextRow = row - 1;
                    int nextCol = col - 1;
                    if (nextRow < 0 || nextRow >= m || nextCol < 0 || nextCol >= n) {
                        break;
                    }
                    set.add(grid[nextRow][nextCol]);
                    row = nextRow;
                    col = nextCol;
                }
                int num2 = set.size();

                ans[i][j] = Math.abs(num1 - num2);
            }
        }
        return ans;
    }


    /**
     * 1093. 大样本统计
     */
    public double[] sampleStats(int[] count) {
        int min = -1;
        int max = -1;
        long sum = 0;
        int num = 0;
        int maxFreqNum = 0;
        int freq = 0;
        for (int i = 0; i <= 255; i++) {
            if (count[i] == 0) continue;
            max = i;
            if (min == -1) min = i;
            num += count[i];
            sum += (long) count[i] * i;
            if (count[i] > freq) {
                freq = count[i];
                maxFreqNum = i;
            }
        }
        double avg = sum * 1.0 / num;
        //------------------------------------------------------
        // 本题的关键在于求解中位数，而且如果一共偶数个数字，还要求平均数，不能用传统的排序方式，会超时
        //------------------------------------------------------
        int xx = 0;
        int yy = 255;
        for (int step = (num - 1) >> 1; step >= count[xx]; step -= count[xx++]) ;
        for (int step = (num - 1) >> 1; step >= count[yy]; step -= count[yy--]) ;
        double mid = (xx + yy) / 2.0;
        return new double[]{min, max, avg, mid, maxFreqNum};
    }


    /**
     * 851. 喧闹和富有
     */
    public int[] loudAndRich(int[][] richer, int[] quiet) {
        int n = quiet.length;
        int[] ans = new int[n];
        int[] visited = new int[n];
        int[] inDegree = new int[n];
        ArrayList<HashSet<Integer>> adjacent = new ArrayList<>();
        ArrayList<HashSet<Integer>> adjacentDepth = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjacent.add(new HashSet<>());
            adjacentDepth.add(new HashSet<>());
        }
        for (int[] edge : richer) {
            int node1 = edge[0];
            int node2 = edge[1];
            inDegree[node1]++;
            adjacent.get(node2).add(node1);
        }
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                loudAndRichDFS(adjacentDepth, adjacent, visited, i);
            }
        }
        for (int i = 0; i < n; i++) {
            int targetNode = i;
            HashSet<Integer> nodes = adjacentDepth.get(i);
            for (int currNode : nodes) {
                if (quiet[currNode] < quiet[targetNode]) {
                    targetNode = currNode;
                }
            }
            ans[i] = targetNode;
        }
        return ans;
    }

    private void loudAndRichDFS(ArrayList<HashSet<Integer>> adjacentDepth, ArrayList<HashSet<Integer>> adjacent, int[] visited, int currNode) {
        //1、递归终止条件
        if (adjacent.get(currNode).size() == 0) {
            return;
        }

        visited[currNode] = 1;
        HashSet<Integer> affectNodes = adjacent.get(currNode);
        for (int nextNode : affectNodes) {
            if (visited[nextNode] == 0) {
                loudAndRichDFS(adjacentDepth, adjacent, visited, nextNode);
            }
            adjacentDepth.get(currNode).add(nextNode);
            adjacentDepth.get(currNode).addAll(adjacentDepth.get(nextNode));
        }
    }


    /**
     * 228. 汇总区间
     */
    public List<String> summaryRanges(int[] nums) {
        int n = nums.length;
        List<String> ans = new ArrayList<>();
        int currIndex = 0;
        while (currIndex < n) {
            int prevIndex = currIndex;
            currIndex++;
            while (currIndex < n && nums[currIndex] == nums[currIndex - 1] + 1) {
                currIndex++;
            }
            int nextIndex = currIndex - 1;
            if (prevIndex == nextIndex) {
                ans.add(String.valueOf(nums[prevIndex]));
            } else {
                ans.add(nums[prevIndex] + "->" + nums[nextIndex]);
            }
        }
        return ans;
    }


    /**
     * 1268. 搜索推荐系统
     */
    public List<List<String>> suggestedProducts(String[] products, String searchWord) {
        Arrays.sort(products, String::compareTo);
        List<List<String>> ans = new ArrayList<>();
        for (int i = 0; i < searchWord.length(); i++) {
            String start = searchWord.substring(0, i + 1);
            List<String> curr = new ArrayList<>();
            for (String product : products) {
                if (product.startsWith(start)) {
                    curr.add(product);
                }
                if (curr.size() == 3) break;
            }
            ans.add(curr);
        }
        return ans;
    }


    /**
     * 57. 插入区间
     */
    public int[][] insert(int[][] intervals, int[] newInterval) {
        int m = intervals.length;
        int[][] grids = new int[m + 1][2];
        System.arraycopy(intervals, 0, grids, 0, m);
        grids[m] = newInterval;
        Arrays.sort(grids, (o1, o2) -> o1[0] - o2[0]);
        ArrayDeque<int[]> queue = new ArrayDeque<>();
        for (int i = 0; i <= m; i++) {
            if (queue.isEmpty() || queue.peekLast()[1] < grids[i][0]) {
                queue.add(grids[i]);
            } else {
                int[] prev = queue.pollLast();
                prev[1] = Math.max(prev[1], grids[i][1]);
                queue.add(prev);
            }
        }
        ArrayList<int[]> ans = new ArrayList<>();
        while (!queue.isEmpty()) {
            ans.add(queue.pollFirst());
        }
        return ans.toArray(new int[ans.size()][]);
    }


    /**
     * 2511. 最多可以摧毁的敌人城堡数目
     */
    public int captureForts(int[] forts) {
        //-------------------------------
        // 题意的本质为求解 -1 和 1 之间最多的 0 的个数
        //-------------------------------
        int maxNums = 0;
        int prevIndex = -1;
        int n = forts.length;
        for (int i = 0; i < n; i++) {
            if (forts[i] == 1 || forts[i] == -1) {
                if (prevIndex >= 0 && forts[i] * forts[prevIndex] == -1) {
                    maxNums = Math.max(maxNums, i - prevIndex - 1);
                }
                prevIndex = i;
            }
        }
        return maxNums;
    }


    /**
     * 551. 学生出勤记录 I
     */
    public boolean checkRecord(String str) {
        int numA = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == 'A') numA++;
            if (numA == 2) return false;
        }
        return !str.contains("LLL");
    }

    /**
     * 771. 宝石与石头
     */
    public int numJewelsInStones(String jewels, String stones) {
        int ans = 0;
        for (int i = 0; i < stones.length(); i++) {
            if (jewels.contains(String.valueOf(stones.charAt(i)))) ans++;
        }
        return ans;
    }





}
