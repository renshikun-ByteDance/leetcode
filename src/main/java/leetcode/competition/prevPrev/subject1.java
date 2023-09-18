package leetcode.competition.prevPrev;

import java.util.ArrayList;
import java.util.Scanner;

public class subject1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int day = Integer.parseInt(scanner.nextLine());
        ArrayList<String> scan = new ArrayList<>();
        String digit = "";
        while (scanner.hasNext()) {
            String currentLine = scanner.nextLine();
            if (Character.isDigit(currentLine.charAt(0))) {  //如果是最后一行偏移量，则赋值并退出循环
                digit = currentLine;
                break;
            }
            scan.add(currentLine);
        }
        //预处理文本
        int rows = scan.size();
        int cols = scan.get(0).length();
        Character[][] sentences = new Character[rows][cols + 1];
        for (int i = 0; i < rows; i++) {
            for (int j = 1; j <= cols; j++) {
                sentences[i][j] = scan.get(i).charAt(j - 1);  //将星期与索引相关联
            }
        }
        int[] move = new int[digit.length()];
        //预处理偏移量
        for (int i = 0; i < digit.length(); i++) {
            move[i] = Integer.parseInt(String.valueOf(digit.charAt(i)));
        }
        StringBuilder ans = new StringBuilder();
        int[][] targetIndex = new int[rows][2];  //记录第几个字的坐标
        int currentRow = 0;
        int currentCol = day;
        //第一个目标元素
        if (currentCol + move[0] <= cols) {
            targetIndex[0] = new int[]{0, currentCol + move[0]};
        } else {
            int moveRow = (currentCol + move[0]) / cols;
            int remain = (currentCol + move[0]) % cols;
            targetIndex[0] = new int[]{moveRow, remain};  //移动行和列
        }
        int currentIndex = 1;  //该写此索引
        //其余目标元素
        while (currentIndex < move.length) {
            //1、右下 45度移动
            while (currentCol < cols && currentIndex < move.length) {
                //1 整体位移
                int nextRow = ++currentRow;
                int nextCol = ++currentCol;
                //2 在整体位移的基础上，考虑偏移量，计算目标位点（上下分开考虑）
                if (nextCol + move[currentIndex] <= cols) {
                    targetIndex[currentIndex] = new int[]{nextRow, nextCol + move[currentIndex]};
                } else {
                    int moveRow = (nextCol + move[currentIndex]) / cols;
                    int remain = (nextCol + move[currentIndex]) % cols;
                    targetIndex[currentIndex] = new int[]{nextRow + moveRow, remain};  //移动行和列
                }
                //1 整体位移
                currentIndex++;
                //3 转换方向
                if (nextCol == cols) break;
            }

            //2、左下45度移动
            while (currentCol >= 1 && currentIndex < move.length) {
                //1 整体位移
                int nextRow = ++currentRow;
                int nextCol = --currentCol;
                //2 在整体位移的基础上，考虑偏移量，计算目标位点（上下分开考虑）
                if (nextCol + move[currentIndex] <= cols) {
                    targetIndex[currentIndex] = new int[]{nextRow, nextCol + move[currentIndex]};
                } else {
                    int moveRow = (nextCol + move[currentIndex]) / cols;
                    int remain = (nextCol + move[currentIndex]) % cols;
                    targetIndex[currentIndex] = new int[]{nextRow + moveRow, remain};  //移动行和列
                }
                //1 整体位移
                currentIndex++;
                //3 转换方向
                if (nextCol == 1) break;
            }
        }
        for (int[] index : targetIndex) {
            Character ch = sentences[index[0]][index[1]];
            if (ch != null) {
                ans.append(ch);
            }
        }
        System.out.println(ans.toString());
    }


}
