package leetcode.competition.current;


import java.util.ArrayList;
import java.util.Scanner;

/**
 * 2022年中国民生银行信用卡中心"第五届 1024编程大赛"
 */
public class hot {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String nextLine = scanner.next();
        ArrayList<Integer> ans = new ArrayList<>();
        for (int num = 100; num <= 999; num++) {
            String str = String.valueOf(num);
            String[] split = str.split("");
            int aa = Integer.parseInt(split[0]);
            int bb = Integer.parseInt(split[1]);
            int cc = Integer.parseInt(split[2]);
            if (Math.pow(aa, 3) + Math.pow(bb, 3) + Math.pow(cc, 3) == num) {
                ans.add(num);
            }
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ans.size(); i++) {
            builder.append(ans.get(i)).append(",");
        }
        String target = builder.toString();
        System.out.println(target.substring(0, target.length() - 1));
    }
}
