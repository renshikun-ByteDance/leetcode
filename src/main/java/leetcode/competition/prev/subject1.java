package leetcode.competition.prev;


import java.util.Scanner;

/**
 * 信用卡分期还款金额计算
 */
public class subject1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] split = line.split(",");
        double money = Double.parseDouble(split[0]);
        int nums = Integer.parseInt(split[1]);
        double fate = Double.parseDouble(split[2]) / 100;

        System.out.println(String.format("%.2f", money / nums + money * 3 * fate / 3));
    }
}
