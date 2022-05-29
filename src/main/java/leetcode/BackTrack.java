package leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回溯算法
 */
public class BackTrack {


    /**
     * 17. 电话号码的字母组合
     */
    public List<String> letterCombinations(String digits) {
        ArrayList<String> combinations = new ArrayList<>();
        if (digits.length() == 0)
            return combinations;
        HashMap<Character, String> hTable = new HashMap<>();
        hTable.put('2', "abc");
        hTable.put('3', "def");
        hTable.put('4', "ghi");
        hTable.put('5', "jkl");
        hTable.put('6', "mno");
        hTable.put('7', "pqrs");
        hTable.put('8', "tuv");
        hTable.put('9', "wxyz");
        //回溯（递归）
        backtrack(digits, hTable, 0, new StringBuilder(), combinations);

        return combinations;
    }

    private void backtrack(String digits, HashMap<Character, String> hTable, int index, StringBuilder builder, ArrayList<String> combinations) {
        //递归到叶子节点，则将拼接的结果写入到集合中
        if (index == digits.length()) {   //index为树的高度，即 digits的长度
            combinations.add(builder.toString());
        } else {
            //获取当前位置的数字
            char digit = digits.charAt(index);
            //获取数据对应的字符组合
            String digitToMap = hTable.get(digit);
            for (int i = 0; i < digitToMap.length(); i++) {  //遍历此 digit 对应的所有 字母
                builder.append(digitToMap.charAt(i));        //第index位的 digit 对应的 字母（其中一种情况）
                backtrack(digits, hTable, index + 1, builder, combinations); //递归处理 在这种组合（叶节点）的 下一位
                builder.deleteCharAt(index);  //build的每一位都是树结构的一层，某个层有多个并行节点，均应该占据build的同一位置
                // 可以想象为数状结构，从上而下对build的复用程度逐步发散，对应同一层的节点，先添加某个节点，再删除替换为另外一个节点
            }
        }
    }


}
