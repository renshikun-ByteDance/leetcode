import java.util.*;

public class BitOperation {


    /**
     * @param board
     * @return
     */
    public boolean isValidSudoku000(char[][] board) {
        int[][] rows = new int[9][9];
        int[][] columns = new int[9][9];
        int[][][] areas = new int[3][3][9];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] != '.') {
                    int charTonum = board[i][j] - '0' - 1;
                    rows[i][charTonum]++;
                    columns[j][charTonum]++;
                    areas[i / 3][j / 3][charTonum]++;
                    if (rows[i][charTonum] > 1 || columns[j][charTonum] > 1 || areas[i / 3][j / 3][charTonum] > 1)
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * 36. 有效的数独
     * <p>
     * <p>
     * 二维数组和一维数组+位运算区别在于：
     * 二维数组：
     * 索引##一维：坐标
     * 索引##二维：目标值
     * 索引##值：目标值出现的次数
     * <p>
     * 一维数组+位运算：
     * 索引##一维：坐标
     * 索引##值：int型整数，其对应的二进制的每一位0-9位（低位）：标识目标值[0-9],0-9位（低位）的值（0/1）表示出现过的次数（超过1不表示）
     *
     * @param board
     * @return
     */


    public boolean isValidSudoku(char[][] board) {
        int[] row = new int[10], col = new int[10], area = new int[10];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                char c = board[i][j];
                if (c == '.') continue;
                int charTonum = c - '0';
                int idx = i / 3 * 3 + j / 3;
                if (((row[i] >> charTonum) & 1) == 1 || ((col[j] >> charTonum) & 1) == 1 || ((area[idx] >> charTonum) & 1) == 1)
                    return false; //一定要有 &1,这个相当于是和..00000001做的与运算，只看这一位，否则会有其他位置的干扰
                row[i] |= (1 << charTonum);
                col[j] |= (1 << charTonum);
                area[idx] |= (1 << charTonum);
            }
        }
        return true;
    }


    /**
     * 187. 重复的DNA序列
     *
     * @param s
     * @return
     */

    public List<String> findRepeatedDnaSequences(String s) {
        int windowLength = 10;
        ArrayList<String> seqRepeat = new ArrayList<>();
        if (s.length() < windowLength) {
            return seqRepeat;
        }
        HashMap<Character, Integer> chatTonum = new HashMap<>();
        chatTonum.put('A', 0);//00
        chatTonum.put('C', 1);//01
        chatTonum.put('G', 2);//10
        chatTonum.put('T', 3);//11
        int windowNum = 0;
        for (int i = 0; i < windowLength - 1; ++i) {
            windowNum = (windowNum << 2) | chatTonum.get(s.charAt(i));
        }
        HashMap<Integer, Integer> seqAndnum = new HashMap<>();
        for (int left = 0; left <= s.length() - windowLength; ++left) {
            windowNum = ((windowNum << 2) | chatTonum.get(s.charAt(left + windowLength - 1))) & ((1 << windowLength * 2) - 1);//位运算的优先级
            seqAndnum.put(windowNum, seqAndnum.getOrDefault(windowNum, 0) + 1);
            if (seqAndnum.get(windowNum) == 2)
                seqRepeat.add(s.substring(left, left + windowLength));
        }
        return seqRepeat;
    }


    /**
     * 自定义模板
     *
     * @param s
     * @return
     */
    public List<String> findRepeatedDnaSequences05(String s) {
        int windowLength = 10;
        ArrayList<String> seqRepeat = new ArrayList<>();
        if (s.length() < windowLength) {
            return seqRepeat;
        }
        HashMap<Character, Integer> chatTonum = new HashMap<>();
        chatTonum.put('A', 0);//00
        chatTonum.put('C', 1);//01
        chatTonum.put('G', 2);//10
        chatTonum.put('T', 3);//11
        int windowNum = 0;
        HashMap<Integer, Integer> seqAndnum = new HashMap<>();
        int left = 0;
        int right = 0;
        while (right < s.length()) {
            windowNum = (windowNum << 2) | chatTonum.get(s.charAt(right));
            while (right - left + 1 > windowLength) {
                windowNum = windowNum & ((1 << windowLength * 2) - 1);
                left++;
            }
            if (right - left + 1 == windowLength) {
                seqAndnum.put(windowNum, seqAndnum.getOrDefault(windowNum, 0) + 1);
                if (seqAndnum.get(windowNum) == 2)
                    seqRepeat.add(s.substring(left, left + windowLength));
            }
            right++;
        }
        return seqRepeat;
    }


    public List<String> findRepeatedDnaSequences00(String s) {
        int L = 10;
        Map<Character, Integer> bin = new HashMap<Character, Integer>() {{
            put('A', 0);
            put('C', 1);
            put('G', 2);
            put('T', 3);
        }};
        List<String> ans = new ArrayList<String>();
        int n = s.length();
        if (n <= L) {
            return ans;
        }
        int x = 0;
        for (int i = 0; i < L - 1; ++i) {
            x = (x << 2) | bin.get(s.charAt(i));
        }
        Map<Integer, Integer> cnt = new HashMap<Integer, Integer>();
        for (int i = 0; i <= n - L; ++i) {
            x = ((x << 2) | bin.get(s.charAt(i + L - 1))) & ((1 << (L * 2)) - 1);
            cnt.put(x, cnt.getOrDefault(x, 0) + 1);
            if (cnt.get(x) == 2) {
                ans.add(s.substring(i, i + L));
            }
        }
        return ans;
    }


    public List<String> findRepeatedDnaSequences01(String s) {
        int L = 10;
        List<String> ans = new ArrayList<String>();
        HashMap<String, Integer> seqAndNum = new HashMap<>();
        for (int i = 0; i <= s.length() - L; i++) {
            String temp = s.substring(i, i + L);   //substring不存在数组越界的问题
            seqAndNum.put(temp, seqAndNum.getOrDefault(temp, 0) + 1);
            if (seqAndNum.get(temp) == 2)
                ans.add(temp);
        }
        return ans;
    }


    public List<String> findRepeatedDnaSequences02(String s) {
        int windowLength = 10;
        ArrayList<String> seqRepeat = new ArrayList<>();
        if (s.length() <= 10)
            return seqRepeat;
        HashMap<Character, Integer> charTonum = new HashMap<>();
        charTonum.put('A', 1);//001
        charTonum.put('C', 2);//020
        charTonum.put('G', 3);//011
        charTonum.put('T', 4);//100
        Integer oneWindow = 0;
        HashMap<Integer, Integer> seqAndTimes = new HashMap<>();
//        for (int i = 0; i < windowLength - 1; i++) {
//            oneWindow = (oneWindow << 3) | charTonum.get(s.charAt(i));
//        }
//        for (int i = 0; i <= s.length() - windowLength; i++) {
//            oneWindow = ((oneWindow << 3) | charTonum.get(s.charAt(i + windowLength - 1))) & ((1 << windowLength * 3) - 1);
//            seqAndTimes.put(oneWindow, seqAndTimes.getOrDefault(oneWindow, 0) + 1);
//            if (seqAndTimes.get(oneWindow) == 2)
//                seqRepeat.add(s.substring(i, i + windowLength));  //substring不会有数组越界
//        }
        for (int i = 0; i < windowLength; i++) {
            oneWindow = (oneWindow << 3) | charTonum.get(s.charAt(i));
        }
        seqAndTimes.put(oneWindow, 1);
        for (int i = windowLength; i < s.length(); i++) {
            oneWindow = ((oneWindow << 3) | charTonum.get(s.charAt(i))) & ((1 << windowLength * 3) - 1);
            seqAndTimes.put(oneWindow, seqAndTimes.getOrDefault(oneWindow, 0) + 1);
            if (seqAndTimes.get(oneWindow) == 2)
                if (i == s.length() - 1)
                    seqRepeat.add(s.substring(i - windowLength + 1));
                else
                    seqRepeat.add(s.substring(i - windowLength + 1, i + 1));
        }
        return seqRepeat;
    }


}
