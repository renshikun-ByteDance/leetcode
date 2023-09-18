package leetcode.competition.prev;


import java.util.*;

public class subject4 {

    private static int minNums = Integer.MAX_VALUE;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String mainInfo = scanner.nextLine();
        String[] split = mainInfo.split("\\s+");
        int n = Integer.parseInt(split[0]);  //接口个数
        int m = Integer.parseInt(split[1]);  //外围系统个数
        int k = Integer.parseInt(split[2]);  //最多影响的外围系统的个数
        HashMap<Integer, ArrayList<String>> hTable = new HashMap<>();  //记录各个接口对应的字段
        HashMap<Integer, HashSet<String>> systemAndField = new HashMap<>();  //记录各个接口对应的字段
        HashSet<String> allFields = new HashSet<>();  //记录所有的字段
        //初始化
        for (int i = 1; i <= Math.max(n, m); i++) {
            if (i <= n) {
                hTable.put(i, new ArrayList<>());
            }
            if (i <= m) {
                systemAndField.put(i, new HashSet<>());
            }
        }
        int nums = 1;
        //初始化接口和字段的对应关系
        while (scanner.hasNext() && nums <= n) {
            String nextLine = scanner.nextLine();
            String[] currentInfo = nextLine.split("\\s+");
            int interfaceID = Integer.parseInt(currentInfo[0]);
            for (int i = 1; i < currentInfo.length; i++) {
                hTable.get(interfaceID).add(currentInfo[i]);
                allFields.add(currentInfo[i]);
            }
            nums++;
        }
        nums = 1;
        //初始化外围系统和字段的对应关系
        while (scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            String[] currentInfo = nextLine.split("\\s+");
            String systemID = currentInfo[0];
            for (int i = 1; i < currentInfo.length; i++) {
                ArrayList<String> fields = hTable.getOrDefault(Integer.parseInt(currentInfo[i]), new ArrayList<>());
                for (String field : fields) {
                    systemAndField.getOrDefault(Integer.parseInt(systemID), new HashSet<>()).add(field);
                }
            }
            if (++nums > m) break;
        }
        //---------------------------------------------------------------------
        // 以下为深度优先搜索 DFS 算法中典型的求组合的问题，组合内元素个数为 m - k
        //---------------------------------------------------------------------
        int[] system = new int[m + 1];
        for (int i = 1; i <= m; i++) {
            system[i] = i;
        }
        LinkedList<Integer> path = new LinkedList<>(); //搜索的路径及组合
        //开始深度优先搜索
        combineDfs(system, systemAndField, m - k, path, 1); //找到 m -k 个系统，使其对应的字段数最少
        //因此，存在其余 k 个系统，对应的字段数最多（符合题意）
        System.out.println(allFields.size() - minNums);
    }

    private static void combineDfs(int[] system, HashMap<Integer, HashSet<String>> systemAndField, int targetNums, LinkedList<Integer> path, int currentIndex) {
        //递归终止条件一：完成全部搜索
        if (currentIndex > system.length || path.size() > targetNums) {
            return;
        }

        //递归终止条件二：找到一个目标组合
        if (path.size() == targetNums) {
            HashSet<String> infectFields = new HashSet<>();
            for (int systemID : path) {
                HashSet<String> fields = systemAndField.get(systemID);
                infectFields.addAll(fields);
            }
            minNums = Math.min(minNums, infectFields.size());
            return;
        }

        //横向枚举搜索
        for (int i = currentIndex; i < system.length; i++) {
            //1、增加元素
            path.add(i);

            //2、深度递归
            combineDfs(system, systemAndField, targetNums, path, i + 1);

            //3、移除元素
            path.removeLast();
        }
    }
}
